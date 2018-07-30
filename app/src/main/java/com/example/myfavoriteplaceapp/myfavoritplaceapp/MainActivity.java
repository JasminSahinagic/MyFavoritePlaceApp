package com.example.myfavoriteplaceapp.myfavoritplaceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private Button buttonLogIn;
    private Button buttonSignUp;
    private EditText editTextEmail;
    private EditText editTextPass;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainSetUp();
        buttonSignUp.setOnClickListener(this);
        buttonLogIn.setOnClickListener(this);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Toast.makeText(MainActivity.this, "User connceted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "User not connceted", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public void mainSetUp(){
        buttonLogIn = (Button) findViewById(R.id.buttonMainLogIn);
        buttonSignUp = (Button) findViewById(R.id.buttonMainSignUp);
        editTextEmail = (EditText) findViewById(R.id.editTextMainEmail);
        editTextPass = (EditText) findViewById(R.id.editTextMainPass);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonMainLogIn:
                logIn();
                break;
            case R.id.buttonMainSignUp:
                startActivity(new Intent(MainActivity.this,CreateAccount.class));
                break;
        }
    }

    private void logIn() {
        String email = editTextEmail.getText().toString();
        String pass = editTextPass.getText().toString();
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)){
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "User logged in.",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this,MyFavoritePlace.class));
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
