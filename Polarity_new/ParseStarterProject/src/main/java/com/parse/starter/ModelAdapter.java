package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Cameron on 2/8/2016.
 */
public class ModelAdapter extends BaseAdapter{
    Context context;
    ArrayList<Model> modelList;

    public ModelAdapter() {}

    public ModelAdapter(Context context, ArrayList<Model> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = null;

        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listitem_generic, null);

            TextView tv = (TextView) convertView.findViewById(R.id.generic_tbTitle);
            Button rm_btn = (Button) convertView.findViewById(R.id.generic_btnRemove);

            if(position % 2 == 1) convertView.setBackgroundColor(convertView.getResources().getColor(R.color.listview_background_1));
            else convertView.setBackgroundColor(convertView.getResources().getColor(R.color.listview_background_2));

            Model m = modelList.get(position);
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
