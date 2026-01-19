package com.example.golden_rose_apk.Screens.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.golden_rose_apk.ViewModel.OrdersViewModel
import com.example.golden_rose_apk.model.Order
import com.example.golden_rose_apk.utils.formatPrice
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    navController: NavController,
    ordersViewModel: OrdersViewModel
) {
    val orders by ordersViewModel.orders.collectAsState()

    LaunchedEffect(Unit) {
        ordersViewModel.listenMyOrders()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis compras") },
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
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Todavía no tienes compras.")
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
                    OrderItemCard(order = order) {
                        // Al hacer click en "Ver boleta"
                        navController.navigate("receipt/${order.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItemCard(order: Order, onSeeReceipt: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Pedido ${order.id.takeLast(6)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val dateText = dateFormat.format(Date(order.createdAt))

            Text(text = "Fecha: $dateText", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(4.dp))

            Text(
                text = "Total: $${order.total.formatPrice()}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onSeeReceipt,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver boleta")
            }
        }
    }
}
