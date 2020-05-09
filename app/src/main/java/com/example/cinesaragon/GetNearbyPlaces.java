package com.example.cinesaragon;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.cinesaragon.model.Cine;
import com.example.cinesaragon.model.Pelicula;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlaces extends AsyncTask<Object, String , String> {



    private String googlePlaceData;
    private GoogleMap mMap;
    String url;


    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();

        try{
            googlePlaceData = downloadUrl.ReadTheURL(url);

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return  googlePlaceData;
    }

    @Override
    protected void onPostExecute(String s) {
       // super.onPostExecute(s);

        try {
            JSONObject parentObject = new JSONObject(s);
            JSONArray resultArray = parentObject.getJSONArray("results");

            // Initializate bbdd

            DatabaseReference db = FirebaseDatabase.getInstance().getReference();

            // Recorro cada cine y creo una marca y los guardo en la bbdd
            for(int i=0; i<resultArray.length(); i++){
                JSONObject jsonObject = resultArray.getJSONObject(i);
                JSONObject locationObj = jsonObject.getJSONObject("geometry").getJSONObject("location");

                String latitude = locationObj.getString("lat");
                String longitude = locationObj.getString("lng");

                JSONObject nameObject = resultArray.getJSONObject(i);

                String name = nameObject.getString("name");
                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                String address = jsonObject.getString("formatted_address");


                // Write cinema in the Firebase
                Cine cine = new Cine(name,address,"", "", latLng.latitude, latLng.longitude);
                cine.addPelicula(new Pelicula("Peli1"));
                cine.addPelicula(new Pelicula("Peli2"));
                cine.addPelicula(new Pelicula("Peli3"));


                db.child("Cines").child(cine.getNombre()).setValue(cine);


                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(name);
                markerOptions.position(latLng);

                mMap.addMarker(markerOptions);

            }

        } catch (JSONException e ){
            e.printStackTrace();
        }

        List<HashMap<String, String>> nearbyplaceslist = null;
        DataParser dataParser = new DataParser();
        nearbyplaceslist = dataParser.parse(s);

        DisplayNearbyPlaces(nearbyplaceslist);

    }




    private void DisplayNearbyPlaces(List<HashMap<String, String>> nearbyplaceslist){
        for(int i=0; i < nearbyplaceslist.size(); i++){

            MarkerOptions markerOptions = new MarkerOptions();

            HashMap<String, String> googleNearbyPlace = nearbyplaceslist.get(i);
            String nameOfPlace = googleNearbyPlace.get("place_name");
            String vicinity = googleNearbyPlace.get("vicinity");
           //double rating = Double.parseDouble(googleNearbyPlace.get("rating"));
            double lat = Double.parseDouble( googleNearbyPlace.get("lat"));
            double lng = Double.parseDouble(googleNearbyPlace.get("lng"));

            Log.d("NearbyCinema", "placename = " + nameOfPlace + "vicinity =" + vicinity );

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(nameOfPlace + " : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }
}
