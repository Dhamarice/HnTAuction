package com.hammerandtongues.online.hntauction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruvimbo on 2/2/2018.
 */
public class TransactionHistory  extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    private String cartID="";
    private TextView storeName, storeDesc;
    public static final String OrderNumber = "idKey";
    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String GETTRANSACTION_HISTORY_URL = "https://devshop.hammerandtongues.com/webservice/gettransactionhistory.php";

    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_PRODUCTDETAILS = "posts";
    private static final String ECORESPONSE_URL = "https://devshop.hammerandtongues.com/wp-content/themes/Walleto/CheckDb.php";
    private static final String DELETEORDER_URL = "https://devshop.hammerandtongues.com/webservice/deleteoder.php";
    private  double total;
    private  String OrderID, uid, ptype, totalPrc;
    int success;
    private int currcart;
    DatabaseHelper  dbHandler;
    String userID, strOrders;
    String OrderNo,OrderDate, Total,Items="";
    String imgurl ="";
    ImageView imgstore[] = new ImageView[200];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_history);

        final SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        OrderID=  shared.getString("OrderID", "");
        uid = (shared.getString("userid", ""));
        totalPrc = (shared.getString("totalPrc", ""));

        if (shared.getString("CartID", "") != null && shared.getString("CartID", "") !="") {

            currcart = Integer.parseInt(shared.getString("CartID", ""));

        }


        System.gc();


        if (shared.getString("userid", "") != null && shared.getString("userid", "") !="") {
            userID = (shared.getString("userid", ""));
            new GetProductDetails().execute();


            if (shared.getString("ptype", "") != null && shared.getString("ptype", "") !="") {
                ptype = (shared.getString("ptype", ""));
                new ProcessRequest().execute();


                SharedPreferences.Editor editor = shared.edit();
                editor.remove("ptype");
                editor.apply();
            }


        }
        else {
            Toast.makeText(this,"You Are Not Logged In",Toast.LENGTH_LONG).show();
            ProgressBar pg1 = (ProgressBar) findViewById(R.id.progressBar1);
            pg1.setVisibility(View.GONE);
        }




        Button addtocart = (Button) findViewById(R.id.btn_back);
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(TransactionHistory.this, UserActivity.class);
                startActivity(intent);
            }
        });

        Button backtoshop = (Button) findViewById(R.id.btn_checkout);
        backtoshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(TransactionHistory.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }



    public void setuielements() {
        final SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        LinearLayout layout = (LinearLayout) findViewById(R.id.cartlayout);
        Log.e("Set UI Elements", strOrders);
        ProgressBar pgr = (ProgressBar) findViewById(R.id.progressBar1);
        pgr.setVisibility(View.GONE);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // default
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_error).delayBeforeLoading(50)
                .cacheInMemory(false) // default
                .cacheOnDisk(false)
                .considerExifParams(true).resetViewBeforeLoading(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        ImageLoader imageLoader = ImageLoader.getInstance();


        JSONArray jsonarray02 = null;

        try {
            jsonarray02 = new JSONArray(strOrders);
        } catch (JSONException e) {
            Log.e("String Orders Error", e.toString());
            e.printStackTrace();
        }


        if (jsonarray02 != null) {
                //iterate through transactions in JSON string and
                for (int i = 0; i < jsonarray02.length(); i++) {
                    LinearLayout itmcontr = new LinearLayout(this);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    layoutParams.setMargins(10, 10, 10, 10);

                    itmcontr.setOrientation(LinearLayout.HORIZONTAL);
                    itmcontr.setBackgroundColor(Color.WHITE);

                    imgstore[i] = new ImageView(this);
                    itmcontr.addView(imgstore[i]);

                    try {
                        JSONObject jsonobject = jsonarray02.getJSONObject(i);
                        OrderNo = jsonobject.getString("OrderNo");
                        OrderDate = jsonobject.getString("OrderDate");
                        Total = jsonobject.getString("totalprice");
                        Items = jsonobject.getString("items");
                    } catch (JSONException ex) {
                        Log.e("JSON Error: ", ex.toString());
                        ex.printStackTrace();
                    }

                    imageLoader.displayImage(imgurl, imgstore[i], options);
                    android.view.ViewGroup.LayoutParams imglayoutParams = imgstore[i].getLayoutParams();
                    imglayoutParams.width = 150;
                    imglayoutParams.height = 150;
                    imgstore[i].setLayoutParams(imglayoutParams);

                    Log.e("Setting Banner", imgurl);
                    LinearLayout orderinfo = new LinearLayout(this);
                    orderinfo.setOrientation(LinearLayout.VERTICAL);
                    TextView ONo = new TextView(this);
                    TextView ODate = new TextView(this);
                    TextView OItems = new TextView(this);
                    TextView OTotal = new TextView(this);
                    ODate.setText("Order Date: " + OrderDate);
                    ONo.setText("Order Number: " + OrderNo);
                    OItems.setText("Items in Order: " + Items);
                    OTotal.setText("US $" + Total);
                    OTotal.setTextColor(getResources().getColor(R.color.colorAmber));
                    OTotal.setTypeface(null, Typeface.BOLD);
                    OTotal.setTextSize(13);

                    orderinfo.addView(ONo);
                    orderinfo.addView(ODate);
                    orderinfo.addView(OItems);
                    orderinfo.addView(OTotal);
                    itmcontr.addView(orderinfo);
                    itmcontr.setId(Integer.parseInt(OrderNo));
                    itmcontr.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            // do stuff
                            String id1 = Integer.toString(view.getId());
                            SharedPreferences.Editor editor = shared.edit();
                            editor.putString(OrderNumber, id1);
                            editor.commit();
                            //Intent i = new Intent(TransactionHistory.this, Product.class);
                            
                            //startActivity(i);
                        }

                    });
                    layout.addView(itmcontr, layoutParams);
                }

            }


        }

        public void seterror(){
            LinearLayout layout = (LinearLayout) findViewById(R.id.cartlayout);
            ProgressBar pgr = (ProgressBar) findViewById(R.id.progressBar1);
            pgr.setVisibility(View.GONE);

            LinearLayout itmcontr = new LinearLayout(this);
            itmcontr.setBackgroundColor(Color.WHITE);
            TextView NetError = new TextView(this);
            NetError.setText("You are currenly offline, Please check your network connection... ");
            itmcontr.addView(NetError);
itmcontr.setVisibility(View.VISIBLE);
            layout.addView(itmcontr);
        }



    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  pDialog = new ProgressDialog(Store.this);
            pDialog.setMessage("Getting Product Details...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            */
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;

            try {
                // Building Parameters
                JSONObject json1 = new JSONObject();
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("uid", userID));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                json1 = jsonParser.makeHttpRequest(
                        GETTRANSACTION_HISTORY_URL, "POST", params);
                Log.e("Cart JSONing...", userID + GETTRANSACTION_HISTORY_URL);
                // check your log for json response
                if (json1 != null) {

                    Log.d("Login attempt", json1.toString());

                    // json success tag
                    success = json1.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.e("Get Cart Success", json1.getString(TAG_PRODUCTDETAILS));
                        strOrders = json1.getString(TAG_PRODUCTDETAILS);
                        return json1.getString(TAG_PRODUCTDETAILS);

                    } else {
                        return json1.getString(TAG_MESSAGE);
                    }
                } }
            catch(JSONException e){
                    e.printStackTrace();
                }

                return null;

            }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String posts) {
            // dismiss the dialog once product deleted
            //pDialog.dismiss();
            if (posts != null) {
                setuielements();
                Log.e("JSONing", posts);
            }

            else{
                seterror();
            }

        }

    }


    class ProcessRequest extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         *
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TransactionHistory.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag

            try {
                // Building Parameters
                total = 0.0;
                JSONObject json = new JSONObject();
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                dbHandler = new DatabaseHelper (getBaseContext());
                JSONArray JsonArr = new JSONArray();

                if (dbHandler.getProduct(currcart) != null) {
                    Cursor cursor01 = dbHandler.getProduct(currcart);
                    if (cursor01 != null && cursor01.moveToFirst()) {
                        Log.e("Cart Cursor Count", "Items in Cart: " + cursor01.getCount()
                                + "  PrdID:" + cursor01.getString(8)
                                + "  qnty:" + cursor01.getString(3)
                                + "  price:" + cursor01.getString(4)
                                + "  title:" + cursor01.getString(7)
                                + "  storeid:" + cursor01.getString(10)
                                + "  storename:" + cursor01.getString(11)
                                + "  seller:" + cursor01.getString(12)
                                + "  discount:"
                                + "  variation:"
                        );

                        do {
                            JSONObject jsonprdct = new JSONObject();
                            jsonprdct.put("productid", cursor01.getString(8));
                            jsonprdct.put("qnty", cursor01.getString(3));
                            jsonprdct.put("variation", "");
                            jsonprdct.put("price", cursor01.getString(4));
                            jsonprdct.put("ptitle", cursor01.getString(7));
                            jsonprdct.put("storeid", cursor01.getString(10));
                            jsonprdct.put("storename", cursor01.getString(11));
                            //TO DO SELLER WANGU
                            jsonprdct.put("seller", cursor01.getString(12));
                            jsonprdct.put("discount", "0.0");


                            JsonArr.put(jsonprdct);
                            Double SubTotal = 0.0;
                            total = total + SubTotal;
                        } while (cursor01.moveToNext());
                        cursor01.close();
                        Log.e("Cart Items JSON String", JsonArr.toString());


                        params.add(new BasicNameValuePair("cartitems", JsonArr.toString()));
                        params.add(new BasicNameValuePair("userid", uid));
                        params.add(new BasicNameValuePair("ptype", ptype));
                        params.add(new BasicNameValuePair("amt", totalPrc));

                        params.add(new BasicNameValuePair("oid", OrderID));


                        json = jsonParser.makeHttpRequest(
                                ECORESPONSE_URL, "POST", params);

                        Log.e("cart items", "" + JsonArr.toString());

                        try {
                            if (TAG_SUCCESS != null) {
                                success = json.getInt(TAG_SUCCESS);
                                Log.d("JSon Results", "Success-" + json.getInt(TAG_SUCCESS) + "  |Message-" + json.getString(TAG_PRODUCTDETAILS));
                            }
                        } catch (Exception e) {
                            Log.e("Ecocashresp error: ", e.getMessage());
                        }

                        if (success == 1) {

                            Log.e("SUCCESS", "you can delete " + OrderID);
                            DatabaseHelper  DatabaseHelper  = new DatabaseHelper (getBaseContext());

                            DatabaseHelper .clearAuctions();

                            Log.e("logging", "starting intent...............");
                            Intent intent = new Intent(TransactionHistory.this, TransactionHistory.class);
                            startActivity(intent);

                            Toast.makeText(TransactionHistory.this, " Payment successfull", Toast.LENGTH_LONG).show();

                            return json.getString(TAG_MESSAGE);


                        } else {
                            Log.e("Payment not valid!", json.getString(TAG_MESSAGE));

                            params.add(new BasicNameValuePair("oid", OrderID));
                            params.add(new BasicNameValuePair("cartitems", JsonArr.toString()));
                            json = jsonParser.makeHttpRequest(
                                    DELETEORDER_URL, "POST", params);

                            Log.e("Order cleared!", json.getString(TAG_MESSAGE));

                            Toast.makeText(TransactionHistory.this, "Payment validation failed, Please Try again later!", Toast.LENGTH_LONG).show();
                            return json.getString(TAG_MESSAGE);

                        }



                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Cart Async Task Error ", e.toString());

            }

            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String posts) {
            // dismiss the dialog once product deleted



            pDialog.dismiss();
            if (posts != null){
                Toast.makeText(TransactionHistory.this, posts, Toast.LENGTH_LONG).show();
            }
        }

    }






}

