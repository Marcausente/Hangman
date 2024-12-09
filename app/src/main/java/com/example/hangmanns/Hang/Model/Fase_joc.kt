package com.example.hangman.Model

class Fase_joc {
    data class GameState(
        val wordToGuess: String,
        val hiddenWord: String = "_ ".repeat(wordToGuess.length).trim(),
        val attemptsLeft: Int = 5,
        val guessedLetters: List<Char> = emptyList()
    )
}