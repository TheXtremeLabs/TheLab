package com.riders.thelab.ui.contacts.addcontact;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.riders.thelab.R;
import com.riders.thelab.data.local.model.Contact;
import com.riders.thelab.ui.base.BaseViewImpl;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static android.content.Context.INPUT_METHOD_SERVICE;

@SuppressLint("NonConstantResourceId")
public class AddContactView extends BaseViewImpl<AddContactPresenter>
        implements AddContactContract.View {

    @BindView(R.id.input_name)
    TextInputEditText inputName;
    @BindView(R.id.input_email)
    TextInputEditText inputEmail;
    @BindView(R.id.input_password)
    TextInputEditText inputPassword;
    @BindView(R.id.input_layout_name)
    TextInputLayout inputLayoutName;
    @BindView(R.id.input_layout_email)
    TextInputLayout inputLayoutEmail;
    @BindView(R.id.input_layout_password)
    TextInputLayout inputLayoutPassword;
    @BindView(R.id.floating_labels_btn_signup)
    MaterialButton btnSignUp;
    private AddContactActivity context;

    @Inject
    AddContactView(AddContactActivity context) {
        this.context = context;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onCreate() {
        Timber.d("onCreate()");
        getPresenter().attachView(this);
        ButterKnife.bind(this, context.findViewById(android.R.id.content));

        context.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context.getSupportActionBar().setDisplayShowHomeEnabled(true);

        context.getSupportActionBar().setTitle(context.getString(R.string.activity_title_add_new_contact));

        inputName.addTextChangedListener(new AddContactView.MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new AddContactView.MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new AddContactView.MyTextWatcher(inputPassword));
    }

    @Override
    public void onDestroy() {
        Timber.d("onDestroy()");
        getPresenter().detachView();
        context = null;
    }

    @Override
    public void showLoading() {
        Timber.d("showLoading()");

    }

    @Override
    public void hideLoading() {
        Timber.e("hideLoading()");

    }

    @Override
    public Contact getEnteredFormData() {
        return new Contact(
                inputName.getText().toString(),
                inputEmail.getText().toString(),
                inputPassword.getText().toString()
        );
    }

    @Override
    public void onAddContactSuccess() {
        Timber.d("onAddContactSuccess()");
        context.finish();
    }

    @Override
    public void onAddContactError() {
        Timber.e("onAddContactError()");
    }

    @OnClick(R.id.floating_labels_btn_signup)
    void onButtonClicked() {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            Timber.e(e);
        }

        submitForm();
    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        Toast.makeText(context.getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();

        getPresenter().saveContact();
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(context.getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(context.getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(context.getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private class MyTextWatcher implements TextWatcher {

        private final View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable sEditable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;

                case R.id.input_email:
                    validateEmail();
                    break;

                case R.id.input_password:
                    validatePassword();
                    break;

                default:
                    break;
            }
        }
    }
}
