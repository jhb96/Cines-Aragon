package com.example.cinesaragon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        FirebaseApp.initializeApp(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            FirebaseAuth.getInstance().signOut();
            //startActivity(new Intent(getApplicationContext(), MenuPrincipalActivity.class));
            //finish();
        }

        Button loginButton = findViewById(R.id.signInWelcome);
        Button noLoginButton = findViewById(R.id.accessNoLogin);
        Button registerButton = findViewById(R.id.signUpWelcome);


        // NO LOGIN
        View.OnClickListener nologinButtonListener = new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MenuPrincipalActivity.class);
                startActivity(i);
            }
        };

        // LOGIN
        View.OnClickListener loginButtonListener = new View.OnClickListener() {

            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        };

        // REGISTER
        View.OnClickListener regiseterButtonListener = new View.OnClickListener() {

            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        };


        loginButton.setOnClickListener(loginButtonListener);
        noLoginButton.setOnClickListener(nologinButtonListener);
        registerButton.setOnClickListener(regiseterButtonListener);

    }

}
