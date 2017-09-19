
package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.activity.PhotoSelectorActivity;
import com.beisheng.base.imgselector.ImageActivityUtils;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.BitmapUtil;
import com.beisheng.base.utils.FileUtil;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class LiveAddThemeActivity extends BaseActivity implements OnClickListener {
    private boolean mCommitFlag = true;

    private EditText mTitleEt, mDesTv;
    private Button mAddBt;
    private ImageView mPhotoImg;
    private RelativeLayout mPhotoLayout;
    private static final int RESULT_CUT = 0x000003;// 裁剪后返回码
    private File mFile;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.live_add_theme_activity, mBaseContentLayout);

    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {
        mBaseTitleTv.setText("添加直播主题");
        mTitleEt = (EditText) findViewById(R.id.live_title);
        mDesTv = (EditText) findViewById(R.id.desc_tv);
        mAddBt = (Button) findViewById(R.id.add_bt);
        mPhotoImg = (ImageView) findViewById(R.id.photo_img);
        mPhotoLayout = (RelativeLayout) findViewById(R.id.photo_layout);
        mPhotoLayout.setBackground(BaseCommonUtils.setBackgroundShap(this, 0, R.color.devider_bg, R.color.C1, 1));
        mAddBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.sy_title_color, R.color.sy_title_color));
        mTitleEt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.devider_bg, R.color.C1));
        mDesTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.devider_bg, R.color.C1));
    }

    @Override
    public void bindViewsListener() {
        mAddBt.setOnClickListener(this);
        mPhotoLayout.setOnClickListener(this);
    }

    public void commit() {
        mCommitFlag = false;
        showProgressDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("title", mTitleEt.getText().toString());
            params.put("uid", AppApplication.getInstance().getUid());
            params.put("sessionid", AppApplication.getInstance().getSessionid());
            params.put("descriptions", mDesTv.getText().toString());

            params.put("img", BitmapUtil.bitmapToFile(mFile.getPath()));
            // params.put("img", mFile);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = Constant.DOMAIN_NAME + Constant.LIVE_ADD_THREME_URL;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mCommitFlag = true;
                dismissProgressDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                String result = new String(arg2);
                dismissProgressDialog();
                mCommitFlag = true;
                try {
                    JSONObject jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_SUCCESS_CODE.equals(code)) {
                        showCustomToast(str);
                        mBaseContentLayout.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                LiveAddThemeActivity.this.finish();
                            }
                        }, 1000);

                    } else {
                        showCustomToast(str);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo_layout:
                ImageActivityUtils.setImageForActivity(this);
                break;

            case R.id.add_bt:
                if (mTitleEt.getText().toString().trim().length() == 0) {
                    showCustomToast("主题必须填写");
                    return;
                }

                if (mDesTv.getText().toString().trim().length() == 0) {
                    showCustomToast("直播描述必须填写");
                    return;
                }

                if (mFile == null || !mFile.exists()) {
                    showCustomToast("图片必须上传");
                    return;
                }
                if (mCommitFlag)
                    commit();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent == null)
            return;
        switch (requestCode) {
            case ImageActivityUtils.REQUEST_IMAGE:
                final ArrayList<String> selectPath = intent.getStringArrayListExtra(PhotoSelectorActivity.EXTRA_RESULT);
                File file = new File(selectPath.get(0));
                startPhotoZoom(file);
                break;
            case RESULT_CUT:
                mFile = new File(FileUtil.getSaveFilePath(this) + "temp.jpg");
                Bitmap b = mImageLoader.loadImageSync(Uri.fromFile(mFile).toString());
                mPhotoImg.setImageBitmap(BitmapUtil.zoomImg(b, mPhotoImg.getWidth()));
                break;
            default:
                break;
        }
    }

    public void startPhotoZoom(File file) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 16);
        intent.putExtra("aspectY", 9);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 450);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("output", Uri.fromFile(new File(FileUtil.getSaveFilePath(this) + "temp.jpg")));
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        intent.putExtra("scaleUpIfNeeded", true);// 黑边
        startActivityForResult(intent, 3);
    }
}
