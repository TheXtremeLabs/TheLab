package com.riders.thelab.ui.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.R
import com.riders.thelab.data.local.model.Contact
import timber.log.Timber
import java.util.*

class ContactsAdapter(
        val context: Context,
        val contactList: ArrayList<Contact>,
        val listener: ContactsClickListener
) : RecyclerView.Adapter<ContactsViewHolder>(), Filterable {

    private var contactListFiltered: MutableList<Contact>? = contactList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
                context,
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.row_contact_item, parent, false))
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val item = contactListFiltered!![position]

        holder.bindData(item)
        holder.viewBinding.cvContactItem.setOnClickListener { view: View? -> listener.onContactItemCLickListener(item, holder.adapterPosition) }
    }

    override fun getItemCount(): Int {
        return if (contactListFiltered != null) {
            contactListFiltered!!.size
        } else 0
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                Timber.d("performFiltering()")
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    Timber.d("charString.isEmpty()()")
                    contactListFiltered = contactList
                } else {
                    val filteredList: MutableList<Contact> = ArrayList()
                    for (row in contactList) {
                        Timber.d("row : %s", row.toString())

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.name.toLowerCase(Locale.ROOT).contains(charString.toLowerCase(Locale.ROOT))
                                || row.email.contains(charSequence)) {
                            filteredList.add(row)
                        }
                    }
                    contactListFiltered = filteredList
                    Timber.d("contactListFiltered size : %s", contactListFiltered!!.size)
                }
                val filterResults = FilterResults()
                filterResults.values = contactListFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                Timber.d("publishResults()")
                contactListFiltered = filterResults.values as MutableList<Contact>?

                // refresh the list with filtered data
                notifyDataSetChanged()
            }
        }
    }

    fun removeItem(position: Int) {
        contactListFiltered!!.removeAt(position)
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position)
    }

    fun restoreItem(item: Contact, position: Int) {
        contactListFiltered!!.add(position, item)
        // notify item added by position
        notifyItemInserted(position)
    }

}