package com.riders.thelab.ui.contacts.addcontact

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.R
import com.riders.thelab.core.common.utils.Validator
import com.riders.thelab.core.data.local.model.Contact
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.databinding.ActivityAddContactBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AddContactActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityAddContactBinding

    private val mAddContactViewModel: AddContactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAddContactBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        supportActionBar?.title = getString(R.string.activity_title_add_new_contact)

        setListeners()
        initViewModelObservers()
    }

    private fun initViewModelObservers() {
        mAddContactViewModel.getAddedContact().observe(this) { added ->
            if (added)
                finish()
            else
                Timber.e("onAddContactError()")
        }
    }

    private fun setListeners() {
        viewBinding.inputName.addTextChangedListener(
            MyTextWatcher(
                this@AddContactActivity,
                viewBinding.inputName
            )
        )
        viewBinding.inputEmail.addTextChangedListener(
            MyTextWatcher(
                this@AddContactActivity,
                viewBinding.inputEmail
            )
        )
        viewBinding.inputPassword.addTextChangedListener(
            MyTextWatcher(
                this@AddContactActivity,
                viewBinding.inputPassword
            )
        )

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

    fun validateName(): Boolean {
        if (viewBinding.inputName.text.toString().trim { it <= ' ' }.isEmpty()) {
            viewBinding.inputLayoutName.error = getString(R.string.err_msg_name)
            requestFocus(viewBinding.inputName)
            return false
        } else {
            viewBinding.inputLayoutName.isErrorEnabled = false
        }
        return true
    }

    fun validateEmail(): Boolean {
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

    fun validatePassword(): Boolean {
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

    open class MyTextWatcher(
        private val activity: AddContactActivity,
        private val view: View
    ) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(sEditable: Editable) {
            when (view.id) {
                R.id.input_name -> activity.validateName()
                R.id.input_email -> activity.validateEmail()
                R.id.input_password -> activity.validatePassword()
            }
        }
    }
}