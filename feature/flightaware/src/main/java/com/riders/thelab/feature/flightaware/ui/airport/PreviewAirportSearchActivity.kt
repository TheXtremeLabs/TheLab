package com.riders.thelab.feature.flightaware.ui.airport

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.flight.AirportSearchModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography
import com.riders.thelab.feature.flightaware.core.theme.backgroundColor
import com.riders.thelab.feature.flightaware.core.theme.cardBackgroundColor
import com.riders.thelab.feature.flightaware.core.theme.searchTextColor
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import timber.log.Timber


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AirportSearchContent(
    airportQuery: String,
    onUpdateAirportQuery: (String) -> Unit,
    isQueryLoading:Boolean,
    airportList: List<AirportSearchModel>
) {
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()

    var isFieldFocused: Boolean by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocus by interactionSource.collectIsFocusedAsState()

    val borderAnimation by animateDpAsState(
        targetValue = if (!isFieldFocused) 0.dp else 2.dp,
        label = "border width animation"
    )

    val airportFoundCount = airportList.size

    TheLabTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        BasicTextField2(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .onFocusChanged {
                                    if (isFieldFocused != it.isFocused) {
                                        isFieldFocused = it.isFocused
                                        if (!it.isFocused) {
                                            keyboardController?.hide()
//                                            onUpdateKeyboardVisible(it.isFocused)
                                        } else {
//                                            onUpdateKeyboardVisible(it.isFocused)
                                            keyboardController?.show()
                                        }
                                    }
                                }
                                .indication(
                                    interactionSource = interactionSource,
                                    indication = LocalIndication.current
                                ),
                            value = airportQuery,
                            onValueChange = onUpdateAirportQuery,
                            textStyle = TextStyle(textAlign = TextAlign.Start, color = Color.White),
                            interactionSource = interactionSource,
                            keyboardActions = KeyboardActions(),
                            decorator = { innerTextFieldDecorator ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .border(
                                            width = borderAnimation,
                                            color = searchTextColor,
                                            shape = RoundedCornerShape(8.dp)
                                        ),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    TextFieldDefaults.DecorationBox(
                                        value = airportQuery,
                                        placeholder = {
                                            Text(
                                                text = "Enter an airport ID (CDG, ORY, JFK,...)",
                                                color = Color.LightGray
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = "Airport ID",
                                                color = Color.LightGray
                                            )
                                        },
                                        interactionSource = interactionSource,
                                        enabled = true,
                                        singleLine = true,
                                        visualTransformation = VisualTransformation.None,
                                        innerTextField = {
                                            innerTextFieldDecorator.invoke()
                                        },
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = cardBackgroundColor,
                                            unfocusedContainerColor = backgroundColor,
                                            focusedIndicatorColor = Color.Transparent, //hide the indicator
                                            unfocusedIndicatorColor = Color.Transparent
                                        )
                                    )
                                }
                            },
                            lineLimits = TextFieldLineLimits.SingleLine,
                            cursorBrush = SolidColor(Color.White),
                            readOnly = false
                        )
                        /*TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = airportQuery,
                            onValueChange = onUpdateAirportQuery,
                            placeholder = { Text(text = "Enter an airport ID (CDG, ORY, JFK,...)") },
                            label = { Text(text = "Airport ID") },
                            singleLine = true,
                            maxLines = 1
                        )*/
                    },
                    navigationIcon = {
                        IconButton(onClick = { (context as AirportSearchActivity).backPressed() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                contentDescription = "navigation_back_icon",
                                tint = Color.LightGray
                            )
                        }
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = backgroundColor)
                )

            },
            containerColor = backgroundColor
        ) { contentPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .clickable {
                        if (isFieldFocused) {
                            focusRequester.freeFocus()
                            focusManager.clearFocus()
                            isFieldFocused = false
                        }
                    }
                    .indication(
                        indication = null,
                        interactionSource = interactionSource
                    ),
                state = lazyListState,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    AnimatedContent(
                        targetState = 0 != airportFoundCount,
                        label = "airport found animation"
                    ) { targetState ->
                        if (!targetState) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = "Here will be displayed, the list of airports found",
                                style = Typography.titleMedium,
                                color = Color.White
                            )
                        } else {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = "$airportFoundCount airport(s) found",
                                style = Typography.displaySmall,
                                color = Color.White
                            )
                        }
                    }
                }

                items(items = airportList) { item -> AirportSearchItem(item = item) }
            }

            AnimatedVisibility(modifier = Modifier.fillMaxWidth(), visible = isQueryLoading) {
                LinearProgressIndicator()
            }
        }
    }

    LaunchedEffect(interactionSource) {
        Timber.d("LaunchedEffect | interactionSource: $interactionSource | coroutineContext: ${this.coroutineContext}")

        if (isPressed) {
            Timber.d("Pressed")
        }
        if (isFocus) {
            Timber.d("Focused")
        }
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@OptIn(ExperimentalKotoolsTypesApi::class)
@DevicePreviews
@Composable
private fun PreviewAirportSearchContent(@PreviewParameter(PreviewProviderAirports::class) airports: List<AirportSearchModel>) {
    TheLabTheme {
        AirportSearchContent(airportQuery = "LAX", onUpdateAirportQuery =  {}, isQueryLoading = true, airportList = airports)
    }
}