package com.example.cinesaragon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cinesaragon.model.Perfil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


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

      db = FirebaseDatabase.getInstance();
      dbRef = db.getReference("Datos de usuario").child("Perfil");
      Query q = dbRef.equalTo(currentUser.getUid());
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Perfil p = dataSnapshot.child(currentUser.getUid()).getValue(Perfil.class);
                System.out.println(p);
                try{
                    nombreText.setText(p.getNombre());
                    apellidosText.setText(p.getApellidos());
                } catch (Exception e){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });


        //TODO
        //TO GET THE NAME AND SURNAME IF EXISTS
    }


}
