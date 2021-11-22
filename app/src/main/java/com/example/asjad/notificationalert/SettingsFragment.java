package com.example.asjad.notificationalert;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private View mMainview;
    private RelativeLayout mNewContactBtn;
    private RelativeLayout mNewGroupBtn;
    private RelativeLayout mLoadBtn;
    private ProgressDialog mLoadProgress;
    private DatabaseHelper mDatabase;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainview = inflater.inflate(R.layout.fragment_settings, container, false);

        mLoadProgress = new ProgressDialog(getActivity());

        mDatabase = new  DatabaseHelper(getActivity());

        mNewContactBtn = (RelativeLayout) mMainview.findViewById(R.id.settings_new_contact_btn);
        mNewGroupBtn = (RelativeLayout) mMainview.findViewById(R.id.settings_new_group_btn);
        mLoadBtn = (RelativeLayout) mMainview.findViewById(R.id.settings_load_btn);

        mNewContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newContactIntent = new Intent(getActivity(), NewContactActivity.class);
                startActivity(newContactIntent);
            }
        });

        mNewGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createGroupIntent = new Intent(getActivity(), CreategroupActivity.class);
                startActivity(createGroupIntent);
            }
        });

        mLoadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    Toast.makeText(getActivity(), "Permission not granted", Toast.LENGTH_SHORT).show();
                }else {
                    // Permission is granted
                    mLoadProgress.setTitle("Load Contacts");
                    mLoadProgress.setMessage("Please wait while we Load contacts from phoneBook...");
                    mLoadProgress.setCanceledOnTouchOutside(false);
                    mLoadProgress.show();

                    fp_get_Android_Contacts();

                }

            }
        });

        return mMainview;
    }

    public class Android_Contact {

        public String android_contact_Name = "";
        public String android_contact_TelefonNr = "";
        public String android_contact_ID = "";

    }

    public void fp_get_Android_Contacts() {

        ArrayList<Android_Contact> arrayList_Android_Contacts = new ArrayList<>();

        Cursor cursor_Android_Contacts = null;
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        try {
            cursor_Android_Contacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        } catch (Exception ex) {
            Log.e("Error on contact", ex.getMessage());
        }

        Integer no_added = 0;

        if (cursor_Android_Contacts.getCount() > 0) {

            while (cursor_Android_Contacts.moveToNext()) {

                Android_Contact android_contact = new Android_Contact();
                String contact_id = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts._ID));
                String contact_display_name = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contact_phone_number = "";

                android_contact.android_contact_Name = contact_display_name;
                android_contact.android_contact_ID = contact_id;

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

                        android_contact.android_contact_TelefonNr = phoneNumber;

                    }
                    phoneCursor.close();
                }

                boolean is_inserted = false;

                if (!mDatabase.ifHasContact(contact_id)) {

                    is_inserted = mDatabase.insertContact(contact_id, contact_display_name, contact_phone_number);

                }

                if (is_inserted){
                    no_added = no_added + 1;
                }

                arrayList_Android_Contacts.add(android_contact);
            }

        }

        //mLoadProgress.dismiss();

        if (no_added == 0){
            Toast.makeText(getActivity(), "No New Contacts", Toast.LENGTH_SHORT).show();
        }else if (no_added == 1){
            Toast.makeText(getActivity(), "One Contact is Loaded", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(), no_added + "contacts are Loaded", Toast.LENGTH_SHORT).show();
        }

    }

}
