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
        // region findViewById
        TextView eventName = (TextView) convertView.findViewById(R.id.eventListItem_tbTitle);
        TextView eventMonth = (TextView) convertView.findViewById(R.id.listitem_event_month);
        TextView eventDate = (TextView) convertView.findViewById(R.id.listitem_event_date);
        TextView eventTime = (TextView) convertView.findViewById(R.id.listitem_event_time);
        TextView numPeopleAttending = (TextView) convertView.findViewById(R.id.listitem_event_number_people_attending);
        // endregion
        EventModel m = eventModelList.get(position);
        // region display information for each event
        eventName.setText(m.getName());
        eventMonth.setText(m.getMonthName());
        eventDate.setText(Integer.toString(m.getDay()));
        eventTime.setText(m.getTime());
        if(m.getNumFriendsAttending() == 1){
            numPeopleAttending.setText(String.valueOf(m.getNumFriendsAttending()) + " person attending");
        }
        else{
            numPeopleAttending.setText(String.valueOf(m.getNumFriendsAttending()) + " people attending");
        }
        // endregion
        return convertView;
    }

}
