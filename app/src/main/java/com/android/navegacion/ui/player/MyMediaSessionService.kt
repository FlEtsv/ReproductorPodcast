package com.android.navegacion.ui.player

import android.content.Intent
import androidx.media3.common.Player

import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

/**
 * Esta es una clase de servicio que maneja la sesión de medios para la reproducción de audio.
 * Hereda de MediaSessionService.
 */
class MyMediaSessionService : MediaSessionService() {

    // MediaSession es una clase que permite controlar la reproducción de medios.
    private lateinit var mediaSession: MediaSession

    // AndroidAudioPlayer es una clase personalizada para la reproducción de audio.
    private lateinit var audioPlayer: AndroidAudioPlayer

    // Este es un objeto de devolución de llamada que escucha los cambios en el estado de reproducción del reproductor.
    private val mediaSessionCallback = object : Player.Listener {
        // Este método se llama cuando cambia el estado de reproducción del reproductor.
        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            // TODO: Manejar los comandos de reproducción y pausa
        }

        // TODO: Implementar otros comandos de reproducción
    }

    /**
     * Este método se llama cuando se crea el servicio.
     *
     * En este método, se realizan las siguientes operaciones:
     * 1. Se inicializa el AndroidAudioPlayer.
     * 2. Se crea una nueva MediaSession.
     * 3. Se establece la devolución de llamada en la MediaSession.
     * 4. Se agrega el listener a la instancia de Player.
     */
    override fun onCreate() {
        super.onCreate()
        audioPlayer = AndroidAudioPlayer(this)
        mediaSession =
            MediaSession.Builder(/*context=*/ this, /*player=*/ audioPlayer.player).build()
        mediaSession.setPlayer(audioPlayer.player)
        audioPlayer.player.addListener(mediaSessionCallback)
    }

    /**
     * Este método se llama cuando se solicita una MediaSession.
     *
     * En este método, se devuelve la MediaSession que se creó en onCreate().
     */
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }

    /**
     * Este método se llama cuando se destruye la tarea de la aplicación.
     *
     * En este método, se detiene la reproducción y se liberan los recursos.
     */
    override fun onTaskRemoved(rootIntent: Intent) {
        mediaSession.player.stop()
        mediaSession.player.release()
        stopSelf()
    }

    /**
     * Este método se llama cuando se destruye el servicio.
     *
     * En este método, se libera el reproductor de audio.
     */
    override fun onDestroy() {
        mediaSession.player.release()
        super.onDestroy()
    }
}