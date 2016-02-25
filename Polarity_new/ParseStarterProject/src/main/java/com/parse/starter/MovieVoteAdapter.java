package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.internal.view.menu.MenuView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lpett on 2/20/2016.
 */
public class MovieVoteAdapter extends BaseAdapter {

    public static final String TAG = MovieVoteAdapter.class.getSimpleName();
    Context context;
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.listitem_movievote, null);

        TextView tv = (TextView) convertView.findViewById(R.id.movieVoteListItem_tbTitle);
        ImageView iv = (ImageView) convertView.findViewById(R.id.movieVoteListItem_ivSelected);

        MovieModel m = movieModelList.get(position);
        if(position % 2 == 1) convertView.setBackgroundColor(convertView.getResources().getColor(R.color.listview_background_1));
        else convertView.setBackgroundColor(convertView.getResources().getColor(R.color.listview_background_2));

        tv.setText(m.getName());

        if(m.hasVote) {
            iv.setImageResource(R.drawable.signcheckicon);
            Log.d(TAG, "Item[" + m.getName() + "].ImageView is Checked");
        }
        else {
            iv.setImageResource(R.drawable.signuncheckicon);
            Log.d(TAG, "Item[" + m.getName() + "].ImageView is Unchecked");
        }
        return convertView;
    }
}
