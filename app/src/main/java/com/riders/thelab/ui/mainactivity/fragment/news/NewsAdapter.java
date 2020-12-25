package com.riders.thelab.ui.mainactivity.fragment.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.App;
import com.riders.thelab.ui.mainactivity.MainActivityAppClickListener;

import java.util.List;

import timber.log.Timber;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private final Context context;
    private final List<App> appList;
    private MainActivityAppClickListener listener;

    public NewsAdapter(Context context, List<App> appList, MainActivityAppClickListener listener) {
        this.context = context;
        this.appList = appList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(
                context,
                LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Timber.d("onBindViewHolder");
        final App item = appList.get(position);

        holder.bind(item);

        holder.cardView.setOnClickListener(view ->
                listener.onAppItemCLickListener(view, item, position));
    }

    @Override
    public int getItemCount() {
        Timber.d("getItemCount");
        if (null != appList && !appList.isEmpty())
            return appList.size();
        return 0;
    }
}
