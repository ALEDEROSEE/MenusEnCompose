package com.example.menusencompose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/** Contenedor común: aplica el padding del Scaffold y permite hacer scroll. */
@Composable
private fun Hoja(padding: PaddingValues, contenido: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
            // 88.dp abajo para que el FAB no se monte sobre la última fila:
            // el padding del Scaffold cubre la barra inferior, pero no el FAB.
            .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 88.dp)
    ) {
        contenido()
    }
}

@Composable
fun PantallaPerfil(padding: PaddingValues) {
    Hoja(padding) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(110.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Outlined.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(72.dp)
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            "Alejandro Aaron Alvarez Sayaverde",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            "Diseño y Desarrollo de Software · Ciclo V",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))
        HorizontalDivider()
        FilaIcono(Icons.Filled.MenuBook, "Cursos matriculados", "6 cursos este ciclo")
        FilaIcono(Icons.Filled.BarChart, "Promedio general", "15.4 de 20")
        FilaIcono(Icons.Filled.Star, "Cursos destacados", "2 marcados")
        Spacer(Modifier.height(12.dp))
        Text(
            "Esta vista se abre desde el icono Icons.Outlined.AccountCircle " +
                "de la barra superior.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private data class Curso(val nombre: String, val docente: String, val promedio: Double)

private val cursos = listOf(
    Curso("Programación en Móviles", "Benjamin Pareja", 16.5),
    Curso("Aplicaciones Empresariales", "Jorge Beeck", 15.0),
    Curso("Internet de las Cosas", "Jorge Miranda", 14.2),
    Curso("Taller de Proyectos", "Hugo Ccoaquira", 17.0),
    Curso("Inglés V", "Área de Idiomas", 13.8),
    Curso("Ética Profesional", "Área de Humanidades", 15.9)
)

@Composable
fun PantallaCursos(padding: PaddingValues) {
    Hoja(padding) {
        Text("Cursos del ciclo", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        cursos.forEach { curso ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(curso.nombre, style = MaterialTheme.typography.titleMedium)
                    Text(curso.docente, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(6.dp))
                    Text("Promedio: ${curso.promedio}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun PantallaPromedios(padding: PaddingValues) {
    val general = cursos.sumOf { it.promedio } / cursos.size
    Hoja(padding) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Promedio ponderado", style = MaterialTheme.typography.titleMedium)
                Text(
                    String.format("%.2f", general),
                    style = MaterialTheme.typography.displayMedium
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        Text("Detalle por curso", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        cursos.forEach { curso ->
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(curso.nombre, style = MaterialTheme.typography.bodyMedium)
                    Text("${curso.promedio}", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { (curso.promedio / 20.0).toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun PantallaFavoritos(padding: PaddingValues) {
    Hoja(padding) {
        Text("Cursos destacados", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text(
            "Marco aquí los cursos que estoy vigilando de cerca porque el " +
                "promedio está justo.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(12.dp))
        FilaIcono(Icons.Filled.Star, "Inglés V", "13.8 — necesito 16 en el final")
        FilaIcono(Icons.Filled.Star, "Internet de las Cosas", "14.2 — falta la última práctica")
    }
}

@Composable
fun PantallaPapelera(padding: PaddingValues) {
    Hoja(padding) {
        Text("Papelera", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text(
            "Las notas que borro quedan 30 días aquí antes de eliminarse " +
                "definitivamente.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(12.dp))
        FilaIcono(Icons.Filled.Delete, "Práctica 2 de Inglés V", "Eliminada hace 3 días")
        FilaIcono(Icons.Filled.Delete, "Examen parcial de Ética", "Eliminada hace 11 días")
    }
}

@Composable
fun PantallaAjustes(padding: PaddingValues) {
    var temaOscuro by rememberSaveable { mutableStateOf(false) }
    var avisos by rememberSaveable { mutableStateOf(true) }
    Hoja(padding) {
        Text("Ajustes", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Seguir el tema del sistema", style = MaterialTheme.typography.bodyLarge)
            Switch(checked = temaOscuro, onCheckedChange = { temaOscuro = it })
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Avisarme si bajo de 13", style = MaterialTheme.typography.bodyLarge)
            Switch(checked = avisos, onCheckedChange = { avisos = it })
        }
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))
        Text(
            "Esta vista está enlazada desde el menú lateral (Drawer).",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
