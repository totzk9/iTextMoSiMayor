package gte.com.itextmosimayor.activities.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.adapters.ResponseAdapter;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.database.DatabaseHandler;
import gte.com.itextmosimayor.database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.database.Queries;
import gte.com.itextmosimayor.modules.CheckConnection;
import gte.com.itextmosimayor.modules.ColumnIndexCache;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.modules.SingletonRequest;

public class Respond extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    ImageButton btnBack;
    ImageButton btnResolved;
    ImageButton btnRespond;
    ImageButton btnRefresh;
    TextView txtNumber;
    RecyclerView recyclerView;
    Cursor c;
    DatabaseHandler db;
    ResponseAdapter responseAdapter;
    String MobileNumber;
    SwipeRefreshLayout swipeRefreshLayout;
    ColumnIndexCache cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            MobileNumber = extras.getString("MobileNumber");

        db = new DatabaseHandler(this);
        c = Queries.getInstance(this).getConvo();

        btnBack = findViewById(R.id.btnBack);
        btnResolved = findViewById(R.id.btnResolved);
        btnRespond = findViewById(R.id.btnRespond);
        btnRefresh = findViewById(R.id.btnRefresh);
        txtNumber = findViewById(R.id.txtNumber);
        recyclerView = findViewById(R.id.recycler_view_message);

        btnResolved.setImageResource(R.drawable.ic_resolved);
        btnBack.setImageResource(R.drawable.ic_arrow_back);
        btnRespond.setImageResource(R.drawable.ic_send_white);
        btnRefresh.setImageResource(R.drawable.ic_refresh);
        txtNumber.setText(MobileNumber);

        btnRespond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeSmsMessage();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        btnResolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!c.moveToFirst()) return;
                do {
                    cache = new ColumnIndexCache();
                    String contentID = c.getString(cache.getColumnIndex(c, "ContentID"));
                    String convoID = c.getString(cache.getColumnIndex(c, "ConvoID"));
                    cache.clear();
                    updateMessageToResolved(contentID, convoID);
                } while (c.moveToNext());
            }
        });

        recyclerView.setHasFixedSize(true);
        responseAdapter = new ResponseAdapter(c, MobileNumber);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(responseAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        responseAdapter.notifyDataSetChanged();

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
                fetchConvo();
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        fetchConvo();
    }

    public void updateMessageToResolved(String ConvoID, String ContentID) {
        swipeRefreshLayout.setRefreshing(true);
        String url = Constants.URL + Constants.UPDATEMESSAGETORESOLVED2 + "&ConvoID=" + ConvoID + "&ContentID=" + ContentID;
        Log.e("updateMessageToResolved", url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                });
        SingletonRequest.getInstance(this).addToRequestQueue(postRequest);
        swipeRefreshLayout.setRefreshing(false);
    }

    public void composeSmsMessage() {
        String fullName = Preference.getInstance(this).getPrefString(DBInfo.FIRSTNAME) + " " + Preference.getInstance(this).getPrefString(DBInfo.LASTNAME);
        String mes = "Hi! This is " + fullName + " and we have received your concern. Please tell us more about your problem.\n#iTextMoSiMayor";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("sms_body", mes);
        intent.putExtra("address", MobileNumber);
        intent.setData(Uri.parse("sms:"));
        startActivity(intent);
    }

    public void fetchConvo() {
        String REQUEST_TAG = Constants.URL + Constants.FETCHCONVOBYDEPARTMENT + "&DepartmentID=" + Preference.getInstance(this).
                getPrefString(DBInfo.DEPARTMENTID) + "&MobileNumber=" + MobileNumber;
        Log.e("REQUEST_TAG", REQUEST_TAG);

        if (CheckConnection.isNetworkConnected(this)) {
            db.TRUNCATE_CONVO_KUAN();
            final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, REQUEST_TAG, null, new com.android.volley.Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if (response.toString().length() > 3) {
                        try {
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject obj = response.getJSONObject(i);
                                String messageContent = obj.getString("MessageContent");
                                String dateSent = obj.getString("DateSent");
                                String sentBy = obj.getString("SentBy");
                                String convoID = obj.getString("ConvoID");
                                String contentID = obj.getString("ContentID");
                                db.INSERT_CONVO_KUAN(messageContent, dateSent, sentBy, convoID, contentID);
                                c = Queries.getInstance(Respond.this).getConvo();
                                initializeRecyclerView();
                            }
                        } catch (JSONException e) {
                            Log.e("Fail caught", e.toString());
                        }
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Respond.this, "Unable to connect to server. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
            SingletonRequest.getInstance(this).addToRequestQueue(request);
        } else {
            Toast.makeText(this, "Please check your internet connection.", Toast.LENGTH_LONG).show();
            initializeRecyclerView();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    public void initializeRecyclerView() {
        responseAdapter = new ResponseAdapter(c, MobileNumber);
        recyclerView.setAdapter(responseAdapter);
        responseAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        c.close();
        db.TRUNCATE_CONVO_KUAN();
        responseAdapter.notifyDataSetChanged();
//        db.close();
        Intent intent = new Intent();
        setResult(1, intent);
    }
}