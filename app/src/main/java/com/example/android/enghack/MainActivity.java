package com.example.android.enghack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        String myURL = getString(R.string.url);

        makeRequest(myURL);

    }


    String makeRequest(String myURL) {
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, myURL , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.e(LOG_TAG, response);
                Log.e(LOG_TAG, "Success!");
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                error.printStackTrace();
                Log.e(LOG_TAG, "HTTP Request Error");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("latitude", "43"); //Add the data you'd like to send to the server.
                MyData.put("longitude", "-80");
                return MyData;
            }
        };

        return "";
    }
}
