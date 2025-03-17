package com.riders.thelab.ui.vocalassistant

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.utils.UIManager
import kotlinx.coroutines.launch

class VocalAssistantActivity : BaseComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UIManager.showToast(this, "Trigger phrase detected!")

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    VocalAssistantContent()
                }
            }
        }
    }

    override fun backPressed() {
        finish()
    }
}