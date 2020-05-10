package com.example.cinesaragon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cinesaragon.Adapter.castListAdapter;
import com.example.cinesaragon.model.cast;
import com.example.cinesaragon.model.movieObj;
import com.example.cinesaragon.requestOperators.movieDetailsRequestOperator;
import com.example.cinesaragon.requestOperators.movieRequestOperator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class InfoPeliActivity extends AppCompatActivity implements movieDetailsRequestOperator.RequestOperatorListener, movieRequestOperator.RequestOperatorListener {

    private CheckBox addFavorites;
    private ImageView moviePoster;
    private TextView movieName,movieDirector,movieLength,movieRate,movieReleaseDate, movieOverview, movieGenres;
    private ListView castLv;
    private Button watchTrailer, buyTicketButton;

    private castListAdapter castAdapter;


    private List<cast> castList=new ArrayList<>();
    private List<movieObj> publications = new ArrayList<>();
    private List<movieObj> recMovies=new ArrayList<>();


    private movieObj publication;

    private boolean gotDetails=false;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbFavorites;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sendRequest();

        setContentView(R.layout.activity_info_peli);
//
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(myToolbar);


        firebaseAuth= FirebaseAuth.getInstance();

        dbFavorites = FirebaseDatabase.getInstance().getReference("Favorites");

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        addFavorites = findViewById(R.id.addFavoritesButton);
        movieName=(TextView) findViewById(R.id.movieDetailTitle);
        movieDirector=(TextView) findViewById(R.id.movieDetailDirector);
        movieReleaseDate=(TextView) findViewById(R.id.movieDetailReleaseDate);
        movieLength=(TextView) findViewById(R.id.movieDetailLength);
        movieRate=(TextView) findViewById(R.id.movieDetailRate);
        movieOverview=(TextView) findViewById(R.id.movieOverview);
        movieGenres=(TextView) findViewById(R.id.movieDetailGenres);
        moviePoster=(ImageView) findViewById(R.id.movieDetailPoster);
        castLv=(ListView) findViewById(R.id.castListView);
        buyTicketButton = (Button) findViewById(R.id.buyTicket_button);
//        watchTrailer=(Button) findViewById(R.id.watchTrailer);


//        watchTrailer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                watchYoutubeVideo(publication.getVideoList().get(0).getKey());
//            }
//        });
        buyTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ComprarEntradasActivity.class);
                startActivity(i);

            }
        });

        castAdapter = new castListAdapter(this,castList);
        castLv.setAdapter(castAdapter);


        movieObj item = movieObj.getMovieByID(CarteleraActivity.jsonMovies, CarteleraActivity.clickedMovie);
        movieName.setText(item.getTitle());
        setTitle(item.getTitle().toUpperCase());

        String imgPath = "https://image.tmdb.org/t/p/w780"+item.getBackdrop_path();

        Glide.with(getApplicationContext())
                .load(imgPath)
                .into(moviePoster);


        if(item.getVote_average()>0){
            movieRate.setText(item.getVote_average()+"/10");
        }
        else{
            movieRate.setText("-/10");
        }

//        addFavorites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
//                if(isChecked){
//                    String id=firebaseAuth.getCurrentUser().getUid();
//
//                    int movie_id=soonpage.clickedMovie;
//                    List<Integer> movie_ids=new ArrayList<>();
//
//                    for(movieToSave movies:favoriteMovies){
//                        if(movies.getUser_id().equals(id)){
//                            movie_ids=movies.getMovie_id();
//                            if(!movieToSave.hasContain(movie_ids,movie_id)){
//                                movie_ids.add(movie_id);
//                            }
//                        }
//                    }
//
//                    movieToSave movie=new movieToSave(id,movie_ids);
//                    dbFavorites.child(id).setValue(movie);
//
//                    if(!alreadyinFavs){
//                        /* MediaPlayer mpFa = MediaPlayer.create(getApplicationContext(),R.raw.fillingyourinbox);
//                         mpFa.start();*/
//                        Toast.makeText(getApplicationContext(), "Movie has been added to your favorites.",Toast.LENGTH_SHORT).show();
//                        //mpFa.seekTo(0);
//                    }
//                }else{
//                    String id=firebaseAuth.getCurrentUser().getUid();
//
//                    int movie_id=soonpage.clickedMovie;
//                    List<Integer> movie_ids=new ArrayList<>();
//
//                    for(movieToSave movies:favoriteMovies){
//                        if(movies.getUser_id().equals(id)){
//                            movie_ids=movies.getMovie_id();
//                            movie_ids=movieToSave.removeItem(movie_ids,movie_id);
//                        }
//                    }
//
//                    movieToSave movie=new movieToSave(id,movie_ids);
//                    dbFavorites.child(id).setValue(movie);
//
//                     /*MediaPlayer mpFr = MediaPlayer.create(getApplicationContext(),R.raw.caseclosed);
//                     mpFr.start();*/
//                    Toast.makeText(getApplicationContext(), "Movie has been removed from your favorites.",Toast.LENGTH_SHORT).show();
//                    //mpFr.seekTo(0);
//                }
//            }
//        }


