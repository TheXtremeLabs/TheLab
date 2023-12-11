package com.riders.thelab.feature.kat

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
import kotlinx.coroutines.launch
import timber.log.Timber

class KatMainActivity : BaseComponentActivity() {

    private val mViewModel: KatMainViewModel by viewModels<KatMainViewModel>()


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate()")

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            KatMainContent(viewModel = mViewModel)
                        }
                    }
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        mViewModel.checkIfUserSignIn(this@KatMainActivity)
    }

    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }

    fun launchKatActivity(username: String) {
        Intent(this, KatActivity::class.java)
            .apply {
            putExtra(KatActivity.EXTRA_USERNAME, username)
        }
            .run {
            startActivity(this)
            }
    }
}