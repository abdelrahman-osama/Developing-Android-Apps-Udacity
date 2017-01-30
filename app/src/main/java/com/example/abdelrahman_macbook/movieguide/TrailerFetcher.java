package com.example.abdelrahman_macbook.movieguide;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Abdelrahman-Macbook on 12/2/16.
 */
public class TrailerFetcher extends AsyncTask<String, Void, List<Trailer>> {
    public AsyncTaskResponse delegate;
    public final String API_KEY = "ff68611b11392b28d8b46f3e461fae97";
    Trailer[] trailers;

    public TrailerFetcher(AsyncTaskResponse delegate){
        this.delegate = delegate;
    }
    @Override
    protected void onPostExecute(List<Trailer> results) {
        if (results != null) {
            delegate.onTaskCompletedTrailer(results);
        }
    }
    @Override
    protected List<Trailer> doInBackground(String... strings) {

        if(strings.length==0){
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String trailersJsonStr = null;

        try {

            String BASE_URL = "https://api.themoviedb.org/3/movie/";
            String id; //ANA 3AYZ A5OD ID EL FILM HENA
            final String vid = "/videos?";
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
            trailersJsonStr = buffer.toString();
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
            return extractData(trailersJsonStr);
        } catch (JSONException e) {

            e.printStackTrace();
        }



        return null;
    }
    private List<Trailer> extractData(String trailersJson) throws JSONException {

        JSONObject testJson = new JSONObject(trailersJson);


        Gson gson = new GsonBuilder().create();
        trailers =  gson.fromJson(testJson.getJSONArray("results").toString(), Trailer[].class);


        return Arrays.asList(trailers);
    }


}