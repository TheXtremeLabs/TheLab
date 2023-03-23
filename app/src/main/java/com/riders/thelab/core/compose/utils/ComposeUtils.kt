package com.riders.thelab.core.compose.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.riders.thelab.data.local.model.Message

fun Context.findActivity(): Activity? = when (this) {
    is AppCompatActivity -> this
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

object ComposeUtils {
    private const val senderPierre = "Pi'erre"
    private const val senderCarti = "Carti"
    private const val senderNudy = "Nudy"
    private const val senderUzi = "Uzi"

    val conversationSample = listOf(
        Message(senderNudy, "Ayoo! Yo Pi'erre you wanna come out here ?", false),
        Message(senderPierre, "Heeeeinnn", true),
        Message(senderUzi, "Wooow it's Lil Uzi Vert", false),
        Message(senderCarti, "Pu n bout that shi", false),
        Message(senderPierre, "What on my what ?", true),
        Message(senderCarti, "See that shi we eat at the police", false),
        Message(senderPierre, "What?", true),
        Message(senderNudy, "Who?", false),
        Message(senderUzi, "And I'm not from Earth, I'm from outta space?", false),
        Message(senderPierre, "Wow?", true),
        Message(senderUzi, "That's different", false),
        Message(
            senderUzi,
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            false
        )
    )
}


@Composable
fun isKeyboardVisible(): Boolean = WindowInsets.ime.getBottom(LocalDensity.current) > 0


/*
use example :
val isKeyboardOpen by keyboardAsState() // true or false

 */
@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}

@Composable
fun keyboardAsStateView(): State<Boolean> {
    val keyboardState = remember { mutableStateOf(false) }
    val view = LocalView.current
    LaunchedEffect(view) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            keyboardState.value = insets.isVisible(WindowInsetsCompat.Type.ime())
            insets
        }
    }
    return keyboardState
}