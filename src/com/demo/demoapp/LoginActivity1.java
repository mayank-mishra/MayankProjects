package com.demo.demoapp;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.adani.models.SFDCDetails;
import com.demo.network.HttpClientClass;
import com.demo.network.WebServiceUrls;
import com.demo.utils.AppUtils;

public class LoginActivity1 extends ActionBarActivity {
	WebView web_view;
	ProgressBar progress_bar, progress_horizontal;
	ImageView img_splash;	
	int spinPosition = 0;
	@SuppressLint("SetJavaScriptEnabled")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		progress_bar = (ProgressBar) findViewById(R.id.progressbar_circular);
		progress_bar.setVisibility(View.GONE);
		progress_horizontal = (ProgressBar) findViewById(R.id.progressbar_horizontal);
		progress_horizontal.setVisibility(View.GONE);
		web_view = (WebView) findViewById(R.id.web_view);
		WebSettings mWebSettings = web_view.getSettings();
		mWebSettings.setSaveFormData(false);

		img_splash = (ImageView) findViewById(R.id.img_splash);
		img_splash.setVisibility(View.GONE);

		web_view.getSettings().setJavaScriptEnabled(true);

		//String url = "https://test.salesforce.com/services/oauth2/authorize?&response_type=code&client_id=3MVG9e2mBbZnmM6muWEz.rVsjfeZNGH.vsX_t.2naEhbbqSL5ULaneCbcGXhxlYtLJd3K1A6pn6ILtwgBd.qC&redirect_uri=https://success";

		web_view.loadUrl(WebServiceUrls.LOAD_URL+WebServiceUrls.AUTHORIZECODE_URL);

		web_view.setWebViewClient(new WebviewWebViewClient());

		web_view.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				progress_horizontal.setProgress(newProgress);
				if (newProgress == 100) {
					progress_horizontal.setVisibility(View.GONE);
				} else {
					progress_horizontal.setVisibility(View.VISIBLE);
				}
			}
		});

	}


	private class WebviewWebViewClient extends WebViewClient {

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			view.loadUrl("file:///android_asset/myerrorpage.html");
		}
		@Override
		public boolean shouldOverrideUrlLoading(final WebView view, String url) {
			
			AppUtils.displayLog("---------------------Complete Url----------------", ":"+ url);
			
			if (url.contains("https://success")) {

				Uri uri = Uri.parse(url);
				String verifier = uri.getQueryParameter("code");
				if(verifier!=null){
					new getAccessToken().execute(verifier);
				}

			}else {
				view.loadUrl(url);
			}
			return true;
		}

		public void onPageFinished(final WebView view, String url) {
			// do your stuff here
			super.onPageFinished(view,url);
			try {
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	private class getAccessToken extends AsyncTask<String, Void, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			web_view.setVisibility(View.GONE);
			img_splash.setVisibility(View.VISIBLE);
			progress_bar.setVisibility(View.VISIBLE);

		}

		@Override
		protected String doInBackground(String... params) {
			return getAccessTokenString(params[0]);
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progress_bar.setVisibility(View.GONE);
			progress_horizontal.setVisibility(View.GONE);
			try {
				Log.i("-------------Response-----------", ":"+result);
				JSONObject obj = new JSONObject(result);
				String refresh_token = obj.optString("refresh_token");
				String access_token = obj.optString("access_token");
				String instance_url = obj.optString("instance_url");
				String[] user_id_params = obj.optString("id").split("/");
				String user_id = user_id_params[user_id_params.length - 1];

				SFDCDetails sfdc = new SFDCDetails();
				sfdc.user_id = user_id;
				sfdc.instance_url = instance_url+"/services/apexrest/";
				sfdc.refresh_token = refresh_token;
				sfdc.access_token = access_token;
				Intent i = new Intent(LoginActivity1.this, HomeActivity.class);
				//i.setClassName(LoginActivity1.this, HomeActivity.class);
				i.putExtra(AppUtils.INTENT_KEY, sfdc);
				startActivity(i);
				finish();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String getAccessTokenString(String authorize_code) {
		String token = "";
		try {
		    HttpClient client = HttpClientClass.getHttpClient(30000);
			HttpPost post = null;
			post = new HttpPost(WebServiceUrls.LOAD_URL+WebServiceUrls.TOKEN_URL + authorize_code);
			HttpResponse response = client.execute(post);
			token = EntityUtils.toString(response.getEntity());
			//Log.i("-----------Response--------------", ":" + token);
		} catch (Exception e) {

		}
		return token;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
