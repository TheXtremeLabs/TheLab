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
import com.riders.thelab.data.local.model.compose.LoginUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .size(96.dp),
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
                    modifier = Modifier.fillMaxWidth(0.5f),
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
            versionVisibility.value = false
            delay(200L)
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
@DevicePreviews
@Composable
fun PreviewLoginContent() {
    val viewModel: LoginViewModel = hiltViewModel()
    TheLabTheme {
        LoginContent(viewModel = viewModel)
    }
}