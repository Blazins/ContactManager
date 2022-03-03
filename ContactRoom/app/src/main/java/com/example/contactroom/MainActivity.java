package com.example.contactroom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.contactroom.adapter.RecyclerViewAdapter;
import com.example.contactroom.model.Contact;
import com.example.contactroom.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener {
    public static final String TAG = "Clicked";
    public static final String CONTACT_ID = "contact_id";
    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;
    private TextView textView;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LiveData<List<Contact>> contactList;

    private ContactViewModel contactViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this
        .getApplication())
        .create(ContactViewModel.class);

        //using observable enable instant changing of data in the database once chnge occurs in the UI

        contactViewModel.getAllContacts().observe(this, contacts -> {

            recyclerViewAdapter = new RecyclerViewAdapter(contacts,MainActivity.this,this);
            recyclerView.setAdapter(recyclerViewAdapter);
            /*getAdapterPosition() is a method,
            that is available to us inside the RecyclerView.Adapter,
            so we can retrieve the current row object.*/
//            StringBuilder builder = new StringBuilder();
//            for (Contact contact : contacts) {
//                builder.append(" - ").append(contact.getName()).append(" ").append(contact.getOccupation());
//                Log.d("TAG", "Running " + contact.getName());
//            }

//            textView.setText(builder);
         });



        FloatingActionButton fab = findViewById(R.id.add_contact_fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,NewContact.class);
            startActivityForResult(intent,NEW_CONTACT_ACTIVITY_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){

            assert data != null;
            String name = data.getStringExtra(NewContact.NAME_REPLY);
            String occupation = data.getStringExtra(NewContact.OCCUPATION_REPLY);
            Contact contact = new Contact(name,occupation);


           ContactViewModel.insert(contact);
        }
    }


    @Override
    public void onContactClick(int position) {

        Contact contact = Objects.requireNonNull(contactViewModel.allContacts.getValue()).get(position);
        Log.d("Main","row_textview: "+contact.getName());
        Log.d("Mainpos","Postion_Index "+position);

        Intent intent = new Intent(MainActivity.this, NewContact.class);
        intent.putExtra(CONTACT_ID, contact.getId());
        startActivity(intent);

        //startActivity(new Intent(MainActivity.this,NewContact.class));

    }
}
