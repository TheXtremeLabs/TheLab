package com.riders.thelab.core.compose.component

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.previewprovider.IslandStatePreviewProvider
import com.riders.thelab.data.local.model.compose.IslandState
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@DevicePreviews
@Composable
fun DynamicIsland(@PreviewParameter(IslandStatePreviewProvider::class) islandState: IslandState) {
    val config = LocalConfiguration.current

    val startPadding by animateDpAsState(
        targetValue = (config.screenWidthDp.dp / 2) - islandState.fullWidth / 2,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioLowBouncy,
        )
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

    MetaContainer(
        modifier = Modifier.height(200.dp)
    ) {
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
                IslandContent(state = islandState)
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

@OptIn(ExperimentalAnimationApi::class)
private val bubbleEnterTransition = scaleIn(initialScale = .7f) + slideInHorizontally(
    animationSpec = spring(
        stiffness = Spring.StiffnessLow,
        dampingRatio = Spring.DampingRatioLowBouncy,
    )
) { -it }

@OptIn(ExperimentalAnimationApi::class)
private val bubbleExitTransition = scaleOut(targetScale = .7f) + slideOutHorizontally(
    animationSpec = spring(
        stiffness = Spring.StiffnessLow
    )
) { (-it * 1.2f).roundToInt() }