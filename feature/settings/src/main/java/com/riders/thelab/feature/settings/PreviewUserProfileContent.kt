package com.riders.thelab.feature.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_surfaceVariant
import com.riders.thelab.core.ui.compose.theme.md_theme_light_background
import com.riders.thelab.core.ui.compose.theme.md_theme_light_surfaceVariant
import java.util.Locale

private val lightGradient =
    listOf(
        Color.Transparent,
        md_theme_light_background,
        md_theme_light_surfaceVariant,
        md_theme_light_surfaceVariant
    )
private val darkGradient =
    listOf(
        Color.Transparent,
        md_theme_dark_background,
        md_theme_dark_surfaceVariant,
        md_theme_dark_surfaceVariant
    )

///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun FormFields(viewModel: UserProfileViewModel) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val shape = RoundedCornerShape(12.dp)

    val focusRequester = remember { FocusRequester() }

    val passwordVisibility = remember { mutableStateOf(false) }
    val passwordConfirmationVisibility = remember { mutableStateOf(false) }
    val passwordsHasError by viewModel.passwordsHasError.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // First Name
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = viewModel.firstname,
            onValueChange = { viewModel.updateFirstname(it) },
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
                cursorColor = Color.Blue,
                focusedIndicatorColor = Color.Transparent, //hide the indicator
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        // Password
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = viewModel.password,
            onValueChange = { viewModel.updatePassword(it) },
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
            label = { Text(text = "New " + stringResource(id = com.riders.thelab.core.ui.R.string.hint_confirm_password)) },
            placeholder = { Text(text = "New " + stringResource(id = com.riders.thelab.core.ui.R.string.hint_confirm_your_password)) },
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
                cursorColor = Color.Blue,
                focusedIndicatorColor = Color.Transparent, //hide the indicator
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun UserProfileContent(viewModel: UserProfileViewModel) {
    val lazyListState = rememberLazyListState()

    TheLabTheme(viewModel.isDarkMode) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TheLabTopAppBar(
                    viewModel = viewModel,
                    title = stringResource(id = R.string.title_activity_user_information)
                )
            }
        ) { contentPadding ->
            // Background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = Brush.verticalGradient(if (!viewModel.isDarkMode) lightGradient else darkGradient))
                    .zIndex(1f)
            )

            // Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .zIndex(5f),
                state = lazyListState,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {

                    // User Profile Image
                    Box(
                        modifier = Modifier.padding(top = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .size(96.dp),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier.fillMaxSize(),
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }

                item {
                    AnimatedContent(
                        targetState = null != viewModel.user,
                        label = ""
                    ) { targetState ->

                        if (!targetState) {
                            CircularProgressIndicator()
                        } else {
                            // Username and Email
                            Column(
                                modifier = Modifier.padding(top = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = viewModel.user?.username!!,
                                    style = TextStyle(
                                        fontWeight = FontWeight.W600,
                                        fontSize = 24.sp
                                    ),
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = viewModel.user?.email!!,
                                    style = TextStyle(
                                        fontWeight = FontWeight.W200,
                                        fontSize = 16.sp
                                    ),
                                    overflow = TextOverflow.Ellipsis
                                )

                                // Form
                                FormFields(viewModel = viewModel)

                                // Update buttons
                                Button(
                                    modifier = Modifier.padding(bottom = 16.dp),
                                    onClick = { viewModel.updateUser() }
                                ) {
                                    Text(text = "Update Info".uppercase(Locale.getDefault()))
                                }
                            }
                        }
                    }
                }
            }
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
private fun PreviewUserProfileContent() {
    val viewModel: UserProfileViewModel = hiltViewModel()
    TheLabTheme {
        UserProfileContent(viewModel = viewModel)
    }
}