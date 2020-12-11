package com.riders.thelab.ui.contacts;

import android.annotation.SuppressLint;

import com.riders.thelab.ui.base.BaseViewImpl;

import javax.inject.Inject;

@SuppressLint("NonConstantResourceId")
public class ContactsView extends BaseViewImpl<ContactsPresenter>
        implements ContactsContract.View {

    private ContactsActivity context;

    @Inject
    ContactsView(ContactsActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }
}
