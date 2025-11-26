package com.example.ejercicio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ejercicio.ui.theme.EjercicioTheme


class MainActivity : ComponentActivity() {

    private val pantallaViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EjercicioTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JuegoPantalla(pantallaViewModel)
                }
            }
        }
    }
}

@Composable
fun JuegoPantalla(viewModel: GameViewModel) {
    val estadoUi by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Adivina el número entre 1 y 50")
        Text(text = "Intentos restantes: ${estadoUi.intentosRestantes}")
        OutlinedTextField(
            value = estadoUi.textoIngreso,
            onValueChange = { viewModel.actualizarTexto(it) },
            label = { Text("Tu número") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(text = estadoUi.mensaje)

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { viewModel.intentarAdivinar() },
                enabled = estadoUi.estadoJuego == GameState.EnJuego
            ) {
                Text("Adivinar")
            }
            OutlinedButton(onClick = { viewModel.reiniciarJuego() }) {
                Text("Reiniciar")
            }
        }

        when (estadoUi.estadoJuego) {
            GameState.Ganado -> Text(text = "Ganaste el juego")
            GameState.Perdido -> Text(text = "Perdiste el juego")
            GameState.EnJuego -> {}
        }
    }
}
