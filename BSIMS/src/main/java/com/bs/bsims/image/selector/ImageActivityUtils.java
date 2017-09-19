/**
 * 
 */

package com.bs.bsims.image.selector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.transition.Explode;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.bs.bsims.activity.ImagePreviewActivity;
import com.bs.bsims.adapter.UploadAdapter;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-4-11
 * @version 1.23
 */
public class ImageActivityUtils {

    public static final int REQUEST_IMAGE = 2;
    public static final int REQUEST_IMAGE_BYSDCARD = 5;

    /* 拍照选择照片统一同居类 */

    public static void setImageForActivity(Activity mContext, UploadAdapter mAdapter, int position) {
        Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, 1);
        // // 默认选择 会导致用户选错
        // if (mAdapter.mPicList != null && mAdapter.mPicList.size() > 0) {
        // intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
        // (ArrayList<String>) mAdapter.mPicList);
        // }
        switch (mAdapter.mList.size()) {
            case 0:
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 3);
                mContext.startActivityForResult(intent, REQUEST_IMAGE);
                break;
            case 1:
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 2);
                if (position == 1) {
                    mContext.startActivityForResult(intent, REQUEST_IMAGE);
                }
                else {
                    // try {
                    // mAdapter.mPicList.remove(position);
                    // mAdapter.mList.remove(position);
                    // mAdapter.notifyDataSetChanged();
                    // } catch (Exception e) {
                    // CustomToast.showShortToast(mContext, "图片删除错误");
                    // }

                    imgPreviewActivity((ArrayList<String>) mAdapter.mPicList, mContext, position);

                }
                break;
            case 2:
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                if (position == 2) {
                    mContext.startActivityForResult(intent, REQUEST_IMAGE);
                }
                else {
                    // try {
                    // mAdapter.mPicList.remove(position);
                    // mAdapter.mList.remove(position);
                    // mAdapter.notifyDataSetChanged();
                    // } catch (Exception e) {
                    // CustomToast.showShortToast(mContext, "图片删除错误");
                    // }

                    imgPreviewActivity((ArrayList<String>) mAdapter.mPicList, mContext, position);
                }
                break;

            case 3:
                // try {
                // mAdapter.mPicList.remove(position);
                // mAdapter.mList.remove(position);
                // mAdapter.notifyDataSetChanged();
                // } catch (Exception e) {
                // CustomToast.showShortToast(mContext, "图片删除错误");
                // }

                imgPreviewActivity((ArrayList<String>) mAdapter.mPicList, mContext, position);
                break;

        }
    }

    public static void setImageGetActivity(Intent intent, final UploadAdapter mAdapter) {
        Bitmap bitmap;
        if (null == intent)
            return;
        ArrayList<String> mSelectPath = intent.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
        for (String p : mSelectPath) {
            mAdapter.mPicList.add(p);
            bitmap = ImageLoader.getInstance().loadImageSync("file://" + p);
            ImageLoader.getInstance().loadImage("file://" + p, new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String arg0, View arg1) {

                }

                @Override
                public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

                }

                @Override
                public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
                    mAdapter.mList.add(bitmap);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onLoadingCancelled(String arg0, View arg1) {

                }
            });

        }

    }

    /* 回收图片内存 */

    public static void crashBoBitmap(UploadAdapter mAdapter) {
        if (mAdapter != null && mAdapter.mList != null && mAdapter.mList.size() > 0) {
            for (int i = 0; i < mAdapter.mList.size(); i++) {
                mAdapter.mList.get(i).recycle();
            }
        }
    }

    /* 点击预览效果 */
    public static void imgPreviewActivity(ArrayList<String> listArray, Activity mContext, int indexs) {
        Intent intent = new Intent();
        // intent.putStringArrayListExtra("piclist", (ArrayList<String>) strlist);
        intent.putStringArrayListExtra("piclist", listArray);
        intent.setClass(mContext, ImagePreviewActivity.class);
        intent.putExtra("imgIndex", indexs);
        intent.putExtra("isScard", "true");
        mContext.startActivityForResult(intent, REQUEST_IMAGE_BYSDCARD);
    }

    /* 点击预览效果 */
    @SuppressLint("NewApi")
    public static void imgPreviewActivity1(View view, ArrayList<String> listArray, Activity mContext, int indexs) {
        Intent intent = new Intent();
        // intent.putStringArrayListExtra("piclist", (ArrayList<String>) strlist);
        intent.putStringArrayListExtra("piclist", listArray);
        intent.setClass(mContext, ImagePreviewActivity.class);
        intent.putExtra("imgIndex", indexs);
        intent.putExtra("isScard", "true");
        ActivityOptions activityOptions = ActivityOptions.makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
        ActivityCompat.startActivityForResult(mContext, intent, REQUEST_IMAGE_BYSDCARD, activityOptions.toBundle());
        // mContext.startActivityForResult(intent, REQUEST_IMAGE_BYSDCARD);
    }

    /* 预览之后删除图片 */
    public static void imgPreviewDelteActivity(Intent intent, UploadAdapter adapter) {
        if (intent != null)
        {
            int postion = intent.getIntExtra("piclist", 0);
            try {
                adapter.mList.remove(postion);
                adapter.mPicList.remove(postion);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                adapter.mList.clear();
                adapter.mPicList.clear();
                adapter.notifyDataSetChanged();
            }

        }
    }

    /* 拍照选择照片统一同居类 */

    public static void setImageForActivity(View view, Activity mContext, UploadAdapter mAdapter, int position) {
        Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, 1);
        // // 默认选择 会导致用户选错
        // if (mAdapter.mPicList != null && mAdapter.mPicList.size() > 0) {
        // intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
        // (ArrayList<String>) mAdapter.mPicList);
        // }
        switch (mAdapter.mList.size()) {
            case 0:
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 3);
                mContext.startActivityForResult(intent, REQUEST_IMAGE);
                break;
            case 1:
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 2);
                if (position == 1) {
                    mContext.startActivityForResult(intent, REQUEST_IMAGE);
                }
                else {

                    imgPreviewActivity1(view, (ArrayList<String>) mAdapter.mPicList, mContext, position);

                }
                break;
            case 2:
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                if (position == 2) {
                    mContext.startActivityForResult(intent, REQUEST_IMAGE);
                }
                else {

                    imgPreviewActivity1(view, (ArrayList<String>) mAdapter.mPicList, mContext, position);
                }
                break;

            case 3:

                imgPreviewActivity1(view, (ArrayList<String>) mAdapter.mPicList, mContext, position);
                break;

        }
    }

    /**
     * 读取本地缓存的头像转换成流文件进行传递
     */
    @SuppressLint("NewApi")
    public static void sendByteImg(ImageLoader mImageLoader, final Context context, final Intent intent, final String url, final BSCircleImageView bsCircleImageView) {
        intent.putExtra("imageUrl", url);

        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 21) {
            context.startActivity(intent);
        } else {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, bsCircleImageView, "head_icon");
            context.startActivity(intent, options.toBundle());

        }

    }

    /* 过渡需要的动画 进入的activity的页面 */
    public static void setInWindowAnimations(Activity mContext) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 21)
            return;
        Explode explode = new Explode();
        explode.setDuration(1000);
        mContext.getWindow().setEnterTransition(explode);
    }

    /* 过渡需要的动画 进入的activity的页面 共享元素 */
    public static void setInWindowAnimationsShareElements(Activity mContext) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 21)
            return;
        Explode explode = new Explode();
        explode.setDuration(1000);
        explode.setInterpolator(new BounceInterpolator());
        mContext.getWindow().setSharedElementEnterTransition(explode);
    }

    /* 过渡需要的动画 进入的activity的页面 共享元素 */
    public static void setOutWindowAnimationsShareElements(Activity mContext) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 21)
            return;
        Explode explode = new Explode();
        explode.setDuration(1000);
        explode.setInterpolator(new BounceInterpolator());
        mContext.getWindow().setSharedElementExitTransition(explode);
    }

    /* 过渡需要的动画 退出的activity的页面 */
    public static void setOutWindowAnimations(Activity mContext) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 21)
            return;
        Explode explode = new Explode();
        explode.setDuration(1000);
        mContext.getWindow().setExitTransition(explode);
    }
}
