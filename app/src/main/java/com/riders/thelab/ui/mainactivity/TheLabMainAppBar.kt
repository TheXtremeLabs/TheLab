package com.riders.thelab.ui.mainactivity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_primary

@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
fun UserCardIcon() {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Card(
            modifier = Modifier
                .size(48.dp)
                .padding(top = 8.dp, end = 4.dp),
            onClick = {
                expanded = true
            },
            shape = CircleShape
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                imageVector = Icons.Filled.Person,
                contentDescription = "user_icon"
            )
        }

        DropdownMenu(
            modifier = Modifier
                .padding(0.dp)// margin
                .padding(8.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = "Refresh") },
                onClick = {
                    // Handle refresh!
                })

            Divider()

            DropdownMenuItem(
                text = { Text(text = "Settings") },
                onClick = {
                    // Handle settings!
                })

            Divider()

            DropdownMenuItem(
                text = { Text(text = "Send Feedback") },
                onClick = {
                    // Handle send feedback!
                })
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppBarContent(viewModel: MainActivityViewModel, focusManager: FocusManager) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val focus = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                color = Color.Black,
                shape = RoundedCornerShape(35.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 4.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon"
            )

            Spacer(modifier = Modifier.size(8.dp))

            TextField(
                value = viewModel.searchedAppRequest,
                onValueChange = { newValue -> viewModel.updateSearchAppRequest(newValue) },
                placeholder = { Text(text = "Search an App...") },
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.8f)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (focus.value != it.isFocused) {
                            focus.value = it.isFocused
                            if (!it.isFocused) {
                                keyboardController?.hide()
                                viewModel.updateKeyboardVisible(it.isFocused)
                            } else {
                                viewModel.updateKeyboardVisible(it.isFocused)
                            }
                        }
                    }
                // .navigationBarsPadding(),
                //    .background(Color.Black)
                ,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    focusedPlaceholderColor = Color.LightGray,
                    disabledTextColor = Color.Transparent,
                    cursorColor = md_theme_dark_primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    textAlign = TextAlign.Start
                ),
                maxLines = 1,
                trailingIcon = {
                    if (viewModel.searchedAppRequest.isNotBlank()) {
                        IconButton(
                            onClick = { viewModel.updateSearchAppRequest("") }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "close_icon"
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    KeyboardType.Text,
                    ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.size(8.dp))

            UserCardIcon()
        }
    }

    /*LaunchedEffect(focusRequester) {
        if (keyboardController?.equals(true) == true){
            focusRequester.requestFocus()
        }
    }*/
}

@Composable
fun TheLabMainTopAppBar(viewModel: MainActivityViewModel, focusManager: FocusManager) {
    TheLabTheme {
        Box(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .padding(0.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            AppBarContent(viewModel, focusManager)
        }
    }
}