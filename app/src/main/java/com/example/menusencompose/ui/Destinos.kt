package com.example.menusencompose.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Rutas de la app. Las agrupo en un enum para no repetir strings sueltos
 * en el NavHost y en la barra inferior.
 */
enum class Destino(val ruta: String, val titulo: String) {
    Inicio("inicio", "NOTIVas"),
    Perfil("perfil", "Mi perfil"),
    Cursos("cursos", "Mis cursos"),
    Promedios("promedios", "Promedios"),
    Favoritos("favoritos", "Destacados"),
    Papelera("papelera", "Papelera"),
    Ajustes("ajustes", "Ajustes")
}

/** Botones de la BottomAppBar: icono + destino + descripción de accesibilidad. */
data class BotonInferior(
    val icono: ImageVector,
    val destino: Destino,
    val descripcion: String
)

val botonesInferiores = listOf(
    BotonInferior(Icons.Filled.MenuBook, Destino.Cursos, "Cursos"),
    BotonInferior(Icons.Filled.BarChart, Destino.Promedios, "Promedios"),
    BotonInferior(Icons.Filled.Star, Destino.Favoritos, "Destacados"),
    BotonInferior(Icons.Filled.Delete, Destino.Papelera, "Papelera")
)

val destinosDelDrawer = listOf(
    Destino.Inicio, Destino.Cursos, Destino.Promedios,
    Destino.Favoritos, Destino.Papelera, Destino.Ajustes
)

fun iconoDeDestino(destino: Destino): ImageVector = when (destino) {
    Destino.Cursos -> Icons.Filled.MenuBook
    Destino.Promedios -> Icons.Filled.BarChart
    Destino.Favoritos -> Icons.Filled.Star
    Destino.Papelera -> Icons.Filled.Delete
    Destino.Ajustes -> Icons.Filled.Settings
    else -> Icons.Filled.MenuBook
}
