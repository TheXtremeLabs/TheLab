package com.riders.thelab.ui.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.R
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
@Composable
fun Login(viewModel: LoginViewModel, focusRequester: FocusRequester) {

    val loginFieldState by viewModel.loginFieldUiState.collectAsStateWithLifecycle()
    val loginHasError by viewModel.loginHasError.collectAsStateWithLifecycle()

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
                colors = TextFieldDefaults.colors(
                    // containerColor = if (!focus.value) Color.DarkGray else lightBlue,
                    // textColor = if (!focus.value) Color.Gray else Color.White,
                    cursorColor = Color.Blue,
                    focusedIndicatorColor = Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

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

@Composable
fun Password(viewModel: LoginViewModel, focusRequester: FocusRequester) {
    val passwordFieldState by viewModel.passwordFieldUiState.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current
    val passwordVisible = remember { mutableStateOf(false) }

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
            colors = TextFieldDefaults.colors(
                // containerColor = if (!focus.value) Color.DarkGray else lightBlue,
                // textColor = if (!focus.value) Color.Gray else Color.White,
                cursorColor = Color.Blue,
                focusedIndicatorColor = Color.Transparent, //hide the indicator
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun ErrorMessage(viewModel: LoginViewModel) {
    val loginUiState by viewModel.loginUiState.collectAsStateWithLifecycle()
    val alphaAnimation = remember { Animatable(0f) }

    // Text error
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alphaAnimation.value),
        text = stringResource(id = R.string.err_msg_wrong_email_or_password),
        color = Color.Red
    )

    if (loginUiState is LoginUiState.UserError) {
        LaunchedEffect(loginUiState) {
            when (loginUiState) {
                is LoginUiState.UserError -> {
                    alphaAnimation.animateTo(1f)
                }

                else -> {
                    alphaAnimation.animateTo(0f)
                }
            }
        }
    }
}

@Composable
fun RememberUser(modifier: Modifier, viewModel: LoginViewModel) {
    Box(modifier = modifier, contentAlignment = CenterStart) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = viewModel.isRememberCredentials,
                onCheckedChange = { viewModel.updateIsRememberCredentials(it) }
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.remember_me),
                fontSize = 12.sp,
                maxLines = 2
            )
        }
    }
}

@Composable
fun Submit(modifier: Modifier, viewModel: LoginViewModel) {

    val uiState by viewModel.loginUiState.collectAsStateWithLifecycle()

    TheLabTheme {
        Box(modifier = modifier, contentAlignment = Alignment.CenterEnd) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    onClick = {
                        Timber.d("Login button clicked")
                        viewModel.login()
                    },
                    contentPadding = PaddingValues(8.dp),
                    enabled = uiState !is LoginUiState.Connecting
                ) {
                    AnimatedContent(
                        targetState = uiState,
                        label = "login_button_animated_content"
                    ) { targetState ->
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
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f),
                                        text = "Log In",
                                        fontSize = 14.sp,
                                        maxLines = 1
                                    )

                                    Icon(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .weight(.5f),
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Form(viewModel: LoginViewModel) {
    val loginUiState by viewModel.loginUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val windowSizeClass: WindowSizeClass = if (LocalInspectionMode.current) {
        WindowSizeClass.EXPANDED
    } else {
        (context as LoginActivity).getDeviceWindowsSizeClass()
    }

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
                            16.dp
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
            AnimatedVisibility(visible = loginUiState is LoginUiState.UserError) {
                Spacer(modifier = Modifier.size(8.dp))
                ErrorMessage(viewModel)
                Spacer(modifier = Modifier.size(8.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RememberUser(modifier = Modifier.weight(2f), viewModel = viewModel)
                Submit(modifier = Modifier.weight(1f), viewModel = viewModel)
            }
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
    Submit(modifier = Modifier, viewModel = viewModel)
}

@DevicePreviews
@Composable
fun PreviewForm() {
    val viewModel: LoginViewModel = hiltViewModel()
    Form(viewModel = viewModel)
}