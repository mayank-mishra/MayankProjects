package com.adani.database;

public class DBFeilds {

	
	public static final String TABLE_SFDC_DETAILS="table_sfdc_details";
	public static final String TABLE_USER = "userprofile";
	public static final String TABLE_PRODUCTS = "products";
	public static final String TABLE_DISTRIBUTER = "distributer";
	public static final String TABLE_INVOICE = "invoice";
	public static final String TABLE_ORDER = "table_order";
	public static final String TABLE_ORDER_ITEM = "order_item";
	
	
	public static final String INSTANCE_URL = "instance_url";
	public static final String ACCESS_TOKEN = "auth_code";
	public static final String REFRESH_TOKEN = "refresh_token";
	public static final String USER_ID = "Userid";
	
	
	public static final String ID="id";
	public static final String NAME="name";
	public static final String EMAIL="email";
	public static final String DIVISION="division";
	public static final String COMPANYNAME="companyname";
	
	
	//Product fields
	public static final String PRODUCT_ID="Id";
	public static final String PRODUCT_USER_ID="puser_id";
	public static final String PRODUCT_Brand_Code="Brand_Code__c";
	public static final String PRODUCT_Brand_Name__c="Brand_Name__c";
	public static final String PRODUCT_Category__c="Category__c";
	public static final String PRODUCT_Division_Code__c="Division_Code__c";
	public static final String PRODUCT_Division_Name__c="Division_Name__c";
	public static final String PRODUCT_Name="Name";
	public static final String PRODUCT_Pack_Size__c="Pack_Size__c";
	public static final String PRODUCT_Process__c="Process__c";
	public static final String PRODUCT_Product_Code__c="Product_Code__c";
	public static final String PRODUCT_Product_Type__c="Product_Type__c";
	
	//INVIOICE Fields
	public static final String INVIOICE_ID="Id";
	public static final String INVIOICE_USER_ID="iuser_id";
	public static final String INVIOICE_Distributor__c="Distributor__c";
	public static final String INVIOICE_Due_Amount__c="Due_Amount__c";
	public static final String INVIOICE_Invoice_Amount__c="Invoice_Amount__c";
	public static final String INVIOICE_Invoice_Date__c="Invoice_Date__c";
	public static final String INVIOICE_Invoice_Number__c="Invoice_Number__c";
	public static final String INVIOICE_Name="Name";
	
	//Distributer Fields
	public static final String DISTRIBUTER_ID="Id";
	public static final String DISTRIBUTER_USER_ID="duser_id";
	public static final String DISTRIBUTER_NAME="Name";
	public static final String DISTRIBUTER_TYPE__C="Distributor_Type__c";
	public static final String DISTRIBUTER_CODE__C="Distributor_Code__c";	
	
	//Order table Fields
	public static final String ORDER_ID="Id";
	public static final String ORDER_No_of_Sales_Line_Items__c="No_of_Sales_Line_Items__c";
	public static final String ORDER_Type__c="Order_Type__c";
	public static final String ORDER_RecordTypeId="RecordTypeId";
	public static final String ORDER_RecordType_Name="RecordType_Name";
	public static final String ORDER_NAME="Name";
	public static final String ORDER_Distributer_c="Distributor__c";
	public static final String ORDER_USER_ID="ouser_id";
	
	//OrderItem fields
	public static final String ORDER_ITEM_ID="Id";
	public static final String ORDER_ITEM_Brand_Name__c="Brand_Name__c";
	public static final String ORDER_ITEM_Name="Name";
	public static final String ORDER_ITEM_Product_Name__c="Product_Name__c";
	public static final String ORDER_ITEM_Quantity__c="Quantity__c";
	public static final String ORDER_ITEM_Rate__c="Rate__c";
	public static final String ORDER_ITEM_Sales_Order__c="Sales_Order__c";
	public static final String ORDER_ITEM_UOM__c="UOM__c";
}
