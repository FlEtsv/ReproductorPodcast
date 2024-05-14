package com.android.navegacion.ui.player

import android.content.Context
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.annotation.OptIn
import android.media.AudioAttributes as MediaAndroidAudioAttributes
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlaybackException
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import com.universae.reproductor.domain.usecases.AudioPlayerUseCases
import com.universae.reproductor.domain.entities.tema.Tema

class AndroidAudioPlayer @OptIn(UnstableApi::class) constructor(private val context: Context) : AudioPlayerUseCases {
    private var player: ExoPlayer? = null
    val session: MediaSession
    val controller: MediaController
    private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    init {
        createPlayer()
        session = MediaSession.Builder(context, player!!).build()
        controller = MediaController.Builder(context, session.token).buildAsync().get()
    }

    private fun createPlayer() {
        player = ExoPlayer.Builder(context).build()
        // Add your player listener here
        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                when (state) {
                    Player.STATE_IDLE -> {
                        println("Player state: IDLE")
                    }
                    Player.STATE_BUFFERING -> {
                        println("Player state: BUFFERING")
                    }
                    Player.STATE_READY -> {
                        println("Player state: READY")
                    }
                    Player.STATE_ENDED -> {
                        println("Player state: ENDED")
                    }
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                println("Player error: ${error.message}")
            }
        })
    }

    override fun play(tema: Tema) {
        //stop()
        if (player == null) {
            createPlayer()
        }

        val mediaItem: MediaItem = MediaItem.fromUri(tema.audioUrl)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.playWhenReady = true
        //player?.play()
        controller.play()
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }

    override fun pausa() {
        player?.pause()
    }

    override fun continuar() {
        player?.play()
    }

    private fun requestAudioFocus() {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        val focusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> player?.playWhenReady = true
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT, AudioManager.AUDIOFOCUS_LOSS -> player?.playWhenReady = false
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
            audioManager.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        }
    }

    fun getPlayer(): ExoPlayer? {
        return player
    }

}