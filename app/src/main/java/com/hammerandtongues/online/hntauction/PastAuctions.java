package com.hammerandtongues.online.hntauction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PastAuctions extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Category = "idKey";
    //public static final String Category = "idcateg";
    SharedPreferences sharedpreferences;

    private ProgressDialog pDialog ;
    private DatabaseHelper  dbHandler;

    private String post_id, name, startdate, closedate, splitmonthinwords, ppost_id,discount,deposit, bid_date, cdatevalue, UserID, bid_amnt, pname, minimun_increament, end_date, bid, pproduct_id, bid_fromserver , date_won;
    private int cartid, limit, offset, server_bid, my_bid;
    private ImageView imgstore[] = new ImageView[150];

    Context context;
    Calendar calendar = Calendar.getInstance();
    Calendar calenderfuture = Calendar.getInstance();
    int hourofday = calendar.get(Calendar.HOUR_OF_DAY);
    int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
    int month = calendar.get(Calendar.MONTH);
    int dayofmonth = calendar.get(Calendar.DAY_OF_MONTH);
    int houroftime = calendar.get(Calendar.HOUR);
    int minuteoftime = calendar.get(Calendar.MINUTE);
    int secondoftime = calendar.get(Calendar.SECOND);
    int yearofcal = calendar.get(Calendar.YEAR);
    public String PName, monthinwords, dayinwords, dayofyeartext, dayofyearstored, timeofday, previoussec = "1";
    private  TextView noresult;
    private LinearLayout noAuction, mybids;
    private TextView txtDay, txtHour, txtMinute, txtSecond, txtnomybid;
    private TextView tvEventStart;
    private Handler handler;
    private Runnable runnable;
    private int banner;
    int monthmatch = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_auctions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);


        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        UserID = sharedpreferences.getString("userid", "");

        final LinearLayout btnhome, btncategs, btnsearch, btncart, btnprofile;

        btnhome = (LinearLayout) findViewById(R.id.btn_home);
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PastAuctions.this, MainActivity.class);

                startActivity(intent);
            }
        });

        btncategs = (LinearLayout) findViewById(R.id.btn_Categories);
        btncategs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PastAuctions.this, Categories.class);

                startActivity(intent);
            }
        });

        btncart = (LinearLayout) findViewById(R.id.btn_Cart);
        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserID != null && UserID != ""){

                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString("request", "My bids");
                    editor.apply();

                    Intent intent = new Intent(PastAuctions.this, MyBids.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                else{

                    Toast ToastMessage = Toast.makeText(PastAuctions.this, "You are not logged in!", Toast.LENGTH_LONG);
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
                Intent intent = new Intent(PastAuctions.this, Login.class);

                startActivity(intent);
            }
        });

        btnsearch = (LinearLayout) findViewById(R.id.btn_Search01);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PastAuctions.this, Search.class);

                startActivity(intent);
            }
        });




        loaduielements();
    }


    public void loaduielements() {




        CoordinatorLayout drawerLayout = (CoordinatorLayout) findViewById(R.id.coordnate);
        LinearLayout timerclose = (LinearLayout) findViewById(R.id.relativeLayout);
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.auctsv);
        noresult = (TextView) findViewById(R.id.txtmaininfo);
        noAuction = (LinearLayout) findViewById(R.id.lnnoauction);
        dbHandler = new DatabaseHelper(this);
        //dbHandler.async_data();
        if (dbHandler.getallAuctions()!= null) {
            Cursor cursor = dbHandler.getallAuctions();
            if (cursor != null && cursor.moveToFirst()) {

                Log.e("Cursor Full", cursor.getColumnCount() + " Columns");
                Log.e("Locations", "Values" + DatabaseUtils.dumpCursorToString(cursor));

                int cartitms[] = new int[cursor.getCount()];

                for (int i = 0; i < cursor.getCount(); i++) {


                    ImageView aucimage = new ImageView(this);
                    TextView starttxt = new TextView(this);
                    TextView shopnametxt = new TextView(this);
                    TextView txtminbid = new TextView(this);
                    TextView txtdiscount = new TextView(this);
                    TextView txtdiposit = new TextView(this);


                    TextView txtdays = new TextView(this);
                    TextView txthours = new TextView(this);
                    TextView txtmin = new TextView(this);
                    TextView txtsec = new TextView(this);
                    LinearLayout expires_in = new LinearLayout (this);
                    LinearLayout discdep = new LinearLayout (this);
                    discdep.setOrientation(LinearLayout.HORIZONTAL);

                    expires_in.setOrientation(LinearLayout.HORIZONTAL);
                    expires_in.setBackgroundColor(getResources().getColor(R.color.colorLightness));
                    txtdays.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txthours.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtmin.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtsec.setTextColor(getResources().getColor(R.color.colorPrimary));

                    txtdiscount.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtdiposit.setTextColor(getResources().getColor(R.color.colorPrimary));

                    discdep.addView(txtdiposit);
                    discdep.addView(txtdiscount);

                    expires_in.addView(txtdays);
                    expires_in.addView(txthours);
                    expires_in.addView(txtmin);
                    expires_in.addView(txtsec);



                    LinearLayout hzvw = new LinearLayout(this);
                    LinearLayout.LayoutParams hzvwParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    hzvwParams.setMargins(12, 12, 12, 2);

                    hzvw.setOrientation(LinearLayout.VERTICAL);
                    hzvw.setBackgroundColor(getResources().getColor(R.color.colorLightness));


                    name = cursor.getString(3);
                    post_id = cursor.getString(1);
                    startdate = cursor.getString(9);
                    closedate = cursor.getString(8);
                    minimun_increament = cursor.getString(5);
                    deposit = cursor.getString(6);
                    discount = cursor.getString(7);
                    end_date = cursor.getString(8);


                    Log.e("DB Full", " Loading Views!");

                    if ( deposit == null || deposit.contentEquals("")){

                        deposit = "0";
                    }

                    if ( discount == null || discount.contentEquals("")){

                        discount = "0";
                    }



                    //Assigning banners

                    if (name.contains("GENERAL")){


                        banner = R.drawable.generalgoods;
                    }


                    else  if (name.contains("VEHICLE")){


                        banner = R.drawable.vihicles;
                    }


                    else  if (name.contains("FURNITURE")){


                        banner = R.drawable.furnitureauc;
                    }

                    else  if (name.contains("SMALL")){


                        banner = R.drawable.smallgoods;
                    }

                    else  if (name.contains("GENERATOR")){


                        banner = R.drawable.generators;
                    }

                    else  if (name.contains("LIVESTOCK")){


                        banner = R.drawable.livestock;
                    }

                    else  if (name.contains("SHEEP")){


                        banner = R.drawable.sheep;
                    }


                    else {


                        banner = R.drawable.generalgoodiss;
                    }






//Splitting date formatts


                    String CurrentString = startdate;
                    String[] separated = CurrentString.split(" ");
                    String startdayserver = separated[0];


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




                    countDownStart(closedayserver, txtdays, txthours, txtmin, txtsec);


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


                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyyy-MM-dd");
                        // Please here set your event date//YYYY-MM-DD
                        Date futureDate = dateFormat.parse(closedayserver);
                        Date currentDate = new Date();

                        if (currentDate.after(futureDate)){

                            shopnametxt.setTypeface(null, Typeface.BOLD);
                            shopnametxt.setPaintFlags(shopnametxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            shopnametxt.setText(name.toString().toUpperCase());
                            shopnametxt.setTextSize(20);
                            shopnametxt.setTextColor(getResources().getColor(R.color.colorAmber));


                            starttxt.setText("Started on:    " + splitday + "     " + splitmonthinwords + "     " + splityear);
                            starttxt.setBackgroundColor(getResources().getColor(R.color.colorLightness));
                            starttxt.setTextColor(getResources().getColor(R.color.colorPrimary));


                            txtdiscount.setText("Discount: " + discount+ "%");
                            txtdiposit.setText("Deposit: $" + deposit + "       ");


                            // TextView end = new TextView(this);
                            //end.setText(updateTimeOnEachSecond());
                            //end.setPadding(10, 20,  20, 0);


                            try {
                                imgstore[i] = aucimage;



                                hzvw.addView(shopnametxt, hzvwParams);

                                hzvw.addView(starttxt, hzvwParams);

                                hzvw.addView(expires_in, hzvwParams);

                                hzvw.addView(discdep, hzvwParams);

                                hzvw.addView(imgstore[i], hzvwParams);

                                //hzvw.addView(mylayout);
                                mylayout.addView(hzvw, hzvwParams);
                                //layout.addView(mylayout);


                                //imageLoader.displayImage(imgurl, imgstore[i], options);
                                Picasso.with(this).load(banner)
                                        .placeholder(R.drawable.progress_animation)
                                        .error(R.drawable.ic_launcher_59)
                                        .into(imgstore[i]);

                                //android.view.ViewGroup.LayoutParams layoutParams = imgstore[i].getLayoutParams();
                                //layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                //layoutParams.height = 305;
                                //imgstore[i].setLayoutParams(layoutParams);
                                //mylayout.setId(Integer.parseInt(post_id));

                                sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


                                hzvw.setId(Integer.parseInt(post_id));

                                cdatevalue = txtdays.getText().toString();

                            } catch (Exception ex) {
                                Log.e("Image Button Error", ex.toString());
                            }
                        }



                        hzvw.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                try {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                                            "yyyyy-MM-dd");
                                    // Please here set your event date//YYYY-MM-DD
                                    Date futureDate = dateFormat.parse(closedayserver);
                                    Date currentDate = new Date();

                                    if (currentDate.after(futureDate)){

                                        String id1 = Integer.toString(view.getId());
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(Category, id1);
                                        editor.putString("Aucstate", "closed");
                                        editor.putString("Type", "Auction");
                                        editor.commit();
                                        Intent i = new Intent(PastAuctions.this, Products_list.class);

                                        startActivity(i);
                                    }

                                    else {
                                        Log.e("Close date", "VALUE" + cdatevalue);
                                        // do stuff
                                        //String id1 = post_id;
                                        String id1 = Integer.toString(view.getId());
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(Category, id1);
                                        editor.putString("Type", "Auction");
                                        editor.commit();
                                        Intent i = new Intent(PastAuctions.this, Products_list.class);

                                        startActivity(i);
                                    }

                                }
                                catch (Exception e) {

                                    Log.e("Date Error", e.toString());
                                }

                            }

                        });




                    cursor.moveToNext();


                    } catch (Exception ex) {
                        Log.e("Image Button Error", ex.toString());
                    }
                }



                //noAuction.setVisibility(View.GONE);
            }else {

                Log.e("Cursor Null!", cursor.getColumnCount() + " Cursor Null!!~!!!");
                noresult.setText("We are working on out next auction, please come back to check later");
                Toast.makeText(this, "We are working on out next auction, please come back to check later", Toast.LENGTH_LONG).show();
                noAuction.setVisibility(View.VISIBLE);
            }


        }else {



            Log.e("DB Full",  " DataBase null!");
            //noresult.setText("We are working on out next auction, please come back to check later");
            Toast.makeText(this,"We are working on out next auction, please come back to check later",Toast.LENGTH_LONG).show();
           // noAuction.setVisibility(View.VISIBLE);
        }

    }


    public void countDownStart( final String closedate, final TextView day, final TextView hour, final TextView min, final  TextView sec) {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyyy-MM-dd");
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

                        day.setText("Auction" );
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



}
