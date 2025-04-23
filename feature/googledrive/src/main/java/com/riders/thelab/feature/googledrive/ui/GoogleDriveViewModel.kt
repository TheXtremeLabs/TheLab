package com.riders.thelab.feature.googledrive.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.google.api.services.drive.model.File
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.common.network.NetworkState
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.feature.googledrive.core.google.GoogleSignInManager
import com.riders.thelab.feature.googledrive.data.local.compose.GoogleDriveUiState
import com.riders.thelab.feature.googledrive.data.local.compose.GoogleSignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class GoogleDriveViewModel @Inject constructor(
    labNetworkManager: LabNetworkManager,
    val uiRepository: IUiRepository
) : BaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()

    /////////////////////////////////////////////////
    // Composable states
    /////////////////////////////////////////////////
    private var _googleDriveUiState: MutableStateFlow<GoogleDriveUiState> =
        MutableStateFlow(GoogleDriveUiState.Loading)
    val googleDriveUiState: StateFlow<GoogleDriveUiState> = _googleDriveUiState.asStateFlow()
    private var _signInState: MutableStateFlow<GoogleSignInState> =
        MutableStateFlow(GoogleSignInState.Disconnected)
    val signInState: StateFlow<GoogleSignInState> = _signInState.asStateFlow()

    // Network State
    val hasInternetConnection: StateFlow<Boolean> = labNetworkManager.isConnectedFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = false
    )

    var driveFileList: SnapshotStateList<File> = mutableStateListOf()

    fun updateGoogleDriveUiState(newState: GoogleDriveUiState) {
        this._googleDriveUiState.value = newState
    }

    fun updateGoogleSignInState(newState: GoogleSignInState) {
        this._signInState.value = newState
    }

    fun updateDriveFileList(newList: List<File>) {
        if (driveFileList.isNotEmpty()) {
            driveFileList.clear()
        }
        driveFileList.addAll(newList)
    }


    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        Timber.e("Coroutine Exception caught with message: ${throwable.message} (${throwable.javaClass})")
    }


    ///////////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////////
    fun onEvent(activity: GoogleDriveActivity, uiEvent: UiEvent) {
        Timber.d("onEvent() | event: ${uiEvent.javaClass.simpleName}")

        when (uiEvent) {
            is UiEvent.OnSignIn -> {
                GoogleSignInManager.getInstance(activity).signInLegacy()
            }

            is UiEvent.OnHandleAccount -> {
                updateGoogleSignInState(GoogleSignInState.Connected(uiEvent.account))
            }

            is UiEvent.OnSignOut -> {
                GoogleSignInManager.getInstance(activity)
                    .signOut(
                        activity = activity,
                        onFailure = {
                            Timber.e("onEvent() | message: ${it?.message}")
                        },
                        onSuccess = { isLoggedOut ->
                            if (isLoggedOut) {
                                updateGoogleSignInState(GoogleSignInState.Disconnected)
                            }
                        }
                    )
            }
        }
    }

    fun getGoogleDriveFiles() {
        viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {
        }
    }
}