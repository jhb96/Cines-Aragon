package com.example.cinesaragon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cinesaragon.model.Perfil;
import com.example.cinesaragon.model.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;

public class PerfilActivity extends AppCompatActivity {


    public static String email;
    public static String nombre;
    public static String apellidos;

    private Button saveButton;
    private EditText nombreText, apellidosText, emailText;

    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        nombreText = findViewById(R.id.user_name_perfil);
        apellidosText = findViewById(R.id.user_surname_perfil);
        emailText = findViewById(R.id.user_email_perfil);

        email = currentUser.getEmail();

        emailText.setText(email);

        loadDataFb();

        saveButton = findViewById(R.id.perfil_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInFB();
                finish();
            }
        });
        }


    private void saveInFB(){

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("Datos de usuario").child("Perfil");

        String nombre = nombreText.getText().toString();
        String apellidos = apellidosText.getText().toString();

        Perfil perfil = new Perfil(email, nombre, apellidos);
        dbRef.child(currentUser.getUid()).setValue(perfil);

    }

    private void loadDataFb(){
        //TODO
        //TO GET THE NAME AND SURNAME IF EXISTS
    }


}
