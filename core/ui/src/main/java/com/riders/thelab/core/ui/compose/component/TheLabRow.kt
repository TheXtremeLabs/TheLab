package com.riders.thelab.core.ui.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


/*
 * Here we define a custom composable that will be used to display a list of items in lazy row.
 */


@Composable
fun <T> TheLabRow(
    modifier: Modifier = Modifier,
    list: List<T>,
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal =
        if (!reverseLayout) Arrangement.Start else Arrangement.End,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    itemContent: @Composable (item: T) -> Unit,
    onItemClicked: ((item: T) -> Unit)? = null
) {
    val lazyListState = rememberLazyListState()

    LazyRow(
        modifier = modifier,
        state = lazyListState,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        itemsIndexed(items = list) { index, item ->
            itemContent(item)
        }
    }
}