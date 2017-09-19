
package com.bs.bsims.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.ApplicationMarketActivity;
import com.bs.bsims.activity.ApprovalViewActivity;
import com.bs.bsims.activity.AttendanceSummaryActivity;
import com.bs.bsims.activity.CreativeActivity;
import com.bs.bsims.activity.CrmApprovalPaymentActivity;
import com.bs.bsims.activity.CrmBusinessHomeListActivity;
import com.bs.bsims.activity.CrmClientListActivity;
import com.bs.bsims.activity.CrmProductManagementListActivity;
import com.bs.bsims.activity.CrmSalesTargetListActivity;
import com.bs.bsims.activity.CrmStatisticsIndexActivity;
import com.bs.bsims.activity.CrmTradeContantHomeListActivity;
import com.bs.bsims.activity.CrmVisitRecordListActivity;
import com.bs.bsims.activity.DanganIndexoneActivity;
import com.bs.bsims.activity.EXTSharedfilesdGroupHomeActivity;
import com.bs.bsims.activity.EXTSignInMapActivity;
import com.bs.bsims.activity.EXTTaskHomeLAVAActivity;
import com.bs.bsims.activity.GaoDeMapLoactionIndexActivity;
import com.bs.bsims.activity.HumanResoureActivity;
import com.bs.bsims.activity.LocusLineAcitvity;
import com.bs.bsims.activity.NoticeActivity;
import com.bs.bsims.activity.ScheduleActivity;
import com.bs.bsims.activity.SignInActivity;
import com.bs.bsims.activity.StatisticsArticlesActivity;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.MenuVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkManagementFragment extends BaseFragment implements OnClickListener, OnItemClickListener {

    private static final String TAG = "WorkFragment";
    private Activity mActivity;
    private TextView mNoMenu,mMakertLogoTv;
    private LinearLayout mRootLayout;

    public static WorkManagementFragment newInstance() {
        WorkManagementFragment categoryFragment = new WorkManagementFragment();

        return categoryFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_management, container,
                false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initViews(View view) {
        mRootLayout = (LinearLayout) view.findViewById(R.id.root_layout);
        mMakertLogoTv = (TextView) view.findViewById(R.id.txt_comm_head_right);
        mMakertLogoTv.setOnClickListener(this);
        createGroup();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public String getFragmentName() {
        return TAG;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_comm_head_right:
                startActivity(new Intent(mActivity, ApplicationMarketActivity.class));
                break;
        }
        
    }

    private class ImtemOnclick implements OnClickListener {
        private String name;

        public ImtemOnclick(String name) {
            this.name = name;
        }

        @Override
        public void onClick(View arg0) {

            Intent intent = new Intent();
            if (name.contains("Affair")) {
                switch (Integer.parseInt(name.split("Affair")[1])) {
                    case 1:
                        intent.putExtra("type", "2");
                        intent.putExtra("isall", BSApplication.getInstance().getApprovalBoss());
                        intent.setClass(mActivity, ApprovalViewActivity.class);
                        break;
                    // 任务管理
                    case 2:
                        intent.setClass(mActivity, EXTTaskHomeLAVAActivity.class);
                        break;
                    // 创意管理
                    case 3:
                        intent.putExtra("isboss", CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL003));// 0为没有权限，1为有权限
                        intent.putExtra("type", "2");// 1为创意
                        intent.putExtra("isall", "1");
                        intent.putExtra("isadd", "0");
                        intent.setClass(mActivity, CreativeActivity.class);
                        break;
                    // 建议管理
                    case 4:
                        intent.putExtra("isboss", CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL003));
                        intent.putExtra("type", "2");// 1为创意
                        intent.putExtra("isall", "1");
                        intent.putExtra("isadd", "0");
                        intent.setClass(mActivity, CreativeActivity.class);
                        break;
                    // 行政通知
                    case 5:
                        intent.putExtra("noticeid", "2");
                        intent.putExtra("sortid", "3");
                        intent.setClass(mActivity, NoticeActivity.class);
                        break;
                    // 人事通知
                    case 6:
                        intent.putExtra("noticeid", "1");
                        intent.putExtra("sortid", "3");
                        intent.setClass(mActivity, NoticeActivity.class);
                        break;
                    // 考勤管理
                    case 7:
                        intent.setClass(mActivity, AttendanceSummaryActivity.class);
                        break;
                    // 企业动态
                    case 8:
                        intent.putExtra("noticeid", "");
                        intent.putExtra("isshow", "1");
                        intent.putExtra("sortid", "19");
                        intent.setClass(mActivity, NoticeActivity.class);
                        break;
                    // 公文管理
                    case 9:
                        intent.putExtra("noticeid", "");
                        intent.putExtra("isshow", "1");
                        intent.putExtra("sortid", "11");
                        intent.setClass(mActivity, NoticeActivity.class);
                        break;
                    // 员工档案
                    case 10:
                        intent.setClass(mActivity, DanganIndexoneActivity.class);
                        break;
                    // 办公用品
                    case 11:
                        intent.setClass(mActivity, StatisticsArticlesActivity.class);
                        break;
                    // 员工轨迹
                    case 12:
                        if (ResultVO.getInstance() != null) {
                            intent.setClass(mActivity, GaoDeMapLoactionIndexActivity.class);
                        }
                        break;
                    default:
                        break;
                }
            } else if (name.contains("Office")) {
                switch (Integer.parseInt(name.split("Office")[1])) {

                // 上班打卡
                    case 1:
                        intent.setClass(mActivity, EXTSignInMapActivity.class);
                        intent.setClass(mActivity, SignInActivity.class);
                        break;
                    // 通知
                    case 3:
                        intent.putExtra("noticeid", "");
                        intent.putExtra("sortid", "3");
                        intent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                        intent.putExtra("isshow", "1");
                        intent.setClass(mActivity, NoticeActivity.class);
                        break;
                    // 公文
                    case 4:
                        intent.putExtra("noticeid", "");
                        intent.putExtra("sortid", "11");
                        intent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                        intent.putExtra("isshow", "1");
                        intent.setClass(mActivity, NoticeActivity.class);
                        break;
                    // 制度
                    case 5:
                        intent.putExtra("noticeid", "");
                        intent.putExtra("sortid", "12");
                        intent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                        intent.setClass(mActivity, NoticeActivity.class);
                        break;
                    // 企业文化
                    case 6:
                        intent.putExtra("noticeid", "");
                        intent.putExtra("sortid", "19");
                        intent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                        intent.putExtra("isshow", "1");
                        intent.setClass(mActivity, NoticeActivity.class);
                        break;
                    case 7:
                        intent.putExtra("key", "1");
                        intent.setClass(mActivity, EXTSharedfilesdGroupHomeActivity.class);// 云盘
                        break;
                    case 8:
                        intent.setClass(mActivity, ScheduleActivity.class);// 日程
                        break;
                    case 9:
                        intent.setClass(mActivity, HumanResoureActivity.class);// 人力资源
                        break;
                    case 10:
                        intent.setClass(mActivity, CrmTradeContantHomeListActivity.class);// 合同审批列表
                        break;
                    case 11:
                        intent.setClass(mActivity, CrmApprovalPaymentActivity.class);// 回款审批
                        break;

                    default:
                        break;
                }
            } else if (name.contains("Marketing")) {

                switch (Integer.parseInt(name.split("Marketing")[1])) {
                    case 1:
                        intent.setClass(mActivity, CrmVisitRecordListActivity.class);// 跟单记录
                        break;
                    case 2:
                        intent.setClass(mActivity, LocusLineAcitvity.class);// 普通员工CRM签到
                        break;
                    case 3:
                        break;
                    case 4:
                        intent.setClass(mActivity, CrmClientListActivity.class);// 客户
                        break;
                    case 5:
                        intent.setClass(mActivity, CrmBusinessHomeListActivity.class);// 销售机会
                        break;
                    case 6:
                        intent.setClass(mActivity, CrmTradeContantHomeListActivity.class);// 合同
                        break;
                    case 7:
                        intent.setClass(mActivity, CrmSalesTargetListActivity.class);// 销售目标
                        break;
                    case 8:
                        intent.setClass(mActivity, CrmProductManagementListActivity.class);// 产品
                        break;
                    case 9:
                        intent.setClass(mActivity, CrmStatisticsIndexActivity.class);// 仪表盘
                        break;

                    default:
                        break;
                }
            }

            if (intent.getComponent() == null)
                return;
            startActivity(intent);

        }
    }

    public void createGroup() {
        ArrayList<HashMap<String, Object>> item = new ArrayList<HashMap<String, Object>>();

        List<MenuVO> listVo = BSApplication.getInstance().getUserFromServerVO().getMenu();
        for (int i = 0; i < listVo.size(); i++) {
            if ("Special".equals(listVo.get(i).getMalias()) || "Publish".equals(listVo.get(i).getMalias()))
                continue;

            TextView tv = new TextView(mActivity);
            tv.setPadding(CommonUtils.dip2px(mActivity, 15), CommonUtils.dip2px(mActivity, 10), CommonUtils.dip2px(mActivity, 15), CommonUtils.dip2px(mActivity, 10));
            tv.setTextColor(mActivity.getResources().getColor(R.color.C5));
            tv.setBackgroundColor(mActivity.getResources().getColor(R.color.C3));
            tv.setText(listVo.get(i).getMname());
            mRootLayout.addView(tv);
            BSGridView gv = new BSGridView(mActivity);
            gv.setNumColumns(4);
            gv.setVerticalSpacing(CommonUtils.dip2px(mActivity, 30));
            gv.setPadding(0, CommonUtils.dip2px(mActivity, 15), 0, CommonUtils.dip2px(mActivity, 15));
            mRootLayout.addView(gv);

            // 填出数据
            MenuItmeAdapter adapter = new MenuItmeAdapter(mActivity);
            gv.setAdapter(adapter);

            adapter.updateData(listVo.get(i).getMenu());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
    }

    class MenuItmeAdapter extends BaseAdapter {
        private Context context;
        public List<MenuVO> list;

        public MenuItmeAdapter(Context context) {
            this.context = context;
            list = new ArrayList<MenuVO>();
        }

        public void updateDataLast(List<MenuVO> menu) {

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.work_menu_item, null);
                holder.itemImage = (ImageView) convertView.findViewById(R.id.item_image);
                holder.itemName = (TextView) convertView.findViewById(R.id.item_name);
                holder.itemLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            MenuVO vo = list.get(position);
            Class<com.bs.bsims.R.drawable> cls = R.drawable.class;
            try {
                String icon_name = vo.getMalias();
                int imageId = cls.getDeclaredField(icon_name.replaceFirst(icon_name.substring(0, 1), icon_name.substring(0, 1).toLowerCase())).getInt(null);
                holder.itemImage.setImageResource(imageId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.itemName.setText(vo.getMname());
            holder.itemName.setTag(vo.getMalias());
            holder.itemLayout.setOnClickListener(new ImtemOnclick(vo.getMalias()));
            return convertView;
        }

        public void updateData(List<MenuVO> list) {
            this.list.clear();
            this.list.addAll(list);
            this.notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        private ImageView itemImage;
        private TextView itemName;
        private LinearLayout itemLayout;
    }

}
