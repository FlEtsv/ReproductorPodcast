/*
 * Copyright 2019 Google Inc. All rights reserved.
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

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.android.uamp.media.R
import com.example.android.uamp.media.extensions.urlEncoded

/**
 * Represents a tree of media that's used by [MusicService.onLoadChildren].
 *
 * [BrowseTree] maps a media id (see: [MediaMetadataCompat.METADATA_KEY_MEDIA_ID]) to one (or
 * more) [MediaMetadataCompat] objects, which are children of that media id.
 *
 * For example, given the following conceptual tree:
 * root
 *  +-- Albums
 *  |    +-- Album_A
 *  |    |    +-- Song_1
 *  |    |    +-- Song_2
 *  ...
 *  +-- Artists
 *  ...
 * root
 * +-- Recomendados
 * |    +-- Tema_1
 * |    +-- Tema_2
 * ...
 * +-- Grados
 * /    +-- Grado_1
 * /    |    +-- Asignatura_1
 * /    |    |    +-- Tema_1
 * /    |    |    |     Song_1
 * /    |    |    +-- Tema_2
 * /    |    |    |     Song_2
 * /    |    +-- Asignatura_2
 * ...
 * +-- Albums
 * |    +-- Album_A
 * |    |    +-- Song_1
 * ...
 *  Requesting `browseTree["root"]` would return a list that included "Albums", "Artists", and
 *  any other direct children. Taking the media ID of "Albums" ("Albums" in this example),
 *  `browseTree["Albums"]` would return a single item list "Album_A", and, finally,
 *  `browseTree["Album_A"]` would return "Song_1" and "Song_2". Since those are leaf nodes,
 *  requesting `browseTree["Song_1"]` would return null (there aren't any children of it).
 */
