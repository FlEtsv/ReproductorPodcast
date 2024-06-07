/*
 * Copyright 2020 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.uamp.media

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Clase PersistentStorage.
 * Esta clase se utiliza para almacenar datos que deben persistir entre reinicios, como la canción más recientemente reproducida.
 */
internal class PersistentStorage private constructor(val context: Context) {

    /**
     * Almacena cualquier dato que deba persistir entre reinicios, como la canción más recientemente reproducida.
     */
    private var preferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    companion object {

        @Volatile
        private var instance: PersistentStorage? = null

        /**
         * Método para obtener la instancia de PersistentStorage.
         */
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: PersistentStorage(context).also { instance = it }
            }

        private const val PREFS_NAME = "com.example.app.PREFS"
        private const val RECENT_TRACKS_KEY = "RECENT_TRACKS"
    }

    /**
     * Método para guardar la canción reciente.
     */
    suspend fun saveRecentSong(mediaItem: MediaItem, position: Long) {

        withContext(Dispatchers.IO) {
            /**
             * Después del arranque, Android intentará construir controles de medios estáticos para la canción más
             * recientemente reproducida. El arte para estos controles de medios no debe cargarse
             * desde la red ya que puede ser demasiado lento o no estar disponible inmediatamente después del arranque. En su lugar,
             * convertimos el iconUri para apuntar al caché en disco de Glide.
             */
            preferences.edit()
                .putString(RECENT_SONG_MEDIA_ID_KEY, mediaItem.mediaId)
                .putString(RECENT_SONG_TITLE_KEY, mediaItem.mediaMetadata.title.toString())
                .putString(RECENT_SONG_SUBTITLE_KEY, mediaItem.mediaMetadata.subtitle.toString())
                .putString(RECENT_SONG_ICON_URI_KEY, mediaItem.mediaMetadata.artworkUri.toString())
                .putLong(RECENT_SONG_POSITION_KEY, position)
                .apply()
        }
    }

    /**
     * Método para cargar la canción reciente.
     */
    fun loadRecentSong(): MediaItem? {
        val mediaId = preferences.getString(RECENT_SONG_MEDIA_ID_KEY, null)
        return if (mediaId == null) {
            null
        } else {
            val extras = Bundle().also {
                val position = preferences.getLong(RECENT_SONG_POSITION_KEY, 0L)
                it.putLong(MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS, position)
            }
            val metadataBuilder = with(MediaMetadata.Builder()) {
                setTitle(preferences.getString(RECENT_SONG_TITLE_KEY, ""))
                setSubtitle(preferences.getString(RECENT_SONG_SUBTITLE_KEY, ""))
                setFolderType(MediaMetadata.FOLDER_TYPE_NONE)
                setIsPlayable(true)
                setArtworkUri(Uri.parse(preferences.getString(RECENT_SONG_ICON_URI_KEY, "")))
                setExtras(extras)
            }
            return with(MediaItem.Builder()) {
                setMediaId(mediaId)
                setMediaMetadata(metadataBuilder.build())
                build()
            }
        }
    }

    /**
     * Método para borrar la canción reciente.
     */
    fun clearRecentSong() {
        preferences.edit()
            .remove(RECENT_SONG_MEDIA_ID_KEY)
            .remove(RECENT_SONG_TITLE_KEY)
            .remove(RECENT_SONG_SUBTITLE_KEY)
            .remove(RECENT_SONG_ICON_URI_KEY)
            .remove(RECENT_SONG_POSITION_KEY)
            .apply()
    }
}

private const val PREFERENCES_NAME = "uamp"
private const val RECENT_SONG_MEDIA_ID_KEY = "recent_song_media_id"
private const val RECENT_SONG_TITLE_KEY = "recent_song_title"
private const val RECENT_SONG_SUBTITLE_KEY = "recent_song_subtitle"
private const val RECENT_SONG_ICON_URI_KEY = "recent_song_icon_uri"
private const val RECENT_SONG_POSITION_KEY = "recent_song_position"