package com.hammerandtongues.online.hntauction;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import preferences.MyPref;
import preferences.MyPrefAddress;

/**
 * Created by NgonidzaIshe on 18/6/2016.
 */
public class Login extends AppCompatActivity {


    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences shared;
    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();


    private static final String LOGIN_URL = "https://devauction.hammerandtongues.com/webservice/login.php";
    private static final String LOGINN_URL = "https://devauction.hammerandtongues.com/webservice/register.php";
    private static final String UPDATE_URL = "https://devauction.hammerandtongues.com/webservice/updatedetails.php";
    private static final String RESET_URL = "https://devauction.hammerandtongues.com/webservice/ResetPass.php";
    private static final String FIELDS_URL = "https://devshop.hammerandtongues.com/webservice/getsurburbs.php";


    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_CATEGORIES = "posts";
    TextView ewallet, pemail, address, welcum, ewall, addwarning, membership, points, addresslbl, plsaddlbl, mobilenum;
    int success = 0;

    EditText username, password, nameoremail;
    String uname, pword, uEname, fEname, eEmail, sEname, Plogin, Pconfirm, userReset, oldpassword;
    String Email, uName, membership_level, loyalty_points, number, Balance = "";
    String request_type, Address = "";
    String UserID = "";
    RelativeLayout tione;
    ImageView symbolcoin;
    LinearLayout add_details, forgotform, input_container, inside, BasicForm, notifyform, my_finances;
    Button update, logout, basic, shopping, addadd, back, bac, updat, reset, btnnotify, deposit_money, withdraw, my_transactions, transfer_credits, redeem_points, back_to_profile, finances, registeration;
    EditText txtadd1, txtadd2, txtidno, uEName, fName, sName, email, pword_login, pword_confrim, oldpass;
    String idno, add1, add2, suburb, city, region, country, default_country, default_city, default_region,default_suburb ;
    AutoCompleteTextView txtsurbub, txtcity, txtregion;
    Spinner txtcountry;

    boolean isClicked = true;
    PopupWindow popUpWindow;
    LinearLayout.LayoutParams layoutParams;
    LinearLayout mainLayout;
    Button btnresend, btnforgot;
    LinearLayout containerLayout;
    TextView txtuser;
    //Global Variables

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            //View view = inflater.inflate(R.layout.login, container, false);


