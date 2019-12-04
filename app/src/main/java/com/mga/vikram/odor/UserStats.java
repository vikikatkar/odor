package com.mga.vikram.odor;

import org.json.JSONException;
import org.json.JSONObject;

public class UserStats {

    public UserStats(int emailHash) {
        this.emailHash = emailHash;
    }

    public UserStats(int emailHash, int numberOfReports, int numberOfReportsVerified, int numberOfVerifications, int numberOfReportsInLast30Days, int activeDaysInLast30Days, boolean isVerifier) {
        this.emailHash = emailHash;
        this.numberOfReports = numberOfReports;
        this.numberOfReportsVerified = numberOfReportsVerified;
        this.numberOfVerifications = numberOfVerifications;
        this.numberOfReportsInLast30Days = numberOfReportsInLast30Days;
        this.activeDaysInLast30Days = activeDaysInLast30Days;
        this.isVerifier = isVerifier;
    }

    int emailHash;
    int numberOfReports;
    int numberOfReportsVerified;
    int numberOfVerifications;
    int numberOfReportsInLast30Days;
    int activeDaysInLast30Days;
    boolean isVerifier;


    public static UserStats getUserStatsFromJSON(JSONObject jsonReport){
        UserStats userStats = null;
        try {
            userStats = new UserStats(
                    jsonReport.getInt("emailHash"),
                    jsonReport.getInt("numberOfReports"),
                    jsonReport.getInt("numberOfReportsVerified"),
                    jsonReport.getInt("numberOfVerifications"),
                    jsonReport.getInt("numberOfReportsInLast30Days"),
                    jsonReport.getInt("activeDaysInLast30Days"),
                    jsonReport.getBoolean("verifier")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userStats;
    }
    public int getEmailHash() {
        return emailHash;
    }
    public void setEmailHash(int emailHash) {
        this.emailHash = emailHash;
    }
    public int getNumberOfReports() {
        return numberOfReports;
    }
    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }
    public int getNumberOfReportsVerified() {
        return numberOfReportsVerified;
    }
    public void setNumberOfReportsVerified(int numberOfReportsVerified) {
        this.numberOfReportsVerified = numberOfReportsVerified;
    }
    public int getNumberOfVerifications() {
        return numberOfVerifications;
    }
    public void setNumberOfVerifications(int numberOfVerifications) {
        this.numberOfVerifications = numberOfVerifications;
    }
    public int getNumberOfReportsInLast30Days() {
        return numberOfReportsInLast30Days;
    }
    public void setNumberOfReportsInLast30Days(int numberOfReportsInLast30Days) {
        this.numberOfReportsInLast30Days = numberOfReportsInLast30Days;
    }
    public int getActiveDaysInLast30Days() {
        return activeDaysInLast30Days;
    }
    public void setActiveDaysInLast30Days(int activeDaysInLast30Days) {
        this.activeDaysInLast30Days = activeDaysInLast30Days;
    }
    public boolean isVerifier() {
        return isVerifier;
    }
    public void setVerifier(boolean isVerifier) {
        this.isVerifier = isVerifier;
    }

}
