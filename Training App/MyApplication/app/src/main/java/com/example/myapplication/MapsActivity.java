package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLatLong;
    private GoogleMap mMap;

    Runnable runnable;
    Handler handler;
    LatLng bitis,baslangic;
    LatLng yeni,son;
    boolean aldi=false;



    // private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //binding = ActivityMapsBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(@NonNull Location location) {
                LatLng gecici;
                gecici=bitis;
                userLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                //mMap.clear();
               // mMap.addMarker(new MarkerOptions().position(userLatLong).title("your location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLong));
                bitis=userLatLong;
                Toast.makeText(MapsActivity.this,"changed",Toast.LENGTH_SHORT).show();
                addline(gecici,bitis);


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };

        askLocationPermission();
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void askLocationPermission() {
        //Toast.makeText(MapsActivity.this,"girdi",Toast.LENGTH_SHORT).show();
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                //kullanıcının son konumunu alma
                Location lastLocation =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                userLatLong = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLatLong).title("your location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLong));
                //mMap.clear();
                if(aldi==false)
                {
                    yeni=userLatLong;
                    aldi=true;

                }
                bitis=userLatLong;
                //addline();
                yeni=bitis;
               // zaman();


            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                token.continuePermissionRequest();
            }
        }).check();
    }

    private void addline(LatLng a,LatLng b)
    {
        //Toast.makeText(MapsActivity.this,"çizgiye girdi",Toast.LENGTH_SHORT).show();
        mMap.addPolyline(new PolylineOptions().add(new LatLng(a.latitude,a.longitude),new LatLng(b.latitude,b.longitude)).width(5).color(Color.RED));
    }

    public void zaman(){
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                askLocationPermission();
                //Toast.makeText(MapsActivity.this,"update data",Toast.LENGTH_SHORT).show();
            }
        };handler.postDelayed(runnable,14000);


    }

    
}