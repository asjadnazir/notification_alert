package com.example.asjad.notificationalert;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    private static final String TAG = "ContactsFragment";

    private View mMainView;

    ArrayList<String> mContactName = new ArrayList<>();
    private DatabaseHelper mDatabase;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_contacts, container, false);

        mDatabase  = new DatabaseHelper(getActivity());

        Log.d(TAG, "onCreateView: preparing contacts");


        Cursor res = mDatabase.getAllContacts();

        mContactName.clear();

        if(res.getCount() == 0) {
            // show message
        }

        int contact_name_index = res.getColumnIndex("CONTACT_NAME");
        while (res.moveToNext()) {

            mContactName.add(res.getString(contact_name_index));



        }

        Log.d(TAG, "onCreateView: init recyclerView");
        RecyclerView recyclerView = mMainView.findViewById(R.id.contacts_recyclerView);
        ContactsRecyclerViewAdapter adapter = new ContactsRecyclerViewAdapter(getActivity() , mContactName);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return mMainView;

    }

}