//        dbFavorites.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                favoriteMovies.clear();
//                for(DataSnapshot usersSnapShot : dataSnapshot.getChildren()){
//                    if(usersSnapShot.getValue(movieToSave.class).getUser_id().equals(firebaseAuth.getCurrentUser().getUid())){
//                        favoriteMovies.add(usersSnapShot.getValue(movieToSave.class));
//                        if(movieToSave.hasContain(usersSnapShot.getValue(movieToSave.class).getMovie_id(),soonpage.clickedMovie)){
//                            alreadyinFavs =true;
//                            addFavorites.setChecked(true);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


    }



    private void sendRequest(){
        gotDetails=true;
        movieDetailsRequestOperator.urlToRequest="https://api.themoviedb.org/3/movie/"+CarteleraActivity.clickedMovie+"?api_key=21f9b30c3b2a6a1e906d03a384132fda&append_to_response=credits,videos,images";
        movieDetailsRequestOperator ro= new movieDetailsRequestOperator();
        ro.setListener(this);
        ro.start();
    }


    public void updatePublication(){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                if(publication!=null && gotDetails){
                    movieReleaseDate.setText(publication.getRelease_date());

                    String[] releaseDateArray = publication.getRelease_date().split("-");

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

                    if(daysDiff<=0){
                        //this movie is already released you cant set reminder for it
//                        setReminder.setVisibility(View.INVISIBLE);
//                        setReminder.setClickable(false);
                    }
                    if(publication.getLength()>0){
                        movieLength.setText(publication.getLength()+" min");
                    }
                    else{
                        movieLength.setText("- min");
                    }
                    String genresOfMovie = new String();
                    for(int i=0;i<publication.getGenres().size();i++){
                        if(publication.getGenres().size()-1 == i){
                            genresOfMovie+=publication.getGenres().get(i).getTitle().toString();
                        }
                        else{
                            genresOfMovie+=publication.getGenres().get(i).getTitle().toString()+" • ";
                        }
                    }

                    movieOverview.setText(publication.getOverview());

                    movieGenres.setText(genresOfMovie);

                    String director="";

                    for(int i=0;i<publication.getCrewList().size();i++){
                        if(publication.getCrewList().get(i).getJob().equals("Director") && director==""){
                            director=publication.getCrewList().get(i).getName();
                        }
                        else if(publication.getCrewList().get(i).getJob().equals("Director") && director!=""){
                            director+=", "+publication.getCrewList().get(i).getName();
                        }
                    }

                    movieDirector.setText("By "+director);

                    for(int i=0;i<publication.getCastList().size();i++){
                        castList.add(new cast(publication.getCastList().get(i).getId(), publication.getCastList().get(i).getName(), publication.getCastList().get(i).getImagePath(), publication.getCastList().get(i).getCharacter()));
                    }
                    castLv.invalidateViews();


                    if(publication.getVideoList().isEmpty() || publication.getVideoList()==null){
//                        watchTrailer.setVisibility(View.INVISIBLE);
                    }

                    final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
                    int pixels = (int) (240 * scale + 0.5f);


                    if(publication.getCastList()!=null && !publication.getCastList().isEmpty()){
                        ViewGroup.LayoutParams params = castLv.getLayoutParams();
                        params.height = pixels;
                        castLv.setLayoutParams(params);
                    }

//                    fillViewPager();
//                    recommendedMoviesRequest();

                }
                else{

                }


                if(publications!=null && !publications.isEmpty() && gotDetails){
                    for(int i=0;i<publications.size();i++){
                        recMovies.add(new movieObj(publications.get(i).getId(),publications.get(i).getTitle(),publications.get(i).getRelease_date(),publications.get(i).getPoster_path()));
                        recMovies.get(i).setBackdrop_path(publications.get(i).getBackdrop_path());
                        //System.out.println(publications.get(i).getTitle()+" is recommended. from movie details.");
                    }
                    final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
                    int pixels = (int) (350 * scale + 0.5f);
                    //                    viewPager.setLayoutParams(params);
//                    recMoviesAdapter.notifyDataSetChanged();

                }
                else{
                }
            }
        });

    }



    @Override
    public void success(movieObj publication) {
        this.publication=publication;
        updatePublication();
    }

    @Override
    public void success(List<movieObj> publications) {
        this.publications=publications;
        updatePublication();
    }

    @Override
    public void failed(int responseCode) {
        this.publications=null;
        updatePublication();
    }
}
