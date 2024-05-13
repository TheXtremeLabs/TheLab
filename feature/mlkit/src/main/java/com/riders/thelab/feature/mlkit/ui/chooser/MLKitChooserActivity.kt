package com.riders.thelab.feature.mlkit.ui.chooser

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
import com.riders.thelab.feature.mlkit.ui.compose.MLKitComposeActivity
import com.riders.thelab.feature.mlkit.ui.xml.LiveBarcodeScanningActivity
import kotlinx.coroutines.launch
import timber.log.Timber

class MLKitChooserActivity : BaseComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {

                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            MLKitChooserContent()
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }

    fun launchMLKitXmlActivity() = Intent(this, LiveBarcodeScanningActivity::class.java)
        .run {
            startActivity(this)
            finish()
        }

    fun launchMLKitComposeActivity() = Intent(this, MLKitComposeActivity::class.java)
        .run {
            startActivity(this)
            finish()
        }
}