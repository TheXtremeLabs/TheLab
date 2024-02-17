package com.riders.thelab.feature.songplayer.ui

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Environment
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.util.fastAny
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.media.session.MediaButtonReceiver
import com.riders.thelab.core.common.storage.LabFileManager
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.common.utils.LabNotificationManager
import com.riders.thelab.core.data.local.model.music.SongModel
import com.riders.thelab.core.data.utils.Constants
import com.riders.thelab.core.player.service.PlaybackService
import com.riders.thelab.feature.songplayer.core.SongsManager
import com.riders.thelab.feature.songplayer.utils.SongPlayerUtils
import com.riders.thelab.feature.songplayer.utils.parseSongName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.File
import java.util.Random
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class SongPlayerViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel(), DefaultLifecycleObserver,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    //////////////////////////////////////////
    // Variables
    //////////////////////////////////////////
    private var listCreationIndex: Int = 0

    private var mMediaButtonReceiver: MediaButtonReceiver? = null

    // Media Player
    private var mp: MediaPlayer? = null

    // Handler to update UI timer, progress bar etc,.
    private var mHandler: Handler? = null
    private var songManager: SongsManager? = null
    // private val seekForwardTime = 5000 // 5000 milliseconds
    // private val seekBackwardTime = 5000 // 5000 milliseconds


    private val isShuffle = false
    private val isRepeat = false

    /**
     * Background Runnable thread
     */
    private val mUpdateTimeTask: Runnable = object : Runnable {
        override fun run() {

            mp?.let {
                val totalDuration = it.duration.toLong()
                val currentDuration = it.currentPosition.toLong()

                // Displaying Total Duration time
                // Timber.d("mUpdateTimeTask | run() | Current duration: $currentDuration on Total Duration time: $totalDuration")

                // Updating progress bar
                val progress = SongPlayerUtils.getProgressPercentage(currentDuration, totalDuration)
                // Timber.d("mUpdateTimeTask | run() | progress: $progress")

                if (!it.isPlaying) {

                } else {
                    updateCurrentProgress(progress)

                    @Suppress("DEPRECATION")
                    mHandler = Handler()
                    // Running this thread after 100 milliseconds
                    mHandler?.postDelayed(this, 100)
                }
            }
        }
    }

    private lateinit var mServiceMusic: PlaybackService
    private var mBound: Boolean = false

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as PlaybackService.LocalBinder
            mServiceMusic = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }


    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    // Songs list
    val songList: SnapshotStateList<SongModel> = mutableStateListOf()
    var currentSongIndex: Int by mutableIntStateOf(-1)
        private set
    private var isPlayerCardVisible: Boolean by mutableStateOf(false)
    var isPlayerCardExpanded: Boolean by mutableStateOf(false)
        private set
    private var isPlaying: Boolean by mutableStateOf(false)
        private set
    var currentSongProgress: Float by mutableFloatStateOf(0f)
        private set

    val playPauseState: Boolean by derivedStateOf { isPlaying }

    val isAnySongPlaying: Boolean by derivedStateOf {
        if (songList.isEmpty()) {
            false
        } else {
            songList.fastAny { it.isPlaying }
        }
    }


    private fun updateSongList(newSongList: List<SongModel>) {
        songList.addAll(newSongList)
    }

    fun updateCurrentSongIndex(newIndex: Int) {
        Timber.d("updateCurrentSongIndex() | newIndex: $newIndex")
        this.currentSongIndex = newIndex
    }

    private fun updateIsPlayerCardVisible(cardVisible: Boolean) {
        this.isPlayerCardVisible = cardVisible
    }

    fun toggleViewToggle(cardExpanded: Boolean) {
        this.isPlayerCardExpanded = cardExpanded
    }

    fun updateCurrentProgress(newProgress: Float) {
        this.currentSongProgress = newProgress
    }

    //////////////////////////////////////////
    //
    // Class Methods
    //
    //////////////////////////////////////////
    fun init() {
        Timber.d("init()")
        // Media Player
        mp = MediaPlayer()

        mp?.let {
            it.setOnPreparedListener(this)
            it.setOnErrorListener(this)
            it.setOnCompletionListener(this)
        }
        songManager = SongsManager()
        mMediaButtonReceiver = MediaButtonReceiver()
    }

    fun retrieveSongFiles(context: Context) {
        Timber.d("retrieveSongFiles()")

        val fullList: MutableList<String> = mutableListOf()

        runCatching {
            if (!LabCompatibilityManager.isAndroid10()) {
                val scCardFiles: List<String>? = LabFileManager.getSdCardPaths(context, true)
                val downloadFiles: List<String>? =
                    (File(Environment.DIRECTORY_DOWNLOADS).listFiles()?.map { it.absolutePath })

                fullList.addAll(scCardFiles ?: emptyList())
                fullList.addAll(downloadFiles ?: emptyList())
            } else {
                val scCardFiles: List<String>? = LabFileManager.getSdCardPaths(context, true)
                val downloadFiles: String =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
                val musicFiles: String =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath


                fullList.addAll(scCardFiles ?: emptyList())
                fullList.add(downloadFiles)
                fullList.add(musicFiles)
            }

            fullList.forEach { volumePath ->
                Timber.e("volumePath: $volumePath")
                if (volumePath.contains("download", true)) {
                    updateSongList(getFilesWithPath(volumePath))
                }
                if (volumePath.contains("music", true)) {
                    updateSongList(getFilesWithPath(volumePath))
                }

                if (volumePath.contains("0000")) {
                    updateSongList(getFilesWithPath(volumePath))
                }
            }

        }
            .onFailure {
                Timber.e("runCatching - onFailure() | Error caught: ${it.message}")
            }
            .onSuccess {
                Timber.d("runCatching - onSuccess() | app list fetched successfully")
            }

    }

    private fun getFilesWithPath(path: String): List<SongModel> = runCatching {
        Timber.d("getFilesWithPath() | path: $path | runCatching...")
        val fileList = mutableListOf<SongModel>()

        // Create file with path (can be either a folder or a file)
        val f = File(path)
        // Get list if it's a directory
        val files = f.listFiles() ?: return fileList.toList()

        if (path.contains(DOWNLOAD_PLACEHOLDER)) {
            files.forEachIndexed { _, musicFile ->
                if (musicFile.name.parseSongName().isNotEmpty()) {
                    Timber.e("musicFile : ${musicFile.absolutePath}")

                    fileList.add(
                        SongModel(
                            listCreationIndex,
                            musicFile.name.parseSongName(),
                            musicFile.absolutePath,
                            "",
                            false
                        )
                    )
                    listCreationIndex++
                }
            }
        }


        // Root Music directory path
        val musicPath =
            files
                .find { inFile -> inFile.isDirectory && inFile.name == MUSIC_PLACEHOLDER }
                .toString()

        Timber.e("musicPathname : $musicPath")

        if (musicPath.isBlank()) {
            return fileList.toList()
        }

        val musicDirectory = File(musicPath)
        val musicFiles = musicDirectory.listFiles() ?: return fileList

        // Root Prod directory path
        val prodPath =
            musicFiles
                .find { musicFile -> musicFile.isDirectory && musicFile.name == PROD_PLACEHOLDER }
                .toString()

        Timber.e("prodPathname : $prodPath")

        if (prodPath.isBlank()) {
            return fileList.toList()
        }

        val prodDirectory = File(prodPath)
        val prodFiles = prodDirectory.listFiles() ?: return fileList

        prodFiles.forEachIndexed { _, prodFile ->
            fileList.add(
                SongModel(
                    listCreationIndex,
                    prodFile.name.parseSongName(),
                    prodPath + Constants.SZ_SEPARATOR + prodFile.name,
                    "",
                    false
                )
            )
            listCreationIndex++
        }

        fileList.toList()
    }
        .onFailure {
            it.printStackTrace()
            Timber.e("runCatching | onFailure | error caught class: ${it.javaClass.simpleName}, with message: ${it.message}")
        }
        .onSuccess {
            Timber.d("runCatching | onSuccess | files fetched successfully")
        }
        .getOrDefault(listOf())


    @SuppressLint("InlinedApi")
    fun playSong(context: Context, songId: Int) {

        songList.forEach {
            if (it.id != songId) {
                it.isPlaying = false
            }
        }

        songList.firstOrNull { it.id == songId }?.let { songModel ->
            runCatching {
                Timber.d("playSong() | ${songModel.path}")

                if (!isPlayerCardVisible) {
                    isPlayerCardVisible = true
                }

                mp?.let { player ->
                    player.reset()
                    player.setDataSource(songModel.path)
                    player.prepare()
                    // Play song
                    player.start()
                }

                songModel.isPlaying = true

                // Displaying Song title via Notification
                LabNotificationManager.createNotificationChannel(
                    context,
                    context.getString(com.riders.thelab.core.ui.R.string.music_channel_name),
                    context.getString(com.riders.thelab.core.ui.R.string.music_channel_description),
                    NotificationManager.IMPORTANCE_HIGH,
                    Constants.NOTIFICATION_MUSIC_CHANNEL_ID
                )

                val mediaSession = mp?.run {
                    SongPlayerUtils.createMediaSession(
                        context,
                        this,
                        songModel.name,
                        songModel.path,
                        LabFileManager.getDrawableURI(
                            context,
                            com.riders.thelab.core.ui.R.drawable.ic_music
                        )
                    )
                }
                val mediaController =
                    mediaSession?.run { SongPlayerUtils.createMediaController(this) }

                mediaSession?.let { session ->
                    mediaController?.let { controller ->
                        LabNotificationManager.displayMusicNotification(
                            context = context,
                            session,
                            controller,
                            mServiceMusic,
                            smallIcon = com.riders.thelab.core.ui.R.drawable.ic_music,
                            contentTitle = songModel.name,
                            contentText = songModel.path,
                            largeIcon = com.riders.thelab.core.ui.R.drawable.ic_music,
                            actionIcon = if (!mp!!.isPlaying) com.riders.thelab.core.ui.R.drawable.ic_play else com.riders.thelab.core.ui.R.drawable.ic_pause,
                            actionTitle = if (!mp!!.isPlaying) com.riders.thelab.core.ui.R.string.action_play else com.riders.thelab.core.ui.R.string.action_pause
                        )

                        // Updating progress bar
                        updateProgressBar()
                    }
                }
            }
                .onFailure {
                    it.printStackTrace()
                    Timber.e("runCatching | onFailure | error caught class: ${it.javaClass.simpleName}, with message: ${it.message}")
                }
        } ?: run { Timber.e("Unable to find song item with id $songId") }
    }

    /**
     * Update timer on seekbar
     */
    private fun updateProgressBar() {
        if (null == mHandler) {
            mHandler = Handler(Looper.getMainLooper())
        }

        mHandler?.postDelayed(mUpdateTimeTask, 100)
    }


    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Timber.d("onStart()")
        // Bind to LocalService
        Intent(context, PlaybackService::class.java).also { intent ->
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Timber.e("onPause()")
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Timber.d("onResume()")

        runCatching {
            if (null == mMediaButtonReceiver) {
                mMediaButtonReceiver = MediaButtonReceiver()
            }
            context.registerReceiver(mMediaButtonReceiver, IntentFilter(Intent.ACTION_MEDIA_BUTTON))
        }
            .onFailure {
                Timber.e("runCatching - onFailure() | Error caught: ${it.message}")
            }
            .onSuccess {
                Timber.d("runCatching - onSuccess() | app list fetched successfully")
            }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Timber.e("onStop()")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onStop(owner)
        Timber.e("onDestroy()")

        runCatching {
            if (true == mp?.isPlaying) {
                mp?.let {
                    it.stop()
                    it.reset()
                    it.release()
                }
            }
        }
            .onFailure {
                Timber.e("runCatching - onFailure() | Error caught: ${it.message}")
            }
            .onSuccess {
                Timber.d("runCatching - onSuccess()")
                mp = null
            }

        mHandler = null

        context.unregisterReceiver(mMediaButtonReceiver)
        context.unbindService(connection)
        mBound = false
    }

    override fun onPrepared(mp: MediaPlayer?) {
        Timber.d("onPrepared()")
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Timber.e("onError() | what: $what | extra: $extra")
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        Timber.i("onCompletion()")

        // check for repeat is ON or OFF
        if (isRepeat) {
            // repeat is on play same song again
            playSong(context, songList[currentSongIndex].id)
        } else if (isShuffle) {
            // shuffle is on - play a random song
            val rand = Random()
            currentSongIndex = rand.nextInt(songList.size - 1 - 0 + 1) + 0
            playSong(context, songList[currentSongIndex].id)
        } else {
            // no repeat or shuffle ON - play next song
            updateCurrentSongIndex(if (currentSongIndex < songList.size - 1) currentSongIndex + 1 else 0)
            playSong(context, songList[currentSongIndex].id)
        }
    }

    fun togglePlayPauseSong() {
        Timber.d("togglePlayPauseSong()")

        mp?.let {
            if (!it.isPlaying) {
                it.start()
                songList[currentSongIndex].isPlaying = true
            } else {
                it.pause()
                songList[currentSongIndex].isPlaying = false
            }
        }
    }

    fun playPreviousSong(context: Context) {
        Timber.d("playPreviousSong()")
        if (currentSongIndex > 0) {
            playSong(context, currentSongIndex - 1)
            currentSongIndex -= 1
        } else {
            // play last song
            updateCurrentSongIndex(songList.last().id)
            playSong(context, songList.last().id)
        }
    }

    fun playNextSong(context: Context) {
        Timber.d("playPreviousSong()")
        // check if next song is there or not
        if (currentSongIndex < (songList.size - 1)) {
            playSong(context, currentSongIndex + 1)
            currentSongIndex += 1
        } else {
            // play first song
            updateCurrentSongIndex(songList.first().id)
            playSong(context, songList.first().id)
        }
    }

    companion object {
        const val MUSIC_PLACEHOLDER = "Music"
        const val PROD_PLACEHOLDER = "Prod"
        const val DOWNLOAD_PLACEHOLDER = "Download"
    }
}