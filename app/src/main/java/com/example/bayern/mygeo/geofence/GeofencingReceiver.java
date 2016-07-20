package com.example.bayern.mygeo.geofence;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.bayern.mygeo.activities.MainActivity;
import com.example.bayern.mygeo.R;

public class GeofencingReceiver extends ReceiveGeofenceTransitionIntentService
{
    @Override
    protected void onEnteredGeofences(String[] strings)
    {
        Log.d(GeofencingReceiver.class.getName(), "onEnter");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.geo_icon)
                        .setContentTitle("You have just entered a location:")
                        .setContentText(strings[0])
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(this, MainActivity.class);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }


    @Override
    protected void onExitedGeofences(String[] strings)
    {
        Log.d(GeofencingReceiver.class.getName(), "onExit");

        Toast.makeText(getApplicationContext(), "onExitedGeofences",
                Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onError(int i)
    {
        Log.e(GeofencingReceiver.class.getName(), "Error: " + i);

        Toast.makeText(getApplicationContext(), "onError",
                Toast.LENGTH_LONG).show();
    }

}