package com.hammerandtongues.online.hntauction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
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

//import static com.hammerandtongues.online.hntonline.JSONParser.json;

/**
 *  Created by Ruvimbo on 25/1/2018..
 */

public class DatabaseHelper extends SQLiteOpenHelper {



    JSONObject json=null;

    //private static final String GETCATEGORIES_URL = "https://devshop.hammerandtongues.com/webservice/getcategories.php";
    private static final String GETAUCTIONS_URL = "http://10.0.2.2:8012/auctionwebservice/getauctionnames.php";
    private static final String GETAUCTIONSCONTENT_URL = "http://10.0.2.2:8012/auctionwebservice/getauctioncontent.php";
    private static final String GETCATEGORIES_URL = "http://10.0.2.2:8012/auctionwebservice/getauctioncategories.php";
    private static final String GETLOCATIONS_URL = "http://10.0.2.2:8012/auctionwebservice/getauctionlocations.php";
    private static final String LOGIN_URL = "https://devshop.hammerandtongues.com/webservice/login.php";
    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_CATEGORIES = "posts";
    private Context context;

    //Global Variables
    public String GET_TYPE;
    public String Categories;
    public String Products;
    public String Stores;
    private ProgressBar pDialog;


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "hntauction.db";
    //private static String DB_PATH = "/data/data/com.hammerandtongues.online.hntonline/databases/";
    //private static String DB_NAME = "hntonlinemall.db";
    private SQLiteDatabase myDataBase;
    //private final Context myContext;




    //Constructor
    public DatabaseHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {




//CREATING TABLE_AUCTIONCONTENT TABLE AND IT'S COLUMNS
        String CREATE_TABLE_AUCTIONCONTENT = "CREATE TABLE `tblauctioncontent` (\n" +
                "\t`id`\tINTEGER,\n" +
                "\t`post_id`\tINTEGER,\n" +
                "\t`date_Created`\tDATETIME DEFAULT (CURRENT_TIMESTAMP),\n" +
                "\t`quantity`\tINTEGER,\n" +
                "\t`starting_price`\tDOUBLE ( 5 , 2 ),\n" +
                "\t`reserve_price`\tDOUBLE ( 5 , 2 ),\n" +
                "\t`tax`\tDOUBLE ( 5 , 5 ),\n" +
                "\t`name`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`auction_id`\tVARCHAR ( 50 , 0 ),\n" +
                "\t`img_url`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`category`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`auction`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`location`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`grv_num`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`conditionauc`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`vihicle_condition`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`amount_times`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`amount_days`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`latitude`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`longitude`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`only_buy_now`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`current_bid`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`starting_date`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`closing_date`\tVARCHAR ( 500 , 0 ),\n" +
                "\tPRIMARY KEY(`id`)\n" +
                ");";
        db.execSQL(CREATE_TABLE_AUCTIONCONTENT);



        //CREATING TABLE_AUCTIONS TABLE AND IT'S COLUMNS
    String CREATE_TABLE_AUCTIONS = "CREATE TABLE `tblauctions` (\n" +
            "\t`id`\tINTEGER,\n" +
            "\t`post_id`\tINTEGER,\n" +
            "\t`date_Created`\tDATETIME DEFAULT (CURRENT_TIMESTAMP),\n" +
            "\t`name`\tVARCHAR ( 500 , 0 ),\n" +
            "\t`slug`\tVARCHAR ( 500 , 0 ),\n" +
            "\t`minimun_increament`\tVARCHAR ( 500 , 0 ),\n" +
            "\t`deposit`\tVARCHAR ( 500 , 0 ),\n" +
            "\t`discount`\tVARCHAR ( 500 , 0 ),\n" +
            "\t`end_date`\tVARCHAR ( 500 , 0 ),\n" +
            "\tPRIMARY KEY(`id`)\n" +
            ");";
        db.execSQL(CREATE_TABLE_AUCTIONS);



    //CREATING TABLE_CATEGORIES TABLE AND IT'S COLUMNS
    String CREATE_TABLE_CATEGORIES = "CREATE TABLE `tblcategories` (\n" +
                "\t`id`\tINTEGER,\n" +
                "\t`post_id`\tINTEGER,\n" +
                "\t`date_Created`\tDATETIME DEFAULT (CURRENT_TIMESTAMP),\n" +
                "\t`name`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`slug`\tVARCHAR ( 500 , 0 ),\n" +
                "\tPRIMARY KEY(`id`)\n" +
                ");";
        db.execSQL(CREATE_TABLE_CATEGORIES);

        //CREATING TABLE_LOCATIONS TABLE AND IT'S COLUMNS
        String CREATE_TABLE_LOCATIONS = "CREATE TABLE `tbllocations` (\n" +
                "\t`id`\tINTEGER,\n" +
                "\t`post_id`\tINTEGER,\n" +
                "\t`date_Created`\tDATETIME DEFAULT (CURRENT_TIMESTAMP),\n" +
                "\t`name`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`slug`\tVARCHAR ( 500 , 0 ),\n" +
                "\tPRIMARY KEY(`id`)\n" +
                ");";
        db.execSQL(CREATE_TABLE_LOCATIONS);



        String CREATE_TABLE_USER = "CREATE TABLE `tbluser` (\n" +
                "\t`id`\tINTEGER,\n" +
                "\t`post_id`\tINTEGER,\n" +
                "\t`date_Created`\tDATETIME DEFAULT (CURRENT_TIMESTAMP),\n" +
                "\t`name`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`username`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`password`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`bid_product`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`bid_amount`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`bid_date`\tVARCHAR ( 500 , 0 ),\n" +
                "\tPRIMARY KEY(`id`)\n" +
                ");";
        db.execSQL(CREATE_TABLE_USER);


        String CREATE_TABLE_BIDS = "CREATE TABLE `tblbids` (\n" +
                "\t`id`\tINTEGER,\n" +
                "\t`post_id`\tINTEGER,\n" +
                "\t`date_Created`\tDATETIME DEFAULT (CURRENT_TIMESTAMP),\n" +
                "\t`bid_amount`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`product`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`winner`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`date_made`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`date_won`\tVARCHAR ( 500 , 0 ),\n" +
                "\tPRIMARY KEY(`id`)\n" +
                ");";
        db.execSQL(CREATE_TABLE_BIDS);
}


