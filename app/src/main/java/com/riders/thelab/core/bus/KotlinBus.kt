package com.riders.thelab.core.bus

import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance

class KotlinBus private constructor() {
    private val _events = MutableSharedFlow<Any>()
    val events = _events.asSharedFlow()

    suspend fun publish(event: Any) {
        _events.emit(event)
    }

    /**
     * We first of all found all the methods from the child class,
     * then we identified those method who have Listen annotation to them.
     *
     * After that we just invoked those functions using Kotlin refection.
     * Thus we automatically subscribed to the events which are expected to be received by this class.
     */
    suspend inline fun <reified T> subscribe(crossinline onEvent: (T) -> Unit) {
        events.filterIsInstance<T>()
            .collectLatest { event ->
                coroutineContext.ensureActive()
                onEvent(event)
            }
    }

    companion object {
        private var mInstance: KotlinBus? = null

        fun getInstance(): KotlinBus {
            if (null == mInstance) {
                mInstance = KotlinBus()
            }

            return mInstance as KotlinBus
        }
    }
}