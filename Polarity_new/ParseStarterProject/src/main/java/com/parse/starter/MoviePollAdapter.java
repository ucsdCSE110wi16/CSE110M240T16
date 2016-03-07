package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lpett on 2/20/2016.
 */
public class MoviePollAdapter extends BaseAdapter {

    public static final String TAG = MovieVoteAdapter.class.getSimpleName();
    Context context;
    ArrayList<MovieModel> movieModelList;

    public MoviePollAdapter(Context context, ArrayList<MovieModel> modelList) {
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
        convertView = mInflater.inflate(R.layout.listitem_moviepolls, null);

        if(position % 2 == 1) convertView.setBackgroundColor(convertView.getResources().getColor(R.color.listview_background_1));
        else convertView.setBackgroundColor(convertView.getResources().getColor(R.color.listview_background_2));

        TextView title = (TextView) convertView.findViewById(R.id.moviePolls_txtTitle);
        TextView percentage = (TextView) convertView.findViewById(R.id.moviePolls_txtPercentage);
        ProgressBar pb = (ProgressBar) convertView.findViewById(R.id.moviePolls_pbGraph);

        MovieModel m = movieModelList.get(position);
        title.setText(m.getName());

        int votes = m.getNumVotes();
        int maxPossibleVotes = m.getNumMaxPossibleVotes();

        percentage.setText((votes*100/maxPossibleVotes) + "%");
        pb.setMax(m.getNumMaxPossibleVotes());
        pb.setProgress(m.getNumVotes());

        return convertView;
    }
}