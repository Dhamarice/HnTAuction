package com.hammerandtongues.online.hntauction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;


/**
 * Created by Ruvimbo on 2/2/2018.
 */
public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    private DatabaseHelper  dbHandler;
    public static final String MyPREFERENCES = "MyPrefs";
    private BroadcastReceiver broadcastReceiver;

    public String dayofyeartext, dayofyearstored;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {



            Thread.sleep(10000);

            super.onCreate(savedInstanceState);
            setContentView(R.layout.splashscreen);

            dbHandler = new DatabaseHelper (this);
            sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


            try {


                Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isfirstrun", true);


                Log.e("ISFIRST RUN IS  ", "Equals " + isFirstRun);


                if (isFirstRun) {
                    // do the thing for the first time



                    dbHandler.fill_auctionscontent(this);
                    dbHandler.fill_auctions(this);
                    dbHandler.fill_categories(this);
                    dbHandler.fill_locations(this);




                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isfirstrun", false).commit();

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("storedday", dayofyeartext);
                    editor.apply();


                    Log.e("Spalshscreen  ", "Starting Main Activity");
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    // other time your app loads

                    broadcastReceiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {


                            registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                            registerReceiver(new MyAppReciever(), new IntentFilter(Intent.ACTION_TIME_TICK));
                            Log.e("My App Receiver  ", "Registered My App receiver");


                        }
                    };

                    dayofyeartext = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
                    dayofyearstored = (sharedpreferences.getString("storedday", ""));

                    Log.e("My App day  ", "Previously stored day from splash screen was" + dayofyearstored);

                    new GetConnectionStatus().execute();

                    Log.e("Spalshscreen  ", "Starting Main Activity");
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);








/*
                        tickReceiver=new BroadcastReceiver(){
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                if(intent.getAction().compareTo(Intent.ACTION_TIME_TICK)==0)

                                {


                                    hourText = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                                    minuteText = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));




                                    timeofday = hourText + ":" + minuteText;

                                    Log.e("My App Receiver  ", "Time has changed my dear!" + timeofday );

                                    if (timeofday.contentEquals("13:25")) {

                                        Log.e("My App Receiver  ", "Time has changed has reached wanted my dear!" );

                                        new GetConnectionStatus().execute();

                                    }



                                }

                            }
                        };

*/
                    //Register the broadcast receiver to receive TIME_TICK
                    //registerReceiver(tickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));


                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            //}





        } catch (Exception ex) {
            Log.e("Splash Thread Exception", "Error: " + ex.toString());
            System.gc();
        }

    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this. getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




    class GetConnectionStatus extends AsyncTask<String, Void, Boolean> {

        private Context mContext;

        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected Boolean doInBackground(String... urls) {
            // TODO: Connect

            //pDialog.dismiss();
            if (isNetworkAvailable() == true) {


                try {
                    HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                    urlc.setRequestProperty("User-Agent", "Test");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.getConnectTimeout();
                    urlc.connect();

                    return (urlc.getResponseCode() == 200);

                } catch (IOException e) {
                    Log.e("Network Check", "Error checking internet connection", e);
                }
            } else {
                Log.e("Network Check", "No network available!");
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {


                if (result == true)

                {

                    Log.e("Result true", "The two not equal! " + dayofyeartext + " " + dayofyearstored);


                    if (!dayofyeartext.contentEquals(dayofyearstored)) {


                        Log.e("Conditon met", "The two not equal! " + dayofyeartext + " " + dayofyearstored);


                        //pDialog.show();
                        dbHandler = new DatabaseHelper (getBaseContext());


                        dbHandler.clearAuctionscontent();
                        dbHandler.clearAuctions();
                        dbHandler.clearCategories();
                        dbHandler.clearLocation();

                        dbHandler.fill_auctions(getBaseContext());
                        dbHandler.fill_categories(getBaseContext());
                        dbHandler.fill_locations(getBaseContext());
                        dbHandler.fill_auctionscontent(getBaseContext());

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("storedday", dayofyeartext);
                        editor.apply();

                    } else {
                        Log.e("Network Check", "The two are equal! " + dayofyeartext + " " + dayofyearstored);
                    }
                } else

                {

                }
            } catch (Exception ex) {
//                Toast.makeText(getContext(), "Weak / No Network Connection", Toast.LENGTH_SHORT).show();
            }







        }
    }





}