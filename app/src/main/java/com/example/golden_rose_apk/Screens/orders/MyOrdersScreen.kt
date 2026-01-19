package com.example.golden_rose_apk.Screens.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.golden_rose_apk.ViewModel.OrdersViewModel
import com.example.golden_rose_apk.model.Order
import com.example.golden_rose_apk.utils.formatPrice
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrdersScreen(
    navController: NavController,
    ordersViewModel: OrdersViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        ordersViewModel.listenMyOrders()
    }

    val orders by ordersViewModel.orders.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis compras") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
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
            if (orders.isEmpty()) {
                Text("Aún no tienes compras registradas.")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(orders) { order ->
                        OrderRow(order = order) {
                            // Ver boleta de esta orden
                            navController.navigate("receipt/${order.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderRow(order: Order, onViewReceipt: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Pedido #${order.id}", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("Total: $${order.total.formatPrice()}")
            Spacer(Modifier.height(4.dp))
            Text(
                "Productos: ${order.items.joinToString { "${it.productName} x${it.quantity}" }}",
                maxLines = 1
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onViewReceipt,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver boleta")
            }
        }
    }
}
