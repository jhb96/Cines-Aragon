package com.example.cinesaragon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ComprarEntradasActivity extends AppCompatActivity {


    public static final String SHOW_TIME ="com.example.thang.assignment2_ticketbuy_SHOWTIME";
    String _movieName= "Ejemplo"; //store movie name to transfer to other activity

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_entradas);


        //get the Intent that started this activity and get the movie name string
       // Intent intent = getIntent();
        //_movieName = intent.getStringExtra(MovieListActivity.MOVIE_NAME);

        //Capture the TextView and display movie name
        TextView textView=(TextView)findViewById(R.id.textView2);
        textView.setText("You select " + _movieName + ".\nPlease select show time as below.");

    }


    //Handling RadioButton Click
    public void onComprarEntradasClick(View view){

        String showTime=null;

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.showTime1:
                if (checked){
                    showTime=getString(R.string.showtime1);
                    openTicketTypeActivity(_movieName, showTime);
                }
                break;
            case R.id.showTime2:
                if (checked){
                    showTime=getString(R.string.showtime2);
                    openTicketTypeActivity(_movieName, showTime);
                }
                break;
            case R.id.showTime3:
                if (checked){
                    showTime=getString(R.string.showtime3);
                    openTicketTypeActivity(_movieName, showTime);
                }
                break;
            case R.id.showTime4:
                if (checked){
                    showTime=getString(R.string.showtime4);
                    openTicketTypeActivity(_movieName, showTime);
                }
                break;
            case R.id.showTime5:
                if (checked){
                    showTime=getString(R.string.showtime5);
                    openTicketTypeActivity(_movieName, showTime);
                }
                break;
        }
    }

    //Open Ticket type Activity
    public void openTicketTypeActivity(String movieName, String showTime){
        Intent intent=new Intent(this, SeleccionTicketActivity.class);
        intent.putExtra(MenuPrincipalActivity.PELICULA_NAME, _movieName);
        intent.putExtra(ComprarEntradasActivity.SHOW_TIME, showTime);
        startActivity(intent);
    }




}
