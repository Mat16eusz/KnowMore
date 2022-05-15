package com.mateusz.jasiak.knowmore;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMNotificationSend {

    String userFCMToken;
    String title;
    Context context;
    Activity activity;

    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    String FCMServerKey = BuildConfig.FCM_SERVER_KEY;

    public FCMNotificationSend(String userFCMToken, String title, Context context, Activity activity) {
        this.userFCMToken = userFCMToken;
        this.title = title;
        this.context = context;
        this.activity = activity;
    }

    public void SendNotifications() {
        requestQueue = Volley.newRequestQueue(activity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFCMToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("icon", R.drawable.ic_launcher_background);
            mainObj.put("notification", notiObject);

            Log.v("Send notification - title", title);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + FCMServerKey);

                    return header;
                }
            };

            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
