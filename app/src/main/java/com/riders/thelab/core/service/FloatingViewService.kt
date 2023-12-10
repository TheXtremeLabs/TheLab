package com.riders.thelab.core.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.riders.thelab.R
import com.riders.thelab.databinding.LayoutFloatingWidgetBinding
import com.riders.thelab.ui.floatingview.FloatingViewActivity

class FloatingViewService : Service() {

    private var mWindowManager: WindowManager? = null
    private var mFloatingView: View? = null

    private lateinit var viewBinding: LayoutFloatingWidgetBinding


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()
        //Inflate the floating view layout we created
        viewBinding = LayoutFloatingWidgetBinding.inflate(LayoutInflater.from(this))
        mFloatingView = viewBinding.root //inflate(R.layout.layout_floating_widget, null)
        val params: WindowManager.LayoutParams =
            if (Build.VERSION_CODES.O > Build.VERSION.SDK_INT) {
                //Add the view to the window.
                WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,  // Above Oreo
                    // WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    // Below Oreo
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
                )
            } else {
                WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,  // Above Oreo
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,  // Below Oreo
                    //WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
                )
            }

        //Specify the view position
        params.gravity =
            Gravity.TOP or Gravity.LEFT //Initially view will be added to top-left corner
        params.x = 0
        params.y = 100

        //Add the view to the window
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mWindowManager!!.addView(mFloatingView, params)

        //….
        //….

        //The root element of the collapsed view layout
        val collapsedView = mFloatingView!!.findViewById<View>(R.id.collapse_view)
        //The root element of the expanded view layout
        val expandedView = mFloatingView!!.findViewById<View>(R.id.expanded_container)


        //Set the close button
        val closeButtonCollapsed = mFloatingView!!.findViewById<View>(R.id.close_btn) as ImageView
        closeButtonCollapsed.setOnClickListener { //close the service and remove the from from the window
            stopSelf()
        }


        //Set the view while floating view is expanded.
        //Set the play button.
        val playButton = mFloatingView!!.findViewById<View>(R.id.play_btn) as ImageView
        playButton.setOnClickListener {
            Toast.makeText(
                this@FloatingViewService,
                "Playing the song.",
                Toast.LENGTH_LONG
            )
                .show()
        }


        //Set the next button.
        val nextButton = mFloatingView!!.findViewById<View>(R.id.next_btn) as ImageView
        nextButton.setOnClickListener {
            Toast.makeText(
                this@FloatingViewService,
                "Playing next song.",
                Toast.LENGTH_LONG
            )
                .show()
        }


        //Set the pause button.
        val prevButton = mFloatingView!!.findViewById<View>(R.id.prev_btn) as ImageView
        prevButton.setOnClickListener {
            Toast.makeText(
                this@FloatingViewService,
                "Playing previous song.",
                Toast.LENGTH_LONG
            )
                .show()
        }


        //Set the close button
        val closeButton = mFloatingView!!.findViewById<View>(R.id.close_button) as ImageView
        closeButton.setOnClickListener {
            collapsedView.visibility = View.VISIBLE
            expandedView.visibility = View.GONE
        }


        //Open the application on thi button click
        val openButton = mFloatingView!!.findViewById<View>(R.id.open_button) as ImageView
        openButton.setOnClickListener {
            //Open the application  click.
            val intent = Intent(this@FloatingViewService, FloatingViewActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

            //close the service and remove view from the view hierarchy
            stopSelf()
        }

        //Drag and move floating view using user's touch action.
        mFloatingView!!.findViewById<View>(R.id.root_container)
            .setOnTouchListener(object : OnTouchListener {
                private var initialX = 0
                private var initialY = 0
                private var initialTouchX = 0f
                private var initialTouchY = 0f
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            //remember the initial position.
                            initialX = params.x
                            initialY = params.y

                            //get the touch location
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                            return true
                        }

                        MotionEvent.ACTION_MOVE -> {
                            //Calculate the X and Y coordinates of the view.
                            params.x = initialX + (event.rawX - initialTouchX).toInt()
                            params.y = initialY + (event.rawY - initialTouchY).toInt()

                            //Update the layout with new X & Y coordinate
                            mWindowManager!!.updateViewLayout(mFloatingView, params)
                            return true
                        }

                        MotionEvent.ACTION_UP -> {
                            val xDiff = (event.rawX - initialTouchX).toInt()
                            val yDiff = (event.rawY - initialTouchY).toInt()

                            //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                            //So that is click event.
                            if (xDiff < 10 && yDiff < 10) {
                                if (isViewCollapsed()) {
                                    //When user clicks on the image view of the collapsed layout,
                                    //visibility of the collapsed layout will be changed to "View.GONE"
                                    //and expanded view will become visible.
                                    collapsedView.visibility = View.GONE
                                    expandedView.visibility = View.VISIBLE
                                }
                            }
                            return true
                        }
                    }
                    return false
                }
            })
    }

    /**
     * Detect if the floating view is collapsed or expanded.
     *
     * @return true if the floating view is collapsed.
     */
    private fun isViewCollapsed(): Boolean {
        return (mFloatingView == null
                || mFloatingView!!.findViewById<View>(R.id.collapse_view).visibility == View.VISIBLE)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mFloatingView != null) mWindowManager!!.removeView(mFloatingView)
    }
}