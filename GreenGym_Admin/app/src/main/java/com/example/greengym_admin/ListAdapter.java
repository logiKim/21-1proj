package com.example.greengym_admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<item> item;
    private TextView park;
    private TextView date;

    public ListAdapter(Context context, ArrayList<item> item){
        this.context = context;
        this.item = item;
    }

    @Override
    public int getCount() {
        return this.item.size();
    }

    @Override
    public item getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item, null);

        date = (TextView) convertView.findViewById(R.id.date_list);
        park = (TextView) convertView.findViewById(R.id.park_list);

        date.setText(item.get(position).getR_date());
        park.setText(item.get(position).getP_name());

        return convertView;
    }

}