package com.norway.bergen.ronny.bergen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Concert extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        
        WebView webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);

        final ProgressDialog waitDialog = new ProgressDialog(Concert.this);
        waitDialog.setTitle(R.string.play_wait_dialog_title);
        waitDialog.setMessage(getResources().getString(R.string.play_wait_dialog_message));
        webView.setWebChromeClient(new WebChromeClient() {
          public void onProgressChanged(WebView view, int progress) {
        	  if(progress==100) {
        		  if(waitDialog.isShowing()) {
        			  waitDialog.hide();  
        		  }
        	  }
        	  else {
        		  if(!waitDialog.isShowing()) {
        			  waitDialog.show();
        		  }
        	  }
          }
        });
        webView.setWebViewClient(new WebViewClient() {
          public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        	  Toast.makeText(getBaseContext(), getResources().getString(R.string.concert_toast_bad) + description, Toast.LENGTH_LONG).show();
          }
        });
        webView.loadUrl(getResources().getString(R.string.concert_url));
    }
}