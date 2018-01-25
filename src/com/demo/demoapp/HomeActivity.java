package com.demo.demoapp;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.adani.database.DBHelper;
import com.adani.models.DashboardModel;
import com.adani.models.SFDCDetails;
import com.adani.models.UserProfile;
import com.adani.utils.ActivityUtils;
import com.adani.utils.Constants;
import com.adani.utils.SharedPrefsUtils;
import com.demo.network.HttpGetMethod;
import com.demo.network.HttpPostData;
import com.demo.network.WebServiceUrls;
import com.demo.utils.AlertDialogCustom;
import com.demo.utils.AppUtils;
import com.demo.utils.Validation;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by chandrasekar on 21/10/16.
 */

public class HomeActivity extends BaseActivity {
    private ListView listview;
    private ArrayList<DashboardModel> dashboardList;
    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomContentView(R.layout.activity_home);
        if(getIntent().getSerializableExtra(AppUtils.INTENT_KEY)!=null){
        	sfdc_details = (SFDCDetails) this.getIntent().getSerializableExtra(AppUtils.INTENT_KEY);
        }
		if (BaseActivity.db == null) {
			BaseActivity.db = new DBHelper(this);
			try {
				BaseActivity.db.open();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		BaseActivity.db.Insert_Update_SFDC(sfdc_details);
		sfdc_details = db.getSFDCDETAILS();
		//if(!db.isUSEREXISTS(sfdc_details.user_id)){
			requestType=WebServiceUrls.WS_USERDETAILS;
			doRequest();
		/*}else{
			requestType=WebServiceUrls.WS_CountDetails;
	    	doRequest();
		}*/
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2==0){
					startActivity(new Intent(HomeActivity.this,ProductListAvtivity.class));
				}else if(arg2==1){
					startActivity(new Intent(HomeActivity.this,DistributerListActivity.class));
				}else if(arg2==2){
					startActivity(new Intent(HomeActivity.this,OrderListActivity.class));
				}else if(arg2==3){
					startActivity(new Intent(HomeActivity.this,InvoiceListActivity.class));
				}else if(arg2==1){
					
				}
				
			}
		});
    }
    
    protected void onResume(){
    	goneMenu();
		line_home.setVisibility(View.VISIBLE);
		if(sfdc_details!=null){
			requestType=WebServiceUrls.WS_USERDETAILS;
			doRequest();
		}
		super.onResume();
	}
    
    @Override
    public void setCustomContentView(int layout) {
    	main_container.addView(baseInflater.inflate(layout, null));
    	findViews();
    	
    }

    private void findViews() {
    	img_logo.setVisibility(View.VISIBLE);
    	listview = (ListView) main_container.findViewById(R.id.listview);
    }
    
    @Override
    public void doRequest() {
    	if(AppUtils.isOnline(this)){
    	if(requestType.equals(WebServiceUrls.WS_USERDETAILS)){
    		getMethod = new HttpGetMethod(sfdc_details.instance_url+WebServiceUrls.WS_USERDETAILS+sfdc_details.user_id, "", sfdc_details.access_token,this);
    		getMethod.execute();
    	}else{
    		getMethod = new HttpGetMethod(sfdc_details.instance_url+WebServiceUrls.WS_CountDetails+sfdc_details.user_id, "", sfdc_details.access_token,this);
    		getMethod.execute();
    	}
    	
    	}else{
    		txt_baseError.setVisibility(View.VISIBLE);
			showCustomToast(this,"Please Check Your Internet Connection.");
    	}// TODO Auto-generated method stub
    	super.doRequest();
    }
    
    @Override
    public void parseJsonResponse(String response, int resultCode) {
    	try {
			if(requestType.equals(WebServiceUrls.WS_USERDETAILS)){
				UserProfile userdetail =new Gson().fromJson(response, UserProfile.class);
				db.Insert_Update_USER(userdetail);				 
				requestType=WebServiceUrls.WS_CountDetails;
				doRequest();
			}else if(requestType.equals(WebServiceUrls.WS_CountDetails)){
				//String jsonStr=response.split("=")[1];
				final DashboardModel dashboard=new Gson().fromJson(response, DashboardModel.class);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						initDashboardData(dashboard);
					}
				});
			}
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AlertDialogCustom dialog =new AlertDialogCustom(this);
			dialog.setParamenters("Error!", response, null, 1);
			dialog.show();
		}
    	super.parseJsonResponse(response, resultCode);
    }
    
    private void initDashboardData(DashboardModel dashboard){
    	
    	user = db.getUSERPROFILE();
		txtEmail.setText(user.Email);
        if (!Validation.NullChecker(user.Name).isEmpty()) {
			txt_user_letters.setText(String.valueOf(user.Name.toUpperCase().charAt(0)));
			txt_username.setText(user.Name);
		}
    	
    	dashboardList=new ArrayList<DashboardModel>();
    	dashboardList.add(new DashboardModel("PRODUCTS",dashboard.ProductsCount));
    	dashboardList.add(new DashboardModel("DISTRIBUTORS", dashboard.DistributorsCount));
    	dashboardList.add(new DashboardModel("ORDERS", dashboard.SalesOrdersCount));
    	dashboardList.add(new DashboardModel("INVOICES", dashboard.InvoicesCount));
    	if(adapter!=null){
    		adapter.notifyDataSetChanged();
    	}else{
    		adapter=new MyAdapter();
    		listview.setAdapter(adapter);
    	}
    }
    
    private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return dashboardList.size();
		}

		@Override
		public DashboardModel getItem(int position) {
			return dashboardList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View view, ViewGroup viewGroup) {
			
			view =baseInflater.inflate(R.layout.home_item, null);
			TextView txt_name=(TextView)view.findViewById(R.id.txt_name);
			TextView txt_number=(TextView)view.findViewById(R.id.txt_number);
			
			txt_name.setText(dashboardList.get(position).name);
			txt_number.setText(dashboardList.get(position).count);
			return view;
		}
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                SharedPrefsUtils.putBoolean(Constants.SHARED_PREFS.KEY_FLAG_MAINSCREEN, false, HomeActivity.this);

                ActivityUtils.launchActivity(HomeActivity.this, SplashActivity.class, true);

                return true;


        }

        return super.onOptionsItemSelected(item); // important line
    }
    
    @Override
	public void onBackPressed() {
		line_home.setVisibility(View.GONE);
		super.onBackPressed();
	}
	
}
