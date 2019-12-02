package com.mga.vikram.odor;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        LatLng milpitas = new LatLng(37.432335, -121.899574);
        MarkerOptions aMarkerOption = new MarkerOptions().position(milpitas).title("Milpitas");
        //aMarker.setTag(0);
        //aMarker.setSnippet("Smell Type : Fertilizer\n Level : High");

        BitmapDescriptor bd = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
        //BitmapDescriptor bd = BitmapDescriptorFactory.fromAsset("poop.png");
        aMarkerOption.icon(bd);

        Marker aMarker = mMap.addMarker(aMarkerOption);
        aMarker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(milpitas));

        mMap.setOnMarkerClickListener(this);

        String url = OdorServer.serverURL+"/odor/report";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            String statOf = null;
                            // Display the first 500 characters of the response string.
                            Log.i("MapFragment", " Response :  " + array);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonReport = array.getJSONObject(i);

                                Report report = Report.getReportFromJSON(jsonReport);

                                LatLng pos = new LatLng(report.lat,report.lng);
                                MarkerOptions aMarkerOption = new MarkerOptions().position(pos).title(report.odorCategory);
                                BitmapDescriptor bd = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                                aMarkerOption.icon(bd);

                                Marker aMarker = mMap.addMarker(aMarkerOption);
                                aMarker.showInfoWindow();
                                aMarker.setTag(report);
                            }

                        } catch (
                                JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MapFragment", "Error on request : "+error.getMessage());
            }
        });
        queue.add(stringRequest);

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        //if (clickCount != null) {
        //    clickCount = clickCount + 1;
        Report report = (Report) marker.getTag();
            Toast.makeText(this,
                    marker.getTitle() +
                            report.odorDescription + " " + report.customDescription,
                    Toast.LENGTH_SHORT).show();
        //}

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

}
