package com.riders.thelab.ui.base;

import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SimpleActivity extends AppCompatActivity {

    public void setupToolBar(Toolbar toolbar, final String title, final String subTitle, final boolean showButtonHome) {
        // Set up the layout_toolbar.
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(showButtonHome);
            ab.setDisplayShowHomeEnabled(showButtonHome);
            ab.setTitle(title);
            if (subTitle != null) ab.setSubtitle(subTitle);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
