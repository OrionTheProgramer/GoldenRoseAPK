package com.example.golden_rose_apk.service

import com.example.golden_rose_apk.config.Constants
import com.example.golden_rose_apk.model.Skin
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface ProductApi {
    @GET("/api/productos")
    suspend fun getProducts(): List<Skin>

    @GET("/api/productos/{id}")
    suspend fun getProduct(@Path("id") id: String): Skin
}

object ProductApiService {
    private fun buildRetrofit(baseUrl: String): ProductApi {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi::class.java)
    }

    val productoApi: ProductApi by lazy { buildRetrofit(Constants.BASE_PRODUCTOS) }
    val catalogoApi: ProductApi by lazy { buildRetrofit(Constants.BASE_CATALOGO) }
}