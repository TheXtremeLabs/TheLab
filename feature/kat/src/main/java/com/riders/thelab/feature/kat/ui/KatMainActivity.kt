package com.riders.thelab.feature.kat.ui

import android.content.Intent
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class KatMainActivity : BaseComponentActivity() {

    private val mMainViewModel: KatMainViewModel by viewModels<KatMainViewModel>()
    private val mProfileViewModel: KatProfileViewModel by viewModels<KatProfileViewModel>()


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate()")

        mMainViewModel.checkIfUserSignIn(this@KatMainActivity)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            KatMainContent(
                                viewModel = mMainViewModel,
                                profileViewModel = mProfileViewModel
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mMainViewModel.getFCMToken(this@KatMainActivity)
    }


    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }

    fun notifyCurrentUsername(currentUsername: String) {
        mProfileViewModel.updateCurrentProfileUsername(currentUsername)
    }

    fun launchKatChatActivity(userId: String, phone: String, username: String) {
        Intent(this, KatChatActivity::class.java)
            .apply {
                putExtra(KatChatActivity.EXTRA_USER_ID, userId)
                putExtra(KatChatActivity.EXTRA_PHONE, phone)
                putExtra(KatChatActivity.EXTRA_USERNAME, username)
            }
            .run {
                startActivity(this)
            }
    }
}