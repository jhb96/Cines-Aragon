package com.example.cinesaragon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SeleccionAsientoActivity extends AppCompatActivity {


    //Class variables
    String _movieName, _showTime, _adultQuan, _childrenQuan;
    private String timeChoosen;
    private int  ticketAdult, ticketChildren, ticketYoung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_asiento);


        Intent i = getIntent();

        timeChoosen = i.getStringExtra("timeChoosen");
        ticketAdult = i.getIntExtra("adultTicket", 0);
        ticketChildren = i.getIntExtra("childrenTicket", 0);
        ticketYoung = i.getIntExtra("youngTicket", 0);





        //Get the TextViews from layout




    }

}
