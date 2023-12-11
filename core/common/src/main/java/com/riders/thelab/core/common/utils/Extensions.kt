package com.riders.thelab.core.common.utils

import timber.log.Timber
import java.security.MessageDigest


/////////////////////////////////////////////////////
// String commons
/////////////////////////////////////////////////////
fun String.isValidPhone(): String {
    Timber.d("isValidPhone() | phone value: $this")

    val phoneNumberLength = 10
//    require(phoneNumberLength == this.length)
    return if (phoneNumberLength == this.length) {
        this
    } else {
        throw IllegalArgumentException("phone number length must equal 10")
    }
}

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