/**
 * 
 */

package com.bs.bsims.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.AddByDepartmentActivity;
import com.bs.bsims.activity.AddByPersonActivity;
import com.bs.bsims.activity.BaseActivity;
import com.bs.bsims.activity.HeadActivity;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.chatutils.ConcatInfoUtils;
import com.bs.bsims.chatutils.ConversationBgDbManager;
import com.bs.bsims.image.selector.ImageActivityUtils;
import com.bs.bsims.image.selector.MultiImageSelectorActivity;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSGridView;
import com.bs.bsims.view.BSSwitchView;
import com.bs.bsims.view.BSSwitchView.OnChangedListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzxIM.IMManager;
import com.yzxIM.data.db.ConversationInfo;
import com.yzxIM.data.db.DiscussionInfo;
import com.yzxIM.listener.DiscussionGroupCallBack;
import com.yzxtcp.data.UcsReason;
import com.yzxtcp.tools.CustomLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-7-5
 * @version 2.0
 */

// 讨论组详情
public class ChatGroupDetail extends BaseActivity implements DiscussionGroupCallBack, OnChangedListener {

    private BSGridView inform_gv;
    private int discussionNums;// 讨论组成员数
    private ConversationInfo conversationinfo;
    private DiscussionInfo discussioninfo;
    private HeadChatAdapter mInformAdapter;
    private HeadAdapter mInformAdapter1;
    private String membersUser[];
    private List<EmployeeVO> mEmployList;
    private TextView mGroupTitle;
    private Boolean isOwner = false;// 是否拥有管理权权限

