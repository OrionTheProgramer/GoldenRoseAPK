package com.example.golden_rose_apk.config.api

import android.content.Context
import com.example.golden_rose_apk.config.AuthInterceptor
import com.example.golden_rose_apk.config.Constants
import com.example.golden_rose_apk.config.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private var retrofitAuth: Retrofit? = null
    private var retrofitProductos: Retrofit? = null

    fun getAuthClient(context: Context): Retrofit {
        if (retrofitAuth == null) {
            retrofitAuth = buildRetrofit(
                baseUrl = Constants.BASE_AUTH,
                context = context,
                useAuthInterceptor = false // login/registro no usan token
            )
        }
        return retrofitAuth!!
    }

    fun getProductosClient(context: Context): Retrofit {
        if (retrofitProductos == null) {
            retrofitProductos = buildRetrofit(
                baseUrl = Constants.BASE_PRODUCTOS,
                context = context,
                useAuthInterceptor = true
            )
        }
        return retrofitProductos!!
    }

    private fun buildRetrofit(
        baseUrl: String,
        context: Context,
        useAuthInterceptor: Boolean
    ): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(logging)

        if (useAuthInterceptor) {
            val sessionManager = SessionManager(context)
            clientBuilder.addInterceptor(AuthInterceptor(sessionManager))
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
