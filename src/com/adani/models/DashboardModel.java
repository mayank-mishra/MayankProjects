package com.adani.models;

public class DashboardModel {

	public String DistributorsCount="",InvoicesCount="",ProductsCount="",SalesOrdersCount="",count="",name="";
	
	public DashboardModel(String name,String count) {
		this.name=name;
		this.count=count;
	}
}
