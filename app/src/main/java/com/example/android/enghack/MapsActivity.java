package com.example.android.enghack;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    final String LOG_TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    ArrayList<Event> events;
    private Event setEvent = new Event("","","",0.0,0.0,"");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        events = getIntent().getBundleExtra("Bundle").getParcelableArrayList("EVENTS");
    }

    public void addMarkers() {
        mMap.addMarker(new MarkerOptions().title("HELLO!").
                position(new LatLng(43.473, -80.54)).
                snippet("DESCRIPTION"));
        try{
            for(int i = 0; i < events.size(); i++){
                Event mEvent = events.get(i);
                Log.d(LOG_TAG, "Event Created: " + mEvent.toString());
                mMap.addMarker(new MarkerOptions().title(mEvent.getEventName())
                        .snippet(mEvent.getDescription())
                        .position(new LatLng(mEvent.getLatitude(), mEvent.getLongitude())))
                        .setIcon(BitmapDescriptorFactory.defaultMarker(colorSelector(mEvent.getType())));
            }
        } catch (NullPointerException e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Null Pointer Error when setting colors");
        }
    }
    Float colorSelector(String type){
        Log.d(LOG_TAG, "Type is "+ type);
        switch (type){
            case "food":
                return BitmapDescriptorFactory.HUE_RED;
            case "shopping":
                return BitmapDescriptorFactory.HUE_AZURE;
            case "garbage":
                return BitmapDescriptorFactory.HUE_YELLOW;
            case "washroom":
                return BitmapDescriptorFactory.HUE_VIOLET;
            case "events":
                return BitmapDescriptorFactory.HUE_ORANGE;
        }



        return (float) 0.0;
    }


    private void setMapLongClick(final GoogleMap map){
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener(){
            @Override
            public void onMapLongClick(LatLng latLng) {
                setEvent.Latitude=latLng.latitude;
                setEvent.Longitude=latLng.longitude;
                getEventDetails();
            }
        });
    }

    /**
     * The following 4 methods allow users to specify an event to add
     */
    private void getEventDetails(){
         showAddItemDialog(MapsActivity.this, setEvent);
    }
    private void showAddItemDialog(final Context c, final Event newEvent) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Add a New Event")
                .setMessage("What is the title?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newEvent.EventName = String.valueOf(taskEditText.getText());
                        addDescription(c, newEvent);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
    private void addDescription(final Context c, final Event newEvent) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Add a description.")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newEvent.Description = String.valueOf(taskEditText.getText());
                        addType(c, newEvent);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
    private void addType(final Context c, final Event newEvent) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Add a Type of event")
                .setMessage("food, shopping, washroom, garbage, events")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newEvent.Type = String.valueOf(taskEditText.getText());
                        Log.d(LOG_TAG, "Set Event is now: " + newEvent);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng waterloo = new LatLng(43.472899, -80.539542);
        float zoom = 15;
        mMap.addMarker(new MarkerOptions().position(waterloo).title("Here We Are!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(waterloo, zoom));

        addMarkers();
        setMapLongClick(mMap);
    }

    /**
     * Creates a new event, posts towards the API
     * @param event - event being sent
     */
    public void createNewEvent(final Event event){
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        String url = getString(R.string.urlInsert);

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d(LOG_TAG, "Success!");
                /**
                 * Create a toast to let the use know
                 */
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
                MyData.put("event", event.getEventName());
                MyData.put("description", event.getDescription());
                MyData.put("latitude", event.getLatitude().toString());
                MyData.put("longitude", event.getLongitude().toString());
                MyData.put("type", event.getType());
                return MyData;
            }
        };

        mRequestQueue.add(MyStringRequest);

    }
}
