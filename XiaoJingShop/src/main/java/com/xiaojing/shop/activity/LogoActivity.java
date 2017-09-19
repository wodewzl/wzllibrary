package com.xiaojing.shop.activity;

import android.content.Intent;

import com.wuzhanglong.library.activity.BaseLogoActivity;
import com.wuzhanglong.library.fragment.BaseFragment;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.xiaojing.shop.R;
import com.xiaojing.shop.fragment.TabFourFragment;
import com.xiaojing.shop.fragment.TabOneFragment;
import com.xiaojing.shop.fragment.TabThreeFragment;
import com.xiaojing.shop.fragment.TabTwoFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/8.
 */

public class LogoActivity extends BaseLogoActivity {
    public List<BaseFragment> list = new ArrayList<>();

    @Override
    public void initLogo() {
        EventBus.getDefault().register(this);
        mLogoImageView.setBackgroundResource(R.drawable.logo);
//        initPermissions();
        list = new ArrayList<>();
        TabOneFragment one = new TabOneFragment();
        TabTwoFragment two = new TabTwoFragment();
        TabThreeFragment three = new TabThreeFragment();
        TabFourFragment four = new TabFourFragment();
        list.add(one);
        list.add(two);
        list.add(three);
        list.add(four);
    }


    @Subscribe
    public void onEventMainThread(EBMessageVO event) {
        if ("guide".equals(event.getMessage())) {
            Intent intent = new Intent();
            intent.putExtra("fragment_list", (Serializable) list);
            intent.setClass(this, HomeActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

}
