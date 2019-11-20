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

public class DeletedRepository {
    private DatabaseHandler db;
    private static DeletedRepository instance;
    private ArrayList<MessagesData> trashDataSet = new ArrayList<>();

    public static DeletedRepository getInstance() {
        if (instance == null)
            instance = new DeletedRepository();
        return instance;
    }

    public MutableLiveData<List<MessagesData>> getDeletedMessages(Application application) {
        setDeletedMessages(application);
        MutableLiveData<List<MessagesData>> data = new MutableLiveData<>();
        data.setValue(trashDataSet);
        return data;
    }

    private void setDeletedMessages(final Application application) {
        String REQUEST_TAG = Constants.URL + Constants.FETCHDELETEDMESSAGE + "&MayorID=1";
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, REQUEST_TAG, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    db = new DatabaseHandler(application);
                    trashDataSet.clear();
                    db.TRUNCATE_DELETED_MESSAGE();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        String messageID = obj.getInt("MessageID") + "";
                        String dateSent = obj.getString("DateSent");
                        String clientMobileNumber = obj.getString("ClientMobileNumber");
                        String content = obj.getString("Content");
                        String mayorID = obj.getInt("MayorID") + "";
                        String priorityLevel = obj.getString("PriorityLevel");
                        String isAssigned = obj.getInt("isAssigned") + "";
                        String status = obj.getString("Status");
                        db.INSERT_DELETED_MESSAGE(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status);
                        trashDataSet.add(new MessagesData(messageID, dateSent, clientMobileNumber, content, mayorID, priorityLevel, isAssigned, status));
                    }
                } catch (JSONException e) {
                    Log.e("Fail caught", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TrashRepoError", error.getMessage());
            }
        });
        SingletonRequest.getInstance(application).addToRequestQueue(request);
    }
}