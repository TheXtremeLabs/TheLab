package com.riders.thelab.ui.googlesignin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.riders.thelab.R
import com.riders.thelab.databinding.ActivityGoogleSignInBinding
import timber.log.Timber

class GoogleSignInActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val RC_GET_TOKEN = 9002
    }

    private lateinit var viewBinding: ActivityGoogleSignInBinding


    private var mGoogleSignInClient: GoogleSignInClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityGoogleSignInBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Views
        viewBinding.buttonOptionalAction.setText(R.string.refresh_token)

        // Button click listeners
        viewBinding.signInButton.setOnClickListener(this)
        viewBinding.signOutButton.setOnClickListener(this)
        viewBinding.disconnectButton.setOnClickListener(this)
        viewBinding.buttonOptionalAction.setOnClickListener(this)

        // For sample only: make sure there is a valid server client ID.
        validateServerClientID()

        // [START configure_signin]
        // Request only the user's ID token, which can be used to identify the
        // user securely to your backend. This will contain the user's basic
        // profile (name, profile picture URL, etc) so you should not need to
        // make an additional call to personalize your application.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        // [END configure_signin]

        // Build GoogleAPIClient with the Google Sign-In API and the above options.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun getIdToken() {
        // Show an account picker to let the user choose a Google account from the device.
        // If the GoogleSignInOptions only asks for IDToken and/or profile and/or email then no
        // consent screen will be shown here.
        val signInIntent: Intent = mGoogleSignInClient?.signInIntent!!
        startActivityForResult(signInIntent, RC_GET_TOKEN)
    }

    private fun refreshIdToken() {
        // Attempt to silently refresh the GoogleSignInAccount. If the GoogleSignInAccount
        // already has a valid token this method may complete immediately.
        //
        // If the user has not previously signed in on this device or the sign-in has expired,
        // this asynchronous branch will attempt to sign in the user silently and get a valid
        // ID token. Cross-device single sign on will occur in this branch.
        mGoogleSignInClient?.silentSignIn()
            ?.addOnCompleteListener(this) { completedTask: Task<GoogleSignInAccount> ->
                handleSignInResult(completedTask)
            }
    }

    // [START handle_sign_in_result]
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken = account.idToken
            Timber.d("Token fetched : $idToken")

            // TODO(developer): send ID Token to server and validate
            updateUI(account)
        } catch (e: ApiException) {
            Timber.e("handleSignInResult: %s", e.message)
            updateUI(null)
        }
    }
    // [END handle_sign_in_result]


    private fun signOut() {
        mGoogleSignInClient?.signOut()?.addOnCompleteListener(
            this
        ) { updateUI(null) }
    }

    private fun revokeAccess() {
        mGoogleSignInClient?.revokeAccess()?.addOnCompleteListener(
            this
        ) { updateUI(null) }
    }

    @Deprecated("DEPRECATED - Use registerActivityForResult")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GET_TOKEN) {
            // [START get_id_token]
            // This task is always completed immediately, there is no need to attach an
            // asynchronous listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
            // [END get_id_token]
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            viewBinding.status.setText(R.string.signed_in)
            val idToken = account.idToken
            viewBinding.detail.text = getString(R.string.id_token_fmt, idToken)
            viewBinding.signInButton.visibility = View.GONE
            viewBinding.signOutAndDisconnect.visibility = View.VISIBLE
            viewBinding.detail.visibility = View.VISIBLE
        } else {
            viewBinding.status.setText(R.string.signed_out)
            viewBinding.detail.text = getString(R.string.id_token_fmt, "null")
            viewBinding.signInButton.visibility = View.VISIBLE
            viewBinding.signOutAndDisconnect.visibility = View.GONE
            viewBinding.detail.visibility = View.GONE
        }
    }

    /**
     * Validates that there is a reasonable server client ID in strings.xml, this is only needed
     * to make sure users of this sample follow the README.
     */
    private fun validateServerClientID() {
        val serverClientId = getString(R.string.server_client_id)
        val suffix = ".apps.googleusercontent.com"
        if (!serverClientId.trim { it <= ' ' }.endsWith(suffix)) {
            val message =
                "Invalid server client ID in strings.xml, must end with $suffix"
            Timber.d(message)
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.sign_in_button -> getIdToken()
            R.id.sign_out_button -> signOut()
            R.id.disconnect_button -> revokeAccess()
            R.id.button_optional_action -> refreshIdToken()
            else -> {
            }
        }
    }
}