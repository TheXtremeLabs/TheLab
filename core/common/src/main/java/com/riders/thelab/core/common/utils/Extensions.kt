package com.riders.thelab.core.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.InputStream
import java.nio.charset.Charset
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/////////////////////////////////////////////////////
// Flows
/////////////////////////////////////////////////////
inline fun <reified T> instantCombine(vararg flows: Flow<T>) = channelFlow {
    val array = Array(flows.size) {
        false to (null as T?) // first element stands for "present"
    }

    flows.forEachIndexed { index, flow ->
        launch {
            flow.collect { emittedElement ->
                array[index] = true to emittedElement
                send(array.filter { it.first }.map { it.second })
            }
        }
    }
}

/////////////////////////////////////////////////////
// Location
/////////////////////////////////////////////////////
fun Pair<Double, Double>.toLocation(): Location = Location("").apply {
    latitude = first
    longitude = second
}


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


/////////////////////////////////////////////////////
// String encryption
/////////////////////////////////////////////////////
private const val ALGORITHM: String = "AES"
private const val TRANSFORMATION: String = "AES/CBC/PKCS5Padding"

private val SECRET_KEY_SPEC: ByteArray = "12345678901234561234567890123456".toByteArray()
private val initializationVector: IvParameterSpec = IvParameterSpec(ByteArray(16))

@OptIn(ExperimentalEncodingApi::class)
fun String.decrypt(): String {
    val cipher = Cipher.getInstance(TRANSFORMATION)
    val secretKeySpec = SecretKeySpec(SECRET_KEY_SPEC, ALGORITHM)
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, initializationVector)
    val plainText = cipher.doFinal(Base64.decode(this))
    return String(plainText)
}

@OptIn(ExperimentalEncodingApi::class)
fun String.encrypt(): String {
    val cipher = Cipher.getInstance(TRANSFORMATION)
    val secretKeySpec = SecretKeySpec(SECRET_KEY_SPEC, ALGORITHM)
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, initializationVector)
    val cipherText = cipher.doFinal(this.toByteArray())
    return Base64.encode(cipherText)
}

fun InputStream.asStringData(charset: Charset = Charsets.UTF_8): String =
    this.bufferedReader(charset).use { reader ->
        reader.readText()
    }.also { Timber.d("InputStream.asStringData() | content length : ${it.length}") }



/////////////////////////////////////////////////////
// Image
/////////////////////////////////////////////////////
fun Int.toBitmap(context: Context): Bitmap? {
    require(0 != this) { "Drawable must be greater than 0" }
    return ContextCompat.getDrawable(context, this)?.toBitmap()
}

/////////////////////////////////////////////////////
// Worker
/////////////////////////////////////////////////////

