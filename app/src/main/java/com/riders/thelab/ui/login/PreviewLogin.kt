@file:OptIn(ExperimentalMaterial3Api::class)

package com.riders.thelab.ui.login

import android.text.Html
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.ui.theme.Shapes
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.compose.utils.findActivity
import com.riders.thelab.data.local.model.compose.LoginUiState
import com.riders.thelab.navigator.Navigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun LoginContent(activity: LoginActivity, viewModel: LoginViewModel, navigator: Navigator) {

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
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
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
                }

                // Version
                AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(CenterHorizontally),
                    visible = if (LocalInspectionMode.current) true else versionVisibility.value,
                    exit = fadeOut()
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = if (LocalInspectionMode.current) "12.0.0" else viewModel.version,
                        style = TextStyle(color = Color.White, textAlign = TextAlign.Center)
                    )
                }

                // Form
                AnimatedVisibility(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    visible = if (LocalInspectionMode.current) false else formVisibility.value
                ) {
                    Spacer(modifier = Modifier.size(32.dp))
                    Form(viewModel = viewModel)
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
                        navigator.callSignUpActivity()
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
            delay(200L)
            formVisibility.value = true
            registerVisibility.value = true

            viewModel.login()
        }
    }

    if (loginUiState is LoginUiState.Error) {
        LaunchedEffect(Unit) {
            callMainActivity(activity, navigator)
        }
    }
}

fun callMainActivity(activity: LoginActivity, navigator: Navigator) {
    Timber.d("callMainActivity()")
    navigator.callMainActivity()
    activity.finish()
}


///////////////////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////////////////
@DevicePreviews
@Composable
fun PreviewLoginContent() {
    val context = LocalContext.current
    val activity = context.findActivity() as LoginActivity
    val viewModel: LoginViewModel = hiltViewModel()
    val navigator = Navigator(activity)

    TheLabTheme {
        LoginContent(
            activity = activity,
            viewModel = viewModel,
            navigator = navigator
        )
    }
}