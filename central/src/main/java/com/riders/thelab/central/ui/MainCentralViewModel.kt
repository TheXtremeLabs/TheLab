package com.riders.thelab.central.ui

import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Stable
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.common.utils.LabPackageManager
import com.riders.thelab.core.data.local.model.app.PackageApp
import com.riders.thelab.core.data.utils.UiState
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.UiRepository
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


    private val _centralUiState: MutableStateFlow<UiState<List<PackageApp>>> =
        MutableStateFlow(UiState.Idle)

    @Stable
    val centralUiState: StateFlow<UiState<List<PackageApp>>> get() = _centralUiState

    fun updateCentralUiState(newState: UiState<List<PackageApp>>) {
        _centralUiState.update { newState }
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        Timber.e("Coroutine Exception caught with message: ${throwable.message} (${throwable.javaClass})")

        updateCentralUiState(UiState.Error(error = throwable.message.toString()))
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        fetchPackages()
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
                    packages.forEach {
                        val packageName: String? = it.packageName
                        val pInfo: PackageInfo =
                            context.packageManager.getPackageInfo(it.packageName, 0)
                        val version: String? = pInfo.versionName
                        val icon: Drawable =
                            context.packageManager.getApplicationIcon(it.packageName)

                        version?.let { appVersion ->
                            packageName?.let { appPackageName ->
                                appList.add(
                                    PackageApp(
                                        packageName,
                                        icon,
                                        appVersion,
                                        appPackageName
                                    )
                                )
                            } ?: run {
                                Timber.e("fetchPackages() | Unable to get app package name")
                            }
                        } ?: run {
                            Timber.e("fetchPackages() | Unable to get $packageName version")
                        }
                    }
                    appList.sortBy { app -> app.name }
                    updateCentralUiState(UiState.Success(appList))
                }
        }
    }
}