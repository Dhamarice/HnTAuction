package com.hammerandtongues.online.hntauction;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Ruvimbo on 2/2/2018.
 */

public class MyBids extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Product = "idKey";

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    String categoryID,PName, PID, PPrice="";
    Boolean GetProducts = Boolean.FALSE;
    ImageView imgstore[] = new ImageView[1001];
    DatabaseHelper dbHandler;
    Cursor cursor01;
    int   limit, offset;
    SharedPreferences shared;

    private static final String GETPRODUCT_URL = "https://devauction.hammerandtongues.com/webservice/getsingleproduct.php";
    private static final String BIDS_URL = "http://devauction.hammerandtongues.com/webservice/highest_bids.php";

    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_PRODUCTDETAILS = "posts";

    String productID, name, won, price,post_id, stock,UserID, highest_bid, bid_amount, ppost_id,  imgurl = "";
    Single_Product product = null;
    ImageView banner;
    TextView inStock;
    int cartid, currcart, type_id, bid_amt, high_bid;
    LinearLayout layout;
    View main;
    String lastid, request, type = "Product";
    String getlastid = "0";
    Button mybids;

    private int cnt_cart;
    private TextView txtcartitems;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.gc();
        try {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.content_my_bids);

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
            ImageLoader.getInstance().init(config);
            pDialog = new ProgressDialog(MyBids.this);
            pDialog.setMessage("Checking Stock Availability...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            try {

                final SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                if (shared.getString("CartID", "") != null && shared.getString("CartID", "") != "") {
                    currcart = Integer.parseInt(shared.getString("CartID", ""));
                } else {
                    currcart = 0;
                }


                mybids = (Button) findViewById(R.id.btn_mybids);

                shared.edit().remove("lastid").apply();

                UserID = shared.getString("userid", "");

                request = shared.getString("request", "");

                mybids.setText(request);



                categoryID = (shared.getString("idKey", ""));
                productID = (shared.getString("productID", ""));
                limit = 15;
                offset = 0;

                GetProducts = Boolean.FALSE;
                new GetProductDetails().execute();
            } catch (Exception ex) {
                Log.e("OnCreate Error", ex.toString());
            }


            final LinearLayout btnhome, btncategs, btnsearch, btncart, btnprofile;

            btnhome = (LinearLayout) findViewById(R.id.btn_home);
            btnhome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MyBids.this, MainActivity.class);

                    startActivity(intent);
                }
            });

            btncategs = (LinearLayout) findViewById(R.id.btn_Categories);
            btncategs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MyBids.this, Categories.class);

                    startActivity(intent);
                }
            });

            btncart = (LinearLayout) findViewById(R.id.btn_Cart);
            btncart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (UserID != null && UserID != ""){

                        SharedPreferences.Editor editor = shared.edit();

                        editor.putString("request", "My bids");
                        editor.apply();

                        Intent intent = new Intent(MyBids.this, MyBids.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    else{

                        Toast ToastMessage = Toast.makeText(MyBids.this, "You are not logged in!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();
                    }
                }
            });

            btnprofile = (LinearLayout) findViewById(R.id.btn_Profile);
            btnprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MyBids.this, Login.class);

                    startActivity(intent);
                }
            });

            btnsearch = (LinearLayout) findViewById(R.id.btn_Search01);
            btnsearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MyBids.this, Search.class);

                    startActivity(intent);
                }
            });

            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

            final Button btncateg, btnstore, btnpromo;

            btncateg = (Button) findViewById(R.id.btn_categs);
            btncateg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent intent = new Intent(Products_list.this, CategoriesActivity.class);

                    //startActivity(intent);

                }
            });

            btnstore = (Button) findViewById(R.id.btn_stores);
            btnstore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent intent = new Intent(Products_list.this, StoresFragment.class);
                    //startActivity(intent);
                }
            });

            btnpromo = (Button) findViewById(R.id.btn_promos);
            btnpromo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Intent intent = new Intent(Products_list.this, PromotionsFragment.class);
                    //startActivity(intent);
                }
            });
            System.gc();

            final NestedScrollView sv = (NestedScrollView) findViewById(R.id.svmain);
            sv.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    //int scrollY = rootScrollView.getScrollY(); // For ScrollView
                    //int scrollX = rootScrollView.getScrollX(); // For HorizontalScrollView
                    // DO SOMETHING WITH THE SCROLL COORDINATES
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.child);
                    if ((linearLayout.getMeasuredHeight()) <= (sv.getScrollY() +
                            sv.getHeight())) {

                        if (UserID != null && UserID != "") {
                            setuielements();
                        }

                        else {


                        }
                        //Toast.makeText(Products_List.this,"Hechoni",Toast.LENGTH_LONG).show();
                    } else {
                        //do nothing
                        //Toast.makeText(Products_List.this,"Zvikudotekaira",Toast.LENGTH_LONG).show();
                    }
                    Log.e("Scrollview", "Height - " + sv.getScrollY());
                }
            });





        } catch (Exception ex) {
            Log.e("Main Thread Exception", "Error: " + ex.toString());
            System.gc();
        }
    }

    //Inquire product status from server
    private void get_bids(final String Pid){
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
        //pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BIDS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //pDialog.dismiss();

                Log.e("Success",""+s);
                Log.e("Zitapass", "" + Pid);


                try {
                    JSONObject jsonobject=new JSONObject(s);
                    String message=jsonobject.getString("message");
                    int success=jsonobject.getInt("success");

                    if(success==1){

                        String won =jsonobject.getString("date_chosen");

                        dbHandler.update_won_prdct( won, Pid);




                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();
                volleyError.printStackTrace();
                Log.e("RUEERROR",""+volleyError);
                Toast.makeText(getBaseContext(), "Please check your Intenet and try again!", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> values=new HashMap();
                values.put("productid",Pid);


                return values;
            }


        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);



    }






    public void setuielements() {
        ProgressBar pgr = (ProgressBar) findViewById(R.id.progressBar3);
        pgr.setVisibility(View.GONE);

        final SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        LinearLayout layout = (LinearLayout) findViewById(R.id.productslist);


        categoryID = (shared.getString("idKey", ""));

        TextView noresult = (TextView) findViewById(R.id.txtinfo);

        //Drawable img = (Drawable) getResources().getDrawable(R.drawable.appliances);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int densityDpi = (int)(metrics.density * 160f);

        int i;
        dbHandler = new DatabaseHelper(this);
        //.dbHandler.async_data();

        if (shared.getString("lastid", "") != null && shared.getString("lastid", "") != "") {

            getlastid = shared.getString("lastid", "");
        }



        if (request == "Past bids"){

            cursor01 = dbHandler.getpastbids(UserID);
        }

        else if (request == "My bids"){

            cursor01 = dbHandler.getmybids(UserID);
        }


        if (cursor01 != null) {

            if (cursor01 != null && cursor01.moveToFirst()) {

                Log.e("Product_list", "Values" + DatabaseUtils.dumpCursorToString(cursor01));

                if (cursor01.getString(1) != null) {




                    int i2 = cursor01.getCount();
                    for (i = 0; i < i2; i++) {
                        LinearLayout itmcontr = new LinearLayout(this);


                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        layoutParams.setMargins(10, 10, 10, 10);

                        itmcontr.setOrientation(LinearLayout.HORIZONTAL);
                        itmcontr.setBackgroundColor(Color.WHITE);

                        imgstore[i] = new ImageView(this);
                        itmcontr.addView(imgstore[i]);

                        PName = cursor01.getString(9);
                        lastid = cursor01.getString(0);
                        post_id = cursor01.getString(1);
                        imgurl = cursor01.getString(10);
                        ppost_id = cursor01.getString(11);
                        bid_amount = cursor01.getString(7);
                        highest_bid = cursor01.getString(8);

                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString("lastid", lastid);
                        editor.commit();
                        editor.apply();

                        get_bids(ppost_id);

                        //imageLoader.displayImage(imgurl, imgstore[i], options);
                        Picasso.with(this).load(imgurl)
                                .placeholder(R.drawable.progress_animation)
                                .error(R.drawable.offline)
                                .into(imgstore[i]);
                        ViewGroup.LayoutParams imglayoutParams = imgstore[i].getLayoutParams();

                        int imglength = (int) (metrics.density * 80);
                        int imgwidth = (int) (metrics.density * 80);


                        imglayoutParams.width = imglength;
                        imglayoutParams.height = imgwidth;

                        imgstore[i].setLayoutParams(imglayoutParams);

                        bid_amt = Integer.parseInt(bid_amount);

                        if (highest_bid != null) {
                            high_bid = Integer.parseInt(highest_bid);
                        } else {

                            high_bid = 0;

                        }

                        Log.e("Setting Banner", imgurl);
                        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        LinearLayout prdctinfo = new LinearLayout(this);
                        prdctinfo.setOrientation(LinearLayout.VERTICAL);


                        TextView ProductName = new TextView(this);
                        TextView Price = new TextView(this);
                        ProductName.setText(PName);
                        ProductName.setTypeface(null, Typeface.BOLD);
                        Price.setText("Bid amount: $" + bid_amount);
                        Price.setTextColor(getResources().getColor(R.color.colorAmber));
                        Price.setTypeface(null, Typeface.BOLD);
                        Price.setTextSize(14);

                        LinearLayout.LayoutParams btnsize = new LinearLayout.LayoutParams(200, 60);
                        btnsize.setMargins(5, 5, 5, 5);
                        btnsize.gravity = Gravity.RIGHT;

                        prdctinfo.addView(ProductName);
                        prdctinfo.addView(Price);

                        LinearLayout theBut = new LinearLayout(this);
                        theBut.setOrientation(LinearLayout.HORIZONTAL);


                        if (won != null && won != "" && won != "0" && !won.contentEquals("0")) {


                            TextView Status = new TextView(this);
                            Status.setText("Product has been WON!");
                            Status.setTextColor(Color.WHITE);
                            Status.setTextSize(12);
                            Status.setId(Integer.parseInt(ppost_id));


                            int length = (int) (metrics.density * 100);
                            int width = (int) (metrics.density * 45);

                            LinearLayout.LayoutParams btnsize2 = new LinearLayout.LayoutParams(length, width, 1);
                            btnsize2.setMargins(5, 5, 5, 5);

                            Status.setLayoutParams(btnsize2);
                            Status.setBackgroundColor(getResources().getColor(R.color.colorOrange));

                            theBut.addView(Status);
                            prdctinfo.addView(theBut);

                        } else {


                            // do stuff

                            if (bid_amt < high_bid) {


                                final TextView Status = new TextView(this);
                                Status.setText("Loosing!");
                                Status.setTextColor(Color.RED);
                                Status.setTextSize(15);
                                Status.setTypeface(null, Typeface.BOLD);
                                Status.setId(Integer.parseInt(ppost_id));


                                   if (request == "My bids"){

                                    cursor01 = dbHandler.getmybids(UserID);


                                       Button Place_Bid = new Button(this);
                                       Place_Bid.setText("Update Bid");
                                       Place_Bid.setTextColor(Color.WHITE);
                                       Place_Bid.setTextSize(12);
                                       Place_Bid.setId(Integer.parseInt(ppost_id));
                                       Place_Bid.setOnClickListener(new View.OnClickListener() {

                                           @Override
                                           public void onClick(View view) {


                                               String id1 = Integer.toString(view.getId());
                                               SharedPreferences.Editor editor = shared.edit();
                                               editor.putString(Product, id1);
                                               editor.commit();
                                               Intent i = new Intent(MyBids.this, Single_Product.class);
                                               startActivity(i);


                                           }


                                       });

                                       int length = (int) (metrics.density * 100);
                                       int width = (int) (metrics.density * 35);

                                       LinearLayout.LayoutParams btnsize2 = new LinearLayout.LayoutParams(length, width, 1);
                                       btnsize2.setMargins(15, 5, 15, 5);

                                       Place_Bid.setLayoutParams(btnsize2);
                                       Place_Bid.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                                       theBut.addView(Place_Bid);
                                }




                                int length = (int) (metrics.density * 100);
                                int width = (int) (metrics.density * 35);


                                LinearLayout.LayoutParams btnsize2 = new LinearLayout.LayoutParams(length, width, 1);
                                btnsize2.setMargins(15, 5, 15, 5);


                                Status.setLayoutParams(btnsize2);



                                //prdctinfo.setOrientation(LinearLayout.HORIZONTAL);
                                theBut.addView(Status);
                                prdctinfo.addView(theBut);


                            } else {


                                final TextView Status = new TextView(this);
                                Status.setText("Winning!");
                                Status.setTextColor(getResources().getColor(R.color.colorPrimary));
                                Status.setTextSize(15);
                                Status.setTypeface(null, Typeface.BOLD);
                                Status.setId(Integer.parseInt(ppost_id));


                                if (request == "My bids"){

                                    cursor01 = dbHandler.getmybids(UserID);


                                    Button Place_Bid = new Button(this);
                                    Place_Bid.setText("Update Bid");
                                    Place_Bid.setTextColor(Color.WHITE);
                                    Place_Bid.setTextSize(12);
                                    Place_Bid.setId(Integer.parseInt(ppost_id));
                                    Place_Bid.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View view) {


                                            String id1 = Integer.toString(view.getId());
                                            SharedPreferences.Editor editor = shared.edit();
                                            editor.putString(Product, id1);
                                            editor.commit();
                                            Intent i = new Intent(MyBids.this, Single_Product.class);
                                            startActivity(i);


                                        }


                                    });

                                    int length = (int) (metrics.density * 100);
                                    int width = (int) (metrics.density * 35);

                                    LinearLayout.LayoutParams btnsize2 = new LinearLayout.LayoutParams(length, width, 1);
                                    btnsize2.setMargins(15, 5, 15, 5);

                                    Place_Bid.setLayoutParams(btnsize2);
                                    Place_Bid.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                                    theBut.addView(Place_Bid);
                                }


                                int length = (int) (metrics.density * 100);
                                int width = (int) (metrics.density * 35);

                                LinearLayout.LayoutParams btnsize2 = new LinearLayout.LayoutParams(length, width, 1);
                                btnsize2.setMargins(15, 0, 15, 0);


                                Status.setLayoutParams(btnsize2);

                                //prdctinfo.setOrientation(LinearLayout.HORIZONTAL);
                                theBut.addView(Status);

                                prdctinfo.addView(theBut);


                            }


                            prdctinfo.setLayoutParams(lparams);
                            itmcontr.addView(prdctinfo);
                            itmcontr.setId(Integer.parseInt(post_id));
                            layout.addView(itmcontr, layoutParams);
                            cursor01.moveToNext();


                        }
                        noresult.setVisibility(View.GONE);

                    }

                }

                else{

                    String msg="";
                    msg="You have no bids to show in " + request;

                    Toast.makeText(this, msg , Toast.LENGTH_LONG).show();
                    noresult.setVisibility(View.VISIBLE);
                }
            }
            else {
                String msg="";
                if (offset==0) {
                    msg="You have no bids to show in " + request;
                }
                else{
                    msg ="No Additional bids to Display";
                }
                noresult.setText(msg);
                Toast.makeText(this, msg , Toast.LENGTH_LONG).show();
                noresult.setVisibility(View.VISIBLE);
            }
            noresult.setVisibility(View.GONE);
        }else {



            String msg="";
            if (offset==0) {
                msg="You have no bids to show in " + request;
            }
            else{
                msg ="No Additional bids to Display";
            }
            noresult.setText(msg);
            Toast.makeText(this, msg , Toast.LENGTH_LONG).show();
            noresult.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;

    }



    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Products_List Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.hammerandtongues.online.hntauction/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        Picasso.with(this).cancelRequest(banner);
        for (int i = 0; i<imgstore.length; i++ ) {
            Picasso.with(this).cancelRequest(imgstore[i]);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Products_List Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.hammerandtongues.online.hntauction/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_search) {
            Intent intent = new Intent(MyBids.this, Search.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyBids.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            pDialog.dismiss();

        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            int catId = 21;
            dbHandler = new DatabaseHelper(MyBids.this);


            //dbHandler.async_data();
            if (dbHandler.getAuctionsContets(catId, limit, offset, lastid) != null) {
                Cursor cursor = dbHandler.getAuctionsContets(catId, limit, offset, lastid);
                if (cursor != null && cursor.moveToFirst()) {
                    return "Success";
                }
            }
            return "";
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String posts) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (posts != null && GetProducts == Boolean.FALSE) {
                setuielements();
            } else {
                Toast.makeText(MyBids.this, posts, Toast.LENGTH_LONG).show();
            }

        }

    }

}


