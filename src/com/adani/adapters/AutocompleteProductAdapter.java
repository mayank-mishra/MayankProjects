package com.adani.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.adani.models.ProductModel;
import com.demo.demoapp.R;

public class AutocompleteProductAdapter extends BaseAdapter{
	
	private ArrayList<ProductModel> list;
	private LayoutInflater inflater;
	private Context context;
	private boolean isEnable=true;
	private String[] type_data={"EA","KG","CAR"};
	
	public AutocompleteProductAdapter(ArrayList<ProductModel> productList,LayoutInflater inflater,Context context,boolean isEnable) {
		this.list=productList;
		this.inflater=inflater;
		this.context=context;
		this.isEnable=isEnable;
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

	@SuppressLint({ "ViewHolder", "NewApi" })
	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		final ViewHolder holder ;
		//if(view==null){
			holder=new ViewHolder();
			view =inflater.inflate(R.layout.autocomplete_product_adapter_item, null);
			holder.txt_name=(TextView)view.findViewById(R.id.txt_pname);
			holder.txt_amount=(TextView)view.findViewById(R.id.txt_pamount);
			holder.edit_price=(EditText)view.findViewById(R.id.edit_price);
			holder.edit_qty=(EditText)view.findViewById(R.id.edit_qty);
			holder.spinner_type=(Spinner)view.findViewById(R.id.spinner_type);
			
			final CheckBox checkBox=(CheckBox)view.findViewById(R.id.check_product);
			//view.setTag(holder);
		/*}else{
			holder=(ViewHolder) view.getTag();
		}*/
		holder.txt_name.setText(getItem(position).Name);
		holder.edit_price.setText(getItem(position).amount);
		holder.edit_qty.setText(getItem(position).qty);
		//holder.spinner_type.
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, type_data);
	      
	      // Drop down layout style - list view with radio button
	      dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
	      holder.spinner_type.setAdapter(dataAdapter);
	      if(!getItem(position).umo.isEmpty()){
	    	  for(int i=0;i<type_data.length;i++){
	    		  if(getItem(position).umo.equals(type_data[i])){
	    			  holder.spinner_type.setSelection(i);
	    		  }
	    	  }
	      }
		
		if(!getItem(position).qty.isEmpty()){
			checkBox.setSelected(true);
			checkBox.setChecked(true);
		}
		
		if(!isEnable){
			holder.edit_price.setText("Rs "+getItem(position).amount);
			holder.edit_qty.setEnabled(false);
			holder.edit_price.setEnabled(false);
			holder.spinner_type.setEnabled(false);
			checkBox.setEnabled(false);
			checkBox.setChecked(true);
			checkBox.setSelected(true);
		}
		
		holder.spinner_type.setTag(position);
		holder.spinner_type.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				int gpos = (Integer) holder.edit_qty.getTag();
				getItem(gpos).umo= parent.getItemAtPosition(position).toString();
				//getItem(arg2).umo=holder.spinner_type.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		checkBox.setTag(position);
		checkBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int gpos = (Integer) holder.edit_qty.getTag();
				if(checkBox.isChecked()){
					if(getItem(gpos).qty.isEmpty()){
						holder.edit_qty.setText("1");
						checkBox.setChecked(true);
						checkBox.setSelected(true);
					}
				}else{
					holder.edit_qty.setText("0");
					checkBox.setChecked(false);
					checkBox.setSelected(false);
				}
			}
		});
		
		
		
		holder.edit_qty.setTag(position);
		holder.edit_qty.addTextChangedListener(new TextWatcher() {

			int gpos = (Integer) holder.edit_qty.getTag();
			
			@Override
			public void afterTextChanged(Editable s) {
				getItem(gpos).qty = s.toString();
				if(getItem(gpos).qty.isEmpty()){
					checkBox.setSelected(false);
					checkBox.setChecked(false);
				}else{
					checkBox.setSelected(true);
					checkBox.setChecked(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
					int before, int count) {

			}
		});
		
		holder.edit_price.setTag(position);
		holder.edit_price.addTextChangedListener(new TextWatcher() {

			int gpos = (Integer) holder.edit_qty.getTag();
			
			@Override
			public void afterTextChanged(Editable s) {
				getItem(gpos).amount = s.toString();
				/*if(getItem(gpos).amount.isEmpty()){
					checkBox.setSelected(false);
					checkBox.setChecked(false);
				}else{
					checkBox.setSelected(true);
					checkBox.setChecked(true);
				}*/
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
					int before, int count) {

			}
		});

		
		return view;
	}
	
	public ArrayList<ProductModel> getAllItems(){
		return list;
	}
	
	public void refreshAdapter(ArrayList<ProductModel> list){
		if(list!=null)
		this.list=list;
		notifyDataSetChanged();
	}
	
	class ViewHolder{
		TextView txt_name;
		TextView txt_amount;
		EditText edit_qty,edit_price;
		Spinner spinner_type;
		
	}
	
}
