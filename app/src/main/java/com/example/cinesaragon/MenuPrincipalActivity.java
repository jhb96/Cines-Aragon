package com.example.cinesaragon;

import android.content.Intent;
import android.os.Bundle;

import com.example.cinesaragon.model.Cine;
import com.example.cinesaragon.model.Pelicula;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

    private TextView unloginedUser;
    private DrawerLayout drawer;
    public static final String PELICULA_NAME ="com.example.thang.assignment2_ticketbuy_SHOWTIME";
    public static List<Pelicula> jsonMovies = new ArrayList<>();

    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setToolbar();

        firebaseAuth=FirebaseAuth.getInstance();


        if (navigationView != null) {
            // Añadir carácteristicas
        }

        cargarPeliculas();

        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        unloginedUser = (TextView) header.findViewById(R.id.unloginedUserEmail);
        unloginedUser.setText("Unknown");


        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();
        Intent i = new Intent();

        switch(id){
            case R.id.nav_perfil:
                break;

            case R.id.nav_misentradas:
                break;

            case R.id.nav_favoritas:
                break;

            case R.id.nav_logout:
                firebaseAuth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
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
