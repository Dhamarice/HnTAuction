package com.hammerandtongues.online.hntauction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.util.HashMap;
import java.util.Map;

import preferences.MyPref;

public class Register extends AppCompatActivity {



    JSONParser jsonParser ;
    //testing on Emulator:
    //  private static final String LOGIN_URL = "https://hammerandtongues.com/webservice/register.php";

    private static final String LOGIN_URL = "http://10.0.2.2:8012/auctionwebservice/register.php";
    private static final String CODE_URL = "http://10.0.2.2:8012/auctionwebservice/sms_api.php";
    private static final String VERIFY_URL = "http://10.0.2.2:8012/auctionwebservice/verify.php";
    private static final String EDIT_URL = "http://10.0.2.2:8012/auctionwebservice/DeleteNum.php";
    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_POSTS = "posts";
    private ProgressDialog pDialog;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    SharedPreferences sharedpreferences;
    LinearLayout pdetails, adetails, cdetails;
    Button submit,signin, editnum;
    EditText txtusername, txtemail, txtpassword, txtfname, txtsname, txtpasswordcon, txttelno;
    EditText txtcode;
    String username, email, password, fname, sname, idno, telno, add1,add2,suburb, city, region, userid, code, mobilenum, selectcode, Pconfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

   try{
                jsonParser = new JSONParser();
                System.gc();
                SharedPreferences sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                pdetails = (LinearLayout) findViewById(R.id.personaldetails);
                adetails = (LinearLayout) findViewById(R.id.addressdetails);
                cdetails= (LinearLayout) findViewById(R.id.codedetails);



                txtfname = (EditText) findViewById(R.id.first_name);
                txtsname = (EditText) findViewById(R.id.surname);
                txtusername = (EditText) findViewById(R.id.username);
                txtemail = (EditText) findViewById(R.id.useremail);
                txtpasswordcon =(EditText) findViewById(R.id.password_confrim);
                txtpassword = (EditText) findViewById(R.id.password_login);
                txttelno = (EditText) findViewById(R.id.telephone);
                txtcode = (EditText) findViewById(R.id.code);
                editnum = (Button) findViewById(R.id.editnum);
                final Spinner spinner = (Spinner) findViewById(R.id.codespinner);


                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        selectcode =spinner.getSelectedItem().toString();

                        //Toast.makeText(getContext(), spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });



                //final  String selectcode = spinner.getSelectedItem().toString();
                mobilenum = txttelno.getText().toString();

                //final String mobile = (selectcode + txttelno.getText().toString());

                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Sending code...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);



