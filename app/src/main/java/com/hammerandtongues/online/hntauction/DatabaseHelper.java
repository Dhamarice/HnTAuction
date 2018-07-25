package com.hammerandtongues.online.hntauction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ProgressBar;

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

//import static com.hammerandtongues.online.hntonline.JSONParser.json;

/**
 *  Created by Ruvimbo on 25/1/2018..
 */

public class DatabaseHelper extends SQLiteOpenHelper {



    JSONObject json=null;

    //private static final String GETCATEGORIES_URL = "https://devshop.hammerandtongues.com/webservice/getcategories.php";
    private static final String GETAUCTIONS_URL = "http://devauction.hammerandtongues.com/webservice/getauctionnames.php";
    private static final String GETAUCTIONSCONTENT_URL = "http://devauction.hammerandtongues.com/webservice/getauctioncontent.php";
    private static final String GETCATEGORIES_URL = "http://devauction.hammerandtongues.com/webservice/getauctioncategories.php";
    private static final String GETLOCATIONS_URL = "http://devauction.hammerandtongues.com/webservice/getauctionlocations.php";
    private static final String BIDS_URL = "http://devauction.hammerandtongues.com/webservice/getbids.php";

    /*

    private static final String GETAUCTIONS_URL = "http://10.0.2.2/webservice/getauctionnames.php";
    private static final String GETAUCTIONSCONTENT_URL = "http://10.0.2.2/webservice/getauctioncontent.php";
    private static final String GETCATEGORIES_URL = "http://10.0.2.2/webservice/getauctioncategories.php";
    private static final String GETLOCATIONS_URL = "http://10.0.2.2/webservice/getauctionlocations.php";
    private static final String BIDS_URL = "http://10.0.2.2/webservice/getbids.php";
     */
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
    private SQLiteDatabase myDataBase;




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
                "\t`win`\tVARCHAR ( 500 , 0 ),\n" +
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
                "\t`uid`\tVARCHAR ( 500 , 0 ),\n" +
                "\tPRIMARY KEY(`id`)\n" +
                ");";
        db.execSQL(CREATE_TABLE_BIDS);


