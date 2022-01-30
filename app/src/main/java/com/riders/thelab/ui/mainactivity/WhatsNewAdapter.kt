package com.riders.thelab.ui.mainactivity

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.databinding.RowWhatSNewItemBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class WhatsNewAdapter constructor(
    private val mContext: Context,
    private val mRecentAppList: List<App>,
    private val mListener: MainActivityAppClickListener
) : RecyclerView.Adapter<RowWhatsNewViewHolder>() {

    private var mRecyclerView: RecyclerView? = null
    var IS_UPDATE_RUNNING: Boolean = false

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        Timber.d("onAttachedToRecyclerView()")
        this.mRecyclerView = recyclerView

        // fancy animations can skip if like
        TransitionManager.beginDelayedTransition(recyclerView)
    }

    override fun getItemCount(): Int {
        return mRecentAppList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowWhatsNewViewHolder {
        val viewBinding: RowWhatSNewItemBinding =
            RowWhatSNewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RowWhatsNewViewHolder(mContext, viewBinding, mListener)
    }

    override fun onBindViewHolder(holder: RowWhatsNewViewHolder, position: Int) {
        holder.bindData(mRecentAppList[position])
    }

    override fun onViewRecycled(holder: RowWhatsNewViewHolder) {
        super.onViewRecycled(holder)

        Timber.d("onViewRecycled()")
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("NewApi")
    fun updateViewHolderProgressBars() {
        Timber.d("updateViewHolderProgressBars()")

        var position: Int = 0
        var progress = 0
        val PROGRESS_BAR_MAX_VALUE = 100

        try {
            val viewHolder = mRecyclerView?.findViewHolderForLayoutPosition(position)
            val progressBar = (viewHolder as RowWhatsNewViewHolder).getProgressBar()

            while (IS_UPDATE_RUNNING) {
                GlobalScope.launch {
                    if (position != 3) {
                        while (PROGRESS_BAR_MAX_VALUE != progress) {
                            progressBar.setProgress(progress, true)
                            progress++
                            delay(TimeUnit.MICROSECONDS.toMillis(10))
                        }
                        position++
                        mRecyclerView?.smoothScrollToPosition(position)
                    } else {
                        position = 0
                        mRecyclerView?.smoothScrollToPosition(position)
                        updateViewHolderProgressBars()
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }
}