                signin = (Button) findViewById(R.id.register);
                signin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {


                        if (signin.getText().toString().trim().toUpperCase().contentEquals("VERIFY NUMBER")
                                ) {
                            if(txttelno.getText().toString().contentEquals("") ) {
                                Toast.makeText(getApplicationContext(), "Please enter your mobile number", Toast.LENGTH_LONG).show();
                            }

                            else{
                                telno = (selectcode + txttelno.getText().toString());
                                Log.e("Submit Button Text", signin.getText().toString());
//                            pdetails.setVisibility(View.GONE);
//                            adetails.setVisibility(View.VISIBLE);
//                            cdetails.setVisibility(View.VISIBLE);
//
//
//
//                            signin.setText("SEND CODE");
                                saveTel(telno);

                                Log.e("Success", "" + telno);
                            }
                        }

                        else if (signin.getText().toString().trim().toUpperCase().contentEquals("SEND CODE")
                                ) {
                            if(txtcode.getText().toString().contentEquals("") ) {
                                Toast.makeText(getApplicationContext(), "Please enter the confirmation code!", Toast.LENGTH_LONG).show();
                            }

                            else{
                                code = txtcode.getText().toString();

                                saveCode(code, telno);


                            }
                        }

                        else if (signin.getText().toString().trim().toUpperCase().contentEquals("SIGN UP")
                                ) {
                            if(txtusername.getText().toString().contentEquals("")  || txtpassword.getText().toString().contentEquals("") || txtemail.getText().toString().contentEquals("") ||
                                    txtfname.getText().toString().contentEquals("")  || txtsname.getText().toString().contentEquals("") ) {
                                Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_LONG).show();
                            }

                            else{

                                Log.e("Submit Button Text", signin.getText().toString());
                                username = txtusername.getText().toString();
                                password = txtpassword.getText().toString();
                                Pconfirm = txtpasswordcon.getText().toString();
                                email = txtemail.getText().toString();

                                telno = txttelno.getText().toString();
                                fname = txtfname.getText().toString();
                                sname = txtsname.getText().toString();

                                code = txtcode.getText().toString();

                                if(password.contentEquals(Pconfirm) ) {
                                    saveUser(password,sname,email,fname,username);
                                }

                                else{
                                    Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_LONG).show();

                                }






                                Log.e("Submit Button Text", signin.getText().toString());
                            }
                        }





                    }

                });



                //userid = (sharedpreferences.getString("Regid", ""));
                editnum.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        Log.e("onClick", "num from prefs" + telno);
                        Editnum(telno);   //get user id from lastinsert save code & upload editinum script



                    }

                });






            }
            catch (Exception ex)
            {
                Log.e("Main Thread Exception", "Error: " + ex.toString());
                System.gc();

            }

        }




        private void saveUser(final String password, final String surname, final String email,
        final String firstname,final String username){
            com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());



            Log.e("NUMBER", MyPref.getPhone(getApplicationContext())+"<<<<<<<<<<<<<<<<<<<<<<<<<");
            pDialog.show();
            StringRequest stringRequest=new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    pDialog.dismiss();

                    Log.e("Success",""+s);
                    //{"success":1,"message":"Username Successfully Added!"}

                    try {
                        JSONObject jsonObject=new JSONObject(s);
                        int success=jsonObject.getInt("success");
                        if(success==1){

                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);


                            //  mChangeAdapter.callbackToZero(0);
                            Toast.makeText(getApplicationContext(), "Your account was created successful", Toast.LENGTH_SHORT).show();



                        }else{
                            Toast.makeText(getApplicationContext(), (s), Toast.LENGTH_SHORT).show();
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
                    SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    Map<String,String> values=new HashMap();
                    values.put("username",username);
                    values.put("password",password);
                    values.put("surname", surname);

                    values.put("email", email);
                    values.put("telno", MyPref.getPhone(getApplicationContext()));

                    values.put("firstname", firstname);
                    values.put("user_id", (sharedpreferences.getString("user_id", "")));

                    return values;
                }
            };
            SharedPreferences sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            Log.e("RUEERROR",""+(sharedpreferences.getString("user_id", "")));

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(stringRequest);




        }

        private void saveTel(final String telno){
            com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(this);


            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Sending code...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            StringRequest stringRequest=new StringRequest(Request.Method.POST, CODE_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    pDialog.dismiss();

                    Log.e("Success",""+s);
                    //{"success":1,"message":"Username Successfully Added!"}

                    try {
                        JSONObject jsonObject=new JSONObject(s);
                        int success=jsonObject.getInt("success");
                        if(success==1){

                            Log.e("Success", "" + telno);

                            pdetails.setVisibility(View.GONE);
                            adetails.setVisibility(View.GONE);
                            cdetails.setVisibility(View.VISIBLE);



                            signin.setText("SEND CODE");


                            MyPref.savePhoneNumber(getApplicationContext(),telno);


                            Toast.makeText(getApplicationContext(),"Code sent!", Toast.LENGTH_SHORT).show();


                            JSONArray array=jsonObject.getJSONArray("posts");

                            JSONObject object=array.getJSONObject(0);



                        }else{
                            Toast.makeText(getApplicationContext(), ("Number already in use!"), Toast.LENGTH_SHORT).show();
                        }



                    } catch (JSONException e) {

                        Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();

                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    pDialog.dismiss();

                    Log.e("RUEERROR",""+volleyError);


                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> values=new HashMap();
                    values.put("telno", telno);

                    return values;
                }
            };
            requestQueue.add(stringRequest);




        }

        private void saveCode(final String code, final String telno){
            com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
            pDialog.setMessage("Verifying user...");
            pDialog.show();
            StringRequest stringRequest=new StringRequest(Request.Method.POST, VERIFY_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    pDialog.dismiss();

                    Log.e("Success",""+s);
                    //{"success":1,"message":"Username Successfully Added!"}

                    try {
                        JSONObject jsonObject=new JSONObject(s);
                        int success=jsonObject.getInt("success");
                        if(success==1){

                            String codeDB=jsonObject.getString("code");



                            if(!code.equals(codeDB)){

                                Toast.makeText(getApplicationContext(), "Invalid code", Toast.LENGTH_LONG).show();

                                return;
                            }


                            Log.e("Submit Button Text", signin.getText().toString());
                            pdetails.setVisibility(View.VISIBLE);
                            adetails.setVisibility(View.GONE);
                            cdetails.setVisibility(View.GONE);
                            signin.setText("SIGN UP");


                            Toast.makeText(getApplicationContext(), "Verification successful", Toast.LENGTH_SHORT).show();



                            String UserId=jsonObject.getString("user_id");

                            SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString("user_id", UserId);
                            editor.commit();


                        }else{
                            Toast.makeText(getApplicationContext(), ("Verification failed"), Toast.LENGTH_SHORT).show();
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
                    values.put("code", code);
                    values.put("telno", telno);

                    return values;
                }
            };
            requestQueue.add(stringRequest);




        }




        private void Editnum(final String TElNo){
            com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());


            StringRequest stringRequest=new StringRequest(Request.Method.POST, EDIT_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {

                    Log.e("Success",""+s);
                    //{"success":1,"message":"Username Successfully Added!"}

                    try {
                        JSONObject jsonObject=new JSONObject(s);
                        int success=jsonObject.getInt("success");
                        if(success==1){




                            Toast.makeText(getApplicationContext(), "Number edit", Toast.LENGTH_SHORT).show();

                            pdetails.setVisibility(View.GONE);
                            cdetails.setVisibility(View.GONE);
                            adetails.setVisibility(View.VISIBLE);
                            signin.setText("VERIFY NUMBER");


                        }else{
                            Toast.makeText(getApplicationContext(), ("Cannot edit number"), Toast.LENGTH_SHORT).show();
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
                    values.put("telno",TElNo);

                    return values;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(stringRequest);




        }




    }
