
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.image.selector.ImageActivityUtils;
import com.bs.bsims.utils.AsyncImageLoader;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.FileUtil;
import com.bs.bsims.view.FlowIndicator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import uk.co.senab.photoview.PhotoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 图片预览
 */
public class ImagePreviewActivity extends Activity {

    private ViewPager mViewPager;
    private ArrayList<String> mPicPathList;
    private int imgIndex;
    private Bitmap[] mDrawables;
    private boolean finish = false;
    private FlowIndicator mFlowIndicator;
    private int mCount = 0;
    private TextView mDelteInco;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        setContentView(R.layout.image_preview);
        initView();
        ImageActivityUtils.setInWindowAnimations(this);
        mImageLoader = ImageLoader.getInstance();
        // mOptions = CommonUtils.initImageLoaderOptions();
        mOptions = new DisplayImageOptions.Builder().showStubImage(0).showImageForEmptyUri(R.drawable.common_ic_image_default)
                .showImageOnFail(R.drawable.common_ic_image_default).cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public void initData() {
        // CustomDialog.showProgressDialog(this);
        mPicPathList = this.getIntent().getStringArrayListExtra("piclist");
        imgIndex = this.getIntent().getIntExtra("imgIndex", 0);
        mDrawables = new Bitmap[mPicPathList.size()];
        mViewPager.setAdapter(new SamplePagerAdapter(this));
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mViewPager.setCurrentItem(imgIndex);
        mFlowIndicator.setCount(mPicPathList.size());
        if (imgIndex != 0) {
            mFlowIndicator.setSeletion(imgIndex);
        }

        if (this.getIntent().getStringExtra("isScard") == null) {
            mDelteInco.setVisibility(View.VISIBLE);
            mDelteInco.setText("保存");
            mDelteInco.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        // 如果来自聊天图片
        if (this.getIntent().getStringExtra("isChat") != null) {
            mDelteInco.setVisibility(View.GONE);
        }
        else {
            mDelteInco.setVisibility(View.VISIBLE);
        }

    }

    class SamplePagerAdapter extends PagerAdapter {
        private Context mContext;

        public SamplePagerAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mPicPathList.size();
        }

        @SuppressLint("NewApi")
        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            final PhotoView photoView = new PhotoView(container.getContext());
            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            if (ImagePreviewActivity.this.getIntent().getStringExtra("isScard") != null) {
//                File file = new File(mPicPathList.get(position));
//                String uri = Uri.fromFile(file).toString();
                mImageLoader.displayImage("file://"+mPicPathList.get(position) , photoView, mOptions, new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {

                    }

                    @Override
                    public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

                    }

                    @Override
                    public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
                        mDrawables[position] = bitmap;
                    }

                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {

                    }
                });
            } else {
                // mImageLoader.displayImage(mPicPathList.get(position), photoView, mOptions);
                mImageLoader.displayImage(mPicPathList.get(position), photoView, mOptions, new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {

                    }

                    @Override
                    public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

                    }

                    @Override
                    public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
                        mDrawables[position] = bitmap;
                    }

                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {

                    }
                });
            }

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

    // 从网络加载图片，带接口地址的
    public void getData() {
        mDelteInco.setVisibility(View.VISIBLE);
        mDelteInco.setText("保存");
        mDelteInco.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        for (int i = 0; i < mPicPathList.size(); i++) {
            AsyncImageLoader imageLoader = new AsyncImageLoader(this);
            // 将图片缓存至外部文件中
            imageLoader.setCache2File(true); // false
            // 设置外部缓存文件夹
            imageLoader.setCachedDir(FileUtil.getSaveFilePath(this));
            // 下载图片，第二个参数是否缓存至内存中
            imageLoader.downloadImage(mPicPathList.get(i), true, new AsyncImageLoader.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap, String imageUrl) {

                    mDrawables[mCount] = bitmap;
                    mCount++;
                    if (mCount == mPicPathList.size()) {
                        mViewPager.setAdapter(new SamplePagerAdapter(ImagePreviewActivity.this));
                        mViewPager.setCurrentItem(imgIndex);
                        CustomDialog.closeProgressDialog();
                    }
                }
            });
        }

        // mImageLoader.dis

    }

    // 从sdcard内加载图片

    public void getDataBySdCard() {

        for (int i = 0; i < mPicPathList.size(); i++) {

            mDrawables[mCount] = ImageLoader.getInstance().loadImageSync("file://" + mPicPathList.get(i));
            mCount++;
            if (mCount == mPicPathList.size()) {
                mViewPager.setAdapter(new SamplePagerAdapter(ImagePreviewActivity.this));
                mViewPager.setCurrentItem(imgIndex);
                CustomDialog.closeProgressDialog();
            }
        }
        mDelteInco.setVisibility(View.VISIBLE);
    }

    public void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mFlowIndicator = (FlowIndicator) findViewById(R.id.galleryIndicator);
        mDelteInco = (TextView) findViewById(R.id.txt_comm_head_right);
        initData();
        bindListener();
    }

    public class MyOnPageChangeListener implements OnPageChangeListener {

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int arg0) {
            mFlowIndicator.setSeletion(arg0);
            imgIndex = arg0;
        }
    }

    public void bindListener() {
        findViewById(R.id.img_head_back).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ImagePreviewActivity.this.finish();
            }
        });

        mDelteInco.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (ImagePreviewActivity.this.getIntent().getStringExtra("isScard") != null) {
                    Intent intent = new Intent();
                    try {
                        mPicPathList.remove(imgIndex);
                        intent.putExtra("piclist", imgIndex);
                        setResult(RESULT_OK, intent);
                        ImagePreviewActivity.this.finish();
                    } catch (Exception e) {
                        CustomToast.showShortToast(getApplicationContext(), "删除错误");
                    }
                } else {
                    // if (mDrawables[imgIndex] == null) {
                    // return;
                    // }
                    saveImageToGallery(ImagePreviewActivity.this, mDrawables[imgIndex]);
                    CustomToast.showLongToast(ImagePreviewActivity.this, "保存成功");
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDrawables != null && mDrawables.length > 0) {
            for (int i = 0; i < mDrawables.length; i++) {
                if (mDrawables[i] != null && !mDrawables[i].isRecycled()) {
                    mDrawables[i].recycle();
                }
            }
        }
        System.gc();
    }

    public void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(FileUtil.getSaveFilePath(this), fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        // context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
        // Uri.parse("file://" + path)));
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }
}
