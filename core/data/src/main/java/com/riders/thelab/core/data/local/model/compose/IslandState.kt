package com.riders.thelab.core.data.local.model.compose

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

    data object DefaultState : IslandState()

    class WelcomeState : IslandState(
        contentSize = DpSize(
            300.dp, 50.dp
        ),
    )

    class SearchState : IslandState(
        contentSize = DpSize(
            325.dp, 70.dp
        ),
    )

    class FaceUnlockState : IslandState(
        contentSize = DpSize(
            150.dp, 150.dp
        ),
        hasMainContent = true,
    )

    class CallState : IslandState(
        leadingContentSize = 65.dp,
        trailingContentSize = 55.dp,
        hasLeadingContent = true,
        hasTrailingContent = true,
    )

    sealed class NetworkState : IslandState(
        contentSize = DpSize(
            240.dp, 50.dp
        ),
        leadingContentSize = 65.dp,
        trailingContentSize = 55.dp,
        hasLeadingContent = true
    ) {
        data object Available : NetworkState()
        data object Lost : NetworkState()
        data object Unavailable : NetworkState()
    }

    class CallTimerState : IslandState(
        leadingContentSize = 50.dp,
        hasLeadingContent = true,
        hasBubbleContent = true
    )
}
