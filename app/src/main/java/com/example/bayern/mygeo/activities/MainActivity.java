package com.example.bayern.mygeo.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.bayern.mygeo.R;
import com.example.bayern.mygeo.fragments.GeoLocationFragment;
import com.example.bayern.mygeo.fragments.ListFragment;
import com.example.bayern.mygeo.geofence.GeofencingRegisterer;
import com.example.bayern.mygeo.geofence.GeofencingRegistererCallbacks;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.Geofence;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private static final int GEOFENCE_RADIUS_IN_METERS = 100;
    private static final String GEOFANCE_ID = "GEOFANCE_ID_10";
    private FragmentPagerAdapter mAdapterViewPager;
    private ArrayList<Geofence> mGeofenceList = new ArrayList<>();
    private GeofencingRegisterer mGeofencingRegisterer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.history));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.create_new_geo_location));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });

        //starting geo from here:
        //=======================

        mGeofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(GEOFANCE_ID)

                .setCircularRegion(
                        32.032001,
                        34.892691,
                        GEOFENCE_RADIUS_IN_METERS
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build());

        mGeofencingRegisterer = new GeofencingRegisterer(this);
        mGeofencingRegisterer.setGeofencingCallback(createGeofencingCallback());
        mGeofencingRegisterer.registerGeofences(mGeofenceList);
    }

    private GeofencingRegistererCallbacks createGeofencingCallback()
    {
        return new GeofencingRegistererCallbacks()
        {
            @Override
            public void onApiClientConnected()
            {
                Toast.makeText(MainActivity.this, "onApiClientConnected()",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onApiClientSuspended()
            {
                Toast.makeText(MainActivity.this, "onApiClientSuspended()",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onApiClientConnectionFailed(ConnectionResult connectionResult)
            {
                Toast.makeText(MainActivity.this, "onApiClientConnectionFailed()",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onGeofencesRegisteredSuccessful()
            {
                Toast.makeText(MainActivity.this, "onGeofencesRegisteredSuccessful()",
                        Toast.LENGTH_SHORT).show();
            }
        };
    }

    public static class PagerAdapter extends FragmentStatePagerAdapter
    {
        private static int NUM_ITEMS = 2;

        public PagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    return ListFragment.newInstance();
                case 1:
                    return GeoLocationFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount()
        {
            return NUM_ITEMS;
        }
    }
}
