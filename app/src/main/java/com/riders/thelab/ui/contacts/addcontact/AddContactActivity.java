package com.riders.thelab.ui.contacts.addcontact;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.riders.thelab.R;

public class AddContactActivity extends BaseActivity<AddContactView> {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_contact);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}
