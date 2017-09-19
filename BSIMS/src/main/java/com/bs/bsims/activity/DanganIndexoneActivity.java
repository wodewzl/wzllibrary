
package com.bs.bsims.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.AddByDepartmentAdapter;
import com.bs.bsims.adapter.DanganBasicSortAdapter;
import com.bs.bsims.adapter.TwoTreeAdapterBk.ItemOnClickCallback;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.EmployeeOnclickCallback;
import com.bs.bsims.model.DangBasicUserInfo;
import com.bs.bsims.model.DanganBasicInfoMoreSelect;
import com.bs.bsims.model.NoticeObjectResultVO;
import com.bs.bsims.model.NoticeObjectVO;
import com.bs.bsims.model.PDFOutlineElementVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.model.SortComparatorVO1;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.utils.CharacterParser;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.DepartmentMoreUtis;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.bs.bsims.view.BSSideBar;
import com.bs.bsims.view.BSSideBar.OnTouchingLetterChangedListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DanganIndexoneActivity extends BaseActivity implements OnClickListener,
        EmployeeOnclickCallback, OnItemClickListener {

    private SortComparatorVO1 mSortComparatorVO;
    private FrameLayout danganindexoneframlyout_loadingfather;
    private static final String TAG = "DanganIndexoneActivity";
    private TextView mTextView, txt_comm_head_activityName;
    private TextView mTitleTv;
    private TextView mMsgTv, loadingfile1;
    private String mMsgName;
    private DangBasicUserInfo userinfoVo;
    private TextView mBackImage;
    private DanganBasicInfoMoreSelect danganmoreselect;
    private List<DangBasicUserInfo> mList;
    private CharacterParser mCharacterParser;
    private BSSideBar mBSSideBar;
    /** 最外层数据 */
    private ArrayList<PDFOutlineElementVO> mPdfOutlinesCount = new ArrayList<PDFOutlineElementVO>();

    private List<DangBasicUserInfo> list;
    /** 内层数据 */
    private ArrayList<PDFOutlineElementVO> mPdfOutlines = new ArrayList<PDFOutlineElementVO>();
    private DanganBasicSortAdapter treeViewAdapter = null;
    private ListView dplistview;

    private BSIndexEditText mClearEditText;
    private Handler backHandler;
    private int uUstate = 1;
    private CharacterParser characterParser;
    private int moreselectfalge = 0;
    private String type = "1";
    private Map<String, String> moreSelectmap;
    private RelativeLayout four_title_one;
    private LinearLayout mTitle02, mTitle03, mTitle01, mTitleLayout, mTitle04;
    private TextView mTitleName02, mTitleName03, mTitleName01, mTitleName04, mDialog;
    private ImageView mSelectOne, mSelectTwo, mSelectThree, mSelectFour;
    private BSPopupWindwos mPop;
    private AddByDepartmentAdapter mAddByDepartmentAdapter;
    private DepartmentMoreUtis mDepartmentUtis;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mAddByDepartmentAdapter.notifyDataSetChanged();
        };
    };

    private String mDid, mPositions, mPostsid, mKeyword, mMoreChildkey, mMoreChildValue,
            mMoreParentkey, mMoreParentValue;

    private List<NoticeObjectVO> mPostsVOList, mMoreList;
    private String mStatus = "1";// 默认1是岗位，2是更多

    private String isentry = "", isquit = "", date = "";

    private BSPopupWindowsTitle mBsPopupWindowsTitle;
    private int mPopType; // mPopType 值1为部门的，2更多
    private BSPopupWindowsTitle mBsPopupWindowsTitlePost;

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    }

    public void setMsgName(String msgName) {
        this.mMsgName = msgName;
    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.ac_ext_four_title, null);
        mContentLayout.addView(layout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void initView() {
        mAddByDepartmentAdapter = new AddByDepartmentAdapter(this, this,
                R.layout.item_contacts_department_tree_view, false);
        if (ResultVO.getInstance() != null) {
            mDepartmentUtis = new DepartmentMoreUtis(this, ResultVO.getInstance(), mHandler, false);
            mAddByDepartmentAdapter.mfilelist = mDepartmentUtis.getPdfOutlinesCount();
        }

        initTitle();
        mPostsVOList = new ArrayList<NoticeObjectVO>();
        mMoreList = new ArrayList<NoticeObjectVO>();

        initData();
    }

    public void initData() {
        Intent intent = this.getIntent();
        if (intent.getStringExtra("isentry") != null) {
            isentry = intent.getStringExtra("isentry");
            mBSSideBar.setVisibility(View.GONE);
            four_title_one.setVisibility(View.GONE);
            mClearEditText.setVisibility(View.GONE);
            uUstate = 2;
            mTitleTv.setText("新进员工");
            treeViewAdapter.setShowLetter(true);
            type = "2";
            if (intent.getStringExtra("date") != null)
                date = intent.getStringExtra("date");
        }

        if (intent.getStringExtra("isquit") != null) {
            isquit = intent.getStringExtra("isquit");
            mBSSideBar.setVisibility(View.GONE);
            four_title_one.setVisibility(View.GONE);
            mClearEditText.setVisibility(View.GONE);
            uUstate = 2;
            mTitleTv.setText("离职员工");
            treeViewAdapter.setShowLetter(true);
            type = "2";
            if (intent.getStringExtra("date") != null)
                date = intent.getStringExtra("date");
        }
        else if (null == intent.getStringExtra("isentry") && null == intent.getStringExtra("isquit")) {
            uUstate = 1;
        }

        if (intent.getStringExtra("mMoreChildkey") != null && intent.getStringExtra("mMoreChildValue") != null) {
            mMoreChildkey = intent.getStringExtra("mMoreChildkey");
            mMoreChildValue = intent.getStringExtra("mMoreChildValue");
            uUstate = 2;
            four_title_one.setVisibility(View.GONE);
        }
    }

    @Override
    public void bindViewsListener() {
        mTitle01.setOnClickListener(this);
        mTitle02.setOnClickListener(this);
        mTitle04.setOnClickListener(this);
    }

    public void initTitle() {
        danganindexoneframlyout_loadingfather = (FrameLayout) findViewById(R.id.danganindexoneframlyout_loadingfather);
        loadingfile1 = (TextView) findViewById(R.id.loadingfile1);
        mBSSideBar = (BSSideBar) findViewById(R.id.sidrbar);
        mDialog = (TextView) findViewById(R.id.dialog);
        mDialog.setVisibility(View.GONE);
        mBSSideBar.setTextView(mDialog);
        txt_comm_head_activityName = (TextView) findViewById(R.id.txt_comm_head_activityName);
        txt_comm_head_activityName.setText("审批统计");
        mTitle01 = (LinearLayout) findViewById(R.id.title01);
        mTitle02 = (LinearLayout) findViewById(R.id.title02);
        mTitle04 = (LinearLayout) findViewById(R.id.title04);
        four_title_one = (RelativeLayout) findViewById(R.id.four_title_one1);// popwindow的布局文件
        mTitleLayout = (LinearLayout) findViewById(R.id.title_layout1);
        mSelectOne = (ImageView) findViewById(R.id.select_icon01);
        mSelectTwo = (ImageView) findViewById(R.id.select_icon02);
        mSelectFour = (ImageView) findViewById(R.id.select_icon04);

        mTitleName01 = (TextView) findViewById(R.id.title_name_01);
        mTitleName02 = (TextView) findViewById(R.id.title_name_02);
        mTitleName04 = (TextView) findViewById(R.id.title_name_04);
        mTitleName01.setText("全部部门");
        mTitleName02.setText("全部岗位");
        mTitleName04.setText("更多");

        characterParser = CharacterParser.getInstance();
        mClearEditText = (BSIndexEditText) this.findViewById(R.id.edit_single_search);
        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mBSSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = treeViewAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    dplistview.setSelection(position);
                }
            }
        });

        mTitleTv = (TextView) this.findViewById(R.id.txt_comm_head_activityName);
        mTitleTv.setText("员工档案");
        mBackImage = (TextView) this.findViewById(R.id.img_head_back);
        dplistview = (ListView) findViewById(R.id.listView_departmentList1);

        mCharacterParser = CharacterParser.getInstance();
        mList = new ArrayList<DangBasicUserInfo>();
        treeViewAdapter = new DanganBasicSortAdapter(getApplicationContext(), mList, true);

    }

    // 获取部门的数据
    public boolean getData() {
        Gson gson = new Gson();
        String urlStr;
        String jsonUrlStr, jsonUrlStr1;
        try {
            if (uUstate == 1) {

                urlStr = UrlUtil.getUrlByMap1(Constant.DANGANARCHIVESALLUSERINFO, null);
                jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
                userinfoVo = gson.fromJson(jsonUrlStr, DangBasicUserInfo.class);

                String ppostsUlr = BSApplication.getInstance().getHttpTitle()
                        + Constant.NOTICE_OBJECT_POSITIONS + BSApplication.getInstance().getmCompany() + "/userid/" + BSApplication.getInstance().getUserId();
                String jsonPosts = HttpClientUtil.get(ppostsUlr, Constant.ENCODING).trim();
                NoticeObjectResultVO postsResultVO = gson.fromJson(jsonPosts,
                        NoticeObjectResultVO.class);
                mPostsVOList = postsResultVO.getArray();

                String moreselect = UrlUtil
                        .getUrlByMap1(Constant.DANGANARCHIVESALLMORESELECT, null);
                String morejson = HttpClientUtil.get(moreselect, Constant.ENCODING).trim();

                NoticeObjectResultVO moreResultVO = gson.fromJson(morejson,
                        NoticeObjectResultVO.class);
                mMoreList = moreResultVO.getArray();
                uUstate = 2;
                CustomLog.e("WorkUrl12", moreselect);
                if (!userinfoVo.getCode().equals("200") || !postsResultVO.getCode().equals("200")
                        || !moreResultVO.getCode().equals("200")) {
                    return false;
                }
            }
            else {
                moreSelectmap = new HashMap<String, String>();
                moreSelectmap.put("keyword", mKeyword);
                moreSelectmap.put("depart", mDid);
                moreSelectmap.put("positions", mPositions);
                moreSelectmap.put("postsid", mPostsid);
                moreSelectmap.put(mMoreParentkey, mMoreParentValue);
                moreSelectmap.put(mMoreChildkey, mMoreChildValue);
                moreSelectmap.put("isentry", isentry);
                moreSelectmap.put("isquit", isquit);
                moreSelectmap.put("date", date);
                String moreselect = UrlUtil.getUrlByMap1(Constant.DANGANARCHIVESALLUSERINFO,
                        moreSelectmap);
                CustomLog.e("WorkUrl1", moreselect);
                jsonUrlStr = HttpClientUtil.get(moreselect, Constant.ENCODING).trim();
                userinfoVo = gson.fromJson(jsonUrlStr, DangBasicUserInfo.class);
                if (!userinfoVo.getCode().equals("200")) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void executeFailure() {

        if (null == userinfoVo) {
            CommonUtils.setNonetIcon(getApplicationContext(), mLoading, this);
            return;
        }
        dplistview.setAdapter(null);
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        if (userinfoVo != null) {

            if ("400".equals(userinfoVo.getCode())) {
                loadingfile1.setVisibility(View.VISIBLE);
                danganindexoneframlyout_loadingfather.setVisibility(View.GONE);
            }
        }
        else {
            loadingfile1.setVisibility(View.VISIBLE);
            danganindexoneframlyout_loadingfather.setVisibility(View.GONE);
        }

        CustomDialog.closeProgressDialog();
    }

    @Override
    public void executeSuccess() {
        loadingfile1.setVisibility(View.GONE);
        danganindexoneframlyout_loadingfather.setVisibility(View.VISIBLE);
        if (userinfoVo == null) {
            CommonUtils.setNonetIcon(getApplicationContext(), mLoading, this);
            return;
        }

        mLoading.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        // 填充数据
        mSortComparatorVO = new SortComparatorVO1();
        list = userinfoVo.getArray();
        sortData(list);
        Collections.sort(mList, mSortComparatorVO);

        if (treeViewAdapter != null) {
            treeViewAdapter.setType(type);
            dplistview.setAdapter(treeViewAdapter);
            treeViewAdapter.notifyDataSetChanged();

        }
        CustomDialog.closeProgressDialog();
    }

    // 过滤搜索
    private void filterData(String filterStr) {
        List<DangBasicUserInfo> filterDateList = new ArrayList<DangBasicUserInfo>();
        treeViewAdapter.setType(type);
        if (filterStr.trim().equals("")) {
            filterDateList = list;
            loadingfile1.setVisibility(View.GONE);
            danganindexoneframlyout_loadingfather.setVisibility(View.VISIBLE);
            dplistview.setVisibility(View.VISIBLE);
            Collections.sort(filterDateList, mSortComparatorVO);
            treeViewAdapter.updateListView(filterDateList);
        } else {
            filterDateList.clear();
            for (DangBasicUserInfo personnelVO : mList) {
                String name = personnelVO.getFullname();
                if (name.indexOf(filterStr.toString()) != -1
                        || mCharacterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(personnelVO);
                }
            }
            if (filterDateList.size() == 0) {
                loadingfile1.setVisibility(View.VISIBLE);
                danganindexoneframlyout_loadingfather.setVisibility(View.GONE);
            } else {
                loadingfile1.setVisibility(View.GONE);
                danganindexoneframlyout_loadingfather.setVisibility(View.VISIBLE);
                // 根据a-z进行排序
                Collections.sort(filterDateList, mSortComparatorVO);
                treeViewAdapter.updateListView(filterDateList);
                treeViewAdapter.notifyDataSetChanged();
            }
        }

        //

    }

    private class BSPopupWindwos extends PopupWindow implements OnClickListener {
        private Context mContext;
        private Activity mActivity;
        private ExpandableListView mExpandableListView, mExpandablePostListView;
        private ListView mListView;

        private LinearLayout mTitleLayout01, mTitleLayout02, mTitleLayout03;
        private Button mOkBt;
        private int mType;
        private boolean mOne = true;

        private List<String> groupArray;// 组列表
        private List<List<String>> childArray;// 子列表

        private Button mBt;

        public BSPopupWindwos(Context context, View parent) {
            this.mContext = context;
            this.mActivity = (Activity) context;
            View view = View.inflate(context, R.layout.approval_pop_item_05, null);
            mTitleLayout01 = (LinearLayout) view.findViewById(R.id.title_layout_01);
            mTitleLayout02 = (LinearLayout) view.findViewById(R.id.title_layout_02);
            mTitleLayout03 = (LinearLayout) view.findViewById(R.id.title_layout_03);

            mListView = (ListView) view.findViewById(R.id.lv_department);
            mListView.setAdapter(mAddByDepartmentAdapter);

            mExpandablePostListView = (ExpandableListView) view.findViewById(R.id.post_list);
            mExpandablePostListView.setAdapter(new TwoTreeAdapter(mContext, mPostsVOList));
            mExpandablePostListView.setGroupIndicator(null);

            mExpandableListView = (ExpandableListView) view.findViewById(R.id.more_list);
            mExpandableListView.setAdapter(new TwoTreeAdapter(mContext, mMoreList));
            mExpandableListView.setGroupIndicator(null);

            mListView.setAdapter(mAddByDepartmentAdapter);
            mOkBt = (Button) view.findViewById(R.id.ok_bt);
            mOkBt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<PDFOutlineElementVO> list = new ArrayList<PDFOutlineElementVO>();
                    for (int i = 0; i < mAddByDepartmentAdapter.mfilelist.size(); i++) {
                        if (mAddByDepartmentAdapter.mfilelist.get(i).isSelect()) {
                            list.add(mAddByDepartmentAdapter.mfilelist.get(i));
                        }
                    }

                    if (list.size() == 1) {
                        mDid = list.get(0).getDepartmentandwmployee().getDepartmentid();
                        mTitleName01.setText(list.get(0).getDepartmentandwmployee().getDname());
                    } else if (list.size() == 2) {
                        mDid = list.get(1).getDepartmentandwmployee().getDepartmentid();
                        mTitleName01.setText(list.get(1).getDepartmentandwmployee().getDname());
                    } else if (list.size() > 2) {
                        mDid = list.get(0).getDepartmentandwmployee().getDepartmentid();
                        mTitleName01.setText(list.get(0).getDepartmentandwmployee().getDname());

                    } else if (list.size() == 0) {
                        mTitleName01.setText("全部部门");
                    }
                    match(1, mDid);
                    dismiss();
                }
            });

            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
            setWidth(LayoutParams.MATCH_PARENT);
            setHeight(LayoutParams.WRAP_CONTENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            update();

        }

        public void currentView(Context context, int type) {
            moreselectfalge = type;
            if (type == 1) {
                mTitleLayout01.setVisibility(View.VISIBLE);
                mTitleLayout02.setVisibility(View.GONE);
                mTitleLayout03.setVisibility(View.GONE);
            } else if (type == 2) {
                mTitleLayout02.setVisibility(View.VISIBLE);
                mTitleLayout03.setVisibility(View.GONE);
                mTitleLayout01.setVisibility(View.GONE);
            } else {
                mTitleLayout03.setVisibility(View.VISIBLE);
                mTitleLayout02.setVisibility(View.GONE);
                mTitleLayout01.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                default:
                    break;
            }

        }

        private void addInfo(String group, String[] child) {

            groupArray.add(group);

            List<String> childItem = new ArrayList<String>();

            for (int index = 0; index < child.length; index++) {
                childItem.add(child[index]);
            }
            childArray.add(childItem);
        }
    }

    class TwoTreeAdapter extends BaseExpandableListAdapter {
        private Context mContext;
        private List<NoticeObjectVO> groupArray;// 组列表
        ItemOnClickCallback mCallback;

        public TwoTreeAdapter(Context context, List<NoticeObjectVO> groupArray) {
            this.mContext = context;
            this.groupArray = groupArray;
        }

        /*-----------------Child */
        @Override
        public NoticeObjectVO getChild(int groupPosition, int childPosition) {
            if ("1".equals(mStatus)) {
                return groupArray.get(groupPosition).getPositions().get(childPosition);
            } else {
                return groupArray.get(groupPosition).getList().get(childPosition);
            }

        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition,
                boolean isLastChild, View convertView, ViewGroup parent) {

            ChildHolder holder = null;
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.tow_tree_child_itme,
                        null);
                holder.tvName = (TextView) convertView.findViewById(R.id.child_name);
                holder.childLayout = (LinearLayout) convertView.findViewById(R.id.child_layout);

                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }
            if ("1".equals(mStatus)) {
                holder.tvName.setText(groupArray.get(groupPosition).getPositions()
                        .get(childPosition).getPositionsname());
                if ("".equals(groupArray.get(groupPosition).getPositions().get(childPosition))) {
                    holder.tvName.setVisibility(View.GONE);
                    holder.childLayout.setVisibility(View.GONE);
                }
                holder.tvName.setOnClickListener(new ItemOnclick(groupArray.get(groupPosition)
                        .getPositions().get(childPosition)));

            } else {
                holder.tvName.setText(groupArray.get(groupPosition).getList().get(childPosition)
                        .getName());

                if ("".equals(groupArray.get(groupPosition).getList().get(childPosition))) {
                    holder.tvName.setVisibility(View.GONE);
                    holder.childLayout.setVisibility(View.GONE);
                }
                holder.tvName.setOnClickListener(new ItemOnclick(groupArray.get(groupPosition)
                        .getList().get(childPosition)));
            }

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if ("1".equals(mStatus)) {
                if (groupArray.get(groupPosition).getPositions() != null) {
                    return groupArray.get(groupPosition).getPositions().size();
                } else {
                    return 0;
                }
            } else {
                if (groupArray.get(groupPosition).getList() != null) {
                    return groupArray.get(groupPosition).getList().size();
                } else {
                    return 0;
                }
            }

        }

        @Override
        public Object getGroup(int groupPosition) {
            return getGroup(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return groupArray.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {

            ParentHolder holder = null;
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.tow_tree_parent_item,
                        null);
                holder = new ParentHolder();
                holder.parentName = (TextView) convertView.findViewById(R.id.parent_name);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            } else {
                holder = (ParentHolder) convertView.getTag();
            }

            if ("1".equals(mStatus)) {
                holder.parentName.setText(groupArray.get(groupPosition).getPostsname());
            } else {
                holder.parentName.setText(groupArray.get(groupPosition).getName());
            }

            if (moreselectfalge == 2) {
                if (isExpanded) {
                    holder.icon
                            .setImageResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
                } else {
                    holder.icon
                            .setImageResource(R.drawable.ic_contacts_department_fragment_arrow_default);
                }

                holder.parentName.setOnClickListener(new ItemOnclick(groupArray.get(groupPosition)));
            }
            else {
                CustomLog.e("aa", 11 + "");
            }

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private TextView getGenericView(String string) {
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            TextView textView = new TextView(mContext);
            textView.setLayoutParams(layoutParams);

            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

            textView.setPadding(40, 0, 0, 0);
            textView.setText(string);
            return textView;
        }

        class ParentHolder {
            TextView parentName;
            ImageView icon;
        }

        class ChildHolder {
            TextView tvName;
            LinearLayout childLayout;
        }

        class ItemOnclick implements OnClickListener {
            private NoticeObjectVO mNoticeObjectVO;
            private int groupPosition;
            private int childPosition;

            public ItemOnclick(NoticeObjectVO noticeObjectVO) {
                this.mNoticeObjectVO = noticeObjectVO;
                this.groupPosition = groupPosition;
            }

            @Override
            public void onClick(View v) {

                if ("1".equals(mStatus)) {

                    switch (v.getId()) {
                        case R.id.child_name:
                            mTitleName02.setText(mNoticeObjectVO.getPositionsname());

                            match(3, mNoticeObjectVO.getPositionsid());
                            mPop.dismiss();
                            break;

                        case R.id.parent_name:
                            mTitleName02.setText(mNoticeObjectVO.getPostsname());
                            match(2, mNoticeObjectVO.getPostsid());
                            mPop.dismiss();
                            break;
                        default:
                            break;
                    }
                } else {

                    switch (v.getId()) {
                        case R.id.child_name:
                            mTitleName04.setText(mNoticeObjectVO.getName());
                            mMoreChildkey = mNoticeObjectVO.getType();
                            match(5, mNoticeObjectVO.getId());
                            mPop.dismiss();
                            break;
                        case R.id.parent_name:
                            mTitleName04.setText(mNoticeObjectVO.getName());
                            mMoreParentkey = mNoticeObjectVO.getType();
                            match(4, mNoticeObjectVO.getId());
                            mPop.dismiss();
                            break;
                        default:
                            break;
                    }

                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title01:

                mTitleName01.setTextColor(Color.parseColor("#00A9FE"));
                mTitleName02.setTextColor(Color.parseColor("#999999"));
                mTitleName04.setTextColor(Color.parseColor("#999999"));
                mSelectOne
                        .setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
                mSelectTwo
                        .setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_default1);

                mSelectFour
                        .setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_default1);

                // mPop = new BSPopupWindwos(this, mTitleLayout);
                // mPop.currentView(this, 1);
                // if (!mPop.isShowing()) {
                // mPop.showAsDropDown(mTitleLayout, mTitleLayout.getLayoutParams().width / 2, 0);
                // } else {
                // mPop.dismiss();
                // }
                mTitleName01.setText("全部部门");
                mTitleName02.setText("全部岗位");
                mTitleName04.setText("更多");
                mPopType = 1;
                initPopDowns(mTitleLayout);
                // match(6, "");
                mStatus = "2";
                break;
            case R.id.title02:
                mStatus = "1";
                mTitleName02.setTextColor(Color.parseColor("#00A9FE"));
                mSelectTwo
                        .setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
                // mPop = new BSPopupWindwos(this, mTitleLayout);
                // mPop.currentView(this, 2);
                //
                // if (!mPop.isShowing()) {
                // mPop.showAsDropDown(mTitleLayout, mTitleLayout.getLayoutParams().width / 2, 0);
                // } else {
                // mPop.dismiss();
                // }

                mPopType = 2;
                initPopDowns(mTitleLayout);

                break;
            case R.id.title04:
                mStatus = "2";
                mTitleName04.setTextColor(Color.parseColor("#00A9FE"));
                mSelectFour
                        .setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
                // mPop = new BSPopupWindwos(this, mTitleLayout);
                // mPop.currentView(this, 3);
                //
                // if (!mPop.isShowing()) {
                // mPop.showAsDropDown(mTitleLayout, mTitleLayout.getLayoutParams().width / 2, 0);
                // } else {
                // mPop.dismiss();
                // }

                mPopType = 3;
                initPopDowns(mTitleLayout);
                break;
        }
    }

    public void getDanganUserbasicinfo(String dpdid, String positions, String postid,
            String moreselectname,
            String moreselectid
            ) {

        if (!"".equals(dpdid)) {
            moreSelectmap.put("depart", dpdid);
        }
        else if (!"".equals(postid)) {
            moreSelectmap.put(positions, postid);
        }
        else if (!"".equals(moreselectid) && !"".equals(moreselectname)) {
            moreSelectmap.put(moreselectname, moreselectid);
        }
    }

    @Override
    public void employeeOnclick(int arg2, int viewType, PDFOutlineElementVO vo) {

        mDepartmentUtis.employeeOnclick(arg2, viewType);
    }

    private void sortData(List<DangBasicUserInfo> list) {
        mList.clear();
        mCharacterParser = CharacterParser.getInstance();
        for (int i = 0; i < list.size(); i++) {
            DangBasicUserInfo personnelVO = list.get(i);
            String pinyin = mCharacterParser.getSelling(personnelVO.getFullname());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                personnelVO.setSortLetters(sortString.toUpperCase());
            } else {
                personnelVO.setSortLetters("#");
            }
            mList.add(personnelVO);
        }
    }

    public void match(int key, String value) {
        switch (key) {
            case 0:
                break;
            case 1:
                mDid = value;
                break;
            case 2:
                mPostsid = value;
                break;
            case 3:
                mPositions = value;
                break;
            case 4:
                mMoreParentValue = value;
                break;
            case 5:
                mMoreChildValue = value;
                break;
            case 6:
                mDid = value;
                mPostsid = value;
                mPositions = value;
                mMoreParentValue = value;
                mMoreChildValue = value;
                break;
            default:
                break;
        }
        CustomDialog.showProgressDialog(DanganIndexoneActivity.this);
        new ThreadUtil(this, this).start();

    }

    private void initPopDowns(View view)
    {
        if (mPopType == 1) {
            ArrayList<TreeVO> list = CommonUtils.getTreeVOList();
            mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list, callback);
            mBsPopupWindowsTitle.showPopupWindow(view);
        } else if (mPopType == 2) {
            if (mBsPopupWindowsTitlePost == null) {
                ArrayList<TreeVO> list = CommonUtils.getOneLeveTreeVo(mPostsVOList);
                mBsPopupWindowsTitlePost = new BSPopupWindowsTitle(this, list, callback, CommonUtils.getScreenHigh(this) / 3);
            }
            mBsPopupWindowsTitlePost.showPopupWindow(view);
        } else {
            ArrayList<TreeVO> list = new ArrayList<TreeVO>();
            int cout = 0;

            for (int i = 0; i < mMoreList.size(); i++) {
                TreeVO parentVo = new TreeVO();
                parentVo.setId(i + 1);
                parentVo.setParentId(0);
                parentVo.setName(mMoreList.get(i).getName());
                parentVo.setLevel(1);
                if (mMoreList.get(i).getList() != null &&
                        mMoreList.get(i).getList().size() > 0) {
                    parentVo.setHaschild(true);
                } else {
                    parentVo.setHaschild(false);
                }
                list.add(parentVo);

                if (mMoreList.get(i).getList() != null &&
                        mMoreList.get(i).getList().size() > 0) {
                    for (int j = 0; j < mMoreList.get(i).getList().size(); j++) {
                        cout++;
                        TreeVO childVo = new TreeVO();
                        childVo.setId(cout + mMoreList.size());
                        childVo.setParentId(i + 1);
                        childVo.setName(mMoreList.get(i).getList().get(j).getName());
                        childVo.setChildSearchId(mMoreList.get(i).getList().get(j).getId());
                        childVo.setLevel(2);
                        childVo.setType(mMoreList.get(i).getList().get(j).getType());
                        list.add(childVo);
                    }
                }
            }
            mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list, callback);
            mBsPopupWindowsTitle.showPopupWindow(view);
        }

    }

    TreeCallBack callback = new TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
            if (mPopType == 1) {
                mDid = vo.getDepartmentid();
                // -1代表全部部门
                if ("-1".equals(mDid)) {
                    match(6, "");
                } else {
                    match(1, mDid);
                }
                mTitleName01.setText(vo.getDname());

            } else if (mPopType == 2) {
                mPostsid = "";
                mPositions = vo.getSearchId() + "";
                mTitleName02.setText(vo.getName());
                match(3, mPositions);
            } else {
                mTitleName04.setText(vo.getName());
                mMoreChildkey = vo.getType();
                match(5, vo.getChildSearchId() + "");
            }

        }
    };
}
