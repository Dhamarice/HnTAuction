package com.hammerandtongues.online.hntauction;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import preferences.MyPrefAddress;

/**
 * Created by Ruvimbo on 2/2/2018.
 */

public class UserDetails extends Fragment implements View.OnClickListener{
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    SharedPreferences sharedpreferences;
    private   RadioGroup rg02;
    private  LinearLayout ll01,ll02;
    private Spinner spin, spin1, spcountry, spprovince, spcity, spsurburb;
    private String type,value, totalprice, chrg;
    private TextView txtaddress , txterr;
    private EditText txtaddline1,txtaddline2;
    private LinearLayout main,top;
    private RadioGroup rdg;
    private  RadioButton rdNormal;
    private RadioButton   rdExpress;
    private RadioButton rdExtended ;



    // Progress Dialog
    private ProgressDialog pDialog;

    private static final String GETPUSER_URL = "https://devshop.hammerandtongues.com/webservice/getuserdetail.php";
    private static final String GETCITIES_URL = "https://devshop.hammerandtongues.com/webservice/getcities.php";
    private static final String GETSURBURBS_URL = "https://devshop.hammerandtongues.com/webservice/getsurburbs.php";
    private static final String GETPICKUPLOCATIONS_URL = "https://devshop.hammerandtongues.com/webservice/getpickuplocations.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_PRODUCTDETAILS = "posts";

