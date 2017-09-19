
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.chat.ChatMessageWithPerson;
import com.bs.bsims.chatutils.ConcatInfoUtils;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.image.selector.ImageActivityUtils;
import com.bs.bsims.model.UserInfoDetailResultVO;
import com.bs.bsims.model.UserInfoDetailVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSCircleImageView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzxIM.data.CategoryId;
import com.yzxIM.data.db.ConversationInfo;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class ContactPersonActivity extends BaseActivity implements OnClickListener {

    private Context mContext;
    private String mUid;

    private TextView mBackTextView, mTitleName, mLikeImageView;
    private ImageView mContactSex;
    private TextView mManyLike, mDPName;
    private BSCircleImageView bsCircleImageView;
    private LinearLayout mActirelLy, mFooterLy, mRLayout;
    private TextView mPhone, mShortPhone, mQQTv, mMsg;
    private ImageButton mPhoneBt, mPhoneShortBt, mSendMsg, mMsgBt, mQQBt;
    private UserInfoDetailVO mUserInfoDetailVO;
    private UserInfoDetailResultVO detailResultVO;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Button mSendChatBt;

    // 员工档案的textview
    private TextView mArtriInfo;

    private boolean goIMchat;

    @Override
    public void initView() {
        imageLoader = ImageLoader.getInstance();
        options = CommonUtils.initImageLoaderOptions();
        mBackTextView = (TextView) findViewById(R.id.head_back1);
        mTitleName = (TextView) findViewById(R.id.contact_name);
        mLikeImageView = (TextView) findViewById(R.id.contact_like);
        mManyLike = (TextView) findViewById(R.id.howperson_like);
        mDPName = (TextView) findViewById(R.id.contact_pdname);
        bsCircleImageView = (BSCircleImageView) findViewById(R.id.head_icon_s);
        mContactSex = (android.widget.ImageView) findViewById(R.id.contact_sex);
        mActirelLy = (LinearLayout) findViewById(R.id.contact_archives);
        mFooterLy = (LinearLayout) findViewById(R.id.person_footer);
        mRLayout = (LinearLayout) findViewById(R.id.round_scroller);
        mPhone = (TextView) findViewById(R.id.phone_num_tv);
        mShortPhone = (TextView) findViewById(R.id.phone_short_tv);
        mQQTv = (TextView) findViewById(R.id.contact_qq);
        mMsg = (TextView) findViewById(R.id.contact_msg);
        mPhoneBt = (ImageButton) findViewById(R.id.call_bt);
        mPhoneShortBt = (ImageButton) findViewById(R.id.call_short_bt);
        mMsgBt = (ImageButton) findViewById(R.id.msg_bt);
        mSendMsg = (ImageButton) findViewById(R.id.sendmsg_bt);
        mQQBt = (ImageButton) findViewById(R.id.qq);
        mArtriInfo = (TextView) findViewById(R.id.person_archives);
        mSendChatBt = (Button) findViewById(R.id.im_chat_btn);
        mLikeImageView.setBackground(CommonUtils.setBackgroundShap(mContext, 15, R.color.translucent, R.color.fuxk_settle_black_mark));
        initData();
    }

    public void initData() {
        mUid = getIntent().getStringExtra("uid");
        if (getIntent().getStringExtra("username") != null) {
            mTitleName.setVisibility(View.VISIBLE);
            mTitleName.setText(getIntent().getStringExtra("username"));
        }

        if (getIntent().getByteArrayExtra("image") != null) {
            // byte[] byteArray = CommonImageUtils.getByteImg();
            byte[] byteArray = getIntent().getByteArrayExtra("image");
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            bsCircleImageView.setImageBitmap(bitmap);
        }
        else if (getIntent().getStringExtra("imageUrl") != null) {
            imageLoader.displayImage(getIntent().getStringExtra("imageUrl"), bsCircleImageView, options);
        }

        if ("1".equals(CommonUtils.getLimitsSpecial(Constant.LIMITS_AFFAIR010))) {
            mActirelLy.setOnClickListener(this);
        }
        else {
            mArtriInfo.setTextColor(getResources().getColor(R.color.C6));
        }

        if (ConcatInfoUtils.getInstance().getUserByBQX(mUid) != null && !ConcatInfoUtils.getInstance().getUserByBQX(mUid).getHxuname().equals("")) {
            mSendChatBt.setVisibility(View.VISIBLE);
        }
        else {
            mSendChatBt.setVisibility(View.GONE);
        }

        // //判断聊天缓存是否有这个人
        // if(ChatMessageHeadDbManager.getInstance().haveUser(mUid)){
        // //存在的情况
        // goIMchat =true;
        // }
        // else{
        // goIMchat= false;
        // }

    }

    @SuppressLint("NewApi")
    public void updateUI() {
        // mTitleName.setText(mUserInfoDetailVO.getFullname());
        mManyLike.setText("粉丝:" + mUserInfoDetailVO.getFavorCount());
        mDPName.setText(mUserInfoDetailVO.getDname() + "/" + mUserInfoDetailVO.getPname());
        // imageLoader.displayImage(mUserInfoDetailVO.getHeadpic(), bsCircleImageView, options);
        if (mUserInfoDetailVO.getSex().equals("女")) {
            mContactSex.setBackgroundResource(R.drawable.person_woman);
        }
        else {
            mContactSex.setBackgroundResource(R.drawable.person_man);

        }
        if (mUserInfoDetailVO.getIscollect().equals("0")) {
            mLikeImageView.setText("添加收藏");
            mLikeImageView.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.contacts_nolike), null, null, null);
        }
        else {
            mLikeImageView.setText("已收藏");
            mLikeImageView.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.contacts_islike), null, null, null);
        }
        mPhoneBt.setVisibility(View.VISIBLE);
        mPhoneShortBt.setVisibility(View.VISIBLE);
        mMsgBt.setVisibility(View.VISIBLE);
        mQQBt.setVisibility(View.VISIBLE);
        mLikeImageView.setVisibility(View.VISIBLE);
        mSendMsg.setVisibility(View.VISIBLE);
        mPhone.setText(mUserInfoDetailVO.getTel());
        mShortPhone.setText(mUserInfoDetailVO.getCornet());
        mQQTv.setText(mUserInfoDetailVO.getQq());
        mMsg.setText(mUserInfoDetailVO.getEmail());
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.head_back1:
                this.finish();
                break;

            case R.id.contact_archives:
                intent.putExtra("uid", mUid);
                intent.setClass(ContactPersonActivity.this, DanganIndextwoActivity.class);
                startActivity(intent);
                break;
            case R.id.person_footer:
                intent.putExtra("uid", mUid);
                intent.putExtra("time", DateUtils.getCurrentDate());
                intent.setClass(ContactPersonActivity.this, JournalFootPrintActivity.class);
                startActivity(intent);
                break;
            case R.id.call_bt:
                if (mUserInfoDetailVO.getTel() != null) {
                    CommonUtils.call(ContactPersonActivity.this, mUserInfoDetailVO.getTel());
                }
                break;
            case R.id.call_short_bt:
                if (mUserInfoDetailVO.getCornet() != null) {
                    CommonUtils.call(ContactPersonActivity.this, mUserInfoDetailVO.getCornet());
                }
                break;
            case R.id.sendmsg_bt:
                if (mUserInfoDetailVO.getTel() != null) {
                    CommonUtils.sendMsg(ContactPersonActivity.this, mUserInfoDetailVO.getTel());
                }
                break;
            case R.id.msg_bt:
                if (mUserInfoDetailVO.getEmail() != null) {
                    CommonUtils.sendEmail(ContactPersonActivity.this, mUserInfoDetailVO.getEmail());
                }
                break;
            case R.id.contact_like:
                if (mUserInfoDetailVO.getIscollect().equals("0")) {
                    commitLike();
                }
                // 取消关注
                else {
                    commitDisLike();
                }
                break;
                
            case R.id.im_chat_btn:
                sendimChat();
                break;

        }
    }

    public boolean getData() {
        try {
            String urlStr = UrlUtil.getPersonDetailUrl(Constant.USER_INFO_DETAIL, BSApplication.getInstance().getmCompany(), mUid);
            String jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            Gson gson = new Gson();
            detailResultVO = gson.fromJson(jsonUrlStr, UserInfoDetailResultVO.class);
            mUserInfoDetailVO = detailResultVO.getArray().get(0);
            if (detailResultVO.getCode().equals(Constant.RESULT_CODE)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.contact_person_new, null);
        mContext = this;
        mContentLayout.addView(layout);
        mHeadLayout.setVisibility(View.GONE);
        mLoadingaAimation.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        baseHeadLayout.setBackgroundColor(Color.parseColor("#2A76D4"));
        ImageActivityUtils.setInWindowAnimationsShareElements(this);
    }

    @Override
    public boolean getDataResult() {

        return getData();

    }

    @Override
    public void executeFailure() {
        // TODO Auto-generated method stub
        mRLayout.setVisibility(View.GONE);
        CommonUtils.setNonetIcon(this, (TextView) findViewById(R.id.loadings), this);
    }

    @Override
    public void updateUi() {
        mLoading.setVisibility(View.GONE);
        updateUI();
    }

    @Override
    public void bindViewsListener() {
        mBackTextView.setOnClickListener(this);
        mLikeImageView.setOnClickListener(this);
        mFooterLy.setOnClickListener(this);
        mPhoneBt.setOnClickListener(this);
        mPhoneShortBt.setOnClickListener(this);
        mMsgBt.setOnClickListener(this);
        mSendMsg.setOnClickListener(this);
        mSendChatBt.setOnClickListener(this);
    }

    // 关注
    public void commitLike() {
        RequestParams params = new RequestParams();
        String url = null;
        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("uid", mUid);// 日志的用户id"171"mLoguid
            url = BSApplication.getInstance().getHttpTitle() + Constant.CONTACTS_LIKE;

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            }

            @SuppressLint("NewApi")
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String code = (String) jsonObject.get("code");
                    String str = (String) jsonObject.get("retinfo");
                    if (Constant.RESULT_CODE.equals(code)) {
                        mLikeImageView.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.contacts_islike), null, null, null);
                        mLikeImageView.setText("已收藏");
                        mUserInfoDetailVO.setIscollect("1");
                    }
                    CustomToast.showShortToast(mContext, str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    // 取消关注
    public void commitDisLike() {
        RequestParams params = new RequestParams();
        String url = null;
        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("uid", mUid);// 日志的用户id"171"mLoguid
            url = BSApplication.getInstance().getHttpTitle() + Constant.CONTACTS_DISLIKE;

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            }

            @SuppressLint("NewApi")
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String code = (String) jsonObject.get("code");
                    String str = (String) jsonObject.get("retinfo");
                    if (Constant.RESULT_CODE.equals(code)) {
                        mLikeImageView.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.contacts_nolike), null, null, null);
                        mLikeImageView.setText("添加收藏");
                        mUserInfoDetailVO.setIscollect("0");
                    }
                    CustomToast.showShortToast(mContext, str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 21) {
            this.finishAfterTransition();
        }

        super.onDestroy();
    }

    public void sendimChat() {
        Intent i = new Intent();
        ConversationInfo info = new ConversationInfo();
        info.setTargetId(ConcatInfoUtils.getInstance().getUserByBQX(mUid).getHxuname());
        info.setCategoryId(CategoryId.PERSONAL.ordinal());
        // 如果要创建会话 则需要把当前收到消息的人的头像和自己的头像放在会话消息title中
        info.setConversationTitle(ConcatInfoUtils.getInstance().getUserByBQX(mUid).getFullname());
        i.putExtra("chatTitle", mTitleName.getText().toString());
        i.setClass(mContext, ChatMessageWithPerson.class);
        i.putExtra("conversation", info);
        // 把当前的这个的头像地址和姓名和IMid缓存到数据库中
        startActivity(i);
    }

}
