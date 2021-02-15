package com.example.roomcontactdb.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomcontactdb.R;
import com.example.roomcontactdb.adapter.ContactAdapter;
import com.example.roomcontactdb.db.entity.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ContactAdapter contactAdapter;
    private ContactViewModel contactViewModel;
    private ArrayList<Contact> contactArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = findViewById(R.id.rv_contacts);
        contactArrayList = new ArrayList<>();
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        contactViewModel.getAllContacts().observe(this, contacts -> {
            contactArrayList.clear();
            contactArrayList.addAll(contacts);
            contactAdapter.notifyDataSetChanged();
        });
        contactAdapter = new ContactAdapter(contact -> addOrUpdateContact(true, contact), contactArrayList);
        recyclerView.setAdapter(contactAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> addOrUpdateContact(false, null));
    }

    private void addOrUpdateContact(boolean isUpdate, Contact updateContact) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.add_contact, null);
        EditText editTextName = view.findViewById(R.id.et_add_contact_name);
        EditText editTextEmail = view.findViewById(R.id.et_add_contact_email);
        TextView textViewTitle = view.findViewById(R.id.tv_add_contact_title);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(view);

        textViewTitle.setText(isUpdate ? "Update Contact" : "Add New Contact");
        if (isUpdate) {
            editTextName.setText(updateContact.getName());
            editTextEmail.setText(updateContact.getEmail());
        }

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(isUpdate ? "UPDATE" : "SAVE", (dialogInterface, i) -> {
                    String updatedName = editTextName.getText().toString();
                    String updatedEmail = editTextEmail.getText().toString();
                    if (!TextUtils.isEmpty(updatedName) && !TextUtils.isEmpty(updatedEmail)) {
                        if (isUpdate) {
                            updateContact.setName(updatedName);
                            updateContact.setEmail(updatedEmail);
                            contactViewModel.updateContact(updateContact);
                        } else {
                            contactViewModel.addContact(new Contact(updatedName, updatedEmail));
                        }
                        contactAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(isUpdate ? "DELETE" : "CANCEL", (dialogInterface, i) -> {
                    if (isUpdate) {
                        contactViewModel.deleteContact(updateContact);
                        contactAdapter.notifyDataSetChanged();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        contactViewModel.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}