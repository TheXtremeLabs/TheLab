package com.riders.thelab.ui.signup

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import org.kotools.types.EmailAddress
import timber.log.Timber

@AndroidEntryPoint
class SignUpActivity : BaseComponentActivity() {

    private val mViewModel: SignUpViewModel by viewModels()

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            Timber.d("coroutine launch with name ${this.coroutineContext}")
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    val userState by mViewModel.userState.collectAsStateWithLifecycle()
                    val emailHasError by mViewModel.emailHasError.collectAsStateWithLifecycle()
                    val passwordsHasError by mViewModel.passwordsHasError.collectAsStateWithLifecycle()

                    TheLabTheme(mViewModel.isDarkMode) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            SignUpContent(
                                isDarkMode = mViewModel.isDarkMode,
                                currentDestination = mViewModel.currentDestination,
                                onUpdateCurrentNavDestination = mViewModel::updateCurrentNavDestination,
                                userUiState = userState,
                                emailHasError = emailHasError,
                                passwordsHasError = passwordsHasError,
                                firstname = mViewModel.firstname,
                                onUpdateFirstname = mViewModel::updateFirstname,
                                lastname = mViewModel.lastname,
                                onUpdateLastname = mViewModel::updateLastname,
                                username = mViewModel.username,
                                onUpdateUsername = mViewModel::updateUsername,
                                email = EmailAddress.fromString(mViewModel.email),
                                onUpdateEmail = { mViewModel::updateEmail },
                                password = mViewModel.password,
                                onUpdatePassword = mViewModel::updatePassword,
                                passwordConfirmation = mViewModel.passwordConfirmation,
                                onUpdatePasswordConfirmation = mViewModel::updatePasswordConfirmation,
                                shouldShowExitDialogConfirmation = mViewModel.shouldShowExitDialogConfirmation,
                                onUpdateShouldShowExitDialogConfirmation = mViewModel::updateShouldShowExitDialogConfirmation,
                                userFormButtonEnabled = mViewModel.userFormButtonEnabled,
                                onSubmitForm = mViewModel::submitForm,
                                isSubmitSuccess = mViewModel.isSubmitSuccess,
                                message = mViewModel.message,
                                shouldShowSaveOrErrorView = mViewModel.shouldShowSaveOrErrorView
                            )
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }


    /////////////////////////////////////
    //
    // CLASSES METHODS
    //
    /////////////////////////////////////
    fun launchMainActivity() {
        Timber.d("launchMainActivity()")
        Navigator(this).callMainActivity()
        finish()
    }
}