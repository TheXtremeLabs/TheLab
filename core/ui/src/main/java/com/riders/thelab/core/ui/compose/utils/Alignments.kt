package com.riders.thelab.core.ui.compose.utils

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment

val Start: Alignment.Horizontal = BiasAlignment.Horizontal(-1f)
val CenterHorizontally: Alignment.Horizontal = BiasAlignment.Horizontal(0f)
val End: Alignment.Horizontal = BiasAlignment.Horizontal(1f)

val Top: Alignment.Vertical = BiasAlignment.Vertical(-1f)
val CenterVertically: Alignment.Vertical = BiasAlignment.Vertical(0f)
val Bottom: Alignment.Vertical = BiasAlignment.Vertical(1f)

@SuppressLint("UnrememberedMutableState")
@Composable
fun animateHorizontalAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment.Horizontal> {
    val bias by animateFloatAsState(targetBiasValue, label = "horizontal animation")
    return derivedStateOf { BiasAlignment.Horizontal(bias) }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun animateVerticalAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment.Vertical> {
    val bias by animateFloatAsState(targetBiasValue, label = "vertical animation")
    return derivedStateOf { BiasAlignment.Vertical(bias) }
}