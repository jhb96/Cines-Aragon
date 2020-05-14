package com.example.cinesaragon;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.example.cinesaragon.Adapter.movieListAdapterMainPage;
import com.example.cinesaragon.model.movieObj;
import com.example.cinesaragon.requestOperators.movieRequestOperator;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;




public class searchMovie extends AppCompatActivity implements movieRequestOperator.RequestOperatorListener {

    ListView searchLv;
    private movieListAdapterMainPage searchListAdapter;

    public static List<movieObj> jsonMoviesSearchMovie = new ArrayList<>();
    public static boolean clickedFromSearch;

    private FirebaseAuth firebaseAuth;
    private List<movieObj> publications=new ArrayList<>();

    private List<movieObj> nextPageJsonMovies = new ArrayList<>();
    public static boolean loadingMore=false;
    private int queryCurrentPage =1;

    private String keepSearchText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("SEARCH MOVIE");

        setContentView(R.layout.searchmoviedesign);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        jsonMoviesSearchMovie.clear();

        searchLv = (ListView) findViewById(R.id.searchLv);

        firebaseAuth= FirebaseAuth.getInstance();



        searchLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToMovieDetails(jsonMoviesSearchMovie.get(i).getId());
            }
        });

        searchListAdapter =new movieListAdapterMainPage(this, jsonMoviesSearchMovie);
        searchLv.setAdapter(searchListAdapter);


        searchLv.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                int lastInScreen = firstVisibleItem + visibleItemCount;
                if((lastInScreen == totalItemCount) && !loadingMore){
                    loadingMore=true;
                    queryCurrentPage++;
                    sendRequest(keepSearchText, String.valueOf(queryCurrentPage));
                }
            }
        });

    }


    private void sendRequest(String searchViewText, String page){
//        movieRequestOperator.urlToRequest="https://api.themoviedb.org/3/search/movie?api_key=a092bd16da64915723b2521295da3254&query="+searchViewText+"&page="+page;
//        movieRequestOperator.urlToRequest="https://api.themoviedb.org/3/movie/upcoming?api_key=21f9b30c3b2a6a1e906d03a384132fda&language=es-ES&page="+page;
        movieRequestOperator.urlToRequest= "https://api.themoviedb.org/3/search/movie?api_key=a092bd16da64915723b2521295da3254&language=es-ES&query="+ searchViewText+"&page="+page;
        movieRequestOperator ro= new movieRequestOperator();
        ro.setListener(this);
        ro.start();

    }

    private void goToMovieDetails(int movieId){
        clickedFromSearch =true;
        CarteleraActivity.clickedMovie=movieId;
        Intent newAct = new Intent(this,InfoPeliActivity.class);
        startActivity(newAct);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if(query.length()>0){
                            jsonMoviesSearchMovie.removeAll(jsonMoviesSearchMovie);
                            queryCurrentPage=1;
                            keepSearchText=query;
                            sendRequest(query, String.valueOf(queryCurrentPage));
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if(newText.length()>0){
                            jsonMoviesSearchMovie.removeAll(jsonMoviesSearchMovie);
                            queryCurrentPage=1;
                            keepSearchText=newText;
                            sendRequest(newText, String.valueOf(queryCurrentPage));
                        }
                        return false;
                    }
                }
        );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void addListItemToAdapter(List<movieObj> list){
        jsonMoviesSearchMovie.addAll(list);
        searchLv.invalidateViews();
        searchListAdapter.notifyDataSetChanged();
        nextPageJsonMovies.removeAll(nextPageJsonMovies);
        publications.removeAll(publications);
        loadingMore=false;
    }

    public void updatePublication(){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                //if(!publications.isEmpty()){
                if(publications!=null && !publications.isEmpty()){
                    //findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    for(int i=0;i<publications.size();i++) {
                        nextPageJsonMovies.add(new movieObj(publications.get(i).getId(), publications.get(i).getTitle(), publications.get(i).getRelease_date(), publications.get(i).getPoster_path(), publications.get(i).getBackdrop_path(), publications.get(i).getOverview(), publications.get(i).getVote_average()));
                    }
                    //searchLv.invalidateViews();
                    addListItemToAdapter(nextPageJsonMovies);
                }
                else{
                   jsonMoviesSearchMovie.removeAll(jsonMoviesSearchMovie);
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
    public void failed(int responseCode){
        this.publications=null;
        updatePublication();
    }
}