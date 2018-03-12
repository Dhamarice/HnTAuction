package com.hammerandtongues.online.hntauction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Ruvimbo on 2/2/2018.
 */
public class ContactUs  extends AppCompatActivity {
    private TextView txtcartitems;
    private int cnt_cart, flag = 0, currcart;
    private String url = "";
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactus);

        System.gc();
        TextView aboutus = (TextView) findViewById(R.id.contactus);
        String abtus = "<br>" +
                "<br>" +
                "\n" +
                "\n" +
                "<h4>Head Office</h4>\n" +
                "<p>Block 3 First Floor,Tendeseka Office Park<br />\n" +
                "Samora Machel Avenue,<br />\n" +
                "Harare Zimbabwe<br />\n" +
                "Tel: +263 (4) 790148 / 790149 / 791926<br />\n" +
                "Email: <a href=\"mailto:info@hammerandtongues.com?Subject=Customer%20Inquiry\" target=\"_top\">info@hammerandtongues.com</a><br />\n" +
                "Office Hours: Monday &#8211; Friday: 8:00 AM to 4:30 PM</p>\n" +
                "<h3><u>BRANCHES</u></h3>\n" +
                "<h4>Harare</h4>\n" +
                "<p>18005 Dhlela Way, Graniteside<br />\n" +
                "Harare, Zimbabwe<br />\n" +
                "Tel: +263 4 748118/20, 748647, 775814, 775289<br />\n" +
                "Email: <a href=\"mailto:online@hammerandtongues.com?Subject=Customer%20Inquiry\" target=\"_top\">online@hammerandtongues.com</a></p>\n" +
                "<h4>Bulawayo</h4>\n" +
                "<p>36 Josiah Chinamano, Belmont<br />\n" +
                "Bulawayo, Zimbabwe<br />\n" +
                "Tel: +263 9 77420, 880107<br />\n" +
                "Email: <a href=\"mailto:online@hammerandtongues.com?Subject=Customer%20Inquiry\" target=\"_top\">online@hammerandtongues.com</a></p>\n" +
                "<h4>Zambia</h4>\n" +
                "<p>Plot 7213/4 Cnr Kachidza/Mulaika Rds<br />\n" +
                "Off Lumumba Rd, Light Industrial Area<br />\n" +
                "Lusaka, Zambia<br />\n" +
                "Tel: +260 211 287334/5, +260 211 287327/8<br />\n" +
                "Email: <a href=\"mailto:online@hammerandtongues.com?Subject=Customer%20Inquiry\" target=\"_top\">online@hammerandtongues.com</a></p>\n" +
                "\t\t\t\n" +
                "\n";
        aboutus.setText(Html.fromHtml(abtus));


        final SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        if (shared.getString("CartID", "") != null && shared.getString("CartID", "") != "") {
            currcart = Integer.parseInt(shared.getString("CartID", ""));
        } else {
            currcart = 0;
        }


        ImageView fb, tw, yt, in;
        fb = (ImageView) findViewById(R.id.fb);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://www.facebook.com/hammerandtonguesshoppingmall";
                loadWebView();
            }
        });

        tw = (ImageView) findViewById(R.id.tw);
        tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://twitter.com/hntmall";
                loadWebView();
            }
        });

        yt = (ImageView) findViewById(R.id.yt);
        yt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://www.youtube.com/channel/UCXkVsn-icWGRkoqUuysVGyg";
                loadWebView();
            }
        });

        in = (ImageView) findViewById(R.id.in);
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://goo.gl/vNZDz5";
                loadWebView();
            }
        });
    }


    public void loadWebView() {

        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

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
            Intent intent = new Intent(ContactUs.this, Search.class);

            startActivity(intent);
            return true;
        }


        return false;

    }
}
