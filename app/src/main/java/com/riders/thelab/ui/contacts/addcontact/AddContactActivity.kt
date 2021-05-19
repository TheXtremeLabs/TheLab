package com.riders.thelab.ui.contacts.addcontact

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.R
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.local.model.Contact
import com.riders.thelab.databinding.ActivityAddContactBinding
import com.riders.thelab.utils.Validator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
open class AddContactActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityAddContactBinding

    @Inject
    private lateinit var mAddContactViewModel: AddContactViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAddContactBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        supportActionBar?.setTitle(getString(R.string.activity_title_add_new_contact))

        viewBinding.inputName.addTextChangedListener(MyTextWatcher(viewBinding.inputName))
        viewBinding.inputEmail.addTextChangedListener(MyTextWatcher(viewBinding.inputEmail))
        viewBinding.inputPassword.addTextChangedListener(MyTextWatcher(viewBinding.inputPassword))

        mAddContactViewModel.getAddedContact().observe(this, { added ->
            if (added)
                finish()
            else
                Timber.e("onAddContactError()")

        })

        viewBinding.floatingLabelsBtnSignup.setOnClickListener {
            try {
                (this@AddContactActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            } catch (e: Exception) {
                Timber.e(e)
            }

            submitForm()
        }
    }

    override fun onDestroy() {
        mAddContactViewModel.clearDisposable()
        super.onDestroy()
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

        UIManager.showActionInToast(this, "Thank You")

        mAddContactViewModel.saveContact(
            Contact(
                viewBinding.inputName.text.toString(),
                viewBinding.inputEmail.text.toString(),
                viewBinding.inputPassword.text.toString()
            )
        )
    }

    open fun validateName(): Boolean {
        if (viewBinding.inputName.text.toString().trim { it <= ' ' }.isEmpty()) {
            viewBinding.inputLayoutName.error = getString(R.string.err_msg_name)
            requestFocus(viewBinding.inputName)
            return false
        } else {
            viewBinding.inputLayoutName.setErrorEnabled(false)
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
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    internal class MyTextWatcher constructor(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(sEditable: Editable) {
            when (view.id) {
                R.id.input_name -> AddContactActivity.validateName()
                R.id.input_email -> AddContactActivity.validateEmail()
                R.id.input_password -> AddContactActivity.validatePassword()
            }
        }
    }
}