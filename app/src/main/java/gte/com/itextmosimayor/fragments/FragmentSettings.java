package gte.com.itextmosimayor.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.activities.login.LoginActivity;
import gte.com.itextmosimayor.activities.settings.AccountSettings;
import gte.com.itextmosimayor.activities.settings.AppSettings;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.modules.Preference;

public class FragmentSettings extends Fragment {

    public static FragmentSettings newInstance() {
        return new FragmentSettings();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        MaterialButton btnAccountSettings = view.findViewById(R.id.btnAccountSettings);
        MaterialButton btnAppSettings = view.findViewById(R.id.btnAppSettings);
        MaterialButton btnLogout = view.findViewById(R.id.btnLogout);

        btnAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountSettings.class);
                startActivity(intent);
            }
        });

        btnAppSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppSettings.class);
                startActivity(intent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        return view;
    }

    private void showDialog() {
        new AlertDialog.Builder(getContext(), R.style.CustomDialog)
                .setTitle("Log out?")
                .setMessage("You will need to log in your credentials to use the app again.")
                .setCancelable(true)
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Preference.getInstance(getContext()).clear();
                        getContext().deleteDatabase(Constants.DATABASE_NAME);
                        getActivity().finish();
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }
}