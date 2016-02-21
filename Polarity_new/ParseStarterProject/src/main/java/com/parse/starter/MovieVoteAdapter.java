package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lpett on 2/20/2016.
 */
public class MovieVoteAdapter extends ModelAdapter {

    public static final String TAG = MovieVoteAdapter.class.getSimpleName();
    ArrayList<MovieModel> movieModelList;

    public MovieVoteAdapter(Context context, ArrayList<MovieModel> modelList) {
        this.context = context;
        this.movieModelList = modelList;
    }

    @Override
    public int getCount() {
        return movieModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieModelList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.listitem_movievote, null);

        TextView tv = (TextView) convertView.findViewById(R.id.movieVoteListItem_tbTitle);
        ImageView iv = (ImageView) convertView.findViewById(R.id.movieVoteListItem_ivSelected);

        MovieModel m = movieModelList.get(position);
        tv.setText(m.getName());
        if(m.hasVote) {
            iv.setVisibility(View.VISIBLE);
            tv.setText(m.getName() + " - VOTE");
            Log.d(TAG, "Item[" + m.getName() + "].ImageView is VISIBLE");
        }
        else {
            iv.setVisibility(View.INVISIBLE);
            Log.d(TAG, "Item[" + m.getName() + "].ImageView is INVISIBLE");
        }
        return convertView;
    }
}
