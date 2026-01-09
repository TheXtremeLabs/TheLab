package com.riders.thelab.call.core.utils

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.riders.thelab.call.core.service.LabCallService
import timber.log.Timber

object LabCallManager {

    private lateinit var telecomManager: TelecomManager
    private lateinit var phoneAccountHandle: PhoneAccountHandle

    fun registerPhoneAccount(context: Context) {
        telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val componentName = ComponentName(context, LabCallService::class.java)
        phoneAccountHandle = PhoneAccountHandle(componentName, "Caller")

        val phoneAccount = PhoneAccount.builder(phoneAccountHandle, "Caller")
            .setCapabilities(PhoneAccount.CAPABILITY_CALL_PROVIDER)
            .build()
        telecomManager.registerPhoneAccount(phoneAccount)
    }

    @RequiresPermission(Manifest.permission.CALL_PHONE)
    fun startCall(context: Context, number: String) {
        val uri = Uri.fromParts("tel", number, null)
        if (!hasCallPhonePermission(context)) {
            Timber.e("startCall() | Permission not granted")
            return
        }

        val extras = Bundle().apply {
            putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle)
        }
        if (hasCallPhonePermission(context)) {
            Timber.d("startCall() | Starting call to $number")
            telecomManager.placeCall(uri, extras)
        }
    }

    fun hasCallPhonePermission(context: Context): Boolean =
        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CALL_PHONE
        )
}
