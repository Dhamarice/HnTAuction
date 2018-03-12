package com.hammerandtongues.online.hntauction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Ruvimbo on 2/2/2018.
 */
public class Search extends AppCompatActivity {


    public static final String MyPREFERENCES = "MyPrefs";

    public static final String Product = "idKey";
    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String GETSEARCH_PRODUCTS_URL = "https://devshop.hammerandtongues.com/webservice/getsearchproducts.php";

    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_PRODUCTDETAILS = "posts";
    String storeID;
    String PName,PPrice, post_id="";
    String SearchTerm = "";
    String imgurl = "";
    String Search, Query;
    String type = "Product";
    String productID;
    Boolean GetProducts = Boolean.FALSE;
    ImageView imgstore[] = new ImageView[200];
    private EditText txt_query;
    LinearLayout layout;
    TextView noresult;
    private TextView txtcartitems;
    private int cnt_cart,  flag=0;
    int cartid, currcart, limit, offset;
    SharedPreferences shared;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.gc();
       try {
           try {
               super.onCreate(savedInstanceState);
               setContentView(R.layout.search);
               pDialog = new ProgressDialog(Search.this);
               pDialog.setMessage("Searching...");
               pDialog.setIndeterminate(false);
               pDialog.setCancelable(true);
               System.gc();
               final SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
               if (shared.getString("CartID", "") != null && shared.getString("CartID", "") != "") {
                   currcart = Integer.parseInt(shared.getString("CartID", ""));
               } else {
                   currcart = 0;
               }
               limit = 15;
               offset = 0;
               txt_query = (EditText) findViewById(R.id.searchterm);
               layout = (LinearLayout) findViewById(R.id.header);

               GetProducts = false;
               noresult = new TextView(this);

               Button search = (Button) findViewById(R.id.btn_search);
               search.setOnClickListener(new View.OnClickListener() {

                   @Override
                   public void onClick(View view) {

                       SearchTerm = txt_query.getText().toString();
                       layout.removeAllViews();
                   //    new GetConnectionStatus().execute();

                       getsearch(SearchTerm);

                   }
               });

               productID = (shared.getString("productID", ""));

               final LinearLayout btnhome, btncategs, btnsearch, btncart, btnprofile;

               btnhome = (LinearLayout) findViewById(R.id.btn_home);
               btnhome.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent = new Intent(Search.this, MainActivity.class);
                       startActivity(intent);
                   }
               });

               btncategs = (LinearLayout) findViewById(R.id.btn_Categories);
               btncategs.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent = new Intent(Search.this, MainActivity.class);
                       startActivity(intent);
                   }
               });



               btnprofile = (LinearLayout) findViewById(R.id.btn_Profile);
               btnprofile.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent = new Intent(Search.this, UserActivity.class);
                       startActivity(intent);
                   }
               });

               btnsearch = (LinearLayout) findViewById(R.id.btn_Search01);
               btnsearch.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent = new Intent(Search.this, Search.class);
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
       }
    catch (OutOfMemoryError  ex)
    {
        Log.e("Main Thread Exception", "Error: " + ex.toString());
        System.gc();
    }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


public  void getSearchResults(String Result)

