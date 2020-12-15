package com.riders.thelab.ui.contacts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.riders.thelab.R;
import com.riders.thelab.ui.base.SimpleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class ContactDetailActivity extends SimpleActivity {

    private Context mContext;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.contact_detail_collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.contact_detail_toolbar)
    Toolbar toolbar;
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

        mContext = this;

        ButterKnife.bind(this);

        getBundle();

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), String.valueOf(R.drawable.image5));
        supportPostponeEnterTransition();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mCollapsingToolbar.setTitle(itemNameDetail);
        mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
//        getSupportActionBar().setTitle(itemNameDetail);

        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mCollapsingToolbar.setTitle(itemNameDetail);
                    isShow = true;
                } else if (isShow) {
                    mCollapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();

        if (null == bundle) {
            Timber.e("bundle is null");
            return;
        }

        itemNameDetail = bundle.getString(CONTACT_NAME);
        itemEmailDetail = bundle.getString(CONTACT_EMAIL);
        itemImage = bundle.getString(CONTACT_IMAGE);

        setViews();
    }

    private void setViews() {
        mNameTextView.setText(itemNameDetail);
        mEmailTextView.setText(itemEmailDetail);
    }
}
