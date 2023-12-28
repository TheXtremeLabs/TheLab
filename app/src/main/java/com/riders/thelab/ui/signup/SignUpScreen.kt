package com.riders.thelab.ui.signup

import androidx.annotation.StringRes
import com.riders.thelab.R

/*
 * EXCEPTIONALLY This class is created in the ui package because of the string res annotation's call.
 * Data module doesn't have a resources package then we use the app resources folder directly
 *
 */
sealed class SignUpScreen(val route: String, @StringRes val resourceId: Int) {
    data object EULA : SignUpScreen("EULA", R.string.title_activity_license_agreement)
    data object Form : SignUpScreen("User Form", R.string.title_activity_user_inscription_form)
    data object SignUpSuccess :
        SignUpScreen("Sign Up Success", R.string.title_activity_successful_sign_up)
}
