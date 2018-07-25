package com.hammerandtongues.online.hntauction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ruvimbo on 2/2/2018.
 */

public class Single_Product extends AppCompatActivity  {


    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences shared;



    private TextView prodName, productDesc, prodPrice, prodBidInc, highest_bid;
    private static final String TAG = "Single Product";
    // Progress Dialog
    private ProgressDialog pDialog;
    int limit, offset;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    String GET_TYPE = "";

    private static final String GETPRODUCT_URL = "https://devshop.hammerandtongues.com/webservice/getsingleproduct.php";
    private static final String GETPRODUCT_VARIATION = "https://devshop.hammerandtongues.com/webservice/getproductvariation.php";
    private static final String BIDS_URL = "http://devauction.hammerandtongues.com/webservice/highest_bids.php";
    private static final String PLACE_BIDS_URL = "http://devauction.hammerandtongues.com/webservice/place_bid.php";


    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_PRODUCTDETAILS = "posts";

    String productID, userID, name, price, post_id, qnty, desc, seller, location, auction_name, min_bid_inc, startdate, closedate,splitmonthinwords,minebid, imgurl = "";
    Single_Product product = null;
    ImageView banner;
    TextView inStock, Location, Aucname, ProductID,Qnty, txtdays, txthours, txtmin, txtsec, mybid ;
    EditText txtquantity;
    int cartid, currcart, priceint, minbidint, maxbid, mymaxbid;
    LinearLayout layout, produtc;
    TextView noresult;
    private int cnt_cart;
    String bidinfo, Bidamount, high_bid;
    private TextView txtcartitems;
    SharedPreferences sharedpreferences;
    DatabaseHelper dbHandler;
    private Handler handler;
    private Runnable runnable;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            try {

                super.onCreate(savedInstanceState);
                setContentView(R.layout.product);
                SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                noresult = (TextView) findViewById(R.id.noresult);
                productID = (shared.getString("idKey", ""));
                if (shared.getString("userid", "") != null && shared.getString("userid", "") != "") {
                    userID = shared.getString("userid", "");
                }
                else {

                    userID = "0";

                }
                produtc = (LinearLayout) findViewById(R.id.product);
                GET_TYPE = "description";


                //shared.edit().remove("bid_amount").apply();

                SharedPreferences.Editor editor = shared.edit();
                editor.remove("from_product");
                editor.apply();

                dbHandler = new DatabaseHelper(this);

                new GetConnectionStatus().execute();
                //getOneProduct();

                get_bids(productID);

                //if (haveNetworkConnection() == true && isNetworkAvailable() == true) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                productDesc = (TextView) findViewById(R.id.description);
                prodName = (TextView) findViewById(R.id.prdctname);
                prodBidInc = (TextView) findViewById(R.id.prdctbidinc);
                prodPrice = (TextView) findViewById(R.id.prdctprice);
                highest_bid = (TextView) findViewById(R.id.highestbid);
                mybid = (TextView) findViewById(R.id.mytbid);
                banner = (ImageView) findViewById(R.id.product_banner);
                inStock = (TextView) findViewById(R.id.instock);
                ProductID = (TextView) findViewById(R.id.prdctid);
                 Qnty = (TextView) findViewById(R.id.txtQnty);
                Aucname = (TextView) findViewById(R.id.prdctauc);
                Location = (TextView) findViewById(R.id.prdctlocation);
                txtdays = (TextView) findViewById(R.id.txtdays);
                txthours = (TextView) findViewById(R.id.txthours);
                txtmin = (TextView) findViewById(R.id.txtmin);
                txtsec = (TextView) findViewById(R.id.txtsec);
                if (shared.getString("CartID", "") != null && shared.getString("CartID", "") != "") {
                    currcart = Integer.parseInt(shared.getString("CartID", ""));
                } else {
                    currcart = 0;
                }
                //
                Map<String, ?> allEntries = shared.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    Log.d("map values 123", entry.getKey() + ": " + entry.getValue().toString());
                }

                txtquantity = (EditText) findViewById(R.id.txtQnty);
                //
                final TextView Qnty = (TextView) findViewById(R.id.txtQnty);
                Qnty.setText("1");





