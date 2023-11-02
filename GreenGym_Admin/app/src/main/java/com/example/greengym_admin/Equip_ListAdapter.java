package com.example.greengym_admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Equip_ListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<park_item> item;
    private TextView park;

    public Equip_ListAdapter(Context context, ArrayList<park_item> item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public int getCount() {
        return this.item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_equip_item, null);

        }
        park = (TextView) convertView.findViewById(R.id.park_list);
        park.setText(item.get(position).getPark_name());

        return convertView;
    }
}