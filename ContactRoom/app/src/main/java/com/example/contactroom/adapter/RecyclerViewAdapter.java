package com.example.contactroom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactroom.R;
import com.example.contactroom.model.Contact;

import java.util.List;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private OnContactClickListener contactClickListener;
    //know size of data being looked at -data comming in as contactViewmodel
    private List<Contact> contactList;
    private Context context;

    public RecyclerViewAdapter(List<Contact> contactList, Context context, OnContactClickListener onContactClickListener) {
        this.contactList = contactList;
        this.context = context;
        this.contactClickListener = onContactClickListener;
    }

    @NonNull
    @Override
    //The onCreateViewHolder method returns the inflated view which represents a single row for the recyclerview.
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Create a view then use layoutInflater then get contact_row and make it into a contact object
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row,parent,false);
        //View is a row in the recycler view
        return new ViewHolder(view, contactClickListener);
    }
    //return a view
    //This class is used in onCreateViewHolder to inflate, parse, the xml file that is responsible for laying out the user interface and widgets for the row.

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //object we are getting
        Contact contact = Objects.requireNonNull(contactList.get(position));
        holder.name.setText(contact.getName());
        holder.occupation.setText(contact.getOccupation());
        //bind widgets to data
    }

    @Override
    public int getItemCount() {
        //to know size of data to expect
        return Objects.requireNonNull(contactList).size();
    }

    //ViewHolder class enable us to recycle views
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       //intstance of interface
        OnContactClickListener onContactClickListener;
        public TextView name;
        public  TextView occupation;
        public ViewHolder(@NonNull View itemView, OnContactClickListener onContactClickListener)  {
            super(itemView);
            //We can use findViewbyID since we are not in an activity
            name = itemView.findViewById(R.id.row_text_view);
            occupation = itemView.findViewById(R.id.row_occupation_textview);
            this.onContactClickListener = onContactClickListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
           onContactClickListener.onContactClick(getAdapterPosition());
        }
    }

    public interface OnContactClickListener {
        void onContactClick(int position);
    }

}
