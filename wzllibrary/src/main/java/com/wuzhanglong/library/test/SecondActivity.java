package com.wuzhanglong.library.test;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.wuzhanglong.library.R;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;

//import me.imid.swipebacklayout.lib.SwipeBackLayout;

/**
 * Created by Administrator on 2017/3/7.
 */

public class SecondActivity extends BaseActivity {
    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.second_activity, mBaseContentLayout);

//        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_ALL);
    }

    @Override
    public void initView() {
        EditText tv= (EditText) findViewById(R.id.et);
        tv.setText("第二个");
    }

    @Override
    public void bindViewsListener() {

    }

    @Override
    public void getData() {
        HttpClientUtil.show(mThreadUtil);
    }

    @Override
    public void hasData(BaseVO vo) {

    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    @Override
    public void finishActivity(Activity activity, int code) {

    }
}
