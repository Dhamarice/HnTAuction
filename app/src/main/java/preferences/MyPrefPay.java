package preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ruvimbo on 16/11/2017.
 */

public class MyPrefPay {


    private static final String DlvryChrg = "dlvrychrg";
    private static final String OrderTotal = "ordertotal";
    private static final String Discount = "discount";

    //static   SharedPreferences shared;
    //static String DlvryChrg = (shared.getString("DlvryChrg", ""));
    //static   String ordertotal = (shared.getString("total", ""));
    //static  String discount = (shared.getString("discount", ""));


    public static void saveAllAddress(Context context, String dlvrychrg, String ordertotal, String discount ) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DlvryChrg, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(OrderTotal, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = context.getSharedPreferences(Discount, Context.MODE_PRIVATE);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dlvrychrg", dlvrychrg);
        editor.apply();


        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putString("ordertotal", ordertotal);
        editor1.apply();


        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        editor2.putString("discount", discount);
        editor2.apply();



    }

    public  static  String getDlvryChrg(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(DlvryChrg, Context.MODE_PRIVATE);

        return  sharedPreferences.getString("dlvrychrg",null);

    }

    public  static  String getOrderTotal(Context context) {

        SharedPreferences sharedPreferences1 = context.getSharedPreferences(OrderTotal, Context.MODE_PRIVATE);

        return sharedPreferences1.getString("ordertotal", null);
    }

    public  static  String getDiscount(Context context) {

        SharedPreferences sharedPreferences2 = context.getSharedPreferences(Discount, Context.MODE_PRIVATE);

        return sharedPreferences2.getString("discount", null);
    }

    public   String getDlvryChrg(){

        return DlvryChrg;

    }

}
