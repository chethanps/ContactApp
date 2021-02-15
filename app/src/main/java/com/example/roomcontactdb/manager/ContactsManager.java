package com.example.roomcontactdb.manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.roomcontactdb.db.entity.Contact;
import com.example.roomcontactdb.db.room.ContactDAO;
import com.example.roomcontactdb.db.room.ContactsAppDatabase;
import com.example.roomcontactdb.db.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class ContactsManager {
    public static final String TAG = ContactsManager.class.getSimpleName();
    private final boolean ENABLE_ROOM = true;
    private ArrayList<Contact> contacts;
    private DatabaseHelper databaseHelper;
    private ContactsAppDatabase roomDataBase;
    private ContactDAO contactDAO;
    private CompositeDisposable compositeDisposable;
    private Context context;
    private MutableLiveData<List<Contact>> contactsLiveData;

    public ContactsManager(Context cxt) {
        context = cxt;
        contacts = new ArrayList<>();
        contactsLiveData = new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();
        if (ENABLE_ROOM) {
            roomDataBase = Room
                    .databaseBuilder(context, ContactsAppDatabase.class, "ContactDB")
                    .build();
            contactDAO = roomDataBase.getContactDao();
            compositeDisposable.add(
                    contactDAO.getContacts()
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe((List<Contact> dbContacts) -> {
                                contactsLiveData.postValue(dbContacts);
                            }, throwable -> {

                            }))
            ;
        } else {
            databaseHelper = new DatabaseHelper(context);
            contacts = databaseHelper.getAllContacts();
        }

    }

    public LiveData<List<Contact>> getContactsLiveData() {
        return contactsLiveData;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public Contact getContact(int position) {
        return contacts.get(position);
    }

    public void addContact(Contact newContact) {
        if (ENABLE_ROOM) {
            compositeDisposable.add(
                    Completable.fromAction(() -> contactDAO.addContact(newContact))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableCompletableObserver() {
                                @Override
                                public void onComplete() {
                                    Toast.makeText(context, "Contact Added Successfully", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    Toast.makeText(context, "Error Adding the Contact", Toast.LENGTH_LONG).show();
                                }
                            })
            );
        } else {
            long id = databaseHelper.insertContact(newContact);
            Contact tempContact = databaseHelper.getContact(id);
            newContact.setId(id);
            contacts.add(newContact);
        }
    }

    public void updateContact(Contact contact) {
        if (ENABLE_ROOM) {
            Log.i(TAG, "updateContact: New Value " + contact);
            compositeDisposable.add(
                    Completable.fromAction(() -> contactDAO.updateContact(contact))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableCompletableObserver() {
                                @Override
                                public void onComplete() {
                                    Toast.makeText(context, "Updated the Contact successfully", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    Toast.makeText(context, "Error Updating the Contact", Toast.LENGTH_LONG).show();
                                }
                            })
            );
        } else {
            databaseHelper.updateContact(contact);
            //        contacts.set(position, contact);
        }

    }

    public void deleteContact(Contact contact) {
//        Contact contact = contacts.get(position);
        if (ENABLE_ROOM) {
            compositeDisposable.add(
                    Completable.fromAction(() -> contactDAO.deleteContact(contact))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableCompletableObserver() {
                                @Override
                                public void onComplete() {
                                    Toast.makeText(context, "Deleted the Contact successfully", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    Toast.makeText(context, "Error Deleting the Contact", Toast.LENGTH_LONG).show();
                                }
                            })
            );
        } else {
            databaseHelper.deleteContact(contact);
//        contacts.remove(position);
        }
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
