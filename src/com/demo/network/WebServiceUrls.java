package com.demo.network;

public class WebServiceUrls {

	public static final String URL = "https://test.salesforce.com/";
	public static String LOAD_URL = URL;
	

	public static  String AUTHORIZECODE_URL = "services/oauth2/authorize?&response_type=code&client_id=3MVG9e2mBbZnmM6muWEz.rVsjfeZNGH.vsX_t.2naEhbbqSL5ULaneCbcGXhxlYtLJd3K1A6pn6ILtwgBd.qC&redirect_uri=https://success";
	public static  String TOKEN_URL = "services/oauth2/token?grant_type=authorization_code&client_id=3MVG9e2mBbZnmM6muWEz.rVsjfeZNGH.vsX_t.2naEhbbqSL5ULaneCbcGXhxlYtLJd3K1A6pn6ILtwgBd.qC&client_secret=4309507858446134463&redirect_uri=https://success&format=json&code=";
	//public static  String REFRESH_TOKEN = "services/oauth2/token?grant_type=refresh_token&client_id=3MVG9e2mBbZnmM6muWEz.rVsjfeZNGH.vsX_t.2naEhbbqSL5ULaneCbcGXhxlYtLJd3K1A6pn6ILtwgBd.qC&client_secret=4309507858446134463&refresh_token=";
	

	
	public static  String WS_USERDETAILS="WS_UserDetails/";
	public static  String WS_DISTRIBUTORDETAILS="WS_DistributorDetails/";
	public static  String WS_PRODUCTDETAILS="WS_ProductDetails?";
	public static  String WS_INVOICEDETAILS="WS_InvoiceDetails?";
	public static  String WS_SYNCSALESORDERS="WS_SyncSalesOrders?";
	public static  String WS_SalesOrderDetails="WS_SalesOrderDetails?";
	public static 	String WS_CountDetails="WS_CountDetails/";
	
}
