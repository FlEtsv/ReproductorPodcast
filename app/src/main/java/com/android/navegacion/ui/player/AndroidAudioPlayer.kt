package com.universae.reproductor.ui.player

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.universae.reproductor.domain.usecases.AudioPlayerUseCases
import com.universae.reproductor.domain.entities.tema.Tema

class AndroidAudioPlayer(private val context: Context) : AudioPlayerUseCases {
    private var mediaPlayer: MediaPlayer? = null
    // TODO: guardar el tema actual para poder continuar la reproducción
    // TODO: comprobar si el tema a reproducir tiene punto de parada y continuar desde ahí

    override fun play(tema: Tema) {
        stop() // para cualquier audio que se esté reproduciendo
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            setDataSource(tema.audioUrl)
            prepareAsync() // asincrono, se ejecuta en segundo plano
            setOnPreparedListener { // se ejecuta cuando el audio está listo para reproducirse
                start()
            }
        }
    }

    override fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun pausa() {
        mediaPlayer?.pause()
    }

    override fun continuar() {
        mediaPlayer?.start()
    }
}