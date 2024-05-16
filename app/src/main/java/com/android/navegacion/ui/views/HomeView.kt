
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.navegacion.components.*
import com.universae.reproductor.domain.entities.alumno.Alumno
import com.universae.reproductor.domain.entities.asignatura.Asignatura
import com.universae.reproductor.domain.entities.grado.Grado
import com.universae.reproductor.domain.entities.grado.GradoId
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domain.usecases.AlumnoUseCaseImpl
import com.universae.reproductor.domain.usecases.AsignaturaUseCasesImpl
import com.universae.reproductor.domain.usecases.GradoUseCaseImpl
import com.universae.reproductor.domaintest.PreviewTemas
import com.universae.reproductor.ui.theme.GrisOscuro
import com.universae.reproductor.ui.theme.gradientBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
fun HomeView(navController: NavController, alumnoId: Int) {

    val alumno: Alumno = AlumnoUseCaseImpl.getAlumnoById(alumnoId)!! // Si llegamos a esta view es porque el alumno existe y siempre retorna un alumno
    val grado : Grado? = GradoUseCaseImpl.getGrado(alumno.gradosId.first())
    val nombreReal = alumno.nombreReal

    //val alumno: Alumno = AlumnoUseCaseImpl.gatAlumnoById(alumnoId)!! // Si llegamos a esta view es porque el alumno existe y siempre retorna un alumno
    // TODO("comprobar si hay cola de reproducción")

    var hayCola = false
    // TODO("Obtener listas")

    var listaAsignaturas: List<Asignatura> = mutableListOf()

    for (gradoId: GradoId in alumno.gradosId) {
        listaAsignaturas += AsignaturaUseCasesImpl.asignaturasNoCompletadas(gradoId)
    }

    var listadoReal: Set<Asignatura> = listaAsignaturas.sortedBy { it.asignaturaId.id }.toSet()

    val listaTemas: List<Tema> = PreviewTemas
    // Estructura básica con barra superior y botón flotante
    Scaffold(
        floatingActionButton = { ActionButton() }
    ) { innerPadding ->
        // Columna Lazy que se ajusta al padding proporcionado por el Scaffold
        LazyColumn(
            modifier = Modifier.gradientBackground()
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (hayCola) {
                item {Spacer(modifier = Modifier.height(10.dp)) }
                item { TituloIzquierda(texto = "${nombreReal}, ¿Quieres continuar escuchando...?",) }
                item {Spacer(modifier = Modifier.height(10.dp)) }
                item { FilaTituloCola() }// Todo("hacer despues de todo listo")
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }else{
                item {Spacer(modifier = Modifier.height(10.dp)) }
                item { TituloIzquierda(texto = "Bienvenido ${nombreReal} nos escanta volver a verte!") }
                item {Spacer(modifier = Modifier.height(20.dp))}
                item {FilaTituloNoCola(grado) }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                }

            item {
                    TituloMedianoCentralLeft(texto = "Asignaturas...") }


            item { Spacer(modifier = Modifier.height(5.dp)) }
            item { PodcastsAsignaturasTemas(listadoReal.toList(), navController = navController) }

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
                    text = "",
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
 * */

@Composable
fun FilaTituloNoCola(grado : Grado?) {

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
                .weight(1f) // Usa weight para permitir que el elemento ocupe un espacio proporcional en el Row
                .shadow(
                    8.dp,
                    shape = RoundedCornerShape(16.dp)
                ), // Sombra más pronunciada y bordes redondeados
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Decorative Icon",
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp)) // Espaciador para crear un padding mínimo entre la Card y el texto
// Columna para el texto y el progreso a la derecha de la Card
        Column(
            modifier = Modifier.weight(2f), // Usa un peso mayor para la columna
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start // Alinea el texto al inicio (izquierda de su columna)
        ) {
            grado?.nombreModulo?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        lineHeight = 28.sp
                    ).merge(MaterialTheme.typography.bodyLarge)
                )
            }
            Text(
                text = "Título: X",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontSize = 16.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Progreso",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                val progress = remember { mutableStateOf(0.0f) }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(48.dp) // Tamaño del CircularProgressIndicator
                ) {
// CircularProgressBar
                    CircularProgressIndicator(
                        progress = { progress.value },
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp,
                    )
// Texto del porcentaje
                    Text(
                        text = "${(progress.value * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    )
                }
// Manejo adecuado de la corutina dentro de la composable
                LaunchedEffect(Unit) {
                    withContext(Dispatchers.Default) {
                        for (i in 1..100) {
                            delay(100) // Simula un trabajo que toma tiempo 1 segundo
                            progress.value = i / 100f
                        }
                    }
                }
            }
        }
    }
}
