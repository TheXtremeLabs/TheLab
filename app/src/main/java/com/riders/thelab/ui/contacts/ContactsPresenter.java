package com.riders.thelab.ui.contacts;

import android.content.Intent;

import com.riders.thelab.data.local.LabRepository;
import com.riders.thelab.data.local.model.Contact;
import com.riders.thelab.ui.base.BasePresenterImpl;

import javax.inject.Inject;


import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class ContactsPresenter extends BasePresenterImpl<ContactsView>
        implements ContactsContract.Presenter {

    @Inject
    ContactsActivity activity;

    @Inject
    Navigator navigator;

    @Inject
    LabRepository repository;

    CompositeDisposable compositeDisposable;

    @Inject
    ContactsPresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getContactList() {
        Timber.d("getContactList()");

        getView().showLoader();

        Disposable disposable =
                repository
                        .getAllContacts()
                        .subscribe(
                                contacts -> {
                                    if (contacts.isEmpty()) {
                                        Timber.e("Contact list is empty");
                                        getView().hideLoader();
                                        getView().hideContactsLayout();
                                        getView().onNoContactRecordFound();
                                    } else {

                                        Timber.d("contacts  : %s", contacts);
                                        getView().hideLoader();
                                        getView().hideNoContactFoundLayout();
                                        getView().showContactsLayout();
                                        getView().onContactsFetchedSuccess(contacts);
                                    }

                                }, throwable -> {
                                    Timber.e(throwable);
                                    getView().hideLoader();
                                    getView().hideContactsLayout();
                                    getView().onContactsFetchedError();
                                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void addNewContact() {
        Timber.d("addNewContact()");
        Timber.d("callAddNewContactActivity()");
        navigator.callAddContactActivity();
    }

    @Override
    public void showDetailContact(Contact contact) {
        Timber.d("showDetailContact()");
        Intent intent = new Intent(activity, ContactDetailActivity.class);

        intent.putExtra(ContactDetailActivity.CONTACT_NAME, contact.getName());
        intent.putExtra(ContactDetailActivity.CONTACT_EMAIL, contact.getEmail());
        intent.putExtra(ContactDetailActivity.CONTACT_IMAGE, "");

        navigator.callContactDetailActivity(intent);
    }

    @Override
    public void detachView() {

        Timber.e("detachView()");

        if (compositeDisposable != null)
            compositeDisposable.dispose();
        super.detachView();
    }
}
