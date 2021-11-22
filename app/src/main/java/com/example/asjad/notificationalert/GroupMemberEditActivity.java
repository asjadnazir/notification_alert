package com.example.asjad.notificationalert;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberEditActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ListView mListView;
    private DatabaseHelper mDatabase;
    private FloatingActionButton mDoneBtn;
    private ArrayList<String> mMembers = new ArrayList<>();
    private ArrayList<String> mDeleteMembers = new ArrayList<>();
    private ListviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_edit);

        mToolbar = (Toolbar)findViewById(R.id.member_edit_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Edit Group Members");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mListView = (ListView) findViewById(R.id.member_edit_list_view);
        mDoneBtn = (FloatingActionButton)findViewById(R.id.member_edit_done_fab);
        mDatabase = new DatabaseHelper(this);

        final String group_id = getIntent().getExtras().getString("group_id");
        String group_name = getIntent().getExtras().getString("group_name");

        //------------------------------------------------------------------------------------

        Cursor members_cursor = mDatabase.getAllMember(group_id);

        if(!(members_cursor.getCount() == 0)){

        }

        int id_index = members_cursor.getColumnIndex("CONTACT_ID");

        while (members_cursor.moveToNext()){

            mMembers.add(members_cursor.getString(id_index));

        }

        //------------------------------------------------------------------------------------

        Cursor res = mDatabase.getAllContacts();

        if(res.getCount() == 0) {
            // show message
            Toast.makeText(GroupMemberEditActivity.this, "No Contacts found.", Toast.LENGTH_SHORT).show();

        }

        final List<ContactsModel> contacts = new ArrayList<>();

        while (res.moveToNext()) {

            int contact_id_index = res.getColumnIndex("CONTACT_ID");
            int contact_name_index = res.getColumnIndex("CONTACT_NAME");
            int contact_number_index = res.getColumnIndex("CONTACT_NUMBER");

            String contactId = res.getString(contact_id_index);
            String contactName = res.getString(contact_name_index);
            String contactNumber = res.getString(contact_number_index);

            if (mMembers.contains(contactId)){
                mMembers.remove(contactId);
                contacts.add(new ContactsModel(true, contactId, contactName, contactNumber));
            }else {
                contacts.add(new ContactsModel(false, contactId, contactName, contactNumber));
            }

        }

        adapter = new ListviewAdapter(this, contacts);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ContactsModel model = contacts.get(i);

                if (model.isSelected()){
                    model.setSelected(false);

                    if (mMembers.contains(model.getId())){
                        mMembers.remove(model.getId());
                    }else {
                        mDeleteMembers.add(model.getId());
                    }

                }
                else{

                    model.setSelected(true);

                    if(mDeleteMembers.contains(model.getId())){
                        mDeleteMembers.remove(model.getId());
                    }else {
                        mMembers.add(model.getId());
                    }

                }

                contacts.set(i, model);

                //now update adapter
                adapter.updateRecords(contacts);

            }
        });

        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mMembers.size() == 0 && mDeleteMembers.size() == 0){
                    Toast.makeText(GroupMemberEditActivity.this, "Participants are same", Toast.LENGTH_SHORT).show();
                    finish();
                }else {

                    if (!(mMembers.size() == 0)){

                        boolean isInserted = false;
                        for (int i=0;i < mMembers.size();i++)
                        {
                            isInserted = mDatabase.insertMember(group_id ,mMembers.get(i));

                        }

                        if (isInserted ==true) {
                            Toast.makeText(GroupMemberEditActivity.this, "member is added ", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(GroupMemberEditActivity.this, "Error in saving members", Toast.LENGTH_SHORT).show();
                        }

                    }

                    if (!(mDeleteMembers.size() == 0)){

                        boolean isDeleted = false;
                        for (int i=0;i < mDeleteMembers.size();i++)
                        {

                            Integer is = mDatabase.deleteMember(group_id ,mDeleteMembers.get(i));
                            if (is == 0) isDeleted = false; else isDeleted = true;

                        }

                        if (isDeleted ==true) {
                            Toast.makeText(GroupMemberEditActivity.this, "member is deleted ", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(GroupMemberEditActivity.this, "Error in deleting members", Toast.LENGTH_SHORT).show();
                        }

                    }

                    Intent goToMainIntent = new Intent(GroupMemberEditActivity.this, MainActivity.class);
                    goToMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(goToMainIntent);

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
