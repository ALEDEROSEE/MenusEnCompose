package com.example.menusencompose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

// Función Composable que crea un Scaffold personalizado
@Composable
fun CustomScaffold() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Estado que vive en la pantalla principal: cuántas veces se pulsó el FAB.
    // rememberSaveable para que el contador sobreviva a un giro de pantalla.
    var vecesPulsado by rememberSaveable { mutableIntStateOf(0) }

    // La ruta actual me sirve para el título de la barra y para decidir
    // si muestro el icono de menú o la flecha de volver.
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route
    val destinoActual = Destino.entries.firstOrNull { it.ruta == rutaActual } ?: Destino.Inicio
    val esInicio = destinoActual == Destino.Inicio

    ModalNavigationDrawer(
        drawerState = drawerState,
        // Drawer: el menú lateral que se expande/contrae
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "NOTIVas",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 28.dp, top = 24.dp, bottom = 4.dp)
                )
                Text(
                    text = "Promedios al día",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 28.dp, bottom = 12.dp)
                )
                HorizontalDivider()
                destinosDelDrawer.forEach { destino ->
                    NavigationDrawerItem(
                        icon = { Icon(iconoDeDestino(destino), contentDescription = null) },
                        label = { Text(destino.titulo) },
                        selected = destino == destinoActual,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navegarA(navController, destino)
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            // Barra superior
            topBar = {
                CustomTopBar(
                    titulo = destinoActual.titulo,
                    esInicio = esInicio,
                    onMenu = { scope.launch { drawerState.open() } },
                    onVolver = { navController.navigateUp() },
                    onBuscar = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Buscador aún no implementado")
                        }
                    },
                    onPerfil = { navegarA(navController, Destino.Perfil) }
                )
            },
            // Barra inferior
            bottomBar = {
                CustomBottomBar(onBoton = { navegarA(navController, it.destino) })
            },
            // Botón flotante personalizado
            floatingActionButton = { CustomFAB(onClick = { vecesPulsado++ }) },
            // Host de Snackbar (otro slot que Material 3 ofrece en el Scaffold)
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            // Contenido principal
            content = { padding ->
                NavHost(
                    navController = navController,
                    startDestination = Destino.Inicio.ruta
                ) {
                    composable(Destino.Inicio.ruta) {
                        CustomContent(padding, vecesPulsado)
                    }
                    composable(Destino.Perfil.ruta) { PantallaPerfil(padding) }
                    composable(Destino.Cursos.ruta) { PantallaCursos(padding) }
                    composable(Destino.Promedios.ruta) { PantallaPromedios(padding) }
                    composable(Destino.Favoritos.ruta) { PantallaFavoritos(padding) }
                    composable(Destino.Papelera.ruta) { PantallaPapelera(padding) }
                    composable(Destino.Ajustes.ruta) { PantallaAjustes(padding) }
                }
            }
        )
    }
}

/**
 * Navega evitando apilar la misma pantalla dos veces si el usuario
 * vuelve a tocar el botón del destino en el que ya está.
 */
private fun navegarA(
    navController: androidx.navigation.NavHostController,
    destino: Destino
) {
    if (navController.currentDestination?.route != destino.ruta) {
        navController.navigate(destino.ruta) { launchSingleTop = true }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    titulo: String,
    esInicio: Boolean,
    onMenu: () -> Unit,
    onVolver: () -> Unit,
    onBuscar: () -> Unit,
    onPerfil: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            if (esInicio) {
                IconButton(onClick = onMenu) {
                    Icon(imageVector = Icons.Rounded.Menu, contentDescription = "Abrir menú")
                }
            } else {
                IconButton(onClick = onVolver) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            }
        },
        title = { Text(text = titulo) },
        actions = {
            IconButton(onClick = onBuscar) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Buscar"
                )
            }
            IconButton(onClick = onPerfil) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Mi perfil"
                )
            }
        }
    )
}

@Composable
fun CustomBottomBar(onBoton: (BotonInferior) -> Unit) {
    BottomAppBar {
        // El Row con SpaceEvenly es lo que evita que los iconos queden
        // apilados a la izquierda, que es el comportamiento por defecto.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            botonesInferiores.forEach { boton ->
                IconButton(onClick = { onBoton(boton) }) {
                    Icon(boton.icono, contentDescription = boton.descripcion)
                }
            }
        }
    }
}

@Composable
fun CustomFAB(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Text(
            fontSize = 24.sp, // Tamaño de fuente del texto del botón
            text = "+" // Texto del botón
        )
    }
}

@Composable
fun CustomContent(padding: PaddingValues, vecesPulsado: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Pantalla principal de NOTIVas",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Veces que pulsaste el FAB",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "$vecesPulsado",
                        fontSize = 56.sp,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Usa el menú lateral o la barra inferior para moverte " +
                    "entre las vistas. El icono de la cuenta abre el perfil.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    )
}

/** Fila reutilizable para las pantallas de detalle. */
@Composable
fun FilaIcono(icono: ImageVector, titulo: String, detalle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = null, modifier = Modifier.padding(end = 16.dp))
        Column {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Text(detalle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
