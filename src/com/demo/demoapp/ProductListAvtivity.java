package com.demo.demoapp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.adani.adapters.ProductAdapter;
import com.adani.database.DBFeilds;
import com.adani.models.ProductModel;
import com.demo.network.HttpGetMethod;
import com.demo.network.WebServiceUrls;
import com.demo.utils.AlertDialogCustom;
import com.demo.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class ProductListAvtivity extends BaseActivity{
	
	private ListView list_products;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCustomContentView(R.layout.activity_products);
		
		list_products.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg3) {
				Intent i=new Intent(ProductListAvtivity.this,ProductDetailActivity.class);
				i.putExtra(AppUtils.INTENT_KEY, ((ProductAdapter) list_products.getAdapter()).getItem(position).Id);
				startActivity(i);
			}
		});
	}
	
	@Override
	protected void onResume() {
		goneMenu();
		line_products.setVisibility(View.VISIBLE);
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
    	txt_title.setText("Products");
    }
	
	private void initData() {
		
		try {
			ArrayList<ProductModel> productList=db.getProducts(""/*" where "+DBFeilds.PRODUCT_USER_ID+" = '"+user.Id+"'"*/);
			list_products.setAdapter(new ProductAdapter(productList, baseInflater, this));
			if(productList.size()==0){
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
		getMethod=new HttpGetMethod(sfdc_details.instance_url+WebServiceUrls.WS_PRODUCTDETAILS, "", sfdc_details.access_token, this);
		getMethod.execute();
	}
	
	
	@Override
	public void parseJsonResponse(String response, int resultCode) {
		super.parseJsonResponse(response, resultCode);
		try {
			new ParseProductResponse().execute(response);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateUi() {
		super.updateUi();
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try{
					ArrayList<ProductModel> productList=db.getProducts(" where "+DBFeilds.PRODUCT_USER_ID+" = '"+user.Id+"'");
					((ProductAdapter) list_products.getAdapter()).refreshAdapter(productList);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	
	private class ParseProductResponse extends AsyncTask<String, Void, String>{
		ProgressDialog _dialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			_dialog = new ProgressDialog(ProductListAvtivity.this);
			_dialog.setMessage("Please wait...");
			_dialog.setCanceledOnTouchOutside(false);
			_dialog.setCancelable(false);
			_dialog.show();
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			_dialog.dismiss();

			if(result!=null){
				AlertDialogCustom d=new AlertDialogCustom(ProductListAvtivity.this);
				d.setParamenters("Alert", "Something went wrong,Please try again", null, 1);
				d.show();
			}
		}
		@Override
		protected String doInBackground(String... params) {

			try {
				Type listType = new TypeToken<List<ProductModel>>() {}.getType();
				List<ProductModel> gson=new Gson().fromJson(params[0], listType);
				for(ProductModel model:gson){
					db.insertAndUpdateProducts(model);
				}
				updateUi();
			} catch (Exception e) {
				e.printStackTrace();
				return "Error";

			}

			return null;
		}

	}
	
	@Override
	public void onBackPressed() {
		line_products.setVisibility(View.GONE);
		super.onBackPressed();
	}
	
}

