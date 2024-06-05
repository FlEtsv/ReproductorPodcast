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
    private val storage = PersistentStorage.getInstance(context)

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
            if (mediaItem.mediaMetadata.extras?.getInt("Completion State") == TEMA_NO_COMPLETADO &&
                (mediaIdToChildren[UAMP_RECOMMENDED_ROOT]?.none { it.mediaMetadata.albumTitle == mediaItem.mediaMetadata.albumTitle } ?: true)) {
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
     * Provides access to the list of children with the `get` operator.
     * i.e.: `browseTree\[UAMP_BROWSABLE_ROOT\]`
     */
    operator fun get(mediaId: String) = mediaIdToChildren[mediaId]

    /** Provides access to the media items by media id. */
    fun getMediaItemByMediaId(mediaId: String) = mediaIdToMediaItem[mediaId]


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
                (mediaIdToChildren[UAMP_RECOMMENDED_ROOT]?.none { it.mediaMetadata.albumTitle == mediaItem.mediaMetadata.albumTitle } ?: true)) {
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
