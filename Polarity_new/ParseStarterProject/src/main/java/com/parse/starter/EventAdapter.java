package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lpett on 2/20/2016.
 */
public class EventAdapter extends BaseAdapter {

    Context context;
    ArrayList<EventModel> eventModelList;

    public EventAdapter(Context context, ArrayList<EventModel> modelList) {
        this.context = context;
        this.eventModelList = modelList;
    }

    @Override
    public int getCount() {
        return eventModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.listitem_event, null);

        // set background color of the events
        if(position % 2 == 1) convertView.setBackgroundColor(convertView.getResources().getColor(R.color.listview_background_1));
        else convertView.setBackgroundColor(convertView.getResources().getColor(R.color.listview_background_2));

        TextView eventName = (TextView) convertView.findViewById(R.id.eventListItem_tbTitle);
        TextView eventMonth = (TextView) convertView.findViewById(R.id.listitem_event_month);


        EventModel m = eventModelList.get(position);

        eventName.setText(m.getName());
        eventMonth.setText(String.valueOf(m.getDate()).substring(4, 7));

        Log.d("TAG", String.valueOf(m.getName()));
//        Log.d("TAG", String.valueOf(m.getDate()));
        Log.d("TAG", String.valueOf(m.getNumFriendsAttending()));
        return convertView;
    }

}
