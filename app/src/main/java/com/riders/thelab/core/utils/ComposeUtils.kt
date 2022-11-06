package com.riders.thelab.core.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
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
        Message(senderNudy, "Ayoo! Yo Pi'erre you wanna come out here ?"),
        Message(senderPierre, "Heeeeinnn"),
        Message(senderUzi, "Wooow it's Lil Uzi Vert"),
        Message(senderCarti, "Pu n bout that shi"),
        Message(senderPierre, "What on my what ?"),
        Message(senderCarti, "See that shi we eat at the police"),
        Message(senderPierre, "What?"),
        Message(senderNudy, "Who?"),
        Message(senderUzi, "And I'm not from Earth, I'm from outta space?"),
        Message(senderPierre, "Wow?"),
        Message(senderUzi, "That's different"),
        Message(
            senderUzi,
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        )
    )
}