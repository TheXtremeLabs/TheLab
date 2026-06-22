package com.riders.thelab.feature.locationonmaps

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.common.location.LabLocationManager
import com.riders.thelab.core.common.location.LocationState
import com.riders.thelab.core.common.utils.toLocation
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LocationOnMapsActivity : BaseComponentActivity() {

    private val mViewModel: LocationOnMapsViewModel by viewModels<LocationOnMapsViewModel>()

    private val mLocationManager: LabLocationManager by lazy {
        LabLocationManager.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLocationManager.setLocationListener(object : LocationListener {
            override fun onLocationChanged(location: Location) {
            }
        })

        getCurrentLocation()

        mViewModel.initPlaces()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    val theme: AppTheme by mViewModel
                        .theme
                        .collectAsStateWithLifecycle()
                    val isDarkTheme: Boolean? by mViewModel
                        .isDarkMode
                        .collectAsStateWithLifecycle()

                    val location by mLocationManager.locationState.collectAsStateWithLifecycle()

                    TheLabTheme(theme = theme, darkTheme = isDarkTheme ?: isSystemInDarkTheme()) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            when (location) {
                                is LocationState.Located -> {
                                    LocationOnMapsContent(
                                        theme = theme,
                                        darkTheme = isDarkTheme ?: isSystemInDarkTheme(),
                                        location = (location as LocationState.Located).location,
                                        isSearchPlaceVisible = mViewModel.isSearchPlaceVisible,
                                        uiEvent = { event ->
                                            when (event) {
                                                is UiEvent.OnPlaceSelected -> {
                                                    event.place.location?.let {
                                                        Timber.d("Recomposition | UiEvent.OnPlaceSelected | ${it.latitude}, ${it.longitude}")
                                                        mLocationManager.updateLocationState((it.latitude to it.longitude).toLocation())
                                                    }
                                                }

                                                else -> mViewModel.onEvent(event)
                                            }
                                        },
                                        mapUiEvent = { mapEvent ->
                                            when (mapEvent) {
                                                is GoogleMapUiEvent.OnMyLocationButtonClick -> getCurrentLocation()
                                                else -> mViewModel.onGoogleMapEvent(mapEvent)
                                            }
                                        }
                                    )
                                }

                                else -> {
                                    LabLoader(modifier = Modifier.size(30.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }

    fun getCurrentLocation() {
        if (mLocationManager.canGetLocation()) {
            runOnUiThread { mLocationManager.getCurrentLocation() }
        }
    }
}