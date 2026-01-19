package com.example.golden_rose_apk

import com.example.golden_rose_apk.config.Constants
import com.example.golden_rose_apk.utils.formatPrice
import com.example.golden_rose_apk.utils.getTierInfoFromUrl
import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsExtensionsTest {

    @Test
    fun `formatPrice adds thousand separators without decimals`() {
        assertEquals("12.345", 12_345.0.formatPrice())
        assertEquals("999", 999.0.formatPrice())
    }

    @Test
    fun `productoImagenEndpoint trims trailing slash`() {
        val urlWithSlash = Constants.productoImagenEndpoint("123")
        assertEquals("http://54.243.112.243:8008/api/productos/123/imagen", urlWithSlash)
    }

    @Test
    fun `getTierInfoFromUrl returns expected tier name and color`() {
        val deluxe = getTierInfoFromUrl("https://c-valorant-api.op.gg/Assets/ContentTiers/0cebb8be-46d7-c12a-d306-e9907bfc5a25.svg")
        assertEquals("Deluxe", deluxe.name)
        assertEquals(androidx.compose.ui.graphics.Color(0xFF2196F3), deluxe.color)
    }

    @Test
    fun `getTierInfoFromUrl defaults to desconocido`() {
        val unknown = getTierInfoFromUrl("https://example.com/tier")
        assertEquals("Desconocido", unknown.name)
        assertEquals(androidx.compose.ui.graphics.Color.Gray, unknown.color)
    }
}