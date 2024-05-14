import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyMusicService : MediaBrowserServiceCompat() {
    private lateinit var session: MediaSessionCompat
    private var player: ExoPlayer? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main)

    // Se llama cuando el servicio se crea
    override fun onCreate() {
        super.onCreate()
        initializeMediaSession()
        initializePlayer()
    }

    // Inicializa la sesión de medios
    private fun initializeMediaSession() {
        session = MediaSessionCompat(this, "MyMusicService")
        sessionToken = session.sessionToken
        session.isActive = true
        session.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlay() {
                player?.playWhenReady = true
            }

            override fun onPause() {
                player?.playWhenReady = false
            }

            override fun onPrepare() {
                serviceScope.launch {
                    preparePlayer("file:///android_asset/Adele.mp3")
                }
            }

            override fun onSeekTo(pos: Long) {
                serviceScope.launch {
                    player?.seekTo(pos)
                }
            }
        })
    }

    // Inicializa el reproductor ExoPlayer
    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this).build().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(C.CONTENT_TYPE_MUSIC)
                    .setUsage(C.USAGE_MEDIA)
                    .build(), true
            )
        }
    }

    // Prepara el reproductor con un URI de media
    private suspend fun preparePlayer(uriString: String) {
        val mediaItem = MediaItem.fromUri(Uri.parse(uriString))
        player?.setMediaItem(mediaItem)
        player?.prepare()
    }

    // Se llama cuando el servicio se destruye
    override fun onDestroy() {
        session.release()
        player?.release()
        player = null
        super.onDestroy()
    }

    // Retorna el root de navegación para el MediaBrowser
    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot {
        return BrowserRoot("ROOT_ID", null)
    }

    // Carga los hijos para el navegador de medios
    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        result.sendResult(null)
    }
}