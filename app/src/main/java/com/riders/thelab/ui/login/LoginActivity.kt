package com.riders.thelab.ui.login

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.BuildConfig
import com.riders.thelab.R
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.utils.LabNetworkManagerNewAPI
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.databinding.ActivityLoginBinding
import com.riders.thelab.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(),
    CoroutineScope,
    View.OnClickListener, TextView.OnEditorActionListener, CompoundButton.OnCheckedChangeListener {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var _viewBinding: ActivityLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    var isPasswordVisible: Boolean = false


    //TODO : Due to Heroku back-end free services ending,
    // Use of the database to store and log users
    private val mViewModel: LoginViewModel by viewModels()

    private var mNetworkManager: LabNetworkManagerNewAPI? = null
    private lateinit var navigator: Navigator

    private var isChecked: Boolean = false

    var networkState: Boolean = false


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            mNetworkManager = LabNetworkManagerNewAPI.getInstance(this@LoginActivity)
            val isOnline = mNetworkManager?.isOnline()
            Timber.d("Is app online : $isOnline")
        }

        initViewModelObservers()

        navigator = Navigator(this)

        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            LoginContent(viewModel = mViewModel)
                        }
                    }
                }
            }
        }

        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // Note that this happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                mViewModel.networkState.collect { state ->
                    // New value received
                    when (state) {
                        is NetworkState.Available -> {
                            networkState = state.available
                            //enableEditTexts()
                            //enableButton()
                           // hideMainActivityButton()
                        }

                        is NetworkState.Disconnected -> {
                            networkState = state.disconnected
                            //disableEditTexts()
                           // disableButton()
                            //showGoToMainActivityButton()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    private fun setListeners() {
        Timber.d("setListeners()")

        binding.btnEnter.isClickable = true
        binding.btnEnter.isEnabled = true
        binding.btnMainActivity.isClickable = true
        binding.btnMainActivity.isEnabled = true

        binding.inputEmail.addTextChangedListener(MyTextWatcher(binding.inputEmail))
        binding.inputPassword.addTextChangedListener(MyTextWatcher(binding.inputPassword))
        binding.inputPassword.setOnEditorActionListener(this)

        binding.btnPasswordVisibility.setOnClickListener(this)
        binding.cbRememberMe.setOnCheckedChangeListener(this)
        binding.btnMainActivity.setOnClickListener(this)
        binding.btnEnter.setOnClickListener(this)

        binding.btnNoAccountRegister.setOnClickListener(this)
    }

    private fun initViewModelObservers() {
        Timber.d("initViewModelObservers()")
        /*mViewModel.getDataStoreEmail().observe(this) { binding.inputEmail.setText(it) }
        mViewModel.getDataStorePassword().observe(this) { binding.inputPassword.setText(it) }
        mViewModel.getDataStoreRememberCredentials()
            .observe(this) { binding.cbRememberMe.isChecked = it }*/

        mNetworkManager?.getConnectionState()?.observe(
            this
        ) {
            UIManager.showConnectionStatusInSnackBar(
                this,
                it
            )
        }
    }

    private fun onLoginFailed() {
        Timber.e("onLoginFailed()")
        binding.inputLayoutEmail.error = getString(R.string.err_msg_wrong_email_or_password)
        binding.inputLayoutPassword.error = getString(R.string.err_msg_wrong_email_or_password)
    }

    /**
     * logging in user. Will make http post request with name, email
     * as parameters
     */
    private fun login() {
        Timber.e("login()")

        if (!validateEmail()) {
            return
        }
        if (!validatePassword()) {
            return
        }

        UIManager.hideKeyboard(this, findViewById(android.R.id.content))

        showLoading()

        if (isChecked) {
            Timber.d("save user credentials in datastore")
            mViewModel.saveUserDataInDataStore(
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString(),
                isChecked
            )
        }

        mViewModel.makeCallLogin(
            binding.inputEmail.text.toString(),
            binding.inputPassword.text.toString()
        )
    }


    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    // Validating email
    private fun validateEmail(): Boolean {
        val email: String = binding.inputEmail.text.toString().trim { it <= ' ' }
        if (email.isEmpty() || !isValidEmail(email)) {
            binding.inputLayoutEmail.error = getString(R.string.err_msg_email)
            requestFocus(binding.inputEmail)
            return false
        } else {
            binding.inputLayoutEmail.isErrorEnabled = false
        }
        return true
    }


    // Validating password
    private fun validatePassword(): Boolean {
        if (binding.inputPassword.text.toString().trim { it <= ' ' }.isEmpty()) {
            binding.inputLayoutPassword.error = getString(R.string.err_msg_name)
            requestFocus(binding.inputPassword)
            return false
        } else {
            binding.inputLayoutPassword.isErrorEnabled = false
        }
        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return (!TextUtils.isEmpty(email)
                && Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }

    private fun showLoading() {
        if (View.VISIBLE != binding.progressBar.visibility) {
            binding.progressBar.isIndeterminate = true
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        if (View.VISIBLE == binding.progressBar.visibility) {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun enableButton() {
        if (!binding.btnEnter.isEnabled) {
            Timber.d("enableButton()")

            binding.btnEnter.isClickable = true
            binding.btnEnter.isEnabled = true
        }
    }

    private fun disableButton() {
        if (binding.btnEnter.isEnabled) {
            Timber.e("disableButton()")

            binding.btnEnter.isClickable = false
            binding.btnEnter.isEnabled = false
        }
    }

    private fun hideMainActivityButton() {
        Timber.e("hideMainActivityButton()")

        if (View.VISIBLE == binding.btnMainActivity.visibility) {
            binding.btnMainActivity.visibility = View.GONE
        }
    }

    private fun enableEditTexts() {
        Timber.e("enableEditTexts()")
        if (!binding.inputEmail.isEnabled) {
            binding.inputEmail.isEnabled = true
        }

        if (!binding.inputPassword.isEnabled) {
            binding.inputPassword.isEnabled = true
        }
    }

    private fun disableEditTexts() {
        Timber.e("disableEditTexts()")
        if (binding.inputEmail.isEnabled) {
            binding.inputEmail.isEnabled = false
        }

        if (binding.inputPassword.isEnabled) {
            binding.inputPassword.isEnabled = false
        }
    }


    private fun showGoToMainActivityButton() {
        if (View.VISIBLE != binding.btnMainActivity.visibility) {
            Timber.d("showGoToMainActivityButton()")
            binding.btnMainActivity.visibility = View.VISIBLE
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun callMainActivity() {
        Timber.d("callMainActivity()")

        lifecycleScope.launch(coroutineContext) {
            delay(TimeUnit.SECONDS.toMillis(3))
            navigator.callMainActivity()
            finish()
        }
    }

    fun callSignUpActivity() {
        Timber.d("callSignUpActivity()")
/*
        val intent = Intent(this, SignUpActivity::class.java)

        val sePairThumb: Pair<View, String> =
            Pair.create(
                binding.cvLogo,
                getString(R.string.splash_background_transition_name)
            )

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sePairThumb)

        // Call navigator to switch activity with or without transition according
        // to the device's version running the application
        options.toBundle()?.let { navigator.callSignUpActivity(intent, it) }*/
        navigator.callSignUpActivity()
    }

    override fun onClick(view: View?) {

        when (view?.id) {
            R.id.btn_password_visibility -> {
                Timber.d("onPasswordVisibilityButtonClicked()")

                // If flag is false - password hidden (default)
                if (!isPasswordVisible) {
                    binding.inputPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    binding.btnPasswordVisibility.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_visibility_off
                        )
                    )
                    // Tint color programmatically
                    // https://stackoverflow.com/questions/20121938/how-to-set-tint-for-an-image-view-programmatically-in-android
                    binding.btnPasswordVisibility.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.purple_200
                        ), PorterDuff.Mode.SRC_IN
                    )
                } else {
                    binding.inputPassword.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    binding.btnPasswordVisibility.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_visibility
                        )
                    )
                    binding.btnPasswordVisibility.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.white
                        ), PorterDuff.Mode.SRC_IN
                    )
                }

                isPasswordVisible = !isPasswordVisible
            }
            R.id.btn_enter -> {
                login()
            }

            R.id.btn_main_activity -> {
                callMainActivity()
            }

            R.id.btn_no_account_register -> {
                callSignUpActivity()
            }
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            UIManager.hideKeyboard(this, findViewById(android.R.id.content))
            return true
        }
        return false
    }

    override fun onCheckedChanged(checkBox: CompoundButton?, checked: Boolean) {
        this.isChecked = checked
    }

    inner class MyTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Ignored
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Ignored
        }

        override fun afterTextChanged(s: Editable?) {
            when (view.id) {
                R.id.input_password -> validatePassword()
                R.id.input_email -> validateEmail()
            }
        }
    }
}