package com.riders.thelab.feature.kat.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.riders.thelab.core.common.utils.LabDeviceManager
import com.riders.thelab.core.common.utils.isValidPhone
import com.riders.thelab.core.data.local.model.kat.KatUserAuthModel
import com.riders.thelab.core.data.local.model.kat.KatUserModel
import com.riders.thelab.core.data.local.model.kat.toDto
import com.riders.thelab.core.data.remote.dto.kat.FCDKatUser
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.feature.kat.BuildConfig
import com.riders.thelab.feature.kat.utils.FirebaseUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class KatMainViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : BaseViewModel() {

    /////////////////////////
    // Variables
    /////////////////////////
    // TODO: DEBUG only
    private lateinit var mKatUserAuth: KatUserAuthModel
    private var mFirebaseCloudDatastoreUser: FCDKatUser? = null
    private var deviceFCMToken: String? = null

    fun updateToken(newToken: String) {
        this.deviceFCMToken = newToken
    }

    // private var mKatUserAuth: KatUserAuth? = null


    /////////////////////////
    // Composable States
    /////////////////////////
    var modelName: String by mutableStateOf("")
        private set
    var userEmail: String by mutableStateOf("")
        private set

    var chatRooms: List<KatUserModel> by mutableStateOf(emptyList())
        private set

    private fun updateModelName(newModelName: String) {
        this.modelName = newModelName
    }

    private fun updateUserEmail(newEmail: String) {
        this.userEmail = newEmail
    }

    private fun updateChatRooms(newChatRooms: List<KatUserModel>) {
        this.chatRooms = newChatRooms
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
        FirebaseUtils.initAuth()
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

        mKatUserAuth = KatUserAuthModel(
            email = when (modelName) {
                "TC56" -> {
                    "jane.doe@test.com"
                }

                "TC57" -> {
                    "john.smith@test.com"
                }

                "TC58" -> {
                    "mike@test.fr"
                }

                else -> {
                    if (modelName.contains(LabDeviceManager.MODEL_NAME_GALAXY_NOTE_8, true)) {
                        "michael.sainthonore@gmail.com"
                    } else {
                        "test@test.fr"
                    }
                }
            },
            password = "test1234"
        )

        mKatUserAuth?.let { updateUserEmail(it.email) }
    }

    fun checkIfUserSignIn(context: Activity) {
        Timber.d("checkIfUserSignIn()")

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUtils.auth?.let { firebaseAuth ->

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

        (context.findActivity() as KatMainActivity).notifyCurrentUsername(userEmail)
    }


    private fun setUsernameToFirestoreDatabase(context: Activity) {
        Timber.d("setUsernameToFirestoreDatabase()")
        if (null == mFirebaseCloudDatastoreUser) {
            if (null != mKatUserAuth) {
                val newKatUser = KatUserModel(
                    userId = FirebaseUtils.getCurrentUserID(),
                    phone = "0614589309".isValidPhone(),
                    username = mKatUserAuth!!.email,
                    createdTimestamp = Timestamp.now(),
                    fcmToken = deviceFCMToken!!
                )

                mFirebaseCloudDatastoreUser = newKatUser.toDto()
            }
        } else {
            Timber.d("Already logged in")
        }

        mFirebaseCloudDatastoreUser?.let { fcdUser ->
            FirebaseUtils.setUser(
                context = context,
                katUser = fcdUser,
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