package com.universae.navegacion.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.navegacion.components.iconArrowBack
import com.android.navegacion.components.iconArrowForward
import com.android.navegacion.components.iconCast
import com.android.navegacion.components.iconFastForward
import com.android.navegacion.components.iconFastReward
import com.android.navegacion.components.iconPause
import com.android.navegacion.components.iconPlay
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domain.entities.tema.TemaId
import com.universae.reproductor.ui.theme.AzulClaro
import com.universae.reproductor.ui.theme.AzulOscuro
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun ReproductorPodcast(navController: NavController, tituloTema: String) {
    var reproduciendo by remember { mutableStateOf(false) }
    val progress = remember { mutableStateOf(0.0f) }
    // crea instancia audioPlayer e inicializa el controlador de reproducción mediante el composable AndroidAudioPlayerComposable
    val context = LocalContext.current
    val audioPlayer = remember { AndroidAudioPlayer(context) }


    // Replace with your actual Tema instance
    //TODO("Reemplazar con tu instancia real de Tema")
    val tema = Tema(
        temaId = TemaId(21),
        nombreTema = "nombreTema",
        descripcionTema = "descripciontema",
        duracionAudio = 120.toDuration(DurationUnit.SECONDS),
        audioUrl = "https://file-examples.com/storage/fe54a1a6c9664b5d793d4bf/2017/11/file_example_MP3_700KB.mp3"
    )


    // Interfaz de usuario del reproductor de podcast
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    ) // Ajusta el contenido de descripción según sea necesario
                }
            }

            Column(
                modifier = Modifier.weight(1f), // Ocupa la otra mitad del ancho disponible
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                BotonCompartir()
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
        PortadaPodcast()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = tituloTema,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProgressBarRow(progress = progress)
        Spacer(modifier = Modifier.height(16.dp))
        ControlesReproduccion(
            reproduciendo = reproduciendo,
            onReproduccionPausaToggle = {
                if (reproduciendo) {
                    audioPlayer.pausa()
                } else {
                    /*
                    if (audioPlayer.controller?.playbackState == Player.STATE_READY && audioPlayer.controller?.playWhenReady?.not() == true) {
                        audioPlayer.continuar()
                    } else {

                     */
                        audioPlayer.reproducir(tema)
                    //}
                }
                reproduciendo = !reproduciendo
            },
            onRetroceder = { /* TODO: Retroceder */ },
            onAvanzar = { /* TODO: Avanzar */ },
            onAvanzarRapido = { },
            onBajarVelocidad = {
                //
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
    Spacer(modifier = Modifier.height(16.dp))
}


@Composable
fun PortadaPodcast() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape)
            .background(AzulOscuro),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = "Portada del Podcast",
            modifier = Modifier.size(150.dp),
            tint = AzulClaro
        )
    }
}

@Composable
fun ControlesReproduccion(
    reproduciendo: Boolean,
    onReproduccionPausaToggle: () -> Unit,
    onRetroceder: () -> Unit,
    onAvanzar: () -> Unit,
    onAvanzarRapido: () -> Unit,
    onBajarVelocidad: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onBajarVelocidad }) {
            Icon(
                imageVector = iconFastReward(),
                contentDescription = "Retroceder rápidamente",
                tint = Color.White
            )
        }

        // Botón para volver a la pista anterior
        IconButton(onClick = {

        }) {
            Icon(
                imageVector = iconArrowBack(),
                contentDescription = "Pista anterior",
                tint = Color.White
            )
        }

        // Botón de reproducción/pausa
        IconButton(onClick = {
            onReproduccionPausaToggle()
        }) {
            Icon(
                imageVector = if (reproduciendo) iconPause() else iconPlay(),
                contentDescription = if (reproduciendo) "Pausa" else "Reproducir",
                tint = Color.White
            )
        }

        // Botón para avanzar 5 segundos en la pista actual
        IconButton(onClick = {

        }) {
            Icon(
                imageVector = iconArrowForward(),
                contentDescription = "Avanzar rápidamente",
                tint = Color.White
            )
        }

        // Botón para pasar a la siguiente pista
        IconButton(onClick = {

        }) {
            Icon(
                imageVector = iconFastForward(),
                contentDescription = "Siguiente pista",
                tint = Color.White
            )
        }
    }
}

@Composable
fun BotonCompartir() {
    IconButton(
        onClick = { /* TODO: Lógica para compartir con Android Auto */ }
    ) {
        Icon(
            imageVector = iconCast(),
            contentDescription = "Compartir",
            tint = Color.White
        )
    }
}

@Composable
fun ProgressBarRow(progress: MutableState<Float>) {  // 'progress' debería ser un valor entre 0f y 1f
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), content = {
            LinearProgressIndicator(
                progress = { progress.value },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
            )
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewPodcast() {
    var idTitulo: String = "titulo del Podcast"
    ReproductorPodcast(navController = rememberNavController(), idTitulo)
}