            Runtime.getRuntime().gc();

            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_login);

            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            shared = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            System.gc();


            input_container = (LinearLayout) findViewById(R.id.input_container);
            forgotform = (LinearLayout) findViewById(R.id.forgotform);
            notifyform = (LinearLayout) findViewById(R.id.notifyform);
            BasicForm = (LinearLayout) findViewById(R.id.BasicForm);
            add_details = (LinearLayout) findViewById(R.id.add_details);
            my_finances = (LinearLayout) findViewById(R.id.my_finances);
            btnforgot = (Button) findViewById(R.id.btnforgot);
            btnnotify = (Button) findViewById(R.id.btnnotify);
            btnresend = (Button) findViewById(R.id.btnresend);
            registeration = (Button) findViewById(R.id.btnregistration);
            addadd = (Button) findViewById(R.id.add_add);
            logout = (Button) findViewById(R.id.logout);
            update = (Button) findViewById(R.id.update);
            back = (Button) findViewById(R.id.bac);
            updat = (Button) findViewById(R.id.updat);
            bac = (Button) findViewById(R.id.back);
            basic = (Button) findViewById(R.id.basicInfo);
            reset = (Button) findViewById(R.id.reset);
            shopping = (Button) findViewById(R.id.shopping);
            finances = (Button) findViewById(R.id.finances);
            deposit_money = (Button) findViewById(R.id.deposit_money);
            withdraw = (Button) findViewById(R.id.withdraw);
            //my_transactions = (Button) view.findViewById(R.id.my_transactions);
            transfer_credits = (Button) findViewById(R.id.transfer_credits);
            redeem_points = (Button) findViewById(R.id.redeem_points);
            back_to_profile = (Button) findViewById(R.id.back_to_profile);
            tione = (RelativeLayout) findViewById(R.id.tione);
            inside = (LinearLayout) findViewById(R.id.inside);
            welcum = (TextView) findViewById(R.id.pusername);
            pemail = (TextView) findViewById(R.id.pemail);
            ewall = (TextView) findViewById(R.id.ewall);
            addwarning = (TextView) findViewById(R.id.addwarning);
            membership = (TextView) findViewById(R.id.membershiplevel);
            points = (TextView) findViewById(R.id.loyalty_points);
            addresslbl = (TextView) findViewById(R.id.paddresslabel);
            plsaddlbl = (TextView) findViewById(R.id.pladdress);
            mobilenum = (TextView) findViewById(R.id.mobilenum);
            nameoremail = (EditText) findViewById(R.id.nameoremailll);
            txtadd1 = (EditText) findViewById(R.id.addressLn1);
            txtadd2 = (EditText) findViewById(R.id.addressLn2);
            txtsurbub = (AutoCompleteTextView) findViewById(R.id.surbub);
            txtcity = (AutoCompleteTextView) findViewById(R.id.city);
            txtregion = (AutoCompleteTextView) findViewById(R.id.regionstate);
            txtcountry = (Spinner) findViewById(R.id.country);
            txtidno = (EditText) findViewById(R.id.idno);
            uEName = (EditText) findViewById(R.id.uName);
            fName = (EditText) findViewById(R.id.fName);
            sName = (EditText) findViewById(R.id.sName);
            email = (EditText) findViewById(R.id.eEmail);
            pword_login = (EditText) findViewById(R.id.pword_login);
            pword_confrim = (EditText) findViewById(R.id.pword_confrim);
            oldpass = (EditText) findViewById(R.id.oldpassword);
            symbolcoin = (ImageView) findViewById(R.id.coin);


            uEName.setText(shared.getString("uname", ""));
            uEName.setEnabled(false);
            fName.setText(shared.getString("Fname", ""));
            sName.setText(shared.getString("sname", ""));
            email.setText(shared.getString("umail", ""));
            email.setEnabled(false);

            username = (EditText) findViewById(R.id.username);
            password = (EditText) findViewById(R.id.password);
            Button signin = (Button) findViewById(R.id.btnlogin);
            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    uname = username.getText().toString();
                    pword = password.getText().toString();
                    loginMethod(pword, uname);
                    // new AttemptLogin().execute();

                }

            });


            logout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Log.e("Logout: ", "UserId: " + shared.getString("userid", "") + "Address: " + shared.getString("address", ""));


                    SharedPreferences.Editor editor = shared.edit();

                    editor.remove("userid");
                    editor.apply();
                    editor.commit();

                    editor.remove("address");
                    editor.apply();
                    editor.commit();

                    input_container.setVisibility(View.VISIBLE);
                    inside.setVisibility(View.GONE);


                    Log.e("Logout: ", "UserId: " + shared.getString("userid", "") + "Address: " + shared.getString("address", ""));


                }

            });




            registeration.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Login.this, Register.class);
                    startActivity(intent);
                }

            });


            shopping.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                }

            });

            addadd.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    //idcontainer.setVisibility(View.GONE);
                    inside.setVisibility(View.GONE);
                    BasicForm.setVisibility(View.GONE);
                    tione.setVisibility(View.VISIBLE);
                    add_details.setVisibility(View.VISIBLE);
                }

            });

            deposit_money.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("request_type", "deposit");
                    editor.apply();

                    Intent myintent = new Intent(Login.this, MyFinances.class);
                    Login.this.finish();
                    startActivity(myintent);
                }

            });

            withdraw.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("request_type", "withdraw");
                    editor.apply();

                    Intent myintent = new Intent(Login.this, MyFinances.class);
                    Login.this.finish();
                    startActivity(myintent);
                }

            });


            transfer_credits.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("request_type", "transfer_credits");
                    editor.apply();

                    Intent myintent = new Intent(Login.this, MyFinances.class);
                    Login.this.finish();
                    startActivity(myintent);
                }

            });

            redeem_points.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("request_type", "redeem_points");
                    editor.apply();

                    Intent myintent = new Intent(Login.this, MyFinances.class);
                    Login.this.finish();
                    startActivity(myintent);
                }

            });

            back_to_profile.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    tione.setVisibility(View.GONE);
                    my_finances.setVisibility(View.GONE);
                    inside.setVisibility(View.VISIBLE);


                }
            });

            finances.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    //idcontainer.setVisibility(View.GONE);
                    inside.setVisibility(View.GONE);
                    BasicForm.setVisibility(View.GONE);
                    tione.setVisibility(View.VISIBLE);
                    my_finances.setVisibility(View.VISIBLE);
                }

            });


            basic.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    //idcontainer.setVisibility(View.GONE);
                    inside.setVisibility(View.GONE);
                    tione.setVisibility(View.VISIBLE);
                    BasicForm.setVisibility(View.VISIBLE);
                    add_details.setVisibility(View.GONE);
                }

            });

            back.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Login.this, Login.class);
                    startActivity(intent);

                }
            });

            bac.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Login.this, Login.class);
                    startActivity(intent);


                }
            });

            updat.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (uEName.getText().toString().contentEquals("") || fName.getText().toString().contentEquals("") || sName.getText().toString().contentEquals("") ||
                            email.getText().toString().contentEquals("")) {
                        //Toast.makeText(getContext(), "Fields cannot be empty!", Toast.LENGTH_LONG).show();


                        Toast ToastMessage = Toast.makeText(Login.this, "Fields cannot be empty!!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();

                    } else {

                        UserID = (shared.getString("userid", ""));
                        uEname = uEName.getText().toString();
                        fEname = fName.getText().toString();
                        sEname = sName.getText().toString();
                        eEmail = email.getText().toString();


                        saveReg(sEname, UserID, fEname, uEname);
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString("address", add1 + add2);
                        editor.commit();
                    }
                }
            });


            reset.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {


                    UserID = (shared.getString("userid", ""));

                    if (pword_login.getText().toString().contentEquals("") || pword_confrim.getText().toString().contentEquals("")) {

                        //Toast.makeText(getContext(), "Password fields can not be empty for reset!", Toast.LENGTH_LONG).show();

                        Toast ToastMessage = Toast.makeText(Login.this, "Password fields can not be empty for reset!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();


                    }

                    Plogin = pword_login.getText().toString();
                    Pconfirm = pword_confrim.getText().toString();
                    oldpassword = oldpass.getText().toString();
                    userReset = "notnecessary";
                    if (Plogin.contentEquals(Pconfirm)) {
                        ResetPass(Plogin, UserID, userReset, oldpassword);
                    } else {

                        //Toast.makeText(getContext(), "Passwords do not match!", Toast.LENGTH_LONG).show();

                        Toast ToastMessage = Toast.makeText(Login.this, "Passwords do not match!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();
                    }


                }
            });


            btnforgot.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    input_container.setVisibility(View.GONE);
                    forgotform.setVisibility(View.VISIBLE);

                }
            });


            btnresend.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (nameoremail.getText().toString().contentEquals("")) {

                        //Toast.makeText(getContext(), "Please enter your username or Email", Toast.LENGTH_LONG).show();

                        Toast ToastMessage = Toast.makeText(Login.this, "Please enter your username or Email!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();


                    } else {
                        userReset = nameoremail.getText().toString();
                        UserID = "unknownhapanazeronada";
                        Plogin = "unknownhapanazeronada";
                        oldpassword = "notthere";

                        ResetPass(Plogin, UserID, userReset, oldpassword);
                    }

                }
            });

            btnnotify.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Login.this, Login.class);
                    startActivity(intent);

                }
            });


