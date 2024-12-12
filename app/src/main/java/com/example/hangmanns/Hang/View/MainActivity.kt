package com.example.hangman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hangman.ui.theme.HangmanTheme

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Carga.route  // Empezamos con la pantalla de carga
    ) {
        composable(Routes.Carga.route) {
            SplashScreen(navController)
        }
        composable(Routes.MainActivity.route) {
            HangmanMenu(navController)
        }
        composable(Routes.Pantalla1.route) {
            Screen1(navController)
        }
        composable(Routes.Pantalla2.route) {
            Screen2(navController)
        }
        composable(
            "${Routes.Pantalla3.route}/{attemptsLeft}",
            arguments = listOf(navArgument("attemptsLeft") { type = NavType.IntType })
        ) { backStackEntry ->
            val attemptsLeft = backStackEntry.arguments?.getInt("attemptsLeft") ?: 0
            Screen3(navController, attemptsLeft)
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HangmanTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun HangmanMenu(navController: NavController) {
    var selectedDifficulty by remember { mutableStateOf("Fácil") }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),  // Fondo blanco
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Agregar la imagen encima del título
        Image(
            painter = painterResource(id = R.drawable.marc),  // Asegúrate de tener esta imagen en tu carpeta drawable
            contentDescription = "Hangman Logo",
            modifier = Modifier
                .size(128.dp) // Tamaño de la imagen
                .padding(bottom = 16.dp)  // Espacio entre la imagen y el título
        )

        // Título de la aplicación
        Text(
            text = "Hangman",
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Selector de Dificultad
        Box {
            Button(onClick = { expanded = true }) {
                Text(text = "Dificultad: $selectedDifficulty")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf("Fácil", "Media", "Difícil").forEach { difficulty ->
                    DropdownMenuItem(onClick = {
                        selectedDifficulty = difficulty
                        expanded = false
                    }, text = { Text(text = difficulty) })
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Help que navega a Pantalla 2
        Button(onClick = {
            navController.navigate(Routes.Pantalla2.route) // Cambiado a Pantalla2
        }) {
            Text(text = "Ayuda")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para navegar a la Pantalla 1 (Screen2)
        Button(onClick = {
            navController.navigate(Routes.Pantalla1.route)
        }) {
            Text(text = "Jugar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HangmanMenuPreview() {
    HangmanTheme {
        HangmanMenu(
            navController = rememberNavController() // Corregido para usar un navController válido en la vista previa
        )
    }
}
