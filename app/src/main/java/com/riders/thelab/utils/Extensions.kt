package com.riders.thelab.utils

import com.riders.thelab.core.data.local.model.compose.TimerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import timber.log.Timber
import kotlin.time.Duration


/**
 * The timer emits the total seconds immediately.
 * Each second after that, it will emit the next value.
 */
fun countDownFlow(totalSeconds: Int): Flow<TimerState> =
    (totalSeconds - 1 downTo 0).asFlow() // Emit total - 1 because the first was emitted onStart
        .onEach { delay(1000) } // Each second later emit a number
        .onStart { emit(totalSeconds) } // Emit total seconds immediately
        .conflate() // In case the creating of State takes some time, conflate keeps the time ticking separately
        .transform { remainingSeconds: Int ->
            Timber.d("transform | remainingSeconds: $remainingSeconds")
            emit(TimerState(remainingSeconds))
        }

fun tickerFlow(period: Duration, initialDelay: Duration = Duration.ZERO) = flow {
    delay(initialDelay)
    while (true) {
        emit(Unit)
        delay(period)
    }
}

fun String.isLetterOrDigits(): Boolean = this.matches("^[a-zA-Z0-9]*$".toRegex())
