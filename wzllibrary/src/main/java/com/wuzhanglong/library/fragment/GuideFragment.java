
package com.wuzhanglong.library.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wuzhanglong.library.R;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.BitmapUtil;


public class GuideFragment extends Fragment {
    private Activity mActivity;
    private RelativeLayout mLogoLayout1;

    private int index;
    private int drawableId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
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

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public void showView(int index) {
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeResource(mActivity.getResources(), drawableId);

//        switch (index) {
//            case 0:
//                bitmap = BitmapFactory.decodeResource(mActivity.getResources(), drawableIds[index]);
//                break;
//            case 1:
//                bitmap = BitmapFactory.decodeResource(mActivity.getResources(), drawableIds[index]);
//
//                break;
//            case 2:
//                bitmap = BitmapFactory.decodeResource(mActivity.getResources(), drawableIds[index]);
//
//                break;
//
//            default:
//                bitmap = BitmapFactory.decodeResource(mActivity.getResources(), drawableIds[index]);
//                break;
//        }

        Bitmap newBitmap = BitmapUtil.zoomImg(bitmap, BaseCommonUtils.getScreenWidth(mActivity),
                BaseCommonUtils.getScreenHigh(mActivity));
        Drawable drawable = new BitmapDrawable(newBitmap);
        mLogoLayout1.setBackgroundDrawable(drawable);
    }

}
