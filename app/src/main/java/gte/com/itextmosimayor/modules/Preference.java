package gte.com.itextmosimayor.modules;

import android.content.Context;
import android.content.SharedPreferences;

import gte.com.itextmosimayor.constant.Constants;

public class Preference {
    //private Context context;
    private SharedPreferences sharedPreferences;

    private static Preference preference;


    public static Preference getInstance(Context context) {
        if(preference == null) {
            preference = new Preference(context);
        }

        return preference;
    }

    public Preference(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.MYPREFERENCES, Context.MODE_PRIVATE);
    }
    public void savePrefString(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public void savePrefInt(String key, int value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
    }

    public void savePrefBoolean(String key, Boolean value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }
    public String getPrefString(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    public int getPrefInt(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, 0);
        }
        return 0;
    }

    public Boolean getPrefBoolean(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, false);
        }
        return false;
    }
}
