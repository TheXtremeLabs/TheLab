package com.riders.thelab.feature.musicrecognition.ui.acrcloud

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.acrcloud.rec.ACRCloudClient
import com.acrcloud.rec.ACRCloudConfig
import com.acrcloud.rec.ACRCloudResult
import com.acrcloud.rec.IACRCloudListener
import com.acrcloud.rec.utils.ACRCloudLogger
import com.riders.thelab.core.data.local.model.Song
import com.riders.thelab.core.data.local.model.compose.ACRUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import timber.log.Timber

///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
/*
 * To do that we will write an extension Composable function for ViewModel which
 * will receive Composable lifecycle Owner LocalLifecycleOwner.current.lifecycle
 * and will add observer and remove observer on onDispose block.
 *
 * The ViewModel will implement DefaultLifecycleObserver and will start receiving lifecycle events.
 */
@Composable
fun <viewModel : LifecycleObserver> viewModel.observeLifecycleEvents(lifecycle: Lifecycle) {
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(this@observeLifecycleEvents)
        onDispose {
            lifecycle.removeObserver(this@observeLifecycleEvents)
        }
    }
}

class ACRCloudViewModel : ViewModel(), DefaultLifecycleObserver, IACRCloudListener {

    private var mClient: ACRCloudClient? = null
    private var mConfig: ACRCloudConfig? = null

    private var _uiState: MutableStateFlow<ACRUiState> = MutableStateFlow(ACRUiState.Idle)
    val uiState: StateFlow<ACRUiState> = _uiState

    var canLaunchAudioRecognition by mutableStateOf(false)
        private set

    var isRecognizing by mutableStateOf(false)
        private set

    var result by mutableStateOf("")
        private set

    fun updateUiState(newState: ACRUiState) {
        this._uiState.value = newState
    }

    fun updateCanLaunchAudioRecognition(canLaunch: Boolean) {
        this.canLaunchAudioRecognition = canLaunch
    }

    fun updateIsRecognizing(isRecognizing: Boolean) {
        this.isRecognizing = isRecognizing
    }

    fun updateResult(newValue: String) {
        this.result = newValue
    }


    ///////////////////////////////
    //
    // INITIALIZATION
    //
    ///////////////////////////////
    init {
        Timber.i("init method")
    }


    ///////////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////////
    fun initACRCloud(context: Context) {
        Timber.d("initACRCloud()")
        if (null == mConfig) {
            Timber.d("init ACRCloudConfig")
            mConfig = ACRCloudConfig().apply {
                this.acrcloudListener = this@ACRCloudViewModel
                this.context = context

                // Please create project in "http://console.acrcloud.cn/service/avr".
                // Please create project in "http://console.acrcloud.cn/service/avr".
                this.host = "identify-eu-west-1.acrcloud.com"
                this.accessKey = "2ca3a1020d39133e1130e3b6e0ca40da"
                this.accessSecret = "oCP4NsfdfubfOtkY392029J7Fk6Dvbj2dX7nwuR0"

                this.recorderConfig.rate = 8000
                this.recorderConfig.channels = 1
            }
        }

        if (null == mClient) {
            Timber.d("init ACRCloudClient")
            mClient = ACRCloudClient().apply {
                //if (BuildConfig.DEBUG) {
                ACRCloudLogger.setLog(true)
                //}
                if (!initWithConfig(mConfig)) {
                    Timber.e("initWithConfig() | ACRCloudClient Configuration error. Please verify your information")
                    updateUiState(ACRUiState.Error("ACRCloudClient Configuration error"))
                } else {
                    Timber.d("initWithConfig() | ACRCloudClient Configuration successful.")
                    updateCanLaunchAudioRecognition(true)
                }
            }
        }
    }

    fun startRecognition() {
        mClient?.let {
            if (it.startRecognize()) {
                updateUiState(ACRUiState.ProcessRecognition(running = true))
                updateIsRecognizing(true)
                updateResult("Recognizing...")
            } else {
                updateResult("Init error")
                updateUiState(ACRUiState.Error("Init error"))
            }
        } ?: run {
            updateResult("Client not ready")
            updateCanLaunchAudioRecognition(false)
            updateUiState(ACRUiState.Error("Client not ready"))
        }
    }

    private fun handleResult(acrResult: String) {
        Timber.d("handleResult() | acrResult: $acrResult")

        updateIsRecognizing(false)

        val songFetched = runCatching {
            val json = JSONObject(acrResult)
            val status: JSONObject = json.getJSONObject("status")
            val code = status.getInt("code")
            if (code == 0) {
                val metadata: JSONObject = json.getJSONObject("metadata")
                if (metadata.has("music")) {
                    val musics = metadata.getJSONArray("music")
                    val tt = musics[0] as JSONObject
                    val genres = tt.getJSONArray("genres")
                    // val genre = genres[0] as JSONObject
                    val title = tt.getString("title")
                    val label = tt.getString("label")
                    val releaseDate = tt.getString("release_date")
                    val artists = tt.getJSONArray("artists")
                    val art = artists[0] as JSONObject
                    val artist = art.getString("name")
                    val externalMetadata = tt.getJSONObject("external_metadata")
                    val spotify = externalMetadata.getJSONObject("spotify")
                    val track = spotify.getJSONObject("track")
                    val trackName = track.getString("name")
                    val trackID = track.getString("id")
                    val album = spotify.getJSONObject("album")
                    val albumName = album.getString("name")
                    val albumID = album.getString("id")

                    // build song object
                    val song = Song.toModel(
                        genres,
                        title,
                        artists,
                        label,
                        releaseDate,
                        album,
                        externalMetadata
                    )

                    Timber.d("Song created: ${song.toString()}")

                    updateUiState(ACRUiState.RecognitionSuccessful(song))

                    "$title ($artist)"
                } else {
                    Timber.e("acrResult JSONObject has no metadata")
                    acrResult
                }
            } else {
                // TODO: Handle error
                acrResult
            }
        }
            .onFailure {
                Timber.e("runCatching | onFailure | error caught class: ${it.javaClass.simpleName}, with message: ${it.message}")
            }
            .onSuccess {
                Timber.d("runCatching | onSuccess | song fetched successfully")
            }
            .getOrElse {
                Timber.e("runCatching | getOrElse | error caught with message: ${it.message}")
                "Error parsing metadata"
            }

        updateResult(songFetched)
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
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Timber.e("onDestroy()")
        mClient?.let {
            it.release()
            mClient = null
        }
    }

    override fun onResult(acrResult: ACRCloudResult?) {
        Timber.d("onResult() | result: $acrResult")
        acrResult?.let {
            Timber.d("acr cloud result received: ${it.result}")
            handleResult(it.result)
        } ?: {
            updateUiState(ACRUiState.RecognitionError("Error message"))
        }
    }

    override fun onVolumeChanged(volume: Double) {
        Timber.e("onVolumeChanged() | volume: $volume")
    }
}

