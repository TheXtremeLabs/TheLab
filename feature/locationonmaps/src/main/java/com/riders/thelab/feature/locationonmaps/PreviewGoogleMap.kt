package com.riders.thelab.feature.locationonmaps

import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.riders.thelab.core.common.utils.toLocation
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun GoogleMap(
    modifier: Modifier,
    properties: MapProperties,
    uiSettings: MapUiSettings,
    location: Location,
    markerTitle: String? = null,
    markerSnippet: String? = null,
    onMapLoaded: () -> Unit
) {

    val userPosition = LatLng(location.latitude, location.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userPosition, 10f)
    }

    TheLabTheme {
        GoogleMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings,
            onMapLoaded = { onMapLoaded() }
        ) {
            if (null == markerTitle && null == markerSnippet) {
                Marker(
                    state = MarkerState(position = userPosition)
                )
            } else {
                Marker(
                    state = MarkerState(position = userPosition),
                    title = markerTitle,
                    snippet = markerSnippet
                )
            }
        }
    }
}

///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewGoogleMap() {
    val uiSettings by remember { mutableStateOf(MapUiSettings()) }
    val properties by remember { mutableStateOf(MapProperties(mapType = MapType.SATELLITE)) }

    TheLabTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                properties = properties,
                uiSettings = uiSettings,
                location = (1.35 to 103.87).toLocation(),
                markerTitle = "Singapore",
                markerSnippet = "Marker in Singapore",
                onMapLoaded = {}
            )
        }
    }
}