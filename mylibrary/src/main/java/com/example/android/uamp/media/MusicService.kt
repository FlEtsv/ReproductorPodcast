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

package com.example.android.uamp.media

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.ConditionVariable
import android.util.Log
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.media3.cast.CastPlayer
import androidx.media3.cast.SessionAvailabilityListener
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.EVENT_MEDIA_ITEM_TRANSITION
import androidx.media3.common.Player.EVENT_PLAY_WHEN_READY_CHANGED
import androidx.media3.common.Player.EVENT_POSITION_DISCONTINUITY
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.example.android.uamp.media.library.BrowseTree
import com.example.android.uamp.media.library.DomainMediaSource
import com.example.android.uamp.media.library.MEDIA_SEARCH_SUPPORTED
import com.example.android.uamp.media.library.MusicSource
import com.example.android.uamp.media.library.UAMP_BROWSABLE_ROOT
import com.example.android.uamp.media.library.UAMP_RECENT_ROOT
import com.google.android.gms.cast.framework.CastContext
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.universae.domain.Sesion
import com.universae.domain.SesionObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.math.max
import kotlin.math.min

/**
 * Servicio para explorar el catálogo y recibir un [MediaController] desde la interfaz de usuario
 * de la aplicación y otras aplicaciones que deseen reproducir música a través de UAMP (por ejemplo,
 * Android Auto o Google Assistant).
 *
 * La exploración comienza con el método [MusicService.MusicServiceCallback.onGetLibraryRoot], y
 * continúa en el callback [MusicService.MusicServiceCallback.onGetChildren].
 *
 * Esta clase también maneja la reproducción para sesiones de Cast. Cuando una sesión de Cast está
 * activa, los comandos de reproducción se pasan a un [CastPlayer].
 */
@OptIn(UnstableApi::class)
open class MusicService : MediaLibraryService(), SesionObserver {

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    protected lateinit var mediaSession: MediaLibrarySession
    private var currentMediaItemIndex: Int = 0

    private lateinit var musicSource: MusicSource
    private lateinit var packageValidator: PackageValidator
    private lateinit var storage: PersistentStorage

    /**
     * Esto debe ser `by lazy` porque el [musicSource] no estará inicialmente listo. Use
     * [callWhenMusicSourceReady] para asegurarse de que esté listo de manera segura para su uso.
     */
    val browseTree: BrowseTree by lazy {
        BrowseTree(applicationContext, musicSource)
    }

