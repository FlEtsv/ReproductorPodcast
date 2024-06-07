package com.universae.navegacion.views

import android.content.ComponentName
import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import com.android.navegacion.R
import com.android.navegacion.components.arrowForwardTenSec
import com.android.navegacion.components.iconBackwardTenSec
import com.android.navegacion.components.iconCast
import com.android.navegacion.components.iconFastForward
import com.android.navegacion.components.iconFastReward
import com.android.navegacion.components.iconPause
import com.android.navegacion.components.iconPlay
import com.example.android.uamp.common.MusicServiceConnection
import com.universae.navegacion.player.AndroidAudioPlayer
import com.universae.reproductor.domain.usecases.AsignaturaUseCasesImpl
import com.universae.reproductor.domain.usecases.TemaUseCasesImpl
import com.universae.navegacion.theme.AzulClaro
import com.universae.navegacion.theme.AzulOscuro
import com.universae.navegacion.theme.gradientBackground
import kotlin.math.sqrt
import androidx.compose.runtime.livedata.observeAsState
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.android.uamp.common.EMPTY_PLAYBACK_STATE
import com.example.android.uamp.common.PlaybackState
import com.example.android.uamp.media.MusicService
import com.example.android.uamp.media.extensions.isEnded
import com.example.android.uamp.media.extensions.isPlayEnabled
import com.universae.navegacion.theme.Blanco
import com.universae.navegacion.theme.ralewayFamily

//TODO: actualizar info en base al servicio de musica para que cambie el composable incluso si usas el reproductor fuera de la vista de la app
@OptIn(UnstableApi::class)
@Composable
fun ReproductorPodcast(navController: NavController, idTema: Int) {

    val context: Context = LocalContext.current
    val musicServiceConnection = remember { MusicServiceConnection(context, ComponentName(context, MusicService::class.java)) }

    val currentMediaItem by musicServiceConnection.nowPlaying.observeAsState(initial = MediaItem.EMPTY)
    val playbackState by musicServiceConnection.playbackState.observeAsState(initial = EMPTY_PLAYBACK_STATE)

    var reproduciendo by remember { mutableStateOf(false) }
    val progress = remember { mutableFloatStateOf(0.0f) }
    // crea instancia audioPlayer e inicializa el controlador de reproducción mediante el composable AndroidAudioPlayerComposable
    val audioPlayer = remember { AndroidAudioPlayer(context) }
    val tema = TemaUseCasesImpl.getTemaById(idTema)!!

    Box(
        modifier = Modifier
            .fillMaxSize()
            .gradientBackground()
    ) {

        // Interfaz de usuario del reproductor de podcast
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
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
                                contentDescription = "Back",
                                tint = Blanco
                            )
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
            }
            item {PortadaPodcast(currentMediaItem.mediaMetadata.artworkUri.toString(), currentMediaItem.mediaMetadata.title.toString()) }
            //Spacer(modifier = Modifier.height(16.dp))
            item{Text(
                text = currentMediaItem.mediaMetadata.title?.toString() ?: tema.nombreTema,
                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = ralewayFamily, color = Blanco)
            )}
            item{ ProgressBarRow(progress = progress) }
            item{ ControlesReproduccion(
                playbackState = playbackState,
                musicServiceConnection = musicServiceConnection,
                reproduciendo = reproduciendo,
                onPlayPauseToggle = {
                    audioPlayer.reproducir(
                        tema = tema,
                        parentMediaId = AsignaturaUseCasesImpl.getNombreAsignaturaByTemaId(tema.temaId)
                    )
                    reproduciendo = !reproduciendo
                },
                onRetrocederTenSecs = { audioPlayer.retrocederDiezSegundos() },
                onNextSong = { audioPlayer.siguienteTema() },
                onAvanzarTenSecs = { audioPlayer.adelantarDiezSegundos() },
                onPreviousSong = { audioPlayer.temaAnterior() }
            )}
        }
    }
}


@Composable
fun PortadaPodcast(temaImagen: String, temaNombre: String) {
    val diameter = 200.dp
    Box(
        modifier = Modifier
            .size(diameter)
            .clip(CircleShape)
            .background(AzulClaro),
        contentAlignment = Alignment.Center
    ) {
        ImageWithColoredPlaceholder(
            imageUrl = temaImagen,
            placeholderRes = R.mipmap.escudo,
            placeholderColor = AzulOscuro,
            modifier = Modifier
                .size(diameter / 2 * sqrt(2f)) // Escala la imagen para que su diagonal sea igual al diámetro del círculo
                .align(Alignment.Center),
            contentDescription = "Icono de $temaNombre",
            padding = 16
        )
    }
}

@Composable
fun ControlesReproduccion(
    playbackState: PlaybackState,
    musicServiceConnection: MusicServiceConnection,
    reproduciendo: Boolean,
    onPlayPauseToggle: () -> Unit,
    onRetrocederTenSecs: () -> Unit,
    onNextSong: () -> Unit,
    onAvanzarTenSecs: () -> Unit,
    onPreviousSong: () -> Unit
) {
    val player = musicServiceConnection.player?: return

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onPreviousSong() }) {
            Icon(
                imageVector = iconFastReward(),
                contentDescription = "Retroceder rápidamente",
                tint = Color.White
            )
        }

        // Botón para volver a la pista anterior
        IconButton(onClick = {
            onRetrocederTenSecs()
        }) {
            Icon(
                imageVector = iconBackwardTenSec(),
                contentDescription = "Pista anterior",
                tint = Color.White
            )
        }

        // Botón de reproducción/pausa
        IconButton(onClick = {
            val isPrepared = player.playbackState != Player.STATE_IDLE
            if (isPrepared) {
                when {
                    player.isPlaying -> player.pause()
                    player.isPlayEnabled -> player.play()
                    player.isEnded -> player.seekTo(C.TIME_UNSET)
                }
            }
        }) {
            Icon(
                imageVector = if (playbackState.isPlaying) iconPause() else iconPlay(),
                contentDescription = if (playbackState.isPlaying) "Pausa" else "Reproducir",
                tint = Color.White
            )
        }

        // Botón para avanzar 5 segundos en la pista actual
        IconButton(onClick = {
            onAvanzarTenSecs()
        }) {
            Icon(
                imageVector = arrowForwardTenSec(),
                contentDescription = "Avanzar rápidamente",
                tint = Color.White
            )
        }

        // Botón para pasar a la siguiente pista
        IconButton(onClick = {
            onNextSong()
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
            tint = Blanco
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