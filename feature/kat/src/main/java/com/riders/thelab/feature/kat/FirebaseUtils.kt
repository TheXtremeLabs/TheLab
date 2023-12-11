package com.riders.thelab.feature.kat

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.riders.thelab.core.ui.utils.UIManager
import timber.log.Timber

object FirebaseUtils {

    // Collections
    const val COLLECTION_USERS: String = "users"
    const val COLLECTION_CHATS: String = "chats"


    private fun currentUserID(): String? = FirebaseAuth.getInstance().uid

    fun isLoggedIn(): Boolean = null != currentUserID()

    fun currentUserDetails(): DocumentReference? {
        Timber.d("currentUserDetails()")

        return if (null == currentUserID()) {
            Timber.e("currentUserID is null")
            null
        } else {
            Timber.d("currentUserID not null. userID: ${currentUserID()}")
            FirebaseFirestore.getInstance().collection(COLLECTION_USERS).document(currentUserID()!!)
        }
    }


    fun createUserWithEmailAndPassword(
        context: Activity,
        auth: FirebaseAuth,
        email: String,
        password: String
    ) {
        Timber.d("createUserWithEmailAndPassword() | email: $email, password: $password")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnFailureListener { throwable ->
                Timber.e("createUserWithEmailAndPassword | addOnFailureListener | message: ${throwable.message}")
            }
            .addOnCompleteListener(context) { task ->
                Timber.e("createUserWithEmailAndPassword | addOnCompleteListener")
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("createUserWithEmail:success")
                    val user = auth.currentUser
                    Timber.d("user: $user")
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.e("createUserWithEmail:failure", task.exception)
                    UIManager.showToast(context, "Authentication failed.")
                }
            }
    }
    /*fun createUserWithEmailAndPassword(auth:FirebaseAuth, email:String,password:String): Task<AuthResult> {
        Timber.d("createUserWithEmailAndPassword()")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnFailureListener { throwable ->
                Timber.e("createUserWithEmailAndPassword | addOnFailureListener | message: ${throwable.message}")
            }
            .addOnCompleteListener(this) { task ->
                Timber.e("createUserWithEmailAndPassword | addOnCompleteListener")
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
    }*/


    fun signInWithEmailAndPassword(
        auth: FirebaseAuth,
        email: String,
        password: String
    ): Task<AuthResult> {
        Timber.d("signInWithEmailAndPassword() | email: $email, password: $password")

        return auth.signInWithEmailAndPassword(email, password)
    }

}