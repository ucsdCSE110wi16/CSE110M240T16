package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kendrick on 2/20/16.
 */
public class FriendAdapter extends BaseAdapter {

    Context context;
    ArrayList<FriendModel> friendModelList;

    public FriendAdapter(Context context, ArrayList<FriendModel> modelList){
        this.context = context;
        this.friendModelList = modelList;
    }

    @Override
    public int getCount() {
        return friendModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.listitem_friends, null);

        TextView tv = (TextView) convertView.findViewById(R.id.friendListItem_tbName);
        ImageView iv = (ImageView) convertView.findViewById(R.id.friendListItem_ivSelected);
        Button btn = (Button) convertView.findViewById(R.id.friend_btnRemove);

        if (position % 2 == 1)
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.listview_background_1));
        else
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.listview_background_2));

        FriendModel m = friendModelList.get(position);
        tv.setText(m.getName());

        if (m.isSelected) {
            iv.setImageResource(R.drawable.signcheckicon);
        } else {
            iv.setImageResource(R.drawable.signuncheckicon);
        }

        if(m.isDeletable) {
            btn.setEnabled(false);
            btn.setVisibility(View.VISIBLE);
        }
        else {
            btn.setEnabled(true);
            btn.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
