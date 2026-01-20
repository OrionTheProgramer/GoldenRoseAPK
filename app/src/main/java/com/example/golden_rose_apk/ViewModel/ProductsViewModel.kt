package com.example.golden_rose_apk.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.golden_rose_apk.model.ProductFirestore
import com.example.golden_rose_apk.repository.LocalProductRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LocalProductRepository(application)

    private val _products = MutableStateFlow<List<ProductFirestore>>(emptyList())
    val products: StateFlow<List<ProductFirestore>> = _products

    init {
        loadProducts()
    }

    fun refresh() {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _products.value = runCatching { repository.loadProducts() }
                .getOrElse { emptyList() }
        }
    }
}
