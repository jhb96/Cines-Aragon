package com.example.cinesaragon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    private Marker currentUserLocationMarker;
    private int ProximityRadius = 1000;
    private String mapKey = "AIzaSyCiMzmMun-pBgyHtezTpxaUJ6TTbm0wsJM";
    double latitude, longitude;

    private String cineElegido;

    Location myLocation;
    Location aragonLocation;

    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;

//    public static HashMap<String, String> cinemas;
    public static String[] cinemas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final Spinner spinner = findViewById(R.id.cinemas_spinner);
        Button carteleraButton = findViewById(R.id.button_cartelera);

        setUPGClient();

        String [] cinemas_array = new String[4];

        cinemas_array[0] = "Cines Toyac";
        cinemas_array[1] = "Cines Pepe";
        cinemas_array[2] = "Cines Ramires";
        cinemas_array[3] = "Cines Platon";


// Spinner de cinemas

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, cinemas_array);

        // Specify the layout to use when the list of choices appears
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(spinnerArrayAdapter);

        View.OnClickListener onCarteleraButtonListener = new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CarteleraActivity.class);

                i.putExtra("cineElegido", (String) spinner.getSelectedItem());
                startActivity(i);
            }
        };

        carteleraButton.setOnClickListener(onCarteleraButtonListener);

    }


    private void setUPGClient(){

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


       // LatLng sydney = new LatLng(-34, 151);

       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sidney"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(aragon, 15.0f));

        setAragonLocation();
        getNearByCinemas();

    }




    @Override
    public void onLocationChanged(Location location){
        myLocation = location;

        //setAragonLocation();
//        if(myLocation != null){
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
//
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(new LatLng(latitude, longitude));
//            markerOptions.title("you");
//            mMap.addMarker(markerOptions);
//
            getNearByCinemas();
//        }

    }

    /**
     * Method to genereate the Url to find the nearby cinemas
     * @return
     */
    private void  getNearByCinemas(){


        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?query=cinemas+in+Aragon&key=AIzaSyCiMzmMun-pBgyHtezTpxaUJ6TTbm0wsJM");

        latitude = 41.656;
        longitude = -0.87734;

//        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
//        googleURL.append("location=" + latitude + "," + longitude);
//        googleURL.append("&radius=" + ProximityRadius);
//        googleURL.append("&type=" + "Cinema");
//        googleURL.append("&key=" + mapKey);

        String url = googleURL.toString();

        Log.d("MapsActivity", "url = " + url);


        Object transferData[] = new Object[2];
        transferData[0] = mMap;
        transferData[1] = url;

        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
        getNearbyPlaces.execute(transferData);

        Toast.makeText(this, "Searching cinemas", Toast.LENGTH_SHORT).show();
    }




    private void getMyLocation(){
        if(googleApiClient!=null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    myLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, (com.google.android.gms.location.LocationListener) this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(MapsActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {


                                        myLocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);


                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(MapsActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);


                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }


                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });

                }
            }
        }
    }


    private void setAragonLocation(){
        LatLng aragon = new LatLng(41, -1);

        LatLng zgz = new LatLng(41.656, -0.87734);

        //mMap.addMarker(new MarkerOptions().position(aragon).title("Marker in Aragon"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(zgz));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(zgz, 13.0f));

//        getNearByCinemas();
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(aragon);
//        markerOptions.title("Aragon");
//        mMap.addMarker(markerOptions);
//


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        checkPermission();

    }

    private void checkPermission(){

        int permissionLocation = ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermission = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);

            if(!listPermission.isEmpty()){
                ActivityCompat.requestPermissions(this, listPermission.toArray(new String[listPermission.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }
        else{
        //getMyLocation();

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionLocation == PackageManager.PERMISSION_GRANTED){
            //getMyLocation();

        } else {


        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
