package com.android.navegacion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.navegacion.R
import com.universae.domain.entities.asignatura.Asignatura
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domain.usecases.AsignaturaUseCasesImpl
import com.universae.navegacion.theme.AzulClaro
import com.universae.navegacion.theme.AzulDark
import com.universae.navegacion.theme.AzulOscuro
import com.universae.navegacion.theme.Blanco
import com.universae.navegacion.theme.GrisOscuro
import com.universae.navegacion.theme.Negro
import com.universae.navegacion.theme.ralewayFamily
import com.universae.navegacion.views.ImageWithColoredPlaceholder

@Composable
fun TitleView(name: String) {
    Text(text = name, fontSize = 40.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun Space() {
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun MainButton(name: String, backColor: Color, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick, colors = ButtonDefaults.buttonColors(
            containerColor = backColor,
            contentColor = color
        )
    ) {
        Text(text = "Prueba")
    }
}

/**
 * Muestra una fila de tarjetas para cada tema de podcast proporcionado.
 * @param asignaturaList Lista de nombres de podcasts para mostrar.
 */
@Composable
fun PodcastsAsignaturasTemas(asignaturaList: List<Asignatura>, navController: NavController) {
    var focusedAsignaturaId by remember { mutableStateOf<Int?>(null) }
    var displayedTemas by remember { mutableStateOf<List<Tema>>(emptyList()) }

    var temaMarcado by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(focusedAsignaturaId) {
        displayedTemas = if (focusedAsignaturaId != null) {
            asignaturaList.find { it.asignaturaId.id == focusedAsignaturaId }?.temas ?: emptyList()
        } else {
            asignaturaList.mapNotNull { asignatura ->
                asignatura.temas.firstOrNull { !it.terminado }
            }
        }
    }

    Column {
        LazyRow(
            modifier = Modifier
                .height(120.dp)
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(asignaturaList) { asignatura ->
                val isFocused = asignatura.asignaturaId.id == focusedAsignaturaId
                val onCardClick: () -> Unit = {
                    if (isFocused) {
                        navController.navigate("Detail/${asignatura.asignaturaId.id}")
                    } else {
                        focusedAsignaturaId = asignatura.asignaturaId.id
                        temaMarcado = "Temas de " + asignatura.nombreAsignatura
                    }
                }
                AsignaturaCard(
                    asignatura = asignatura,
                    onCardClick = onCardClick
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        TituloMedianoCentralLeft(texto = temaMarcado ?: "Temas sugeridos")
        Spacer(modifier = Modifier.height(5.dp))

        LazyRow(
            modifier = Modifier
                .height(120.dp)
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(displayedTemas) { tema ->
                TemaCard(
                    tema = tema,
                    onClick = { navController.navigate("Podcast/${tema.temaId.id}") }
                )
            }
        }
    }
}


/**
 * Muestra una tarjeta para un tema de podcast.
 * @param topic Tema del podcast a mostrar.
 */
@Composable
fun AsignaturaCard(asignatura: Asignatura, onCardClick: () -> Unit, anchoTotal: Int = 240, anchoImagen: Int = 80) {
    Card(
        modifier = Modifier
            .width(anchoTotal.dp)
            .clickable(onClick = onCardClick)
            .shadow(
                8.dp,
                shape = RoundedCornerShape(16.dp)
            ) // Sombra más pronunciada y bordes redondeados
            .clip(RoundedCornerShape(16.dp))
            .background(AzulClaro),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Row (modifier = Modifier.fillMaxWidth().background(AzulClaro),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageWithColoredPlaceholder(
                imageUrl = asignatura.icoAsignatura,
                placeholderRes = R.mipmap.escudo,
                placeholderColor = AzulOscuro,
                modifier = Modifier
                    .width(anchoImagen.dp)
                    .background(color = AzulClaro),
                contentDescription = "Icono de ${asignatura.nombreAsignatura}",
                padding = 8
            )
            Text(
                text = asignatura.nombreAsignatura,
                style = MaterialTheme.typography.bodyLarge.copy(color = AzulDark),
                fontFamily = ralewayFamily,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun TemaCard(tema: Tema, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable(onClick = onClick)
            .shadow(
                8.dp,
                shape = RoundedCornerShape(16.dp)
            ), // Sombra más pronunciada y bordes redondeados
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth().background(AzulClaro)) { // Asegura que el contenido ocupe todo el ancho disponible
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ImageWithColoredPlaceholder(
                    imageUrl = tema.imagenUrl,
                    placeholderRes = R.mipmap.escudo,
                    placeholderColor = AzulDark,
                    modifier = Modifier
                        .width(80.dp)
                        .background(color = AzulClaro),
                    contentDescription = "Icono de ${tema.nombreTema}",
                    padding = 8
                )
                Text(
                    text = tema.nombreTema,
                    style = MaterialTheme.typography.bodyLarge.copy(fontFamily = ralewayFamily, color = AzulDark),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

/**
 * Muestra un título grande con estilo imponente.
 * @param texto El contenido del texto a mostrar.
 */
@Composable
fun TituloPrincipal(texto: String) {
    Text(
        text = texto,
        style = TextStyle(
            color = Blanco, // Color del texto
            fontSize = 20.sp, // Tamaño del texto muy grande para realzar la importancia
            fontWeight = FontWeight.Bold, // Grosor de la fuente para destacar el título
            letterSpacing = 1.sp, // Espaciado entre letras para mejorar la legibilidad
            lineHeight = 25.sp // Altura de línea ajustada para manejar el tamaño de fuente más grande
        ).merge(MaterialTheme.typography.displayLarge) // Combina con un estilo de display grande de MaterialTheme
    )
}

/**
 * Muestra un título grande con estilo imponente, alineado a la izquierda.
 * @param texto El contenido del texto a mostrar.
 */
@Composable
fun TituloIzquierda(texto: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center // Centra la Box internamente
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), // Asegura que la Box interna ocupe todo el ancho disponible
            contentAlignment = Alignment.Center // Centra el contenido internamente
        ) {
            Text(
                text = texto,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                    lineHeight = 40.sp,
                    shadow = Shadow(
                        color = Negro,
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                ).merge(MaterialTheme.typography.bodyLarge)
            )
        }
    }
}

/**
 * Muestra un título Grande con estilo personalizado.
 * @param texto El contenido del texto a mostrar.
 */
@Composable
fun TituloGrande(texto: String) {
    Text(
        text = texto,
        style = TextStyle(
            color = Blanco, // Color del texto
            fontSize = 35.sp, // Tamaño del texto
            fontWeight = FontWeight.Bold, // Grosor de la fuente
            letterSpacing = 3.sp, // Espaciado entre letras
            lineHeight = 30.sp // Altura de línea del texto

        ).merge(MaterialTheme.typography.bodyLarge) // Combina con los estilos predeterminados de MaterialTheme
    )
}

/**
 * Muestra un título mediano con estilo personalizado.
 * @param texto El contenido del texto a mostrar.
 */
@Composable
fun TituloMediano(texto: String) {
    Text(
        text = texto,
        style = TextStyle(
            color = Blanco, // Color del texto
            fontSize = 20.sp, // Tamaño del texto
            fontWeight = FontWeight.Bold, // Grosor de la fuente
            letterSpacing = 2.sp, // Espaciado entre letras
            lineHeight = 28.sp, // Altura de línea del texto
            background = Color.Transparent // Asegura que no hay fondo
        ).merge(MaterialTheme.typography.bodyMedium) // Combina con los estilos predeterminados de MaterialTheme
    )
}

/**
 * @param texto sirve para determinar el texto a mostrar alineamento a la izquierda
 */
@Composable
fun TituloMedianoCentralLeft(texto: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start // Alinea el contenido al inicio
    ) {
        Text(
            text = texto,
            style = TextStyle(
                color = Blanco,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                lineHeight = 30.sp,
                shadow = Shadow(
                    color = GrisOscuro,
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            ).merge(MaterialTheme.typography.bodyMedium) // Combina con los estilos predeterminados de MaterialTheme
        )
    }
}

@Composable
fun DynamicCircularProgressBar(progress: MutableState<Float>) {
    CircularProgressIndicator(
        progress = { progress.value },
        modifier = Modifier.padding(24.dp),
        strokeWidth = 8.dp,
    )
}

@Composable
fun iconCast(): ImageVector {
    val imageVector = ImageVector.vectorResource(id = R.drawable.cast)
    Icon(
        imageVector = imageVector,
        contentDescription = "Cast"
    )
    return imageVector
}

@Composable
fun iconPause(): ImageVector {
    val imageVector = ImageVector.vectorResource(id = R.drawable.pause)
    Icon(
        imageVector = imageVector,
        contentDescription = "Pausa"
    )
    return imageVector
}

@Composable
fun iconPlay(): ImageVector {
    val imageVector = ImageVector.vectorResource(id = R.drawable.play)
    Icon(
        imageVector = imageVector,
        contentDescription = "Play"
    )
    return imageVector
}

@Composable
fun iconArrowBack(): ImageVector {
    val imageVector = ImageVector.vectorResource(id = R.drawable.arroyback)
    Icon(
        imageVector = imageVector,
        contentDescription = "Flecha de regreso"
    )
    return imageVector
}

@Composable
fun iconBackwardTenSec(): ImageVector {
    val imageVector = ImageVector.vectorResource(id = R.drawable.baseline_replay_10_24)
    Icon(
        imageVector = imageVector,
        contentDescription = "Retroceso diez segundos"
    )
    return imageVector
}

@Composable
fun iconFastReward(): ImageVector {
    val imageVector = ImageVector.vectorResource(id = R.drawable.fastrewind)
    Icon(
        imageVector = imageVector,
        contentDescription = "Signo de retroceso rápido"
    )
    return imageVector
}

@Composable
fun iconFastForward(): ImageVector {
    val imageVector = ImageVector.vectorResource(id = R.drawable.fastfoward)
    Icon(
        imageVector = imageVector,
        contentDescription = "Signo de avance rápido"
    )
    return imageVector
}

@Composable
fun arrowForwardTenSec(): ImageVector {
    val imageVector = ImageVector.vectorResource(id = R.drawable.baseline_forward_10_24)
    Icon(
        imageVector = imageVector,
        contentDescription = "Avance diez segundos"
    )
    return imageVector
}


