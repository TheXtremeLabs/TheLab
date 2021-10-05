package com.riders.thelab.ui.songplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.data.local.model.music.SongModel
import com.riders.thelab.databinding.RowSongPlayerBinding
import timber.log.Timber
import kotlin.properties.Delegates

class SongPlayerAdapter(
    private val mContext: Context,
    private val fileList: ArrayList<SongModel>,
    private val mListener: SongClickedListener
) : RecyclerView.Adapter<SongPlayerViewHolder>() {

    // Allows to remember the last item shown on screen
    private var lastClickedPosition = -1

    // This keeps track of the currently selected position
    var selectedPosition by Delegates.observable(-1) { _, oldPos, newPos ->
        if (newPos in fileList.indices) {
            notifyItemChanged(oldPos)
            notifyItemChanged(newPos)
        }
    }

    private var isClickable: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongPlayerViewHolder {
        // https://stackoverflow.com/questions/45308328/recyclerview-content-not-using-full-width-of-fragment-parent/45308754
        // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dish_item_layout, null);
        // It is important here to not supply true, but do supply the parent:
        // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dish_item_layout, parent, false);

        val viewBinding: RowSongPlayerBinding =
            RowSongPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongPlayerViewHolder(mContext, viewBinding, mListener)
    }

    override fun onBindViewHolder(holder: SongPlayerViewHolder, position: Int) {

        if (!isClickable)
            return;

        // do your click stuff
        if (position in fileList.indices) {
            holder.bindData(fileList[position], position == selectedPosition)

            holder.itemView.setOnClickListener { view ->
                if (!isClickable) {
                    Timber.e("view expand then recyclerview not clickable")
                } else {
                    // do your click stuff
                    selectedPosition = position
                    mListener.onSongClick(view!!, fileList[position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    fun setClickable(clickable: Boolean) {
        Timber.d("setClickable()")
        this.isClickable = clickable
    }
}