package com.riders.thelab.ui.contacts;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.riders.thelab.R;
import com.riders.thelab.ui.base.BaseViewImpl;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

@SuppressLint("NonConstantResourceId")
public class ContactsView extends BaseViewImpl<ContactsPresenter>
        implements ContactsContract.View {

    private ContactsActivity context;

    @BindView(R.id.btn_add_new_contact)
    Button btnAddNewContact;


    @Inject
    ContactsView(ContactsActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        Timber.d("onCreate()");
        getPresenter().attachView(this);
        ButterKnife.bind(this, context.findViewById(android.R.id.content));
        getPresenter().getContactList();
    }

    @Override
    public void onDestroy() {
        Timber.d("onDestroy()");
        getPresenter().detachView();
        context = null;
    }

    @OnClick(R.id.btn_add_new_contact)
    void onAddContactButtonClicked() {
        getPresenter().addNewContact();
    }


    @Override
    public void showLoader() {
        Timber.d("showLoader()");


    }

    @Override
    public void hideLoader() {
        Timber.d("hideLoader()");


    }

    @Override
    public void onContactsFetchedSuccess() {
        Timber.d("onContactsFetchedSuccess()");

        View.inflate(context, R.layout.content_contacts, context.findViewById(R.id.contacts_layout_container));
    }

    @Override
    public void onContactsFetchedError() {
        Timber.d("onContactsFetchedError()");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        /*View childLayout =
                inflater.inflate(R.layout.child, (ViewGroup) findViewById(R.id.child_id));
        parentLayout.addView(childLayout);*/

        View.inflate(context, R.layout.content_no_contact_found, context.findViewById(R.id.contacts_layout_container));
    }

    @Override
    public void onNoContactRecordFound() {
        Timber.d("onNoContactRecordFound()");

    }
}
