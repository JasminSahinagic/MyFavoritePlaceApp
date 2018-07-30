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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignUp;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPass;
    private RadioButton radioButtonFemale;
    private RadioButton radioButtonMale;
    private ImageButton imageButtonProfile;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private Uri profileImage;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_accout);
        createAccountSetUp();
        imageButtonProfile.setOnClickListener(this);
        buttonSignUp.setOnClickListener(this);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };

    }

    public void createAccountSetUp(){
        buttonSignUp = (Button) findViewById(R.id.buttonCASignUp);
        editTextFirstName = (EditText) findViewById(R.id.editTextCAFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextCALastName);
        editTextEmail = (EditText) findViewById(R.id.editTextCAEmail);
        editTextPass = (EditText) findViewById(R.id.editTextCAPass);
        radioButtonFemale = (RadioButton) findViewById(R.id.radioButtonCAFemale);
        radioButtonMale = (RadioButton) findViewById(R.id.radioButtonCAMale);
        radioButtonFemale.setChecked(true);
        imageButtonProfile = (ImageButton) findViewById(R.id.imageButtonProfile) ;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonCASignUp:
                createUserAccount();
                break;
            case R.id.imageButtonProfile:
                userProfileImage();
                break;
        }
    }

    private void userProfileImage() {
                 CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("My Crop")
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle("Done")
                .setRequestedSize(400, 400)
                .setCropMenuCropButtonIcon(R.drawable.donewhite).start(this);
                 imageButtonProfile.setBackgroundColor(Color.WHITE);
    }

    private void createUserAccount() {
        final String name = editTextFirstName.getText().toString().trim();
        final String lastName = editTextLastName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String pass = editTextPass.getText().toString().trim();
        if(radioButtonFemale.isChecked()) gender="Female";
        gender="Male";
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(gender) && profileImage != null){
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
            auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final StorageReference filepath = storageReference.child("User_profile_images").child(profileImage.getLastPathSegment());
                                filepath.putFile(profileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Date c = Calendar.getInstance().getTime();
                                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                                String formattedDate = df.format(c);
                                                Map<String, String> dataToSave = new HashMap<>();
                                                dataToSave.put("firstName", name);
                                                dataToSave.put("lastName", lastName);
                                                dataToSave.put("image", uri.toString());
                                                dataToSave.put("email", email);
                                                dataToSave.put("password", pass);
                                                dataToSave.put("gender", gender);
                                                dataToSave.put("userID", user.getUid().toString());
                                                dataToSave.put("date", formattedDate);
                                                progressDialog.dismiss();
                                                String userId = user.getUid();
                                                databaseReference.child("Users").child(userId).setValue(dataToSave);
                                                startActivity(new Intent(CreateAccount.this, MyFavoritePlace.class));
                                                finish();
                                                }


                                        });
                                    }}); }
                                    else {
                                Toast.makeText(CreateAccount.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileImage = result.getUri();
                imageButtonProfile.setImageURI(profileImage);
                Toast.makeText(
                        this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG)
                        .show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
