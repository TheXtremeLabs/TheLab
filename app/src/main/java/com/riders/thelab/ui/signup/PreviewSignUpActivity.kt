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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.riders.thelab.core.data.local.model.User
import com.riders.thelab.core.data.local.model.compose.UserState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background
import com.riders.thelab.core.ui.compose.theme.md_theme_light_background
import com.riders.thelab.core.ui.compose.utils.findActivity
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.web.EmailAddress
import timber.log.Timber


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@OptIn(ExperimentalKotoolsTypesApi::class)
@Composable
fun SignUpNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    onUpdateCurrentNavDestination: (NavDestination) -> Unit,
    isDarkMode: Boolean,
    userUiState: UserState,
    userFormButtonEnabled: Boolean,
    onSubmitForm: () -> Unit,
    isSubmitSuccess: Boolean,
    message: String,
    emailHasError: Boolean,
    passwordsHasError: Boolean,
    firstname: String,
    onUpdateFirstname: (String) -> Unit,
    lastname: String,
    onUpdateLastname: (String) -> Unit,
    username: String,
    onUpdateUsername: (String) -> Unit,
    email: EmailAddress,
    onUpdateEmail: (EmailAddress) -> Unit,
    password: String,
    onUpdatePassword: (String) -> Unit,
    passwordConfirmation: String,
    onUpdatePasswordConfirmation: (String) -> Unit,
    shouldShowSaveOrErrorView: Boolean,
    onUpdateShouldShowExitDialogConfirmation: (Boolean) -> Unit
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
                isDarkMode = isDarkMode,
                onNavigateToUserFormScreen = { navController.navigate(SignUpScreen.Form.route) }
            )
        }
        composable(SignUpScreen.Form.route) {
            FormScreen(
                userUiState = userUiState,
                isDarkMode = isDarkMode,
                emailHasError = emailHasError,
                passwordsHasError = passwordsHasError,
                firstname = firstname,
                onUpdateFirstname = onUpdateFirstname,
                lastname = lastname,
                onUpdateLastname = onUpdateLastname,
                username = username,
                onUpdateUsername = onUpdateUsername,
                email = email,
                onUpdateEmail = onUpdateEmail,
                password = password,
                onUpdatePassword = onUpdatePassword,
                passwordConfirmation = passwordConfirmation,
                onUpdatePasswordConfirmation = onUpdatePasswordConfirmation,
                userFormButtonEnabled = userFormButtonEnabled,
                onSubmitForm = onSubmitForm,
                isSubmitSuccess = isSubmitSuccess,
                message = message,
                shouldShowSaveOrErrorView = shouldShowSaveOrErrorView,
                onUpdateShouldShowExitDialogConfirmation = onUpdateShouldShowExitDialogConfirmation,
                onNavigateToSignUpSuccessScreen = { navController.navigate(SignUpScreen.SignUpSuccess.route) }
            )
        }
        composable(SignUpScreen.SignUpSuccess.route) {
            SignUpSuccessScreen(
                isDarkMode = isDarkMode,
                username = username,
                onNavigateToSignUpSuccessScreen = { (context.findActivity() as SignUpActivity).launchMainActivity() }
            )
        }
    }

    LaunchedEffect(currentDestination) {
        currentDestination?.let { onUpdateCurrentNavDestination(it) }
    }
}

@OptIn(ExperimentalKotoolsTypesApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpContent(
    isDarkMode: Boolean,
    currentDestination: NavDestination?,
    onUpdateCurrentNavDestination: (NavDestination) -> Unit,
    userUiState: UserState,
    emailHasError: Boolean,
    passwordsHasError: Boolean,
    firstname: String,
    onUpdateFirstname: (String) -> Unit,
    lastname: String,
    onUpdateLastname: (String) -> Unit,
    username: String,
    onUpdateUsername: (String) -> Unit,
    email: EmailAddress,
    onUpdateEmail: (EmailAddress) -> Unit,
    password: String,
    onUpdatePassword: (String) -> Unit,
    passwordConfirmation: String,
    onUpdatePasswordConfirmation: (String) -> Unit,
    shouldShowExitDialogConfirmation: Boolean,
    onUpdateShouldShowExitDialogConfirmation: (Boolean) -> Unit,
    userFormButtonEnabled: Boolean,
    onSubmitForm: () -> Unit,
    isSubmitSuccess: Boolean,
    message: String,
    shouldShowSaveOrErrorView: Boolean
) {
    val context = LocalContext.current

    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    TheLabTheme(darkTheme = isDarkMode) {
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
                    currentDestination?.let {
                        SignUpHeader(
                            isDarkMode = isDarkMode,
                            currentDestination = it,
                            onUpdateShouldShowExitDialogConfirmation = onUpdateShouldShowExitDialogConfirmation
                        )
                    }

                    // Content
                    SignUpNavHost(
                        modifier = Modifier.fillMaxWidth(),
                        isDarkMode = isDarkMode,
                        navController = navController,
                        startDestination = SignUpScreen.EULA.route,
                        emailHasError = emailHasError,
                        passwordsHasError = passwordsHasError,
                        firstname = firstname,
                        onUpdateFirstname = onUpdateFirstname,
                        lastname = lastname,
                        onUpdateLastname = onUpdateLastname,
                        username = username,
                        onUpdateUsername = onUpdateUsername,
                        email = email,
                        onUpdateEmail = onUpdateEmail,
                        password = password,
                        onUpdatePassword = onUpdatePassword,
                        passwordConfirmation = passwordConfirmation,
                        onUpdatePasswordConfirmation = onUpdatePasswordConfirmation,
                        userUiState = userUiState,
                        onUpdateCurrentNavDestination = onUpdateCurrentNavDestination,
                        userFormButtonEnabled = userFormButtonEnabled,
                        onSubmitForm = onSubmitForm,
                        isSubmitSuccess = isSubmitSuccess,
                        message = message,
                        shouldShowSaveOrErrorView = shouldShowSaveOrErrorView,
                        onUpdateShouldShowExitDialogConfirmation = onUpdateShouldShowExitDialogConfirmation
                    )
                }

                // Exit dialog
                if (shouldShowExitDialogConfirmation) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Black.copy(alpha = .7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedVisibility(visible = shouldShowExitDialogConfirmation) {
                            ExitSignUp(
                                onConfirmed = { (context.findActivity() as SignUpActivity).finish() },
                                onDismiss = { onUpdateShouldShowExitDialogConfirmation(false) }
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(navBackStackEntry) {
        Timber.d("LaunchedEffect | navBackStackEntry")
        navBackStackEntry?.destination?.let { onUpdateCurrentNavDestination(it) }
    }
}


///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@OptIn(ExperimentalKotoolsTypesApi::class)
@DevicePreviews
@Composable
private fun PreviewSignUpNavHost(@PreviewParameter(PreviewProviderUserState::class) state: UserState) {
    val user: User = User.mockUserForTests[0]

    SignUpNavHost(
        modifier = Modifier.fillMaxSize(),
        navController = rememberNavController(),
        startDestination = SignUpScreen.EULA.route,
        isDarkMode = isSystemInDarkTheme(),
        userUiState = state,
        emailHasError = false,
        passwordsHasError = false,
        firstname = user.firstname,
        onUpdateFirstname = {},
        lastname = user.lastname,
        onUpdateLastname = {},
        username = user.username,
        onUpdateUsername = {},
        email = EmailAddress.create(user.email),
        onUpdateEmail = {},
        password = user.password,
        onUpdatePassword = {},
        passwordConfirmation = user.password,
        onUpdatePasswordConfirmation = {},
        onUpdateCurrentNavDestination = {},
        userFormButtonEnabled = state !is UserState.Saving,
        onSubmitForm = {},
        isSubmitSuccess = state is UserState.Saved,
        message = "Please enter",
        shouldShowSaveOrErrorView = state is UserState.NotSaved,
        onUpdateShouldShowExitDialogConfirmation = {}
    )
}

@OptIn(ExperimentalKotoolsTypesApi::class)
@DevicePreviews
@Composable
private fun PreviewSignUpContent(@PreviewParameter(PreviewProviderUserState::class) state: UserState) {
    val user: User = User.mockUserForTests[0]

    TheLabTheme {
        SignUpContent(
            isDarkMode = isSystemInDarkTheme(),
            currentDestination = NavDestination(""),
            onUpdateCurrentNavDestination = {},
            userUiState = state,
            emailHasError = false,
            passwordsHasError = false,
            firstname = user.firstname,
            onUpdateFirstname = {},
            lastname = user.lastname,
            onUpdateLastname = {},
            username = user.username,
            onUpdateUsername = {},
            email = EmailAddress.create(user.email),
            onUpdateEmail = {},
            password = user.password,
            onUpdatePassword = {},
            passwordConfirmation = user.password,
            onUpdatePasswordConfirmation = {},
            shouldShowExitDialogConfirmation = false,
            onUpdateShouldShowExitDialogConfirmation = {},
            userFormButtonEnabled = state !is UserState.Saving,
            onSubmitForm = {},
            isSubmitSuccess = state is UserState.Saved,
            message = "Please enter",
            shouldShowSaveOrErrorView = state is UserState.NotSaved
        )
    }
}