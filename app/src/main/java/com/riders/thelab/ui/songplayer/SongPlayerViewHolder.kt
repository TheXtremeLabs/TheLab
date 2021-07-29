package com.riders.thelab.ui.songplayer

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.R
import com.riders.thelab.data.local.model.music.SongModel
import com.riders.thelab.databinding.RowSongPlayerBinding
import timber.log.Timber

class SongPlayerViewHolder(
    private val context: Context,
    private val itemBinding: RowSongPlayerBinding,
    private val mListener: SongClickedListener
) : RecyclerView.ViewHolder(itemBinding.root) {

    val viewBinding: RowSongPlayerBinding get() = itemBinding

    /*init {
        itemBinding.run {
            this.listener = mListener
        }
    }*/

    fun bindData(songModel: SongModel) {
        viewBinding.songModel = songModel

        viewBinding.ivSongPlayStatus.setImageResource(if (!songModel.isPlaying) 0 else R.drawable.ic_equalizer)
    }

    fun bindData(songModel: SongModel, selected: Boolean) {
        viewBinding.songModel = songModel

        if (selected) {
            Timber.d("Item selected")
            viewBinding.ivSongPlayStatus.setImageResource(R.drawable.ic_equalizer)
            viewBinding.cvSong.strokeWidth = 2
            viewBinding.cvSong.strokeColor =
                ContextCompat.getColor(context, R.color.teal_200)
        } else {
            // nothing is selected
            viewBinding.cvSong.strokeWidth = 0
            viewBinding.cvSong.strokeColor =
                ContextCompat.getColor(context, R.color.transparent)

            viewBinding.ivSongPlayStatus.setImageResource(0)
        }
    }
}