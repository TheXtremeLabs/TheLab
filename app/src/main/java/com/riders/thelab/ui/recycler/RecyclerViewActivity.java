package com.riders.thelab.ui.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.data.local.bean.RecyclerEnum;
import com.riders.thelab.data.local.model.RecyclerItem;
import com.riders.thelab.ui.base.SimpleActivity;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


@SuppressLint("NonConstantResourceId")
public class RecyclerViewActivity extends SimpleActivity
        implements RecyclerClickListener {

    private Context context;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        context = this;

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.activity_title_recycler_view));

        adapter =
                new RecyclerViewAdapter(
                        (ArrayList<RecyclerItem>) RecyclerEnum.getRecyclerItems(),
                        this);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onRecyclerClick(RecyclerItem item) {
        Timber.d("onRecyclerClick()");
        Timber.d(item.toString());
    }

    @Override
    public void onDetailClick(RecyclerItem item, ImageView sharedImageView, int position) {
        Timber.d("onDetailClick(item, sharedImageView, position)");

        Intent intent = new Intent(this, RecyclerViewDetailActivity.class);

        intent.putExtra(RecyclerViewDetailActivity.EXTRA_RECYCLER_ITEM, Parcels.wrap(item));

        // Check if we're running on Android 5.0 or higher
        if (LabCompatibilityManager.isLollipop()) {
            Timber.d("Apply activity transition");

            intent.putExtra(
                    RecyclerViewDetailActivity.EXTRA_TRANSITION_ICON_NAME,
                    ViewCompat.getTransitionName(sharedImageView));

            // Apply activity transition
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                            RecyclerViewActivity.this,
                            sharedImageView,
                            ViewCompat.getTransitionName(sharedImageView));
            startActivity(intent, options.toBundle());
        } else {
            Timber.d("Swap without transition");
            // Swap without transition
            startActivity(intent);
        }
    }


    @Override
    public void onDeleteClick(RecyclerItem item, int position) {
        Timber.d("onDeleteClick() item %s at position : %s", item.getName(), position);

        // get the removed item name to display it in snack bar
        String name = item.getName();

        // backup of removed item for undo purpose
        final RecyclerItem deletedItem = item;
        final int deletedIndex = position;

        // remove the item from recycler view
        adapter.removeItem(position);

        // showing snack bar with Undo option
        Snackbar snackbar =
                Snackbar.make(
                        coordinatorLayout,
                        name + " removed from cart!",
                        BaseTransientBottomBar.LENGTH_LONG);
        snackbar.setAction(
                "UNDO",
                view -> {
                    // undo is selected, restore the deleted item
                    adapter.restoreItem(deletedItem, deletedIndex);
                });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }
}
