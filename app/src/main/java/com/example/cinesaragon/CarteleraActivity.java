package com.example.cinesaragon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toolbar;

import com.example.cinesaragon.Adapter.movieListAdapterMainPage;
import com.example.cinesaragon.model.movieObj;
import com.example.cinesaragon.requestOperators.movieRequestOperator;

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

    private List<movieObj> nextPageJsonMovies = new ArrayList<>();

    private movieListAdapterMainPage soonAdapter;

    Context thiscontext;

    public static boolean sorted=false,loadingMore=false;

    private int queryCurrentPage =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartelera);

        thiscontext = this;

        Intent intent = getIntent();
        String nameCine = intent.getStringExtra("cineElegido");
        this.setTitle(nameCine);



        jsonMovies.clear();
        nextPageJsonMovies.clear();
        publications.clear();

        moviesLv =(ListView) findViewById(R.id.soonListView);

        soonAdapter = new movieListAdapterMainPage(thiscontext, jsonMovies);

        moviesLv.setAdapter(soonAdapter);

        moviesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedMovie = jsonMovies.get(i).getId();
                Intent intent = new Intent(getApplicationContext(), InfoPeliActivity.class);
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
                    sendRequest(String.valueOf(queryCurrentPage));
                }
            }
        });

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
//        movieRequestOperator.urlToRequest="https://api.themoviedb.org/3/movie/now_playing?api_key=21f9b30c3b2a6a1e906d03a384132fda&sort_by=release_date.asc&language=es&page="+page+"&region=ES";
        movieRequestOperator.urlToRequest="https://api.themoviedb.org/3/movie/now_playing?api_key=21f9b30c3b2a6a1e906d03a384132fda&sort_by=popularity.desc&page="+page;
        movieRequestOperator ro= new movieRequestOperator();
        ro.setListener(this);
        ro.start();

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
