package com.riders.thelab.feature.settings.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.data.local.model.User
import com.riders.thelab.core.data.local.model.compose.settings.UserUiState
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography
import com.riders.thelab.core.ui.compose.utils.findActivity
import java.util.Locale


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun EditProfileCardRowItem(username: String, email: String) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 12.dp)
            .clickable { (context.findActivity() as SettingsActivity).launchEditProfileActivity() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // USer Profile Image
        Box(
            modifier = Modifier
                .weight(.7f)
                .padding(vertical = 4.dp)
        ) {
            Card(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
//                    colors = CardDefaults.cardColors(containerColor = if (!isSystemInDarkTheme()) md_theme_dark_primaryContainer else md_theme_light_primaryContainer)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Filled.Person, contentDescription = null)
                }
            }
        }

        // Username and Email
        Column(
            modifier = Modifier.weight(2f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = username,
                style = TextStyle(fontWeight = FontWeight.W600),
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = email,
                style = TextStyle(fontWeight = FontWeight.W200, fontSize = 11.sp),
                overflow = TextOverflow.Ellipsis
            )
        }

        // Arrow Icon
        Box(
            modifier = Modifier.weight(.5f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "arrow_right"
            )
        }
    }
}

@Composable
fun UserSection(
    userUiState: UserUiState,
    uiEvent: (UiEvent) -> Unit
) {
    TheLabTheme {
        AnimatedContent(
            targetState = userUiState,
            label = "content_transition",
        ) { targetState ->

            when (targetState) {
                is UserUiState.Loading -> {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator()
                        Text(text = "Fetching device's data. Please wait...")
                    }
                }

                is UserUiState.Error -> {}
                is UserUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 24.dp),
                            text = "User",
                            style = Typography.titleMedium
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                EditProfileCardRowItem(
                                    username = targetState.user.username,
                                    email = targetState.user.email
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 24.dp, vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Button(
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { uiEvent.invoke(UiEvent.OnLogoutClicked) }
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.action_logout)
                                                .uppercase(Locale.getDefault())
                                        )
                                    }
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
private fun PreviewEditProfileCardRowItem(user: User = User.mockUserForTests[0]) {
    TheLabTheme {
        EditProfileCardRowItem(user.username, user.email)
    }
}

@DevicePreviews
@Composable
private fun PreviewUserSection(@PreviewParameter(PreviewProviderUserUiState::class) userUiState: UserUiState) {
    TheLabTheme {
        UserSection(userUiState = userUiState) {}
    }
}