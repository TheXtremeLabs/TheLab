package com.riders.thelab.core.testing.utils

import java.io.InputStream

fun <T : Any> T.getResourceFileAsInputStream(filename: String): InputStream? =
    runCatching {
        this.javaClass.classLoader?.getResourceAsStream(filename)
    }
        .onFailure { exception -> println("=======> getResourceFileAsInputStream() | exception : $exception") }
        .getOrNull()

fun <T : Any> T.getResourceAsStringData(filename: String): String? =
    getResourceFileAsInputStream(filename)
        ?.bufferedReader()
        ?.use { it.readText() }