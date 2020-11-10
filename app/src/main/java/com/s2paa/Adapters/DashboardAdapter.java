package com.s2paa.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.s2paa.Listener.DashboardItemClickListeners;
import com.s2paa.Model.DashboardItem;
import com.s2paa.R;
import com.s2paa.Utils.TTextView;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

/**
 * Created by admin on 5/11/2017.
 */

public class DashboardAdapter extends BaseAdapter {


    ArrayList<DashboardItem> list;
    Context context;
    DashboardItemClickListeners click;

    LayoutInflater inflater;
    public DashboardAdapter(Context context,ArrayList<DashboardItem> list){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }
    static class ViewHolder {
        TTextView tv_item;
        ImageView img;
        LinearLayout main;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public DashboardItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DashboardItem  item  = list.get(position);

        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.dashboard_items, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView) view.findViewById(R.id.img_item);
            viewHolder.tv_item = (TTextView) view.findViewById(R.id.tv_item_name);
            viewHolder.main=(LinearLayout)view.findViewById(R.id.main);
            viewHolder.tv_item.setOnClickListener(onclickListener);
            viewHolder.img.setOnClickListener(onclickListener);
            viewHolder.main.setOnClickListener(onclickListener);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tv_item.setText(item.title);
        Picasso.with(context).load(item.icon).into(viewHolder.img);
        viewHolder.tv_item.setTag(item.title);
        viewHolder.img.setTag(item.title);
        viewHolder.main.setTag(item.title.toString());

        return view;
    }
    public void attach(Fragment f) {
        try {
            click = (DashboardItemClickListeners) f;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "Fragment must implement NavigationDrawerListHandler.");
        }
    }


    private View.OnClickListener onclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectNavItem(v.getTag().toString());
        }
    };
    public void selectNavItem(String id) {
        click.onItemClick(id);
    }
}
