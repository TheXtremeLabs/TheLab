package com.riders.thelab.core.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

fun defaultDispatchers() = Dispatchers.Default
fun ioDispatchers() = Dispatchers.IO
fun mainDispatchers() = Dispatchers.Main

val defaultCoroutineScope = CoroutineScope(defaultDispatchers())
val ioCoroutineScope = CoroutineScope(ioDispatchers())
val mainCoroutineScope = CoroutineScope(mainDispatchers())
