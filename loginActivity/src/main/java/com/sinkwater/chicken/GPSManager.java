package com.sinkwater.chicken;

/**
 * Created by justin on 11/16/14.
 */

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

/*
TODO:
- GPS Manager is mainly used to:
    get the user's GPS location
    compare it with the user's GPS location and determine if it falls within a certain defined range

*/

public class GPSManager extends Service implements LocationListener {

    public final Context mContext;

    public GPSManager(Context context) {
        this.mContext = context;
    }

    /*
    public Location getLocation() {
        //Code
        return new Location;
    }
    */

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
