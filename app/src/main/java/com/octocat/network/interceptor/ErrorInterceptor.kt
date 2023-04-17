package com.octocat.network.interceptor

import com.google.gson.Gson
import com.octocat.network.exception.ApiException
import com.octocat.network.exception.ApiRateLimitException
import io.getstream.log.taggedLogger
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.net.HttpURLConnection
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http.promisesBody
import java.util.*
import java.util.concurrent.TimeUnit

private const val TYPE_APPLICATION = "application"
private const val SUBTYPE_JSON = "json"
private const val HEADER_RATELIMIT_REMAINING = "x-ratelimit-remaining"
private const val HEADER_RATELIMIT_RESET = "x-ratelimit-reset"

// https://docs.github.com/en/rest/overview/resources-in-the-rest-api?apiVersion=2022-11-28#client-errors
class ErrorInterceptor(
    private val gson: Gson
) : Interceptor {

    private val logger by taggedLogger("ErrorInterceptor")

    override fun intercept(chain: Interceptor.Chain): Response = try {
        chain.proceed().apply {
            if (!isSuccessful) {
                val rateLimitRemaining = header(HEADER_RATELIMIT_REMAINING)?.toIntOrNull()
                if (rateLimitRemaining == 0) {
                    val rateLimitReset = header(HEADER_RATELIMIT_RESET)?.toLongOrNull()
                    val rateLimitResetDate = rateLimitReset?.let {
                        Date(TimeUnit.SECONDS.toMillis(it))
                    }
                    throw ApiRateLimitException(rateLimitResetDate)
                }
                logger.e { "[interceptError] failedResponse: $this" }
                parseErrorIfJsonBodyExists(gson)?.let {
                    logger.e { "[interceptError] parsed: $it" }
                    throw it
                }
            }
        }
    } catch (e: Throwable) {
        logger.e { "[interceptError] failed: $e; req: ${chain.request()}" }
        throw e
    }
}

private fun Interceptor.Chain.proceed() = proceed(request())

@Throws(IOException::class)
private fun Response.parseErrorIfJsonBodyExists(gson: Gson): ApiException? = when {
    hasBody() && hasJsonBody() -> parseError(gson)
    else -> null
}

@Throws(IOException::class)
private fun Response.parseError(gson: Gson) = openReader()?.use {
    gson.fromJson(it, ApiException::class.java)
}

private fun Response.hasBody() = promisesBody()

private fun Response.hasJsonBody(): Boolean = try {
    val contentType = body?.contentType()
    contentType?.type == TYPE_APPLICATION && contentType.subtype == SUBTYPE_JSON
} catch (e: Throwable) {
    false
}

@Throws(IOException::class)
fun Response.openReader(): Reader? = body?.run {
    val buffer = source().apply { request(java.lang.Long.MAX_VALUE) }.run { buffer }
    val charset = contentType()?.run { charset(Charsets.UTF_8) } ?: Charsets.UTF_8
    return InputStreamReader(buffer.clone().inputStream(), charset)
}