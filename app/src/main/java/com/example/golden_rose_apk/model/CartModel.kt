package com.example.golden_rose_apk.model

data class CartItemDto(
    val id: Long? = null,
    val productoId: Long,
    val nombre: String? = null,
    val cantidad: Int,
    val precioUnitario: Double? = null
)

data class CartItem(
    val productoId: Long,
    val nombre: String,
    val imagenUrl: String? = null,
    val cantidad: Int,
    val precioUnitario: Double,
    val subtotal: Double = cantidad * precioUnitario
)

data class OrderRequest(
    val direccion: String,
    val metodoPago: String = "CARD"
)

// Modelo local para respuesta de orden (diferente nombre)
data class OrderLocalResponse(
    val id: Long? = null,
    val total: Double? = null,
    val estado: String? = null
)

data class OrderResponse(
    val id: Long?,
    val total: Double?,
    val estado: String?
)

data class OrderItem(
    val productName: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0
)

data class Order(
    val id: String = "",
    val userId: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val items: List<OrderItem> = emptyList(),
    val total: Double = 0.0
)