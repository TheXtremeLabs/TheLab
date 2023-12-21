package com.riders.thelab.ui.login

import android.annotation.SuppressLint
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.BuildConfig
import com.riders.thelab.R
import com.riders.thelab.core.data.local.model.compose.LoginUiState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.LabHtmlText
import com.riders.thelab.core.ui.compose.theme.Shapes
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_onPrimaryContainer
import com.riders.thelab.core.ui.compose.theme.md_theme_light_onPrimaryContainer
import com.riders.thelab.core.ui.compose.utils.animatePlacement
import com.riders.thelab.core.ui.compose.utils.findActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun SignUpButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = if (!isSystemInDarkTheme()) md_theme_dark_onPrimaryContainer else md_theme_light_onPrimaryContainer),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LabHtmlText(
                    modifier = Modifier.fillMaxWidth(),
                    stringResId = com.riders.thelab.core.ui.R.string.no_account_register,
                    textAlignment = View.TEXT_ALIGNMENT_CENTER,
                    onClick = onClick
                )
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun LoginContent(viewModel: LoginViewModel) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val versionVisibility = remember { mutableStateOf(true) }
    val formVisibility = remember { mutableStateOf(false) }
    val registerVisibility = remember { mutableStateOf(false) }

    val loginUiState by viewModel.loginUiState.collectAsState()

    var arrangement: Arrangement.Vertical by remember {
        mutableStateOf(Arrangement.Center)
    }

    TheLabTheme {
        /*Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
            ) {
                // Logo icon with version
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
                        .align(Alignment.CenterHorizontally),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                    .height(40.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 72.dp),
                visible = if (LocalInspectionMode.current) true else registerVisibility.value
            ) {
                SignUpButton {
                    // Should register user
                    (context.findActivity() as LoginActivity).launchSignUpActivity()
                }
            }
        }*/

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
                            .padding(8.dp),
                        painter = painterResource(id = R.drawable.ic_the_lab_12_logo_white),
                        contentDescription = "Lab Icon"
                    )
                }

                // Version
                AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.CenterHorizontally),
                    visible = if (LocalInspectionMode.current) true else versionVisibility.value,
                    exit = fadeOut()
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = if (LocalInspectionMode.current) "12.0.0" else viewModel.version,
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
                visible = if (LocalInspectionMode.current) false else formVisibility.value
            ) {
                Form(viewModel = viewModel)
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
                    verticalArrangement = Arrangement.Center
                ) {
                    SignUpButton {
                        // Should register user
                        (context.findActivity() as LoginActivity).launchSignUpActivity()
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

            if (BuildConfig.DEBUG) {
                viewModel.login()
            }
        }
    }

    if (loginUiState is LoginUiState.UserSuccess) {
        LaunchedEffect(Unit) {
            (context.findActivity() as LoginActivity).launchMainActivity()
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
fun PreviewSignUpButton() {
    TheLabTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            SignUpButton {}
        }
    }
}

@DevicePreviews
@Composable
fun PreviewLoginContent() {
    val viewModel: LoginViewModel = hiltViewModel()
    TheLabTheme { LoginContent(viewModel = viewModel) }
}