package com.universae.navegacion.player

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.example.android.uamp.common.MusicServiceConnection
import com.example.android.uamp.media.MusicService
import com.example.android.uamp.media.extensions.isEnded
import com.example.android.uamp.media.extensions.isPlayEnabled
import com.universae.reproductor.domain.entities.tema.Tema
import com.universae.reproductor.domain.usecases.AudioPlayerUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AndroidAudioPlayer(private val context: Context) : AudioPlayerUseCases {
    private val serviceComponent = ComponentName(context, MusicService::class.java)
    private val musicServiceConnection =
        MusicServiceConnection.getInstance(context.applicationContext, serviceComponent)

    override fun reproducir(
        tema: Tema,
        pauseThenPlaying: Boolean?,
        parentMediaId: String?
    ): Boolean {
        Log.e(TAG, "reproducir")
        val nowPlaying = musicServiceConnection.nowPlaying.value
        val player = musicServiceConnection.player ?: return false

        val isPrepared = player.playbackState != Player.STATE_IDLE
        if (isPrepared && tema.temaId.id.toString() == nowPlaying?.mediaId) {
            Log.e(TAG, "en el iffffffffffffffffffffffff")
            when {
                player.isPlaying ->
                    if (pauseThenPlaying!!) player.pause() else Unit

                player.isPlayEnabled -> player.play()
                player.isEnded -> player.seekTo(C.TIME_UNSET)
                else -> {
                    Log.w(
                        TAG, "Playable item clicked but neither play nor pause are enabled!" +
                                " (temaId= ${tema.temaId.id})" +
                                " (nombreTema=${tema.nombreTema})"
                    )
                }
            }
        } else {
            Log.e(TAG, "en el else")
            CoroutineScope(Dispatchers.Main).launch {

                val mediaItem: MediaItem = musicServiceConnection.getMediaItemByMediaId(tema.temaId.id.toString())!!

                var playlist: MutableList<MediaItem> = arrayListOf()
                // load the children of the parent if requested
                parentMediaId?.let {
                    playlist = musicServiceConnection.getChildren(parentMediaId).let { children ->
                        children.filter {
                            it.mediaMetadata.isPlayable ?: false
                        }
                    }.toMutableList()
                }
                if (playlist.isEmpty()) {
                    playlist.add(mediaItem)
                }
                val indexOf = playlist.indexOf(mediaItem)
                val startWindowIndex = if (indexOf >= 0) indexOf else 0
                player.setMediaItems(
                    playlist, startWindowIndex, /* startPositionMs= */ C.TIME_UNSET
                )
                player.prepare()
                player.play()
            }
        }
        return true
    }

    override fun reproducir(temas: List<Tema>) {
        // Similar a reproducir(tema: Tema), pero para una lista de temas
    }

    override fun stop() {
        // Enviar comando de stop a MyMediaSessionService a través de MusicServiceConnection
    }

    override fun pausa() {
        // implementar
    }

    override fun continuar() {
        // Enviar comando de play a MyMediaSessionService a través de MusicServiceConnection
    }

    override fun adelantarDiezSegundos() {
        musicServiceConnection.player?.let { player ->
            val newPosition = player.currentPosition + 10000
            player.seekTo(newPosition.coerceAtMost(player.duration))
        }
    }

    override fun retrocederDiezSegundos() {
        musicServiceConnection.player?.let { player ->
            val newPosition = player.currentPosition - 10000
            player.seekTo(newPosition.coerceAtLeast(0))
        }
    }

    override fun siguienteTema() {
        musicServiceConnection.player?.let { player ->
            if (player.hasNextMediaItem()) {
                player.seekToNextMediaItem()
            }
        }
    }

    override fun temaAnterior() {
        musicServiceConnection.player?.let { player ->
            if (player.hasPreviousMediaItem()) {
                player.seekToPrevious()
            }
        }
    }

    override fun isPlaying(): Boolean {
        return if ((musicServiceConnection.player?.mediaItemCount
                ?: -1) > 0 && musicServiceConnection.player?.isPlaying == true
        ) true else false
    }
}

private const val TAG = "AndroidAudioPlayer"