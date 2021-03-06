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
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cinesaragon.model.Cine;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Spinner spinner;

    private ArrayAdapter<String> spinnerArrayAdapter;

    private GoogleMap mMap;
    public static FirebaseDatabase db;
    GoogleApiClient googleApiClient;
    private Marker currentUserLocationMarker;
    private int ProximityRadius = 1000;
    private String mapKey = "AIzaSyCiMzmMun-pBgyHtezTpxaUJ6TTbm0wsJM";
    double latitude, longitude;

    private Cine cineElegido;

    Location myLocation;
    Location aragonLocation;

    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;

//  public static HashMap<String, String> cinemas;
    public static List<Cine> cinemas;
    public static String[] cinemas_array;
    private String cinemas_loaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        spinner = findViewById(R.id.cinemas_spinner);
        Button carteleraButton = findViewById(R.id.button_cartelera);
        final TextView direccionTextView = findViewById(R.id.text_direccion);
        final TextView horarioTextView = findViewById(R.id.text_horario);


        setUPGClient();



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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

              cineElegido = cinemas.get(i);
              direccionTextView.setText(cineElegido.getDireccion());
              horarioTextView.setText(cineElegido.getHorario());


          }

          @Override
          public void onNothingSelected(AdapterView<?> adapterView) {

          }
        });

        carteleraButton.setOnClickListener(onCarteleraButtonListener);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    public static void loadCinemas(){
        db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("Cines");
        cinemas = new ArrayList<>();
        System.out.println("Veamos");

        // Attach a listener to read the data at our posts reference
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int size = (int) dataSnapshot.getChildrenCount();
                System.out.println("sise" + size);
                int i = 0;

                cinemas_array = new String[size];
                //use the for loop here to step over each child and retrieve data
                for (DataSnapshot cineSnapshot : dataSnapshot.getChildren()){
                    Cine cine = cineSnapshot.getValue(Cine.class);
                    cinemas.add(cine);
                    cinemas_array[i] = cine.getNombre();
                    System.out.println("namess" + cinemas_array[i]);
                    i++;
//                    Log.i("Load Cinemas", cine.toString());
                    System.out.println("Cine " + cine.getNombre() + " dirección " +  cine.getDireccion());
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

        setAragonLocation();
        getNearByCinemas();

    }


    @Override
    public void onLocationChanged(Location location){
        myLocation = location;

        setAragonLocation();
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
//            getNearByCinemas();
//        }

    }

    /**
     * Method to genereate the Url to find the nearby cinemas
     * @return
     */
    private void  getNearByCinemas(){


        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?query=cinemas+in+Aragon&key=AIzaSyCiMzmMun-pBgyHtezTpxaUJ6TTbm0wsJM");
        String url = googleURL.toString();

        Log.d("MapsActivity", "url = " + url);
        Object transferData[] = new Object[2];
        transferData[0] = mMap;
        transferData[1] = url;

        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
        getNearbyPlaces.execute(transferData);

        Toast.makeText(this, "Searching cinemas", Toast.LENGTH_SHORT).show();
    }

    //TO IMPROVE
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

        mMap.moveCamera(CameraUpdateFactory.newLatLng(zgz));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(zgz, 10.0f));


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
