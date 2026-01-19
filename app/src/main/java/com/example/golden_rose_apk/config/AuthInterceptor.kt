package com.example.golden_rose_apk.config

import okhttp3.Interceptor
import okhttp3.Response
import com.example.golden_rose_apk.config.SessionManager

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val path = original.url.encodedPath
        val isAuthEndpoint = path.contains("/api/auth/login") ||
                path.contains("/api/auth/register")

        return if (isAuthEndpoint) {
            chain.proceed(original)
        } else {
            val token = sessionManager.getToken()
            val requestBuilder = original.newBuilder()
                .header("Content-Type", "application/json")

            if (!token.isNullOrEmpty()) {
                requestBuilder.header("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        }
    }
}
