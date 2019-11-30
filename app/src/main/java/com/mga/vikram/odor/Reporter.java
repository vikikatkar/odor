package com.mga.vikram.odor;

public class Reporter {
    private Reporter(String displayName) {
        this.displayName = displayName;
    }

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
            reporterInstance = new Reporter("No Yet Signed in !");
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
