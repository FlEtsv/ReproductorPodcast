package com.universae.navegacion.player

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import com.universae.audioplayerlibrary.common.MusicServiceConnection
import com.universae.audioplayerlibrary.media.MyMediaSessionService
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domain.usecases.AudioPlayerUseCases

class AndroidAudioPlayer(context: Context) : AudioPlayerUseCases {
    private val serviceComponent = ComponentName(context, MyMediaSessionService::class.java)
    private val musicServiceConnection = MusicServiceConnection(context, serviceComponent)

    override fun reproducir(tema: Tema) {
        // Convertir el ID del tema a String, que es el mediaId
        val mediaId = tema.temaId.id.toString()

        // Utilizar MusicServiceConnection para obtener el MediaItem correspondiente
        val mediaItemFuture = musicServiceConnection.browser!!.getItem(mediaId)
        mediaItemFuture.addListener(
            Runnable {
                val fullMediaItem = mediaItemFuture.get().value ?: return@Runnable

                // Una vez obtenido el MediaItem, enviarlo para reproducción
                musicServiceConnection.transportControls.playFromMediaId(fullMediaItem.mediaId, null)
            },
            MoreExecutors.directExecutor()
        )
    }

    override fun reproducir(temas: List<Tema>) {
        // Similar a reproducir(tema: Tema), pero para una lista de temas
    }

    override fun stop() {
        // Enviar comando de stop a MyMediaSessionService a través de MusicServiceConnection
    }

    override fun pausa() {
        // Enviar comando de pausa a MyMediaSessionService a través de MusicServiceConnection
    }

    override fun continuar() {
        // Enviar comando de play a MyMediaSessionService a través de MusicServiceConnection
    }

    override fun adelantarDiezSegundos() {
        // Implementar lógica para adelantar diez segundos en la reproducción actual
    }

    override fun retrocederDiezSegundos() {
        // Implementar lógica para retroceder diez segundos en la reproducción actual
    }

    override fun siguienteTema() {
        // Enviar comando para reproducir el siguiente tema a MyMediaSessionService a través de MusicServiceConnection
    }

    override fun temaAnterior() {
        // Enviar comando para reproducir el tema anterior a MyMediaSessionService a través de MusicServiceConnection
    }
}