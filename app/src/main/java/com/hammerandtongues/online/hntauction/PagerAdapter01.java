package com.hammerandtongues.online.hntauction;

/**
 * Created by Ruvimbo on 2/2/2018.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter01 extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter01(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                //CategoriesFragment tab1 = new CategoriesFragment();
                //return tab1;
            case 1:
                /*StoresFragment tab2 = new StoresFragment();
                return tab2;*/
            case 2:
              /*  PromotionsFragment tab3 = new PromotionsFragment();
                return tab3;*/
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
