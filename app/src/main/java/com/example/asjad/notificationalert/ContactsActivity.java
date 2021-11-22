package com.example.asjad.notificationalert;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;


public class ContactsActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private ListView listView_Android_Contacts;
    private Button btnLoad_AndroidContacts;
    private DatabaseHelper mDatabase;
    private Button mContacts_new_group_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mToolbar = (Toolbar) findViewById(R.id.contacts_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Contacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContacts_new_group_btn = (Button) findViewById(R.id.contacts_new_group_btn);

        listView_Android_Contacts = (ListView) findViewById(R.id.listview_Android_Contacts);
        btnLoad_AndroidContacts = (Button) findViewById(R.id.btnLoad_AndroidContacts);
        mDatabase = new  DatabaseHelper(this);

        mContacts_new_group_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent createGroupIntent = new Intent(ContactsActivity.this, CreategroupActivity.class);
                startActivity(createGroupIntent);

            }
        });

        btnLoad_AndroidContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fp_get_Android_Contacts();

            }
        });

    }


    //=============================< Functions: Android.Contacts >=============================
    public class Android_Contact {
        //----------------< fritzbox_Contacts() >----------------
        public String android_contact_Name = "";
        public String android_contact_TelefonNr = "";
        public String android_contact_ID = "";
//----------------</ fritzbox_Contacts() >----------------
    }

    public void fp_get_Android_Contacts() {
//----------------< fp_get_Android_Contacts() >----------------
        ArrayList<Android_Contact> arrayList_Android_Contacts = new ArrayList<Android_Contact>();

//--< get all Contacts >--
        Cursor cursor_Android_Contacts = null;
        ContentResolver contentResolver = getContentResolver();
        try {
            cursor_Android_Contacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        } catch (Exception ex) {
            Log.e("Error on contact", ex.getMessage());
        }
//--</ get all Contacts >--
//----< Check: hasContacts >----
        if (cursor_Android_Contacts.getCount() > 0) {
//----< has Contacts >----
//----< @Loop: all Contacts >----
            while (cursor_Android_Contacts.moveToNext()) {
//< init >
                Android_Contact android_contact = new Android_Contact();
                String contact_id = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts._ID));
                String contact_display_name = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contact_phone_number = "";
//</ init >

//----< set >----
                android_contact.android_contact_Name = contact_display_name;
                android_contact.android_contact_ID = contact_id;
//----< get phone number >----
                int hasPhoneNumber = Integer.parseInt(cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                            , null
                            , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                            , new String[]{contact_id}
                            , null);

                    while (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contact_phone_number = phoneNumber;
//< set >
                        android_contact.android_contact_TelefonNr = phoneNumber;
//</ set >
                    }
                    phoneCursor.close();
                }
// Insert contacts on database

                String random = "-";
                boolean bool = mDatabase.insertContact(random, contact_display_name, contact_phone_number);
               /** if (bool)
                    Toast.makeText(ContactsActivity.this, "Contacts are saved", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ContactsActivity.this, "Error in saving contacts", Toast.LENGTH_SHORT).show();
                */


//----</ set >----
//----</ get phone number >----
// Add the contact to the ArrayList
                arrayList_Android_Contacts.add(android_contact);
            }
//----</ @Loop: all Contacts >----

//< show results >
            Adapter_for_Android_Contacts adapter = new Adapter_for_Android_Contacts(this, arrayList_Android_Contacts);
            listView_Android_Contacts.setAdapter(adapter);
//</ show results >

        }
//----</ Check: hasContacts >----

// ----------------</ fp_get_Android_Contacts() >----------------
    }

    public class Adapter_for_Android_Contacts extends BaseAdapter {
        //----------------< Adapter_for_Android_Contacts() >----------------
//< Variables >
        Context mContext;
        List<Android_Contact> mList_Android_Contacts;
//</ Variables >

//< constructor with ListArray >
public Adapter_for_Android_Contacts(Context mContext, List<Android_Contact> mContact) {
    this.mContext = mContext;
    this.mList_Android_Contacts = mContact;
}
//</ constructor with ListArray >

        @Override
        public int getCount() {
            return mList_Android_Contacts.size();
        }

        @Override
        public Object getItem(int position) {
            return mList_Android_Contacts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //----< show items >----
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(mContext,R.layout.contactlist_android_items,null);
//< get controls >
            TextView textview_contact_Name= (TextView) view.findViewById(R.id.textview_android_contact_name);
            TextView textview_contact_TelefonNr= (TextView) view.findViewById(R.id.textview_android_contact_phoneNr);
            TextView textview_contact_id = (TextView) view.findViewById(R.id.textview_android_contact_id);
//</ get controls >

//< show values >
            textview_contact_Name.setText(mList_Android_Contacts.get(position).android_contact_Name);
            textview_contact_TelefonNr.setText(mList_Android_Contacts.get(position).android_contact_TelefonNr);
            textview_contact_id.setText(mList_Android_Contacts.get(position).android_contact_ID);
//</ show values >


            view.setTag(mList_Android_Contacts.get(position).android_contact_Name);
            return view;
        }
//----</ show items >----
//----------------</ Adapter_for_Android_Contacts() >----------------
    }
//=============================</ Functions: Android.Contacts >=============================
        }
