package com.riders.thelab.feature.songplayer.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media.session.MediaButtonReceiver
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.ConnectionResult.AcceptedResultBuilder
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.permissions.Permission
import com.riders.thelab.core.permissions.PermissionManager
import com.riders.thelab.core.player.service.PlaybackService
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.base.observeLifecycleEvents
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.songplayer.core.SongsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SongPlayerActivity : BaseComponentActivity(), MediaSession.Callback {

    ///////////////////////////////////////////
    // Context & ViewModel
    ///////////////////////////////////////////
    private val mViewModel: SongPlayerViewModel by viewModels()

    ///////////////////////////////////////////
    // Service
    ///////////////////////////////////////////
    var mServiceMusic: PlaybackService? = null
        private set
    var mBound: Boolean = false
        private set

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

    private var mMediaButtonReceiver: MediaButtonReceiver? = null

    private var songManager: SongsManager? = null
    private var currentSongIndex = 0

    ////////////////////////////////////////
    //
    // OVERRIDE
    //
    ////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.initWeakReference(this@SongPlayerActivity)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {

                    // Register lifecycle events
                    mViewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)

                    val theme: AppTheme by mViewModel
                        .theme
                        .collectAsStateWithLifecycle()
                    val isDarkTheme: Boolean? by mViewModel
                        .isDarkMode
                        .collectAsStateWithLifecycle()

                    val songPlayerUiState by mViewModel.songUiState.collectAsStateWithLifecycle()
                    val cardPlayerUiState by mViewModel.cardPlayerUiState.collectAsStateWithLifecycle()

                    TheLabTheme(theme = theme, darkTheme = isDarkTheme ?: isSystemInDarkTheme()) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            SongPlayerContent(
                                theme = theme,
                                darkTheme = isDarkTheme ?: isSystemInDarkTheme(),
                                songPlayerUiState = songPlayerUiState,
                                cardPlayerState = cardPlayerUiState,
                                currentSongIndex = mViewModel.currentSongIndex,
                                isSongPlaying = mViewModel.isPlaying,
                                isCardExpanded = mViewModel.isPlayerCardExpanded,
                                songProgress = mViewModel.currentSongProgress,
                                uiEvent = mViewModel::onEvent
                            )
                        }
                    }
                }
            }
        }

        checkPermissions()
    }

    override fun onStart() {
        super.onStart()
        // Bind to LocalService
        Intent(this, PlaybackService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onPause() {
        super.onPause()
        Timber.e("onPause()")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume()")
        registerReceivers()
    }

    @Deprecated("DEPRECATED - Use registerActivityForResult")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 100) {
            currentSongIndex = data?.extras?.getInt("songIndex")!!
            // play selected song
            //  playSong(songsList[currentSongIndex])
        }
    }

    override fun backPressed() {
        if (mViewModel.isPlayerCardExpanded) {
            mViewModel.toggleCardPlayerView(cardExpanded = !mViewModel.isPlayerCardExpanded)
        } else {
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        Timber.e("onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mBound) {
            unbindService(connection)
            mBound = false
            mServiceMusic = null
        }
    }

    ////////////////////////////////////////
    //
    // CLASS METHODS
    //
    ////////////////////////////////////////
    @SuppressLint("NewApi")
    private fun checkPermissions() {
        PermissionManager
            .from(this@SongPlayerActivity)
            .request(
                if (LabCompatibilityManager.isTiramisu()) {
                    Permission.MediaLocationAndroid13
                } else {
                    Permission.Storage
                }
            )
            .rationale("Theses permissions are mandatory to fetch data")
            .checkPermission { granted: Boolean ->
                if (!granted) {
                    Timber.e("All permissions are not granted")
                } else {
                    Timber.i("All permissions are granted")
                    mViewModel.init()
                    mViewModel.retrieveSongFiles(this@SongPlayerActivity)
                }
            }
    }

    fun registerReceivers() {
        runCatching {
            if (null == mMediaButtonReceiver) {
                mMediaButtonReceiver = MediaButtonReceiver()
            }
            if (LabCompatibilityManager.isR()) {
                ContextCompat.registerReceiver(
                    this,
                    mMediaButtonReceiver,
                    IntentFilter(Intent.ACTION_MEDIA_BUTTON),
                    ContextCompat.RECEIVER_EXPORTED
                )
            } else {
                @SuppressLint("UnspecifiedRegisterReceiverFlag")
                registerReceiver(
                    mMediaButtonReceiver,
                    IntentFilter(Intent.ACTION_MEDIA_BUTTON)
                )
            }
        }
            .onFailure {
                Timber.e("onResume() | onFailure | Error caught: ${it.message}")
            }
            .onSuccess {
                Timber.d("onResume() | onSuccess | app list fetched successfully")
            }
    }

    ////////////////////////////////////////
    //
    // IMPLEMENTS
    //
    ////////////////////////////////////////
    @OptIn(UnstableApi::class)
    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo
    ): MediaSession.ConnectionResult {
        Timber.d("onConnect()")

        /*if (session.isMediaNotificationController(controller)) {
            val sessionCommands =
                ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
                    .add(customCommandSeekBackward)
                    .add(customCommandSeekForward)
                    .build()
            val playerCommands =
                ConnectionResult.DEFAULT_PLAYER_COMMANDS.buildUpon()
                    .remove(COMMAND_SEEK_TO_PREVIOUS)
                    .remove(COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM)
                    .remove(COMMAND_SEEK_TO_NEXT)
                    .remove(COMMAND_SEEK_TO_NEXT_MEDIA_ITEM)
                    .build()
            // Custom layout and available commands to configure the legacy/framework session.
            return AcceptedResultBuilder(session)
                .setCustomLayout(
                    ImmutableList.of(
                        createSeekBackwardButton(customCommandSeekBackward),
                        createSeekForwardButton(customCommandSeekForward)
                    )
                )
                .setAvailablePlayerCommands(playerCommands)
                .setAvailableSessionCommands(sessionCommands)
                .build()
        }*/

        // Default commands with default custom layout for all other controllers.
        return AcceptedResultBuilder(session).build()
    }
}