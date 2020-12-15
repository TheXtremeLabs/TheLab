package com.riders.thelab.ui.contacts.addcontact;

import com.riders.thelab.data.local.model.Contact;
import com.riders.thelab.ui.base.BaseView;

public interface AddContactContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onAddContactSuccess();

        void onAddContactError();

        /**
         * Gather all data entered by the user in order to  save them in database
         *
         * @return
         */
        Contact getEnteredFormData();
    }

    interface Presenter {

        void saveContact();

        void goToContactActivity();
    }
}
