package preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ruvimbo on 9/11/2017.
 */

public class MyPrefAddress {

    private static final String ADDRESS1 = "address1";
    private static final String ADDRESS2 = "address2";
    private static final String CITY = "city";
    private static final String REGION = "region";
    private static final String SURBUB = "surbub";
    private static final String COUNTRY = "country";

    public static void saveAllAddress(Context context, String address1, String address2, String city, String region, String surbub, String country ) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ADDRESS1, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(ADDRESS2, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = context.getSharedPreferences(CITY, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences3 = context.getSharedPreferences(REGION, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences4 = context.getSharedPreferences(SURBUB, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences5 = context.getSharedPreferences(COUNTRY, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("address1", address1);
        editor.apply();


        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putString("address2", address2);
        editor1.apply();


        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        editor2.putString("city", city);
        editor2.apply();

        SharedPreferences.Editor editor3 = sharedPreferences3.edit();
        editor3.putString("region", region);
        editor3.apply();

        SharedPreferences.Editor editor4 = sharedPreferences4.edit();
        editor4.putString("surbub", surbub);
        editor4.apply();

        SharedPreferences.Editor editor5 = sharedPreferences5.edit();
        editor5.putString("country", country);
        editor5.apply();

    }

    public  static  String getAddress1(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(ADDRESS1, Context.MODE_PRIVATE);

        return  sharedPreferences.getString("address1",null);

    }

    public  static  String getAddress2(Context context) {

        SharedPreferences sharedPreferences1 = context.getSharedPreferences(ADDRESS2, Context.MODE_PRIVATE);

        return sharedPreferences1.getString("address2", null);
    }

    public  static  String getCity(Context context) {

        SharedPreferences sharedPreferences2 = context.getSharedPreferences(CITY, Context.MODE_PRIVATE);

        return sharedPreferences2.getString("city", null);
    }

    public  static  String getRegion(Context context) {

        SharedPreferences sharedPreferences3 = context.getSharedPreferences(REGION, Context.MODE_PRIVATE);

        return sharedPreferences3.getString("region", null);
    }

    public  static  String getSurbub(Context context) {

        SharedPreferences sharedPreferences4 = context.getSharedPreferences(SURBUB, Context.MODE_PRIVATE);

        return sharedPreferences4.getString("surbub", null);
    }

    public  static  String getCountry(Context context) {

        SharedPreferences sharedPreferences5 = context.getSharedPreferences(COUNTRY, Context.MODE_PRIVATE);

        return sharedPreferences5.getString("country", null);
    }



}
