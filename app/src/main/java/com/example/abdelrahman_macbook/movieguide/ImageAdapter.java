package com.example.abdelrahman_macbook.movieguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdelrahman-Macbook on 11/3/16.
 */
public class ImageAdapter extends ArrayAdapter<String> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    private int layoutId;
    private int imageViewResourceId;
    public ImageAdapter(Context context, int layoutId, int imageViewResourceId, ArrayList<String> urls) {
        super(context, 0,urls);

        this.mLayoutInflater = LayoutInflater.from(context);
        this.context=context;
        this.layoutId=layoutId;
        this.imageViewResourceId=imageViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        String url;
        if(v==null){
            v = mLayoutInflater.inflate(layoutId, parent, false);

        }

        ImageView imageView = (ImageView) v.findViewById(imageViewResourceId);
        url = getItem(position);
        Picasso.with(context).load(url).into(imageView);


        return v;
    }
}
