package com.hammerandtongues.online.hntauction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import java.util.HashMap;
import java.util.Map;

import preferences.MyPref;
import preferences.MyPrefAddress;

public class Login extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences shared;
    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //private static final String LOGIN_URL = "https://hammerandtongues.com/webservice/login.php";
    //private static final String EWALL_URL = "https://hammerandtongues.com/webservice/getewalletbal.php";
    private static final String LOGIN_URL = "http://10.0.2.2:8012/auctionwebservice/login.php";
    private static final String LOGINN_URL = "http://10.0.2.2:8012/auctionwebservice/register.php";
    private static final String UPDATE_URL = "http://10.0.2.2:8012/auctionwebservice/updatedetails.php";
    private static final String RESET_URL = "http://10.0.2.2:8012/auctionwebservice/ResetPass.php";


    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_CATEGORIES = "posts";
    TextView ewallet, welcome, address,welcum, ewall, addwarning;
    int success=0;

    EditText username, password, nameoremail;
    String uname, pword, uEname, fEname, eEmail, sEname,Plogin,Pconfirm;
    String UserID, Address, Email, uName,userReset, Balance="";
    RelativeLayout tione;
    LinearLayout add_details, loggedin, input_container, inside,BasicForm, notifyform, forgotform;
    Button update, logout, basic, shopping, addadd, back,bac,updat, reset, register;
    Button btnresend, btnforgot, btnnotify;
    EditText txtadd1, txtadd2, txtsurbub, txtcity, txtregion, txtidno, txtcountry, uEName, fName, sName, email, pword_login, pword_confrim;
    String   idno, add1,add2, suburb, city, region, country;

    boolean isClicked = true;
    PopupWindow popUpWindow;
    LinearLayout.LayoutParams layoutParams;
    LinearLayout mainLayout;
    Button btnClickHere;
    LinearLayout containerLayout;
    TextView tvMsg;
    //Global Variables

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

