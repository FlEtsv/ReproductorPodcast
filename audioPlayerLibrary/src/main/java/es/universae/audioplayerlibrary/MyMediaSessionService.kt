package es.universae.audioplayerlibrary

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

/**
 * Esta es una clase de servicio que maneja la sesión de medios para la reproducción de audio.
 * Hereda de MediaSessionService.
 */
class MyMediaSessionService : MediaSessionService() {

    private var _mediaSession: MediaSession? = null
    private val mediaSession get() = _mediaSession!!

    companion object {
        private const val NOTIFICATION_ID = 42
        private const val CHANNEL_ID = "session_notification_channel_id"
    }

    /**
     * Este método se llama cuando se crea el servicio.
     *
     * En este método, se inicializan las instancias de ExoPlayer y MediaSession y se establece el MediaSessionServiceListener.
     */
    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate() // Call the superclass method

        // Create an ExoPlayer instance
        val player = ExoPlayer.Builder(this).build()

        // Create a MediaSession instance
        _mediaSession = MediaSession.Builder(this, player)
            .build() // Build the MediaSession instance

        // Set the listener for the MediaSessionService
        setListener(MediaSessionServiceListener())
    }

    /**
     * Este método se llama cuando se solicita una MediaSession.
     *
     * En este método, se devuelve la MediaSession que se creó en onCreate().
     */
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return _mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        // Get the player from the media session
        val player = mediaSession.player

        // Check if the player is not ready to play or there are no items in the media queue
        if (!player.playWhenReady || player.mediaItemCount == 0) {
            // Stop the service
            stopSelf()
        }
    }

    /**
     * Este método se llama cuando se destruye el servicio.
     *
     * En este método, se libera el reproductor de audio.
     */
    @OptIn(UnstableApi::class)
    override fun onDestroy() {
        // If _mediaSession is not null, run the following block
        _mediaSession?.run {
            // Release the player
            player.release()
            // Release the MediaSession instance
            release()
            // Set _mediaSession to null
            _mediaSession = null
        }
        // Clear the listener
        clearListener()
        // Call the superclass method
        super.onDestroy()
    }

    @UnstableApi
    private inner class MediaSessionServiceListener : Listener {

        /**
         * This method is only required to be implemented on Android 12 or above when an attempt is made
         * by a media controller to resume playback when the {@link MediaSessionService} is in the
         * background.
         */
        override fun onForegroundServiceStartNotAllowedException() {
            if (
                Build.VERSION.SDK_INT >= 33 &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                // Notification permission is required but not granted
                return
            }
            val notificationManagerCompat =
                NotificationManagerCompat.from(this@MyMediaSessionService)
            ensureNotificationChannel(notificationManagerCompat)
            val builder =
                NotificationCompat.Builder(this@MyMediaSessionService, CHANNEL_ID)
                    .setSmallIcon(androidx.media3.session.R.drawable.media3_notification_small_icon)
                    .setContentTitle("Payback no puede continuar")
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText("Press on the play button on the media notification if it is still present, otherwise please open the app to start the playback and re-connect the session to the controller")
                    )
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun ensureNotificationChannel(notificationManagerCompat: NotificationManagerCompat) {
        if (
            Build.VERSION.SDK_INT < 26 ||
            notificationManagerCompat.getNotificationChannel(CHANNEL_ID) != null
        ) {
            return
        }

        val channel =
            NotificationChannel(
                CHANNEL_ID,
                "Payback no puede continuar",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        notificationManagerCompat.createNotificationChannel(channel)
    }
}

// TODO(" manejar el audiofocus")
/*
import android.content.Context
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener

private lateinit var audioManager: AudioManager
private var audioFocusRequest: AudioFocusRequest? = null
private val audioFocusChangeListener = OnAudioFocusChangeListener { focusChange ->
    when (focusChange) {
        AudioManager.AUDIOFOCUS_LOSS -> {
            // Handle loss of audio focus
        }
        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
            // Handle temporary loss of audio focus
        }
        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
            // Handle loss of audio focus where lowering volume is allowed
        }
        AudioManager.AUDIOFOCUS_GAIN -> {
            // Handle regain of audio focus
        }
    }
}

private fun requestAudioFocus(): Boolean {
    audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val result: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && audioFocusRequest == null) {
        audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
            setOnAudioFocusChangeListener(audioFocusChangeListener)
            build()
        }
        audioManager.requestAudioFocus(audioFocusRequest!!)
    } else {
        audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
    }
    return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
}

private fun abandonAudioFocus() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && audioFocusRequest != null) {
        audioManager.abandonAudioFocusRequest(audioFocusRequest!!)
    } else {
        audioManager.abandonAudioFocus(audioFocusChangeListener)
    }
}
 */