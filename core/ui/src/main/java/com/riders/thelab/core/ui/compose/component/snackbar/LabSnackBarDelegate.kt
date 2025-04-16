package com.riders.thelab.core.ui.compose.component.snackbar

import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import com.riders.thelab.core.ui.compose.color.error
import com.riders.thelab.core.ui.compose.color.success
import com.riders.thelab.core.ui.compose.color.warning
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty

class LabSnackBarDelegate(
    var snackbarHostState: SnackbarHostState,
    var coroutineScope: CoroutineScope
) {
    private var snackbarState: SnackBarState = SnackBarState.DEFAULT

    val snackbarBackgroundColor: Color
        @Composable
        get() = when (snackbarState) {
            SnackBarState.DEFAULT -> SnackbarDefaults.color
            SnackBarState.ERROR -> error
            SnackBarState.WARNING -> warning
            SnackBarState.SUCCESS -> success
        }

    fun showSnackbar(
        state: SnackBarState,
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        this.snackbarState = state
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = duration
            )
        }
    }

    private var snackbarDelegate: LabSnackBarDelegate? = null

    operator fun getValue(nothing: Nothing?, property: KProperty<*>): LabSnackBarDelegate {
        if (snackbarDelegate == null) {
            snackbarDelegate = LabSnackBarDelegate(snackbarHostState, coroutineScope)
        }
        return snackbarDelegate!!
    }
}

@Composable
fun rememberInstance(): LabSnackBarDelegate {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    return LabSnackBarDelegate(snackbarHostState, coroutineScope)
}