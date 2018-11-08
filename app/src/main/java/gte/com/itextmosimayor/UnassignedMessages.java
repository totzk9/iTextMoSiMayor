package gte.com.itextmosimayor;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gte.com.itextmosimayor.Database.DatabaseHandler;
import gte.com.itextmosimayor.Database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.dialogs.ForwardMessage;
import gte.com.itextmosimayor.adapter.UnassignedMessagesAdapter;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.modules.Module;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.requests.SingletonRequest;

public class UnassignedMessages extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    DatabaseHandler db;
    SQLiteDatabase database;
    UnassignedMessagesAdapter unassignedMessagesAdapter;
    String MobileNumber;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView view_unassigned;
    Cursor c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unassigned_messages);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            MobileNumber = extras.getString("MobileNumber");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle(MobileNumber);

        db = new DatabaseHandler(this);
        database = db.getWritableDatabase();
        c = getAllItems();
        view_unassigned = findViewById(R.id.view_unassigned);
        view_unassigned.setHasFixedSize(true);
        unassignedMessagesAdapter = new UnassignedMessagesAdapter(this, c);
        view_unassigned.setLayoutManager(new LinearLayoutManager(this));
//        view_unassigned.addItemDecoration(new DividerItemDecoration(view_unassigned.getContext(), DividerItemDecoration.VERTICAL));
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
                fetchUnassignedMessages(Preference.getInstance(UnassignedMessages.this).getPrefInt(DBInfo.MAYORID), MobileNumber);
            }
        });
    }

    public void initializeRecyclerView() {
        unassignedMessagesAdapter = new UnassignedMessagesAdapter(this, c);
        view_unassigned.setAdapter(unassignedMessagesAdapter);
        unassignedMessagesAdapter.setOnItemClickListener(new UnassignedMessagesAdapter.OnItemClickListener() {
            @Override
            public void onClickimgCheck(String messageID) {
                updateMessageToResolved(messageID);
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        fetchUnassignedMessages(Preference.getInstance(UnassignedMessages.this).getPrefInt(DBInfo.MAYORID), MobileNumber);
                    }
                });
            }

            @Override
            public void onClickimgForward(String messageID) {
                Intent intent = new Intent(UnassignedMessages.this, ForwardMessage.class);
                intent.putExtra("MessageID", messageID);
                startActivity(intent);
            }
        });
        unassignedMessagesAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        fetchUnassignedMessages(Preference.getInstance(this).getPrefInt(DBInfo.MAYORID), MobileNumber);
    }

    private Cursor getAllItems(){
        return database.query(DBInfo.Message, null, null, null, null, null, DBInfo.MESSAGEID + " DESC");
    }

    public void updateMessageToResolved(String MessageID) {
        String url = Constants.URL + Constants.UPDATEMESSAGETORESOLVED + "&MessageID=" + MessageID;
        Log.e("updateMessageToResolved", url);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.length() < 10)
                        Toast.makeText(UnassignedMessages.this, "Ticket resolved", Toast.LENGTH_SHORT).show();
                        long cnt = db.MESSAGE_ENTRIES_COUNT();
                        if (cnt <= 1){
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        swipeRefreshLayout.setRefreshing(false);

                    }
                });
        SingletonRequest.getInstance(this).addToRequestQueue(postRequest);
    }

    public void fetchUnassignedMessages(int mayorID, String mobileNumber) {
        String REQUEST_TAG = Constants.URL + Constants.FETCHUNASSIGNEDMESSAGETHREAD + "&MayorID=" + mayorID + "&ClientMobileNumber=" + mobileNumber;
        Log.e("URL", REQUEST_TAG);

        if (Module.isNetworkConnected(this)) {
            db.TRUNCATE_MESSAGE(db);
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
                                String assignedToID = obj.getInt("AssignedToID") + "";
                                String status = obj.getString("Status");
                                db.INSERT_MESSAGE(db, messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, assignedToID, status);
                                c = getAllItems();
                                initializeRecyclerView();
                            }
                        } catch (JSONException e) {
                            Log.e("Fail caught", e.toString());
                        }
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(UnassignedMessages.this, "Failed", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        swipeRefreshLayout.setRefreshing(true);
        fetchUnassignedMessages(Preference.getInstance(UnassignedMessages.this).getPrefInt(DBInfo.MAYORID), MobileNumber);
    }
}
