package com.riders.thelab.feature.songplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
class SongPlayerActivity : BaseComponentActivity() {

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

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
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


    /*
       override fun onClick(view: View?) {
           when (view?.id) {
               R.id.btn_arrow_down -> {
                   // make iv clickable only if view is toggled
                   if (isToggle)
                       toggleAnimation(view)
               }

               R.id.tv_song_path -> {
                   if (!isToggle)
                       toggleAnimation(view)
               }

               R.id.btn_play_pause -> {
                   // check for already playing
                   if (mp.isPlaying) {
                       mp.pause()
                       // Changing button image to play button
                       binding.btnPlayPause.setImageResource(R.drawable.ic_play)
                   } else {
                       // Resume song
                       mp.start()
                       // Changing button image to pause button
                       binding.btnPlayPause.setImageResource(R.drawable.ic_pause)
                   }
               }

               R.id.btn_previous -> {
                   if (currentSongIndex > 0) {
                       playSong(songsList[currentSongIndex - 1])
                       currentSongIndex -= 1
                   } else {
                       // play last song
                       playSong(songsList[songsList.size - 1])
                       currentSongIndex = songsList.size - 1
                   }

               }

               R.id.btn_next -> {
                   // check if next song is there or not
                   if (currentSongIndex < (songsList.size - 1)) {
                       playSong(songsList[currentSongIndex + 1])
                       currentSongIndex += 1
                   } else {
                       // play first song
                       playSong(songsList[0])
                       currentSongIndex = 0
                   }
               }
           }
       }


       override fun onStartTrackingTouch(seekBar: SeekBar?) {
           // remove message Handler from updating progress bar
           mHandler?.removeCallbacks(mUpdateTimeTask)
       }

       override fun onStopTrackingTouch(seekBar: SeekBar?) {
           mHandler?.removeCallbacks(mUpdateTimeTask)
           val totalDuration = mp.duration
           val currentPosition: Int =
               SongPlayerUtils.progressToTimer(seekBar!!.progress, totalDuration)

           // forward or backward to certain seconds
           mp.seekTo(currentPosition)

           // update timer progress again
           updateProgressBar()
       }

       */
}