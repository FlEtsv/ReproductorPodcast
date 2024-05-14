package com.android.navegacion.ui.player

import android.content.Context
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.Commands
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domain.usecases.AudioPlayerUseCases
import android.media.AudioAttributes as MediaAndroidAudioAttributes

/**
 * AndroidAudioPlayer es una clase que implementa la interfaz AudioPlayerUseCases.
 * Proporciona funcionalidad para reproducir audio en un entorno Android.
 *
 * @property context El contexto en el que opera el reproductor de audio.
 * @property player La instancia de ExoPlayer utilizada para la reproducción de audio.
 * @property session La instancia de MediaSession utilizada para controlar la reproducción de medios.
 * @property controller La instancia de MediaController utilizada para controlar la reproducción de medios.
 * @property audioManager La instancia de AudioManager utilizada para gestionar el enfoque de audio.
 */
class AndroidAudioPlayer(private val context: Context) : AudioPlayerUseCases {

    lateinit var player: ExoPlayer
    val session: MediaSession
    val controller: MediaController
    private val audioManager: AudioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    /**
     * Inicializa el reproductor, la sesión y el controlador.
     */
    init {
        createPlayer()
        session = MediaSession.Builder(context, player).build()
        controller = MediaController.Builder(context, session.token).buildAsync().get()
    }

    /**
     * Reproduce un solo elemento de audio.
     *
     * @param tema El elemento de audio a reproducir.
     */
    override fun reproducir(tema: Tema) {
        val mediaItem: MediaItem = MediaItem.fromUri(tema.audioUrl)
        controller.setMediaItem(mediaItem)
        controller.prepare()
        requestAudioFocus() // Solicita el enfoque de audio antes de reproducir
        controller.play()
    }

    /**
     * Reproduce una lista de elementos de audio.
     *
     * @param temas La lista de elementos de audio a reproducir.
     */
    override fun reproducir(temas: List<Tema>) {
        val mediaItems = temas.map { MediaItem.fromUri(it.audioUrl) }
        controller.setMediaItems(mediaItems)
        controller.prepare()
        requestAudioFocus()
        controller.play()
    }

    /**
     * Detiene la reproducción de audio y libera los recursos.
     */
    override fun stop() {
        controller.stop()
        controller.release()
    }

    /**
     * Pausa la reproducción de audio.
     */
    override fun pausa() {
        controller.pause()
    }

    /**
     * Reanuda la reproducción de audio.
     */
    override fun continuar() {
        controller.play()
    }

    /**
     * Avanza la reproducción de audio en diez segundos.
     */
    override fun adelantarDiezSegundos() {
        controller.seekTo(controller.currentPosition + 10000)
    }

    /**
     * Retrocede la reproducción de audio en diez segundos.
     */
    override fun retrocederDiezSegundos() {
        controller.seekTo(controller.currentPosition - 10000)
    }

    /**
     * Reproduce el siguiente elemento de audio.
     */
    override fun siguienteTema() {
        val commands: Commands = controller.availableCommands
        if (commands.contains(MediaController.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM)) {
            controller.seekToNextMediaItem()
        } else {
            controller.seekTo(controller.duration)
        }
    }

    /**
     * Reproduce el elemento de audio anterior.
     */
    override fun temaAnterior() {
        val commands: Commands = controller.availableCommands
        if (commands.contains(MediaController.COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM)) {
            controller.seekToPreviousMediaItem()
        } else {
            controller.seekTo(0L)
        }
    }

    /**
     * Solicita el enfoque de audio.
     */
    private fun requestAudioFocus() {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        val focusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> player.playWhenReady = true
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT, AudioManager.AUDIOFOCUS_LOSS -> player.playWhenReady =
                    false
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mediaAudioAttributes = MediaAndroidAudioAttributes.Builder()
                .setContentType(MediaAndroidAudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(mediaAudioAttributes)
                .setOnAudioFocusChangeListener(focusChangeListener)
                .build()

            audioManager.requestAudioFocus(focusRequest)
        } else {
            @Suppress("DEPRECATION")
            audioManager.requestAudioFocus(
                focusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }
    }

    /**
     * Objeto complementario que contiene una instancia singleton de ExoPlayer.
     */
    companion object {
        private var playerInstance: ExoPlayer? = null
    }

    /**
     * Crea una instancia de ExoPlayer si no existe.
     */
    private fun createPlayer() {
        if (playerInstance == null) {
            playerInstance = ExoPlayer.Builder(context).build()
        }
        player = playerInstance!!
    }
}