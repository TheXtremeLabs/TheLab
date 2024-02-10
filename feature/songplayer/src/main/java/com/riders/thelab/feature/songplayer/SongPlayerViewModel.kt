package com.riders.thelab.feature.songplayer

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
import android.support.v4.media.session.MediaControllerCompat
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.media.session.MediaButtonReceiver
import com.riders.thelab.core.common.storage.LabFileManager
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.common.utils.LabNotificationManager
import com.riders.thelab.core.data.local.model.music.SongModel
import com.riders.thelab.core.data.utils.Constants
import com.riders.thelab.feature.songplayer.core.SongsManager
import com.riders.thelab.feature.songplayer.core.service.MusicMediaPlaybackService
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
    // Songs list
    val songList: SnapshotStateList<SongModel> = mutableStateListOf()

    var mMediaButtonReceiver: MediaButtonReceiver? = null

    // Media Player
    private lateinit var mp: MediaPlayer

    // Handler to update UI timer, progress bar etc,.
    private var mHandler: Handler? = null
    private var songManager: SongsManager? = null
    // private val seekForwardTime = 5000 // 5000 milliseconds
    // private val seekBackwardTime = 5000 // 5000 milliseconds

    var currentSongIndex: Int by mutableIntStateOf(-1)
        private set

    private val isShuffle = false
    private val isRepeat = false

    /**
     * Background Runnable thread
     */
    private val mUpdateTimeTask: Runnable = object : Runnable {
        override fun run() {
            val totalDuration = mp.duration.toLong()
            val currentDuration = mp.currentPosition.toLong()

            // Displaying Total Duration time
            //songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration))
            // Displaying time completed playing
            //songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration))

            // Updating progress bar
            val progress = SongPlayerUtils.getProgressPercentage(currentDuration, totalDuration)
            Timber.d("progress: $progress")

            updateCurrentProgress((progress * 100).toFloat())
            //Log.d("Progress", ""+progress);
            //if (_viewBinding != null) binding.songProgressBar.progress = progress

            @Suppress("DEPRECATION")
            mHandler = Handler()
            // Running this thread after 100 milliseconds
            mHandler?.postDelayed(this, 100)
        }
    }

    private lateinit var controller: MediaControllerCompat
    private lateinit var mServiceMusic: MusicMediaPlaybackService
    private var mBound: Boolean = false

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MusicMediaPlaybackService.LocalBinder
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
    var isPlayerCardVisible: Boolean by mutableStateOf(false)
        private set
    var isPlayerCardExpanded: Boolean by mutableStateOf(false)
        private set
    var isPlaying: Boolean by mutableStateOf(false)
        private set
    var currentSongProgress: Float by mutableFloatStateOf(0f)
        private set

    val playPauseState: Boolean by derivedStateOf { isPlaying }

    val isAnySongPlaying: Boolean by derivedStateOf {
        if (songList.isEmpty()) {
            false
        } else {
            songList.firstOrNull { it.isPlaying }?.isPlaying ?: false
        }
    }


    fun updateSongList(newSongList: List<SongModel>) {
        songList.addAll(newSongList)
    }

    fun updateSongIsPlaying(songId: Int) {
        Timber.d("updateSongIsPlaying() | songId: $songId")

        if (currentSongIndex == songId) {
            Timber.e("updateSongIsPlaying() | Song is already selected")
            return
        }

        currentSongIndex = songId

        songList.forEach { it.isPlaying = false }
        songList.first { it.id == currentSongIndex }.isPlaying = true
    }

    fun updateIsPlayerCardVisible(cardVisible: Boolean) {
        this.isPlayerCardVisible = cardVisible
    }

    fun toggleViewToggle(cardExpanded: Boolean) {
        this.isPlayerCardExpanded = cardExpanded
    }

    fun updateIsPlaying(playing: Boolean) {
        this.isPlaying = playing
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
        mp.setOnPreparedListener(this)
        mp.setOnErrorListener(this)
        mp.setOnCompletionListener(this)
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
                    getFilesWithPath(volumePath)?.let { updateSongList(it) }
                }
                if (volumePath.contains("music", true)) {
                    getFilesWithPath(volumePath)?.let { updateSongList(it) }
                }

                if (volumePath.contains("0000")) {
                    getFilesWithPath(volumePath)?.let { updateSongList(it) }
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
            files.forEachIndexed { index, musicFile ->
                if (musicFile.name.parseSongName().isNotEmpty()) {

                    Timber.e("musicFile : ${musicFile.absolutePath}")

                    fileList.add(
                        SongModel(
                            index,
                            musicFile.name.parseSongName(),
                            musicFile.absolutePath,
                            "",
                            false
                        )
                    )
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

        fileList.addAll(
            prodFiles.mapIndexed { index, prodFile ->
                SongModel(
                    index,
                    prodFile.name.parseSongName(),
                    prodPath + Constants.SZ_SEPARATOR + prodFile.name,
                    "",
                    false
                )
            }
        )

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

    private fun String.parseSongName(): String = when {
        this.endsWith(".mp3", true) -> {
            this.split(".mp3")[0]
        }

        this.endsWith(".m4a", true) -> {
            this.split(".m4a")[0]
        }

        else -> {
            ""
        }
    }

    @SuppressLint("InlinedApi")
    fun playSong(context: Context, songId: Int) {
        songList.firstOrNull { it.id == songId }?.let {
            runCatching {
                Timber.d("playSong() | ${it.path}")

                if (!isPlayerCardVisible) {
                    updateIsPlayerCardVisible(true)
                }

                // Play song
                mp.reset()
                mp.setDataSource(it.path)
                mp.prepare()
                mp.start()

                // Displaying Song title via Notification
                LabNotificationManager.createNotificationChannel(
                    context,
                    context.getString(com.riders.thelab.core.ui.R.string.music_channel_name),
                    context.getString(com.riders.thelab.core.ui.R.string.music_channel_description),
                    NotificationManager.IMPORTANCE_HIGH,
                    Constants.NOTIFICATION_MUSIC_CHANNEL_ID
                )

                val mediaSession = SongPlayerUtils.createMediaSession(context, mp)
                val mediaController = SongPlayerUtils.createMediaController(mediaSession)

                LabNotificationManager.displayMusicNotification(
                    context = context,
                    mediaSession,
                    mediaController,
                    mServiceMusic,
                    smallIcon = com.riders.thelab.core.ui.R.drawable.ic_music,
                    contentTitle = it.name,
                    contentText = it.path,
                    largeIcon =
                    com.riders.thelab.core.ui.R.drawable.logo_colors,
                    actionIcon = com.riders.thelab.core.ui.R.drawable.ic_pause,
                    actionTitle = com.riders.thelab.core.ui.R.string.action_pause
                )

                // Updating progress bar
                updateProgressBar()
            }
                .onFailure {

                }
        } ?: run { Timber.e("Unable to find song item with id $songId") }
    }

    /**
     * Update timer on seekbar
     */
    private fun updateProgressBar() {
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

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Timber.d("onStart()")
        // Bind to LocalService
        Intent(context, MusicMediaPlaybackService::class.java).also { intent ->
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

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

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Timber.e("onPause()")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Timber.d("onResume()")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Timber.e("onStop()")
        if (mp.isPlaying) {
            mp.stop()
            mp.reset()
            mp.release()
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
            currentSongIndex = if (currentSongIndex < songList.size - 1) {
                playSong(context, songList[currentSongIndex + 1].id)
                currentSongIndex + 1
            } else {
                // play first song
                playSong(context, songList[0].id)
                0
            }
        }
    }

    fun togglePlayPauseSong() {
        Timber.d("togglePlayPauseSong()")

        mp?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    companion object {
        const val MUSIC_PLACEHOLDER = "Music"
        const val PROD_PLACEHOLDER = "Prod"
        const val DOWNLOAD_PLACEHOLDER = "Download"
    }


}