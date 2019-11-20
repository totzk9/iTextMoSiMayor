package gte.com.itextmosimayor.repository;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.database.DatabaseHandler;
import gte.com.itextmosimayor.database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.models.MessagesData;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.modules.SingletonRequest;

public class ResolvedRepository {
    private DatabaseHandler db;
    private static ResolvedRepository instance;
    private ArrayList<MessagesData> resolvedDataSet = new ArrayList<>();

    public static ResolvedRepository getInstance() {
        if (instance == null)
            instance = new ResolvedRepository();
        return instance;
    }

    public MutableLiveData<List<MessagesData>> getResolvedMessages(Application application) {
        setResolvedMessages(application);
        MutableLiveData<List<MessagesData>> data = new MutableLiveData<>();
        data.setValue(resolvedDataSet);
        return data;
    }

    public MutableLiveData<List<MessagesData>> getDepartmentResolvedMessages(Application application) {
        setDepartmentResolvedMessages(application);
        MutableLiveData<List<MessagesData>> data = new MutableLiveData<>();
        data.setValue(resolvedDataSet);
        return data;
    }

    private void setResolvedMessages(final Application application) {
        String REQUEST_TAG = Constants.URL + Constants.FETCHRESOLVEDMESSAGE + "&MayorID=1";
        Log.e("resolved", REQUEST_TAG);
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, REQUEST_TAG, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    db = new DatabaseHandler(application);
                    resolvedDataSet.clear();
                    db.TRUNCATE_RESOLVED_MESSAGE();
                    // Loop through the array elements
                    for (int i = 0; i < response.length(); i++) {
                        // Get current json object
                        JSONObject obj = response.getJSONObject(i);
                        String messageID = obj.getInt("MessageID") + "";
                        String dateSent = obj.getString("DateSent");
                        String clientMobileNumber = obj.getString("ClientMobileNumber");
                        String content = obj.getString("Content");
                        String mayorID = obj.getInt("MayorID") + "";
                        String priorityLevel = obj.getString("PriorityLevel");
                        String isAssigned = obj.getInt("isAssigned") + "";
                        String status = obj.getString("Status");
                        db.INSERT_RESOLVED_MESSAGE(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status);
                        resolvedDataSet.add(new MessagesData(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status));
                    }
                } catch (JSONException e) {
                    Log.e("Fail caught", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ResolvedRepoError", error.getMessage());
            }
        });
        SingletonRequest.getInstance(application).addToRequestQueue(request);
    }


    private void setDepartmentResolvedMessages(final Application application) {
        final String departmentID = Preference.getInstance(application).getPrefString(DBInfo.DEPARTMENTID);
        String REQUEST_TAG = Constants.URL + Constants.FETCHRESOLVEDASSIGNEDMESSAGE + "&DepartmentID=" + departmentID;
        Log.e("resolved", REQUEST_TAG);
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, REQUEST_TAG, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    db = new DatabaseHandler(application);
                    resolvedDataSet.clear();
                    db.TRUNCATE_RESOLVED_MESSAGE();
                    // Loop through the array elements
                    for (int i = 0; i < response.length(); i++) {
                        // Get current json object
                        JSONObject obj = response.getJSONObject(i);
                        String messageID = obj.getInt("MessageID") + "";
                        String dateSent = obj.getString("DateSent");
                        String clientMobileNumber = obj.getString("ClientMobileNumber");
                        String content = obj.getString("Content");
                        String mayorID = obj.getInt("MayorID") + "";
                        String priorityLevel = obj.getString("PriorityLevel");
                        String isAssigned = obj.getInt("isAssigned") + "";
                        String status = obj.getString("Status");
                        db.INSERT_RESOLVED_MESSAGE(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status);
                        resolvedDataSet.add(new MessagesData(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status));
                    }
                } catch (JSONException e) {
                    Log.e("Fail caught", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ResolvedRepoError", error.getMessage());
            }
        });
        SingletonRequest.getInstance(application).addToRequestQueue(request);
    }
}