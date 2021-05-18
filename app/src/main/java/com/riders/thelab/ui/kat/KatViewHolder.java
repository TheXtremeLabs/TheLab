package com.riders.thelab.ui.kat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.data.remote.dto.kat.Kat;
import com.riders.thelab.databinding.RowKatMessageOtherBinding;
import com.riders.thelab.databinding.RowKatMessageSelfBinding;
import com.riders.thelab.utils.DateTimeUtils;

import timber.log.Timber;

public class KatViewHolder extends RecyclerView.ViewHolder {

    RowKatMessageOtherBinding rowOtherBinding;
    RowKatMessageSelfBinding rowSelfBinding;

    public KatViewHolder(@NonNull RowKatMessageSelfBinding selfItemView) {
        super(selfItemView.getRoot());

        this.rowSelfBinding = selfItemView;
    }

    public KatViewHolder(@NonNull RowKatMessageOtherBinding otherItemView) {
        super(otherItemView.getRoot());

        this.rowOtherBinding = otherItemView;
    }

    public void bindOther(Kat data) {
        Timber.d("bindOther() object : %s", data.toString());
        rowOtherBinding.tvMessageSender.setText(data.getSenderId());
        rowOtherBinding.tvMessageContent.setText(data.getMessage());
        rowOtherBinding.tvMessageTimestamp.setText(DateTimeUtils.formatMillisToTimeHoursMinutes(data.getTimestamp()));
    }

    public void bindSelf(Kat data) {
        Timber.d("bindSelf() object : %s", data.toString());
        rowSelfBinding.tvMessageSender.setText(data.getSenderId());
        rowSelfBinding.tvMessageContent.setText(data.getMessage());
        rowSelfBinding.tvMessageTimestamp.setText(DateTimeUtils.formatMillisToTimeHoursMinutes(data.getTimestamp()));
    }
}
