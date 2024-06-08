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
import com.example.android.uamp.media.PersistentStorage
import com.example.android.uamp.media.R
import com.example.android.uamp.media.extensions.urlEncoded

/**
 * Representa un árbol de medios que se utiliza en [MusicService.onLoadChildren].
 *
 * [BrowseTree] mapea un id de medios (ver: [MediaMetadataCompat.METADATA_KEY_MEDIA_ID]) a uno (o
 * más) objetos [MediaMetadataCompat], que son hijos de ese id de medios.
 *
 * Por ejemplo, dado el siguiente árbol conceptual:
 * raíz
 *  +-- Álbumes
 *  |    +-- Álbum_A
 *  |    |    +-- Canción_1
 *  |    |    +-- Canción_2
 *  ...
 *  +-- Artistas
 *  ...
 * raíz
 * +-- Recomendados
 * |    +-- Tema_1
 * |    +-- Tema_2
 * ...
 * +-- Grados
 * /    +-- Grado_1
 * /    |    +-- Asignatura_1
 * /    |    |    +-- Tema_1
 * /    |    |    |     Canción_1
 * /    |    |    +-- Tema_2
 * /    |    |    |     Canción_2
 * /    |    +-- Asignatura_2
 * ...
 * +-- Álbumes
 * |    +-- Álbum_A
 * |    |    +-- Canción_1
 * ...
 *  Solicitar `browseTree["raíz"]` devolvería una lista que incluiría "Álbumes", "Artistas", y
 *  cualquier otro hijo directo. Tomando el id de medios de "Álbumes" ("Álbumes" en este ejemplo),
 *  `browseTree["Álbumes"]` devolvería una lista de un solo elemento "Álbum_A", y, finalmente,
 *  `browseTree["Álbum_A"]` devolvería "Canción_1" y "Canción_2". Dado que esos son nodos hoja,
 *  solicitar `browseTree["Canción_1"]` devolvería null (no hay ningún hijo de él).
 */
