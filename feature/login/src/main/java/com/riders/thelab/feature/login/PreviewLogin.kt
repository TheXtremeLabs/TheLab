package com.riders.thelab.feature.login

import android.annotation.SuppressLint
import android.view.View
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.riders.thelab.core.data.local.model.compose.LoginFieldsUIState
import com.riders.thelab.core.data.local.model.compose.LoginUiState
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsTV
import com.riders.thelab.core.ui.compose.component.LabHtmlText
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.Shapes
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.TheLabThemeTV
import com.riders.thelab.core.ui.compose.utils.animatePlacement
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

///////////////////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////////////////
@Composable
fun GoogleButton(
    theme: AppTheme,
    darkTheme: Boolean,
    modifier: Modifier = Modifier,
    uiEvent: (UiEvent) -> Unit
) {
    val activity = LocalActivity.current as LoginActivity

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .then(modifier),
            contentAlignment = Alignment.Center
        ) {
            if(activity.isTv){
                androidx.tv.material3.Button(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = { uiEvent.invoke(UiEvent.OnGoogleButtonLoginClicked) },
                    colors = androidx.tv.material3.ButtonDefaults.colors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = com.riders.thelab.core.ui.R.drawable.googleg_color),
                            contentDescription = "google_icon"
                        )
                        androidx.tv.material3.Text(
                            text = "Continue with Google".uppercase(Locale.getDefault()),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {

                Button(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = { uiEvent.invoke(UiEvent.OnGoogleButtonLoginClicked) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = com.riders.thelab.core.ui.R.drawable.googleg_color),
                            contentDescription = "google_icon"
                        )
                        Text(
                            text = "Continue with Google".uppercase(Locale.getDefault()),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SignUpButton(
    theme: AppTheme,
    darkTheme: Boolean,
    modifier: Modifier = Modifier,
    uiEvent: (UiEvent) -> Unit
) {
    val activity = LocalActivity.current as LoginActivity

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .then(modifier),
            contentAlignment = Alignment.Center
        ) {
            if(activity.isTv){
                androidx.tv.material3.Button(
                    onClick = { uiEvent.invoke(UiEvent.OnSignUpClicked) },
                    colors = androidx.tv.material3.ButtonDefaults.colors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                ) {
                    Box(
                        modifier = Modifier.widthIn(max = 240.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        LabHtmlText(
                            modifier = Modifier.wrapContentSize(),
                            stringResId = com.riders.thelab.core.ui.R.string.no_account_register,
                            textAlignment = View.TEXT_ALIGNMENT_CENTER,
                            onClick = { uiEvent.invoke(UiEvent.OnSignUpClicked) }
                        )
                    }
                }
            } else {
            Button(
                onClick = { uiEvent.invoke(UiEvent.OnSignUpClicked) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            ) {
                Box(
                    modifier = Modifier.wrapContentSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LabHtmlText(
                        modifier = Modifier.fillMaxWidth(),
                        stringResId = com.riders.thelab.core.ui.R.string.no_account_register,
                        textAlignment = View.TEXT_ALIGNMENT_CENTER,
                        onClick = { uiEvent.invoke(UiEvent.OnSignUpClicked) }
                    )
                }}
            }
        }
    }
}


///////////////////////////////////////////////////
//
// MOBILE
//
///////////////////////////////////////////////////
@SuppressLint("NewApi")
@Composable
fun LoginContent(
    theme: AppTheme,
    darkTheme: Boolean,
    version: String,
    loginUiState: LoginUiState,
    loginFieldState: LoginFieldsUIState.Login,
    login: String,
    loginHasError: Boolean,
    loginHasLocalError: Boolean,
    passwordFieldState: LoginFieldsUIState.Password,
    password: String,
    isRememberCredentialsChecked: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val versionVisibility = remember { mutableStateOf(true) }
    val formVisibility = remember { mutableStateOf(false) }
    val registerVisibility = remember { mutableStateOf(false) }

    var arrangement: Arrangement.Vertical by remember { mutableStateOf(Arrangement.Center) }

    TheLabTheme(theme = theme, darkTheme = darkTheme) {

        // Main column
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {
            // Logo icon with version
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = if (arrangement == Arrangement.Center) {
                    Arrangement.Center
                } else {
                    Arrangement.Bottom
                }
            ) {
                Card(
                    modifier = Modifier
                        .size(96.dp)
                        .animatePlacement(),
                    shape = Shapes.large,
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.ic_lab_twelve_background))
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        painter = painterResource(id = R.drawable.ic_lab_6_lab),
                        colorFilter = ColorFilter.tint(Color.White),
                        contentDescription = "Lab Icon"
                    )
                }

                // Version
                AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.CenterHorizontally),
                    visible = if (LocalInspectionMode.current) false else versionVisibility.value,
                    exit = fadeOut()
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = if (LocalInspectionMode.current) "12.0.0" else version,
                        style = TextStyle(
                            color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }

            // Form visibility
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                visible = if (LocalInspectionMode.current) true else formVisibility.value
            ) {
                Form(
                    theme = theme,
                    darkTheme = darkTheme,
                    loginUiState = loginUiState,
                    loginFieldState = loginFieldState,
                    login = login,
                    loginHasError = loginHasError,
                    loginHasLocalError = loginHasLocalError,
                    passwordFieldState = passwordFieldState,
                    password = password,
                    isRememberCredentialsChecked = isRememberCredentialsChecked,
                    uiEvent = uiEvent
                )
            }

            // Register button
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(40.dp)
                    .weight(1f),
                visible = if (LocalInspectionMode.current) true else registerVisibility.value
            ) {
                // SignUp button
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GoogleButton(
                        theme = theme,
                        darkTheme = darkTheme,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        uiEvent = uiEvent
                    )

                    HorizontalDivider(modifier = Modifier.fillMaxWidth(.85f))

                    SignUpButton(
                        theme = theme,
                        darkTheme = darkTheme,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        uiEvent = uiEvent
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        scope.launch {
            delay(200L)
            versionVisibility.value = false
            delay(200L)
            formVisibility.value = true
            registerVisibility.value = true
            delay(150L)
            arrangement = Arrangement.Bottom

            /*if (BuildConfig.DEBUG) {
                uiEvent.invoke(UiEvent.OnLoginClicked)
            }*/
        }
    }

    if (loginUiState is LoginUiState.UserSuccess) {
        LaunchedEffect(Unit) {
            uiEvent.invoke(UiEvent.OnLaunchMainActivity)
        }
    }
}

///////////////////////////////////////////////////
//
// TV
//
///////////////////////////////////////////////////
@SuppressLint("NewApi")
@Composable
fun LoginContentTV(
    theme: AppTheme,
    darkTheme: Boolean,
    version: String,
    loginUiState: LoginUiState,
    loginFieldState: LoginFieldsUIState.Login,
    login: String,
    loginHasError: Boolean,
    loginHasLocalError: Boolean,
    passwordFieldState: LoginFieldsUIState.Password,
    password: String,
    isRememberCredentialsChecked: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    val scope = rememberCoroutineScope()

    val versionVisibility = remember { mutableStateOf(true) }
    val formVisibility = remember { mutableStateOf(false) }
    val registerVisibility = remember { mutableStateOf(false) }

    var arrangement: Arrangement.Vertical by remember { mutableStateOf(Arrangement.Center) }

    TheLabThemeTV(theme = theme, darkTheme = darkTheme) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            // Main column
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.8f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally)
            ) {
                // Logo icon with version
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = if (arrangement == Arrangement.Center) {
                        Arrangement.Center
                    } else {
                        Arrangement.Bottom
                    }
                ) {
                    androidx.tv.material3.Card(
                        modifier = Modifier
                            .size(96.dp)
                            .animatePlacement(),
                        onClick = {},
                        shape = androidx.tv.material3.CardDefaults.shape(),
                        colors = androidx.tv.material3.CardDefaults.colors(
                            containerColor = colorResource(
                                id = R.color.ic_lab_twelve_background
                            )
                        )
                    ) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            painter = painterResource(id = R.drawable.ic_lab_6_lab),
                            colorFilter = ColorFilter.tint(Color.White),
                            contentDescription = "Lab Icon"
                        )
                    }

                    // Version
                    AnimatedVisibility(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .align(Alignment.CenterHorizontally),
                        visible = if (LocalInspectionMode.current) false else versionVisibility.value,
                        exit = fadeOut()
                    ) {
                        androidx.tv.material3.Text(
                            modifier = Modifier.padding(8.dp),
                            text = if (LocalInspectionMode.current) "12.0.0" else version,
                            style = TextStyle(
                                color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }

                // Form visibility
                AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.5f),
                    visible = if (LocalInspectionMode.current) true else formVisibility.value
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Form(
                            theme = theme,
                            darkTheme = darkTheme,
                            loginUiState = loginUiState,
                            loginFieldState = loginFieldState,
                            login = login,
                            loginHasError = loginHasError,
                            loginHasLocalError = loginHasLocalError,
                            passwordFieldState = passwordFieldState,
                            password = password,
                            isRememberCredentialsChecked = isRememberCredentialsChecked,
                            uiEvent = uiEvent
                        )

                        Spacer(modifier = Modifier.size(36.dp))

                        // Register button
                        AnimatedVisibility(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(250.dp),
                            visible = if (LocalInspectionMode.current) true else registerVisibility.value
                        ) {
                            // SignUp button
                            Column(
                                modifier = Modifier,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                GoogleButton(
                                    theme = theme,
                                    darkTheme = darkTheme,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp),
                                    uiEvent = uiEvent
                                )

                                HorizontalDivider(modifier = Modifier.fillMaxWidth(.85f))

                                SignUpButton(
                                    theme = theme,
                                    darkTheme = darkTheme,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp),
                                    uiEvent = uiEvent
                                )
                            }
                        }
                    }

                }
            }
        }
    }

    LaunchedEffect(Unit) {
        scope.launch {
            delay(200L)
            versionVisibility.value = false
            delay(200L)
            formVisibility.value = true
            registerVisibility.value = true
            delay(150L)
            arrangement = Arrangement.Bottom

            /*if (BuildConfig.DEBUG) {
                uiEvent.invoke(UiEvent.OnLoginClicked)
            }*/
        }
    }

    if (loginUiState is LoginUiState.UserSuccess) {
        LaunchedEffect(Unit) {
            uiEvent.invoke(UiEvent.OnLaunchMainActivity)
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
fun PreviewSignUpButton(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = AppTheme.Default) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            SignUpButton(
                theme = appTheme,
                darkTheme = isSystemInDarkTheme(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
            ) {}
        }
    }
}

