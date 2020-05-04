package com.example.cinesaragon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class WelcomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button loginButton = findViewById(R.id.signInWelcome);
        Button noLoginButton = findViewById(R.id.accessNoLogin);


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
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
            }
        };


        loginButton.setOnClickListener(loginButtonListener);
        noLoginButton.setOnClickListener(nologinButtonListener);


    }
}
