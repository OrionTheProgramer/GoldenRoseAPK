package com.example.golden_rose_apk.service

import com.example.golden_rose_apk.model.ProductoDto
import com.example.golden_rose_apk.model.ProductoRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductosService {
    @GET("api/productos")
    suspend fun listar(): List<ProductoDto>

    @GET("api/productos/{id}")
    suspend fun obtener(@Path("id") id: Long): ProductoDto

    @POST("api/productos")
    suspend fun crear(@Body req: ProductoRequest): ProductoDto
}
