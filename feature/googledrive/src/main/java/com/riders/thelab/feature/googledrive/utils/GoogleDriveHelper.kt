package com.riders.thelab.feature.googledrive.utils

/*import com.google.api.services.drive.Drive*/

/*import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;*/
//import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
/*import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;*/
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveClient
import com.google.android.gms.drive.DriveResourceClient
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.services.drive.DriveScopes
import com.riders.thelab.feature.googledrive.core.google.GoogleKeyValidation
import timber.log.Timber
import java.lang.ref.WeakReference


class GoogleDriveHelper<T : Activity>(activity: T) : GoogleKeyValidation() {

    private var mWeakRefActivity: WeakReference<T>? = null

    private var mGoogleApiClient: GoogleApiClient? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mLastGoogleAccount: GoogleSignInAccount? = null
    private var mLastGoogleAccountCredential: GoogleAccountCredential? = null

    /**
     * Handles high-level drive functions like sync
     */
    private var mDriveClient: DriveClient? = null

    /**
     * Handle access to Drive resources/files.
     */
    private var mDriveResourceClient: DriveResourceClient? = null

    init {
        Timber.i("init method")
        mWeakRefActivity = WeakReference<T>(activity)
    }


    private fun isUserSignedIn(): Boolean {
        Timber.i("isUserSignedIn()")

        return mWeakRefActivity?.get()?.let { activity ->

            /*return if(null == mLastGoogleAccountCredential) {
                false
            } else {
                true
            }*/

            val account = GoogleSignIn.getLastSignedInAccount(activity)
            account != null
        } ?: run {
            Timber.e("isUserSignedIn() - activity is null")
            return false
        }
    }

    fun googleSignIn() {
        Timber.i("googleSignIn()")

        if (!isUserSignedIn()) {
            Timber.e("NOT isUserSignedIn()")

            mWeakRefActivity?.get()?.let { activity ->

                val googleSignInOptions = GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(activity.getString(com.riders.thelab.core.ui.R.string.server_client_id))
                    .requestEmail()
                    .requestProfile()
                    .requestScopes(Scope(DriveScopes.DRIVE))
                    .requestScopes(Scope(DriveScopes.DRIVE_FILE))
                    .requestScopes(Scope(DriveScopes.DRIVE_PHOTOS_READONLY))
                    .requestScopes(Scope(DriveScopes.DRIVE_METADATA_READONLY))
                    .build()

                mGoogleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions)

                // From the GoogleSignInClient object we can get the signInIntent
                // which we can use in startActivityForResult to trigger the login flow
                val signInIntent = mGoogleSignInClient?.signInIntent

                if (null != signInIntent) {
                    activity.startActivityForResult(signInIntent, 1234567)
                }
            }
        }
    }

    fun googleSignIn(googleSignInRequestLauncher: ActivityResultLauncher<Intent>) {
        Timber.i("googleSignIn()")

        if (!isUserSignedIn()) {
            Timber.e("NOT isUserSignedIn()")

            mWeakRefActivity?.get()?.let { activity ->

                val googleSignInOptions = GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(activity.getString(com.riders.thelab.core.ui.R.string.server_client_id))
                    .requestEmail()
                    .requestProfile()
                    .requestScopes(Scope(DriveScopes.DRIVE))
                    .requestScopes(Scope(DriveScopes.DRIVE_FILE))
                    .requestScopes(Scope(DriveScopes.DRIVE_PHOTOS_READONLY))
                    .requestScopes(Scope(DriveScopes.DRIVE_METADATA_READONLY))
                    .build()

                mGoogleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions)

                // From the GoogleSignInClient object we can get the signInIntent
                // which we can use in startActivityForResult to trigger the login flow
                val signInIntent = mGoogleSignInClient?.signInIntent

                if (null != signInIntent) {
//                    activity.startActivityForResult(signInIntent, 1234567)
                    googleSignInRequestLauncher.launch(signInIntent)
                }
            }
        }
    }

    private fun signOut() {
        Timber.e("signOut()")

        mWeakRefActivity?.get()?.let { activity ->
            mGoogleSignInClient
                ?.signOut()
                ?.addOnCompleteListener(activity) {
                    Toast.makeText(activity, "Signed Out", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(
                Scope(DriveScopes.DRIVE),
                Scope(DriveScopes.DRIVE_FILE),
                Scope(DriveScopes.DRIVE_PHOTOS_READONLY),
                Scope(DriveScopes.DRIVE_METADATA_READONLY)
            )
            .build()

        return GoogleSignIn.getClient(context, signInOptions)/*.also {
            Timber.d("getGoogleSignInClient() | account: ${it.toString()}")
        }*/
    }

    /**
     * Continues the sign-in process, initializing the Drive clients with the current
     * user's account.
     */
    fun initializeDriveClient(signInAccount: GoogleSignInAccount) {
        mWeakRefActivity?.get()?.let { activity ->
            mDriveClient = Drive.getDriveClient(activity, signInAccount)
            mDriveResourceClient =
                Drive.getDriveResourceClient(activity, signInAccount)
        }
    }

    fun checkLastSignedInAccount(context: Context): Drive? {
        Timber.i("checkLastSignedInAccount()")

        GoogleSignIn.getLastSignedInAccount(context)?.let { googleAccount ->
            Timber.d("connected account - mail : ${googleAccount.email}")

            // Now we need to create a GoogleAccountCredential to authenticate the signed-in account
            // with DRIVE_FILE permission to access the user google drive contents.
            val credential =
                GoogleAccountCredential.usingOAuth2(
                    context,
                    listOf(
                        DriveScopes.DRIVE,
                        DriveScopes.DRIVE_FILE,
                        DriveScopes.DRIVE_METADATA_READONLY
                    )
                )

            credential.selectedAccount = googleAccount.account!!
            mLastGoogleAccountCredential?.selectedAccount = googleAccount.account!!

            // we need to create the Drive service instance via its builder
            // where need to pass AndroidHttp, JacksonFactory,
            // and GoogleAccountCredential as parameters
            /*return Drive
                .Builder(
                    NetHttpTransport.Builder().build(),
                    JacksonFactory.getDefaultInstance(),
                    credential
                )
                .setApplicationName(getString(R.string.app_name))
                .build()*/

        }

        return null
    }

    fun setGoogleAccount(account: GoogleSignInAccount) {
        Timber.i("setGoogleAccount() | account: ${account.toString()}")
        this.mLastGoogleAccount = account
    }
}