    private val recentRootMediaItem: MediaItem by lazy {
        MediaItem.Builder()
            .setMediaId(UAMP_RECENT_ROOT)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setFolderType(MediaMetadata.FOLDER_TYPE_ALBUMS)
                    .setIsPlayable(false)
                    .build())
            .build()
    }

    private val catalogueRootMediaItem: MediaItem by lazy {
        MediaItem.Builder()
            .setMediaId(UAMP_BROWSABLE_ROOT)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setFolderType(MediaMetadata.FOLDER_TYPE_ALBUMS)
                    .setIsPlayable(false)
                    .build())
            .build()
    }

    private val executorService by lazy {
        MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor())
    }

    private val uAmpAudioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    private val playerListener = PlayerEventListener()

    /**
     * Configura ExoPlayer para manejar el enfoque de audio por nosotros. Consulte [ExoPlayer.Builder.setAudioAttributes]
     * para obtener más detalles.
     */
    private val exoPlayer: Player by lazy {
        val player = ExoPlayer.Builder(this).build().apply {
            setAudioAttributes(uAmpAudioAttributes, true)
            setHandleAudioBecomingNoisy(true)
            addListener(playerListener)
        }
        player.addAnalyticsListener(EventLogger(null, "exoplayer-uamp"))
        player
    }

    /**
     * Si Cast está disponible, crea un CastPlayer para manejar la comunicación con una sesión de Cast.
     */
    private val castPlayer: CastPlayer? by lazy {
        try {
            val castContext = CastContext.getSharedInstance(this)
            CastPlayer(castContext, CastMediaItemConverter()).apply {
                setSessionAvailabilityListener(UampCastSessionAvailabilityListener())
                addListener(playerListener)
            }
        } catch (e: Exception) {
            // Normalmente no atraparíamos la excepción genérica `Exception`, sin embargo,
            // llamar a `CastContext.getSharedInstance` puede lanzar varias excepciones, todas
            // las cuales indican que Cast no está disponible.
            // Bug interno relacionado b/68009560.
            Log.i(
                TAG, "Cast no está disponible en este dispositivo. " +
                        "Excepción lanzada al intentar obtener CastContext. " + e.message)
            null
        }
    }

    val replaceableForwardingPlayer: ReplaceableForwardingPlayer by lazy {
        ReplaceableForwardingPlayer(exoPlayer)
    }

    /**
     * @return el [MediaLibrarySessionCallback] que se usará para construir la sesión de medios.
     */
    open fun getCallback(): MediaLibrarySession.Callback {
        return MusicServiceCallback()
    }

    override fun onCreate() {
        super.onCreate()

        if (castPlayer?.isCastSessionAvailable == true) {
            replaceableForwardingPlayer.setPlayer(castPlayer!!)
        }

        mediaSession = with(MediaLibrarySession.Builder(
            this, replaceableForwardingPlayer, getCallback())) {
            setId(packageName)
            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                setSessionActivity(
                    PendingIntent.getActivity(
                        /* context= */ this@MusicService,
                        /* requestCode= */ 0,
                        sessionIntent,
                        if (Build.VERSION.SDK_INT >= 23) FLAG_IMMUTABLE
                        else FLAG_UPDATE_CURRENT
                    )
                )
            }
            build()
        }

        try {
            musicSource = DomainMediaSource(Sesion)
            serviceScope.launch {
                musicSource.load()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading music source", e)
        }

        packageValidator = PackageValidator(this, R.xml.allowed_media_browser_callers)
        storage = PersistentStorage.getInstance(applicationContext)

        Sesion.addObserver(this)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return if ("android.media.session.MediaController" == controllerInfo.packageName
            || packageValidator.isKnownCaller(controllerInfo.packageName, controllerInfo.uid)) {
            mediaSession
        } else null
    }

    /** Llamado al deslizar la actividad para eliminarla de recientes. */
    override fun onTaskRemoved(rootIntent: Intent) {
        saveRecentSongToStorage()
        super.onTaskRemoved(rootIntent)
        // La elección de qué hacer aquí es específica de la aplicación. Algunas aplicaciones detienen la reproducción,
        // mientras que otras permiten que continúe la reproducción y permiten a los usuarios detenerla con la notificación.
        releaseMediaSession()
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMediaSession()
    }

    private fun releaseMediaSession() {
        mediaSession.run {
            release()
            if (player.playbackState != Player.STATE_IDLE) {
                player.removeListener(playerListener)
                player.release()
            }
        }
        // Cancelar las corutinas cuando el servicio está desapareciendo.
        serviceJob.cancel()
    }

    private fun saveRecentSongToStorage() {
        // Obtener los detalles de la canción actual *antes* de guardarlos en un hilo separado, de lo contrario
        // el reproductor actual puede haber sido descargado en el momento en que se ejecuta la rutina de guardado.
        val currentMediaItem = replaceableForwardingPlayer.currentMediaItem ?: return
        serviceScope.launch {
            val mediaItem =
                browseTree.getMediaItemByMediaId(currentMediaItem.mediaId) ?: return@launch
            storage.saveRecentSong(mediaItem, replaceableForwardingPlayer.currentPosition)
        }
    }

    private fun preparePlayerForResumption(mediaItem: MediaItem) {
        musicSource.whenReady {
            if (it) {
                val playableMediaItem = browseTree.getMediaItemByMediaId(mediaItem.mediaId)
                val albumMediaItems: List<MediaItem> =
                    browseTree.getMediaItemsInAlbum(albumTitle = playableMediaItem?.mediaMetadata?.albumTitle.toString())

                val selectedItemIndex: Int = albumMediaItems.indexOfFirst {
                    it.mediaId == (playableMediaItem?.mediaId ?: -1)
                }

                if (selectedItemIndex != -1) {
                    exoPlayer.setMediaItems(albumMediaItems)
                    exoPlayer.prepare()
                    exoPlayer.seekTo(selectedItemIndex, C.TIME_UNSET)
                    exoPlayer.playWhenReady = true

                    // Actualizar la cola de reproducción y la metadata del elemento de medios actual
                    exoPlayer.setMediaItems(albumMediaItems.mapIndexed { index, item ->
                        item.buildUpon().setMediaId(index.toString()).build()
                    })
                    if (playableMediaItem != null) {
                        exoPlayer.setMediaItem(playableMediaItem)
                    }
                }
            }
        }
    }

    /**
     * Devuelve una función que abre la variable de condición cuando se llama.
     */
    private fun openWhenReady(conditionVariable: ConditionVariable): (Boolean) -> Unit = {
        val successfullyInitialized = it
        if (!successfullyInitialized) {
            Log.e(TAG, "loading music source failed")
        }
        conditionVariable.open()
    }

    /**
     * Devuelve un futuro que ejecuta la acción cuando la fuente de música está lista. Esto puede ser una
     * ejecución inmediata si la fuente de música está lista, o una ejecución asincrónica diferida si la
     * fuente de música todavía se está cargando.
     *
     * @param action La función que se llamará cuando la fuente de música esté lista.
     */
    private fun <T> callWhenMusicSourceReady(action: () -> T): ListenableFuture<T> {
        val conditionVariable = ConditionVariable()
        return if (musicSource.whenReady(openWhenReady(conditionVariable))) {
            Futures.immediateFuture(action())
        } else {
            executorService.submit<T> {
                conditionVariable.block()
                action()
            }
        }
    }

    open inner class MusicServiceCallback : MediaLibrarySession.Callback {

        override fun onGetLibraryRoot(
            session: MediaLibrarySession, browser: MediaSession.ControllerInfo, params: LibraryParams?
        ): ListenableFuture<LibraryResult<MediaItem>> {
            val isKnownCaller = packageValidator.isKnownCaller(browser.packageName, browser.uid)
            val rootExtras = Bundle().apply {
                putBoolean(
                    MEDIA_SEARCH_SUPPORTED,
                    isKnownCaller || browseTree.searchableByUnknownCaller
                )
                putBoolean(CONTENT_STYLE_SUPPORTED, true)
                putInt(CONTENT_STYLE_BROWSABLE_HINT, CONTENT_STYLE_GRID)
                putInt(CONTENT_STYLE_PLAYABLE_HINT, CONTENT_STYLE_LIST)
            }
            val libraryParams = LibraryParams.Builder().setExtras(rootExtras).build()
            val rootMediaItem = if (!isKnownCaller) {
                MediaItem.EMPTY
            } else if (params?.isRecent == true) {
                if (exoPlayer.currentTimeline.isEmpty) {
                    storage.loadRecentSong()?.let {
                        preparePlayerForResumption(it)
                    }
                }
                recentRootMediaItem
            } else {
                catalogueRootMediaItem
            }
            return Futures.immediateFuture(LibraryResult.ofItem(rootMediaItem, libraryParams))
        }

        override fun onGetChildren(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            parentId: String,
            page: Int,
            pageSize: Int,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
            if (parentId == recentRootMediaItem.mediaId) {
                return Futures.immediateFuture(
                    LibraryResult.ofItemList(
                        storage.loadRecentSong()?.let { song -> listOf(song) }!!,
                        LibraryParams.Builder().build()
                    )
                )
            }
            return callWhenMusicSourceReady {
                LibraryResult.ofItemList(
                    browseTree[parentId] ?: ImmutableList.of(),
                    LibraryParams.Builder().build()
                )
            }
        }

        override fun onGetItem(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            mediaId: String
        ): ListenableFuture<LibraryResult<MediaItem>> {
            return callWhenMusicSourceReady {
                when (mediaId) {
                    recentRootMediaItem.mediaId -> {
                        LibraryResult.ofItem(recentRootMediaItem, LibraryParams.Builder().build())
                    }
                    catalogueRootMediaItem.mediaId -> {
                        LibraryResult.ofItem(catalogueRootMediaItem, LibraryParams.Builder().build())
                    }
                    UAMP_BROWSABLE_ROOT -> {
                        LibraryResult.ofItem(
                            browseTree.getMediaItemByMediaId(UAMP_BROWSABLE_ROOT) ?: MediaItem.EMPTY,
                            LibraryParams.Builder().build()
                        )
                    }
                    UAMP_RECENT_ROOT -> {
                        LibraryResult.ofItem(
                            browseTree.getMediaItemByMediaId(UAMP_RECENT_ROOT) ?: MediaItem.EMPTY,
                            LibraryParams.Builder().build()
                        )
                    }
                    else -> {
                        val mediaItem = browseTree.getMediaItemByMediaId(mediaId)
                        if (mediaItem != null) {
                            LibraryResult.ofItem(mediaItem, LibraryParams.Builder().build())
                        } else {
                            Log.e(TAG, "Unknown mediaId: $mediaId")
                            LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE, null)
                        }
                    }
                }
            }
        }

        override fun onSearch(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            query: String,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<Void>> {
            return callWhenMusicSourceReady {
                val searchResult = musicSource.search(query, params?.extras ?: Bundle())
                mediaSession.notifySearchResultChanged(browser, query, searchResult.size, params)
                LibraryResult.ofVoid()
            }
        }

        override fun onGetSearchResult(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            query: String,
            page: Int,
            pageSize: Int,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
            return callWhenMusicSourceReady {
                val searchResult = musicSource.search(query, params?.extras ?: Bundle())
                val fromIndex = max((page - 1) * pageSize, 0)
                val toIndex = min(fromIndex + pageSize, searchResult.size)
                LibraryResult.ofItemList(searchResult.subList(fromIndex, toIndex), params)
            }
        }

        override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: MutableList<MediaItem>
        ): ListenableFuture<MutableList<MediaItem>> {
            return callWhenMusicSourceReady {
                mediaItems.mapNotNull { browseTree.getMediaItemByMediaId(it.mediaId) }.toMutableList()
            }
        }

        override fun onCustomCommand(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
            customCommand: SessionCommand,
            args: Bundle
        ): ListenableFuture<SessionResult> {
            return Futures.immediateFuture(SessionResult(SessionResult.RESULT_ERROR_NOT_SUPPORTED))
        }
    }

    private inner class UampCastSessionAvailabilityListener : SessionAvailabilityListener {

        /**
         * Llamado cuando se ha iniciado una sesión de Cast y el usuario desea controlar la reproducción en un
         * receptor de Cast remoto en lugar de reproducir audio localmente.
         */
        override fun onCastSessionAvailable() {
            replaceableForwardingPlayer.setPlayer(castPlayer!!)
        }

        /**
         * Llamado cuando ha finalizado una sesión de Cast y el usuario desea controlar la reproducción localmente.
         */
        override fun onCastSessionUnavailable() {
            replaceableForwardingPlayer.setPlayer(exoPlayer)
        }
    }

    /** Escuchar eventos de ExoPlayer. */
    private var lastMediaItem: MediaItem? = null

    private inner class PlayerEventListener : Player.Listener {
        private var currentMediaItem: MediaItem? = null
        private var playbackPosition: Long = 0

        override fun onEvents(player: Player, events: Player.Events) {
            if (events.contains(EVENT_MEDIA_ITEM_TRANSITION)) {
                val selectedMediaItem: MediaItem? = replaceableForwardingPlayer.currentMediaItem
                val recentMediaItem = storage.loadRecentSong()

                if (selectedMediaItem == recentMediaItem) {
                    exoPlayer.setMediaItem(recentMediaItem!!)
                    exoPlayer.prepare()
                    exoPlayer.playWhenReady = true
                } else {
                    val albumMediaItems: List<MediaItem> = browseTree.getMediaItemsInAlbum(albumTitle = selectedMediaItem?.mediaMetadata?.albumTitle.toString())

                    val selectedItemIndex: Int = albumMediaItems.indexOfFirst {
                        it.mediaId == (selectedMediaItem?.mediaId ?: "")
                    }

                    if (exoPlayer.mediaItemCount != albumMediaItems.size) {
                        exoPlayer.setMediaItems(albumMediaItems)
                        exoPlayer.prepare()
                    }

                    val position = when {
                        (currentMediaItem?.mediaId ?: -2) != (selectedMediaItem?.mediaId ?: -1) -> 0L
                        selectedMediaItem == recentMediaItem && recentMediaItem != null ->
                            recentMediaItem.mediaMetadata.extras?.getLong(MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS, 0L) ?: 0L
                        else -> playbackPosition
                    }

                    if (selectedItemIndex in 0 until exoPlayer.mediaItemCount && position >= 0) {
                        try {
                            exoPlayer.seekTo(selectedItemIndex, position)
                        } catch (e: IllegalArgumentException) {
                            exoPlayer.seekTo(0, 0L)
                        }
                    }

                    exoPlayer.playWhenReady = true
                    currentMediaItem = selectedMediaItem
                }
            }

            if (events.contains(EVENT_MEDIA_ITEM_TRANSITION) || events.contains(EVENT_POSITION_DISCONTINUITY) || events.contains(EVENT_PLAY_WHEN_READY_CHANGED)) {

                // Guardar el último MediaItem
                lastMediaItem?.let {
                    serviceScope.launch {
                        storage.saveRecentSong(it, playbackPosition)
                        browseTree.updateRecentTrack(it)
                    }
                }

                // Actualizar lastMediaItem a currentMediaItem antes de cambiar a uno nuevo
                lastMediaItem = currentMediaItem

                // Actualizar currentMediaItem al nuevo MediaItem y almacenar su posición
                currentMediaItem = player.currentMediaItem
                playbackPosition = player.currentPosition

                if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED)) {
                    if (player.playbackState == Player.STATE_ENDED) {
                        currentMediaItem?.let {
                            serviceScope.launch {
                                storage.clearRecentSong()
                                browseTree.updateRecentTrack(it)
                            }
                        }
                    }
                }
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            var message = R.string.generic_error
            Log.e(TAG, "Player error: " + error.errorCodeName + " (" + error.errorCode + ")", error)
            if (error.errorCode == PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS
                || error.errorCode == PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND
            ) {
                message = R.string.error_media_not_found
            }
            Toast.makeText(
                applicationContext,
                message,
                Toast.LENGTH_LONG
            ).show()
        }

        /**
         * Verifica si un elemento de medios existe por su ID.
         *
         * @param mediaId El ID del elemento de medios a verificar.
         * @return Verdadero si el elemento de medios existe, falso en caso contrario.
         */
        fun mediaItemExists(mediaId: String): Boolean {
            return browseTree.getMediaItemByMediaId(mediaId) != null
        }

        /**
         * Maneja los cambios en el estado playWhenReady.
         *
         * @param playWhenReady Verdadero si la reproducción debe comenzar, falso en caso contrario.
         * @param reason La razón del cambio.
         */
        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            super.onPlayWhenReadyChanged(playWhenReady, reason)
            if (!playWhenReady) {
                playbackPosition = exoPlayer.currentPosition
                serviceScope.launch {
                    storage.saveRecentSong(currentMediaItem!!, playbackPosition)
                }
            }
        }
    }

    override fun onSesionUpdated() {
        // Recargar musicSource y BrowseTree
        serviceScope.launch {
            musicSource.load()
            // Recargar BrowseTree
            browseTree.reload()
        }
    }
}

/** Constantes de estilo de contenido */
private const val CONTENT_STYLE_BROWSABLE_HINT = "android.media.browse.CONTENT_STYLE_BROWSABLE_HINT"
private const val CONTENT_STYLE_PLAYABLE_HINT = "android.media.browse.CONTENT_STYLE_PLAYABLE_HINT"
private const val CONTENT_STYLE_SUPPORTED = "android.media.browse.CONTENT_STYLE_SUPPORTED"
private const val CONTENT_STYLE_LIST = 1
private const val CONTENT_STYLE_GRID = 2

const val MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS = "playback_start_position_ms"

private const val TAG = "MusicService"
