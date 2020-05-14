package com.example.cinesaragon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ComprarEntradasActivity extends AppCompatActivity {

    private TextView textView,movieTitleTextView, directorView;
    private ImageView movieImageView;
    private Button continueButton;
    //Declare 2 spinner
    private Spinner spinner1,spinner2,spinner3;

    public static final String SHOW_TIME ="com.example.thang.assignment2_ticketbuy_SHOWTIME";
    private String movieName= "Ejemplo"; //store movie name to transfer to other activity
    private String imgPath,timeChoosen,cinema,director;

    private int  ticketAdult, ticketChildren, ticketYoung;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_entradas);

        this.setTitle("Compra de entrada");

        //get the Intent that started this activity and get the movie name string
       // Intent intent = getIntent();
        //movieName = intent.getStringExtra(MovieListActivity.MOVIE_NAME);

        textView = findViewById(R.id.textView2);
        movieImageView = findViewById(R.id.movieImage);
        movieTitleTextView = findViewById(R.id.movieDetailTitle);
        directorView = findViewById(R.id.movieDetailDirector);

        continueButton = findViewById(R.id.continueButton);
        spinner1 = findViewById(R.id.quantityAdult);
        spinner2 = findViewById(R.id.quantityChildren);
        spinner3 = findViewById(R.id.quantityJoven);

        Intent i = getIntent();
        movieName = i.getStringExtra("movieName");
        imgPath = i.getStringExtra("imageMoviePath");
        cinema = i.getStringExtra("cinema");
        director = i.getStringExtra("director");
        //Capture the TextView and display movie name

        Glide.with(getApplicationContext())
                .load(imgPath)
                .into(movieImageView);

        textView.setText("Por favor, escoja una hora: ");
        movieTitleTextView.setText(movieName);
        directorView.setText(director);




        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ticketQuantity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


//        continueButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                onComprarEntradasClick(view);
//            }
//        });


        spinner1.setAdapter(adapter);
        spinner1.setEnabled(false);
        spinner2.setAdapter(adapter);
        spinner2.setEnabled(false);
        spinner3.setAdapter(adapter);
        spinner3.setEnabled(false);
    }


    public void onSelectTimeClick(View view){

        String showTime=null;

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.showTime1:
                if (checked){
                    timeChoosen=getString(R.string.showtime1);
                    //openTicketTypeActivity(movieName, showTime);
                }
                break;
            case R.id.showTime2:
                if (checked){
                    timeChoosen=getString(R.string.showtime2);
                    // openTicketTypeActivity(movieName, showTime);
                }
                break;
            case R.id.showTime3:
                if (checked){
                    timeChoosen=getString(R.string.showtime3);
                    //openTicketTypeActivity(movieName, showTime);
                }
                break;
        }
    }

    //Handling RadioButton Click
    public void onComprarClick(View view){

        Intent i = new Intent(this, RealizarPagoActivity.class);
        if(spinner1.getSelectedItem() != null && spinner1.isEnabled()){
            ticketAdult = Integer.parseInt(spinner1.getSelectedItem().toString());
        }else{
            ticketAdult = 0;
        }

        if(spinner2.getSelectedItem() != null && spinner2.isEnabled()){
            ticketChildren = Integer.parseInt(spinner2.getSelectedItem().toString());
        }else{
            ticketChildren = 0;
        }

        if(spinner3.getSelectedItem() != null && spinner3.isEnabled()){
            ticketYoung = Integer.parseInt(spinner3.getSelectedItem().toString());
        }else{
            ticketYoung = 0;
        }


        i.putExtra("timeChoosen", timeChoosen);
        i.putExtra("adultTickets", ticketAdult);
        i.putExtra("childrenTickets", ticketChildren);
        i.putExtra("youngTickets", ticketYoung);
        i.putExtra("movieName", movieName);
        i.putExtra("cinema",cinema);
        startActivity(i);

    }


    //Handler for check boxes
    public void onCheckboxSeleccionado(View view){
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.adult:
                if (checked)
                    spinner1.setEnabled(true); //enable spinner if check box is checked
                else
                    spinner1.setEnabled(false);
                    spinner1.resetPivot();
                break;
            case R.id.children:
                if (checked)
                    spinner2.setEnabled(true);
                else
                    spinner2.setEnabled(false);
                break;
            case R.id.carnetJoven:
                if (checked)
                    spinner3.setEnabled(true);
                else
                    spinner3.setEnabled(false);
                break;
        }
    }



}
