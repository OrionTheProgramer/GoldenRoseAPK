package com.example.golden_rose_apk.model

import com.google.gson.annotations.SerializedName

data class Skin(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("price")
    val price: Double,

    // Campos privados para Type y Category
    @SerializedName("Type")
    private val _type: String? = null,

    @SerializedName("Category")
    private val _category: String? = null,

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("desc")
    val desc: String? = null,

    // Propiedades adicionales
    val hasImageData: Boolean = false,
    val imageUrl: String? = null,
    val categoryName: String? = null
) {
    // Propiedades computadas - NO generan conflictos en JVM

    // Para código que usa skin.Type (con T mayúscula)
    val Type: String?
        @JvmName("getTypeUpperCase")  // Cambia el nombre del getter
        get() = _type ?: categoryName

    // Para código que usa skin.type (con t minúscula)
    val type: String?
        @JvmName("getTypeLowerCase")
        get() = _type ?: categoryName

    // Para código que usa skin.Category (con C mayúscula)
    val Category: String?
        @JvmName("getCategoryUpperCase")
        get() = _category

    // Para código que usa skin.category (con c minúscula)
    val category: String?
        @JvmName("getCategoryLowerCase")
        get() = _category

    // Métodos de conveniencia - con nombres ÚNICOS
    @JvmName("getSkinType")
    fun getType(): String? = _type ?: categoryName

    @JvmName("getSkinCategory")
    fun getCategory(): String? = _category

    // Para compatibilidad con ID como Long/Int
    fun getIdAsLong(): Long = id.toLongOrNull() ?: -1L
    fun getIdAsInt(): Int = id.toIntOrNull() ?: -1

    // Constructor secundario para compatibilidad total
    companion object {
        // Método factory para mantener compatibilidad
        fun create(
            id: String,
            name: String,
            price: Double,
            type: String? = null,
            category: String? = null,
            image: String? = null,
            desc: String? = null,
            hasImageData: Boolean = false,
            imageUrl: String? = null,
            categoryName: String? = null
        ): Skin {
            return Skin(
                id = id,
                name = name,
                price = price,
                _type = type,
                _category = category,
                image = image,
                desc = desc,
                hasImageData = hasImageData,
                imageUrl = imageUrl,
                categoryName = categoryName
            )
        }
    }
}