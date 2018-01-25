package com.adani.database;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.adani.models.DistributerModel;
import com.adani.models.InvoiceModel;
import com.adani.models.OrderLineItemModel;
import com.adani.models.OrderModel;
import com.adani.models.OrderResponse;
import com.adani.models.ProductModel;
import com.adani.models.SFDCDetails;
import com.adani.models.UserProfile;
import com.demo.demoapp.BaseActivity;
import com.demo.utils.AppUtils;

public class DBHelper {
	public  final Context context;
	private DatabaseHelper dataHelper;
	private SQLiteDatabase db;
	public static String DB_PATH = "data/data/com.adani/databases/";
	public static String DATABASE_NAME = "Adani";
	public static int DATABASE_VERSION = 1;
	public ContentValues contentvalues;
	
	

	
	private static final String CREATE_TABLE_SFDC_DETAILS = "CREATE TABLE IF NOT EXISTS "
			+ DBFeilds.TABLE_SFDC_DETAILS
			+ "(_id integer primary key autoincrement,"
			+ DBFeilds.INSTANCE_URL
			+ " TEXT,"
			+ DBFeilds.ACCESS_TOKEN
			+ " TEXT,"
			+ DBFeilds.USER_ID
			+ " TEXT,"
			+ DBFeilds.REFRESH_TOKEN
			+ " TEXT" + ")";
	
	
	private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "
			+ DBFeilds.TABLE_USER
			+ "(_id integer primary key autoincrement, "
			+ DBFeilds.ID+ " TEXT,"
			+ DBFeilds.EMAIL+ " TEXT,"
			+ DBFeilds.DIVISION+ " TEXT,"
			+ DBFeilds.COMPANYNAME+ " TEXT,"
			+ DBFeilds.NAME+ " TEXT)";
	
	private static final String CREATE_TABLE_PRODUCT = "CREATE TABLE IF NOT EXISTS "
			+ DBFeilds.TABLE_PRODUCTS
			+ "(_id integer primary key autoincrement, "
			+ DBFeilds.PRODUCT_ID+ " TEXT,"
			+ DBFeilds.PRODUCT_USER_ID+ " TEXT,"
			+ DBFeilds.PRODUCT_Brand_Code+ " TEXT,"
			+ DBFeilds.PRODUCT_Brand_Name__c+ " TEXT,"
			+ DBFeilds.PRODUCT_Category__c+ " TEXT,"
			+ DBFeilds.PRODUCT_Division_Code__c+ " TEXT,"
			+ DBFeilds.PRODUCT_Division_Name__c+ " TEXT,"
			+ DBFeilds.PRODUCT_Name+ " TEXT,"
			+ DBFeilds.PRODUCT_Pack_Size__c+ " TEXT,"
			+ DBFeilds.PRODUCT_Process__c+ " TEXT,"
			+ DBFeilds.PRODUCT_Product_Code__c+ " TEXT,"
			+ DBFeilds.PRODUCT_Product_Type__c+ " TEXT)";

		
	private static final String CREATE_TABLE_DISTRIBUTER = "CREATE TABLE IF NOT EXISTS "
			+ DBFeilds.TABLE_DISTRIBUTER
			+ "(_id integer primary key autoincrement, "
			+ DBFeilds.DISTRIBUTER_ID+ " TEXT,"
			+ DBFeilds.DISTRIBUTER_USER_ID+ " TEXT,"
			+ DBFeilds.DISTRIBUTER_CODE__C+ " TEXT,"
			+ DBFeilds.DISTRIBUTER_NAME+ " TEXT,"
			+ DBFeilds.DISTRIBUTER_TYPE__C+ " TEXT)";
	
	private static final String CREATE_TABLE_INVOICE = "CREATE TABLE IF NOT EXISTS "
			+ DBFeilds.TABLE_INVOICE
			+ "(_id integer primary key autoincrement, "
			+ DBFeilds.INVIOICE_ID+ " TEXT,"
			+ DBFeilds.INVIOICE_USER_ID+ " TEXT,"
			+ DBFeilds.INVIOICE_Distributor__c+ " TEXT,"
			+ DBFeilds.INVIOICE_Due_Amount__c+ " TEXT,"
			+ DBFeilds.INVIOICE_Invoice_Amount__c+ " TEXT,"
			+ DBFeilds.INVIOICE_Invoice_Number__c+ " TEXT,"
			+ DBFeilds.INVIOICE_Invoice_Date__c+ " DATE ,"
			+ DBFeilds.INVIOICE_Name+ " TEXT)";
	
