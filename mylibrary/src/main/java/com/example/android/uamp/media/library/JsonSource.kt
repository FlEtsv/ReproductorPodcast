/*
 * Copyright 2017 Google Inc. All rights reserved.
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

package com.example.android.uamp.media.library

import android.net.Uri
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * Clase JsonSource.
 * Esta clase es una fuente de objetos [MediaMetadataCompat] creados a partir de un flujo JSON básico.
 *
 * La definición del JSON se especifica en los documentos de [JsonMusic] en este archivo,
 * que es la representación del objeto de la misma.
 */
internal class JsonSource(private val source: Uri) : AbstractMusicSource() {

    companion object {
        const val ORIGINAL_ARTWORK_URI_KEY = "com.example.android.uamp.JSON_ARTWORK_URI"
    }

    private var catalog: List<androidx.media3.common.MediaItem> = emptyList()

    init {
        state = STATE_INITIALIZING
    }

    override fun iterator(): Iterator<MediaItem> = catalog.iterator()

    /**
     * Carga el catálogo desde la fuente.
     */
    @OptIn(UnstableApi::class)
    override suspend fun load() {
        Log.d("JsonSource", "Starting to load catalog")
        updateCatalog(source)?.let { updatedCatalog ->
            catalog = updatedCatalog
            state = STATE_INITIALIZED
            Log.d("JsonSource", "Catalog loaded successfully")
        } ?: run {
            catalog = emptyList()
            state = STATE_ERROR
            Log.e("JsonSource", "Failed to load catalog")
        }
    }


    /**
     * Función para conectar a una URI remota y descargar/procesar el archivo JSON que corresponde a
     * objetos [MediaMetadataCompat].
     */
    @OptIn(UnstableApi::class)
    private suspend fun updateCatalog(catalogUri: Uri): List<MediaItem>? {
        return withContext(Dispatchers.IO) {
            val musicCat = try {
                downloadJson(catalogUri)
            } catch (ioException: IOException) {
                Log.e("JsonSource", "Error downloading JSON", ioException)
                return@withContext null
            }

            val baseUri = catalogUri.toString().removeSuffix(catalogUri.lastPathSegment ?: "")

            musicCat.music.map { song ->
                catalogUri.scheme?.let { scheme ->
                    if (!song.source.startsWith(scheme)) {
                        song.source = baseUri + song.source
                    }
                    if (!song.image.startsWith(scheme)) {
                        song.image = baseUri + song.image
                    }
                }

                val jsonImageUri = Uri.parse(song.image)
                val imageUri = AlbumArtContentProvider.mapUri(jsonImageUri)
                val mediaMetadata = MediaMetadata.Builder()
                    .from(song)
                    .apply {
                        setArtworkUri(imageUri)
                        val extras = Bundle()
                        extras.putString(ORIGINAL_ARTWORK_URI_KEY, jsonImageUri.toString())
                        setExtras(extras)
                    }
                    .build()
                MediaItem.Builder()
                    .apply {
                        setMediaId(song.id)
                        setUri(song.source)
                        setMimeType(MimeTypes.AUDIO_MPEG)
                        setMediaMetadata(mediaMetadata)
                    }.build()
            }.toList()
        }
    }


    /**
     * Intenta descargar un catálogo desde una Uri dada.
     *
     * @param catalogUri URI para intentar descargar el catálogo.
     * @return El catálogo descargado, o un catálogo vacío si ocurrió un error.
     */
    @Throws(IOException::class)
    private fun downloadJson(catalogUri: Uri): JsonCatalog {
        val catalogConn = URL(catalogUri.toString())
        val reader = BufferedReader(InputStreamReader(catalogConn.openStream()))
        return Gson().fromJson(reader, JsonCatalog::class.java)
    }
}

/**
 * Método de extensión para [MediaMetadataCompat.Builder] para establecer los campos desde
 * nuestro objeto construido JSON (para hacer que el código sea un poco más fácil de ver).
 */
fun MediaMetadata.Builder.from(jsonMusic: JsonMusic): MediaMetadata.Builder {
    setTitle(jsonMusic.title)
    setDisplayTitle(jsonMusic.title)
    setArtist(jsonMusic.artist)
    setAlbumTitle(jsonMusic.album)
    setGenre(jsonMusic.genre)
    setArtworkUri(Uri.parse(jsonMusic.image))
    setTrackNumber(jsonMusic.trackNumber.toInt())
    setTotalTrackCount(jsonMusic.totalTrackCount.toInt())
    setFolderType(MediaMetadata.FOLDER_TYPE_NONE)
    setIsPlayable(true)
    // The duration from the JSON is given in seconds, but the rest of the code works in
    // milliseconds. Here's where we convert to the proper units.
    val durationMs = TimeUnit.SECONDS.toMillis(jsonMusic.duration)
    val bundle = Bundle()
    bundle.putLong("durationMs", durationMs)
    return this
}

/**
 * Objeto contenedor para nuestro JSON para ser procesado fácilmente por GSON.
 */
class JsonCatalog {
    var music: List<JsonMusic> = ArrayList()
}

/**
 * A single piece of music included in our JSON catalog.
 * The server format is specified as follows:
 * ```
 *     { "music" : [
 *     { "title" : // Title of the piece of music
 *     "album" : // Album title of the piece of music
 *     "artist" : // Artist of the piece of music
 *     "genre" : // Primary genre of the music
 *     "source" : // Path to the music, which may be relative
 *     "image" : // Path to the art for the music, which may be relative
 *     "trackNumber" : // Track number
 *     "totalTrackCount" : // Track count
 *     "duration" : // Duration of the music in seconds
 *     "site" : // Source of the music, if applicable
 *     }
 *     ]}
 * ```
 *
 * `source` and `image` can be provided in either relative or
 * absolute paths. For example:
 * ``
 *     "source" : "https://www.example.com/music/ode_to_joy.mp3",
 *     "image" : "ode_to_joy.jpg"
 * ``
 *
 * The `source` specifies the full URI to download the piece of music from, but
 * `image` will be fetched relative to the path of the JSON file itself. This means
 * that if the JSON was at "https://www.example.com/json/music.json" then the image would be found
 * at "https://www.example.com/json/ode_to_joy.jpg".
 */

@Suppress("unused")
class JsonMusic {
    var id: String = ""
    var title: String = ""
    var album: String = ""
    var artist: String = ""
    var genre: String = ""
    var source: String = ""
    var image: String = ""
    var trackNumber: Long = 0
    var totalTrackCount: Long = 0
    var duration: Long = -1
    var site: String = ""
}
