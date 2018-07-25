package com.hammerandtongues.online.hntauction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import preferences.MyPref;

public class MyFinances extends AppCompatActivity {



        private static final String FINANCES_URL = "https://devauction.hammerandtongues.com/wp-content/themes/AuctionTheme/FinancesMobile.php";
        private static final String ECOCASH_URL = "https://devauction.hammerandtongues.com/wp-content/themes/AuctionTheme/mobile_deposit_ecash.php";
        private static final String TELECASH_URL = "https://devauction.hammerandtongues.com/wp-content/themes/AuctionTheme/deposit_tele_mobile.php";
        private static final String UPDATEBAL_URL = "https://devauction.hammerandtongues.com/webservice/updatebalances.php";


        private EditText econum, bankinfo,amount;

        private EditText txtbyeco, txtbytele,txtbypaynw, txteconum, txttelnum, txttelotp, txtredeem, txttransamount;
        private  Button btnbyeco, btnbytel, btnbypaynw, pendingwiths;

        private TextView mybalance, mypoints;
        String imgurl ="";


        private Button withdraw, btnredeem, btntransfer, back;
        private String paydetails, userid, methods, theamount, type, depeconum, deptelnum, deptelotp, user_email, Balance, loyalty_points;
        private String request_type, typedeposit = "deposit", typewithdraw = "withdraw", typetransfer_credits = "transfer_credits", typeredeem_points = "redeem_points", typetransactions = "transactions";

        private ProgressDialog pDialog;
        private Spinner wallspinner;
        ImageView imgstore[] = new ImageView[200];

        private LinearLayout layoutwithdraw, layoutdeposit, layoutredeem, layouttransfer, layout;

        public static final String MyPREFERENCES = "MyPrefs";
        SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_finances);


            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);


            shared = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            econum = (EditText) findViewById(R.id.econumber);
            bankinfo = (EditText) findViewById(R.id.bankinfo);
            txtbyeco = (EditText) findViewById(R.id.depamounteco);
            txtbytele = (EditText) findViewById(R.id.depamounttele);
            txtbypaynw = (EditText) findViewById(R.id.depamountpaynw);
            amount = (EditText) findViewById(R.id.withamount);
            txteconum = (EditText) findViewById(R.id.depnumeco);
            txttelnum = (EditText) findViewById(R.id.depnumtele);
            txttelotp = (EditText) findViewById(R.id.depotptele);
            txtredeem = (EditText) findViewById(R.id.thepoints);
            txttransamount = (EditText) findViewById(R.id.trans_amount);
            withdraw = (Button) findViewById(R.id.btnwithdraw);
            btnbyeco = (Button) findViewById(R.id.btndepeco);
            btnbytel = (Button) findViewById(R.id.btndeptele);
            btnbypaynw = (Button) findViewById(R.id.btndeppaynw);
            btnredeem = (Button) findViewById(R.id.btnredeem);
            btntransfer = (Button) findViewById(R.id.btntransfer);
            pendingwiths = (Button) findViewById(R.id.btnpendingwithdraw);
            back = (Button) findViewById(R.id.financeback);
            layoutwithdraw = (LinearLayout) findViewById(R.id.withdraw_layout);
            layoutdeposit = (LinearLayout) findViewById(R.id.deposit_layout);
            layoutredeem = (LinearLayout) findViewById(R.id.redeem_layout);
            layouttransfer = (LinearLayout) findViewById(R.id.transfer_layout);
            layout = (LinearLayout) findViewById(R.id.pending_layout);
            wallspinner =  (Spinner) findViewById(R.id.trans_site);
            mybalance = (TextView)  findViewById(R.id.mybalance);
            mypoints = (TextView)  findViewById(R.id.mypoints);






            userid = shared.getString("userid", "");
            user_email = shared.getString("umail", "");
            request_type = shared.getString("request_type", "");
            Balance = (shared.getString("balance", ""));

            if (shared.getString("points", "") != null && shared.getString("points", "") != "" && shared.getString("points", "") != "null") {

                loyalty_points = (shared.getString("points", ""));
            }

            else {


                loyalty_points = "0";
            }

            updatebal(userid);


            mybalance.setText("Wallet Balance: $" + Balance);
            mypoints.setText("Loyalty Points: " + loyalty_points);


            if(request_type.contentEquals(typedeposit)) {

                //webView.loadUrl("https://shopping.hammerandtongues.com/my-account/my-finances/?pg=deposit");
                layoutdeposit.setVisibility(View.VISIBLE);

            }

            else if(request_type.contentEquals(typetransfer_credits)) {

                //webView.loadUrl("https://shopping.hammerandtongues.com/my-account/my-finances/?pg=transfer");

                layouttransfer.setVisibility(View.VISIBLE);

            }

            else if(request_type.contentEquals(typewithdraw)) {

                //webView.loadUrl("https://shopping.hammerandtongues.com/my-account/my-finances/?pg=withdraw");
                layoutwithdraw.setVisibility(View.VISIBLE);

            }

            else if(request_type.contentEquals(typeredeem_points)) {

                //webView.loadUrl("https://shopping.hammerandtongues.com/my-account/my-finances/?pg=redeem");
                layoutredeem.setVisibility(View.VISIBLE);

            }