    String UserID, Address, Options, city, Address1, Address2, Country, Region, Surbub="";
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private int success;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.userdetail, container, false);

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        System.gc();

        final SharedPreferences shared = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        rg02 = (RadioGroup) view.findViewById(R.id.radioGroup2);
        ll01 = (LinearLayout) view.findViewById(R.id.editables);
        ll02 = (LinearLayout) view.findViewById(R.id.dlvryoptions);
        spin = (Spinner) view.findViewById(R.id.spin_city);
        spin1 = (Spinner) view.findViewById(R.id.spin_pickuplocations);
        spcountry = (Spinner) view.findViewById(R.id.spin_country);
        spprovince = (Spinner) view.findViewById(R.id.spin_Province);
        spcity = (Spinner) view.findViewById(R.id.spin_city1);
        spsurburb = (Spinner) view.findViewById(R.id.spin_surburbs);
        txtaddress = (TextView) view.findViewById(R.id.address);
        txtaddline1 = (EditText) view.findViewById(R.id.addressLn1);
        txtaddline1 = (EditText) view.findViewById(R.id.addressLn2);
        txterr = (TextView) view.findViewById(R.id.txtinfo);
        main = (LinearLayout) view.findViewById(R.id.editables);
        //top = (LinearLayout) view.findViewById(R.id.dlvryoptions);


        rdg = new RadioGroup(getContext());
        rdNormal = new RadioButton(getContext());
        rdExpress = new RadioButton(getContext());
        rdExtended = new RadioButton(getContext());

        if (shared.getString("userid", "") != null && shared.getString("userid", "") != "") {
            UserID = (shared.getString("userid", ""));
            Address = (shared.getString("address", ""));


            Options = (shared.getString("options", ""));
            // UserID=(shared.getString("userid", ""));
            //Address=(shared.getString("address", ""));;
            //totalprice = (shared.getString("total", ""));
            Address1 = MyPrefAddress.getAddress1(getContext());
            Address2 = MyPrefAddress.getAddress2(getContext());
            Country = MyPrefAddress.getCountry(getContext());
            city = MyPrefAddress.getRegion(getContext());
            Region = MyPrefAddress.getRegion(getContext());
            Surbub = MyPrefAddress.getSurbub(getContext());
            //Radio Button Delivery Selected


            ll01.setVisibility(View.GONE);
            ll02.setVisibility(View.VISIBLE);


            view.findViewById(R.id.rd_delivery).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //Make Delivery Options Visible
                    rg02.setVisibility(View.VISIBLE);
                    //Make Alternative Address Details Invisible
                    ll01.setVisibility(View.GONE);
                    //Make City Spinner invisible
                    spin.setAdapter(null);
                    spin.setVisibility(View.GONE);

                    spin1.setAdapter(null);
                    spin1.setVisibility(View.GONE);

                    txtaddress.setText(Address);
                    txtaddress.setVisibility(View.VISIBLE);
                    rdg.setVisibility(View.VISIBLE);
                    rdExpress.setVisibility(View.VISIBLE);
                    rdExtended.setVisibility(View.VISIBLE);
                    rdNormal.setVisibility(View.VISIBLE);

                    ll02.removeView(rdg);
                    // Toast.makeText(getContext(),"Delivery Selected",Toast.LENGTH_SHORT).show();
                }
            });

            //Radio Button Pickup Selected
            view.findViewById(R.id.rd_pickup).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //MAKE DELIVERY OPTIONS INVISIBLE
                    rg02.setVisibility(View.GONE);
                    ll02.removeView(rdg);
                    //Make Alternative Address Details Invisible
                    ll01.setVisibility(View.GONE);
                    // Toast.makeText(getContext(),"Pickup Selected",Toast.LENGTH_SHORT).show();

                    //Make City Spinner visible
                    if (isNetworkAvailable() == true) {
                        new getCities().execute();
                    } else {
                        Toast.makeText(getContext(), "Please Check Your Network Connectivity", Toast.LENGTH_LONG).show();
//                        pDialog.dismiss();
                    }
                    spin.setVisibility(View.VISIBLE);
                    txtaddress.setVisibility(View.GONE);
                }
            });

            //Radio Button Registered Address Selected
            view.findViewById(R.id.rd_RegAdd).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    try {
                        //Make Alternative Address Details Invisible
                        ll01.setVisibility(View.GONE);
                        ll02.removeView(rdg);
                        //Make City Spinner invisible
                        //rdg.removeAllViews();
                        spin.setVisibility(View.GONE);
                        //txtaddress.setText(Address);
                        txtaddress.setVisibility(View.VISIBLE);
                        txtaddress.setText(Address);
                        rdExpress.setVisibility(View.VISIBLE);
                        rdExtended.setVisibility(View.VISIBLE);
                        rdNormal.setVisibility(View.VISIBLE);
                        rdNormal.setText("Normal ($5)");
                        rdExpress.setText("Express ($10)");
                        rdExtended.setText("Extended ($3)");
                        if (Address.toUpperCase().contains("HARARE") || Address.toUpperCase().contains("BULAWAYO")) {
                            rdg.removeAllViews();
                            rdg.addView(rdNormal);
                            rdg.addView(rdExpress);
                            rdg.addView(rdExtended);
                            rdNormal.setText("Normal ($5)");
                            rdExpress.setText("Express ($10)");
                            rdExtended.setText("Extended ($3)");
                            rdg.setOrientation(LinearLayout.VERTICAL);
                            rdg.setVisibility(View.VISIBLE);
                        } else {
                            txterr.setText("No Delivery Service Available for Location of Registered Address");
                        }
                    } catch (Exception ex) {
                        Log.e("Error:", ex.toString());
                    }
                }
            });

            //Radio Button Alternative address Selected
            view.findViewById(R.id.rd_AltAdd).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    //Make Alternative Address Details Visible
                    ll01.setVisibility(View.VISIBLE);
                    //Make City Spinner invisible
                    spin.setVisibility(View.GONE);
                    txtaddress.setVisibility(View.GONE);
                    //Get Regions Zimbabwe
                    if (spcountry.getSelectedItemPosition() == 1) {
                        type = "Province";
                        if (isNetworkAvailable() == true) {
                            new getDropDownValues().execute();
                        } else {
                            Toast.makeText(getContext(), "Please Check Your Network Connectivity", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        txterr.setText("No Delivery Option available for the Selected Country");
                    }
                }
            });


            final RadioButton rddlvry, rdpckup, rdregadd, rdaltadd;
            rddlvry = (RadioButton) view.findViewById(R.id.rd_delivery);
            rdpckup = (RadioButton) view.findViewById(R.id.rd_pickup);
            rdregadd = (RadioButton) view.findViewById(R.id.rd_RegAdd);
            rdaltadd = (RadioButton) view.findViewById(R.id.rd_AltAdd);


            final Button btn_next;
            btn_next = (Button) view.findViewById(R.id.btn_submit);
            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!rddlvry.isChecked() && !rdpckup.isChecked()) {
                        Toast.makeText(getActivity(), "Please Select either Delivery or Pickup Options", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (rddlvry.isChecked()) {
                        if (!rdregadd.isChecked() && !rdaltadd.isChecked()) {
                            Toast.makeText(getActivity(), "Please Select either Registered Add or Alternative", Toast.LENGTH_LONG).show();
                            return;
                        } else if (rdregadd.isChecked() && !rdaltadd.isChecked()) {

                            //rdg.setVisibility(View.VISIBLE);
                            //rdExpress.setVisibility(View.VISIBLE);
                            //rdExtended.setVisibility(View.VISIBLE);
                            //rdNormal.setVisibility(View.VISIBLE);
                            txtaddress.setVisibility(View.VISIBLE);
                            //rdg.removeAllViews();
                            //ll02.removeView(rdg);
                            //rdg.addView(rdNormal);
                            //rdg.addView(rdExpress);
                            //rdg.addView(rdExtended);
                            //rdg.setOrientation(LinearLayout.HORIZONTAL);
                            //ll02.addView(rdg);

                            rdg.removeAllViews();
                            ll02.removeAllViews();
                            rdg.addView(rdNormal);
                            rdg.addView(rdExpress);
                            rdg.addView(rdExtended);
                            rdNormal.setText("Normal ($5)");
                            rdExpress.setText("Express ($10)");
                            rdExtended.setText("Extended ($3)");
                            rdg.setOrientation(LinearLayout.VERTICAL);
                            rdg.setVisibility(View.VISIBLE);
                            ll02.addView(rdg);


                            if (txtaddress.getText().toString().isEmpty()) {
                                Toast.makeText(getActivity(), "No Valid Registered Delivery Address Detected", Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                if (rdg.getCheckedRadioButtonId() > -1) {
                                    int selectedId = rdg.getCheckedRadioButtonId();
// find the radiobutton by returned id
                                    RadioButton rd = (RadioButton) getActivity().findViewById(selectedId);
                                    String chrg = rd.getText().toString();

                                    int startIndex = chrg.indexOf("(");
                                    int endIndex = chrg.indexOf(")");
                                    String id1 = chrg.substring(startIndex + 2, endIndex);

                                    String DlvryAdd;
                                    DlvryAdd = Address1 + Address2 + Region + Surbub + city + Country;
                                    SharedPreferences.Editor editor = shared.edit();
                                    editor.putString("DlvryChrg", id1);
                                    editor.putString("DlvryAdd", DlvryAdd);
                                    editor.putString("DlvryType", "Delivery");
                                    editor.commit();
                                    Toast.makeText(getContext(), "Your Delivery Charge is: $" + id1, Toast.LENGTH_LONG).show();

                                    nexttab();
                                } else {
                                    Toast.makeText(getActivity(), "Please Select a Delivery Service (Normal / Express / Extended)", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        } else if (!rdregadd.isChecked() && rdaltadd.isChecked()) {
                            if (txtaddline1.getText().toString().isEmpty()) {
                                Toast.makeText(getActivity(), "Invalid Delivery Address Detected", Toast.LENGTH_LONG).show();
                                return;
                            }

                            if (rdg.getCheckedRadioButtonId() > -1) {
                                int selectedId = rdg.getCheckedRadioButtonId();
// find the radiobutton by returned id
                                RadioButton rd = (RadioButton) getActivity().findViewById(selectedId);
                                String chrg = rd.getText().toString();

                                int startIndex = chrg.indexOf("(");
                                int endIndex = chrg.indexOf(")");
                                String id1 = chrg.substring(startIndex + 2, endIndex);

                                String DlvryAdd;
                                DlvryAdd = Address1 + "," + Address2 +
                                        "," + Country + "," +
                                        Region + "," +
                                        Surbub;

                                SharedPreferences.Editor editor = shared.edit();
                                editor.putString("DlvryChrg", id1);
                                editor.putString("DlvryAdd", DlvryAdd);
                                editor.putString("DlvryType", chrg);
                                editor.commit();
                                Toast.makeText(getContext(), "Your Delivery Charge is: $" + id1, Toast.LENGTH_LONG).show();
                                nexttab();
                            } else {
                                Toast.makeText(getActivity(), "Invalid Delivery Address / Delivery Option Selected", Toast.LENGTH_LONG).show();
                                return;
                            }


                        } else {
                            nexttab();
                        }
                    }

                    if (rdpckup.isChecked()) {
                        if (spin.getSelectedItem().toString().isEmpty() || spin.getSelectedItem().toString().contains("Select")) {
                            Toast.makeText(getActivity(), "Please Select a City/Town", Toast.LENGTH_LONG).show();
                            return;
                        }
                        //if (!rdg.isSelected()) {
                        String city = spin.getSelectedItem().toString();
                        if (city.contentEquals("Harare")) {
                            Spinner sItems = (Spinner) getActivity().findViewById(R.id.spin_pickuplocations);

                            chrg = sItems.getSelectedItem().toString();
                            if (!chrg.isEmpty() && chrg != null) {
                                int startIndex = chrg.indexOf("(");
                                int endIndex = chrg.indexOf(")");
                                String id1 = chrg.substring(startIndex + 2, endIndex);
                                SharedPreferences.Editor editor = shared.edit();
                                editor.putString("DlvryChrg", id1);
                                editor.putString("DlvryType", "PickUp");
                                editor.putString("DlvryAdd", chrg);
                                editor.commit();
                                Toast.makeText(getContext(), "Your Pickup Charge is: $" + id1, Toast.LENGTH_LONG).show();
                                nexttab();
                            }
                        } else {
                            Toast.makeText(getActivity(), "No Pickup Locations in Selected City" + spin.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        //}
                    }

                }
            });
            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    final String newValue = (String) spin.getItemAtPosition(position);
                    //Toast.makeText(getContext(),"city selected" +newValue,Toast.LENGTH_LONG).show();
                    txtaddress.setText("");
                    txtaddress.setVisibility(View.GONE);

                    city = newValue;
                    if (isNetworkAvailable() == true) {
                        new getPickUpLocations().execute();
                    } else {
                        Toast.makeText(getContext(), "Please Check Your Network Connectivity", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });


            spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    final String newValue = (String) spin1.getItemAtPosition(position);
                    //Toast.makeText(getContext(),"city selected" +newValue,Toast.LENGTH_LONG).show();
                    txtaddress.setText(newValue);
                    txtaddress.setVisibility(View.VISIBLE);
                    txtaddress.setTextColor(getResources().getColor(R.color.colorAccent));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            spcountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    final String newValue = (String) spin.getItemAtPosition(position);
                    //Toast.makeText(getContext(),"city selected" +newValue,Toast.LENGTH_LONG).show();
                    if (spcountry.getSelectedItemPosition() == 0) {
                        type = "Province";
                        value = "Zimbabwe";

                        if (isNetworkAvailable() == true) {
                            new getDropDownValues().execute();
                        } else {
                            Toast.makeText(getContext(), "Please Check Your Network Connectivity", Toast.LENGTH_LONG).show();
                            //pDialog.dismiss();
                        }
                        txterr.setText("");
                        txterr.setVisibility(View.GONE);
                    } else {
                        txterr.setText("No Delivery Option available for the Selected Country");
                        txterr.setVisibility(View.VISIBLE);
                        spprovince.setAdapter(null);
                        spcity.setAdapter(null);
                        spsurburb.setAdapter(null);

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            spprovince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    final String newValue = (String) spprovince.getItemAtPosition(position);
                    //Toast.makeText(getContext(),"city selected" +newValue,Toast.LENGTH_LONG).show();
                    if (spprovince.getSelectedItemPosition() < 2) {
                        type = "City";
                        value = newValue;

                        if (isNetworkAvailable() == true) {
                            new getDropDownValues().execute();
                        } else {
                            Toast.makeText(getContext(), "Please Check Your Network Connectivity", Toast.LENGTH_LONG).show();
                        }
                        txterr.setText("");
                        txterr.setVisibility(View.GONE);
                    } else {
                        txterr.setText("No Delivery Option available for the Selected Province");
                        txterr.setVisibility(View.VISIBLE);
                        spcity.setAdapter(null);
                        spsurburb.setAdapter(null);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
            spcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    final String newValue = (String) spcity.getItemAtPosition(position);
                    //Toast.makeText(getContext(),"city selected" +newValue,Toast.LENGTH_LONG).show();
                    type = "Surburb";
                    value = newValue;

                    if (isNetworkAvailable() == true) {
                        new getDropDownValues().execute();
                    } else {
                        Toast.makeText(getContext(), "Please Check Your Network Connectivity", Toast.LENGTH_LONG).show();
                    }
                    txterr.setText("");
                    txterr.setVisibility(View.GONE);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            spsurburb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    final String newValue = (String) spsurburb.getItemAtPosition(position);
                    //Toast.makeText(getContext(),"city selected" +newValue,Toast.LENGTH_LONG).show();
                    type = "DlvryChrg";
                    value = newValue;

                    if (isNetworkAvailable() == true) {
                        new getDropDownValues().execute();
                    } else {
                        Toast.makeText(getContext(), "Please Check Your Network Connectivity", Toast.LENGTH_LONG).show();
                    }
                    txterr.setText("");
                    txterr.setVisibility(View.GONE);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
        }
            return view;
        }


    public void nexttab()
    {
        TabLayout tabLayout=(TabLayout) getActivity().findViewById(R.id.tabs02);
        final ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager02);
        //final PagerAdapter adapter = new PagerAdapter02 (getFragmentManager(), tabLayout.getTabCount());
        //viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2);
        Log.e("Next 02", "Thus Far the lord has brought us");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity(). getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_submit:

                TabLayout tabLayout=(TabLayout) getActivity().findViewById(R.id.tabs02);
                final ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager02);
                //final PagerAdapter adapter = new PagerAdapter02 (getFragmentManager(), tabLayout.getTabCount());
                //viewPager.setAdapter(adapter);
                viewPager.setCurrentItem(2);
                Log.e("Next 02", "Thus Far the lord has brought us");
                break;


            default:
                break;
        }

    }



    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting Product Details...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String pid = UserID;

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("productid", pid));


                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json1 = jsonParser.makeHttpRequest(
                        GETPUSER_URL, "POST", params);
                Log.e("PID + URL",UserID + GETPUSER_URL);
                // check your log for json response
                Log.d("Login attempt", json1.toString());

                // json success tag
                success = json1.getInt(TAG_SUCCESS);
                if (success == 1) {
                    return json1.getString(TAG_PRODUCTDETAILS);
                } else {
                    return json1.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String posts) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (posts != null) {

                   // Toast.makeText(getActivity(), posts, Toast.LENGTH_LONG).show();
                }

            }

        }

    class getPickUpLocations extends AsyncTask<String, String, String> {

            /**
             * Before starting background thread Show Progress Dialog
             */
            boolean failure = false;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Getting Pick Up Locations...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            @Override
            protected String doInBackground(String... args) {
                // TODO Auto-generated method stub
                // Check for success tag
                int success;
                String pid = UserID;

                try {
                    // Building Parameters
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("city", city));

                    // getting product details by making HTTP request
                    JSONObject json1 = jsonParser.makeHttpRequest(
                            GETPICKUPLOCATIONS_URL, "POST", params);
                    Log.d("GET CITIES", GETPICKUPLOCATIONS_URL);
                    // check your log for json response

                    // json success tag
                    success = json1.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        return json1.getString(TAG_PRODUCTDETAILS);
                    } else {
                        return json1.getString(TAG_MESSAGE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;

            }

            /**
             * After completing background task Dismiss the progress dialog
             **/
            protected void onPostExecute(String posts) {
                // dismiss the dialog once product deleted
                pDialog.dismiss();
                if (posts != null) {
                    JSONArray jsonarray02 = null;
                    try {
                        jsonarray02 = new JSONArray(posts);
                    } catch (JSONException e) {
                    }
                    List<String> spinnerArray =  new ArrayList<String>();

                    if (jsonarray02 != null) {
                        if (jsonarray02.length() > 0) {
                            try {
                                Log.e("JSONing", jsonarray02.toString());

                                for (int i = 0; i < jsonarray02.length() - 1; i++) {
                                    String name, chrg = "";
                                    JSONObject jsonobject = jsonarray02.getJSONObject(i);
                                    name = Html.fromHtml(jsonobject.optString("shop")).toString();
                                    chrg = Html.fromHtml(jsonobject.optString("chrge")).toString();
                                    spinnerArray.add("($" + chrg + ".00)  " +name);
                                }
                            } catch (JSONException e) {
                                Log.e("PickUp Locations Error", e.toString());
                                e.printStackTrace();
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    getContext(), android.R.layout.simple_spinner_item, spinnerArray);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Spinner sItems = (Spinner) getActivity().findViewById(R.id.spin_pickuplocations);
                            sItems.setAdapter(adapter);
                            sItems.setVisibility(View.VISIBLE);

                        } else {
                            Spinner sItems = (Spinner) getActivity().findViewById(R.id.spin_pickuplocations);
                            sItems.setAdapter(null);
                            sItems.setVisibility(View.GONE);
                            txtaddress.setText("No Pick Up Points Available in Your City/Town: " + city);
                            txtaddress.setTextColor(getResources().getColor(R.color.colorOrange));
                            //spin.setSelection(0);
                            txtaddress.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "No Pick Up Points Available in Your City/Town: " + city, Toast.LENGTH_LONG).show();
                            Log.d("Spinner Value ", city);
                        }
                    }
                    //Toast.makeText(getActivity(), posts, Toast.LENGTH_LONG).show();
                }

            }

        }
    class getCities extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting Pick Up Cities...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String pid = UserID;


                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("", pid));

                // getting product details by making HTTP request
                JSONObject json1 = jsonParser.makeHttpRequest(
                        GETCITIES_URL, "POST", params);
                Log.d("GET CITIES", UserID + GETCITIES_URL);
                // check your log for json response

                // json success tag
                try {
                    if (TAG_SUCCESS != null) {
                        success = json1.getInt(TAG_SUCCESS);
                        Log.d("JSon Results", "Success-" + json1.getInt(TAG_SUCCESS) + "  |Message-" + json1.getString(TAG_PRODUCTDETAILS));

                        if (success == 1) {
                            return json1.getString(TAG_PRODUCTDETAILS);


                        } else {
                            return json1.getString(TAG_MESSAGE);
                        }
                    }
                } catch (Exception e) {

//                    Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
  //                  Log.e("Ecocashresp error: ", e.getMessage());


                }


            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String posts) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (posts != null) {
                JSONArray jsonarray02 = null;
                try {
                    jsonarray02 = new JSONArray(posts);
                } catch (JSONException e) {
                }
                List<String> spinnerArray =  new ArrayList<String>();

                if (jsonarray02 != null) {
                    try {
                        Log.e("JSONing", jsonarray02.toString());

                        for (int i =0; i< jsonarray02.length()-1; i++) {
                            String name="";
                            JSONObject jsonobject = jsonarray02.getJSONObject(i);
                            name = Html.fromHtml(jsonobject.optString("cty_name")).toString();
                            spinnerArray.add(name);
                        }
                    } catch (JSONException e) {
                        Log.e("Cities JSON Error: ", e.toString());
                        e.printStackTrace();
                        pDialog.dismiss();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getContext(), android.R.layout.simple_spinner_item, spinnerArray);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Spinner sItems = (Spinner) getActivity(). findViewById(R.id.spin_city);
                    sItems.setAdapter(adapter);
                }
                //Toast.makeText(getActivity(), posts, Toast.LENGTH_LONG).show();
            }

        }

    }


    class getDropDownValues extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting " + type +"...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("type", type));
                params.add(new BasicNameValuePair("value", value));
                // getting product details by making HTTP request
                JSONObject json1 = jsonParser.makeHttpRequest(
                        GETSURBURBS_URL, "POST", params);
                Log.d("GET CITIES", GETSURBURBS_URL);
                // check your log for json response

                // json success tag
                success = json1.getInt(TAG_SUCCESS);
                if (success == 1) {
                    return json1.getString(TAG_PRODUCTDETAILS);
                } else {
                    return json1.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

                //pDialog.dismiss();
            }

            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String posts) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (posts != null) {
                JSONArray jsonarray02 = null;
                try {
                    jsonarray02 = new JSONArray(posts);
                } catch (JSONException e) {
                }
                List<String> spinnerArray =  new ArrayList<String>();

                if (jsonarray02 != null) {
                    try {
                        Log.e("JSONing", jsonarray02.toString());

                        for (int i =0; i< jsonarray02.length(); i++) {
                            String name="";
                            JSONObject jsonobject = jsonarray02.getJSONObject(i);
                            name = Html.fromHtml(jsonobject.optString("name")).toString();
                            spinnerArray.add(name);
                        }
                    } catch (JSONException e) {
                        Log.e("Cities JSON Error: ", e.toString());
                        e.printStackTrace();
                    }
                    if (type != "DlvryChrg") {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getContext(), android.R.layout.simple_spinner_item, spinnerArray);

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        Spinner sItems;
                        if (type == "Province")
                            sItems = spprovince;
                        else if (type == "City")
                            sItems = spcity;
                        else if (type == "Surburb")
                            sItems = spsurburb;
                        else
                            sItems = null;
                        if (sItems != null)
                            sItems.setAdapter(adapter);
                    }
                    else
                    {

                        String Normal, Express, Extended="";
                        try {
                            rdg.removeAllViews();
                            Normal =String.valueOf(jsonarray02.getJSONObject(0).getJSONObject("dlvrychrges").getJSONObject("normal").getString("Large"));
                            rdNormal.setText("Normal ($" +Normal + ")");

                            Express = String.valueOf(jsonarray02.getJSONObject(0).getJSONObject("dlvrychrges").getJSONObject("express").getString("Large"));
                            rdExpress.setText("Express ($" +Express+ ")");

                            Extended = String.valueOf(jsonarray02.getJSONObject(0).getJSONObject("dlvrychrges").getJSONObject("extended").getString("Large"));
                            rdExtended.setText("Extended ($" +Extended+ ")");

                            rdg.removeAllViews();
                            //main.removeAllViews();
                            ll02.removeAllViews();
                            rdg.addView(rdNormal);
                            rdg.addView(rdExpress);
                            rdg.addView(rdExtended);
                            rdg.setOrientation(LinearLayout.HORIZONTAL);
                            rdg.setVisibility(View.VISIBLE);
                            ll02.addView(rdg);


                            //rdg.removeAllViews();
                            //ll02.removeAllViews();
                            //rdg.addView(rdNormal);
                            //rdg.addView(rdExpress);
                            //rdg.addView(rdExtended);
                            //rdNormal.setText("Normal ($5)");
                            //rdExpress.setText("Express ($10)");
                            //rdExtended.setText("Extended ($3)");
                            //rdg.setOrientation(LinearLayout.HORIZONTAL);
                            //rdg.setVisibility(View.VISIBLE);
                            //ll02.addView(rdg);

                        }catch (JSONException e) {
                            Log.e("Cities JSON Error: ", e.toString());
                            e.printStackTrace();
                        }
                    }
                }
                //Toast.makeText(getActivity(), posts, Toast.LENGTH_LONG).show();
            }

        }

    }

}