class BrowseTree(
    val context: Context,
    val musicSource: MusicSource,
    val recentMediaId: String? = null
) {
    private val mediaIdToChildren = mutableMapOf<String, MutableList<MediaItem>>()
    private val mediaIdToMediaItem = mutableMapOf<String, MediaItem>()

    /**
     * Whether to allow clients which are unknown (not on the allowed list) to use search on this
     * [BrowseTree].
     */
    val searchableByUnknownCaller = true

    init {
        val rootList = mediaIdToChildren[UAMP_BROWSABLE_ROOT] ?: mutableListOf()

        val recommendedCategory = MediaMetadata.Builder().apply {
            setTitle(context.getString(R.string.recommended_title))
            setArtworkUri(
                Uri.parse(
                    RESOURCE_ROOT_URI +
                            context.resources.getResourceEntryName(R.drawable.ic_recommended)
                )
            )
            setFolderType(MediaMetadata.FOLDER_TYPE_MIXED)
            setIsPlayable(false)
        }.build()
        rootList += MediaItem.Builder().apply {
            setMediaId(UAMP_RECOMMENDED_ROOT)
            setMediaMetadata(recommendedCategory)
        }.build()
        val gradoAsignaturaMetadata = MediaMetadata.Builder().apply {
            setTitle(context.getString(R.string.grados_title))
            setArtworkUri(
                Uri.parse(
                    RESOURCE_ROOT_URI +
                            context.resources.getResourceEntryName(R.drawable.ic_album)
                )
            )
            setIsPlayable(false)
            setFolderType(MediaMetadata.FOLDER_TYPE_GENRES)
        }.build()
        rootList += MediaItem.Builder().apply {
            setMediaId(GRADO_ASIGNATURA_ROOT)
            setMediaMetadata(gradoAsignaturaMetadata)
        }.build()
        val albumsMetadata = MediaMetadata.Builder().apply {
            setTitle(context.getString(R.string.albums_title))
            setArtworkUri(
                Uri.parse(
                    RESOURCE_ROOT_URI +
                            context.resources.getResourceEntryName(R.drawable.ic_album)
                )
            )
            setIsPlayable(false)
            setFolderType(MediaMetadata.FOLDER_TYPE_PLAYLISTS)
        }.build()
        rootList += MediaItem.Builder().apply {
            setMediaId(UAMP_ALBUMS_ROOT)
            setMediaMetadata(albumsMetadata)
        }.build()
        val recentTracksMetadata = MediaMetadata.Builder().apply {
            setTitle(context.getString(R.string.recent_tracks_title))
            setArtworkUri(
                Uri.parse(
                    RESOURCE_ROOT_URI +
                            context.resources.getResourceEntryName(R.drawable.ic_album)
                )
            )
            setFolderType(MediaMetadata.FOLDER_TYPE_MIXED)
            setIsPlayable(false)
        }.build()
        rootList += MediaItem.Builder().apply {
            setMediaId(UAMP_RECENT_TRACKS_ROOT)
            setMediaMetadata(recentTracksMetadata)
        }.build()
        mediaIdToChildren[UAMP_BROWSABLE_ROOT] = rootList

        musicSource.forEach { mediaItem ->
            buildMediaHierarchy(mediaItem)

            Log.d("BrowseTree", "loading catalogue for " + mediaItem.mediaId)
            // Add the first track of each album to the 'Recommended' category
            if (mediaItem.mediaMetadata.trackNumber == 1) {
                val recommendedChildren = mediaIdToChildren[UAMP_RECOMMENDED_ROOT]
                    ?: mutableListOf()
                recommendedChildren += mediaItem
                mediaIdToChildren[UAMP_RECOMMENDED_ROOT] = recommendedChildren
            }

            // If this was recently played, add it to the recent root.
            if (mediaItem.mediaId == recentMediaId) {
                mediaIdToChildren[UAMP_RECENT_ROOT] = mutableListOf(mediaItem)
            }
            mediaIdToMediaItem[mediaItem.mediaId] = mediaItem
        }
    }

    private fun buildMediaHierarchy(mediaItem: MediaItem) {
        // Extraer el género y el título del álbum del mediaItem y codificarlos como URL seguras
        val genre = mediaItem.mediaMetadata.genre.toString()
        val albumTitle = mediaItem.mediaMetadata.albumTitle.toString()
        val genreMediaId = genre.urlEncoded
        val albumMediaId = albumTitle.urlEncoded

        // Construir la jerarquía de Géneros
        val genreRootList = mediaIdToChildren[GRADO_ASIGNATURA_ROOT] ?: mutableListOf()
        // Si el género no está ya en la lista, lo agrego
        if (!genreRootList.any { it.mediaId == genreMediaId }) {
            val genreMetadata = MediaMetadata.Builder().apply {
                setTitle(genre) // Establezco el título del género
                setFolderType(MediaMetadata.FOLDER_TYPE_GENRES) // Indico que es una carpeta de géneros
                setIsPlayable(false) // Indico que no es reproducible
                setArtworkUri(
                    mediaItem.mediaMetadata.artworkUri // Establezco la imagen del género (misma que la de los álbumes)
                )
            }.build()
            val genreMediaItem = MediaItem.Builder().apply {
                setMediaId(genreMediaId) // Establezco el ID del género
                setMediaMetadata(genreMetadata) // Establezco los metadatos del género
            }.build()
            genreRootList += genreMediaItem // Agrego el género a la lista de géneros
            mediaIdToChildren[GRADO_ASIGNATURA_ROOT] = genreRootList // Actualizo el mapa con la lista de géneros
            mediaIdToChildren[genreMediaId] = mutableListOf() // Inicializo una lista vacía de álbumes para este género
        }

        // Construir la jerarquía de Álbumes bajo el Género
        val albumsList = mediaIdToChildren[genreMediaId] ?: mutableListOf()
        // Si el álbum no está ya en la lista, lo agrego
        if (!albumsList.any { it.mediaId == albumMediaId }) {
            val albumMetadata = MediaMetadata.Builder().apply {
                setTitle(albumTitle) // Establezco el título del álbum
                setFolderType(MediaMetadata.FOLDER_TYPE_PLAYLISTS) // Indico que es una carpeta de álbumes
                setIsPlayable(false) // Indico que no es reproducible
                setArtworkUri(
                    mediaItem.mediaMetadata.artworkUri // Establezco la imagen del álbum
                )
            }.build()
            val albumMediaItem = MediaItem.Builder().apply {
                setMediaId(albumMediaId) // Establezco el ID del álbum
                setMediaMetadata(albumMetadata) // Establezco los metadatos del álbum
            }.build()
            albumsList += albumMediaItem // Agrego el álbum a la lista de álbumes
            mediaIdToChildren[genreMediaId] = albumsList // Actualizo el mapa con la lista de álbumes bajo el género
        }

        // Construir la jerarquía de Pistas bajo el Álbum en la jerarquía de Géneros
        val tracksListInGenre = mediaIdToChildren[albumMediaId] ?: mutableListOf()
        if (!tracksListInGenre.any { it.mediaId == mediaItem.mediaId }) {
            val trackMetadataInGenre = mediaItem.mediaMetadata.buildUpon().apply {
                setFolderType(MediaMetadata.FOLDER_TYPE_NONE) // Indico que no es una carpeta
                setIsPlayable(true) // Indico que es reproducible
            }.build()
            val trackMediaItemInGenre = mediaItem.buildUpon().apply {
                setMediaId(mediaItem.mediaId) // Establezco el ID de la pista
                setMediaMetadata(trackMetadataInGenre) // Establezco los metadatos de la pista
            }.build()
            tracksListInGenre += trackMediaItemInGenre // Agrego la pista a la lista de pistas
            mediaIdToChildren[albumMediaId] = tracksListInGenre // Actualizo el mapa con la lista de pistas bajo el álbum
        }

        // Construir la jerarquía de Álbumes directamente bajo el root de Álbumes
        val albumsRootList = mediaIdToChildren[UAMP_ALBUMS_ROOT] ?: mutableListOf()
        // Si el álbum no está ya en la lista, lo agrego
        if (!albumsRootList.any { it.mediaId == albumMediaId }) {
            val albumMetadata = MediaMetadata.Builder().apply {
                setTitle(albumTitle) // Establezco el título del álbum
                setFolderType(MediaMetadata.FOLDER_TYPE_PLAYLISTS) // Indico que es una carpeta de álbumes
                setIsPlayable(false) // Indico que no es reproducible
                setArtworkUri(
                     mediaItem.mediaMetadata.artworkUri// Establezco la imagen d  el álbum
                )
            }.build()
            val albumMediaItem = MediaItem.Builder().apply {
                setMediaId(albumMediaId) // Establezco el ID del álbum
                setMediaMetadata(albumMetadata) // Establezco los metadatos del álbum
            }.build()
            albumsRootList += albumMediaItem // Agrego el álbum a la lista de álbumes
            mediaIdToChildren[UAMP_ALBUMS_ROOT] = albumsRootList // Actualizo el mapa con la lista de álbumes bajo el root de álbumes

        }

        // Construir la jerarquía de Pistas bajo el Álbum en la jerarquía del root de Álbumes
        val tracksListInAlbumsRoot = mediaIdToChildren[albumMediaId] ?: mutableListOf()
        if (!tracksListInAlbumsRoot.any { it.mediaId == mediaItem.mediaId }) {
            val trackMetadataInAlbumsRoot = mediaItem.mediaMetadata.buildUpon().apply {
                setFolderType(MediaMetadata.FOLDER_TYPE_NONE) // Indico que no es una carpeta
                setIsPlayable(true) // Indico que es reproducible
            }.build()
            val trackMediaItemInAlbumsRoot = mediaItem.buildUpon().apply {
                setMediaId(mediaItem.mediaId) // Establezco el ID de la pista
                setMediaMetadata(trackMetadataInAlbumsRoot) // Establezco los metadatos de la pista
            }.build()
            tracksListInAlbumsRoot += trackMediaItemInAlbumsRoot // Agrego la pista a la lista de pistas
            mediaIdToChildren[albumMediaId] = tracksListInAlbumsRoot // Actualizo el mapa con la lista de pistas bajo el álbum
        }
        // Add each track to the 'Recent Tracks' category
        val recentTracksList = mediaIdToChildren[UAMP_RECENT_TRACKS_ROOT] ?: mutableListOf()
        recentTracksList += mediaItem
        mediaIdToChildren[UAMP_RECENT_TRACKS_ROOT] = recentTracksList
    }


    /**
     * Provides access to the list of children with the `get` operator.
     * i.e.: `browseTree\[UAMP_BROWSABLE_ROOT\]`
     */
    operator fun get(mediaId: String) = mediaIdToChildren[mediaId]

    /** Provides access to the media items by media id. */
    fun getMediaItemByMediaId(mediaId: String) = mediaIdToMediaItem[mediaId]

    fun reload() {
        // Clear existing data
        mediaIdToChildren.clear()
        mediaIdToMediaItem.clear()

        // Reinitialize BrowseTree
        val rootList = mediaIdToChildren[UAMP_BROWSABLE_ROOT] ?: mutableListOf()

        val recommendedCategory = MediaMetadata.Builder().apply {
            setTitle(context.getString(R.string.recommended_title))
            setArtworkUri(
                Uri.parse(
                    RESOURCE_ROOT_URI +
                            context.resources.getResourceEntryName(R.drawable.ic_recommended)
                )
            )
            setFolderType(MediaMetadata.FOLDER_TYPE_MIXED)
            setIsPlayable(false)
        }.build()
        rootList += MediaItem.Builder().apply {
            setMediaId(UAMP_RECOMMENDED_ROOT)
            setMediaMetadata(recommendedCategory)
        }.build()
        val gradoAsignaturaMetadata = MediaMetadata.Builder().apply {
            setTitle(context.getString(R.string.grados_title))
            setArtworkUri(
                Uri.parse(
                    RESOURCE_ROOT_URI +
                            context.resources.getResourceEntryName(R.drawable.ic_album)
                )
            )
            setIsPlayable(false)
            setFolderType(MediaMetadata.FOLDER_TYPE_GENRES)
        }.build()
        rootList += MediaItem.Builder().apply {
            setMediaId(GRADO_ASIGNATURA_ROOT)
            setMediaMetadata(gradoAsignaturaMetadata)
        }.build()

        val albumsMetadata = MediaMetadata.Builder().apply {
            setTitle(context.getString(R.string.albums_title))
            setArtworkUri(
                Uri.parse(
                    RESOURCE_ROOT_URI +
                            context.resources.getResourceEntryName(R.drawable.ic_album)
                )
            )
            setIsPlayable(false)
            setFolderType(MediaMetadata.FOLDER_TYPE_ALBUMS)
        }.build()
        rootList += MediaItem.Builder().apply {
            setMediaId(UAMP_ALBUMS_ROOT)
            setMediaMetadata(albumsMetadata)
        }.build()


        val recentTracksMetadata = MediaMetadata.Builder().apply {
            setTitle(context.getString(R.string.recent_tracks_title))
            setArtworkUri(
                Uri.parse(
                    RESOURCE_ROOT_URI +
                            context.resources.getResourceEntryName(R.drawable.ic_album)
                )
            )
            setFolderType(MediaMetadata.FOLDER_TYPE_MIXED)
            setIsPlayable(false)
        }.build()
        rootList += MediaItem.Builder().apply {
            setMediaId(UAMP_RECENT_TRACKS_ROOT)
            setMediaMetadata(recentTracksMetadata)
        }.build()
        mediaIdToChildren[UAMP_BROWSABLE_ROOT] = rootList

        musicSource.forEach { mediaItem ->
            buildMediaHierarchy(mediaItem)

            Log.d("BrowseTree", "loading catalogue for " + mediaItem.mediaId)
            // Add the first track of each album to the 'Recommended' category
            if (mediaItem.mediaMetadata.trackNumber == 1) {
                val recommendedChildren = mediaIdToChildren[UAMP_RECOMMENDED_ROOT]
                    ?: mutableListOf()
                recommendedChildren += mediaItem
                mediaIdToChildren[UAMP_RECOMMENDED_ROOT] = recommendedChildren
            }

            // If this was recently played, add it to the recent root.
            if (mediaItem.mediaId == recentMediaId) {
                mediaIdToChildren[UAMP_RECENT_ROOT] = mutableListOf(mediaItem)
            }
            mediaIdToMediaItem[mediaItem.mediaId] = mediaItem
        }
    }

    fun getMediaItemsInAlbum(albumTitle: String): List<MediaItem> {
        val albumId = albumTitle.urlEncoded
        return mediaIdToChildren[albumId] ?: emptyList()
    }
}

const val UAMP_BROWSABLE_ROOT = "/"
const val UAMP_EMPTY_ROOT = "@empty@"
const val UAMP_RECOMMENDED_ROOT = "__RECOMMENDED__"
const val UAMP_ALBUMS_ROOT = "__ALBUMS__"
const val UAMP_RECENT_ROOT = "__RECENT__"
const val GRADO_ASIGNATURA_ROOT = "__GRADO__"
const val UAMP_RECENT_TRACKS_ROOT = "__RECENT_TRACKS__"

const val MEDIA_SEARCH_SUPPORTED = "android.media.browse.SEARCH_SUPPORTED"

const val RESOURCE_ROOT_URI = "android.resource://com.universae.navegacion/drawable/"
