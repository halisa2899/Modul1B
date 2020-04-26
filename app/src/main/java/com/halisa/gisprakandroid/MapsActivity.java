package com.halisa.gisprakandroid;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;

import android.os.Build;
import android.os.Bundle;
import android.view.View;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private Marker mCurrLocationMarker;
    private Location mLastLocation;
    private String[] permission = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getUserPermission();
    }

    private void getUserPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(permission,0);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady (GoogleMap googleMap){
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng teknikinformatika = new LatLng(-0.8422232,119.892763);
        LatLng banksyariahmandiri = new LatLng(-0.840035,119.883474);
        //Custom size marker
        int tinggi = 100;
        int lebar = 100;
        BitmapDrawable bitmapStart = (BitmapDrawable) getResources().getDrawable(R.drawable.start);
        BitmapDrawable bitmapDes = (BitmapDrawable) getResources().getDrawable(R.drawable.destination);
        Bitmap s = bitmapStart.getBitmap();
        Bitmap d = bitmapDes.getBitmap();
        Bitmap markerStart = Bitmap.createScaledBitmap(s, lebar, tinggi, false);
        Bitmap markerDes = Bitmap.createScaledBitmap(d, lebar, tinggi, false);
        //Add marker to map
        mMap.addMarker(new MarkerOptions().position(teknikinformatika).title("Marker in teknikinformatika")
                .snippet("Ini adalah Gedung Teknik Informatika")
                .icon(BitmapDescriptorFactory.fromBitmap(markerStart)));
        mMap.addMarker(new MarkerOptions().position(banksyariahmandiri).title("Marker in Bank Syariah Mandiri")
                .snippet("Ini adalah Bank Syariah Mandiri")
                .icon(BitmapDescriptorFactory.fromBitmap(markerDes)));

        mMap.addPolyline(new PolylineOptions().add(
                teknikinformatika,
                new LatLng(-0.8422232,119.892763),
                new LatLng(-0.841835, 119.892318),
                new LatLng(-0.841835, 119.894989),
                new LatLng(-0.843503, 119.894957),
                new LatLng(-0.843428, 119.890923),
                new LatLng(-0.842881, 119.883112),
                new LatLng(-0.840035,119.883474),
                banksyariahmandiri
                ).width(10)
                        .color(Color.BLUE)
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(teknikinformatika, 11.5f));

        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

    }

    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationProviderClient =
                new FusedLocationProviderClient(this);

        Task location = fusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Location currentLocation = (Location) task.getResult();
                mMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("Ini Adalah Lokasiku").snippet("Halisa Mutmainnah F55117096"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),11.5f));
            }
        });
    }

    public void Mylocation(View view) {
        getCurrentLocation();
    }
}

