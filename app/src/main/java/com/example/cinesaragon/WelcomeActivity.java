package com.example.cinesaragon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;



import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;




public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        FirebaseApp.initializeApp(this);
        MapsActivity.loadCinemas();

        // Initialize Firebase Auth
//        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Write a message to the database
        if (currentUser != null) {
            try{
               // MisEntradasActivity.loadEntradas();
            }catch (Exception e){
                System.out.println("No tiene entradas");
            }
            startActivity(new Intent(getApplicationContext(), MenuPrincipalActivity.class));
            finish();
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
        View.OnClickListener registerButtonListener = new View.OnClickListener() {

            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        };


        loginButton.setOnClickListener(loginButtonListener);
        noLoginButton.setOnClickListener(nologinButtonListener);
        registerButton.setOnClickListener(registerButtonListener);

    }

}
