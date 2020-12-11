package com.riders.thelab.ui.contacts;

import com.riders.thelab.data.local.LabRepository;
import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BasePresenterImpl;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import timber.log.Timber;

public class ContactsPresenter extends BasePresenterImpl<ContactsView>
        implements ContactsContract.Presenter {

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

        Disposable disposable =
                repository
                        .getAllContacts()
                        .subscribe(
                                contacts -> {
                                    if (contacts.isEmpty()) {
                                        Timber.e("Contact list is empty");
                                        getView().hideLoader();
                                        getView().onContactsFetchedError();
                                    } else {

                                        Timber.d("contacts  : %s", contacts);
                                        getView().hideLoader();
                                        getView().onContactsFetchedSuccess();
                                    }

                                }, throwable -> {
                                    Timber.e(throwable);
                                    getView().hideLoader();
                                    getView().onContactsFetchedError();
                                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void callDetailActivity() {
        Timber.d("callDetailActivity()");
        navigator.callContactDetailActivity();

    }

    @Override
    public void callAddNewContactActivity() {
        Timber.d("callAddNewContactActivity()");

    }

    @Override
    public void addNewContact() {
        Timber.d("addNewContact()");

        callAddNewContactActivity();
    }

    @Override
    public void detachView() {

        Timber.e("detachView()");

        if (compositeDisposable != null)
            compositeDisposable.dispose();
        super.detachView();
    }
}
