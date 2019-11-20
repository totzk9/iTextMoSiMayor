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
import gte.com.itextmosimayor.models.MessagesData;
import gte.com.itextmosimayor.modules.SingletonRequest;

public class MessagesRepository {
    private DatabaseHandler db;
    private static MessagesRepository instance;
    private ArrayList<MessagesData> unassignedDataSet = new ArrayList<>();
    private ArrayList<MessagesData> openDataSet = new ArrayList<>();
    private ArrayList<MessagesData> importantDataSet = new ArrayList<>();
    private ArrayList<MessagesData> dataSetContents = new ArrayList<>();

    public static MessagesRepository getInstance() {
        if (instance == null)
            instance = new MessagesRepository();
        return instance;
    }

    public MutableLiveData<List<MessagesData>> getUnassignedMessages(Application application) {
        setUnassignedMessages(application);
        MutableLiveData<List<MessagesData>> data = new MutableLiveData<>();
        data.setValue(unassignedDataSet);
        return data;
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

    public MutableLiveData<List<MessagesData>> getUnassignedMessagesData(Application application) {
        String mobNum;
        for (int i = 0; i < unassignedDataSet.size(); i++) {
            mobNum = unassignedDataSet.get(i).getClientMobileNumber();
            setUnassignedMessagesData(mobNum, application);
        }
        MutableLiveData<List<MessagesData>> data = new MutableLiveData<>();
        data.setValue(dataSetContents);
        return data;
    }

    private void setUnassignedMessages(final Application application) {
        String REQUEST_TAG = Constants.URL + Constants.FETCHUNASSIGNEDMESSAGE + "&MayorID=1";
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, REQUEST_TAG, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    db = new DatabaseHandler(application);
                    unassignedDataSet.clear();
                    db.TRUNCATE_UNASSIGNED_MESSAGE();
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
                        String isAssigned = obj.getString("isAssigned");
                        String status = obj.getString("Status");

                        unassignedDataSet.add(new MessagesData(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status));
                        db.INSERT_UNASSIGNED_MESSAGE(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status);
                    }
                } catch (JSONException e) {
                    Log.e("Fail caught", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("unassignedRepoError", error.getMessage());
            }
        });
        SingletonRequest.getInstance(application).addToRequestQueue(request);
    }

    private void setOpenMessages(final Application application) {
        String REQUEST_TAG = Constants.URL + Constants.FETCHOPENMESSAGE + "&MayorID=1";
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, REQUEST_TAG, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    db = new DatabaseHandler(application);
                    openDataSet.clear();
                    db.TRUNCATE_OPEN_MESSAGE();
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
                        String isAssigned = obj.getString("isAssigned");
                        String status = obj.getString("Status");
                        String departmentName = obj.getString("DepartmentName");
                        openDataSet.add(new MessagesData(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status, departmentName));
                        db.INSERT_OPEN_MESSAGE(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status, departmentName, "");
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
        String REQUEST_TAG = Constants.URL + Constants.FETCHIMPORTANTMESSAGE + "&MayorID=1";
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, REQUEST_TAG, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    db = new DatabaseHandler(application);
                    importantDataSet.clear();
                    db.TRUNCATE_IMPORTANT_MESSAGE();
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
                        String departmentName = obj.getString("DepartmentName");
                        importantDataSet.add(new MessagesData(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status, departmentName));
                        db.INSERT_IMPORTANT_MESSAGE(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status, departmentName, "");
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

    private void setUnassignedMessagesData(String mobileNumber, Application application) {
        String REQUEST_TAG = Constants.URL + Constants.FETCHUNASSIGNEDMESSAGETHREAD + "&MayorID=1" + "&ClientMobileNumber=" + mobileNumber;
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, REQUEST_TAG, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.toString().length() > 3) {
                    try {
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
                            boolean doesContain = false;
                            for (int j = 0; j < dataSetContents.size(); j++) {
                                if ((dataSetContents.get(j).getMessageID().equals(messageID)))
                                    doesContain = true;
                            }
                            if (!doesContain) {
                                dataSetContents.add(new MessagesData(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status));
                                db.INSERT_MESSAGE(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status);
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("Fail caught", e.toString());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error history", "" + error.getMessage());
            }
        });
        SingletonRequest.getInstance(application).addToRequestQueue(request);
    }
}