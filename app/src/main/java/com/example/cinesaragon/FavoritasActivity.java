package com.example.cinesaragon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.cinesaragon.Adapter.movieListAdapterMainPage;
import com.example.cinesaragon.model.movieObj;
import com.example.cinesaragon.model.movieToSave;
import com.example.cinesaragon.requestOperators.customListMoviesRequestOperator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritasActivity extends AppCompatActivity implements customListMoviesRequestOperator.RequestOperatorListener {

    ListView favoritesLv;
    public static List<movieObj> jsonMoviesFavs = new ArrayList<>();
    public static boolean clickedFromFavs;
    private movieListAdapterMainPage favoritesAdapter;

    private List<movieObj> publications=new ArrayList<>();

    private movieToSave favoriteMovies=new movieToSave();

    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbFavorites;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritas);


        setTitle("Favoritas");

        jsonMoviesFavs.clear();

        favoritesLv=  findViewById(R.id.favListView);

        firebaseAuth= FirebaseAuth.getInstance();
        dbFavorites= FirebaseDatabase.getInstance().getReference("Datos de usuario").child("Favoritas");


        favoritesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToMovieDetails(jsonMoviesFavs.get(i).getId());
            }
        });

        favoritesAdapter = new movieListAdapterMainPage(this, jsonMoviesFavs);
        favoritesLv.setAdapter(favoritesAdapter);


    }


    @Override
    protected void onResume() {

        jsonMoviesFavs.clear();
        favoritesLv.invalidateViews();

        dbFavorites.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot usersSnapShot : dataSnapshot.getChildren()){
                    if(usersSnapShot.getValue(movieToSave.class).getUser_id().equals(firebaseAuth.getCurrentUser().getUid()) && usersSnapShot.hasChild("movie_id")){
                        favoriteMovies = usersSnapShot.getValue(movieToSave.class);
                        sendRequest(favoriteMovies.getMovie_id());
                    }
                    else{
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        super.onResume();
    }

    private void sendRequest(List<Integer> movieIds){
        customListMoviesRequestOperator.movieIds=movieIds;
        customListMoviesRequestOperator ro= new customListMoviesRequestOperator();
        ro.setListener(this);
        ro.start();

    }




    private void goToMovieDetails(int movieId){
        clickedFromFavs=true;
        CarteleraActivity.clickedMovie=movieId;
        Intent newAct = new Intent(this, InfoPeliActivity.class);
        startActivity(newAct);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void updatePublication(){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                if(!publications.isEmpty() && publications!=null){
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    for(int i=0;i<publications.size();i++){
                        jsonMoviesFavs.add(new movieObj(publications.get(i).getId(), publications.get(i).getTitle(), publications.get(i).getRelease_date(), publications.get(i).getPoster_path(),publications.get(i).getBackdrop_path(), publications.get(i).getOverview(), publications.get(i).getVote_average()));
                    }
                    favoritesLv.invalidateViews();
                }
            }
        });

    }


    @Override
    public void success(List<movieObj> publications) {
        this.publications=publications;
        updatePublication();
    }

    @Override
    public void failed(int responseCode) {
        this.publications=publications;
        updatePublication();
    }
}
