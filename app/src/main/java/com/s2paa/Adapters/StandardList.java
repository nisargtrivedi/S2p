package com.s2paa.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.s2paa.Model.Standard;
import com.s2paa.R;
import com.s2paa.Utils.TTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class StandardList extends ArrayAdapter<Standard> {


    List<Standard> items;
    List<Standard> fItems;
    private LayoutInflater inflater;
    SchoolFilter filter;

    static class ViewHolder{
        TTextView standard_name;
    }

    public StandardList(Context context,List<Standard> items) {
        super(context,R.layout.standard_item);
        inflater = LayoutInflater.from(context);
        this.items = items;
        this.fItems = items;
    }

    @Override
    public int getCount() {
        return fItems.size();
    }

    @Override
    public Standard getItem(int i) {
        return fItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return fItems.get(i).getID();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.standard_item,null);
            holder.standard_name=(TTextView) convertView.findViewById(R.id.standard_name);
            convertView.setTag(holder);

        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        final Standard item=fItems.get(position);

        holder.standard_name.setText(item.name);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter  = new SchoolFilter();
        }
        return filter;
    }

    private class SchoolFilter extends Filter {


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            fItems = (List<Standard>)results.values;
            if(results.count > 0)
                notifyDataSetChanged();
            else
                notifyDataSetInvalidated();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterText = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            if (filterText == null || filterText.length() == 0) {
                synchronized (this) {
                    results.values = items;
                    results.count = items.size();
                }
            }
            else {
                List<Standard> filteredList = new ArrayList<Standard>();
                List<Standard> unFilteredList = new ArrayList<Standard>();
                synchronized (this) {
                    unFilteredList.addAll(items);
                }
                for (Standard li : unFilteredList) {
                    if(li.name.toLowerCase().indexOf(constraint.toString().toLowerCase()) > -1
                            ) {
                        filteredList.add(li);
                    }
                }
                results.values = filteredList;
                results.count = filteredList.size();
            }

            return results;
        }
    }


}
