package com.riders.thelab.ui.mainactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.data.local.model.App;

import java.util.List;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityViewHolder> {

    private final Context context;
    private final List<App> appList;
    private final MainActivityAppClickListener listener;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public MainActivityAdapter(@NonNull Context context,
                               @NonNull List<App> appList,
                               @NonNull MainActivityAppClickListener listener) {
        this.context = context;
        this.appList = appList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return null != appList
                ? appList.size()
                : 0;
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

        if (!LabCompatibilityManager.isTablet(context)) {

            /*
             *
             * Reference : https://levelup.gitconnected.com/android-recyclerview-animations-in-kotlin-1e323ffd39be
             *
             * */
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
                holder.itemCardView.startAnimation(animation);
                lastPosition = position;
            }

            holder.bindData(item);

            holder.itemCardView.setOnClickListener(
                    view -> listener.onAppItemCLickListener(view, item, position));
        } else {
            holder.bindTabletData(item);
            bindTabletViewHolder(holder, item, position);
        }
    }

    private void bindTabletViewHolder(MainActivityViewHolder holder, final App item, int position) {

        if (position == lastPosition) {
            // Item selected
            holder.itemRelativeLayout.setAlpha(1f);

            holder.backgroundLinearLayout.setVisibility(View.VISIBLE);

            holder.itemRelativeLayout.animate()
                    .setDuration(500)
                    .scaleX(1.25f)
                    .scaleY(1.25f)
                    .start();
            holder.itemCardView.setCardElevation(4f);

        } else {
            if (lastPosition == -1) // Check first launch nothing is selected
                holder.itemRelativeLayout.setAlpha(1f);

            else {
                // Item not selected
                holder.backgroundLinearLayout.setVisibility(View.INVISIBLE);

                holder.itemRelativeLayout.setAlpha(0.5f);
//                holder.frameLayout.setStrokeWidth((int) 0f);

                holder.itemRelativeLayout.setScaleX(1f);
                holder.itemRelativeLayout.setScaleY(1f);
                holder.itemCardView.setCardElevation(0f);
            }
        }

        holder
                .itemCardView
                .setOnClickListener(view -> {
                    lastPosition = position;
                    notifyDataSetChanged();
                    listener.onAppItemCLickListener(view, item, holder.getAdapterPosition());
                });

    }
}
