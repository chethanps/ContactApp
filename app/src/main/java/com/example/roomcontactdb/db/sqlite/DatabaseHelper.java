package com.example.roomcontactdb.db.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.roomcontactdb.db.entity.Contact;

import java.util.ArrayList;

import static com.example.roomcontactdb.db.entity.Contact.COLUMN_EMAIL;
import static com.example.roomcontactdb.db.entity.Contact.COLUMN_ID;
import static com.example.roomcontactdb.db.entity.Contact.COLUMN_NAME;
import static com.example.roomcontactdb.db.entity.Contact.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String NAME = "contacts_db";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_EMAIL + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public DatabaseHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long insertContact(Contact contact) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, contact.getName());
        contentValues.put(COLUMN_EMAIL, contact.getEmail());

        long id = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
        return id;

    }

    public void updateContact(Contact contact) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, contact.getName());
        contentValues.put(COLUMN_EMAIL, contact.getEmail());

        sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?",
                new String[] {String.valueOf(contact.getId())});

        sqLiteDatabase.close();
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
        sqLiteDatabase.close();
    }

    public ArrayList<Contact> getAllContacts() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Contact> contacts = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));

                contacts.add(new Contact(name,email,id));
            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return contacts;
    }

    public Contact getContact(long id) {
        Contact contact = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if(cursor.moveToFirst()) {
            long keyId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));

            contact = new Contact(name,email,keyId);
            cursor.close();
        }
        sqLiteDatabase.close();
        return contact;
    }
}
