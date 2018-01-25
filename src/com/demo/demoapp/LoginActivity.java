package com.demo.demoapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginActivity extends AppCompatActivity {
    private WebView webView;
   // private ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        webView = (WebView) findViewById(R.id.web_view);
        webView.setWebViewClient(new MyWebViewClient());

       // progress = (ProgressBar) findViewById(R.id.progressBar);
       // progress.setVisibility(View.GONE);
        // get intent data

        //String url = "https://test.salesforce.com/services/oauth2/authorize?response_type=code&client_id=3MVG9YDQS5WtC11qd1cf7yGNFZvrjO7Q6LEdDup7LwIOdPL7M0Y1i8AjyilRsz.NBF2k2uT8EpZV4NAYmXLxt&redirect_uri=https://ap1.salesforce.com/services/oauth2/token";
       // String url = "https://test.salesforce.com/services/oauth2/authorize?response_type=code&client_id=3MVG9YDQS5WtC11qd1cf7yGNFZvrjO7Q6LEdDup7LwIOdPL7M0Y1i8AjyilRsz.NBF2k2uT8EpZV4NAYmXLxt&redirect_uri=https://ap1.salesforce.com/services/oauth2/token";
        //String url = "https://test.salesforce.com/services/oauth2/authorize?&response_type=code&client_id=3MVG9YDQS5WtC11qd1cf7yGNFZvrjO7Q6LEdDup7LwIOdPL7M0Y1i8AjyilRsz.NBF2k2uT8EpZV4NAYmXLxt&redirect_uri=https://ap1.salesforce.com/services/oauth2/token";
        String url = "https://test.salesforce.com/services/oauth2/authorize?&response_type=code&client_id=3MVG9e2mBbZnmM6muWEz.rVsjfeZNGH.vsX_t.2naEhbbqSL5ULaneCbcGXhxlYtLJd3K1A6pn6ILtwgBd.qC&redirect_uri=https://success";
        if (validateUrl(url)) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);

        }
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private boolean validateUrl(String url) {
        return true;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
           // progress.setVisibility(View.GONE);
           // LoginActivity.this.progress.setProgress(100);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
           // progress.setVisibility(View.VISIBLE);
           // LoginActivity.this.progress.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }
    }



    public void setValue(int progress) {
       // this.progress.setProgress(progress);
    }
}
