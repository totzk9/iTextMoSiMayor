package gte.com.itextmosimayor.SMS;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.constant.Constants;

public class SMSMessaging extends AppCompatActivity {

    ArrayAdapter arrayAdapter;
    ArrayList<String> smsMessagesList = new ArrayList<>();
    RecyclerView recyclerView;
    EditText editSendMessage;
    MaterialButton btnSendMessage;
//    ArrayAdapter arrayAdapter;
    SmsManager smsManager = SmsManager.getDefault();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_messaging);

        //Recycler initialization
//        recyclerView = findViewById(R.id.recycler_view_message);

        //Edit Text Message initialization
        editSendMessage = findViewById(R.id.editSendMessage);
        editSendMessage.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editSendMessage, InputMethodManager.SHOW_IMPLICIT);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, smsMessagesList);
//        recyclerView.setAdapter(arrayAdapter);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS();
        } else {
            refreshSmsInbox();
        }

        //send message button initialization
        btnSendMessage = findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendClick("09055401529");
            }
        });

    }

    public void getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if(Build.VERSION.SDK_INT >= 23) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_SMS)) {
                    Toast.makeText(this, "Allow permission to continue", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{android.Manifest.permission.READ_SMS}, Constants.READ_SMS_PERMISSIONS_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure its original READ_CONTACTS request
        if (requestCode == Constants.READ_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
                refreshSmsInbox();
            } else {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
                    "\n" + smsInboxCursor.getString(indexBody) + "\n";
            arrayAdapter.add(str);
        } while (smsInboxCursor.moveToNext());
    }

    public void onSendClick(String number) {
        try {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                if (!(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.SEND_SMS))) {
                    ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.SEND_SMS}, Constants.MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
            } else {
                if(editSendMessage.getText().toString().trim().length() == 0){
                    return;
                }
                smsManager.sendTextMessage(number, null, editSendMessage.getText().toString() + "\n#iTextMoSiMayor", null, null);
                Toast.makeText(this, "Message sent!", Toast.LENGTH_SHORT).show();
                editSendMessage.setText("");
            }

        } catch (Exception e){
            Log.e("MSG", e.toString());
        }
    }
}
