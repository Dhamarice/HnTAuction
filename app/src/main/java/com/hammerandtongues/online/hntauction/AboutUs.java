package com.hammerandtongues.online.hntauction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

/**
 * Created by Ruvimbo on 2/2/2018.
 */
public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus);

        System.gc();
        TextView aboutus = (TextView) findViewById(R.id.aboutus);
        String abtus = "<br>" +
                "<b>Company Values:</b><br>" +
                "<br>" +
                "Hammer and Tongues Africa is guided by the following values:<br>" +
                "<br>" +
                "<b>Innovation:</b> <br>" +
                "The operating environment is ever changing hence the need to continually innovate and reinvent. Customers are at the core of everything the company does and it will continually come up with new products/services to keep customers satisfied.<br>" +
                "<br>" +
                "<b>Integrity:</b> <br>" +
                "Hammer and Tongues is bound by honor, uprightness and honesty in everything it does and it can be counted on as worthy business partners.<br>" +
                "<br>" +
                "<b>Ability:</b>  <br>" +
                "Hammer and Tongues has the requisite mechanical and human resources to skillfully and efficiently execute our business role.vation and experience gained in the Zimbabwe market. It has already gained the conﬁdence of banks and embassies within that country and has been growing steadily since its formation.<br>";
        aboutus.setText(Html.fromHtml(abtus));
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
    }

    }
