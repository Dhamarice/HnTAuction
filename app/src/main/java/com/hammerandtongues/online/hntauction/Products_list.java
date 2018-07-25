package com.hammerandtongues.online.hntauction;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Ruvimbo on 2/2/2018.
 */

public class Products_list extends AppCompatActivity {

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
    int   limit, offset;
    SharedPreferences shared;

    private static final String GETPRODUCT_URL = "https://devshop.hammerandtongues.com/webservice/getsingleproduct.php";
    private static final String BIDS_URL = "http://devauction.hammerandtongues.com/webservice/highest_bids.php";

    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_PRODUCTDETAILS = "posts";

    String productID, name, won, price,post_id, stock,  listtype, closedate, UserID,  imgurl = "";
    Single_Product product = null;
    ImageView banner;
    TextView inStock;
    int cartid, currcart, type_id;
    LinearLayout layout;
    View main;
    String lastid, type = "Product";
    String getlastid = "0";
    //Cursor cursor01;

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

            setContentView(R.layout.products_list);

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
            ImageLoader.getInstance().init(config);
            pDialog = new ProgressDialog(Products_list.this);
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

                UserID = shared.getString("userid", "");



                shared.edit().remove("lastid").apply();



                categoryID = (shared.getString("idKey", ""));
                listtype =  (shared.getString("Type", ""));
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
                    Intent intent = new Intent(Products_list.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            btncategs = (LinearLayout) findViewById(R.id.btn_Categories);
            btncategs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Products_list.this, Categories.class);

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

                        Intent intent = new Intent(Products_list.this, MyBids.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    else{

                        Toast ToastMessage = Toast.makeText(Products_list.this, "You are not logged in!", Toast.LENGTH_LONG);
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
                    Intent intent = new Intent(Products_list.this, Login.class);

                    startActivity(intent);
                }
            });

            btnsearch = (LinearLayout) findViewById(R.id.btn_Search01);
            btnsearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Products_list.this, Search.class);

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
                        setuielements();
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


        Cursor cursor01 = null;


        categoryID = (shared.getString("idKey", ""));

        TextView noresult = (TextView) findViewById(R.id.txtinfo);

        //Drawable img = (Drawable) getResources().getDrawable(R.drawable.appliances);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int densityDpi = (int)(metrics.density * 160f);


        int catid = Integer.parseInt(categoryID);
        int i;
        dbHandler = new DatabaseHelper(this);
        //.dbHandler.async_data();

        if (shared.getString("lastid", "") != null && shared.getString("lastid", "") != "") {

            getlastid = shared.getString("lastid", "");
        }


        if (listtype.contentEquals("Category")) {


            cursor01 = dbHandler.getCategoryContets(catid, limit, offset, getlastid);
            Log.e("if Product_list", "Values" + listtype);


        }
