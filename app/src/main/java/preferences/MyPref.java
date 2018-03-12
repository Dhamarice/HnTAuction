package preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ruvimbo on 5/11/2017.
 */

public class MyPref {

    private static final String PHONE = "phone";

    public static void savePhoneNumber(Context context, String phone) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PHONE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", phone);

        editor.apply();

    }

    public  static  String getPhone(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(PHONE, Context.MODE_PRIVATE);

        return  sharedPreferences.getString("phone",null);
    }
}
