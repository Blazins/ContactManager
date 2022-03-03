package com.example.contactroom.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.contactroom.model.Contact;
import com.example.contactroom.util.ContactRoomDatabase;

import java.util.List;

public class ContactRepository {
    /*
     Repository is not a part of Room Architecture. gets data from UI, api that cleans up data to interact with the UI.
     Abstraction
     Data can come from anywhere - Connect to Dao and to the network
     */
    private ContactDao contactDao;
    LiveData<List<Contact>> allContacts;

    public ContactRepository(Application application) {
        ContactRoomDatabase db = ContactRoomDatabase.getDatabase(application);
       contactDao = db.contactDao();

       allContacts = contactDao.getAllContacts();
       //Contain everything once instantiated

    }
    public LiveData<List<Contact>> getAllData() {return allContacts;}

    public void insert(Contact contact){
        ContactRoomDatabase.databaseExecutor.execute(() ->{
            contactDao.insert(contact);
        });

    }
    public LiveData<Contact> get(int id){
        return contactDao.get(id);
    }
    public void update(Contact contact){
        ContactRoomDatabase.databaseExecutor.execute(()->
                contactDao.update(contact));
    }
    public void delete(Contact contact){
        ContactRoomDatabase.databaseExecutor.execute(()->
                contactDao.delete(contact));
    }
    /*
    ViewModel - provide data to user interface and survive configuration  changes.
    Communication channel between user interface and repository
    Holds app UI data in lifecycle conscious way in a way that survives configuration changes
    Can be used with livedata for notifications
     */
}
