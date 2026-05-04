package com.riders.thelab.core.ui.compose.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity
import com.riders.thelab.core.ui.compose.base.BaseAppCompatActivity
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

fun Context.findActivity(): Activity? = when (this) {
    is BaseAppCompatActivity -> this
    is BaseComponentActivity -> this
    is AppCompatActivity -> this
    is FragmentActivity -> this
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@JvmName("reifiedFindActivityNullable")
inline fun <reified A : Activity> Context.findActivity(): A? = when (this) {
    is BaseAppCompatActivity -> this
    is BaseComponentActivity -> this
    is AppCompatActivity -> this
    is FragmentActivity -> this
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
} as A?

// OnBackPressedDispatcher
fun Context.executeOnBackPressed() {
    Timber.d("Context.executeOnBackPressed()")

    runCatching {
        Timber.d("Context.executeOnBackPressed() | Attempt to execute backPressed on ComponentActivity()")
        (this.findActivity() as BaseComponentActivity).backPressed()
    }
        .onFailure { baseComponentException ->
            baseComponentException.printStackTrace()
            Timber.e("Context.executeOnBackPressed() | onFailure | error caught with message: ${baseComponentException.message} (class: ${baseComponentException.javaClass.canonicalName})")

            this.fallbackExecuteOnBackPressed()
        }
}

// Fallback onBackPressed
fun Context.fallbackExecuteOnBackPressed() {
    Timber.d("Context.fallbackExecuteOnBackPressed()")

    runCatching {
        Timber.d("Context.fallbackExecuteOnBackPressed | Attempt to execute fallback backPressed on AppCompatActivity()")
        (this.findActivity() as BaseAppCompatActivity).backPressed()
    }
        .onFailure { baseAppCompatException ->
            baseAppCompatException.printStackTrace()
            Timber.e("Context.fallbackExecuteOnBackPressed | onFailure | error caught with message: ${baseAppCompatException.message} (class: ${baseAppCompatException.javaClass.canonicalName})")

            this.deprecatedExecuteOnBackPressed()
        }
}

// Deprecated onBackPressed
fun Context.deprecatedExecuteOnBackPressed() {
    Timber.d("Context.deprecatedExecuteOnBackPressed()")

    runCatching {
        Timber.d("Context.deprecatedExecuteOnBackPressed | Attempt to execute backPressed on ComponentActivity()")
        @Suppress("DEPRECATION")
        (this.findActivity() as Activity).onBackPressed()
    }
        .onFailure { deprecatedComponentException ->
            deprecatedComponentException.printStackTrace()
            Timber.e("Context.deprecatedExecuteOnBackPressed | onFailure | error caught with message: ${deprecatedComponentException.message} (class: ${deprecatedComponentException.javaClass.canonicalName})")
        }
}

@Composable
@ReadOnlyComposable
private fun resourcesAsComposable(): Resources = LocalResources.current

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

@OptIn(ExperimentalMaterial3Api::class)
fun showTooltip(scope: CoroutineScope, tooltipState: TooltipState) = scope.launch {
    if (!tooltipState.isVisible) {
        tooltipState.show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun hideTooltip(scope: CoroutineScope, tooltipState: TooltipState) = scope.launch {
    if (tooltipState.isVisible) {
        tooltipState.dismiss()
    }
}


/**
 * Animate current color to target color
 *
 * @param targetValue represents the target color
 *
 */
@Composable
fun animateColor(targetValue: Color) = animateColorAsState(
    targetValue = targetValue,
    animationSpec = tween(durationMillis = 2000),
    label = "color_animation"
).value