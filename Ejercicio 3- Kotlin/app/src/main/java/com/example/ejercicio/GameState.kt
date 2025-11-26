package com.example.ejercicio

sealed class GameState {
    object EnJuego : GameState()
    object Ganado : GameState()
    object Perdido : GameState()
}
