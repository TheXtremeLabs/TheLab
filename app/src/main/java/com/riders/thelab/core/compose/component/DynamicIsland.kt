package com.riders.thelab.core.compose.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.core.data.local.model.compose.IslandState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.dynamicisland.IslandBubbleContent
import com.riders.thelab.core.ui.compose.component.dynamicisland.MetaContainer
import com.riders.thelab.core.ui.compose.component.dynamicisland.MetaEntity
import com.riders.thelab.core.ui.compose.component.dynamicisland.bubbleEnterTransition
import com.riders.thelab.core.ui.compose.component.dynamicisland.bubbleExitTransition
import com.riders.thelab.core.ui.compose.previewprovider.IslandStatePreviewProvider
import com.riders.thelab.ui.mainactivity.MainActivityViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun DynamicIsland(viewModel: MainActivityViewModel, islandState: IslandState) {
    val config = LocalConfiguration.current

    val startPadding by animateDpAsState(
        targetValue = (config.screenWidthDp.dp / 2) - islandState.fullWidth / 2,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioLowBouncy,
        ),
        label = ""
    )

    val scope = rememberCoroutineScope()

    val shake = remember { androidx.compose.animation.core.Animatable(0f) }
    LaunchedEffect(islandState.hasBubbleContent) {
        scope.launch {
            shake.animateTo(15f)
            shake.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow,
                )
            )
        }
    }

    MetaContainer(modifier = Modifier.height(200.dp)) {
        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .padding(start = startPadding)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {

            MetaEntity(
                modifier = Modifier
                    .offset { IntOffset(shake.value.roundToInt(), 0) }
                    .zIndex(10f),
                metaContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = Color.Black,
                                shape = RoundedCornerShape(35.dp)
                            )
                    )
                }
            ) {
                IslandContent(viewModel = viewModel, state = islandState)
            }

            AnimatedVisibility(
                visible = islandState.hasBubbleContent,
                modifier = Modifier.padding(start = 8.dp),
                enter = bubbleEnterTransition,
                exit = bubbleExitTransition,
            ) {
                MetaEntity(
                    metaContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Color.Black,
                                    shape = RoundedCornerShape(50.dp)
                                )
                        )
                    }
                ) {
                    IslandBubbleContent(state = islandState)
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
fun PreviewDynamicIsland(@PreviewParameter(IslandStatePreviewProvider::class) state: IslandState) {
    val viewModel: MainActivityViewModel = hiltViewModel()
    DynamicIsland(viewModel, state)
}