package com.example.golden_rose_apk

import com.example.golden_rose_apk.model.ProductoApiResponse
import com.example.golden_rose_apk.model.ProductoDto
import com.example.golden_rose_apk.model.toApiResponse
import com.example.golden_rose_apk.model.toDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProductoModelsTest {

    @Test
    fun `toDto maps every field from api response`() {
        val apiResponse = ProductoApiResponse(
            id = 9,
            nombre = "Vandal",
            descripcion = "Skin icónica",
            precio = 4900.0,
            imagenUrl = "http://images/vandal.png",
            categoria = "Rifle",
            tipo = "Premium",
            codigo = "VN-1",
            stock = 3,
            rareza = "Legendaria",
            rarezaIconUrl = "http://images/rareza.png",
            activo = false
        )

        val dto = apiResponse.toDto()

        assertEquals(apiResponse.id, dto.id)
        assertEquals(apiResponse.nombre, dto.nombre)
        assertEquals(apiResponse.descripcion, dto.descripcion)
        assertEquals(apiResponse.precio, dto.precio, 0.0)
        assertEquals(apiResponse.imagenUrl, dto.imagenUrl)
        assertEquals(apiResponse.categoria, dto.categoria)
        assertEquals(apiResponse.tipo, dto.tipo)
        assertEquals(apiResponse.codigo, dto.codigo)
        assertEquals(apiResponse.stock, dto.stock)
        assertEquals(apiResponse.rareza, dto.rareza)
        assertEquals(apiResponse.rarezaIconUrl, dto.rarezaIconUrl)
        assertEquals(apiResponse.activo, dto.activo)
    }

    @Test
    fun `toApiResponse fills defaults when dto fields are null`() {
        val dto = ProductoDto(
            nombre = "Classic",
            precio = 1000.0,
            stock = null,
            activo = null,
            id = null,
            imagenUrl = null,
            categoria = null,
            tipo = null,
            codigo = null,
            descripcion = null,
            rareza = null,
            rarezaIconUrl = null
        )

        val apiResponse = dto.toApiResponse()

        assertEquals(0, apiResponse.id)
        assertEquals(dto.nombre, apiResponse.nombre)
        assertEquals(dto.precio, apiResponse.precio, 0.0)
        assertNull(apiResponse.descripcion)
        assertEquals(0, apiResponse.stock)
        assertEquals(true, apiResponse.activo)
        assertNull(apiResponse.imagenUrl)
        assertNull(apiResponse.categoria)
        assertNull(apiResponse.tipo)
        assertNull(apiResponse.codigo)
        assertNull(apiResponse.rareza)
        assertNull(apiResponse.rarezaIconUrl)
    }
}