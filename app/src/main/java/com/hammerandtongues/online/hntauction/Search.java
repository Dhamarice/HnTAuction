package com.hammerandtongues.online.hntauction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
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
public class Search extends AppCompatActivity {


    public static final String MyPREFERENCES = "MyPrefs";

    public static final String Product = "idKey";
    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String GETSEARCH_PRODUCTS_URL = "https://devshop.hammerandtongues.com/webservice/getsearchproducts.php";
    private static final String BIDS_URL = "http://devauction.hammerandtongues.com/webservice/highest_bids.php";

    String PName,PPrice;
    String productID, name, won, post_id, closedate,  imgurl = "";
    String SearchTerm = "";
    String lastid, type = "Product";
    DatabaseHelper dbHandler;
    Boolean GetProducts = Boolean.FALSE;
    ImageView imgstore[] = new ImageView[200];
    private EditText txt_query;
    LinearLayout layout;
    TextView noresult;
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
               dbHandler = new DatabaseHelper(this);
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

                       //getsearch(SearchTerm);
                       getSearchResults(SearchTerm);

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
    int densityDpi = (int) (metrics.density * 160f);


    final SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

    DatabaseHelper dbHandler = new DatabaseHelper(this);

    if (dbHandler.searchProduct(SearchTerm) != null) {

        layout.removeAllViews();

        Cursor cursor01 = dbHandler.searchProduct(SearchTerm);

        if (cursor01 != null && cursor01.moveToFirst()) {

            Log.e("Product_list", "Values" + DatabaseUtils.dumpCursorToString(cursor01));


            int i2 = cursor01.getCount();
            for (int i = 0; i < i2; i++) {
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

                                new AlertDialog.Builder(Search.this)
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


                    } else if (won != null && won != "" && won != "0" && !won.contentEquals("0")) {


                        Button Place_Bid = new Button(this);
                        Place_Bid.setText("WON!");
                        Place_Bid.setTextColor(Color.WHITE);
                        Place_Bid.setTextSize(12);
                        Place_Bid.setId(Integer.parseInt(post_id));
                        Place_Bid.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                new AlertDialog.Builder(Search.this)
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

                                    new AlertDialog.Builder(Search.this)
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
                                    Intent i = new Intent(Search.this, Single_Product.class);
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


                                    new AlertDialog.Builder(Search.this)
                                            .setTitle("Info")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {


                                                    Intent intent = new Intent(Search.this, Products_list.class);
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

                                        Intent i = new Intent(Search.this, MainActivity.class);
                                        startActivity(i);

                                    }

                                }

                            }


                        });


                        int length = (int) (metrics.density * 100);
                        int width = (int) (metrics.density * 35);
                        //}// else {
                        // AddToCart.setText("Auction Only");
                        // AddToCart.setOnClickListener(new View.OnClickListener() {

                        //  @Override
                        // public void onClick(View view) {
                        // do stuff
                        //  Toast.makeText( Products_List.this, "Auction Only", Toast.LENGTH_SHORT).show();
                        //}

                        //});
                        // }

                        // int length = (int)(metrics.density *100);
                        //int width = (int)(metrics.density * 35);


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
                    /*
                    itmcontr.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            // do stuff
                            String id1 = Integer.toString(view.getId());
                            SharedPreferences.Editor editor = shared.edit();
                            editor.putString(Product, id1);
                            editor.commit();
                            Intent i = new Intent(Products_list.this, Single_Product.class);
                            startActivity(i);
                        }

                    });
                    */
                    layout.addView(itmcontr, layoutParams);



                    noresult.setVisibility(View.GONE);


                } catch (Exception ex) {
                    Log.e("Image Button Error", ex.toString());
                }
            }
            cursor01.moveToNext();


        } else {
            String msg = "";
            if (offset == 0) {
                msg = "Your search could not match any products!";
            } else {
                msg = "No Additional Products to Display";
            }
            noresult.setText(msg);
            noresult.setTextColor(getResources().getColor(R.color.colorAmber));
            noresult.setTypeface(null, Typeface.BOLD);
            noresult.setTextSize(16);
            layout.addView(noresult);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            noresult.setVisibility(View.VISIBLE);
        }
        noresult.setVisibility(View.GONE);

    }

    else {
        String msg = "";
        if (offset == 0) {
            msg = "Your search could not match any products!";
        } else {
            msg = "No Additional Products to Display";
        }
        noresult.setText(msg);
        noresult.setTextColor(getResources().getColor(R.color.colorAmber));
        noresult.setTypeface(null, Typeface.BOLD);
        noresult.setTextSize(16);
        layout.addView(noresult);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        noresult.setVisibility(View.VISIBLE);
    }

}




    public void addtoFav(String id1) {

        String curimgurl="";
//type_id = PID;
        DatabaseHelper  db = new DatabaseHelper (this);
        int qty = 1;

        int id = Integer.parseInt(id1.toString());
        //int catId = Integer.parseInt(categoryID);

        db.fillfav(id);

        Log.d("Notifications", "Picking up variables for saving" + String.valueOf((qty)) + " <= Quantity; " + String.valueOf(cartid) + "<=This is the current cart ID");

        Log.e("Notifications", "Finished saving an item" + id );

        Toast.makeText(Search.this , "Item added to Favourites!", Toast.LENGTH_LONG).show();
        Locale locale = new Locale("en", "US");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);



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

    }




