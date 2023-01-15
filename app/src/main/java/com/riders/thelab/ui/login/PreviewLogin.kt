@file:OptIn(ExperimentalMaterial3Api::class)

package com.riders.thelab.ui.login

import android.text.Html
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.ui.theme.Shapes
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.compose.ui.theme.md_theme_dark_onPrimary
import com.riders.thelab.data.local.model.compose.LoginUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginForm(viewModel: LoginViewModel) {

    val loginUiState by viewModel.loginUiState.collectAsState()

    TheLabTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            // Login
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(12.dp),
                    imageVector = Icons.Filled.Person, contentDescription = "login icon"
                )

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.login.value,
                    onValueChange = { viewModel.updateLogin(it) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Email
                    )
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            // Password
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(12.dp),
                    imageVector = Icons.Filled.Lock, contentDescription = "login icon"
                )

                TextField(
                    value = viewModel.password.value,
                    onValueChange = { viewModel.updatePassword(it) },
                    maxLines = 1,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Ascii
                    )
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .height(36.dp),
                onClick = { viewModel.login() }) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedVisibility(
                        modifier = Modifier.fillMaxSize(),
                        visible = loginUiState !is LoginUiState.Loading,
                        exit = fadeOut(animationSpec = tween(300, easing = FastOutSlowInEasing))
                    ) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = Icons.Filled.ArrowRight,
                            contentDescription = "login_icon"
                        )
                    }

                    AnimatedVisibility(
                        modifier = Modifier.fillMaxSize(),
                        visible = (loginUiState is LoginUiState.Loading),
                        enter = fadeIn(animationSpec = tween(300, easing = FastOutSlowInEasing))
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(38.dp),
                            color = md_theme_dark_onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoginContent(viewModel: LoginViewModel) {

    val context = LocalContext.current

    val loginUiState by viewModel.loginUiState.collectAsState()

    val versionVisibility = remember { mutableStateOf(true) }
    val formVisibility = remember { mutableStateOf(false) }
    val registerVisibility = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    TheLabTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier.size(96.dp),
                    shape = Shapes.large,
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.ic_lab_twelve_background))
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        painter = painterResource(id = R.drawable.ic_the_lab_12_logo_white),
                        contentDescription = "Lab Icon"
                    )
                }

                // Version
                AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(bottom = 72.dp),
                    visible = if (LocalInspectionMode.current) true else versionVisibility.value,
                    exit = fadeOut()
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = if (LocalInspectionMode.current) "12.0.0" else "12.0.0",
                        style = TextStyle(color = Color.White)
                    )
                }

                // Form
                AnimatedVisibility(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    visible = if (LocalInspectionMode.current) false else formVisibility.value
                ) {
                    Spacer(modifier = Modifier.size(32.dp))
                    LoginForm(viewModel = viewModel)
                }
            }

            // Register button
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 72.dp),
                visible = if (LocalInspectionMode.current) true else registerVisibility.value
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        // Should register user
                        (context as LoginActivity).callSignUpActivity()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text =
                            Html
                                .fromHtml(
                                    stringResource(id = R.string.no_account_register),
                                    Html.FROM_HTML_MODE_LEGACY
                                )
                                .toString(),
                            color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        scope.launch {
            delay(200L)
            versionVisibility.value = false
            formVisibility.value = true
            registerVisibility.value = true
        }
    }

    if (loginUiState is LoginUiState.Error) {
        LaunchedEffect(Unit) {
            scope.launch {
                (context as LoginActivity).callMainActivity()
            }
        }
    }

}


///////////////////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////////////////
@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
fun LoginForm() {

    val loginUiState = remember { mutableStateOf(value = LoginUiState.Loading) }

    val login = remember { mutableStateOf("test@test.fr") }
    val password = remember { mutableStateOf("12345") }

    TheLabTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            // Login
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(12.dp),
                    imageVector = Icons.Filled.Person,
                    contentDescription = "login icon"
                )

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = login.value,
                    onValueChange = { login.value = it },
                    maxLines = 1,
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            // Password
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(12.dp),
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "login icon"
                )

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password.value,
                    onValueChange = { password.value = it },
                    maxLines = 1,
                    visualTransformation = PasswordVisualTransformation()
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .height(36.dp),
                onClick = { /*TODO*/ }) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedVisibility(
                        modifier = Modifier.fillMaxSize(),
                        visible = !(loginUiState.value is LoginUiState.Loading),
                        exit = fadeOut(animationSpec = tween(300, easing = FastOutSlowInEasing))
                    ) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = Icons.Filled.ArrowRight,
                            contentDescription = "login_icon"
                        )
                    }
                    AnimatedVisibility(
                        modifier = Modifier.fillMaxSize(),
                        visible = (loginUiState.value is LoginUiState.Loading),
                        enter = fadeIn(animationSpec = tween(300, easing = FastOutSlowInEasing))
                    ) {
                        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}

@DevicePreviews
@Composable
fun LoginContent() {

    val versionVisibility = remember { mutableStateOf(true) }
    val formVisibility = remember { mutableStateOf(false) }
    val registerVisibility = remember { mutableStateOf(false) }

    TheLabTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .size(96.dp)
                        .padding(bottom = 48.dp),
                    shape = Shapes.large,
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.ic_lab_twelve_background))
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        painter = painterResource(id = R.drawable.ic_the_lab_12_logo_white),
                        contentDescription = "Lab Icon"
                    )
                }

                // Version
                AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 72.dp),
                    visible = if (LocalInspectionMode.current) false else versionVisibility.value
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = if (LocalInspectionMode.current) "12.0.0" else "12.0.0",
                        style = TextStyle(color = Color.White)
                    )
                }

                // Form
                AnimatedVisibility(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    visible = if (LocalInspectionMode.current) true else formVisibility.value
                ) {
                    LoginForm()
                }
            }


            // Register button
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 72.dp),
                visible = if (LocalInspectionMode.current) true else registerVisibility.value
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        // Should register user
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text =
                            Html
                                .fromHtml(
                                    stringResource(id = R.string.no_account_register),
                                    Html.FROM_HTML_MODE_LEGACY
                                )
                                .toString(),
                            color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}