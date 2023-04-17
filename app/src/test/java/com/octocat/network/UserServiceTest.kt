package com.octocat.network

import com.octocat.network.exception.ApiRateLimitException
import com.octocat.network.service.UserService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.Headers.Companion.toHeaders
import okhttp3.mockwebserver.MockResponse
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

private const val TEN_SECONDS = 10L

@ExperimentalCoroutinesApi
class UserServiceTest : AbstractApiTest<UserService>() {

  private lateinit var service: UserService

  @Before
  fun initService() {
    service = createService(UserService::class.java)
  }

  @Test
  fun testApiRateLimitFailure() = runTest {
    val now = System.currentTimeMillis()
    val rateLimitResetInSeconds = TimeUnit.MILLISECONDS.toSeconds(now) + TEN_SECONDS
    val expectedException = ApiRateLimitException(
      rateLimitReset = Date(TimeUnit.SECONDS.toMillis(rateLimitResetInSeconds))
    )
    enqueueResponse(
      MockResponse().apply {
        setResponseCode(403)
        headers = mapOf(
          "content-type" to "application/json; charset=utf-8",
          "content-length" to "279",
          "x-ratelimit-remaining" to "0",
          "x-ratelimit-reset" to rateLimitResetInSeconds.toString()
        ).toHeaders()
        setBody(gson.toJson(
          mapOf(
            "message" to "API rate limit exceeded for 64.180.46.184. (But here's the good news: Authenticated requests get a higher rate limit. Check out the documentation for more details.)",
            "documentation_url" to "https://docs.github.com/rest/overview/resources-in-the-rest-api#rate-limiting",
          )
        ))
      }
    )

     try {
      service.getUser(username = "username")
     } catch (e: Throwable) {
       e shouldBeInstanceOf ApiRateLimitException::class
       e.message shouldBeEqualTo expectedException.message
     }
  }

}
