package com.example.mapslocation;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//Note for every g-maps projects  a new api key will be required to be added to the google_map_api.xml
//    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /> :always remember to add
//location access for AndroidManifest.xml
/* Coordinates->>>
* Double lat,lon;
 try {
   lat = location.getLatitude ();
   lon = location.getLongitude ();
   latlon[0] = lat;
   latlon[1] = lon;
   return latlon;
 }
* */

//geo coding -> address to co-ordinates
//reverse geo-coding : cor-dinates to address

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;
    private  String userAddress = null;
//    @SuppressLint("HardwareIds")
//    String androidId = Settings.Secure.getString(getContentResolver(),
//            Settings.Secure.ANDROID_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // check for permission to access location when app opens
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //min time and min distance at which the location changes the update is sent 1s= 1000ms
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locationListener);

            }
        }
    }

    public String getUserAddress(double lat, double longt){
        //reverse geocoding
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(lat, longt, 1);
            if (listAddresses != null && listAddresses.size() > 0){
                Log.i("Address:", listAddresses.get(0).getAddressLine(0));
                //address concatenation
                userAddress = "";
                if(listAddresses.get(0).getSubThoroughfare() != null){
                    userAddress +=  listAddresses.get(0).getSubThoroughfare() + "";
                }
                if (listAddresses.get(0).getThoroughfare() != null){
                    userAddress +=  listAddresses.get(0).getThoroughfare() + ", ";
                }
                if (listAddresses.get(0).getLocality() != null){
                    userAddress +=  listAddresses.get(0).getLocality() + ", ";
                }
                if (listAddresses.get(0).getPostalCode() != null){
                    userAddress +=  listAddresses.get(0).getPostalCode() + ", ";
                }
                if (listAddresses.get(0).getCountryName() != null){
                    userAddress +=  listAddresses.get(0).getCountryName() + "";
                }



            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userAddress;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //setup location manager and location listener
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
//                Toast.makeText(MapsActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
//                Log.i("Location Update", location.toString());
                Log.i("Location Update", String.valueOf(location.getLatitude()));
                //
                // Add a marker in Sydney and move the camera
                mMap.clear();
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 20));
                mMap.getUiSettings().setZoomControlsEnabled(true);
                //reverse geocoding
//                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//                try {
//                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                    if (listAddresses != null && listAddresses.size() > 0){
//                        Log.i("Address:", listAddresses.get(0).getAddressLine(0));
//                        //address concatenation
//                        userAddress = "";
//                        if(listAddresses.get(0).getSubThoroughfare() != null){
//                            userAddress +=  listAddresses.get(0).getSubThoroughfare() + "";
//                        }
//                        if (listAddresses.get(0).getThoroughfare() != null){
//                            userAddress +=  listAddresses.get(0).getThoroughfare() + ", ";
//                        }
//                        if (listAddresses.get(0).getLocality() != null){
//                            userAddress +=  listAddresses.get(0).getLocality() + ", ";
//                        }
//                        if (listAddresses.get(0).getPostalCode() != null){
//                            userAddress +=  listAddresses.get(0).getPostalCode() + ", ";
//                        }
//                        if (listAddresses.get(0).getCountryName() != null){
//                            userAddress +=  listAddresses.get(0).getCountryName() + "";
//                        }
//                        //display address on map
//                        Toast.makeText(MapsActivity.this, userAddress, Toast.LENGTH_LONG).show();
//
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                //get address
                String addr = getUserAddress(location.getLatitude(), location.getLongitude());
                //display address on map
                Toast.makeText(MapsActivity.this, addr, Toast.LENGTH_LONG).show();
                mMap.addMarker(new MarkerOptions().position(userLocation).title(addr));


            }
        };

        // here we check again; necessary for location based code base
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //ASK FOR PERMISSION FOR FINE LOCATION ACCESS
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            // WE HAVE THE PERMISSIONS
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locationListener);
            // the app shows no location once it is started as the code above only checks for a user location every 100s
            // i.e location is displays if there is a change in the location
            // we want it to remember the last known location and keep updating as the user moves
            //last known location
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
//            mMap.clear();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 20));
            mMap.getUiSettings().setZoomControlsEnabled(true);

            //get address
            String addr = getUserAddress(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(userLocation).title(addr));



        }
    }
}