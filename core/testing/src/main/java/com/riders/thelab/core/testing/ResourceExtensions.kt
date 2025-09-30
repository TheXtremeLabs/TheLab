package com.riders.thelab.core.testing

import kotlin.reflect.KClass
import java.io.*

fun <T:Any> KClass<T>.getResourceFileAsInputStream(filename: String) : InputStream? = runCatching {
    (this as Any).javaClass.classLoader?.getResourceAsStream(filename)
}
    .onFailure { exception -> println("=======> getResourceFileAsInputStream() | exception : $exception") }
    .getOrNull()

// fun getResourceAsStringData(filename:String):String? =getResourceFileAsInputStream(filename).readAllBytes