package com.example.asjad.notificationalert;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreategroupActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private ListView mCreateGroupListView;
    private DatabaseHelper mDatabase;
    private FloatingActionButton mDoneBtn;
    private ArrayList<String> mMembers = new ArrayList<>();
    private ListviewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategroup);

        mToolbar = (Toolbar)findViewById(R.id.create_group_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("New Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCreateGroupListView = (ListView) findViewById(R.id.create_group_list_view);

        mDatabase = new DatabaseHelper(this);

        mDoneBtn = (FloatingActionButton) findViewById(R.id.create_group_select_btn);

        Cursor res = mDatabase.getAllContacts();

        if(res.getCount() == 0) {
            // show message
            Toast.makeText(CreategroupActivity.this, "No Contacts found.", Toast.LENGTH_SHORT).show();

        }

        final List<ContactsModel> contacts = new ArrayList<>();

        while (res.moveToNext()) {

            int contact_id_index = res.getColumnIndex("CONTACT_ID");
            int contact_name_index = res.getColumnIndex("CONTACT_NAME");
            int contact_number_index = res.getColumnIndex("CONTACT_NUMBER");

            String contactId = res.getString(contact_id_index);
            String contactName = res.getString(contact_name_index);
            String contactNumber = res.getString(contact_number_index);

            contacts.add(new ContactsModel(false, contactId, contactName, contactNumber));

        }

        adapter = new ListviewAdapter(this, contacts);
        mCreateGroupListView.setAdapter(adapter);

        mCreateGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ContactsModel model = contacts.get(i);

                if (model.isSelected()){
                    model.setSelected(false);

                    mMembers.remove(model.getId());

                }
                else{

                    model.setSelected(true);

                    mMembers.add(model.getId());

                }

                contacts.set(i, model);

                //now update adapter
                adapter.updateRecords(contacts);

            }
        });

        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mMembers.size() == 0){
                    Toast.makeText(CreategroupActivity.this, "At least 1 contact must be selected", Toast.LENGTH_SHORT).show();
                }else {

                    Intent doneIntent = new Intent(CreategroupActivity.this, CreategroupdoneActivity.class);
                    doneIntent.putExtra("Members", mMembers);
                    startActivity(doneIntent);

                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (TextUtils.isEmpty(newText)){
                    adapter.fiter("");
                }else {
                    adapter.fiter(newText);
                }

                return true;
            }
        });
        return true;
    }

}
