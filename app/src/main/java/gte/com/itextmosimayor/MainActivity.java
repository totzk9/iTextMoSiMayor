package gte.com.itextmosimayor;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.dialogs.AddNewDepartment;
import gte.com.itextmosimayor.fragments.FragmentAll;
import gte.com.itextmosimayor.fragments.FragmentDepartments;
import gte.com.itextmosimayor.fragments.FragmentResolved;
import gte.com.itextmosimayor.fragments.MessagesFragment;
import gte.com.itextmosimayor.modules.Module;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.requests.SingletonRequest;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FragmentAll fragmentAll;
    FragmentResolved fragmentResolved;
    FragmentDepartments fragmentDepartments;
    MessagesFragment messagesFragment;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    boolean isDepartments = false;

    SQLiteDatabase database;
    DatabaseHandler db;

//    TextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        database = db.getWritableDatabase();


        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
//        txtName = findViewById(R.id.txtName);
        fragmentAll = FragmentAll.newInstance();
        fragmentResolved = FragmentResolved.newInstance();
        fragmentDepartments = FragmentDepartments.newInstance();
        messagesFragment = MessagesFragment.newInstance();
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

//        String fullName = Preference.getInstance(this).getPrefString(DatabaseInfo.DBInfo.FIRSTNAME) + " " + Preference.getInstance(this).getPrefString(DatabaseInfo.DBInfo.LASTNAME);
//        txtName.setText(fullName);

        setSupportActionBar(toolbar);
        toggle.isDrawerIndicatorEnabled();
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, messagesFragment)
                .addToBackStack(messagesFragment.toString())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

        fetchDepartments(Preference.getInstance(this).getPrefInt(DBInfo.MAYORID));
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                drawer.openDrawer(GravityCompat.START);
//                drawer_layout.closeDrawer(GravityCompat.START)
//                drawer_layout.closeDrawer(GravityCompat.END)
                return true;


//            case R.id.home:
//                drawer.openDrawer(GravityCompat.START); // OPEN DRAWER
//                return true;

            case R.id.add_newdepartment:
                AddNewDepartment dialog = new AddNewDepartment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialog.show(ft, "AddNewDepartment");
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        if(isDepartments) {
            inflater.inflate(R.menu.departments_menu, menu);
        } else {
            inflater.inflate(R.menu.search_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_primary:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, messagesFragment)
                        .addToBackStack(messagesFragment.toString())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                isDepartments = false;
                toolbar.setTitle("Primary");
                invalidateOptionsMenu();
                break;

            case R.id.nav_resolved :
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragmentResolved)
                        .addToBackStack(fragmentResolved.toString())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                isDepartments = false;
                toolbar.setTitle("Resolved");
                invalidateOptionsMenu();
                break;

            case R.id.nav_all:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragmentAll)
                        .addToBackStack(fragmentAll.toString())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                isDepartments = false;
                toolbar.setTitle("All");
                invalidateOptionsMenu();
                break;


            case R.id.nav_departments :
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragmentDepartments)
                        .addToBackStack(fragmentDepartments.toString())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                isDepartments = true;
                toolbar.setTitle("Departments");
                invalidateOptionsMenu();
                break;

//            case R.id.nav_settings:
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void fetchDepartments(int mayorID) {
        String REQUEST_TAG = Constants.URL + Constants.FETCHDEPARTMENTS + "&MayorID=" + mayorID;

        if(Module.isNetworkConnected(this)) {
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
                            }
                        } catch (JSONException e) {
                            Log.e("Fail caught", e.toString());
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("onErrorResponse", error.toString());
                }
            });
            SingletonRequest.getInstance(this).addToRequestQueue(request);
        } else {
            Toast.makeText(this, "Please check internet connection.", Toast.LENGTH_LONG).show();
        }
    }
}
