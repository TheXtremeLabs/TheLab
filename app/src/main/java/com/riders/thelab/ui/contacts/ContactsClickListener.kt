package com.riders.thelab.ui.contacts

import com.riders.thelab.data.local.model.Contact

interface ContactsClickListener {
    fun onContactItemCLickListener(item: Contact, position: Int)
}