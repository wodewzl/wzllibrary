
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.ImagePreviewActivity;
import com.bs.bsims.adapter.ApprovlaNewIdeaAdapter;
import com.bs.bsims.adapter.CrmVisitorImgSelectAdapter;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.model.CrmTranctVos;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class CrmTradeFragment1 extends BaseFragment {
    private CrmTranctVos crmTranctVo;
    private TextView trandeStartTime, trandeEndTime, bussiness, signingDate, saleBack, tradeType, signingPerson, remark,
            bussniesPersonName1, personDpname1, bussniesPersonName2, personDpname2, time, head_momtv, addtime, signtime;
    private BSCircleImageView headpic1, headpic2, headpic3;
    private GridView send_second_person_gv;
    private LinearLayout send_ly;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private HeadAdapter Ccadapter;
    private Context context;
    private BSGridView gridView;
    private CrmVisitorImgSelectAdapter crmVimgsapdater;
    private TextView mApprovalName, mApprovalTime, mApprovalDpName;

    private ListView mListView;
    private ApprovlaNewIdeaAdapter mApprovlaIdeaAdapter;
    private TextView mApprovalIdeaTv;

    public CrmTradeFragment1(Context context, CrmTranctVos crmVo) {
        this.context = context;
        this.crmTranctVo = crmVo;
        imageLoader = ImageLoader.getInstance();
        options = CommonUtils.initImageLoaderOptions();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.crmtradecontant_main_detail_info_view, container, false);
        initViews(view);
        updateData();
        bindViewListener();
        return view;
    }

    private void initViews(View view) {
        trandeStartTime = (TextView) view.findViewById(R.id.trande_starttime);// 开始日期
        trandeEndTime = (TextView) view.findViewById(R.id.trande_endtime);// 结束日期
        bussiness = (TextView) view.findViewById(R.id.bussiness);// 商机
        signingDate = (TextView) view.findViewById(R.id.signing_date);// 签约日期
        saleBack = (TextView) view.findViewById(R.id.sale_back);// 销售返点
        tradeType = (TextView) view.findViewById(R.id.trade_type);// 权限
        signingPerson = (TextView) view.findViewById(R.id.signing_person);// 签约人
        remark = (TextView) view.findViewById(R.id.remark);// 备注
        headpic1 = (BSCircleImageView) view.findViewById(R.id.basicinfoheadpic1);// 头像
        bussniesPersonName1 = (TextView) view.findViewById(R.id.bussnies_person_name1);// 名称
        personDpname1 = (TextView) view.findViewById(R.id.bussnies_person_dpname1);// 职位
        headpic2 = (BSCircleImageView) view.findViewById(R.id.basicinfoheadpic2);// 头像
        bussniesPersonName2 = (TextView) view.findViewById(R.id.bussnies_person_name2);// 名称
        personDpname2 = (TextView) view.findViewById(R.id.bussnies_person_dpname2);// 职位
        time = (TextView) view.findViewById(R.id.bussnies_person_time);// 创建日期
        send_second_person_gv = (GridView) view.findViewById(R.id.send_second_person_gv);// 相关人头像列表
        head_momtv = (TextView) view.findViewById(R.id.head_momtv);
        send_ly = (LinearLayout) view.findViewById(R.id.send_ly);
        gridView = (BSGridView) view.findViewById(R.id.show_images);// 备注下面图片
        crmVimgsapdater = new CrmVisitorImgSelectAdapter(context);
        gridView.setAdapter(crmVimgsapdater);
        addtime = (TextView) view.findViewById(R.id.add_time);
        signtime = (TextView) view.findViewById(R.id.sign_time);
    

        // 2.0合同详情 改成跟审批一样 添加催一下
        mListView = (ListView) view.findViewById(R.id.list_view);
        mApprovlaIdeaAdapter = new ApprovlaNewIdeaAdapter(context);
        mListView.setAdapter(mApprovlaIdeaAdapter);
        mApprovalIdeaTv = (TextView) view.findViewById(R.id.approval_idea_tv);
    }

    private void updateData() {
        trandeStartTime.setText(DateUtils.parseDateDay(crmTranctVo.getStarttime()));
        trandeEndTime.setText(DateUtils.parseDateDay(crmTranctVo.getEndtime()));
        signingDate.setText(DateUtils.parseDateDay(crmTranctVo.getSigning_date()));
        tradeType.setText(crmTranctVo.getType());
        saleBack.setText("￥" + crmTranctVo.getDiscount());
        // isPublic.setText(crmTranctVo.getOpen());
        if (!crmTranctVo.getBname().equals("暂无")) {
            bussiness.setText(crmTranctVo.getBname());
        }
        if (!crmTranctVo.getClient_side().equals("暂无")) {
            signingPerson.setText(crmTranctVo.getClient_side());
        }
        if (!crmTranctVo.getRemark().equals("暂无")) {
            remark.setText(crmTranctVo.getRemark());
        }

        // time.setText("创建日期：" + DateUtils.parseDateDay(crmTranctVo.getAddtime()));
        // 合同负责人
        bussniesPersonName2.setText(crmTranctVo.getFullname());
        personDpname2.setText(crmTranctVo.getDname() + "/" + crmTranctVo.getPname());
        imageLoader.displayImage(crmTranctVo.getHeadpic(), headpic2, options);
        headpic2.setUserId(crmTranctVo.getUserid());// HL:获取头像对应的用户ID，以便响应跳转
        headpic2.setUrl(crmTranctVo.getHeadpic());
        headpic2.setUserName(crmTranctVo.getFullname());
        addtime.setText("创建日期：" + DateUtils.parseDateDay(crmTranctVo.getAddtime()));

        // 我方签约人
        CrmTranctVos mySideVo = crmTranctVo.getMy_side();
        bussniesPersonName1.setText(mySideVo.getFullname());
        personDpname1.setText(mySideVo.getDname() + "/" + mySideVo.getPname());
        imageLoader.displayImage(mySideVo.getHeadpic(), headpic1, options);
        headpic1.setUserId(mySideVo.getUserid());// HL:获取头像对应的用户ID，以便响应跳转
        headpic1.setUrl(crmTranctVo.getHeadpic());
        headpic1.setUserName(crmTranctVo.getFullname());
        signtime.setText("创建日期：" + DateUtils.parseDateDay(crmTranctVo.getSigning_date()));
        //
        // //合同审批人
        //
        // CrmTranctVos approvalPerson = crmTranctVo.getApprover();
        // imageLoader.displayImage(approvalPerson.getHeadpic(), headpic3, options);
        // mApprovalName.setText(approvalPerson.getFullname());
        // mApprovalTime.setText("审批时间:"+DateUtils.parseDateDay(crmTranctVo.getEndtime()));
        // mApprovalDpName.setText(approvalPerson.getDname()+"/"+approvalPerson.getPname());

        if (crmTranctVo.getAppUser() != null) {
            if (crmTranctVo.getOpinion() != null) {
                mApprovalIdeaTv.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.VISIBLE);
                List<EmployeeVO> appUserList = new ArrayList<EmployeeVO>();
                appUserList.addAll(crmTranctVo.getAppUser());
                for (int i = 0; i < crmTranctVo.getAppUser().size(); i++) {
                    for (int j = 0; j < crmTranctVo.getOpinion().size(); j++) {
                        if (crmTranctVo.getOpinion().get(j).getUserid().equals(crmTranctVo.getAppUser().get(i).getUserid())) {
                            appUserList.remove(i);
                            appUserList.add(i, crmTranctVo.getOpinion().get(j));
                            continue;
                        }
                    }
                }
                mApprovlaIdeaAdapter.updateData(appUserList);
                mApprovalIdeaTv.setVisibility(View.VISIBLE);
 
            }
            else{
                mApprovlaIdeaAdapter.updateData(crmTranctVo.getAppUser());
                mApprovalIdeaTv.setVisibility(View.VISIBLE);
            }

        }
        else {
            mApprovalIdeaTv.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
        }

        if (null != crmTranctVo.getInsUser())
            getFromUserToKeyset();
        else {
            head_momtv.setVisibility(View.GONE);
            send_ly.setVisibility(View.GONE);
        }

        // 展现备注下面的图片
        if (crmTranctVo.getImgs() != null) {
            crmVimgsapdater.updateList(crmTranctVo.getImgs());
        }

    }

    // 封装相关人的方法
    public void getFromUserToKeyset() {
        send_ly.setVisibility(View.VISIBLE);
        head_momtv.setVisibility(View.VISIBLE);
        Ccadapter = new HeadAdapter(context, false, true);
        Ccadapter.updateData(crmTranctVo.getInsUser());
        send_second_person_gv.setAdapter(Ccadapter);
        Ccadapter.notifyDataSetChanged();
    }

    private void bindViewListener() {
        // 点击图片，调到另一个浏览
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                // intent.putStringArrayListExtra("piclist", (ArrayList<String>) strlist);
                intent.putStringArrayListExtra("piclist", (ArrayList<String>) crmTranctVo.getImgs());
                intent.setClass(context, ImagePreviewActivity.class);
                intent.putExtra("imgIndex", arg2);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public String getFragmentName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            updateData();
        }
    }

    public CrmTranctVos getCrmTranctVo() {
        return crmTranctVo;
    }

    // 根据数据，更新界面内容
    public void setCrmTranctVo(CrmTranctVos crmTranctVo) {
        this.crmTranctVo = crmTranctVo;
        updateData();
    }

}
