package com.example.cinesaragon;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.cinesaragon.Adapter.movieListAdapterMainPage;
import com.example.cinesaragon.model.movieObj;
import com.example.cinesaragon.requestOperators.movieRequestOperator;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CarteleraActivity extends AppCompatActivity  implements movieRequestOperator.RequestOperatorListener {


    private static ListView moviesLv;
    private List<movieObj> publications = new ArrayList<>();

    public static int clickedMovie;
    public static List<movieObj> jsonMovies = new ArrayList<>();
    private int genre;

    private List<movieObj> nextPageJsonMovies = new ArrayList<>();

    private movieListAdapterMainPage soonAdapter;

    Context thiscontext;

    public static boolean sorted=false,loadingMore=false, filter=false;

    private int queryCurrentPage =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartelera);


        setToolbar();
        setTitle("Cartelera");
        thiscontext = this;

        Intent intent = getIntent();
        final String nameCine = intent.getStringExtra("cineElegido");


        filter=false;
        resetVars();

        moviesLv = findViewById(R.id.soonListView);

        soonAdapter = new movieListAdapterMainPage(thiscontext, jsonMovies);

        moviesLv.setAdapter(soonAdapter);

        moviesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedMovie = jsonMovies.get(i).getId();
                Intent intent = new Intent(getApplicationContext(), InfoPeliActivity.class);
                intent.putExtra("cinema", nameCine);
                startActivity(intent);

            }
        });

        moviesLv.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {



                int lastInScreen = firstVisibleItem + visibleItemCount;
                if((lastInScreen == totalItemCount) && !loadingMore){
                    loadingMore=true;
                    queryCurrentPage++;
                    if(filter){
                        sendFilterRequest(String.valueOf(queryCurrentPage));
                    } else{
                        sendRequest(String.valueOf(queryCurrentPage));
                    }
                }
            }
        });

    }

    private void resetVars() {
        jsonMovies.clear();
        nextPageJsonMovies.clear();
        publications.clear();
        queryCurrentPage = 0;
    }

    public void addListItemToAdapter(List<movieObj> list){
        jsonMovies.addAll(list);
        moviesLv.invalidateViews();
        soonAdapter.notifyDataSetChanged();
        nextPageJsonMovies.removeAll(nextPageJsonMovies);
        publications.removeAll(publications);
        loadingMore=false;
    }

    private void sendRequest(String page){
//        movieRequestOperator.urlToRequest="https://api.themoviedb.org/3/movie/upcoming?api_key=21f9b30c3b2a6a1e906d03a384132fda&sort_by=release_date.asc&language=es&page="+page+"&region=ES";
        movieRequestOperator.urlToRequest="https://api.themoviedb.org/3/movie/upcoming?api_key=21f9b30c3b2a6a1e906d03a384132fda&language=es-ES&page="+page;
//        movieRequestOperator.urlToRequest="https://api.themoviedb.org/3/movie/upcoming?api_key=21f9b30c3b2a6a1e906d03a384132fda&language=es-ES&page="+page+"&region=ES";

//        movieRequestOperator.urlToRequest="https://api.themoviedb.org/3/movie/now_playing?api_key=21f9b30c3b2a6a1e906d03a384132fda&sort_by=popularity.desc&page="+page;
        movieRequestOperator ro= new movieRequestOperator();
        ro.setListener(this);
        ro.start();
    }

    private void sendFilterRequest(String page){
//        movieRequestOperator.urlToRequest="https://api.themoviedb.org/3/genre/"+genre+"/movies?api_key=21f9b30c3b2a6a1e906d03a384132fda&sort_by=created_at.desc&page="+page;
        movieRequestOperator.urlToRequest="https://api.themoviedb.org/3/discover/movie?api_key=21f9b30c3b2a6a1e906d03a384132fda&language=en-ES&sort_by=popularity.desc&include_adult=false&include_video=false&page="+page+"&with_genres="+genre;
        movieRequestOperator ro= new movieRequestOperator();
        ro.setListener(this);
        ro.start();

    }

    private void setToolbar() {
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void updatePublication(){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                if(publications!=null && !publications.isEmpty()){
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    for(int i=0;i<publications.size();i++){

                        String[] releaseDateArray = publications.get(i).getRelease_date().split("-");

                        int year = Integer.parseInt(releaseDateArray[0]);
                        int month = Integer.parseInt(releaseDateArray[1]);
                        int day = Integer.parseInt(releaseDateArray[2]);

                        Date today = new Date();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(today);

                        Calendar cal2 = Calendar.getInstance();
                        cal2.set(year, month - 1, day, 0, 0);

                        long msDiff =cal2.getTimeInMillis()-  cal.getInstance().getTimeInMillis();
                        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

                        publications.get(i).setDayLeft((int)daysDiff);

                        if(daysDiff>1){
                            nextPageJsonMovies.add(new movieObj(publications.get(i).getId(), publications.get(i).getTitle(), publications.get(i).getRelease_date()+" ("+publications.get(i).getDayLeft()+" days left)", publications.get(i).getDayLeft() ,publications.get(i).getPoster_path(),publications.get(i).getBackdrop_path(), publications.get(i).getOverview(), publications.get(i).getVote_average()));

                        }
                        else if(daysDiff==0 && (cal2.get(Calendar.DAY_OF_MONTH)-cal.get(Calendar.DAY_OF_MONTH)==1)){
                            daysDiff=cal2.get(Calendar.DAY_OF_MONTH)-cal.get(Calendar.DAY_OF_MONTH);
                            nextPageJsonMovies.add(new movieObj(publications.get(i).getId(), publications.get(i).getTitle(), publications.get(i).getRelease_date()+" (Tomorrow)", (int)daysDiff, publications.get(i).getPoster_path(),publications.get(i).getBackdrop_path(), publications.get(i).getOverview(), publications.get(i).getVote_average()));
                        }
                        else if(daysDiff==0){
                            nextPageJsonMovies.add(new movieObj(publications.get(i).getId(), publications.get(i).getTitle(), publications.get(i).getRelease_date()+" (Today)" , publications.get(i).getDayLeft(), publications.get(i).getPoster_path(),publications.get(i).getBackdrop_path(), publications.get(i).getOverview(), publications.get(i).getVote_average()));
                        }

                    }
                    addListItemToAdapter(nextPageJsonMovies);
                }
                else{
                    // do nothing
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.drama_movies:
                resetVars();
                genre = 18;
                filter=true;
                queryCurrentPage=1;
                sendFilterRequest(String.valueOf(queryCurrentPage++));
                break;

            case R.id.action_movies:
                resetVars();
                genre = 28;
                filter=true;
                queryCurrentPage=1;
                sendFilterRequest(String.valueOf(queryCurrentPage++));
                break;

            case R.id.comedy_movies:
                resetVars();
                genre = 35;
                filter=true;
                queryCurrentPage=1;
                sendFilterRequest(String.valueOf(queryCurrentPage++));
                break;

            case R.id.romance_movies:
                resetVars();
                genre = 10749;
                filter=true;
                queryCurrentPage=1;
                System.out.println("Genre search");
                sendFilterRequest(String.valueOf(queryCurrentPage++));
                break;
            case R.id.thriller_movies:
                resetVars();
                genre = 53;
                filter=true;
                queryCurrentPage=1;
                System.out.println("Genre search");
                sendFilterRequest(String.valueOf(queryCurrentPage++));
                break;

                case R.id.terror_movies:
                resetVars();
                genre = 27;
                filter=true;
                queryCurrentPage=1;
                System.out.println("Genre search");
                sendFilterRequest(String.valueOf(queryCurrentPage++));
                break;

            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

//    public void filterByGenre(){
//        runOnUiThread(new Runnable() {
//          @Override
//          public void run() {
//              List < movieObj > list_filtered = new ArrayList<>();
//              System.out.println("Size" + jsonMovies.size());
//              for (movieObj movie : jsonMovies){
//                  for(int i =0; i< movie.getGenres().size(); i++){
//                      if(movie.getGenres().get(i).getTitle() == "Drama"){
//                          list_filtered.add(movie);
//                      }
//                  }
//              }
//              jsonMovies.clear();
//              addListItemToAdapter(list_filtered);
//          }
//      });
//
//    }



    @Override
    public void success(List<movieObj> publications) {
        this.publications =  publications;
        updatePublication();

    }

    @Override
    public void failed(int responseCode) {
        this.publications = null;
        updatePublication();

    }

}
