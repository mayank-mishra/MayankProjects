package com.demo.demoapp;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.adani.database.DBFeilds;
import com.adani.models.DistributerModel;
import com.demo.utils.AppUtils;

public class DistributerDetailActivity extends BaseActivity{

	ArrayList<DistributerModel> product;
	String productId="";
	Button btn_done;
	TextView txt_dis_name,txt_dis_tinNo,txt_dis_contact_person,txt_dis_address,txt_dis_contact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCustomContentView(R.layout.distributor_detail);
		
		btn_done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(DistributerDetailActivity.this,OrderListActivity.class));
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
    	txt_title.setText("Distributor Detail");
    	txt_dis_name=(TextView)main_container.findViewById(R.id.txt_dis_name);
    	txt_dis_address=(TextView)main_container.findViewById(R.id.txt_dis_address);
    	txt_dis_tinNo=(TextView)main_container.findViewById(R.id.txt_dis_tinno);
    	txt_dis_contact_person=(TextView)main_container.findViewById(R.id.txt_dis_contact_person);
    	txt_dis_contact=(TextView)main_container.findViewById(R.id.txt_dis_number);
    	btn_done=(Button)main_container.findViewById(R.id.btn_done);
    }
	
	private void initData() {
		 try {
			 productId=getIntent().getStringExtra(AppUtils.INTENT_KEY);
			 product=db.getDistributer(" where "+DBFeilds.DISTRIBUTER_USER_ID+" = '"+user.Id+"' AND "+DBFeilds.DISTRIBUTER_ID+" = '"+productId+"'");
			 txt_dis_name.setText(AppUtils.NullChecker(product.get(0).Name));
			 /*txt_dis_address.setText(AppUtils.NullChecker(product.get(0).));
			 txt_dis_tinNo.setText(AppUtils.NullChecker(product.get(0).Name));
			 txt_dis_contact_person.setText(AppUtils.NullChecker(product.get(0).));
			 txt_dis_contact.setText(AppUtils.NullChecker(product.get(0).Name));*/
		 } catch (Exception e) {
			e.printStackTrace();
		}
	}
}
