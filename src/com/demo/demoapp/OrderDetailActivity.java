package com.demo.demoapp;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.adani.adapters.OrderLIneItemAdapter;
import com.adani.database.DBFeilds;
import com.adani.models.DistributerModel;
import com.adani.models.OrderResponse;
import com.demo.utils.AppUtils;

public class OrderDetailActivity extends BaseActivity{

	ArrayList<OrderResponse> orderResponse;
	OrderLIneItemAdapter adapter;
	String productId="";
	ListView listview_order_items;
	TextView txt_dis_name,txt_order_name,txt_amount;
	double totalAmount=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCustomContentView(R.layout.order_detail);
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
    	txt_title.setText("Order Detail");
    	txt_dis_name=(TextView)main_container.findViewById(R.id.txt_distributer_name);
    	txt_amount=(TextView)main_container.findViewById(R.id.txt_amount);
    	txt_order_name=(TextView)main_container.findViewById(R.id.txt_orderName);
    	listview_order_items=(ListView)main_container.findViewById(R.id.list_product);
    }
	
	private void initData() {
		 try {
			 productId=getIntent().getStringExtra(AppUtils.INTENT_KEY);
			 orderResponse=db.getOrders(" where "+DBFeilds.ORDER_USER_ID+" = '"+user.Id+"' AND "+DBFeilds.ORDER_ID+" = '"+productId+"'");
			 txt_dis_name.setText(AppUtils.NullChecker(orderResponse.get(0).Distributor__c));
			 txt_order_name.setText(AppUtils.NullChecker(orderResponse.get(0).Name));
			 txt_amount.setText("Rs "+String.format( "%.2f", getTotalAmount()));
			 adapter=new OrderLIneItemAdapter(orderResponse.get(0).Sales_Order_Line_Items__r.records, baseInflater, this);
			 listview_order_items.setAdapter(adapter);
		 } catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private double getTotalAmount(){
		double count=0.0;
		try {
			for(int i=0;i<orderResponse.get(0).Sales_Order_Line_Items__r.records.size();i++){
				count+=(Double.parseDouble(orderResponse.get(0).Sales_Order_Line_Items__r.records.get(i).Rate__c)*(Integer.parseInt(orderResponse.get(0).Sales_Order_Line_Items__r.records.get(i).Quantity__c)));
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0.0;
		}
		return count;
	}
}
