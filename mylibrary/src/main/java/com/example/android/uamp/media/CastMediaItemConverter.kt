package com.example.android.uamp.media

import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import androidx.annotation.OptIn
import androidx.media3.cast.DefaultMediaItemConverter
import androidx.media3.cast.MediaItemConverter
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import com.example.android.uamp.media.library.DomainMediaSource
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.MediaQueueItem
import com.google.android.gms.common.images.WebImage

/**
 * Un [MediaItemConverter] para convertir de un [MediaItem] a un [MediaQueueItem] de Cast.
 *
 * Añade todas las propiedades específicas de metadatos de audio y crea un objeto de metadatos de Cast
 * del tipo [MediaMetadata.MEDIA_TYPE_MUSIC_TRACK].
 *
 * Para crear una imagen de portada para Cast, no podemos usar el estándar [MediaItem#mediaMetadata#artworkUri]
 * porque UAMP usa un content provider para servir bitmaps en caché. Los URI que comienzan con `content://`
 * son inútiles en un dispositivo Cast, por lo que necesitamos usar el URI HTTP original que el [DomainMediaSource]
 * almacena en el extra de metadatos con la clave `JsonSource.ORIGINAL_ARTWORK_URI_KEY`.
 */
@OptIn(UnstableApi::class)
internal class CastMediaItemConverter : MediaItemConverter {

    private val defaultMediaItemConverter = DefaultMediaItemConverter()

    /**
     * Convierte un [MediaItem] en un [MediaQueueItem] de Cast.
     *
     * @param mediaItem El elemento de medios a convertir.
     * @return El elemento de cola de medios de Cast.
     */
    override fun toMediaQueueItem(mediaItem: MediaItem): MediaQueueItem {
        val castMediaMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK)
        castMediaMetadata.putString("uamp.mediaid", mediaItem.mediaId)
        mediaItem.mediaMetadata.title?.let {
            castMediaMetadata.putString(MediaMetadata.KEY_TITLE, it.toString())
        }
        mediaItem.mediaMetadata.subtitle?.let {
            castMediaMetadata.putString(MediaMetadata.KEY_SUBTITLE, it.toString())
        }
        mediaItem.mediaMetadata.artist?.let {
            castMediaMetadata.putString(MediaMetadata.KEY_ARTIST, it.toString())
        }
        mediaItem.mediaMetadata.albumTitle?.let {
            castMediaMetadata.putString(MediaMetadata.KEY_ALBUM_TITLE, it.toString())
        }
        mediaItem.mediaMetadata.albumArtist?.let {
            castMediaMetadata.putString(MediaMetadata.KEY_ALBUM_ARTIST, it.toString())
        }
        mediaItem.mediaMetadata.composer?.let {
            castMediaMetadata.putString(MediaMetadata.KEY_COMPOSER, it.toString())
        }
        mediaItem.mediaMetadata.trackNumber?.let {
            castMediaMetadata.putInt(MediaMetadata.KEY_TRACK_NUMBER, it)
        }
        mediaItem.mediaMetadata.discNumber?.let {
            castMediaMetadata.putInt(MediaMetadata.KEY_DISC_NUMBER, it)
        }
        val mediaInfo = MediaInfo.Builder(mediaItem.localConfiguration!!.uri.toString())
            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
            .setContentType(MimeTypes.AUDIO_MPEG)
        mediaItem.localConfiguration?.let {
            mediaInfo.setContentUrl(it.uri.toString())
        }
        mediaItem.mediaMetadata.extras?.let { bundle ->
            // Usar el URI de la obra original para Cast.
            bundle.getString(DomainMediaSource.ORIGINAL_ARTWORK_URI_KEY)?.let {
                castMediaMetadata.addImage(WebImage(Uri.parse(it)))
            }
            mediaInfo.setStreamDuration(bundle.getLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0))
        }
        mediaInfo.setMetadata(castMediaMetadata)
        val mediaQueueItem = defaultMediaItemConverter.toMediaQueueItem(mediaItem)
        mediaQueueItem.media?.customData?.let {
            mediaInfo.setCustomData(it)
        }
        return MediaQueueItem.Builder(mediaInfo.build()).build()
    }

    /**
     * Convierte un [MediaQueueItem] de Cast en un [MediaItem].
     *
     * @param mediaQueueItem El elemento de cola de medios de Cast a convertir.
     * @return El elemento de medios convertido.
     */
    override fun toMediaItem(mediaQueueItem: MediaQueueItem): MediaItem {
        return defaultMediaItemConverter.toMediaItem(mediaQueueItem)
    }
}
