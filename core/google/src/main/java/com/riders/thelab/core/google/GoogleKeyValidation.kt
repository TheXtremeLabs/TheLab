package com.riders.thelab.core.google

import android.content.Context
import android.widget.Toast
import timber.log.Timber

abstract class GoogleKeyValidation {


    /**
     * Validates that there is a reasonable server client ID in strings.xml, this is only needed
     * to make sure users of this sample follow the README.
     */
    fun isServerClientIDValid(context: Context): Boolean {
        Timber.d("validateServerClientID()")

        val serverClientId = context.getString(com.riders.thelab.core.ui.R.string.server_client_id)
        val suffix = ".apps.googleusercontent.com"

        return if (!serverClientId.trim { it <= ' ' }.endsWith(suffix)) {
            val message =
                "Invalid server client ID in strings.xml, must end with $suffix"
            Timber.e("validateServerClientID() | $message")
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            false
        } else {
            true
        }
    }
}