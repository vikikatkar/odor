package com.mga.vikram.odor;

import com.google.firebase.auth.FirebaseUser;

public class Reporter {
    private Reporter() {
        loggedIn = false;
        this.displayName = "No yet Signed In";
    }
    boolean loggedIn;

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
        this.setDisplayName(firebaseUser.getDisplayName());
    }

    FirebaseUser firebaseUser;
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    String displayName;
    long lat;
    long lng;

    private static Reporter reporterInstance;

    public static Reporter getInstance(){

        if( null == reporterInstance){
            reporterInstance = new Reporter();
        }
        return reporterInstance;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public void setLng(long lng) {
        this.lng = lng;
    }

    public long getLat() {
        return lat;
    }

    public long getLng() {
        return lng;
    }
}
