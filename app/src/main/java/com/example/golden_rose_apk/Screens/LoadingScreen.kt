package com.example.golden_rose_apk.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(navController: NavController) {
    val phrases = listOf(
        "Preparando tu arsenal...",
        "Cargando skins legendarias...",
        "Sincronizando con el Protocolo Valorant...",
        "Pulimos cada detalle para tu próxima partida...",
        "Listo para el spike: casi terminamos..."
    )

    var progress by remember { mutableStateOf(0f) }
    var phraseIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        val totalSteps = 100
        for (step in 0..totalSteps) {
            progress = step / totalSteps.toFloat()
            if (step % 20 == 0 && step != 0) {
                phraseIndex = (phraseIndex + 1) % phrases.size
            }
            delay(35)
        }
        navController.navigate("home") {
            popUpTo("loading") { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Cargando",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = phrases[phraseIndex],
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
