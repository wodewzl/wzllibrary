
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.model.CrmBussinesListindexVo;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSGridView;
import com.bs.bsims.view.BSListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
// 商机详情
public class CrmBussinesFragment1 extends BaseFragment implements
        OnClickListener {
    private static final String TAG = "CrmBussinesFragment1";
    private CrmBussinesListindexVo crmBussineLivo1;

    public CrmBussinesListindexVo getCrmBussineLivo1() {
        return crmBussineLivo1;
    }

    public void setCrmBussineLivo1(CrmBussinesListindexVo crmBussineLivo1) {
        this.crmBussineLivo1 = crmBussineLivo1;
    }

    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private BSCircleImageView basicinfoheadpic_create;
    private TextView cilent_company_name, cilent_company_money, txt_comm_head_right,
            bussines_canme,
            bussines_createtime, bussines_type, bussines_resoure, bussines_resoure_fromcstomer,
            bussines_pname, bussines_pmoney, bussines_name,
            bussines_lname, bussines_ldname, bussnies_person_time, bussnies_person_name,
            bussnies_person_dpname, head_momtv, pro_father_name;

    private BSListView proListview;
    private CrmBusinessHomeListAdapter chadapter;
    private Activity activity;
    private List<CrmBussinesListindexVo> list;

    // 知会人，审批人
    private BSGridView mApproverGv, mInformGv;
    private HeadAdapter mApproverAdapter, mInformAdapter;
    private TextView mApproverTv, mApprovalGoTv, mInformTv, mInformGoTv, mCancel, mSure;
    private LinearLayout mApproverLayout, mInformLayout;

    public CrmBussinesFragment1() {
    }

    public CrmBussinesFragment1(CrmBussinesListindexVo lvo) {
        this.crmBussineLivo1 = lvo;
    }

    // DanganChuQingInfoLineChartActivity
    @Override
    public String getFragmentName() {
        // TODO Auto-generated method stub
        return TAG;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.crmbussines_index_fragment1, container, false);
        mImageLoader = ImageLoader.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        // TODO Auto-generated method stub

        bussines_createtime = (TextView) view.findViewById(R.id.bussines_createtime);// 鍒涘缓鏃堕棿
        bussines_resoure = (TextView) view.findViewById(R.id.bussines_resoure);// 鍟嗘満鏉ユ簮
        bussines_resoure_fromcstomer = (TextView) view
                .findViewById(R.id.bussines_resoure_fromcstomer);// 瀹㈡埛鏉ユ簮
        basicinfoheadpic_create = (BSCircleImageView) view
                .findViewById(R.id.basicinfoheadpic_create);// 鍒涘缓浜虹殑澶村儚
        bussnies_person_time = (TextView) view.findViewById(R.id.bussnies_person_time);// 鍒涘缓鏃堕棿
        bussnies_person_name = (TextView) view.findViewById(R.id.bussnies_person_name);// 鍒涘缓浜哄鍚�
        bussnies_person_dpname = (TextView) view.findViewById(R.id.bussnies_person_dpname);// 鍒涘缓浜虹殑鑱屼綅
        proListview = (BSListView) view.findViewById(R.id.bussines_pname);
        list = new ArrayList<CrmBussinesListindexVo>();
        chadapter = new CrmBusinessHomeListAdapter(activity, list);
        proListview.setAdapter(chadapter);
        pro_father_name = (TextView) view.findViewById(R.id.pro_father_name);

        /*
         * 2015 12 22修改
         */

        bussines_name = (TextView) view.findViewById(R.id.bussines_name);// 商机名称
        bussines_canme = (TextView) view.findViewById(R.id.bussines_customer_name);// 客户名称
        bussines_pmoney = (TextView) view.findViewById(R.id.bussines_money);// 商机金额
        bussines_type = (TextView) view.findViewById(R.id.bussines_type);// 商机类型
        mApproverGv = (BSGridView) view.findViewById(R.id.approver_gv);
        mInformGv = (BSGridView) view.findViewById(R.id.inform_gv);
        mApproverAdapter = new HeadAdapter(activity, false, true);
        mInformAdapter = new HeadAdapter(activity, false, true);
        mApproverGv.setAdapter(mApproverAdapter);
        mInformGv.setAdapter(mInformAdapter);
        mApproverTv = (TextView) view.findViewById(R.id.approver_tv);
        mInformTv = (TextView) view.findViewById(R.id.inform_people_tv);
        mInformTv.setText("相关人");
        mInformTv.setTextSize(12);
        mInformTv.setTextColor(getResources().getColor(R.color.C6));
        mApproverTv.setText("联合跟进人");
        mApproverTv.setTextSize(12);
        mApproverTv.setTextColor(getResources().getColor(R.color.C6));
        mApproverLayout = (LinearLayout) view.findViewById(R.id.approver_layout);
        mInformLayout = (LinearLayout) view.findViewById(R.id.inform_people_layout);
        updateUI();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initData();
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    public void updateUI() {
        if (null != crmBussineLivo1.getProduct()) {
            pro_father_name.setVisibility(View.VISIBLE);
            chadapter.mList = crmBussineLivo1.getProduct();
            chadapter.notifyDataSetChanged();
        }

        bussines_createtime.setText(DateUtils.parseDateDay(crmBussineLivo1.getExpirationDate()));
        bussines_resoure.setText(crmBussineLivo1.getSource());
        if (CommonUtils.isNormalString(crmBussineLivo1.getRemark())) {
            bussines_resoure_fromcstomer.setText(crmBussineLivo1.getRemark());
        }
        mImageLoader.displayImage(crmBussineLivo1.getHeadpic(), basicinfoheadpic_create,
                CommonUtils.initImageLoaderOptions());
        basicinfoheadpic_create.setUserId(crmBussineLivo1.getUserid());//HL:获取头像对应用户ID，以便实现跳转
        basicinfoheadpic_create.setUserName(crmBussineLivo1.getFullname());
        basicinfoheadpic_create.setUrl(crmBussineLivo1.getHeadpic());
        bussnies_person_time
                .setText("创建日期:" + DateUtils.parseDateDay(crmBussineLivo1.getAddtime()));
        bussnies_person_name.setText(crmBussineLivo1.getFullname());
        bussnies_person_dpname.setText(crmBussineLivo1.getDname() + "/"
                + crmBussineLivo1.getPname());

        /*
         * 2015 12 22修改
         */
        bussines_name.setText(crmBussineLivo1.getBname());
        bussines_canme.setText(crmBussineLivo1.getCname());
        bussines_pmoney.setText("￥" + crmBussineLivo1.getMoney());
        bussines_type.setText(crmBussineLivo1.getStatusName());

        // 相关人
        if (crmBussineLivo1.getInsUser() != null && crmBussineLivo1.getInsUser().size() != 0) {
            mInformTv.setVisibility(View.VISIBLE);
            mInformLayout.setVisibility(View.VISIBLE);
            // mApproverTv.setVisibility(View.GONE);
            // mApproverLayout.setVisibility(View.GONE);
            mInformAdapter.updateData(crmBussineLivo1.getInsUser());
        } else {
            mInformTv.setVisibility(View.GONE);
            mInformLayout.setVisibility(View.GONE);
        }

        // 跟进人
        if (crmBussineLivo1.getFollow() != null && crmBussineLivo1.getFollow().size() != 0) {
            mApproverTv.setVisibility(View.VISIBLE);
            mApproverLayout.setVisibility(View.VISIBLE);
            mApproverAdapter.updateData(crmBussineLivo1.getFollow());
        } else {
            mApproverTv.setVisibility(View.GONE);
            mApproverLayout.setVisibility(View.GONE);
        }

    }

    public class CrmBusinessHomeListAdapter extends BaseAdapter {
        private Context mContext;
        public List<CrmBussinesListindexVo> mList;

        public CrmBusinessHomeListAdapter(Context context,
                List<CrmBussinesListindexVo> list1) {
            this.mContext = context;
            this.mList = list1;
        }

        public CrmBusinessHomeListAdapter(Context context) {
            this.mContext = context;
            this.mList = new ArrayList<CrmBussinesListindexVo>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return mList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(final int postion, View convertView, ViewGroup arg2) {
            final ViewHolder holder;
            // RelativeLayout.LayoutParams layoutParams = new
            // RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            // ViewGroup.LayoutParams.WRAP_CONTENT);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.crm_changeinfo_list, null);
                holder.proname_txt = (TextView) convertView.findViewById(R.id.text_name);
                holder.proname_txt.setCompoundDrawablesWithIntrinsicBounds(getResources()
                        .getDrawable(R.drawable.crm_bussines_detail_prod), null, null, null);
                holder.proname_txt.setCompoundDrawablePadding(30);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.proname_txt.setText(mList.get(postion).getPname());
            return convertView;
        }
    }

    static class ViewHolder {
        private TextView proname_txt;
    }

}
