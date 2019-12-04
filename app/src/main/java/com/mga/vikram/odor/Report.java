package com.mga.vikram.odor;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Report {
    Long reportId;
    Date dateTime;
    int emailHash;
    double lat;
    double lng;
    String odorCategory;
    String odorDescription;
    String customDescription;
    boolean verified;

    public Report(Long reportId, Date dateTime, int emailHash, double lat, double lng, String odorCategory, String odorDescription, String customDescription, boolean verified) {
        this.reportId = reportId;
        this.dateTime = dateTime;
        this.emailHash = emailHash;
        this.lat = lat;
        this.lng = lng;
        this.odorCategory = odorCategory;
        this.odorDescription = odorDescription;
        this.customDescription = customDescription;
        this.verified = verified;
    }

    public static Report getReportFromJSON(JSONObject jsonReport){
        Report report = null;
        try {
            Date dateTime= null;
            try {
                dateTime = new SimpleDateFormat("MM-dd-yyyy HH:mm").parse(jsonReport.getString("dateTime"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            report = new Report(
                    jsonReport.getLong("reportId"),
                    dateTime,
                    jsonReport.getInt("emailHash"),
                    jsonReport.getDouble("lat"),
                    jsonReport.getDouble("lng"),
                    jsonReport.getString("odorCategory"),
                    jsonReport.getString("odorDescription"),
                    jsonReport.getString("customDescription"),
                    jsonReport.getBoolean("verified")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return report;
    }


    public JSONObject getJSONObject(){
        JSONObject jsReport = new JSONObject();
        try {
            jsReport.put("emailHash",emailHash);
            //Date Server puts
            jsReport.put("lat",lat);
            jsReport.put("lng",lng);
            jsReport.put("odorCategory",odorCategory);
            jsReport.put("odorDescription",odorDescription);
            jsReport.put("customDescription",customDescription);
            return jsReport;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
