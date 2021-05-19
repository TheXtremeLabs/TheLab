package com.riders.thelab.ui.floatingview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.R
import com.riders.thelab.core.service.FloatingViewService
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.databinding.ActivityFloatingViewBinding
import timber.log.Timber

class FloatingViewActivity : AppCompatActivity() {

    companion object {
        private const val CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084
    }

    lateinit var viewBinding: ActivityFloatingViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityFloatingViewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.activity_title_floating_view)


        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API < 23. But for API > 23
        //you have to ask for the permission in runtime.


        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API < 23. But for API > 23
        //you have to ask for the permission in runtime.
        if (LabCompatibilityManager.isMarshmallow()
            && !Settings.canDrawOverlays(this)
        ) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION)
        }

        viewBinding.notifyMe.setOnClickListener {
            Timber.d("onNotifyMeClicked()")
            startService(
                Intent(
                    this@FloatingViewActivity,
                    FloatingViewService::class.java
                )
            )
            moveAppInBackground()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.d("onActivityResult()")
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeView()
            } else { //Permission is not available
                Timber.e("onActivityResult() - Permission is not available")
                Toast.makeText(
                    this,
                    "Draw over other app permission not available. Closing the application",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * Set and initialize the view elements.
     */
    private fun initializeView() {
        Timber.d("initializeView()")
        findViewById<View>(R.id.notify_me).setOnClickListener { view: View? ->
            startService(
                Intent(
                    this@FloatingViewActivity,
                    FloatingViewService::class.java
                )
            )
            moveAppInBackground()
        }
    }


    /**
     * Move the app in background
     *
     *
     * Source : https://stackoverflow.com/questions/10461095/moving-application-in-background-on-back-button-event/10461254
     */
    private fun moveAppInBackground() {
        moveTaskToBack(true)
    }

}