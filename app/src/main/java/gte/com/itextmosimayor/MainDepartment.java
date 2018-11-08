package gte.com.itextmosimayor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Stack;

import gte.com.itextmosimayor.fragments.FragmentDepartmentResolved;
import gte.com.itextmosimayor.fragments.MessagesFragment;

public class MainDepartment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FragmentDepartmentResolved fragmentResolved;
    MessagesFragment messagesFragment;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
//    TextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_department);

        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        fragmentResolved = FragmentDepartmentResolved.newInstance();
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
    }

    // Pushing element on the top of the stack
    static void pushTitleStack(Stack<String> stack, String title) { stack.push(title); }

    // Popping element from the top of the stack
    static void popTitleStack(Stack<String> stack) { stack.pop(); }

    // Displaying element on the top of the stack
    static String peekTitleStack(Stack<String> stack) { return stack.peek(); }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        switch (item.getItemId()) {
//            case R.id.action_settings:
//                drawer.openDrawer(GravityCompat.START);
////                drawer_layout.closeDrawer(GravityCompat.START)
////                drawer_layout.closeDrawer(GravityCompat.END)
//                return true;
//
//            case R.id.home:
//                drawer.openDrawer(GravityCompat.START); // OPEN DRAWER
//                return true;
//
////            R.id.add_newdepartment -> {
////                val dialog: AddNewDepartment = AddNewDepartment()
//////                val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
////                dialog.show(ft, AddNewDepartment.TAG)
////                supportFragmentManager.inTransaction {
////                    add(R.id.frameLayoutContent, fragment)
////                }
////            }
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_primary:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, messagesFragment)
                        .addToBackStack(messagesFragment.toString())
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .commit();
                toolbar.setTitle("Primary");
                invalidateOptionsMenu();
                break;

            case R.id.nav_resolved :
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragmentResolved)
                        .addToBackStack(fragmentResolved.toString())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                toolbar.setTitle("Resolved");
                invalidateOptionsMenu();
                break;

            case R.id.nav_settings:
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
