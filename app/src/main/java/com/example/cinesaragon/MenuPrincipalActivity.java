package com.example.cinesaragon;

import android.content.Intent;
import android.os.Bundle;

import com.example.cinesaragon.model.Pelicula;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;



import java.util.ArrayList;
import java.util.List;

public class MenuPrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView userEmail;
    private DrawerLayout drawer;
    private Button button;
    private NavigationView navigationView;

    public static final String PELICULA_NAME ="com.example.thang.assignment2_ticketbuy_SHOWTIME";
    public static List<Pelicula> jsonMovies = new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView =  findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        userEmail = header.findViewById(R.id.userEmail);

        setTitle("Menú Principal");

        setToolbar();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null) {
            userEmail.setText(currentUser.getEmail());
        } else {
            userEmail.setText("Usuario invitado");
            hideItems();

        }

        cargarPeliculas();

        navigationView.setNavigationItemSelectedListener(this);




        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    private void hideItems(){

        navigationView = findViewById(R.id.nav_view);
        Menu nav_menu = navigationView.getMenu();
        nav_menu.findItem(R.id.nav_perfil).setVisible(false);
        nav_menu.findItem(R.id.nav_favoritas).setVisible(false);
        nav_menu.findItem(R.id.nav_logout).setVisible(false);
        nav_menu.findItem(R.id.nav_misentradas).setVisible(false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();
        Intent i;

        switch(id){

            case R.id.nav_perfil:
                i = new Intent(this, PerfilActivity.class);
                startActivity(i);
                break;

            case R.id.nav_misentradas:
                i = new Intent(this, MisEntradasActivity.class);
                startActivity(i);
                break;

            case R.id.nav_favoritas:
                i = new Intent(this, FavoritasActivity.class);
                startActivity(i);
                break;


            case R.id.nav_logout:
                firebaseAuth.signOut();
                startActivity(new Intent(this, WelcomeActivity.class));
                finish();
                break;

        }
        return true;
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
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//
//            case R.id.search_button:
//                Intent myIntent=new Intent(this,searchMovie.class);
//                startActivity(myIntent);
//                break;

            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void cargarPeliculas(){




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
