package com.example.hangman

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun SplashScreen(navController: NavController) {
    var progress by remember { mutableStateOf(0f) }

    val opacity by rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing), // Reducción de la duración de la animación
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(Unit) {
        while (progress < 1f) {
            delay(30)  // Aumentar el progreso cada 30ms
            progress += 0.01f  // Incremento de la barra de progreso
        }
        delay(1000)  // Espera un poco después de que la barra se haya llenado
        navController.navigate(Routes.MainActivity.route) {
            popUpTo(Routes.Carga.route) { inclusive = true }  // Elimina la pantalla de carga de la pila
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val image: Painter = painterResource(id = R.drawable.icon)  // Reemplaza con tu imagen
            Image(
                painter = image,
                contentDescription = "Loading image",
                modifier = Modifier
                    .size(100.dp)  // Tamaño de la imagen
                    .graphicsLayer(alpha = opacity)  // Aplicar la opacidad intermitente
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Cargando...",
                color = Color.Black,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(6.dp)
                    .padding(horizontal = 16.dp),
                color = Color.Blue,
                trackColor = Color.LightGray
            )
        }
    }
}
