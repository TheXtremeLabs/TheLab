package com.riders.thelab.ui.login

import okhttp3.internal.and
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * This class provides functions to manipulate login input, especially, login's password operations
 */
class LoginUtils private constructor() {
    companion object {

        /**
         * Converts password entered using SHA-1 algorithm to byte-array value
         */
        fun convertToSHA1(password: String): ByteArray? {
            try {
                // Convert to SHA-1
                val digest: MessageDigest = MessageDigest.getInstance("SHA-1")
                val textByteArray: ByteArray =
                    "${password}_the_lab_data".toByteArray(charset("iso-8859-1"))

                digest.update(textByteArray, 0, textByteArray.size)

                return digest.digest()

            } catch (exception: NoSuchAlgorithmException) {
                exception.printStackTrace()
            } catch (ex: UnsupportedEncodingException) {
                ex.printStackTrace()
            }

            return null
        }

        /**
         * Returns encoded sha-1 hashed password to string value
         */
        fun encodedHashedPassword(sha1hash: ByteArray): String? {
            try {
                val sb = StringBuilder()
                for (b in sha1hash) {
                    var halfByte: Int = b.toInt() ushr 4 and 0x0F
                    var twoHalfs: Int = 0

                    do {
                        sb.append(
                            if (halfByte in 0..9) ('0'.code + halfByte).toChar() else ('0'.code + (halfByte + 10)).toChar()
                        )
                        halfByte = (b and 0x0F)
                    } while (twoHalfs++ < 1)
                }
                return sb.toString()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return null
        }
    }
}