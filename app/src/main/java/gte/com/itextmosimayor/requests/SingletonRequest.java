package gte.com.itextmosimayor.requests;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Map;

import gte.com.itextmosimayor.Database.DatabaseHandler;
import gte.com.itextmosimayor.modules.OnTaskCompleted;

public class SingletonRequest {
    private Map<String, String> params;

    private static SingletonRequest mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private OnTaskCompleted taskListener;
    DatabaseHandler db = new DatabaseHandler(mCtx);

    public static synchronized SingletonRequest getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingletonRequest(context);
        }
        return mInstance;
    }

    private SingletonRequest(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

//    public SingletonRequest(OnTaskCompleted taskListener) {
//        this.taskListener = taskListener;
//    }



    public static boolean isNetworkConnected(Context context) {
        boolean status = false;
        try{
            ConnectivityManager cm =
                    (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            status = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;

    }



    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }



}
