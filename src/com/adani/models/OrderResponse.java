package com.adani.models;

import java.io.Serializable;

public class OrderResponse implements Serializable{

	public String Id="",No_of_Sales_Line_Items__c="",Order_Type__c="",RecordTypeId="",Name="",Distributor__c="";
	public RecordType RecordType=new RecordType();
	public OrderLineItemModel Sales_Order_Line_Items__r=new OrderLineItemModel();
	public Distributor Distributor__r=new Distributor();
	
	public class RecordType{
		public String Id="",Name="";
	}
	
	public class Distributor{
		public String Name="",Id="";
	}
}