//Deposit funds

            btnbyeco.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (txtbyeco.getText().toString().contentEquals("")){


                        Toast ToastMessage = Toast.makeText(MyFinances.this, "Please enter deposit amount!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();


                    }


                    else if (txteconum.getText().toString().contentEquals("")){


                        Toast ToastMessage = Toast.makeText(MyFinances.this, "Please enter ecocash number!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();


                    }


                    else {


                        new AlertDialog.Builder(MyFinances.this)
                                .setTitle("Confirm ")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {






                                        paydetails = econum.getText().toString();
                                        methods = "Ecocash";
                                        theamount = txtbyeco.getText().toString();
                                        type = "deposit";
                                        depeconum = txteconum.getText().toString();

                                        //sendfinace(type, theamount, methods, paydetails, userid);
                                        sendtoecocash(depeconum, theamount, userid);
                                        updatebal(userid);


                                    }
                                })
                                .setNegativeButton("No", null)
                                .setMessage(Html.fromHtml("Deposit an amount of $" + txtbyeco.getText().toString() + " via ecocash?" ))
                                .show();




                    }
                }
            });

            btnbytel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {



                    if (txtbytele.getText().toString().contentEquals("")){


                        Toast ToastMessage = Toast.makeText(MyFinances.this, "Please enter deposit amount!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();


                    }


                    else if (txttelnum.getText().toString().contentEquals("")){


                        Toast ToastMessage = Toast.makeText(MyFinances.this, "Please enter telecash number!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();


                    }



                    else if (txttelotp.getText().toString().contentEquals("")){


                        Toast ToastMessage = Toast.makeText(MyFinances.this, "Please enter one time password!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();


                    }

                    else {




                        new AlertDialog.Builder(MyFinances.this)
                                .setTitle("Confirm ")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {





                                        paydetails = econum.getText().toString();
                                        methods = "Telecash";
                                        theamount = txtbytele.getText().toString();
                                        deptelnum = txttelnum.getText().toString();
                                        deptelotp = txttelotp.getText().toString();
                                        type = "deposit";

                                        //sendfinace(type, theamount, methods, paydetails, userid);
                                        sendtotelecash(deptelnum, theamount, userid, deptelotp);

                                        updatebal(userid);


                                    }
                                })
                                .setNegativeButton("No", null)
                                .setMessage(Html.fromHtml("Deposit an amount of $" + txtbytele.getText().toString() + " via telecash?" ))
                                .show();




                    }
                }
            });


            btnbypaynw.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (txtbypaynw.getText().toString().contentEquals("")){


                        Toast ToastMessage = Toast.makeText(MyFinances.this, "Please enter deposit amount!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();


                    }

                    else {



                        new AlertDialog.Builder(MyFinances.this)
                                .setTitle("Confirm ")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {





                                        paydetails = econum.getText().toString();
                                        methods = "Paynow";
                                        theamount = txtbypaynw.getText().toString();
                                        type = "deposit";

                                        //sendfinace(type, theamount, methods, paydetails, userid);

                                        SharedPreferences.Editor editor = shared.edit();
                                        editor.putString("theamount", theamount);
                                        editor.putString("type", "deposit");
                                        editor.apply();

                                        Intent i = new Intent(MyFinances.this, WebViewActivity.class);
                                        startActivity(i);

                                        updatebal(userid);


                                    }
                                })
                                .setNegativeButton("No", null)
                                .setMessage(Html.fromHtml("Deposit an amount of $" + txtbypaynw.getText().toString() + " via paynow?" ))
                                .show();


                    }

                }
            });



