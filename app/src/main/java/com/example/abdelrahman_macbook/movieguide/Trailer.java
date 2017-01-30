package com.example.abdelrahman_macbook.movieguide;

import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Abdelrahman-Macbook on 12/2/16.
 */
public class Trailer {

    private String id;
    private String name;
    private String key;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Trailer(JSONObject trailer) throws JSONException {
        this.id = trailer.getString(id);
        this.name = trailer.getString(name);
        this.key = trailer.getString(key);
    }
}
