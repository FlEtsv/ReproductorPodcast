package com.example.android.uamp.media.library

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.IntDef
import androidx.media3.common.MediaItem
import com.example.android.uamp.media.extensions.containsCaseInsensitive

interface MusicSource : Iterable<MediaItem> {

    suspend fun load()

    fun whenReady(performAction: (Boolean) -> Unit): Boolean

    fun search(query: String, extras: Bundle): List<MediaItem>
}

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
                Log.d(TAG, "Búsqueda enfocada por género: '$genre'")
                filter { song ->
                    song.mediaMetadata.genre?.toString() == genre
                }
            }
            MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE -> {
                val artist = extras[MediaStore.EXTRA_MEDIA_ARTIST]
                Log.d(TAG, "Búsqueda enfocada por artista: '$artist'")
                filter { song ->
                    isArtist(song, artist)
                }
            }
            MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE -> {
                val artist = extras[MediaStore.EXTRA_MEDIA_ARTIST]
                val album = extras[MediaStore.EXTRA_MEDIA_ALBUM]
                Log.d(TAG, "Búsqueda enfocada por álbum: álbum='$album' artista='$artist'")
                filter { song ->
                    (isArtist(song, artist) && song.mediaMetadata.albumTitle?.toString() == album)
                }
            }
            MediaStore.Audio.Media.ENTRY_CONTENT_TYPE -> {
                val title = extras[MediaStore.EXTRA_MEDIA_TITLE]
                val album = extras[MediaStore.EXTRA_MEDIA_ALBUM]
                val artist = extras[MediaStore.EXTRA_MEDIA_ARTIST]
                Log.d(TAG, "Búsqueda enfocada por media: título='$title' álbum='$album' artista='$artist'")
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
                Log.d(TAG, "Búsqueda no enfocada por '$query'")
                filter { song -> // TODO: agregar los casos de busqueda deseados. Ahora busca por titulo, genero y album
                    song.mediaMetadata.title?.toString().containsCaseInsensitive(query)
                            || song.mediaMetadata.genre?.toString().containsCaseInsensitive(query)
                            || song.mediaMetadata.albumTitle?.toString().containsCaseInsensitive(query)
                }
            } else {
                Log.d(TAG, "Búsqueda no enfocada sin palabra clave")
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

fun isArtist(mediaItem: MediaItem, artist: Any?): Boolean {
    return mediaItem.mediaMetadata.artist?.toString() == artist
            || mediaItem.mediaMetadata.albumArtist?.toString() == artist
}

private const val TAG = "FuenteDeMusica"
