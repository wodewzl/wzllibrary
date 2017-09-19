package com.beisheng.easycar.activity;

import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.beisheng.easycar.R;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;

public class TestActivity extends BaseActivity {
    private LinearLayout mView1,mView2;
    private float mDounX,mDounY,mCurrentX,mCurrentY;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_test);

    }

    @Override
    public void initView() {
        mView1=getViewById(R.id.view_1);
        mView2=getViewById(R.id.view_2);
        mView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDounX=event.getX();
                        mDounY=event.getY();
                        break;
                    case MotionEvent.ACTION_CANCEL:

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx=event.getX()-mDounX;
//                        view.setTranslationX(dx);
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mView2
                                .getLayoutParams();
                        layoutParams.leftMargin = BaseCommonUtils.px2dip(TestActivity.this,dx);
                        layoutParams.topMargin = 0;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        mView2.setLayoutParams(layoutParams);
                        break;

                    default:
                        break;
                }

                return true;
            }
        });
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



}
