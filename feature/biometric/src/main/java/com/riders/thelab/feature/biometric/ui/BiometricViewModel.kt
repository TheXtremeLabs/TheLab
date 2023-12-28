package com.riders.thelab.feature.biometric.ui

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.CryptoObject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.data.local.model.compose.getResult
import com.riders.thelab.core.data.local.model.compose.switch
import com.riders.thelab.feature.biometric.R
import com.riders.thelab.feature.biometric.core.exception.InvalidCryptoLayerException
import com.riders.thelab.feature.biometric.data.IBiometric
import com.riders.thelab.feature.biometric.data.IUser
import com.riders.thelab.feature.biometric.data.bean.CryptoPurpose
import com.riders.thelab.feature.biometric.data.biometric.AuthContext
import com.riders.thelab.feature.biometric.data.biometric.BiometricInfo
import com.riders.thelab.feature.biometric.data.biometric.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class BiometricViewModel @Inject constructor(
    private val userRepository: IUser,
    private val biometricRepository: IBiometric
) : ViewModel() {

    //////////////////////////////////////////
    // Variables
    //////////////////////////////////////////
    lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    //////////////////////////////////////////
    // Composable states
    //////////////////////////////////////////

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)
        }

    /*
        @SuppressLint("NewApi")
        fun createBiometricPrompt(context: FragmentActivity): BiometricPrompt.PromptInfo {
            Timber.d("createBiometricPrompt()")
            val executor = ContextCompat.getMainExecutor(context)

            val callback =
                object : BiometricPrompt.AuthenticationCallback() {
                    @SuppressLint("RestrictedApi")
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Log.d(TAG, "$errorCode :: $errString")
                        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                            // loginWithPassword() // Because in this app, the negative button allows the user to enter an account password. This is completely optional and your app doesn’t have to do it.
                        }
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Log.d(TAG, "Authentication failed for an unknown reason")
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        Log.d(TAG, "Authentication was successful")
                        // Proceed with viewing the private encrypted message.
                        // showEncryptedMessage(result.cryptoObject)
                    }
                }

            promptInfo = BiometricPrompt.PromptInfo(
                context.findActivity() as BiometricActivity,
                executor,
                callback
            )
            return promptInfo
        }

        fun canAuthenticate(context: Context) {
            Timber.d("canAuthenticate()")
            val biometricManager = BiometricManager.from(context)
            when (biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS ->
                    Log.d("MY_APP_TAG", "App can authenticate using biometrics.")

                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                    Log.e("MY_APP_TAG", "No biometric features available on this device.")

                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                    Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")

                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    // Prompts the user to create credentials that your app accepts.
                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                        )
                    }
                    //startActivityForResult(enrollIntent, REQUEST_CODE)
                }
            }
        }

        fun authenticate(context: Context) {
            Timber.d("authenticate()")
            val promptInfo = createBiometricPrompt(context.findActivity() as BiometricActivity)

            if (BiometricManager.from(context).canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
                biometricPrompt.authenticate(promptInfo, cryptoObject)
            } else {
                // loginWithPassword()
            }
        }

        private fun generateSecretKey(keyGenParameterSpec: KeyGenParameterSpec) {
            Timber.d("generateSecretKey()")
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
            )
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }

        private fun getSecretKey(): SecretKey {
            Timber.d("getSecretKey()")
            val keyStore = KeyStore.getInstance("AndroidKeyStore")

            // Before the keystore can be accessed, it must be loaded.
            keyStore.load(null)
            return keyStore.getKey(KEY_NAME, null) as SecretKey
        }

        private fun getCipher(): Cipher {
            Timber.d("getCipher()")
            return Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/"
                        + KeyProperties.BLOCK_MODE_CBC + "/"
                        + KeyProperties.ENCRYPTION_PADDING_PKCS7
            )
        }*/

    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(
        LoginUiState.Logged(loggedIn = false)
    )
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        Timber.d("init method")
        viewModelScope.launch(SupervisorJob() + coroutineExceptionHandler) {
            userRepository.isUserLoggedIn
                .map { Pair(it, biometricRepository.getBiometricInfo()) }
                .collect { info -> reduceState(info.first, info.second) }
        }
    }

    private suspend fun reduceState(isLoggedIn: Boolean, biometricInfo: BiometricInfo) {
        Timber.d("reduceState() | isLoggedIn: $isLoggedIn, biometricInfo: $biometricInfo")

        val currentState: LoginUiState.Logged = uiState.value as LoginUiState.Logged
        val askBiometricEnrollment =
            shouldAskTokenEnrollment(isLoggedIn, currentState, biometricInfo)
        var authContext: AuthContext? = currentState.authContext

        // we want to check if enrollment is ok or not
        if (askBiometricEnrollment) {
            getResult { prepareAuthContext(CryptoPurpose.Encryption) }
                .switch(
                    success = { authContext = it },
                    error = {
                        // In this case we decide to not show and error to the end user.
                        Timber.e(it)
                    }
                )
        }

        // update state
        _uiState.update {
            (it as LoginUiState.Logged).copy(
                loggedIn = isLoggedIn,
                canLoginWithBiometry = canLoginWithBiometricToken(biometricInfo),
                askBiometricEnrollment = askBiometricEnrollment,
                authContext = authContext
            )
        }
    }

    private fun canLoginWithBiometricToken(biometricInfo: BiometricInfo) =
        (biometricInfo.biometricTokenPresent
                && biometricInfo.canAskAuthentication())

    private fun shouldAskTokenEnrollment(
        isLoggedIn: Boolean,
        currentState: LoginUiState.Logged,
        biometricInfo: BiometricInfo
    ) = (
            isLoggedIn && !currentState.askBiometricEnrollment
                    && !biometricInfo.biometricTokenPresent
                    && biometricInfo.canAskAuthentication()
            )


    fun setUsername(username: String) {
        _uiState.value = (_uiState.value as LoginUiState.Logged).copy(usernameField = username)
    }

    fun setPassword(password: String) {
        _uiState.value = (_uiState.value as LoginUiState.Logged).copy(passwordField = password)
    }

    fun createPromptInfo(purpose: CryptoPurpose, resources: Resources): BiometricPrompt.PromptInfo {
        return if (purpose == CryptoPurpose.Encryption) {
            BiometricPrompt.PromptInfo.Builder()
                .setTitle(resources.getString(R.string.prompt_title_enroll_token))
                .setSubtitle(resources.getString(R.string.prompt_subtitle_enroll_token))
                .setNegativeButtonText(resources.getString(R.string.prompt_cancel))
                .build()
        } else {
            BiometricPrompt.PromptInfo.Builder()
                .setTitle(resources.getString(R.string.prompt_title_login))
                .setSubtitle(resources.getString(R.string.prompt_subtitle_login))
                .setNegativeButtonText(resources.getString(R.string.prompt_cancel))
                .build()
        }
    }

    fun doLogin() {
        val username = (_uiState.value as LoginUiState.Logged).usernameField
        val password = (_uiState.value as LoginUiState.Logged).passwordField
        if (username.isBlank() || password.isBlank()) {
            showMessage(R.string.msg_error_username_password_required)
            return
        }
        viewModelScope.launch {
            userRepository.login(username, password)
        }
    }

    @SuppressLint("RestrictedApi")
    fun onAuthError(errorCode: Int, errString: String) {
        when (errorCode) {
            BiometricPrompt.ERROR_USER_CANCELED,
            BiometricPrompt.ERROR_CANCELED,
            BiometricPrompt.ERROR_NEGATIVE_BUTTON -> {
                Timber.i("operation is cancelled by user interaction")
            }

            else -> {
                showMessage(errString)
            }
        }
        _uiState.update {
            (it as LoginUiState.Logged).copy(
                askBiometricEnrollment = false,
                authContext = null
            )
        }
    }

    fun onAuthSucceeded(cryptoObject: CryptoObject?) {
        Timber.i("On Auth Succeeded $cryptoObject")
        val pendingAuthContext = (uiState.value as LoginUiState.Logged).authContext
        _uiState.update {
            (it as LoginUiState.Logged).copy(
                askBiometricEnrollment = false,
                authContext = null
            )
        }
        viewModelScope.launch {
            pendingAuthContext?.let { authContext ->
                if (authContext.purpose == CryptoPurpose.Encryption) {
                    startBiometricTokenEnrollment(cryptoObject!!)
                } else {
                    startLoginWithToken(cryptoObject!!)
                }
            }
        }
    }

    private suspend fun startBiometricTokenEnrollment(cryptoObject: CryptoObject) {
        val result = getResult { biometricRepository.fetchAndStoreEncryptedToken(cryptoObject) }
        result.switch(
            success = { Timber.i("fetchAndStoreEncryptedToken done") },
            error = {
                it?.let { ex ->
                    if (ex is InvalidCryptoLayerException) {
                        handleInvalidCryptoException(ex, false)
                    } else {
                        handleError(ex)
                    }
                }
            }
        )
    }

    private suspend fun startLoginWithToken(cryptoObject: CryptoObject) {
        getResult {
            val tokenAsCredential = biometricRepository.decryptToken(cryptoObject)
            doLoginWithToken(tokenAsCredential)
        }.switch(
            success = { Timber.d("Login Done") },
            error = { th ->
                if (th is InvalidCryptoLayerException) {
                    _uiState.update { (it as LoginUiState.Logged).copy(canLoginWithBiometry = false) }
                } else {
                    handleError(th)
                }
            }
        )
    }

    private fun doLoginWithToken(tokenAsCredential: String) {
        viewModelScope.launch {
            delay(100)
            userRepository.loginWithToken(tokenAsCredential)
        }
    }

    fun requireBiometricLogin() {
        viewModelScope.launch {
            getResult { prepareAuthContext(CryptoPurpose.Decryption) }
                .switch(
                    success = { authCtx ->
                        _uiState.update {
                            (it as LoginUiState.Logged).copy(
                                askBiometricEnrollment = false,
                                authContext = authCtx
                            )
                        }
                    },
                    error = {
                        it?.let { ex ->
                            if (ex is InvalidCryptoLayerException) {
                                handleInvalidCryptoException(ex, true)
                            } else {
                                handleError(ex)
                            }
                        }
                    }
                )
        }
    }

    private suspend fun prepareAuthContext(purpose: CryptoPurpose): AuthContext {
        Timber.d("prepareAuthContext() | purpose: $purpose")
        val cryptoObject = biometricRepository.createCryptoObject(purpose)
        return AuthContext(purpose = purpose, cryptoObject = cryptoObject)
    }

    private fun handleError(ex: Throwable?) {
        Timber.e(ex, "handleException: ${ex?.message}")
        ex?.let {
            showMessage(R.string.msg_error_generic)
        }
    }

    private fun handleInvalidCryptoException(ex: InvalidCryptoLayerException, isLogin: Boolean) {
        Timber.e(ex, "handleInvalidCryptoException... isLogin: $isLogin")
        if (ex.isKeyPermanentlyInvalidated()) {
            showMessage(R.string.msg_error_key_permanently_invalidated)
        } else if (ex.isKeyInitFailed()) {
            showMessage(R.string.msg_error_key_init_fail)
        } else {
            showMessage(R.string.msg_error_generic)
        }
        if (isLogin) {
            //update to inform ui that login with biometry is not available
            _uiState.update { (it as LoginUiState.Logged).copy(canLoginWithBiometry = false) }
        }
    }

    private fun showMessage(message: String) {
        Timber.e("showMessage() | message: $message")
        // UIManager.showMessage(message)
    }

    private fun showMessage(@StringRes messageTextId: Int) {
        Timber.e("showMessage() | message: $messageTextId")
        // SnackbarManager.showMessage(messageTextId)
    }

    fun getCryptoObject(): CryptoObject {
        return runBlocking { biometricRepository.createCryptoObject(CryptoPurpose.Decryption) }
    }

    private fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            userRepository.isUserLoggedIn.collect {
                _uiState.value = (uiState.value as LoginUiState.Logged).copy(
                    loggedIn = false
                )
            }
        }
    }

    override fun onCleared() {
        Timber.e("onCleared() | logout")
        logout()
        super.onCleared()
    }

    companion object {
        val TAG: String = BiometricViewModel::class.java.simpleName
    }
}