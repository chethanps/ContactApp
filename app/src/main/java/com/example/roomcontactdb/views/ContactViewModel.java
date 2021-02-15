package com.example.roomcontactdb.views;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.roomcontactdb.db.entity.Contact;
import com.example.roomcontactdb.manager.ContactsManager;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private final ContactsManager contactsManager;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        contactsManager = new ContactsManager(application.getApplicationContext());
    }

    public LiveData<List<Contact>> getAllContacts() {
        return contactsManager.getContactsLiveData();
    }

    public void addContact(Contact contact) {
        contactsManager.addContact(contact);
    }

    public void deleteContact(Contact contact) {
        contactsManager.deleteContact(contact);
    }

    public void updateContact(Contact contact) {
        contactsManager.updateContact(contact);
    }

    public void clear() {
        contactsManager.clear();
    }

}
