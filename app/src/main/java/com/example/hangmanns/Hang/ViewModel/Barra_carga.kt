package com.example.hangman.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import com.example.hangman.viewmodel.SplashScreenViewModel

class SplashScreenViewModel : ViewModel() {
    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress

    init {
        startLoading()
    }

    private fun startLoading() {
        viewModelScope.launch {
            while (_progress.value < 1f) {
                delay(30)
                _progress.value += 0.01f
            }
            delay(1000)
            _progress.value = 1f
        }
    }

    fun navigateToMain(navController: NavController) {
        navController.navigate(Routes.MainActivity.route) {
            popUpTo(Routes.Carga.route) { inclusive = true }
        }
    }
}
