package com.android.navegacion.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.navegacion.R
import com.android.navegacion.components.MainIconButton
import com.android.navegacion.components.TitleBar
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domaintest.PreviewTemas
import com.universae.reproductor.ui.theme.ReproductorTheme
import com.universae.reproductor.ui.theme.ralewayFamily
import androidx.compose.foundation.layout.Box as Box1

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailView(navController: NavController,idCard : String){
    Scaffold (
        topBar = {
            TopAppBar(
                title = { TitleBar(name = "")},
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Blue
                ),
                navigationIcon = {
                    MainIconButton(icon = Icons.Default.ArrowBack) {
                        navController.popBackStack()
                    }
                }
            )
        }
    ){
        ContentDetailView(navController,idCard)
    }
}
@Composable
fun ContentDetailView(navController: NavController, idCard: String){
    BoxWithConstraints {//con esta funcion ya se puede sacar altura

        val screenHeight = maxHeight
        val islandHeight = screenHeight * 0.25f  // Calcular el 25% de la altura

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                IslandRowTittle(idCard = idCard, height = islandHeight)
            }
            item {
                MostrarTemas(PreviewTemas,navController)
            }
        }
    }
}
@Composable
fun MostrarTemas(temas: List<Tema>,navController: NavController) {

        temas.forEach { tema ->
            TarjetaTema(tema = tema, modifier = Modifier, navController = navController)
        }
    }


/**
 * Composable para mostrar una tarjeta de un tema
 * @param tema Tema a mostrar
 * @param modifier Modificador para personalizar la apariencia de la tarjeta
 */
@Composable
fun TarjetaTema(tema: Tema, modifier: Modifier = Modifier, navController: NavController) {
    // Estado mutable para controlar el número de líneas del texto
    var maxLines by rememberSaveable { mutableIntStateOf(2) }
    // Diseño de fila para la tarjeta del tema
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp)) // Aquí se establece el radio de las esquinas redondeadas
            .background(MaterialTheme.colorScheme.onTertiary) // Aquí se establece el color de fondo
            .clickable(onClick = { navController.navigate("Podcast/${tema.nombreTema}") }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del logo
        Image(
            painter = painterResource(id = R.mipmap.escudo),
            contentDescription = "UNIVERSAE logo",
            modifier = Modifier.size(80.dp)
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
                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = ralewayFamily),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )
            // Texto para la descripción del tema
            Text(
                text = tema.descripcionTema,
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = ralewayFamily),
                color = MaterialTheme.colorScheme.secondary,
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
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = ralewayFamily),
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .padding(bottom = 4.dp)

            )
        }
        // Caja para el icono de reproducción
        //agregar pointerInput para lanzar la reproducción
        Box1(
            modifier = modifier
                .padding(12.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
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
fun IslandRowTittle(idCard: String, height : Dp) {
    Box1(
        modifier = Modifier
            .padding(10.dp)  // Aumentar el padding para dar la impresión de más espacio alrededor
            .shadow(16.dp, RoundedCornerShape(12.dp))  // Aumentar la sombra  mayor profundidad
            .clip(RoundedCornerShape(12.dp))  // Suavizar más las esquinas
            .background(Color(0xFF0077CC))  // Fondo azul
            .padding(4.dp)  // Padding interno para separar el contenido del borde
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(horizontal = 16.dp, vertical = 12.dp),  // Padding interno para contenido
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Título de asignatura: $idCard",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ReproductorTheme {
        val idCard : String = "1"
    DetailView(navController = rememberNavController(), idCard = idCard )

    }
}
