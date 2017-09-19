/**
 * 
 */

package com.bs.bsims.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.bs.bsims.R;
import com.bs.bsims.activity.AddByDepartmentActivity;
import com.bs.bsims.activity.BaseActivity;
import com.bs.bsims.activity.JournalPublishActivity;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.chat.ChatMessageFragment.refreshUnReadMessageListener;
import com.bs.bsims.chatutils.ConcatInfoUtils;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.google.gson.Gson;

import com.yzxIM.IMManager;
import com.yzxIM.data.CategoryId;
import com.yzxIM.data.db.ConversationInfo;
import com.yzxIM.data.db.DiscussionInfo;
import com.yzxIM.listener.DiscussionGroupCallBack;
import com.yzxtcp.data.UcsReason;
import com.yzxtcp.tools.CustomLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-4-14
 * @version 2.0
 * @content: IM聊天首页
 */
public class ChatMessageHomeActivity extends BaseActivity implements
        refreshUnReadMessageListener, DiscussionGroupCallBack {
    private Context mContext;
    private ChatMessageFragment chatMessageFragment;
    // 添加员工使用
    protected List<EmployeeVO> mDataList = new ArrayList<EmployeeVO>();
    protected List<EmployeeVO> mDataList1 = new ArrayList<EmployeeVO>();
    private static IMManager imManager;
    private List<String> memberList;
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    CustomDialog.closeProgressDialog();
                    CustomToast.showShortToast(mContext, "创建失败(讨论组中成员账号出现错误。)");
                    break;

            }
            return true;
        }
    });

    @Override
    public void onRefreshUnReadMessage() {
        // TODO Auto-generated method stub
        checkUnReadMsg();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        checkUnReadMsg();
        super.onResume();
    }

    /**
     * 检查未读消息
     */
    private void checkUnReadMsg() {
        int unreadcount = IMManager.getInstance(this).getUnreadCountAll();
        if (unreadcount != 0) {
            if (unreadcount > 99) {
                // conversations_cout.setText("99+");
            } else {
                // conversations_cout.setText(String.valueOf(unreadcount));
            }
        }
    }

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.boss_self, mContentLayout);
        initView();
    }

    public void initData() {
        mOkTv.setText("聊天");
        chatMessageFragment = new ChatMessageFragment();
        try {
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.add_fragment, chatMessageFragment);
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        chatMessageFragment.setRefreshUnReadMessageListener(this);
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub
        initData();
    }

    public boolean getData() {

        if (BSApplication.getInstance().getResultVO() != null && BSApplication.getInstance().isAppliactionChat()) {
            return true;
        }
        else {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.USER_INFO, map);
            try {
                ResultVO resultVO = gson.fromJson(jsonStr, ResultVO.class);
                if (resultVO.getCode().equals(Constant.RESULT_CODE)) {
                    BSApplication.getInstance().setResultVO(resultVO);
                    if (!BSApplication.getInstance().isAppliactionChat()) {
                        ConcatInfoUtils.getInstance().getIMJavaBean();
                    }
                    return true;

                }
                else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mContext = this;
        imManager = IMManager.getInstance(this);
        imManager.setDiscussionGroup(this);
        mTitleTv.setText("沟通");

    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        mOkTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(ChatMessageHomeActivity.this, AddByDepartmentActivity.class);
                intent.putExtra("employ_name", JournalPublishActivity.class);
                intent.putExtra("title_name", "创建单聊或群聊");
                // intent.putExtra("checkboxlist", (Serializable) mDataList);
                intent.putExtra("requst_number", 10);
                startActivityForResult(intent, 10);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub3
        if (requestCode == 10) {
            if (data == null)
                return;
            mDataList.clear();
            mDataList = (List<EmployeeVO>) data.getSerializableExtra("checkboxlist");
            if (mDataList.size() == 0)
                return;
            if (mDataList.size() > 40) {
                CustomToast.showLongToast(mContext, "创建聊天人数不能大于40人");
                return;
            }
            /** 开始创建单人聊天 **/
            else if (mDataList.size() == 1) {

                if (ConcatInfoUtils.getInstance().getUserByBQX(mDataList.get(0).getUserid()) != null) {
                    Intent i = new Intent();
                    ConversationInfo info = new ConversationInfo();
                    info.setTargetId(ConcatInfoUtils.getInstance().getUserByBQX(mDataList.get(0).getUserid()).getHxuname());
                    info.setCategoryId(CategoryId.PERSONAL.ordinal());
                    // 如果要创建会话 则需要把当前收到消息的人的头像和自己的头像放在会话消息title中
                    info.setConversationTitle(mDataList.get(0).getFullname());
                    i.putExtra("chatTitle", mDataList.get(0).getFullname());
                    i.setClass(mContext, ChatMessageWithPerson.class);
                    i.putExtra("conversation", info);
                    startActivity(i);
                }
                else {
                    CustomToast.showShortToast(mContext, "此人为非法用户");
                }

            }
            else {
                /** 开始创建聊天讨论组 **/
                /** 遍历当前选中的人员id是否存在于本地数据库中 **/
                String discussionName1 = "";
                memberList = new ArrayList<String>();
                memberList.clear();
                for (int i = 0; i < mDataList.size(); i++) {
                    discussionName1 += mDataList.get(i).getFullname() + ",";
                    DepartmentAndEmployeeVO andEmployeeVO = ConcatInfoUtils.getInstance().getUserByBQX(mDataList.get(i).getUserid());
                    if (andEmployeeVO != null) {
                        memberList.add(andEmployeeVO.getHxuname());
                    }
                }

                if (memberList.contains(ConcatInfoUtils.getInstance().getUserByBQX(BSApplication.getInstance().getUserId()).getHxuname())) {
                    memberList.remove(ConcatInfoUtils.getInstance().getUserByBQX(BSApplication.getInstance().getUserId()).getHxuname());
                }
                imManager.createDiscussionGroup(discussionName1.substring(0, discussionName1.length() - 1), memberList);

            }
        }
    }

    @Override
    public void onCreateDiscussion(UcsReason reason, DiscussionInfo discussionInfo) {
        // TODO Auto-generated method stub 300304
        if (reason.getReason() == 0) {
            Intent intent = new Intent(ChatMessageHomeActivity.this, ChatMessageWithPerson.class);
            CustomLog.e("targetId == " + reason.getMsg());
            ConversationInfo info = imManager.getConversation(discussionInfo.getDiscussionId());
            if (null != info) {
                CustomDialog.closeProgressDialog();
                intent.putExtra("conversation", info);
                intent.putExtra("discussion", discussionInfo);// 把创建的讨论组信息也传递过去
                startActivity(intent);
                finish();
            } else {
                reason.getMsg();
                mHandler.sendEmptyMessage(1);
            }
        } else {
            reason.getMsg();
            mHandler.sendEmptyMessage(1);
        }
    }

    @Override
    public void onDiscussionAddMember(UcsReason arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDiscussionDelMember(UcsReason arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onModifyDiscussionName(UcsReason arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onQuiteDiscussion(UcsReason arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (memberList != null) {
            memberList.clear();
            memberList = null;
        }
        super.onDestroy();
    }

}
