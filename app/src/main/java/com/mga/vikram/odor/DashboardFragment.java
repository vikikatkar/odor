package com.mga.vikram.odor;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class DashboardFragment extends Fragment{
    private static final String TAG = "DashboardFragment";

    private static String htmlText = "<h2>%s</h2><br><p>Welcome!</p>";
    private static final String statsHtmlStart = "<body>\n" +
            "\t<h1>Dashboard for %s </h1><br>\n" +
            "Number of Odor Reports&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: %s <br>\n" +
            "Verified Reports&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: %s <br>\n" +
            "Number of Reports Done in last 30 days: %s <br>\n" +
            "Active days in last 30 days&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: %s <br>";
    private static final String statsHtml_reporterEnd= "\t<br>\n" +
            "\t<p>Milpits City's odor issues will be solved by our <strong>common efforts</strong></p>\n" +
            "</body>";
    private static final String statsHtml_verifierEnd="Number of Verifications done&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: %s <br>\n" +
            "\t<br>\n" +
            "\t<p>We are grateful for your serivce as a <strong>Verifier</strong>!</p>\n" +
            "</body>";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard,null);

        Reporter reporter = Verifier.getInstance();
        String displayName = reporter.getDisplayName();
        TextView mTextMessage;
        mTextMessage = view.findViewById(R.id.message);

        htmlText = String.format(htmlText,displayName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTextMessage.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT));
        } else {
            mTextMessage.setText(Html.fromHtml(htmlText));
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        Reporter reporter = Verifier.getInstance();
        if( reporter.isLoggedIn() ){

            String url = getString(R.string.server_base_url)+"/odor/stats/"+reporter.getEmailIdHash();
            Log.d(TAG, "url : "+url);

            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            UserStats userStats = UserStats.getUserStatsFromJSON(jsonObject);

                            TextView mTextMessage;
                            mTextMessage = getView().findViewById(R.id.message);
                            String displayName = Verifier.getInstance().getDisplayName();

                            String formattedHtmlText = String.format(statsHtmlStart,
                                    displayName,
                                    userStats.numberOfReports,
                                    userStats.numberOfReportsVerified,
                                    userStats.numberOfReportsInLast30Days,
                                    userStats.activeDaysInLast30Days);
                            if( Verifier.getInstance().isVerifier() ){
                                formattedHtmlText += String.format(statsHtml_verifierEnd,
                                        userStats.numberOfVerifications);
                            }else{
                                formattedHtmlText+=statsHtml_reporterEnd;
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                mTextMessage.setText(Html.fromHtml(formattedHtmlText, Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                mTextMessage.setText(Html.fromHtml(formattedHtmlText));
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
    }

}
