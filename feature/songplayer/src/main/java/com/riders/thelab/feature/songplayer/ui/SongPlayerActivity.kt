package com.riders.thelab.feature.songplayer.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.ConnectionResult
import androidx.media3.session.MediaSession.ConnectionResult.AcceptedResultBuilder
import com.google.common.collect.ImmutableList
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.local.model.Permission
import com.riders.thelab.core.permissions.PermissionManager
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.base.observeLifecycleEvents
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SongPlayerActivity : BaseComponentActivity(), MediaSession.Callback {

    private val viewModel: SongPlayerViewModel by viewModels()

    private var currentSongIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {

                    viewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)

                    TheLabTheme(true) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            SongPlayerContent(
                                songList = viewModel.songList,
                                currentSongIndex = viewModel.currentSongIndex,
                                isSongPlaying = viewModel.isAnySongPlaying,
                                isCardExpanded = viewModel.isPlayerCardExpanded,
                                songProgress = viewModel.currentSongProgress,
                                onCardViewClicked = { viewModel.toggleViewToggle(it) },
                                onItemClicked = { selectedItemId ->
                                    viewModel.updateCurrentSongIndex(selectedItemId)
                                    viewModel.playSong(this@SongPlayerActivity, selectedItemId)
                                },
                                onPreviousClicked = { viewModel.playPreviousSong(this@SongPlayerActivity) },
                                onPlayPauseClicked = { viewModel.togglePlayPauseSong() },
                                onNextClicked = { viewModel.playNextSong(this@SongPlayerActivity) }
                            )
                        }
                    }
                }
            }
        }

        checkPermissions()
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
        if (viewModel.isPlayerCardExpanded) {
            viewModel.toggleViewToggle(!viewModel.isPlayerCardExpanded)
        } else {
            finish()
        }
    }

    ///////////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////////
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
                    viewModel.init()
                    viewModel.retrieveSongFiles(this@SongPlayerActivity)
                }
            }
    }


    ///////////////////////////////
    //
    // IMPLEMENTS
    //
    ///////////////////////////////
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