package com.universae.audioplayerlibrary.media.libreria

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.universae.domain.Sesion
import com.universae.domain.entities.asignatura.Asignatura
import com.universae.reproductor.domain.entities.grado.Grado
import com.universae.reproductor.domain.entities.tema.Tema

internal class DomainMediaSource: CustomMusicSource() {

    private var mediaItems: List<MediaItem> = emptyList()
    override fun iterator(): Iterator<MediaItem> = mediaItems.iterator()

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
                    .setAlbumTitle(asignatura.nombreAsignatura)
                    .setGenre(grado.nombreModulo)
                    .setDescription(tema.descripcionTema) // Opcional, dependiendo de los detalles que quieras incluir
                    .setArtworkUri(Uri.parse("https://estaticos-cdn.prensaiberica.es/epi/public/file/2023/0804/12/universae-f534810.png")) // TODO: Cambiar por la URL de la imagen del tema
                    .build()

                val mediaItem = MediaItem.Builder()
                    .setMediaId(tema.temaId.id.toString())
                    .setUri(tema.audioUrl)
                    .setMediaMetadata(mediaMetadata)
                    .build()

                mediaItems.add(mediaItem)
            }
        }
    }

    return mediaItems
}