// MY SPINNERS

            //DISPLAYING COUNTRIES


            Locale[] locales = Locale.getAvailableLocales();
            ArrayList<String> countries = new ArrayList<String>();
            for (Locale locale : locales) {
                String country = locale.getDisplayCountry();

                if (country.trim().length() > 0 && !countries.contains(country)) {
                    countries.add(country);
                }
            }

            default_country = MyPrefAddress.getCountry(Login.this);
            if (default_country != null) {
                countries.add(default_country);
            }
            Collections.sort(countries);
            for (String country : countries) {
                System.out.println(country);
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Login.this,
                    android.R.layout.simple_spinner_item, countries){

                @Override
                public int getCount() {
                    return super.getCount()-1;            // you don't display last item. It is used as hint.
                }

            };
            // set the view for the Drop down list
            dataAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // set the ArrayAdapter to the spinner



            txtcountry.setAdapter(dataAdapter);
            txtcountry.setSelection(countries.indexOf(default_country));

            System.out.println("# countries found: " + countries.size());

            txtcountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    final String newValue = (String) txtcountry.getItemAtPosition(position);

                    country = newValue;

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });


            getspinnerfields(txtregion, "Province", "not applicable");

            txtregion.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {


                    region = txtregion.getText().toString();

                    Log.e("On text changed", "afterTextChanged: region" + region);
                    getspinnerfields(txtcity, "City", region);

                    // TODO Auto-generated method stub
                }
            });


            txtcity.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                    Log.e("On text changed", "onTextChanged: region" + region);
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {


                    city = txtcity.getText().toString();

                    Log.e("On text changed", "afterTextChanged: region" + city);
                    getspinnerfields(txtsurbub, "Surburb", city);

                    // TODO Auto-generated method stub
                }
            });


            update.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {


                    //else if (next.getText().toString().trim().toUpperCase().contentEquals("SAVE")){

                    if (txtadd1.getText().toString().contentEquals("") || txtadd2.getText().toString().contentEquals("") || txtsurbub.getText().toString().contentEquals("") ||
                            txtcity.getText().toString().contentEquals("") || txtregion.getText().toString().contentEquals("") || txtidno.getText().toString().contentEquals("")) {
                        //Toast.makeText(getContext(), "All fields are mandatory", Toast.LENGTH_LONG).show();

                        Toast ToastMessage = Toast.makeText(Login.this, "All fields are mandatory!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();


                    } else {

                        // Log.e("Submit Button Text", save.getText().toString());
                        idno = txtidno.getText().toString();
                        add1 = txtadd1.getText().toString();
                        add2 = txtadd2.getText().toString();
                        suburb = txtsurbub.getText().toString();


                        SaveDetails(add1, add2, country, region, city, suburb, idno);

                    }
                }
            });


            if (shared.getString("userid", "") != null && shared.getString("userid", "") != "" && shared.getString("userid", "") != "null" && !(shared.getString("userid", "").contentEquals("null")) && !(shared.getString("userid", "").contentEquals(""))) {
                UserID = (shared.getString("userid", ""));
                Address = (shared.getString("address", ""));
                uName = (shared.getString("uname", ""));
                Email = (shared.getString("umail", ""));
                Balance = (shared.getString("balance", ""));
                membership_level = (shared.getString("membership", ""));
                loyalty_points = (shared.getString("points", ""));
                number = (shared.getString("telno", ""));
                request_type = shared.getString("request_type", "");
                input_container.setVisibility(View.GONE);


                // new GetConnectionStatus().execute();

                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                //  welcum.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                //ewall.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                //}


                welcum.setText(Html.fromHtml("<b>" + uName + "</b>"));
                pemail.setText(Html.fromHtml("<b>" + Email + "</b>"));
                mobilenum.setText(Html.fromHtml("<b>" + number + "</b>"));


                if (Address != null && Address != "" && Address != "null null null null null" && !Address.contentEquals("null null null") && !Address.contentEquals("null null null null null") && !Address.contentEquals("null")) {

                    plsaddlbl.setText("Edit address details!");


                    addwarning.setText(Html.fromHtml("<b>" + Address + "</b>"));


                    txtidno.setText(shared.getString("idno", ""));
                    txtadd1.setText(MyPrefAddress.getAddress1(Login.this));
                    txtadd2.setText(MyPrefAddress.getAddress2(Login.this));
                    txtsurbub.setText(MyPrefAddress.getSurbub(Login.this));
                    txtcity.setText(MyPrefAddress.getCity(Login.this));
                    txtregion.setText(MyPrefAddress.getRegion(Login.this));

                    default_country = MyPrefAddress.getCountry(Login.this);

                    inside.setVisibility(View.VISIBLE);

                } else {

                    addwarning.setTextColor(Color.BLACK);
                    addwarning.setText("Please register your address details, for a better shopping experience!!!");
                    addresslbl.setText(" ");


                    inside.setVisibility(View.GONE);
                    BasicForm.setVisibility(View.GONE);
                    tione.setVisibility(View.VISIBLE);
                    add_details.setVisibility(View.VISIBLE);


                    sName.setText(shared.getString("sname", ""));
                    fName.setText(shared.getString("Fname", ""));


                }

                if (request_type.contentEquals("to-finances")){


                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("request_type");
                    editor.apply();
                    inside.setVisibility(View.GONE);
                    BasicForm.setVisibility(View.GONE);
                    tione.setVisibility(View.VISIBLE);
                    my_finances.setVisibility(View.VISIBLE);

                }


                if (Balance != null && isNumeric(Balance)) {
                    if (Double.parseDouble(Balance) == 0.00) {
                        ewall.setTextColor(Color.RED);
                    } else {
                        ewall.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    ewall.setTextColor(Color.RED);
                    Balance = "$0.00";
                }

                ewall.setText("$" + Balance);


                if (membership_level == null || membership_level.contentEquals("") || membership_level.contentEquals("null")) {
                    membership_level = "Bronze";
                }
                membership.setText(Html.fromHtml("<b>" + membership_level + "</b>"));


                if (membership_level.contentEquals("Platinum")) {

                    symbolcoin.setImageResource(R.drawable.platinumalltypes);
                } else if (membership_level.contentEquals("Gold")) {

                    symbolcoin.setImageResource(R.drawable.goldalltypes);
                } else if (membership_level.contentEquals("Silver")) {

                    symbolcoin.setImageResource(R.drawable.silveralltypes);
                } else if (membership_level.contentEquals("Bronze")) {

                    symbolcoin.setImageResource(R.drawable.bronzealltypes);
                }

                if (loyalty_points == null || loyalty_points.contentEquals("") || loyalty_points.contentEquals("null")) {
                    loyalty_points = "0";
                }
                points.setText(Html.fromHtml("<b>" + loyalty_points + " points </b>"));




            } else {

                welcum.setText(Html.fromHtml("<b>You are Logged in, \n \n Hello!</b> "));

                addwarning.setText(Html.fromHtml("<b>Happy shopping!!!</b> "));
                //inside.setVisibility(View.VISIBLE);

                //Toast.makeText(getContext(), "PLEASE ADD YOUR ADDRESS DETAILS!", Toast.LENGTH_LONG).show();

            }




            pDialog.dismiss();


        } catch (Exception ex) {
            Log.e("Main Thread Exception", "Error: " + ex.toString());
            System.gc();

        }


    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) Login.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*
     public void nexttab()
     {
         TabLayout tabLayout=(TabLayout) Login.this.findViewById(R.id.tabs02);
         final ViewPager viewPager = (ViewPager)Login.this.findViewById(R.id.viewpager02);
         final PagerAdapter adapter = new PagerAdapter02 (getFragmentManager(), tabLayout.getTabCount());
         viewPager.setAdapter(adapter);
         viewPager.setCurrentItem(1);
         //Log.e("Next 02", "Thus Far the lord has brought us");
     }
 */
    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    private void loginMethod(final String uname, final String pword) {
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success", "" + s);
                Log.e("Zitapass", "" + pword);
                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonobject = new JSONObject(s);
                    int success = jsonobject.getInt("success");
                    String message = jsonobject.getString("message");
                    if (success == 1) {


                        JSONArray array = jsonobject.getJSONArray("posts");

                        JSONObject object = array.getJSONObject(0);


                        String post_id = object.optString("id");
                        String address1 = object.optString("add1");
                        String address2 = object.optString("add2");
                        String mycity = object.optString("city");
                        String mysurbub = object.optString("surbub");
                        String province = object.optString("province");
                        String country = object.optString("country");
                        String uname = object.optString("uname");
                        String Fname = object.optString("firstname");
                        String sname = object.optString("lastname");
                        String umail = object.optString("umail");
                        String balance = object.optString("balance");
                        String number = object.optString("mobile_number");
                        String membership = object.optString("level");
                        String points = object.optString("loyalty_points");
                        String idno = object.optString("idno");
                        String telno = object.optString("cellno");
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString("userid", post_id);
                        editor.putString("address", address1 + " " + address2 + " " + mysurbub);

                        MyPrefAddress.saveAllAddress(Login.this, address1, address2, mycity, province, mysurbub, country);
                        MyPref.savePhoneNumber(Login.this, telno);

                        editor.putString("uname", uname);
                        editor.putString("Fname", Fname);
                        editor.putString("sname", sname);
                        editor.putString("umail", umail);
                        editor.putString("balance", balance);
                        editor.putString("telno", telno);
                        editor.putString("membership", membership);
                        editor.putString("idno", idno);
                        editor.putString("points", points);
                        editor.putString("options", "checkout");
                        editor.commit();
                        editor.apply();

                        //zvese.setVisibility(View.VISIBLE);
                        //input_container.setVisibility(View.GONE);
                        //inside.setVisibility(View.VISIBLE);


                        if (shared.getString("placebid", "") != null && shared.getString("placebid", "") != "") {

                            editor.remove("placebid");
                            editor.apply();

                            Intent intent = new Intent(Login.this, Single_Product.class);
                            Login.this.finish();
                            startActivity(intent);


                        } else {
                            Intent intent = new Intent(Login.this, Login.class);
                            startActivity(intent);
                        }

                        //Toast.makeText(getContext(), (message), Toast.LENGTH_SHORT).show();

                        Toast ToastMessage = Toast.makeText(Login.this, message, Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();


                    } else {
                        // Toast.makeText(getContext(), (message), Toast.LENGTH_SHORT).show();


                        Toast ToastMessage = Toast.makeText(Login.this, message, Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();

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
                Log.e("RUEERROR", "" + volleyError);

                //Toast.makeText(getContext(), "Please check your Intenet and try again!", Toast.LENGTH_LONG).show();


                Toast ToastMessage = Toast.makeText(Login.this, "Please check your Intenet and try again!", Toast.LENGTH_LONG);
                View toastView = ToastMessage.getView();
                toastView.setBackgroundResource(R.drawable.toast_background);
                ToastMessage.show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> values = new HashMap();
                values.put("username", pword);
                values.put("password", uname);


                return values;
            }


        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        //requestQueue.add(stringRequest);


    }


    private void SaveDetails(final String address1, final String address2, final String country, final String regionstate, final String city, final String surbub, final String Idno) {
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success", "" + s);
                Log.e("Zitapass", "" + shared.getString("lastnumber", ""));
                Log.e("Address pass", "" + (shared.getString("address", "")));
                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonobject = new JSONObject(s);
                    int success = jsonobject.getInt("success");
                    String message = jsonobject.getString("message");
                    if (success == 1) {

                        //Toast.makeText(getContext(), ("Update Successfull!"), Toast.LENGTH_SHORT).show();

                        new AlertDialog.Builder(Login.this)
                                .setTitle("Info")
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(Login.this, Login.class);
                                        startActivity(intent);


                                    }
                                })
                                .setNegativeButton("", null)
                                .setMessage(Html.fromHtml(message))
                                .show();

                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString("address", address1 + " " + address2 + " " + surbub + " " + regionstate + " " + country);
                        editor.putString("idno", idno);
                        editor.putString("telno", shared.getString("lastnumber", ""));
                        editor.commit();

                        MyPref.savePhoneNumber(Login.this,shared.getString("lastnumber", ""));

                        MyPrefAddress.saveAllAddress(Login.this, address1, address2, city, regionstate, surbub, country);



                        //Intent intent = new Intent(getContext(), Login.class);
                        //startActivity(intent);

                        Log.e("City: ", "" + city);
                        Log.e("Surbub: ", "" + surbub);
                        Log.e("Region: ", "" + regionstate);
                        Log.e("Country: ", "" + country);


                    } else {
                        //Toast.makeText(getContext(), ("An error occured please try again..."), Toast.LENGTH_SHORT).show();

                        Toast ToastMessage = Toast.makeText(Login.this, "An error occured please try again...!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();

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
                Log.e("RUEERROR", "" + volleyError);

                //Log.e("Values from method"," By order is "+address1 + " " + address2 + " " + country + " " + regionstate + " " + city + " " + surbub + " " + Idno);

                Toast ToastMessage = Toast.makeText(Login.this, "Please check your Intenet and try again!", Toast.LENGTH_LONG);
                View toastView = ToastMessage.getView();
                toastView.setBackgroundResource(R.drawable.toast_background);
                ToastMessage.show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> values = new HashMap();
                values.put("userid", (shared.getString("userid", "")));
                values.put("address1", address1);
                values.put("address2", address2);
                values.put("country", country);
                values.put("regionstate", regionstate);
                values.put("city", city);
                values.put("surbub", surbub);
                values.put("Idno", Idno);
                values.put("telno", shared.getString("lastnumber", ""));
                //values.put("telno","+263776844024");


                return values;
            }


        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }



    private void saveReg(final String surname, final String user_id,
                         final String firstname, final String username) {
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(Login.this);


        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGINN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success", "" + s);
                Log.e("NUMBER to update", "Number equals" + MyPref.getPhone(Login.this));
                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");
                    if (success == 1) {


                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString("Fname", firstname);
                        editor.putString("sname", surname);
                        editor.commit();
                        editor.apply();


                        new AlertDialog.Builder(Login.this)
                                .setTitle("Info")
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                })
                                .setNegativeButton("", null)
                                .setMessage(Html.fromHtml("Your Details have been updated successfully."))
                                .show();


                    } else {
                        //Toast.makeText(getContext(), (s), Toast.LENGTH_SHORT).show();


                        Toast ToastMessage = Toast.makeText(Login.this, message, Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();

                Log.e("RUEERROR", "" + volleyError);

                Toast ToastMessage = Toast.makeText(Login.this, "Please check your Intenet and try again!", Toast.LENGTH_LONG);
                View toastView = ToastMessage.getView();
                toastView.setBackgroundResource(R.drawable.toast_background);
                ToastMessage.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> values = new HashMap();
                values.put("update", "update");
                values.put("username", username);
                values.put("surname", surname);
                values.put("user_id", user_id);
                values.put("firstname", firstname);

                values.put("telno", MyPref.getPhone(Login.this));


                return values;
            }
        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);


    }

    private void ResetPass(final String Pass, final String userID, final String useroremail, final String oldpassword) {
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(Login.this);

        Log.e("NUMBER", (shared.getString("telno", "")));


        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RESET_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success", "" + s);
                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");

                    Log.e("PassedName", useroremail);

                    if (success == 1) {


                        new AlertDialog.Builder(Login.this)
                                .setTitle("Info")
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                })
                                .setNegativeButton("", null)
                                .setMessage(Html.fromHtml("Your password was reset successfully. New Password is " + Pass))
                                .show();


                    } else if (success == 2) {


                        //Toast.makeText(getContext(), "An sms and an email has been sent to you ", Toast.LENGTH_SHORT).show();


                        Toast ToastMessage = Toast.makeText(Login.this, "An sms has been sent to you ", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();


                        notifyform.setVisibility(View.VISIBLE);
                        forgotform.setVisibility(View.GONE);


                    } else {
                        //Toast.makeText(getContext(), (message), Toast.LENGTH_SHORT).show();

                        Toast ToastMessage = Toast.makeText(Login.this, message, Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                    //Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();

                    Toast ToastMessage = Toast.makeText(Login.this, "An error occurred", Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_background);
                    ToastMessage.show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();

                Log.e("RUEERROR", "" + volleyError);

                //Toast.makeText(getContext(), "No Intenet connection!", Toast.LENGTH_SHORT).show();

                Toast ToastMessage = Toast.makeText(Login.this, "No Intenet connection!", Toast.LENGTH_LONG);
                View toastView = ToastMessage.getView();
                toastView.setBackgroundResource(R.drawable.toast_background);
                ToastMessage.show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> values = new HashMap();
                values.put("password", Pass);
                values.put("userid", userID);
                values.put("useroremail", useroremail);
                values.put("oldpassword", oldpassword);

                return values;
            }
        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);


    }


    //Method to get all levels
    public void getspinnerfields(final AutoCompleteTextView autotxt, final String type, final String value) {


        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(Login.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FIELDS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                Log.e("Success", "" + s);

                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonobject = new JSONObject(s);
                    int success = jsonobject.getInt("success");
                    if (success == 1) {


                        JSONArray array = jsonobject.getJSONArray("posts");


                        List<String> spinnerArray = new ArrayList<String>();

                        if (array != null) {
                            try {


                                for (int i = 0; i < array.length(); i++) {


                                    JSONObject object = array.getJSONObject(i);

                                    String level = object.optString("name");


                                    spinnerArray.add(level);
                                }
                            } catch (JSONException e) {
                                Log.e("Cities JSON Error: ", e.toString());
                                e.printStackTrace();
                            }


                            //Creating the instance of ArrayAdapter containing list of language names
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (Login.this, android.R.layout.select_dialog_item, spinnerArray);
                            //Getting the instance of AutoCompleteTextView
                            //AutoCompleteTextView actv= (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
                            autotxt.setThreshold(0);//will start working from first character
                            autotxt.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                            autotxt.setTextColor(Color.RED);

                            pDialog.dismiss();




/*
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spinner.setAdapter(adapter);
                            spinner.setSelection(2);
*/
                        } else {
                            // Toast.makeText(getActivity(), ("Connection error..."), Toast.LENGTH_SHORT).show();

                            Toast ToastMessage = Toast.makeText(Login.this, "Connection error...!", Toast.LENGTH_LONG);
                            View toastView = ToastMessage.getView();
                            toastView.setBackgroundResource(R.drawable.toast_background);
                            ToastMessage.show();
                        }

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
                Log.e("RUEERROR", "" + volleyError);
                //Toast.makeText(getActivity(), "Please check your Intenet and try again!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> values = new HashMap();

                values.put("type", type);
                values.put("value", value);


                return values;
            }


        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        //requestQueue.add(stringRequest);


    }
}
