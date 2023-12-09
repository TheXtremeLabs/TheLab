package com.riders.thelab.ui.colors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import kotlinx.coroutines.delay
import timber.log.Timber

///////////////////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////////////////
@Composable
fun ColorsButton(currentColor: Int, onButtonClicked: () -> Unit) {

    // Hoist the MutableInteractionSource that we will provide to interactions
    val interactionSource = remember { MutableInteractionSource() }

    // SnapshotStateList we will use to track incoming Interactions in the order they are emitted
    val interactions = remember { mutableStateListOf<Interaction>() }

    val clickable = Modifier.clickable(
        interactionSource = interactionSource,
        indication = LocalIndication.current
    ) { /* update some business state here */ }

    // Observe changes to the binary state for these interactions
    val isPressed by interactionSource.collectIsPressedAsState()

    // Use the state to change our UI
    val (text, color) = when {
        isPressed -> "Pressed" to Color.Blue
        // Default / baseline state
        else -> "Log In" to Color.Black
    }

    var clickNumber = 0

    TheLabTheme {
        Button(
            modifier = Modifier
                .fillMaxWidth(.85f)
                //.height(40.dp)
                .padding(8.dp)
                .then(clickable)
                .indication(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current
                ),
            onClick = {
                clickNumber += 1

                when (clickNumber) {
                    10, 20, 30 -> {
                        Timber.d("${interactions.toString()}")
                    }
                }

                onButtonClicked()
            },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = currentColor)),
            shape = RoundedCornerShape(16.dp),
            interactionSource = interactionSource
        ) {
            Text(
                stringResource(id = R.string.btn_change_color).uppercase(),
                color = colorResource(id = if (currentColor == R.color.white || currentColor == R.color.yellow) R.color.black else R.color.white)
            )
        }
    }

    // Collect Interactions - if they are new, add them to `interactions`. If they represent stop /
    // cancel events for existing Interactions, remove them from `interactions` so it will only
    // contain currently active `interactions`.
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> interactions.add(interaction)
                is PressInteraction.Release -> interactions.remove(interaction.press)
                is PressInteraction.Cancel -> interactions.remove(interaction.press)
                is DragInteraction.Start -> interactions.add(interaction)
                is DragInteraction.Stop -> interactions.remove(interaction.start)
                is DragInteraction.Cancel -> interactions.remove(interaction.start)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Color(viewModel: ColorViewModel) {

    // get current Context and coroutineScope
    val colorActivity = LocalContext.current

    var expanded by remember { mutableStateOf(false) }

    TheLabTheme(darkTheme = viewModel.isDarkMode) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.activity_title_colors),
                            color = colorResource(id = viewModel.randomColor)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { (colorActivity as ColorActivity).onBackPressed() }) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowLeft,
                                contentDescription = "Back",
                                tint = colorResource(id = viewModel.randomColor)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Black)
                )
            }
        ) { contentPadding ->
            // Screen content
            Column(
                modifier = Modifier
                    .background(color = colorResource(id = R.color.default_dark))
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(id = R.string.colors_text_placeholder),
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        color = colorResource(id = viewModel.randomColor),
                    )
                )

                AnimatedVisibility(visible = expanded) {
                    ColorsButton(viewModel.randomColor) { viewModel.updateRandomColor() }
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        delay(300)
        expanded = !expanded
    }
}


///////////////////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewColorsButton() {
    TheLabTheme {
        ColorsButton(R.color.tabColorAccent) {
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewColor() {
    val viewModel: ColorViewModel = hiltViewModel()
    TheLabTheme(darkTheme = viewModel.isDarkMode) {
        Color(viewModel = viewModel)
    }
}