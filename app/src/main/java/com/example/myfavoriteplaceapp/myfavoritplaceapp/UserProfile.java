package com.example.myfavoriteplaceapp.myfavoritplaceapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.LocaleList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfavoriteplaceapp.myfavoritplaceapp.Data.MyFavoritePlaceAdapter;
import com.example.myfavoriteplaceapp.myfavoritplaceapp.Model.LocationPost;
import com.example.myfavoriteplaceapp.myfavoritplaceapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class UserProfile extends AppCompatActivity {

    private ImageView imageView;
    private TextView textViewFirstName;
    private TextView textViewLastName;
    private TextView textViewEmail;
    private TextView textViewPlacesVisited;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference databaseReferencePost;
    private DatabaseReference databaseReferenceUser;
    private List<LocationPost> postList;
    private List<User> userList;
    private User mUser;
    private  LocationPost locationPost;
    int num=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        userProfileSetUp();
    }

    public void userProfileSetUp(){
        imageView = (ImageView) findViewById(R.id.imageViewUUserImage);
        textViewLastName = (TextView) findViewById(R.id.textViewULastName);
        textViewFirstName = (TextView) findViewById(R.id.textViewUUser);
        textViewEmail= (TextView) findViewById(R.id.textViewUEmail);
        textViewPlacesVisited = (TextView) findViewById(R.id.numPlacesVisited);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReferencePost = FirebaseDatabase.getInstance().getReference().child("FavoriteLocations");
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("Users");
        postList = new ArrayList<>();
        userList = new ArrayList<>();
        mUser = new User();
        locationPost = new LocationPost();
    }


    @Override
    protected void onStart() {
        super.onStart();
        databaseReferencePost.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LocationPost post  = dataSnapshot.getValue(LocationPost.class);
                if (post.getUserID().equals(user.getUid())) {
                    num++;
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReferenceUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                User temp  = dataSnapshot.getValue(User.class);
                if (temp.getUserID().equals(user.getUid())) {
                   textViewFirstName.setText("First Name: "+temp.getFirstName());
                    textViewLastName.setText("Last Name: "+temp.getLastName());
                    textViewEmail.setText("Your Email: "+temp.getEmail());
                    imageView.setBackgroundColor(Color.WHITE);
                    Picasso.with(UserProfile.this).load(temp.getImage()).into(imageView);
                    textViewPlacesVisited.setText("You visited "+String.valueOf(num)+" places");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionSignOut:
                auth.getInstance().signOut();
                startActivity(new Intent(UserProfile.this, MainActivity.class));
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
