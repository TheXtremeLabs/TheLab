package com.riders.thelab.ui.contacts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.data.local.model.Contact;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class ContactsViewHolder extends RecyclerView.ViewHolder {

    private final Context context;

    @BindView(R.id.cv_contact_item)
    public MaterialCardView cardView;
    @BindView(R.id.view_background)
    public RelativeLayout viewBackground;
    @BindView(R.id.view_foreground)
    public RelativeLayout viewForeground;
    @BindView(R.id.tv_contact_name)
    MaterialTextView tvContactName;
    @BindView(R.id.tv_contact_mail)
    MaterialTextView tvContactEmail;
    @BindView(R.id.iv_contact_thumbnail)
    ShapeableImageView ivContactThumbnail;

    public ContactsViewHolder(@NonNull Context context, @NonNull View itemView) {
        super(itemView);

        this.context = context;

        ButterKnife.bind(this, itemView);
    }

    public void bindData(Contact contact) {
        tvContactName.setText(contact.getName());
        tvContactEmail.setText(contact.getEmail());
    }
}