try{
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        shared = this. getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        System.gc();


        input_container = (LinearLayout) findViewById(R.id.input_container);
        BasicForm = (LinearLayout) findViewById(R.id.BasicForm);
    forgotform = (LinearLayout) findViewById(R.id.forgotform);
    notifyform = (LinearLayout) findViewById(R.id.notifyform);
        add_details = (LinearLayout) findViewById(R.id.add_details);
    btnforgot = (Button) findViewById(R.id.btnforgot);
    btnnotify = (Button) findViewById(R.id.btnnotify);
    btnresend = (Button) findViewById(R.id.btnresend);
        addadd = (Button) findViewById(R.id.add_add);
        logout = (Button) findViewById(R.id.logout);
        update = (Button) findViewById(R.id.update);
        back = (Button) findViewById(R.id.bac);
        updat = (Button) findViewById(R.id.updat);
        bac = (Button) findViewById(R.id.back);
        basic = (Button) findViewById(R.id.basicInfo);
        reset = (Button) findViewById(R.id.reset);
    nameoremail = (EditText) findViewById(R.id.nameoremailll);
        shopping = (Button) findViewById(R.id.shopping);
        tione =(RelativeLayout) findViewById(R.id.tione);
        inside = (LinearLayout) findViewById(R.id.inside);
        welcum = (TextView) findViewById(R.id.welcum);
        ewall = (TextView) findViewById(R.id.ewall);
        addwarning = (TextView) findViewById(R.id.addwarning);
        txtadd1 = (EditText) findViewById(R.id.addressLn1);
        txtadd2 = (EditText) findViewById(R.id.addressLn2);
        txtsurbub = (EditText) findViewById(R.id.surbub);
        txtcity = (EditText) findViewById(R.id.city);
        txtregion = (EditText) findViewById(R.id.regionstate);
        txtcountry = (EditText) findViewById(R.id.country);
        txtidno = (EditText) findViewById(R.id.idno);
        uEName =(EditText) findViewById(R.id.uName);
        fName = (EditText) findViewById(R.id.fName);
        sName = (EditText) findViewById(R.id.sName);
        email = (EditText) findViewById(R.id.eEmail);
        pword_login = (EditText) findViewById(R.id.pword_login);
        pword_confrim = (EditText) findViewById(R.id.pword_confrim);



        uEName.setText(shared.getString("uname", ""));
        fName.setText(shared.getString("Fname", ""));
        sName.setText(shared.getString("sname", ""));
        email.setText(shared.getString("umail", ""));

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        Button signin = (Button) findViewById(R.id.btnlogin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                uname= username.getText().toString();
                pword= password.getText().toString();
                loginMethod( pword, uname);
                // new AttemptLogin().execute();

            }

        });


    register = (Button) findViewById(R.id.btnSignUp);
    register.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Log.e("Register Button", "onClick: " );
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
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

            if (nameoremail.getText().toString().contentEquals("")){

                Toast.makeText(Login.this, "Please enter your username or Email", Toast.LENGTH_LONG).show();


            }
            else {
                userReset = nameoremail.getText().toString();
                UserID = "unknownhapanazeronada";
                Plogin = "unknownhapanazeronada";

                ResetPass(Plogin, UserID, userReset);
            }

        }
    });


        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                shared.edit().remove("userid").apply();

                DatabaseHelper myDBHandler = new DatabaseHelper(getBaseContext());
                //myDBHandler.clearCartItems();

                input_container.setVisibility(View.VISIBLE);
                inside.setVisibility(View.GONE);
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

        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                //else if (next.getText().toString().trim().toUpperCase().contentEquals("SAVE")){

                if (txtadd1.getText().toString().contentEquals("") || txtadd2.getText().toString().contentEquals("") || txtsurbub.getText().toString().contentEquals("") ||
                        txtcity.getText().toString().contentEquals("") || txtregion.getText().toString().contentEquals("") || txtidno.getText().toString().contentEquals("") ||
                        txtcountry.getText().toString().contentEquals("")) {
                    Toast.makeText(getBaseContext(), "All fields are mandatory", Toast.LENGTH_LONG).show();
                } else {

                    // Log.e("Submit Button Text", save.getText().toString());
                    idno = txtidno.getText().toString();
                    add1 = txtadd1.getText().toString();
                    add2 = txtadd2.getText().toString();

                    suburb = txtsurbub.getText().toString();
                    city = txtcity.getText().toString();
                    region = txtregion.getText().toString();

                    country = txtcountry.getText().toString();


                    SaveDetails(add1, add2, country, region, city, suburb, idno);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("address", add1 + add2);
                    editor.commit();
                }
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
                //idcontainer.setVisibility(View.GONE);
                inside.setVisibility(View.VISIBLE);
                tione.setVisibility(View.GONE);
                add_details.setVisibility(View.GONE);

            }
        });

        bac.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                tione.setVisibility(View.GONE);
                BasicForm.setVisibility(View.GONE);
                inside.setVisibility(View.VISIBLE);


            }
        });

        updat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                uEname = uEName.getText().toString();
                fEname = fName.getText().toString();
                sEname = sName.getText().toString();
                eEmail = email.getText().toString();


                saveReg(sEname,eEmail,fEname,uEname);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("address", add1 + add2);
                editor.commit();

            }
        });


        reset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                UserID = (shared.getString("userid", ""));

                Plogin = pword_login.getText().toString();
                Pconfirm = pword_confrim.getText().toString();
                userReset = "notnecessary";
                if(Plogin.contentEquals(Pconfirm) ) {
                    ResetPass(Plogin,UserID, userReset);
                }

                else{

                    Toast.makeText(getBaseContext(), "Passwords do not match!", Toast.LENGTH_LONG).show();
                }

                SharedPreferences.Editor editor = shared.edit();
                editor.putString("address", add1 + add2);
                editor.commit();

            }
        });



    if (shared.getString("userid", "") != null && shared.getString("userid", "") !="" && shared.getString("userid", "") !="null" && !(shared.getString("userid", "").contentEquals("null")) && !(shared.getString("userid", "").contentEquals(""))) {
        UserID = (shared.getString("userid", ""));
        Address = (shared.getString("address", ""));
        uName = (shared.getString("uname", ""));
        Email = (shared.getString("umail", ""));
        Balance = (shared.getString("balance", ""));
        input_container.setVisibility(View.GONE);



        new GetConnectionStatus().execute();

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        //  welcum.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //ewall.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //}


        welcum.setText(Html.fromHtml("<b>You are Logged in \n  </b>" + "<b>" + uName + "</b>" + " !\n" + "<b>" + Email + "</b>"));


        if (Address !=null && Address !="" && Address !="null" && !Address.contentEquals("null")){


            addwarning.setText("Registered Adress: " + Address);

            inside.setVisibility(View.VISIBLE);

        }
        else {


            tione.setVisibility(View.VISIBLE);
            add_details.setVisibility(View.VISIBLE);

            //addwarning.setTextColor(Color.BLACK);
            //addwarning.setText("Please register your address details, for a better shopping experience!!!");



        }


        if (Balance != null && isNumeric(Balance)) {
            if (Double.parseDouble(Balance) == 0.00) {
                ewall.setTextColor(Color.RED);
            } else {
                ewall.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }
        else {
            ewall.setTextColor(Color.RED);
            Balance = "0.00";
        }

        ewall.setText("E-Wallet Balance: " + Balance);






    }

    else{

        welcum.setText(Html.fromHtml("<b>You are Logged in, \n \n Hello!</b> " ));

        addwarning.setText(Html.fromHtml("<b>Happy Shopping!!!</b> "));
        //inside.setVisibility(View.VISIBLE);

        //Toast.makeText(getContext(), "PLEASE ADD YOUR ADDRESS DETAILS!", Toast.LENGTH_LONG).show();

    }










}
        catch (Exception ex)
    {
        Log.e("Main Thread Exception", "Error: " + ex.toString());
        System.gc();

    }

}

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getBaseContext(). getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
/*
    public void nexttab()
    {
        TabLayout tabLayout=(TabLayout) getBaseContext().findViewById(R.id.tabs02);
        final ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager02);
        final PagerAdapter adapter = new PagerAdapter02 (getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        //Log.e("Next 02", "Thus Far the lord has brought us");
    }
*/
    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }






    private void loginMethod(final String uname, final String pword){
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success",""+s);
                Log.e("Zitapass", "" + pword);
                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonobject=new JSONObject(s);
                    int success=jsonobject.getInt("success");
                    if(success==1){



                        JSONArray array=jsonobject.getJSONArray("posts");

                        JSONObject object=array.getJSONObject(0);





                        String post_id = object.optString("id");
                        String address = object.optString("add");
                        String uname = object.optString("uname");
                        String Fname = object.optString("Fname");
                        String sname = object.optString("sname");
                        String umail = object.optString("umail");
                        String balance = object.optString("balance");
                        String number = object.optString("mobile_number");
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString("userid", post_id);
                        editor.putString("address", address);
                        editor.putString("uname", uname);
                        editor.putString("Fname", Fname);
                        editor.putString("sname", sname);
                        editor.putString("umail", umail);
                        editor.putString("balance", balance);
                        editor.putString("telno", number);
                        editor.putString("options", "checkout");
                        editor.commit();
                        editor.apply();

                        Intent intent = new Intent(getBaseContext(), Login.class);
                        startActivity(intent);


                        Toast.makeText(getBaseContext(), ("Login successfull!"), Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getBaseContext(), ("Incorrect username or password..."), Toast.LENGTH_SHORT).show();
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
                values.put("username",pword);
                values.put("password",uname);


                return values;
            }


//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//
//                Map<String,String> headers=new HashMap();
//                headers.put("Accept","application/json");
//                headers.put("Content-Type","application/json");
//                return headers;
//            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        //requestQueue.add(stringRequest);




    }




    private void SaveDetails(final String address1, final String address2, final String country, final String regionstate, final String city, final String surbub, final String Idno){
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, UPDATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success",""+s);
                Log.e("Zitapass", "" +  (shared.getString("userid", "")));
                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonobject=new JSONObject(s);
                    int success=jsonobject.getInt("success");
                    if(success==1){

                        Toast.makeText(getBaseContext(), ("Update Successfull!"), Toast.LENGTH_SHORT).show();


                        MyPrefAddress.saveAllAddress(getBaseContext(),address1, address2, country, regionstate, city, surbub);


                        inside.setVisibility(View.VISIBLE);
                        tione.setVisibility(View.GONE);
                        add_details.setVisibility(View.GONE);


                    }else{
                        Toast.makeText(getBaseContext(), ("An error occured please try again..."), Toast.LENGTH_SHORT).show();
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
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> values=new HashMap();
                values.put("userid",(shared.getString("userid", "")));
                values.put("address1",address1);
                values.put("address2",address2);
                values.put("country",country);
                values.put("regionstate",regionstate);
                values.put("city", city);
                values.put("surbub", surbub);
                values.put("Idno", Idno);
                values.put("telno", MyPref.getPhone(getBaseContext()));

                return values;
            }


//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//
//                Map<String,String> headers=new HashMap();
//                headers.put("Accept","application/json");
//                headers.put("Content-Type","application/json");
//                return headers;
//            }
        };
        requestQueue.add(stringRequest);




    }











