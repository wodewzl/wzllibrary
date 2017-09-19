
package com.bs.bsims.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.UploadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.image.selector.ImageActivityUtils;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.view.BSProgressDialog;
import com.bs.bsims.view.BSSwitchView;
import com.bs.bsims.view.BSSwitchView.OnChangedListener;
import com.bs.bsims.view.BSUPloadPopWindows;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CreativeIdeaNewActivity extends Activity implements OnItemClickListener, OnClickListener, OnChangedListener {
    private static final int TAKE_PICTURE = 0x000001;
    private static final int RESULT_LOAD_IMAGE = 0x000002;
    private GridView mGrideviewUpload;
    private UploadAdapter mAdapter;
    private TextView mTitle, mIsOpenTitle, mIsAnonymousTitle, mOKTv;
    private String mType = "";
    private BSUPloadPopWindows mPop;
    private LinearLayout mParentView;
    private EditText mTitleContentEt, mContentEt;
    private BSSwitchView mIsOpenSw, mIsAnonymousSw;
    private String mStatus, mIsOpen;

    private List<String> mPicturePathList;
    private BSProgressDialog mDialog;
    private TextView mBackImage;
    private boolean mCommitFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.creative_idea_new);

        initView();
        bindViewsListeners();
    }

    public void initView() {
        mType = this.getIntent().getStringExtra("type");
        mTitle = (TextView) findViewById(R.id.txt_comm_head_activityName);
        mOKTv = (TextView) findViewById(R.id.txt_comm_head_right);
        mOKTv.setVisibility(View.VISIBLE);
        mOKTv.setText(R.string.ok);
        mBackImage = (TextView) findViewById(R.id.img_head_back);
        mIsOpenSw = (BSSwitchView) findViewById(R.id.is_open_status);
        mIsOpenTitle = (TextView) findViewById(R.id.is_open_title);

        mIsAnonymousSw = (BSSwitchView) findViewById(R.id.is_anonymous_status);
        mIsAnonymousTitle = (TextView) findViewById(R.id.is_anonymous_title);

        mTitleContentEt = (EditText) findViewById(R.id.title_content_tv);
        mContentEt = (EditText) findViewById(R.id.content_et);

        mTitle.setText(R.string.new_creative);
        mContentEt.setHint("请输入意见内容");

        mIsOpen = "1";
        mIsOpenSw.setCheckState(true);
        mStatus = "0";
        mIsAnonymousSw.setCheckState(false);

        // 上传图片使用
        mGrideviewUpload = (GridView) findViewById(R.id.grideview_upload);
        mAdapter = new UploadAdapter(this);
        mGrideviewUpload.setAdapter(mAdapter);
        mParentView = (LinearLayout) getLayoutInflater().inflate(R.layout.creative_idea_new, null);
        mPicturePathList = new ArrayList<String>();

        mDialog = new BSProgressDialog(this);
        try {
            // 透明状态栏 5.0以后才可以显示
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public boolean upload() {
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();

        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("title", mTitleContentEt.getText().toString());
            params.put("content", mContentEt.getText().toString());
            params.put("status", mStatus);
            params.put("isopen", mIsOpen);
            for (int i = 0; i < mAdapter.mPicList.size(); i++) {
                File file = CommonUtils.bitmapToString(this, mAdapter.mPicList.get(i));
                params.put("pictures" + i, file);
            }

            // for (int i = 0; i < mAdapter.mPicList.size(); i++) {
            // File file = new File(mAdapter.mPicList.get(i));
            // params.put("pictures" + i, file);
            // }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // params.put("name", "woshishishi");// 传输的字符数据
        String url = BSApplication.getInstance().getHttpTitle() + Constant.CREATIVE_IDEA_ADD;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mCommitFlag = true;
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // mDialog.dismissDialog();
                mCommitFlag = true;
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String code = (String) jsonObject.get("code");
                    String str = (String) jsonObject.get("retinfo");
                    if (Constant.RESULT_CODE.equals("200")) {
                        CustomToast.showShortToast(CreativeIdeaNewActivity.this, str);
                        CommonUtils.sendBroadcast(CreativeIdeaNewActivity.this, Constant.HOME_MSG);
                        CreativeIdeaNewActivity.this.finish();
                    } else {
                        CustomToast.showShortToast(CreativeIdeaNewActivity.this, "提交失败");
                    }
                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        return true;
    }

    public void bindViewsListeners() {
        mGrideviewUpload.setOnItemClickListener(this);
        mOKTv.setOnClickListener(this);
        mBackImage.setOnClickListener(this);
        mIsOpenSw.SetOnChangedListener(this);
        mIsAnonymousSw.SetOnChangedListener(this);
    }

    // 上传图片部分
    @Override
    public void onItemClick(AdapterView<?> parent, View arg0, int arg1, long arg2) {
        // if (arg2 == mAdapter.mList.size()) {
        // mPop = new BSUPloadPopWindows(this, mParentView, null, null, 0);
        //
        // } else {
        // mPop = new BSUPloadPopWindows(this, mParentView, mAdapter, mAdapter.mList.get((int)
        // arg2), (int) arg2);
        // }
        ImageActivityUtils.setImageForActivity(arg0, CreativeIdeaNewActivity.this, mAdapter, (int) arg2);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        // case TAKE_PICTURE:
        // if (mAdapter.mList.size() < 3 && resultCode == RESULT_OK) {
        // if (data == null) {
        // File file = new File(mPop.getFilePath());
        // mPicturePathList.add(mPop.getFilePath());
        // mAdapter.mPicList.add(mPop.getFilePath());
        // // mAdapter.mList.add(CommonUtils.getBitmapFromFile(file, 70, 70));
        // Bitmap bitmap = BitmapFactory.decodeFile(mPop.getFilePath(),
        // CommonUtils.getBitmapOption(2)); // 将图
        // mAdapter.mList.add(bitmap);
        // mAdapter.notifyDataSetChanged();
        // }
        // }
        // break;
        // case RESULT_LOAD_IMAGE:
        //
        // if (resultCode == RESULT_OK && null != data) {
        // Uri selectedImage = data.getData();
        // String[] filePathColumn = {
        // MediaStore.Images.Media.DATA
        // };
        //
        // String picturePath;
        // Cursor cursor = getContentResolver().query(selectedImage,
        // filePathColumn, null, null, null);
        // if (cursor == null) {
        // picturePath = selectedImage.getPath();
        // } else {
        // cursor.moveToFirst();
        // int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        // picturePath = cursor.getString(columnIndex);
        // cursor.close();
        //
        // }
        //
        // mPicturePathList.add(picturePath);
        // mAdapter.mPicList.add(picturePath);
        // Bitmap bitmap = BitmapFactory.decodeFile(picturePath, CommonUtils.getBitmapOption(2)); //
        // 将图
        // mAdapter.mList.add(bitmap);
        // mAdapter.notifyDataSetChanged();
        // }
        // break;
        /* 图片预览之后返回删除图片了 piclist */

            case ImageActivityUtils.REQUEST_IMAGE_BYSDCARD:
                if (resultCode == RESULT_OK) {
                    ImageActivityUtils.imgPreviewDelteActivity(data, mAdapter);
                }

                break;

            case ImageActivityUtils.REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    ImageActivityUtils.setImageGetActivity(data, mAdapter);
                }

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_comm_head_right:
                if (mTitleContentEt.getText().length() == 0) {
                    CustomToast.showLongToast(this, "标题不能为空");
                    return;
                }
                if (mContentEt.getText().length() == 0) {
                    CustomToast.showLongToast(this, "内容不能为空");
                    return;
                }

                if (mCommitFlag) {
                    mCommitFlag = false;
                    upload();
                }

                break;
            case R.id.img_head_back:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void OnChanged(boolean checkState, View v) {
        switch (v.getId()) {
            case R.id.is_open_status:
                if (mIsOpenSw.getCheckState()) {
                    mIsOpen = "1";
                } else {
                    mIsOpen = "0";
                }
                break;

            case R.id.is_anonymous_status:
                if (mIsAnonymousSw.getCheckState()) {
                    mStatus = "1";
                } else {
                    mStatus = "0";
                }
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        ImageActivityUtils.crashBoBitmap(mAdapter);
    }
}
