package com.riders.thelab.ui.colors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.LabColorsManager
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Color(viewModel: ColorViewModel) {

    // get current Context and coroutineScope
    val colorActivity = LocalContext.current

    val firstRandomColor = LabColorsManager.getRandomColor()

    var expanded by remember { mutableStateOf(false) }

    val colorState = remember { mutableStateOf(firstRandomColor) }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    TheLabTheme(darkTheme = viewModel.isDarkMode) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.activity_title_colors),
                            color = colorResource(id = colorState.value)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { (colorActivity as ColorActivity).onBackPressed() }) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowLeft,
                                contentDescription = "Back",
                                tint = colorResource(id = colorState.value)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Black)
                )
            }) { contentPadding ->
            // Screen content
            Column(
                modifier = Modifier
                    .background(color = colorResource(id = R.color.default_dark))
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(id = R.string.colors_text_placeholder),
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        color = colorResource(id = colorState.value),
                    )
                )

                AnimatedVisibility(visible = expanded) {
                    Button(
                        onClick = {
                            colorState.value = LabColorsManager.getRandomColor(colorState.value)
                        },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = colorState.value)),
                        shape = RoundedCornerShape(16.dp),
                        interactionSource = interactionSource
                    ) {
                        Text(
                            stringResource(id = R.string.btn_change_color).uppercase(),
                            color = colorResource(id = if (colorState.value == R.color.white || colorState.value == R.color.yellow) R.color.black else R.color.white)
                        )
                    }
                }
            }
        }
    }


    LaunchedEffect(key1 = Unit) {
        delay(300)
        expanded = !expanded
    }
}


@DevicePreviews
@Composable
private fun PreviewColor() {
    val viewModel: ColorViewModel = hiltViewModel()
    TheLabTheme(darkTheme = viewModel.isDarkMode) {
        Color(viewModel = viewModel)
    }
}