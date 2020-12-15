package com.riders.thelab.ui.contacts;

import android.view.Menu;
import android.view.MenuItem;

import com.riders.thelab.data.local.model.Contact;
import com.riders.thelab.ui.base.BaseView;

import java.util.List;

public interface ContactsContract {

    interface View extends BaseView {

        void onCreateOptionsMenu(Menu menu);

        void onOptionsItemSelected(MenuItem item);

        void onBackPressed();

        void showLoader();

        void hideLoader();

        void showContactsLayout();

        void hideContactsLayout();

        void showNoContactFoundLayout();

        void hideNoContactFoundLayout();

        void onContactsFetchedSuccess(List<Contact> contactList);

        void onContactsFetchedError();

        void onNoContactRecordFound();
    }

    interface Presenter {

        void getContactList();

        void addNewContact();

        void showDetailContact(Contact contact);
    }
}
