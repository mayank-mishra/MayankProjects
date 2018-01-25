package com.adani.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.adani.models.DistributerModel;
import com.demo.demoapp.R;

public class AutocompleteDistributerAdapter extends ArrayAdapter<DistributerModel> {

    Context context;
    int resource, textViewResourceId;
    List<DistributerModel> items, tempItems, suggestions;

    public AutocompleteDistributerAdapter(Context context, int resource, int textViewResourceId, List<DistributerModel> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<DistributerModel>(items); // this makes the difference.
        suggestions = new ArrayList<DistributerModel>();
    }
    
    public AutocompleteDistributerAdapter(Context context,List<DistributerModel> items) {
    	super(context, 0);
        this.context = context;
        this.items = items;
        tempItems = new ArrayList<DistributerModel>(items); // this makes the difference.
        suggestions = new ArrayList<DistributerModel>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.spinner_item, parent, false);
        }
        DistributerModel people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.txt_item);
            if (lblName != null)
                lblName.setText(people.Name);
        }
        return view;
    }
    
    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public DistributerModel getItem(int position)
    {
        return items.get(position);
    }
    
    @Override
    public long getItemId(int position) {
    	// TODO Auto-generated method stub
    	return position;
    }

    @Override
    public int getPosition(DistributerModel item)
    {
        return items.indexOf(item);
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((DistributerModel) resultValue).Name;
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (DistributerModel people : tempItems) {
                    if (people.Name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<DistributerModel> filterList = (ArrayList<DistributerModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (DistributerModel people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
    
    public void refreshAdapter(ArrayList<DistributerModel> list){
    	this.items=list;
    }
    
    public DistributerModel getDisItem(int position){
    	return items.get(position);
    }
}