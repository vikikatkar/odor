package com.mga.vikram.odor;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class OdorServer{
    static String serverURL = "http://192.168.0.109:8080/";
    public static void getReport(Context context){
        String url = serverURL+"/odor/report";
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String statOf = null;
                            // Display the first 500 characters of the response string.
                            Log.i("OdorServer", "Response : to String : " + response.getString(0));
                            Log.i("OdorServer", " Response :  " + response);
                        } catch (
                                JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("OdorServer", "Error on request : "+error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    public static void sendReport(Context context, Report report){
        String url = serverURL +"odor/report";
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String statOf = null;
                            // Display the first 500 characters of the response string.
                            Log.i("OdorServer", "Response : to String : " + response.getString(0));
                            //JSONArray array = response.getJSONArray("details");
                            /*
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject stat = array.getJSONObject(i);
                                Log.d("RESPONSE - stat", stat.toString());
                                double timeOfYear = Double.parseDouble(stat.get("key").toString().split(" ")[1]);
                                double steps = Double.parseDouble(stat.get("steps").toString());
                                Log.i("Stats", "timeOfYear " + timeOfYear + " steps " + steps);
                                Log.i("Stats", "timeOfYear " + stat.get("key").toString());
                                if(statOf == null) // Set only if not found earlier
                                    statOf = stat.get("key").toString().split(" ")[0]; // DAY /WEEK /MONTH

                            }*/
                            Log.i("OdorServer", " Response :  " + response);
                            //graph.getViewport().setYAxisBoundsManual(true);

                        } catch (
                                JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("OdorServer", "Error on request : "+error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

}