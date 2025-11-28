package com.riders.thelab.feature.nfc

import androidx.lifecycle.DefaultLifecycleObserver
import com.riders.thelab.core.nfc.LabNFCManager
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.core.ui.data.local.UiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NFCViewModel @Inject constructor(
    val uiRepository: UiRepository
) : BaseViewModel(), DefaultLifecycleObserver {

    val mLabNFCManager: LabNFCManager? by lazy {
        mWeakReference
            ?.get()
            ?.let { activity ->
                LabNFCManager.getInstance(
                    activity = activity.findActivity() as NFCActivity,
                    nfcReaderCallback = {tag->

                    })
            }
    }
}