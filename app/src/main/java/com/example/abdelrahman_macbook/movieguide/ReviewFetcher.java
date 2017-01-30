package com.example.abdelrahman_macbook.movieguide;

import android.net.Uri;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Abdelrahman-Macbook on 12/3/16.
 */
public class ReviewFetcher extends AsyncTask<String, Void, List<Review>> {


    public AsyncTaskResponse delegate;
    public final String API_KEY = "ff68611b11392b28d8b46f3e461fae97";
    Review[] reviews;

    public ReviewFetcher(AsyncTaskResponse delegate){
        this.delegate = delegate;
    }
    @Override
    protected void onPostExecute(List<Review> results) {
        if (results != null) {
            delegate.onTaskCompletedReview(results);
        }
    }
    @Override
    protected List<Review> doInBackground(String... strings) {

        if(strings.length==0){
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String reviewsJsonStr = null;

        try {

            String BASE_URL = "https://api.themoviedb.org/3/movie/";
            String id; //ANA 3AYZ A5OD ID EL FILM HENA
            final String vid = "/reviews?";
            final String KEY = "api_key";
            String movieID = strings[0];
            BASE_URL += movieID+vid;
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(KEY, API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            reviewsJsonStr = buffer.toString();
        } catch (IOException e) {

            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                }
            }
        }

        try {
            return extractData(reviewsJsonStr);
        } catch (JSONException e) {

            e.printStackTrace();
        }



        return null;
    }
    private List<Review> extractData(String reviewsJson) throws JSONException {

        JSONObject testJson = new JSONObject(reviewsJson);


        Gson gson = new GsonBuilder().create();
        reviews =  gson.fromJson(testJson.getJSONArray("results").toString(), Review[].class);


        return Arrays.asList(reviews);
    }


}
