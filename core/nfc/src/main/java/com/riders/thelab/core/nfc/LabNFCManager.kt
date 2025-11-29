package com.riders.thelab.core.nfc

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Stable
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber


class LabNFCManager private constructor(
    private val activity: ComponentActivity,
    private val nfcReaderCallback: NfcAdapter.ReaderCallback? = null
) {
    val nfcManager: NfcManager? = activity.getSystemService(Context.NFC_SERVICE) as NfcManager?
    var nfcAdapter: NfcAdapter? = null
        private set

    @Stable
    val nfcState: MutableStateFlow<NFCUiState> = MutableStateFlow(NFCUiState.Idle)


    init {
        if (null == nfcManager) {
            //So the service is not supported by the device
            Timber.d("init | No NFC Manager working")
        } else {
            Timber.e("init | NFC Manager working")
        }
    }

    fun getAdapter(): NfcAdapter? = if (null == nfcAdapter) {
        Timber.w("nfc adapter value is null. Set the value...")
        nfcManager?.defaultAdapter?.also { nfcAdapter = it }
    } else {
        nfcAdapter
    }

    /**
     * Checks if the device has NFC hardware.
     *
     * @return `true` if the device has NFC hardware, `false` otherwise.
     */
    fun isNfcSupported(): Boolean = activity.packageManager
        .hasSystemFeature(PackageManager.FEATURE_NFC)
        .also { supported: Boolean? ->
            Timber.d("isNfcSupported() | is NFC supported : $supported")
            if (false == supported) {
                nfcState.update { NFCUiState.NotSupported }
            }
        }

    /**
     * Checks if NFC is currently enabled in the device settings.
     * This will return `false` if the device does not support NFC.
     *
     * @return `true` if NFC is supported and enabled, `false` otherwise.
     */
    fun isNfcEnabled(): Boolean = run {
        if (!isNfcSupported()) false else true == getAdapter()?.isEnabled
    }.also { enabled ->
        Timber.d("isNfcEnabled() | is NFC enabled : $enabled")
        nfcState.update { if (enabled) NFCUiState.Enabled else NFCUiState.Disabled }
    }

    /**
     * Creates an Intent to open the system's NFC settings screen.
     * This allows the user to enable or disable NFC.
     */
    fun createNfcSettingsIntent(): Intent {
        return Intent(Settings.ACTION_NFC_SETTINGS)
    }

    fun enableNfcForegroundDispatch() {
        Timber.d("enableNfcForegroundDispatch()")
        try {
            val intent = Intent(activity, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val nfcPendingIntent = PendingIntent.getActivity(
                activity,
                0,
                intent,
                if (LabCompatibilityManager.isS()) PendingIntent.FLAG_IMMUTABLE else 0
            )

            val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
            val ndefDetected = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
            val techDetected = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
            val nfcIntentFilter = arrayOf(techDetected, tagDetected, ndefDetected)

            val options = Bundle().apply {
                // Work around for some broken Nfc firmware implementations that poll the card too fast
                putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)
            }


            // Enable ReaderMode for all types of card and disable platform sounds
            nfcAdapter?.enableReaderMode(
                activity,
                nfcReaderCallback,
                NfcAdapter.FLAG_READER_NFC_A or
                        NfcAdapter.FLAG_READER_NFC_B or
                        NfcAdapter.FLAG_READER_NFC_F or
                        NfcAdapter.FLAG_READER_NFC_V or
                        NfcAdapter.FLAG_READER_NFC_BARCODE or
                        NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
                options
            )

            // nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, nfcIntentFilter, null)
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
            Timber.e("Error enabling NFC foreground dispatch , with message: ${ex.message}")
        }
    }

    fun disableNfcForegroundDispatch() {
        Timber.e("disableNfcForegroundDispatch()")
        try {
            nfcAdapter?.disableReaderMode(activity)
            //mNfcAdapter?.disableForegroundDispatch(this)
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
            Timber.e("disableNfcForegroundDispatch() | Error disabling NFC foreground dispatch")
        }
    }

    fun onTagDiscovered(detectedTag: Tag): Result<String> {
        Timber.d("onTagDiscovered()")

        return try {
            val ndef = Ndef.get(detectedTag)
            ndef.connect()
            Timber.d("type: ${ndef.type}")
            Timber.d("max size: ${ndef.maxSize}")
            Timber.d(if (ndef.isWritable) "ndef.isWritable: True" else "ndef.isWritable: False")

            val intentMessages = if (!LabCompatibilityManager.isTiramisu()) {
                @Suppress("DEPRECATION")
                activity.intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            } else {
                @SuppressLint("NewApi")
                activity.intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES,
                    NdefMessage::class.java
                )
            }

            val messages: String? = processIntentMessages(intentMessages)

            if (messages.isNullOrEmpty()) {
                val message = "Text is empty. NFC messages may be null"
                Timber.e("onTagDiscovered() | $message")
                ndef.close()
                Result.success(message)
            } else {
                Timber.d("onTagDiscovered() | NFC Messages: $messages")
                ndef.close()
                Result.success(messages)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            Timber.e("onTagDiscovered() | Cannot Read From Tag.")

            if (exception is IllegalArgumentException) {
                if (exception.message?.contains("is not a valid Bluetooth address") == true) {
                    Timber.e(exception.message)
                }
                return Result.failure(exception)
            }

            Result.failure(exception)
        }
    }

    private fun processIntentMessages(intentMessages: Array<out Parcelable>?): String? {
        Timber.d("processNdefMessages() | intentMessages: ${intentMessages.contentToString()}")

        if (null == intentMessages) {
            return null
        }

        val ndefMessages: Array<NdefMessage?> = arrayOfNulls(intentMessages.size)
        for (i in intentMessages.indices) {
            ndefMessages[i] = intentMessages[i] as NdefMessage
        }

        // NDEF Record Structure
        // he most important byte (7th) is the Message Begin byte, this byte is 1 if the it is the starting message otherwise is zero.
        // The 6th byte is the Message End, this byte is 1 if the this record is the end record otherwise is 0.
        // SR is the Short Record and it is 1 if it is a short record.
        // This information are important if we want to handle the NDEF tag data correctly.
        val record = ndefMessages[0]!!.records[0]
        val payload: ByteArray = record.payload

        val text = String(payload, Charsets.UTF_8)
        return text
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: LabNFCManager? = null

        @Synchronized
        fun getInstance(activity: ComponentActivity): LabNFCManager =
            instance ?: synchronized(this) {
                instance ?: LabNFCManager(activity).also { instance = it }
            }

        @Synchronized
        fun getInstance(
            activity: ComponentActivity,
            nfcReaderCallback: NfcAdapter.ReaderCallback
        ): LabNFCManager = instance ?: synchronized(this) {
            instance ?: LabNFCManager(activity, nfcReaderCallback).also { instance = it }
        }
    }
}