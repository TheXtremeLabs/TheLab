package com.riders.thelab.core.utils

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class RecyclerItemClickListener(
    context: Context,
    recyclerView: RecyclerView,
    listener: OnItemClickListener?
) : RecyclerView.OnItemTouchListener {

    private var mGestureDetector: GestureDetector? = null
    private var mListener: OnItemClickListener? = listener

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
        fun onLongClick(view: View?, position: Int)
    }

    init {
        mGestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null && mListener != null) {
                        mListener!!.onLongClick(child, recyclerView.getChildPosition(child))
                    }
                }
            })
    }

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && mListener != null && mGestureDetector!!.onTouchEvent(e)) {
            mListener!!.onItemClick(childView, view.getChildPosition(childView))
            return true
        }
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        Timber.d(e.toString())
    }
}