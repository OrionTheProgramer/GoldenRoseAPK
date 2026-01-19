package com.example.golden_rose_apk.repository

import com.example.golden_rose_apk.model.ProductFirestore
import com.google.firebase.firestore.FirebaseFirestore


class ProductRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getProducts(
        onSuccess: (List<ProductFirestore>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val products = result.documents.mapNotNull {
                    it.toObject(ProductFirestore::class.java)?.copy(id = it.id)
                }
                onSuccess(products)
            }
            .addOnFailureListener { onError(it) }
    }
}
