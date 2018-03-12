package com.hammerandtongues.online.hntauction;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * Created by Ruvimbo on 2/2/2018.
 */

public class Unbinddrawables {


    public   void unbindDrawables(View view)
    {
        if (view.getBackground() != null)
        {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView))
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}
