package com.example.asjad.notificationalert;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GroupSettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mGroupName;
    private TextView mGroupMembers;
    private DatabaseHelper mDatabase;
    private ListView mMemberListView;
    private Button mEditBtn;
    private Button mDeleteBtn;
    private ImageButton mNameEditBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_settings);

        mGroupName = (TextView)findViewById(R.id.group_settings_display_name);
        mGroupMembers = (TextView)findViewById(R.id.group_settings_participant);

        mMemberListView = (ListView)findViewById(R.id.group_settings_members_list_view);

        mEditBtn = (Button)findViewById(R.id.group_settings_edit_btn);
        mDeleteBtn = (Button)findViewById(R.id.group_settings_delete_btn);
        mNameEditBtn = (ImageButton) findViewById(R.id.group_settings_name_edit_btn);

        mDatabase = new DatabaseHelper(this);

        final String group_id = getIntent().getExtras().getString("group_id");
        final String group_name = getIntent().getExtras().getString("group_name");

        mToolbar = (Toolbar) findViewById(R.id.group_settings_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Group Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGroupName.setText(group_name);
        mGroupMembers.setText(String.valueOf(numberOfMembers(group_id))+" Participants");

        Cursor res = mDatabase.getAllMember(group_id);
        if(res.getCount() == 0) {
            // show message
            Toast.makeText(this, "No Contacts found in group", Toast.LENGTH_SHORT).show();
        }

        int contact_id_index = res.getColumnIndex("CONTACT_ID");
        int contact_name_index = res.getColumnIndex("CONTACT_NAME");
        int contact_number_index = res.getColumnIndex("CONTACT_NUMBER");

        final List<MemberModel> members = new ArrayList<>();

        while (res.moveToNext()) {
            String member_id = res.getString(contact_id_index);
            String member_name = res.getString(contact_name_index);
            String member_number = res.getString(contact_number_index);

            members.add(new MemberModel(member_id, member_name, member_number));
        }

        ListviewMembersAdapter adapter = new ListviewMembersAdapter(this, members);
        mMemberListView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(mMemberListView);

        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToEditMemberIntent = new Intent(GroupSettingsActivity.this, GroupMemberEditActivity.class);
                goToEditMemberIntent.putExtra("group_id", group_id);
                goToEditMemberIntent.putExtra("group_name", group_name);
                startActivity(goToEditMemberIntent);

            }
        });

        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.deleteGroup(group_id);
                Toast.makeText(GroupSettingsActivity.this, "Group '"+group_name+"' is deleted", Toast.LENGTH_SHORT).show();
                Intent goToMainIntent = new Intent(GroupSettingsActivity.this, MainActivity.class);
                goToMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goToMainIntent);
            }
        });

        mNameEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToEditNameIntent = new Intent(GroupSettingsActivity.this, GroupNameEditActivity.class);
                goToEditNameIntent.putExtra("group_id", group_id);
                goToEditNameIntent.putExtra("group_name", group_name);
                startActivity(goToEditNameIntent);

                // update group name


            }
        });


    }

    //----------function for count number of members in group--------

    public int numberOfMembers(String group_id) {
        int result = 0;
        Cursor cursor = null;
        try {
            cursor = mDatabase.countAllMember(group_id);
            cursor.moveToFirst();
            result = cursor.getInt(0);

            if (result < 0) {
                result = 0;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return result;
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight() * 2.26;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);

    }

}
