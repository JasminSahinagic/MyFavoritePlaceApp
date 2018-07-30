package com.example.myfavoriteplaceapp.myfavoritplaceapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfavoriteplaceapp.myfavoritplaceapp.Model.LocationPost;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyFavoritePlacePost extends AppCompatActivity implements View.OnClickListener {

    private Button buttonPost;
    private ImageView imageViewPost;
    private EditText editTextPostInput;
    private TextView textViewPostAddress;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri postImage;
    private ProgressDialog progressDialog;
    private LocationPost locationPost;
    private static final int GALLERY_CODE=1;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite_place_post);
        postSetUp();
        textViewPostAddress.setText(locationPost.getAddress().toString());
        buttonPost.setOnClickListener(this);
        imageViewPost.setOnClickListener(this);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }


    public void postSetUp(){
        locationPost = (LocationPost) getIntent().getSerializableExtra("FavoriteLocation");
        mAuth = FirebaseAuth.getInstance();
        mAuth= FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        buttonPost= (Button) findViewById(R.id.buttonPost);
        imageViewPost =(ImageView) findViewById(R.id.imageButtonBlogImage);
        editTextPostInput=(EditText) findViewById(R.id.editTextPInput);
        textViewPostAddress = (TextView) findViewById(R.id.blogAdres);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonPost:
                 createPost();
                break;
            case R.id.imageButtonBlogImage:
                imageViewPost.setBackgroundColor(Color.WHITE);
                chooseImage();
                    break;
        }
    }

    private void chooseImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_CODE);
    }

    private void createPost() {
        final String input = editTextPostInput.getText().toString().trim();
        progressDialog.setMessage("Posting...");
        progressDialog.show();
        if(!TextUtils.isEmpty(input) && postImage!=null){
            final StorageReference filepath = storageReference.child("User_location_images").child(postImage.getLastPathSegment());
            filepath.putFile(postImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                            String formattedDate = df.format(c);
                            String key = databaseReference.child("FavoriteLocations").push().getKey();
                            Map<String, String> dataToSave = new HashMap<>();
                            dataToSave.put("address",locationPost.getAddress());
                            dataToSave.put("attributes",locationPost.getAttributes());
                            dataToSave.put("id",locationPost.getId());
                            dataToSave.put("latLng",locationPost.getLatLng());
                            dataToSave.put("locale",locationPost.getLocale());
                            dataToSave.put("name",locationPost.getName());
                            dataToSave.put("phoneNumber",locationPost.getPhoneNumber());
                            dataToSave.put("placeType",locationPost.getPlaceType());
                            dataToSave.put("priceLevel",locationPost.getPriceLevel());
                            dataToSave.put("rating",locationPost.getRating());
                            dataToSave.put("image",uri.toString());
                            dataToSave.put("userID",user.getUid());
                            dataToSave.put("postDate",formattedDate);
                            dataToSave.put("userInput",input);
                            databaseReference.child("FavoriteLocations").child(user.getUid()+key).setValue(dataToSave);
                            progressDialog.dismiss();
                            startActivity(new Intent(MyFavoritePlacePost.this,MyFavoritePlace.class));
                            finish();
                        }
                    });
                }

            });
        }else {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == GALLERY_CODE && resultCode  == RESULT_OK){
            postImage = data.getData();
            imageViewPost.setImageURI(postImage);
        }
    }
}

