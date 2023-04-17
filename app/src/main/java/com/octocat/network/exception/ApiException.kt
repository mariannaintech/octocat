package com.octocat.network.exception

import com.google.gson.annotations.SerializedName
import java.io.IOException
import java.util.*

open class ApiException(
    @SerializedName("message") override val message: String? = null,
    @SerializedName("errors") val errors: Map<String, String>? = null,
    @SerializedName("documentation_url") val documentationUrl: String? = null
) : IOException()

data class ApiRateLimitException(
    val rateLimitReset: Date?
) : ApiException()