    //upgrading the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE_PRODUCTS = "DROP TABLE IF EXISTS tblauctions";
        db.execSQL(DROP_TABLE_PRODUCTS);



        onCreate(db);
    }



    public Cursor getProduct (int ProdID) {
        //String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " =  \"" + statusid + "\"";
        String mod_query = "select distinct c.*, a.name, a.minimun_increament, l.name from tblauctioncontent c inner join tblauctions a on c.auction = a.post_id inner join tbllocations l on c.location = l.post_id where c.post_id = " + ProdID +" LIMIT  1";
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor cursor = db.rawQuery(mod_query, null);

        Log.e("getProduct", "getProduct: " + mod_query );

        if (cursor.moveToFirst()) {
//            cursor.moveToFirst();

            return cursor;
        } else {
            //db.close();
            return null;
        }

    }




    public Cursor getmybids(String UserId) {
        //String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " =  \"" + statusid + "\"";
        String mod_query = "select b.*, p.name from tblbids b inner join tblauctioncontent p on b.product = p.post_id where b.post_id=" + UserId;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(mod_query, null);

        if (cursor.moveToFirst()) {
//            cursor.moveToFirst();

            return cursor;
        } else {
            //db.close();
            return null;
        }

    }




    public Cursor getallAuctions() {
        //String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " =  \"" + sta'tusid + "\"";
        String mod_query = "select DISTINCT a.*, ac.starting_date, ac.closing_date from tblauctions a inner join tblauctioncontent ac on a.post_id = ac.auction GROUP BY a.post_id;";

        Log.e("getallAuction", "getallAuctions: " + mod_query );
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(mod_query, null);

        if (cursor.moveToFirst()) {
//            cursor.moveToFirst();

            return cursor;
        } else {
            db.close();
            return null;
        }

    }




    public Cursor getAuctionsContets(int AuctionID, int limit, int offset, String lastid) {
        //String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " =  \"" + sta'tusid + "\"";
        String mod_query = "select distinct * from tblauctioncontent where auction = " + AuctionID +" and  id > " + lastid + " ORDER BY id asc LIMIT  " + limit + " Offset " + offset;


        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(mod_query, null);

        Log.e("getAuctionsContets: ", "Query run" + mod_query );

        if (cursor.moveToFirst()) {
//            cursor.moveToFirst();

            return cursor;
        } else {
            db.close();
            return null;
        }

    }








    public void clearAuctions() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tblauctions", "1", null);
    }

    public void clearCategories() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tblcategories", "1", null);
    }

    public void clearLocation() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tbllocations", "1", null);
    }








    public String getStoreBanner(int StoreID) {
        //String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " =  \"" + statusid + "\"";
        String mod_query = "select distinct  banner_url from tblauctions s  where s.post_id=" + StoreID + " Limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("GETSTOREPRODUCTS", mod_query);
        Cursor cursor = db.rawQuery(mod_query, null);
        String url;
        if (cursor.moveToFirst()) {
//            cursor.moveToFirst();
            url = cursor.getString(0).toString();
            return url;
        } else {
            db.close();
            return null;
        }
    }


    public void fill_bids( String user_id, String bid_amount, String product_id){
        String INSERT_TABLE_CART = "INSERT INTO tblbids (post_id ,bid_amount, product) VALUES ('" + user_id + "', '" + bid_amount + "', '" + product_id + "' )";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(INSERT_TABLE_CART);

        Log.e("Fill Bids query", INSERT_TABLE_CART);
    }

    public void fillfav(int ProductID){
        String INSERT_TABLE_FAV = "INSERT INTO tbl_favorites (type_id , type, value_type) VALUES (" + ProductID + ", 'Product', 'Big')";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(INSERT_TABLE_FAV);
    }


    public Cursor getProductFav( String lastid) {
        String mod_query = "select DISTINCT c.* from  tbl_Products c  inner join tbl_favorites f where f.type_id=c.post_id and f.id  ORDER BY f.id asc";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("GETSTOREFAV", mod_query);
        Cursor cursor = db.rawQuery(mod_query, null);

        if (cursor.moveToFirst()) {
//            cursor.moveToFirst();

            return cursor;
        } else {
            db.close();
            return null;
        }
    }


    public Cursor viewFavs(String lastid) {
        String mod_query = "select DISTINCT * from  tbl_favorites  ORDER BY id asc ";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("GETSTOREFAV", mod_query);
        Cursor cursor = db.rawQuery(mod_query, null);

        if (cursor.moveToFirst()) {
//            cursor.moveToFirst();

            return cursor;
        } else {
            db.close();
            return null;
        }
    }

    public Cursor RemoveFavs(int ProductID ) {
        String mod_query = "delete from  tbl_favorites where type_id =" + ProductID ;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("GETSTOREFAV", mod_query);
        Cursor cursor = db.rawQuery(mod_query, null);

        if (cursor.moveToFirst()) {
//            cursor.moveToFirst();

            return cursor;
        } else {
            db.close();
            return null;
        }
    }



    public void clearAuctionscontent() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tblauctioncontent", "1", null);
    }






    public void fill_auctionscontent(Context context){
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(context);
        final SQLiteDatabase db = this.getWritableDatabase();
        //this.context= context;
        //pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, GETAUCTIONSCONTENT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //pDialog.dismiss();

                Log.e("Success",""+s);
                Log.e("Zitapass", "" + s);
                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonobject = new JSONObject(s);
                    int success = jsonobject.getInt("success");
                    if (success == 1) {


                        JSONArray array = jsonobject.getJSONArray("posts");

                        for (int i = 0; i < array.length(); i++) {
                            try {

                                JSONObject object = array.getJSONObject(i);

                                String post_id = object.optString("post_id");
                                String auc_name = object.optString("name");
                                String img_url = object.optString("imgurl");
                                String category = object.optString("category");
                                String auction = object.optString("auction");
                                String location = object.optString("location");
                                String only_buy_now = object.optString("only_buy_now");
                                String tax = object.optString("tax");
                                String grv_num = object.optString("grv_num");
                                String conditionauc = object.optString("conditionauc");
                                String vihicle_condition = object.optString("vihicle_condition");
                                String amount_times = object.optString("amount_times");
                                String amount_days = object.optString("amount_days");
                                String quantity = object.optString("quantity");
                                String starting_price = object.optString("starting_price");
                                String reserve_price = object.optString("reserve_price");
                                String latitude = object.optString("latitude");
                                String longitude = object.optString("longitude");
                                String starting = object.optString("post_date");
                                String closing = object.optString("closed_date");
                                String highbid = object.optString("current_bid");



                                ContentValues values = new ContentValues();
                                values.put("post_id", post_id);
                                values.put("name",auc_name);
                                values.put("img_url", img_url);
                                values.put("category", category);
                                values.put("auction",auction);
                                values.put("location", location);
                                values.put("only_buy_now",only_buy_now);
                                values.put("tax", tax);
                                values.put("grv_num", grv_num);
                                values.put("conditionauc", conditionauc);
                                values.put("vihicle_condition", vihicle_condition);
                                values.put("amount_times", amount_times);
                                values.put("amount_days", amount_days);
                                values.put("quantity", quantity);
                                values.put("starting_price", starting_price);
                                values.put("reserve_price", reserve_price);
                                values.put("latitude", latitude);
                                values.put("longitude", longitude);
                                values.put("starting_date", starting);
                                values.put("closing_date", closing);
                                values.put("current_bid", highbid);
                                db.insert("tblauctioncontent", null, values);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }
                    }

                    else{
                        Log.e("Success", "Not success" + s);
                        db.close();
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //pDialog.dismiss();
                volleyError.printStackTrace();
                Log.e("RUEERROR",""+volleyError);
            }
        }){
            //@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> values=new HashMap();
                // values.put("username",pword);
                //values.put("password",uname);


                return values;
            }

        };

        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000,0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        //requestQueue.add(stringRequest);
    }


    public void fill_auctions(Context context){
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(context);
        final SQLiteDatabase db = this.getWritableDatabase();
        //this.context= context;
        //pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, GETAUCTIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //pDialog.dismiss();

                Log.e("Success",""+s);
                Log.e("Zitapass", "" + s);
                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonobject = new JSONObject(s);
                    int success = jsonobject.getInt("success");
                    if (success == 1) {


                        JSONArray array = jsonobject.getJSONArray("posts");

                        for (int i = 0; i < array.length(); i++) {
                            try {

                                JSONObject object = array.getJSONObject(i);

                                String post_id = object.optString("post_id");
                                String name = object.optString("name");
                                String slug = object.optString("slug");
                                String minimun_increament = object.optString("minimun_increament");
                                String deposit = object.optString("deposit");
                                String discount = object.optString("discount");
                                String end_date = object.optString("end_date");


                                ContentValues values = new ContentValues();
                                values.put("post_id", post_id);
                                values.put("name",name);
                                values.put("slug", slug);
                                values.put("minimun_increament",minimun_increament);
                                values.put("deposit", deposit);
                                values.put("discount",discount);
                                values.put("end_date", end_date);
                                db.insert("tblauctions", null, values);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }
                    }

                    else{
                        Log.e("Success", "Not success" + s);
                        db.close();
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //pDialog.dismiss();
                volleyError.printStackTrace();
                Log.e("RUEERROR",""+volleyError);
            }
        }){
            //@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> values=new HashMap();
                // values.put("username",pword);
                //values.put("password",uname);


                return values;
            }

        };

        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000,0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        //requestQueue.add(stringRequest);
    }




    public void fill_categories(Context context){
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(context);
        final SQLiteDatabase db = this.getWritableDatabase();
        //this.context= context;
        //pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, GETCATEGORIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //pDialog.dismiss();

                Log.e("Success",""+s);
                Log.e("Zitapass", "" + s);
                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonobject = new JSONObject(s);
                    int success = jsonobject.getInt("success");
                    if (success == 1) {


                        JSONArray array = jsonobject.getJSONArray("posts");

                        for (int i = 0; i < array.length(); i++) {
                            try {

                                JSONObject object = array.getJSONObject(i);

                                String post_id = object.optString("post_id");
                                String name = object.optString("name");
                                String slug = object.optString("slug");


                                ContentValues values = new ContentValues();
                                values.put("post_id", post_id);
                                values.put("name",name);
                                values.put("slug", slug);
                                db.insert("tblcategories", null, values);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }
                    }

                    else{
                        Log.e("Success", "Not success" + s);
                        db.close();
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //pDialog.dismiss();
                volleyError.printStackTrace();
                Log.e("RUEERROR",""+volleyError);
            }
        }){
            //@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> values=new HashMap();
                // values.put("username",pword);
                //values.put("password",uname);


                return values;
            }

        };

        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000,0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        //requestQueue.add(stringRequest);
    }



    public void fill_locations(Context context){
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(context);
        final SQLiteDatabase db = this.getWritableDatabase();
        //this.context= context;
        //pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, GETLOCATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //pDialog.dismiss();

                Log.e("Success",""+s);
                Log.e("Zitapass", "" + s);
                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonobject = new JSONObject(s);
                    int success = jsonobject.getInt("success");
                    if (success == 1) {


                        JSONArray array = jsonobject.getJSONArray("posts");

                        for (int i = 0; i < array.length(); i++) {
                            try {

                                JSONObject object = array.getJSONObject(i);

                                String post_id = object.optString("post_id");
                                String name = object.optString("name");
                                String slug = object.optString("slug");


                                ContentValues values = new ContentValues();
                                values.put("post_id", post_id);
                                values.put("name",name);
                                values.put("slug", slug);
                                db.insert("tbllocations", null, values);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }
                    }

                    else{
                        Log.e("Success", "Not success" + s);
                        db.close();
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //pDialog.dismiss();
                volleyError.printStackTrace();
                Log.e("RUEERROR",""+volleyError);
            }
        }){
            //@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> values=new HashMap();
                // values.put("username",pword);
                //values.put("password",uname);


                return values;
            }

        };

        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000,0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        //requestQueue.add(stringRequest);
    }






}