{
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            // default
            .showImageOnLoading(R.drawable.ic_stub)
            .showImageForEmptyUri(R.drawable.ic_empty)
            .showImageOnFail(R.drawable.ic_error).delayBeforeLoading(50)
            .cacheInMemory(false) // default
            .cacheOnDisk(false)
            .considerExifParams(true).resetViewBeforeLoading(true).build();
    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
    ImageLoader.getInstance().init(config);
    ImageLoader imageLoader = ImageLoader.getInstance();


    DisplayMetrics metrics = getResources().getDisplayMetrics();
    int densityDpi = (int)(metrics.density * 160f);


    final SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

    JSONArray jsonarray02 = null;
    try {

      JSONObject object=new JSONObject(Result);

        jsonarray02=object.getJSONArray("posts");
    }
    catch (JSONException e) {Log.e("JSON Error: ", e.toString());
        Log.e("Detail of Error",Result);
        e.printStackTrace();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout itmcontr = new LinearLayout(this);
        itmcontr.setOrientation(LinearLayout.HORIZONTAL);
        itmcontr.setBackgroundColor(Color.WHITE);

        TextView resultno = new TextView(this);
        resultno.setText("No result could match your search!");
        resultno.setTextColor(getResources().getColor(R.color.colorAmber));
        resultno.setTypeface(null, Typeface.BOLD);
        resultno.setTextSize(20);

        itmcontr.addView(resultno);
        layout.addView(itmcontr, layoutParams);


    }

    Log.e("Get Product Flag", GetProducts.toString());
    if (jsonarray02 != null) {
        int i2 = jsonarray02.length();
        //iterate through products in JSON string and
        for (int i = 0; i < i2; i++) {
            LinearLayout itmcontr = new LinearLayout(this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(10, 10, 10, 10);

            itmcontr.setOrientation(LinearLayout.HORIZONTAL);
            itmcontr.setBackgroundColor(Color.WHITE);

            imgstore[i]  = new ImageView(this);
            itmcontr.addView(imgstore[i]);

            try {
                JSONObject jsonobject = jsonarray02.getJSONObject(i);
                PName = jsonobject.getString("name");
                post_id = jsonobject.getString("post_id");
                imgurl = jsonobject.getString("imgurl");
                PPrice = jsonobject.getString("price");
            } catch (JSONException e) {
                Log.e("JSON Error: ", e.toString());
                e.printStackTrace();
            }

            //imageLoader.displayImage(imgurl, imgstore[i], options);
            Picasso.with(this).load(imgurl)
                    .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.ic_error)
                    .into(imgstore[i]);

            android.view.ViewGroup.LayoutParams imglayoutParams = imgstore[i].getLayoutParams();

            int imglength = (int)(metrics.density *80);
            int imgwidth = (int)(metrics.density * 80);
            imglayoutParams.width = imglength;
            imglayoutParams.height = imgwidth;

            imgstore[i].setLayoutParams(imglayoutParams);

            Log.e("Setting Banner", imgurl);
            LinearLayout prdctinfo = new LinearLayout(this);
            prdctinfo.setOrientation(LinearLayout.VERTICAL);
            TextView ProductName = new TextView(this);
            TextView Price = new TextView(this);
            ProductName.setText(PName);
            Price.setText("US $"+PPrice);
            Price.setTextColor(getResources().getColor(R.color.colorAmber));
            Price.setTypeface(null, Typeface.BOLD);
            Price.setTextSize(13);

            LinearLayout.LayoutParams btnsize = new LinearLayout.LayoutParams(200, 60);
            btnsize.setMargins(20, 0, 20, 0);
            btnsize.gravity= Gravity.RIGHT;

            LinearLayout theBut = new LinearLayout(this);
            theBut.setOrientation(LinearLayout.HORIZONTAL);

            Button AddToCart = new Button(this);
            AddToCart.setText("View Product");
            AddToCart.setTextColor(getResources().getColor(R.color.colorAmber));
            AddToCart.setTypeface(null, Typeface.BOLD);
            AddToCart.setTextSize(13);
            AddToCart.setTextColor(Color.WHITE);
            AddToCart.setTextSize(12);
            //btnPopular.setPadding(15, 0, 15, 5);
            AddToCart.setLayoutParams(btnsize);
            AddToCart.setId(Integer.parseInt(post_id));
            //AddToCart.setGravity(View.FOCUS_RIGHT);
            AddToCart.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // do stuff
                    //addProduct(view, view.getId());

                    String id1 = Integer.toString(view.getId());
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString(Product, id1);
                    editor.commit();

                }

            });

            Button AddToFav = new Button(this);
            AddToFav.setText("Favourite");
            AddToFav.setTextColor(Color.WHITE);
            AddToFav.setTextSize(12);
            AddToFav.setId(Integer.parseInt(post_id));
            AddToFav.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // do stuff
                    String id1 = Integer.toString(arg0.getId());
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString(Product, id1);
                    editor.commit();
                    productID = (shared.getString(Product, ""));
                    addtoFav(id1);


                }

            });

            int length = (int)(metrics.density *100);
            int width = (int)(metrics.density * 35);


            LinearLayout.LayoutParams btnsize2 = new LinearLayout.LayoutParams(length,width,1);
            btnsize2.setMargins(15, 0, 15, 0);

            AddToCart.setLayoutParams(btnsize2);
            AddToCart.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            AddToFav.setLayoutParams(btnsize2);
            AddToFav.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            //prdctinfo.setOrientation(LinearLayout.HORIZONTAL);
            theBut.addView(AddToCart);

            theBut.addView(AddToFav);
            prdctinfo.addView(theBut);


            prdctinfo.addView(ProductName);
            prdctinfo.addView(Price);
            //prdctinfo.addView(AddToCart);
            itmcontr.addView(prdctinfo);
            itmcontr.setId(Integer.parseInt(post_id));
            itmcontr.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // do stuff
                    String id1 = Integer.toString(view.getId());
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString(Product, id1);
                    editor.commit();

                }

            });
            layout.addView(itmcontr, layoutParams);
        }
    }

    else {

        LinearLayout itmcontr = new LinearLayout(this);
        itmcontr.setOrientation(LinearLayout.HORIZONTAL);
        itmcontr.setBackgroundColor(Color.WHITE);

        TextView resultno = new TextView(this);
        resultno.setText("No result could match your search!");

        itmcontr.addView(resultno);

    }

}


    public void addtoFav(String id1) {

        String curimgurl="";
//type_id = PID;
        DatabaseHelper  db = new DatabaseHelper (this);
        int qty = 1;

        int id = Integer.parseInt(id1.toString());
        //int catId = Integer.parseInt(categoryID);

        Log.d("Notifications", "Picking up variables for saving" + String.valueOf((qty)) + " <= Quantity; " + String.valueOf(cartid) + "<=This is the current cart ID");

        Log.e("Notifications", "Finished saving an item" + id );

        Toast.makeText(Search.this , "Item added to Favourites!", Toast.LENGTH_LONG).show();
        Locale locale = new Locale("en", "US");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

        // CartManagement crtItms = db.getCartItemsCount(cartid);
        // if (crtItms != null) {
        //    Intent i = new Intent(Products_List.this, Products_List.class);
        //   startActivity(i);
        //   Toast.makeText(Products_List.this , "Network is Currently Unavailable", Toast.LENGTH_LONG).show();
        //idCartItems.setText("Items in cart: " + String.valueOf((crtItms.get_Cart_Items_Count())));
        //} else {
        //idCartItems.setText("Items in cart: 0");
        // }


    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

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
            Intent intent = new Intent(Search.this, Search.class);
            
            startActivity(intent);

            return true;
        }

        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return super.onOptionsItemSelected(item);
    }











    private void getsearch(final String search){
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());



        pDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, GETSEARCH_PRODUCTS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success",""+s);
                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonObject=new JSONObject(s);
                    int success=jsonObject.getInt("success");
                    getSearchResults(s);



                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), "An error accoured, please try again ", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(getApplicationContext(), "No internet connection! ", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                LinearLayout itmcontr = new LinearLayout(getBaseContext());
                itmcontr.setOrientation(LinearLayout.HORIZONTAL);
                itmcontr.setBackgroundColor(Color.WHITE);

                TextView resultno = new TextView(getBaseContext());
                resultno.setText("No result could match your search!");
                resultno.setTextColor(getResources().getColor(R.color.colorAmber));
                resultno.setTypeface(null, Typeface.BOLD);
                resultno.setTextSize(20);

                Log.e("RUEERROR",""+volleyError);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> values=new HashMap();
                values.put("search", search);

                return values;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);




    }







    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Search.this);
            pDialog.setMessage("Searching, Please Wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String pid = storeID;

            try {
                // Building Parameters
                JSONObject json1 =new JSONObject();
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                    params.add(new BasicNameValuePair("search", SearchTerm));
                    Log.d("request!", "starting");
                    // getting product details by making HTTP request
                    json1 = jsonParser.makeHttpRequest(
                            GETSEARCH_PRODUCTS_URL, "POST", params);
                    Log.e("PID + URL", storeID + GETSEARCH_PRODUCTS_URL);
                    // check your log for json response
                    Log.d("Set Banner001", json1.toString());
                // json success tag
                success = json1.getInt(TAG_SUCCESS);
                if (success == 1) {
                    return json1.getString(TAG_PRODUCTDETAILS);
                } else {
                    return json1.getString(TAG_MESSAGE);
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
            if (posts != null && GetProducts==Boolean.FALSE) {
                    //Toast.makeText(Search.this, posts, Toast.LENGTH_LONG).show();
                getSearchResults(posts);
                }

            }

        }




















     class GetConnectionStatus extends AsyncTask<String, Void, Boolean> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Search.this);
            pDialog.setMessage("Searching, Please Wait...");
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
               // new GetProductDetails().execute();
            }
                else{

                    noresult.setText("Network is Currently Unavailable");
                    layout.addView(noresult);

                    Toast.makeText( Search.this ,"Network is Currently Unavailable",Toast.LENGTH_LONG).show();
                }
            }
        }

    @Override
    public void onStop() {
        super.onStop();
        for (int i = 0; i<imgstore.length; i++ ) {
            Picasso.with(this).cancelRequest(imgstore[i]);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (int i = 0; i<imgstore.length; i++ ) {
            Picasso.with(this).cancelRequest(imgstore[i]);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (int i = 0; i<imgstore.length; i++ ) {
            Picasso.with(this).cancelRequest(imgstore[i]);
        }



        }
    }




