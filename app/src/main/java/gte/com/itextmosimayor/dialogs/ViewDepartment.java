package gte.com.itextmosimayor.dialogs;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import gte.com.itextmosimayor.Database.DatabaseHandler;
import gte.com.itextmosimayor.Database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.R;

public class ViewDepartment extends Activity {

    SQLiteDatabase database;
    DatabaseHandler db;
    TextInputLayout txtDepartmentHead;
    TextInputLayout txtDepartmentCode;
    TextInputLayout txtDepartmentName;
    TextInputLayout txtStreetName;
    TextInputLayout txtBarangay;
    TextInputLayout txtMunicipality;
    TextInputLayout txtProvince;
    TextInputLayout txtZipCode;
    TextInputEditText editDepartmentHead;
    TextInputEditText editDepartmentCode;
    TextInputEditText editDepartmentName;
    TextInputEditText editStreetName;
    TextInputEditText editBarangay;
    TextInputEditText editMunicipality;
    TextInputEditText editProvince;
    TextInputEditText editZipCode;
    Toolbar toolbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            DepartmentID = extras.getString("DepartmentID");
        }

        db = new DatabaseHandler(this);
        database = db.getWritableDatabase();

        c = getAllItems(DepartmentID);
        c.moveToFirst();

        DepartmentName = c.getString(c.getColumnIndex(DBInfo.DEPARTMENTNAME));
        StreetName = c.getString(c.getColumnIndex(DBInfo.STREETNAME));
        Barangay = c.getString(c.getColumnIndex(DBInfo.BARANGAY));
        Municipality = c.getString(c.getColumnIndex(DBInfo.MUNICIPALITY));
        Province = c.getString(c.getColumnIndex(DBInfo.PROVINCE));
        ZipCode = c.getString(c.getColumnIndex(DBInfo.ZIPCODE));
        DepartmentHead = c.getString(c.getColumnIndex(DBInfo.DEPARTMENTHEAD));
        DepartmentCode = c.getString(c.getColumnIndex(DBInfo.DEPARTMENTCODE));

        setContentView(R.layout.dialog_view_department);

        txtDepartmentHead = findViewById(R.id.txtDepartmentHead);
        txtDepartmentCode = findViewById(R.id.txtDepartmentCode);
        txtDepartmentName = findViewById(R.id.txtDepartmentName);
        txtStreetName = findViewById(R.id.txtStreetName);
        txtBarangay = findViewById(R.id.txtBarangay);
        txtMunicipality = findViewById(R.id.txtMunicipality);
        txtProvince = findViewById(R.id.txtProvince);
        txtZipCode = findViewById(R.id.txtZipCode);

        editDepartmentHead = findViewById(R.id.editDepartmentHead);
        editDepartmentCode = findViewById(R.id.editDepartmentCode);
        editDepartmentName = findViewById(R.id.editDepartmentName);
        editStreetName = findViewById(R.id.editStreetName);
        editBarangay = findViewById(R.id.editBarangay);
        editMunicipality = findViewById(R.id.editMunicipality);
        editProvince = findViewById(R.id.editProvince);
        editZipCode = findViewById(R.id.editZipCode);

        editDepartmentHead.setText(DepartmentHead);
        editDepartmentHead.setEnabled(false);
        editDepartmentCode.setText(DepartmentCode);
        editDepartmentCode.setEnabled(false);
        editDepartmentName.setText(DepartmentName);
        editDepartmentName.setEnabled(false);
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
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle(DepartmentName);
    }

    private Cursor getAllItems(String DepartmentID) {
        return database.query(DBInfo.Department, null, "DepartmentID = ?", new String[] { DepartmentID }, null, null, null );
    }
}