                Button addtocart = (Button) findViewById(R.id.placebid);
                addtocart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        try {
                            if (!qnty.isEmpty() && qnty != "0") {
                                addProduct(arg0);
                            } else if (qnty == "0") {
                                Toast.makeText(Single_Product.this, "Sorry, This Product is Out of Stock", Toast.LENGTH_LONG).show();
                            } else {
                                addProduct(arg0);
                            }
                        } catch (Exception ex) {
                            Log.e("Add To Cart Error", ex.toString());
                        }

                    }
                });

                Button backtoshop = (Button) findViewById(R.id.btn_product_back);
                backtoshop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(Single_Product.this, MainActivity.class);
                        startActivity(intent);

                    }
                });


                final LinearLayout btnhome, btncategs, btnsearch, btncart, btnprofile;

                btnhome = (LinearLayout) findViewById(R.id.btn_home);
                btnhome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Single_Product.this, MainActivity.class);

                        startActivity(intent);
                    }
                });

                btncategs = (LinearLayout) findViewById(R.id.btn_Categories);
                btncategs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                btncart = (LinearLayout) findViewById(R.id.btn_Cart);
                btncart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                btnprofile = (LinearLayout) findViewById(R.id.btn_Profile);
                btnprofile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Single_Product.this, UserActivity.class);

                        startActivity(intent);
                    }
                });

                btnsearch = (LinearLayout) findViewById(R.id.btn_Search01);
                btnsearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Single_Product.this, Search.class);

                        startActivity(intent);
                    }
                });

                ImageView imgsearch, imgcategories, imghome, imgcart, imgprofile;
                imgsearch = (ImageView) findViewById(R.id.imgsearch01);
                imgsearch.setImageResource(R.drawable.searchselected);

                imghome = (ImageView) findViewById(R.id.imghome);
                imghome.setImageResource(R.drawable.home);

                imgcategories = (ImageView) findViewById(R.id.imgcategs);
                imgcategories.setImageResource(R.drawable.categories);

                imgcart = (ImageView) findViewById(R.id.imgcart);
                imgcart.setImageResource(R.drawable.cart02);

                imgprofile = (ImageView) findViewById(R.id.imgprofile);
                imgprofile.setImageResource(R.drawable.profile);

            } catch (Exception ex) {
                Log.e("Main Thread Exception", "Error: " + ex.toString());
                System.gc();
            }


        } catch (Exception ex) {
            Log.e("Main Thread Exception", "Error: " + ex.toString());
            System.gc();
        } catch (OutOfMemoryError ex) {
            Log.e("Main Thread Exception", "Error: " + ex.toString());
            System.gc();
        }
    }


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void getVariation() {
        try {
            GET_TYPE = "variation";
            new GetProductDetails().execute();
        } catch (Exception ex) {
            Log.e("Product Variation Error", ex.toString());
        }
    }

    public void SetBanner(String uri) {

        try {

            Picasso.with(this).load(uri)
                    .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.offline)
                    .into(banner);

            Log.e("Setting Banner", uri);
        } catch (Exception ex) {
            Log.e("Error getting Banner", ex.toString());
        }
    }




    public void addProduct(View view) {

        if (currcart == 0) {

        } else {
            cartid = currcart;
        }
        DatabaseHelper db = new DatabaseHelper(this);
        int qty = Integer.parseInt(txtquantity.getText().toString());
        int id = Integer.parseInt(productID);
        double itmprice = Double.parseDouble(price);
        double subtot = qty * Double.parseDouble(price);
        //if (sharedpreferences.getString("seller", "") != null && sharedpreferences.getString("seller", "") != "") {

        int seller = Integer.parseInt(sharedpreferences.getString("seller", ""));



        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);


        //get cart items count, set notification item value

        DatabaseHelper db = new DatabaseHelper(this);


        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_search) {
            Intent intent = new Intent(Single_Product.this, Search.class);

            startActivity(intent);
        }
            return true;


    }
    public class ProductVariation {
        private String key;
        private String value;
        private double price;

        public String getKey
                () {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String val) {
            this.value = val;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }

    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Single_Product.this);
            pDialog.setMessage("Getting Product Details...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String pid = productID;

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("productid", pid));
                JSONObject json1;
                if (GET_TYPE == "description") {
                    // getting product details by making HTTP request
                    json1 = jsonParser.makeHttpRequest(
                            GETPRODUCT_URL, "POST", params);
                    Log.e("PID + URL", productID + GETPRODUCT_VARIATION);
                } else// if ((GET_TYPE ="variance"))
                {
                    json1 = jsonParser.makeHttpRequest(
                            GETPRODUCT_VARIATION, "POST", params);
                    Log.e("PID + URL", productID + GETPRODUCT_VARIATION);

                }

                // json success tag
                if (json1 != null) {
                    success = json1.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        return json1.getString(TAG_PRODUCTDETAILS);
                    } else {
                        return json1.getString(TAG_MESSAGE);
                    }
                } else {
                    getOneProduct();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String posts) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (posts != null) {
                Log.e("JSONing", posts);
                if (GET_TYPE == "description") {
                    JSONArray jsonarray02 = null;
                    try {
                        jsonarray02 = new JSONArray(posts);
                    } catch (JSONException e) {
                    }
                    if (jsonarray02 != null) {
                        try {
                            Log.e("JSONing", jsonarray02.toString());
                            JSONObject jsonobject = jsonarray02.getJSONObject(0);
                            name = Html.fromHtml(jsonobject.optString("name")).toString();
                            //desc = Html.fromHtml(jsonobject.optString("content")).toString().trim();
                            imgurl = jsonobject.optString("imgurl");
                            price = jsonobject.optString("price");
                            qnty = jsonobject.optString("quantity");
                            post_id = jsonobject.optString("postid");
                            seller = jsonobject.optString("seller");
                        } catch (JSONException e) {
                            Log.e("JSON Error: ", e.toString());
                            e.printStackTrace();
                        }
                        productDesc.setText(desc);
                        prodName.setText(name);
                        prodPrice.setText("US $" + price);
                        if (qnty.isEmpty())
                            qnty = "0";
                        inStock.setText("Available Quantity: " + qnty + "item(s)");
                        SetBanner(imgurl);
                        getVariation();

                        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("seller", seller);
                        editor.commit();
                        editor.apply();


                    } else {
                        Toast.makeText(Single_Product.this, posts, Toast.LENGTH_LONG).show();
                    }
                } else if (GET_TYPE == "variation") {
                    // you can use this array to find the school ID based on name
                    ArrayList<ProductVariation> schools = new ArrayList<ProductVariation>();
                    // you can use this array to populate your spinner
                    ArrayList<String> Variations = new ArrayList<String>();

                    try {
                        JSONArray jsonArray = new JSONArray(posts);
                        Log.i(MainActivity.class.getName(),
                                "Number of entries " + jsonArray.length());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            ProductVariation prdvar = new ProductVariation();
                            prdvar.setKey(jsonObject.optString("key"));
                            prdvar.setValue(jsonObject.optString("value"));
                            prdvar.setPrice(Double.parseDouble(jsonObject.optString("price")));
                            schools.add(prdvar);
                            Variations.add("SIZE: " + jsonObject.optString("value") + " ($" + jsonObject.optString("price") + ")");

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Product", "Error!!!" + e.toString());
                    }
                    Spinner mySpinner = (Spinner) findViewById(R.id.spn_variation);
                    mySpinner.setAdapter(new ArrayAdapter<String>(Single_Product.this, android.R.layout.simple_spinner_dropdown_item, Variations));
                }

            }

        }


    }






    private void get_bids(final String Pid){
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BIDS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success",""+s);
                Log.e("Zitapass", "" + Pid);


                try {
                    JSONObject jsonobject=new JSONObject(s);
                    String message=jsonobject.getString("message");
                    int success=jsonobject.getInt("success");

                    if(success==1){

                        String bid=jsonobject.getString("bid");

                        shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString("bid_amount", bid);

                        editor.commit();
                        editor.apply();

                        dbHandler.update_curr_bi( bid, Pid);

                        Log.e(TAG, "Bid amount: " + bid);


                    }else{

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




    private void place_bid(final String Product_id, final String User_id, final String Bid_amount){
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, PLACE_BIDS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success",""+s);
                Log.e("Zitapass", "" + Bid_amount);

                try {
                    JSONObject jsonobject=new JSONObject(s);
                    String message=jsonobject.getString("message");
                    int success=jsonobject.getInt("success");

                    if(success==1){

if (mymaxbid > 0){


    dbHandler.update_bids(User_id, Bid_amount, Product_id);

}
                        else {
    dbHandler.fill_bids(User_id, Bid_amount, Product_id);
}

                        Toast.makeText(getBaseContext(), (message), Toast.LENGTH_SHORT).show();


                        new AlertDialog.Builder(Single_Product.this)
                                .setTitle("Info")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        Intent intent = new Intent(Single_Product.this, Single_Product.class);
                                        startActivity(intent);
                                    }
                                })

                                .setNegativeButton("", null)
                                .setMessage(Html.fromHtml(message))
                                .show();


                    }else{

                        Toast.makeText(getBaseContext(), (message), Toast.LENGTH_SHORT).show();
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
                values.put("productid",Product_id);
                values.put("user_id",User_id);
                values.put("bid_amount",Bid_amount);


                return values;
            }


        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);



    }







    class GetConnectionStatus extends AsyncTask<String, Void, Boolean> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Single_Product.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        protected Boolean doInBackground(String... urls) {
            // TODO: Connect
            if  (isNetworkAvailable() ==true) {
                try {
                    HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                    urlc.setRequestProperty("User-Agent", "Test");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();
                    return (urlc.getResponseCode() == 200);
                } catch (IOException e) {
                    Log.e("Network Check", "Error checking internet connection", e);
                }
            } else {
                Log.d("Network Check", "No network available!");
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            // TODO: Check this.exception
            // TODO: Do something with the feed
            pDialog.dismiss();

            if (result==true)
            {
                //new GetProductDetails().execute();
                getOneProduct();
            }
            else{

                getOneProduct();

                //noresult.setText("Network is Currently Unavailable. Available stock maybe not be up to date when adding cart items while offline!");
//                layout.addView(noresult);


                //Toast.makeText( Single_Product.this ,"Network is Currently Unavailable", Toast.LENGTH_LONG).show();
            }
        }
    }



    public void getOneProduct() {
        Log.e("Cursor items","statrted getting product method" );
        //  pDialog = new ProgressDialog(Product.this);
//        pDialog.setMessage("Getting Product Details...");
        //      pDialog.setIndeterminate(false);
        //    pDialog.setCancelable(true);
        //pDialog.show();
       final SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("seller", "100");
        editor.commit();
        editor.apply();

        final String pid = productID;
        limit = 15;
        offset = 0;
        try {

            Log.e("Cursor items","statrted calling database" );

            //productID = (shared.getString("idKey", ""));
            int pID = Integer.parseInt(pid);
            dbHandler.getProduct(pID, userID);
            if (dbHandler.getProduct(pID, userID) != null) {
                Cursor cursor = dbHandler.getProduct(pID, userID);

                Log.e("Cursor items","statrted getting cursor items" );

                //if (cursor != null && cursor.moveToFirst()) {

                Log.e("Cursor items","dumpCursorToString()" + DatabaseUtils.dumpCursorToString(cursor));
                //cursor01.moveToFirst();

                name = cursor.getString(7);
                post_id = cursor.getString(1);
                imgurl = cursor.getString(9);
                price = cursor.getString(4);
                auction_name = cursor.getString(11);
                location = cursor.getString(27);
                min_bid_inc = cursor.getString(26);
                high_bid = cursor.getString(30);
                startdate = cursor.getString(22);
                closedate =   cursor.getString(28);
                minebid =   cursor.getString(29);





                //Splitting date formatts


                String CurrentString = startdate;
                String[] separated = CurrentString.split(" ");
                String startdayserver = separated[0];
                String starttimeserver = separated[1];


                String Unsplitdate = startdayserver;
                String[] separateddate = Unsplitdate.split("-");
                String splityear = separateddate[0];
                String splitmonth = separateddate[1];
                String splitday = separateddate[2];


                String CurrentEnddate = closedate;
                String[] separatedate = CurrentEnddate.split(" ");
                final  String closedayserver = separatedate[0];
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




                String sortclosetime = splitclosehour + ":" + splitcloseminute + ":" + splitclosesecond;

                String sortstartdate = startdayserver + "T" + starttimeserver;

                String sortclosedate = closedayserver + "T" + sortclosetime;



                if (splitmonth.contentEquals("01")) {
                    splitmonthinwords = "JANUARY";
                } else if (splitmonth.contentEquals("02")) {
                    splitmonthinwords = "FEBRUARY";
                } else if (splitmonth.contentEquals("03")) {
                    splitmonthinwords = "MARCH";
                } else if (splitmonth.contentEquals("04")) {
                    splitmonthinwords = "APRIL";
                } else if (splitmonth.contentEquals("05")) {
                    splitmonthinwords = "MAY";
                } else if (splitmonth.contentEquals("06")) {
                    splitmonthinwords = "JUNE";
                } else if (splitmonth.contentEquals("07")) {
                    splitmonthinwords = "JULY";
                } else if (splitmonth.contentEquals("08")) {
                    splitmonthinwords = "AUGUST";
                } else if (splitmonth.contentEquals("09")) {
                    splitmonthinwords = "SEPTEMBER";
                } else if (splitmonth.contentEquals("10")) {
                    splitmonthinwords = "OCTOBER";
                } else if (splitmonth.contentEquals("11")) {
                    splitmonthinwords = "NOVEMBER";
                } else if (splitmonth.contentEquals("12")) {
                    splitmonthinwords = "DECEMBER";
                }



                txtdays.setTextColor(getResources().getColor(R.color.colorPrimary));
                txthours.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtmin.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtsec.setTextColor(getResources().getColor(R.color.colorPrimary));



                countDownStart(sortclosedate, txtdays, txthours, txtmin, txtsec);
                qnty = ("Started on:    " + splitday + "     " + splitmonthinwords + "     " + splityear);


                //imageLoader.displayImage(imgurl, imgstore[i], options);
                Picasso.with(this).load(imgurl)
                        .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.offline)
                        .into(banner);
                //ViewGroup.LayoutParams imglayoutParams = imgstore.getLayoutParams();

                pDialog.dismiss();

                Log.e("BID AMOUNT IS: ", shared.getString("bid_amount", ""));
                // LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


                SetBanner(imgurl);
                prodName.setText(name);
                prodBidInc.setText("Minimum increment: $"+ min_bid_inc);
                prodPrice.setText("Starting Price:  US $" + price);
                prodPrice.setTextColor(getResources().getColor(R.color.colorAmber));
                prodPrice.setTypeface(null, Typeface.BOLD);

                if (high_bid != null && high_bid !="" && high_bid !="null" && !high_bid.contentEquals("null") && !high_bid.contentEquals("")) {
                    highest_bid.setText("Highest bid:  $" + high_bid);
                    highest_bid.setTextColor(getResources().getColor(R.color.colorAmber));
                    highest_bid.setTypeface(null, Typeface.BOLD);

                }


                else {

                    highest_bid.setText("No bids have been placed yet!");
                    highest_bid.setTextColor(getResources().getColor(R.color.colorAmber));
                    highest_bid.setTypeface(null, Typeface.BOLD);

                }

                if (minebid != null && minebid !="" && minebid !="null" && !minebid.contentEquals("null") && !minebid.contentEquals("")) {
                    mybid.setText("My Bid:  $" + minebid);
                    mybid.setTextColor(getResources().getColor(R.color.colorAmber));
                    mybid.setTypeface(null, Typeface.BOLD);

                    mymaxbid = Integer.parseInt(minebid);

                }

                else {

                    mymaxbid = 0;

                }


                //TextView inStock = new TextView(this);
                inStock.setText(qnty);




                ProductID.setText("Item ID: " + post_id);
                ProductID.setTextColor(getResources().getColor(R.color.thatGreen));



                Location.setText("Location: " + location);
                Location.setTextColor(getResources().getColor(R.color.thatGreen));


                Aucname.setText("Auction ID: " + auction_name);
                Aucname.setTextColor(getResources().getColor(R.color.thatGreen));

                LinearLayout.LayoutParams btnsize = new LinearLayout.LayoutParams(200, 60);
                btnsize.setMargins(20, 0, 20, 0);
                btnsize.gravity = Gravity.RIGHT;



            }

            //}

        }

        catch (Exception e){

            Log.e("Database fetch","Error on fetching");
        }

        if (shared.getString("CartID", "") != null && shared.getString("CartID", "") != "") {
            currcart = Integer.parseInt(shared.getString("CartID", ""));
        } else {
            currcart = 0;
        }
        //
        Map<String, ?> allEntries = shared.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
           // Log.d("map values 123", entry.getKey() + ": " + entry.getValue().toString());
        }
