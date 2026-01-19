package com.example.golden_rose_apk.service

import com.example.golden_rose_apk.model.AddCartRequest
import com.example.golden_rose_apk.model.CartItemDto
import com.example.golden_rose_apk.model.OrderRequest
import com.example.golden_rose_apk.model.OrderResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CarritoService {
    @GET("api/carrito")
    suspend fun obtenerCarrito(): List<CartItemDto>

    @POST("api/carrito")
    suspend fun agregar(@Body req: AddCartRequest): List<CartItemDto>

    @POST("api/ordenes/checkout")
    suspend fun checkout(@Body req: OrderRequest): OrderResponse
}
