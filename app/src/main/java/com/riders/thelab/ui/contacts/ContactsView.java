package com.riders.thelab.ui.contacts;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.riders.thelab.R;
import com.riders.thelab.data.local.model.Contact;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class ContactsView extends BaseViewImpl<ContactsPresenter>
        implements ContactsContract.View, ContactsClickListener,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.no_contact_found_layout_container)
    View layoutNoContactFound;
    @BindView(R.id.contacts_layout_container)
    View layoutContacts;
    private ContactsActivity context;
    private SearchView searchView;

    private List<Contact> contactList;
    private ContactsAdapter mAdapter;

    @Inject
    ContactsView(ContactsActivity context) {
        this.context = context;
    }


    @Override
    public void onCreate() {
        Timber.d("onCreate()");
        getPresenter().attachView(this);


        ButterKnife.bind(this, context.findViewById(android.R.id.content));
        ButterKnife.setDebug(true);

        context.setSupportActionBar(toolbar);

        context.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context.getSupportActionBar().setDisplayShowHomeEnabled(true);

        context.getSupportActionBar().setTitle(context.getString(R.string.activity_title_database_contacts));
    }


    @Override
    public void onStart() {
        Timber.e("onStart()");
    }

    @Override
    public void onPause() {
        Timber.e("onPause()");

    }

    @Override
    public void onResume() {
        Timber.d("onResume()");

        getPresenter().getContactList();
    }

    @Override
    public void onDestroy() {
        Timber.d("onDestroy()");

        getPresenter().detachView();
        context = null;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu) {
        context.getMenuInflater()
                .inflate(R.menu.menu_contacts_database, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(context.getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    public void onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_contact:
                getPresenter().addNewContact();
                break;

            //noinspection SimplifiableIfStatement
            case R.id.action_search:
                Timber.d("noinspection SimplifiableIfStatement");
                break;


            case R.id.action_supervisor:
                UIManager.showActionInToast(context, "Action supervisor clicked");
//                if (null != contactList && !contactList.isEmpty()) {
//                    BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance(contactList.get(0));
//                    bottomSheetFragment.show(
//                            context.getSupportFragmentManager(),
//                            bottomSheetFragment.getTag());
//                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
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
    public void showContactsLayout() {
        layoutContacts.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContactsLayout() {
        layoutContacts.setVisibility(View.GONE);
    }

    @Override
    public void showNoContactFoundLayout() {
        layoutNoContactFound.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoContactFoundLayout() {
        layoutNoContactFound.setVisibility(View.GONE);
    }

    @Override
    public void onContactsFetchedSuccess(List<Contact> contactList) {
        Timber.d("onContactsFetchedSuccess()");

        ButterKnife.bind(context);

        // 4. Then, we create objects of the type of the IncludedLayout.
        //      In this example the layout reuse the same layout twice, so, there are two
        //      IncludedLayouts.
        ContactsLayout layout = new ContactsLayout();
        // 5. We bind the elements of the included layouts.
        ButterKnife.bind(layout, layoutContacts);

        RecyclerView recyclerView = layout.rvContacts;

        this.contactList = contactList;

        mAdapter = new ContactsAdapter(context, (ArrayList<Contact>) this.contactList, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }

    @Override
    public void onContactsFetchedError() {
        Timber.d("onContactsFetchedError()");
    }

    @Override
    public void onNoContactRecordFound() {
        Timber.d("onNoContactRecordFound()");

        ButterKnife.bind(context);

        // 4. Then, we create objects of the type of the IncludedLayout.
        //      In this example the layout reuse the same layout twice, so, there are two
        //      IncludedLayouts.
        NoContactIncludedLayout layout = new NoContactIncludedLayout();
        // 5. We bind the elements of the included layouts.
        ButterKnife.bind(layout, layoutNoContactFound);

        layout.btnAddNewContact.setOnClickListener(v -> {
            Timber.e("setOnClickListener()");
            getPresenter().addNewContact();
        });
    }

    @Override
    public void onContactItemCLickListener(Contact item, int position) {
        Timber.d("contact %s clicked", item.toString());
        getPresenter().showDetailContact(item);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ContactsViewHolder) {
            // get the removed item name to display it in snack bar
            String name = contactList.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final Contact deletedItem = contactList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    // 3. We create a static class that will be an container of the elements
    //     of the included layout. In here we declare the components that
    //     hold this. In this example, there is only one TextView.
    static class NoContactIncludedLayout {
        @BindView(R.id.btn_add_new_contact)
        MaterialButton btnAddNewContact;

    }

    static class ContactsLayout {
        @BindView(R.id.rv_contacts)
        RecyclerView rvContacts;
    }
}
