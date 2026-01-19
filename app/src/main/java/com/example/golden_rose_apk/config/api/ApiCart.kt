package com.example.golden_rose_apk.config.api

import com.example.golden_rose_apk.config.Constants.BASE_CARRITO
import com.example.golden_rose_apk.service.CarritoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_CARRITO)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val cartApi: CarritoService by lazy {
        retrofit.create(CarritoService::class.java)
    }
}