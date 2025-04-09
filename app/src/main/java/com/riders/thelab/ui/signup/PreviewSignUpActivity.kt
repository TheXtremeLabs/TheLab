package com.riders.thelab.ui.signup

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
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
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import org.kotools.types.ExperimentalKotoolsTypesApi
import org.kotools.types.EmailAddress
import timber.log.Timber


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@OptIn(ExperimentalKotoolsTypesApi::class)
@Composable
fun SignUpNavHost(
    theme: AppTheme,
    darkTheme: Boolean,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    onUpdateCurrentNavDestination: (NavDestination) -> Unit,
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
                theme = theme,
                darkTheme = darkTheme,
                onNavigateToUserFormScreen = { navController.navigate(SignUpScreen.Form.route) }
            )
        }
        composable(SignUpScreen.Form.route) {
            FormScreen(
                theme = theme,
                darkTheme = darkTheme,
                userUiState = userUiState,
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
                theme = theme, darkTheme = darkTheme,
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
    theme: AppTheme,
    darkTheme: Boolean,
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

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            // Main Container
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                // NavHost
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    // Header
                    currentDestination?.let {
                        SignUpHeader(
                            theme = theme, darkTheme = darkTheme,
                            currentDestination = it,
                            onUpdateShouldShowExitDialogConfirmation = onUpdateShouldShowExitDialogConfirmation
                        )
                    }

                    // Content
                    SignUpNavHost(
                        theme = theme, darkTheme = darkTheme,
                        modifier = Modifier.fillMaxWidth(),
                        navController = navController,
                        startDestination = SignUpScreen.EULA.route,
                        onUpdateCurrentNavDestination = onUpdateCurrentNavDestination,
                        userUiState = userUiState,
                        userFormButtonEnabled = userFormButtonEnabled,
                        onSubmitForm = onSubmitForm,
                        isSubmitSuccess = isSubmitSuccess,
                        message = message,
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
                                theme = theme,
                                darkTheme = darkTheme,
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
        theme = AppTheme.Default,
        darkTheme = isSystemInDarkTheme(),
        modifier = Modifier.fillMaxSize(),
        navController = rememberNavController(),
        startDestination = SignUpScreen.EULA.route,
        userUiState = state,
        emailHasError = false,
        passwordsHasError = false,
        firstname = user.firstname,
        onUpdateFirstname = {},
        lastname = user.lastname,
        onUpdateLastname = {},
        username = user.username,
        onUpdateUsername = {},
        email = EmailAddress.orThrow(user.email),
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

    TheLabTheme(theme = AppTheme.Default) {
        SignUpContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
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
            email = EmailAddress.orThrow(user.email),
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