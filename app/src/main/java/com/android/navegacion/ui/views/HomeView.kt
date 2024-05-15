
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.navegacion.components.*
import com.android.navegacion.data.remote.GradoRepositoryImpl
import com.universae.reproductor.domain.entities.alumno.Alumno
import com.universae.reproductor.domain.entities.asignatura.Asignatura
import com.universae.reproductor.domain.entities.grado.GradoId
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domain.usecases.AlumnoUseCaseImpl
import com.universae.reproductor.domain.usecases.AsignaturaUseCasesImpl
import com.universae.reproductor.domaintest.PreviewAsignaturas
import com.universae.reproductor.domaintest.PreviewTemas
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

    val alumno: Alumno = AlumnoUseCaseImpl.gatAlumnoById(alumnoId)!! // Si llegamos a esta view es porque el alumno existe y siempre retorna un alumno


    //val alumno: Alumno = AlumnoUseCaseImpl.gatAlumnoById(alumnoId)!! // Si llegamos a esta view es porque el alumno existe y siempre retorna un alumno
    // TODO("comprobar si hay cola de reproducción")
    var hayCola = true
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
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (hayCola) {
                item {Spacer(modifier = Modifier.height(20.dp)) }
                item { TituloIzquierda(texto = "${alumno.nombreUsuario}, ¿Quieres continuar escuchando...?",) }
                item {Spacer(modifier = Modifier.height(20.dp)) }
                item { FilaTituloCola() }
                item { Spacer(modifier = Modifier.height(30.dp)) }
            }else{
                item {Spacer(modifier = Modifier.height(20.dp)) }
                item { TituloIzquierda(texto = "Bienvenido ${alumno.nombreUsuario} nos escanta volver a verte!") }
                item {Spacer(modifier = Modifier.height(30.dp))}
                item {FilaTituloNoCola() }
                item { Spacer(modifier = Modifier.height(30.dp)) }
                }

            item {
                    TituloMedianoCentralLeft(texto = "Asignaturas...") }


            item { Spacer(modifier = Modifier.height(5.dp)) }
            item { PodcastsAsignaturas(listadoReal.toList(), navController = navController) }
            item { Spacer(modifier = Modifier.height(40.dp)) }
            item {
                // ya es una row
                    TituloMedianoCentralLeft(texto = "Temas...")
                }

            item { Spacer(modifier = Modifier.height(5.dp)) }
            item { PodcastsTemas(listaTemas, navController = navController) }
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