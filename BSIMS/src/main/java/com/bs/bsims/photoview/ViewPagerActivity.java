
package com.bs.bsims.photoview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.bs.bsims.utils.AsyncImageLoader;
import com.bs.bsims.utils.CustomDialog;

import java.util.ArrayList;

public class ViewPagerActivity extends Activity {

    private ViewPager mViewPager;
    private ArrayList<String> mPicPathList;
    private int imgIndex;
    private Drawable[] mDrawables;
    private boolean finish = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new HackyViewPager(this);
        setContentView(mViewPager);
        initData();
    }

    public void initData() {
        CustomDialog.showProgressDialog(this);
        mPicPathList = this.getIntent().getStringArrayListExtra("piclist");
        imgIndex = this.getIntent().getIntExtra("imgIndex", 0);
        AsyncImageLoader asyncImageLoader = new AsyncImageLoader(this);
        mDrawables = new Drawable[mPicPathList.size()];

        // for (int i = 0; i < mPicPathList.size(); i++) {
        // final int j = i;
        // asyncImageLoader.loadDrawable(mPicPathList.get(i),
        // new ImageCallback() {
        // @Override
        // public void imageLoaded(Drawable imageDrawable,
        // String imageUrl) {
        // mDrawables[j] = imageDrawable;
        // if (j == mPicPathList.size() - 1) {
        // CustomDialog.closeProgressDialog();
        // mViewPager.setAdapter(new SamplePagerAdapter());
        // }
        // }
        // });
        // }
    }

    class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mPicPathList.size();
        }

        @SuppressLint("NewApi")
        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setImageDrawable(mDrawables[position]);

            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    // @Override
    // public void baseSetContentView() {
    // mViewPager = new HackyViewPager(this);
    // mContentLayout.addView(mViewPager);
    // }

    // @Override
    // public boolean getDataResult() {
    // return true;
    // }
    //
    // @Override
    // public void updateUi() {
    //
    // }
    //
    // @Override
    // public void executeSuccess() {
    // super.isRequestFinish();
    // }
    //
    // @Override
    // public void initView() {
    // initData();
    // }
    //
    // @Override
    // public void bindViewsListener() {
    //
    // }

}
