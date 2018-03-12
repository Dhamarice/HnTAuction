package com.hammerandtongues.online.hntauction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONObject;
/**
 * Created by Ruvimbo on 2/2/2018.
 */

public final class WebViewActivity extends Activity {
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    JSONObject json=null;
    int success;
    String OrderID;
    SharedPreferences shared;
    public static final String MyPREFERENCES = "MyPrefs";
    private int currcart;
    private  double total;
    private  String totalPrc, uid;


    private static final String TAG_ID = "id";
    private static final String TAG_BALANCE = "balance";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_PRODUCTDETAILS = "posts";
    public static final String Product = "idKey";
    DatabaseHelper  dbHandler;

    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        shared = this. getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (shared.getString("CartID", "")!= null && shared.getString("CartID", "")!="")
        {
            OrderID=  shared.getString("OrderID", "");
            uid = (shared.getString("userid", ""));

        }


//Get a reference to your WebView//
        webView = (WebView) findViewById(R.id.webView1);
        WebSettings webSettings = webView.getSettings();
        //webSettings.setJavaScriptEnabled(true);

        //webView.getSettings().setAppCacheMaxSize( 10 * 1024 * 1024 ); // 5MB
        //webView.getSettings().setAppCachePath( getApplicationContext().getCacheDir().getAbsolutePath() );
        //webView.getSettings().setAllowFileAccess( true );
        //webView.getSettings().setAppCacheEnabled( true );
        webView.getSettings().setJavaScriptEnabled( true );
        WebSettings settings = webView.getSettings();
        settings.setSupportMultipleWindows(true);
        //webView.getSettings().setBuiltInZoomControls(true);
       // webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //CookieManager.getInstance().setAcceptCookie(true);


//Specify the URL you want to display//
        webView.loadUrl("https://devshop.hammerandtongues.com/webservice/paynowapi.php?action=createtransaction&order_id=" + OrderID);

        webView.setWebViewClient(new WebViewClient(){

            public void onReceivedError (WebView view, int errorCode, String description, String failingUrl) {
                webView.loadUrl("file:///android_asset/error.html");

            }

            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {

                url = webView.getUrl();
                if (url.contains("https://devshop.hammerandtongues.com/webservice/paynowapi.php?action=return&hsh="))
                {
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("ptype", "paynow");
                    editor.apply();

                    Intent intent = new Intent(WebViewActivity.this, TransactionHistory.class);
                    startActivity(intent);
                }
                return false;
            }





        });
    }
    }


