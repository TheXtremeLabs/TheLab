package com.riders.thelab.ui.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.core.data.local.model.compose.LoginUiState
import com.riders.thelab.core.data.local.model.compose.WindowSizeClass
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_primary
import com.riders.thelab.core.ui.compose.theme.md_theme_light_primary
import timber.log.Timber

///////////////////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////////////////
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(viewModel: LoginViewModel, focusRequester: FocusRequester) {
    TheLabTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = viewModel.login,
                onValueChange = { viewModel.updateLogin(it) },
                placeholder = { Text(text = "Login") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp),
                // Change different colors of the text field view
                colors = TextFieldDefaults.textFieldColors(
                    // containerColor = if (!focus.value) Color.DarkGray else lightBlue,
                    // textColor = if (!focus.value) Color.Gray else Color.White,
                    cursorColor = Color.Blue,
                    focusedIndicatorColor = Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            val loginHasError by viewModel.loginHasError.collectAsStateWithLifecycle()

            AnimatedVisibility(visible = loginHasError) {
                Text(
                    text = "Login not available. Please choose a different one.",
                    color = Color(0xFFF02828)
                )
            }

            AnimatedVisibility(visible = viewModel.loginHasLocalError) {
                Text(
                    text = "Login contains invalid characters!",
                    color = Color(0xFFF02828)
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Password(viewModel: LoginViewModel, focusRequester: FocusRequester) {
    val passwordVisible = remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    TheLabTheme {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = viewModel.password,
            onValueChange = { viewModel.updatePassword(it) },
            placeholder = { Text(text = "Password (6+ characters") },
            leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisible.value = !passwordVisible.value
                }) {
                    Icon(
                        imageVector = if (!passwordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            shape = RoundedCornerShape(bottomStart = 35.dp, bottomEnd = 35.dp),
            // Change different colors of the text field view
            colors = TextFieldDefaults.textFieldColors(
                // containerColor = if (!focus.value) Color.DarkGray else lightBlue,
                // textColor = if (!focus.value) Color.Gray else Color.White,
                cursorColor = Color.Blue,
                focusedIndicatorColor = Color.Transparent, //hide the indicator
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Submit(viewModel: LoginViewModel) {

    val status by viewModel.loginUiState.collectAsStateWithLifecycle()

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
        Column(
            modifier = Modifier
                .fillMaxWidth(0.35f)
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    //.padding(top = 16.dp)
                    .then(clickable)
                    .indication(
                        interactionSource = interactionSource,
                        indication = LocalIndication.current
                    ),
                onClick = {
                    Timber.d("Login button clicked")
                    clickNumber += 1

                    when (clickNumber) {
                        10, 20, 30 -> {
                            Timber.d("${interactions.toString()}")
                        }
                    }
                    viewModel.login()
                }
            ) {
                AnimatedContent(targetState = status) { targetState ->
                    when (targetState) {
                        LoginUiState.Connecting -> {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = if (!isSystemInDarkTheme()) md_theme_dark_primary else md_theme_light_primary
                                )
                            }
                        }

                        else -> {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    modifier = Modifier.align(CenterVertically),
                                    text = text
                                )

                                Icon(
                                    modifier = Modifier.align(CenterVertically),
                                    imageVector = Icons.Filled.KeyboardArrowRight,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
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


@Composable
fun Form(viewModel: LoginViewModel) {

    val context = LocalContext.current

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val windowSizeClass: WindowSizeClass = if (LocalInspectionMode.current) {
        WindowSizeClass.EXPANDED
    } else {
        (context as LoginActivity).getDeviceWindowsSizeClass()
    }

    var passwordVisibility: Boolean by remember { mutableStateOf(false) }

    TheLabTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    when (windowSizeClass) {
                        WindowSizeClass.MEDIUM -> {
                            56.dp
                        }

                        WindowSizeClass.EXPANDED -> {
                            72.dp
                        }

                        WindowSizeClass.COMPACT -> {
                            0.dp
                        }

                        else -> {
                            0.dp
                        }
                    }
                ),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Login(viewModel, focusRequester)
            Password(viewModel, focusRequester)
            Submit(viewModel)
        }
    }
}


///////////////////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////////////////
@DevicePreviews
@Composable
fun PreviewLogin() {
    val viewModel: LoginViewModel = hiltViewModel()
    Login(viewModel = viewModel, focusRequester = FocusRequester())
}

@DevicePreviews
@Composable
fun PreviewPassword() {
    val viewModel: LoginViewModel = hiltViewModel()
    Password(viewModel = viewModel, focusRequester = FocusRequester())
}

@DevicePreviews
@Composable
fun PreviewSubmit() {
    val viewModel: LoginViewModel = hiltViewModel()
    Submit(viewModel = viewModel)
}

@DevicePreviews
@Composable
fun PreviewForm() {
    val viewModel: LoginViewModel = hiltViewModel()
    Form(viewModel = viewModel)
}