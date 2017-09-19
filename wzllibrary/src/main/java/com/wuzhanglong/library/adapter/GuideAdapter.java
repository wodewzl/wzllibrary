
package com.wuzhanglong.library.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuideAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    public GuideAdapter(FragmentManager fm) {
        super(fm);
    }

    public GuideAdapter(FragmentManager fragmentManager, List<Fragment> arrayList) {
        super(fragmentManager);
        this.fragmentList = arrayList;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentList.get(arg0);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
