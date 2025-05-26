package com.riders.thelab.feature.mlkit.ui.compose

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.draganddrop.DraggableItem
import com.riders.thelab.core.ui.compose.component.draganddrop.rememberDragDropState
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.mlkit.R
import com.riders.thelab.feature.mlkit.data.local.model.MLKitItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MLKitComposeMainContent(
    theme: AppTheme,
    darkTheme: Boolean,
    list: MutableList<MLKitItem>,
    onSwap: (Int, Int) -> Unit,
    uiEvent: (UiEvent) -> Unit
) {

    val lazyListState = rememberLazyListState()
    // since LazyListState.scrollBy() is a suspend function
    val scope = rememberCoroutineScope()

    var overscrollJob by remember { mutableStateOf<Job?>(null) }
    val dragDropState = rememberDragDropState(lazyListState) { fromIndex, toIndex ->
        onSwap(fromIndex, toIndex)
    }

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(56.dp),
                    title = {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier.size(40.dp),
                                painter = painterResource(id = com.riders.thelab.core.ui.R.drawable.logo_mlkit),
                                contentDescription = null
                            )
                            Text(text = stringResource(id = R.string.ml_kit_app_name))
                        }
                    }
                )
            }
        ) { contentPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .pointerInput(Unit) {
                            detectDragGesturesAfterLongPress(
                                onDrag = { change, offset ->
                                    change.consume()
                                    // compute calculatedOffset
                                    dragDropState.onDrag(offset = offset)

                                    if (overscrollJob?.isActive == true)
                                        return@detectDragGesturesAfterLongPress

                                    dragDropState
                                        .checkForOverScroll()
                                        .takeIf { it != 0f }
                                        ?.let {
                                            overscrollJob =
                                                scope.launch {
                                                    dragDropState.state.animateScrollBy(
                                                        it * 1.3f,
                                                        tween(easing = FastOutLinearInEasing)
                                                    )
                                                }
                                        }
                                        ?: run { overscrollJob?.cancel() }
                                },
                                onDragStart = { offset ->
                                    dragDropState.onDragStart(offset)
                                },
                                onDragEnd = {
                                    dragDropState.onDragInterrupted()
                                    overscrollJob?.cancel()
                                },
                                onDragCancel = {
                                    dragDropState.onDragInterrupted()
                                    overscrollJob?.cancel()
                                }
                            )
                        },
                    state = lazyListState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
                ) {
                    itemsIndexed(items = list) { index, item ->
                        DraggableItem(
                            modifier = Modifier.fillMaxWidth(),
                            dragDropState = dragDropState,
                            index = index
                        ) { isDragging ->
                            MLKitComposeMainItem(
                                theme = theme,
                                darkTheme = darkTheme,
                                modifier = Modifier
                                    .animateItem()
                                    .fillMaxWidth()
                                    .height(dimensionResource(id = com.riders.thelab.core.ui.R.dimen.max_card_image_height) + 64.dp)
                                    .padding(vertical = 8.dp),
                                item = item,
                                isDragging = isDragging,
                                uiEvent = uiEvent
                            )
                        }
                    }
                }
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
private fun PreviewMLKitComposeMainContent(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        MLKitComposeMainContent(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            list = PreviewProviderMLKitListItem().values.first().toMutableList(),
            onSwap = { _, _ -> },
            uiEvent = {}
        )
    }
}
