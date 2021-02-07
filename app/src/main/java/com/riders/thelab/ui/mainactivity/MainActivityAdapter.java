package com.riders.thelab.ui.mainactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.App;

import java.util.List;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityViewHolder> {

    private Context context;
    private List<App> appList;
    private MainActivityAppClickListener listener;

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

        /*
         *
         * Reference : https://levelup.gitconnected.com/android-recyclerview-animations-in-kotlin-1e323ffd39be
         *
         * */
        /*ObjectAnimator animator =
                ObjectAnimator.ofObject(
                        holder.itemCardView,
                        "alpha",
                        new FloatEvaluator(),
                        0f, // Beginning color
                        1f); // Target Color
        animator.setDuration(800);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();*/

        holder.bindData(item);
        holder.itemCardView.setOnClickListener(
                view -> listener.onAppItemCLickListener(view, item, holder.getAdapterPosition()));

        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            holder.itemCardView.startAnimation(animation);
            lastPosition = position;
        }/*
        holder.itemCardView.animate()
                .setDuration(800)
                .translationY(0f)
                .translationYBy(1f)
                .scaleX(0)
                .scaleXBy(1)
                .scaleY(0)
                .scaleYBy(1)
                .alpha(0f)
                .alphaBy(1f)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(null)
                .start();*/
    }
}
