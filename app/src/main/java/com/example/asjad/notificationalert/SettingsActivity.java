package com.example.asjad.notificationalert;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private Button mContacts_btn;

    private DatabaseHelper mDatabase;

    private EditText mId;
    private EditText mName;
    private EditText mNumber;
    private Button mInsert_btn;
    private Button mView_all_btn;
    private Button mView_all_group_btn;
    private Button getmView_all_member_btn;
    private Button mProgressBtn;
    private ProgressDialog mProgress;


    //------------------------------------------------------------test---------------------------

    ArrayList<String> mContactName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = (Toolbar)findViewById(R.id.settings_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressBtn = (Button)findViewById(R.id.button_for_progress);

        mDatabase = new DatabaseHelper(this);

        mId = (EditText) findViewById(R.id.con_id);
        mName = (EditText)findViewById(R.id.con_name);
        mNumber = (EditText)findViewById(R.id.con_number);
        mInsert_btn = (Button)findViewById(R.id.con_btn);

        mProgress = new ProgressDialog(this);

        mProgressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgress.setTitle("Set");
                mProgress.setMessage("Title");
                mProgress.show();



            }
        });

        mInsert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInserted = mDatabase.insertContact(
                        mId.getText().toString(),
                        mName.getText().toString(),
                        mNumber.getText().toString());
                if (isInserted ==true)
                    Toast.makeText(SettingsActivity.this, "Contact is saved", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(SettingsActivity.this, "Error in saving contact", Toast.LENGTH_SHORT).show();
            }
        });


        mView_all_btn = (Button)findViewById(R.id.view_all_contacts_btn);
        mView_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor res = mDatabase.getAllContacts();
                if(res.getCount() == 0) {
                    // show message
                    showMessage("Error","Nothing found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("ID :"+ res.getString(0)+"\n");
                    buffer.append("NAME :"+ res.getString(1)+"\n");
                    buffer.append("NUMBER :"+ res.getString(2)+"\n\n");
                }

                // Show all data
                showMessage("Data",buffer.toString());

            }
        });


        mView_all_group_btn = (Button)findViewById(R.id.view_all_groups_btn);
        mView_all_group_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor res = mDatabase.getAllGroups();
                if(res.getCount() == 0) {
                    // show message
                    showMessage("Error","Nothing found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("ID :"+ res.getString(0)+"\n");
                    buffer.append("NAME :"+ res.getString(1)+"\n");
                }

                // Show all data
                showMessage("Data",buffer.toString());

            }
        });

        getmView_all_member_btn = (Button)findViewById(R.id.view_all_members_btn);
        getmView_all_member_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String string = "2";

                Cursor res = mDatabase.getAllMember(string);
                if(res.getCount() == 0) {
                    // show message
                    showMessage("Error","Nothing found");
                    return;
                }
                int group_id_index = res.getColumnIndex("GROUP_ID");
                int contact_name_index = res.getColumnIndex("CONTACT_NAME");


                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("GROUP ID :"+ res.getString(group_id_index)+"\n");
                    buffer.append("CONTACT Name :"+ res.getString(contact_name_index)+"\n");
                }

                // Show all data
                showMessage("Data",buffer.toString());

            }
        });



        mContacts_btn = (Button) findViewById(R.id.contacts_btn);
        mContacts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent contactsIntent = new Intent(SettingsActivity.this, ContactsActivity.class);
                startActivity(contactsIntent);

            }
        });

        mDatabase  = new DatabaseHelper(this);


        Cursor res = mDatabase.getAllContacts();

        if(res.getCount() == 0) {
            // show message
            Toast.makeText(this, "Nothing found", Toast.LENGTH_SHORT).show();
        }

        int contact_name_index = res.getColumnIndex("CONTACT_NAME");
        while (res.moveToNext()) {

            mContactName.add(res.getString(contact_name_index));



        }

        RecyclerView recyclerView = findViewById(R.id.test_recyclerView);
        ContactsRecyclerViewAdapter adapter = new ContactsRecyclerViewAdapter(this , mContactName);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