else if (listtype.contentEquals("Auction")) {
            cursor01 = dbHandler.getAuctionsContets(catid, limit, offset, getlastid);
            Log.e("else Product_list", "Values" + listtype);
        }

        else if (listtype.contentEquals("Location")) {
            cursor01 = dbHandler.getLocationContets(catid, limit, offset, getlastid);
            Log.e("else Product_list", "Values" + listtype);
        }

            if (cursor01 != null && cursor01.moveToFirst()) {

                Log.e("Product_list", "Values" + DatabaseUtils.dumpCursorToString(cursor01));


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

                    PName = cursor01.getString(7);
                    lastid = cursor01.getString(0);
                    post_id = cursor01.getString(1);
                    imgurl = cursor01.getString(9);
                    PPrice = cursor01.getString(4);
                    won = cursor01.getString(24);
                    closedate = cursor01.getString(25);


                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("lastid", lastid);
                    editor.commit();
                    editor.apply();

                    get_bids(post_id);


                    //Splitting date formatts

                    String CurrentEnddate = closedate;
                    String[] separatedate = CurrentEnddate.split(" ");
                    final String closedayserver = separatedate[0];
                    String closetimeserver = separatedate[1];


                    String Unsplitday = closedayserver;
                    String[] separatedday = Unsplitday.split("-");
                    String splitcloseyear = separatedday[0];
                    String splitclosemonth = separatedday[1];
                    String splitcloseday = separatedday[2];


                    String Unsplittime = closetimeserver;
                    String[] separatedtime = Unsplittime.split("-");
                    String splitclosehour = separatedtime[0];
                    String splitcloseminute = separatedtime[1];
                    String splitclosesecond = separatedtime[2];


                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyyy-MM-dd");
                        // Please here set your event date//YYYY-MM-DD
                        Date futureDate = dateFormat.parse(closedayserver);
                        Date currentDate = new Date();




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

                            Log.e("Setting Banner", imgurl);
                            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            LinearLayout prdctinfo = new LinearLayout(this);
                            prdctinfo.setOrientation(LinearLayout.VERTICAL);


                            TextView ProductName = new TextView(this);
                            TextView Price = new TextView(this);
                            ProductName.setText(PName);
                            Price.setText("Starting Price: $" + PPrice);
                            Price.setTextColor(getResources().getColor(R.color.colorAmber));
                            Price.setTypeface(null, Typeface.BOLD);
                            Price.setTextSize(13);

                            LinearLayout.LayoutParams btnsize = new LinearLayout.LayoutParams(200, 60);
                            btnsize.setMargins(20, 0, 20, 0);
                            btnsize.gravity = Gravity.RIGHT;

                            prdctinfo.addView(ProductName);
                            prdctinfo.addView(Price);

                            LinearLayout theBut = new LinearLayout(this);
                            theBut.setOrientation(LinearLayout.HORIZONTAL);



                        if (currentDate.after(futureDate)) {



                            Button Place_Bid = new Button(this);
                            Place_Bid.setText("Closed!");
                            Place_Bid.setTextColor(Color.WHITE);
                            Place_Bid.setTextSize(12);
                            Place_Bid.setId(Integer.parseInt(post_id));
                            Place_Bid.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {

                                    new AlertDialog.Builder(Products_list.this)
                                            .setTitle("Info")
                                            .setPositiveButton("Ok", null)

                                            .setNegativeButton("", null)
                                            .setMessage(Html.fromHtml("This auction has been closed!"))
                                            .show();


                                }

                            });

                            int length = (int) (metrics.density * 100);
                            int width = (int) (metrics.density * 35);

                            LinearLayout.LayoutParams btnsize2 = new LinearLayout.LayoutParams(length, width, 1);
                            btnsize2.setMargins(15, 0, 15, 0);

                            Place_Bid.setLayoutParams(btnsize2);
                            Place_Bid.setBackgroundColor(getResources().getColor(R.color.colorOrange));

                            theBut.addView(Place_Bid);
                            prdctinfo.addView(theBut);


                        }



                          else  if (won != null && won != "" && won != "0" && !won.contentEquals("0")) {


                                Button Place_Bid = new Button(this);
                                Place_Bid.setText("WON!");
                                Place_Bid.setTextColor(Color.WHITE);
                                Place_Bid.setTextSize(12);
                                Place_Bid.setId(Integer.parseInt(post_id));
                                Place_Bid.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        new AlertDialog.Builder(Products_list.this)
                                                .setTitle("Info")
                                                .setPositiveButton("Ok", null)

                                                .setNegativeButton("", null)
                                                .setMessage(Html.fromHtml("Product has already been won!"))
                                                .show();


                                    }

                                });

                                int length = (int) (metrics.density * 100);
                                int width = (int) (metrics.density * 35);

                                LinearLayout.LayoutParams btnsize2 = new LinearLayout.LayoutParams(length, width, 1);
                                btnsize2.setMargins(15, 0, 15, 0);

                                Place_Bid.setLayoutParams(btnsize2);
                                Place_Bid.setBackgroundColor(getResources().getColor(R.color.colorOrange));

                                theBut.addView(Place_Bid);
                                prdctinfo.addView(theBut);

                            } else {


                                Button Place_Bid = new Button(this);
                                Place_Bid.setText("View");
                                Place_Bid.setTextColor(Color.WHITE);
                                Place_Bid.setTextSize(12);
                                Place_Bid.setId(Integer.parseInt(post_id));
                                Place_Bid.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        // do stuff

                                        if (won != null && won != "" && won != "0" && !won.contentEquals("0")) {

                                            new AlertDialog.Builder(Products_list.this)
                                                    .setTitle("Info")
                                                    .setPositiveButton("Ok", null)

                                                    .setNegativeButton("", null)
                                                    .setMessage(Html.fromHtml("Product has already been won!"))
                                                    .show();

                                        } else {


                                            String id1 = Integer.toString(view.getId());
                                            SharedPreferences.Editor editor = shared.edit();
                                            editor.putString(Product, id1);
                                            editor.commit();
                                            Intent i = new Intent(Products_list.this, Single_Product.class);
                                            startActivity(i);


                                        }


                                    }

                                });


                                Button AddToFav = new Button(this);
                                AddToFav.setText("Watch List");
                                AddToFav.setTextColor(Color.WHITE);
                                AddToFav.setTextSize(12);
                                AddToFav.setId(Integer.parseInt(post_id));
                                AddToFav.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        // do stuff

                                        if (won != null && won != "" && won != "0" && !won.contentEquals("0")) {


                                            new AlertDialog.Builder(Products_list.this)
                                                    .setTitle("Info")
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {


                                                            Intent intent = new Intent(Products_list.this, Products_list.class);
                                                            startActivity(intent);
                                                        }
                                                    })

                                                    .setNegativeButton("", null)
                                                    .setMessage(Html.fromHtml("Product has already been won!"))
                                                    .show();

                                        } else {

                                            String id1 = Integer.toString(arg0.getId());
                                            SharedPreferences.Editor editor = shared.edit();
                                            editor.putString(Product, id1);
                                            editor.commit();
                                            productID = (shared.getString(Product, ""));
                                            addtoFav(id1);

                                            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                            boolean agreed = sharedPreferences.getBoolean("agreedauction", false);
                                            if (!agreed) {

                                                Intent i = new Intent(Products_list.this, MainActivity.class);
                                                startActivity(i);

                                            }

                                        }

                                    }


                                });


                                int length = (int) (metrics.density * 100);
                                int width = (int) (metrics.density * 35);



                                LinearLayout.LayoutParams btnsize2 = new LinearLayout.LayoutParams(length, width, 1);
                                btnsize2.setMargins(15, 0, 15, 0);

                                Place_Bid.setLayoutParams(btnsize2);
                                Place_Bid.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                                AddToFav.setLayoutParams(btnsize2);
                                AddToFav.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                                //prdctinfo.setOrientation(LinearLayout.HORIZONTAL);
                                theBut.addView(Place_Bid);

                                theBut.addView(AddToFav);
                                prdctinfo.addView(theBut);


                            }


                            prdctinfo.setLayoutParams(lparams);
                            itmcontr.addView(prdctinfo);
                            itmcontr.setId(Integer.parseInt(post_id));
                            layout.addView(itmcontr, layoutParams);






                        noresult.setVisibility(View.GONE);


                    } catch (Exception ex) {
                        Log.e("Image Button Error", ex.toString());
                    }
                    cursor01.moveToNext();
                }






            }
            else {
                String msg="";
                if (offset==0) {
                    msg="No Additional Contents to Display";
                }
                else{
                    msg ="No Contents to Display";
                }
                noresult.setText(msg);
                Toast.makeText(this, msg , Toast.LENGTH_LONG).show();
                noresult.setVisibility(View.VISIBLE);
            }
            //noresult.setVisibility(View.GONE);
    }


    public void addtoFav(String id1) {

        String curimgurl="";
//type_id = PID;
        DatabaseHelper db = new DatabaseHelper(this);
        int qty = 1;

        int id = Integer.parseInt(id1.toString());

        db.fillfav(id);
        Log.d("Notifications", "Picking up variables for saving" + String.valueOf((qty)) + " <= Quantity; " + String.valueOf(cartid) + "<=This is the current cart ID");

        Log.e("Notifications", "Saving the product to favs!");
        Log.e("Notifications", "Finished saving an item" + id );

        Toast.makeText(Products_list.this , "Item added to your watchlist!", Toast.LENGTH_LONG).show();
        Locale locale = new Locale("en", "US");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);


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
            Intent intent = new Intent(Products_list.this, Search.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.refresh) {
            Intent intent = new Intent(Products_list.this, Products_list.class);
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
            pDialog = new ProgressDialog(Products_list.this);
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
            int catId = Integer.parseInt(categoryID);
            dbHandler = new DatabaseHelper(Products_list.this);


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
                Toast.makeText(Products_list.this, posts, Toast.LENGTH_LONG).show();
            }

        }

    }




}

