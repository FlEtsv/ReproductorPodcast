package com.universae.audioplayerlibrary.media.libreria

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import com.universae.domain.Sesion
import com.universae.domain.entities.asignatura.Asignatura
import com.universae.reproductor.domain.entities.grado.Grado
import com.universae.reproductor.domain.entities.tema.Tema
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

internal class DomainMediaSource(private val source: Sesion): CustomMusicSource() {

    private var mediaItems: List<MediaItem> = emptyList()

    init {
        state = STATE_INITIALIZING
    }

    override fun iterator(): Iterator<MediaItem> = mediaItems.iterator()

    override suspend fun load() {
        updateCatalog(source)?.let { updatedCatalog ->
            mediaItems = updatedCatalog
            state = STATE_INITIALIZED
        } ?: run {
            mediaItems = emptyList()
            state = STATE_ERROR
        }
    }

    private suspend fun updateCatalog(sesion: Sesion): List<MediaItem>? {
        return sesionToMediaItems(sesion)

    }

    fun loadFromSession(sesion: Sesion) {
        mediaItems = sesionToMediaItems(sesion) //TODO: cambiar a sesion iniciado en app.
    }

    fun getMediaItems(): List<MediaItem> = mediaItems
}
fun sesionToMediaItems(sesion: Sesion): List<MediaItem> {
    val mediaItems = mutableListOf<MediaItem>()

    sesion.grados.forEach { grado ->
        grado.asignaturasId.forEach { asignaturaId ->
            val asignatura = sesion.asignaturas.find { it.asignaturaId.id == asignaturaId.id }
            asignatura?.temas?.forEach { tema ->
                val mediaMetadata = MediaMetadata.Builder()
                    .setTitle(tema.nombreTema)
                    .setDisplayTitle(tema.nombreTema)
                    .setArtist("Universae") //TODO: Cambiar por el nombre del profesor??
                    .setAlbumTitle(asignatura.nombreAsignatura)
                    .setGenre(grado.nombreModulo)
                    .setDescription(tema.descripcionTema) // Opcional, dependiendo de los detalles que quieras incluir
                    .setArtworkUri(Uri.parse("https://estaticos-cdn.prensaiberica.es/epi/public/file/2023/0804/12/universae-f534810.png")) // TODO: Cambiar por la URL de la imagen del tema
                    .setTrackNumber(tema.temaId.id)
                    .setIsPlayable(true)
                    .setIsBrowsable(false)
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