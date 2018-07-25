package com.hammerandtongues.online.hntauction;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View.OnClickListener;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Ruvimbo on 2/2/2018.
 */



public class MainActivity extends AppCompatActivity
        implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener,NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Category = "idKey";
    //public static final String Category = "idcateg";
    SharedPreferences sharedpreferences;

    private static final String BIDS_URL = "http://devauction.hammerandtongues.com/webservice/highest_bids.php";

    private ProgressDialog pDialog ;
    private DatabaseHelper  dbHandler;
    private Button refresh, prevauct;


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


    private BroadcastReceiver broadcastReceiver;
    private static BroadcastReceiver tickReceiver;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
            context=this;
            //task = new DatabaseHelper .GetCategories();
            //View view = getLayoutInflater().inflate(R.layout.activity_home, null);
            dbHandler = new DatabaseHelper(this);

        refresh = (Button) findViewById(R.id.butn_refresh);


        prevauct = (Button) findViewById(R.id.btn_prevauc);




            sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);




        UserID = sharedpreferences.getString("userid", "");




            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
            ImageLoader.getInstance().init(config);

            TabLayout tab = (TabLayout) findViewById(R.id.tabs);
            tab.setVisibility(View.GONE);

            LinearLayout bottombar = (LinearLayout) findViewById(R.id.bottombar);
            bottombar.setVisibility(View.GONE);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        txtnomybid = (TextView) findViewById(R.id.txtmybid);
        mybids = (LinearLayout) findViewById(R.id.bidlist);


        loaduielements();

        if (sharedpreferences.getString("userid", "") != null && sharedpreferences.getString("userid", "") !="" && sharedpreferences.getString("userid", "") !="null" && !(sharedpreferences.getString("userid", "").contentEquals("null")) && !(sharedpreferences.getString("userid", "").contentEquals(""))){

            getmybids();

        }

        else {
            txtnomybid.setText("You are not logged in");

        }



        //pDialog = new ProgressDialog(MainActivity.this);
            //dbHandler.setProgressBar(progress);
            //Store Slider



       // } catch (Exception ex) {
          //  Log.e("Main Thread Exception", "Error: " + ex.toString());
            //System.gc();
        //}





        if (dayofweek == 1) { dayinwords = "SUN";}
        else if (dayofweek == 2) { dayinwords = "MON";}
        else if (dayofweek == 3) { dayinwords = "TUE";}
        else if (dayofweek == 4) { dayinwords = "WED";}
        else if (dayofweek == 5) { dayinwords = "THU";}
        else if (dayofweek == 6) { dayinwords = "FRI";}
        else if (dayofweek == 7) { dayinwords = "SAT";}



        if (month == 0) { monthinwords = "JANUARY";}
        else if (month == 1) { monthinwords = "FEBRUARY";}
        else if (month == 2) { monthinwords = "MARCH";}
        else if (month == 3) { monthinwords = "APRIL";}
        else if (month == 4) { monthinwords = "MAY";}
        else if (month == 5) { monthinwords = "JUNE";}
        else if (month == 6) { monthinwords = "JULY";}
        else if (month == 7) { monthinwords = "AUGUST";}
        else if (month == 8) { monthinwords = "SEPTEMBER";}
        else if (month == 19) { monthinwords = "OCTOBER";}
        else if (month == 10) { monthinwords = "NOVEMBER";}
        else if (month == 11) { monthinwords = "DECEMBER";}



        TextView tv = (TextView) findViewById(R.id.mydate);
        String ct = " " + dayinwords + " " + dayofmonth + " " + monthinwords;
        tv.setText(ct);




        String abtus = "<br>" +
                "<ol>\n" +
                "<li><strong>Introduction</strong></li>\n" +
                "</ol>\n" +
                "<p>These terms govern your use of <u>Hammer&Tongues Online Shopping mobile App</u> and your purchase of products from the different Stores on the App. By accepting these terms and conditions (including the linked information herein), and by using the Site, you represent that you agree to comply with these terms and conditions with Hammer &amp; Tongues Online (Private) Limited t/a Hammer and Tongues Shopping Mall (hereinafter “HT Shopping Mall”) in relation to your use of the Site (the &#8220;User Agreement&#8221;). This User Agreement is effective upon acceptance. Before you may become or continue as a member of the Site, you must read, agree with and accept this User Agreement. You should read this User Agreement and the <u>Privacy Policy</u> and access and read all further linked information referred to in this User Agreement, as such information contains further terms and conditions that apply to you as a user of the site. Such linked information including but not limited to Terms and Conditions on individual Store pages is hereby incorporated by reference into this User Agreement.</p>\n" +
                "<p><strong> </strong></p>\n" +
                "<ol start=\"2\">\n" +
                "<li><strong>Currency</strong></li>\n" +
                "</ol>\n" +
                "<p>All prices are quoted in US Dollar ($) and all transactions will be charged in US$. The actual amount to be paid in your home currency will be determined by the prevailing exchange rate of our bankers.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"3\">\n" +
                "<li><strong>Ordering</strong></li>\n" +
                "</ol>\n" +
                "<p>When buying an item, you agree that:</p>\n" +
                "<ul>\n" +
                "<li>You are responsible for reading the full item listing before making a commitment to buy.</li>\n" +
                "<li>You have read and agreed to be bound by the particular terms and conditions of sale applying to that item.</li>\n" +
                "<li>You enter into a legally binding contract to purchase an item when you commit to buy an item.</li>\n" +
                "<li>You are contracting with the respective Stores from which you are buying.</li>\n" +
                "</ul>\n" +
                "<ul>\n" +
                "<li>We do not transfer legal ownership of items from the seller to the buyer.</li>\n" +
                "<li>If you are buying on behalf of a company, you are confirming that you are authorised to transact on behalf of the company.</li>\n" +
                "</ul>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>Please note that:</p>\n" +
                "<ul>\n" +
                "<li>Placing goods in one’s Cart does not constitute an order.</li>\n" +
                "<li>An order is confirmed when one checks out and receives a <em>proforma</em></li>\n" +
                "<li>Goods can only be held in one’s Cart for 1 hour after check out. Goods not paid within one hour of checking out will automatically return to the store and will be available to other shoppers. For purchases for vehicles and such high valued products, whose purchase value may require special arrangements (like payment through bank RTG system) a deposit as specified by HT Shopping Mall should be paid within one hour of checking out as a show of commitment to buy. The remaining amount should be settled within 24hours. Kindly get in touch with the HT Shopping Mall where such arrangement is needed.</li>\n" +
                "</ul>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>In connection with using or accessing the Services you will not:</p>\n" +
                "<ul>\n" +
                "<li>use our Services if you are not able to form legally binding contracts (for example if you are under 18), or are temporarily or indefinitely suspended from using our sites, services, applications or tools;</li>\n" +
                "<li>fail to pay for items purchased by you, unless you have a valid reason as set out in respective online stores policy, for example, the seller has materially changed the item&#8217;s description after you bid, a clear typographical error is made, or you cannot contact the seller</li>\n" +
                "<li>post false, inaccurate, misleading, defamatory, or libellous content;</li>\n" +
                "<li>transfer your HT Shopping Mall account (including Feedback) and user ID to another party without our consent;</li>\n" +
                "<li>distribute or post spam, unsolicited or bulk electronic communications, chain letters, or pyramid schemes;</li>\n" +
                "<li>distribute viruses or any other technologies that may harm HT Shoping Mall, or the interests or property of users;</li>\n" +
                "<li>commercialize any HT Shopping Mall application or any information or software associated with such application;</li>\n" +
                "<li>harvest or otherwise collect information about users without their consent; or</li>\n" +
                "<li>circumvent any technical measures we use to provide the Services.</li>\n" +
                "</ul>\n" +
                "<p>If we believe or discover that you are abusing HT Shopping Mall in any of the ways mentioned above or otherwise, we may, in our sole discretion, take any steps to prevent and mitigate such abuse such as limiting, suspending, or terminating your user account(s) and access to our Services, delaying or removing hosted content, removing any special status associated with your account(s), reducing or eliminating any discounts, and taking technical and/or legal steps to prevent you from using our Services.</p>\n" +
                "<p>We may cancel unconfirmed accounts or accounts that have been inactive for a long time or modify or discontinue our Services. Additionally, we reserve the right to refuse or terminate our Services to anyone for any reason at our discretion.</p>\n" +
                "<ol start=\"4\">\n" +
                "<li><strong>Your Account and Registration Obligation</strong></li>\n" +
                "</ol>\n" +
                "<p>When you register as a member of the Site you have been or will be required to provide certain information and register a username and password for use on this Site. On becoming a member of the Site, you agree:</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ul>\n" +
                "<li>That you are responsible for maintaining the confidentiality of, and restricting access to and use of, your account and password, and accept responsibility for all activities that occur under your account and password.</li>\n" +
                "<li>To immediately notify HT Shopping Mall of any unauthorized use of your password or account or any other breach of security. In no event will HT Shopping Mall be liable for any direct, indirect or consequential loss or loss of profits, goodwill or damage whatsoever resulting from the disclosure of your username and/or password. You may not use another person&#8217;s account at any time, without the express permission of the account holder.</li>\n" +
                "<li>To reimburse HT Shopping Mall for any improper, unauthorized or illegal use of your account by you or by any person obtaining access to the Site, services or otherwise by using your designated username and password, whether or not you authorized such access.</li>\n" +
                "<li>You will provide true, accurate, current and complete information about yourself as prompted by HT Shopping Mall&#8217;s registration form. HT Shopping Mall may (in its sole discretion and at any time), make any inquiries it considers necessary (whether directly or through a third party), and request that you provide it with further information or documentation, including without limitation to verify your identity and/or proof of residents.</li>\n" +
                "</ul>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"5\">\n" +
                "<li><strong>Electronic Communications</strong></li>\n" +
                "</ol>\n" +
                "<p>You agree to receive calls, including autodialed and/or pre-recorded message calls, from HT Shopping Mall at any of the telephone numbers (including mobile telephone numbers) that we have collected, including telephone numbers you have provided us, or that we have obtained from third parties or collected by our own efforts. If the telephone number that we have collected is a mobile telephone number, you consent to receive SMS or other text messages at that number. Standard telephone minute and text charges may apply if we contact you at a mobile number or device. You agree we may contact you in the manner described above at the telephone numbers we have in our records for these purposes:</p>\n" +
                "<ul>\n" +
                "<li>To contact you for reasons relating to your account or your use of our Services (such as to resolve a dispute, or to otherwise enforce our User Agreement) or as authorized by applicable law</li>\n" +
                "<li>To contact you for marketing, promotional, or other reasons that you have either previously consented to or that you may be asked to consent to in the future.</li>\n" +
                "</ul>\n" +
                "<p>HT Shopping Mall may share your telephone numbers with our service providers (such as billing or collections companies) who we have contracted with to assist us in pursuing our rights or performing our obligations under the User Agreement, our policies, or any other agreement we may have with you. These service providers may also contact you using autodialed or pre-recorded messages calls and/or SMS or other text messages, only as authorized by us to carry out the purposes we have identified above, and not for their own purposes.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"6\">\n" +
                "<li><strong>Fees and Services</strong></li>\n" +
                "</ol>\n" +
                "<p>The Site is an online platform allowing for the sale and purchase of items between sellers and buyers. Membership on the Site is free. HT Shopping Mall does not charge any fee for browsing the Site.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"7\">\n" +
                "<li><strong>Payments</strong></li>\n" +
                "</ol>\n" +
                "<p>The HT Shopping Mall Payments system means that payment for items on the Site may be made online or partly online through the HT Shopping Mall payment facilities which HT Shopping Mall makes available on the Site. By providing these payment facilities, HT Shopping Mall is merely facilitating the making of online payments by buyers possible but HT Shopping Mall is not involved in the process of buying and selling items on the Site. All sales and purchases on the Site continue to be bipartite contracts between the buyer and the seller of an item(s) and HT Shopping Mall is not responsible for any non-performance, breach or any other claim relating to or arising out of any contract entered into between any buyers and sellers, nor does HT Shopping Mall have any fiduciary duty to any user.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"8\">\n" +
                "<li><strong>Pricing Policy</strong></li>\n" +
                "</ol>\n" +
                "<p>All prices are inclusive of VAT (where applicable) at the current rate and are correct at the time of entering the information onto the system. We reserve the right to amend prices without notice from time to time. The total cost of your order is the price of the products ordered plus delivery charges, where applicable. By completing the process for an on-line order you are confirming that the credit/debit card and Ecocash/Telecash account being used for the transaction(s) are yours.</p>\n" +
                "<p>Although we try to ensure all our prices displayed on our website are accurate, errors may sometimes occur. If we discover an error in the price of an item you have ordered we will contact you as soon as possible. You will have the option to reconfirm your order at the correct price or cancel it.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"9\">\n" +
                "<li><strong>Delivery</strong></li>\n" +
                "</ol>\n" +
                "<p>Delivery service is available on selected products. On these products, you may opt to have the purchased item/s delivered to your preferred address or you may opt to collect the purchased item/s from the supplier. If you opt for delivery, you will be notified of the delivery cost at checkout point.  You are required to make payment for the delivery service before goods are delivered. We will make every effort to facilitate the delivery of goods within the estimated timelines. However delays are occasionally inevitable due to unforeseen factors or events outside our control, for example extreme weather, a flood or fire. HT Shopping Mall is merely facilitating the process and shall be under no liability for any delay or failure to deliver the products within estimated timelines. Risk of loss and damage of products passes to you on the date when the products are delivered to you. The following delivery options are available.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><strong>Normal Delivery Service</strong></p>\n" +
                "<p>Goods will be delivered within 24hours of purchase.</p>\n" +
                "<p><strong>Express Delivery</strong></p>\n" +
                "<p>Express Delivery is same day service offered to buyers who are not prepared to wait for the normal delivery times. Delivery under this service is within 3 hours from the time of purchase. This service is available from 0900hrs to 1500hours Monday to Friday.</p>\n" +
                "<p><strong>After Hours Delivery Service</strong></p>\n" +
                "<p>HT Shopping Mall offers flexible delivery times for buyers who may not be available during normal business hours and choose to have their orders delivered late into the night. This service is available between 1700hrs to 2200hours Monday to Saturday.</p>\n" +
                "<p><strong>International Deliveries </strong></p>\n" +
                "<p>International Deliveries will require a separate arrangement from the above options.  Please contact HT Shopping Mall team for arrangements.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"10\">\n" +
                "<li><strong>Returns Policy</strong></li>\n" +
                "</ol>\n" +
                "<p>Please note that not all products are returnable and not all shops will accept returns. Each shop has a returns policy available on their site and you must understand each Store’s returns policy. To claim refund or exchange any item you are not completely happy with, contact us though our customer service email or our available phone lines elaborating the product name, receipt number, and reason of return within 48hours of the item being delivered to your nominated address or collected. HT Shopping Mall will forward the request to the shop the product was purchased. If a return agreement is reached please return items in their original condition, unused, in their original packaging, with garment tags and any other security devices still attached within 14 days. HT Shopping Mall, its officers, employees, agents, and affiliates shall not be liable if the store from which you purchased a product fails to return any goods as we are only facilitating the process. The following products cannot be returned once purchased:</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ul>\n" +
                "<li>Any products personalised to customer’s specifications;</li>\n" +
                "<li>Newspapers and magazines;</li>\n" +
                "<li>Goods sold by way of auction; and</li>\n" +
                "<li>Hampers, food, beverages and perishable goods(including flowers)</li>\n" +
                "</ul>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"11\">\n" +
                "<li><strong>Platform for Communication</strong></li>\n" +
                "</ol>\n" +
                "<p>The Site is a platform for communication whereby users may meet and interact with one another for the purpose of the sale and purchase of items. HT Shopping Mall does not buy or sell items. The Site cannot guarantee that a buyer or seller will complete a transaction or accept the return of an item or provide any refund for the same. HT Shopping Mall is not responsible for any non-performance or breach of any contract entered into between users and does not transfer legal ownership of items from the seller to the buyer. The contract for sale of any item shall be a strictly bipartite contract between the seller and the buyer. At no time shall any right, title or interest over any item vest with HT Shopping Mall nor shall HT Shopping Mall have any obligations or liabilities in respect of such item or the contract between the buyer and seller. HT Shopping Mall is not responsible for unsatisfactory or delayed performance, losses, damages or delays as a result of items which are unavailable.HT Shopping Mall is not required to mediate or resolve any dispute or disagreement between users. The Site has no control over and does not guarantee the quality, safety or legality of items advertised, the truth or accuracy of users&#8217; content or listings, the ability of sellers to sell items, or the ability of buyers to pay for items. HT Shopping Mall does not make any representation or warranty as to the attributes (including but not limited to quality, worth or marketability) of the items proposed to be sold or purchased on the Site. In particular, HT Shopping Mall does not implicitly or explicitly support or endorse the sale or purchase of any items on the Site, nor is HT Shopping Mall a supplier or manufacturer of any items sold by Stores or purchased by users. The Site does not make any representation or warranty as to the attributes (including but not limited to legal title, creditworthiness, or identity) of any of its users.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"12\">\n" +
                "<li><strong>Links to Third Party Websites</strong></li>\n" +
                "</ol>\n" +
                "<p>The Site may include links to third party websites that are controlled by and maintained by others. The Site cannot accept any responsibility for the materials or offers for goods or services featured on such websites and any link to such websites is not an endorsement of such websites or a warranty that such websites will be free of viruses or other such items of a destructive nature and you acknowledge and agree that HT Shopping Mall is not responsible for the content or availability of any such sites.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"13\">\n" +
                "<li><strong>Limitation of Liabilities</strong></li>\n" +
                "</ol>\n" +
                "<p>To the extent permitted by law, HT Shopping Mall, its officers, employees, agents, affiliates and suppliers shall not be liable for any loss or damage whatsoever whether direct, indirect, incidental, special, consequential or exemplary, including but not limited to, losses or damages for loss of profits, goodwill, business, opportunity, data or other intangible losses arising out of or in connection with your use of the Site, its services or this User Agreement (however arising, including negligence or otherwise and whether or not HT Shopping Mall has been advised of the possibility of such losses or damages). If you are dissatisfied with the Site or any content or materials on it, your sole exclusive remedy is to discontinue your use of it. Further, you agree that any unauthorised use of the Site and its services as a result of your negligent act or omission would result in irreparable injury to HT Shopping Mall and HT Shopping Mall shall treat any such unauthorised use as subject to the terms and conditions of this User Agreement.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"14\">\n" +
                "<li><strong>Indemnity</strong></li>\n" +
                "</ol>\n" +
                "<p>You agree to indemnify and hold HT Shopping Mall and its affiliates, officers, employees, agents and suppliers harmless from any and all claims, demands, actions, proceedings, losses, liabilities, damages, costs, expenses (including reasonable legal costs and expenses), howsoever suffered or incurred due to or arising out of your breach of this User Agreement, or your violation of any law or the rights of a third party.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"15\">\n" +
                "<li><strong>Warranties and Disclaimers </strong></li>\n" +
                "</ol>\n" +
                "<p>Neither HT Shopping Mall nor any of their officers, directors, employees or representatives represent or warrant:</p>\n" +
                "<ul>\n" +
                "<li>That the service, including its content, will meet your requirements or be accurate, complete, reliable, or error free;</li>\n" +
                "<li>That the service will always be available or will be uninterrupted, accessible, timely, or secure;</li>\n" +
                "<li>That any defects will be corrected, or that the service will be free from viruses, &#8220;worms,&#8221; &#8220;Trojan horses&#8221; or other harmful properties;</li>\n" +
                "<li>The accuracy, reliability, timeliness, or completeness of any review, recommendation, or other material published or accessible on or through the service or the site;</li>\n" +
                "<li>The availability for sale, or the reliability or quality of any products.</li>\n" +
                "</ul>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>You acknowledge that due to the nature of the Internet, we cannot guarantee that access to the Website will be uninterrupted or that e-mails or other electronic transmissions will be send to you or received by us. You expressly agree that use of this service is at your sole risk. To the fullest extent permissible under the applicable law, HT Shopping Mall and its affiliates disclaim all warranties of any kind, express or implied, including but not limited to, warranties of title, or implied warranties of merchantability or fitness for a particular purpose.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>We try to keep HT Shopping Mall and its Services safe, secure, and functioning properly, but we cannot guarantee the continuous operation of or access to our Services. Site updates and other notification functionality in HT Shopping Mall&#8217;s Services may not occur in real time. Such functionality is subject to delays beyond HT Shopping Mall&#8217;s control. Any Warranty or Guarantee on products is offered by the respective Stores and not HT Shopping Mall. Please refer each store’s Warranty and Guarantee Policy.</p>\n" +
                "<p><strong> </strong></p>\n" +
                "<ol start=\"16\">\n" +
                "<li><strong>Security</strong></li>\n" +
                "</ol>\n" +
                "<p>You are solely responsible for keeping your personal username and password secure and confidential. You should not disclose your username or password to any other party. Once logged on using your username and password whether authorised or unauthorised, you take full responsibility for the ensuing transactions once access to the site is obtained. If you believe that your username and/or password have been compromised or you are aware of any other breach of security regarding the Site, then you must notify us immediately.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"17\">\n" +
                "<li><strong>Intellectual Property </strong></li>\n" +
                "</ol>\n" +
                "<p>Save for any trademarks of the Stores featured on the Website, all contents of this Website including, but not limited to, the text, graphics, links and sounds are owned by HT Shopping Mall and may not be copied, downloaded, distributed or published in any way without their prior written consent, except that you may print, copy, download or temporarily store extracts for your personal information or when you use the Services.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"18\">\n" +
                "<li><strong>Applicable Law</strong></li>\n" +
                "</ol>\n" +
                "<p>Any dispute arising out of your use of this Website or material or content from this Website shall be resolved according to the laws of Zimbabwe. Zimbabwean Courts shall have exclusive jurisdiction over all claims against HT Shopping Mall.</p>\n" +
                "<ol start=\"19\">\n" +
                "<li><strong>Amendments</strong></li>\n" +
                "</ol>\n" +
                "<p>HT may amend this User Agreement at any time, and the amendments will be posted on the site. Changes take effect when they are posted on the HT site. Your continued use of this site after the changes have been posted means that you are in agreement with the changes.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><strong>HAMMER &amp; TONGUES ONLINE (PRIVATE) LIMITED – PRIVACY POLICY</strong></p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>https://shopping.hammerandtongues.com/ (&#8220;the Website&#8221;) is owned and operated by Hammer &amp; Tongues Online (Private) Limited t/a Hammer &amp; Tongues Shopping Mall (HT Shopping Mall), part of the Hammer &amp; Tongues Africa Holdings Group. This policy, together with our <a href=\"http://www.tesco.com/help/terms-and-conditions/\">Terms and Conditions</a>, explain how HT Shopping Mall may use information we collect about you. By accessing the Website you confirm to have understood and agreed to this Policy and to our <a href=\"http://www.tesco.com/help/terms-and-conditions/\">Terms and Conditions</a>.</p>\n" +
                "<p><strong> </strong></p>\n" +
                "<ol>\n" +
                "<li><strong>Information we collect about you</strong></li>\n" +
                "</ol>\n" +
                "<p><strong> </strong></p>\n" +
                "<p>Collecting your personal information helps HT Shopping Mall to improve the goods and services offered on the Website and to administer and operate your account with us. More importantly, we use your information to check and verify your identity, and to prevent or detect crime. We may share your personal information across the Hammer &amp; Tongues Africa Holdings Group so they can provide you with relevant products and services and we may share your information with the suppliers of the products and services offered on the Website.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>We collect information about you when you:</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol>\n" +
                "<li>Register an account with us;</li>\n" +
                "<li>Visit the Website;</li>\n" +
                "<li>Buy any products or services offered on the Website;</li>\n" +
                "<li>Contact us over the telephone or in writing; and</li>\n" +
                "<li>Complete a survey or questionnaire.</li>\n" +
                "</ol>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"2\">\n" +
                "<li><strong>Mailing lists and off-Website marketing</strong></li>\n" +
                "</ol>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>By opening an account with us or making a purchase on the Website, you agree that we may contact you with offers and information about products or services offered through the Website and for feedback on your experience with us.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"3\">\n" +
                "<li><strong>Cookies</strong></li>\n" +
                "</ol>\n" +
                "<p>We may use cookies to give you the best possible shopping experience on the Website. Cookies are text files containing small amounts of information which are downloaded to your personal computer, mobile or other device when you visit a website. Cookies are then sent back to the originating website on each subsequent visit, or to another website that recognises that cookie. Cookies are useful because they allow a website to recognise a user&#8217;s device. Cookies do lots of different jobs, like letting you navigate between pages efficiently, remembering your preferences, and generally improve the user experience. They can also help to ensure that adverts you see online are more relevant to you and your interests.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"4\">\n" +
                "<li><strong>Other websites</strong></li>\n" +
                "</ol>\n" +
                "<p><strong> </strong></p>\n" +
                "<p>The Website may contain links to other sites which are outside our control and not covered by this policy. The operators of these sites may collect information from you that will be used by them in accordance with their policy, which may differ from ours.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<ol start=\"5\">\n" +
                "<li><strong>Liability</strong></li>\n" +
                "</ol>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>HT Shopping Mall disclaims and has no liability for all loss or damage, direct special consequential or otherwise, personal injury or expense of whatsoever nature, arising out of or in connection with the use of the Website and you hereby indemnify HT Shopping Mall and hold it harmless against all loss, liability, damage, direct, special, consequential or otherwise or personal injury or expense of whatsoever nature which may be suffered by you or such third party arising out of or in connection with the use of the Website.</p>\n" +
                "<p><strong> </strong></p>\n" +
                "<ol start=\"6\">\n" +
                "<li><strong>Amendments to Policy</strong></li>\n" +
                "</ol>\n" +
                "<p>We reserve the right to change the policy at any time and you are therefore encouraged to constantly read through the policy for any changes.</p>\n" +
                "<p>&nbsp;</p>";






        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean agreed = sharedPreferences.getBoolean("agreedauction",false);
        if (!agreed) {
            new AlertDialog.Builder(this)
                    .setTitle("Terms and Conditions")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("agreedauction", true);
                            editor.commit();
                        }
                    })
                    .setNegativeButton("Decline", null)
                    .setMessage(Html.fromHtml(abtus))
                    .show();
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

                        day.setText("Auction " );
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


                    LinearLayout hzvw = new LinearLayout(this);
                    LinearLayout.LayoutParams hzvwParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    hzvwParams.setMargins(10,10,10,10);


                    ImageView aucimage = new ImageView(this);
                    TextView shopnametxt = new TextView(this);
                    shopnametxt.setLayoutParams(hzvwParams);
                    Button depozit = new Button(this);
                    depozit.setLayoutParams(hzvwParams);
                    TextView txtdiscount = new TextView(this);
                    txtdiscount.setLayoutParams(hzvwParams);
                    TextView txtdiposit = new TextView(this);
                    txtdiposit.setLayoutParams(hzvwParams);


                    LinearLayout discdep = new LinearLayout (this);
                    discdep.setOrientation(LinearLayout.HORIZONTAL);

                    txtdiscount.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtdiposit.setTextColor(getResources().getColor(R.color.colorPrimary));

                    discdep.addView(txtdiposit);
                    discdep.addView(txtdiscount);

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


                    Log.e("DB Full", " Loading Views! " + name);

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
                    String starttimeserver = separated[1];


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

                    String sortclosetime = splitclosehour + ":" + splitcloseminute + ":" + splitclosesecond;

                    String sortstartdate = startdayserver + "T" + starttimeserver;

                    String sortclosedate = closedayserver + "T" + sortclosetime;

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
                                "yyyy-MM-dd'T'HH:mm:ss");
                        // Please here set your event date//YYYY-MM-DD
                        Date futureDate = dateFormat.parse(sortclosedate);
                        Date StartfutureDate = dateFormat.parse(sortstartdate);
                        Date currentDate = new Date();

                        calendar.setTime(futureDate);
                        calendar.setTime(StartfutureDate);
                        Log.e("FUTURE DATE", "loaduielements: " + currentDate + " FutureDate: " + futureDate + "Raw end date " + sortclosedate);

                        if (!(currentDate.after(futureDate))) {



                            shopnametxt.setTypeface(null, Typeface.BOLD);
                            shopnametxt.setPaintFlags(shopnametxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            shopnametxt.setText(name.toString().toUpperCase());
                            shopnametxt.setTextSize(20);
                            shopnametxt.setTextColor(getResources().getColor(R.color.colorAmber));


                            txtdiscount.setText("Discount: " + discount + "%");
                            txtdiposit.setText("Deposit: $" + deposit + "       ");

                            depozit.setText("Deposit");
                            depozit.setBackgroundResource(R.drawable.default_rounded);



                            try {
                                imgstore[i] = aucimage;


                                hzvw.addView(shopnametxt, hzvwParams);



                                //hzvw.addView(expires_in, hzvwParams);

                                hzvw.addView(discdep, hzvwParams);

                                hzvw.addView(imgstore[i], hzvwParams);

                                hzvw.addView(depozit, hzvwParams);

                                //hzvw.addView(mylayout);
                                mylayout.addView(hzvw);

                                //layout.addView(mylayout);


                                //imageLoader.displayImage(imgurl, imgstore[i], options);
                                Picasso.with(this).load(banner)
                                        .placeholder(R.drawable.progress_animation)
                                        .error(R.drawable.ic_launcher_59)
                                        .into(imgstore[i]);


                                sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


                                hzvw.setId(Integer.parseInt(post_id));



                            } catch (Exception ex) {
                                Log.e("Image Button Error", ex.toString());
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




                                        if (currentDate.after(futureDate)) {


                                            Toast.makeText(context, "Sorry, This auction has been closed", Toast.LENGTH_LONG).show();
                                        } else {

                                            // do stuff
                                            //String id1 = post_id;
                                            String id1 = Integer.toString(view.getId());
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString(Category, id1);
                                            editor.putString("Type", "Auction");
                                            editor.commit();
                                            Intent i = new Intent(MainActivity.this, Products_list.class);

                                            startActivity(i);
                                        }

                                    } catch (Exception e) {

                                        Log.e("Date Error", e.toString());
                                    }

                                }

                            });

                        }





                    } catch (Exception ex) {
                        Log.e("Image Button Error", ex.toString());
                    }

                    cursor.moveToNext();


                }



                if  (mylayout.getChildCount() == 0){

                    Log.e("mylayout Null!", mylayout.getChildCount() + " mylayout Null!!~!!!");


                    noresult.setText("We are working on out next auction, please come back to check later");
                    noresult.setTypeface(null, Typeface.BOLD);
                    noAuction.setVisibility(View.VISIBLE);

                }



                //noAuction.setVisibility(View.GONE);
            }else {

                Log.e("Cursor Null!", cursor.getColumnCount() + " Cursor Null!!~!!!");
                noresult.setText("We are working on out next auction, please come back to check later");
                Toast.makeText(this, "We are working on out next auction, please come back to check later", Toast.LENGTH_LONG).show();
                noAuction.setVisibility(View.VISIBLE);
            }


        }else {




            Log.e("DB Full",  " DataBase null!" + prevauct +" " + refresh);
            noresult.setText("We are working on out next auction, please come back to check later");
            Toast.makeText(this,"We are working on out next auction, please come back to check later",Toast.LENGTH_LONG).show();
            noAuction.setVisibility(View.VISIBLE);


            refresh.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Log.e("REFRESH BUTTON",  " ONclick!"  + refresh);
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);

                    startActivity(intent);
                }

            });


            prevauct.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Log.e("PREVAUC BUTTON",  " ONclick!"  + refresh);
                    Intent intent = new Intent(MainActivity.this, PastAuctions.class);

                    startActivity(intent);

                }
            });
        }

    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_search) {
            Intent intent = new Intent(MainActivity.this, Search.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.refresh) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        SharedPreferences.Editor editor = sharedpreferences.edit();

        if (id == R.id.myaccount) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.mybids) {




            if (UserID != null && UserID != ""){

                editor.putString("request", "My bids");
                editor.apply();

                Intent intent = new Intent(MainActivity.this, MyBids.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            else{

                Toast ToastMessage = Toast.makeText(MainActivity.this, "You are not logged in!", Toast.LENGTH_LONG);
                View toastView = ToastMessage.getView();
                toastView.setBackgroundResource(R.drawable.toast_background);
                ToastMessage.show();
            }


        }else if (id == R.id.mypastbids) {

            if (UserID != null && UserID != ""){

                editor.putString("request", "Past bids");
                editor.apply();

                Intent intent = new Intent(MainActivity.this, MyBids.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            else{

                Toast ToastMessage = Toast.makeText(MainActivity.this, "You are not logged in!", Toast.LENGTH_LONG);
                View toastView = ToastMessage.getView();
                toastView.setBackgroundResource(R.drawable.toast_background);
                ToastMessage.show();
            }


        }
        else if (id == R.id.pastauctions) {

            Intent intent = new Intent(MainActivity.this, PastAuctions.class);
            startActivity(intent);
        }
        else if (id == R.id.categories) {


            Intent intent = new Intent(MainActivity.this, Categories.class);
            startActivity(intent);

        }


        else if (id == R.id.locations) {


            Intent intent = new Intent(MainActivity.this, Locations.class);
            startActivity(intent);

        }

        else if (id == R.id.watchlist) {


            Intent intent = new Intent(MainActivity.this, Watchlist.class);
            startActivity(intent);

        }

        else if (id == R.id.upcomingauction) {

            Intent intent = new Intent(MainActivity.this, UpcomingAuctions.class);
            startActivity(intent);


        } else if (id == R.id.upcomingauction) {

        } else if (id == R.id.calender) {
            Intent intent = new Intent(MainActivity.this, Calender.class);
            startActivity(intent);

        }  else if (id == R.id.about_us) {
            Intent intent = new Intent(MainActivity.this, AboutUs.class);
            startActivity(intent);

        } else if (id == R.id.contact_us) {
            Intent intent = new Intent(MainActivity.this, ContactUs.class);
            startActivity(intent);

        } else if (id == R.id.terms) {
            Intent intent = new Intent(MainActivity.this, TermsAndConditions.class);
            startActivity(intent);
        }
        else if (id == R.id.faqs) {
            Intent intent = new Intent(MainActivity.this, Faqs.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        //final MenuItem awesomeMenuItem = menu.findItem(R.id.cart_item);
        //RelativeLayout cart_wrapper = (RelativeLayout) menu.findItem(R.id.cart_item).getActionView().findViewById(R.id.cartitemwrapper);
        //get cart items count, set notification item value

//        txtcartitems = (TextView) cart_wrapper.findViewById(R.id.cartitems);
        //      txtcartitems.setText(String.valueOf(cnt_cart));

        //    View awesomeActionView = awesomeMenuItem.getActionView();
        //  awesomeActionView.setOnClickListener(new View.OnClickListener() {
        // @Override
        //public void onClick(View v) {
        //        onOptionsItemSelected(awesomeMenuItem);
        //  }
        //});
        return true;

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        String Store= "idcateg";
        String id1;
        if (slider.getBundle().get("extra")=="Garfunkels \"Pork Shop\"") {
            id1 = "61935";
        }
        else  if (slider.getBundle().get("extra")=="Bata Shoe Shop") {
            id1 ="51135";
        }
        else  if (slider.getBundle().get("extra")=="SeedCo") {
            id1 ="46810";
        }
        else  if (slider.getBundle().get("extra")=="Food Lovers Market") {
            id1 = "28735";
        }
        else  {
            id1 ="1236";
        }

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Store, id1);
        editor.commit();

        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}



    @Override
    public void onStop() {
        super.onStop();
        if(tickReceiver!=null)
            unregisterReceiver(tickReceiver);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        String id1 =null;
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Intent intent;
        System.gc();
        switch (v.getId()) {
            case R.id.btn_home:
                intent = new Intent(MainActivity.this, MainActivity.class);

                startActivity(intent);
                //finish();
                break;

            case R.id.btn_Categories:

                intent = new Intent(MainActivity.this, Categories.class);

                startActivity(intent);



                //finish();
                break;


            case R.id.btn_Cart:
                if (UserID != null && UserID != ""){

                    editor = sharedpreferences.edit();

                    editor.putString("request", "My bids");
                    editor.apply();

                   intent = new Intent(MainActivity.this, MyBids.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                else{

                    Toast ToastMessage = Toast.makeText(MainActivity.this, "You are not logged in!", Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_background);
                    ToastMessage.show();
                }
                break;

            case R.id.btn_Profile:
                intent = new Intent(MainActivity.this, Login.class);

                startActivity(intent);
                break;

            case R.id.btn_Search01:
                intent = new Intent(MainActivity.this, Search.class);

                startActivity(intent);
                //finish();
                break;

            default:
                //Toast.makeText(this,v.toString() + "Clicked (Invalid Object Call", Toast.LENGTH_LONG).show();

                Toast ToastMessage = Toast.makeText(MainActivity.this,"Clicked (Invalid Object Call",Toast.LENGTH_LONG);
                View toastView = ToastMessage.getView();
                toastView.setBackgroundResource(R.drawable.toast_background);
                ToastMessage.show();


                break;
        }



    }



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

                       String bid =jsonobject.getString("bid");
                        String date_chosen =jsonobject.getString("date_chosen");

                        dbHandler.update_bids( bid, date_chosen, Pid);




                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //pDialog.dismiss();
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


    public void getmybids() {





        if (dbHandler.getmybids(UserID)!= null) {
            Cursor cursor = dbHandler.getmybids(UserID);
            if (cursor != null && cursor.moveToFirst()) {

                if (cursor.getString(0) != null) {

                    Log.e("Locations", "Values" + DatabaseUtils.dumpCursorToString(cursor));

                    int cartitms[] = new int[cursor.getCount()];

                    for (int i = 0; i < cursor.getCount(); i++) {


                        LinearLayout hzvw = new LinearLayout(this);
                        LinearLayout.LayoutParams hzvwParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        hzvwParams.setMargins(0, 0, 0, 0);

                        hzvw.setOrientation(LinearLayout.VERTICAL);
                        hzvw.setBackgroundColor(getResources().getColor(R.color.colorLightness));


                        pname = cursor.getString(9);
                        ppost_id = cursor.getString(1);
                        pproduct_id = cursor.getString(3);
                        bid_date = cursor.getString(2);
                        bid_amnt = cursor.getString(7);
                        bid_fromserver = cursor.getString(8);
                        date_won = cursor.getString(5);


                        Log.e("DB Full", " Loading Views!");

                        get_bids(pproduct_id);


                        if (bid_fromserver != null && bid_fromserver != "" && !bid_fromserver.contentEquals("null")) {

                            server_bid = Integer.parseInt(bid_fromserver);
                        } else {
                            server_bid = 0;

                            Log.e("SERVERBID", "amount: " + bid_fromserver + " for product: " + pname);

                        }

                        if (bid_amnt != null && bid_amnt != "") {
                            my_bid = Integer.parseInt(bid_amnt);

                        } else {

                            my_bid = 0;

                        }
                        TextView txtmybid = new TextView(this);
                        txtmybid.setTextColor(getResources().getColor(R.color.colorPrimaryDarkold));
                        txtmybid.setTextSize(18);

                        txtmybid.setTypeface(null, Typeface.BOLD);

                        if (server_bid <= my_bid) {

                            txtmybid.setText(pname + " bid is Winning!!!");
                        } else {

                            txtmybid.setText(pname + " bid is Loosing!!!");

                        }

                        if (date_won == null || date_won == "" || date_won.contentEquals("null") || date_won.contentEquals("0")) {

                            mybids.addView(txtmybid);


                        }


                        cursor.moveToNext();
                    }


                    //noAuction.setVisibility(View.GONE);
                }
                else {

                    txtnomybid.setTypeface(null, Typeface.BOLD);
                    txtnomybid.setText("You have not placed any bids!");
                }
            }else {

                txtnomybid.setTypeface(null, Typeface.BOLD);
                txtnomybid.setText("You have not placed any bids!");
            }


        }else {



            txtnomybid.setTypeface(null, Typeface.BOLD);
            txtnomybid.setText("You have not placed any bids!");
        }

    }






}