//withdraw funds

            withdraw.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if(!econum.getText().toString().contentEquals("") && !bankinfo.getText().toString().contentEquals("")){

                        Toast ToastMessage = Toast.makeText(MyFinances.this, "Please fillout one option!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();

                    }

                    else if(econum.getText().toString().contentEquals("")&& bankinfo.getText().toString().contentEquals("")){

                        Toast ToastMessage = Toast.makeText(MyFinances.this, "Please fillout either bank details or ecocash number!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();
                    }


                    else if (amount.getText().toString().contentEquals("")){

                        Toast ToastMessage = Toast.makeText(MyFinances.this, "Please enter withdraw amount!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();


                    }

                    else if(!econum.getText().toString().contentEquals("") && bankinfo.getText().toString().contentEquals("")){




                        new AlertDialog.Builder(MyFinances.this)
                                .setTitle("Confirm ")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        paydetails = econum.getText().toString();
                                        methods = "Ecocash";
                                        theamount = amount.getText().toString();
                                        type = "withdraw";

                                        sendfinace(type, theamount, methods, paydetails, userid);

                                        updatebal(userid);


                                    }
                                })
                                .setNegativeButton("No", null)
                                .setMessage(Html.fromHtml("Withdraw an amount of $" + amount.getText().toString() + " via ecocash?" ))
                                .show();




                    }

                    else if(econum.getText().toString().contentEquals("") && !bankinfo.getText().toString().contentEquals("")){




                        new AlertDialog.Builder(MyFinances.this)
                                .setTitle("Confirm ")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        paydetails = bankinfo.getText().toString();
                                        methods = "Bank";
                                        theamount = amount.getText().toString();
                                        type = "withdraw";

                                        sendfinace(type, theamount, methods, paydetails, userid);

                                        updatebal(userid);


                                    }
                                })
                                .setNegativeButton("No", null)
                                .setMessage(Html.fromHtml("Withdraw an amount of $" + amount.getText().toString() + " via bank?" ))
                                .show();




                    }



                }

            });




            pendingwiths.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    getpendings("pending", userid);
                    layout.setVisibility(View.VISIBLE);
                    layoutwithdraw.setVisibility(View.GONE);



                }

            });

            //Redeem points

            btnredeem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {



                    if(txtredeem.getText().toString().contentEquals("")){

                        Toast ToastMessage = Toast.makeText(MyFinances.this, "Please enter points to redeem!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();


                    }

                    else {


                        new AlertDialog.Builder(MyFinances.this)
                                .setTitle("Confirm ")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {




                                        paydetails = "redeem";
                                        methods = "Bank";
                                        theamount = txtredeem.getText().toString();
                                        type = "rdmpnt";

                                        sendfinace(type, theamount, methods, paydetails, userid);
                                        updatebal(userid);


                                    }
                                })
                                .setNegativeButton("No", null)
                                .setMessage(Html.fromHtml("Redeem " + txtredeem.getText().toString() + " points?" ))
                                .show();

                    }



                }

            });



            wallspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {

                    methods =wallspinner.getSelectedItem().toString();

                    //Toast.makeText(getContext(), spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

                }


                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });



            //Transfer funds
            btntransfer.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {



                    if(txttransamount.getText().toString().contentEquals("")){

                        Toast ToastMessage = Toast.makeText(MyFinances.this, "Please enter amount to transfer!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();


                    }

                    else if(wallspinner.getSelectedItem().toString().contentEquals("")){

                        Toast ToastMessage = Toast.makeText(MyFinances.this, "Please select wallet!", Toast.LENGTH_LONG);
                        View toastView = ToastMessage.getView();
                        toastView.setBackgroundResource(R.drawable.toast_background);
                        ToastMessage.show();


                    }

                    else {



                        new AlertDialog.Builder(MyFinances.this)
                                .setTitle("Confirm ")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {




                                        paydetails = "transfer";
                                        methods = wallspinner.getSelectedItem().toString();
                                        theamount = txttransamount.getText().toString();
                                        type = "selsite";

                                        sendfinace(type, theamount, methods, paydetails, user_email);
                                        updatebal(userid);


                                    }
                                })
                                .setNegativeButton("No", null)
                                .setMessage(Html.fromHtml("Transfer  $" + txttransamount.getText().toString() + " from auction wallet?" ))
                                .show();



                    }



                }

            });

            back.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {


                    updatebal(userid);

                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("request_type", "to-finances");
                    editor.apply();

                    Intent intent = new Intent(MyFinances.this, Login.class);
                    startActivity(intent);



                }

            });

        }



        private void sendfinace(final String type, final String amount, final String method, final String paydetails, final String uid) {
            com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(MyFinances.this);


            pDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, FINANCES_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    pDialog.dismiss();

                    Log.e("Success", "" + s);
                    Log.e("NUMBER to update", "Number equals" + MyPref.getPhone(MyFinances.this));
                    //{"success":1,"message":"Username Successfully Added!"}

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        int success = jsonObject.getInt("success");
                        String message = jsonObject.getString("message");
                        if (success == 1) {

                            new AlertDialog.Builder(MyFinances.this)
                                    .setTitle("Info")
                                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {



                                            Intent intent = new Intent(MyFinances.this, MyFinances.class);
                                            startActivity(intent);

                                        }
                                    })
                                    .setNegativeButton("", null)
                                    .setMessage(Html.fromHtml(message))
                                    .show();


                        } else {
                            //Toast.makeText(getContext(), (s), Toast.LENGTH_SHORT).show();


                            Toast ToastMessage = Toast.makeText(MyFinances.this, message, Toast.LENGTH_LONG);
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

                    Toast ToastMessage = Toast.makeText(MyFinances.this, "Please check your Intenet and try again!", Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_background);
                    ToastMessage.show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> values = new HashMap();
                    values.put("frommobile", "frommobile");
                    values.put("amount", amount);
                    values.put("paypal", paydetails);
                    values.put("methods", method);
                    values.put(type, type);
                    values.put("user_id", uid);

                    values.put("telno", MyPref.getPhone(MyFinances.this));


                    return values;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(stringRequest);


        }

        private void sendtoecocash(final String econumber, final String amount, final String uid) {
            com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(MyFinances.this);





            pDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ECOCASH_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    pDialog.dismiss();

                    Log.e("Success", "" + s);
                    Log.e("NUMBER to update", "Number equals" + MyPref.getPhone(MyFinances.this));
                    //{"success":1,"message":"Username Successfully Added!"}

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        int success = jsonObject.getInt("success");
                        String message = jsonObject.getString("message");
                        if (success == 1) {



                            new AlertDialog.Builder(MyFinances.this)
                                    .setTitle("Info")
                                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                            Intent intent = new Intent(MyFinances.this, MyFinances.class);
                                            startActivity(intent);


                                        }
                                    })
                                    .setNegativeButton("", null)
                                    .setMessage(Html.fromHtml(message))
                                    .show();


                        } else {
                            //Toast.makeText(getContext(), (s), Toast.LENGTH_SHORT).show();


                            Toast ToastMessage = Toast.makeText(MyFinances.this, message, Toast.LENGTH_LONG);
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

                    Toast ToastMessage = Toast.makeText(MyFinances.this, "Please check your Intenet and try again!", Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_background);
                    ToastMessage.show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> values = new HashMap();
                    values.put("frommobile", "frommobile");
                    values.put("am", amount);
                    values.put("yes", "Proceed Now");
                    values.put("endUserId", econumber);
                    values.put("uid", uid);



                    values.put("telno", MyPref.getPhone(MyFinances.this));


                    return values;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(stringRequest);


        }


        private void sendtotelecash(final String econumber, final String amount, final String uid, final String otp) {
            com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(MyFinances.this);

            pDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, TELECASH_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    pDialog.dismiss();

                    Log.e("Success", "" + s);
                    Log.e("NUMBER to update", "Number equals" + MyPref.getPhone(MyFinances.this));
                    //{"success":1,"message":"Username Successfully Added!"}

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        int success = jsonObject.getInt("success");
                        String message = jsonObject.getString("message");
                        if (success == 1) {

                            new AlertDialog.Builder(MyFinances.this)
                                    .setTitle("Info")
                                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                            Intent intent = new Intent(MyFinances.this, MyFinances.class);
                                            startActivity(intent);


                                        }
                                    })
                                    .setNegativeButton("", null)
                                    .setMessage(Html.fromHtml(message))
                                    .show();


                        } else {
                            //Toast.makeText(getContext(), (s), Toast.LENGTH_SHORT).show();


                            Toast ToastMessage = Toast.makeText(MyFinances.this, message, Toast.LENGTH_LONG);
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

                    Toast ToastMessage = Toast.makeText(MyFinances.this, "Please check your Intenet and try again!", Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_background);
                    ToastMessage.show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> values = new HashMap();
                    values.put("frommobile", "frommobile");
                    values.put("am", amount);
                    values.put("yes", "Proceed Now");
                    values.put("endUserId", econumber);
                    values.put("endUserotp", otp);
                    values.put("uid", uid);



                    values.put("telno", MyPref.getPhone(MyFinances.this));


                    return values;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(stringRequest);


        }


        private void updatebal(final String username) {
            com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(MyFinances.this);





            pDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATEBAL_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    pDialog.dismiss();

                    Log.e("Success", "" + s);
                    Log.e("NUMBER to update", "Number equals" + MyPref.getPhone(MyFinances.this));
                    //{"success":1,"message":"Username Successfully Added!"}

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        int success = jsonObject.getInt("success");
                        String message = jsonObject.getString("message");
                        if (success == 1) {

                            JSONArray array = jsonObject.getJSONArray("posts");

                            JSONObject object = array.getJSONObject(0);

                            String balance = object.optString("balance");
                            String points = object.optString("loyalty_points");

                            SharedPreferences.Editor editor = shared.edit();
                            editor.putString("balance", balance);
                            editor.putString("points", points);
                            editor.commit();
                            editor.apply();


                        } else {
                            Toast.makeText(MyFinances.this, (s), Toast.LENGTH_SHORT).show();


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

                    Toast ToastMessage = Toast.makeText(MyFinances.this, "Please check your Intenet and try again!", Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_background);
                    ToastMessage.show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> values = new HashMap();
                    values.put("username", username);



                    return values;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(stringRequest);


        }


        private void getpendings(final String type, final String uid) {
            com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(MyFinances.this);


            pDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, FINANCES_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    pDialog.dismiss();

                    Log.e("Success", "" + s);
                    Log.e("NUMBER to update", "Number equals" + MyPref.getPhone(MyFinances.this));
                    //{"success":1,"message":"Username Successfully Added!"}

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        int success = jsonObject.getInt("success");
                        String message = jsonObject.getString("message");
                        if (success == 1) {

                            JSONArray array = jsonObject.getJSONArray("posts");


                            if (array != null) {
                                try {

                                    DisplayImageOptions options = new DisplayImageOptions.Builder()
                                            // default
                                            .showImageOnLoading(R.drawable.ic_stub)
                                            .showImageForEmptyUri(R.drawable.ic_launcher)
                                            .showImageOnFail(R.drawable.ic_error).delayBeforeLoading(50)
                                            .cacheInMemory(false) // default
                                            .cacheOnDisk(false)
                                            .considerExifParams(true).resetViewBeforeLoading(true).build();
                                    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(MyFinances.this).build();
                                    ImageLoader.getInstance().init(config);
                                    ImageLoader imageLoader = ImageLoader.getInstance();


                                    for (int i = 0; i < array.length(); i++) {



                                        final LinearLayout itmcontr = new LinearLayout(MyFinances.this);

                                        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                        layoutParams.setMargins(10, 10, 10, 10);

                                        itmcontr.setOrientation(LinearLayout.HORIZONTAL);
                                        itmcontr.setBackgroundColor(Color.WHITE);

                                        imgstore[i] = new ImageView(MyFinances.this);
                                        itmcontr.addView(imgstore[i]);



                                        JSONObject object = array.getJSONObject(i);

                                        String datemade = object.optString("datemade");
                                        String amount = object.optString("amount");
                                        String method = object.optString("method");
                                        String email = object.optString("email");




                                        imageLoader.displayImage(imgurl, imgstore[i], options);
                                        android.view.ViewGroup.LayoutParams imglayoutParams = imgstore[i].getLayoutParams();
                                        imglayoutParams.width = 50;
                                        imglayoutParams.height = 50;
                                        imgstore[i].setLayoutParams(imglayoutParams);

                                        Log.e("Setting Banner", imgurl);
                                        LinearLayout orderinfo = new LinearLayout(MyFinances.this);
                                        orderinfo.setOrientation(LinearLayout.VERTICAL);
                                        TextView ONo = new TextView(MyFinances.this);
                                        TextView ODate = new TextView(MyFinances.this);
                                        TextView OItems = new TextView(MyFinances.this);
                                        TextView OTotal = new TextView(MyFinances.this);
                                        ODate.setText("Date: " + datemade);
                                        ONo.setText("By: " + method);
                                        OItems.setText("To: " + email);
                                        OTotal.setText("Amount: " + amount);
                                        OTotal.setTextColor(getResources().getColor(R.color.colorAmber));
                                        OTotal.setTypeface(null, Typeface.BOLD);
                                        OTotal.setTextSize(13);

                                        orderinfo.addView(ONo);
                                        orderinfo.addView(ODate);
                                        orderinfo.addView(OItems);
                                        orderinfo.addView(OTotal);
                                        itmcontr.addView(orderinfo);

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                layout.addView(itmcontr, layoutParams);

                                            }
                                        });






                                    }
                                } catch (JSONException e) {
                                    Log.e("Cities JSON Error: ", e.toString());
                                    e.printStackTrace();
                                }
                            }


                        } else {
                            //Toast.makeText(getContext(), (s), Toast.LENGTH_SHORT).show();


                            Toast ToastMessage = Toast.makeText(MyFinances.this, message, Toast.LENGTH_LONG);
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

                    Toast ToastMessage = Toast.makeText(MyFinances.this, "Please check your Intenet and try again!", Toast.LENGTH_LONG);
                    View toastView = ToastMessage.getView();
                    toastView.setBackgroundResource(R.drawable.toast_background);
                    ToastMessage.show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> values = new HashMap();
                    values.put("frommobile", "frommobile");
                    values.put("pending", type);
                    values.put("user_id", uid);

                    values.put("telno", MyPref.getPhone(MyFinances.this));


                    return values;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(stringRequest);


        }

    }

