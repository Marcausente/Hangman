package com.example.hangman

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType

@Composable
fun Screen1(navController: NavController) {
    var wordToGuess by remember { mutableStateOf("JAVA") }
    var hiddenWord by remember { mutableStateOf("_ _ _ _") }
    var attemptsLeft by remember { mutableStateOf(5) }
    val guessedLetters = remember { mutableStateListOf<Char>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        // Mostrar la palabra oculta
        Text(
            text = hiddenWord,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        // Mostrar intentos restantes
        Text(text = "Intentos restantes: $attemptsLeft", fontSize = 20.sp)

        // Mostrar la imagen del hangman debajo de los intentos restantes
        Image(
            painter = painterResource(id = getHangmanImageResource(attemptsLeft)),
            contentDescription = "Hangman Image",
            modifier = Modifier
                .size(200.dp)  // Tamaño de la imagen
                .padding(top = 16.dp) // Espacio entre el contador y la imagen
        )

        // Crear teclado de letras
        val alphabet = ('A'..'Z').toList()
        for (row in alphabet.chunked(7)) {
            Row(horizontalArrangement = Arrangement.Center) {
                for (letter in row) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Texto encima del botón
                        Text(
                            text = letter.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = {
                                // Adivinar letra
                                guessedLetters.add(letter)
                                if (wordToGuess.contains(letter)) {
                                    hiddenWord = wordToGuess.map {
                                        if (it in guessedLetters) it else '_'
                                    }.joinToString(" ")
                                } else {
                                    attemptsLeft--
                                }
                            },
                            enabled = !guessedLetters.contains(letter) && attemptsLeft > 0, // Deshabilitar si ya se adivinó o si intentos son 0
                            modifier = Modifier.size(40.dp)
                        ) {}
                    }
                }
            }
        }

        // Verificar si el juego terminó y redirigir al resultado
        // Solo redirigimos si ya se agotaron los intentos o se adivinó la palabra
        if (attemptsLeft == 0 || hiddenWord.replace(" ", "") == wordToGuess) {
            val gameResult = if (hiddenWord.replace(" ", "") == wordToGuess) "ganaste" else "perdiste"

            // Redirigir a Pantalla3 con el resultado
            LaunchedEffect(attemptsLeft) {
                navController.navigate("${Routes.Pantalla3.route}/$gameResult/$attemptsLeft")
            }
        }
    }
}

fun getHangmanImageResource(attemptsLeft: Int): Int {
    return when (attemptsLeft) {
        5 -> R.drawable.zero  // Sin partes del cuerpo
        4 -> R.drawable.uno   // Cabeza
        3 -> R.drawable.dos   // Cabeza + Torso
        2 -> R.drawable.tres  // Cabeza + Torso + Brazos
        1 -> R.drawable.quatro  // Cabeza + Torso + Brazos + Piernas
        else -> R.drawable.cinco  // Completo (sin intentos restantes)
    }
}

@Composable
fun Pantalla3(navController: NavController, gameResult: String, attemptsLeft: Int) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Contenido en la parte superior (Texto en el centro)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 128.dp),  // Espacio para los botones (mayor espacio para los dos botones)
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mostrar mensaje de victoria o derrota
            Text(
                text = if (gameResult == "ganaste") "¡Felicidades, ganaste!" else "Perdiste, la palabra era $gameResult",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )

            // Mostrar los intentos restantes debajo del mensaje
            Text(
                text = "Intentos restantes: $attemptsLeft",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // Column que contiene los botones apilados
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)  // Alinea la columna de botones al centro de la parte inferior
                .padding(16.dp),  // Espacio alrededor de los botones
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)  // Espacio entre los botones
        ) {
            Button(
                onClick = { navController.navigate(Routes.MainActivity.route) },
                modifier = Modifier.fillMaxWidth()  // Ajusta el tamaño del botón
            ) {
                Text(text = "Volver al Menú Principal")
            }

            Button(
                onClick = { navController.navigate(Routes.Pantalla1.route) },
                modifier = Modifier.fillMaxWidth()  // Ajusta el tamaño del botón
            ) {
                Text(text = "Tornar a jugar")
            }
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.MainActivity.route) {
        composable(Routes.MainActivity.route) { Screen1(navController) }

        // Composable con parámetros para el resultado y los intentos restantes
        composable(
            "${Routes.Pantalla3.route}/{gameResult}/{attemptsLeft}",
            arguments = listOf(
                navArgument("gameResult") { type = NavType.StringType },
                navArgument("attemptsLeft") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val gameResult = backStackEntry.arguments?.getString("gameResult") ?: "perdiste"
            val attemptsLeft = backStackEntry.arguments?.getInt("attemptsLeft") ?: 0
            Pantalla3(navController, gameResult, attemptsLeft)
        }
    }
}
