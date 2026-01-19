package com.example.golden_rose_apk.ViewModel

import androidx.lifecycle.ViewModel
import com.example.golden_rose_apk.model.ProductFirestore
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _products = MutableStateFlow<List<ProductFirestore>>(emptyList())
    val products: StateFlow<List<ProductFirestore>> = _products

    init {
        loadProducts()
    }

    fun refresh() {
        loadProducts()
    }

    fun loadProducts() {
        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { doc ->
                    doc.toObject(ProductFirestore::class.java)?.copy(
                        id = doc.id
                    )
                }
                _products.value = list
            }
            .addOnFailureListener {
                _products.value = emptyList()
            }
    }
}