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
import kotlin.random.Random

@Composable
fun Screen1(navController: NavController) {
    // Lista de palabras de 4 letras
    val words = listOf("WORD", "GAME", "LOVE", "JUMP", "TUNE", "BIRD")
    val easyWords = listOf("WORD", "GAME", "LOVE", "JUMP", "TUNE")
    val mediumWords = listOf("PENCIL", "MOUSE", "COMPUTER", "RIVER", "FIGHT")
    val hardWords = listOf("PROGRAMMING", "DEVELOPER", "JAVASCRIPT", "REACTJS", "ANDROID")


    // Estado para almacenar las palabras jugadas
    val playedWords = remember { mutableStateListOf<String>() }

    // Función para obtener una palabra aleatoria no repetida
    fun getRandomWord(): String {
        val remainingWords = words.filterNot { playedWords.contains(it) }
        return if (remainingWords.isNotEmpty()) {
            val randomWord = remainingWords[Random.nextInt(remainingWords.size)]
            playedWords.add(randomWord) // Marcamos la palabra como jugada
            randomWord
        } else {
            // Si ya se han jugado todas las palabras, reiniciar la lista
            playedWords.clear()
            val randomWord = words[Random.nextInt(words.size)]
            playedWords.add(randomWord)
            randomWord
        }
    }

    // Obtener una palabra aleatoria
    val wordToGuess = remember { getRandomWord() }

    var hiddenWord by remember { mutableStateOf("_ ".repeat(wordToGuess.length)) } // Inicializar con guiones bajos
    val guessedLetters = remember { mutableStateListOf<Char>() }
    val alphabet = ('A'..'Z').toList()

    // Estado para el número de intentos restantes
    var attemptsLeft by remember { mutableStateOf(5) }

    // Lógica para actualizar la palabra oculta
    fun updateHiddenWord(letter: Char) {
        var newHiddenWord = ""
        for (i in wordToGuess.indices) {
            newHiddenWord += if (wordToGuess[i] == letter) wordToGuess[i] else hiddenWord[i * 2]
            newHiddenWord += " "
        }
        hiddenWord = newHiddenWord.trim()
    }

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

        // Mostrar la cantidad de intentos restantes
        Text(
            text = "Intentos restantes: $attemptsLeft",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Mostrar una imagen estática del hangman
        Image(
            painter = painterResource(id = getHangmanImageResource(attemptsLeft)),
            contentDescription = "Hangman Image",
            modifier = Modifier
                .size(200.dp)
                .padding(top = 16.dp)
        )

        // Crear teclado de letras
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
                                // Si la letra no está en la palabra, restamos un intento
                                if (!wordToGuess.contains(letter)) {
                                    attemptsLeft -= 1
                                } else {
                                    updateHiddenWord(letter) // Actualizar la palabra oculta
                                }

                                // Agregar la letra adivinada a la lista de letras adivinadas
                                guessedLetters.add(letter)

                                // Si el jugador se queda sin intentos, mostramos la pantalla final
                                if (attemptsLeft == 0) {
                                    navController.navigate("${Routes.Pantalla3.route}/$attemptsLeft") // Pasar intentos restantes
                                }

                                // Si el jugador adivina la palabra completa, muestra la pantalla final
                                if (hiddenWord.replace(" ", "") == wordToGuess) {
                                    navController.navigate("${Routes.Pantalla3.route}/$attemptsLeft") // Pasar intentos restantes
                                }
                            },
                            enabled = !guessedLetters.contains(letter),
                            modifier = Modifier.size(40.dp)
                        ) {
                            // Mostrar la letra si ya fue adivinada correctamente
                            if (guessedLetters.contains(letter)) {
                                Text(
                                    text = letter.toString(),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
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
fun Screen3(navController: NavController, attemptsLeft: Int) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Contenido en el centro (Texto)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 128.dp),  // Espacio para los botones
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mostrar mensaje genérico
            Text(
                text = "¡Juego Finalizado!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )

            // Mostrar los intentos restantes
            Text(
                text = "Intentos restantes: $attemptsLeft",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        // Botones apilados en la parte inferior
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { navController.navigate(Routes.MainActivity.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Volver al Menú Principal")
            }

            Button(
                onClick = { navController.navigate(Routes.Pantalla1.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Jugar de nuevo")
            }
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.MainActivity.route) {
        composable(Routes.MainActivity.route) { Screen1(navController) }

        // Composable para Pantalla3 con el argumento de intentos restantes
        composable(
            "${Routes.Pantalla3.route}/{attemptsLeft}",
            arguments = listOf(navArgument("attemptsLeft") { type = NavType.IntType })
        ) { backStackEntry ->
            val attemptsLeft = backStackEntry.arguments?.getInt("attemptsLeft") ?: 0
            Screen3(navController, attemptsLeft)
        }
    }
}
