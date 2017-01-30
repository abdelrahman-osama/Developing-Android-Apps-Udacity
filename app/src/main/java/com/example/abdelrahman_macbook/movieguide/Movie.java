package com.example.abdelrahman_macbook.movieguide;

import android.os.Parcel;
import android.os.Parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abdelrahman-Macbook on 11/3/16.
 */
public class Movie implements Parcelable {

    private String title;
    private String poster;
    private String overview;
    private String releaseDate;
    private String voteAverage;
    private String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Movie(String title, String poster, String overview,String releaseDate, String voteAverage, String id){
        this.title = title;
        this.poster=poster;
        this.overview=overview;
        this.releaseDate=releaseDate;
        this.voteAverage=voteAverage;
        this.id=id;


    }


    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(voteAverage);
    }

    private Movie(Parcel in){
        title = in.readString();
        poster = in.readString();
        overview= in.readString();
        releaseDate= in.readString();
        voteAverage= in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.ClassLoaderCreator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }

        @Override
        public Movie createFromParcel(Parcel source, ClassLoader loader) {
            return null;
        }
    };

}
