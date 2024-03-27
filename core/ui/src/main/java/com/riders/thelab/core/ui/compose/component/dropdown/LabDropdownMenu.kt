package com.riders.thelab.core.ui.compose.component.dropdown

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun <T> LabDropdownMenu(
    modifier: Modifier,
    textFieldState: TextFieldState,
    expanded: Boolean,
    onExpandedChanged: (Boolean) -> Unit,
    onExpandIconClicked: () -> Unit,
    placeholder: String,
    selectedText: String,
    onSelectedTextChanged: (String) -> Unit,
    suggestions: List<T>,
    onOptionsSelected: (T) -> Unit,
    dropdownItemContent: @Composable (index: Int) -> Unit
) {
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var isFieldFocused: Boolean by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocus by interactionSource.collectIsFocusedAsState()


    val borderColorAnimation by animateColorAsState(
        targetValue = if (!isFieldFocused) Color.White.copy(alpha = .58f) else Color(0xFFD37818).copy(
            alpha = .75f
        ),
        label = "border animation animation"
    )

    Timber.d("Recomposition | selectedText: $selectedText, textFieldState.text: ${textFieldState.text}")

    TheLabTheme {
        ExposedDropdownMenuBox(
            modifier = modifier,
            expanded = expanded,
            onExpandedChange = {
                // onExpandedChanged(!expanded)
            }
        ) {
            BasicTextField2(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        //This value is used to assign to the DropDown the same width
                        textFieldSize = coordinates.size.toSize()
                    }
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (isFieldFocused != it.isFocused) {
                            isFieldFocused = it.isFocused
                            if (!it.isFocused) {
                                keyboardController?.hide()
                            } else {
                                keyboardController?.show()
                            }
                        }
                    }
                    .indication(
                        interactionSource = interactionSource,
                        indication = LocalIndication.current
                    )
                    .menuAnchor(),
                state = textFieldState,
                textStyle = TextStyle(
                    textAlign = TextAlign.Justify,
                    color = Color.LightGray
                ),
                interactionSource = interactionSource,
                keyboardActions = KeyboardActions(),
                lineLimits = TextFieldLineLimits.SingleLine,
                decorator = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 2.dp,
                                color = borderColorAnimation,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        AnimatedVisibility(
                            visible = selectedText.isEmpty() || textFieldState.text.isEmpty(),
                            enter = fadeIn() + slideInHorizontally(),
                            exit = slideOutHorizontally() + fadeOut()
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                text = placeholder,
                                style = TextStyle(
                                    fontWeight = FontWeight.W400,
                                    color = Color.LightGray.copy(alpha = .83f)
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            // you have to invoke this function then cursor will focus and you will able to write something
                            innerTextField.invoke()
                        }
                    }
                },
                readOnly = true,
                cursorBrush = SolidColor(Color.LightGray)
            )

            ExposedDropdownMenu(
                modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
                expanded = expanded,
                onDismissRequest = { onExpandedChanged(false) }
            ) {
                suggestions.forEachIndexed { itemIndex, option ->
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onOptionsSelected(option)
                            onSelectedTextChanged(option.toString())
                            onExpandedChanged(false)
                            focusManager.clearFocus(true)
                        },
                        text = { dropdownItemContent(itemIndex) },
                    )
                }
            }
        }
    }

    BackHandler {
        if (expanded) {
            onExpandedChanged(!expanded)
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


@OptIn(ExperimentalFoundationApi::class)
@DevicePreviews
@Composable
private fun PreviewLabDropdownMenu() {
    val textFieldState = rememberTextFieldState()
    TheLabTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            LabDropdownMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textFieldState = textFieldState,
                expanded = true,
                onExpandedChanged = {},
                onExpandIconClicked = { },
                placeholder = "Select an option",
                selectedText = "None",
                onSelectedTextChanged = {},
                suggestions = listOf("test", "mike", "chronopost", "john", "None"),
                onOptionsSelected = {},
                dropdownItemContent = {}
            )
        }
    }
}