
package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.adapter.UploadAdapter;
import com.beisheng.base.imgselector.ImageActivityUtils;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.BitmapUtil;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.utils.PointsAddUtil;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;

@SuppressLint("NewApi")
public class CommunityAddActivity extends BaseActivity implements OnItemClickListener, OnClickListener {
    private GridView mGrideviewUpload;
    private UploadAdapter mAdapter;
    private EditText mContentEt, mTitleEt;
    private boolean mCommitFlag = true;
    private String mLid;
    private VideoView videoView1;// 视频播放控件
    private String mFile;
    private Button mAddBt;
    private String mFid = "88";//
    private String mTid;
    private LinearLayout mTitleLayout;
    private String mType;// 1为发帖，2为回贴
    private String mTitle;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.community_add_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void initView() {
        mTitleLayout = (LinearLayout) findViewById(R.id.title_layout);
        mContentEt = (EditText) findViewById(R.id.content_et);
        mTitleEt = (EditText) findViewById(R.id.title_et);
        mGrideviewUpload = (GridView) findViewById(R.id.grideview_upload);
        mAdapter = new UploadAdapter(this);
        mGrideviewUpload.setAdapter(mAdapter);
        mAddBt = (Button) findViewById(R.id.add_bt);
        mAddBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.sy_title_color, R.color.sy_title_color));
        initData();
    }

    @Override
    public void bindViewsListener() {
        mGrideviewUpload.setOnItemClickListener(this);
        mAddBt.setOnClickListener(this);
    }

    public void initData() {
        Intent intent = this.getIntent();
        mTid = intent.getStringExtra("tid");
        mType = intent.getStringExtra("type");
        mTitle = intent.getStringExtra("title");
        mFid = intent.getStringExtra("fid");
        if ("2".equals(mType)) {
            mBaseTitleTv.setText("回帖");
            mAddBt.setText("回 帖");
            mTitleLayout.setVisibility(View.GONE);
        } else {
            mBaseTitleTv.setText("发帖");
            mAddBt.setText("发 帖");
            mTitleLayout.setVisibility(View.VISIBLE);
        }
    }

    public void commit() {
        mCommitFlag = false;
        showProgressDialog();
        RequestParams params = new RequestParams();
        try {
            if (AppApplication.getInstance().getUserInfoVO() != null) {
                params.put("authorid", AppApplication.getInstance().getUserInfoVO().getTuid());
                params.put("author", AppApplication.getInstance().getUserInfoVO().getUsername());
            }
            params.put("sid", AppApplication.getInstance().getSessionid());
            params.put("message", mContentEt.getText().toString());
            params.put("subject", mTitleEt.getText().toString());
            for (int i = 0; i < mAdapter.mPicList.size(); i++) {
                // File file = BaseCommonUtils.bitmapCompress(this, mAdapter.mPicList.get(i));
                // File file = new File(mAdapter.mPicList.get(i));
                File file = BitmapUtil.bitmapToFile(mAdapter.mPicList.get(i));
                params.put("img" + i, file);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String img = (mAdapter.mPicList.size() > 0) ? "1" : "0";
        String url = "";
        if ("1".equals(mType)) {
            url = Constant.COMMUNITY_ADD_URL + "&fid=" + mFid + "&img=" + img;
        } else {
            url = Constant.COMMUNITY_COMMENT_URL + "&fid=" + mFid + "&img=" + img + "&tid=" + mTid;
        }

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
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_SUCCESS_CODE.equals(code)) {
                        if (mTitle == null) {
                            mTitle = mTitleEt.getText().toString();
                        }
                        if ("1".equals(mType)) {

                            showCustomToast("发帖成功");
                            PointsAddUtil.commitdPoints("6", "0", mTitle);
                        } else {
                            showCustomToast("回帖成功");
                            PointsAddUtil.commitdPoints("7", mTid, mTitle);
                        }
                        mBaseContentLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CommunityAddActivity.this.finish();
                            }
                        }, 1000);

                        //
                    } else {
                        if ("1".equals(mType))
                            showCustomToast("发帖失败");
                        else
                            showCustomToast("回帖失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        ImageActivityUtils.setImageForActivity(this, mAdapter, (int) arg2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case ImageActivityUtils.REQUEST_IMAGE_BYSDCARD:
                if (resultCode == RESULT_OK) {
                    ImageActivityUtils.imgPreviewDelteActivity(intent, mAdapter);
                }
                break;
            case ImageActivityUtils.REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    ImageActivityUtils.setImageGetActivity(intent, mAdapter);
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageActivityUtils.crashBoBitmap(mAdapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_bt:
                if ("1".equals(mType) && mTitleEt.getText().toString().trim().length() == 0) {
                    showCustomToast("标题不能为空");
                    return;
                }

                if (mContentEt.getText().toString().trim().length() == 0) {
                    showCustomToast("内容不能为空");
                    return;
                }

                if (AppApplication.getInstance().getUserInfoVO() == null) {
                    showCustomToast("亲，要先登录哦");
                }
                if (mCommitFlag)
                    commit();
                break;

        }
    }

}
