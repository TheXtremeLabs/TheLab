package com.riders.thelab.core.compose.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.core.data.local.model.compose.IslandState
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_primary
import com.riders.thelab.ui.mainactivity.MainActivityViewModel
import timber.log.Timber

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Search(viewModel: MainActivityViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val focus = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
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
                },
            value = viewModel.searchedAppRequest.value,
            onValueChange = { viewModel.searchApp(it) },
            placeholder = { Text(text = stringResource(id = R.string.search_app_item_placeholder)) },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
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
                if (viewModel.searchedAppRequest.value.isNotBlank()) {
                    IconButton(
                        onClick = { viewModel.searchApp("") }
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
    }

    if (viewModel.dynamicIslandState.value is IslandState.SearchState) {
        Timber.d("Should request focus for textField")
    }
}


@DevicePreviews
@Composable
fun PreviewSearch() {
    val viewModel: MainActivityViewModel = hiltViewModel()
    Search(viewModel)
}