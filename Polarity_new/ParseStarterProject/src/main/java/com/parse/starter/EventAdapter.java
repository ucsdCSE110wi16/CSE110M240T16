package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lpett on 2/20/2016.
 */
public class EventAdapter extends CustomAdapter {

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = null;

        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.items, null);

            TextView tv = (TextView) convertView.findViewById(R.id.name);
            Button rm_btn = (Button) convertView.findViewById(R.id.rm_btn);

            EventModel m = eventModelList.get(position);
            tv.setText(m.getName());

            // click listiner for remove button
            rm_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modelList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }

}