package com.riders.thelab.ui.kat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.data.remote.dto.kat.Kat;
import com.riders.thelab.databinding.RowKatMessageOtherBinding;
import com.riders.thelab.databinding.RowKatMessageSelfBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class KatMessagesAdapter extends RecyclerView.Adapter<KatViewHolder> {

    private Context mContext;
    private List<Kat> mKatModelList;
    private static final int SELF = 100;

    public KatMessagesAdapter(Context context, List<Kat> katModelList) {
        this.mContext = context;
        this.mKatModelList = katModelList;
    }


    @Override
    public int getItemCount() {
        if (!mKatModelList.isEmpty())
            return mKatModelList.size();
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        Kat message = mKatModelList.get(position);
        if (KatActivity.SENDER_ID.equals(message.getSenderId())) {
            return SELF;
        }

        return position;
    }

    @NonNull
    @NotNull
    @Override
    public KatViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        RowKatMessageSelfBinding selfItemView;
        RowKatMessageOtherBinding otherItemView;

        // view type is to identify where to render the chat message
        // left or right
        if (viewType == SELF) {
            // self message
            Timber.d("// self message");
            selfItemView =
                    RowKatMessageSelfBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false);
            return new KatViewHolder(selfItemView);
        } else {
            // others message
            Timber.d("// others message");
            otherItemView =
                    RowKatMessageOtherBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false);

            return new KatViewHolder(otherItemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull KatViewHolder holder, int position) {
        Kat mKatModel = mKatModelList.get(position);

        if (getItemViewType(position) == SELF) {
            holder.bindSelf(mKatModel);
        } else {
            holder.bindOther(mKatModel);
        }
    }

    public void populateItem(Kat katMessage) {
        mKatModelList.add(katMessage);
        notifyDataSetChanged();
    }

    public void populateAllItems(List<Kat> katMessageList) {
        mKatModelList.addAll(katMessageList);
        notifyDataSetChanged();
    }

    public void removeAll() {
        mKatModelList = new ArrayList<>();
        notifyDataSetChanged();
    }
}
