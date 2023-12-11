package com.riders.thelab.feature.kat.ui

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.data.remote.dto.kat.KatChatRoomModel
import com.riders.thelab.core.data.remote.dto.kat.KatModel
import com.riders.thelab.core.data.remote.dto.kat.KatUser
import com.riders.thelab.feature.kat.utils.FirebaseUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
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

    var extraOtherUserId: String by mutableStateOf("")
        private set
    var extraUsername: String by mutableStateOf("")
        private set

    var chatMessages: List<KatModel> by mutableStateOf(emptyList())
        private set

    fun updateKatOtherUserId(extraUserId: String) {
        this.extraOtherUserId = extraUserId
    }

    fun updateKatUsername(extraUsername: String) {
        this.extraUsername = extraUsername
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


    //////////////////////////////////////////
    //
    // CLASS METHODS
    //
    //////////////////////////////////////////
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

                getMessages(context = context, chatRoomId = chatRoomId)
            }
        )
    }

    fun getMessages(context: Activity, chatRoomId: String) {
        Timber.d("getMessages() | mChatRoomId: $mChatRoomId")
        FirebaseUtils.getMessages(
            context = context,
            chatRoomId = chatRoomId,
            onFailure = { Timber.e("runCatching | onFailure() | Error caught: $it") },
            onSuccess = { messages ->
                updateChatMessages(messages)
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
                    if (it) {
                        sendNotification(message)
                    }
                }
            )
        }
            .onFailure { it.printStackTrace() }
    }

    private fun sendNotification(message: NotBlankString) {
        Timber.d("sendNotification() ")

        FirebaseUtils.currentUserDetails()?.get()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userModel: KatUser? = task.result.toObject(KatUser::class.java)

                if (null != userModel) {
                    Timber.d("user: $userModel")

                    runCatching {
                        val jsonObject: JSONObject = JSONObject()
                        val notificationObject: JSONObject = JSONObject().apply {
                            put("title", userModel.username)
                            put("body", message)
                        }

                        val dataObject: JSONObject = JSONObject().apply {
                            put("userId", userModel.userId)
                        }

                        jsonObject.apply {
                            put("notification", notificationObject)
                            put("data", dataObject)
                            put("to", dataObject)
                        }

                        callApi(jsonObject = jsonObject)
                    }
                        .onFailure { it.printStackTrace() }
                        .onSuccess { Timber.d("Notification sent successfully") }

                }
            }
        }
    }


    private fun callApi(jsonObject: JSONObject) {
        Timber.d("callApi() ")
        val jsonMediaType: MediaType = "application/json".toMediaType()
        val client = OkHttpClient
            .Builder()
            .addInterceptor(
                HttpLoggingInterceptor { message: String ->
                    Timber.tag("OkHttp").i(message)
                }
                    .setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
//        val url = "https://fcm.googleapis.com/fcm/send/"
        val serverToken: String =
            "AAAAsPCoRsc:APA91bEXatUhfSdxp9iuIgsR6qCn-W3ZMtnqekms6Y65jWdhS3KuN60N-38kbQJxQ5PtJeYVEFlsfuszSJJX0nqUEQE2CL-_am7GXU76uX8YF6pcY6_M7CaHNvDF-lAx1jwsSlci_AEI"
        val url = " https://fcm.googleapis.com/v1/projects/the-lab-3920e/messages:send/"

        val body: RequestBody =
            jsonObject.toString().toRequestBody(contentType = jsonMediaType)
        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .header(
                "Authorization",
                "Bearer 759951804103-a49rs2ee6v6603o39u0pu06egtofcgh1.apps.googleusercontent.com"
            )
            /*.header(
                "project_id",
                "759951804103"
            )*/
            .build()

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            val response: String =
                client.newCall(request).execute().use { response -> response.body!!.string() }
            Timber.d("response: $response")
        }
    }
}