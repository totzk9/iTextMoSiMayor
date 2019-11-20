package gte.com.itextmosimayor.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;

import androidx.appcompat.app.AppCompatActivity;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.modules.CheckConnection;
import gte.com.itextmosimayor.modules.SingletonRequest;

public class UserCodeVerification extends AppCompatActivity {
    String code;
    EditText editAssignedCode;
    MaterialButton btnSubmit;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_user_code);
        editAssignedCode = findViewById(R.id.editAssignedCode);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setImageResource(R.drawable.ic_arrow_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = editAssignedCode.getText().toString();
                isValidCode(code);
            }
        });
    }

    public void isValidCode(String code) {
        final String url = Constants.URL + Constants.ISVALIDCODE + "&Code=" + code;
        Log.e("ISVALIDCODE", url);
        if (CheckConnection.isNetworkConnected(this)) {
            final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.e("ISVALIDCODE", response.toString());
                    if (response.toString().length() > 3) {
                        proceedToActivity();
                    } else
                        Toast.makeText(UserCodeVerification.this, "Invalid code or non-existent", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("isValidCode", error.toString());

                }
            });
            SingletonRequest.getInstance(this).addToRequestQueue(request);
        } else {
            Toast.makeText(this, "Please check internet connection.", Toast.LENGTH_LONG).show();
        }
    }

    public void proceedToActivity() {
        Intent intent = new Intent(UserCodeVerification.this, MobileNumberVerification.class);
        intent.putExtra("Code", code);
        startActivity(intent);
        finish();
    }
}
