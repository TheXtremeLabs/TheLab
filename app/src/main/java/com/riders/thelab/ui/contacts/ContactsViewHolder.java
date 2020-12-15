package com.riders.thelab.ui.contacts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.Contact;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class ContactsViewHolder extends RecyclerView.ViewHolder {

    private Context context;

    @BindView(R.id.cv_contact_item)
    public CardView cardView;
    @BindView(R.id.tv_contact_name)
    TextView tvContactName;
    @BindView(R.id.tv_contact_mail)
    TextView tvContactEmail;
    @BindView(R.id.iv_contact_thumbnail)
    ImageView ivContactThumbnail;

    @BindView(R.id.view_background)
    public RelativeLayout viewBackground;
    @BindView(R.id.view_foreground)
    public RelativeLayout viewForeground;

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
