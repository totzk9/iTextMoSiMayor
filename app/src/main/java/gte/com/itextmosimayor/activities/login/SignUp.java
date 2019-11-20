package gte.com.itextmosimayor.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.activities.MainActivity;
import gte.com.itextmosimayor.activities.MainDepartment;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.database.DatabaseHandler;
import gte.com.itextmosimayor.database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.models.OtherUsers;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.modules.SingletonRequest;
import gte.com.itextmosimayor.modules.StringFormat;

import static gte.com.itextmosimayor.constant.Constants.DEFAULTIMGURL;

public class SignUp extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseUser currentUser;

    TextInputLayout txtMobileNo;
    TextInputEditText editMobileNo;

    TextInputLayout txtUsername;
    TextInputEditText editUsername;

    TextInputLayout txtPassword;
    TextInputEditText editPassword;

    TextInputLayout txtFirstName;
    TextInputEditText editFirstName;

    TextInputLayout txtLastName;
    TextInputEditText editLastName;

    MaterialButton btnSubmit;
    ImageButton btnBack;

    String code;
    String number;
    String firstName, lastName, username, password;

    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    StringFormat stringFormat = new StringFormat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        txtMobileNo = findViewById(R.id.txtMobileNo);
        txtMobileNo.setHintTextAppearance(R.style.CustomHintDisabled);
        progressBar = findViewById(R.id.progressBar);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            code = extras.getString("Code");
            number = extras.getString("MobileNumber");
        }

        editMobileNo = findViewById(R.id.editMobileNo);
        editMobileNo.setText(number);
        editMobileNo.setEnabled(false);

        txtUsername = findViewById(R.id.txtUsername);
        editUsername = findViewById(R.id.editUsername);

        txtPassword = findViewById(R.id.txtPassword);
        editPassword = findViewById(R.id.editPassword);

        txtFirstName = findViewById(R.id.txtFirstName);
        editFirstName = findViewById(R.id.editFirstName);

        txtLastName = findViewById(R.id.txtLastName);
        editLastName = findViewById(R.id.editLastName);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setImageResource(R.drawable.ic_arrow_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateFields()) {
                    firstName = editFirstName.getText().toString();
                    lastName = editLastName.getText().toString();
                    username = editUsername.getText().toString();
                    password = editPassword.getText().toString();

                    postRegister(firstName, lastName, number, username, password, code);
                }
            }
        });
    }

    public boolean validateFields() {
        if (editUsername.getText().toString().trim().length() == 0) {
            txtUsername.setError("Enter username.");
            return false;
        } else if (editUsername.getText().toString().length() < 3) {
            txtUsername.setError("Enter three or more characters.");
            return false;
        }

        if (editFirstName.getText().toString().trim().length() == 0) {
            txtFirstName.setError("Please enter field.");
            return false;
        }

        if (editLastName.getText().toString().trim().length() == 0) {
            editLastName.setError("Please enter field.");
            return false;
        }

        if (editPassword.getText().toString().trim().length() == 0) {
            editPassword.setError("Please enter field.");
            return false;
        } else if (editPassword.getText().toString().length() < 4) {
            txtPassword.setError("Enter four or more characters.");
            return false;
        }

        return true;
    }

    public void postRegister(String firstName, String lastName, String mobileNumber,
                             final String username, final String password, String code) {
        firstName = stringFormat.convertSpace(firstName);
        lastName = stringFormat.convertSpace(lastName);

        String url = Constants.URL + Constants.REGISTER +
                "&FirstName=" + firstName +
                "&LastName=" + lastName +
                "&MobileNumber=" + mobileNumber +
                "&Username=" + username +
                "&Password=" + password +
                "&Code=" + code;

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.toString().length() > 3) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            // Get current json object
                            JSONObject obj = response.getJSONObject(i);
                            // Get the current student (json object) data
                            int UserID = obj.getInt("UserID");
                            String FirstName = obj.getString("FirstName");
                            String LastName = obj.getString("LastName");
                            String MobileNumber = obj.getString("MobileNumber");
                            String Username = obj.getString("Username");
                            String Password = "";
                            int DepartmentID = obj.getInt("DepartmentID");
                            int MayorID = obj.getInt("MayorID");

                            DatabaseHandler db = new DatabaseHandler(SignUp.this);
                            db.INSERT_USER_INFO(UserID, Username, Password, FirstName, LastName,
                                    MobileNumber, DepartmentID + "", MayorID);

                            Preference.getInstance(SignUp.this).savePrefInt(DBInfo.USERID, UserID);
                            Preference.getInstance(SignUp.this).savePrefString(DBInfo.USERNAME, Username);
                            Preference.getInstance(SignUp.this).savePrefString(DBInfo.PASSWORD, Password);
                            Preference.getInstance(SignUp.this).savePrefString(DBInfo.FIRSTNAME, FirstName);
                            Preference.getInstance(SignUp.this).savePrefString(DBInfo.LASTNAME, LastName);
                            Preference.getInstance(SignUp.this).savePrefString(DBInfo.MOBILENUMBER, MobileNumber);
                            Preference.getInstance(SignUp.this).savePrefString(DBInfo.DEPARTMENTID, DepartmentID + "");
                            if (DepartmentID == 0) {
                                Preference.getInstance(SignUp.this).savePrefString("UserType", "Mayor");
                            } else {
                                Preference.getInstance(SignUp.this).savePrefString("UserType", "Department");
                            }
                            Preference.getInstance(SignUp.this).savePrefInt(DBInfo.MAYORID, MayorID);
                            progressBar.setVisibility(View.VISIBLE);
                            proceedToActivity(username, password, FirstName + " " + LastName, UserID + "", MobileNumber, DepartmentID + "");
                        }
                    } catch (JSONException e) {
                        Log.e("catch", e.toString());
                        Toast.makeText(SignUp.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(SignUp.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUp.this, "Unable to connect to server. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
        SingletonRequest.getInstance(this).addToRequestQueue(request);
    }

    public void proceedToActivity(String username, String password, final String fullname, final String userID, final String mobileNumber,
                                  final String departmentID) {

        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = currentUser.getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                            OtherUsers otherUsers = new OtherUsers(fullname, mobileNumber, departmentID, DEFAULTIMGURL, userID, uid);

                            databaseReference.setValue(otherUsers);

                            switch (Preference.getInstance(SignUp.this).getPrefString("UserType")) {
                                case "Mayor": {
                                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                                case "Department": {
                                    Intent intent = new Intent(SignUp.this, MainDepartment.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                            }
                        }
                    }
                });
    }
}