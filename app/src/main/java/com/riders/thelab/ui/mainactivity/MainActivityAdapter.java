package com.riders.thelab.ui.mainactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.App;

import java.util.List;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityViewHolder> {

    private Context context;
    private List<App> appList;
    private MainActivityAppClickListener listener;

    public MainActivityAdapter(@NonNull Context context,
                               @NonNull List<App> appList,
                               @NonNull MainActivityAppClickListener listener) {
        this.context = context;
        this.appList = appList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        if (appList != null) {
            return appList.size();
        }
        return 0;
    }

    @NonNull
    @Override
    public MainActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainActivityViewHolder(
                context,
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_main_app_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityViewHolder holder, int position) {
        final App item = appList.get(position);

        holder.bindData(item);
        holder.itemCardView.setOnClickListener(
                view -> listener.onAppItemCLickListener(view, item, holder.getAdapterPosition()));
    }
}
