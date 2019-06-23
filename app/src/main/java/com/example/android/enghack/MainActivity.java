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

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = MainActivity.class.getSimpleName();
    RequestQueue MyRequestQueue = null;

    JSONObject masterJSON = null;
    ArrayList<Event> events = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    void init(){
        String myURL = getString(R.string.url);
        MyRequestQueue = Volley.newRequestQueue(this);

        Log.d(LOG_TAG, "making my request");
        makeRequest(myURL);
    }

    void createEvents() {
        try {
            JSONObject top = masterJSON.getJSONObject("distinctQueryResult");
            JSONArray rows = top.getJSONArray("rows");
            int length = 0;
            if(rows.length() < 10){
                length = rows.length();
            } else {
                length = 10;
            }

            for(int i = 0; i < length; i++){
                JSONObject myEvent = (JSONObject) rows.getJSONObject(0);
                JSONObject fields = myEvent.getJSONObject("fields");
                events.add(new Event(myEvent.getString("id"), fields.getString("Event"), fields.getString("Description"),
                        fields.getDouble("Latitude"), fields.getDouble("Longitude"), fields.getString("Type")));
            }

            Log.d(LOG_TAG, events.toString());
        } catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Error creating events");
        }
    }

    void makeRequest(String myURL) {
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, myURL , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d(LOG_TAG, "Success!");

                try{
                    masterJSON = new JSONObject(response);
                    Log.d(LOG_TAG, masterJSON.toString());
                    createEvents();

                } catch (Exception e){
                    e.printStackTrace();
                    Log.d(LOG_TAG, "JSON Conversion error");
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                error.printStackTrace();
                Log.d(LOG_TAG, "HTTP Request Error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("latitude", "43"); //Add the data you'd like to send to the server.
                MyData.put("longitude", "-80");
                return MyData;
            }
        };

        MyRequestQueue.add(MyStringRequest);
    }
}
