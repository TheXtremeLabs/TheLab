package com.riders.thelab.feature.kat.ui

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.riders.thelab.core.data.remote.dto.kat.KatChatRoomModel
import com.riders.thelab.feature.kat.utils.FirebaseUtils
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import timber.log.Timber

class KatChatViewModel : ViewModel() {


    /////////////////////////
    // variables
    /////////////////////////
    private var mChatRoomId: String? = null
    private var mChatRoom: KatChatRoomModel? = null

    fun setChatRoomId(chatRoomId: String) {
        this.mChatRoomId = chatRoomId
    }


    /////////////////////////
    // Composable States
    /////////////////////////
    var message: String by mutableStateOf("")
        private set

    var extraUsername: String by mutableStateOf("")
        private set

    fun updateKatUsername(extraUsername: String) {
        this.extraUsername = extraUsername
    }

    fun updateMessageText(newText: String) {
        this.message = newText
    }

    fun getOrCreateChatRoomReference(context: Activity, chatRoomId: String, otherUserId: String) {
        Timber.d("getOrCreateChatRoomReference() | chatRoomId: $chatRoomId")

        FirebaseUtils.getChatRoom(
            context = context,
            chatroomId = chatRoomId,
            otherUserId = otherUserId,
            onFailure = {
                Timber.e("runCatching - onFailure() | Error caught: ${it.message}")
            },
            onSuccess = {
                mChatRoom = it
            }
        )
    }

    fun sendMessage(context: Activity, textInput: String) {
        Timber.d("sendMessage() | textInput: $textInput")

        runCatching {
            val message: NotBlankString = textInput.toNotBlankString().getOrThrow()
            FirebaseUtils.sendMessageToUser(
                context = context,
                message = message,
                chatroomId = mChatRoomId!!,
                chatRoomModel = mChatRoom!!,
                onFailure = { Timber.e("runCatching | onFailure() | Error caught: ${it}") },
                onSuccess = {

                }
            )
        }
            .onFailure { it.printStackTrace() }
    }
}