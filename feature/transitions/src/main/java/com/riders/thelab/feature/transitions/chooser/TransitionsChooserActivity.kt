package com.riders.thelab.feature.transitions.chooser

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.transitions.compose.TransitionsComposeActivity
import com.riders.thelab.feature.transitions.xml.TransitionActivity
import kotlinx.coroutines.launch
import timber.log.Timber

class TransitionsChooserActivity : BaseComponentActivity() {

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            Timber.d("coroutine launch with name ${this.coroutineContext}")
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {

                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            TransitionsChooserContent { event ->
                                when (event) {
                                    is UiEvent.OnTransitionsComposeClicked -> launchTransitionComposeActivity()
                                    is UiEvent.OnTransitionsXmlClicked -> launchTransitionXMLActivity()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        finish()
    }


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    private fun launchTransitionComposeActivity() =
        Intent(this@TransitionsChooserActivity, TransitionsComposeActivity::class.java)
            .apply { Timber.d("launchTransitionComposeActivity()") }
            .run { startActivity(this) }

    private fun launchTransitionXMLActivity() =
        Intent(this@TransitionsChooserActivity, TransitionActivity::class.java)
            .apply { Timber.d("launchTransitionXMLActivity()") }
            .run { startActivity(this) }
}

