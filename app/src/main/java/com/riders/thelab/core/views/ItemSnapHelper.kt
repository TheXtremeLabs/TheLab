package com.riders.thelab.core.views;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemSnapHelper extends LinearSnapHelper {


    private static final float MILLISECONDS_PER_INCH = 100f;
    private static final int MAX_SCROLL_ON_FLING_DURATION_MS = 1000;

    private Context context;

    private OrientationHelper helper;
    private Scroller scroller = null;
    private int maxScrollDistance;

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {

        if (recyclerView != null) {
            context = recyclerView.getContext();
            scroller = new Scroller(context, new DecelerateInterpolator());
        } else {
            scroller = null;
            context = null;
        }

        super.attachToRecyclerView(recyclerView);
    }

    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        return findFirstView(layoutManager, helper(layoutManager));
    }

    private View findFirstView(RecyclerView.LayoutManager layoutManager, OrientationHelper helper) {
        if (layoutManager == null) return null;

        int childCount = layoutManager.getChildCount();
        if (childCount == 0) return null;

        int absClosest = Integer.MAX_VALUE;
        View closestView = null;
        int start = helper.getStartAfterPadding();

        for (int i = 0; i < childCount; i++) {
            View child = layoutManager.getChildAt(i);
            int childStart = helper.getDecoratedStart(child);
            int absDistanceToStart = Math.abs(childStart - start);
            if (absDistanceToStart < absClosest) {
                absClosest = absDistanceToStart;
                closestView = child;
            }

        }

        return closestView;
    }

    private OrientationHelper helper(RecyclerView.LayoutManager layoutManager) {
        if (helper == null) {
            helper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return helper;
    }

    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {

        int[] out = new int[2];
        out[0] = distanceToStart(targetView, helper(layoutManager));
        return out;
    }

    @Override
    public int[] calculateScrollDistance(int velocityX, int velocityY) {
        int[] out = new int[2];

        if (null == helper) return out;

        if (maxScrollDistance == 0) {
            maxScrollDistance = (helper.getEndAfterPadding() - helper.getStartAfterPadding()) / 2;
        }

        scroller.fling(0, 0, velocityX, velocityY, -maxScrollDistance, maxScrollDistance, 0, 0);
        out[0] = scroller.getFinalX();
        out[1] = scroller.getFinalX();
        return out;
    }

    @Nullable
    @Override
    protected RecyclerView.SmoothScroller createScroller(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)
            return super.createScroller(layoutManager);

        if (null == context) return null;

        return new LinearSmoothScroller(context) {

            @Override
            protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
                int[] snapDistance = calculateDistanceToFinalSnap(layoutManager, targetView);
                int dx = snapDistance[0];
                int dy = snapDistance[1];
                int dt = calculateTimeForDeceleration(Math.abs(dx));
                int time = Math.max(1, Math.min(MAX_SCROLL_ON_FLING_DURATION_MS, dt));
                action.update(dx, dy, time, mDecelerateInterpolator);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }
        };
    }

    private int distanceToStart(View targetView, OrientationHelper helper) {
        int childStart = helper.getDecoratedStart(targetView);
        int containerStart = helper.getStartAfterPadding();
        return childStart - containerStart;
    }
}
