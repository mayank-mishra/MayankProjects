package com.demo.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class AlertDialogCustom extends AlertDialog.Builder{
  
	Intent startclass;
	String title="",message="";
	Context context;
	boolean isfinish;
	
	
   
	public AlertDialogCustom(Context arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
		this.context = arg0;
		
		
		this.create();
	}
	public AlertDialogCustom(Context ctx){
		super(ctx);
		this.context = ctx;
	}
	public void setParamenters(String title,String msg,Intent intent,int btncount){
		this.title = title;
		this.message = msg;
		this.startclass = intent;
		
		if(btncount == 2){
			twoButton();
		}else{
			singleButton();
		}
	}
	
	public void show(String msg){
		this.setMessage(msg);
		this.show();
	}
	private void singleButton(){
		this.setTitle(title);
		this.setMessage(message);
		
		this.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(startclass !=null){
					
					context.startActivity(startclass);
				}
			}
		});
		
	}
	
	private void twoButton(){
		this.setTitle(title);
		this.setMessage(message);
		
      this.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(startclass !=null){
					context.startActivity(startclass);
					
				}
			}
		});
      this.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}
}
