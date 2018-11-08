package gte.com.itextmosimayor.dialogs;

import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.security.SecureRandom;
import java.util.Objects;

import gte.com.itextmosimayor.Database.DatabaseHandler;
import gte.com.itextmosimayor.Database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.modules.Module;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.requests.SingletonRequest;

public class AddNewDepartment extends DialogFragment {

    SQLiteDatabase database;
    DatabaseHandler db;
    TextInputLayout txtDepartmentCode;
    TextInputLayout txtDepartmentName;
    TextInputLayout txtStreetName;
    TextInputLayout txtBarangay;
    TextInputLayout txtMunicipality;
    TextInputLayout txtProvince;
    TextInputLayout txtZipCode;
    TextInputEditText editDepartmentCode;
    TextInputEditText editDepartmentName;
    TextInputEditText editStreetName;
    TextInputEditText editBarangay;
    TextInputEditText editMunicipality;
    TextInputEditText editProvince;
    TextInputEditText editZipCode;
    MaterialButton btnSubmit;
    ImageButton btnClose;

    public static String TAG = "AddNewDepartment";
    String DepartmentCode = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_add_new_department, container, false);

        txtDepartmentCode = view.findViewById(R.id.txtDepartmentCode);
        txtDepartmentName = view.findViewById(R.id.txtDepartmentName);
        txtStreetName = view.findViewById(R.id.txtStreetName);
        txtBarangay = view.findViewById(R.id.txtBarangay);
        txtMunicipality = view.findViewById(R.id.txtMunicipality);
        txtProvince = view.findViewById(R.id.txtProvince);
        txtZipCode = view.findViewById(R.id.txtZipCode);

        editDepartmentCode = view.findViewById(R.id.editDepartmentCode);
        editDepartmentName = view.findViewById(R.id.editDepartmentName);
        editStreetName = view.findViewById(R.id.editStreetName);
        editBarangay = view.findViewById(R.id.editBarangay);
        editMunicipality = view.findViewById(R.id.editMunicipality);
        editProvince = view.findViewById(R.id.editProvince);
        editZipCode = view.findViewById(R.id.editZipCode);

        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnClose = view.findViewById(R.id.btnClose);
        btnClose.setImageResource(R.drawable.ic_close_white_24dp);
//        Toolbar toolbar = view.findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
//        toolbar.setTitleTextColor(Color.WHITE);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        toolbar.setTitle("Add Department");

        db = new DatabaseHandler(getContext());
        database = db.getWritableDatabase();

        DepartmentCode = generateCode();
        editDepartmentCode.setText(DepartmentCode);
        editDepartmentCode.setEnabled(false);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String StreetName = Objects.requireNonNull(editStreetName.getText()).toString();
                String Barangay = editBarangay.getText().toString();
                String Municipality = editMunicipality.getText().toString();
                String Province = editProvince.getText().toString();
                String ZipCode = editZipCode.getText().toString();
                String DepartmentName = editDepartmentName.getText().toString();

                addDepartment(DepartmentCode, StreetName, Barangay, Municipality, Province, ZipCode, DepartmentName);
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public String generateCode() {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for(int i = 0; i < 8; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        String code = sb.toString();
        if(!isExistingCode(code)) {
            return code;
        } else {
            return null;
        }
    }

    public void addDepartment(String DepartmentCode, String StreetName, String Barangay, String Municipality,
                              String Province, String ZipCode, String DepartmentName) {
        String REQUEST_TAG = Constants.URL + Constants.ADDDEPARTMENT +
                "&DepartmentCode=" + DepartmentCode +
                "&StreetName=" + StreetName +
                "&Barangay=" + Barangay +
                "&Municipality=" + Municipality +
                "&Province=" + Province +
                "&ZipCode=" + ZipCode +
                "&DepartmentName=" + DepartmentName +
                "&MayorID=" + Preference.getInstance(getContext()).getPrefInt(DBInfo.MAYORID);

        if(Module.isNetworkConnected(getContext())) {
            final StringRequest request = new StringRequest(Request.Method.GET, REQUEST_TAG, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.length() > 3) {
                        Toast.makeText(getContext(), "Department saved.", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Department not saved. Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Unable to connect to server. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e("onErrorResponse", error.toString());

                }
            });

            SingletonRequest.getInstance(getContext()).addToRequestQueue(request);
        } else {
            Toast.makeText(getContext(), "Please check internet connection.", Toast.LENGTH_LONG).show();
        }
    }



    public boolean isExistingCode(String DepartmentCode) {
        String REQUEST_TAG = Constants.URL + Constants.ISEXISTINGCODE + "&DepartmentCode=" + DepartmentCode;
        final boolean[] exists = { false };
        if(Module.isNetworkConnected(getContext())) {
            final StringRequest request = new StringRequest(Request.Method.GET, REQUEST_TAG, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.length() > 3) {
                        exists[0] = true;
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Unable to connect to server. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e("onErrorResponse", error.toString());
                }
            });
            SingletonRequest.getInstance(getContext()).addToRequestQueue(request);
        } else {
            Log.e("INTERNET CONNECTION", "NO INTERNET");
            Toast.makeText(getContext(), "Please check internet connection.", Toast.LENGTH_LONG).show();
        }
        return exists[0];
    }
}