	private static final String CREATE_TABLE_ORDER = "CREATE TABLE IF NOT EXISTS "
			+ DBFeilds.TABLE_ORDER
			+ "(_id integer primary key autoincrement, "
			+ DBFeilds.ORDER_ID+ " TEXT,"
			+ DBFeilds.ORDER_No_of_Sales_Line_Items__c+ " TEXT,"
			+ DBFeilds.ORDER_RecordType_Name+ " TEXT,"
			+ DBFeilds.ORDER_RecordTypeId+ " TEXT,"
			+ DBFeilds.ORDER_USER_ID+ " TEXT,"
			+ DBFeilds.ORDER_NAME+ " TEXT,"
			+ DBFeilds.ORDER_Distributer_c+ " TEXT,"
			+ DBFeilds.ORDER_Type__c+ " TEXT)";
	
	
	private static final String CREATE_TABLE_ORDER_ITEM = "CREATE TABLE IF NOT EXISTS "
			+ DBFeilds.TABLE_ORDER_ITEM
			+ "(_id integer primary key autoincrement, "
			+DBFeilds.ORDER_ITEM_ID+ " TEXT,"
			+ DBFeilds.ORDER_ITEM_Brand_Name__c+ " TEXT,"
			+ DBFeilds.ORDER_ITEM_Name+ " TEXT,"
			+ DBFeilds.ORDER_ITEM_Product_Name__c+ " TEXT,"
			+ DBFeilds.ORDER_ITEM_Quantity__c+ " TEXT,"
			+ DBFeilds.ORDER_ITEM_Rate__c+ " TEXT,"
			+ DBFeilds.ORDER_ITEM_Sales_Order__c+ " TEXT,"
			+ DBFeilds.ORDER_ITEM_UOM__c+ " TEXT)";

	public DBHelper(Context ctx) {
		this.context = ctx;
		dataHelper = new DatabaseHelper(context);
	}


	private static class DatabaseHelper extends SQLiteOpenHelper {
		Context context;
		SQLiteDatabase db;
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
		}

		/**
		 * This method is used to check whether the database already exist in
		 * device.
		 * 
		 * @return
		 */
		public boolean checkDataBase() {

			File f = new File(DB_PATH + DATABASE_NAME);
			return f.exists();
		}

		public void onCreate(SQLiteDatabase db) {

			try {
				this.db = db;
				
				db.execSQL(CREATE_TABLE_SFDC_DETAILS);
				db.execSQL(CREATE_TABLE_USER);
				db.execSQL(CREATE_TABLE_PRODUCT);
				db.execSQL(CREATE_TABLE_DISTRIBUTER);
				db.execSQL(CREATE_TABLE_INVOICE);
				db.execSQL(CREATE_TABLE_ORDER);
				db.execSQL(CREATE_TABLE_ORDER_ITEM);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			this.db = db;
			if (oldVersion < newVersion) {


				//matchleads tables
				
			}

		}
	}
	
