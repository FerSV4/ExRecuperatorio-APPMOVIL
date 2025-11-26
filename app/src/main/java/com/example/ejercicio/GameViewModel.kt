package com.example.ejercicio

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

data class JuegoUiData(
    val intentosRestantes: Int = 5,
    val numeroObjetivo: Int = Random.nextInt(1, 51),
    val mensaje: String = "",
    val textoIngreso: String = "",
    val estadoJuego: GameState = GameState.EnJuego
)

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(JuegoUiData())
    val uiState: StateFlow<JuegoUiData> = _uiState

    fun actualizarTexto(nuevoTexto: String) {
        _uiState.value = _uiState.value.copy(textoIngreso = nuevoTexto)
    }

    fun intentarAdivinar() {
        val datosActuales = _uiState.value

        if (datosActuales.estadoJuego != GameState.EnJuego) return

        val numeroUsuario = datosActuales.textoIngreso.toIntOrNull()

        if (numeroUsuario == null) {
            _uiState.value = datosActuales.copy(
                mensaje = "Ingresa solo números válidos"
            )
            return
        }

        val nuevoIntento = datosActuales.intentosRestantes - 1

        if (numeroUsuario == datosActuales.numeroObjetivo) {
            _uiState.value = datosActuales.copy(
                mensaje = "Adivinaste el número",
                estadoJuego = GameState.Ganado
            )
            return
        }

        if (nuevoIntento <= 0) {
            _uiState.value = datosActuales.copy(
                intentosRestantes = 0,
                mensaje = "No te quedan intentos. El número era ${datosActuales.numeroObjetivo}",
                estadoJuego = GameState.Perdido
            )
            return
        }

        val pista = if (numeroUsuario < datosActuales.numeroObjetivo) {
            "El número objetivo es mayor"
        } else {
            "El número objetivo es menor"
        }

        _uiState.value = datosActuales.copy(
            intentosRestantes = nuevoIntento,
            mensaje = pista,
            textoIngreso = ""
        )
    }

    fun reiniciarJuego() {
        _uiState.value = JuegoUiData()
    }
}
