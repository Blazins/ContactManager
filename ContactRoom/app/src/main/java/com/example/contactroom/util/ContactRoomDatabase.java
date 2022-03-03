package com.example.contactroom.util;

import android.content.Context;

/*We need Entity, Dao and SQLite
 Room handles SQLite , Entities = Contacts

    Room DataBase is an abstraction that sits on top of SQLite
    Dao is the intermediary between Room and SQLite database
    To issue queries to its database
    Room does not allow you to issue queries on the main thread/ user interface thread
    Room provide compile time checks of SQLite statements -asynchronous background checks

Repository is not a part of Room Architecture. gets data from UI, api that cleans up data to interact with the UI.
Abstraction
Connect to Dao and to the network

 */
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.contactroom.data.ContactDao;
import com.example.contactroom.model.Contact;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Contact.class},version = 1, exportSchema = false)
public abstract class ContactRoomDatabase extends RoomDatabase  {

    public abstract ContactDao contactDao();

    public static final int NUMBER_OF_THREADS=4;

    //able to get rid of itself
    private static volatile ContactRoomDatabase INSTANCE;

    //help us run things in a back thread
    public static final ExecutorService databaseExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ContactRoomDatabase getDatabase(final Context context){
        if(INSTANCE==null){
            //synchronized makes sure everything is running well since we are running things in a background thread
            synchronized (ContactRoomDatabase.class){
                if (INSTANCE==null){
                  INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                          ContactRoomDatabase.class,"contact_database")
                          //once everything is built the callback is called which uses databaseExecutor to run things in the background awy from UI thread
                          //deletes everything through Dao then adds contacts through Dao
                          .addCallback(sRoomDatabaseCallback)
                          .build() ;

                }

            }
        }


        return INSTANCE;
    }
    public static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseExecutor.execute(( )->{
                ContactDao contactDao = INSTANCE.contactDao();
                 contactDao.deleteAll();

                 Contact contact = new Contact("Mike","Software Developer");
                 contactDao.insert(contact);

                 contact = new Contact("Bond","Spy");
                 contactDao.insert(contact);


                contact = new Contact("Peter Parker","Spiderman");
                contactDao.insert(contact);


                contact = new Contact("Bruce Wayne","Batman");
                contactDao.insert(contact);


                contact = new Contact("Tony Stark","Billionare Playboy Philanthropist");
                contactDao.insert(contact);

            });
        }
    };
}