        String CREATE_TABLE_WATCHLIST = "CREATE TABLE `tbl_favorites` (\n" +
                "\t`id`\tINTEGER,\n" +
                "\t`type_id`\tINTEGER,\n" +
                "\t`date_created`\tDATETIME DEFAULT (CURRENT_TIMESTAMP),\n" +
                "\t`type`\tVARCHAR ( 500 , 0 ),\n" +
                "\t`value_type`\tVARCHAR ( 500 , 0 ),\n" +
                "\tPRIMARY KEY(`id`)\n" +
                ");";
        db.execSQL(CREATE_TABLE_WATCHLIST);
}


    //upgrading the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE_PRODUCTS = "DROP TABLE IF EXISTS tblauctions";
        db.execSQL(DROP_TABLE_PRODUCTS);



        onCreate(db);
    }



    public Cursor getProduct (int ProdID, String UserId) {
        //String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " =  \"" + statusid + "\"";
        String mod_query = "select distinct c.*, a.name, a.minimun_increament, l.name, a.end_date, (select  MAX(bid_amount) from tblbids where product = " + ProdID + " and  uid = " + UserId + ") as mybid,   (select  MAX(bid_amount) from tblbids where product = " + ProdID  + ") as highbid from tblauctioncontent c inner join tblauctions a on c.auction = a.post_id inner join tbllocations l on c.location = l.post_id where c.post_id = " + ProdID +" LIMIT  1";
        SQLiteDatabase db = this.getWritableDatabase();



        Log.e("getProduct", "getProduct: " + mod_query );
        Cursor cursor = db.rawQuery(mod_query, null);



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
        String mod_query = "select b.id, b.post_id, b.date_Created, b.product, b.winner, b.date_won, b.uid, MAX(b.bid_amount) AS bid_amount, (select  MAX(bid_amount) from tblbids where product = b.product) as highbid,  p.name, p.img_url, p.post_id from tblbids b inner join tblauctioncontent p on b.product = p.post_id where b.winner = 0 and b.uid = " + UserId ;

        Log.e("getmybids", "getmybids: " + mod_query );

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






    public Cursor getpastbids(String UserId) {
        //String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " =  \"" + statusid + "\"";
        String mod_query = "select b.id, b.post_id, b.date_Created, b.product, b.winner, b.date_won, b.bid_amount AS bid_amount, p.name, p.img_url, p.post_id from tblbids b inner join tblauctioncontent p on b.product = p.post_id where b.winner = '1' and b.uid = " + UserId ;

        Log.e("getpastbids", "getpastbids: " + mod_query );
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
        String mod_query = "select DISTINCT a.*, ac.starting_date, ac.closing_date from tblauctions a inner join tblauctioncontent ac on a.post_id = ac.auction GROUP BY a.id;";

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


    public Cursor getallBids( String proid) {
        //String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " =  \"" + sta'tusid + "\"";
        String mod_query = "select DISTINCT b.*, p.name, p.img_url, p.post_id , (select  MAX(bid_amount) from tblbids where product = " + proid  + ") as highbid from tblbids b inner join tblauctioncontent p on b.product = p.post_id where p.post_id = " + proid +" GROUP BY p.id";

        Log.e("getallBids", "getallBids: " + mod_query );
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



    public Cursor getallCategories() {
        //String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " =  \"" + sta'tusid + "\"";
        String mod_query = "select DISTINCT a.*, ac.starting_date, ac.closing_date from tblcategories a inner join tblauctioncontent ac on a.post_id = ac.category GROUP BY a.id;";

        Log.e("getallCategories", "getallCategories: " + mod_query );
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



    public Cursor getallLocations() {
        //String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " =  \"" + sta'tusid + "\"";
        String mod_query = "select DISTINCT a.*, ac.starting_date, ac.closing_date from tbllocations a inner join tblauctioncontent ac on a.post_id = ac.location GROUP BY a.id;";

        Log.e("getallLocations", "getallLocations: " + mod_query );
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
        String mod_query = "select distinct ac.*, a.end_date from tblauctioncontent ac inner join tblauctions a on ac.auction = a.post_id  where ac.auction = " + AuctionID +" and  ac.id > " + lastid + " GROUP BY ac.id ORDER BY id asc LIMIT  " + limit + " Offset " + offset ;


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


    public Cursor getCategoryContets(int CategoryID, int limit, int offset, String lastid) {
        //String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " =  \"" + sta'tusid + "\"";
        String mod_query = "select distinct ac.*, a.end_date from tblauctioncontent ac inner join tblauctions a on ac.auction = a.post_id  where ac.category = " + CategoryID +" and  ac.id > " + lastid + " GROUP BY ac.id ORDER BY id asc LIMIT  " + limit + " Offset " + offset ;


        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(mod_query, null);

        Log.e("getCategoryContets: ", "Query run" + mod_query );

        if (cursor.moveToFirst()) {
//            cursor.moveToFirst();

            return cursor;
        } else {
            db.close();
            return null;
        }

    }


    public Cursor getLocationContets(int LocationID, int limit, int offset, String lastid) {
        //String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUS + " =  \"" + sta'tusid + "\"";
        String mod_query = "select distinct ac.*, a.end_date from tblauctioncontent ac inner join tblauctions a on ac.auction = a.post_id where ac.location = " + LocationID +" and  ac.id > " + lastid + " GROUP BY ac.id ORDER BY id asc LIMIT  " + limit + " Offset " + offset ;


        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(mod_query, null);

        Log.e("getLocationContets: ", "Query run" + mod_query );

        if (cursor.moveToFirst()) {
//            cursor.moveToFirst();

            return cursor;
        } else {
            db.close();
            return null;
        }

    }



    public Cursor searchProduct( String searchString) {
        String mod_query =  "select distinct ac.*, a.end_date from tblauctioncontent ac inner join tblauctions a on ac.auction = a.post_id where ac.name LIKE '%" + searchString + "%'";



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



    public void clearAuctions() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tblauctions", "1", null);
    }
    public void clearBids() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tblbids", "1", null);
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
        String INSERT_TABLE_BIDS = "INSERT INTO tblbids (post_id ,bid_amount, product, uid) VALUES ('" + user_id + "', '" + bid_amount + "', '" + product_id + "', '" + user_id + "' )";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(INSERT_TABLE_BIDS);

        Log.e("Fill Bids query", INSERT_TABLE_BIDS);
    }

    public void update_bids( String user_id, String bid_amount, String product_id){
        String UPDATE_TABLE_BIDS = "UPDATE tblbids SET  bid_amount = " + bid_amount + " where uid = " + user_id + " and  product = " + product_id ;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(UPDATE_TABLE_BIDS);

        Log.e("Fill Bids query", UPDATE_TABLE_BIDS);
    }


    public void update_won_prdct( String win, String product_id){
        String UPDATE_TABLE_PRODUCT = "UPDATE tblauctioncontent SET  win = " + win + " where post_id = " + product_id ;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(UPDATE_TABLE_PRODUCT);

        Log.e("Fill Bids query", UPDATE_TABLE_PRODUCT);
    }

    public void update_curr_bi( String bid, String product_id){
        String UPDATE_TABLE_PRODUCT = "UPDATE tblauctioncontent SET  current_bid = " + bid + " where post_id = " + product_id ;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(UPDATE_TABLE_PRODUCT);

        Log.e("Fill Bids query", UPDATE_TABLE_PRODUCT);
    }


    public void fillfav(int ProductID){
        String INSERT_TABLE_FAV = "INSERT INTO tbl_favorites (type_id , type, value_type) VALUES (" + ProductID + ", 'Product', 'Big')";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(INSERT_TABLE_FAV);
    }


    public Cursor getProductFav( String lastid) {
        String mod_query = "select DISTINCT c.* from  tblauctioncontent c  inner join tbl_favorites f where f.type_id=c.post_id and f.id  ORDER BY f.id asc";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("GETSTOREFAV", mod_query);
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void fill_bids(Context context){
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(context);
        final SQLiteDatabase db = this.getWritableDatabase();
        //this.context= context;
        //pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BIDS_URL, new Response.Listener<String>() {
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
                                String date_made = object.optString("date_made");
                                String bid_amount = object.optString("bid_amount");
                                String pid = object.optString("pid");
                                String winner = object.optString("winner");
                                String date_choosen = object.optString("date_choosen");
                                String uid = object.optString("uid");


                                ContentValues values = new ContentValues();
                                values.put("post_id", post_id);
                                values.put("date_made",date_made);
                                values.put("bid_amount", bid_amount);
                                values.put("product", pid);
                                values.put("date_won",date_choosen);
                                values.put("winner", winner);
                                values.put("uid", uid);
                                db.insert("tblbids", null, values);



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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }






}
