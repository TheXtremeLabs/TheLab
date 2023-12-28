package com.riders.thelab.feature.kat.utils

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.messaging.FirebaseMessaging
import com.riders.thelab.core.data.local.model.kat.KatModel
import com.riders.thelab.core.data.local.model.kat.KatUserModel
import com.riders.thelab.core.data.remote.dto.kat.FCDKatUser
import com.riders.thelab.core.data.remote.dto.kat.KatChatRoom
import com.riders.thelab.core.data.remote.dto.kat.toModel
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.feature.kat.BuildConfig
import kotools.types.text.NotBlankString
import timber.log.Timber

object FirebaseUtils {

    // Collections
    private const val COLLECTION_USERS: String = "users"
    private const val COLLECTION_CHAT_ROOMS: String = "chatrooms"
    private const val COLLECTION_CHATS: String = "chats"

    var auth: FirebaseAuth? = null
        private set

    // Initialize Firebase Auth
    fun initAuth() {
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance().apply {
            if (BuildConfig.DEBUG) {
                this.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
            }
        }
    }


    /**
     * Returns the collection reference from given name
     *
     * @param collectionName name of the desired collection
     *
     * @return [CollectionReference] object for the given parameters
     */
    private fun getCollectionReference(collectionName: String): CollectionReference =
        FirebaseFirestore.getInstance().collection(collectionName)


    /**
     * Returns the document reference for the given document path
     *
     * @param collectionName root collection
     * @param documentPath root document path
     *
     * @return [DocumentReference] object for the given parameters
     */
    private fun getDocumentReference(
        collectionName: String,
        documentPath: String? = null
    ): DocumentReference = if (null == documentPath) {
        getCollectionReference(collectionName).document()
    } else {
        getCollectionReference(collectionName).document(documentPath)
    }

    /**
     * Returns the sub collection reference from root collection reference for the given root document path
     *
     * @param rootCollectionName root collection
     * @param rootDocumentPath root document path
     * @param subCollectionName sub collection targeted
     *
     * @return [CollectionReference] object for the given parameters
     */
    private fun getSubCollectionReference(
        rootCollectionName: String,
        rootDocumentPath: String,
        subCollectionName: String
    ): CollectionReference =
        getDocumentReference(rootCollectionName, rootDocumentPath).collection(subCollectionName)


