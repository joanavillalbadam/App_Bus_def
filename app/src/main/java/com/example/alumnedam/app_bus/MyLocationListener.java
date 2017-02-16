package com.example.alumnedam.app_bus;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by ALUMNEDAM on 16/02/2017.
 */

public class MyLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {

        if(location != null){
            Log.e("Latitud: ", "" + location.getLatitude());
            Log.e("Longitude: ", "" + location.getLongitude());


        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
