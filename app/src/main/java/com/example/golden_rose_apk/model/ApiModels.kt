package com.example.golden_rose_apk.model

import com.google.gson.annotations.SerializedName

// AUTH
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class AuthResponse(
    @SerializedName("token") val token: String? = null,
    @SerializedName("rol") val rol: String? = null,
    @SerializedName("usuarioId") val usuarioId: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("email") val email: String? = null
)

// CARRITO (API)
data class CartItemPayload(
    @SerializedName("productoId") val productoId: String,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("precioUnitario") val precioUnitario: Double
)

data class AddCartRequest(
    @SerializedName("productoId") val productoId: String,
    @SerializedName("cantidad") val cantidad: Int
)

// ORDENES (API)
data class CreateOrderRequest(
    @SerializedName("usuarioId") val usuarioId: String?,
    @SerializedName("items") val items: List<CartItemPayload>,
    @SerializedName("total") val total: Double
)

data class OrderApiResponse(
    @SerializedName("id") val id: String? = null,
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("total") val total: Double? = null
)

// PAGOS (API)
data class PaymentRequest(
    @SerializedName("ordenId") val ordenId: String,
    @SerializedName("monto") val monto: Double,
    @SerializedName("metodo") val metodo: String = "tarjeta"
)

data class PaymentResponse(
    @SerializedName("id") val id: String? = null,
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("enlacePago") val enlacePago: String? = null
)