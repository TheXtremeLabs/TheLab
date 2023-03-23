package com.riders.thelab.ui.kat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.data.remote.dto.kat.Kat
import com.riders.thelab.databinding.RowKatMessageOtherBinding
import com.riders.thelab.databinding.RowKatMessageSelfBinding
import timber.log.Timber

class KatMessagesAdapter(
    private val context: Context, katModelList: List<Kat>
) : RecyclerView.Adapter<KatViewHolder>() {

    companion object {
        private const val SELF = 100
    }

    private var mKatModelList: MutableList<Kat> = katModelList as MutableList<Kat>

    override fun getItemCount(): Int {
        return if (!mKatModelList.isEmpty()) mKatModelList.size else 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        val message = mKatModelList[position]
        return if (KatActivity.SENDER_ID == message.senderId) {
            SELF
        } else position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KatViewHolder {
        val selfItemView: RowKatMessageSelfBinding
        val otherItemView: RowKatMessageOtherBinding

        // view type is to identify where to render the chat message
        // left or right
        return if (viewType == SELF) {
            // self message
            Timber.d("// self message")
            selfItemView = RowKatMessageSelfBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            KatViewHolder(selfItemView)
        } else {
            // others message
            Timber.d("// others message")
            otherItemView = RowKatMessageOtherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            KatViewHolder(otherItemView)
        }
    }

    override fun onBindViewHolder(holder: KatViewHolder, position: Int) {
        val mKatModel = mKatModelList[position]
        if (getItemViewType(position) == SELF) {
            holder.bindSelf(mKatModel)
        } else {
            holder.bindOther(mKatModel)
        }
    }

    fun populateItem(katMessage: Kat) {
        mKatModelList.add(katMessage)
        notifyDataSetChanged()
    }

    fun populateAllItems(katMessageList: List<Kat>) {
        mKatModelList.addAll(katMessageList)
        notifyDataSetChanged()
    }

    fun removeAll() {
        mKatModelList = ArrayList()
        notifyDataSetChanged()
    }
}