package com.universae.navegacion.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.android.navegacion.R
import com.android.navegacion.components.*
import com.universae.domain.entities.asignatura.Asignatura
import com.universae.navegacion.theme.AzulClaro
import com.universae.navegacion.theme.AzulOscuro
import com.universae.reproductor.domain.entities.alumno.Alumno
import com.universae.reproductor.domain.entities.grado.Grado
import com.universae.reproductor.domain.usecases.AlumnoUseCaseImpl
import com.universae.reproductor.domain.usecases.AsignaturaUseCasesImpl
import com.universae.reproductor.domain.usecases.GradoUseCaseImpl
import com.universae.navegacion.theme.gradientBackground

//TODO("implementar click en grado y muestra asignaturas del grado y sugeridos de las asignaturas del grado")
/**
 * Vista principal de la aplicación, configurada para mostrar una barra superior, un botón flotante y un contenido dinámico.
 * @param navController Controlador de navegación para manejar transiciones de pantalla.
 * @param alumnoId Identificador del usuario.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeView(navController: NavController, alumnoId: Int) {

    val alumno: Alumno =
        AlumnoUseCaseImpl.getAlumnoById(alumnoId)!! // Si llegamos a esta view es porque el alumno existe y siempre retorna un alumno
    val grados: List<Grado> = GradoUseCaseImpl.getGradosByAlumnoID(alumnoId)
    val nombreReal = alumno.nombreReal

    // TODO("comprobar si hay cola de reproducción")

    var hayCola = false

    val allAsignaturasAlumno: List<Asignatura> = alumno.gradosId.flatMap { gradoId ->
        AsignaturaUseCasesImpl.asignaturasNoCompletadas(gradoId)
    }.sortedBy { it.asignaturaId.id }.distinct()

    // Estructura básica con barra superior y botón flotante
    Scaffold(
        floatingActionButton = { ActionButton() }
    ) { innerPadding ->
        // Columna Lazy que se ajusta al padding proporcionado por el Scaffold
        LazyColumn(
            modifier = Modifier
                .gradientBackground()
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (hayCola) {
                item { Spacer(modifier = Modifier.height(10.dp)) }
                item { TituloIzquierda(texto = "${nombreReal}, ¿Quieres continuar escuchando...?") }
                item { Spacer(modifier = Modifier.height(10.dp)) }
                item { FilaTituloCola() }// Todo("hacer despues de todo listo")
                item { Spacer(modifier = Modifier.height(20.dp)) }
            } else {
                item { Spacer(modifier = Modifier.height(10.dp)) }
                item { TituloIzquierda(texto = "Bienvenido $nombreReal nos escanta volver a verte!") }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item { FilaTituloNoCola(grados = grados) }
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }

            item {
                TituloMedianoCentralLeft(texto = "Asignaturas...")
            }

            item { Spacer(modifier = Modifier.height(5.dp)) }
            item { PodcastsAsignaturasTemas(allAsignaturasAlumno, navController = navController) }

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
 * Muestra una Card pequeña alineada a la izquierda con texto a su derecha, ambos centrados en el Row. TODO: revisar este composable
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
fun FilaTituloNoCola(grados: List<Grado?>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp) // Espaciado entre elementos
    ) {
        items(grados.size) { index ->
            val grado = grados[index]
            GradoCard(grado)
        }
    }
}

@Composable
fun ImageWithColoredPlaceholder(
    imageUrl: String,
    placeholderRes: Int,
    placeholderColor: Color,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    padding: Int = 0
) {
    val imagePainter = rememberImagePainter(data = imageUrl)

    Box(modifier = modifier) {
        if (imagePainter.state is ImagePainter.State.Loading || imageUrl.equals("null")) {
            val placeholder = painterResource(id = placeholderRes)
            Image(
                painter = placeholder,
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding.dp),
                colorFilter = ColorFilter.tint(placeholderColor)
            )
        }

        Image(
            painter = imagePainter,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding.dp)
        )
    }
}

@Composable
fun GradoCard(grado: Grado?) {
    Card(
        modifier = Modifier
            .width(340.dp) // Define el ancho aquí
            .height(100.dp)
            .background(Color.Transparent),
        colors = CardColors(Color.Transparent, Color.Transparent, Color.Transparent, Color.Transparent)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start // Centra los elementos dentro del Row
        ) {
            // Card pequeña a la izquierda
            Card(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .shadow(
                        8.dp,
                        shape = RoundedCornerShape(16.dp)
                    ), // Sombra más pronunciada y bordes redondeados
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                ImageWithColoredPlaceholder(
                    imageUrl = grado?.icoGrado ?: "",
                    placeholderRes = R.mipmap.escudo,
                    placeholderColor = AzulOscuro,
                    modifier = Modifier.fillMaxSize().background(color = AzulClaro),
                    contentDescription = "Icono del Grado"
                )
            }
            Spacer(modifier = Modifier.width(16.dp)) // Espaciador para crear un padding mínimo entre la Card y el texto
            // Columna para el texto y el progreso a la derecha de la Card
            Column(
                //modifier = Modifier.weight(1f), // Usa un peso mayor para la columna
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
                        ).merge(MaterialTheme.typography.bodyLarge),
                        maxLines = 2
                    )
                }
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
                    val progress = remember { mutableFloatStateOf(0.0f) }
                    // Observa los cambios en porcentajeCompletadoGrado y actualiza progress
                    LaunchedEffect(key1 = grado) {
                        grado?.let {
                            progress.value = GradoUseCaseImpl.porcentajeCompletadoGrado(grado.gradoId) / 100f
                        }
                    }
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
                }
            }
        }
    }
}