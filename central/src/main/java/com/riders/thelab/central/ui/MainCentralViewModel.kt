package com.riders.thelab.central.ui

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.riders.thelab.central.BuildConfig
import com.riders.thelab.core.common.utils.LabPackageManager
import com.riders.thelab.core.data.local.model.app.PackageApp
import com.riders.thelab.core.data.utils.UiState
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.UiRepository
import com.riders.thelab.core.ui.utils.LabNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainCentralViewModel @Inject constructor(
    @param:ApplicationContext val context: Context,
    val uiRepository: UiRepository
) : BaseViewModel(), DefaultLifecycleObserver {

    //////////////////////////////////////////
    // Variables
    //////////////////////////////////////////
    private var mNavigator: LabNavigator? = null


    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    private val _centralUiState: MutableStateFlow<UiState<List<PackageApp>>> =
        MutableStateFlow(UiState.Idle)

    @Stable
    val centralUiState: StateFlow<UiState<List<PackageApp>>> get() = _centralUiState

    var searchModeEnabled: Boolean by mutableStateOf(false)
    var searchPackageQuery: String by mutableStateOf("")

    fun updateCentralUiState(newState: UiState<List<PackageApp>>) {
        _centralUiState.update { newState }
    }

    fun updateSearchMode(enabled: Boolean) {
        this.searchModeEnabled = enabled
    }

    fun updateSearchPackageQuery(newQuery: String) {
        this.searchPackageQuery = newQuery
    }


    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        Timber.e("Coroutine Exception caught with message: ${throwable.message} (${throwable.javaClass})")

        updateCentralUiState(UiState.Error(error = throwable.message.toString()))
    }


    ////////////////////////////////////////
    //
    // OVERRIDE METHODS
    //
    ////////////////////////////////////////
    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Timber.d("onCreate()")

        fetchPackages()
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


    ////////////////////////////////////////
    //
    // CLASS METHODS
    //
    ////////////////////////////////////////
    fun initNavigator() {
        if (null == mWeakReference || null == mWeakReference?.get()) {
            Timber.e("initNavigator() | Weak Reference is null, or unable to get Weak Reference Activity value. Leave method immediately")
            return
        }

        if (null != mNavigator) {
            Timber.w("initNavigator() | Navigator is already set.")
            return
        }

        mNavigator = LabNavigator.getInstance(mWeakReference?.get()!!)
    }

    fun fetchPackages() {
        if (_centralUiState.value is UiState.Loading) {
            Timber.d("fetchPackages() | Already loading")
            return
        }

        updateCentralUiState(UiState.Loading)

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            LabPackageManager
                .getInstance(context = context)
                .getFilteredPackageList(
                    "com.riders", "fr.chronopost"
                ) { packages ->
                    val appList = mutableListOf<PackageApp>()

                    packages
                        .filter { BuildConfig.APPLICATION_ID != it.packageName }
                        .forEach { applicationInfo ->
                            val packageManager = context.packageManager

                            val packageLabel =
                                packageManager.getApplicationLabel(applicationInfo).toString()
                            val packageName: String = applicationInfo.packageName
                            val packageIcon: Drawable =
                                packageManager.getApplicationIcon(packageName)
                            val packageVersion: String? = packageManager
                                .getPackageInfo(packageName, 0)
                                .versionName

                            packageVersion?.let { appVersion ->
                                appList.add(
                                    PackageApp(
                                        name = packageLabel,
                                        drawableIcon = packageIcon,
                                        version = appVersion,
                                        packageName = packageName
                                    )
                                )
                            } ?: run {
                                Timber.e("fetchPackages() | Unable to get $packageName version")
                            }
                        }
                    appList.sortBy { app -> app.name }
                    updateCentralUiState(UiState.Success(appList))
                }
        }
    }

    fun onEvent(event: UiEvent) {
        Timber.d("onEvent() | event: $event")

        when (event) {
            is UiEvent.OnUpdateSearchMode -> updateSearchMode(event.enabled)
            is UiEvent.OnUpdateSearchQuery -> updateSearchPackageQuery(event.newQuery)
            is UiEvent.OnClearSearchQuery -> updateSearchPackageQuery("")

            is UiEvent.OnPackageClicked -> {
                initNavigator()
                mNavigator?.callIntentForPackageName(packageName = event.packageItem.packageName)
            }
        }
    }
}