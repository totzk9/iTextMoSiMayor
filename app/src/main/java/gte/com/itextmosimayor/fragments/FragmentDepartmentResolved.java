package gte.com.itextmosimayor.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import gte.com.itextmosimayor.adapter.FragmentResolvedAdapter;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.modules.Module;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.requests.SingletonRequest;

public class FragmentDepartmentResolved extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    FragmentResolvedAdapter fragmentResolvedAdapter;
    DatabaseHandler db;
    SQLiteDatabase database;
    RecyclerView view_resolved;
    SwipeRefreshLayout swipeRefreshLayout;
    Cursor c;

    public static FragmentDepartmentResolved newInstance() {
        return new FragmentDepartmentResolved();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resolved, container, false);

        db = new DatabaseHandler(getContext());
        database = db.getWritableDatabase();
        c = getAllItems();

        view_resolved = view.findViewById(R.id.view_resolved);
        view_resolved.setHasFixedSize(true);
        fragmentResolvedAdapter = new FragmentResolvedAdapter(getContext(), c);
        view_resolved.setLayoutManager(new LinearLayoutManager(getContext()));
        view_resolved.setAdapter(fragmentResolvedAdapter);
        view_resolved.addItemDecoration(new DividerItemDecoration(view_resolved.getContext(), DividerItemDecoration.VERTICAL));
        view_resolved.setItemAnimator(new DefaultItemAnimator());

//        fragmentResolvedAdapter.setOnItemClickListener(new FragmentResolvedAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(String MessageID) {
//                Intent intent = new Intent(getActivity(), ForwardMessage.class);
//                intent.putExtra("MessageID", MessageID);
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
                fetchResolvedMessages(Preference.getInstance(getContext()).getPrefInt(DBInfo.MAYORID));
            }
        });
        return view;
    }

    public void initializeRecyclerView() {
        fragmentResolvedAdapter = new FragmentResolvedAdapter(getContext(), c);
        view_resolved.setAdapter(fragmentResolvedAdapter);
        fragmentResolvedAdapter.setOnItemClickListener(new FragmentResolvedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String MessageID) {
//                Intent intent = new Intent(getActivity(), UnassignedMessages.class);
//                intent.putExtra("MessageID", MobileNumber);
//                startActivity(intent);
            }
        });
        fragmentResolvedAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        fetchResolvedMessages(Preference.getInstance(getContext()).getPrefInt(DBInfo.MAYORID));
    }

    private Cursor getAllItems() {
        return database.query(DBInfo.ResolvedMessage, null, null, null, null, null, DBInfo.MESSAGEID + " DESC");
    }

    public void fetchResolvedMessages(int mayorID) {
        String REQUEST_TAG = Constants.URL + Constants.FETCHRESOLVEDMESSAGE + "&MayorID=" + mayorID;

        if(Module.isNetworkConnected(getContext())) {
            db.TRUNCATE_RESOLVED_MESSAGE(db);
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
                            db.INSERT_RESOLVED_MESSAGE(db, messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, assignedToID, status);
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
