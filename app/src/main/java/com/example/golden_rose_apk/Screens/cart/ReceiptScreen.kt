package com.example.golden_rose_apk.Screens.cart

import android.content.ActivityNotFoundException
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.golden_rose_apk.ViewModel.CartViewModel
import com.example.golden_rose_apk.utils.formatPrice
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.FileProvider
import com.example.golden_rose_apk.ViewModel.CartItem
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreen(
    navController: NavController,
    totalAmount: Double = 0.0,         // solo para mostrar; para el PDF recalculamos
    cartViewModel: CartViewModel
) {
    val context = LocalContext.current
    val cartItems by cartViewModel.cartItems.collectAsState()

    // 👇 para comprobar en el log si llega vacío o no
    Log.d("ReceiptScreen", "cartItems size = ${cartItems.size}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Compra Exitosa")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = "Éxito",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "¡Gracias por tu compra!",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Tu pedido ha sido procesado exitosamente.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Resumen de la Boleta",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Detalle de productos del carrito en la pantalla
                    cartItems.forEach { item ->
                        ReceiptDetailRow(
                            label = "${item.product.name} x${item.quantity}",
                            value = "$${((item.product.price ?: 0.0) * item.quantity).formatPrice()}"
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Este total puede venir del nav, no afecta al PDF
                    ReceiptDetailRow("Monto Total:", "$${totalAmount.formatPrice()}")
                    ReceiptDetailRow("Método de Pago:", "Simulado")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Button(
                    onClick = {
                        if (cartItems.isEmpty()) {
                            Toast.makeText(
                                context,
                                "No hay productos en el carrito para generar la boleta",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        try {
                            // Recalcular totales basados en el carrito ACTUAL
                            val subtotal = cartItems.sumOf { cartItem ->
                                (cartItem.product.price ?: 0.0) * cartItem.quantity
                            }
                            val commission = if (subtotal > 0) subtotal * 0.05 else 0.0
                            val shipping = if (subtotal > 0) 5.0 else 0.0
                            val total = subtotal + commission + shipping

                            val uri = generateReceiptPdf(
                                context = context,
                                orderId = null,
                                items = cartItems,
                                subtotal = subtotal,
                                shipping = shipping,
                                commission = commission,
                                totalAmount = total
                            )

                            openPdf(context, uri)

                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(
                                context,
                                "Error al generar la boleta",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Icon(Icons.Default.Download, contentDescription = null)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Descargar")
                }

                Button(
                    onClick = {
                        // 👉 AHORA sí limpiamos el carrito después de ver la boleta
                        cartViewModel.clearCart()

                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Volver al Inicio")
                }
            }
        }
    }
}

@Composable
fun ReceiptDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
        Text(value)
    }
}

/**
 * Genera un PDF con el detalle de la compra
 */
fun generateReceiptPdf(
    context: Context,
    orderId: String? = null,
    items: List<CartItem>,
    subtotal: Double,
    shipping: Double,
    commission: Double,
    totalAmount: Double
): Uri {
    val pdfDocument = PdfDocument()

    // Tamaño A4 aprox. en puntos
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    val paint = Paint()

    val marginStart = 40f
    val marginEnd = 555f
    var y = 50f

    // ---------- ENCABEZADO ----------
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    paint.textSize = 18f
    canvas.drawText("Golden Rose - Boleta Electrónica", marginStart, y, paint)

    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.textSize = 12f

    y += 25f
    val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
    canvas.drawText("Fecha: $dateStr", marginStart, y, paint)

    orderId?.let {
        canvas.drawText("N° Pedido: $it", marginEnd - 150f, y, paint)
    }

    y += 25f
    canvas.drawLine(marginStart, y, marginEnd, y, paint)
    y += 20f

    // ---------- TITULO DETALLE ----------
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    canvas.drawText("Detalle de la compra:", marginStart, y, paint)
    y += 15f

    // ---------- ENCABEZADO TABLA ----------
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

    // ---------- FILAS DE PRODUCTOS ----------
    for (item in items) {
        if (y > 780f) { // evitar salirnos de la página
            break
        }

        val name = item.product.name ?: "Sin nombre"
        val qty = item.quantity
        val unitPrice = item.product.price ?: 0.0
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

    // ---------- LÍNEA DE SEPARACIÓN ----------
    y += 10f
    canvas.drawLine(marginStart, y, marginEnd, y, paint)
    y += 20f

    // ---------- RESUMEN DE TOTALES ----------
    fun drawTotalRow(label: String, value: String, bold: Boolean = false) {
        paint.isFakeBoldText = bold
        canvas.drawText(label, colSubX - 90f, y, paint)
        canvas.drawText(value, colSubX, y, paint)
        paint.isFakeBoldText = false
        y += 18f
    }

    drawTotalRow("Subtotal:", "$${subtotal.formatPrice()}")
    drawTotalRow("Envío:", "$${shipping.formatPrice()}")
    drawTotalRow("Comisión:", "$${commission.formatPrice()}")
    y += 5f
    canvas.drawLine(colSubX - 100f, y, marginEnd, y, paint)
    y += 20f
    drawTotalRow("TOTAL:", "$${totalAmount.formatPrice()}", bold = true)

    // ---------- PIE ----------
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

    // ---------- GUARDAR EN /Android/data/<app>/files/Download ----------
    val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        ?: context.getExternalFilesDir(null)
        ?: context.filesDir   // fallback interno

    if (!downloadsDir.exists()) downloadsDir.mkdirs()

    val file = File(downloadsDir, "boleta_${System.currentTimeMillis()}.pdf")
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()

    // FileProvider -> Uri para abrirlo
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}

/**
 * Abre el PDF con alguna app de visor de PDFs
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