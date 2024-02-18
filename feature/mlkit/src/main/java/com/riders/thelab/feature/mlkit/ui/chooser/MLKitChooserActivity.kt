package com.riders.thelab.feature.mlkit.ui.chooser

import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import timber.log.Timber

class MLKitChooserActivity : BaseComponentActivity() {

    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }
}