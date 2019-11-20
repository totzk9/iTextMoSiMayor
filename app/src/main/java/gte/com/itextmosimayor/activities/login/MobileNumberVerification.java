package gte.com.itextmosimayor.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;

import androidx.appcompat.app.AppCompatActivity;
import gte.com.itextmosimayor.R;

public class MobileNumberVerification extends AppCompatActivity {
    String number;
    EditText editNumber;
    MaterialButton btnSubmit;
    String code;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_mobile_number_verification);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            code = extras.getString("Code");
        }
        btnBack = findViewById(R.id.btnBack);
        btnBack.setImageResource(R.drawable.ic_arrow_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editNumber = findViewById(R.id.editNumber);
        editNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = editNumber.getText().toString();

                if (!number.isEmpty() || number.trim().length() != 0) {
                    Intent intent = new Intent(MobileNumberVerification.this, Verification.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Code", code);
                    bundle.putString("MobileNumber", number);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

    }
}
