package gte.com.itextmosimayor.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.database.DatabaseHandler;
import gte.com.itextmosimayor.database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.fragments.AssigneesFragment;
import gte.com.itextmosimayor.fragments.FragmentConfidential;
import gte.com.itextmosimayor.fragments.FragmentDeleted;
import gte.com.itextmosimayor.fragments.FragmentResolved;
import gte.com.itextmosimayor.fragments.FragmentSettings;
import gte.com.itextmosimayor.fragments.MessagesFragment;
import gte.com.itextmosimayor.modules.CheckConnection;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.modules.SingletonRequest;
import gte.com.itextmosimayor.viewmodels.ConfidentialMessagesViewModel;
import gte.com.itextmosimayor.viewmodels.DeletedMessagesViewModel;
import gte.com.itextmosimayor.viewmodels.MessagesViewModel;
import gte.com.itextmosimayor.viewmodels.ResolvedMessagesViewModel;

/*
 *  Created by Tyrone Chris Abad | tyrone.abad@got-the-experts.com
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FragmentDeleted fragmentDeleted;
    FragmentConfidential fragmentConfidential;
    FragmentResolved fragmentResolved;
    FragmentSettings fragmentSettings;

    AssigneesFragment assigneesFragment;
    MessagesFragment messagesFragment;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;

    SQLiteDatabase database;
    DatabaseHandler db;
    int count = 0;
    String currentUID;
    String imgURL;

    TextView txtName;
    View headerView;
    CircleImageView imgProfilePicture;

    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        db = new DatabaseHandler(this);
        database = db.getWritableDatabase();
        initialize();
        initViewModels();
        try {
            initializeFirebase();
        } catch (Exception x) {
            Log.e("FirebaseError", x.getMessage());
        }
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else {
            toolbar.setTitle("#iTextMoSiMayor");
            super.onBackPressed();
            if (count == 0)
                finish();
            else
                count--;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_primary:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, messagesFragment)
                        .addToBackStack(messagesFragment.toString())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                toolbar.setTitle("Messages");
//                invalidateOptionsMenu();
                count++;
//                item.setChecked(true);
                break;

            case R.id.nav_resolved:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragmentResolved)
                        .addToBackStack(fragmentResolved.toString())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                toolbar.setTitle("Resolved");
//                invalidateOptionsMenu();
                count++;
//                item.setChecked(true);
                break;

            case R.id.nav_confidential:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragmentConfidential)
                        .addToBackStack(fragmentConfidential.toString())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                toolbar.setTitle("Confidential");
//                invalidateOptionsMenu();
                count++;
//                item.setChecked(true);
                break;

            case R.id.nav_deleted:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragmentDeleted)
                        .addToBackStack(fragmentDeleted.toString())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                toolbar.setTitle("Trash");
//                invalidateOptionsMenu();
                count++;
//                item.setChecked(true);
                break;

            case R.id.nav_assignees:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, assigneesFragment)
                        .addToBackStack(assigneesFragment.toString())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                toolbar.setTitle("Communicate");
//                invalidateOptionsMenu();
                count++;
//                item.setChecked(true);
                break;

            case R.id.nav_settings:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragmentSettings)
                        .addToBackStack(fragmentSettings.toString())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                toolbar.setTitle("Settings");
                count++;
                break;

        }
//        item.setChecked(true);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initialize() {
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);

        navigationView = findViewById(R.id.nav_view);
        assigneesFragment = AssigneesFragment.newInstance();
        fragmentDeleted = FragmentDeleted.newInstance();
        fragmentSettings = FragmentSettings.newInstance();
        fragmentResolved = FragmentResolved.newInstance();
        messagesFragment = MessagesFragment.newInstance();
        fragmentConfidential = FragmentConfidential.newInstance();

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();

        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeButtonEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        toggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(toggle);

        String fullName = Preference.getInstance(this).getPrefString(DBInfo.FIRSTNAME) + " " + Preference.getInstance(this).getPrefString(DBInfo.LASTNAME);
        headerView = navigationView.getHeaderView(0);
        imgProfilePicture = headerView.findViewById(R.id.imgProfilePicture);
        txtName = headerView.findViewById(R.id.txtName);
        txtName.setText(fullName);

        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, messagesFragment)
                .addToBackStack(messagesFragment.toString())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        toolbar.setTitle("Primary");
        toggle.syncState();

        fetchDepartments();
    }

    public void fetchDepartments() {
        if (CheckConnection.isNetworkConnected(this)) {
            String REQUEST_TAG = Constants.URL + Constants.FETCHDEPARTMENTS + "&MayorID=1";
            final DatabaseHandler db = new DatabaseHandler(this);
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
        } else
            Toast.makeText(this, "Please check internet connection.", Toast.LENGTH_LONG).show();
    }

    private void initializeFirebase() {
        //Initialize Firebase
        currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUID).child("img");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imgURL = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                Picasso.get().load(imgURL).fit().centerInside().into(imgProfilePicture);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("MAYOR" + Preference.getInstance(this).getPrefInt(DBInfo.MAYORID)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful())
                    Log.e("subscribe fcm", "failed to subscribe topic");
                else{
                    Log.e("subscribe fcm", "subscribe topic successful");
                    Log.e("SubscribedTopic", "MAYOR" + Preference.getInstance(MainActivity.this).getPrefInt(DBInfo.MAYORID));
                }
            }
        });
    }

    private void initViewModels() {
        MessagesViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(this)).get(MessagesViewModel.class);
        viewModel.initOpen();
        viewModel.initImportant();
        viewModel.initUnassigned();

        ResolvedMessagesViewModel resolvedMessagesViewModel = ViewModelProviders.of(Objects.requireNonNull(this)).get(ResolvedMessagesViewModel.class);
        resolvedMessagesViewModel.init();

        ConfidentialMessagesViewModel confidentialMessagesViewModel = ViewModelProviders.of(Objects.requireNonNull(this)).get(ConfidentialMessagesViewModel.class);
        confidentialMessagesViewModel.init();

        DeletedMessagesViewModel deletedMessagesViewModel = ViewModelProviders.of(Objects.requireNonNull(this)).get(DeletedMessagesViewModel.class);
        deletedMessagesViewModel.init();
    }
}
