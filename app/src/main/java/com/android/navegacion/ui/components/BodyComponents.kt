package com.android.navegacion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
import com.universae.reproductor.domain.entities.asignatura.Asignatura
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.ui.theme.AzulClaro
import com.universae.reproductor.ui.theme.Blanco
import com.universae.reproductor.ui.theme.GrisClaro
import com.universae.reproductor.ui.theme.GrisOscuro
import com.universae.reproductor.ui.theme.Negro

@Composable
fun TitleView(name : String){
    Text(text = name, fontSize = 40.sp, fontWeight = FontWeight.Bold)
}
@Composable
fun Space(){
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun MainButton(name : String, backColor : Color, color : Color, onClick:() -> Unit){
    Button(onClick = onClick, colors = ButtonDefaults.buttonColors(
        containerColor     = backColor,
        contentColor  = color
    )) {
        Text(text = "Prueba")
    }
}
/**
 * Muestra una fila de tarjetas para cada tema de podcast proporcionado.
 * @param podcasts Lista de nombres de podcasts para mostrar.
 */
@Composable
fun PodcastsAsignaturasTemas(podcasts: List<Asignatura>, navController: NavController) {
    var focusedAsignaturaId by remember { mutableStateOf<Int?>(null) }
    var displayedTemas by remember { mutableStateOf<List<Tema>>(emptyList()) }

    LaunchedEffect(focusedAsignaturaId) {
        displayedTemas = if (focusedAsignaturaId != null) {
            podcasts.find { it.asignaturaId.id == focusedAsignaturaId }?.temas ?: emptyList()
        } else {
            podcasts.mapNotNull { it.temas.firstOrNull() }
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
            items(podcasts) { podcast ->
                val isFocused = podcast.asignaturaId.id == focusedAsignaturaId
                val onCardClick: () -> Unit = {
                    if (isFocused) {
                        navController.navigate("Detail/${podcast.asignaturaId.id}")
                    } else {
                        focusedAsignaturaId = podcast.asignaturaId.id
                    }
                }
                PodcastTopicCard(
                    isFocused = isFocused,
                    titulo = podcast.nombreAsignatura,
                    onCardClick = onCardClick
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        TituloMedianoCentralLeft(texto = "Temas...")
        Spacer(modifier = Modifier.height(5.dp))

        LazyRow(
            modifier = Modifier
                .height(120.dp)
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(displayedTemas) { tema ->
                cardTema(
                    titulo = tema.nombreTema,
                    onClick = { navController.navigate("Podcast/${tema.temaId.id}") }
                )
            }
        }
    }
}



/**
 * Muestra una tarjeta para un tema de podcast específico.
 * @param topic Tema del podcast a mostrar.
 */
@Composable
fun PodcastTopicCard(isFocused: Boolean, titulo: String, onCardClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(120.dp)
            .clickable(onClick = onCardClick)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp)), // Sombra más pronunciada y bordes redondeados
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            AzulClaro,
                            Blanco,
                            GrisClaro
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Decorative Icon",
                    modifier = Modifier.size(22.dp),
                    tint = Blanco
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.bodyLarge.copy(color = Negro),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}






@Composable
fun cardTema(titulo : String, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .width(200.dp)
            .height(120.dp)
            .clickable(onClick = onClick)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp)), // Sombra más pronunciada y bordes redondeados
shape = RoundedCornerShape(16.dp),
elevation = CardDefaults.cardElevation(
defaultElevation = 8.dp
)
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        AzulClaro,
                        Blanco,
                        GrisClaro
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Decorative Icon",
                modifier = Modifier.size(22.dp),
                tint = Blanco
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodyLarge.copy(color = Negro),
                modifier = Modifier.padding(horizontal = 16.dp)
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
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(

                            AzulClaro,
                            Blanco,
                            GrisClaro
                        )
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
                .fillMaxWidth(), // Asegura que la Box interna ocupe todo el ancho disponible
            contentAlignment = Alignment.Center // Centra el contenido internamente
        ) {
            Text(
                text = texto,
                style = TextStyle(
                    color = Negro,
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
}/**
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
        contentDescription = "Descripción del icono"
    )
    return imageVector
}
@Composable
fun iconPause(): ImageVector {
    val imageVector = ImageVector.vectorResource(id = R.drawable.pause)
    Icon(
        imageVector = imageVector,
        contentDescription = "Descripción del icono"
    )
    return imageVector
}
@Composable
fun iconPlay(): ImageVector {
    val imageVector = ImageVector.vectorResource(id = R.drawable.play)
    Icon(
        imageVector = imageVector,
        contentDescription = "Descripción del icono"
    )
    return imageVector
}
@Composable
fun iconArrowBack(): ImageVector {
    val imageVector = ImageVector.vectorResource(id = R.drawable.arroyback)
    Icon(
        imageVector = imageVector,
        contentDescription = "Descripción del icono"
    )
    return imageVector
}

@Composable
fun iconFastReward(): ImageVector {
    val imageVector = ImageVector.vectorResource(id = R.drawable.fastrewind)
    Icon(
        imageVector = imageVector,
        contentDescription = "Descripción del icono"
    )
    return imageVector
}
@Composable
fun iconFastForward(): ImageVector {
    val imageVector = ImageVector.vectorResource(id = R.drawable.fastfoward)
    Icon(
        imageVector = imageVector,
        contentDescription = "Descripción del icono"
    )
    return imageVector
}
@Composable
fun iconArrowForward(): ImageVector {
    val imageVector = ImageVector.vectorResource(id = R.drawable.arrowforward)
    Icon(
        imageVector = imageVector,
        contentDescription = "Descripción del icono"
    )
    return imageVector
}


