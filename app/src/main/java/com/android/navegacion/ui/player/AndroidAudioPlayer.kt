package com.android.navegacion.ui.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.universae.reproductor.domain.usecases.AudioPlayerUseCases
import com.universae.reproductor.domain.entities.tema.Tema

class AndroidAudioPlayer(private val context: Context) : AudioPlayerUseCases {
    // Variable para almacenar la instancia de ExoPlayer
    private var exoPlayer: ExoPlayer? = null

    // Método para reproducir un tema
    override fun play(tema: Tema) {
        // Detiene cualquier audio que se esté reproduciendo
        stop()
        // Crea un MediaItem a partir de la URL del audio del tema
        val mediaItem = MediaItem.fromUri(tema.audioUrl)
        // Inicializa ExoPlayer
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            // Establece el MediaItem a reproducir
            setMediaItem(mediaItem)
            // Prepara el reproductor
            prepare()
            // Inicia la reproducción
            play()
        }
    }

    // Método para detener la reproducción
    override fun stop() {
        // Si ExoPlayer no es nulo, detiene y libera la instancia
        exoPlayer?.let {
            it.stop()
            it.release()
        }
        // Establece ExoPlayer a nulo
        exoPlayer = null
    }

    // Método para pausar la reproducción
    override fun pausa() {
        // Si ExoPlayer no es nulo, pausa la reproducción
        exoPlayer?.pause()
    }

    // Método para continuar la reproducción
    override fun continuar() {
        // Si ExoPlayer no es nulo, reanuda la reproducción
        exoPlayer?.play()
    }

    // Método para reproducir una muestra de MP3
    fun playSample() {
        // Detiene cualquier audio que se esté reproduciendo
        exoPlayer?.release()
        exoPlayer = null

        // Crea un MediaItem que representa la muestra de MP3
        val mediaItem = MediaItem.fromUri("https://file-examples.com/storage/fe92070d83663e82d92ecf7/2017/11/file_example_MP3_700KB.mp3")

        // Inicializa ExoPlayer
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            // Establece el MediaItem a reproducir
            setMediaItem(mediaItem)
            // Prepara el reproductor
            prepare()
            // Inicia la reproducción
            play()
        }
    }
}