package com.example.roomcontactdb.db.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.roomcontactdb.db.entity.Contact;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ContactDAO {
    @Insert
    public long addContact(Contact contact);

    @Update
    public void updateContact(Contact contact);

    @Delete
    public void deleteContact(Contact contact);

    @Query("select * from contacts")
    public Flowable<List<Contact>> getContacts();

    @Query("select * from contacts where contact_id == :contactId")
    public Contact getContact(long contactId);
}
