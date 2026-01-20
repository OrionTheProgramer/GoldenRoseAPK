package com.example.golden_rose_apk.utils

import androidx.compose.ui.graphics.Color
import com.example.golden_rose_apk.config.ValorantApi

// Funcion que mapea la etiqueta local a la informacion del Tier
data class TierInfo(
    val name: String,
    val color: Color,
    val iconUrl: String?
)

fun getTierInfoFromLabel(label: String): TierInfo {
    return when (label.trim().lowercase()) {
        "select" -> TierInfo(
            name = "Select",
            color = Color(0xFF4CAF50),
            iconUrl = ValorantApi.contentTierIconUrl("5a629df4-4765-0214-bd40-fbb96542941f")
        )
        "deluxe" -> TierInfo(
            name = "Deluxe",
            color = Color(0xFF2196F3),
            iconUrl = ValorantApi.contentTierIconUrl("0cebb8be-46d7-c12a-d306-e9907bfc5a25")
        )
        "premium" -> TierInfo(
            name = "Premium",
            color = Color(0xFF9C27B0),
            iconUrl = ValorantApi.contentTierIconUrl("60bca009-4182-7998-dee7-b8a2558dc369")
        )
        "exclusive" -> TierInfo(
            name = "Exclusive",
            color = Color(0xFFFF9800),
            iconUrl = ValorantApi.contentTierIconUrl("e046854e-406c-37f4-6607-19a9ba8426fc")
        )
        "ultra" -> TierInfo(
            name = "Ultra",
            color = Color(0xFFFFEB3B),
            iconUrl = ValorantApi.contentTierIconUrl("12683d76-48d7-84a3-4e09-6985794f0445")
        )
        else -> TierInfo("Desconocido", Color.Gray, null)
    }
}
