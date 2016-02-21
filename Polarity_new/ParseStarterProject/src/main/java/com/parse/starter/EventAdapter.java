package com.parse.starter;

import android.app.Activity;
import android.content.Context;
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

        TextView tv = (TextView) convertView.findViewById(R.id.eventListItem_tbTitle);

        EventModel m = eventModelList.get(position);
        tv.setText(m.getName());
        return convertView;
    }

}
