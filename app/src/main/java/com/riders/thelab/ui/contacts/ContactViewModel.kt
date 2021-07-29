package com.riders.thelab.ui.contacts

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riders.thelab.data.RepositoryImpl
import com.riders.thelab.data.local.model.Contact
import com.riders.thelab.navigator.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    val repositoryImpl: RepositoryImpl
) : ViewModel() {

    private var progressVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private var hideContactsLayout: MutableLiveData<Boolean> = MutableLiveData()
    private var showContactsLayout: MutableLiveData<Boolean> = MutableLiveData()
    private var noContactFound: MutableLiveData<List<Contact>> = MutableLiveData()
    private var contacts: MutableLiveData<List<Contact>> = MutableLiveData()
    private var contactsFailed: MutableLiveData<Boolean> = MutableLiveData()

    val compositeDisposable: CompositeDisposable = CompositeDisposable()


    fun getProgressBarVisibility(): LiveData<Boolean> {
        return progressVisibility
    }

    fun hideContactsLayout(): LiveData<Boolean> {
        return hideContactsLayout
    }

    fun showContactsLayout(): LiveData<Boolean> {
        return showContactsLayout
    }

    fun getNoContactFound(): LiveData<List<Contact>> {
        return noContactFound
    }

    fun getContacts(): LiveData<List<Contact>> {
        return contacts
    }

    fun getContactsFailed(): LiveData<Boolean> {
        return contactsFailed
    }


    fun fetchContacts() {
        Timber.d("getContactList()")
        progressVisibility.value = true

        val disposable: Disposable =
            repositoryImpl
                .getAllContacts()
                .subscribe(
                    { dbContacts ->
                        if (dbContacts.isEmpty()) {
                            Timber.e("Contact list is empty")
                            progressVisibility.value = false
                            hideContactsLayout.value = true
                            noContactFound.value = dbContacts
                        } else {
                            Timber.d("contacts  : %s", contacts)
                            progressVisibility.value = false
                            showContactsLayout.value = true
                            showContactsLayout.value = true
                            contacts.value = dbContacts
                        }
                    },
                    { throwable ->
                        Timber.e(throwable)
                        progressVisibility.value = false
                        hideContactsLayout.value = true
                        contactsFailed.value = true
                    })
        compositeDisposable.add(disposable)
    }

    fun addNewContact(navigator: Navigator) {
        Timber.d("addNewContact()")
        navigator.callAddContactActivity()
    }

    fun showDetailContact(activity: ContactsActivity, navigator: Navigator, contact: Contact) {
        Timber.d("showDetailContact()")
        val intent = Intent(activity, ContactDetailActivity::class.java)
        intent.putExtra(ContactDetailActivity.CONTACT_NAME, contact.name)
        intent.putExtra(ContactDetailActivity.CONTACT_EMAIL, contact.email)
        intent.putExtra(ContactDetailActivity.CONTACT_IMAGE, "")
        navigator.callContactDetailActivity(intent)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}