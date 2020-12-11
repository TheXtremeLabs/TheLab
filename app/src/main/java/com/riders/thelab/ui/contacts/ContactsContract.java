package com.riders.thelab.ui.contacts;

import com.riders.thelab.ui.base.BaseView;

public interface ContactsContract {

    interface View extends BaseView {

        void showLoader();

        void hideLoader();

        void onContactsFetchedSuccess();

        void onContactsFetchedError();

        void onNoContactRecordFound();
    }

    interface Presenter {

        void getContactList();

        void callDetailActivity();

        void callAddNewContactActivity();

        void addNewContact();
    }
}