    ///////////////////////////////////////////////////
    // Users
    ///////////////////////////////////////////////////
    // Create
    fun createUserWithEmailAndPassword(
        context: Activity,
        auth: FirebaseAuth,
        email: String,
        password: String,
        onFailure: (throwable: Throwable) -> Unit,
        onSuccess: (task: Task<AuthResult>) -> Unit
    ) {
        Timber.d("createUserWithEmailAndPassword() | email: $email, password: $password")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnFailureListener { throwable ->
                Timber.e("createUserWithEmailAndPassword | addOnFailureListener | message: ${throwable.message}")
                onFailure(throwable)
            }
            .addOnCompleteListener(context) { task ->
                Timber.e("createUserWithEmailAndPassword | addOnCompleteListener")
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("createUserWithEmail:success")
                    val user = auth.currentUser
                    Timber.d("user: $user")
                    onSuccess(task)
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.e("createUserWithEmail:failure", task.exception)
                    UIManager.showToast(context, "Authentication failed.")
                }
            }
    }


    // Sign-In
    fun signInWithEmailAndPassword(
        context: Activity,
        auth: FirebaseAuth,
        email: String,
        password: String,
        onFailure: (throwable: Throwable) -> Unit,
        onSuccess: (user: FirebaseUser) -> Unit
    ) {
        Timber.d("signInWithEmailAndPassword() | email: $email, password: $password")

        auth
            .signInWithEmailAndPassword(email, password)
            .addOnFailureListener { throwable ->
                Timber.e("signInWithEmailAndPassword | addOnFailureListener | message: ${throwable.message}")
                onFailure(throwable)
            }
            .addOnCompleteListener(context) { task ->
                Timber.e("signInWithEmailAndPassword | addOnCompleteListener")
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInWithEmail:success")
                    val user: FirebaseUser? = auth.currentUser

                    if (null != user) {
                        Timber.d("user: $user")
                        onSuccess(user)
                    } else {
                        Timber.e("user is null")
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.e("signInWithEmail:failure", task.exception)
                    UIManager.showToast(context, "Authentication failed.")
                }
            }
    }

    fun isLoggedIn(): Boolean =
        null != getCurrentUserID() && (null != getCurrentUserID() && getCurrentUserID()?.isNotBlank() == true)

    fun logOut() = auth?.signOut()

    // Token
    fun getFcmToken(
        context: Activity,
        onFailure: (throwable: Throwable) -> Unit,
        onSuccess: (newToken: String) -> Unit
    ) {
        Timber.d("getFcmToken()")

        FirebaseMessaging.getInstance()
            .token
            .addOnFailureListener(context) { throwable ->
                Timber.e("FirebaseMessaging.getToken() | addOnFailureListener | message: ${throwable.message}")
                onFailure(throwable)
            }
            .addOnCompleteListener(context) { task ->
                Timber.d("FirebaseMessaging.getToken() | addOnCompleteListener")
                if (task.isSuccessful) {
                    val token = task.result
                    Timber.d("token: $token")

                    currentUserDetails()?.let {
                        it.update("fcmToken", token)
                        onSuccess(token)
                    }
                        ?: run { Timber.e("getFcmToken | currentUserDetails Document reference object is null") }
                }
            }
    }

    // Current user
    fun getCurrentUserID(): String? = auth?.run {
        // Timber.d("getCurrentUserID() with uid: $uid")
        uid
    }

    fun currentUserDetails(): DocumentReference? = getCurrentUserID()?.run {
        getCollectionReference(COLLECTION_USERS).document(this)
    }

    fun currentUserReference(): DocumentReference? = getCurrentUserID()?.run {
        getDocumentReference(COLLECTION_USERS, this)
    }

    // Set User
    fun setUser(
        context: Activity,
        katUser: FCDKatUser,
        onFailure: (throwable: Throwable) -> Unit,
        onSuccess: (loggedIn: Boolean) -> Unit
    ) {
        Timber.d("setUser() | katUser: $katUser")

        currentUserDetails()?.let { documentReference: DocumentReference ->
            documentReference
                .set(katUser)
                .addOnFailureListener { throwable ->
                    Timber.e("currentUserDetails.set() | addOnFailureListener | message: ${throwable.message}")
                    onFailure(throwable)
                }
                .addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        Timber.d("Successfully logged in Firebase Firestore")

                        onSuccess(true)
                    }
                }
        } ?: run { Timber.e("setUsernameToFirestoreDatabase | Document reference object is null") }
    }


    // Get Users
    fun getUser(
        context: Activity,
        onFailure: (throwable: Throwable) -> Unit,
        onSuccess: (userModel: KatUserModel) -> Unit
    ) {
        currentUserDetails()?.let { documentReference: DocumentReference ->
            documentReference
                .get()
                .addOnFailureListener { throwable ->
                    Timber.e("getUser | addOnFailureListener | message: ${throwable.message}")
                    onFailure(throwable)
                }
                .addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        val fcdUserModel: FCDKatUser? = task.result.toObject(FCDKatUser::class.java)

                        if (null == fcdUserModel) {
                            onFailure(Throwable("User model is null"))
                        } else {
                            onSuccess(fcdUserModel.toModel())
                        }
                    }
                }
        } ?: run {
            val errorMessage = "Unable to fetch current user data"
            Timber.e("getUser | Document reference object is null")
            onFailure(Throwable(errorMessage))
            UIManager.showToast(context, errorMessage)
        }
    }

    fun getUserById(
        context: Activity,
        fcmKatUserId: String,
        onFailure: (throwable: Throwable) -> Unit,
        onSuccess: (userModel: KatUserModel) -> Unit
    ) {
        getCollectionReference(COLLECTION_USERS)
            .document(fcmKatUserId)
            .get()
            .addOnFailureListener(context) { throwable ->
                Timber.e("currentUserDetails.get() | addOnFailureListener | message: ${throwable.message}")
                onFailure(throwable)
            }
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    val fcdUserModel: FCDKatUser? = task.result.toObject(FCDKatUser::class.java)

                    if (null == fcdUserModel) {
                        onFailure(Throwable("User model is null"))
                    } else {
                        onSuccess(fcdUserModel.toModel())
                    }
                }
            }
    }

    fun getAuthenticatedUserByID(
        context: Activity,
        onFailure: (throwable: Throwable) -> Unit,
        onSuccess: (user: KatUserModel?) -> Unit
    ) {
        Timber.d("getUserByUID()")
        currentUserDetails()?.let { documentReference: DocumentReference ->
            documentReference
                .get()
                .addOnFailureListener { throwable ->
                    Timber.e("currentUserDetails.get() | addOnFailureListener | message: ${throwable.message}")
                    onFailure(throwable)
                }
                .addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        Timber.e("currentUserDetails.get() | addOnCompleteListener | task.isSuccessful: ${task.result}")

                        val katUser = runCatching {
                            task.result.toObject(FCDKatUser::class.java)
                        }
                            .onFailure {
                                Timber.e("runCatching - onFailure() | Error caught: ${it.message}")
                            }
                            .onSuccess {
                                Timber.d("runCatching - onSuccess()")
                            }
                            .getOrNull()

                        katUser?.let {
                            Timber.d("user: $it")
                            onSuccess(it.toModel())
                        } ?: run {
                            Timber.e("getUsernameFromFirestoreDatabase | Error kat user model is null")
                            onSuccess(null)
                        }
                    }
                }
        }
            ?: run { Timber.e("getUsernameFromFirestoreDatabase | Document reference object is null") }
    }

    fun getUsersWithCallbacks(
        onFailure: (throwable: Throwable) -> Unit,
        onSuccess: (users: List<KatUserModel>) -> Unit
    ) {
        Timber.d("getUsers()")
        getCollectionReference(COLLECTION_USERS)
            .addSnapshotListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (null != error) {
                    Timber.e("Error caught: ${error.message}")
                    onFailure(error)
                    return@addSnapshotListener
                }

                if (null == value) {
                    onFailure(Throwable("Value is null"))
                } else {
                    Timber.d("value: ${value.toString()}")
                    val users: List<FCDKatUser> = value.toObjects(FCDKatUser::class.java)
                    Timber.d("users: ${users.toString()}")
                    onSuccess(users.map { it.toModel() })
                }
            }
    }

    fun getChatRoom(
        context: Activity,
        chatroomId: String,
        otherUserId: String,
        onFailure: (throwable: Throwable) -> Unit,
        onSuccess: (chats: KatChatRoom) -> Unit
    ) {
        Timber.d("getChatRoom()")
        getCollectionReference(COLLECTION_CHAT_ROOMS)
            .document(chatroomId)
            .get()
            .addOnFailureListener { throwable ->
                Timber.e("getChatRoom() | addOnFailureListener | message: ${throwable.message}")
                onFailure(throwable)
            }
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    Timber.e("getChatRoom() | addOnCompleteListener | task.isSuccessful: ${task.result}")
                    var chatRoomModel: KatChatRoom? =
                        task.result.toObject(KatChatRoom::class.java)

                    if (null == chatRoomModel) {
                        // First time chatting
                        chatRoomModel = KatChatRoom(
                            chatroomId,
                            listOf(getCurrentUserID()!!, otherUserId),
                            Timestamp.now(),
                            ""
                        )

                        FirebaseFirestore.getInstance()
                            .collection(COLLECTION_CHAT_ROOMS)
                            .document(chatroomId)
                            .set(chatRoomModel)

                        onSuccess(chatRoomModel)
                    }

                    // Return chat room
                    onSuccess(chatRoomModel)
                }
            }
    }

    // Method user to return a unique chatroom id
    fun getChatRoomId(userId1: String, userId2: String): String =
        if (userId1.hashCode() < userId2.hashCode()) {
            userId1 + "_" + userId2
        } else {
            userId2 + "_" + userId1
        }

    ///////////////////////////////////////////////////
    // Messages
    ///////////////////////////////////////////////////
    fun getMessages(
        context: Activity,
        chatRoomId: String,
        onFailure: (throwable: Throwable) -> Unit,
        onSuccess: (chatMessageList: List<KatModel>) -> Unit
    ) {
        Timber.d("getMessages() | chatroomId: $chatRoomId")

        // Get chat room id reference
        getDocumentReference(COLLECTION_CHAT_ROOMS, chatRoomId)
            // Get chat chat messages collection reference
            .collection(COLLECTION_CHATS)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener(context) { value: QuerySnapshot?, error: FirebaseFirestoreException? ->

                if (null != error) {
                    Timber.e("Error caught: ${error.message}")
                    onFailure(error)
                    return@addSnapshotListener
                }

                if (null == value) {
                    onFailure(Throwable("Value is null"))
                } else {
                    Timber.d("value: ${value.toString()}")
                    val messages: List<KatModel> = value.toObjects(KatModel::class.java)
                    Timber.d("users: ${messages.toString()}")
                    onSuccess(messages)
                }
            }
    }

    fun sendMessageToUser(
        context: Activity,
        message: NotBlankString,
        chatroomId: String,
        chatRoomModel: KatChatRoom,
        onFailure: (throwable: Throwable) -> Unit,
        onSuccess: (sent: Boolean) -> Unit
    ) {
        Timber.d("sendMessageToUser() | message: %s", message)

        val chatRoomUpdated: KatChatRoom = chatRoomModel.copy(
            lastSenderId = getCurrentUserID()!!,
            lastMessageTimestamp = Timestamp.now()
        )

        getCollectionReference(COLLECTION_CHAT_ROOMS)
            .document(chatroomId)
            .set(chatRoomUpdated)

        val chatModel = KatModel(
            message = message.toString(),
            senderId = getCurrentUserID()!!,
            timestamp = Timestamp.now()
        )

        // Get chat messages collection from chat room collection reference for given chat room id
        val chatMessagesCollectionReference =
            getSubCollectionReference(
                rootCollectionName = COLLECTION_CHAT_ROOMS,
                rootDocumentPath = chatroomId,
                subCollectionName = COLLECTION_CHATS
            )

        chatMessagesCollectionReference
            .add(chatModel)
            .addOnFailureListener(context) { throwable ->
                Timber.e("getChatRoom() | addOnFailureListener | message: ${throwable.message}")
                onFailure(throwable)
            }
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    onSuccess(true)
                }
            }
    }
}


