package com.example.golden_rose_apk.Screens.orders

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.core.content.FileProvider
import com.example.golden_rose_apk.ViewModel.OrdersViewModel
import com.example.golden_rose_apk.model.Order
import com.example.golden_rose_apk.utils.formatPrice
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navController: NavController,
    orderId: String,
    ordersViewModel: OrdersViewModel = viewModel()
) {
    val context = LocalContext.current
    val orders by ordersViewModel.orders.collectAsState()

    // Por si aún no se han cargado las órdenes, escuchamos
    LaunchedEffect(Unit) {
        if (orders.isEmpty()) {
            ordersViewModel.listenMyOrders()
        }
    }

    val order = orders.firstOrNull { it.id == orderId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de compra") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->

        if (order == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Cargando detalle de la compra...")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Encabezado
            Text(
                text = "Pedido ${order.id.takeLast(6)}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val dateStr = dateFormat.format(Date(order.createdAt))

            Spacer(Modifier.height(4.dp))
            Text(text = "Fecha: $dateStr", style = MaterialTheme.typography.bodySmall)

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Productos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(8.dp))

            // Lista de items de la orden
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(order.items) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.productName, fontWeight = FontWeight.Medium)
                            Text(
                                "x${item.quantity}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        val lineTotal = item.price * item.quantity
                        Text(
                            text = "$${lineTotal.formatPrice()}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${order.total.formatPrice()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(24.dp))

            // Botón para descargar boleta PDF de esta order
            Button(
                onClick = {
                    try {
                        val uri = generateOrderReceiptPdf(context, order)
                        openPdf(context, uri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(
                            context,
                            "Error al generar la boleta",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Descargar boleta en PDF")
            }
        }
    }
}

/**
 * Genera un PDF con el detalle de una Order (historial de compra).
 * Basado en tu generateReceiptPdf pero usando Order en vez de CartItem.
 */
fun generateOrderReceiptPdf(
    context: Context,
    order: Order
): Uri {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    val paint = Paint()

    val marginStart = 40f
    val marginEnd = 555f
    var y = 50f

    // ENCABEZADO
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    paint.textSize = 18f
    canvas.drawText("Golden Rose - Boleta Electrónica", marginStart, y, paint)

    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.textSize = 12f

    y += 25f
    val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        .format(Date(order.createdAt))
    canvas.drawText("Fecha: $dateStr", marginStart, y, paint)

    canvas.drawText("N° Pedido: ${order.id}", marginEnd - 200f, y, paint)

    y += 25f
    canvas.drawLine(marginStart, y, marginEnd, y, paint)
    y += 20f

    // TÍTULO DETALLE
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    canvas.drawText("Detalle de la compra:", marginStart, y, paint)
    y += 15f

    // ENCABEZADO TABLA
    val colProductoX = marginStart
    val colCantX = 320f
    val colPrecioX = 380f
    val colSubX = 470f

    paint.textSize = 12f
    paint.isFakeBoldText = true
    canvas.drawText("Producto", colProductoX, y, paint)
    canvas.drawText("Cant.", colCantX, y, paint)
    canvas.drawText("P. Unit.", colPrecioX, y, paint)
    canvas.drawText("Subtotal", colSubX, y, paint)

    y += 8f
    paint.isFakeBoldText = false
    canvas.drawLine(marginStart, y, marginEnd, y, paint)
    y += 18f

    // FILAS DE PRODUCTOS
    for (item in order.items) {
        if (y > 780f) break

        val name = item.productName
        val qty = item.quantity
        val unitPrice = item.price
        val lineTotal = unitPrice * qty

        canvas.drawText(
            if (name.length > 25) name.take(22) + "..." else name,
            colProductoX,
            y,
            paint
        )
        canvas.drawText(qty.toString(), colCantX, y, paint)
        canvas.drawText("$${unitPrice.formatPrice()}", colPrecioX, y, paint)
        canvas.drawText("$${lineTotal.formatPrice()}", colSubX, y, paint)

        y += 18f
    }

    // SEPARADOR
    y += 10f
    canvas.drawLine(marginStart, y, marginEnd, y, paint)
    y += 20f

    // TOTAL
    fun drawTotalRow(label: String, value: String, bold: Boolean = false) {
        paint.isFakeBoldText = bold
        canvas.drawText(label, colSubX - 90f, y, paint)
        canvas.drawText(value, colSubX, y, paint)
        paint.isFakeBoldText = false
        y += 18f
    }

    drawTotalRow("TOTAL:", "$${order.total.formatPrice()}", bold = true)

    // PIE
    y += 30f
    paint.textSize = 10f
    paint.isFakeBoldText = false
    canvas.drawText(
        "Gracias por tu compra en Golden Rose. Esta boleta es un comprobante electrónico.",
        marginStart,
        y,
        paint
    )

    pdfDocument.finishPage(page)

    val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        ?: context.getExternalFilesDir(null)
        ?: context.filesDir

    if (!downloadsDir.exists()) downloadsDir.mkdirs()

    val file = File(downloadsDir, "boleta_${order.id}_${System.currentTimeMillis()}.pdf")
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}

/**
 * Abre el PDF con una app de visor de PDFs
 */
fun openPdf(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            "No se encontró una app para abrir PDF",
            Toast.LENGTH_LONG
        ).show()
    }
}