package com.example.hangman

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class SplashScreenViewModel : ViewModel() {

    // Estado para el progreso de la barra
    private val _progress = mutableStateOf(0f)
    val progress: State<Float> = _progress

    // Estado para saber cuándo completar la animación
    private val _navigateToMain = mutableStateOf(false)
    val navigateToMain: State<Boolean> = _navigateToMain

    // Función que maneja la animación de progreso y la navegación
    fun startLoading() {
        viewModelScope.launch {
            // Incrementa el progreso hasta llegar a 1 (100%)
            while (_progress.value < 1f) {
                delay(30) // Se incrementa cada 30 ms
                _progress.value += 0.01f
            }

            // Después de que se haya llenado la barra, espera un poco
            delay(1000) // 1 segundo de espera antes de navegar

            // Indicar que la carga ha terminado y navegar a la pantalla principal
            _navigateToMain.value = true
        }
    }
}
