package com.example.greengym_admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Equip_detailAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<equip_item> item;
    private TextView equip;

    public Equip_detailAdapter(Context context, ArrayList<equip_item> item) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.equip_list, null);

        }
        equip = (TextView) convertView.findViewById(R.id.equip_list);
        equip.setText(item.get(position).getE_name());

        return convertView;
    }
}
