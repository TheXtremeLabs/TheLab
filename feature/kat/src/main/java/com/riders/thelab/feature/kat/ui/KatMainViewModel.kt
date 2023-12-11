package com.riders.thelab.feature.kat.ui

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.riders.thelab.core.common.utils.LabDeviceManager
import com.riders.thelab.core.common.utils.isValidPhone
import com.riders.thelab.core.data.remote.dto.kat.KatUser
import com.riders.thelab.core.data.remote.dto.kat.KatUserAuth
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.feature.kat.BuildConfig
import com.riders.thelab.feature.kat.utils.FirebaseUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

class KatMainViewModel : BaseViewModel() {

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

    var chatRooms: List<KatUser> by mutableStateOf(emptyList())
        private set
    var extraUsername: String by mutableStateOf("")
        private set

    private fun updateModelName(newModelName: String) {
        this.modelName = newModelName
    }

    private fun updateUserEmail(newEmail: String) {
        this.userEmail = newEmail
    }

    private fun updateChatRooms(newChatRooms: List<KatUser>) {
        this.chatRooms = newChatRooms
    }

    fun updateKatUsername(extraUsername: String) {
        this.extraUsername = extraUsername
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
        Timber.d("KatMainViewModel | init method")

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

    fun checkIfUserSignIn(context: Activity) {
        Timber.d("checkIfUserSignIn()")

        // Check if user is signed in (non-null) and update UI accordingly.
        auth?.let { firebaseAuth ->

            val currentUser: FirebaseUser? = firebaseAuth.currentUser
            if (null == currentUser) {
                FirebaseUtils.signInWithEmailAndPasswordWithCallbacks(
                    context = context,
                    auth = firebaseAuth,
                    email = mKatUserAuth.email,
                    password = mKatUserAuth.password,
                    onFailure = { throwable ->
                        Timber.e("signInWithEmailAndPassword | addOnFailureListener | message: ${throwable.message}")
                    },
                    onSuccess = { user: FirebaseUser ->
                        Timber.d("user: $user")
                        setUsernameToFirestoreDatabase(context)
                    }
                )
            } else {
                Timber.d("User is authenticated")

                if (BuildConfig.DEBUG) {
                    getUsernameFromFirestoreDatabase(context)
                } else {
                    // Do nothing
                }
            }
        } ?: run { Timber.e("checkIfUserSignIn | Firebase Auth object is null") }
    }


    private fun setUsernameToFirestoreDatabase(context: Activity) {
        Timber.d("setUsernameToFirestoreDatabase()")
        if (null == mKatUser) {
            if (null != mKatUserAuth) {
                val newKatUser = KatUser(
                    userId = FirebaseUtils.getCurrentUserID(),
                    phone = "0614589309".isValidPhone(),
                    username = mKatUserAuth!!.email,
                    createdTimestamp = Timestamp.now()
                )

                mKatUser = newKatUser
            }
        } else {
            Timber.d("Already logged in")
        }

        mKatUser?.let { katUser ->
            FirebaseUtils.setUser(
                context = context,
                katUser = katUser,
                onFailure = { throwable ->
                    Timber.e("currentUserDetails.set() | onFailure | message: ${throwable.message}")

                },
                onSuccess = {
                    getUsernameFromFirestoreDatabase(context)
                }
            )
        } ?: run { Timber.e("setUsernameToFirestoreDatabase | mKatUser object is null") }
    }

    private fun getUsernameFromFirestoreDatabase(context: Activity) {
        Timber.d("getUsernameFromFirestoreDatabase()")

        FirebaseUtils.getUserByUID(
            context = context,
            onFailure = { throwable ->
                Timber.e("currentUserDetails.get() | onFailure | message: ${throwable.message}")
            },
            onSuccess = { user ->
                if (null == user) {
                    setUsernameToFirestoreDatabase(context)
                } else {
                    getAllUsers(context)
                }
            }
        )
    }

    private fun getAllUsers(context: Activity) {
        Timber.d("getAllUsers()")

        FirebaseUtils.getUsersWithCallbacks(
            onFailure = { throwable ->
                Timber.e("getUsers | onFailure | message: ${throwable.message}")
                UIManager.showToast(context, "Unable to get all users")
            },
            onSuccess = {
                updateChatRooms(it)
            }
        )
    }
}