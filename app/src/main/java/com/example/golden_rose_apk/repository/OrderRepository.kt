package com.example.golden_rose_apk.repository

import com.example.golden_rose_apk.config.Constants
import com.example.golden_rose_apk.model.CreateOrderRequest
import com.example.golden_rose_apk.model.OrderResponse
import com.example.golden_rose_apk.model.PaymentRequest
import com.example.golden_rose_apk.model.PaymentResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OrderApi {
    @POST("/api/ordenes")
    suspend fun createOrder(
        @Header("Authorization") auth: String?,
        @Body request: CreateOrderRequest
    ): OrderResponse
}

interface PaymentApi {
    @POST("/api/pagos")
    suspend fun createPayment(
        @Header("Authorization") auth: String?,
        @Body request: PaymentRequest
    ): PaymentResponse
}

class OrderRepository {
    private val api: OrderApi = RetrofitProvider.build(Constants.BASE_ORDENES, OrderApi::class.java)

    suspend fun createOrder(request: CreateOrderRequest, token: String?): OrderResponse {
        val authHeader = token?.let { "Bearer $it" }
        return api.createOrder(authHeader, request)
    }
}

class PaymentRepository {
    private val api: PaymentApi = RetrofitProvider.build(Constants.BASE_PAGOS, PaymentApi::class.java)

    suspend fun createPayment(request: PaymentRequest, token: String?): PaymentResponse {
        val authHeader = token?.let { "Bearer $it" }
        return api.createPayment(authHeader, request)
    }
}
