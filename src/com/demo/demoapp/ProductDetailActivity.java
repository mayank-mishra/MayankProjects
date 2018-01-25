package com.demo.demoapp;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adani.database.DBFeilds;
import com.adani.models.ProductModel;
import com.demo.utils.AppUtils;

public class ProductDetailActivity extends BaseActivity{

	ArrayList<ProductModel> product;
	String productId="";
	ImageView img_product;
	TextView txt_name,txt_desc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCustomContentView(R.layout.activity_product_detail);
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
    	txt_title.setText("Product Detail");
    	txt_name=(TextView)main_container.findViewById(R.id.txt_product_name);
    	txt_desc=(TextView)main_container.findViewById(R.id.txt_product_desc);
    	img_product=(ImageView)main_container.findViewById(R.id.img_product);
    }
	
	private void initData() {
		 try {
			 productId=getIntent().getStringExtra(AppUtils.INTENT_KEY);
			 product=db.getProducts(" where "+DBFeilds.PRODUCT_USER_ID+" = '"+user.Id+"' AND "+DBFeilds.PRODUCT_ID+" = '"+productId+"'");
			 txt_name.setText(AppUtils.NullChecker(product.get(0).Name));
			 txt_desc.setText(AppUtils.NullChecker(product.get(0).Product_Type__c));
		 } catch (Exception e) {
			e.printStackTrace();
		}
	}
}
