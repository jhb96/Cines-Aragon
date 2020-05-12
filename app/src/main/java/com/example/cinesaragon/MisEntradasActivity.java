package com.example.cinesaragon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.cinesaragon.model.Ticket;

import java.util.List;

public class MisEntradasActivity extends AppCompatActivity {

    private List<Ticket> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_entradas);

        loadEntradas();

    }



    private void loadEntradas(){



    }






}
