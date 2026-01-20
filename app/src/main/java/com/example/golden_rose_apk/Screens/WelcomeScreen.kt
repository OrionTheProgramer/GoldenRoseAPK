package com.example.golden_rose_apk.Screens

import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.golden_rose_apk.R
import kotlinx.coroutines.delay
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun WelcomeScreen(navController: NavController? = null) {
    
    // Asignando Variables
    val images = listOf(
        R.drawable.carousel_1,
        R.drawable.carousel_2,
        R.drawable.carousel_3,
        R.drawable.carousel_4
    )

    // Cambio de paginas
    val pagerState = rememberPagerState(pageCount = { images.size })

    // Prevención para el Preview
    if (!LocalInspectionMode.current) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(3000)
                pagerState.animateScrollToPage(
                    (pagerState.currentPage + 1) % images.size
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(70.dp))

        // Carrusel
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth(),
            pageSpacing = 10.dp,
            flingBehavior = PagerDefaults.flingBehavior(pagerState)
        ) { page ->

            Image(
                painter = painterResource(id = images[page]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(30.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop,
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Puntitos
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(images.size) { index ->
                Text(
                    text = if (index == pagerState.currentPage) "●" else "○",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        // Espaciado
        Spacer(modifier = Modifier.height(40.dp))

        // Botón Login
        Button(
            onClick = { navController?.navigate("login") },
            modifier = Modifier
                .width(200.dp)
                .height(45.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(50)
        ) {
            Text("Iniciar Sesión", color = MaterialTheme.colorScheme.onPrimary)
        }

        // Espaciado
        Spacer(modifier = Modifier.height(35.dp))

        // Botón Crear Cuenta
        Button(
            onClick = { navController?.navigate("register") },
            modifier = Modifier
                .width(200.dp)
                .height(45.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(50)
        ) {
            Text("Crear cuenta", color = MaterialTheme.colorScheme.onPrimary)
        }

        // Espaciado
        Spacer(modifier = Modifier.height(50.dp))

        // Invitado
        Text(
            text = "Ingresar como Invitado",
            fontSize = 13.sp,
            modifier = Modifier.clickable {
                navController?.navigate("home/true") {
                    popUpTo("welcome") { inclusive = true }
                }
            }
        )

        Spacer(modifier = Modifier.height(50.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen()
}
