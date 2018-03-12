package com.hammerandtongues.online.hntauction;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Ruvimbo on 17/1/2018.
 */



public class MyAppReciever extends BroadcastReceiver {

    Calendar calendar = Calendar.getInstance();
    int hourofday = calendar.get(Calendar.HOUR_OF_DAY);

    String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);

    private Context context;
    private DatabaseHelper  db;
    private ProgressDialog pDialog;

    public void onReceive(Context context, Intent intent) {

        this.context = context;

        db = new DatabaseHelper (context);

        final String action = intent.getAction();

        if(intent.getAction().compareTo(Intent.ACTION_TIME_TICK)==0) {

            Log.e("My App Receiver  ", "Time has changed my dear!" + time );


            if (time.contentEquals("12:10")) {

                db.clearAuctions();



                //db.clearProducts();


                db.fill_auctions(context);



            }


        }

        else {

            Log.e("My App Receiver  ", "Hapana Chaitika!" );


        }


    }
}



