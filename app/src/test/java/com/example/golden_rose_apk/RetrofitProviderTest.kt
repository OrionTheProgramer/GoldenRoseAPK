package com.example.golden_rose_apk

import com.example.golden_rose_apk.repository.RetrofitProvider
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.http.GET

class RetrofitProviderTest {

    private lateinit var server: MockWebServer

    private interface TestApi {
        @GET("hello")
        fun hello(): Call<ResponseBody>
    }

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `base client sets logging and timeouts`() {
        val baseClientMethod = RetrofitProvider::class.java.getDeclaredMethod("baseClient")
        baseClientMethod.isAccessible = true

        val client = baseClientMethod.invoke(RetrofitProvider) as OkHttpClient

        val logging = client.interceptors.filterIsInstance<HttpLoggingInterceptor>().first()
        assertEquals(HttpLoggingInterceptor.Level.BODY, logging.level)
        assertEquals(12_000, client.connectTimeoutMillis.toLong())
        assertEquals(12_000, client.readTimeoutMillis.toLong())
    }

    @Test
    fun `build ensures sanitized base url`() {
        server.enqueue(MockResponse().setResponseCode(200).setBody("ok"))

        val base = server.url("/api").toString().removeSuffix("/")
        val api = RetrofitProvider.build(base, TestApi::class.java)

        val response = api.hello().execute()
        assertTrue(response.isSuccessful)

        val request = server.takeRequest()
        assertEquals("/api/hello", request.path)
    }
}