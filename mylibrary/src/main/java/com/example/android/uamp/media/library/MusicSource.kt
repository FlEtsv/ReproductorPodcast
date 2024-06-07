package com.example.android.uamp.media.library

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.IntDef
import androidx.media3.common.MediaItem
import com.example.android.uamp.media.extensions.containsCaseInsensitive

/**
 * Interface que define una fuente de música, permitiendo iterar sobre elementos de medios,
 * cargarlos, verificar su estado de disponibilidad y realizar búsquedas.
 */
interface MusicSource : Iterable<MediaItem> {

    /**
     * Carga la fuente de música.
     */
    suspend fun load()

    /**
     * Realiza una acción cuando la fuente de música esté lista.
     *
     * @param performAction La acción a realizar.
     * @return Verdadero si la acción se realizó inmediatamente, falso si se realizará más tarde.
     */
    fun whenReady(performAction: (Boolean) -> Unit): Boolean

    /**
     * Busca elementos de música que coincidan con la consulta dada.
     *
     * @param query La consulta de búsqueda.
     * @param extras Datos adicionales para enfocar la búsqueda.
     * @return Una lista de elementos de medios que coincidan con la consulta.
     */
    fun search(query: String, extras: Bundle): List<MediaItem>
}

/**
 * Anotación que define los posibles estados de la fuente de música.
 */
@IntDef(
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
)
@Retention(AnnotationRetention.SOURCE)
annotation class State

const val STATE_CREATED = 1
const val STATE_INITIALIZING = 2
const val STATE_INITIALIZED = 3
const val STATE_ERROR = 4

/**
 * Clase abstracta que implementa la interfaz MusicSource y maneja los estados de la fuente de música.
 */
abstract class AbstractMusicSource : MusicSource {
    @State
    var state: Int = STATE_CREATED
        set(value) {
            if (value == STATE_INITIALIZED || value == STATE_ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == STATE_INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    override fun whenReady(performAction: (Boolean) -> Unit): Boolean =
        when (state) {
            STATE_CREATED, STATE_INITIALIZING -> {
                onReadyListeners += performAction
                false
            }

            else -> {
                performAction(state != STATE_ERROR)
                true
            }
        }

    override fun search(query: String, extras: Bundle): List<MediaItem> {
        val focusSearchResult = when (extras[MediaStore.EXTRA_MEDIA_FOCUS]) {
            MediaStore.Audio.Genres.ENTRY_CONTENT_TYPE -> {
                val genre = extras[EXTRA_MEDIA_GENRE]
                filter { song ->
                    song.mediaMetadata.genre?.toString() == genre
                }
            }

            MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE -> {
                val artist = extras[MediaStore.EXTRA_MEDIA_ARTIST]
                filter { song ->
                    isArtist(song, artist)
                }
            }

            MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE -> {
                val artist = extras[MediaStore.EXTRA_MEDIA_ARTIST]
                val album = extras[MediaStore.EXTRA_MEDIA_ALBUM]
                filter { song ->
                    (isArtist(song, artist) && song.mediaMetadata.albumTitle?.toString() == album)
                }
            }

            MediaStore.Audio.Media.ENTRY_CONTENT_TYPE -> {
                val title = extras[MediaStore.EXTRA_MEDIA_TITLE]
                val album = extras[MediaStore.EXTRA_MEDIA_ALBUM]
                val artist = extras[MediaStore.EXTRA_MEDIA_ARTIST]
                filter { song ->
                    isArtist(song, artist)
                            && song.mediaMetadata.albumTitle?.toString() == album
                            && song.mediaMetadata.title?.toString() == title
                }
            }

            else -> {
                emptyList()
            }
        }

        if (focusSearchResult.isEmpty()) {
            return if (query.isNotBlank()) {
                filter { song ->
                    song.mediaMetadata.title?.toString().containsCaseInsensitive(query)
                            || song.mediaMetadata.genre?.toString().containsCaseInsensitive(query)
                            || song.mediaMetadata.albumTitle?.toString()
                        .containsCaseInsensitive(query)
                }
            } else {
                return shuffled()
            }
        } else {
            return focusSearchResult
        }
    }

    private val EXTRA_MEDIA_GENRE
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MediaStore.EXTRA_MEDIA_GENRE
        } else {
            "android.intent.extra.genre"
        }
}

/**
 * Comprueba si el artista de un MediaItem coincide con el artista dado.
 *
 * @param mediaItem El elemento de medios a comprobar.
 * @param artist El artista a comparar.
 * @return Verdadero si el artista coincide, falso en caso contrario.
 */
fun isArtist(mediaItem: MediaItem, artist: Any?): Boolean {
    return mediaItem.mediaMetadata.artist?.toString() == artist
            || mediaItem.mediaMetadata.albumArtist?.toString() == artist
}

private const val TAG = "FuenteDeMusica"
