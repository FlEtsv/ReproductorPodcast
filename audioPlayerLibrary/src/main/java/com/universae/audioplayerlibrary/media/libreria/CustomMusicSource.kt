package com.universae.audioplayerlibrary.media.libreria

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.media3.common.MediaItem
import com.universae.audioplayerlibrary.media.extensiones.containsCaseInsensitive
import com.universae.audioplayerlibrary.media.library.MusicSource
import com.universae.audioplayerlibrary.media.library.isArtist

abstract class CustomMusicSource : MusicSource {

    private var state: Int = STATE_CREATED
    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    override suspend fun load() {
        state = STATE_INITIALIZED
        notifyWhenReady(state == STATE_INITIALIZED)
    }

    override fun whenReady(performAction: (Boolean) -> Unit): Boolean {
        if (state == STATE_INITIALIZED || state == STATE_ERROR) {
            performAction(state == STATE_INITIALIZED)
            return true
        } else {
            onReadyListeners.add(performAction)
            return false
        }
    }

    private fun notifyWhenReady(ready: Boolean) {
        onReadyListeners.forEach { it(ready) }
    }

//TODO("Repasar la busqueda de mediaItems. Cual es el metadata que tenemos y queremos buscar??")
    /**
     * Handles searching a [MusicSource] from a focused voice search, often coming
     * from the Google Assistant.
     */
    override fun search(query: String, extras: Bundle): List<MediaItem> {
        // First attempt to search with the "focus" that's provided in the extras.
        val focusSearchResult = when (extras[MediaStore.EXTRA_MEDIA_FOCUS]) {
            MediaStore.Audio.Genres.ENTRY_CONTENT_TYPE -> {
                // For a Genre focused search, only genre is set.
                val genre = extras[EXTRA_MEDIA_GENRE]
                Log.d(TAG, "Focused genre search: '$genre'")
                filter { song ->
                    song.mediaMetadata.genre?.toString() == genre
                }
            }
            MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE -> {
                // For an Artist focused search, only the artist is set.
                val artist = extras[MediaStore.EXTRA_MEDIA_ARTIST]
                Log.d(TAG, "Focused artist search: '$artist'")
                filter { song ->
                    isArtist(song, artist)
                }
            }
            MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE -> {
                // For an Album focused search, album and artist are set.
                val artist = extras[MediaStore.EXTRA_MEDIA_ARTIST]
                val album = extras[MediaStore.EXTRA_MEDIA_ALBUM]
                Log.d(TAG, "Focused album search: album='$album' artist='$artist")
                filter { song ->
                    (isArtist(song, artist) && song.mediaMetadata.albumTitle?.toString() == album)
                }
            }
            MediaStore.Audio.Media.ENTRY_CONTENT_TYPE -> {
                // For a Song (aka Media) focused search, title, album, and artist are set.
                val title = extras[MediaStore.EXTRA_MEDIA_TITLE]
                val album = extras[MediaStore.EXTRA_MEDIA_ALBUM]
                val artist = extras[MediaStore.EXTRA_MEDIA_ARTIST]
                Log.d(TAG, "Focused media search: title='$title' album='$album' artist='$artist")
                filter { song ->
                    isArtist(song, artist)
                            && song.mediaMetadata.albumTitle?.toString() == album
                            && song.mediaMetadata.title?.toString() == title
                }
            }
            else -> {
                // There isn't a focus, so no results yet.
                emptyList()
            }
        }

        // If there weren't any results from the focused search (or if there wasn't a focus
        // to begin with), try to find any matches given the 'query' provided, searching against
        // a few of the fields.
        // In this sample, we're just checking a few fields with the provided query, but in a
        // more complex app, more logic could be used to find fuzzy matches, etc...
        if (focusSearchResult.isEmpty()) {
            return if (query.isNotBlank()) {
                Log.d(TAG, "Unfocused search for '$query'")
                filter { song ->
                    song.mediaMetadata.title?.toString().containsCaseInsensitive(query)
                            || song.mediaMetadata.genre?.toString().containsCaseInsensitive(query)
                }
            } else {
                // If the user asked to "play music", or something similar, the query will also
                // be blank. Given the small catalog of songs in the sample, just return them
                // all, shuffled, as something to play.
                Log.d(TAG, "Unfocused search without keyword")
                return shuffled()
            }
        } else {
            return focusSearchResult
        }
    }
    /**
     * [MediaStore.EXTRA_MEDIA_GENRE] is missing on API 19. Hide this fact by using our
     * own version of it.
     */
    private val EXTRA_MEDIA_GENRE
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MediaStore.EXTRA_MEDIA_GENRE
        } else {
            "android.intent.extra.genre"
        }

    companion object {
        const val STATE_CREATED = 0
        const val STATE_INITIALIZING = 1
        const val STATE_INITIALIZED = 2
        const val STATE_ERROR = 3
    }
}

private const val TAG = "MusicSource"