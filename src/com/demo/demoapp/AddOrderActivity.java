package com.demo.demoapp;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.adani.adapters.AutocompleteDistributerAdapter;
import com.adani.adapters.AutocompleteProductAdapter;
import com.adani.database.DBFeilds;
import com.adani.models.DistributerModel;
import com.adani.models.ProductModel;
import com.demo.utils.AppUtils;

public class AddOrderActivity extends BaseActivity{

	ArrayList<DistributerModel> distributerList;
	ArrayList<ProductModel> productList;
	
	AutocompleteDistributerAdapter autocomplete_adapter;
	AutocompleteProductAdapter productAdapter;
	AutoCompleteTextView txtSearch_dis;
	ListView listviewProduct;
	Button btn_placeOrder;
	String productId="";
	DistributerModel selectItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCustomContentView(R.layout.activity_add_order);
		
		btn_placeOrder.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				try {
					ArrayList<ProductModel> List=new ArrayList<ProductModel>();
					ArrayList<ProductModel> pList=((AutocompleteProductAdapter) listviewProduct.getAdapter()).getAllItems();
					for(int i=0;i<distributerList.size();i++){
						if(distributerList.get(i).Name.trim().equals(txtSearch_dis.getText().toString().trim())){
							selectItem=distributerList.get(i);
						}
					}
					for(int i=0;i<pList.size();i++){
						if(!pList.get(i).qty.isEmpty()){
							List.add(pList.get(i));
						}
					}
					Intent i=new Intent(AddOrderActivity.this,OrderSummaryActivity.class);
					i.putExtra("NAME", selectItem.Name);
					i.putExtra("ID", selectItem.Id);
					i.putExtra(AppUtils.INTENT_KEY, List);
					startActivityForResult(i, 1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		txtSearch_dis.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				distributerList=db.getDistributer(" where "+DBFeilds.DISTRIBUTER_USER_ID+" = '"+user.Id+"' AND "+DBFeilds.DISTRIBUTER_NAME+" like '"+txtSearch_dis.getText().toString().toLowerCase()+"%'");
				 //autocomplete_adapter = new AutocompleteDistributerAdapter(AddOrderActivity.this, distributerList);
				 autocomplete_adapter.refreshAdapter(distributerList);
				 //txtSearch_dis.setAdapter(autocomplete_adapter);
				// txtSearch_dis.requestFocus();
				// txtSearch_dis.showDropDown();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		txtSearch_dis.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				//selectItem=((AutocompleteDistributerAdapter) txtSearch_dis.getAdapter()).getDisItem(position);

			}
		});
		
		txtSearch_dis.setOnTouchListener(new OnTouchListener() {

	        
	        @SuppressLint("ClickableViewAccessibility")
			@Override
	        public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
	            // TODO Auto-generated method stub
	        	txtSearch_dis.showDropDown();
	        	txtSearch_dis.requestFocus();
	            return false;
	        }
	    });
		
		
	}
	
		
	@Override
	protected void onResume() {
		line_products.setVisibility(View.VISIBLE);
		initData();
		super.onResume();
	}
	
	@Override
    public void setCustomContentView(int layout) {
    	main_container.addView(baseInflater.inflate(layout, null));
    	findViews();
    	initData();
    }

	private void findViews() {
    	txt_title.setText("Place Order");
    	txtSearch_dis = (AutoCompleteTextView)main_container. findViewById(R.id.txt_autocomplete);
    	listviewProduct = (ListView)main_container. findViewById(R.id.list_product);
    	btn_placeOrder=(Button)main_container. findViewById(R.id.btn_done);
    	txtSearch_dis.setThreshold(1);
    }
	
	private void initData() {
		 try {
			 distributerList=db.getDistributer(" where "+DBFeilds.DISTRIBUTER_USER_ID+" = '"+user.Id+"'");
			 autocomplete_adapter = new AutocompleteDistributerAdapter(this, distributerList);
			 txtSearch_dis.setAdapter(autocomplete_adapter);
			 productList=db.getProducts(" where "+DBFeilds.PRODUCT_USER_ID+" = '"+user.Id+"'");
			 productAdapter=new AutocompleteProductAdapter(productList, baseInflater, this,true);
			 listviewProduct.setAdapter(productAdapter);
			 
			 //txtSearch_dis.showDropDown();
		 } catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if(arg1==1){
			if(arg2!=null)
			finish();
		}
		super.onActivityResult(arg0, arg1, arg2);
	}
	
	/*@Override
	public void doRequest() {
		super.doRequest();
		if(AppUtils.isOnline(this)){
			postMethod =new HttpPostData(sfdc_details.instance_url+WebServiceUrls.WS_SYNCSALESORDERS, getJsonData(), sfdc_details.access_token, this);
			postMethod.execute();
		}else{
			txt_baseError.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void parseJsonResponse(String response, int resultCode) {
		AlertDialogCustom dialog =new AlertDialogCustom(this);
		dialog.setParamenters("Alert!", response, null, 1);
		dialog.show();
		super.parseJsonResponse(response, resultCode);
	}
	
	private String getJsonData(){
		JSONObject mainObj=new JSONObject();
		try {
			
			JSONArray mainarray=new JSONArray();
			JSONArray productarray=new JSONArray();
			ArrayList<ProductModel> pList=((AutocompleteProductAdapter) listviewProduct.getAdapter()).getAllItems();
			JSONObject jsonObj=new JSONObject();
			jsonObj.put("Date", AppUtils.getStringDate(new Date()));
			for(int i=0;i<pList.size();i++){
				JSONObject Obj=new JSONObject();
				Obj.put("PricePerUnit", "100");
				Obj.put("Quantity", pList.get(i).qty);
				Obj.put("ProductId", pList.get(i).Id);
				Obj.put("UOM", "Each");
				productarray.put(Obj);
			}
			jsonObj.put("SalesOrderItems", productarray);
			mainarray.put(jsonObj);
			mainObj.put("SalesOrders", mainarray);
			return mainObj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
*/
}
