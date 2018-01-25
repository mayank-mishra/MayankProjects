package com.demo.demoapp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.adani.adapters.DistributerAdapter;
import com.adani.adapters.ProductAdapter;
import com.adani.database.DBFeilds;
import com.adani.models.DistributerModel;
import com.adani.models.ProductModel;
import com.demo.network.HttpGetMethod;
import com.demo.network.WebServiceUrls;
import com.demo.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class DistributerListActivity extends BaseActivity{
	
	private ListView list_products;
	ArrayList<DistributerModel> distributerList;
	LinearLayout lay_top;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCustomContentView(R.layout.activity_products);
		
		list_products.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg3) {
				Intent i=new Intent(DistributerListActivity.this,DistributerDetailActivity.class);
				i.putExtra(AppUtils.INTENT_KEY, ((DistributerAdapter) list_products.getAdapter()).getItem(position).Id);
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
	}
	
	@Override
	protected void onResume() {
		goneMenu();
		line_distributers.setVisibility(View.VISIBLE);
		super.onResume();
	}
	
	@Override
    public void setCustomContentView(int layout) {
    	main_container.addView(baseInflater.inflate(layout, null));
    	user = db.getUSERPROFILE();
    	findViews();
    	initData();
    }

	private void findViews() {
    	list_products = (ListView) main_container.findViewById(R.id.listview_products);
    	TextView txt_headname=(TextView)main_container.findViewById(R.id.txt_headname);
    	TextView txt_headprice=(TextView)main_container.findViewById(R.id.txt_headprice);
    	lay_top=(LinearLayout)main_container.findViewById(R.id.lay_top);
    	lay_top.setVisibility(View.GONE);
    	txt_headname.setText("distributor Name");
    	txt_title.setText("Distributors");
    }
	
	private void initData() {
		
		 try {
			distributerList=db.getDistributer(" where "+DBFeilds.DISTRIBUTER_USER_ID+" = '"+sfdc_details.user_id+"'");
			list_products.setAdapter(new DistributerAdapter(distributerList, baseInflater, this));
			OFFSET=distributerList.size();
			if(distributerList.size()==0){
				doRequest();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void doRequest() {
		super.doRequest();
		getMethod=new HttpGetMethod(sfdc_details.instance_url+WebServiceUrls.WS_DISTRIBUTORDETAILS+user.Id, "", sfdc_details.access_token, this);
		getMethod.execute();
	}
	
	@Override
	public void parseJsonResponse(String response, int resultCode) {
		super.parseJsonResponse(response, resultCode);
		try {
			Type listType = new TypeToken<List<DistributerModel>>() {}.getType();
			List<DistributerModel> gson=new Gson().fromJson(response, listType);
			for(DistributerModel model:gson){
				db.insertAndUpdateDistributers(model);
			}
			
			updateUi();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			openAlertDialog(this,response,"Error!");
		}
	}
	
	@Override
	public void updateUi() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try{
					distributerList=db.getDistributer(" where "+DBFeilds.DISTRIBUTER_USER_ID+" = '"+user.Id+"'");
					((DistributerAdapter) list_products.getAdapter()).refreshAdapter(distributerList);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		super.updateUi();
	}
	
	@Override
	public void onBackPressed() {
		line_distributers.setVisibility(View.GONE);
		super.onBackPressed();
	}
	
}
