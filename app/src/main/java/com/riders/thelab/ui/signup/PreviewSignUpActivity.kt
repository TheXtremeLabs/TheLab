package com.riders.thelab.ui.signup

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
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
                onNavigateToSignUpSuccessScreen = { navController.navigate(SignUpScreen.Form.route) }
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

    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    TheLabTheme(darkTheme = viewModel.isDarkMode) {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
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