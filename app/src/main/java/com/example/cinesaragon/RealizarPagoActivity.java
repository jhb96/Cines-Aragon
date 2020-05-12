package com.example.cinesaragon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cinesaragon.model.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RealizarPagoActivity extends AppCompatActivity {


    private TextView adultTickets, childrenTickets, youngTickets, totalCost;
    private EditText name, surname, email;
    private Button pay_button;

    public static final double ADULT_COST = 5;
    public static final double CHILDREN_COST = 3.5;
    public static final double YOUNG_COST = 4;

    private int adult, children, young;
    private double cost;
    private String uId, nombre_completo, movieName, time;


    private List<Ticket> tickets;

    private FirebaseDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_pago);


        adultTickets = findViewById(R.id.adultTickets);
        childrenTickets = findViewById(R.id.childrenTicket);
        youngTickets = findViewById(R.id.youngTicket);
        totalCost = findViewById(R.id.totalCost);
        name = findViewById(R.id.edit_text_name);
        surname = findViewById(R.id.edit_text_surname);
        email = findViewById(R.id.edit_text_email);
        pay_button = findViewById(R.id.pay_button);


        summary();

        pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre_completo = name.getText().toString() + " " + surname.getText().toString();
                String pay_email = email.getText().toString();

                ticketCreate();
                saveTicketOnFirebase();
                tickets.clear();

                Toast.makeText(getApplicationContext(),"Accede a tus entradas para ver tu entrada",Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), MenuPrincipalActivity.class);
                startActivity(i);

            }
        });
    }

    private void summary(){

        Intent i = getIntent();
        time = i.getStringExtra("timeChoosen");
        adult =  i.getIntExtra("adultTickets", 0);
        children = i.getIntExtra("childrenTickets", 0);
        young = i.getIntExtra("youngTickets", 0);
        cost = ADULT_COST * adult + CHILDREN_COST*children + YOUNG_COST * young;
        movieName = i.getStringExtra("movieName");

        String adultTicketString;
        String childrenTicketString;
        String youngTicketString;

        if(adult == 0) {
            adultTicketString = adult + " entrada de adulto.";
        }else{
            adultTicketString = adult +" entradas de adulto.";

        }

        if(children == 0) {
            childrenTicketString = children + " entrada de niño.";
        }else{
            childrenTicketString = children +" entradas de niño.";

        }

        if(young == 0) {
            youngTicketString = young + " entrada con carnét joven.";
        }else{
            youngTicketString = young +" entradas con carnét joven.";

        }

        String finalPrice = "El precio total es: " +  cost + "€";

        adultTickets.setText(adultTicketString);
        childrenTickets.setText(childrenTicketString);
        youngTickets.setText(youngTicketString);
        totalCost.setText(finalPrice);

    }

    private void ticketCreate(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String time_format = format.format(currentTime) +" " + time;

        tickets = new ArrayList<>();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = currentUser.getUid();

        while(adult>0){
            adult--;
            Ticket t = new Ticket(uId, nombre_completo, movieName, "adulto", ADULT_COST, time_format);
            tickets.add(t);
        }

        while(children>0){
            children--;
            Ticket t = new Ticket(uId,nombre_completo, movieName, "niño", CHILDREN_COST, time_format);
            tickets.add(t);
        }

        while(young>0){
            young--;
            Ticket t = new Ticket(uId,nombre_completo, movieName, "joven", YOUNG_COST, time_format);
            tickets.add(t);
        }
    }

    private void saveTicketOnFirebase(){
        db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("Datos de usuario").child("Entradas");

            for(Ticket t: tickets){
                dbRef.push().setValue(t);
                System.out.println("Por cada entrada" + t.getFecha());
            }
    }
}
