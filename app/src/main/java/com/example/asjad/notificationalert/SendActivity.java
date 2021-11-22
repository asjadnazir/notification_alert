package com.example.asjad.notificationalert;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendActivity extends AppCompatActivity {

    private static final String TAG = "SendActivity";

    private Toolbar mToolbar;
    private EditText mNotificationEditText;
    private FloatingActionButton mSendBtn;
    private DatabaseHelper mDatabase;
    private ProgressDialog mSendProgress;
    private ProgressBar mProgressBar;

    //private int intCount = 0, initialStringLength = 0;
    //private String strText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        mToolbar = (Toolbar) findViewById(R.id.send_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Send");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = new DatabaseHelper(this);

        mNotificationEditText = (EditText) findViewById(R.id.notification_sent_edittext);
        mNotificationEditText.setText("Dear @Employee:\n\n\nSincerely,\nDate: "+getDate(), TextView.BufferType.EDITABLE);
        mNotificationEditText.setSelection(16);


        /**

         String str1 = "@Employee";
         String message_string = mNotificationEditText.getText().toString();

         if (message_string.contains(str1)) {

         SpannableString spannable = new SpannableString(message_string);
         spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
         message_string.indexOf(str1),
         message_string.indexOf(str1) + str1.length(),
         Spannable.SPAN_INCLUSIVE_INCLUSIVE);

         mNotificationEditText.setText(spannable);
         } else {
         mNotificationEditText.setText(message_string);
         } **/





        mNotificationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                Log.d(TAG, "onTextChanged: called.");


                /**
                String str1 = "Employee";
                String message_string = mNotificationEditText.getText().toString();

                if (message_string.contains(str1)) {

                    SpannableString spannable = new SpannableString(message_string);
                    spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                            message_string.indexOf(str1),
                            message_string.indexOf(str1) + str1.length(),
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                    mNotificationEditText.removeTextChangedListener(this);
                    mNotificationEditText.setText(spannable);
                    mNotificationEditText.addTextChangedListener(this);

                } else {
                    mNotificationEditText.removeTextChangedListener(this);
                    mNotificationEditText.setText(message_string);
                    mNotificationEditText.addTextChangedListener(this);
                }
                **/






                /**
                String strET = mNotificationEditText.getText().toString();
                String[] str = strET.split(" ");
                int cnt = 0;
                if (charSequence.length() != initialStringLength && charSequence.length() != 0) {
                    if (!strET.substring(strET.length() - 1).equals(" ")) {
                        initialStringLength = charSequence.length();
                        cnt = intCount;
                        for (int u = 0; u < str.length; u++)
                            if (str[u].charAt(0) == '#')
                                strText = strText + " " + "<font color='#EE0000'>" + str[u] + "</font>";
                            else
                                strText = strText + " " + str[u];
                    }
                    if (intCount == cnt) {
                        intCount = str.length;
                        mNotificationEditText.removeTextChangedListener(this);
                        mNotificationEditText.setText(Html.fromHtml(strText));
                        mNotificationEditText.addTextChangedListener(this);
                        //mNotificationEditText.setSelection(textShareMemories.getText().toString().length());
                    }
                } else {
                    strText = "";
                }

                 **/


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        /**

        SpannableString ss = new SpannableString("Android is a operating system for mobile. Android latest version is kitkat.");
        String textToSearch = "Android";
        Pattern pattern = Pattern.compile(textToSearch);
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find()) {
            ss.setSpan(new ForegroundColorSpan(Color.RED), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mNotificationEditText.setText(ss);

        **/





        mSendBtn = (FloatingActionButton) findViewById(R.id.notification_send_btn);

        mSendProgress = new ProgressDialog(this);
        mSendProgress.setTitle("Notification Sending");
        mSendProgress.setMessage("Wait until Notification sent completely.");
        mSendProgress.setCanceledOnTouchOutside(false);

        final String group_id = getIntent().getExtras().getString("group_id");

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mNotificationEditText.getText().toString().length() == 0) {
                    Toast.makeText(SendActivity.this, "Type Message", Toast.LENGTH_SHORT).show();
                }else {



                    /*
                    RelativeLayout layout = findViewById(R.id.display);

                    mProgressBar = new ProgressBar(SendActivity.this,null,android.R.attr.progressBarStyleLarge);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    layout.addView(mProgressBar,params);
                    mProgressBar.setVisibility(View.VISIBLE);  //To show ProgressBar
                    */


                    //mSendProgress.show();

                    String message = mNotificationEditText.getText().toString();

                    Cursor res = mDatabase.getAllMember(group_id);
                    if(res.getCount() == 0) {
                        // show message
                        Toast.makeText(SendActivity.this, "Add contacts in group to Send", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int contact_id_index = res.getColumnIndex("CONTACT_ID");
                    int contact_name_index = res.getColumnIndex("CONTACT_NAME");
                    int contact_number_index = res.getColumnIndex("CONTACT_NUMBER");

                    String is_sent = "true";
                    ArrayList<String> stringsOfContact = new ArrayList<>();

                    while (res.moveToNext()) {
                        String member_id = res.getString(contact_id_index);
                        String member_name = res.getString(contact_name_index);
                        String member_number = res.getString(contact_number_index);



                        //-------- change @employee to member_name
                        String str1 = "@Employee";
                        String message_string = message;

                        if (message_string.contains(str1)) {

                            message_string = message_string.replace(str1, member_name);

                        } else {
                            message_string = message;
                        }






                        String s = member_number.substring(0,1);
                        if(s.equals("9")){
                            member_number = "+"+member_number;
                        }else if(!s.equals("0")){
                            member_number = "0"+member_number;
                        }



                        //Log.i("Number", member_number);

                        Boolean message_sent = sendSMS(member_number, message_string);


                        if (!message_sent) {
                            is_sent = "false";
                            stringsOfContact.add(member_id);
                        }

                    }

                    if (!(stringsOfContact.size() == 0)){

                        long message_id = mDatabase.insertMessages(group_id, message, is_sent, getTime());

                        Toast.makeText(SendActivity.this, "Notification not Sent to "+stringsOfContact.size(), Toast.LENGTH_SHORT).show();

                        //mSendProgress.dismiss();


                        //mProgressBar.setVisibility(View.GONE);     // To Hide ProgressBar

                        finish();

                    }else {

                        long message_id = mDatabase.insertMessages(group_id, message, is_sent, getTime());

                        Toast.makeText(SendActivity.this, "Notification Sent", Toast.LENGTH_SHORT).show();

                        //mSendProgress.dismiss();


                        //mProgressBar.setVisibility(View.GONE);     // To Hide ProgressBar

                        finish();
                    }

                }
            }
        });

    }

    private String getTime (){


        Calendar calendar;
        SimpleDateFormat simpleDateFormat;
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String time = simpleDateFormat.format(calendar.getTime());
        return time;

    }


    private String getDate (){


        Calendar calendar;
        SimpleDateFormat simpleDateFormat;
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String time = simpleDateFormat.format(calendar.getTime());
        return time;

    }

    private boolean sendMessage (String number , String message){

        try {

            //Thread.sleep(500);

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, null,null);

            return true;

        }catch (Exception e){

            return false;

        }
    }

    //---------web ---------------------


    private boolean sendSMS(String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        //Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        //Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        //Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        //Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        //Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        },new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        try {

            Thread.sleep(500);

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

            return true;

        }catch (Exception e){

            return false;

        }

    }


}
