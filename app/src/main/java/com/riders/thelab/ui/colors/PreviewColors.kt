package com.riders.thelab.ui.colors

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabColorsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode",
    showSystemUi = false
)
@Composable
fun Color() {

    // get current Context and coroutineScope
    val colorActivity = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Get colors values in order to take a random
    val colors = LabColorsManager.getDefaultColors()

    // Get a random color
    fun getRandomColor(excludeColor: Int? = null): Int = colors.filterNot { it == excludeColor }
        .random()

    // use of lambda
    val randomColor: (Int?) -> Int = { excludeColor ->
        colors.filterNot { it == excludeColor }.random()
    }

    val firstRandomColor = getRandomColor()

    var expanded by remember { mutableStateOf(false) }

    val colorState = remember { mutableStateOf(firstRandomColor) }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    /*if (isPressed) colors[Random().nextInt(colors.size)] else MaterialTheme.colorScheme.primary*/

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
                colors = TopAppBarDefaults.mediumTopAppBarColors(androidx.compose.ui.graphics.Color.Black)
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
                    onClick = { colorState.value = getRandomColor(colorState.value) },
                    modifier = Modifier
                        .padding(8.dp),
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

    run {
        coroutineScope.launch {
            delay(300)
            expanded = !expanded
        }
    }
}