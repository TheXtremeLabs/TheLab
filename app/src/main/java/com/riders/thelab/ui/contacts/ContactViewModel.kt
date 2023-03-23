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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val repositoryImpl: IRepository
) : ViewModel() {

    private val progressVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val hideContactsLayout: MutableLiveData<Boolean> = MutableLiveData()
    private val showContactsLayout: MutableLiveData<Boolean> = MutableLiveData()
    private val noContactFound: MutableLiveData<List<Contact>> = MutableLiveData()
    private val contacts: MutableLiveData<List<Contact>> = MutableLiveData()
    private val contactsFailed: MutableLiveData<Boolean> = MutableLiveData()


    ///////////////
    //
    // Observers
    //
    ///////////////
    fun getProgressBarVisibility(): LiveData<Boolean> = progressVisibility
    fun hideContactsLayout(): LiveData<Boolean> = hideContactsLayout
    fun showContactsLayout(): LiveData<Boolean> = showContactsLayout
    fun getNoContactFound(): LiveData<List<Contact>> = noContactFound
    fun getContacts(): LiveData<List<Contact>> = contacts
    fun getContactsFailed(): LiveData<Boolean> = contactsFailed


    ///////////////
    //
    // Functions
    //
    ///////////////
    fun fetchContacts() {
        Timber.d("getContactList()")
        progressVisibility.value = true

        viewModelScope.launch(ioContext) {
            try {
                val dbContacts = repositoryImpl.getAllContacts()
                if (dbContacts.isEmpty()) {
                    Timber.e("Contact list is empty")

                    withContext(mainContext) {
                        progressVisibility.value = false
                        hideContactsLayout.value = true
                        noContactFound.value = dbContacts
                    }

                } else {
                    Timber.d("contacts  : %s", contacts)

                    withContext(mainContext) {
                        progressVisibility.value = false
                        showContactsLayout.value = true
                        showContactsLayout.value = true
                        contacts.value = dbContacts
                    }
                }
            } catch (throwable: Exception) {
                Timber.e(throwable)

                withContext(mainContext) {
                    progressVisibility.value = false
                    hideContactsLayout.value = true
                    contactsFailed.value = true
                }
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


    companion object {
        val ioContext = Dispatchers.IO + Job()
        val mainContext = Dispatchers.Main + Job()
    }
}