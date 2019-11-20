package gte.com.itextmosimayor.activities.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.adapters.ForwardMessageAdapter;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.database.Queries;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.modules.SingletonRequest;
import gte.com.itextmosimayor.modules.StringFormat;


public class ForwardMessage extends Activity {

    StringFormat stringFormat = new StringFormat();
    String MessageID;
    String Remarks;
    RecyclerView view_forward;
    ForwardMessageAdapter forwardMessageAdapter;
    Cursor c;
    ImageButton btnClose;
    ImageButton btnForward;
    private ArrayList<String> deptList = new ArrayList<>();
    AlertDialog dialog;
    String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward_message);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            MessageID = extras.getString("MessageID");
            Remarks = extras.getString("Remarks");
        }

        c = Queries.getInstance(this).getAllDepartments();
        view_forward = findViewById(R.id.view_forward);
        view_forward.setHasFixedSize(true);
        forwardMessageAdapter = new ForwardMessageAdapter(c);
        forwardMessageAdapter.setOnItemCheckListener(new ForwardMessageAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(String departmentID) {
                if (!deptList.contains(departmentID))
                    deptList.add(departmentID);

                btnForward.setEnabled(true);
                btnForward.setImageResource(R.drawable.ic_send_white);
            }

            @Override
            public void onItemUncheck(String departmentID) {
                deptList.remove(departmentID);

                if (deptList.size() == 0) {
                    btnForward.setEnabled(false);
                    btnForward.setImageResource(R.drawable.ic_send_disabled);
                }
            }
        });
        view_forward.setLayoutManager(new LinearLayoutManager(this));
        view_forward.setAdapter(forwardMessageAdapter);
        view_forward.addItemDecoration(new DividerItemDecoration(view_forward.getContext(), DividerItemDecoration.VERTICAL));
        view_forward.setItemAnimator(new DefaultItemAnimator());
        btnClose = findViewById(R.id.btnClose);
        btnClose.setImageResource(R.drawable.ic_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnForward = findViewById(R.id.btnForward);
        btnForward.setImageResource(R.drawable.ic_send_disabled);
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forwardMessage();
            }
        });
        btnForward.setEnabled(false);

        fullName = Preference.getInstance(this).getPrefString(DBInfo.FIRSTNAME) + " " + Preference.getInstance(this).getPrefString(DBInfo.LASTNAME);
        fullName = fullName.replace(" ", "%20");
    }

    public void forwardMessage() {
        final String[] items = {"Normal", "Important"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select priority level");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                for (int i = 0; i < deptList.size(); i++) {
                    assignMessage(MessageID, deptList.get(i), items[item], fullName);
                }
                dialog.cancel();
                dialog.dismiss();
                Intent intent = new Intent();
                setResult(3, intent);
                finish();
            }
        }).show();
    }

    public void assignMessage(String MessageID, String DepartmentID, String Priority, String fullName) {
        String url;
        if (Remarks.matches("") || (Remarks.trim().isEmpty()))
            url = Constants.URL + Constants.ASSIGNMESSAGE + "&MessageID=" + MessageID + "&DepartmentID=" + DepartmentID + "&PriorityLevel=" + Priority;
        else {
            Remarks = stringFormat.convertSpace(Remarks);
            url = Constants.URL + Constants.ASSIGNMESSAGE2 + "&MessageID=" + MessageID + "&DepartmentID=" + DepartmentID + "&PriorityLevel=" + Priority + "&Remarks=" + Remarks
                    + "&SentByy=" + fullName;
        }

        Log.e("assignMessage", url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 5) {
                    Toast.makeText(ForwardMessage.this, "Sent", Toast.LENGTH_SHORT).show();
                    Log.e("SENT", response);
                } else {
                    Log.e("FAILED", response);
//                    Toast.makeText(ForwardMessage.this, "Failed to send. Please check your connection.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ForwardMessage.this, "Failed to send. Please check your connection.", Toast.LENGTH_SHORT).show();
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
        c.close();
    }
}