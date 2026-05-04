package com.riders.thelab.core.common.location

import android.location.Location
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface LocationState {
    @Stable
    data class Located(val location: Location) : LocationState

    @Stable
    @Immutable
    data object Losing : LocationState

    @Stable
    @Immutable
    data object Lost : LocationState

    @Stable
    @Immutable
    data object UnableToGetLocation : LocationState

    @Stable
    @Immutable
    data object Enabled : LocationState

    @Stable
    @Immutable
    data object Disabled : LocationState


    @Stable
    @Immutable
    data object Unknown : LocationState
}