package com.riders.thelab.ui.floatinglabels

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.databinding.ActivityFloatingLabelsBinding
import com.riders.thelab.utils.Validator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class FloatingLabelsActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityFloatingLabelsBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Force screen orientation to Landscape mode
        if (LabCompatibilityManager.isTablet(this))
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        //Force screen orientation to Portrait mode
        else
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        viewBinding = ActivityFloatingLabelsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle(getString(R.string.activity_title_floating_labels))

        viewBinding.inputName.addTextChangedListener(MyTextWatcher(viewBinding.inputName))
        viewBinding.inputEmail.addTextChangedListener(MyTextWatcher(viewBinding.inputEmail))
        viewBinding.inputPassword.addTextChangedListener(MyTextWatcher(viewBinding.inputPassword))

        viewBinding.floatingLabelsBtnSignup.setOnClickListener {
            try {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            } catch (e: Exception) {
                e.message
            }
            submitForm()
        }
    }

    /**
     * Validating form
     */
    private fun submitForm() {
        if (!validateName()) {
            return
        }
        if (!validateEmail()) {
            return
        }
        if (!validatePassword()) {
            return
        }
        Toast.makeText(applicationContext, "Thank You!", Toast.LENGTH_SHORT).show()
    }

    open fun validateName(): Boolean {
        if (viewBinding.inputName.text.toString().trim { it <= ' ' }.isEmpty()) {
            viewBinding.inputLayoutName.error = getString(R.string.err_msg_name)
            requestFocus(viewBinding.inputName)
            return false
        } else {
            viewBinding.inputLayoutName.isErrorEnabled = false
        }
        return true
    }

    open fun validateEmail(): Boolean {
        val email: String = viewBinding.inputEmail.text.toString().trim { it <= ' ' }
        if (email.isEmpty() || !Validator.isValidEmail(email)) {
            viewBinding.inputLayoutEmail.error = getString(R.string.err_msg_email)
            requestFocus(viewBinding.inputEmail)
            return false
        } else {
            viewBinding.inputLayoutEmail.isErrorEnabled = false
        }
        return true
    }

    open fun validatePassword(): Boolean {
        if (viewBinding.inputPassword.text.toString().trim { it <= ' ' }.isEmpty()) {
            viewBinding.inputLayoutPassword.error = getString(R.string.err_msg_password)
            requestFocus(viewBinding.inputPassword)
            return false
        } else {
            viewBinding.inputLayoutPassword.isErrorEnabled = false
        }
        return true
    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }


    internal class MyTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(sEditable: Editable) {
            when (view.id) {
                R.id.input_name -> FloatingLabelsActivity().validateName()
                R.id.input_email -> FloatingLabelsActivity().validateEmail()
                R.id.input_password -> FloatingLabelsActivity().validatePassword()
            }
        }
    }
}