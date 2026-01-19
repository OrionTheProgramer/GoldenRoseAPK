package com.example.golden_rose_apk.config

object Constants {
    // Bases por microservicio
    const val BASE_AUTH = "http://54.243.112.243:8001/"
    const val BASE_CARRITO = "http://54.243.112.243:8002/"
    const val BASE_CATALOGO = "http://54.243.112.243:8003/"
    const val BASE_INVENTARIO = "http://54.243.112.243:8004/"
    const val BASE_ORDENES = "http://54.243.112.243:8005/"
    const val BASE_PAGOS = "http://54.243.112.243:8006/"
    const val BASE_USUARIO = "http://54.243.112.243:8007/"
    const val BASE_PRODUCTOS = "http://54.243.112.243:8008/"

    fun productoImagenEndpoint(id: String) = "${BASE_PRODUCTOS.trimEnd('/')}/api/productos/$id/imagen"
}
