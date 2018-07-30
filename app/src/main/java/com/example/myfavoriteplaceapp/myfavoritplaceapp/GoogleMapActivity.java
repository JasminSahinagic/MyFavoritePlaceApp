package com.example.myfavoriteplaceapp.myfavoritplaceapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myfavoriteplaceapp.myfavoritplaceapp.Model.LocationPost;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private PlaceAutocompleteFragment placeAutoComplete;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button buttonLocationPost;
    private LocationPost locationPost;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        locationPost = new LocationPost();
        buttonLocationPost = (Button) findViewById(R.id.buttonLocationPost);
        buttonLocationPost.setEnabled(false);
        buttonLocationPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoogleMapActivity.this, MyFavoritePlacePost.class);
                intent.putExtra("FavoriteLocation",locationPost);
                startActivity(intent);
            }
        });
        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    addMarker(place);
                    Log.d("Maps", "Place selected: " + place.getName());
                    Toast.makeText(GoogleMapActivity.this, place.getAddress().toString(), Toast.LENGTH_SHORT).show();
                    addPlace(place);
                    buttonLocationPost.setEnabled(true);
                }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });
        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
    }

    public void addPlace(Place place){
        if(place.getAddress() !=null)locationPost.setAddress(place.getAddress().toString());
        else locationPost.setAddress("Adress: N/A");
        if(place.getAttributions() !=null)locationPost.setAttributes(place.getAttributions().toString());
        else locationPost.setAttributes("Attributions: N/A");
        if(place.getId() != null) locationPost.setId(place.getId().toString());
        else locationPost.setId("ID: N/A");
        if(place.getLatLng() != null ) locationPost.setLatLng(place.getLatLng().toString());
        else locationPost.setLatLng("LatLng: N/A");
        if(place.getLocale() != null )locationPost.setLocale(place.getLocale().toString());
        else locationPost.setLocale("Locale: N/A");
        if(place.getName() != null)locationPost.setName(place.getName().toString());
        else locationPost.setName("Name: N/A");
        if(place.getPhoneNumber() != null) locationPost.setPhoneNumber(place.getPhoneNumber().toString());
        else locationPost.setPhoneNumber("PhoneNumber: N/A");
        if(place.getPlaceTypes() != null )locationPost.setPlaceType(place.getPlaceTypes().toString());
        else locationPost.setPlaceType("PlaceType: N/A");
        if(place.getPriceLevel() != 0 )locationPost.setPriceLevel(String.valueOf(place.getPriceLevel()));
        else locationPost.setPriceLevel(String.valueOf("Price lvl: N/A"));
        if(place.getRating() != 0)locationPost.setRating(String.valueOf(place.getRating()));
        else locationPost.setRating("Rating: N/A");
        locationPost.setImage("");
        locationPost.setUserID("");
        locationPost.setPostDate("");
        locationPost.setUserInput("");
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)==
                    PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Log", location.toString());
                mMap.clear();
                LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(newLocation).title("User Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addressList = geocoder.getFromLocation(location.getLongitude(),location.getLatitude(),1);
                    if(addressList != null && addressList.size() > 0){
                        Log.d("Address:",addressList.get(0).toString());
                    }else{
                        Log.d("Address:","Address: N/A");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


        if(Build.VERSION.SDK_INT < 23){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }else{
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                return;
            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }}


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(markerOptions);
            }


        });
    }

    public void addMarker(Place p){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(p.getLatLng());
        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.add_btn));
        markerOptions.title(p.getName()+"");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(p.getLatLng()));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }
}