priceint = Integer.parseInt(price);
        minbidint = Integer.parseInt(min_bid_inc);
        txtquantity = (EditText) findViewById(R.id.txtQnty);
        //

        Qnty.setText(price);


        bidinfo = "Proceed with bid amount:    $" + txtquantity.getText().toString();





        //Bidamount = txtquantity.getText().toString();





                Button placebid = (Button) findViewById(R.id.placebid);
        placebid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
             try {
                bidinfo = "Proceed with bid amount:    $" + txtquantity.getText().toString();
                    {


                    if    (shared.getString("userid", "") != null && shared.getString("userid", "") !="" && shared.getString("userid", "") !="null" && !(shared.getString("userid", "").contentEquals("null")) && !(shared.getString("userid", "").contentEquals(""))) {

                        int bid = Integer.parseInt(txtquantity.getText().toString());

                        if (high_bid != null && high_bid !="" && high_bid !="null" && !high_bid.contentEquals("null") && !high_bid.contentEquals(""))


                        {
                            maxbid = Integer.parseInt(high_bid);
                        }

                        else {
                            maxbid = 0;
                        }

                            if (bid < priceint) {

                                //Toast.makeText(Single_Product.this, ("Bid is less than starting price!"), Toast.LENGTH_SHORT).show();

                                new AlertDialog.Builder(Single_Product.this)
                                        .setTitle("Info")
                                        .setPositiveButton("Ok", null)

                                        .setNegativeButton("", null)
                                        .setMessage(Html.fromHtml("Bid is less than starting price!"))
                                        .show();

                            } else if (bid <= maxbid) {

                                //Toast.makeText(Single_Product.this, ("Bid amount is less than or equal to current highest bid!"), Toast.LENGTH_SHORT).show();
                                new AlertDialog.Builder(Single_Product.this)
                                        .setTitle("Info")
                                        .setPositiveButton("Ok", null)

                                        .setNegativeButton("", null)
                                        .setMessage(Html.fromHtml("Please place a bid that is more than the current highest bid!"))
                                        .show();

                            } else if (bid < (mymaxbid + minbidint)) {

                                //Toast.makeText(Single_Product.this, ("Increment is less than minimum bid increment!"), Toast.LENGTH_SHORT).show();

                                new AlertDialog.Builder(Single_Product.this)
                                        .setTitle("Info")
                                        .setPositiveButton("Ok", null)

                                        .setNegativeButton("", null)
                                        .setMessage(Html.fromHtml("Increment is less than minimum bid increment!"))
                                        .show();

                            } else {


                                new AlertDialog.Builder(Single_Product.this)
                                        .setTitle("Confirm Bid")
                                        .setPositiveButton("Place Bid", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {


                                                place_bid(post_id, userID, txtquantity.getText().toString());
                                            }
                                        })
                                        .setNegativeButton("Cancel", null)
                                        .setMessage(Html.fromHtml(bidinfo))
                                        .show();
                            }

                    }
                    else {

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("placebid", "placebid");
                        editor.commit();
                        editor.apply();

                        Intent intent = new Intent(getBaseContext(), Login.class);
                        startActivity(intent);


                    }

                    }

                }
               catch(Exception ex){
                    Log.e("Place Bid Error", ex.toString());
              }

            }
        });


        Button backtoshop = (Button) findViewById(R.id.btn_product_back);
        backtoshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Single_Product.this, MainActivity.class);
                startActivity(intent);

            }
        });

        Button viewbids = (Button) findViewById(R.id.viewbids);
        viewbids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {


                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("prname", name );
                editor.apply();

                Intent intent = new Intent(Single_Product.this, AllBids.class);
                startActivity(intent);



            }

        });

    }


    public void countDownStart( final String closedate, final TextView day, final TextView hour, final TextView min, final  TextView sec) {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss");
                    // Please here set your event date//YYYY-MM-DD
                    Date futureDate = dateFormat.parse(closedate);
                    Date currentDate = new Date();
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime()
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        day.setText("Expiring in:  Days: " + String.format("%02d", days));
                        hour.setText("  Hrs: " + String.format("%02d", hours));
                        min.setText("  Min:"
                                + String.format("%02d", minutes));
                        sec.setText("  Sec: "
                                + String.format("%02d", seconds));

                        //Log.e("countDownStart", "Still running: " + closedate);
                    } else {
                        //tvEventStart.setVisibility(View.VISIBLE);
                        //tvEventStart.setText("The event started!");
                        //textViewGone();

                        day.setText("Auction " );
                        hour.setText("closed");
                        min.setText("  on  ");
                        sec.setText(closedate);

                        //Log.e("countDownStart", "Closed!: " + closedate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1 * 1000);
    }







    @Override

    public void onStop(){
        super.onStop();

        Picasso.with(this).cancelRequest(banner);
    }


}
