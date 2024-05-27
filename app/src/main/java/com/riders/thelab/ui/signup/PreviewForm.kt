package com.riders.thelab.ui.signup

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.R
import com.riders.thelab.core.data.local.model.User
import com.riders.thelab.core.data.local.model.compose.UserState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import org.kotools.types.EmailAddress
import timber.log.Timber

///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@OptIn(ExperimentalKotoolsTypesApi::class)
@Composable
fun FormFields(
    modifier: Modifier,
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
    onUpdatePasswordConfirmation: (String) -> Unit
) {

    val verticalScroll = rememberScrollState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val shape = RoundedCornerShape(12.dp)

    //val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val passwordVisibility = remember { mutableStateOf(false) }
    val passwordConfirmationVisibility = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .verticalScroll(verticalScroll),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // First Name
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = firstname,
            onValueChange = { onUpdateFirstname(it) },
            label = { Text(text = stringResource(id = com.riders.thelab.core.ui.R.string.hint_first_name)) },
            placeholder = { Text(text = stringResource(id = com.riders.thelab.core.ui.R.string.hint_first_name)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            shape = shape,
            // Change different colors of the text field view
            colors = TextFieldDefaults.colors(
                // containerColor = if (!focus.value) Color.DarkGray else lightBlue,
                // textColor = if (!focus.value) Color.Gray else Color.White,
                cursorColor = Color.Blue,
                focusedIndicatorColor = Color.Transparent, //hide the indicator
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        // Last Name
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = lastname,
            onValueChange = { onUpdateLastname(it) },
            label = { Text(text = stringResource(id = com.riders.thelab.core.ui.R.string.hint_last_name)) },
            placeholder = { Text(text = stringResource(id = com.riders.thelab.core.ui.R.string.hint_last_name)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            shape = shape,
            // Change different colors of the text field view
            colors = TextFieldDefaults.colors(
                // containerColor = if (!focus.value) Color.DarkGray else lightBlue,
                // textColor = if (!focus.value) Color.Gray else Color.White,
                cursorColor = Color.Blue,
                focusedIndicatorColor = Color.Transparent, //hide the indicator
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        // Username
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = username,
            onValueChange = { onUpdateUsername(it) },
            label = { Text(text = stringResource(id = com.riders.thelab.core.ui.R.string.hint_username)) },
            placeholder = { Text(text = stringResource(id = com.riders.thelab.core.ui.R.string.hint_username)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            shape = shape,
            // Change different colors of the text field view
            colors = TextFieldDefaults.colors(
                // containerColor = if (!focus.value) Color.DarkGray else lightBlue,
                // textColor = if (!focus.value) Color.Gray else Color.White,
                cursorColor = Color.Blue,
                focusedIndicatorColor = Color.Transparent, //hide the indicator
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        // Email
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = email.toString(),
            onValueChange = { onUpdateEmail(EmailAddress.fromString(it)) },
            label = { Text(text = stringResource(id = com.riders.thelab.core.ui.R.string.hint_email)) },
            placeholder = { Text(text = stringResource(id = com.riders.thelab.core.ui.R.string.hint_email)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.AlternateEmail,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            shape = shape,
            // Change different colors of the text field view
            colors = TextFieldDefaults.colors(
                // containerColor = if (!focus.value) Color.DarkGray else lightBlue,
                // textColor = if (!focus.value) Color.Gray else Color.White,
                cursorColor = Color.Blue,
                focusedIndicatorColor = Color.Transparent, //hide the indicator
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        AnimatedVisibility(visible = emailHasError) {
            Text(
                text = "Please enter a valid e-mail address.",
                color = Color(0xFFF02828)
            )
        }

        // Password
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = password,
            onValueChange = { onUpdatePassword(it) },
            label = { Text(text = stringResource(id = com.riders.thelab.core.ui.R.string.hint_password)) },
            placeholder = { Text(text = stringResource(id = com.riders.thelab.core.ui.R.string.hint_password_min_characters)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility.value = !passwordVisibility.value
                }) {
                    Icon(
                        imageVector = if (!passwordVisibility.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            shape = shape,
            // Change different colors of the text field view
            colors = TextFieldDefaults.colors(
                // containerColor = if (!focus.value) Color.DarkGray else lightBlue,
                // textColor = if (!focus.value) Color.Gray else Color.White,
                cursorColor = Color.Blue,
                focusedIndicatorColor = Color.Transparent, //hide the indicator
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        AnimatedVisibility(visible = passwordsHasError) {
            Text(
                text = "Login not available. Please choose a different one.",
                color = Color(0xFFF02828)
            )
        }

        // Password Confirmation
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = passwordConfirmation,
            onValueChange = { onUpdatePasswordConfirmation(it) },
            label = { Text(text = stringResource(id = com.riders.thelab.core.ui.R.string.hint_confirm_password)) },
            placeholder = { Text(text = stringResource(id = com.riders.thelab.core.ui.R.string.hint_confirm_your_password)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(onClick = {
                    passwordConfirmationVisibility.value =
                        !passwordConfirmationVisibility.value
                }) {
                    Icon(
                        imageVector = if (!passwordConfirmationVisibility.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (passwordConfirmationVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            shape = shape,
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
fun SubmitFormButton(
    userState: UserState,
    userFormButtonEnabled: Boolean,
    onSubmitForm: () -> Unit
) {
    Button(
        onClick = onSubmitForm,
        enabled = userFormButtonEnabled && userState !is UserState.Saving
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(visible = userState is UserState.Saving) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }

            Text(text = stringResource(id = R.string.action_continue))
        }
    }
}

@OptIn(ExperimentalKotoolsTypesApi::class)
@Composable
fun FormScreen(
    userUiState: UserState,
    isDarkMode: Boolean,
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
    userFormButtonEnabled: Boolean,
    onSubmitForm: () -> Unit,
    isSubmitSuccess: Boolean,
    message: String,
    shouldShowSaveOrErrorView: Boolean,
    onUpdateShouldShowExitDialogConfirmation: (Boolean) -> Unit,
    onNavigateToSignUpSuccessScreen: () -> Unit
) {
    TheLabTheme(darkTheme = isDarkMode) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            // Form Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Please fill this form to register",
                    color = if (!isDarkMode) Color.Black else Color.White
                )

                FormFields(
                    modifier = Modifier.weight(3f),
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
                    onUpdatePasswordConfirmation = onUpdatePasswordConfirmation
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(.5f),
                    contentAlignment = Alignment.Center
                ) {
                    SubmitFormButton(
                        userState = userUiState,
                        userFormButtonEnabled = userFormButtonEnabled,
                        onSubmitForm = onSubmitForm
                    )
                }
            }


            // Save or Error info top view
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                visible = shouldShowSaveOrErrorView
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(modifier = Modifier.padding(start = 8.dp), text = message)
                    CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
                }
            }
        }
    }

    LaunchedEffect(isSubmitSuccess) {
        if (isSubmitSuccess) {
            onNavigateToSignUpSuccessScreen()
        }
    }

    BackHandler(enabled = true) {
        Timber.e("BackHandler()")
        onUpdateShouldShowExitDialogConfirmation(true)
    }
}

///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
private fun PreviewSubmitFormButton(@PreviewParameter(PreviewProviderUserState::class) state: UserState) {
    TheLabTheme {
        SubmitFormButton(userState = state, userFormButtonEnabled = state !is UserState.Saving) {}
    }
}

@OptIn(ExperimentalKotoolsTypesApi::class)
@DevicePreviews
@Composable
private fun PreviewFormFields(@PreviewParameter(PreviewProviderUser::class) user: User) {
    TheLabTheme {
        FormFields(
            modifier = Modifier.fillMaxSize(),
            emailHasError = false,
            passwordsHasError = false,
            firstname = user.firstname,
            onUpdateFirstname = {},
            lastname = user.lastname,
            onUpdateLastname = {},
            username = user.username,
            onUpdateUsername = {},
            email = EmailAddress.fromString(user.email),
            onUpdateEmail = {},
            password = user.password,
            onUpdatePassword = {},
            passwordConfirmation = user.password,
            onUpdatePasswordConfirmation = {}
        )
    }
}

@OptIn(ExperimentalKotoolsTypesApi::class)
@DevicePreviews
@Composable
private fun PreviewFormScreen(@PreviewParameter(PreviewProviderUserState::class) state: UserState) {
    val user: User = User.mockUserForTests[0]

    TheLabTheme {
        FormScreen(
            userUiState = state,
            isDarkMode = isSystemInDarkTheme(),
            userFormButtonEnabled = state !is UserState.Saving,
            onSubmitForm = {},
            isSubmitSuccess = state is UserState.Saved,
            message = "Please enter",
            shouldShowSaveOrErrorView = false,
            onUpdateShouldShowExitDialogConfirmation = {},
            onNavigateToSignUpSuccessScreen = {},
            emailHasError = false,
            passwordsHasError = false,
            firstname = user.firstname,
            onUpdateFirstname = {},
            lastname = user.lastname,
            onUpdateLastname = {},
            username = user.username,
            onUpdateUsername = {},
            email = EmailAddress.fromString(user.email),
            onUpdateEmail = {},
            password = user.password,
            onUpdatePassword = {},
            passwordConfirmation = user.password,
            onUpdatePasswordConfirmation = {}
        )
    }
}