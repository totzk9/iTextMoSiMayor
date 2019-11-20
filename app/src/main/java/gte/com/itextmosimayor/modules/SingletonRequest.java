package gte.com.itextmosimayor.modules;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SingletonRequest {
    private static SingletonRequest mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    public static synchronized SingletonRequest getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingletonRequest(context.getApplicationContext());
        }
        return mInstance;
    }

    private SingletonRequest(Context context) {
        mCtx = context.getApplicationContext();
        mRequestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
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