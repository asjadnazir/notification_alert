package com.example.asjad.notificationalert;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button mSendBtn;
    private DatabaseHelper mDatabase;
    private ArrayList<String> mMessageId = new ArrayList<>();
    private ArrayList<String> mMessageText = new ArrayList<>();
    private ArrayList<String> mMessageTime = new ArrayList<>();
    private ArrayList<String> mMessageStatus = new ArrayList<>();
    private String group_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        final String group_name = getIntent().getExtras().getString("group_name");
        group_id = getIntent().getExtras().getString("group_id");

        mToolbar = (Toolbar)findViewById(R.id.group_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(group_name);
        getSupportActionBar().setSubtitle("Tap here for group info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase  = new DatabaseHelper(this);

        mSendBtn = (Button)findViewById(R.id.group_activity_send_btn);

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent group_settings_intent = new Intent(GroupActivity.this, GroupSettingsActivity.class);
                group_settings_intent.putExtra("group_id", group_id);
                group_settings_intent.putExtra("group_name", group_name);
                startActivity(group_settings_intent);
            }
        });

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent sendActivityIntent = new Intent(GroupActivity.this, SendActivity.class);
                sendActivityIntent.putExtra("group_id", group_id);
                startActivity(sendActivityIntent);

            }

        });

    }

    //--------------------------------------------------------------------------------

    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here

        mMessageId.clear();
        mMessageText.clear();
        mMessageTime.clear();
        mMessageStatus.clear();

        Cursor res = mDatabase.getAllMessages(group_id);

        if(res.getCount() == 0) {
            // show message
            
        }

        int message_id_index = res.getColumnIndex("MESSAGE_ID");
        int message_text_index = res.getColumnIndex("MESSAGE");
        int time_index = res.getColumnIndex("TIME");
        int status_index = res.getColumnIndex("STATUS");

        while (res.moveToNext()) {

            mMessageId.add(res.getString(message_id_index));
            mMessageText.add(res.getString(message_text_index));
            mMessageTime.add( res.getString(time_index));
            mMessageStatus.add(res.getString(status_index));

        }
        initRecyclerView();

    }

    //---------------------------------------------------------------------------------

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);


    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.message_recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this ,mMessageId ,mMessageText ,mMessageTime , mMessageStatus);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        registerForContextMenu(recyclerView);


    }

}
