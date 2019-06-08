package com.example.part4assignment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int REQUEST_LOCATION = 99;
    private GoogleMap mMap;
    MarkerOptions mo=new MarkerOptions();
    Marker m=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url="https://api.opencagedata.com/geocode/v1/json?q=countdown&key="+getString(R.string.api_key)+"&language=en&pretty=1";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
    //            StringBuilder localities = new StringBuilder();
      //          StringBuilder latitude = new StringBuilder();

                try {
                    JSONArray data = response.getJSONArray("results");
    //                JSONArray data1 = response.getJSONArray("latitude");



                    for (int index = 0; index < data.length(); index++) {

                        JSONObject result = data.getJSONObject(index);
                        JSONObject geometry = result.getJSONObject("geometry");
                        LatLng cl = new LatLng(Double.parseDouble(geometry.getString("lat")), Double.parseDouble(geometry.getString("lng")));


                        mo.position(cl).title("CountDown");
                        m = mMap.addMarker(mo);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(cl));
                        mMap.resetMinMaxZoomPreference();
//                        localities.append(data.get(index) + "\n");
//                        latitude.append(data1.get(index) + "\n");

                    }

//                    localities.append(data.get(0));
//                    localities.append(data.get(1));
                    System.err.println(data);
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }
                //  textView.setText(localities.toString());

            }



        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //       textView.setText("That did not work!");
                    }
                }

        );

        queue.add(jsObjRequest);

        final FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(MapsActivity.this);


  //      LocationRequest req = new LocationRequest();

   //     req.setInterval(10000); // 10 seconds

   //     req.setFastestInterval(500); // 500 milliseconds
   //     req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
//request the permission
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            // Permission has already been granted

//            client.requestLocationUpdates(req, new LocationCallback() {
//                @Override
//                public void onLocationResult(LocationResult locationResult) {
//                    Log.e("location:", locationResult.getLastLocation().toString());
//
//                    Toast.makeText(MapsActivity.this, locationResult.getLastLocation().toString(), Toast.LENGTH_LONG).show();
// Add a marker in Sydney and move the camera
//                    LatLng clocation = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
//                    mMap.clear();
//                    mMap.addMarker(new MarkerOptions().position(clocation).title("Current Location"));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(clocation, 17.2f));

                }
            }









    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
