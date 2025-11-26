package com.example.ejercicio_1_permisos

data class PermisosState(
    val contactosConcedido: Boolean = false,
    val fotosConcedido: Boolean = false,
    val pendientes: List<String> = emptyList(),
    val mostrarIrAjustes: Boolean = false
)

sealed class PermisosIntent {
    object PedirContactos : PermisosIntent()
    object PedirFotos : PermisosIntent()
    object PedirAmbos : PermisosIntent()
    object ActualizarDesdeRuntime : PermisosIntent()
}
