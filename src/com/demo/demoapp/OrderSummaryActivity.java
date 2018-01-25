package com.demo.demoapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adani.adapters.AutocompleteDistributerAdapter;
import com.adani.adapters.AutocompleteProductAdapter;
import com.adani.database.DBFeilds;
import com.adani.models.DistributerModel;
import com.adani.models.OrderResponse;
import com.adani.models.ProductModel;
import com.demo.network.HttpPostData;
import com.demo.network.WebServiceUrls;
import com.demo.utils.AlertDialogCustom;
import com.demo.utils.AppUtils;
import com.google.gson.Gson;

public class OrderSummaryActivity extends BaseActivity {

	ArrayList<DistributerModel> distributerList;
	ArrayList<ProductModel> productList;

	AutocompleteDistributerAdapter autocomplete_adapter;
	AutocompleteProductAdapter productAdapter;
	AutoCompleteTextView txtSearch_dis;
	ListView listviewProduct;
	Button btn_placeOrder;
	TextView txt_distributername, txt_amount;
	String productId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCustomContentView(R.layout.order_summary_activity);

		btn_placeOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doRequest();
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
		txt_title.setText("Order Summary");
		txtSearch_dis = (AutoCompleteTextView) findViewById(R.id.txt_autocomplete);
		listviewProduct = (ListView) main_container.findViewById(R.id.list_product);
		txt_distributername = (TextView) main_container.findViewById(R.id.txt_distributername);
		txt_amount = (TextView) main_container.findViewById(R.id.txt_amount);
		btn_placeOrder = (Button) main_container.findViewById(R.id.btn_done);
		txtSearch_dis.setThreshold(0);

	}

	private void initData() {
		try {
			try {
				int amount = 0;
				txt_distributername.setText(getIntent().getStringExtra("NAME").toString());
				productList = (ArrayList<ProductModel>) getIntent().getSerializableExtra(AppUtils.INTENT_KEY);
				productAdapter = new AutocompleteProductAdapter(productList,baseInflater, this, false);
				listviewProduct.setAdapter(productAdapter);
				for (int i = 0; i < productList.size(); i++) {
					amount += (Integer.parseInt(productList.get(i).qty) * Integer.parseInt(productList.get(i).amount));
				}
				txt_amount.setText("Rs. " + String.valueOf(amount));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doRequest() {
		super.doRequest();
		try {
			if (AppUtils.isOnline(this)) {
				postMethod = new HttpPostData(sfdc_details.instance_url
						+ WebServiceUrls.WS_SYNCSALESORDERS + getJsonData(),
						"", sfdc_details.access_token, this);
				postMethod.execute();
			} else {
				txt_baseError.setVisibility(View.VISIBLE);
				showCustomToast(this, "Please Check Your Internet Connection.");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void parseJsonResponse(String response, int resultCode) {
		try {
			JSONObject obj = new JSONObject(response);	
			if (obj.has("Msg")) {
				if (obj.getString("Msg").equals("Success")) {
					if(obj.has("OrderDetails")){
						String orderdetail=obj.getString("OrderDetails");
						OrderResponse gson=new Gson().fromJson(orderdetail, OrderResponse.class);
						db.insertAndUpdateOrder(gson);
						openAlertDialog("Order Successfully Placed.", "Success!");
					}else{
						openAlertDialog("Something went wrong.", "Error!");
					}
				} else {
					openAlertDialog("Something went wrong.", "Error!");
				}

			} else {
				openAlertDialog(response, "Error!");
			}
		} catch (JSONException e) {
			openAlertDialog("Something went wrong.", "Error!");
			e.printStackTrace();
		}
		super.parseJsonResponse(response, resultCode);
	}

	private String getJsonData() {
		JSONObject mainObj = new JSONObject();
		try {

			JSONArray mainarray = new JSONArray();
			JSONArray productarray = new JSONArray();
			ArrayList<ProductModel> pList = ((AutocompleteProductAdapter) listviewProduct
					.getAdapter()).getAllItems();
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("Date", AppUtils.getStringDate(new Date()));
			jsonObj.put("Distributor",
					AppUtils.NullChecker(getIntent().getStringExtra("ID")));
			for (int i = 0; i < pList.size(); i++) {
				JSONObject Obj = new JSONObject();
				Obj.put("PricePerUnit", pList.get(i).amount);
				Obj.put("Quantity", pList.get(i).qty);
				Obj.put("ProductId", pList.get(i).Id);
				Obj.put("UOM", pList.get(i).umo);
				productarray.put(Obj);
			}
			jsonObj.put("SalesOrderItems", productarray);
			mainarray.put(jsonObj);
			mainObj.put("SalesOrders", mainarray);

			byte[] data = null;
			try {
				data = mainObj.toString().getBytes("UTF-16");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.i("-----cJSON DATA ---", mainObj.toString());
			String base64 = Base64.encodeToString(data, Base64.DEFAULT);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("JsonString", mainarray
					.toString()));
			return AppUtils.getQuery(nameValuePairs).toString();
			// return mainObj.toString();
			/*
			 * try { return URLEncoder.encode(mainObj.toString(), "UTF-8"); }
			 * catch (UnsupportedEncodingException e) { // TODO Auto-generated
			 * catch block e.printStackTrace(); }
			 */
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	private void openAlertDialog(String string, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(string).setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent i = new Intent(OrderSummaryActivity.this,
								OrderListActivity.class);
						i.putExtra("Date", "Success");
						setResult(1, i);
						finish();
					}
				})
		/*
		 * .setNegativeButton("No", new DialogInterface.OnClickListener() {
		 * public void onClick(DialogInterface dialog, int id) { // Action for
		 * 'NO' Button dialog.cancel(); } })
		 */;

		// Creating dialog box
		AlertDialog alert = builder.create();
		// Setting the title manually
		alert.setTitle(title);
		alert.show();
	}

}