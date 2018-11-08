package gte.com.itextmosimayor.modules;

import org.json.JSONArray;

/**
 * Created by bernie on 19/07/2017.
 */

public interface OnTaskCompleted {
    void onTaskCompleted(String status, JSONArray response, String username, String pw);
}


