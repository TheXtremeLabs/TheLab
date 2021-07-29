package com.riders.thelab.ui.contacts

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.data.local.model.Contact
import com.riders.thelab.databinding.RowContactItemBinding

class ContactsViewHolder(context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {

    var viewBinding: RowContactItemBinding = RowContactItemBinding.bind(itemView)

    fun bindData(contact: Contact) {
        viewBinding.tvContactName.text = contact.name
        viewBinding.tvContactMail.text = contact.email
    }
}