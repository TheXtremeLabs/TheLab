package com.riders.thelab.feature.mlkit.ui.compose.translate

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.SyncAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.riders.mlkitcompose.data.local.compose.translate.TranslateResultState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.feature.mlkit.ui.compose.barcodescanner.BarcodeScannerActivity
import java.util.Locale

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslateSelector(
    theme: AppTheme,
    darkTheme: Boolean,
    modifier: Modifier = Modifier,
    options: List<String>,
    selectedValue: String,
    label: String,
    onValueChangedEvent: (String) -> Unit
) {
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = modifier
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        //This value is used to assign to the DropDown the same width
                        textFieldSize = coordinates.size.toSize()
                    },
                readOnly = true,
                value = selectedValue,
                onValueChange = onValueChangedEvent,
                label = { Text(text = label) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            ExposedDropdownMenu(
                modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option: String ->
                    DropdownMenuItem(
                        modifier = Modifier.background(
                            if (selectedValue == option) MaterialTheme.colorScheme.primary.copy(
                                alpha = .533f
                            ) else Color.Transparent
                        ),
                        text = {
                            Text(
                                text = Locale(option).run {
                                    this.getDisplayLanguage(this).trim().uppercase()
                                },
                                style = TextStyle(fontWeight = if (selectedValue == option) FontWeight.W600 else FontWeight.W300)
                            )
                        },
                        onClick = {
                            expanded = false
                            onValueChangedEvent(option)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Header(
    theme: AppTheme,
    darkTheme: Boolean,
    modifier: Modifier = Modifier,
    options: List<String>,
    fromSelectedValue: String,
    toSelectedValue: String,
    uiEvent: (UiEvent) -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .then(modifier),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TranslateSelector(
                theme = theme,
                darkTheme = darkTheme,
                modifier = Modifier.weight(2f),
                options = options,
                selectedValue = fromSelectedValue,
                label = "From",
                onValueChangedEvent = { uiEvent.invoke(UiEvent.OnSourceLanguageChanged(it)) }
            )

            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    // Should switch content
                }
            ) { Icon(imageVector = Icons.Rounded.SyncAlt, contentDescription = null) }


            TranslateSelector(
                theme = theme,
                darkTheme = darkTheme,
                modifier = Modifier.weight(2f),
                options = options,
                selectedValue = toSelectedValue,
                label = "To",
                onValueChangedEvent = { uiEvent.invoke(UiEvent.OnTargetLanguageChanged(it)) }
            )
        }
    }
}

@Composable
fun InputFieldContent(
    theme: AppTheme,
    darkTheme: Boolean,
    modifier: Modifier,
    inputToTranslate: String,
    uiEvent: (UiEvent) -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Card(modifier = modifier) {
            TextField(
                modifier = Modifier.fillMaxSize(),
                value = if (LocalInspectionMode.current) "Hello" else inputToTranslate,
                onValueChange = { uiEvent.invoke(UiEvent.OnUpdateInput(it)) },
                textStyle = TextStyle(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { uiEvent.invoke(UiEvent.OnTranslate) },
                    onSearch = { uiEvent.invoke(UiEvent.OnTranslate) }),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun TranslateResultContent(
    theme: AppTheme,
    darkTheme: Boolean,
    modifier: Modifier,
    translatedResult: TranslateResultState
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Card(modifier = modifier) {

            when (translatedResult) {
                is TranslateResultState.Translated -> {
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        text = translatedResult.translation,
                        style = TextStyle(fontSize = 16.sp)
                    )
                }

                is TranslateResultState.Error -> {

                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        text = translatedResult.reason,
                        style = TextStyle(fontSize = 16.sp)
                    )
                }

                is TranslateResultState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.size(40.dp))
                }

                is TranslateResultState.Idle -> Box {}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslateContent(
    theme: AppTheme,
    darkTheme: Boolean,
    translatedResult: TranslateResultState,
    inputToTranslate: String,
    options: List<String>,
    fromSelectedValue: String,
    toSelectedValue: String,
    uiEvent: (UiEvent) -> Unit
) {
    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                // close Translator & Recognizer here
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose { lifecycleOwner.lifecycle.removeObserver(lifecycleObserver) }
    }

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(56.dp),
                    navigationIcon = {
                        IconButton(
                            modifier = Modifier.fillMaxHeight(),
                            onClick = { (context.findActivity() as BarcodeScannerActivity).backPressed() }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                contentDescription = "nav_back_icon"
                            )
                        }
                    },
                    title = {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clip(CircleShape),
                            verticalArrangement = Arrangement.Center
                        ) { Text(text = "Translate", textAlign = TextAlign.Center) }
                    },
                    actions = {
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(8.dp)
                                    .clip(CircleShape),
                                onClick = { uiEvent.invoke(UiEvent.OnTranslate) },
                                contentPadding = PaddingValues(4.dp),
                                enabled = inputToTranslate.isNotEmpty()
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InputFieldContent(
                    theme = theme,
                    darkTheme = darkTheme,
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth(),
                    inputToTranslate = inputToTranslate,
                    uiEvent = uiEvent
                )

                TranslateResultContent(
                    theme = theme,
                    darkTheme = darkTheme,
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth(),
                    translatedResult = translatedResult
                )

                Header(
                    theme = theme,
                    darkTheme = darkTheme,
                    modifier = Modifier
                        .weight(.75f)
                        .fillMaxWidth(),
                    options = options,
                    fromSelectedValue = fromSelectedValue,
                    toSelectedValue = toSelectedValue,
                    uiEvent = uiEvent
                )
            }
        }
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewTranslateSelector(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        TranslateSelector(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            modifier = Modifier.fillMaxWidth(),
            selectedValue = "English",
            options = listOf("English, French"),
            label = "From"
        ) {}
    }
}

@DevicePreviews
@Composable
private fun PreviewHeader(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        Header(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            modifier = Modifier.fillMaxWidth(),
            options = listOf("English, French"),
            fromSelectedValue = "English",
            toSelectedValue = "French",
        ) {}
    }
}

@DevicePreviews
@Composable
private fun PreviewInputFieldContent(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        InputFieldContent(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(), modifier = Modifier.fillMaxWidth(), "Hello"
        ) {}
    }
}

@DevicePreviews
@Composable
private fun PreviewTranslateResultContent(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        TranslateResultContent(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            modifier = Modifier.fillMaxWidth(),
            TranslateResultState.Translated("Bonjour")
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewTranslateContent(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        TranslateContent(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            translatedResult = TranslateResultState.Translated("Bonjour"),
            "Hello",
            listOf("English", "French"),
            "English",
            "French"
        ) {}
    }
}