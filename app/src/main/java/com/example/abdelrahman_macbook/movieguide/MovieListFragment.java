package com.example.abdelrahman_macbook.movieguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment implements Sorter{

    static private MovieListener movieListener;
    private  final String STORED_MOVIES = "stored_movies";
    private SharedPreferences prefs;
    private ImageAdapter moviePosterAdapter;
    String sortOrder;
    List<Movie> movies = new ArrayList<Movie>();
    private OnFragmentInteractionListener mListener;
    SharedPreferences sharedPreferences;

    public MovieListFragment() {
        setHasOptionsMenu(true);
    }



    void setMovieListener(MovieListener movieListener){
    this.movieListener = movieListener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setSorter(this);
       setHasOptionsMenu(true);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortOrder = prefs.getString("sortType", "popular");
        if(savedInstanceState != null){
            ArrayList<Movie> storedMovies = new ArrayList<Movie>();
            storedMovies = savedInstanceState.<Movie>getParcelableArrayList(STORED_MOVIES);
            movies.clear();
            movies.addAll(storedMovies);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        moviePosterAdapter = new ImageAdapter(
                getActivity(), R.layout.list_item_poster, R.id.list_item_poster_imageview, new ArrayList<String>());
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.main_movie_grid);
        gridView.setAdapter(moviePosterAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie details = movies.get(position);
                if (movieListener != null) {
                    movieListener.setSelectedMovie(details);
                }
            }

        });
         return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        String prefSortOrder = prefs.getString(getString(R.string.display_preferences_sort_order_key),
                getString(R.string.display_preferences_sort_default_value));

        if(movies.size() > 0 && prefSortOrder.equals(sortOrder)) {
            updatePosterAdapter();
        }else{
            sortOrder = prefSortOrder;
            getMovies();
        }
    }
    private void getMovies() {
        MovieFetcher fetchMoviesTask = new MovieFetcher(new AsyncTaskResponse() {
            @Override
            public void onTaskCompleted(List<Movie> results) {
                movies.clear();
                movies.addAll(results);
                updatePosterAdapter();
            }

            @Override
            public void onTaskCompletedTrailer(List<Trailer> results) {

            }

            @Override
            public void onTaskCompletedReview(List<Review> results) {

            }
        });
        fetchMoviesTask.execute(sortOrder);
    }


    private void updatePosterAdapter() {
        moviePosterAdapter.clear();
        for(Movie movie : movies) {
            moviePosterAdapter.add(movie.getPoster());
        }
    }

    @Override
    public void chooseSortCondition(String s) {
        if(s.equals("favourites")){
            sharedPreferences = getActivity().getSharedPreferences("movies", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

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
            movies.clear();
            movies.addAll(mSelectedList);
            updatePosterAdapter();

        }else {


            MovieFetcher fetchMoviesTask = new MovieFetcher(new AsyncTaskResponse() {
                @Override
                public void onTaskCompleted(List<Movie> results) {
                    movies.clear();
                    movies.addAll(results);
                    updatePosterAdapter();

                }

                @Override
                public void onTaskCompletedTrailer(List<Trailer> results) {

                }

                @Override
                public void onTaskCompletedReview(List<Review> results) {

                }
            });
            fetchMoviesTask.execute(s);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Movie> storedMovies = new ArrayList<Movie>();
        storedMovies.addAll(movies);
        outState.putParcelableArrayList(STORED_MOVIES, storedMovies);
    }

}
