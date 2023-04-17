package com.octocat.network

import com.google.gson.GsonBuilder
import com.octocat.base.BaseTest
import com.octocat.coroutines.CoroutinesRule
import com.octocat.network.interceptor.ErrorInterceptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
abstract class AbstractApiTest<T> : BaseTest() {

  protected val gson by lazy { GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create() }

  @get:Rule
  val coroutinesRule = CoroutinesRule()

  lateinit var mockWebServer: MockWebServer

  @Before
  fun mockServer() {
    mockWebServer = MockWebServer()
    mockWebServer.start()
  }

  @After
  fun stopServer() {
    mockWebServer.shutdown()
  }

  fun enqueueResponse(response: MockResponse) {
    mockWebServer.enqueue(response)
  }

  fun createService(clazz: Class<T>): T {
    val okHttpClient = OkHttpClient.Builder()
      .addInterceptor(ErrorInterceptor(gson))
      .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
      })
      .build()

    return Retrofit.Builder()
      .baseUrl(mockWebServer.url("/"))
      .client(okHttpClient)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .build()
      .create(clazz)
  }
}