    private TextView mDucissName;
    private BSDialog dialog;
    private Activity mActivity;
    private ProgressDialog mProgressDialog;
    private Timer mTimer;
    private Handler mHandler;
    private IMManager imManager;
    private int onMovePositon;
    private List<String> memberList;
    // 添加员工使用
    protected List<EmployeeVO> mDataList = new ArrayList<EmployeeVO>();
    protected List<EmployeeVO> mDataList1 = new ArrayList<EmployeeVO>();
    private StringBuffer mIMid;
    private BSSwitchView mIsAnonymousSw;

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.im_chatgroupdetail, mContentLayout);
        mActivity = this;
        imManager = IMManager.getInstance(mActivity);
        imManager.setDiscussionGroup(this);
        initView();
        initData();
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        inform_gv = (BSGridView) findViewById(R.id.approver_gv);
        mGroupTitle = (TextView) findViewById(R.id.approver_tv);
        mDucissName = (TextView) findViewById(R.id.dussciss_name);
        mIsAnonymousSw = (BSSwitchView) findViewById(R.id.is_anonymous_status);
    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub

        findViewById(R.id.clear_allmsg).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v2) {
                // TODO Auto-generated method stub
                View v1 = LayoutInflater.from(mActivity).inflate(R.layout.dialog_lv_item, null);
                final TextView textView = (TextView) v1.findViewById(R.id.textview);
                // textView.setText("您当前有" + dataClean.getFileSize() + "可以清除，确定清楚吗？");
                textView.setText("您确定清除聊记录吗？");
                dialog = new BSDialog(mActivity, "清除聊天记录", v1, new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        conversationinfo.clearMessages();
                        conversationinfo.setDraftMsg("");
                        dialog.dismiss();
                        setResult(3);
                        ChatGroupDetail.this.finish();
                    }
                });
                dialog.show();
            }
        });

        findViewById(R.id.owr_groupname).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v2) {
                // TODO Auto-generated method stub
                View v1 = LayoutInflater.from(mActivity).inflate(R.layout.dialog_edittext, null);
                final EditText textView = (EditText) v1.findViewById(R.id.edit_content);
                textView.setText(conversationinfo.getConversationTitle());
                dialog = new BSDialog(mActivity, "请输入内容", v1, new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        String titleStr = textView.getText().toString().trim();
                        if (titleStr.length() > 0 && titleStr.length() < 10) {
                            mDucissName.setText(textView.getText().toString());
                            imManager.modifyDiscussionTitle(discussioninfo.getDiscussionId(), titleStr);
                        } else {
                            CustomToast.showShortToast(mActivity, "请填写正确讨论组名称,长度不合法");
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        findViewById(R.id.change_bg).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // startActivityForResult(new Intent(ChatGroupDetail.this,
                // IMMessageBgActivity.class),2);
            }
        });

        mIsAnonymousSw.SetOnChangedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 判断是否有背景
        if (ConversationBgDbManager.getInstance().getById(conversationinfo.getTargetId()) != null) {
            mIsAnonymousSw.setCheckState(true);
        }
        else {
            mIsAnonymousSw.setCheckState(false);
        }
    }

    public void initData() {
        conversationinfo = (ConversationInfo) getIntent().getSerializableExtra("conversation");
        discussioninfo = imManager.getDiscussionInfo(conversationinfo.getTargetId());
        discussionNums = discussioninfo.getMemberCount();// 获取人数
        membersUser = discussioninfo.getDiscussionMembers().split(",");
        mEmployList = new ArrayList<EmployeeVO>();
        memberList = new ArrayList<String>();
        for (int i = 0; i < membersUser.length; i++) {
            DepartmentAndEmployeeVO vo = ConcatInfoUtils.getInstance().getUserByIM(membersUser[i]);
            if (vo == null)
                continue;
            EmployeeVO e = new EmployeeVO();
            e.setFullname(vo.getFullname());
            e.setHeadpic(vo.getHeadpic());
            e.setUserid(vo.getUserid());
            mEmployList.add(e);
        }
        // 判断管理员权限
        if (ConcatInfoUtils.getInstance().getUserByBQX(BSApplication.getInstance().getUserId()).getHxuname().equals(discussioninfo.getOwnerId())) {
            isOwner = true;
            mInformAdapter = new HeadChatAdapter(this, true, 1);
            inform_gv.setAdapter(mInformAdapter);
            if (null != mEmployList) {
                mInformAdapter.mList.clear();
                mInformAdapter.mList.addAll(mEmployList);
                mInformAdapter.notifyDataSetChanged();
            }
            findViewById(R.id.owr_groupname).setVisibility(View.VISIBLE);
        }
        else {
            mInformAdapter1 = new HeadAdapter(this, false);
            inform_gv.setAdapter(mInformAdapter1);

            if (null != mEmployList) {
                mInformAdapter1.mList.clear();
                mInformAdapter1.mList.addAll(mEmployList);
                mInformAdapter1.notifyDataSetChanged();
            }
            findViewById(R.id.owr_groupname).setVisibility(View.GONE);
        }

        mTitleTv.setText("讨论组详情");
        mGroupTitle.setText("讨论组成员(" + discussionNums + ")");
        mDucissName.setText(conversationinfo.getConversationTitle());
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer = null;
                }
                switch (msg.what) {
                    case 1:// 增加讨论组人数
                           // 更新讨论组信息
                        discussioninfo = imManager.getDiscussionInfo(conversationinfo.getTargetId());
                        discussionNums += mDataList.size();
                        mGroupTitle.setText("讨论组成员(" + discussionNums + ")");
                        mEmployList.addAll(mDataList);
                        mInformAdapter.mList.clear();
                        mInformAdapter.mList.addAll(mEmployList);
                        mInformAdapter.notifyDataSetChanged();

                        break;
                    case 2:// 减少讨论组人数
                           // 更新讨论组信息
                        discussioninfo = imManager.getDiscussionInfo(conversationinfo.getTargetId());
                        discussionNums -= 1;
                        mGroupTitle.setText("讨论组成员(" + discussionNums + ")");
                        mInformAdapter.mList.remove(onMovePositon);
                        mInformAdapter.notifyDataSetChanged();

                        break;
                    case 3:
                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                        }
                        CustomToast.showShortToast(mActivity, "添加成员失败");
                        break;
                    case 4:
                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                        }
                        CustomToast.showShortToast(mActivity, "删除成员失败");
                        break;
                    case 5:
                        CustomToast.showShortToast(mActivity, "退出讨论组失败");
                        break;
                }
                return true;
            }
        });
    }

    private void showProgress(String content) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(content);
        mProgressDialog.show();

    }

    public void onQuiteDcs(View view) {
        View v1 = LayoutInflater.from(mActivity).inflate(R.layout.dialog_lv_item, null);
        final TextView textView = (TextView) v1.findViewById(R.id.textview);
        // textView.setText("您当前有" + dataClean.getFileSize() + "可以清除，确定清楚吗？");
        textView.setText("删除并退出后,将不再接收此讨论组信息");
        dialog = new BSDialog(mActivity, "退出讨论组", v1, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                imManager.quitDiscussionGroup(discussioninfo.getDiscussionId());
                mTimer = new Timer();
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        CustomLog.d("退出讨论组超时");
                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                        }
                        mHandler.sendEmptyMessage(5);
                    }
                }, 30000);
                showProgress("正在退出");
            }
        });
        dialog.show();
    }

    @Override
    public void onCreateDiscussion(UcsReason arg0, DiscussionInfo arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDiscussionAddMember(UcsReason reason) {
        // TODO Auto-generated method stub
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (reason.getReason() == 0) {
            mHandler.sendEmptyMessage(1);
        } else {
            mHandler.sendEmptyMessage(3);
        }
    }

    @Override
    public void onDiscussionDelMember(UcsReason reason) {
        // TODO Auto-generated method stub
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (reason.getReason() == 0) {
            mHandler.sendEmptyMessage(2);
        } else {
            mHandler.sendEmptyMessage(4);
        }
    }

    @Override
    public void onModifyDiscussionName(UcsReason arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onQuiteDiscussion(UcsReason reason) {
        // TODO Auto-generated method stub
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (reason.getReason() == 0) {
            mHandler.sendEmptyMessage(6);
            setResult(RESULT_FIRST_USER);
            finish();
        } else {
            mHandler.sendEmptyMessage(5);
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        switch (arg0) {
        /* 相关人 */
            case 11:
                if (arg1 == 11) {
                    if (arg2 != null) {
                        // 添加人员
                        imManager.setDiscussionGroup(this);
                        mDataList.clear();
                        mDataList = (List<EmployeeVO>) arg2.getSerializableExtra("checkboxlist");
                        String userId[];
                        if (mDataList.size() == 0) {
                            mHandler.sendEmptyMessage(3);
                            return;
                        }
                        else {
                            // A:mEmployList
                            // B:mDataList
                            List<Integer> numList = new ArrayList<Integer>();
                            List<EmployeeVO> mList5 = new ArrayList<EmployeeVO>();
                            memberList = new ArrayList<String>();
                            for (int i = 0; i < mDataList.size(); i++) {
                                for (int j = 0; j < mEmployList.size(); j++) {
                                    if (mEmployList.get(j).getUserid().equals(mDataList.get(i).getUserid())) {
                                        numList.add(i);
                                    }
                                }
                            }
                            mList5.clear();
                            mList5.addAll(mDataList);
                            for (int i = 0; i < numList.size(); i++) {
                                mDataList.remove(mList5.get(numList.get(i)));
                            }
                            if (mDataList.size() > 0) {
                                numList.clear();
                                for (int i = 0; i < mDataList.size(); i++) {
                                    DepartmentAndEmployeeVO andEmployeeVO = ConcatInfoUtils.getInstance().getUserByBQX(mDataList.get(i).getUserid());
                                    if (andEmployeeVO != null) {
                                        memberList.add(andEmployeeVO.getHxuname());
                                    }
                                    else {
                                        numList.add(i);
                                    }
                                }
                                for (int i = 0; i < numList.size(); i++) {
                                    mDataList.remove(numList.get(i));// 删除没有聊天id的人。
                                }
                                imManager.addDiscussionGroupMember(discussioninfo.getDiscussionId(),
                                        memberList);
                            }
                            else {
                                CustomToast.showShortToast(mActivity, "未添加任何成员");
                            }
                        }
                    }
                }
                break;

            case ImageActivityUtils.REQUEST_IMAGE:
                if (arg1 == RESULT_OK) {
                    ArrayList<String> mSelectPath = arg2.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    try {
                        if (TextUtils.isEmpty(mSelectPath.get(0))) {
                            CustomToast.showShortToast(this, "图片选择错误");
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    setResult(2, arg2);
                    this.finish();
                }
                break;

        }
    }

    public class HeadChatAdapter extends BaseAdapter {
        public List<EmployeeVO> mList;
        private int mState;
        private Context mContext;
        private ImageLoader mImageLoader;
        private DisplayImageOptions mOptions;
        private boolean mAdd;
        private boolean approval = false;
        private boolean proven = false;
        private String status;

        private int requesKey;

        // 单选
        private String isone;
        public List<EmployeeVO> mEmployeeVOList;

        // 只显示一行数据
        private boolean mOneItem = false;

        public boolean isProven() {
            return proven;
        }

        public void setProven(boolean proven) {
            this.proven = proven;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isApproval() {
            return approval;
        }

        public void setApproval(boolean approval) {
            this.approval = approval;
        }

        // CcAdapter state 参数为图片边缘的提醒的小图样式
        public HeadChatAdapter(Context context, int state) {
            // TODO Auto-generated constructor stub
            mList = new ArrayList<EmployeeVO>();
            mState = state;
            mContext = context;
            mImageLoader = ImageLoader.getInstance();
        }

        public HeadChatAdapter(Context context, boolean add, int crmJionrequesKey) {
            mContext = context;
            mAdd = add;
            mList = new ArrayList<EmployeeVO>();
            requesKey = crmJionrequesKey;
            mImageLoader = ImageLoader.getInstance();
            mOptions = CommonUtils.initImageLoaderOptions();
        }

        public HeadChatAdapter(Context context, boolean add, String isone) {
            mContext = context;
            mAdd = add;
            mList = new ArrayList<EmployeeVO>();
            mImageLoader = ImageLoader.getInstance();
            mOptions = CommonUtils.initImageLoaderOptions();
            this.isone = isone;
        }

        public HeadChatAdapter(Context context, boolean add, boolean oneItme) {
            mContext = context;
            mAdd = add;
            mList = new ArrayList<EmployeeVO>();
            mImageLoader = ImageLoader.getInstance();
            mOptions = CommonUtils.initImageLoaderOptions();
            mOneItem = oneItme;
        }

        @Override
        public int getCount() {
            if (mAdd) {
                return (mList.size() + 1);
            } else if (mOneItem) {
                if (mList.size() >= 5) {
                    return 5;
                } else {
                    return mList.size();
                }

            } else {
                return mList.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.gv_head_icon_item, null);
                holder.itmeLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
                holder.personHead = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
                holder.personName = (TextView) convertView.findViewById(R.id.person_name);
                holder.deleteIcon = (ImageView) convertView.findViewById(R.id.delete_icon);
                holder.go = (TextView) convertView.findViewById(R.id.go_bt);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                holder.personHead.setSemicircleNumber("");
            }

            if (mList.size() == position) {
                holder.personHead.setBackground(null);
                holder.personHead.setImageResource(R.drawable.add_persion);
                holder.personName.setVisibility(View.GONE);
                holder.deleteIcon.setVisibility(View.GONE);
            } else {
                EmployeeVO infoVO = mList.get(position);
                mImageLoader.displayImage(infoVO.getHeadpic(), holder.personHead,
                        CommonUtils.initImageLoaderOptions());
                holder.personHead.setUserId(infoVO.getUserid());// HL
                holder.personName.setText(infoVO.getFullname());
                holder.personName.setVisibility(View.VISIBLE);
                holder.deleteIcon.setVisibility(View.VISIBLE);
                if (position == 0) {
                    holder.deleteIcon.setVisibility(View.GONE);
                }
            }

            // holder.itmeLayout.setOnLongClickListener(new HeadLongTouchListener());
            if (mAdd) {
                if (mList.size() == position) {
                    holder.personHead.setOnClickListener(new HeadLongTouchListener(null, position));
                } else {
                    holder.personHead.setOnClickListener(new HeadLongTouchListener(mList.get(position),
                            position));
                    if (position == 0) {
                        holder.personHead.setOnClickListener(null);
                    }
                }
            }

            holder.go.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    intent.putExtra("head_list", (Serializable) mList);
                    intent.putExtra("title", "知会人");
                    intent.setClass(mContext, HeadActivity.class);
                    mContext.startActivity(intent);
                }
            });

            return convertView;
        }

        class ViewHolder
        {
            private BSCircleImageView personHead;
            private TextView personName, go;
            private LinearLayout itmeLayout;
            private ImageView deleteIcon;
        }

        public void updateData(List<EmployeeVO> list) {
            mList.clear();
            mList.addAll(list);
            this.notifyDataSetChanged();
        }

        private class HeadLongTouchListener implements OnLongClickListener, OnClickListener {
            private Activity context;
            private int position;
            private EmployeeVO employeeVO;

            public HeadLongTouchListener(EmployeeVO employeeVO, int position) {
                this.context = (Activity) mContext;
                this.position = position;
                this.employeeVO = employeeVO;
            }

            @Override
            public boolean onLongClick(View arg0) {
                return false;
            }

            @Override
            public void onClick(View arg0) {
                if ("1".equals(isone)) {
                    Intent intent = new Intent();
                    intent.setClass(context, AddByPersonActivity.class);
                    intent.putExtra("requst_number", 2014);
                    intent.putExtra("checkboxlist", (Serializable) getmEmployeeVOList());
                    intent.putExtra("is_new_data", true);
                    context.startActivityForResult(intent, 2014);
                } else {
                    if (employeeVO == null) {
                        Intent intent = new Intent();
                        intent.setClass(context, AddByDepartmentActivity.class);
                        intent.putExtra("employ_name", ChatGroupDetail.class);
                        intent.putExtra("checkboxlist", (Serializable) mList);
                        // 表示选择的联合跟进人
                        if (requesKey == 1) {
                            intent.putExtra("requst_number", 11);
                            intent.putExtra("isSelect", true);// 让选中的不能点击
                            context.startActivityForResult(intent, 11);

                        }
                        // 表示选择相关人
                        else if (requesKey == 2) {
                            intent.putExtra("requst_number", 10);
                            context.startActivityForResult(intent, 10);
                        }
                    } else {
                        showProgress("正在删除");
                        mTimer = new Timer();
                        mTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                CustomLog.d("删除成员超时");
                                if (mProgressDialog != null) {
                                    mProgressDialog.dismiss();
                                }
                                mHandler.sendEmptyMessage(4);
                            }
                        }, 30000);
                        onMovePositon = position;
                        List<String> mList = new ArrayList<String>();
                        mList.clear();
                        mList.add(ConcatInfoUtils.getInstance().getUserByBQX(employeeVO.getUserid()).getHxuname());
                        imManager.delDiscussionGroupMember(discussioninfo.getDiscussionId(), mList);
                    }
                }
            }
        }

        public boolean ismAdd() {
            return mAdd;
        }

        public void setmAdd(boolean mAdd) {
            this.mAdd = mAdd;
        }

        public List<EmployeeVO> getmEmployeeVOList() {
            return mEmployeeVOList;
        }

        public void setmEmployeeVOList(List<EmployeeVO> mEmployeeVOList) {
            this.mEmployeeVOList = mEmployeeVOList;
        }

    }

    @Override
    public void OnChanged(boolean checkState, View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.is_anonymous_status:
                if (mIsAnonymousSw.getCheckState()) {
                    Intent intent = new Intent(ChatGroupDetail.this, MultiImageSelectorActivity.class);
                    // 是否显示拍摄图片
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
                    // 选择模式
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, 1);
                    // 最大可选择图片数量
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                    startActivityForResult(intent, ImageActivityUtils.REQUEST_IMAGE);

                } else {
                    ConversationBgDbManager.getInstance().deleteById(conversationinfo.getTargetId());
                }

                break;
        }
    }

}
