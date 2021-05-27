package com.riders.thelab.ui.contacts

import android.app.SearchManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.R
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.local.bean.SnackBarType
import com.riders.thelab.data.local.model.Contact
import com.riders.thelab.databinding.ActivityContactsBinding
import com.riders.thelab.navigator.Navigator
import com.riders.thelab.ui.contacts.RecyclerItemTouchHelper.RecyclerItemTouchHelperListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class ContactsActivity : AppCompatActivity(), ContactsClickListener,
    RecyclerItemTouchHelperListener {

    lateinit var viewBinding: ActivityContactsBinding
    private var searchView: SearchView? = null

    private var mAdapter: ContactsAdapter? = null

    private val mContactViewModel: ContactViewModel by viewModels()


    private var contactList: List<Contact>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setSupportActionBar(viewBinding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        supportActionBar?.title = getString(R.string.activity_title_database_contacts)

        mContactViewModel
            .getProgressBarVisibility()
            .observe(this, {})

        mContactViewModel
            .getNoContactFound()
            .observe(this, {
                viewBinding.noContactFoundLayoutContainer.layoutNoContactsFound.visibility =
                    View.VISIBLE
            })

        mContactViewModel
            .getContactsFailed()
            .observe(this, {
                Timber.d("onContactsFetchedError()")
            })

        mContactViewModel
            .getContacts()
            .observe(this, { contacts ->

                contactList = contacts

                mAdapter = ContactsAdapter(
                    this@ContactsActivity,
                    contactList as ArrayList<Contact>,
                    this
                )

                val linearLayoutManager = LinearLayoutManager(this@ContactsActivity)
                viewBinding.contactsLayoutContainer.rvContacts.layoutManager = linearLayoutManager
                viewBinding.contactsLayoutContainer.rvContacts.itemAnimator = DefaultItemAnimator()
                viewBinding.contactsLayoutContainer.rvContacts.adapter = mAdapter

                // adding item touch helper
                // only ItemTouchHelper.LEFT added to detect Right to Left swipe
                // if you want both Right -> Left and Left -> Right
                // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
                val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
                    RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
                ItemTouchHelper(itemTouchHelperCallback)
                    .attachToRecyclerView(viewBinding.contactsLayoutContainer.rvContacts)
            })

        viewBinding.noContactFoundLayoutContainer.btnAddNewContact.setOnClickListener {
            Navigator(this).callAddContactActivity()
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mContactViewModel.fetchContacts()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contacts_database, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = this.getSystemService(SEARCH_SERVICE) as SearchManager

        searchView = menu!!.findItem(R.id.action_search).actionView as SearchView
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView!!.maxWidth = Int.MAX_VALUE

        // listening to search query text change
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                mAdapter!!.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                mAdapter!!.filter.filter(query)
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_contact -> mContactViewModel.addNewContact(Navigator(this))
            R.id.action_search -> Timber.d("noinspection SimplifiableIfStatement")
            R.id.action_supervisor ->
                UIManager.showActionInToast(this, "Action supervisor clicked")
            else -> {
                return false
            }
        }

        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()

        // close search view on back button pressed
        if (!searchView?.isIconified!!) {
            searchView?.isIconified = true
            return
        }
    }


    override fun onContactItemCLickListener(item: Contact, position: Int) {
        Timber.d("contact %s clicked", item.toString())
        mContactViewModel.showDetailContact(this, Navigator(this), item)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {

        if (viewHolder is ContactsViewHolder) {
            // get the removed item name to display it in snack bar
            val name: String? = contactList?.get(viewHolder.getAdapterPosition())?.name

            // backup of removed item for undo purpose
            val deletedItem: Contact? = contactList?.get(viewHolder.getAdapterPosition())
            val deletedIndex = viewHolder.getAdapterPosition()

            // remove the item from recycler view
            mAdapter!!.removeItem(viewHolder.getAdapterPosition())

            // showing snack bar with Undo option
            UIManager.showActionInSnackBar(
                this,
                viewBinding.coordinator,
                "$name removed from cart!",
                SnackBarType.WARNING,
                "UNDO"
            ) {
                // undo is selected, restore the deleted item
                if (deletedItem != null) {
                    mAdapter!!.restoreItem(deletedItem, deletedIndex)
                }
            }
        }
    }


}