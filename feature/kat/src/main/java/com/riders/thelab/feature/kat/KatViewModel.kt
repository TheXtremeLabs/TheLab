package com.riders.thelab.feature.kat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.riders.thelab.core.common.utils.LabDeviceManager
import com.riders.thelab.core.data.local.model.User
import com.riders.thelab.core.data.remote.dto.kat.KatUser
import com.riders.thelab.core.data.remote.dto.kat.KatUserAuth
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.utils.UIManager
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

class KatViewModel : BaseViewModel() {

    /////////////////////////
    // Variables
    /////////////////////////
    private var auth: FirebaseAuth? = null
    private var mKatUser: KatUser? = null

    // TODO: DEBUG only
    private lateinit var mKatUserAuth: KatUserAuth
    // private var mKatUserAuth: KatUserAuth? = null


    /////////////////////////
    // Composable States
    /////////////////////////
    var modelName: String by mutableStateOf("")
        private set
    var userEmail: String by mutableStateOf("")
        private set

    private fun updateModelName(newModelName: String) {
        this.modelName = newModelName
    }

    private fun updateUserEmail(newEmail: String) {
        this.userEmail = newEmail
    }

    /////////////////////////
    // Coroutines
    /////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)
        }


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    init {
        Timber.d("KatViewModel | init method")

        pairUserByDevice()

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance().apply {
            if (BuildConfig.DEBUG) {
                this.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
    }

    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    private fun pairUserByDevice() {
        Timber.d("pairUserByDevice()")
        val modelName = LabDeviceManager.getModel()
        Timber.d("model name: $modelName")

        updateModelName(
            if (modelName.contains(LabDeviceManager.MODEL_NAME_GALAXY_NOTE_8, true)) {
                "${LabDeviceManager.getBrand()} Galaxy Note 8"
            } else if (modelName.contains(LabDeviceManager.MODEL_NAME_GALAXY_NOTE_4, true)) {
                "${LabDeviceManager.getBrand()} Galaxy Note 4"
            } else if (modelName.contains("TC", true)) {
                "${LabDeviceManager.getBrand()} $modelName"
            } else {
                modelName
            }
        )

        mKatUserAuth = when (modelName) {
            "TC56" -> {
                KatUserAuth("jane.doe@test.com", "test1234")
            }

            "TC57" -> {
                KatUserAuth("john.smith@test.com", "test1234")
            }

            "TC58" -> {
                KatUserAuth("mike@test.fr", "test1234")
            }

            else -> {
                if (modelName.contains(LabDeviceManager.MODEL_NAME_GALAXY_NOTE_8, true)) {
                    KatUserAuth("michael.sainthonore@gmail.com", "test1234")
                } else {
                    KatUserAuth("test@test.fr", "test1234")
                }
            }
        }

        mKatUserAuth?.let { updateUserEmail(it.email) }
    }

    fun checkIfUserSignIn(context: KatActivity) {
        Timber.d("checkIfUserSignIn()")

        // Check if user is signed in (non-null) and update UI accordingly.
        auth?.let { firebaseAuth ->

            val currentUser: FirebaseUser? = firebaseAuth.currentUser
            if (null == currentUser) {
                val mockRandomUser: User = User.mockUserForTests.random()

                FirebaseUtils.signInWithEmailAndPassword(
                    firebaseAuth,
                    mKatUserAuth.email,
                    mKatUserAuth.password
                    // "test1234"
                )
                    .addOnFailureListener { throwable ->
                        Timber.e("signInWithEmailAndPassword | addOnFailureListener | message: ${throwable.message}")
                    }
                    .addOnCompleteListener(context) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Timber.d("signInWithEmail:success")
                            val user = firebaseAuth.currentUser
                            Timber.d("user: $user")

                            setUsernameToFirestoreDatabase(context)
                        } else {
                            // If sign in fails, display a message to the user.
                            Timber.e("signInWithEmail:failure", task.exception)
                            UIManager.showToast(context, "Authentication failed.")
                        }
                    }

            } else {
                Timber.d("User is authenticated")

                if (BuildConfig.DEBUG) {
                    getUsernameFromFirestoreDatabase(context)
                } else {
                    // Do nothing
                }
            }
        } ?: run { Timber.e("Firebase Auth object is null") }
    }


    fun setUsernameToFirestoreDatabase(context: KatActivity) {
        Timber.d("setUsernameToFirestoreDatabase()")
        if (null == mKatUser) {
            if (null != mKatUserAuth) {
                val newKatUser = KatUser(
                    phone = "06145809",
                    username = mKatUserAuth!!.email,
                    createdTimestamp = Timestamp.now()
                )

                mKatUser = newKatUser
            }
        } else {
            Timber.d("Already logged in")
        }

        mKatUser?.let { katUser ->
            FirebaseUtils.currentUserDetails()?.let { documentReference: DocumentReference ->
                documentReference
                    .set(katUser)
                    .addOnFailureListener { throwable ->
                        Timber.e("currentUserDetails.set() | addOnFailureListener | message: ${throwable.message}")
                    }
                    .addOnCompleteListener(context) { task ->
                        if (task.isSuccessful) {
                            Timber.d("Successfully logged in Firebase Firestore")

                            getUsernameFromFirestoreDatabase(context)
                        }
                    }
            } ?: run { Timber.e("Document reference object is null") }
        } ?: run { Timber.e("mKatUser object is null") }


    }

    fun getUsernameFromFirestoreDatabase(context: KatActivity) {
        Timber.d("getUsernameFromFirestoreDatabase()")

        FirebaseUtils.currentUserDetails()?.let { documentReference: DocumentReference ->
            documentReference
                .get()
                .addOnFailureListener { throwable ->
                    Timber.e("currentUserDetails.get() | addOnFailureListener | message: ${throwable.message}")
                }
                .addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        Timber.e(
                            "currentUserDetails.get() | addOnCompleteListener | task.isSuccessful: ${
                                task.result.toObject(
                                    String::class.java
                                )
                            }"
                        )

                        val katUser = task.result.toObject(KatUser::class.java)

                        katUser?.let {
                            mKatUser = katUser
                        } ?: run { Timber.e("Error kat user model is null") }
                    }
                }
        } ?: run { Timber.e("Document reference object is null") }
    }
}