package com.demo.demoapp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.adani.adapters.InvoiceAdapter;
import com.adani.database.DBFeilds;
import com.adani.models.DistributerModel;
import com.adani.models.InvoiceModel;
import com.demo.network.HttpGetMethod;
import com.demo.network.WebServiceUrls;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class InvoiceListActivity extends BaseActivity{
	
	private ListView list_products;
	ArrayList<InvoiceModel> invoiceList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCustomContentView(R.layout.activity_products);
		
		list_products.setOnScrollListener(new OnScrollListener() {
			 
			@Override
			public void onScrollStateChanged(AbsListView view,
					int scrollState) { // TODO Auto-generated method stub
				int threshold = 1;
				int count = list_products.getCount();

				if (scrollState == SCROLL_STATE_IDLE) {
					if (list_products.getLastVisiblePosition() >= count- threshold) {
						// Execute LoadMoreDataTask AsyncTask
						OFFSET=invoiceList.size();
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
		line_invoices.setVisibility(View.VISIBLE);
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
    	TextView txt_headname=(TextView)main_container.findViewById(R.id.txt_headname);
    	TextView txt_headprice=(TextView)main_container.findViewById(R.id.txt_headprice);
    	
    	txt_headname.setText("Invoice Name");
    	txt_title.setText("Invoices");
    }
	
	private void initData() {
		
		 try {
			 invoiceList=db.getInvoice(" where "+DBFeilds.INVIOICE_USER_ID+" = '"+user.Id+"'");
			list_products.setAdapter(new InvoiceAdapter(invoiceList, baseInflater, this));
			OFFSET=invoiceList.size();
			if(invoiceList.size()==0){
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
		getMethod=new HttpGetMethod(sfdc_details.instance_url+WebServiceUrls.WS_INVOICEDETAILS+"offsetLmt="+String.valueOf(OFFSET), "", sfdc_details.access_token, this);
		getMethod.execute();
	}
	
	@Override
	public void parseJsonResponse(String response, int resultCode) {
		super.parseJsonResponse(response, resultCode);
		try {
			Type listType = new TypeToken<List<InvoiceModel>>() {}.getType();
			List<InvoiceModel> gson=new Gson().fromJson(response, listType);
			for(InvoiceModel model:gson){
				db.insertAndUpdateInvoice(model);
			}
			updateUi();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateUi() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try{
					invoiceList=db.getInvoice(" where "+DBFeilds.INVIOICE_USER_ID+" = '"+user.Id+"'");
					((InvoiceAdapter) list_products.getAdapter()).refreshAdapter(invoiceList);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		super.updateUi();
	}
	
	@Override
	public void onBackPressed() {
		line_invoices.setVisibility(View.GONE);
		super.onBackPressed();
	}
	
}
