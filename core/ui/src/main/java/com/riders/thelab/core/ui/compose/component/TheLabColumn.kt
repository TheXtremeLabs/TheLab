package com.riders.thelab.core.ui.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/*
 * Here we define a custom composable that will be used to display a list of items in lazy column.
 */


@Composable
fun <T> TheLabColumn(
    modifier: Modifier = Modifier,
    list: List<T>,
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    itemContent: @Composable (item: T) -> Unit,
    onItemClicked: ((item: T) -> Unit)? = null
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement
    ) {
        itemsIndexed(items = list) { _, item ->
            itemContent(item)
        }
    }
}