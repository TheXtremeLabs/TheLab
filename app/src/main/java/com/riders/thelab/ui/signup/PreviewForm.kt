package com.riders.thelab.ui.signup

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.R
import com.riders.thelab.core.data.local.model.compose.UserState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FormFields(modifier: Modifier, viewModel: SignUpViewModel) {

    val verticalScroll = rememberScrollState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val shape = RoundedCornerShape(12.dp)

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val emailHasError by viewModel.emailHasError.collectAsStateWithLifecycle()
    val passwordVisibility = remember { mutableStateOf(false) }
    val passwordConfirmationVisibility = remember { mutableStateOf(false) }
    val passwordsHasError by viewModel.passwordsHasError.collectAsStateWithLifecycle()

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
            value = viewModel.firstname,
            onValueChange = { viewModel.updateFirstname(it) },
            placeholder = { Text(text = "First Name") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            shape = shape,
            // Change different colors of the text field view
            colors = TextFieldDefaults.textFieldColors(
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
            value = viewModel.lastname,
            onValueChange = { viewModel.updateLastname(it) },
            placeholder = { Text(text = "Last Name") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            shape = shape,
            // Change different colors of the text field view
            colors = TextFieldDefaults.textFieldColors(
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
            value = viewModel.username,
            onValueChange = { viewModel.updateUsername(it) },
            placeholder = { Text(text = "Username") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            shape = shape,
            // Change different colors of the text field view
            colors = TextFieldDefaults.textFieldColors(
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
            value = viewModel.email,
            onValueChange = { viewModel.updateEmail(it) },
            placeholder = { Text(text = "Email") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.AlternateEmail,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            shape = shape,
            // Change different colors of the text field view
            colors = TextFieldDefaults.textFieldColors(
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
            value = viewModel.password,
            onValueChange = { viewModel.updatePassword(it) },
            placeholder = { Text(text = "Password (6+ characters") },
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
                autoCorrect = false,
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
            colors = TextFieldDefaults.textFieldColors(
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
            value = viewModel.passwordConfirmation,
            onValueChange = { viewModel.updatePasswordConfirmation(it) },
            placeholder = { Text(text = "Password (6+ characters") },
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
                autoCorrect = false,
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
            colors = TextFieldDefaults.textFieldColors(
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
fun SubmitFormButton(viewModel: SignUpViewModel, userState: UserState) {
    Button(
        onClick = { viewModel.submitForm() },
        enabled = viewModel.userFormButtonEnabled && userState !is UserState.Saving
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FormScreen(viewModel: SignUpViewModel, onNavigateToSignUpSuccessScreen: () -> Unit) {

    val context = LocalContext.current
    val shape = RoundedCornerShape(12.dp)
    val keyboardController = LocalSoftwareKeyboardController.current

    val verticalScroll = rememberScrollState()

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val userState by viewModel.userState.collectAsStateWithLifecycle()

    val emailHasError by viewModel.emailHasError.collectAsStateWithLifecycle()
    val passwordVisibility = remember { mutableStateOf(false) }
    val passwordConfirmationVisibility = remember { mutableStateOf(false) }
    val passwordsHasError by viewModel.passwordsHasError.collectAsStateWithLifecycle()

    TheLabTheme(darkTheme = viewModel.isDarkMode) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            // Form Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(text = "Please fill this form to register ")

                FormFields(
                    modifier = Modifier
                        .weight(3f)
                    //    .verticalScroll(verticalScroll)
                    ,
                    viewModel = viewModel
                )

                /*Column(
                    modifier = Modifier
                        .weight(3f)
                        .verticalScroll(verticalScroll),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // First Name
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        value = viewModel.firstname,
                        onValueChange = { viewModel.updateFirstname(it) },
                        placeholder = { Text(text = "First Name") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        shape = shape,
                        // Change different colors of the text field view
                        colors = TextFieldDefaults.textFieldColors(
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
                        value = viewModel.lastname,
                        onValueChange = { viewModel.updateLastname(it) },
                        placeholder = { Text(text = "Last Name") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        shape = shape,
                        // Change different colors of the text field view
                        colors = TextFieldDefaults.textFieldColors(
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
                        value = viewModel.username,
                        onValueChange = { viewModel.updateUsername(it) },
                        placeholder = { Text(text = "Username") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        shape = shape,
                        // Change different colors of the text field view
                        colors = TextFieldDefaults.textFieldColors(
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
                        value = viewModel.email,
                        onValueChange = { viewModel.updateEmail(it) },
                        placeholder = { Text(text = "Email") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.AlternateEmail,
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        shape = shape,
                        // Change different colors of the text field view
                        colors = TextFieldDefaults.textFieldColors(
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
                        value = viewModel.password,
                        onValueChange = { viewModel.updatePassword(it) },
                        placeholder = { Text(text = "Password (6+ characters") },
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
                            autoCorrect = false,
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
                        colors = TextFieldDefaults.textFieldColors(
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
                        value = viewModel.passwordConfirmation,
                        onValueChange = { viewModel.updatePasswordConfirmation(it) },
                        placeholder = { Text(text = "Password (6+ characters") },
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
                            autoCorrect = false,
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
                        colors = TextFieldDefaults.textFieldColors(
                            // containerColor = if (!focus.value) Color.DarkGray else lightBlue,
                            // textColor = if (!focus.value) Color.Gray else Color.White,
                            cursorColor = Color.Blue,
                            focusedIndicatorColor = Color.Transparent, //hide the indicator
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }*/

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(.5f),
                    contentAlignment = Alignment.Center
                ) {
                    SubmitFormButton(viewModel, userState)
                    /*Button(
                        onClick = { viewModel.submitForm() },
                        enabled = viewModel.userFormButtonEnabled && userState !is UserState.Saving
                    ) {
                        Row(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AnimatedVisibility(visible = userState is UserState.Saving) {
                                CircularProgressIndicator()
                            }
                            Text(text = stringResource(id = R.string.action_continue))
                        }
                    }*/
                }
            }


            // Save or Error info top view
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                visible = viewModel.shouldShowSaveOrErrorView
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(modifier = Modifier.padding(start = 8.dp), text = viewModel.message)
                    CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
                }
            }
        }
    }

    LaunchedEffect(viewModel.isSubmitSuccess) {
        if (viewModel.isSubmitSuccess) {
            onNavigateToSignUpSuccessScreen()
        }
    }
}

///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
private fun PreviewSubmitFormButton() {
    val viewModel: SignUpViewModel = hiltViewModel()
    TheLabTheme {
        SubmitFormButton(viewModel, UserState.Saving)
    }
}

@DevicePreviews
@Composable
private fun PreviewFormFields() {
    val viewModel: SignUpViewModel = hiltViewModel()

    TheLabTheme {
        FormFields(Modifier.fillMaxSize(), viewModel)
    }
}

@DevicePreviews
@Composable
private fun PreviewFormScreen() {
    val viewModel: SignUpViewModel = hiltViewModel()

    TheLabTheme(darkTheme = viewModel.isDarkMode) {
        FormScreen(viewModel = viewModel) {}
    }
}