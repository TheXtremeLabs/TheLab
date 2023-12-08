package com.riders.thelab.core.common.utils

import java.security.MessageDigest


/////////////////////////////////////////////////////
// String encoding
/////////////////////////////////////////////////////
fun String.encodeToMd5(): String {
    return hashString(this, "MD5")
}

fun String.encodeToSha256(): String {
    return hashString(this, "SHA-256")
}

private fun hashString(input: String, algorithm: String): String {
    return MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
        .fold("") { str, it -> str + "%02x".format(it) }
}