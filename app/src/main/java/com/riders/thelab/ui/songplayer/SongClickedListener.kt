package com.riders.thelab.ui.songplayer

import android.view.View
import com.riders.thelab.data.local.model.music.SongModel

interface SongClickedListener {
    fun onSongClick(view: View, item: SongModel)
}