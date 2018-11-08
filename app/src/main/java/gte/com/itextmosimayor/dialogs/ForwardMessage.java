package gte.com.itextmosimayor.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

import gte.com.itextmosimayor.Database.DatabaseHandler;
import gte.com.itextmosimayor.Database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.adapter.ForwardMessageAdapter;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.requests.SingletonRequest;



public class ForwardMessage extends Activity {

    String MessageID;
    SQLiteDatabase database;
    DatabaseHandler db;
    RecyclerView view_forward;
    ForwardMessageAdapter forwardMessageAdapter;
    Cursor c;
    ImageButton btnClose;
    ImageButton btnForward;
    private ArrayList<String> deptList = new ArrayList<>();
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward_message);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            MessageID = extras.getString("MessageID");
        }
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
//        toolbar.setTitleTextColor(Color.WHITE);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        toolbar.setTitle("Forward To");

        db = new DatabaseHandler(this);
        database = db.getWritableDatabase();
        c = getAllItems();
        view_forward = findViewById(R.id.view_forward);
        view_forward.setHasFixedSize(true);
        forwardMessageAdapter = new ForwardMessageAdapter(this, c);
        forwardMessageAdapter.setOnItemCheckListener(new ForwardMessageAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(String departmentID) {
                if (!deptList.contains(departmentID)) {
                    deptList.add(departmentID);
                }
            }

            @Override
            public void onItemUncheck(String departmentID) {
                if (deptList.contains(departmentID)) {
                    deptList.remove(departmentID);
                    Log.e("REMOVE", departmentID);
                }
            }
        });
        view_forward.setLayoutManager(new LinearLayoutManager(this));
        view_forward.setAdapter(forwardMessageAdapter);
        view_forward.addItemDecoration(new DividerItemDecoration(view_forward.getContext(), DividerItemDecoration.VERTICAL));
        view_forward.setItemAnimator(new DefaultItemAnimator());
        btnClose = findViewById(R.id.btnClose);
        btnClose.setImageResource(R.drawable.ic_close_white_24dp);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnForward = findViewById(R.id.btnForward);
        btnForward.setImageResource(R.drawable.ic_send_white);
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forwardMessage();
            }
        });
    }

    private Cursor getAllItems() {
        return database.query(DBInfo.Department, null, null, null, null, null, DBInfo.DEPARTMENTID + " ASC");
    }

    public void forwardMessage(){
        final String[] singleChoiceItems = getResources().getStringArray(R.array.dialog_single_choice_array);
        int itemSelected = 0;
        dialog = new AlertDialog.Builder(this)
                .setTitle("Select priority level")
                .setSingleChoiceItems(singleChoiceItems, itemSelected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                        for (int i = 0; i < deptList.size(); i++) {
                            assignMessage(MessageID, deptList.get(i), singleChoiceItems[selectedIndex]);
                        }
                        dialogInterface.cancel();
                        dialogInterface.dismiss();
                        finish();
                    }
                })
                .setCancelable(true)
                .show();
    }

    public void assignMessage(String MessageID, String DepartmentID, String Priority) {
        String url = Constants.URL + Constants.ASSIGNMESSAGE + "&MessageID=" + MessageID + "&DepartmentID=" + DepartmentID + "&PriorityLevel=" + Priority;
        Log.e("updateMessageToResolved", url);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.length() > 5) {
                            Toast.makeText(ForwardMessage.this, "Sent", Toast.LENGTH_SHORT).show();
                            Log.e("SENT", response);

                        } else {
                            Log.e("FAILED", response);
                            Toast.makeText(ForwardMessage.this, "Failed to send. Please check your connection.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                });
        SingletonRequest.getInstance(this).addToRequestQueue(postRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
