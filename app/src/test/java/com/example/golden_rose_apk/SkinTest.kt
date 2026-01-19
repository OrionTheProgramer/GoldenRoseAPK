package com.example.golden_rose_apk

import com.example.golden_rose_apk.model.Skin
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SkinTest {

    @Test
    fun `create uses explicit type and category`() {
        val skin = Skin.create(
            id = "101",
            name = "Glitched",
            price = 1999.0,
            type = "Rifle",
            category = "Premium",
            categoryName = "Category Name"
        )

        assertEquals("Rifle", skin.getType())
        assertEquals("Premium", skin.getCategory())
        assertEquals("Rifle", skin.Type)
        assertEquals("Premium", skin.Category)
    }

    @Test
    fun `type falls back to category name when missing`() {
        val skin = Skin.create(
            id = "202",
            name = "Phantom",
            price = 2999.0,
            categoryName = "Sidearm"
        )

        assertEquals("Sidearm", skin.getType())
        assertEquals("Sidearm", skin.type)
        assertNull(skin.getCategory())
    }

    @Test
    fun `getId helpers handle invalid values`() {
        val skin = Skin.create(id = "abc", name = "Broken", price = 0.0)

        assertEquals(-1L, skin.getIdAsLong())
        assertEquals(-1, skin.getIdAsInt())
    }
}