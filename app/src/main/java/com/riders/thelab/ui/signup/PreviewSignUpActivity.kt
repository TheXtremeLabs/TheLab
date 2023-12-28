package com.riders.thelab.ui.signup

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background
import com.riders.thelab.core.ui.compose.theme.md_theme_light_background
import com.riders.thelab.core.ui.compose.utils.findActivity
import timber.log.Timber


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun SignUpNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    viewModel: SignUpViewModel
) {
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(SignUpScreen.EULA.route) {
            EULAScreen(
                viewModel = viewModel,
                onNavigateToUserFormScreen = { navController.navigate(SignUpScreen.Form.route) }
            )
        }
        composable(SignUpScreen.Form.route) {
            FormScreen(
                viewModel = viewModel,
                onNavigateToSignUpSuccessScreen = { navController.navigate(SignUpScreen.SignUpSuccess.route) }
            )
        }
        composable(SignUpScreen.SignUpSuccess.route) {
            SignUpSuccessScreen(
                viewModel = viewModel,
                onNavigateToSignUpSuccessScreen = { (context.findActivity() as SignUpActivity).launchMainActivity() }
            )
        }
    }

    LaunchedEffect(currentDestination) {
        currentDestination?.let { viewModel.updateCurrentNavDestination(it) }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpContent(viewModel: SignUpViewModel) {
    val context = LocalContext.current

    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    TheLabTheme(darkTheme = viewModel.isDarkMode) {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            // Main Container
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                // NavHost
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background)
                ) {
                    // Header
                    viewModel.currentDestination?.let {
                        SignUpHeader(
                            viewModel = viewModel,
                            currentDestination = it
                        )
                    }

                    // Content
                    SignUpNavHost(
                        modifier = Modifier.fillMaxWidth(),
                        viewModel = viewModel,
                        navController = navController,
                        startDestination = SignUpScreen.EULA.route
                    )
                }

                // Exit dialog
                if (viewModel.shouldShowExitDialogConfirmation) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Black.copy(alpha = .7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedVisibility(visible = viewModel.shouldShowExitDialogConfirmation) {
                            ExitSignUp(
                                onConfirmed = { (context.findActivity() as SignUpActivity).finish() },
                                onDismiss = { viewModel.updateShouldShowExitDialogConfirmation(false) }
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(navBackStackEntry) {
        Timber.d("LaunchedEffect | navBackStackEntry")
        navBackStackEntry?.destination?.let { viewModel.updateCurrentNavDestination(it) }
    }
}


///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
private fun PreviewSignUpNavHost() {
    val viewModel: SignUpViewModel = hiltViewModel()
    SignUpNavHost(
        modifier = Modifier.fillMaxSize(),
        viewModel = viewModel,
        navController = rememberNavController(),
        startDestination = SignUpScreen.EULA.route
    )
}

@DevicePreviews
@Composable
private fun PreviewSignUpContent() {
    val viewModel: SignUpViewModel = hiltViewModel()
    TheLabTheme {
        SignUpContent(viewModel = viewModel)
    }
}