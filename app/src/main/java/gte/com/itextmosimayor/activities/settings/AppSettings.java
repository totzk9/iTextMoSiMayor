package gte.com.itextmosimayor.activities.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.modules.Preference;

public class AppSettings extends AppCompatActivity {

    Switch switchTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_app_settings);

        switchTabs = findViewById(R.id.switchTabs);

        initPreferences();

        switchTabs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Preference.getInstance(AppSettings.this).savePrefBoolean("tabs", true);
                else
                    Preference.getInstance(AppSettings.this).savePrefBoolean("tabs", false);
            }
        });
    }


    public void quit(View view) {
        finish();
    }

    private void initPreferences() {
        if (Preference.getInstance(this).getPrefBoolean("tabs"))
            switchTabs.setChecked(true);
    }
}