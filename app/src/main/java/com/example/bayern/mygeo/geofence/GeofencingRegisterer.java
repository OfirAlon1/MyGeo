package com.example.bayern.mygeo.geofence;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class GeofencingRegisterer implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private List<Geofence> geofencesToAdd;
    private PendingIntent mGeofencePendingIntent;

    private GeofencingRegistererCallbacks mCallback;

    public final String TAG = this.getClass().getName();

    public GeofencingRegisterer(Context context)
    {
        mContext = context;
    }

    public void setGeofencingCallback(GeofencingRegistererCallbacks callback)
    {
        mCallback = callback;
    }

    public void registerGeofences(List<Geofence> geofences)
    {
        geofencesToAdd = geofences;

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    private GeofencingRequest getGeofencingRequest()
    {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofencesToAdd);
        return builder.build();
    }


    @Override
    public void onConnected(Bundle bundle)
    {
        if (mCallback != null)
        {
            mCallback.onApiClientConnected();
        }

        mGeofencePendingIntent = createRequestPendingIntent();

//        if (mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//            // TODO: Consider calling
//            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for Activity#requestPermissions for more details.
//            return;
//        }

        PendingResult<Status> result = LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, getGeofencingRequest(), mGeofencePendingIntent);
        result.setResultCallback(new ResultCallback<Status>()
        {
            @Override
            public void onResult(Status status)
            {
                if (status.isSuccess())
                {
                    // Successfully registered
                    if (mCallback != null)
                    {
                        mCallback.onGeofencesRegisteredSuccessful();
                    }
                } else if (status.hasResolution())
                {
                    // Google provides a way to fix the issue
                    /*
                    status.startResolutionForResult(
                            mContext,     // your current activity used to receive the result
                            RESULT_CODE); // the result code you'll look for in your
                    // onActivityResult method to retry registering
                    */
                } else
                {
                    // No recovery. Weep softly or inform the user.
                    Log.e(TAG, "Registering failed: " + status.getStatusMessage());
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        if (mCallback != null)
        {
            mCallback.onApiClientSuspended();
        }

        Log.e(TAG, "onConnectionSuspended: " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        if (mCallback != null)
        {
            mCallback.onApiClientConnectionFailed(connectionResult);
        }

        Log.e(TAG, "onConnectionFailed: " + connectionResult.getErrorCode());
    }


    /**
     * Returns the current PendingIntent to the caller.
     *
     * @return The PendingIntent used to create the current set of geofences
     */
    public PendingIntent getRequestPendingIntent()
    {
        return createRequestPendingIntent();
    }

    /**
     * Get a PendingIntent to send with the request to add Geofences. Location
     * Services issues the Intent inside this PendingIntent whenever a geofence
     * transition occurs for the current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence
     * transitions.
     */
    private PendingIntent createRequestPendingIntent()
    {
        if (mGeofencePendingIntent != null)
        {
            return mGeofencePendingIntent;
        } else
        {
            Intent intent = new Intent(mContext, GeofencingReceiver.class);
            return PendingIntent.getService(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }
}
