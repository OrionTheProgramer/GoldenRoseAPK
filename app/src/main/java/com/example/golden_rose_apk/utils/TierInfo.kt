package com.example.golden_rose_apk.utils

import androidx.compose.ui.graphics.Color

// Funcion que mapea la URL a la informacion del Tier
data class TierInfo(val name: String, val color: Color)

fun getTierInfoFromUrl(categoryUrl: String): TierInfo {
    return when (categoryUrl) {
        "https://c-valorant-api.op.gg/Assets/ContentTiers/12683d76-48d7-84a3-4e09-6985794f0445.svg" -> TierInfo(
            "Select",
            Color(0xFF4CAF50)
        )
        "https://c-valorant-api.op.gg/Assets/ContentTiers/0cebb8be-46d7-c12a-d306-e9907bfc5a25.svg" -> TierInfo(
            "Deluxe",
            Color(0xFF2196F3)
        )
        "https://c-valorant-api.op.gg/Assets/ContentTiers/60BCA009-4182-7998-DEE7-B8A2558DC369.svg" -> TierInfo(
            "Premium",
            Color(0xFF9C27B0)
        )
        "https://c-valorant-api.op.gg/Assets/ContentTiers/E046854E-406C-37F4-6607-19A9BA8426FC.svg" -> TierInfo(
            "Exclusive",
            Color(0xFFFF9800)
        )
        "https://c-valorant-api.op.gg/Assets/ContentTiers/411E4A55-4E59-7757-41F0-86A53F101BB5.svg" -> TierInfo(
            "Ultra",
            Color(0xFFFFEB3B)
        )
        else -> TierInfo("Desconocido", Color.Gray)
    }
}
