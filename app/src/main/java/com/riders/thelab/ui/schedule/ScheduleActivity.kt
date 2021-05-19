package com.riders.thelab.ui.schedule

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.core.broadcast.ScheduleAlarmReceiver
import com.riders.thelab.core.bus.AlarmEvent
import com.riders.thelab.databinding.ActivityScheduleBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
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

        mViewModel.getCountDown().observe(this, { countDown ->

            showCountDownView()

            val millsFuture = (countDown * 1000).toLong()

            Completable.complete()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        object : CountDownTimer(millsFuture, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                updateContDownUI(millisUntilFinished / 1000)
                            }

                            override fun onFinish() {
                                Timber.d("Count down finished")
                            }
                        }.start()
                    },
                    { t: Throwable? -> Timber.e(t) })
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ScheduleAlarmReceiver.REQUEST_CODE == resultCode) {
            Timber.e("result code caught !!!!");

            hideCountDownView();
        }
    }


    override fun onResume() {
        super.onResume()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        mViewModel.registerReceiver(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        mViewModel.unregisterReceiver(this)
        super.onStop()
    }

    override fun onDestroy() {
        mViewModel.unbindService(this)
        super.onDestroy()
    }


    fun showCountDownView() {
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
    fun onAlarmEventCaught(event: AlarmEvent?) {
        Timber.e("onAlarmEventCaught()")
        Timber.d("Count down finished event")
        hideCountDownView()
    }
}