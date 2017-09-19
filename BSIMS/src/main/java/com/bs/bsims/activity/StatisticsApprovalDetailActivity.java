
package com.bs.bsims.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.AddByDepartmentAdapter;
import com.bs.bsims.adapter.StatisticsLeaveDetailAdapter;
import com.bs.bsims.adapter.StatisticsSuppliesDetailAdapter;
import com.bs.bsims.adapter.TwoTreeAdapterBk.ItemOnClickCallback;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.EmployeeOnclickCallback;
import com.bs.bsims.model.PDFOutlineElementVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.model.StatisticsApprovalVO;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.time.ScreenInfo;
import com.bs.bsims.time.WheelMain;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DepartmentMoreUtis;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class StatisticsApprovalDetailActivity extends BaseActivity implements EmployeeOnclickCallback, OnClickListener, OnItemClickListener {
    private View mDivider02;
    private LinearLayout mTitle02, mTitle03, mTitle01, mTitleLayout;
    private TextView mTitleName02, mTitleName03, mTitleName01;
    private ImageView mSelectOne, mSelectTwo, mSelectThree;
    private BSPopupWindwos mPop;
    private String mClass, mType, mDatetype, mDept;
    // private StatisticsSuppliesAdapter mStatisticsSuppliesAdapter;
    private StatisticsSuppliesDetailAdapter mStatisticsApprovalAdapter;
    private StatisticsLeaveDetailAdapter mStatisticsLeaveDetailAdapter;
    private StatisticsApprovalVO mStatisticsApprovalVO;

    private List<String> groupArray;// 组列表
    private List<List<String>> childArray;// 子列表

    private ArrayList<PDFOutlineElementVO> mPdfOutlinesCount;
    private DepartmentMoreUtis mDepartmentUtis;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mAddByDepartmentAdapter.notifyDataSetChanged();
        };
    };

    private AddByDepartmentAdapter mAddByDepartmentAdapter;
    private BSRefreshListView mRefreshListView;
    private TextView mTotalTxt, mSortTxt;
    private int mStatus = 0;
    public WheelMain wheelMain;
    private RelativeLayout mLayout;

    private BSPopupWindowsTitle mBsPopupWindowsTitle;
    private String mParentItem[] = {
            "请假", "物资", "加班", "费用", "考勤"
    };
    private String mChildItme[][] = {
            new String[] {
                    "事假", "病假", "（陪）产假", "公休假", "调休假", "婚假", "丧假"
            },
            new String[] {

            },
            new String[] {

            },
            new String[] {
                    "借支", "采购", "公关", "差旅", "宣传类", "管理类"
            },
            new String[] {
                    "缺卡单", "缺日志"
            }
    };
    private TextView mListTitleTv;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.statistics_approval_detail, null);
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
        mTitleTv.setText("审批统计");
        mLayout = (RelativeLayout) findViewById(R.id.total_layout);
        mTotalTxt = (TextView) findViewById(R.id.total_txt);
        mSortTxt = (TextView) findViewById(R.id.sort_txt);
        mRefreshListView = (BSRefreshListView) findViewById(R.id.listview);
        mStatisticsApprovalAdapter = new StatisticsSuppliesDetailAdapter(this);
        mStatisticsLeaveDetailAdapter = new StatisticsLeaveDetailAdapter(this);
        mRefreshListView.setAdapter(mStatisticsLeaveDetailAdapter);
        initTitle();
        mTitleName01.setTextColor(Color.parseColor("#00A9FE"));
        mTitleName02.setTextColor(Color.parseColor("#00A9FE"));
        mTitleName03.setTextColor(Color.parseColor("#00A9FE"));
        mTitleName01.setTextColor(Color.parseColor("#00A9FE"));
        mTitleName02.setTextColor(Color.parseColor("#00A9FE"));
        mTitleName03.setTextColor(Color.parseColor("#00A9FE"));
        mSelectThree.setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
        mSelectTwo.setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
        mSelectOne.setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
        this.findViewById(R.id.three_title).setVisibility(View.GONE);
        mListTitleTv = (TextView) findViewById(R.id.list_title_tv);

        initData();
    }

    @Override
    public void bindViewsListener() {
        mTitle01.setOnClickListener(this);
        mTitle02.setOnClickListener(this);
        mTitle03.setOnClickListener(this);
        mRefreshListView.setOnItemClickListener(this);
    }

    public void initData() {
        Intent intent = this.getIntent();
        mClass = intent.getStringExtra("class");
        if (mClass == null)
            mClass = "1";
        mType = intent.getStringExtra("type");
        if (mType == null) {
            mType = "";
        }

        if (intent.getStringExtra("d") != null) {
            mDatetype = intent.getStringExtra("d");
        }
        if (intent.getStringExtra("did") != null) {
            mDept = intent.getStringExtra("did");
        }

        if (intent.getStringExtra("bossIndex") != null) {

            this.findViewById(R.id.three_title).setVisibility(View.GONE);
            mLayout.setVisibility(View.GONE);
            mTitleTv.setText(intent.getStringExtra("type_name"));
            mStatisticsLeaveDetailAdapter.setShowDepart(true);
        }
        mStatisticsApprovalAdapter.setType(Integer.parseInt(mClass));
        mStatisticsLeaveDetailAdapter.setType(Integer.parseInt(mClass));

        mTitleName01.setText(intent.getStringExtra("type_name"));
        mTitleName02.setText(intent.getStringExtra("dname"));
        mTitleName03.setText(mDatetype);

        mTitleTv.setText(intent.getStringExtra("type_name"));

        mListTitleTv.setText("部门" + intent.getStringExtra("type_name") + "排行");
    }

    public void initTitle() {
        mTitle01 = (LinearLayout) findViewById(R.id.title01);
        mDivider02 = findViewById(R.id.devider_02);
        mTitle01.setVisibility(View.VISIBLE);
        mDivider02.setVisibility(View.VISIBLE);
        mTitle02 = (LinearLayout) findViewById(R.id.title02);
        mTitle03 = (LinearLayout) findViewById(R.id.title03);
        mTitleLayout = (LinearLayout) findViewById(R.id.title_layout);
        mSelectOne = (ImageView) findViewById(R.id.select_icon01);
        mSelectTwo = (ImageView) findViewById(R.id.select_icon02);
        mSelectThree = (ImageView) findViewById(R.id.select_icon03);

        mTitleName01 = (TextView) findViewById(R.id.title_name_01);
        mTitleName02 = (TextView) findViewById(R.id.title_name_02);
        mTitleName03 = (TextView) findViewById(R.id.title_name_03);
        mTitleName01.setText("全部审批");
        mTitleName02.setText("全部类型");
        mTitleName03.setText("全部状态");

        mAddByDepartmentAdapter = new AddByDepartmentAdapter(this, this, R.layout.item_contacts_department_tree_view, false, true);
        mDepartmentUtis = new DepartmentMoreUtis(this, ResultVO.getInstance(), mHandler, false, true, false);
        mAddByDepartmentAdapter.mfilelist = mDepartmentUtis.getPdfOutlinesCount();

        mPop = new BSPopupWindwos(this, mTitleLayout);
    }

    public boolean getData() {
        try {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("class", mClass);
            map.put("type", mType);
            map.put("d", mDatetype);
            map.put("did", mDept);
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() +
                    Constant.STATISTICS_APPROVAL_DETAIL, map);
            Gson gson = new Gson();
            mStatisticsApprovalVO = gson.fromJson(jsonStr, StatisticsApprovalVO.class);
            // StatisticsAttendanceVO vo = gson.fromJson(jsonStr, StatisticsAttendanceVO.class);
            if (Constant.RESULT_CODE.equals(mStatisticsApprovalVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    class TwoTreeAdapter extends BaseExpandableListAdapter {
        private Context mContext;
        private List<String> groupArray;// 组列表
        private List<List<String>> childArray;// 子列表
        ItemOnClickCallback mCallback;

        public TwoTreeAdapter(Context context, List<String> groupArray, List<List<String>> childArray)
        {
            this.mContext = context;
            this.groupArray = groupArray;
            this.childArray = childArray;
        }

        /*-----------------Child */
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childArray.get(groupPosition).get(childPosition);
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.tow_tree_child_itme, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.child_name);
                holder.childLayout = (LinearLayout) convertView.findViewById(R.id.child_layout);

                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }
            holder.tvName.setText(childArray.get(groupPosition).get(childPosition));
            if ("".equals(childArray.get(groupPosition).get(childPosition))) {
                holder.tvName.setVisibility(View.GONE);
                holder.childLayout.setVisibility(View.GONE);
            }
            holder.tvName.setOnClickListener(new ItemOnclick(groupPosition, childPosition, holder.tvName.getText().toString()));
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childArray.get(groupPosition).size();
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
        public View getGroupView(final int groupPosition, boolean isExpanded,
                View convertView, ViewGroup parent) {

            ParentHolder holder = null;
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.tow_tree_parent_item, null);
                holder = new ParentHolder();
                holder.parentName = (TextView) convertView.findViewById(R.id.parent_name);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            } else {
                holder = (ParentHolder) convertView.getTag();
            }
            holder.parentName.setText(groupArray.get(groupPosition));

            int childCount = getChildrenCount(groupPosition);
            if (childCount == 1) {
                holder.icon.setVisibility(View.INVISIBLE);
            } else {
                holder.icon.setVisibility(View.VISIBLE);
            }
            if (isExpanded) {
                holder.icon.setImageResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
            } else {
                holder.icon.setImageResource(R.drawable.ic_contacts_department_fragment_arrow_default);
            }

            holder.parentName.setOnClickListener(new ItemOnclick(groupPosition, 0, holder.parentName.getText().toString()));
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition)
        {
            return true;
        }

        private TextView getGenericView(String string)
        {
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

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
            private String itemName;
            private int groupPosition, childPosition;

            public ItemOnclick(int groupPosition, int childPosition, String itemName) {
                this.groupPosition = groupPosition;
                this.childPosition = childPosition;
                this.itemName = itemName;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.child_name:
                        if (groupPosition == 3) {
                            if (childPosition == 0) {
                                match(3, (groupPosition + 1) + "," + 7);
                            } else if (childPosition == 1 || childPosition == 2) {
                                match(3, (groupPosition + 1) + "," + childPosition);
                            } else {
                                match(3, (groupPosition + 1) + "," + (childPosition + 1));
                            }
                        } else {
                            match(3, (groupPosition + 1) + "," + (childPosition + 1));
                        }

                        mTitleName01.setText(itemName);
                        mPop.dismiss();
                        break;
                    case R.id.parent_name:
                        match(2, (groupPosition + 1) + "");
                        mTitleName01.setText(itemName);

                        mPop.dismiss();
                        break;
                    default:
                        break;
                }
            }

        }

    }

    private class BSPopupWindwos extends PopupWindow implements OnClickListener {
        private Context mContext;
        private Activity mActivity;
        private ExpandableListView mExpandableListView;
        private ListView mListView;

        private LinearLayout mTitleLayout01, mTitleLayout02, mTitleLayout03;
        private Button mOkBt;
        private int mType;
        private TextView textViw01, textViw02, textViw03, textViw04, textViw05, textViw06, textViw07, textViw08, textViw09;
        private boolean mOne = true;

        private List<String> groupArray;// 组列表
        private List<List<String>> childArray;// 子列表

        private Button mBt;

        public BSPopupWindwos(Context context, View parent) {
            this.mContext = context;
            this.mActivity = (Activity) context;
            View view = View.inflate(context, R.layout.approval_pop_item_03, null);
            view.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {

                    dismiss();
                    return false;
                }
            });
            mTitleLayout01 = (LinearLayout) view.findViewById(R.id.title_layout_01);
            mTitleLayout02 = (LinearLayout) view.findViewById(R.id.title_layout_02);
            mTitleLayout03 = (LinearLayout) view.findViewById(R.id.title_layout_03);

            mExpandableListView = (ExpandableListView) view.findViewById(R.id.list);
            mListView = (ListView) view.findViewById(R.id.lv_department);
            mTitleLayout03.addView(initDateView());

            groupArray = new ArrayList<String>();
            childArray = new ArrayList<List<String>>();
            initPopData();
            mExpandableListView.setAdapter(new TwoTreeAdapter(context, groupArray, childArray));
            mExpandableListView.setGroupIndicator(null);

            mListView.setAdapter(mAddByDepartmentAdapter);

            mOkBt = (Button) view.findViewById(R.id.ok_bt);
            mOkBt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mStatus == 1) {

                        List<PDFOutlineElementVO> list = new ArrayList<PDFOutlineElementVO>();
                        for (int i = 0; i < mAddByDepartmentAdapter.mfilelist.size(); i++) {
                            if (mAddByDepartmentAdapter.mfilelist.get(i).isSelect()) {
                                list.add(mAddByDepartmentAdapter.mfilelist.get(i));
                            }
                        }
                        String did = "";
                        if (list.size() == 1) {
                            did = list.get(0).getDepartmentandwmployee().getDepartmentid();
                            mTitleName02.setText(list.get(0).getDepartmentandwmployee().getDname());
                        } else if (list.size() == 2) {
                            did = list.get(1).getDepartmentandwmployee().getDepartmentid();
                            mTitleName02.setText(list.get(1).getDepartmentandwmployee().getDname());
                        } else if (list.size() > 2) {
                            did = list.get(0).getDepartmentandwmployee().getDepartmentid();
                            mTitleName02.setText(list.get(0).getDepartmentandwmployee().getDname());

                        }
                        match(4, did);

                    } else {
                        String date = wheelMain.getTime().substring(0, wheelMain.getTime().lastIndexOf("-"));
                        mTitleName03.setText(date);
                        match(1, date);
                    }
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

            if (type == 1) {
                mTitleLayout01.setVisibility(View.VISIBLE);
                mTitleLayout02.setVisibility(View.GONE);
                mTitleLayout03.setVisibility(View.GONE);
                mOkBt.setVisibility(View.GONE);
            } else if (type == 2) {

                mTitleLayout02.setVisibility(View.VISIBLE);
                mTitleLayout03.setVisibility(View.GONE);
                mTitleLayout01.setVisibility(View.GONE);
                mOkBt.setVisibility(View.VISIBLE);
            } else {
                mTitleLayout03.setVisibility(View.VISIBLE);
                mTitleLayout02.setVisibility(View.GONE);
                mTitleLayout01.setVisibility(View.GONE);
                mOkBt.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.text_05:
                    match(1, "1");
                    mTitleName03.setText(textViw05.getText());
                    dismiss();
                    break;
                case R.id.text_06:
                    match(1, "2");
                    mTitleName03.setText(textViw06.getText());
                    dismiss();
                    break;
                case R.id.text_07:
                    match(1, "3");
                    mTitleName03.setText(textViw07.getText());
                    dismiss();
                    break;
                case R.id.text_08:
                    match(1, "4");
                    mTitleName03.setText(textViw08.getText());
                    dismiss();
                    break;

                default:
                    break;
            }

        }

        private void initPopData()
        {
            addInfo("请假", new String[] {
                    "事假", "病假", "（陪）产假", "公休假", "调休假", "婚假", "丧假"
            });
            addInfo("物资", new String[] {
                    ""
            });
            addInfo("加班", new String[] {
                    ""
            });
            addInfo("费用申请", new String[] {
                    "借支", "采购", "公关", "差旅", "宣传类", "管理类"
            });
            addInfo("考勤申诉", new String[] {
                    "缺卡单", "缺卡日期"
            });
        }

        private void addInfo(String group, String[] child) {

            groupArray.add(group);

            List<String> childItem = new ArrayList<String>();

            for (int index = 0; index < child.length; index++)
            {
                childItem.add(child[index]);
            }
            childArray.add(childItem);
        }
    }

    public void match(int key, String value) {
        switch (key) {
            case 0:
                break;
            case 1:
                mDatetype = value;
                break;
            case 2:
                mClass = value;
                mType = "";
                break;
            case 3:
                mClass = value.split(",")[0];
                mType = value.split(",")[1];
                break;
            case 4:
                mDept = value;
                break;

            default:
                break;
        }
        mStatisticsApprovalAdapter.setType(Integer.parseInt(mClass));
        mStatisticsLeaveDetailAdapter.setType(Integer.parseInt(mClass));
        mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(this, this).start();

    }

    @Override
    public void employeeOnclick(int arg2, int viewType, PDFOutlineElementVO vo) {

        mDepartmentUtis.employeeOnclick(arg2, viewType);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title01:
                // mPop.currentView(this, 1);
                // if (!mPop.isShowing()) {
                // mPop.showAsDropDown(mTitleLayout, mTitleLayout.getLayoutParams().width / 2, 0);
                // } else {
                // mPop.dismiss();
                // }

                initPopDowns(mTitleLayout);
                break;
            case R.id.title02:
                mPop.currentView(this, 2);
                if (!mPop.isShowing()) {
                    mPop.showAsDropDown(mTitleLayout, mTitleLayout.getLayoutParams().width / 2, 0);
                } else {
                    mPop.dismiss();
                }

                mStatus = 1;
                break;
            case R.id.title03:
                mPop.currentView(this, 3);
                if (!mPop.isShowing()) {
                    mPop.showAsDropDown(mTitleLayout, mTitleLayout.getLayoutParams().width / 2, 0);
                } else {
                    mPop.dismiss();
                }
                mStatus = 2;
                break;

            default:
                break;

        }
    }

    @Override
    public void executeSuccess() {
        mStatisticsLeaveDetailAdapter.mList.clear();
        mStatisticsApprovalAdapter.mList.clear();

        mRefreshListView.onRefreshComplete();
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);

        StatisticsApprovalVO vo = mStatisticsApprovalVO.getArray();

        if ("1".equals(vo.getContrast())) {
            mSortTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.statistics_up), null);
        } else if ("2".equals(vo.getContrast())) {
            mSortTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.statistics_same), null);

        } else if ("3".equals(vo.getContrast())) {
            mSortTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.statistics_down), null);
        }
        mSortTxt.setCompoundDrawablePadding(CommonUtils.dip2px(this, 5));

        mSortTxt.setCompoundDrawablePadding(10);
        if ("1".equals(mClass) || "3".equals(mClass) || "5".equals(mClass)) {
            mRefreshListView.setAdapter(mStatisticsLeaveDetailAdapter);
            mStatisticsLeaveDetailAdapter.updateData(vo.getDetails());
        } else {
            mRefreshListView.setAdapter(mStatisticsApprovalAdapter);
            mStatisticsApprovalAdapter.updateData(vo.getDetails());
        }
    }

    @Override
    public void executeFailure() {
        mLayout.setVisibility(View.GONE);
        mRefreshListView.onRefreshComplete();
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mListTitleTv.setVisibility(View.GONE);

        List<StatisticsApprovalVO> list = new ArrayList<StatisticsApprovalVO>();
        if ("1".equals(mClass) || "3".equals(mClass) || "5".equals(mClass)) {
            mRefreshListView.setAdapter(mStatisticsLeaveDetailAdapter);
            mStatisticsLeaveDetailAdapter.updateData(list);
        } else {
            mRefreshListView.setAdapter(mStatisticsApprovalAdapter);
            mStatisticsApprovalAdapter.updateData(list);
        }
    }

    public View initDateView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(this);
        wheelMain = new WheelMain(timepickerview, false, false);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, 0, 0, 0);
        return timepickerview;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        String findUid = "";
        Intent intent = new Intent();
        if (!"2".equals(mClass) && !"4".equals(mClass)) {
            if (mStatisticsLeaveDetailAdapter.mList.size() <= arg3)
                return;
            if (mStatisticsLeaveDetailAdapter.mList.get((int) arg3).getUid() != null) {
                findUid = mStatisticsLeaveDetailAdapter.mList.get((int) arg3).getUid();
            } else {
                findUid = mStatisticsLeaveDetailAdapter.mList.get((int) arg3).getUserid();
            }
            intent.putExtra("isall", "1");
            intent.putExtra("modeid", "0");
            intent.putExtra("bigtypeid", mClass);
            intent.putExtra("date", mDatetype);
            intent.putExtra("smalltypeid", mType);
            intent.putExtra("findUid", findUid);
            intent.putExtra("userid", BSApplication.getInstance().getUserId());
            intent.setClass(this, ApprovalViewActivity.class);
            this.startActivity(intent);
        } else {
            if (mStatisticsApprovalAdapter.mList.size() <= arg3)
                return;
            if (mStatisticsApprovalAdapter.mList.get((int) arg3).getUid() != null) {
                findUid = mStatisticsApprovalAdapter.mList.get((int) arg3).getUid();
            } else {
                findUid = mStatisticsApprovalAdapter.mList.get((int) arg3).getUserid();
            }

            intent.putExtra("alid", mStatisticsApprovalAdapter.mList.get((int) arg3).getId());
            intent.putExtra("uid", BSApplication.getInstance().getUserId());
            if ("2".equals(mClass)) {
                intent.setClass(this, ApprovalSuppliesDetailActivity.class);
            } else if ("4".equals(mClass)) {
                intent.setClass(this, ApprovalFeeApplyDetailActivity.class);
            }
            this.startActivity(intent);
        }

    }

    private void initPopDowns(View view)
    {

        ArrayList<TreeVO> list = CommonUtils.getTreeVOList(mParentItem, mChildItme);

        mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list, callback);
        mBsPopupWindowsTitle.showPopupWindow(view);
    }

    TreeCallBack callback = new TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {

            if (vo.getLevel() == 1) {
                // 审批一级菜单
                match(2, vo.getParentSerachId() + "");
                mTitleName01.setText(vo.getName());

            } else {

                if ("全部".equals(vo.getName())) {
                    match(2, vo.getParentId() + "");
                    mTitleName01.setText(vo.getName());
                    return;
                } else {
                    mTitleName01.setText(vo.getName());
                    // 审批自定义审批菜单
                }

                if (vo.getParentId() == 4) {
                    if (Integer.parseInt(vo.getChildSearchId()) == 1) {
                        match(3, vo.getParentId() + "," + 7);
                    } else if (Integer.parseInt(vo.getChildSearchId()) == 2 || Integer.parseInt(vo.getChildSearchId()) == 3) {
                        match(3, vo.getParentId() + "," + (Integer.parseInt(vo.getChildSearchId()) - 1));
                    } else {
                        match(3, vo.getParentId() + "," + vo.getChildSearchId());
                    }
                } else {
                    match(3, vo.getParentId() + "," + vo.getChildSearchId());
                }
                mTitleName01.setText(vo.getName());
            }
        }

    };
}
