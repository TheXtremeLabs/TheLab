package com.riders.thelab.ui.contacts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.riders.thelab.R;
import com.riders.thelab.ui.base.SimpleActivity;

import butterknife.BindView;

@SuppressLint("NonConstantResourceId")
public class ContactDetailActivity extends SimpleActivity {

    private Context mContext;

    @BindView(R.id.contact_detail_collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.iv_image_detail_toolbar)
    ImageView mImageDetailToolbar;
    @BindView(R.id.tv_name_detail)
    TextView mNameTextView;
    @BindView(R.id.tv_surname_detail)
    TextView mSurnameTextView;
    @BindView(R.id.tv_email_detail)
    TextView mEmailTextView;

    public static final String CONTACT_NAME = "contact_name";
    public static final String CONTACT_SURNAME = "contact_surname";
    public static final String CONTACT_EMAIL = "contact_email";
    public static final String CONTACT_IMAGE = "contact_image";

    String itemNameDetail;
    String itemSurnameDetail;
    String itemEmailDetail;
    String itemImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detail);
    }
}
