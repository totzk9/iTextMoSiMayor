package gte.com.itextmosimayor.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.activities.dialogs.AddNewDepartment;
import gte.com.itextmosimayor.activities.dialogs.ViewDepartment;
import gte.com.itextmosimayor.adapters.FragmentDepartmentsAdapter;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.database.DatabaseHandler;
import gte.com.itextmosimayor.database.Queries;
import gte.com.itextmosimayor.modules.CheckConnection;
import gte.com.itextmosimayor.modules.SingletonRequest;

public class FragmentDepartments extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView view_departments;
    SwipeRefreshLayout swipeRefreshLayout;
    private FragmentDepartmentsAdapter fragmentDepartmentsAdapter;
    private Cursor c;
    private DatabaseHandler db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department, container, false);

        db = new DatabaseHandler(getContext());
        view_departments = view.findViewById(R.id.recyclerView);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        c = Queries.getInstance(getContext()).getAllDepartments();

        view_departments.setHasFixedSize(true);
        fragmentDepartmentsAdapter = new FragmentDepartmentsAdapter(c);
        view_departments.setLayoutManager(new LinearLayoutManager(getContext()));
        view_departments.setAdapter(fragmentDepartmentsAdapter);
        view_departments.addItemDecoration(new DividerItemDecoration(view_departments.getContext(), DividerItemDecoration.VERTICAL));
        view_departments.setItemAnimator(new DefaultItemAnimator());

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
                fetchDepartments();
                initializeRecyclerView();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentActivity(v);

            }
        });


//        long cnt = db.DEPARTMENT_ENTRIES_COUNT();
//        if (cnt == 0L)
//            presentActivity(view);

        return view;
    }

    private void initializeRecyclerView() {
        fragmentDepartmentsAdapter = new FragmentDepartmentsAdapter(c);
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

    private void presentActivity(View view) {
        final ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(Objects.requireNonNull(getActivity()), view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        final Intent intent = new Intent(getContext(), AddNewDepartment.class);
        intent.putExtra(AddNewDepartment.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(AddNewDepartment.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityCompat.startActivity(getContext(), intent, options.toBundle());
            }
        },500);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        fetchDepartments();
        c = Queries.getInstance(getContext()).getAllDepartments();
        initializeRecyclerView();
    }

    private void fetchDepartments() {
        String REQUEST_TAG = Constants.URL + Constants.FETCHDEPARTMENTS + "&MayorID=1";

        if (CheckConnection.isNetworkConnected(getContext())) {
            db.TRUNCATE_DEPARTMENT();
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
                                db.INSERT_DEPARTMENT(id, name, street, brgy, municipal, province, zipcode, head, code);
                            }
                            c = Queries.getInstance(getContext()).getAllDepartments();
                            initializeRecyclerView();
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