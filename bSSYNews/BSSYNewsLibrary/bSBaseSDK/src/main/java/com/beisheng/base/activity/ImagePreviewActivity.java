
package com.beisheng.base.activity;

import android.annotation.SuppressLint;
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
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beisheng.base.R;
import com.beisheng.base.imgselector.ImageActivityUtils;
import com.beisheng.base.photoview.PhotoView;
import com.beisheng.base.utils.BitmapUtil;
import com.beisheng.base.utils.FileUtil;
import com.beisheng.base.view.BSFlowIndicator;
import com.beisheng.base.view.BSPopWindowsBottom;
import com.beisheng.base.view.BSPopWindowsBottom.PopCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 图片预览
 */

public class ImagePreviewActivity extends BaseActivity {

    private ViewPager mViewPager;
    private ArrayList<String> mPicPathList;
    private int imgIndex;
    private Bitmap[] mDrawables;
    private boolean finish = false;
    private BSFlowIndicator mFlowIndicator;
    private int mCount = 0;
    private TextView mDelteInco;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private BSPopWindowsBottom mPop;
    private RelativeLayout mHeadLayout;
    private String[] array = {
            "保存图片", "取消"
    };
    private String mCurrentUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void baseSetContentView() {
        mBaseHeadLayout.setVisibility(View.GONE);
        View.inflate(this, R.layout.image_preview, mBaseContentLayout);
        initView();

    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void bindViewsListener() {

    }

    public void initView() {
        mHeadLayout = (RelativeLayout) findViewById(R.id.head_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mFlowIndicator = (BSFlowIndicator) findViewById(R.id.galleryIndicator);
        mDelteInco = (TextView) findViewById(R.id.txt_comm_head_right);
        initData();
        bindListener();
        initPop();

        ImageActivityUtils.setInWindowAnimations(this);
        mImageLoader = ImageLoader.getInstance();
        mOptions = new DisplayImageOptions.Builder()
                .showStubImage(0)
                .showImageForEmptyUri(R.drawable.base_article_bigimage)
                .showImageOnFail(R.drawable.base_article_bigimage)
                .cacheInMemory().cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public void initData() {
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
            mHeadLayout.setVisibility(View.GONE);
            mDelteInco.setText("保存");
            mDelteInco.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            mHeadLayout.setVisibility(View.VISIBLE);
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
                File file = new File(mPicPathList.get(position));
                String uri = Uri.fromFile(file).toString();
                mImageLoader.displayImage(uri, photoView, mOptions, new ImageLoadingListener() {

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
                photoView.setOnLongClickListener(new OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View arg0) {
                        mCurrentUrl = mPicPathList.get(position);
                        mPop.showPopupWindow(photoView);
                        return false;
                    }
                });

                photoView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                    }
                });

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
                        // CustomToast.showShortToast(getApplicationContext(), "删除错误");
                        Toast.makeText(ImagePreviewActivity.this, "删除错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // if (mDrawables[imgIndex] == null) {
                    // return;
                    // }
                    saveImageToGallery(ImagePreviewActivity.this, mDrawables[imgIndex]);
                    // Toast.showLongToast(ImagePreviewActivity.this, "保存成功");
                    // Toast.makeText(context, text, duration);
                    Toast.makeText(ImagePreviewActivity.this, "删除错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

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
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

    public void initPop() {
        PopCallback callback = new PopCallback() {

            @Override
            public void callback(String str, int position) {
                switch (position) {
                    case 0:
                        Bitmap bitmap = mImageLoader.loadImageSync(mCurrentUrl);
                        if (bitmap != null) {
                            BitmapUtil.saveImageToGallery(ImagePreviewActivity.this, bitmap);
                            showCustomToast("图片保存成功");
                        }
                        break;
                    case 1:
                        mPop.dismiss();
                        break;

                    default:
                        break;
                }
            }
        };
        mPop = new BSPopWindowsBottom(this, array, callback);
    }

}
