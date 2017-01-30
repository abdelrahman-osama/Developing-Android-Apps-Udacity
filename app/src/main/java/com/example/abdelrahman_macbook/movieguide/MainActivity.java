package com.example.abdelrahman_macbook.movieguide;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MovieListener {
    public static boolean is2Pan=false;
    static Sorter sorter;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private FragmentManager fragmentManager = getFragmentManager();
    MovieListFragment fragment;
    private SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            fragment = new MovieListFragment();
            fragment.setMovieListener(this);
            fragmentManager.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }else{
            fragment = (MovieListFragment) fragmentManager.getFragment(
                    savedInstanceState, "fragmentContent");

          //  PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        }

        if(null!=findViewById(R.id.DetailPane)){
            //Toast.makeText(MainActivity.this,"two pane",Toast.LENGTH_LONG).show();
            is2Pan=true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static void setSorter(Sorter s){
        sorter = s;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sort_by_top_rated) {
            //MovieListFragment.sortBy('')
            //startActivity(new Intent(this, SettingsActivity.class));
            sorter.chooseSortCondition("top_rated");
            return true;
        }

        if (id == R.id.sort_by_popular) {
            //MovieListFragment.sortBy('')
            //startActivity(new Intent(this, SettingsActivity.class));
            sorter.chooseSortCondition("popular");
            return true;
        }

        if(id == R.id.favourites){

            sorter.chooseSortCondition("favourites");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        fragmentManager.putFragment(savedInstanceState, "fragmentContent", fragment);
    }

    public void setSelectedMovie(Movie details) {

    if(!is2Pan){
                        Intent intent = new Intent(this, MovieDetailActivity.class)
                        .putExtra("movies_details", details.describeContents())
                        .putExtra("overview", details.getOverview())
                        .putExtra("title", details.getTitle())
                        .putExtra("poster", details.getPoster())
                        .putExtra("release_date", details.getReleaseDate())
                        .putExtra("votes", details.getVoteAverage())
                                .putExtra("movie_id", details.getId());
                startActivity(intent);
    }
        else{
    //2Pane
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle extras = new Bundle();
        extras.putString("overview", details.getOverview());
        extras.putString("title", details.getTitle());
        extras.putString("poster", details.getPoster());
        extras.putString("release_date", details.getReleaseDate());
        extras .putString("votes", details.getVoteAverage());
        extras.putString("movie_id", details.getId());
       // Toast.makeText(MainActivity.this, "two paneeeeee fragment", Toast.LENGTH_SHORT).show();
        movieDetailFragment.setArguments(extras);
        getFragmentManager().beginTransaction().replace(R.id.DetailPane,movieDetailFragment, "").commit();

    }


    }


}
