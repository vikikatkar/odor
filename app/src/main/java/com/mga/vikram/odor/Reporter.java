package com.mga.vikram.odor;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.core.content.PermissionChecker;
import com.google.firebase.auth.FirebaseUser;

public class Reporter implements LocationListener {

    boolean loggedIn;
    String emailId;
    String displayName;
    double lat;
    double lng;
    boolean locationAvailable;

    protected Reporter() {
        loggedIn = false;
        locationAvailable = false;
        this.displayName = "No yet Signed In";
    }

    /*
    private static Reporter reporterInstance;
    public static Reporter getInstance(){

        if( null == reporterInstance){
            reporterInstance = new Reporter();
        }
        return reporterInstance;
    }
    */

    FirebaseUser firebaseUser;
    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
        if( firebaseUser != null ) {
            this.setDisplayName(firebaseUser.getDisplayName());
            this.setEmailId(firebaseUser.getEmail());
            loggedIn = true;
        }
    }
    public String getDisplayName() {
        return displayName;
    }
    public String getEmailId() {
        return emailId;
    }
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }
    boolean isLoggedIn(){
        return loggedIn;
    }

    public void logout(){
        loggedIn = false;
        locationAvailable = false;
        this.displayName = "No yet Signed In";
        this.setEmailId(null);
        this.firebaseUser = null;
    }
    boolean isLocationAvailable() {
        return locationAvailable;
    }
    void updateLocation(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        int permission = PermissionChecker.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permission == PermissionChecker.PERMISSION_GRANTED) {
            // good to go
            locationAvailable = true;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
        } else {
            // permission not granted, you decide what to do
            locationAvailable = false;
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        setLat(location.getLatitude());
        setLng(location.getLongitude());
    }
    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }
    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}
