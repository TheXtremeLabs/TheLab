package com.riders.thelab.ui.schedule

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.broadcast.ScheduleAlarmReceiver
import com.riders.thelab.core.bus.KotlinBus
import com.riders.thelab.core.bus.Listen
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.ui.base.BaseActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber

class ScheduleActivity : BaseActivity() {

    private val mViewModel: ScheduleViewModel by viewModels()

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
                            ScheduleContent(mViewModel)
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        if (ScheduleAlarmReceiver.REQUEST_CODE == resultCode) {
            Timber.e("result code caught !!!!")
        }
    }

    override fun onPause() {
        try {
            mViewModel.unregisterReceiver(this)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        try {
            mViewModel.registerReceiver(this)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    override fun onDestroy() {
        mViewModel.unbindService(this)
        super.onDestroy()
    }

    ////////////////////////////
    //
    // EVENT BUS
    //
    ////////////////////////////
    @OptIn(DelicateCoroutinesApi::class)
    @Listen
    fun onEventTriggered() {
        Timber.d("onEventTriggered()")
        lifecycleScope.launch {
            KotlinBus.subscribe<String> {
                Timber.d("Received | Count down finished event with, $it")
                mViewModel.updateCountDownDone(true)
            }
        }
    }
}

