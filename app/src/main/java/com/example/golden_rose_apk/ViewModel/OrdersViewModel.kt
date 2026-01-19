package com.example.golden_rose_apk.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.golden_rose_apk.model.Order
import com.example.golden_rose_apk.model.OrderItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class OrdersViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // ====== STATE: LISTA DE MIS ÓRDENES ======
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    // ====== CREAR ORDEN A PARTIR DEL CARRITO ======
    fun createOrderFromCart(
        cartItems: List<CartItem>,
        onResult: (Order?) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: ""

        // 1) Pasar carrito -> lista de OrderItem
        val items = cartItems.map { cartItem ->
            OrderItem(
                productName = cartItem.product.name ?: "Sin nombre",
                quantity = cartItem.quantity,
                price = cartItem.product.price ?: 0.0
            )
        }

        val total = items.sumOf { it.price * it.quantity }

        // 2) Crear Order sin id
        val order = Order(
            userId = userId,
            items = items,
            total = total
        )

        // 3) Guardar en Firestore
        viewModelScope.launch {
            db.collection("orders")
                .add(order)
                .addOnSuccessListener { doc ->
                    val finalOrder = order.copy(id = doc.id)
                    // opcional: guardar el id dentro del documento
                    doc.update("id", doc.id)
                    onResult(finalOrder)
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    onResult(null)
                }
        }
    }

    // ====== ESCUCHAR MIS ÓRDENES (HISTORIAL) ======
    fun listenMyOrders() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("orders")
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    e.printStackTrace()
                    return@addSnapshotListener
                }
                val list = snapshot?.toObjects(Order::class.java) ?: emptyList()
                _orders.value = list
            }
    }
}