package com.diamond.future.service;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class AppLocationService extends Service implements LocationListener {

    protected LocationManager locationManager;
    public static Location location;
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;
    Context context;
    Activity activity;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public AppLocationService(Context context, Activity activity) {
        this.context=context;
        this.activity=activity;
        locationManager = (LocationManager) context
                .getSystemService(LOCATION_SERVICE);
    }

    public Location getLocation(String provider) {
        if (locationManager.isProviderEnabled(provider)) {
            if (checkAndRequestPermissions()) {
                locationManager.requestLocationUpdates(provider,
                        MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(provider);
                    return location;
                }
            }
        }
        return null;
    }
    public boolean checkAndRequestPermissions() {
        try {
            int locationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
            int readPhoneState = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
            int profilephoto = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

            List<String> listPermissionsNeeded = new ArrayList<>();
            if (locationPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (profilephoto != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (readPhoneState != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }
        } catch (Exception e) {

        }
        return true;
    }
    @Override
    public void onLocationChanged(Location location) {
        this.location=location;
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (locationManager.isProviderEnabled(provider)) {
            if (checkAndRequestPermissions()) {
                locationManager.requestLocationUpdates(provider,
                        MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
                if (locationManager != null) {
                    this.location = locationManager.getLastKnownLocation(provider);
                }
            }
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
