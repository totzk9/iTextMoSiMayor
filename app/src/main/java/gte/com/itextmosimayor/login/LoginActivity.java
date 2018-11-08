package gte.com.itextmosimayor.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gte.com.itextmosimayor.Database.DatabaseHandler;
import gte.com.itextmosimayor.Database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.MainActivity;
import gte.com.itextmosimayor.MainDepartment;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.modules.Module;
import gte.com.itextmosimayor.modules.OnTaskCompleted;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.requests.SingletonRequest;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor>,OnTaskCompleted {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    // UI references.
    private AutoCompleteTextView mUsername;
    private EditText mPassword;
    private View mProgressView;
    MaterialButton btnGoogleLogin;
    MaterialButton btnLogin;
    TextView txtSignUpNowLabel;

    private View mLoginFormView;

    SingletonRequest singletonRequest;
    SQLiteDatabase database;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DatabaseHandler(this);

        long cnt = db.USER_ENTRIES_COUNT();
        if (cnt == 1L){
            proceedToActivity();
        }

        // Set up the login form.
        mUsername =  findViewById(R.id.username);
        populateAutoComplete();
        txtSignUpNowLabel = findViewById(R.id.txtSignUpNowLabel);

        mPassword = findViewById(R.id.password);
        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
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

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        mUsername.setError(null);
        mPassword.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            mUsername.setError(getString(R.string.error_field_required));
            focusView = mUsername;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true);
            userLogin(username, password);

        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
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

    @Override
    public void onTaskCompleted(String status, JSONArray response, String username, String pw) {

        switch (status) {
            case "200" :
                if(response.length() > 0 ) {
//                    MainActivity.MainActivity.getMainActivity().saveUserInfo(response);
                    Preference.getInstance(this).savePrefBoolean(Constants.ISLOGIN, true);
                    Preference.getInstance(this).savePrefString(Constants.USERNAME, username);
                    Preference.getInstance(this).savePrefString(Constants.USERPASS, pw);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    this.finish();

                } else {
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
                break;
            case "400" :
                break;
            case "500" :
                break;
            default:
        }

    }
    public void userLogin(final String username, final String password) {
        if(Module.isNetworkConnected(this)) {
            showProgress(true);
            String urlRequest = Constants.URL + Constants.USERLOGIN + "&Username=" + username + "&Password=" + password;

            Log.e("url", urlRequest);
            final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlRequest, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.e("LOGINresponse",response.toString());
                    if (response.toString().length() > 5){
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
                                db.INSERT_USER_INFO(db, UserID, Username, Password, FirstName, LastName,
                                        MobileNumber, DepartmentID +"", MayorID);

                                Preference.getInstance(LoginActivity.this).savePrefBoolean(Constants.ISLOGIN, true);
                                Preference.getInstance(LoginActivity.this).savePrefInt(DBInfo.MAYORID, UserID);
                                Preference.getInstance(LoginActivity.this).savePrefString(DBInfo.USERNAME, Username);
                                Preference.getInstance(LoginActivity.this).savePrefString(DBInfo.PASSWORD, Password);
                                Preference.getInstance(LoginActivity.this).savePrefString(DBInfo.FIRSTNAME, FirstName);
                                Preference.getInstance(LoginActivity.this).savePrefString(DBInfo.LASTNAME, LastName);
                                Preference.getInstance(LoginActivity.this).savePrefString(DBInfo.MOBILENUMBER, MobileNumber);
                                Preference.getInstance(LoginActivity.this).savePrefString(DBInfo.DEPARTMENTID, DepartmentID +"");
                                if(DepartmentID == 0){
                                    Preference.getInstance(LoginActivity.this).savePrefString("UserType", "Mayor");
                                } else {
                                    Preference.getInstance(LoginActivity.this).savePrefString("UserType", "Department");
                                }
                                Preference.getInstance(LoginActivity.this).savePrefInt(DBInfo.MAYORID, MayorID);

                                proceedToActivity();
                            }
                        } catch (JSONException e) {
                            Log.e("catch", e.toString());
                            Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOGINresponse",error.toString());
                }
            });
            SingletonRequest.getInstance(this).addToRequestQueue(request);
            showProgress(false);
        }else {
            Toast.makeText(this ,"Please check your internet connection.",Toast.LENGTH_LONG).show();
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
    }

    public void proceedToActivity(){
        if (Preference.getInstance(this).getPrefString("UserType").equals("Mayor")){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else if(Preference.getInstance(this).getPrefString("UserType").equals("Department")) {
            Intent intent = new Intent(LoginActivity.this, MainDepartment.class);
            startActivity(intent);
        } else {
            return;
        }
        finish();
    }

}

