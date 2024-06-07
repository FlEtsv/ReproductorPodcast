package com.universae.navegacion.player

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.example.android.uamp.common.MusicServiceConnection
import com.example.android.uamp.media.MusicService
import com.example.android.uamp.media.extensions.isEnded
import com.example.android.uamp.media.extensions.isPlayEnabled
import com.universae.domain.entities.tema.Tema
import com.universae.domain.usecases.AudioPlayerUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Clase AndroidAudioPlayer que implementa la interfaz AudioPlayerUseCases.
 * Esta clase se encarga de la reproducción de audio en la aplicación.
 *
 * @param context Contexto de la aplicación.
 */
class AndroidAudioPlayer(private val context: Context) : AudioPlayerUseCases {
    // Componente del servicio de música
    private val serviceComponent = ComponentName(context, MusicService::class.java)

    // Conexión con el servicio de música
    private val musicServiceConnection =
        MusicServiceConnection.getInstance(context.applicationContext, serviceComponent)

    /**
     * Método para reproducir un tema.
     *
     * @param tema El tema a reproducir.
     * @param pauseThenPlaying Si se debe pausar antes de reproducir.
     * @param parentMediaId El ID del medio padre.
     * @return Boolean Indica si la reproducción fue exitosa.
     */
    override fun reproducir(
        tema: Tema,
        pauseThenPlaying: Boolean?,
        parentMediaId: String?
    ): Boolean {
        val nowPlaying = musicServiceConnection.nowPlaying.value
        val player = musicServiceConnection.player ?: return false

        val isPrepared = player.playbackState != Player.STATE_IDLE
        if (isPrepared && tema.temaId.id.toString() == nowPlaying?.mediaId) {
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
            CoroutineScope(Dispatchers.Main).launch {

                val mediaItem: MediaItem =
                    musicServiceConnection.getMediaItemByMediaId(tema.temaId.id.toString())!!

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

    /**
     * Método para reproducir una lista de temas.
     *
     * @param temas La lista de temas a reproducir.
     */
    override fun reproducir(temas: List<Tema>) {
        // Implementación del método reproducir para una lista de temas
    }

    /**
     * Método para detener la reproducción.
     */
    override fun stop() {
        // Implementación del método stop
    }

    /**
     * Método para pausar la reproducción.
     */
    override fun pausa() {
        // Implementación del método pausa
    }

    /**
     * Método para continuar la reproducción.
     */
    override fun continuar() {
        // Implementación del método continuar
    }

    /**
     * Método para adelantar diez segundos en la reproducción.
     */
    override fun adelantarDiezSegundos() {
        musicServiceConnection.player?.let { player ->
            val newPosition = player.currentPosition + 10000
            player.seekTo(newPosition.coerceAtMost(player.duration))
        }
    }

    /**
     * Método para retroceder diez segundos en la reproducción.
     */
    override fun retrocederDiezSegundos() {
        musicServiceConnection.player?.let { player ->
            val newPosition = player.currentPosition - 10000
            player.seekTo(newPosition.coerceAtLeast(0))
        }
    }

    /**
     * Método para pasar al siguiente tema en la lista de reproducción.
     */
    override fun siguienteTema() {
        musicServiceConnection.player?.let { player ->
            if (player.hasNextMediaItem()) {
                player.seekToNextMediaItem()
            }
        }
    }

    /**
     * Método para pasar al tema anterior en la lista de reproducción.
     */
    override fun temaAnterior() {
        musicServiceConnection.player?.let { player ->
            if (player.hasPreviousMediaItem()) {
                player.seekToPrevious()
            }
        }
    }

    /**
     * Método para verificar si la reproducción está en curso.
     *
     * @return Boolean Indica si la reproducción está en curso.
     */
    override fun isPlaying(): Boolean {
        return (musicServiceConnection.player?.mediaItemCount
            ?: -1) > 0 && musicServiceConnection.player?.isPlaying == true
    }
}

private const val TAG = "AndroidAudioPlayer"