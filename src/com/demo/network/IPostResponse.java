package com.demo.network;



public interface IPostResponse {

	public void doRequest();
	public void parseJsonResponse(String response,int resultCode);
	public void insertDB();
	public void setCustomContentView(int layout);
	public void updateUi();
	
}
