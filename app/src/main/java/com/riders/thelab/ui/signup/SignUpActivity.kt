package com.riders.thelab.ui.signup

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SignUpActivity : BaseComponentActivity() {

    private val mViewModel: SignUpViewModel by viewModels()

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            Timber.d("coroutine launch with name ${this.coroutineContext}")
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            SignUpContent(viewModel = mViewModel)
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        Timber.e("backPressed()")


    }

    override fun onDestroy() {
        super.onDestroy()
    }


    /////////////////////////////////////
    //
    // CLASSES METHODS
    //
    /////////////////////////////////////

    /*@SuppressLint("NewApi")
    private fun updateToolbarUserForm() {
        Timber.d("updateToolbarUserForm()")
        if (binding.includeToolbarSignUpLayout.progressBarUserForm.progress == 0) {
            binding.includeToolbarSignUpLayout.progressBarUserForm.setProgress(100, true)
        }

        if (ContextCompat.getColor(
                this@SignUpActivity,
                R.color.white
            ) != binding.includeToolbarSignUpLayout.tvUserForm.textColors.defaultColor
        ) {
            binding.includeToolbarSignUpLayout.tvUserForm.setTextColor(
                ContextCompat.getColor(
                    this@SignUpActivity,
                    R.color.white
                )
            )
        }
    }

    @SuppressLint("NewApi")
    private fun removeToolbarUserForm() {
        Timber.d("removeToolbarUserForm()")
        if (binding.includeToolbarSignUpLayout.progressBarUserForm.progress == 100) {
            binding.includeToolbarSignUpLayout.progressBarUserForm.setProgress(0, true)
        }

        if (ContextCompat.getColor(
                this@SignUpActivity,
                R.color.white
            ) == binding.includeToolbarSignUpLayout.tvUserForm.textColors.defaultColor
        ) {
            binding.includeToolbarSignUpLayout.tvUserForm.setTextColor(
                ContextCompat.getColor(
                    this@SignUpActivity,
                    R.color.jumbo
                )
            )
        }
    }

    @SuppressLint("NewApi")
    private fun updateToolbarSuccessful() {
        Timber.d("updateToolbarSuccessful()")
        if (binding.includeToolbarSignUpLayout.progressBarSuccessful.progress == 0) {
            binding.includeToolbarSignUpLayout.progressBarSuccessful.setProgress(100, true)
        }

        if (ContextCompat.getColor(
                this@SignUpActivity,
                R.color.white
            ) != binding.includeToolbarSignUpLayout.tvSuccessful.textColors.defaultColor
        ) {
            binding.includeToolbarSignUpLayout.tvSuccessful.setTextColor(
                ContextCompat.getColor(
                    this@SignUpActivity,
                    R.color.white
                )
            )
        }
    }*/

    fun launchMainActivity() {
        Timber.d("launchMainActivity()")
        Navigator(this).callMainActivity()
        finish()
    }
}