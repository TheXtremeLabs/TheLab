package com.riders.thelab.core.data.local.model.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Stable
sealed class IslandState(
    val contentSize: DpSize = DpSize(150.dp, 50.dp),
    val hasMainContent: Boolean = false,

    val leadingContentSize: Dp = Dp.Hairline,
    val hasLeadingContent: Boolean = false,

    val trailingContentSize: Dp = Dp.Hairline,
    val hasTrailingContent: Boolean = false,

    val bubbleContentSize: DpSize = DpSize(50.dp, 50.dp),
    val hasBubbleContent: Boolean = false
) {
    val fullWidth = contentSize.width + leadingContentSize + trailingContentSize

    @Stable
    @Immutable
    data object DefaultState : IslandState()

    @Stable
    class WelcomeState : IslandState(
        contentSize = DpSize(
            300.dp, 50.dp
        ),
    )

    @Stable
    class SearchState : IslandState(
        contentSize = DpSize(
            325.dp, 70.dp
        ),
    )

    @Stable
    class FaceUnlockState : IslandState(
        contentSize = DpSize(
            150.dp, 150.dp
        ),
        hasMainContent = true,
    )

    @Stable
    class CallState : IslandState(
        leadingContentSize = 65.dp,
        trailingContentSize = 55.dp,
        hasLeadingContent = true,
        hasTrailingContent = true,
    )

    @Stable
    sealed class NetworkState : IslandState(
        contentSize = DpSize(
            240.dp, 50.dp
        ),
        leadingContentSize = 65.dp,
        trailingContentSize = 55.dp,
        hasLeadingContent = true
    ) {
        @Stable
        data object Available : NetworkState()

        @Stable
        data object Lost : NetworkState()

        @Stable
        data object Unavailable : NetworkState()
    }

    @Stable
    class CallTimerState : IslandState(
        leadingContentSize = 50.dp,
        hasLeadingContent = true,
        hasBubbleContent = true
    )
}
