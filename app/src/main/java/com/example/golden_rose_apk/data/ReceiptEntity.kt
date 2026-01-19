package com.example.golden_rose_apk.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receipts")
data class ReceiptEntity(
    @PrimaryKey val id: String,
    val buyerName: String,
    val buyerEmail: String,
    val paymentMethod: String,
    val itemsJson: String,
    val subtotal: Double,
    val shipping: Double,
    val commission: Double,
    val total: Double,
    val createdAt: Long
)
