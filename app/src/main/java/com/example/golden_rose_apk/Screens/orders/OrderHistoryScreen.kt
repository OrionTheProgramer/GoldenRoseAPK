package com.example.golden_rose_apk.Screens.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.golden_rose_apk.ViewModel.OrdersViewModel
import com.example.golden_rose_apk.model.Order
import com.example.golden_rose_apk.utils.formatPrice
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    navController: NavController,
    ordersViewModel: OrdersViewModel = viewModel()
) {
    // Escuchamos las órdenes del usuario
    LaunchedEffect(Unit) {
        ordersViewModel.listenMyOrders()
    }

    val orders by ordersViewModel.orders.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de compras") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->

        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Todavía no tienes compras registradas.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orders) { order ->
                    OrderHistoryItem(
                        order = order,
                        onViewDetail = {
                            // Navegamos a la pantalla de detalle/boleta
                            navController.navigate("orderDetail/${order.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderHistoryItem(
    order: Order,
    onViewDetail: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Título del pedido
            Text(
                text = "Pedido ${order.id.takeLast(6)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Fecha
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val dateStr = dateFormat.format(Date(order.createdAt))

            Spacer(Modifier.height(4.dp))
            Text(
                text = "Fecha: $dateStr",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.height(4.dp))

            // Resumen rápido de productos (una línea)
            if (order.items.isNotEmpty()) {
                val resumenProductos = order.items
                    .joinToString { "${it.productName} x${it.quantity}" }

                Text(
                    text = resumenProductos,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }

            Spacer(Modifier.height(8.dp))

            // Total
            Text(
                text = "Total: $${order.total.formatPrice()}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(12.dp))

            // Botón ver detalle / boleta
            Button(
                onClick = onViewDetail,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver detalle / boleta")
            }
        }
    }
}


