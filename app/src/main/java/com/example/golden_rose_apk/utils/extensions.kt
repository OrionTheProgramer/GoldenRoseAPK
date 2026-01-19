package com.example.golden_rose_apk.utils

fun Double.formatPrice(): String {
    return String.format("%,.0f", this)
}

