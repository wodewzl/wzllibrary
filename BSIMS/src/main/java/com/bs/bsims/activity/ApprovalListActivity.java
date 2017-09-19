
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.TwoTreeAdapterBk.ItemOnClickCallback;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CustomApprovalListVO;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.view.BSExpandableListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApprovalListActivity extends BaseActivity {
    private List<View> mListView;
    private CustomApprovalListVO mCustomApprovalVO;
    private BSExpandableListView mExpandableListView;
    private TwoTreeAdapter mTreeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.approval_publish, null);
        mContentLayout.addView(layout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    public boolean getData() {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("iscustom", "1");
            map.put("isappeal", "1");
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.APPROVAL_PUBLIC_LIST, map);
            Gson gson = new Gson();
            mCustomApprovalVO = gson.fromJson(jsonStr, CustomApprovalListVO.class);
            if (Constant.RESULT_CODE.equals(mCustomApprovalVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void updateUi() {
    }

    @Override
    public void executeSuccess() {
        mLoading.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        mTreeAdapter.updateData(mCustomApprovalVO.getArray());
    }

    @Override
    public void executeFailure() {
        mLoading.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void initView() {
        mTitleTv.setText(R.string.approval_type);
        mExpandableListView = (BSExpandableListView) findViewById(R.id.expandable_listview);
        mTreeAdapter = new TwoTreeAdapter(this);
        mExpandableListView.setAdapter(mTreeAdapter);
        mExpandableListView.setGroupIndicator(null);
        initData();
    }

    public void initData() {
    }

    @Override
    public void bindViewsListener() {

    }

    // 自定义审批列表
    class TwoTreeAdapter extends BaseExpandableListAdapter {
        private Context mContext;
        ItemOnClickCallback mCallback;
        private List<CustomApprovalListVO> mList;

        public TwoTreeAdapter(Context context)
        {
            this.mContext = context;
            mList = new ArrayList<CustomApprovalListVO>();
        }

        /*-----------------Child */
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mList.get(groupPosition).getOption().get(childPosition);
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.custom_child_item, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.child_name);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.attendance_appeals_child_layout);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            final String childId = mList.get(groupPosition).getOption().get(childPosition).getAtid();
            final String childName = mList.get(groupPosition).getOption().get(childPosition).getName();
            holder.tvName.setText(childName);
            final Intent intent = new Intent();
            switch (groupPosition) {
                case 0:
                    intent.setClass(ApprovalListActivity.this, ApprovalLeaveActivity.class);
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    intent.setClass(ApprovalListActivity.this, ApprovalFeeApplyActivity.class);
                    break;
                case 4:
                    intent.setClass(ApprovalListActivity.this, ApprovalAttendanceActivity.class);
                    break;
                case 5:
                    intent.setClass(ApprovalListActivity.this, ApprovalCustomActivity.class);
                    break;
                default:
                    break;
            }

            holder.layout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    intent.putExtra("typeId", childId);
                    intent.putExtra("atid", childId);
                    intent.putExtra("type", childId);
                    intent.putExtra("title", childName);
                    intent.putExtra("approvalType", childName);
                    intent.putExtra("uid", BSApplication.getInstance().getUserFromServerVO().getUserid());

                    if (intent.getComponent() != null)
                        startActivity(intent);
                }
            });
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (mList.get(groupPosition).getOption() == null) {
                return 0;
            } else {
                return mList.get(groupPosition).getOption().size();
            }

        }

        @Override
        public Object getGroup(int groupPosition) {
            return getGroup(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return mList.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, final boolean isExpanded,
                View convertView, ViewGroup parent) {

            ParentHolder holder = null;
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.custom_parent_item, null);
                holder = new ParentHolder();
                holder.parentName = (TextView) convertView.findViewById(R.id.parent_name);
                holder.parentLayout = (LinearLayout) convertView.findViewById(R.id.parent_layout);
                convertView.setTag(holder);
            } else {
                holder = (ParentHolder) convertView.getTag();
            }
            holder.parentName.setText(mList.get(groupPosition).getName());
            final Intent intent = new Intent();
            switch (groupPosition) {
                case 0:
                    holder.parentName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.approval_type1, 0, 0, 0);
                    break;
                case 1:
                    holder.parentName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.approval_type2, 0, 0, 0);
                    holder.parentLayout.setTag("1");
                    intent.setClass(ApprovalListActivity.this, ApprovalSuppliesActivity.class);
                    break;
                case 2:
                    holder.parentName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.approval_type3, 0, 0, 0);
                    intent.setClass(ApprovalListActivity.this, ApprovalOvertimeActivity.class);
                    holder.parentLayout.setTag("2");
                    break;
                case 3:
                    holder.parentName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.approval_type4, 0, 0, 0);
                    break;
                case 4:
                    holder.parentName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.approval_type5, 0, 0, 0);
                    break;

                default:
                    break;
            }

            holder.parentLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (groupPosition == 1 || groupPosition == 2) {
                        intent.putExtra("approvalType", mList.get(groupPosition).getName());
                        intent.putExtra("uid", BSApplication.getInstance().getUserFromServerVO().getUserid());
                        if (intent.getComponent() != null)
                            startActivity(intent);
                    } else {
                        if (isExpanded) {
                            mExpandableListView.collapseGroup(groupPosition);
                        } else {
                            mExpandableListView.expandGroup(groupPosition);
                        }
                    }

                }
            });

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
            LinearLayout parentLayout;
        }

        class ChildHolder {
            TextView tvName;
            LinearLayout layout;
        }

        public void updateData(List<CustomApprovalListVO> list) {
            if (list == null)
                list = new ArrayList<CustomApprovalListVO>();
            mList.clear();
            mList.addAll(list);
            this.notifyDataSetChanged();
        }

    }

}
