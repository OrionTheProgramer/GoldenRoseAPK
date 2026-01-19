package com.example.golden_rose_apk.Screens.cart

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.golden_rose_apk.ViewModel.AuthViewModel
import com.example.golden_rose_apk.ViewModel.CartViewModel
import com.example.golden_rose_apk.ViewModel.OrdersViewModel
import com.example.golden_rose_apk.model.Order
import com.example.golden_rose_apk.utils.formatPrice
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    authViewModel: AuthViewModel,
    ordersViewModel: OrdersViewModel = viewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Cálculos reales a partir del carrito
    val subtotal = cartItems.sumOf { (it.product.price ?: 0.0) * it.quantity }
    val commission = if (subtotal > 0) subtotal * 0.05 else 0.0
    val shipping = if (subtotal > 0) 5.0 else 0.0   // aquí ya bajaste el envío
    val total = subtotal + commission + shipping

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmar Compra") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Resumen del Pedido", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            // Lista de items del carrito
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(cartItems) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${item.product.name} (x${item.quantity})")
                        Text(
                            "$${((item.product.price ?: 0.0) * item.quantity).formatPrice()}"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Divider()
            SummaryRow("Subtotal:", subtotal)
            SummaryRow("Envío:", shipping)
            SummaryRow("Comisión:", commission)
            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total:", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    "$${total.formatPrice()}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 Botón Pagar con creación de orden, limpieza de carrito y navegación a boleta
            Button(
                onClick = {
                    scope.launch {
                        if (cartItems.isEmpty()) return@launch

                        ordersViewModel.createOrderFromCart(cartItems) { order: Order? ->
                            if (order != null) {

                                // 2) Navegar a la boleta pasando el total real
                                val safeTotal = order.total
                                navController.navigate("receipt/$safeTotal") {
                                    // sacamos la pantalla de carrito de la pila
                                    popUpTo("cart") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error al crear la orden",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = cartItems.isNotEmpty()
            ) {
                Text("Pagar")
            }
        }
    }
}