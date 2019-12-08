package com.mga.vikram.odor;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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
    VerifiedReport verifiedReport;
    private Spinner spinner;
    private Handler handler;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submit_odor_report,null);

        report = null;
        verifiedReport = null;

        Verifier reporter = Verifier.getInstance();
        String displayName = reporter.getDisplayName();

        spinner = view.findViewById(R.id.odorSpinner);

        ArrayAdapter<CharSequence> odorAdapter = ArrayAdapter.createFromResource(getContext(),
                            R.array.odors,
                            R.layout.spinner_textview);
        odorAdapter.setDropDownViewResource(R.layout.spinner_textview);

        spinner.setAdapter(odorAdapter);

        spinner.setOnItemSelectedListener(this);


        TextView reportedTextView = view.findViewById(R.id.reportedTextView);

        //Disable Submission till item is not selected
        Button submitReportButton = view.findViewById(R.id.submitReportButton);
        if( reporter.isVerifier() && reporter.getReportToVerify()!=null){
            submitReportButton.setText("Verifiy");

            reportedTextView.setText("Reported Odor at this location : \n"
                    + reporter.getReportToVerify().odorCategory + "\n "
                    + reporter.getReportToVerify().odorDescription);

        }else{
            submitReportButton.setText("Report");
            reportedTextView.setText("");//Blank it
        }
        submitReportButton.setClickable(false);
        submitReportButton.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);

        return view;
    }

    private boolean userLooking;
    @Override
    public void onStart() {
        super.onStart();
        userLooking = true;
        //To Open Spinner automatically
        handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Open the Spinner...
                        if( userLooking ) {
                            spinner.performClick();
                            userLooking = false;
                        }
                  }
                }, 2000);
            }
        }).start();
    }

    @Override
    public void onStop() {
        super.onStop();
        userLooking = false;
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
        Verifier reporter = Verifier.getInstance();


        if( reporter.isVerifier() && reporter.getReportToVerify()!=null) {
            boolean isVerifiedTrue = !reporter.getReportToVerify().odorDescription.equals(getString(R.string.clean_air));
            verifiedReport = new VerifiedReport(reporter.getReportToVerify(),
                    reporter.getEmailIdHash(),"", odorDescription,"",
                    isVerifiedTrue);
        }else{
            report = new Report(0L, new Date(), reporter.emailId.hashCode(),
                    reporter.lat,reporter.lng,"", odorDescription,"", false);
        }
        //Toast.makeText(parent.getContext(), odorDescription, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {
        //Submit button
        Verifier reporter = Verifier.getInstance();

        String url = getString(R.string.server_base_url);
        RequestQueue queue = Volley.newRequestQueue(getView().getContext());
        JsonObjectRequest stringRequest;

        if( reporter.isVerifier() && reporter.getReportToVerify()!=null) {
            url+="odor/verify";


            stringRequest = new JsonObjectRequest(Request.Method.POST, url, verifiedReport.getJSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Odor-Server", " Response :  " + response);

                        Verifier reporter = Verifier.getInstance();
                        String displayName = reporter.getDisplayName();

                        Toast.makeText(getView().getContext(), displayName + "\n Thank you for Verigying Odor Report!" +
                                        "\n You are helping Milpitas community in understanding odor cause!"
                                , Toast.LENGTH_LONG).show();

                        //Resetting View
                        Button submitReportButton = getView().findViewById(R.id.submitReportButton);
                        submitReportButton.setClickable(false);
                        submitReportButton.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                        submitReportButton.setText("Report");
                        TextView reportedTextView = getView().findViewById(R.id.reportedTextView);
                        reportedTextView.setText("");//Blank it
                        reporter.verifyReport(null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Odor-Server", "Error on request : "+error.getMessage());
                    }
                });
        }else{
            url+="odor/report";
            stringRequest = new JsonObjectRequest(Request.Method.POST, url, report.getJSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Odor-Server", " Response :  " + response);

                        Verifier reporter = Verifier.getInstance();
                        String displayName = reporter.getDisplayName();

                        Toast.makeText(getView().getContext(), displayName + "\n Thank you for Reporting Odor!" +
                                        "\n You are helping Milpitas community in understanding odor cause!"
                                , Toast.LENGTH_LONG).show();
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
        }


        queue.add(stringRequest);
    }
}
