package com.example.abdelrahman_macbook.movieguide;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MovieDetailFragment extends Fragment {
    private Intent intent;
    private ArrayList<Trailer> trailers = new ArrayList<Trailer>();
    private TrailersAdapter trailersAdapter;
    private String movieId;
    SharedPreferences sharedPreferences;
    private String savedMovie;
    public MovieDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        intent = getActivity().getIntent();
            movieInfo(rootView);
        //this.trailers=trailers;

        trailersAdapter = new TrailersAdapter();


        ListView listView = (ListView) rootView.findViewById(R.id.TrailerNReviewsList);
        listView.setAdapter(trailersAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String key = trailers.get(position).getKey();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key)));

            }
        });
        return rootView;
    }



   private void movieInfo(View v){
       TextView title = (TextView) v.findViewById(R.id.movie_title_view);
       ImageView poster = (ImageView) v.findViewById(R.id.poster_image_view);
       TextView releaseDate = (TextView) v.findViewById(R.id.release_date);
       TextView rating = (TextView) v.findViewById(R.id.ratings_view);
       TextView overview = (TextView) v.findViewById(R.id.overview_view);
       final Bundle bundle;
       if (MainActivity.is2Pan) {
           bundle = getArguments();
           //Log.i("pane", "movieInfo: two pane "+bundle.getString("title"));

       }
       else{

           bundle = getActivity().getIntent().getExtras();
           //Log.i("pane", "movieInfo: one pane "+bundle.getString("title"));

       }


       title.setText(bundle.getString("title"));
       Picasso.with(getActivity()).load(bundle.getString("poster")).into(poster);
       releaseDate.setText(bundle.getString("release_date"));
       rating.setText(bundle.getString("votes") + "/10");
       overview.setText("Overview \n \n" + bundle.getString("overview"));
       movieId = bundle.getString("movie_id");

       ImageButton fav = (ImageButton) v.findViewById(R.id.favouriteButton);

       fav.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Context context = getActivity();
               CharSequence text = "You have favorited this movie!";
               int duration = Toast.LENGTH_SHORT;

               Toast toast = Toast.makeText(context, text, duration);
               toast.show();


               sharedPreferences = getActivity().getSharedPreferences("movies", Context.MODE_PRIVATE);
               SharedPreferences.Editor editor = sharedPreferences.edit();

               Movie movie = new Movie(bundle.getString("title"), bundle.getString("poster"), bundle.getString("overview"), bundle.getString("release_date"), bundle.getString("votes"), bundle.getString("movie_id"));

               //RETRIEVING DATA FROM SHARED PREF
               String favjson = sharedPreferences.getString("movies", "");
               Gson gson = new Gson();
               String empty_list = gson.toJson(new ArrayList<Movie>());
               ArrayList<Movie> mSelectedList;
               if (sharedPreferences.getString("movies", "").isEmpty()) {

                   mSelectedList = new ArrayList<Movie>();
               } else {
                   mSelectedList = gson.fromJson(sharedPreferences.getString("movies", empty_list),
                           new TypeToken<ArrayList<Movie>>() {
                           }.getType());
               }

               if(favExists(mSelectedList, movie)){
                   Toast.makeText(getActivity(), "Movie removed from favourites!", Toast.LENGTH_SHORT).show();
               }
               else
                mSelectedList.add(movie);

               String jsonString = gson.toJson(mSelectedList);
               editor.putString("movies", jsonString);
               editor.commit();
           }
       });

   }

    public boolean favExists(ArrayList<Movie> arr, Movie movie){

        for(int i=0; i<arr.size();i++){
        if(arr.get(i).getId().equals(movie.getId())){
            arr.remove(i);
            return true;
        }
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        getTrailers();
    }

    private void getTrailers() {
        TrailerFetcher fetchTrailersTask = new TrailerFetcher(new AsyncTaskResponse() {
            @Override
            public void onTaskCompleted(List<Movie> results) {

            }

            @Override
            public void onTaskCompletedTrailer(List<Trailer> results) {
                trailers.clear();
                trailers.addAll(results);
                trailersAdapter.trailerList = trailers;
                trailersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTaskCompletedReview(List<Review> results) {

            }
        });
        fetchTrailersTask.execute(movieId);

        ReviewFetcher fetchReviewsTask = new ReviewFetcher(new AsyncTaskResponse() {
            @Override
            public void onTaskCompleted(List<Movie> results) {

            }

            @Override
            public void onTaskCompletedTrailer(List<Trailer> results) {

            }

            @Override
            public void onTaskCompletedReview(List<Review> results) {
                String finalString = "";
                for (int i=0; i<results.size(); i++){
                    finalString = finalString + results.get(i).getAuthor() + "\n\n" + results.get(i).getContent() + "\n\n\n\n";
                }
                if ((TextView) getView().findViewById(R.id.reviewText) != null) {
                    ((TextView) getView().findViewById(R.id.reviewText)).setText(finalString);
                }
            }
        });
        fetchReviewsTask.execute(movieId);
    }
}
