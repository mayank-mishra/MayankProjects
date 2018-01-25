package com.adani.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.adani.models.OrderModel;
import com.demo.demoapp.R;

public class OrderLIneItemAdapter extends BaseAdapter{
	
	private ArrayList<OrderModel> list;
	private LayoutInflater inflater;
	private Context context;
	
	public OrderLIneItemAdapter(ArrayList<OrderModel> distributerList,LayoutInflater inflater,Context context) {
		this.list=distributerList;
		this.inflater=inflater;
		this.context=context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public OrderModel getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder holder = null;
		if(view==null){
			holder=new ViewHolder();
			view =inflater.inflate(R.layout.order_detail_item, null);
			holder.txt_name=(TextView)view.findViewById(R.id.txt_item_name);
			holder.txt_amount=(TextView)view.findViewById(R.id.txt_item_amount);
			holder.txt_others=(TextView)view.findViewById(R.id.txt_item_qty);
			view.setTag(holder);
		}else{
			holder=(ViewHolder) view.getTag();
		}
		holder.txt_name.setText(getItem(position).Product_Name__c);
		holder.txt_others.setText(getItem(position).Quantity__c);
		holder.txt_amount.setText(getItem(position).Rate__c);
		holder.txt_others.setVisibility(View.VISIBLE);
		holder.txt_amount.setVisibility(View.VISIBLE);
		return view;
	}
	
	public void refreshAdapter(ArrayList<OrderModel> list){
		if(list!=null)
		this.list=list;
		notifyDataSetChanged();
	}
	
	class ViewHolder{
		TextView txt_name;
		TextView txt_amount,txt_others;
	}
	
}
