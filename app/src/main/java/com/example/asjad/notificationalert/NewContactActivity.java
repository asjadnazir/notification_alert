package com.example.asjad.notificationalert;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

public class NewContactActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputLayout mContactName;
    private TextInputLayout mContactNumber;
    private FloatingActionButton mDoneBtn;
    private DatabaseHelper mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        mToolbar = (Toolbar) findViewById(R.id.new_contact_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("New Contact");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = new DatabaseHelper(this);

        mContactName = (TextInputLayout)findViewById(R.id.new_contact_name_input);
        mContactNumber = (TextInputLayout)findViewById(R.id.new_contact_number_input);
        mDoneBtn = (FloatingActionButton) findViewById(R.id.new_contact_done_btn);

        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String contactName = mContactName.getEditText().getText().toString();
                String contactNumber = mContactNumber.getEditText().getText().toString();

                if (!TextUtils.isEmpty(contactName) && !TextUtils.isEmpty(contactNumber)) {

                    String random = "-";

                    boolean isInserted = mDatabase.insertContact(random, contactName, contactNumber);

                    if (isInserted == true) {
                        Toast.makeText(NewContactActivity.this, "Contact is saved", Toast.LENGTH_SHORT).show();

                        Intent goToMainIntent = new Intent(NewContactActivity.this, MainActivity.class);
                        goToMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(goToMainIntent);

                    } else {
                        Toast.makeText(NewContactActivity.this, "Error in saving contact", Toast.LENGTH_SHORT).show();
                    }




                }else {
                    Toast.makeText(NewContactActivity.this, "Please enter both fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
