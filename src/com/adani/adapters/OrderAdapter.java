package com.adani.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.adani.database.DBFeilds;
import com.adani.models.DistributerModel;
import com.adani.models.OrderResponse;
import com.demo.demoapp.BaseActivity;
import com.demo.demoapp.R;

public class OrderAdapter extends BaseAdapter{
	
	private ArrayList<OrderResponse> list;
	private LayoutInflater inflater;
	private Context context;
	
	public OrderAdapter(ArrayList<OrderResponse> distributerList,LayoutInflater inflater,Context context) {
		this.list=distributerList;
		this.inflater=inflater;
		this.context=context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public OrderResponse getItem(int position) {
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
			view =inflater.inflate(R.layout.product_item, null);
			holder.txt_name=(TextView)view.findViewById(R.id.txt_pname);
			holder.txt_amount=(TextView)view.findViewById(R.id.txt_pamount);
			holder.txt_others=(TextView)view.findViewById(R.id.txt_other);
			view.setTag(holder);
		}else{
			holder=(ViewHolder) view.getTag();
		}
		ArrayList<DistributerModel> dist=BaseActivity.db.getDistributer(" where "+DBFeilds.DISTRIBUTER_ID+" = '"+getItem(position).Distributor__c+"'");
		holder.txt_name.setText(getItem(position).Name);
		holder.txt_others.setText(getItem(position).Distributor__c);
		holder.txt_others.setVisibility(View.VISIBLE);
		holder.txt_amount.setVisibility(View.GONE);
		return view;
	}
	
	public void refreshAdapter(ArrayList<OrderResponse> list){
		if(list!=null)
		this.list=list;
		notifyDataSetChanged();
	}
	
	class ViewHolder{
		TextView txt_name;
		TextView txt_amount,txt_others;
	}
	
}
