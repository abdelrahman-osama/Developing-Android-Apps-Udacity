package com.example.abdelrahman_macbook.movieguide;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

public class MovieDetailActivity extends AppCompatActivity {
    Movie movie;
    private FragmentManager fragmentManager = getFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if(savedInstanceState==null){


            String title =  getIntent().getStringExtra("title");
            String overview = getIntent().getStringExtra("overview");
            String release_date = getIntent().getStringExtra("release_date");
            String votes = getIntent().getStringExtra("votes");
            String poster = getIntent().getStringExtra("poster");
            Bundle bundle = getIntent().getExtras();
            MovieDetailFragment mdf = new MovieDetailFragment();
            mdf.setArguments(bundle);

            fragmentManager.beginTransaction().add(R.id.container, mdf).commit();

        }


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
