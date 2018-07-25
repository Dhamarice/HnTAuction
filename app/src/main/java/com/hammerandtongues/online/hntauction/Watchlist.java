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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.google.android.gms.common.api.GoogleApiClient;

import com.squareup.picasso.Picasso;

public class Watchlist extends AppCompatActivity {


    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Product = "idKey";
    SharedPreferences sharedpreferences;

    // Progress Dialog
    private ProgressDialog pDialog;



    String categoryID,PName, lastfavid, UserID, PPrice="";
    ImageView imgstore[] = new ImageView[1001];
    DatabaseHelper dbHandler;
    int offset;
    String productID, name, post_id, imgurl = "";
    String getlastid = "0";
    String lastid = "0";
    ImageView banner;
    int cartid, currcart;
    LinearLayout layout, productlist;
    View main;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);



        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        UserID = sharedpreferences.getString("userid", "");

        final LinearLayout btnhome, btncategs, btnsearch, btncart, btnprofile;

        btnhome = (LinearLayout) findViewById(R.id.btn_home);
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Watchlist.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btncategs = (LinearLayout) findViewById(R.id.btn_Categories);
        btncategs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Watchlist.this, Categories.class);

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

                    Intent intent = new Intent(Watchlist.this, MyBids.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                else{

                    Toast ToastMessage = Toast.makeText(Watchlist.this, "You are not logged in!", Toast.LENGTH_LONG);
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
                Intent intent = new Intent(Watchlist.this, Login.class);

                startActivity(intent);
            }
        });

        btnsearch = (LinearLayout) findViewById(R.id.btn_Search01);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Watchlist.this, Search.class);

                startActivity(intent);
            }
        });




        setuielements();


    }




    public void setuielements() {
        ProgressBar pgr = (ProgressBar) findViewById(R.id.progressBar3);
        pgr.setVisibility(View.GONE);

        final SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        LinearLayout layout = (LinearLayout) findViewById(R.id.watchlistlist);


        categoryID = (shared.getString("idKey", ""));
        productID = shared.getString("idKey", "");

        TextView noresult = (TextView) findViewById(R.id.txtinfo);


        DisplayMetrics metrics = getResources().getDisplayMetrics();

        int i;





            dbHandler = new DatabaseHelper(this);

            if (shared.getString("lastfavid", "") != null && shared.getString("lastfavid", "") != "") {

                getlastid = shared.getString("lastfavid", "");
            }

            Cursor cursor02 = dbHandler.viewFavs(getlastid);
            Log.e("Cursor", "Values" + DatabaseUtils.dumpCursorToString(cursor02));

            if (dbHandler.getProductFav(getlastid) != null) {
                Cursor cursor01 = dbHandler.getProductFav(getlastid);
                Log.e("Cursor", "Values" + DatabaseUtils.dumpCursorToString(cursor01));
                if (cursor02 != null && cursor02.moveToFirst()) {

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
                        lastfavid = cursor01.getString(0);
                        post_id = cursor01.getString(1);
                        imgurl = cursor01.getString(9);
                        PPrice = cursor01.getString(4);

                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString("lastfavid", lastfavid);
                        editor.commit();


                        Picasso.with(this).load(R.drawable.progress_animation)
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

                        LinearLayout prdctinfo = new LinearLayout(this);
                        prdctinfo.setOrientation(LinearLayout.VERTICAL);

                        TextView ProductName = new TextView(this);
                        TextView Price = new TextView(this);
                        ProductName.setText(PName);
                        Price.setText("Starting Price $" + PPrice);
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

                        Button AddToCart = new Button(this);
                        AddToCart.setText("View");
                        AddToCart.setTextColor(Color.WHITE);
                        AddToCart.setTextSize(12);
                        AddToCart.setId(Integer.parseInt(post_id));
                        AddToCart.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // do stuff
                                String id1 = Integer.toString(view.getId());
                                SharedPreferences.Editor editor = shared.edit();
                                editor.putString(Product, id1);
                                editor.commit();
                                Intent i = new Intent(Watchlist.this, Single_Product.class);
                                startActivity(i);


                            }

                        });


                        Button AddToFav = new Button(this);
                        AddToFav.setText("Remove");
                        AddToFav.setTextColor(Color.WHITE);
                        AddToFav.setTextSize(12);
                        AddToFav.setId(Integer.parseInt(post_id));
                        AddToFav.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // do stuff

                                final String id1 = Integer.toString(view.getId());


                                new AlertDialog.Builder(Watchlist.this)
                                        .setTitle("Remove Product")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {



                                                SharedPreferences.Editor editor = shared.edit();
                                                editor.putString(Product, id1);
                                                editor.commit();
                                                productID = (shared.getString(Product, ""));
                                                RemoveFav(id1);
                                                Intent i = new Intent(Watchlist.this, Watchlist.class);
                                                startActivity(i);



                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .setMessage(Html.fromHtml("Remove " + PName + " from Watchlist?" ))
                                        .show();




                            }

                        });


                        int length = (int) (metrics.density * 100);
                        int width = (int) (metrics.density * 35);

                        LinearLayout.LayoutParams btnsize2 = new LinearLayout.LayoutParams(length, width, 1);
                        btnsize2.setMargins(15, 0, 15, 0);

                        AddToCart.setLayoutParams(btnsize2);
                        AddToCart.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                        AddToFav.setLayoutParams(btnsize2);
                        AddToFav.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                        theBut.addView(AddToCart);

                        theBut.addView(AddToFav);
                        prdctinfo.addView(theBut);

                        prdctinfo.setLayoutParams(layoutParams);
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
                                Intent i = new Intent(Watchlist.this, Single_Product.class);
                                startActivity(i);
                            }

                        });
                        layout.addView(itmcontr, layoutParams);
                        cursor01.moveToNext();
                    }
                    noresult.setVisibility(View.GONE);
                } else {
                    String msg = "";
                    if (offset == 0) {
                        msg = "";
                    } else {
                        msg = "No Additional Products to Display";
                    }
                    noresult.setText(msg);
                    Snackbar snackbar = Snackbar
                            .make(productlist,"NO FAVOURITE PRODUCTS TO DISPLAY", Snackbar.LENGTH_LONG);

                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                    noresult.setVisibility(View.VISIBLE);
                }
                noresult.setVisibility(View.GONE);
            } else {
                String msg = "";
                if (offset == 0) {
                    msg = "";
                } else {
                    msg = "No Additional Products to Display";
                }
                noresult.setText(msg);

                Snackbar snackbar = Snackbar
                        .make(productlist,"NO FAVOURITE PRODUCTS TO DISPLAY", Snackbar.LENGTH_LONG);

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
                noresult.setVisibility(View.VISIBLE);
            }


    }


    public void RemoveFav(String id) {

        int idl = Integer.parseInt(id.toString());
        dbHandler.RemoveFavs(idl);

        Toast ToastMessage = Toast.makeText(this,"Item removed from Favourites!",Toast.LENGTH_LONG);
        View toastView = ToastMessage.getView();
        toastView.setBackgroundResource(R.drawable.toast_background);
        ToastMessage.show();

        Log.e("Deleting", "removing from favs");

    }




}
