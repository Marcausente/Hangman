package com.example.hangman.Model

class Paraules_penjat {
        private val wordList = listOf("KOTLIN", "ANDROID", "JAVA", "COMPOSE", "HANGMAN")

        fun getRandomWord(): String {
            return wordList.random()
        }
}