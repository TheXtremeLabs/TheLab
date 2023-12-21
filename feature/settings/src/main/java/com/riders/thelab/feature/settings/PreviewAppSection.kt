package com.riders.thelab.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography
import kotlinx.coroutines.launch


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppThemeCardRowItem(
    preselectedThemeOptions: String,
    themeOptions: List<String>,
    onOptionsSelected: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val expanded = remember { mutableStateOf(false) }
    val selectedText = remember {
        mutableStateOf(themeOptions.firstOrNull { it == preselectedThemeOptions }
            ?: themeOptions.last())
    }

    val focusManager: FocusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            text = "Dark Mode"
        )

        ExposedDropdownMenuBox(
            modifier = Modifier.weight(1f),
            expanded = expanded.value,
            onExpandedChange = { expanded.value = !expanded.value }
        ) {
            TextField(
                value = selectedText.value,
                onValueChange = { newValue: String -> selectedText.value = newValue },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        //This value is used to assign to the DropDown the same width
                        textFieldSize = coordinates.size.toSize()
                    }
                    // .focusRequester(focusRequester)
                    .menuAnchor(),
                textStyle = TextStyle(textAlign = TextAlign.End),
                trailingIcon = {
                    IconButton(
                        onClick = { expanded.value = !expanded.value }
                    ) {
                        Icon(
                            modifier = Modifier.clickable {
                                scope.launch {
                                }
                            },
                            imageVector = if (!expanded.value) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                            contentDescription = "contentDescription"
                        )
                    }
                },
                singleLine = true,
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                readOnly = true
            )

            ExposedDropdownMenu(
                modifier = Modifier
                    .width(with(LocalDensity.current) {
                        textFieldSize.width.toDp()
                    }
                ),
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                themeOptions.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onOptionsSelected(option)
                            selectedText.value = option
                            expanded.value = false
                            focusManager.clearFocus(true)
                        },
                        text = { Text(text = option) },
                    )
                }
            }
        }
    }
}

@Composable
fun VibrationCardRowItem(isVibration: Boolean, onToggleVibration: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1.5f),
            verticalArrangement = Arrangement.spacedBy(
                8.dp,
                Alignment.CenterVertically
            )
        ) {
            Text(text = "Vibration")
            Text(
                text = if (isVibration) "Disable Vibration" else "Enable Vibration",
                style = TextStyle(
                    fontSize = 12.sp
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .weight(.5f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Switch(
                    modifier = Modifier
                        .size(30.dp)
                        .padding(horizontal = 24.dp),
                    checked = isVibration,
                    onCheckedChange = {
                        onToggleVibration(it)
                    }
                )
            }
        }
    }
}

@Composable
fun AppSettingsSection(viewModel: SettingsViewModel) {
    TheLabTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 24.dp),
                text = "App Settings",
                style = Typography.titleMedium
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    AppThemeCardRowItem(
                        preselectedThemeOptions = if (viewModel.isDarkMode) viewModel.themeOptions.first {
                            it.contains(
                                "Dark",
                                true
                            )
                        } else viewModel.themeOptions[2],
                        themeOptions = viewModel.themeOptions
                    ) {
                        if (it.contains("system", true)) {
                            viewModel.updateDarkMode(viewModel.isDarkMode)
                        } else {
                            viewModel.updateDarkModeDatastore()
                        }
                    }

                    VibrationCardRowItem(isVibration = viewModel.isVibration) {
                        viewModel.updateVibrationDatastore()
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
private fun PreviewAppThemeCardRowItem() {
    val themeOptions: List<String> = listOf("Light", "Dark", "Use System")
    TheLabTheme {
        AppThemeCardRowItem("Dark", themeOptions) {}
    }
}

@DevicePreviews
@Composable
private fun PreviewVibrationCardRowItem() {
    TheLabTheme {
        VibrationCardRowItem(isVibration = true) {}
    }
}

@DevicePreviews
@Composable
private fun PreviewAppSettingsSection() {
    val viewModel: SettingsViewModel = hiltViewModel()
    TheLabTheme {
        AppSettingsSection(viewModel)
    }
}