package gte.com.itextmosimayor.activities.dialogs;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.adapters.UsersAdapter;
import gte.com.itextmosimayor.database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.database.Queries;
import gte.com.itextmosimayor.models.OtherUsers;

public class ViewDepartment extends AppCompatActivity {

    TextInputEditText editDepartmentHead;
    TextInputEditText editDepartmentCode;
    TextInputEditText editStreetName;
    TextInputEditText editBarangay;
    TextInputEditText editMunicipality;
    TextInputEditText editProvince;
    TextInputEditText editZipCode;
    Toolbar toolbar;
    RecyclerView view_users;
    String DepartmentID;
    String DepartmentName;
    String StreetName;
    String Barangay;
    String Municipality;
    String Province;
    String ZipCode;
    String DepartmentHead;
    String DepartmentCode;
    Cursor c;
    UsersAdapter usersAdapter;
    private ArrayList<OtherUsers> otherUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_department);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            DepartmentID = extras.getString("DepartmentID");

        getData();

        editDepartmentHead = findViewById(R.id.editDepartmentHead);
        editDepartmentCode = findViewById(R.id.editDepartmentCode);
        editStreetName = findViewById(R.id.editStreetName);
        editBarangay = findViewById(R.id.editBarangay);
        editMunicipality = findViewById(R.id.editMunicipality);
        editProvince = findViewById(R.id.editProvince);
        editZipCode = findViewById(R.id.editZipCode);

        editDepartmentHead.setText(DepartmentHead);
        editDepartmentHead.setEnabled(false);
        editDepartmentCode.setText(DepartmentCode);
        editDepartmentCode.setEnabled(false);
        editStreetName.setText(StreetName);
        editStreetName.setEnabled(false);
        editBarangay.setText(Barangay);
        editBarangay.setEnabled(false);
        editMunicipality.setText(Municipality);
        editMunicipality.setEnabled(false);
        editProvince.setText(Province);
        editProvince.setEnabled(false);
        editZipCode.setText(ZipCode);
        editZipCode.setEnabled(false);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_purple);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle(DepartmentName);

        view_users = findViewById(R.id.view_users);

        initializeData();
        initializeRecyclerView();
    }

    private void initializeData() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                otherUsers.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    OtherUsers users = data.getValue(OtherUsers.class);
                    if (DepartmentID.equals(users.getDepartmentID()))
                        otherUsers.add(users);
                }
                usersAdapter.setList(otherUsers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("OtherUsersError", "The read failed: " + databaseError.getCode());
            }
        });
    }

    private void initializeRecyclerView() {
        usersAdapter = new UsersAdapter(otherUsers);
        usersAdapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String fid, String name, String img) {
                Intent intent = new Intent(ViewDepartment.this, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("fid", fid);
                bundle.putString("name", name);
                bundle.putString("img", img);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        view_users.setAdapter(usersAdapter);
        view_users.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        view_users.setHasFixedSize(true);
        view_users.setItemAnimator(new DefaultItemAnimator());
        usersAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getData() {
        c = Queries.getInstance(this).getDepartments(DepartmentID);
        c.moveToFirst();

        DepartmentName = c.getString(c.getColumnIndex(DBInfo.DEPARTMENTNAME));
        StreetName = c.getString(c.getColumnIndex(DBInfo.STREETNAME));
        Barangay = c.getString(c.getColumnIndex(DBInfo.BARANGAY));
        Municipality = c.getString(c.getColumnIndex(DBInfo.MUNICIPALITY));
        Province = c.getString(c.getColumnIndex(DBInfo.PROVINCE));
        ZipCode = c.getString(c.getColumnIndex(DBInfo.ZIPCODE));
        DepartmentHead = c.getString(c.getColumnIndex(DBInfo.DEPARTMENTHEAD));
        DepartmentCode = c.getString(c.getColumnIndex(DBInfo.DEPARTMENTCODE));
    }
}
