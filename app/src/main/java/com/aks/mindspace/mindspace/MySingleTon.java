package com.aks.mindspace.mindspace;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by akash on 06-11-2017.
 */

public class MySingleTon {

    private static MySingleTon mInstance;
    private RequestQueue requestQueue;
    private Context mCtx;

    private MySingleTon(Context context)
    {
        mCtx=context;
        requestQueue=getRequestQueue();
    }

    private RequestQueue getRequestQueue() {

        if(requestQueue==null) {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleTon getInstance(Context context)
    {

        if(mInstance==null)
        {
            mInstance=new MySingleTon(context);
        }

        return mInstance;

    }

    public <T> void addToRequestQue(Request<T> request)
    {
        getRequestQueue().add(request);
    }
}
