package gte.com.itextmosimayor.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import gte.com.itextmosimayor.MainActivity;
import gte.com.itextmosimayor.MainDepartment;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.requests.SingletonRequest;

public class SignUp extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtMobileNo = findViewById(R.id.txtMobileNo);
        txtMobileNo.setHintTextAppearance(R.style.CustomHintDisabled);
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
                if(validateFields()) {
                    firstName = editFirstName.getText().toString();
                    lastName = editLastName.getText().toString();
                    username = editUsername.getText().toString();
                    password = editPassword.getText().toString();

                    postRegister(firstName,lastName,number,username,password,code);
                }
            }
        });
    }

    public boolean validateFields(){
        if (editUsername.getText().toString().trim().length() == 0) {
            txtUsername.setError("Enter username.");
            return false;
        }
        else if (editUsername.getText().toString().length() < 3){
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
        }
        else if (editPassword.getText().toString().length() < 4) {
            txtPassword.setError("Enter four or more characters.");
            return false;
        }

        return true;
    }



    public void postRegister(String firstName, String lastName, String mobileNumber,
                             String username, String password, String code) {
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
                if (response.toString().length() > 3){
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
                            db.INSERT_USER_INFO(db, UserID, Username, Password, FirstName, LastName,
                                    MobileNumber, DepartmentID +"", MayorID);

                            Preference.getInstance(SignUp.this).savePrefBoolean(Constants.ISLOGIN, true);
                            Preference.getInstance(SignUp.this).savePrefInt(DBInfo.USERID, UserID);
                            Preference.getInstance(SignUp.this).savePrefString(DBInfo.USERNAME, Username);
                            Preference.getInstance(SignUp.this).savePrefString(DBInfo.PASSWORD, Password);
                            Preference.getInstance(SignUp.this).savePrefString(DBInfo.FIRSTNAME, FirstName);
                            Preference.getInstance(SignUp.this).savePrefString(DBInfo.LASTNAME, LastName);
                            Preference.getInstance(SignUp.this).savePrefString(DBInfo.MOBILENUMBER, MobileNumber);
                            Preference.getInstance(SignUp.this).savePrefString(DBInfo.DEPARTMENTID, DepartmentID +"");
                            if(DepartmentID == 0){
                                Preference.getInstance(SignUp.this).savePrefString("UserType", "Mayor");
                            } else {
                                Preference.getInstance(SignUp.this).savePrefString("UserType", "Department");
                            }
                            Preference.getInstance(SignUp.this).savePrefInt(DBInfo.MAYORID, MayorID);

                            proceedToActivity();
                        }
                    } catch (JSONException e) {
                        Log.e("catch", e.toString());
                        Toast.makeText(SignUp.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SignUp.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUp.this, "Unable to connect to server. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
        SingletonRequest.getInstance(this).addToRequestQueue(request);

    }

    public void proceedToActivity() {
        if (Preference.getInstance(this).getPrefString("UserType").equals("Mayor")){
            Intent intent = new Intent(SignUp.this, MainActivity.class);
            startActivity(intent);
        } else if(Preference.getInstance(this).getPrefString("UserType").equals("Department")) {
            Intent intent = new Intent(SignUp.this, MainDepartment.class);
            startActivity(intent);
        } else {
            return;
        }
        finish();
    }

}
