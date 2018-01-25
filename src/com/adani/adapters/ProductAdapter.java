package com.adani.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.adani.models.DistributerModel;
import com.adani.models.ProductModel;
import com.demo.demoapp.R;

public class ProductAdapter extends BaseAdapter{
	
	private ArrayList<ProductModel> list;
	private LayoutInflater inflater;
	private Context context;
	
	public ProductAdapter(ArrayList<ProductModel> productList,LayoutInflater inflater,Context context) {
		this.list=productList;
		this.inflater=inflater;
		this.context=context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public ProductModel getItem(int position) {
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
			view.setTag(holder);
		}else{
			holder=(ViewHolder) view.getTag();
		}
		holder.txt_name.setText(getItem(position).Name);
		holder.txt_amount.setText(getItem(position).Product_Code__c);
		return view;
	}
	
	public void refreshAdapter(ArrayList<ProductModel> list){
		if(list!=null)
		this.list=list;
		notifyDataSetChanged();
	}
	
	class ViewHolder{
		TextView txt_name;
		TextView txt_amount;
	}
	
}
