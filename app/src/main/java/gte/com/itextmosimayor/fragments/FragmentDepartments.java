package gte.com.itextmosimayor.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import gte.com.itextmosimayor.adapter.FragmentDepartmentsAdapter;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.dialogs.AddNewDepartment;
import gte.com.itextmosimayor.dialogs.ViewDepartment;
import gte.com.itextmosimayor.modules.Module;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.requests.SingletonRequest;

public class FragmentDepartments extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static FragmentDepartments newInstance() {
        return new FragmentDepartments();
    }

    SQLiteDatabase database;
    RecyclerView view_departments;
    DatabaseHandler db;
    SwipeRefreshLayout swipeRefreshLayout;
    FragmentDepartmentsAdapter fragmentDepartmentsAdapter;
    Cursor c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_departments, container, false);

        db = new DatabaseHandler(getContext());
        database = db.getWritableDatabase();
        view_departments = view.findViewById(R.id.view_departments);
        c = getAllItems();

        view_departments.setHasFixedSize(true);
        fragmentDepartmentsAdapter = new FragmentDepartmentsAdapter(getContext(), c);
        view_departments.setLayoutManager(new LinearLayoutManager(getContext()));
        view_departments.setAdapter(fragmentDepartmentsAdapter);
        view_departments.addItemDecoration(new DividerItemDecoration(view_departments.getContext(), DividerItemDecoration.VERTICAL));
        view_departments.setItemAnimator(new DefaultItemAnimator());

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
                fetchDepartments(Preference.getInstance(getContext()).getPrefInt(DBInfo.MAYORID));
            }
        });

        long cnt = db.DEPARTMENT_ENTRIES_COUNT();
        if (cnt == 0L){
            AddNewDepartment dialog = new AddNewDepartment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            dialog.show(ft, AddNewDepartment.TAG);
        }

        return view;
    }

    public void initializeRecyclerView() {
        fragmentDepartmentsAdapter = new FragmentDepartmentsAdapter(getContext(), c);
        view_departments.setAdapter(fragmentDepartmentsAdapter);
        fragmentDepartmentsAdapter.setOnItemClickListener(new FragmentDepartmentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String DepartmentID) {
                Intent intent = new Intent(getActivity(), ViewDepartment.class);
                intent.putExtra("DepartmentID", DepartmentID);
                startActivity(intent);
            }
        });
        fragmentDepartmentsAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        fetchDepartments(Preference.getInstance(getContext()).getPrefInt(DBInfo.MAYORID));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_newdepartment:
                AddNewDepartment dialog = new AddNewDepartment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, AddNewDepartment.TAG);
        }
        return super.onOptionsItemSelected(item);
    }

    private Cursor getAllItems() {
        return database.query(DBInfo.Department, null, null, null, null, null, DBInfo.DEPARTMENTID + " ASC");
    }

    public void fetchDepartments(int mayorID) {
        String REQUEST_TAG = Constants.URL + Constants.FETCHDEPARTMENTS + "&MayorID=" + mayorID;

        if(Module.isNetworkConnected(getContext())) {
            db.TRUNCATE_DEPARTMENT(db);
            final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, REQUEST_TAG, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if (response.toString().length() > 3) {
                        try {
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject obj = response.getJSONObject(i);
                                Object id = obj.getString("DepartmentID");
                                Object name = obj.getString("DepartmentName");
                                Object street = obj.getString("StreetName");
                                Object brgy = obj.getString("Barangay");
                                Object municipal = obj.getString("Municipality");
                                Object province = obj.getString("Province");
                                Object zipcode = obj.getString("ZipCode");
                                Object head = obj.getString("DepartmentHead");
                                Object code = obj.getString("DepartmentCode");

                                db.INSERT_DEPARTMENT(db, id, name, street, brgy, municipal, province, zipcode, head, code);
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
                    Log.e("onErrorResponse", error.toString());
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