class GetConnectionStatus extends AsyncTask<String, Void, Boolean> {

    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage(" Please Wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

    }

    protected Boolean doInBackground(String... urls) {
        // TODO: Connect
        if (isNetworkAvailable() == true) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("Network Check", "Error checking internet connection", e);
            }
        } else {
            Log.d("Network Check", "No network available!");
        }
        return false;
    }

    protected void onPostExecute(Boolean result) {
        // TODO: Check this.exception
        // TODO: Do something with the feed
        pDialog.dismiss();
        try {


            if (result == true)

            {

                //todo getwallet balance
//                    new GetEWalletBalance().execute();



            } else

            {
                Toast.makeText(getBaseContext(), "Network is Currently Unavailable", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception ex) {
//                Toast.makeText(getContext(), "Weak / No Network Connection", Toast.LENGTH_SHORT).show();
        }
    }
}



    private void saveReg(final String surname, final String email,
                         final String firstname,final String username){
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());

        Log.e("NUMBER",(shared.getString("telno", "")));
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, LOGINN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success",""+s);
                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonObject=new JSONObject(s);
                    int success=jsonObject.getInt("success");
                    if(success==1){

                        Intent intent = new Intent(Login.this, UserActivity.class);
                        startActivity(intent);


                        //  mChangeAdapter.callbackToZero(0);
                        Toast.makeText(getBaseContext(), "Your account was created successful", Toast.LENGTH_SHORT).show();




                    }else{
                        Toast.makeText(getBaseContext(), (s), Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();

                Log.e("RUEERROR",""+volleyError);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> values=new HashMap();
                values.put("username",username);
                values.put("surname", surname);

                values.put("email", email);
                values.put("telno", (shared.getString("telno", "")));

                values.put("firstname", firstname);

                return values;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);




    }

    private void ResetPass(final String Pass, final String userID, final String useroremail){
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());

        Log.e("NUMBER",(shared.getString("telno", "")));


        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, RESET_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success",""+s);
                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonObject=new JSONObject(s);
                    int success=jsonObject.getInt("success");
                    String message=jsonObject.getString("message");

                    Log.e("PassedName",useroremail);

                    if(success==1){

                        Intent intent = new Intent(getBaseContext(), UserActivity.class);
                        startActivity(intent);


                        //  mChangeAdapter.callbackToZero(0);
                        Toast.makeText(getBaseContext(), "Your password was reset successful Password to " + Pass, Toast.LENGTH_SHORT).show();



                    }

                    else if (success == 2){



                        Toast.makeText(getBaseContext(), "An sms and an email has been sent to you ", Toast.LENGTH_SHORT).show();


                        notifyform.setVisibility(View.VISIBLE);
                        forgotform.setVisibility(View.GONE);


                    }


                    else{
                        Toast.makeText(getBaseContext(), (message), Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(getBaseContext(), "An error occurred", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();

                Log.e("RUEERROR",""+volleyError);

                Toast.makeText(getBaseContext(), "No Intenet connection!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> values=new HashMap();
                values.put("password",Pass);
                values.put("userid",userID);
                values.put("useroremail",useroremail);

                return values;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);




    }


}


