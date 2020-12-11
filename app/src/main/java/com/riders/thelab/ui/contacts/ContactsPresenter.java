package com.riders.thelab.ui.contacts;

import com.riders.thelab.ui.base.BasePresenterImpl;

import javax.inject.Inject;

public class ContactsPresenter extends BasePresenterImpl<ContactsView>
        implements ContactsContract.Presenter {

    @Inject
    ContactsPresenter() {
    }
}