@DevicePreviews
@Composable
fun PreviewLoginContent(@PreviewParameter(PreviewProviderLoginState::class) loginUiState: LoginUiState) {
    val loginFieldUiState: LoginFieldsUIState.Login = LoginFieldsUIState.Login.Ok
    val passwordUiState: LoginFieldsUIState.Password = LoginFieldsUIState.Password.Ok

    val login =
        if (loginUiState is LoginUiState.UserSuccess) loginUiState.user.email else "john.smith@test.com"
    val password =
        if (loginUiState is LoginUiState.UserSuccess) loginUiState.user.password else "test1234"

    TheLabTheme(theme = AppTheme.Default) {
        LoginContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            version = "12.10.3",
            loginUiState = loginUiState,
            loginFieldState = loginFieldUiState,
            login = login,
            loginHasError = false,
            loginHasLocalError = false,
            passwordFieldState = passwordUiState,
            password = password,
            isRememberCredentialsChecked = true
        ) {}
    }
}

@DevicePreviewsTV
@Composable
fun PreviewLoginContentTV(@PreviewParameter(PreviewProviderLoginState::class) loginUiState: LoginUiState) {
    val loginFieldUiState: LoginFieldsUIState.Login = LoginFieldsUIState.Login.Ok
    val passwordUiState: LoginFieldsUIState.Password = LoginFieldsUIState.Password.Ok

    val login =
        if (loginUiState is LoginUiState.UserSuccess) loginUiState.user.email else "john.smith@test.com"
    val password =
        if (loginUiState is LoginUiState.UserSuccess) loginUiState.user.password else "test1234"

    TheLabTheme(theme = AppTheme.Default) {
        LoginContentTV(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            version = "12.10.3",
            loginUiState = loginUiState,
            loginFieldState = loginFieldUiState,
            login = login,
            loginHasError = false,
            loginHasLocalError = false,
            passwordFieldState = passwordUiState,
            password = password,
            isRememberCredentialsChecked = true
        ) {}
    }
}