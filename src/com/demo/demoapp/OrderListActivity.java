package com.demo.demoapp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;

import com.adani.adapters.DistributerAdapter;
import com.adani.adapters.OrderAdapter;
import com.adani.adapters.ProductAdapter;
import com.adani.database.DBFeilds;
import com.adani.models.DistributerModel;
import com.adani.models.OrderResponse;
import com.adani.utils.ActivityUtils;
import com.demo.network.HttpGetMethod;
import com.demo.network.WebServiceUrls;
import com.demo.utils.AlertDialogCustom;
import com.demo.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class OrderListActivity extends BaseActivity{
	
	private ListView list_products;
	ArrayList<OrderResponse> distributerList;
	LinearLayout lay_top;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCustomContentView(R.layout.activity_products);
		
		list_products.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				OrderResponse item=(OrderResponse) list_products.getAdapter().getItem(position);
				Intent i=new Intent(OrderListActivity.this,OrderDetailActivity.class);
				i.putExtra(AppUtils.INTENT_KEY, item.Id);
				startActivity(i);
			}
		});
		
		list_products.setOnScrollListener(new OnScrollListener() {
			 
			@Override
			public void onScrollStateChanged(AbsListView view,
					int scrollState) { // TODO Auto-generated method stub
				int threshold = 1;
				int count = list_products.getCount();

				if (scrollState == SCROLL_STATE_IDLE) {
					if (list_products.getLastVisiblePosition() >= count- threshold) {
						// Execute LoadMoreDataTask AsyncTask
						OFFSET=distributerList.size();
						doRequest();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}

		});
		
		btn_done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(OrderListActivity.this, AddOrderActivity.class));
			}
		});
	}
	
	@Override
	protected void onResume() {
		goneMenu();
		line_orders.setVisibility(View.VISIBLE);
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
    	list_products = (ListView) main_container.findViewById(R.id.listview_products);
    	lay_top=(LinearLayout)main_container.findViewById(R.id.lay_top);
    	TextView txt_headname=(TextView)main_container.findViewById(R.id.txt_headname);
    	TextView txt_headprice=(TextView)main_container.findViewById(R.id.txt_headprice);
    	
    	txt_headname.setText("Order Name");
    	btn_done.setText("Add Order");
    	txt_title.setText("Orders");
    	lay_top.setVisibility(View.GONE);
    	img_menu.setVisibility(View.GONE);
    	btn_done.setVisibility(View.VISIBLE);
    	img_menu.setImageResource(R.drawable.plus);
    }
	
	private void initData() {

		try {
			if(list_products!=null){
				distributerList=db.getOrders(" where "+DBFeilds.ORDER_USER_ID+" = '"+user.Id+"' ORDER BY "+DBFeilds.ORDER_NAME+" DESC ");
				list_products.setAdapter(new OrderAdapter(distributerList, baseInflater, this));
				OFFSET=distributerList.size();
				if(distributerList.size()==0){
					doRequest();
				}
			}
		} catch (Exception e) {
			//doRequest();
			e.printStackTrace();
		}
	}
	
	@Override
	public void doRequest() {
		super.doRequest();
		getMethod=new HttpGetMethod(sfdc_details.instance_url+WebServiceUrls.WS_SalesOrderDetails+"offsetLmt="+String.valueOf(OFFSET), "", sfdc_details.access_token, this);
		getMethod.execute();
	}
	
	@Override
	public void parseJsonResponse(String response, int resultCode) {
		super.parseJsonResponse(response, resultCode);
		try {
			
			Type listType = new TypeToken<List<OrderResponse>>() {}.getType();
			List<OrderResponse> gson=new Gson().fromJson(response, listType);
			for(OrderResponse model:gson){
				db.insertAndUpdateOrder(model);
			}
			
			updateUi();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AlertDialogCustom dialog =new AlertDialogCustom(this);
			dialog.setParamenters("Alert!", response, null, 1);
			dialog.show();
		}
	}
	
	@Override
	public void updateUi() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try{
					distributerList=db.getOrders(" where "+DBFeilds.ORDER_USER_ID+" = '"+user.Id+"' ORDER BY "+DBFeilds.ORDER_NAME+" DESC ");
					((OrderAdapter) list_products.getAdapter()).refreshAdapter(distributerList);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		super.updateUi();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		line_orders.setVisibility(View.GONE);
	}
	
}
