package com.riders.thelab.core.nfc

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import timber.log.Timber

class LabNFCManager private constructor(
    private val activity: ComponentActivity,
    private val nfcReaderCallback: NfcAdapter.ReaderCallback? = null
) {
    var nfcAdapter: NfcAdapter? = null
        private set

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

    fun onTagDiscovered(detectedTag: Tag) {
        Timber.d("readFromTag()")

        try {
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

            val messages = processIntentMessages(intentMessages)

            if (messages.isNullOrEmpty()) {
                Timber.e("onTagDiscovered() | Text is empty. NFC messages may be null")
                ndef.close()
            } else {
                Timber.d("onTagDiscovered() | NFC Messages: $messages")
                ndef.close()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            Timber.e("onTagDiscovered() | Cannot Read From Tag.")

            if (exception is IllegalArgumentException) {
                if (exception.message?.contains("is not a valid Bluetooth address") == true) {
                    Timber.e(exception.message)
                }
            }
        }
    }

    private
    fun processIntentMessages(intentMessages: Array<out Parcelable>?): String? {
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