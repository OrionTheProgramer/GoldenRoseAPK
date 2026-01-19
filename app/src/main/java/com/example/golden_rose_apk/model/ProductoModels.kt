package com.example.golden_rose_apk.model

import com.google.gson.annotations.SerializedName

// Modelo para respuesta de API (con SerializedName)
data class ProductoApiResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("precio") val precio: Double,
    @SerializedName("imagenUrl") val imagenUrl: String? = null,
    @SerializedName("categoria") val categoria: String? = null,
    @SerializedName("tipo") val tipo: String? = null,
    @SerializedName("codigo") val codigo: String? = null,
    @SerializedName("stock") val stock: Int = 0,
    @SerializedName("rareza") val rareza: String? = null,
    @SerializedName("rarezaIconUrl") val rarezaIconUrl: String? = null,
    @SerializedName("activo") val activo: Boolean = true
)

// Modelo DTO local (sin SerializedName, para uso interno)
data class ProductoDto(
    val id: Long? = null,
    val nombre: String,
    val descripcion: String? = null,
    val precio: Double,
    val rareza: String? = null,
    val rarezaIconUrl: String? = null,
    val imagenUrl: String? = null,
    val stock: Int? = null,
    val activo: Boolean? = true,
    val categoria: String? = null,
    val tipo: String? = null,
    val codigo: String? = null
)

// Modelo para peticiones POST/PUT
data class ProductoRequest(
    val nombre: String,
    val descripcion: String? = null,
    val precio: Double,
    val stock: Int? = null,
    val rareza: String? = null,
    val rarezaIconUrl: String? = null,
    val imagenUrl: String? = null,
    val categoria: String? = null,
    val tipo: String? = null,
    val codigo: String? = null
)

// Modelo para respuestas paginadas de API
data class ProductosResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<ProductoApiResponse>,
    @SerializedName("total") val total: Int? = null,
    @SerializedName("page") val page: Int? = null,
    @SerializedName("limit") val limit: Int? = null
)

// Extensión para convertir entre modelos
fun ProductoApiResponse.toDto(): ProductoDto {
    return ProductoDto(
        id = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        rareza = this.rareza,
        rarezaIconUrl = this.rarezaIconUrl,
        imagenUrl = this.imagenUrl,
        stock = this.stock,
        activo = this.activo,
        categoria = this.categoria,
        tipo = this.tipo,
        codigo = this.codigo
    )
}

fun ProductoDto.toApiResponse(): ProductoApiResponse {
    return ProductoApiResponse(
        id = this.id ?: 0,
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        imagenUrl = this.imagenUrl,
        categoria = this.categoria,
        tipo = this.tipo,
        codigo = this.codigo,
        stock = this.stock ?: 0,
        rareza = this.rareza,
        rarezaIconUrl = this.rarezaIconUrl,
        activo = this.activo ?: true
    )
}

