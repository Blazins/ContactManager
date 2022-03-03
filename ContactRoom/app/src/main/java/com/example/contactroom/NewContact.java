package com.example.contactroom;

import static com.example.contactroom.R.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contactroom.model.Contact;
import com.example.contactroom.model.ContactViewModel;
import com.google.android.material.snackbar.Snackbar;

public class NewContact extends AppCompatActivity {
    public static final String NAME_REPLY = "name_reply";
    public static final String OCCUPATION_REPLY = "occupation_reply";
    private EditText enterName;
    private EditText enterOccupation;
    private Button saveButton;
    private ContactViewModel contactViewModel;
    private int contactID =0;
    private Boolean isEdit = false;
    private Button updateButton;
    private Button deleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_new_contact);

        enterName = findViewById(R.id.enter_name);
        enterOccupation = findViewById(R.id.enter_occupation);
        saveButton = findViewById(R.id.save_button);
        updateButton = findViewById(id.update_button);
        deleteButton = findViewById(id.delete_button);

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(NewContact.this
                .getApplication())
                .create(ContactViewModel.class);


        Bundle data = getIntent().getExtras();
        if(getIntent().hasExtra(MainActivity.CONTACT_ID)){
            contactID = getIntent().getIntExtra(MainActivity.CONTACT_ID,0);
            contactViewModel.get(contactID).observe(this,contact -> {
                if(contact!=null) {
                    enterName.setText(contact.getName());
                    enterOccupation.setText(contact.getOccupation());
                }
            });
            isEdit=true;
        }

        saveButton.setOnClickListener(v -> {

            Intent replyIntent = new Intent();


            if(!TextUtils.isEmpty(enterName.getText()) && !TextUtils.isEmpty(enterOccupation.getText())){

                Contact contact = new Contact(enterName.getText().toString(),enterOccupation.getText().toString());

                String name = enterName.getText().toString();
                String occupation = enterOccupation.getText().toString();

                replyIntent.putExtra(NAME_REPLY,name);
                replyIntent.putExtra(OCCUPATION_REPLY,occupation);
                setResult(RESULT_OK,replyIntent);

                //Using actual ContactViewModel class(Interface) no need for instantiation of object
                //ContactViewModel.insert(contact);
            }else{
                setResult(RESULT_CANCELED,replyIntent);
            }
            finish();
            //to get rid of activity - avoid backstack


        });

        //delete button EventListener
        deleteButton.setOnClickListener(v -> {
            String name = enterName.getText().toString().trim();
            String occupation = enterOccupation.getText().toString().trim();

            if(TextUtils.isEmpty(name) || (TextUtils.isEmpty(occupation))){
                Snackbar.make(enterName,R.string.empty,Snackbar.LENGTH_SHORT)
                        .show();
            }else{
                Contact contact = new Contact();
                contact.setId(contactID);
                contact.setName(name);
                contact.setOccupation(occupation);
                ContactViewModel.delete(contact);
                finish();

            }
        });

        //Update Button
        Button updateButton = findViewById(id.update_button);
        updateButton.setOnClickListener(v -> {
            int id = contactID;
            String name = enterName.getText().toString().trim();
            String occupation = enterOccupation.getText().toString().trim();

            if(TextUtils.isEmpty(name) || (TextUtils.isEmpty(occupation))){
                Snackbar.make(enterName,R.string.empty,Snackbar.LENGTH_SHORT)
                        .show();
            }else{
                Contact contact = new Contact();
                contact.setId(contactID);
                contact.setName(name);
                contact.setOccupation(occupation);
                ContactViewModel.update(contact);
                finish();

            }
        });

        if(isEdit == true){
            saveButton.setVisibility(View.INVISIBLE);
        }else {
            updateButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
        }



    }
}