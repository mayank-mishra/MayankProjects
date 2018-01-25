package com.demo.network;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.demo.demoapp.BaseActivity;
import com.demo.demoapp.MainActivity;
import com.demo.utils.AlertDialogCustom;
import com.demo.utils.AppUtils;


public class HttpPostData extends AsyncTask<String, Void, String> {
	private String _url;
	// private String _jsonstring;
	String _values,_access_token;
	//private final int HTTP_OK = 200;
	private BaseActivity _baseactivity;
	private ProgressDialog _dialog;
	AlertDialogCustom alert_dialog;
	private int resultCode = 0;

	public HttpPostData(String _url, String values,String access_token,BaseActivity baseactivity) {
		this._url = _url;
		this._values = values;
		this._access_token = access_token;
		this._baseactivity = baseactivity;
		this._dialog = new ProgressDialog(_baseactivity);
		this.alert_dialog = new AlertDialogCustom(_baseactivity);
	}

	protected void onPreExecute() {
		super.onPreExecute();

		_dialog.setMessage("Please wait...");
		_dialog.setCanceledOnTouchOutside(false);
		_dialog.setCancelable(false);
		_dialog.show();
	}

	@Override
	protected String doInBackground(String... params) {
		String response ="";
		try {
			if(params !=null && params.length>0)
				resultCode = Integer.parseInt(params[0]);
			response = executeRequestNew();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			_dialog.dismiss();
			_baseactivity.parseJsonResponse(result,resultCode);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/*
	private String executeRequest() {
		String responce = null;
		int error = 0;
		URL url;
		try {
			url = new URL(_url);
			AppUtils.displayLog("---------------------Url--------------",	":" + url.toString());
			AppUtils.displayLog("---------------------Access Token--------------",	":" + _access_token);
			AppUtils.displayLog("--------------------Body Values--------------",	":" + _values);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Authorization","OAuth " +_access_token);
			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(_values);
			writer.flush();
			writer.close();
			os.close();
			conn.connect();
			error = conn.getResponseCode();
			if (error == HTTP_OK) {
				InputStream content = (InputStream) conn.getContent();
				responce = IOUtils.toString(content);
			}else if(error >=500){
				InputStream content = (InputStream) conn.getContent();
				responce = IOUtils.toString(content);
			}else{
				responce = conn.getResponseMessage();
			}
			AppUtils.displayLog("-------------Post Method----", ":" + responce);
			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
			AppUtils.displayLog("--------------MalformedURLException----",":"+e.getMessage());
		} catch (ProtocolException e) {
			e.printStackTrace();
			AppUtils.displayLog("--------------ProtocolException----",":"+e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			AppUtils.displayLog("--------------IOException----",":"+e.getMessage());
		}
		return responce;
	}*/


	private String executeRequestNew() {
		String response="";
		try {
			int _responsecode;
			HttpParams params = new BasicHttpParams();
			int timeoutconnection = 30000;
			HttpConnectionParams.setConnectionTimeout(params, timeoutconnection);
			int sockettimeout = 30000;
			HttpConnectionParams.setSoTimeout(params, sockettimeout);
			HttpResponse _httpresponse ;
			HttpClient _httpclient = HttpClientClass.getHttpClient(30000);
			//HttpClient _httpclient = new DefaultHttpClient();
			HttpPost _httppost = new HttpPost(_url);
			AppUtils.displayLog("----------------Post Url-------------",":"+_url);
			try {

				_httppost.addHeader("Authorization","OAuth "+ _access_token);
				AppUtils.displayLog("-----BEARER TOKEN---",":OAuth " + _access_token);
				if(_values != null && _values.length()>0){
					AppUtils.displayLog("---JSON Object Data----",":"+_values.toString());
					_httppost.setEntity(new StringEntity(_values.toString(),"UTF-8"));
				}
				_httpresponse= _httpclient.execute(_httppost);
				_responsecode = _httpresponse.getStatusLine().getStatusCode();
				AppUtils.displayLog("HTTP RESPONSE CODE",":"+_responsecode);

				response = EntityUtils.toString(_httpresponse.getEntity());
				AppUtils.displayLog("--------------Post Method Response -----------",":"+response);


			} catch (Exception e) {
				e.printStackTrace();
				response = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		return response;
	}


}
