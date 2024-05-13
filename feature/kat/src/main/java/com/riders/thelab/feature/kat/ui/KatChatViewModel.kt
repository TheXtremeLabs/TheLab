package com.riders.thelab.feature.kat.ui

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.riders.thelab.core.data.local.model.kat.KatModel
import com.riders.thelab.core.data.local.model.kat.KatUserModel
import com.riders.thelab.core.data.remote.dto.kat.KatChatRoom
import com.riders.thelab.core.data.remote.dto.kat.NotificationData
import com.riders.thelab.core.data.remote.dto.kat.PushNotification
import com.riders.thelab.core.data.remote.rest.KatRestClient
import com.riders.thelab.feature.kat.utils.FirebaseUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import timber.log.Timber


@Suppress("EmptyMethod")
class KatChatViewModel : ViewModel() {

    /////////////////////////
    // variables
    /////////////////////////
    private var mChatRoom: KatChatRoom? = null
        private set

    // Local variables
    private var mExtraOtherUserId: String? = null
    private var mExtraUsername: String? = null
    private var mChatRoomId: String? = null
    private var mOtherKatUser: KatUserModel? = null
        private set


    fun setChatRoomId(chatRoomId: String) {
        this.mChatRoomId = chatRoomId
    }

    fun setOtherKatUser(otherKatUser: KatUserModel) {
        this.mOtherKatUser = otherKatUser
    }

    /////////////////////////
    // Composable States
    /////////////////////////
    var currentUserDocument: DocumentReference? by mutableStateOf(null)
        private set
    var message: String by mutableStateOf("")
        private set

    private var otherUserId: String? by mutableStateOf(null)
        private set
    var otherUsername: String by mutableStateOf("")
        private set

    var chatMessages: List<KatModel> by mutableStateOf(emptyList())
        private set

    fun updateCurrentUserDocument(currentUserDocument: DocumentReference) {
        this.currentUserDocument = currentUserDocument
    }

    fun updateKatOtherUserId(extraUserId: String) {
        this.otherUserId = extraUserId
    }

    fun updateKatOtherUsername(extraUsername: String) {
        this.otherUsername = extraUsername
    }

    fun updateMessageText(newText: String) {
        this.message = newText
    }

    fun updateChatMessages(newMessages: List<KatModel>) {
        this.chatMessages = newMessages
    }

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)
        }


    init {
        FirebaseUtils.currentUserDetails()?.let { updateCurrentUserDocument(it) }
    }

    //////////////////////////////////////////
    //
    // CLASS METHODS
    //
    //////////////////////////////////////////
    fun getBundle(intent: Intent) {
        Timber.d("getBundle()")

        intent.extras?.let { extras ->

            // EXTRA OTHER USER USERNAME
            extras.getString(KatChatActivity.EXTRA_USERNAME)?.let {
                this.mExtraUsername = it
                updateKatOtherUsername(it)
            }
                ?: run { Timber.e("EXTRA_USERNAME is null") }

            // EXTRA OTHER USER ID
            extras.getString(KatChatActivity.EXTRA_USER_ID)?.let {
                this.mExtraOtherUserId = it
                updateKatOtherUserId(it)
            }
                ?: run { Timber.e("EXTRA_USER_ID is null") }
        } ?: run { Timber.e("Intent extras is null") }
    }

    fun setChatRoomId() {
        Timber.d("setChatRoomId()")

        FirebaseUtils.getCurrentUserID()?.let { firebaseUserId ->
            otherUserId?.let { userId ->
                setChatRoomId(FirebaseUtils.getChatRoomId(firebaseUserId, userId))
            }
        }
    }

    fun findOtherUserById(context: KatChatActivity) {
        Timber.d("findOtherUserById()")

        otherUserId?.let { userId ->
            FirebaseUtils.getUserById(
                context = context,
                fcmKatUserId = userId,
                onFailure = { Timber.e("Failed to get user") },
                onSuccess = { otherUser ->
                    setOtherKatUser(otherUser)
                }
            )
        } ?: run { Timber.e("Other user id is null") }
    }

    fun getOrCreateChatRoomReference(context: Activity) {
        Timber.d("getOrCreateChatRoomReference() | chatRoomId: $mChatRoomId")

        mChatRoomId?.let { chatRoomId ->
            otherUserId?.let { userId ->

                FirebaseUtils.getChatRoom(
                    context = context,
                    chatroomId = chatRoomId,
                    otherUserId = userId,
                    onFailure = {
                        Timber.e("runCatching - onFailure() | Error caught: ${it.message}")
                    },
                    onSuccess = {
                        mChatRoom = it

                        getMessages(context = context, chatRoomId = chatRoomId)
                    }
                )
            } ?: run { Timber.e("Other User ID is null") }
        } ?: run { Timber.e("Chat room id is null") }

    }

    fun getMessages(context: Activity, chatRoomId: String) {
        Timber.d("getMessages() | for chat room id: $mChatRoomId")
        FirebaseUtils.getMessages(
            context = context,
            chatRoomId = chatRoomId,
            onFailure = { Timber.e("runCatching | onFailure() | Error caught: $it") },
            onSuccess = { messages ->
                updateChatMessages(messages)
            }
        )
    }

    @OptIn(ExperimentalKotoolsTypesApi::class)
    fun sendMessage(context: Activity, textInput: String) {
        Timber.d("sendMessage() | textInput: $textInput")

        runCatching {
            val message: NotBlankString = NotBlankString.create(textInput)

            FirebaseUtils.sendMessageToUser(
                context = context,
                message = message,
                chatroomId = mChatRoomId!!,
                chatRoomModel = mChatRoom!!,
                onFailure = { Timber.e("runCatching | onFailure() | Error caught: $it") },
                onSuccess = {
                    if (it) {
                        // clear Message TextField
                        updateMessageText("")

                        sendNotification(context, message)
                    }
                }
            )
        }
            .onFailure { it.printStackTrace() }
    }

    private fun sendNotification(context: Activity, message: NotBlankString) {
        Timber.d("sendNotification() ")

        mOtherKatUser?.let { user ->
            FirebaseUtils.getUser(
                context = context,
                onFailure = { Timber.e("sendNotification | onFailure() | Error caught: ${it.message}") },
                onSuccess = { userModel ->

                    val pushNotification = PushNotification(
                        data = NotificationData(
                            title = userModel.username,
                            message = message.toString()
                        ),
                        to = user.fcmToken
                    )

                    // Call REST Client and make call
                    viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
                        val callResponseBody =
                            KatRestClient()
                                .getApiService()
                                .sendNotification(pushNotification)

                        if (!callResponseBody.isSuccessful) {
                            Timber.e("Error call sendNotification Endpoint")
                        } else {
                            val response = callResponseBody.body().toString()
                            Timber.d("response: $response")
                        }
                    }
                }
            )
        } ?: run { Timber.e("Other Kat user not available") }
    }
}