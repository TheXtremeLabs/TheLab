package com.riders.thelab.ui.recycler;


import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.RecyclerItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


@SuppressLint("NonConstantResourceId")
public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.row_card_view)
    public CardView cardView;

    @BindView(R.id.transition_imageView)
    ImageView transitionImageView;
    @BindView(R.id.row_name_text_view)
    TextView nameTextView;

    @BindView(R.id.row_details_linear_layout)
    public RelativeLayout detailsLinearLayout;
    @BindView(R.id.row_detail_btn)
    public Button btnDetail;
    @BindView(R.id.row_delete_btn)
    public ImageButton btnDelete;

    private static RecyclerItem itemSelection;
    private static int position;

    public MyRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(RecyclerItem item) {
        nameTextView.setText(item.getName());
    }

    public void storeItem(final RecyclerItem item, int position) {
        Timber.d("storeItem()");
        this.itemSelection = item;
        this.position = position;
    }
}