	private static boolean isColumnExists(SQLiteDatabase dbnew,
			String columname, String tablename) {
		boolean isclumexists = false;
		Cursor c = null;
		try {
			c = dbnew.rawQuery("select * from " + tablename + " limit 1", null);

			if (c.getColumnIndex(columname) != -1) {
				isclumexists = true;
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return isclumexists;
	}
	public DBHelper open() throws SQLException {
		try {
			boolean isExist = dataHelper.checkDataBase();

			if (isExist == false) {
				db = dataHelper.getWritableDatabase();
				/*
				 * if (db.isOpen()) { db.close(); }
				 */
			}
			db = dataHelper.getWritableDatabase();
		} catch (Exception e) {
		}
		return this;
	}

	public boolean isDBExist() {

		return dataHelper.checkDataBase();
	}

	public void close() {
		db.close();
		return;
	}

	public boolean isOpen() {
		return db.isOpen();
	}
	
	private boolean isRecordExist(String tableName,String whereClause){
		boolean isExist=false;
		Cursor c=db.rawQuery("SELECT * FROM "+tableName+" "+whereClause, null);
		if(c.getCount()>0)
			isExist=true;
		else
			isExist=false;
		c.close();
		return isExist;
	}

	public void Insert_Update_SFDC(SFDCDetails sfdc_details) {
		try {
			ContentValues values = new ContentValues();
			values.put(DBFeilds.INSTANCE_URL, sfdc_details.instance_url);
			values.put(DBFeilds.USER_ID, sfdc_details.user_id);
			values.put(DBFeilds.ACCESS_TOKEN, sfdc_details.access_token);
			values.put(DBFeilds.REFRESH_TOKEN, sfdc_details.refresh_token);
			if (isUSEREXISTS(sfdc_details.user_id)) {
				db.update(DBFeilds.TABLE_SFDC_DETAILS, values, DBFeilds.USER_ID + "='"
						+ sfdc_details.user_id + "'", null);
			} else {
				db.insert(DBFeilds.TABLE_SFDC_DETAILS, null, values);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public SFDCDetails getSFDCDETAILS() {
		SFDCDetails sfdc = new SFDCDetails();

		try {
			Cursor c = db.rawQuery("select * from " + DBFeilds.TABLE_SFDC_DETAILS, null);
			if (c != null) {
				if (c.getCount() != 0) {
					c.moveToFirst();
					sfdc.instance_url = c.getString(c
							.getColumnIndex(DBFeilds.INSTANCE_URL));
					sfdc.user_id = c.getString(c.getColumnIndex(DBFeilds.USER_ID));
					sfdc.access_token = c.getString(c
							.getColumnIndex(DBFeilds.ACCESS_TOKEN));
					sfdc.refresh_token = c.getString(c
							.getColumnIndex(DBFeilds.REFRESH_TOKEN));
				}
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sfdc;
	}

	public boolean isUSEREXISTS(String user_id) {
		Cursor c = db.rawQuery("select * from " + DBFeilds.TABLE_USER + " where "
				+ DBFeilds.ID + " = '" + user_id + "'", null);
		if (c == null) {
			return false;
		} else if (c.getCount() != 0) {
			c.close();
			return true;
		}
		c.close();
		return false;
	}
	
	public void Insert_Update_USER(UserProfile user_profile) {
		ContentValues values = new ContentValues();
		values.put(DBFeilds.ID, user_profile.Id);
		values.put(DBFeilds.NAME, user_profile.Name);
		values.put(DBFeilds.DIVISION, user_profile.Division);
		values.put(DBFeilds.COMPANYNAME, user_profile.CompanyName);
		values.put(DBFeilds.EMAIL, user_profile.Email);
		
		boolean isExists = isUSEREXISTS(user_profile.Id);
		if (isExists) {
			db.update(DBFeilds.TABLE_USER, values, DBFeilds.ID + " = '"+ user_profile.Id + "'", null);
		} else {
			db.insert(DBFeilds.TABLE_USER, null, values);
		}
	}
	
	public UserProfile getUSERPROFILE() {
		UserProfile user_profile = new UserProfile();
		Cursor c = db
				.rawQuery("select * from " + DBFeilds.TABLE_USER + " limit 1", null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			user_profile.Id = c.getString(c.getColumnIndex(DBFeilds.ID));
			user_profile.Email = c.getString(c.getColumnIndex(DBFeilds.EMAIL));
			user_profile.Name = c.getString(c.getColumnIndex(DBFeilds.NAME));
			user_profile.CompanyName = c.getString(c.getColumnIndex(DBFeilds.COMPANYNAME));
			user_profile.Division = c.getString(c.getColumnIndex(DBFeilds.DIVISION));
						
			c.close();
		}
		return user_profile;
	}
	
	public void insertAndUpdateProducts(ProductModel product){
		contentvalues=new ContentValues();
		contentvalues.put(DBFeilds.PRODUCT_Brand_Code, product.Brand_Code__c);
		contentvalues.put(DBFeilds.PRODUCT_Brand_Name__c, product.Brand_Name__c);
		contentvalues.put(DBFeilds.PRODUCT_Category__c, product.Category__c);
		contentvalues.put(DBFeilds.PRODUCT_Division_Code__c, product.Division_Code__c);
		contentvalues.put(DBFeilds.PRODUCT_Division_Name__c, product.Division_Name__c);
		contentvalues.put(DBFeilds.PRODUCT_ID, product.Id);
		contentvalues.put(DBFeilds.PRODUCT_Name, product.Name);
		contentvalues.put(DBFeilds.PRODUCT_Pack_Size__c, product.Pack_Size__c);
		contentvalues.put(DBFeilds.PRODUCT_Process__c, product.Process__c);
		contentvalues.put(DBFeilds.PRODUCT_Product_Code__c, product.Product_Code__c);
		contentvalues.put(DBFeilds.PRODUCT_USER_ID, BaseActivity.user.Id);
		contentvalues.put(DBFeilds.PRODUCT_Product_Type__c, product.Product_Type__c);
		
		String where=DBFeilds.PRODUCT_ID+" = '"+product.Id+"'  AND "+DBFeilds.PRODUCT_USER_ID+" = '"+BaseActivity.user.Id+"'";
		if(!isRecordExist(DBFeilds.TABLE_PRODUCTS, " where "+where)){
			db.insert(DBFeilds.TABLE_PRODUCTS, null, contentvalues);
		}else{
			db.update(DBFeilds.TABLE_PRODUCTS, contentvalues, where, null);
		}
	}
	
	public ArrayList<ProductModel> getProducts(String where){
		ArrayList<ProductModel> productList=new ArrayList<ProductModel>();
		Cursor c=db.rawQuery("SELECT * FROM "+DBFeilds.TABLE_PRODUCTS+" "+where, null);
		for(int i=0;i<c.getCount();i++){
			c.moveToPosition(i);
			ProductModel product =new ProductModel();
			product.Brand_Code__c=c.getString(c.getColumnIndex(DBFeilds.PRODUCT_Brand_Code));
			product.Brand_Name__c=c.getString(c.getColumnIndex(DBFeilds.PRODUCT_Brand_Name__c));
			product.Category__c=c.getString(c.getColumnIndex(DBFeilds.PRODUCT_Category__c));
			product.Division_Code__c=c.getString(c.getColumnIndex(DBFeilds.PRODUCT_Division_Code__c));
			product.Division_Name__c=c.getString(c.getColumnIndex(DBFeilds.PRODUCT_Division_Name__c));
			product.Id=c.getString(c.getColumnIndex(DBFeilds.PRODUCT_ID));
			product.Name=c.getString(c.getColumnIndex(DBFeilds.PRODUCT_Name));
			product.Pack_Size__c=c.getString(c.getColumnIndex(DBFeilds.PRODUCT_Pack_Size__c));
			product.Process__c=c.getString(c.getColumnIndex(DBFeilds.PRODUCT_Process__c));
			product.Product_Code__c=c.getString(c.getColumnIndex(DBFeilds.PRODUCT_Product_Code__c));
			product.Product_Type__c=c.getString(c.getColumnIndex(DBFeilds.PRODUCT_Product_Type__c));
			
			productList.add(product);			
		}
		return productList;
	}
	
	public void insertAndUpdateDistributers( DistributerModel distributer){
		contentvalues=new ContentValues();
		contentvalues.put(DBFeilds.DISTRIBUTER_ID, distributer.Id);
		contentvalues.put(DBFeilds.DISTRIBUTER_NAME, distributer.Name);
		contentvalues.put(DBFeilds.DISTRIBUTER_CODE__C, distributer.Distributor_Code__c);
		contentvalues.put(DBFeilds.DISTRIBUTER_TYPE__C, distributer.Distributor_Type__c);
		contentvalues.put(DBFeilds.DISTRIBUTER_USER_ID, BaseActivity.user.Id);
				
		String where=DBFeilds.DISTRIBUTER_ID+" = '"+distributer.Id+"' AND "+DBFeilds.DISTRIBUTER_USER_ID+" = '"+BaseActivity.user.Id+"'";
		if(!isRecordExist(DBFeilds.TABLE_DISTRIBUTER, " where "+where)){
			db.insert(DBFeilds.TABLE_DISTRIBUTER, null, contentvalues);
		}else{
			db.update(DBFeilds.TABLE_DISTRIBUTER, contentvalues, where, null);
		}
	}
	
	public ArrayList<DistributerModel> getDistributer(String where){
		ArrayList<DistributerModel> distributerList=new ArrayList<DistributerModel>();
		Cursor c=db.rawQuery("SELECT * FROM "+DBFeilds.TABLE_DISTRIBUTER+" "+where, null);
		for(int i=0;i<c.getCount();i++){
			c.moveToPosition(i);
			DistributerModel distributer =new DistributerModel();
			distributer.Id=c.getString(c.getColumnIndex(DBFeilds.DISTRIBUTER_ID));
			distributer.Name=c.getString(c.getColumnIndex(DBFeilds.DISTRIBUTER_NAME));
			distributer.Distributor_Code__c=c.getString(c.getColumnIndex(DBFeilds.DISTRIBUTER_CODE__C));
			distributer.Distributor_Type__c=c.getString(c.getColumnIndex(DBFeilds.DISTRIBUTER_TYPE__C));
			distributerList.add(distributer);			
		}
		return distributerList;
	}
	
	public void insertAndUpdateInvoice( InvoiceModel invoice){
		contentvalues=new ContentValues();
		contentvalues.put(DBFeilds.INVIOICE_ID, invoice.Id);
		contentvalues.put(DBFeilds.INVIOICE_Name, invoice.Name);
		contentvalues.put(DBFeilds.INVIOICE_Distributor__c, invoice.Distributor__c);
		contentvalues.put(DBFeilds.INVIOICE_Due_Amount__c, invoice.Due_Amount__c);
		contentvalues.put(DBFeilds.INVIOICE_Invoice_Amount__c, invoice.Invoice_Amount__c);
		contentvalues.put(DBFeilds.INVIOICE_Invoice_Date__c, invoice.Invoice_Date__c);
		contentvalues.put(DBFeilds.INVIOICE_Invoice_Number__c, invoice.Invoice_Number__c);
		contentvalues.put(DBFeilds.INVIOICE_USER_ID, BaseActivity.user.Id);
				
		String where=DBFeilds.INVIOICE_ID+" = '"+invoice.Id+"' AND "+DBFeilds.INVIOICE_USER_ID+" = '"+BaseActivity.user.Id+"'";
		if(!isRecordExist(DBFeilds.TABLE_INVOICE, " where "+where)){
			db.insert(DBFeilds.TABLE_INVOICE, null, contentvalues);
		}else{
			db.update(DBFeilds.TABLE_INVOICE, contentvalues, where, null);
		}
	}
	
	public ArrayList<InvoiceModel> getInvoice(String where){
		ArrayList<InvoiceModel> invoiceList=new ArrayList<InvoiceModel>();
		Cursor c=db.rawQuery("SELECT * FROM "+DBFeilds.TABLE_INVOICE+" "+where, null);
		for(int i=0;i<c.getCount();i++){
			c.moveToPosition(i);
			InvoiceModel invoice =new InvoiceModel();
			invoice.Id=c.getString(c.getColumnIndex(DBFeilds.INVIOICE_ID));
			invoice.Name=c.getString(c.getColumnIndex(DBFeilds.INVIOICE_Name));
			invoice.Distributor__c=c.getString(c.getColumnIndex(DBFeilds.INVIOICE_Distributor__c));
			invoice.Due_Amount__c=c.getString(c.getColumnIndex(DBFeilds.INVIOICE_Due_Amount__c));
			invoice.Invoice_Amount__c=c.getString(c.getColumnIndex(DBFeilds.INVIOICE_Invoice_Amount__c));
			invoice.Invoice_Date__c=c.getString(c.getColumnIndex(DBFeilds.INVIOICE_Invoice_Date__c));
			invoice.Invoice_Number__c=c.getString(c.getColumnIndex(DBFeilds.INVIOICE_Invoice_Number__c));
			invoiceList.add(invoice);			
		}
		return invoiceList;
	}
	
	
	
	public void insertAndUpdateOrder( OrderResponse invoice){
		contentvalues=new ContentValues();
		contentvalues.put(DBFeilds.ORDER_ID, invoice.Id);
		contentvalues.put(DBFeilds.ORDER_No_of_Sales_Line_Items__c, invoice.No_of_Sales_Line_Items__c);
		contentvalues.put(DBFeilds.ORDER_RecordType_Name, invoice.RecordType.Name);
		contentvalues.put(DBFeilds.ORDER_RecordTypeId, invoice.RecordTypeId);
		contentvalues.put(DBFeilds.ORDER_Type__c, "");
		contentvalues.put(DBFeilds.ORDER_Distributer_c, invoice.Distributor__r.Name);
		contentvalues.put(DBFeilds.ORDER_NAME, invoice.Name);
		contentvalues.put(DBFeilds.ORDER_USER_ID, BaseActivity.user.Id);
				
		String where=DBFeilds.ORDER_ID+" = '"+invoice.Id+"' AND "+DBFeilds.ORDER_USER_ID+" = '"+BaseActivity.user.Id+"'";
		if(!isRecordExist(DBFeilds.TABLE_ORDER, " where "+where)){
			db.insert(DBFeilds.TABLE_ORDER, null, contentvalues);
		}else{
			db.update(DBFeilds.TABLE_ORDER, contentvalues, where, null);
		}
		
		for(OrderModel model:invoice.Sales_Order_Line_Items__r.records){
			insertAndUpdateOrderItem(model);
		}
	}
	
	public void insertAndUpdateOrderItem( OrderModel invoice){
		contentvalues=new ContentValues();
		contentvalues.put(DBFeilds.ORDER_ITEM_ID, invoice.Id);
		contentvalues.put(DBFeilds.ORDER_ITEM_Brand_Name__c, invoice.Brand_Name__c);
		contentvalues.put(DBFeilds.ORDER_ITEM_Name, invoice.Name);
		contentvalues.put(DBFeilds.ORDER_ITEM_Product_Name__c, invoice.Product_Name__c);
		contentvalues.put(DBFeilds.ORDER_ITEM_Quantity__c, invoice.Quantity__c);
		contentvalues.put(DBFeilds.ORDER_ITEM_Rate__c, invoice.Rate__c);
		contentvalues.put(DBFeilds.ORDER_ITEM_Sales_Order__c, invoice.Sales_Order__c);
		contentvalues.put(DBFeilds.ORDER_ITEM_UOM__c, invoice.UOM__c);
				
		String where=DBFeilds.ORDER_ITEM_ID+" = '"+invoice.Id+"'";
		if(!isRecordExist(DBFeilds.TABLE_ORDER_ITEM, " where "+where)){
			db.insert(DBFeilds.TABLE_ORDER_ITEM, null, contentvalues);
		}else{
			db.update(DBFeilds.TABLE_ORDER_ITEM, contentvalues, where, null);
		}
	}
	
	public ArrayList<OrderResponse> getOrders(String where){
		ArrayList<OrderResponse> invoiceList=new ArrayList<OrderResponse>();
		Cursor c=db.rawQuery("SELECT * FROM "+DBFeilds.TABLE_ORDER+" "+where, null);
		for(int i=0;i<c.getCount();i++){
			c.moveToPosition(i);
			OrderResponse invoice =new OrderResponse();
			invoice.Id=c.getString(c.getColumnIndex(DBFeilds.ORDER_ID));
			invoice.No_of_Sales_Line_Items__c=c.getString(c.getColumnIndex(DBFeilds.ORDER_No_of_Sales_Line_Items__c));
			invoice.RecordTypeId=c.getString(c.getColumnIndex(DBFeilds.ORDER_RecordTypeId));
			invoice.RecordType.Name=c.getString(c.getColumnIndex(DBFeilds.ORDER_RecordType_Name));
			invoice.Name=c.getString(c.getColumnIndex(DBFeilds.ORDER_NAME));
			invoice.Distributor__c=c.getString(c.getColumnIndex(DBFeilds.ORDER_Distributer_c));
			invoice.Sales_Order_Line_Items__r.records=getOrderItem(" where "+DBFeilds.ORDER_ITEM_Sales_Order__c+" ='"+invoice.Id+"'");
			
			invoiceList.add(invoice);			
		}
		return invoiceList;
	}
	private ArrayList<OrderModel> getOrderItem(String where) {
		ArrayList<OrderModel> invoiceList=new ArrayList<OrderModel>();
		Cursor c=db.rawQuery("SELECT * FROM "+DBFeilds.TABLE_ORDER_ITEM+" "+where, null);
		for(int i=0;i<c.getCount();i++){
			c.moveToPosition(i);
			OrderModel invoice =new OrderModel();
			invoice.Id=c.getString(c.getColumnIndex(DBFeilds.ORDER_ITEM_ID));
			invoice.Brand_Name__c=c.getString(c.getColumnIndex(DBFeilds.ORDER_ITEM_Brand_Name__c));
			invoice.Name=c.getString(c.getColumnIndex(DBFeilds.ORDER_ITEM_Name));
			invoice.Product_Name__c=c.getString(c.getColumnIndex(DBFeilds.ORDER_ITEM_Product_Name__c));
			invoice.Quantity__c=c.getString(c.getColumnIndex(DBFeilds.ORDER_ITEM_Quantity__c));
			invoice.Rate__c=c.getString(c.getColumnIndex(DBFeilds.ORDER_ITEM_Rate__c));
			invoice.Sales_Order__c=c.getString(c.getColumnIndex(DBFeilds.ORDER_ITEM_Sales_Order__c));
			invoice.UOM__c=c.getString(c.getColumnIndex(DBFeilds.ORDER_ITEM_UOM__c));
			invoiceList.add(invoice);			
		}
		return invoiceList;
	}

}
