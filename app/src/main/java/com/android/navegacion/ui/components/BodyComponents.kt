package com.android.navegacion.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.navegacion.R
import com.universae.reproductor.domain.entities.asignatura.Asignatura
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        Text(text = name)
    }
}
/**
 * Muestra una fila de tarjetas para cada tema de podcast proporcionado.
 * @param podcasts Lista de nombres de podcasts para mostrar.
 */
@Composable
fun PodcastsRow(podcasts: List<Asignatura>, navController: NavController) {
    LazyRow(
        modifier = Modifier
            .height(120.dp)
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(podcasts) { podcast ->
            var idCard : String = podcast.asignaturaId.id.toString()
            PodcastTopicCard(topic = podcast.nombreAsignatura, { navController.navigate("Detail/${idCard}") })
        }
    }
}

/**
 * Muestra una tarjeta para un tema de podcast específico.
 * @param topic Tema del podcast a mostrar.
 */
@Composable
fun PodcastTopicCard(topic: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp)
            .clickable(onClick = onClick ), // Agregando la funcionalidad clickable aquí
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
 * Muestra un título grande con estilo imponente.
 * @param texto El contenido del texto a mostrar.
 */
@Composable
fun TituloPrincipal(texto: String) {
    Text(
        text = texto,
        style = TextStyle(
            color = Color.White, // Color del texto
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
    Text(
        text = texto,
        modifier = Modifier
            .padding(top = 5.dp)// Añade un padding en la parte superior de 3dp
            .padding(start = 14.dp)
            .fillMaxWidth()  // Asegura que el Text ocupe el ancho completo para permitir la alineación a la izquierda
            .wrapContentWidth(Alignment.Start),  // Alinea el contenido del Text a la izquierda
        style = TextStyle(
            color = Color.White, // Color del texto
            fontSize = 35.sp, // Tamaño del texto
            letterSpacing = 3.sp, // Espaciado entre letras
            lineHeight = 30.sp // Altura de línea del texto

        ).merge(MaterialTheme.typography.bodyLarge) // Combina con los estilos predeterminados de MaterialTheme
    )
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
            color = Color.White, // Color del texto
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
            color = Color.White, // Color del texto
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
    Row( modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = texto,
            style = TextStyle(
                color = Color.White, // Color del texto
                fontSize = 20.sp, // Tamaño del texto
                fontWeight = FontWeight.Bold, // Grosor de la fuente
                letterSpacing = 2.sp, // Espaciado entre letras
                lineHeight = 28.sp, // Altura de línea del texto
                background = Color.Transparent // Asegura que no hay fondo


            ).merge(MaterialTheme.typography.bodyMedium) // Combina con los estilos predeterminados de MaterialTheme
        )}
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
fun iconArroyBack(): ImageVector {
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
fun iconArroyForward(): ImageVector {
    val imageVector = ImageVector.vectorResource(id = R.drawable.arrowforward)
    Icon(
        imageVector = imageVector,
        contentDescription = "Descripción del icono"
    )
    return imageVector
}


