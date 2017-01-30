package com.example.abdelrahman_macbook.movieguide;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Abdelrahman-Macbook on 12/2/16.
 */
public class TrailersAdapter extends BaseAdapter {
    public ArrayList<Trailer> trailerList ;

    public TrailersAdapter() {

        this.trailerList=new ArrayList<>();
    }


    @Override
    public int getCount() {
        return trailerList.size();
    }

    @Override
    public Trailer getItem(int i) {
        return trailerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Trailer trailer = trailerList.get(position);
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(parent.getContext());
            v = vi.inflate(R.layout.trailer_object, null);
        }
        TextView trailerName = (TextView) v.findViewById(R.id.trailerName);

        trailerName.setText(trailerList.get(position).getName());
        return v;
    }
}
