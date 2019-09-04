package com.example.shiva.try1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 16/11/17.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    public List<Contact> contactList;
    private List<Contact> contactListFiltered;
    private ContactsAdapterListener listener;
 int pos;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone;
        public ImageView thumbnail,overflow;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            phone = view.findViewById(R.id.phone);
            thumbnail = view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                   // pos = listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Contact contact = contactListFiltered.get(position);
        holder.name.setText(contact.getName());
        holder.phone.setText(contact.getPhone());
        Glide.with(context).load(contact.getImage()).into(holder.thumbnail);
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mobil,college;
                mobil= contact.getPhone();
                college = contact.getCollege();
                PopupMenu popup = new PopupMenu(context, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_album, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit_action:
                                //contactList.
                                // final Contact contact = contactListFiltered.get(position);
                                Bundle basket_edit= new Bundle();
                                basket_edit.putString("searchValue_edit", mobil);
                                basket_edit.putString("college",college);
                                Intent edit=new Intent(context,edit_form.class);
                                edit.putExtras(basket_edit);
                                context.startActivity(edit);
                                Toast.makeText(context, "Edit Profile", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.profile_action:
                                Bundle basket_view= new Bundle();
                                basket_view.putString("college",college);
                                basket_view.putString("searchValue", mobil);
                                Intent view_=new Intent(context,profile.class);
                                view_.putExtras(basket_view);
                                context.startActivity(view_);
                                Toast.makeText(context, "View Profile", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }
    private void showPopupMenu(View view) {
        // inflate menu

    }
//    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
//
//        public MyMenuItemClickListener() {
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//          // int pos = contactList.get(getAdapterPosition());
//            switch (menuItem.getItemId()) {
//                case R.id.edit_action:
//                    //contactList.
//                   // final Contact contact = contactListFiltered.get(position);
//
//                    Toast.makeText(context, "Edit Profile", Toast.LENGTH_SHORT).show();
//                    return true;
//                case R.id.profile_action:
//                    Toast.makeText(context, "View Profile", Toast.LENGTH_SHORT).show();
//                    return true;
//                default:
//            }
//            return false;
//        }
//    }


    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<Contact> filteredList = new ArrayList<>();

                    for (Contact row : contactList) {
                       // filteredList=null;
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Contact contact);

        boolean onCreateOptionsMenu(Menu menu);
    }
}
