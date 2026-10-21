package com.riders.thelab.core.common.extensions

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

fun defaultDispatchers(): CoroutineDispatcher = Dispatchers.Default
fun ioDispatchers(): CoroutineDispatcher = Dispatchers.IO
fun mainDispatchers(): CoroutineDispatcher = Dispatchers.Main

val defaultCoroutineScope = CoroutineScope(defaultDispatchers())
val ioCoroutineScope = CoroutineScope(ioDispatchers())
val mainCoroutineScope = CoroutineScope(mainDispatchers())
