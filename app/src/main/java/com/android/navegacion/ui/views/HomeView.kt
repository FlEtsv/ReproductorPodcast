// Importaciones necesarias de Android y Jetpack Compose
import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.navegacion.R
import com.android.navegacion.components.*
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domaintest.PreviewTemas
import com.universae.reproductor.ui.theme.ReproductorTheme
import com.universae.reproductor.ui.theme.ralewayFamily

/**
 * Vista principal de la aplicación, configurada para mostrar una barra superior, un botón flotante y un contenido dinámico.
 * @param navController Controlador de navegación para manejar transiciones de pantalla.
 * @param id Identificador del usuario.
 * @param pass Contraseña del usuario, opcional.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(navController: NavController, id: String, pass: String?) {
    // Estructura básica con barra superior y botón flotante
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar(name = "Home View de $id") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Red)
            )
        },
        floatingActionButton = { ActionButton() }
    ) { innerPadding ->
        // Columna Lazy que se ajusta al padding proporcionado por el Scaffold
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { PodcastsRow() }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            items(PreviewTemas) { tema ->
                TarjetaTema(tema)
            }
        }
    }
}

/**
 * Muestra una fila Lazy de temas de podcasts.
 */
@Composable
fun PodcastsRow() {
    LazyRow(
        modifier = Modifier
            .height(120.dp)
            .background(Color(0xFF596FB3))
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(listOf("Podcast 1", "Podcast 2", "Podcast 3", "Podcast 4")) { podcast ->
            PodcastTopicCard(topic = podcast)
        }
    }
}

/**
 * Muestra una tarjeta para un tema de podcast específico.
 * @param topic Tema del podcast a mostrar.
 */
@Composable
fun PodcastTopicCard(topic: String) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = topic, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

/**
 * Muestra una tarjeta con detalles sobre un tema musical.
 * @param tema Objeto Tema que contiene la información a mostrar.
 */
@Composable
fun TarjetaTema(tema: Tema) {
    var maxLines by rememberSaveable { mutableIntStateOf(2) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.mipmap.escudo),
                contentDescription = "UNIVERSAE logo",
                modifier = Modifier.size(80.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = tema.nombreTema,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = tema.descripcionTema,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
