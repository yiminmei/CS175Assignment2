package com.wearable.fitnessapp;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import android.location.Location;
import android.location.LocationManager;
import android.location.Criteria;
import android.content.pm.PackageManager;
import android.graphics.Color;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;


public class Gmap extends Fragment implements OnMapReadyCallback,
                             LocationListener,GoogleApiClient.ConnectionCallbacks,
                                           GoogleApiClient.OnConnectionFailedListener
{
    Location beginninglocation;
    private  GoogleApiClient googleApiClient;
    LocationRequest mLocationRequest;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 500 * 10;
    private static final long FASTEST_INTERVAL = 500 * 5;
    Location mCurrentLocation;
    GoogleMap googleMap;
    private boolean startworkout=false;

    public void receive(boolean clicked){
        startworkout= clicked;
    }

    public void onStart(){
        super.onStart();;
        googleApiClient.connect();
    }
    public void onStop(){
        super.onStop();
        googleApiClient.disconnect();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this.getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
        }

        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onLocationChanged(Location location) {

        if(startworkout==true) {
            Polyline line = googleMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),
                            new LatLng(location.getLatitude(),location.getLongitude()))
                    .width(5)
                    .color(Color.RED));

        }
        mCurrentLocation= location;

        googleMap.moveCamera(CameraUpdateFactory.newLatLng
                (new LatLng(location.getLatitude(),location.getLongitude())));

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.gmap,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapFragment mapFragment = (MapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        googleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String locationProvider = lm.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this.getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

        }
        beginninglocation = lm.getLastKnownLocation(locationProvider);
        mCurrentLocation = beginninglocation;
        googleMap.setMyLocationEnabled(true);
        this.googleMap= googleMap;
        googleMap.moveCamera(CameraUpdateFactory.zoomBy(14));

    }
}

