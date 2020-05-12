package com.example.cinesaragon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

public class SeleccionTicketActivity extends AppCompatActivity {

    //declare fields
    private String movieName, showTime;

    //Declare 2 spinner
    private Spinner spinner1,spinner2;

    //Store ticket quantity
    private String _adultQuan, _childrenQuan;

    //Keys for Intent putExtra()
    public static final String TICKET_ADULT = "com.example.thang.assignment2_TICKET_ADULT";
    public static final String TICKET_CHILDREN = "com.example.thang.assignment2_TICKET_CHILDREN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_ticket);


        //Get intent and its content
        Intent intent = getIntent();
        movieName = intent.getStringExtra(MenuPrincipalActivity.PELICULA_NAME);
        showTime = intent.getStringExtra(ComprarEntradasActivity.SHOW_TIME);

        //Display movie name and show time in the TextView
        TextView textView = findViewById(R.id.textView3);
        textView.setText("Please, select ticket type and quantity");


        //Create Spinner and populate data
        spinner1 = findViewById(R.id.quantityAdult);
        spinner2 = findViewById(R.id.quantityChildren);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ticketQuantity, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner1.setAdapter(adapter);
        spinner1.setEnabled(false); //Disable the spinner after created
        spinner2.setAdapter(adapter);
        spinner2.setEnabled(false);//Disable spinner


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
                break;
            case R.id.children:
                if (checked)
                    spinner2.setEnabled(true);
                else
                    spinner2.setEnabled(false);
                break;

        }
    }

    //Handler for Check Out button
    public void onPagarButton(View view){

        //Get selected value (quantity) from spinners
        if (spinner1.isEnabled()){
            _adultQuan= spinner1.getSelectedItem().toString();
        }
        if (spinner2.isEnabled()){
            _childrenQuan = spinner2.getSelectedItem().toString();
        }

        //create intent to pass data
        Intent intent=new Intent(this, RandomActivity.class);
        intent.putExtra(MenuPrincipalActivity.PELICULA_NAME, movieName);
        intent.putExtra(ComprarEntradasActivity.SHOW_TIME, showTime);
        if (_adultQuan!=null)
            intent.putExtra(SeleccionTicketActivity.TICKET_ADULT, _adultQuan);
        else
            intent.putExtra(SeleccionTicketActivity.TICKET_ADULT, "0");
        if (_childrenQuan!=null){
            intent.putExtra(SeleccionTicketActivity.TICKET_CHILDREN, _childrenQuan);
        }
        else
            intent.putExtra(SeleccionTicketActivity.TICKET_CHILDREN, "0");
        //open next activity
        startActivity(intent);
    }
}
