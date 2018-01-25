package com.demo.network;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.demo.demoapp.BaseActivity;
import com.demo.demoapp.MainActivity;
import com.demo.utils.AppUtils;


public class HttpGetMethod extends AsyncTask<String, Void, String> {
	String url,values,accessToken;
	BaseActivity baseactivty;
	ProgressDialog dialog;
	private int resultCode = 0;
	public HttpGetMethod(String url,String values,String accessToken, BaseActivity baseactivty) {
		this.url = url;
		this.values = values;
		this.accessToken = accessToken;
		this.baseactivty = baseactivty;
		this.dialog = new ProgressDialog(baseactivty);
	}

	protected void onPreExecute() {
		super.onPreExecute();
		dialog.setMessage("Please wait...");
		dialog.setCancelable(false);
		dialog.show();
	}
	@Override
	protected String doInBackground(String... params) {
		String response = "";
		try {
			if(params !=null && params.length>0)
				resultCode = Integer.parseInt(params[0]);
			response = GetMethod();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			dialog.dismiss();
			baseactivty.parseJsonResponse(result,resultCode);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String GetMethod() {
		String response = "";
		try {

			HttpParams params = new BasicHttpParams();
			int timeoutconnection = 5000;
			HttpConnectionParams
			.setConnectionTimeout(params, timeoutconnection);
			int sockettimeout = 5000;
			HttpConnectionParams.setSoTimeout(params, sockettimeout);
			 HttpClient _httpclient = HttpClientClass.getHttpClient(30000);
			//HttpClient _httpclient = new DefaultHttpClient(params);
			AppUtils.displayLog("--------------Get Method req url ----------",":"+url);
			AppUtils.displayLog("--------------Access Token ----------",":"+"OAuth "+accessToken);

			HttpGet _httpget = new HttpGet(url);

			_httpget.setHeader("Authorization", "OAuth "+accessToken);

			HttpResponse _response = _httpclient.execute(_httpget);
			int _responsecode =_response.getStatusLine().getStatusCode();
			AppUtils.displayLog("--------------Get Method Response Code-----------",":"+_responsecode);
			if (_responsecode == 200) {
				response = EntityUtils.toString(_response.getEntity());
			}else{
				response = _response.getStatusLine().getReasonPhrase();
			}
			AppUtils.displayLog("--------------Get Method Response-----------",":"+response);
		} catch (Exception e) {
			AppUtils.displayLog("--------------Get Method Exception-----------",":"+e.getMessage());
		}
		return response;
	}
}
