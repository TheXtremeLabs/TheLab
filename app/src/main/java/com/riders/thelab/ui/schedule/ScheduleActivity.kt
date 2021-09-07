package com.riders.thelab.ui.schedule

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.core.broadcast.ScheduleAlarmReceiver
import com.riders.thelab.core.bus.AlarmEvent
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.core.views.toast.ToastTypeEnum
import com.riders.thelab.databinding.ActivityScheduleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

class ScheduleActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityScheduleBinding

    private val mViewModel: ScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        mViewModel.getCountDown().observe(
            this,
            { countDown ->

                showCountDownView()

                val millsFuture = (countDown * 1000).toLong()

                CoroutineScope(Dispatchers.Main).launch {
                    object : CountDownTimer(millsFuture, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            updateContDownUI(millisUntilFinished / 1000)
                        }

                        override fun onFinish() {
                            Timber.d("Count down finished")
                        }
                    }.start()
                }

            })

        viewBinding.button.setOnClickListener {
            viewBinding.time.text.toString().isNotBlank().let {
                if (it)
                    mViewModel.startAlert(this, viewBinding.time.text.toString())
                else UIManager.showCustomToast(
                    this,
                    ToastTypeEnum.WARNING,
                    "Field cannot be empty. Please enter a valid number"
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ScheduleAlarmReceiver.REQUEST_CODE == resultCode) {
            Timber.e("result code caught !!!!");

            hideCountDownView();
        }
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        try {
            mViewModel.unregisterReceiver(this)

        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        try {
            mViewModel.registerReceiver(this)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

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


    private fun showCountDownView() {
        viewBinding.llDelayTimeContainer.visibility = View.VISIBLE
    }

    private fun hideCountDownView() {
        viewBinding.llDelayTimeContainer.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    fun updateContDownUI(millisUntilFinished: Long) {
        runOnUiThread { viewBinding.tvDelayTime.text = "" + millisUntilFinished }
    }

    ////////////////////////////
    //
    // EVENT BUS
    //
    ////////////////////////////
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAlarmEventCaught(event: AlarmEvent) {
        Timber.e("onAlarmEventCaught()")
        Timber.d("Count down finished event")
        hideCountDownView()
    }
}