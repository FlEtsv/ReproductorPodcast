
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
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.navegacion.Greeting
import com.android.navegacion.R
import com.android.navegacion.components.*
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domaintest.PreviewTemas
import com.universae.reproductor.ui.theme.ReproductorTheme
import com.universae.reproductor.ui.theme.ralewayFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    var hayCola = true
    val podcasts1 = listOf("1","2","3","4","5","6","7")
    val podcasts2 = listOf("1","2","3","4","5","6","7","8","9","10")
    // Estructura básica con barra superior y botón flotante
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar(name = "Home View de $id") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.DarkGray)
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
            if (hayCola) {
                item {Spacer(modifier = Modifier.height(20.dp)) }
                item { TituloIzquierda(texto = " Quieres continuar escuchando...",) }
                item {Spacer(modifier = Modifier.height(20.dp)) }
                item { FilaTituloCola() }
                item { Spacer(modifier = Modifier.height(30.dp)) }
            }else{
                item {Spacer(modifier = Modifier.height(20.dp)) }
                item { TituloIzquierda(texto = "Bienvenido $id nos escanta volver a verte!") }
                item {Spacer(modifier = Modifier.height(30.dp))}
                item {FilaTituloNoCola() }
                item { Spacer(modifier = Modifier.height(30.dp)) }
                }

            item {
                    TituloMedianoCentralLeft(texto = "Asignaturas...") }


            item { Spacer(modifier = Modifier.height(5.dp)) }
            item { PodcastsRow(podcasts1, navController = navController) }
            item { Spacer(modifier = Modifier.height(40.dp)) }
            item {
                // ya es una row
                    TituloMedianoCentralLeft(texto = "Temas...")
                }

            item { Spacer(modifier = Modifier.height(5.dp)) }
            item { PodcastsRow(podcasts2, navController = navController) }
            /*
            funcion para generar las lineas
            items(PreviewTemas) { tema ->
                TarjetaTema(tema)
            }

             */
        }
    }
}

/**
 * Muestra una Card pequeña alineada a la izquierda con texto a su derecha, ambos centrados en el Row.
 */
@Composable
fun FilaTituloCola() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center // Centra los elementos dentro del Row
    ) {
        // Card pequeña a la izquierda
        Card(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .weight(1f), // Usa weight para permitir que el elemento ocupe un espacio proporcional en el Row
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "Card",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp)) // Espaciador para crear un padding mínimo entre la Card y el texto
        // Columna para el texto a la derecha de la Card
        Column(
            modifier = Modifier.weight(1f), // Igual peso que la Card para centrar los elementos dentro del Row
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start // Alinea el texto al inicio (izquierda de su columna)
        ) {
            TituloGrande("Asignatura: X")
            TituloMediano("Tema: X")
            TituloMediano("Titulo: X")
        }
    }
}
/**
* Muestra una Card pequeña alineada a la izquierda con texto a su derecha, ambos centrados en el Row.
*/
@Composable
fun FilaTituloNoCola() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center // Centra los elementos dentro del Row
    ) {
        // Card pequeña a la izquierda
        Card(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .weight(1f), // Usa weight para permitir que el elemento ocupe un espacio proporcional en el Row
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "Card",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp)) // Espaciador para crear un padding mínimo entre la Card y el texto
        // Columna para el texto a la derecha de la Card
        Column(
            modifier = Modifier.weight(1f), // Igual peso que la Card para centrar los elementos dentro del Row
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start // Alinea el texto al inicio (izquierda de su columna)
        ) {
            Text("Grado: X", style = MaterialTheme.typography.bodyLarge)
            Text("Título: X", style = MaterialTheme.typography.bodyMedium)
            Row {
                
                val progress = remember { mutableStateOf(0.0f) }
                Column {
                    Text(text = "Progreso", style = MaterialTheme.typography.bodySmall)
                }
                Column {

                    // Manejo adecuado de la corutina dentro de la composable
                    LaunchedEffect(Unit) {
                        withContext(Dispatchers.Default) {
                            for (i in 1..100) {
                                delay(100) // Simula un trabajo que toma tiempo 1 segundo
                                progress.value = i / 100f
                            }
                        }
                    }

                    DynamicCircularProgressBar(progress = progress)
                    
                }
            }

        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ReproductorTheme {
         val id : String = "1"
        val pass : String = "1234"
        HomeView(navController = rememberNavController(), id =id  , pass = pass )
    }
}