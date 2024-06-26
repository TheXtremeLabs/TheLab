package com.riders.thelab.core.compose.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.compose.IslandState
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.dynamicisland.CallWaveform
import com.riders.thelab.core.ui.compose.previewprovider.IslandStatePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_primary
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.ui.mainactivity.MainActivity
import timber.log.Timber

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun Search(
    dynamicIslandState: IslandState,
    searchedAppRequest: String,
    onSearchAppRequestChanged: (String) -> Unit,
    isMicrophoneEnabled: Boolean,
    onUpdateMicrophoneEnabled: (Boolean) -> Unit,
    onUpdateKeyboardVisible: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val focus = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (focus.value != it.isFocused) {
                        focus.value = it.isFocused
                        if (!it.isFocused) {
                            keyboardController?.hide()
                            onUpdateKeyboardVisible(it.isFocused)
                        } else {
                            onUpdateKeyboardVisible(it.isFocused)
                        }
                    }
                },
            value = searchedAppRequest,
            onValueChange = onSearchAppRequestChanged,
            placeholder = {
                Text(
                    text = if (isMicrophoneEnabled && searchedAppRequest.isNotBlank()) {
                        "Ecoute en cours..."
                    } else {
                        stringResource(id = R.string.search_app_item_placeholder)
                    },
                    color = Color.Gray
                )
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedPlaceholderColor = Color.LightGray,
                disabledTextColor = Color.Transparent,
                cursorColor = md_theme_dark_primary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle(
                textAlign = TextAlign.Start
            ),
            maxLines = 1,
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AnimatedContent(
                        targetState = if (LocalInspectionMode.current) true else isMicrophoneEnabled,
                        transitionSpec = { fadeIn() + slideInVertically() togetherWith slideOutVertically() + fadeOut() },
                        label = "animation microphone"
                    ) { targetState ->
                        if (!targetState) {
                            IconButton(
                                onClick = {
                                    onUpdateMicrophoneEnabled(!isMicrophoneEnabled)
                                    (context.findActivity() as MainActivity).launchSpeechToText()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Mic,
                                    contentDescription = "close_icon"
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CallWaveform()
                            }
                        }
                    }

                    AnimatedVisibility(visible = if (LocalInspectionMode.current) true else searchedAppRequest.isNotBlank()) {
                        IconButton(
                            onClick = {
                                onUpdateMicrophoneEnabled(false)
                                onSearchAppRequestChanged("")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "close_icon"
                            )
                        }
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )
    }

    if (dynamicIslandState is IslandState.SearchState) {
        Timber.d("Should request focus for textField")
    }

    LaunchedEffect(searchedAppRequest) {
        Timber.d("LaunchedEffect | $searchedAppRequest")
        /*delay(150L)
        focusRequester.freeFocus()
        focusManager.clearFocus(true)*/
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
fun PreviewSearch(@PreviewParameter(IslandStatePreviewProvider::class) state: IslandState) {
    TheLabTheme {
        Search(
            dynamicIslandState = state,
            searchedAppRequest = "Colors",
            onSearchAppRequestChanged = {},
            isMicrophoneEnabled = false,
            onUpdateMicrophoneEnabled = {},
            onUpdateKeyboardVisible = {}
        )
    }
}