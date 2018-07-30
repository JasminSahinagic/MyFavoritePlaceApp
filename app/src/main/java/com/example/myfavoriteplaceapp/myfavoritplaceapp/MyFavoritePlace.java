package com.example.myfavoriteplaceapp.myfavoritplaceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.myfavoriteplaceapp.myfavoritplaceapp.Data.MyFavoritePlaceAdapter;
import com.example.myfavoriteplaceapp.myfavoritplaceapp.Model.LocationPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class MyFavoritePlace extends AppCompatActivity implements View.OnClickListener{


    private RecyclerView recyclerView;
    private  RecyclerView.Adapter adapter;
    private Button buttonLogOut;
    private Button buttonProfile;
    private Button buttonMap;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private List<LocationPost> postList;
    private FirebaseUser user;
    private List<LocationPost> tempList;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite_place);
        myFavoritePlaceSetUp();
        buttonMap.setOnClickListener(this);
        buttonLogOut.setOnClickListener(this);
        buttonProfile.setOnClickListener(this);
    }

    public void myFavoritePlaceSetUp(){
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("FavoriteLocations");
        postList = new ArrayList<>();
        tempList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMyFavoritePlace);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        buttonMap = (Button) findViewById(R.id.buttonMap);
        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);
        buttonProfile = (Button) findViewById(R.id.buttonProfile);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                LocationPost post = dataSnapshot.getValue(LocationPost.class);
                if (post.getUserID().equals(user.getUid())) {
                    postList.add(post);
                }
                HashSet<LocationPost> uniqueValues = new HashSet<>(postList);
                for (LocationPost value : uniqueValues) {
                    tempList.add(value);
                }
                Collections.reverse(tempList);
                adapter = new MyFavoritePlaceAdapter(MyFavoritePlace.this, tempList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                postList.clear();
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
                mAuth.getInstance().signOut();
                startActivity(new Intent(MyFavoritePlace.this, MainActivity.class));
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonMap:
                startActivity(new Intent(MyFavoritePlace.this,GoogleMapActivity.class));
                break;
            case R.id.buttonProfile:
                startActivity(new Intent(MyFavoritePlace.this,UserProfile.class));
                break;
            case R.id.buttonLogOut:
                mAuth.getInstance().signOut();
                startActivity(new Intent(MyFavoritePlace.this, MainActivity.class));
                finish();
                break;
        }
    }
}
