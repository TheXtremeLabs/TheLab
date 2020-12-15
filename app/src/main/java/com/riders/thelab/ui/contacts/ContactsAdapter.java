package com.riders.thelab.ui.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.Contact;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsViewHolder>
        implements Filterable {

    private Context context;
    private List<Contact> contactList;
    private List<Contact> contactListFiltered;
    private ContactsClickListener listener;


    public ContactsAdapter(@NonNull Context context,
                           @NonNull ArrayList<Contact> contactList,
                           @NonNull ContactsClickListener listener) {
        this.context = context;
        this.contactList = contactList;
        this.listener = listener;
        this.contactListFiltered = contactList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Timber.d("performFiltering()");
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    Timber.d("charString.isEmpty()()");
                    contactListFiltered = contactList;
                } else {
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact row : contactList) {
                        Timber.d("row : %s", row.toString());

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getEmail().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                    Timber.d("contactListFiltered size : %s", contactListFiltered.size());
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Timber.d("publishResults()");
                contactListFiltered = (ArrayList<Contact>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        if (contactListFiltered != null) {
            return contactListFiltered.size();
        }
        return 0;
    }


    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactsViewHolder(
                context,
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_contact_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        final Contact item = contactListFiltered.get(position);

        holder.bindData(item);
        holder.cardView.setOnClickListener(
                view -> listener.onContactItemCLickListener(item, holder.getAdapterPosition()));
    }


    public void removeItem(int position) {
        contactListFiltered.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Contact item, int position) {
        contactListFiltered.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
