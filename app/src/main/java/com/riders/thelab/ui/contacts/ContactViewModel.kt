package com.riders.thelab.ui.contacts


import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.data.IRepository
import com.riders.thelab.data.local.model.Contact
import com.riders.thelab.navigator.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    val repositoryImpl: IRepository
) : ViewModel() {

    private var progressVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private var hideContactsLayout: MutableLiveData<Boolean> = MutableLiveData()
    private var showContactsLayout: MutableLiveData<Boolean> = MutableLiveData()
    private var noContactFound: MutableLiveData<List<Contact>> = MutableLiveData()
    private var contacts: MutableLiveData<List<Contact>> = MutableLiveData()
    private var contactsFailed: MutableLiveData<Boolean> = MutableLiveData()


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

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val dbContacts = repositoryImpl.getAllContacts()
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
            } catch (throwable: Exception) {
                Timber.e(throwable)
                progressVisibility.value = false
                hideContactsLayout.value = true
                contactsFailed.value = true
            }
        }
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
}