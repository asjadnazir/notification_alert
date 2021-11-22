package com.example.asjad.notificationalert;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class CreategroupdoneActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private EditText mGroupName;
    private FloatingActionButton mCreateGroupBtn;
    private DatabaseHelper mDatabase;
    private ArrayList<String> mMembers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategroupdone);

        mToolbar = (Toolbar) findViewById(R.id.create_group_done_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("New Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGroupName = (EditText) findViewById(R.id.create_group_name_edit_text);
        mCreateGroupBtn = (FloatingActionButton) findViewById(R.id.create_group_btn);
        mMembers = getIntent().getExtras().getStringArrayList("Members");

        mDatabase = new DatabaseHelper(this);

        final String group_name = mGroupName.getText().toString();


        mCreateGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mGroupName.getText().toString().length() == 0){
                    Toast.makeText(CreategroupdoneActivity.this, "Set Name for Group", Toast.LENGTH_SHORT).show();
                }else {

                    long result = mDatabase.insertGroup(mGroupName.getText().toString());

                    String group_id = String.valueOf(result);


                    if (result == -1)
                        Toast.makeText(CreategroupdoneActivity.this, "Error in creating group", Toast.LENGTH_SHORT).show();
                    else{

                        boolean isInserted = false;
                        for (int i=0;i < mMembers.size();i++)
                        {

                            isInserted = mDatabase.insertMember(group_id ,mMembers.get(i));

                        }

                        if (isInserted ==true) {
                            Toast.makeText(CreategroupdoneActivity.this, "Group is created ", Toast.LENGTH_SHORT).show();
                            Intent goToMainIntent = new Intent(CreategroupdoneActivity.this, MainActivity.class);
                            goToMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(goToMainIntent);
                        }
                        else {
                            Toast.makeText(CreategroupdoneActivity.this, "Error in saving members", Toast.LENGTH_SHORT).show();
                        }

                    }

                }

            }
        });

    }
}
