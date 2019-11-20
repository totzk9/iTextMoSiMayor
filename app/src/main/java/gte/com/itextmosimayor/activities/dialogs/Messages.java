package gte.com.itextmosimayor.activities.dialogs;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.adapters.MessagesAdapter;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.database.DatabaseHandler;
import gte.com.itextmosimayor.database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.database.Queries;
import gte.com.itextmosimayor.modules.CheckConnection;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.modules.SingletonRequest;

public class Messages extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    DatabaseHandler db;
    MessagesAdapter MessagesAdapter;
    String MobileNumber;
    String status;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView view_unassigned;
    Cursor c;
    AlertDialog dialog;
    ImageButton btnBack;
    TextView txtNumber;
    ImageButton btnDeleteAll;
    Bundle extras;
    boolean firstLaunch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        firstLaunch = true;
        extras = getIntent().getExtras();
        if (extras != null) {
            MobileNumber = extras.getString("MobileNumber");
            status = extras.getString("Status");
        }

        btnBack = findViewById(R.id.btnBack);
        txtNumber = findViewById(R.id.txtNumber);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);
        view_unassigned = findViewById(R.id.view_unassigned);

        btnBack.setImageResource(R.drawable.ic_arrow_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtNumber.setText(MobileNumber);

        if (status.equals("Open")) {
            btnDeleteAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteAll();
                }
            });
            btnDeleteAll.setImageResource(R.drawable.ic_delete_all);
        } else
            btnDeleteAll.setVisibility(View.GONE);

        db = new DatabaseHandler(this);
        db.TRUNCATE_MESSAGE();
        c = Queries.getInstance(this).getMessages();

        view_unassigned.setHasFixedSize(true);
        MessagesAdapter = new MessagesAdapter(c, status);
        view_unassigned.setAdapter(MessagesAdapter);
        view_unassigned.setLayoutManager(new LinearLayoutManager(this));
        view_unassigned.setItemAnimator(new DefaultItemAnimator());

        // SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                switch (status) {
                    case "Open":
                        fetchMessages(Preference.getInstance(Messages.this).getPrefInt(DBInfo.MAYORID), MobileNumber);
                        break;
                    case "Deleted":
                        fetchDeletedMessage(Preference.getInstance(Messages.this).getPrefInt(DBInfo.MAYORID), MobileNumber);
                        break;
                    case "Confidential":
                        fetchConfidentialMessageThread(MobileNumber);
                        break;
                }
            }
        });
    }

    private void deleteAll() {
        new AlertDialog.Builder(this)
                .setTitle("Delete all?")
                .setMessage("This will delete all messages.")
                .setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {
                                c.moveToFirst();
                                do {
                                    String messageID = c.getString(c.getColumnIndex(DBInfo.MESSAGEID));
                                    deleteMessage(messageID);
                                    c.moveToNext();
                                } while (!c.isAfterLast());
                                finish();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
    }

    public void initializeRecyclerView() {
        MessagesAdapter = new MessagesAdapter(c, status);
        view_unassigned.setAdapter(MessagesAdapter);
        MessagesAdapter.setOnItemClickListener(new MessagesAdapter.OnItemClickListener() {
            @Override
            public void onClickimgCheck(String messageID) {
                updateMessageToResolved(messageID);
            }

            @Override
            public void onClickimgForward(String messageID) {
                showDialog(messageID);
            }

            @Override
            public void onClickimgDelete(String messageID) {
                deleteMessage(messageID);
            }

            @Override
            public void onClickimgConfidential(String messageID) {
                moveToConfidential(messageID);
            }
        });
        MessagesAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        switch (status) {
            case "Open":
                fetchMessages(Preference.getInstance(Messages.this).getPrefInt(DBInfo.MAYORID), MobileNumber);
                break;
            case "Deleted":
                fetchDeletedMessage(Preference.getInstance(Messages.this).getPrefInt(DBInfo.MAYORID), MobileNumber);
                break;
            case "Confidential":
                fetchConfidentialMessageThread(MobileNumber);
                break;
        }
    }

    public void updateMessageToResolved(String MessageID) {
        final String url = Constants.URL + Constants.UPDATEMESSAGETORESOLVED1 + "&MessageID=" + MessageID;
        Log.e("updateMessageToResolved", url);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.length() > 3) {
                            Toast.makeText(Messages.this, "Message resolved", Toast.LENGTH_SHORT).show();
                            fetchMessages(Preference.getInstance(Messages.this).getPrefInt(DBInfo.MAYORID), MobileNumber);
                        } else
                            Log.d("Error.Response", response + "\n\n" + url);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
        SingletonRequest.getInstance(this).addToRequestQueue(postRequest);
    }

    public void deleteMessage(String MessageID) {
        final String url = Constants.URL + Constants.DELETEMESSAGE + "&MessageID=" + MessageID;
        Log.e("deleteMessage", url);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.length() > 3) {
                            Toast.makeText(Messages.this, "Message deleted", Toast.LENGTH_SHORT).show();
                            fetchMessages(Preference.getInstance(Messages.this).getPrefInt(DBInfo.MAYORID), MobileNumber);
                        } else
                            Log.d("Else", response + "\n" + url);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
        SingletonRequest.getInstance(this).addToRequestQueue(postRequest);
    }

    public void fetchMessages(int mayorID, String mobileNumber) {
        String REQUEST_TAG = Constants.URL + Constants.FETCHUNASSIGNEDMESSAGETHREAD + "&MayorID=" + mayorID + "&ClientMobileNumber=" + mobileNumber;
        Log.e("URL", REQUEST_TAG);

        if (CheckConnection.isNetworkConnected(this)) {
            db.TRUNCATE_MESSAGE();
            final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, REQUEST_TAG, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if (response.toString().length() > 3) {
                        try {
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject obj = response.getJSONObject(i);
                                String messageID = obj.getInt("MessageID") + "";
                                String dateSent = obj.getString("DateSent");
                                String clientMobileNumber = obj.getString("ClientMobileNumber");
                                String content = obj.getString("Content");
                                String mayorID = obj.getInt("MayorID") + "";
                                String priorityLevel = obj.getString("PriorityLevel");
                                String isAssigned = obj.getInt("isAssigned") + "";
                                String status = obj.getString("Status");
                                db.INSERT_MESSAGE(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status);
                            }
                            c = Queries.getInstance(Messages.this).getMessages();
                            initializeRecyclerView();
                        } catch (JSONException e) {
                            Log.e("Fail caught", e.toString());
                        }
                    } else
                        finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Messages.this, "Failed", Toast.LENGTH_LONG).show();
                    Log.e("error history", "" + error.getMessage());
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
            SingletonRequest.getInstance(this).addToRequestQueue(request);
        } else {
            Toast.makeText(this, "Please check internet connection.", Toast.LENGTH_LONG).show();
            initializeRecyclerView();
        }
    }

    public void fetchDeletedMessage(int mayorID, String mobileNumber) {
        String REQUEST_TAG = Constants.URL + Constants.FETCHDELETEDMESSAGETHREAD + "&MayorID=" + mayorID + "&ClientMobileNumber=" + mobileNumber;
        Log.e("URL", REQUEST_TAG);

        if (CheckConnection.isNetworkConnected(this)) {
            db.TRUNCATE_MESSAGE();
            final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, REQUEST_TAG, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if (response.toString().length() > 3) {
                        try {
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject obj = response.getJSONObject(i);
                                String messageID = obj.getInt("MessageID") + "";
                                String dateSent = obj.getString("DateSent");
                                String clientMobileNumber = obj.getString("ClientMobileNumber");
                                String content = obj.getString("Content");
                                String mayorID = obj.getInt("MayorID") + "";
                                String priorityLevel = obj.getString("PriorityLevel");
                                String isAssigned = obj.getInt("isAssigned") + "";
                                String status = obj.getString("Status");
                                db.INSERT_MESSAGE(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status);
                            }
                            c = Queries.getInstance(Messages.this).getMessages();
                            initializeRecyclerView();
                        } catch (JSONException e) {
                            Log.e("Fail caught", e.toString());
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Messages.this, "Failed", Toast.LENGTH_LONG).show();
                    Log.e("error history", error.getMessage());
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
            SingletonRequest.getInstance(this).addToRequestQueue(request);
        } else {
            Toast.makeText(this, "Please check internet connection.", Toast.LENGTH_LONG).show();
            initializeRecyclerView();
        }
    }

    public void fetchConfidentialMessageThread(String mobileNumber) {
        String REQUEST_TAG = Constants.URL + Constants.FETCHCONFIDENTIALMESSAGETHREAD + "&ClientMobileNumber=" + mobileNumber;
        Log.e("URL", REQUEST_TAG);

        if (CheckConnection.isNetworkConnected(this)) {
            db.TRUNCATE_MESSAGE();
            final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, REQUEST_TAG, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if (response.toString().length() > 3) {
                        try {
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject obj = response.getJSONObject(i);
                                String messageID = obj.getInt("ConfidentialMessageID") + "";
                                String dateSent = obj.getString("DateSent");
                                String clientMobileNumber = obj.getString("ClientMobileNumber");
                                String content = obj.getString("Content");
                                db.INSERT_MESSAGE(messageID, dateSent, clientMobileNumber, content, "", "", "", "");
                            }
                            c = Queries.getInstance(Messages.this).getMessages();
                            if (c.getCount() == 0)
                                finish();
                            initializeRecyclerView();
                        } catch (JSONException e) {
                            Log.e("Fail caught", e.toString());
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Messages.this, "Failed", Toast.LENGTH_LONG).show();
                    Log.e("error history", error.getMessage());
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
            SingletonRequest.getInstance(this).addToRequestQueue(request);
        } else {
            Toast.makeText(this, "Please check internet connection.", Toast.LENGTH_LONG).show();
            initializeRecyclerView();
        }
    }

    private void showDialog(final String messageID) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View dialogRemarks = layoutInflaterAndroid.inflate(R.layout.dialog_add_remarks, null);
        final EditText txtRemarks = dialogRemarks.findViewById(R.id.txtRemarks);
        txtRemarks.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    forward(messageID, txtRemarks.getText().toString());
                    return true;
                }
                return false;
            }
        });
        dialog = new AlertDialog.Builder(this)
                .setView(dialogRemarks)
                .setCancelable(true)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        forward(messageID, txtRemarks.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Skip", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        forward(messageID, "");
                    }
                })
                .show();
    }

    private void moveToConfidential(String msgID) {
        final String url = Constants.URL + Constants.MOVETOCONFIDENTIAL + "&MessageID=" + msgID;
        Log.e("moveToConfidential", url);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.length() > 3) {
                            Toast.makeText(Messages.this, "Message successfully moved to Confidential folder.", Toast.LENGTH_SHORT).show();
                            fetchMessages(Preference.getInstance(Messages.this).getPrefInt(DBInfo.MAYORID), MobileNumber);
                        } else
                            Log.d("Else", response + "\n" + url);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
        SingletonRequest.getInstance(this).addToRequestQueue(postRequest);
    }

    private void forward(String msgID, String remarks) {
        Intent intent = new Intent(Messages.this, ForwardMessage.class);
        Bundle bundle = new Bundle();
        bundle.putString("MessageID", msgID);
        bundle.putString("Remarks", remarks);
        intent.putExtras(bundle);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 3) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    fetchMessages(Preference.getInstance(Messages.this).getPrefInt(DBInfo.MAYORID), MobileNumber);
                }
            }, 2500);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        Intent intent = new Intent();
        setResult(1, intent);
    }
}