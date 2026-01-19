package com.example.golden_rose_apk.repository

import android.content.Context
import com.example.golden_rose_apk.data.AppDatabase
import com.example.golden_rose_apk.data.ReceiptEntity
import com.example.golden_rose_apk.data.ReceiptItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class LocalReceiptRepository(context: Context) {
    private val receiptDao = AppDatabase.getInstance(context).receiptDao()
    private val gson = Gson()

    suspend fun createReceipt(
        buyerName: String,
        buyerEmail: String,
        paymentMethod: String,
        items: List<ReceiptItem>,
        subtotal: Double,
        shipping: Double,
        commission: Double,
        total: Double,
        receiptId: String? = null
    ): String {
        val finalReceiptId = receiptId ?: UUID.randomUUID().toString()
        val receipt = ReceiptEntity(
            id = finalReceiptId,
            buyerName = buyerName,
            buyerEmail = buyerEmail,
            paymentMethod = paymentMethod,
            itemsJson = gson.toJson(items),
            subtotal = subtotal,
            shipping = shipping,
            commission = commission,
            total = total,
            createdAt = System.currentTimeMillis()
        )
        withContext(Dispatchers.IO) {
            receiptDao.insertReceipt(receipt)
        }
        return finalReceiptId
    }

    suspend fun getReceipt(receiptId: String): ReceiptEntity? {
        return withContext(Dispatchers.IO) {
            receiptDao.getReceiptById(receiptId)
        }
    }

    fun decodeItems(itemsJson: String): List<ReceiptItem> {
        val type = object : TypeToken<List<ReceiptItem>>() {}.type
        return gson.fromJson(itemsJson, type) ?: emptyList()
    }
}
