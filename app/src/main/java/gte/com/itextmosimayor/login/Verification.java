package gte.com.itextmosimayor.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.requests.SingletonRequest;

public class Verification extends AppCompatActivity {

    EditText editVerificationCode;
    MaterialButton btnSubmit;
    MaterialButton btnResendCode;
    SingletonRequest singletonRequest;
    ImageButton btnBack;
    String code;
    String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        singletonRequest = SingletonRequest.getInstance(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            code = extras.getString("Code");
            number = extras.getString("MobileNumber");
        }

        editVerificationCode = findViewById(R.id.editVerificationCode);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setImageResource(R.drawable.ic_arrow_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnResendCode = findViewById(R.id.btnResendCode);
        btnResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Verification.this, "Code resent!", Toast.LENGTH_SHORT).show();
            }
        });
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Verification.this, SignUp.class);
                Bundle bundle = new Bundle();
                bundle.putString("Code", code);
                bundle.putString("MobileNumber", number);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
