package com.example.cinesaragon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SeleccionAsientoActivity extends AppCompatActivity {


    //Class variables
    String _movieName, _showTime, _adultQuan, _childrenQuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_asiento);


        //get intent and content
        Intent intent=getIntent();
        _movieName = intent.getStringExtra(MenuPrincipalActivity.PELICULA_NAME);
        _showTime = intent.getStringExtra(ComprarEntradasActivity.SHOW_TIME);
        _adultQuan = intent.getStringExtra(SeleccionTicketActivity.TICKET_ADULT);
        _childrenQuan = intent.getStringExtra(SeleccionTicketActivity.TICKET_CHILDREN);

        //Get the TextViews from layout


    }

}
