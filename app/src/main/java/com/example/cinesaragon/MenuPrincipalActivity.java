package com.example.cinesaragon;

import android.content.Intent;
import android.os.Bundle;

import com.example.cinesaragon.model.Cine;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;



import java.util.ArrayList;

public class MenuPrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView unloginedUser;
    private DrawerLayout drawer;
    public static final String PELICULA_NAME ="com.example.thang.assignment2_ticketbuy_SHOWTIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        setToolbar();



        if (navigationView != null) {
            // Añadir carácteristicas
        }
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        unloginedUser = (TextView) header.findViewById(R.id.unloginedUserEmail);
        unloginedUser.setText("Unknown");

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        ArrayList<Cine> cines= new ArrayList <>();

        //Pruebas
        Cine cine1 = new Cine("Cine de niños", "cine de niños");
        Cine cine2 = new Cine("Cine de adultos", "cine de adultos");
        cines.add(cine1);
        cines.add(cine2);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Cine> adapter = new ArrayAdapter<Cine>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, cines);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.activity_menu_principal_drawer, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void onComprarEntradasClick(View v){
        Intent i = new Intent(this, ComprarEntradasActivity.class);
        startActivity(i);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_perfil:
                Intent i = new Intent(this, MapsActivity.class);
                startActivity(i);

            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }



    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_launcher_background);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }
}
