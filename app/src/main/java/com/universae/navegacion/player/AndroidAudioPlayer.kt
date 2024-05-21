package com.universae.navegacion.player

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import com.universae.audioplayerlibrary.common.MusicServiceConnection
import com.universae.audioplayerlibrary.media.MyMediaSessionService
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domain.usecases.AudioPlayerUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch

class AndroidAudioPlayer(private val context: Context) : AudioPlayerUseCases {
    private val serviceComponent = ComponentName(context, MyMediaSessionService::class.java)
    private val musicServiceConnection = MusicServiceConnection(context, serviceComponent)

    override fun reproducir(tema: Tema) {
        val mediaId = tema.temaId.id.toString()
        musicServiceConnection.playMediaItem(mediaId)
    }

    override fun reproducir(temas: List<Tema>) {
        // Similar a reproducir(tema: Tema), pero para una lista de temas
    }

    override fun stop() {
        // Enviar comando de stop a MyMediaSessionService a través de MusicServiceConnection
    }

    override fun pausa() {
        musicServiceConnection.pause()
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