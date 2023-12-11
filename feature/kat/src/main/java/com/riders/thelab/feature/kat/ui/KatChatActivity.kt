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
import com.riders.thelab.feature.kat.utils.FirebaseUtils
import kotlinx.coroutines.launch
import timber.log.Timber

class KatChatActivity : BaseComponentActivity() {

    private val mViewModel: KatChatViewModel by viewModels<KatChatViewModel>()

    private var mOtherUserId: String? = null
    private var mOtherUsername: String? = null
    private var mChatRoomId: String? = null


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate()")

        getBundle()

        FirebaseUtils.getCurrentUserID()?.let { firebaseUserId ->
            mOtherUserId?.let { otherUserId ->
                mChatRoomId = FirebaseUtils.getChatRoomId(firebaseUserId, otherUserId)

                mViewModel.setChatRoomId(mChatRoomId!!)
            }
        }

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

        mViewModel.getOrCreateChatRoomReference(this, mChatRoomId!!, mOtherUserId!!)
    }

    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }


    private fun getBundle() {
        Timber.d("getBundle()")
        intent.extras?.let {
            val username = it.getString(EXTRA_USERNAME)
            if (username != null) {
                mViewModel.updateKatUsername(username)
            }
            val userId = it.getString(EXTRA_USER_ID)
            mOtherUserId = userId
        }
    }

    fun clearMessageTextField() {
        mViewModel.updateMessageText("")
    }

    companion object {
        const val EXTRA_USER_ID: String = "EXTRA_USER_ID"
        const val EXTRA_PHONE: String = "EXTRA_PHONE"
        const val EXTRA_USERNAME: String = "EXTRA_USERNAME"
    }
}