package gte.com.itextmosimayor.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gte.com.itextmosimayor.Database.DatabaseHandler;
import gte.com.itextmosimayor.Database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.adapter.FragmentImportantAdapter;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.modules.Module;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.requests.SingletonRequest;

public class FragmentImportant extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {
    DatabaseHandler db;
    SwipeRefreshLayout swipeRefreshLayout;
    SQLiteDatabase database;
    RecyclerView view_important;
    FragmentImportantAdapter fragmentImportantAdapter;
    Cursor c;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_important, container, false);

        db = new DatabaseHandler(getContext());
        database = db.getWritableDatabase();
        c = getAllItems();

        view_important = view.findViewById(R.id.view_important);
        view_important.setHasFixedSize(true);
        fragmentImportantAdapter = new FragmentImportantAdapter(getContext(), c);
        view_important.setLayoutManager(new LinearLayoutManager(getContext()));
        view_important.setAdapter(fragmentImportantAdapter);
        view_important.addItemDecoration(new DividerItemDecoration(view_important.getContext(), DividerItemDecoration.VERTICAL));
        view_important.setItemAnimator(new DefaultItemAnimator());

//        fragmentOpenAdapter.setOnItemClickListener(new FragmentOpenAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(String MobileNumber) {
//                Intent intent = new Intent(getActivity(), ForwardMessage.class);
//                intent.putExtra("MobileNumber", MobileNumber);
//                startActivity(intent);
//            }
//        });

        // SwipeRefreshLayout
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                fetchImportantMessages(Preference.getInstance(getContext()).getPrefInt(DBInfo.MAYORID));
            }
        });
        return view;
    }

    public void initializeRecyclerView() {
        fragmentImportantAdapter = new FragmentImportantAdapter(getContext(), c);
        view_important.setAdapter(fragmentImportantAdapter);
        fragmentImportantAdapter.setOnItemClickListener(new FragmentImportantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String MessageID) {
//                Intent intent = new Intent(getActivity(), UnassignedMessages.class);
//                intent.putExtra("MobileNumber", MobileNumber);
//                startActivity(intent);

                //start activity for convo

            }
        });
        fragmentImportantAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        fetchImportantMessages(Preference.getInstance(getContext()).getPrefInt(DBInfo.MAYORID));
    }

    private Cursor getAllItems() {
        return database.query(DBInfo.ImportantMessage, null, null, null, null, null, DBInfo.MESSAGEID + " DESC");
    }

    public void fetchImportantMessages(int mayorID) {
        String REQUEST_TAG = Constants.URL + Constants.FETCHIMPORTANTMESSAGE + "&MayorID=" + mayorID;

        if(Module.isNetworkConnected(getContext())) {
            db.TRUNCATE_IMPORTANT_MESSAGE(db);
            final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, REQUEST_TAG, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try{
                        // Loop through the array elements
                        for(int i=0;i<response.length();i++){
                            // Get current json object
                            JSONObject obj = response.getJSONObject(i);
                            String messageID = obj.getInt("MessageID") +"";
                            String dateSent = obj.getString("DateSent");
                            String clientMobileNumber = obj.getString("ClientMobileNumber");
                            String content = obj.getString("Content");
                            String mayorID = obj.getInt("MayorID") + "";
                            String priorityLevel = obj.getString("PriorityLevel");
                            String assignedToID = obj.getInt("AssignedToID") +"";
                            String status = obj.getString("Status");
                            db.INSERT_IMPORTANT_MESSAGE(db, messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, assignedToID, status);
                            c = getAllItems();
                            initializeRecyclerView();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Unable to connect to server. Please try again.", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
            SingletonRequest.getInstance(getContext()).addToRequestQueue(request);
        } else {
            Toast.makeText(getContext(), "Please check internet connection.", Toast.LENGTH_LONG).show();
            initializeRecyclerView();
        }
    }
}
