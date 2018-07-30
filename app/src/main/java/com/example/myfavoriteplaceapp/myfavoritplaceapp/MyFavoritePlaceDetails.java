package com.example.myfavoriteplaceapp.myfavoritplaceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfavoriteplaceapp.myfavoritplaceapp.Model.LocationPost;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class MyFavoritePlaceDetails extends AppCompatActivity implements OnMapReadyCallback {

    private ImageView imageViewDImage;
    private TextView textViewAddress;
    private TextView textViewAttributes;
    private TextView textViewLaLng;
    private TextView textViewLocale;
    private TextView textViewName;
    private TextView textViewPhoneNum;
    private TextView textViewPlaceType;
    private TextView textViewPriceLevel;
    private TextView textViewRating;
    private GoogleMap map;
    private FirebaseAuth auth;
    private TextView textViewInput;
    private TextView textViewDate;
    private LocationPost post;
    private double latitude=0.0;
    private double longitude=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite_place_details);
        detailsSetUp();
        userScreen();
        convertToLatLng();
    }

    public void convertToLatLng(){
        String[] latlong =  post.getLatLng().split(",");
        String[] temp = latlong[0].split("\\(");
        String[] secTemp = latlong[1].split("\\)");
        latitude = Double.parseDouble(temp[1]);
        longitude=Double.parseDouble(secTemp[0]);
    }

    public void detailsSetUp(){
        post = (LocationPost) getIntent().getSerializableExtra("Value");
        imageViewDImage = (ImageView) findViewById(R.id.imageViewDImage);
        textViewAddress = (TextView) findViewById(R.id.textViewDAddress);
        textViewAttributes = (TextView) findViewById(R.id.textViewDAttributes);
        textViewLaLng = (TextView) findViewById(R.id.textViewDLaLng);
        textViewLocale = (TextView) findViewById(R.id.textViewDLocale);
        textViewName = (TextView) findViewById(R.id.textViewDName);
        textViewPhoneNum = (TextView) findViewById(R.id.textViewDPhoneNum);
        textViewPlaceType = (TextView) findViewById(R.id.textViewDPlaceType);
        textViewPriceLevel = (TextView) findViewById(R.id.textViewDPriceLvl);
        textViewRating = (TextView) findViewById(R.id.textViewDRating);
        textViewInput = (TextView) findViewById(R.id.textViewDInput);
        textViewDate = (TextView) findViewById(R.id.textViewDDate);
        auth = FirebaseAuth.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapDetails);
        mapFragment.getMapAsync(this);
    }

    public void userScreen(){
        Picasso.with(this).load(post.getImage()).into(imageViewDImage);
        textViewAddress.setText(post.getAddress());
        textViewAttributes.setText("Attributes: "+post.getAttributes());
        textViewLocale.setText("Locale: "+post.getLocale());
        textViewName.setText("Name: "+post.getName());
        textViewPhoneNum.setText("Name"+post.getPhoneNumber());
        textViewPlaceType.setText("Place Type: "+post.getPlaceType());
        textViewPriceLevel.setText("Price level: "+post.getPriceLevel());
        textViewRating.setText("Rating: "+post.getRating());
        textViewInput.setText(post.getUserInput());
        textViewDate.setText(post.getPostDate());
        textViewLaLng.setText("LaLng: "+post.getLatLng());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(latitude,longitude));
        markerOptions.title(post.getAddress());
        map.clear();
        map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude)));
        float zoomLevel = (float) 14.0;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), zoomLevel));
        map.addMarker(markerOptions);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionSignOut:
                auth.getInstance().signOut();
                startActivity(new Intent(MyFavoritePlaceDetails.this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
