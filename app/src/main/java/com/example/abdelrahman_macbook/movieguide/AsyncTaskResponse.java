package com.example.abdelrahman_macbook.movieguide;

import java.util.List;

/**
 * Created by Abdelrahman-Macbook on 11/3/16.
 */
public interface AsyncTaskResponse {

    void onTaskCompleted( List<Movie> results );
    void onTaskCompletedTrailer(List<Trailer> results);
    void onTaskCompletedReview(List<Review> results);
}
