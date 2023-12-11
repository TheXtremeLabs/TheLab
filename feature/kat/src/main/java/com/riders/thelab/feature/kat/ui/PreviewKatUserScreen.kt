package com.riders.thelab.feature.kat.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.feature.kat.utils.FirebaseUtils
import timber.log.Timber


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun KatUserScreen(viewModel: KatProfileViewModel) {

    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

    TheLabTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                UserIcon(modifier = Modifier.size(96.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Current username is ${viewModel.profileCurrentUsername}"
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .focusRequester(focusRequester),
                    value = viewModel.profileUsername,
                    onValueChange = { viewModel.updateProfileUsername(it) },
                    placeholder = { Text(text = "Update Username") },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .focusRequester(focusRequester),
                    value = viewModel.profilePhoneNumber,
                    onValueChange = { viewModel.updateProfilePhone(it) },
                    placeholder = { Text(text = "Update Phone") },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { Timber.d("update user info") }) {
                    Text(text = "Update")
                }

                Button(
                    onClick = {
                        Timber.d("logout user")
                        FirebaseUtils.logOut()
                        (context.findActivity() as KatMainActivity).finish()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(text = "LOGOUT", color = Color.White)
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
private fun PreviewKaUserScreen() {
    val viewModel: KatProfileViewModel = hiltViewModel()
    TheLabTheme {
        KatUserScreen(viewModel)
    }
}