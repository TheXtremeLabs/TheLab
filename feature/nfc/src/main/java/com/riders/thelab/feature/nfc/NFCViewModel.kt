package com.riders.thelab.feature.nfc

import com.riders.thelab.core.nfc.LabNFCManager
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.compose.utils.findActivity

class NFCViewModel : BaseViewModel() {

    val mLabNFCManager: LabNFCManager? by lazy {
        mWeakReference
            ?.get()
            ?.let { activity ->
                LabNFCManager.getInstance(
                    activity = activity.findActivity() as NFCActivity,
                    nfcReaderCallback = {

                    })
            }
    }
}