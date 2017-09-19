
package com.bs.bsims.viewpager;

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

import com.bs.bsims.R;
import com.bs.bsims.activity.LoginActivity;
import com.bs.bsims.utils.CommonUtils;

public class Fragment1 extends Fragment implements OnClickListener {
    private Activity mActivity;
    private RelativeLayout mLogoLayout1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_1, container, false);
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
        // intent.setClass(this, MainActivity.class);
        this.startActivity(intent);
        mActivity.finish();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLogoLayout1 = (RelativeLayout) view.findViewById(R.id.logo_layout1);

        Bitmap bitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.guide_1);
        Bitmap newBitmap = CommonUtils.zoomImg(bitmap, CommonUtils.getScreenWidth(mActivity), CommonUtils.getScreenHigh(mActivity));
        Drawable drawable = new BitmapDrawable(newBitmap);
        mLogoLayout1.setBackgroundDrawable(drawable);
    }
}
