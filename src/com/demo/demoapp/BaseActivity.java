package com.demo.demoapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adani.database.DBHelper;
import com.adani.models.SFDCDetails;
import com.adani.models.UserProfile;
import com.adani.utils.Utils;
import com.demo.network.HttpGetMethod;
import com.demo.network.HttpPostData;
import com.demo.network.IPostResponse;
import com.demo.utils.AppUtils;
import com.demo.utils.Validation;

/**
 * Created by Chandrasekar on 17-May-15.
 */
public class BaseActivity extends AppCompatActivity implements IPostResponse {

	private DrawerLayout drawer_layout;
    public FrameLayout main_container;
    public LayoutInflater baseInflater;
    public LinearLayout action_bar_layout;
    public FrameLayout lay_home,lay_products,lay_distributers,lay_orders,lay_invoices;
    public View line_home,line_products,line_distributers,line_orders,line_invoices;
    public ImageView img_logo,img_menu;
    public TextView txt_title,txtEmail,txt_user_letters,txt_username,txt_baseError;
    public static DBHelper db;
    public static SFDCDetails sfdc_details;
    public static UserProfile user;
    public HttpPostData postMethod;
	public HttpGetMethod getMethod;
	public Button btn_done;
	public int OFFSET=0;
	public String requestType="";
    
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base1);
        
       // toolbar = (Toolbar) findViewById(R.id.toolbar);
        main_container=(FrameLayout)findViewById(R.id.main_container);
        action_bar_layout=(LinearLayout)findViewById(R.id.action_bar_layout);
        drawer_layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        img_logo=(ImageView)findViewById(R.id.img_logo);
        img_menu=(ImageView)findViewById(R.id.img_menu);
        txt_title=(TextView)findViewById(R.id.txt_title);
        txtEmail = (TextView) findViewById(R.id.txt_email);
        txt_user_letters = (TextView) findViewById(R.id.txt_user);
        txt_username = (TextView) findViewById(R.id.txt_username);
        txt_baseError= (TextView) findViewById(R.id.txt_baseError);
        /* All the menu layouts*/
        lay_home=(FrameLayout)findViewById(R.id.lay_home);
        lay_products=(FrameLayout)findViewById(R.id.lay_products);
        lay_distributers=(FrameLayout)findViewById(R.id.lay_distributers);
        lay_orders=(FrameLayout)findViewById(R.id.lay_orders);
        lay_invoices=(FrameLayout)findViewById(R.id.lay_invoices);
        btn_done=(Button)findViewById(R.id.btn_save);
        
        /*All the lines*/
        line_home=(View)findViewById(R.id.line_home);
        line_distributers=(View)findViewById(R.id.line_distributers);
        line_invoices=(View)findViewById(R.id.line_invoices);
        line_products=(View)findViewById(R.id.line_products);
        line_orders=(View)findViewById(R.id.line_orders);
       
        img_logo.setVisibility(View.GONE);       
        baseInflater=getLayoutInflater();
       // setSupportActionBar(toolbar);
       // initNavigationDrawer();
        getTypeface();
        
        if (db == null) {
			db = new DBHelper(this);
			try {
				db.open();
			} catch (Exception e) {

			}
		}
        
        user = db.getUSERPROFILE();
        
        txtEmail.setText(user.Email);
        if (!Validation.NullChecker(user.Name).isEmpty()) {
			txt_user_letters.setText(String.valueOf(user.Name.toUpperCase().charAt(0)));
			txt_username.setText(user.Name);
		}
        
        lay_home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				line_home.setVisibility(View.VISIBLE);
				if (drawer_layout.isDrawerOpen(Gravity.START)) {
					drawer_layout.closeDrawer(Gravity.START);
				}
			
				Intent home = new Intent(BaseActivity.this,HomeActivity.class);
				home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(home);
				
			}
		});
        
        lay_products.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				

				line_products.setVisibility(View.VISIBLE);
				if (drawer_layout.isDrawerOpen(Gravity.START)) {
					drawer_layout.closeDrawer(Gravity.START);
				}
			
				Intent home = new Intent(BaseActivity.this,ProductListAvtivity.class);
				home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(home);
				
			
			}
		});
        
        lay_distributers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				line_distributers.setVisibility(View.VISIBLE);
				if (drawer_layout.isDrawerOpen(Gravity.START)) {
					drawer_layout.closeDrawer(Gravity.START);
				}
			
				Intent home = new Intent(BaseActivity.this,DistributerListActivity.class);
				home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(home);
				
			
				
			}
		});
        
        lay_orders.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				

				line_orders.setVisibility(View.VISIBLE);
				if (drawer_layout.isDrawerOpen(Gravity.START)) {
					drawer_layout.closeDrawer(Gravity.START);
				}
			
				Intent home = new Intent(BaseActivity.this,OrderListActivity.class);
				home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(home);
				
			
			}
		});
        
        lay_invoices.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				

				line_invoices.setVisibility(View.VISIBLE);
				if (drawer_layout.isDrawerOpen(Gravity.START)) {
					drawer_layout.closeDrawer(Gravity.START);
				}
			
				Intent home = new Intent(BaseActivity.this,InvoiceListActivity.class);
				home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(home);
				
			
			}
		});
        
        action_bar_layout.setOnClickListener(new OnClickListener() {

			@SuppressLint("InlinedApi")
			@Override
			public void onClick(View v) {

				if (drawer_layout.isDrawerOpen(Gravity.START)) {
					drawer_layout.closeDrawer(Gravity.START);
				} else {
					drawer_layout.openDrawer(Gravity.START);
				}

				//Log.i("-----------------Action Bar Click----------", ":");
			}
		});
    }
    
    
    @Override
    protected void onResume() {
    	if(!AppUtils.isOnline(this)){
    		txt_baseError.setVisibility(View.VISIBLE);
    	}
    	super.onResume();
    }

    public Typeface getTypeface(){
        return Utils.getTypeface(this);
    }

    public void initNavigationDrawer() {/*
    	drawer_layout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawer_layout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawer_layout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    */}

	@Override
	public void doRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parseJsonResponse(String response, int resultCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertDB() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCustomContentView(int layout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUi() {
		// TODO Auto-generated method stub
		
	}
	
	public void goneMenu(){
		line_orders.setVisibility(View.GONE);
        line_distributers.setVisibility(View.GONE);
        line_home.setVisibility(View.GONE);
        line_products.setVisibility(View.GONE);
        line_invoices.setVisibility(View.GONE);
	}
	
	public static void showCustomToast(Context context,String message){
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View layout = inflater.inflate(R.layout.ml_custom_toast,null);
		ImageView image = (ImageView) layout.findViewById(R.id.img_toastImg);
		TextView text = (TextView) layout.findViewById(R.id.txt_tostText);
		text.setText(message);
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.BOTTOM, 0, 50);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}
	
	protected void openAlertDialog(Context context,String string, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(string).setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
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
