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

public class ConfidentialRepository {
    private DatabaseHandler db;
    private static ConfidentialRepository instance;
    private ArrayList<MessagesData> confidentialDataSet = new ArrayList<>();

    public static ConfidentialRepository getInstance() {
        if (instance == null)
            instance = new ConfidentialRepository();
        return instance;
    }

    public MutableLiveData<List<MessagesData>> getConfidentialMessages(Application application) {
        setConfidentialMessages(application);
        MutableLiveData<List<MessagesData>> data = new MutableLiveData<>();
        data.setValue(confidentialDataSet);
        return data;
    }

    private void setConfidentialMessages(final Application application) {
        String REQUEST_TAG = Constants.URL + Constants.FETCHCONFIDENTIALMESSAGE;
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, REQUEST_TAG, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    db = new DatabaseHandler(application);
                    confidentialDataSet.clear();
                    db.TRUNCATE_CONFIDENTIAL_MESSAGE();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        String messageID = obj.getInt("ConfidentialMessageID") + "";
                        String dateSent = obj.getString("DateSent");
                        String clientMobileNumber = obj.getString("ClientMobileNumber");
                        String content = obj.getString("Content");
                        db.INSERT_CONFIDENTIAL_MESSAGE(messageID, dateSent, clientMobileNumber, content);
                        confidentialDataSet.add(new MessagesData(messageID, dateSent, clientMobileNumber, content));
                    }
                } catch (JSONException e) {
                    Log.e("Fail caught", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ConfiRepoError", error.getMessage());
            }
        });
        SingletonRequest.getInstance(application).addToRequestQueue(request);
    }
}