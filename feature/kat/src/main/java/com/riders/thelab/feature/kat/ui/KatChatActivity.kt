package com.riders.thelab.feature.kat.ui

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
import kotlinx.coroutines.launch
import timber.log.Timber

class KatChatActivity : BaseComponentActivity() {

    private val mViewModel: KatChatViewModel by viewModels<KatChatViewModel>()


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate()")

        mViewModel.getBundle(intent)

        mViewModel.setChatRoomId()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            KatChatRoomContent(viewModel = mViewModel)
                        }
                    }
                }
            }
        }

        mViewModel.findOtherUserById(this@KatChatActivity)
    }

    override fun onStart() {
        super.onStart()

        mViewModel.getOrCreateChatRoomReference(this@KatChatActivity)
    }

    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }

    companion object {
        const val EXTRA_USER_ID: String = "EXTRA_USER_ID"
        const val EXTRA_PHONE: String = "EXTRA_PHONE"
        const val EXTRA_USERNAME: String = "EXTRA_USERNAME"
    }
}