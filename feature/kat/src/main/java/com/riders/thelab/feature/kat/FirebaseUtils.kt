package com.riders.thelab.feature.kat

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.riders.thelab.core.data.remote.dto.kat.KatUser
import com.riders.thelab.core.ui.utils.UIManager
import timber.log.Timber

object FirebaseUtils {

    // Collections
    private const val COLLECTION_USERS: String = "users"
    private const val COLLECTION_CHATS: String = "chats"

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
        auth: FirebaseAuth,
        email: String,
        password: String
    ): Task<AuthResult> {
        Timber.d("signInWithEmailAndPassword() | email: $email, password: $password")

        return auth.signInWithEmailAndPassword(email, password)
    }

    fun signInWithEmailAndPasswordWithCallbacks(
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

    fun isLoggedIn(): Boolean = null != getCurrentUserID()


    // Current user
    fun getCurrentUserID(): String? = FirebaseAuth.getInstance().uid

    fun currentUserDetails(): DocumentReference? {
        Timber.d("currentUserDetails()")

        return if (null == getCurrentUserID()) {
            Timber.e("getCurrentUserID is null")
            null
        } else {
            Timber.d("getCurrentUserID not null. userID: ${getCurrentUserID()}")
            FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                .document(getCurrentUserID()!!)
        }
    }

    // Set User
    fun setUser(
        context: Activity,
        katUser: KatUser,
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
    fun getUserByUID(
        context: Activity,
        onFailure: (throwable: Throwable) -> Unit,
        onSuccess: (user: KatUser?) -> Unit
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
                            task.result.toObject(KatUser::class.java)
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
                            onSuccess(it)
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
        onSuccess: (users: List<KatUser>) -> Unit
    ) {
        FirebaseFirestore.getInstance()
            .collection(COLLECTION_USERS)
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
                    val users: List<KatUser> = value.toObjects(KatUser::class.java)
                    Timber.d("users: ${users.toString()}")
                    onSuccess(users)
                }
            }
    }

}
