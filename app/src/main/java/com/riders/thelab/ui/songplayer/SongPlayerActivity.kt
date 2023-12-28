package com.riders.thelab.ui.songplayer

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v4.media.session.MediaControllerCompat
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.animation.addListener
import androidx.core.view.marginTop
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.media.session.MediaButtonReceiver
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.riders.thelab.R
import com.riders.thelab.core.data.local.model.music.SongModel
import com.riders.thelab.core.service.MusicMediaPlaybackService
import com.riders.thelab.core.utils.LabNotificationManager
import com.riders.thelab.core.utils.SongsManager
import com.riders.thelab.databinding.ActivitySongPlayerBinding
import com.riders.thelab.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.util.Random
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class SongPlayerActivity : AppCompatActivity(),
    CoroutineScope, SongClickedListener, View.OnClickListener, MediaPlayer.OnCompletionListener,
    SeekBar.OnSeekBarChangeListener, MotionLayout.TransitionListener {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var _viewBinding: ActivitySongPlayerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val viewModel: SongPlayerViewModel by viewModels()

    // Media Player
    private lateinit var mp: MediaPlayer

    // Handler to update UI timer, progress bar etc,.
    private var mHandler: Handler? = null
    private var songManager: SongsManager? = null
    // private val seekForwardTime = 5000 // 5000 milliseconds

    // private val seekBackwardTime = 5000 // 5000 milliseconds

    private var currentSongIndex = 0
    private val isShuffle = false
    private val isRepeat = false


    // Songs list
    private var songsList = ArrayList<SongModel>()

    private var isCardViewPlayerShown: Boolean = false
    private var isToggle: Boolean = false

    private lateinit var mAdapter: SongPlayerAdapter

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
            //Log.d("Progress", ""+progress);
            if (_viewBinding != null) binding.songProgressBar.progress = progress

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivitySongPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Media Player
        mp = MediaPlayer()
        songManager = SongsManager()

        // Listeners
        setListeners()
        initViewModelsObservers()

        checkPermissions()
    }

    override fun onStart() {
        super.onStart()

        // Bind to LocalService
        Intent(this, MusicMediaPlaybackService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    @Deprecated("DEPRECATED - Use registerActivityForResult")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 100) {
            currentSongIndex = data?.extras?.getInt("songIndex")!!
            // play selected song
            playSong(songsList[currentSongIndex])
        }
    }

    override fun onPause() {
        if (mp.isPlaying) {
            mp.pause()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume()")
        runCatching {
            registerReceiver(MediaButtonReceiver(), IntentFilter(Intent.ACTION_MEDIA_BUTTON))
        }
            .onFailure {
                Timber.e("runCatching - onFailure() | Error caught: ${it.message}")
            }
            .onSuccess {
                Timber.d("runCatching - onSuccess() | app list fetched successfully")
            }
    }

    override fun onStop() {
        super.onStop()
        if (mp.isPlaying) {
            mp.stop()
            mp.reset()
            mp.release()
        }
        mHandler = null

        unbindService(connection)
        mBound = false
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")

        _viewBinding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (isToggle) {
            toggleAnimation(binding.btnArrowDown)
        } else {
            super.onBackPressed()
        }
    }


    ///////////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////////
    private fun setListeners() {
        binding.tvSongPath.setOnClickListener(this)
        binding.btnArrowDown.setOnClickListener(this)
        binding.btnPlayPause.setOnClickListener(this)
        binding.btnPrevious.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
        binding.songProgressBar.setOnSeekBarChangeListener(this) // Important
        mp.setOnCompletionListener(this) // Important
        binding.motionLayout.addTransitionListener(this)
    }

    private fun checkPermissions() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Timber.i("All permissions are granted")

                        viewModel.retrieveSongFiles(this@SongPlayerActivity)
                    } else {
                        Timber.e("All permissions are not granted")
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    list: List<PermissionRequest>,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            })
            .withErrorListener { dexterError: DexterError -> Timber.e(dexterError.toString()) }
            .onSameThread()
            .check()
    }

    private fun initViewModelsObservers() {
        Timber.i("initViewModelsObservers()")
        viewModel.getFiles().observe(this) { fileList ->
            Timber.e("getFiles().observe")
            if (null == fileList) {
                Timber.e("File list is null")
            } else {
                if (fileList.isEmpty()) {
                    Timber.e("File list is empty")
                } else {
                    songsList = fileList as ArrayList<SongModel>

                    bindData(songsList)
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun bindData(list: List<SongModel>) {
        Timber.i("bindData()")

        mAdapter = SongPlayerAdapter(this, list as ArrayList<SongModel>, this)

        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvFileList.layoutManager = layoutManager
        binding.rvFileList.adapter = mAdapter
    }

    /**
     * Function to play a song
     * @param item - index of song
     */
    @SuppressLint("InlinedApi")
    private fun playSong(item: SongModel) {
        Timber.d("playSong()")

        binding.songModel = item

        if (!isCardViewPlayerShown)
            changeViews()

        if (binding.cvSongPlayer.visibility != View.VISIBLE) {

            val params = binding.rvFileList.layoutParams
            val animator = ValueAnimator.ofInt(
                binding.rvFileList.height,
                binding.rvFileList.height - (binding.cvSongPlayer.height + (2 * binding.cvSongPlayer.marginTop))
            )
            animator.addUpdateListener { valueAnimator ->
                params.height = (valueAnimator.animatedValue as Int)
                binding.rvFileList.requestLayout()
            }
            animator.addListener(onEnd = { fadeViews() })
            animator.duration = 700
            animator.start()

            isCardViewPlayerShown = true
        }

        // Play song
        try {
            mp.reset()
            mp.setDataSource(item.path)
            mp.prepare()
            mp.start()

            // Displaying Song title via Notification
            LabNotificationManager.createNotificationChannel(
                this@SongPlayerActivity,
                getString(R.string.music_channel_name),
                getString(R.string.music_channel_description),
                NotificationManager.IMPORTANCE_HIGH,
                Constants.NOTIFICATION_MUSIC_CHANNEL_ID
            )

            val mediaSession = SongPlayerUtils.createMediaSession(this, mp)
            val mediaController = SongPlayerUtils.createMediaController(mediaSession)

            /*LabNotificationManager.displayMusicNotification(
                this@SongPlayerActivity,
                mediaSession,
                mediaController,
                mServiceMusic,
                item
            )*/

            //displaySessionNotification(item)

            // Changing Button Image to pause image
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause)

            // set Progress bar values
            binding.songProgressBar.progress = 0
            binding.songProgressBar.max = 100

            // Updating progress bar
            updateProgressBar()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun changeViews() {
        CoroutineScope(coroutineContext).launch {
            makeViewVisible(binding.cvSongPlayer)
            makeViewVisible(binding.clSongPlayer)
            makeViewVisible(binding.tvSongName)
            makeViewVisible(binding.tvSongPath)
            makeViewVisible(binding.ivThumb)
            makeViewVisible(binding.songProgressBar)
            makeViewVisible(binding.btnPrevious)
            makeViewVisible(binding.btnPlayPause)
            makeViewVisible(binding.btnNext)
            makeViewVisible(binding.btnClose)
            makeViewVisible(binding.btnArrowDown)
        }
    }

    private fun fadeViews() {
        CoroutineScope(coroutineContext).launch {
            fadeView(binding.cvSongPlayer)
            fadeView(binding.clSongPlayer)
            fadeView(binding.tvSongName)
            fadeView(binding.tvSongPath)
            fadeView(binding.ivThumb)
            fadeView(binding.songProgressBar)
            fadeView(binding.btnPrevious)
            fadeView(binding.btnPlayPause)
            fadeView(binding.btnNext)
            fadeView(binding.btnClose)

        }
    }

    private suspend fun makeViewVisible(targetViewToVisible: View) {
        delay(200)
        targetViewToVisible.visibility = View.INVISIBLE
    }


    private suspend fun fadeView(targetViewToFade: View) {
        delay(250)

        val objectAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(targetViewToFade, View.ALPHA, 0f, 1f)

        objectAnimator.interpolator = LinearOutSlowInInterpolator()
        objectAnimator.duration = 800
        objectAnimator.addListener(
            onEnd = {
                Timber.d("onEnd()")
            }
        )

        objectAnimator.start()

        targetViewToFade.visibility = View.VISIBLE
    }

    private fun toggleAnimation(view: View) {
        if (!isToggle) {
            binding.motionLayout.transitionToEnd()
            // Disable card view click listener
            binding.cvSongPlayer.isClickable = false
            mAdapter.setClickable(false)
        } else {
            binding.motionLayout.transitionToStart()
            // Enable card view click listener
            binding.cvSongPlayer.isClickable = true
            mAdapter.setClickable(true)
        }
    }

    /**
     * Update timer on seekbar
     */
    private fun updateProgressBar() {
        mHandler?.postDelayed(mUpdateTimeTask, 100)
    }


    override fun onSongClick(view: View, item: SongModel) {
        Timber.d("onFileClick() - $item")

        playSong(item)
    }

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

    override fun onCompletion(mp: MediaPlayer?) {
        // check for repeat is ON or OFF
        if (isRepeat) {
            // repeat is on play same song again
            playSong(songsList[currentSongIndex])
        } else if (isShuffle) {
            // shuffle is on - play a random song
            val rand = Random()
            currentSongIndex = rand.nextInt(songsList.size - 1 - 0 + 1) + 0
            playSong(songsList[currentSongIndex])
        } else {
            // no repeat or shuffle ON - play next song
            currentSongIndex = if (currentSongIndex < songsList.size - 1) {
                playSong(songsList[currentSongIndex + 1])
                currentSongIndex + 1
            } else {
                // play first song
                playSong(songsList[0])
                0
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        // ignored
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

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
        Timber.i("onTransitionStarted()")
    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
        // Ignored
    }

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
        Timber.e("onTransitionCompleted()")
        isToggle = !isToggle
    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
        Timber.i("onTransitionTrigger()")
    }
}