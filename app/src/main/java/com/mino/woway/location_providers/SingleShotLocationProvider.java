package com.mino.woway.location_providers;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.mino.woway.listeners.OnUserLocationUpdatedListener;

public class SingleShotLocationProvider {

    private final FusedLocationProviderClient mFusedLocationClient;
    private final OnUserLocationUpdatedListener mListener;

    private SingleShotLocationProvider(Context context, OnUserLocationUpdatedListener listener) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mListener = listener;
    }

    public static SingleShotLocationProvider newInstance(Context context, OnUserLocationUpdatedListener listener) {
        return new SingleShotLocationProvider(context, listener);
    }

    @SuppressLint("MissingPermission")
    public void requestSingleUpdate() {
        mFusedLocationClient
                .getLastLocation()
                .addOnSuccessListener(mListener::onLocationUpdated)
                .addOnFailureListener(Throwable::printStackTrace);
    }
}
