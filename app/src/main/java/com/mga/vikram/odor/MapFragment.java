package com.mga.vikram.odor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener{
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps,container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        return view;
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

        Reporter reporter = Reporter.getInstance();
        if( reporter != null && reporter.isLoggedIn() ) {
            //TBD : Check if User is Verifier or Reporter
            //OdorServer.getReport(getContext());

            String url = OdorServer.serverURL+"/odor/report";
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(milpitas));

        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Report report = (Report) marker.getTag();
        if( report == null ){
            Toast.makeText(getActivity().getApplicationContext(),
                    marker.getTitle() +
                            " The city Of Milpitas!",
                    Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(getActivity().getApplicationContext(),
                    marker.getTitle() + " \n " +
                            report.odorDescription
                            + ( report.customDescription.length() > 0 ? "\n " + report.customDescription: ""),
                    Toast.LENGTH_SHORT).show();
            //Reporter is Verifier
            //if( Reporter.getInstance().)
            {
                Reporter verifier = Reporter.getInstance();
                double R = 6371e3; // metres
                double report_l_r = Math.toRadians(report.lat);
                double verifier_l_r = Math.toRadians(verifier.getLat());
                double lat_diff = Math.toRadians((verifier.getLat()-report.lat));
                double lng_diff = Math.toRadians((verifier.getLng()-report.lng));

                double a = Math.sin(lat_diff/2) * Math.sin(lat_diff/2) +
                        Math.cos(report_l_r) * Math.cos(verifier_l_r) *
                                Math.sin(lng_diff/2) * Math.sin(lng_diff/2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

                double d = R * c;

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                // Create and show the dialog.
                if ( d > 100 ){
                    YesNoDialog newFragment = new YesNoDialog ("Verifier! ",
                            "Do you want to verify this report? \n " +
                                    "You will have to be in same vicinity as this report to verify this report. \n" +
                                    " Current Distance : " + d + " meters"
                    );
                    newFragment.show(ft, "dialog");
                }else{
                    YesNoDialog newFragment = new YesNoDialog ("Verifier! ",
                            "Do you want to verify this report? \n " +
                                    "You are near the reported location! Thanks for getting here in time!"
                    );
                    newFragment.show(ft, "dialog");
                }
            }
        }
        //}

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
}
