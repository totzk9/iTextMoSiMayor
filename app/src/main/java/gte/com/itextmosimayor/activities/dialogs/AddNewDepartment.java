package gte.com.itextmosimayor.activities.dialogs;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.SecureRandom;

import androidx.annotation.Nullable;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.modules.CheckConnection;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.modules.SingletonRequest;
import gte.com.itextmosimayor.modules.StringFormat;

public class AddNewDepartment extends Activity {

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";

    View rootLayout;

    ProgressBar progressBar;
    TextInputLayout txtDepartmentName;
    TextInputEditText editDepartmentCode;
    TextInputEditText editDepartmentName;
    TextInputEditText editStreetName;
    TextInputEditText editBarangay;
    TextInputEditText editMunicipality;
    TextInputEditText editProvince;
    TextInputEditText editZipCode;
    MaterialButton btnSubmit;
    ImageButton btnClose;

    StringFormat stringFormat = new StringFormat();
    String DepartmentCode = null;
    private int revealX;
    private int revealY;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_department);
        rootLayout = findViewById(R.id.root_layout);
        activateTransition(savedInstanceState);

        progressBar = findViewById(R.id.progressBar);

        txtDepartmentName = findViewById(R.id.txtDepartmentName);

        editDepartmentCode = findViewById(R.id.editDepartmentCode);
        editDepartmentName = findViewById(R.id.editDepartmentName);
        editStreetName = findViewById(R.id.editStreetName);
        editBarangay = findViewById(R.id.editBarangay);
        editMunicipality = findViewById(R.id.editMunicipality);
        editProvince = findViewById(R.id.editProvince);
        editZipCode = findViewById(R.id.editZipCode);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setImageResource(R.drawable.ic_close);

        DepartmentCode = generateCode();
        editDepartmentCode.setText(DepartmentCode);
        editDepartmentCode.setEnabled(false);
        txtDepartmentName.requestFocus();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(editStreetName.getText() != null || editBarangay.getText() != null || editMunicipality.getText() != null
                        || editProvince.getText() != null || editZipCode.getText() != null || editDepartmentName.getText() != null) {
                    String StreetName = editStreetName.getText().toString();
                    String Barangay = editBarangay.getText().toString();
                    String Municipality = editMunicipality.getText().toString();
                    String Province = editProvince.getText().toString();
                    String ZipCode = editZipCode.getText().toString();
                    String DepartmentName = editDepartmentName.getText().toString();

                    addDepartment(DepartmentCode, StreetName, Barangay, Municipality, Province, ZipCode, DepartmentName);
                    finish();
                }

            }
        });
    }

    public String generateCode() {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        String code = sb.toString();
        if (!isExistingCode(code))
            return code;
        else
            return null;
    }

    public void addDepartment(String DepartmentCode, String StreetName, String Barangay, String Municipality,
                              String Province, String ZipCode, String DepartmentName) {
        StreetName = stringFormat.convertSpace(StreetName);
        Barangay = stringFormat.convertSpace(Barangay);
        Municipality = stringFormat.convertSpace(Municipality);
        Province = stringFormat.convertSpace(Province);
        DepartmentName = stringFormat.convertSpace(DepartmentName);

        String REQUEST_TAG = Constants.URL + Constants.ADDDEPARTMENT +
                "&DepartmentCode=" + DepartmentCode +
                "&StreetName=" + StreetName +
                "&Barangay=" + Barangay +
                "&Municipality=" + Municipality +
                "&Province=" + Province +
                "&ZipCode=" + ZipCode +
                "&DepartmentName=" + DepartmentName +
                "&MayorID=" + Preference.getInstance(this).getPrefInt(DBInfo.MAYORID);

        if (CheckConnection.isNetworkConnected(this)) {
            final StringRequest request = new StringRequest(Request.Method.GET, REQUEST_TAG, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.length() > 3) {
                        Toast.makeText(AddNewDepartment.this, "Department saved.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(AddNewDepartment.this, "Department not saved. Please try again", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AddNewDepartment.this, "Unable to connect to server. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e("onErrorResponse", error.toString());

                }
            });
            SingletonRequest.getInstance(this).addToRequestQueue(request);
        } else
            Toast.makeText(this, "Please check internet connection.", Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);
    }


    protected void activateTransition(@Nullable Bundle savedInstanceState) {
        final Intent intent = getIntent();
        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            rootLayout.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);

            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else
            rootLayout.setVisibility(View.VISIBLE);
    }

    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(620);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else
            finish();
    }

    public boolean isExistingCode(String DepartmentCode) {
        String REQUEST_TAG = Constants.URL + Constants.ISEXISTINGCODE + "&DepartmentCode=" + DepartmentCode;
        final boolean[] exists = {false};
        if (CheckConnection.isNetworkConnected(this)) {
            final StringRequest request = new StringRequest(Request.Method.GET, REQUEST_TAG, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.length() > 3)
                        exists[0] = true;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AddNewDepartment.this, "Unable to connect to server. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e("onErrorResponse", error.toString());
                }
            });
            SingletonRequest.getInstance(AddNewDepartment.this).addToRequestQueue(request);
        } else {
            Log.e("INTERNET CONNECTION", "NO INTERNET");
            Toast.makeText(AddNewDepartment.this, "Please check internet connection.", Toast.LENGTH_LONG).show();
        }
        return exists[0];
    }
}