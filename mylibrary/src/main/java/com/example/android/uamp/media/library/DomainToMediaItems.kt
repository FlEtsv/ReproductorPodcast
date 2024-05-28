package com.example.android.uamp.media.library

import android.net.Uri
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.universae.domain.Sesion

internal class DomainMediaSource(private val source: Sesion): AbstractMusicSource() {

    companion object {
        //const val ORIGINAL_ARTWORK_URI_KEY = "com.universae.navegacion.JSON_ARTWORK_URI"
        const val ORIGINAL_ARTWORK_URI_KEY = "com.example.android.uamp.JSON_ARTWORK_URI"
    }

    private var mediaItems: List<MediaItem> = emptyList()

    init {
        state = STATE_INITIALIZING
    }

    override fun iterator(): Iterator<MediaItem> = mediaItems.iterator()

    @OptIn(UnstableApi::class) override suspend fun load() {
        Log.d("DomainToMediaItems", "Starting to load catalog")
        updateCatalog(source)?.let { updatedCatalog ->
            mediaItems = updatedCatalog
            state = STATE_INITIALIZED
            Log.d("DomainToMediaItems", "Catalog loaded successfully")
        } ?: run {
            mediaItems = emptyList()
            state = STATE_ERROR
            Log.e("DomainToMediaItems", "Failed to load catalog")
        }
    }

    private suspend fun updateCatalog(sesion: Sesion): List<MediaItem>? {
        return sesionToMediaItems(sesion)

    }

    fun loadFromSession(sesion: Sesion) {
        mediaItems = sesionToMediaItems(sesion)
    }

    fun getMediaItems(): List<MediaItem> = mediaItems
}
private fun sesionToMediaItems(sesion: Sesion): List<MediaItem> {
    val mediaItems = mutableListOf<MediaItem>()

    sesion.grados.forEach { grado ->
        grado.asignaturasId.forEach { asignaturaId ->
            val asignatura = sesion.asignaturas.find { it.asignaturaId.id == asignaturaId.id }
            val totalTemas: Int? = asignatura?.temas?.size
            asignatura?.temas?.forEach { tema ->
                val temaImageUri = Uri.parse(tema.imagenUrl)
                val imageUri = AlbumArtContentProvider.mapUri(temaImageUri)
                val extras = Bundle()
                extras.putString(DomainMediaSource.ORIGINAL_ARTWORK_URI_KEY, tema.imagenUrl)
                val mediaMetadata = MediaMetadata.Builder()
                    .setTitle(tema.nombreTema)
                    .setDisplayTitle(tema.nombreTema)
                    .setArtist("Universae") //TODO: Cambiar por el nombre del profesor??
                    .setAlbumTitle(asignatura.nombreAsignatura)
                    .setGenre(grado.nombreModulo)
                    .setDescription(tema.descripcionTema) // Opcional, dependiendo de los detalles que quieras incluir
                    //.setArtworkUri(temaImageUri)
                    .setArtworkUri(imageUri)
                    .setTrackNumber(tema.trackNumber)
                    .setTotalTrackCount(totalTemas)
                    .setIsPlayable(true)
                    .setIsBrowsable(false)
                    .setMediaType(MediaMetadata.MEDIA_TYPE_AUDIO_BOOK_CHAPTER)
                    .setExtras(extras)
                    .build()

                val mediaItem = MediaItem.Builder()
                    .setMediaId(tema.temaId.id.toString())
                    .setUri(tema.audioUrl)
                    .setMimeType(
                        MimeTypes.AUDIO_MPEG)
                    .setMediaMetadata(mediaMetadata)
                    .build()

                mediaItems.add(mediaItem)
            }
        }
    }

    return mediaItems
}