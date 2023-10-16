package com.riders.thelab.ui.contacts.addcontact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.Contact
import com.riders.thelab.navigator.Navigator
import com.riders.thelab.ui.contacts.ContactsActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor(
    private val repositoryImpl: IRepository
) : ViewModel() {

    private val addContact: MutableLiveData<Boolean> = MutableLiveData()

    fun getAddedContact(): LiveData<Boolean> {
        return addContact
    }

    fun saveContact(contactToSave: Contact) {
        viewModelScope.launch {
            try {
                val job = repositoryImpl.insertContactRX(contactToSave)
                Timber.e("Contact %s successfully added to the database", job.toString())
                addContact.value = true

            } catch (throwable: Exception) {
                Timber.e("Error occurred while adding the contact in the database")
                Timber.e(throwable)
                addContact.value = false
            }
        }
    }

    fun goToContactActivity(activity: AddContactActivity, navigator: Navigator) {
        navigator.callIntentActivity(ContactsActivity::class.java)
        activity.finish()
    }
}