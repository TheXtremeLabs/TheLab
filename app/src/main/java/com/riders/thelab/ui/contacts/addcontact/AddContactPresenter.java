package com.riders.thelab.ui.contacts.addcontact;

import com.riders.thelab.data.local.LabRepository;
import com.riders.thelab.data.local.model.Contact;
import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BasePresenterImpl;
import com.riders.thelab.ui.contacts.ContactsActivity;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class AddContactPresenter extends BasePresenterImpl<AddContactView>
        implements AddContactContract.Presenter {

    @Inject
    AddContactActivity activity;

    @Inject
    Navigator navigator;

    @Inject
    LabRepository repository;

    CompositeDisposable compositeDisposable;

    @Inject
    AddContactPresenter() {
        compositeDisposable = new CompositeDisposable();
    }


    @Override
    public void saveContact() {
        Timber.e("saveContact()");

        Contact contactToSave = getView().getEnteredFormData();

        Disposable disposable =
                repository
                        .insertContactRX(contactToSave)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                contact -> {
                                    Timber.e("Contact %s successfully added to the database", contact.toString());
                                    getView().onAddContactSuccess();

                                }, throwable -> {
                                    Timber.e("Error occurred while adding the contact in the database");
                                    Timber.e(throwable);
                                    getView().onAddContactError();
                                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void goToContactActivity() {
        navigator.callIntentActivity(ContactsActivity.class);
        activity.finish();
    }

    @Override
    public void detachView() {

        Timber.e("detachView()");

        if (compositeDisposable != null)
            compositeDisposable.dispose();
        super.detachView();
    }
}
