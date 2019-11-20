package gte.com.itextmosimayor.activities.login;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import gte.com.itextmosimayor.activities.MainActivity;
import gte.com.itextmosimayor.activities.MainDepartment;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.database.DatabaseHandler;
import gte.com.itextmosimayor.database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.modules.CheckConnection;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.modules.SingletonRequest;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    // UI references.
    private AutoCompleteTextView mUsername;
    private EditText mPassword;
    //    MaterialButton btnGoogleLogin;
    MaterialButton btnLogin;
    TextView txtSignUpNowLabel;
    TextView loginLBL;
    ProgressBar progressBar;
    VectorDrawableCompat drawableCompat;
    private FirebaseAuth mAuth;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DatabaseHandler(this);
        mAuth = FirebaseAuth.getInstance();
        long cnt = db.USER_ENTRIES_COUNT();
        if (cnt == 1L)
            proceedToActivity();

        // Set up the login form.
        progressBar = findViewById(R.id.progressBar);
        mUsername = findViewById(R.id.username);
        drawableCompat = VectorDrawableCompat.create(getResources(), R.drawable.ic_user, mUsername.getContext().getTheme());
        mUsername.setCompoundDrawablesRelativeWithIntrinsicBounds(drawableCompat, null, null, null);
        populateAutoComplete();
        txtSignUpNowLabel = findViewById(R.id.txtSignUpNowLabel);
        loginLBL = findViewById(R.id.loginLBL);
        loginLBL.setText("#iTextMoSiMayor\nText Issue Managing App");
        mPassword = findViewById(R.id.password);
        drawableCompat = VectorDrawableCompat.create(getResources(), R.drawable.ic_pass, mPassword.getContext().getTheme());
        mPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(drawableCompat, null, null, null);
        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        txtSignUpNowLabel.setText(Html.fromHtml(Constants.SIGNUPNOWLABEL));
        txtSignUpNowLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, UserCodeVerification.class);
                startActivity(intent);
            }
        });

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    private void populateAutoComplete() {
        if (!mayRequestContacts())
            return;
        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
            return true;

        if (!shouldShowRequestPermissionRationale(READ_CONTACTS))
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                populateAutoComplete();
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    private void attemptLogin() {
        progressBar.setVisibility(View.VISIBLE);
        mUsername.setError(null);
        mPassword.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            progressBar.setVisibility(View.GONE);
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            progressBar.setVisibility(View.GONE);
            mUsername.setError(getString(R.string.error_field_required));
            focusView = mUsername;
            cancel = true;
        }


        if (cancel){
            focusView.requestFocus();
            progressBar.setVisibility(View.GONE);
        } else
            userLogin(username, password);
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUsername.setAdapter(adapter);
    }

    //TWO WAY LOGIN -  Server Backend Login then Firebase Auth

    public void userLogin(final String username, final String password) {
        if (CheckConnection.isNetworkConnected(this)) {
            String urlRequest = Constants.URL + Constants.USERLOGIN + "&Username=" + username + "&Password=" + password;
            final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlRequest, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if (response.toString().length() > 5) {
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

                                DatabaseHandler db = new DatabaseHandler(LoginActivity.this);
                                db.INSERT_USER_INFO(UserID, Username, Password, FirstName, LastName,
                                        MobileNumber, DepartmentID + "", MayorID);

                                Preference.getInstance(LoginActivity.this).savePrefInt(DBInfo.MAYORID, UserID);
                                Preference.getInstance(LoginActivity.this).savePrefString(DBInfo.USERNAME, Username);
                                Preference.getInstance(LoginActivity.this).savePrefString(DBInfo.PASSWORD, Password);
                                Preference.getInstance(LoginActivity.this).savePrefString(DBInfo.FIRSTNAME, FirstName);
                                Preference.getInstance(LoginActivity.this).savePrefString(DBInfo.LASTNAME, LastName);
                                Preference.getInstance(LoginActivity.this).savePrefString(DBInfo.MOBILENUMBER, MobileNumber);
                                Preference.getInstance(LoginActivity.this).savePrefString(DBInfo.DEPARTMENTID, DepartmentID + "");
                                if (DepartmentID == 0)
                                    Preference.getInstance(LoginActivity.this).savePrefString("UserType", "Mayor");
                                else
                                    Preference.getInstance(LoginActivity.this).savePrefString("UserType", "Department");

                                Preference.getInstance(LoginActivity.this).savePrefInt(DBInfo.MAYORID, MayorID);

                                proceedToActivity(username, password);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOGINresponse", error.toString());
                }
            });
            SingletonRequest.getInstance(this).addToRequestQueue(request);
        } else {
            Toast.makeText(this, "Please check your internet connection.", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
        int ADDRESS = 0;
    }

    public void proceedToActivity(String username, String password) {
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FIREBASELOGIN", "signInWithEmail:success");
                            progressBar.setVisibility(View.GONE);
                            switch (Preference.getInstance(LoginActivity.this).getPrefString("UserType")) {
                                case "Mayor": {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                                case "Department": {
                                    Intent intent = new Intent(LoginActivity.this, MainDepartment.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FIREBASELOGIN", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public void proceedToActivity() {
        switch (Preference.getInstance(this).getPrefString("UserType")) {
            case "Mayor": {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case "Department": {
                Intent intent = new Intent(LoginActivity.this, MainDepartment.class);
                startActivity(intent);
                break;
            }
            default:
                return;
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        db.close();
    }
}