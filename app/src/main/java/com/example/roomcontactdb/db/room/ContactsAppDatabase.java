package com.example.roomcontactdb.db.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.roomcontactdb.db.entity.Contact;

@Database(entities = {Contact.class}, version = 1)
public abstract class ContactsAppDatabase extends RoomDatabase {
    public abstract ContactDAO getContactDao();
}
