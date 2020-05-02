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
    String _movieName=null, _showTime=null;

    //Declare 2 spinner
    Spinner _spinner1=null;
    Spinner _spinner2=null;

    //Store ticket quantity
    String _adultQuan, _childrenQuan;

    //Keys for Intent putExtra()
    public static final String TICKET_ADULT = "com.example.thang.assignment2_TICKET_ADULT";
    public static final String TICKET_CHILDREN = "com.example.thang.assignment2_TICKET_CHILDREN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_ticket);


        //Get intent and its content
        Intent intent=getIntent();
        _movieName = intent.getStringExtra(MenuPrincipalActivity.PELICULA_NAME);
        _showTime = intent.getStringExtra(ComprarEntradasActivity.SHOW_TIME);

        //Display movie name and show time in the TextView
        TextView textView=(TextView)findViewById(R.id.textView3);
        textView.setText("You select "
                + _movieName
                + " and show time at "
                + _showTime
                +".\nPlease select ticket types and quantity.");


        //Create Spinner and populate data
        _spinner1 = (Spinner) findViewById(R.id.quantityAdult);
        _spinner2 = (Spinner) findViewById(R.id.quantityChildren);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ticketQuantity, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        _spinner1.setAdapter(adapter);
        _spinner1.setEnabled(false); //Disable the spinner after created
        _spinner2.setAdapter(adapter);
        _spinner2.setEnabled(false);//Disable spinner


    }

    //Handler for check boxes
    public void onCheckboxSeleccionado(View view){
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.adult:
                if (checked)
                    _spinner1.setEnabled(true); //enable spinner if check box is checked
                else
                    _spinner1.setEnabled(false);
                break;
            case R.id.children:
                if (checked)
                    _spinner2.setEnabled(true);
                else
                    _spinner2.setEnabled(false);
                break;

        }
    }

    //Handler for Check Out button
    public void onPagarButton(View view){

        //Get selected value (quantity) from spinners
        if (_spinner1.isEnabled()){
            _adultQuan= _spinner1.getSelectedItem().toString();
        }
        if (_spinner2.isEnabled()){
            _childrenQuan = _spinner2.getSelectedItem().toString();
        }

        //create intent to pass data
        Intent intent=new Intent(this, SeleccionAsientoActivity.class);
        intent.putExtra(MenuPrincipalActivity.PELICULA_NAME, _movieName);
        intent.putExtra(ComprarEntradasActivity.SHOW_TIME, _showTime);
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
