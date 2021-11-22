package com.example.asjad.notificationalert;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GroupNameEditActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mEditText;
    private DatabaseHelper mDatabase;
    private FloatingActionButton mDoneBtn;
    private FloatingActionButton mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_name_edit);

        mToolbar = (Toolbar) findViewById(R.id.name_edit_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Enter New Name");

        mDatabase = new DatabaseHelper(this);
        mEditText = (EditText)findViewById(R.id.name_edit_edittext);
        mDoneBtn =(FloatingActionButton)findViewById(R.id.name_edit_done_fab);
        mBackBtn = (FloatingActionButton)findViewById(R.id.name_edit_back_fab);

        final String group_id = getIntent().getExtras().getString("group_id");
        String group_name = getIntent().getExtras().getString("group_name");

        mEditText.setText(group_name, TextView.BufferType.EDITABLE);
        mEditText.setSelectAllOnFocus(true);

        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.updateGroup(group_id, mEditText.getText().toString());
                Toast.makeText(GroupNameEditActivity.this, "Group Name is updated to '"+ mEditText.getText().toString() +"'", Toast.LENGTH_SHORT).show();

                Intent goToMainIntent = new Intent(GroupNameEditActivity.this, MainActivity.class);
                goToMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goToMainIntent);

            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
