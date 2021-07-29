package com.riders.thelab.ui.contacts.addcontact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riders.thelab.data.RepositoryImpl
import com.riders.thelab.data.local.model.Contact
import com.riders.thelab.navigator.Navigator
import com.riders.thelab.ui.contacts.ContactsActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor(
    val repositoryImpl: RepositoryImpl
) : ViewModel() {

    private val addContact: MutableLiveData<Boolean> = MutableLiveData()

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun getAddedContact(): LiveData<Boolean> {
        return addContact
    }

    fun saveContact(contactToSave: Contact) {
        val disposable: Disposable =
            repositoryImpl
                .insertContactRX(contactToSave)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { contact ->
                        Timber.e(
                            "Contact %s successfully added to the database",
                            contact.toString()
                        )
                        addContact.value = true
                    },
                    { throwable ->
                        Timber.e("Error occurred while adding the contact in the database")
                        Timber.e(throwable)
                        addContact.value = false
                    })

        compositeDisposable.add(disposable)
    }

    fun goToContactActivity(activity: AddContactActivity, navigator: Navigator) {
        navigator.callIntentActivity(ContactsActivity::class.java)
        activity.finish()
    }


    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}