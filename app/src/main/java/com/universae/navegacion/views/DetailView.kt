package com.android.navegacion.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.navegacion.R
import com.android.navegacion.components.AsignaturaCard
import com.android.navegacion.components.iconArrowBack
import com.universae.domain.entities.asignatura.Asignatura
import com.universae.domain.entities.asignatura.AsignaturaId
import com.universae.navegacion.theme.Azul
import com.universae.navegacion.theme.AzulClaro
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domain.usecases.AsignaturaUseCasesImpl
import com.universae.navegacion.theme.AzulDark
import com.universae.navegacion.theme.AzulMedio
import com.universae.navegacion.theme.AzulOscuro
import com.universae.navegacion.theme.gradientBackground
import com.universae.navegacion.theme.ralewayFamily
import kotlin.math.roundToInt
import androidx.compose.foundation.layout.Box as Box1

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailView(navController: NavController, idAsignatura: Int) {
    Box(modifier = Modifier
        .fillMaxSize()
        .gradientBackground()
    ) {
        Column {
            BarraSuperior(navController)
            ContentDetailView(navController, idAsignatura)
        }
    }
}

@Composable
fun BarraSuperior(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(), // Agrega un relleno en la parte superior igual a la altura de la barra de estado
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f), // Ocupa la mitad del ancho disponible
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(8.dp) // Ajusta el espaciado según sea necesario
            ) {
                Icon(
                    iconArrowBack(),
                    contentDescription = "Back",
                    tint = Color.White
                ) // Ajusta el contenido de descripción según sea necesario
            }
        }
    }
}

@Composable
fun ContentDetailView(navController: NavController, idAsignatura: Int) {
    BoxWithConstraints {//con esta funcion ya se puede sacar altura
        val asignatura = AsignaturaUseCasesImpl.getAsignaturaByAsignaturaId(AsignaturaId(idAsignatura))
        val nombreAsignatura = asignatura?.nombreAsignatura
        val screenHeight = maxHeight
        val islandHeight = screenHeight * 0.25f  // Calcular el 25% de la altura

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                if (nombreAsignatura != null) {
                    IslandRowTittle(idAsignatura = idAsignatura, height = islandHeight)
                }
            }
            item {

                AsignaturaUseCasesImpl.getAsignaturaByAsignaturaId(AsignaturaId(idAsignatura))
                    ?.let { asignatura ->
                        MostrarTemas(asignatura, navController)
                    }

            }
        }
    }
}

@Composable
fun MostrarTemas(asignatura: Asignatura, navController: NavController) {

    asignatura.temas.forEach { tema ->
        TarjetaTema(idAsignatura= asignatura.asignaturaId.id, tema = tema, modifier = Modifier, navController = navController)
    }
}


/**
 * Composable para mostrar una tarjeta de un tema
 * @param tema Tema a mostrar
 * @param modifier Modificador para personalizar la apariencia de la tarjeta
 */
@Composable
fun TarjetaTema(idAsignatura: Int, tema: Tema, modifier: Modifier = Modifier, navController: NavController) {
    // Estado mutable para controlar el número de líneas del texto
    var maxLines by rememberSaveable { mutableIntStateOf(2) }
    // Diseño de fila para la tarjeta del tema
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp)) // Aquí se establece el radio de las esquinas redondeadas
            .background(AzulClaro), // Aquí se establece el color de fondo,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del logo
        Image(
            painter = painterResource(id = R.mipmap.escudo),
            contentDescription = "UNIVERSAE logo",
            modifier = Modifier.size(80.dp),
            colorFilter = ColorFilter.tint(AzulMedio)
        )
        // Columna para el texto
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // Texto para el nombre del tema
            Text(
                text = tema.nombreTema,
                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = ralewayFamily, color = AzulDark),
                modifier = Modifier.padding(top = 4.dp)
            )
            // Texto para la descripción del tema
            Text(
                text = tema.descripcionTema,
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = ralewayFamily, color = Azul),
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            maxLines =
                                if (maxLines == Int.MAX_VALUE) 2 else Int.MAX_VALUE
                        })
                    }
            )
            // Texto para la duración del audio
            Text(
                text = tema.duracionAudio.toString(),
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = ralewayFamily, color = AzulOscuro),
                modifier = Modifier
                    .padding(bottom = 4.dp)

            )
        }
        // Caja para el icono de reproducción
        // TODO: agregar pointerInput para lanzar la reproducción
        Box1(
            modifier = modifier
                .padding(12.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                .clickable(onClick = { navController.navigate("Podcast/${tema.temaId.id}") })
        ) {
            // Icono de reproducción
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "incono de reproducción",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.surface
            )
        }
    }
}

@Composable
fun IslandRowTittle(idAsignatura: Int, height: Dp) {
    BoxWithConstraints (modifier = Modifier.padding(10.dp).height(height).clip(RoundedCornerShape(16.dp)).background(AzulClaro)
    ) {
        val anchoTotal: Int = maxWidth.value.roundToInt()
        AsignaturaCard(asignatura = AsignaturaUseCasesImpl.getAsignaturaById(idAsignatura)!!, onCardClick = {}, anchoTotal = anchoTotal, anchoImagen = anchoTotal/3)
    }
}