class BrowseTree(
    val context: Context,
    val musicSource: MusicSource,
    val recentMediaId: String? = null
) {
    private val mediaIdToChildren = mutableMapOf<String, MutableList<MediaItem>>()
    private val mediaIdToMediaItem = mutableMapOf<String, MediaItem>()
    private val storage = PersistentStorage.getInstance(context)

    /**
     * Indica si se permite a los clientes que son desconocidos (no están en la lista permitida) usar la búsqueda en este
     * [BrowseTree].
     */
    val searchableByUnknownCaller = true

    init {
        // Aquí se construye el árbol de medios, agrupando los medios por categorías y subcategorías.
        // Se crean las categorías principales y se añaden los medios a cada una de ellas.
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
            if (mediaItem.mediaMetadata.extras?.getInt("Completion State") == TEMA_NO_COMPLETADO &&
                (mediaIdToChildren[UAMP_RECOMMENDED_ROOT]?.none { it.mediaMetadata.albumTitle == mediaItem.mediaMetadata.albumTitle }
                    ?: true)
            ) {
                val recommendedChildren = mediaIdToChildren[UAMP_RECOMMENDED_ROOT]
                    ?: mutableListOf()
                recommendedChildren += mediaItem
                mediaIdToChildren[UAMP_RECOMMENDED_ROOT] = recommendedChildren
            }

            if (mediaItem.mediaId == recentMediaId) {
                mediaIdToChildren[UAMP_RECENT_ROOT] = mutableListOf(mediaItem)
            }
            mediaIdToMediaItem[mediaItem.mediaId] = mediaItem
        }
    }
    // Aquí se definen varios métodos para interactuar con el árbol de medios, como obtener los hijos de un nodo,
    // actualizar la pista reciente, construir la jerarquía de medios, obtener elementos de medios por id de medios, etc.

    fun updateRecentTrack(mediaItem: MediaItem) {
        val recentTracksList = mutableListOf(mediaItem)
        mediaIdToChildren[UAMP_RECENT_TRACKS_ROOT] = recentTracksList
    }


    private fun buildMediaHierarchy(mediaItem: MediaItem) {
        val genre = mediaItem.mediaMetadata.genre.toString()
        val albumTitle = mediaItem.mediaMetadata.albumTitle.toString()
        val genreMediaId = genre.urlEncoded
        val albumMediaId = albumTitle.urlEncoded

        val genreRootList = mediaIdToChildren[GRADO_ASIGNATURA_ROOT] ?: mutableListOf()
        if (!genreRootList.any { it.mediaId == genreMediaId }) {
            val genreMetadata = MediaMetadata.Builder().apply {
                setTitle(genre)
                setFolderType(MediaMetadata.FOLDER_TYPE_GENRES)
                setIsPlayable(false)
                setArtworkUri(
                    AlbumArtContentProvider.mapUri(
                        Uri.parse(
                            mediaItem.mediaMetadata.extras?.getString(
                                "artworkGrado"
                            ) ?: mediaItem.mediaMetadata.artworkUri.toString()
                        )
                    )
                )
            }.build()
            val genreMediaItem = MediaItem.Builder().apply {
                setMediaId(genreMediaId)
                setMediaMetadata(genreMetadata)
            }.build()
            genreRootList += genreMediaItem
            mediaIdToChildren[GRADO_ASIGNATURA_ROOT] = genreRootList
            mediaIdToChildren[genreMediaId] = mutableListOf()
        }

        val albumsList = mediaIdToChildren[genreMediaId] ?: mutableListOf()
        if (!albumsList.any { it.mediaId == albumMediaId }) {
            val albumMetadata = MediaMetadata.Builder().apply {
                setTitle(albumTitle)
                setFolderType(MediaMetadata.FOLDER_TYPE_PLAYLISTS)
                setIsPlayable(false)
                setArtworkUri(mediaItem.mediaMetadata.artworkUri)
            }.build()
            val albumMediaItem = MediaItem.Builder().apply {
                setMediaId(albumMediaId)
                setMediaMetadata(albumMetadata)
            }.build()
            albumsList += albumMediaItem
            mediaIdToChildren[genreMediaId] = albumsList
        }

        val tracksListInGenre = mediaIdToChildren[albumMediaId] ?: mutableListOf()
        if (!tracksListInGenre.any { it.mediaId == mediaItem.mediaId }) {
            val trackMetadataInGenre = mediaItem.mediaMetadata.buildUpon().apply {
                setFolderType(MediaMetadata.FOLDER_TYPE_NONE)
                setIsPlayable(true)
            }.build()
            val trackMediaItemInGenre = mediaItem.buildUpon().apply {
                setMediaId(mediaItem.mediaId)
                setMediaMetadata(trackMetadataInGenre)
            }.build()
            tracksListInGenre += trackMediaItemInGenre
            mediaIdToChildren[albumMediaId] = tracksListInGenre
        }

        val albumsRootList = mediaIdToChildren[UAMP_ALBUMS_ROOT] ?: mutableListOf()
        if (!albumsRootList.any { it.mediaId == albumMediaId }) {
            val albumMetadata = MediaMetadata.Builder().apply {
                setTitle(albumTitle)
                setFolderType(MediaMetadata.FOLDER_TYPE_PLAYLISTS)
                setIsPlayable(false)
                setArtworkUri(mediaItem.mediaMetadata.artworkUri)
            }.build()
            val albumMediaItem = MediaItem.Builder().apply {
                setMediaId(albumMediaId)
                setMediaMetadata(albumMetadata)
            }.build()
            albumsRootList += albumMediaItem
            mediaIdToChildren[UAMP_ALBUMS_ROOT] = albumsRootList
        }

        val tracksListInAlbumsRoot = mediaIdToChildren[albumMediaId] ?: mutableListOf()
        if (!tracksListInAlbumsRoot.any { it.mediaId == mediaItem.mediaId }) {
            val trackMetadataInAlbumsRoot = mediaItem.mediaMetadata.buildUpon().apply {
                setFolderType(MediaMetadata.FOLDER_TYPE_NONE)
                setIsPlayable(true)
            }.build()
            val trackMediaItemInAlbumsRoot = mediaItem.buildUpon().apply {
                setMediaId(mediaItem.mediaId)
                setMediaMetadata(trackMetadataInAlbumsRoot)
            }.build()
            tracksListInAlbumsRoot += trackMediaItemInAlbumsRoot
            mediaIdToChildren[albumMediaId] = tracksListInAlbumsRoot
        }

        val recentTrack = storage.loadRecentSong()
        if (mediaItem.mediaId == recentTrack?.mediaId) {
            val recentTracksList = mediaIdToChildren[UAMP_RECENT_TRACKS_ROOT] ?: mutableListOf()
            recentTracksList += mediaItem
            mediaIdToChildren[UAMP_RECENT_TRACKS_ROOT] = recentTracksList
        }
    }


    /**
     * Proporciona acceso a la lista de hijos con el operador `get`.
     * es decir: `browseTree\[UAMP_BROWSABLE_ROOT\]`
     */
    operator fun get(mediaId: String) = mediaIdToChildren[mediaId]

    /** Proporciona acceso a los elementos de medios por id de medios. */
    fun getMediaItemByMediaId(mediaId: String) = mediaIdToMediaItem[mediaId]

    // Aquí se definen varias constantes utilizadas en la clase, como los ids de medios para las categorías principales,
    // la URI de la raíz del recurso, etc.
    fun reload() {
        mediaIdToChildren.clear()
        mediaIdToMediaItem.clear()

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
            if (mediaItem.mediaMetadata.extras?.getInt("Completion State") == TEMA_NO_COMPLETADO &&
                (mediaIdToChildren[UAMP_RECOMMENDED_ROOT]?.none { it.mediaMetadata.albumTitle == mediaItem.mediaMetadata.albumTitle }
                    ?: true)
            ) {
                val recommendedChildren = mediaIdToChildren[UAMP_RECOMMENDED_ROOT]
                    ?: mutableListOf()
                recommendedChildren += mediaItem
                mediaIdToChildren[UAMP_RECOMMENDED_ROOT] = recommendedChildren
            }

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
