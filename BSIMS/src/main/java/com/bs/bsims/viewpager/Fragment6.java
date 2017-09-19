
package com.bs.bsims.viewpager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bs.bsims.R;
import com.bs.bsims.activity.LoginActivity;

public class Fragment6 extends Fragment implements OnClickListener {
    private Activity mActivity;
    private RelativeLayout mLogoLayout6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_6, container, false);
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
        view.findViewById(R.id.login_layout).setOnClickListener(this);

        mLogoLayout6 = (RelativeLayout) view.findViewById(R.id.logo_layout6);

        // Bitmap bitmap = BitmapFactory.decodeResource(mActivity.getResources(),
        // R.drawable.guide_6);
        // Bitmap newBitmap = CommonUtils.zoomImg(bitmap, CommonUtils.getScreenWidth(mActivity),
        // CommonUtils.getScreenHigh(mActivity));
        // Drawable drawable = new BitmapDrawable(newBitmap);
        // mLogoLayout6.setBackgroundDrawable(drawable);
    }

}
