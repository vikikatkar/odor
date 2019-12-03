package com.mga.vikram.odor;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import java.util.Date;

public class OdorReportSubmissionFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    Report report;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submit_odor_report,null);

        Reporter reporter = Reporter.getInstance();
        String displayName = reporter.getDisplayName();

        Spinner spinner = view.findViewById(R.id.odorSpinner);

        ArrayAdapter<CharSequence> odorAdapter = ArrayAdapter.createFromResource(getContext(),
                            R.array.odors,
                            android.R.layout.simple_spinner_dropdown_item);

        odorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(odorAdapter);

        spinner.setOnItemSelectedListener(this);

        //Disable Submission till item is not selected
        Button submitReportButton = view.findViewById(R.id.submitReportButton);
        submitReportButton.setClickable(false);
        submitReportButton.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Button submitReportButton = getView().findViewById(R.id.submitReportButton);

        if( position == 0 ){
            submitReportButton.setClickable(false);
            submitReportButton.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            return;
        }
        String odorDescription = parent.getItemAtPosition(position).toString();
        if( submitReportButton != null ){
            submitReportButton.setClickable(true);
            submitReportButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            submitReportButton.setOnClickListener(this);
        }
        Reporter reporter = Reporter.getInstance();
        report = new Report(0L, new Date(), reporter.emailId.hashCode(),
                reporter.lat,reporter.lng,"", odorDescription,"");


        Toast.makeText(parent.getContext(), odorDescription, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        //Submit button

        String url = getString(R.string.server_base_url) +"odor/report";
        RequestQueue queue = Volley.newRequestQueue(getView().getContext());


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, report.getJSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Odor-Server", " Response :  " + response);
                        Toast.makeText(getView().getContext(), "Thanks for Reporting Odor!", Toast.LENGTH_SHORT).show();
                        Button submitReportButton = getView().findViewById(R.id.submitReportButton);
                        submitReportButton.setClickable(false);
                        submitReportButton.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                    }
                },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Odor-Server", "Error on request : "+error.getMessage());
            }
        });
        queue.add(stringRequest);
    }
}
