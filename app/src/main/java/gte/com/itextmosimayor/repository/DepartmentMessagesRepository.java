package gte.com.itextmosimayor.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.database.DatabaseHandler;
import gte.com.itextmosimayor.database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.models.MessagesData;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.modules.SingletonRequest;

public class DepartmentMessagesRepository {
    private DatabaseHandler db;
    private static DepartmentMessagesRepository instance;
    private ArrayList<MessagesData> openDataSet = new ArrayList<>();
    private ArrayList<MessagesData> importantDataSet = new ArrayList<>();

    public static DepartmentMessagesRepository getInstance() {
        if (instance == null)
            instance = new DepartmentMessagesRepository();
        return instance;
    }

    public MutableLiveData<List<MessagesData>> getOpenMessages(Application application) {
        setOpenMessages(application);
        MutableLiveData<List<MessagesData>> data = new MutableLiveData<>();
        data.setValue(openDataSet);
        return data;
    }

    public MutableLiveData<List<MessagesData>> getImportantMessages(Application application) {
        setImportantMessages(application);
        MutableLiveData<List<MessagesData>> data = new MutableLiveData<>();
        data.setValue(importantDataSet);
        return data;
    }

    private void setOpenMessages(final Application application) {
        final String departmentID = Preference.getInstance(application).getPrefString(DBInfo.DEPARTMENTID);
        String REQUEST_TAG = Constants.URL + Constants.FETCHASSIGNEDMESSAGE + "&DepartmentID=" + departmentID;
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, REQUEST_TAG, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    db = new DatabaseHandler(application);
                    openDataSet.clear();
                    db.TRUNCATE_OPEN_MESSAGE();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        String messageID = obj.getString("MessageID");
                        String dateSent = obj.getString("DateSent");
                        String clientMobileNumber = obj.getString("ClientMobileNumber");
                        String content = obj.getString("Content");
                        String mayorID = obj.getString("MayorID");
                        String priorityLevel = obj.getString("PriorityLevel");
                        String isAssigned = obj.getString("isAssigned");
                        String status = obj.getString("Status");
                        String convoID = obj.getString("ConvoID");
                        openDataSet.add(new MessagesData(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status, departmentID, convoID));
                        db.INSERT_OPEN_MESSAGE(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status, departmentID, convoID);
                    }
                } catch (JSONException e) {
                    Log.e("Fail caught", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("openRepoError", error.getMessage());
            }
        });
        SingletonRequest.getInstance(application).addToRequestQueue(request);
    }

    private void setImportantMessages(final Application application) {
        final String departmentID = Preference.getInstance(application).getPrefString(DBInfo.DEPARTMENTID);
        String REQUEST_TAG = Constants.URL + Constants.FETCHIMPORTANTASSIGNEDMESSAGE + "&DepartmentID=" + departmentID;
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, REQUEST_TAG, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    db = new DatabaseHandler(application);
                    importantDataSet.clear();
                    db.TRUNCATE_IMPORTANT_MESSAGE();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        String messageID = obj.getString("MessageID");
                        String dateSent = obj.getString("DateSent");
                        String clientMobileNumber = obj.getString("ClientMobileNumber");
                        String content = obj.getString("Content");
                        String mayorID = obj.getString("MayorID");
                        String priorityLevel = obj.getString("PriorityLevel");
                        String isAssigned = obj.getInt("isAssigned") + "";
                        String status = obj.getString("Status");
                        String convoID = obj.getString("ConvoID");
                        db.INSERT_IMPORTANT_MESSAGE(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status, departmentID, convoID);
                        importantDataSet.add(new MessagesData(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status, departmentID, convoID));
                    }
                } catch (JSONException e) {
                    Log.e("Fail caught", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("importantRepoError", error.getMessage());
            }
        });
        SingletonRequest.getInstance(application).addToRequestQueue(request);
    }
}