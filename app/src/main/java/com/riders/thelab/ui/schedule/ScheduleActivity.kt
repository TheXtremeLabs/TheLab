package com.riders.thelab.ui.schedule

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.riders.thelab.core.broadcast.ScheduleAlarmReceiver
import com.riders.thelab.core.bus.AlarmEvent
import com.riders.thelab.core.bus.KotlinBus
import com.riders.thelab.core.bus.Listen
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.core.views.toast.ToastTypeEnum
import com.riders.thelab.databinding.ActivityScheduleBinding
import com.riders.thelab.ui.base.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

class ScheduleActivity : BaseActivity() {

    private var _viewBinding: ActivityScheduleBinding? = null

    private val binding get() = _viewBinding!!

    private val mViewModel: ScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel.getCountDown().observe(
            this
        ) { countDown ->

            showCountDownView()

            val millsFuture = (countDown * 1000).toLong()

            CoroutineScope(Dispatchers.Main).launch {
                object : CountDownTimer(millsFuture, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        updateContDownUI(millisUntilFinished / 1000)
                    }

                    override fun onFinish() {
                        Timber.e("Count down finished")
                    }
                }.start()
            }

        }

        binding.button.setOnClickListener {
            binding.time.text.toString().isNotBlank().let {
                if (it)
                    mViewModel.startAlert(this, binding.time.text.toString())
                else UIManager.showCustomToast(
                    this@ScheduleActivity,
                    ToastTypeEnum.WARNING,
                    "Field cannot be empty. Please enter a valid number"
                )
            }
        }

        // onEventTriggered()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        if (ScheduleAlarmReceiver.REQUEST_CODE == resultCode) {
            Timber.e("result code caught !!!!")

            hideCountDownView()
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

        _viewBinding = null
    }

    private fun showCountDownView() {
        binding.llDelayTimeContainer.visibility = View.VISIBLE
    }

    private fun hideCountDownView() {
        binding.llDelayTimeContainer.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    fun updateContDownUI(millisUntilFinished: Long) {
        runOnUiThread { binding.tvDelayTime.text = "" + millisUntilFinished }
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

    @Listen
    fun onEventTriggered() {
        Timber.d("onEventTriggered()")
        lifecycleScope.launch {
            KotlinBus.getInstance().subscribe<String> {
                Timber.d("Received | Count down finished event with, $it")
                hideCountDownView()
                UIManager.hideKeyboard(this@ScheduleActivity , binding.root)
            }
        }
    }
}