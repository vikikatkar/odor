package com.mga.vikram.odor;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VerifiedReport {
    Long reportId;
    int reporterId;
    int verifierId;
    Date dateTime;
    double lat;
    double lng;
    String odorCategory;
    String odorDescription;
    String customDescription;
    boolean verified;

    public VerifiedReport(Long reportId, int reporterId, int verifierId, Date dateTime, double lat, double lng, String odorCategory, String odorDescription, String customDescription, boolean verified) {
        this.reportId = reportId;
        this.reporterId = reporterId;
        this.verifierId = verifierId;
        this.dateTime = dateTime;
        this.lat = lat;
        this.lng = lng;
        this.odorCategory = odorCategory;
        this.odorDescription = odorDescription;
        this.customDescription = customDescription;
        this.verified = verified;
    }

    public static VerifiedReport getReportFromJSON(JSONObject jsonReport){
        VerifiedReport report = null;
        try {
            Date dateTime= null;
            try {
                dateTime = new SimpleDateFormat("MM-dd-yyyy HH:mm").parse(jsonReport.getString("dateTime"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            report = new VerifiedReport(
                    jsonReport.getLong("reportId"),
                    jsonReport.getInt("reporterId"),
                    jsonReport.getInt("verifierId"),
                    dateTime,
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
            jsReport.put("reportId",reportId);
            jsReport.put("reporterId",reporterId);
            jsReport.put("verifierId",verifierId);
            //Date Server puts
            jsReport.put("lat",lat);
            jsReport.put("lng",lng);
            jsReport.put("odorCategory",odorCategory);
            jsReport.put("odorDescription",odorDescription);
            jsReport.put("customDescription",customDescription);
            jsReport.put("verified",verified);
            return jsReport;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
