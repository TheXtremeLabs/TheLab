package com.riders.thelab.ui.colors

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabColorsManager
import timber.log.Timber
import java.util.*

class ColorActivity : ComponentActivity() {

    lateinit var colors: IntArray
    var fromColor = 0
    var toColor = 0
    var randomColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        colors = LabColorsManager.getDefaultColors(this)

        setContent {
            MaterialTheme {
                Color()
            }
        }

/*

        colors = LabColorsManager.getDefaultColors(this)

        binding.changeColorButton.setOnClickListener {
            // Get a random color
            randomColor = colors[Random().nextInt(colors.size)]

            // Set as target color to use in animation
            toColor = randomColor
            // Get current TextView color
            fromColor = binding.targetColorTextView.currentTextColor
            // Apply fade color transition to TextView
            LabAnimationsManager
                .getInstance()
                .applyFadeColorAnimationToView(
                    binding.targetColorTextView,
                    fromColor,
                    toColor,
                    LabAnimationsManager
                        .getInstance()
                        .shortAnimationDuration
                )

            // Get current button color
            fromColor = binding.changeColorButton.currentTextColor
            // Apply fade color transition to Button
            LabAnimationsManager
                .getInstance()
                .applyFadeColorAnimationToView(
                    binding.changeColorButton,
                    fromColor,
                    toColor,
                    LabAnimationsManager
                        .getInstance()
                        .shortAnimationDuration
                )

        }*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }
}

fun randomColor(colors: IntArray, colorState: MutableState<Int>) {
    Timber.d("randomColor()")
    // Get a random color
    val randomColor = colors[Random().nextInt(colors.size)]
    Timber.d("randomColor() - $randomColor")
    colorState.value = randomColor
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode",
    showSystemUi = false
)
@Composable
fun Color() {

    val colorActivity = LocalContext.current/*.findActivity()*/
    // val resources = LocalContext.current.resources

    val colors = LabColorsManager.getDefaultColors(colorActivity)
    var colorState = remember { mutableStateOf(R.color.white) }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val color = R.color.black
    /*if (isPressed) colors[Random().nextInt(colors.size)] else MaterialTheme.colorScheme.primary*/

    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier.background(colorResource(id = color)),
            title = { Text(text = stringResource(id = R.string.activity_title_colors)) },
            navigationIcon = {
                IconButton(onClick = { (colorActivity as ColorActivity).onBackPressed() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            })
    }) { contentPadding ->
        // Screen content
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .background(color = colorResource(id = R.color.default_dark)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(id = R.string.colors_text_placeholder),
                    modifier = Modifier.fillMaxWidth(),
                    colorResource(id = colorState.value),
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = { },
                    modifier = Modifier
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = color)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        stringResource(id = R.string.btn_change_color).uppercase(),
                        color = colorResource(id = R.color.white)
                    )
                }
            }
        }
    }
}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}