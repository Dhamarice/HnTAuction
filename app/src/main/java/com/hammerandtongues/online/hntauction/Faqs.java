package com.hammerandtongues.online.hntauction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Ruvimbo on 2/2/2018.
 */
public class Faqs extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    private TextView txtcartitems;
    private int cnt_cart,  flag=0, currcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faqs);

        System.gc();

        final SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        if (shared.getString("CartID", "") != null && shared.getString("CartID", "") != "") {
            currcart = Integer.parseInt(shared.getString("CartID", ""));
        } else {
            currcart = 0;
        }




        TextView aboutus = (TextView) findViewById(R.id.faqs);
        String abtus = "<br>" +
                "<b>Why and how should I Register and Log In?</b>" +
        "<br>" +
                "To be able to buy on the platform you need to fill in the registration form available <a href='https://shopping.hammerandtongues.com/wp-login.php?action=register'>here</a>, put in your personal information as well as create your username and password to your account. Once you become registered you will then be able to do your shopping on this site." +
                "<br><br>" +
                "<b>How do I make purchases?</b>" +
                "<br>" +
                "Simply go through the shops adding the items you want to buy into your shopping cart. When you are done shopping, click on checkout, choose whether you want to have your purchases delivered or you want to pick them up yourself and you will be asked to choose your payment method in order to complete the transaction." +
                "<br><br>" +
                "<b>What payment options are available?</b>" +
                "<br>" +
                "The following payment platforms are used on the Hammer & Tongues Shopping Mall site: - Vpayments - Paypal - Ecocash - Visa - Mastercard Your account also has an e-wallet provision where you can transfer funds to and make your payments from." +
                "<br><br>" +
                "<b>How soon will my order be delivered?</b>" +
                "<br>" +
                "You will get your order within 24hrs, unless otherwise advised, from the time of purchasing. You will get notifications from the time your order is packed into our delivery van up to the moment you sign confirming receipt of the order." +
                "<br><br>" +
                "<b>How do I know my order is secure?</b>" +
                "<br>" +
                "Goods will not be released from our delivery truck until you produce and input a valid PIN into our mobile units. You will receive this PIN number via SMS or email once you indicate that you want your order delivered to you." +
                "<br><br>" +
                "<b>Why should I buy online?</b>" +
                "<br>" +
                "Simple!!! Save time, Buy cheaper and have fun doing so." +
                "<br><br>" +
                "<b>Can I find neat stuff Online?</b>" +
                "<br>" +
                "You betcha!!! The Shopping Mall is laden with brand new quality stuff that you find when you physically do your shopping, with the only difference that you will find them cheaper online. Enjoy the same warranties from the original manufacturers as you would do buying inside any shop (applies for products issued with warranties from the manufacturer. ";
        aboutus.setText(Html.fromHtml(abtus));

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
            Intent intent = new Intent(Faqs.this, Search.class);

            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
