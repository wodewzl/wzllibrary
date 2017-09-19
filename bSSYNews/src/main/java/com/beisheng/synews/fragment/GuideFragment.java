
package com.beisheng.synews.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.BitmapUtil;
import com.beisheng.synews.activity.LoginActivity;
import com.im.zhsy.R;

public class GuideFragment extends Fragment implements OnClickListener {
    private Activity mActivity;
    private RelativeLayout mLogoLayout1;
    private int index;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.guide_fragment, container, false);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(mActivity, LoginActivity.class);
        this.startActivity(intent);
        mActivity.finish();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLogoLayout1 = (RelativeLayout) view.findViewById(R.id.logo_layout1);
        showView(index);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void showView(int index) {
        Bitmap bitmap = null;

        switch (index) {
            case 0:
                bitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.guide_1);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.guide_2);

                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.guide_3);

                break;

            default:
                bitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.guide_1);

                break;
        }
        Bitmap newBitmap = BitmapUtil.zoomImg(bitmap, BaseCommonUtils.getScreenWidth(mActivity), BaseCommonUtils.getScreenHigh(mActivity));
        Drawable drawable = new BitmapDrawable(newBitmap);
        mLogoLayout1.setBackgroundDrawable(drawable);
    }

}
