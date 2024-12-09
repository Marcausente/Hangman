package com.example.hangman

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Screen2(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Yellow), // Fondo amarillo
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Cómo jugar Hangman",
            fontSize = 28.sp,
            color = Color.Black,
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = """
                En el juego de Hangman (El ahorcado), tienes que adivinar una palabra secreta. 
                Cada vez que adivinas una letra, se revela si está en la palabra.
                
                Tienes un número limitado de intentos para adivinar la palabra antes de que se complete el dibujo de un hombre ahorcado. 
                Si adivinas la palabra antes de que se complete el dibujo, ¡ganas!

                Reglas básicas:
                - Una letra incorrecta te acerca al final del juego.
                - Cada intento es crucial, así que piensa bien antes de adivinar.
                - Elige una dificultad para hacer el juego más desafiante.
            """.trimIndent(),
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { navController.navigate(Routes.MainActivity.route) }) {
            Text(text = "Volver al Menú Principal")
        }
    }
}
