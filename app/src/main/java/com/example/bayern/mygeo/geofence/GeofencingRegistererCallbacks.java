package com.example.bayern.mygeo.geofence;

import com.google.android.gms.common.ConnectionResult;

public interface GeofencingRegistererCallbacks {
    public void onApiClientConnected();
    public void onApiClientSuspended();
    public void onApiClientConnectionFailed(ConnectionResult connectionResult);

    public void onGeofencesRegisteredSuccessful();
}
