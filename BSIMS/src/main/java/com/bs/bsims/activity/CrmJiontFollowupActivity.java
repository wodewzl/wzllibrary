
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmBussinesListindexVo;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSGridView;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;
import org.xutils.ex.HttpException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrmJiontFollowupActivity extends BaseActivity implements OnClickListener {

    private BSGridView mApproverGv, inform_gv;
    private HeadAdapter mApproverAdapter;
    private ImageLoader imageloader;
    private TextView txt_taskevent_releasetask_fuzeren;
    private ImageView bussines_ze;
    private BSCircleImageView image_taskevent_releasetask_fuzeren,
            image_taskevent_releasetask_fuzeren_change;

    private LinearLayout fuzeren_father_manger;
    /** 相关人的集合 */
    private List<EmployeeVO> mDataList = new ArrayList<EmployeeVO>();

    private String personid;// 负责人id
    private StringBuffer follow = new StringBuffer();// 联合跟进人id

    // 因为联合跟进人和相关人是一样的，根据这个key来判断点击到底事哪一个
    private String requesKey = "";
    private Context mContext;

    private String bid = "";
    private CrmBussinesListindexVo crmBussineLivo;

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.crm_jiont_followup, null);
        mContentLayout.addView(layout);
        imageloader = ImageLoader.getInstance();
        mContext = this;
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void initView() {

        mOkTv.setText("确定");
        mTitleTv.setText("联合跟进人");
        // 传过来的商机的id
        bid = getIntent().getStringExtra("bid");
        // 责任人
        fuzeren_father_manger = (LinearLayout) findViewById(R.id.fuzeren_father_manger);
        bussines_ze = (ImageView) findViewById(R.id.bussines_ze);
        image_taskevent_releasetask_fuzeren = (BSCircleImageView) findViewById(R.id.image_taskevent_releasetask_fuzeren);
        txt_taskevent_releasetask_fuzeren = (TextView) findViewById(R.id.txt_taskevent_releasetask_fuzeren);
        // 点击更换负责人
        image_taskevent_releasetask_fuzeren_change = (BSCircleImageView) findViewById(R.id.image_taskevent_releasetask_fuzeren_change);

        if (null != getIntent().getSerializableExtra("evo")) {
            crmBussineLivo = (CrmBussinesListindexVo) getIntent().getSerializableExtra("evo");
        }
        else {
            crmBussineLivo = new CrmBussinesListindexVo();
        }

        // 在这里要做判断当接口给我有负责的值话就显示，否则不显示
        if (null != crmBussineLivo.getFullname()) {
            txt_taskevent_releasetask_fuzeren.setText(
                    crmBussineLivo.getFullname());
            imageloader.displayImage(crmBussineLivo.getHeadpic(),
                    image_taskevent_releasetask_fuzeren, CommonUtils.initImageLoaderOptions());
            personid = crmBussineLivo.getUserid();
        }
        else {
            fuzeren_father_manger.setVisibility(View.GONE);
        }

        if (!txt_taskevent_releasetask_fuzeren.getText().toString().trim().equals("添加")) {
            bussines_ze.setVisibility(View.VISIBLE);
        }

      

        // 联合跟进人
        mApproverGv = (BSGridView) findViewById(R.id.approver_gv);
        mApproverAdapter = new HeadAdapter(this, true, "0", "1");
        // 商机需要新的负责人需要传递1(1.25) 7月8号
        if (getIntent().getStringExtra("cid") != null) {
            mApproverAdapter.setIsTrade(null);
            mApproverAdapter.setIsBussines("1");
            mApproverAdapter.setmCids(getIntent().getStringExtra("cid"));
        }
        mApproverGv.setAdapter(mApproverAdapter);
        if (null != crmBussineLivo.getFollow()) {
            mDataList.clear();
            mDataList = crmBussineLivo.getFollow();
            mApproverAdapter.mList.clear();
            mApproverAdapter.mList.addAll(mDataList);
            mApproverAdapter.notifyDataSetChanged();
            follow.setLength(0);// 参数数据
            for (int i = 0; i < mDataList.size(); i++) {
                follow.append(mDataList.get(i).getUserid());
                if (i != mDataList.size() - 1) {
                    follow.append(",");
                }
            }
        }
    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        image_taskevent_releasetask_fuzeren_change.setOnClickListener(this);
        mOkTv.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        switch (arg0) {

        

                /* 负责人 */
            case 2014:
                if (arg1 == 2014) {
                    if (arg2 != null) {
                        /** 采用post请求 */

                        EmployeeVO employee = (EmployeeVO) arg2
                                .getSerializableExtra("approve_activity");
                        if (employee == null) {
                            employee = ((List<EmployeeVO>) arg2
                                    .getSerializableExtra("checkboxlist")).get(0);
                        }
                        if (employee != null) {
                            /** 采用post请求 */
                            imageloader.displayImage(employee.getHeadpic(),
                                    image_taskevent_releasetask_fuzeren,
                                    CommonUtils.initImageLoaderOptions());
                            txt_taskevent_releasetask_fuzeren.setText(employee.getFullname());
                            bussines_ze.setVisibility(View.VISIBLE);
                            personid = employee.getUserid();
                            // 返回的时候要把数据带回去
                            crmBussineLivo.setUserid(personid);
                            crmBussineLivo.setHeadpic(employee.getHeadpic());
                            crmBussineLivo.setDname(employee.getDname());
                            crmBussineLivo.setPname(employee.getPname());
                            crmBussineLivo.setFullname(employee.getFullname());
                        }

                    }
                }
                break;

            /* 联合跟进人 */
            case 10:
                if (arg1 == 0) {
                    if (arg2 != null) {
                        mDataList.clear();
                        mDataList = (List<EmployeeVO>) arg2.getSerializableExtra("checkboxlist");
                        mApproverAdapter.mList.clear();
                        mApproverAdapter.mList.addAll(mDataList);
                        mApproverAdapter.notifyDataSetChanged();
                        // follow.setLength(0);// 参数数据
                        // for (int i = 0; i < mApproverAdapter.mList.size(); i++) {
                        // follow.append( mApproverAdapter.mList.get(i).getUserid());
                        // if (i != mApproverAdapter.mList.size() - 1) {
                        // follow.append(",");
                        // }
                        // }

                    }
                }
                break;

        }

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.image_taskevent_releasetask_fuzeren_change:
                Intent intent1 = new Intent();
                intent1.putExtra("proKey", "0");
                intent1.putExtra("typekey", "6");
                intent1.putExtra("bid", bid);//
                // 商机需要新的负责人需要传递1(1.25) 7月8号
                if (getIntent().getStringExtra("cid") != null) {
                    intent1.putExtra("cid", getIntent().getStringExtra("cid"));
                    intent1.putExtra("isfrom", "1");
                }
                intent1.setClass(CrmJiontFollowupActivity.this, AddByPersonCRMActivity.class);
                intent1.putExtra("requst_number", 2014);
                startActivityForResult(intent1, 2014);
                break;

            case R.id.txt_comm_head_right:
                CustomDialog.showProgressDialog(mContext, "正在提交信息...");
         
                // 在取联合跟进人
                follow.setLength(0);// 参数数据
                for (int i = 0; i < mApproverAdapter.mList.size(); i++) {
                    follow.append(mApproverAdapter.mList.get(i).getUserid());
                    if (i != mApproverAdapter.mList.size() - 1) {
                        follow.append(",");
                    }
                }
                crmBussineLivo.setFollow(mApproverAdapter.mList);
                PostAllPersonInfo();
                break;
            default:
                break;
        }
    }

    /*
     * 提交所有的信息
     */
    public void PostAllPersonInfo() {
        final String url = BSApplication.getInstance().getHttpTitle()
                + Constant.CRM_FOLLOW_UPDATE;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("ftoken", BSApplication.getInstance().getmCompany());
        map.put("userid", BSApplication.getInstance().getUserId());
        map.put("follow", follow.toString());
        map.put("bid", bid);
        map.put("uid", personid);
        new HttpUtilsByPC().sendPostBYPC(url, map,
                /**
                 * @author Administrator
                 */
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        CustomToast.showLongToast(mContext, "更改失败,网络似乎断开了哦");
                        CustomDialog.closeProgressDialog();
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo rstr) {
                        // TODO Auto-generated method stub
                        CustomDialog.closeProgressDialog();
                        // editKey1 = "0";// 假设定 提高体验不刷新数据
                        String str = "";

                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(new String(rstr.result.toString()));
                            str = (String) jsonObject.get("retinfo");
                            String code = (String) jsonObject.get("code");
                            if (code.equals("200")) {
                                Intent i = new Intent();
                                i.putExtra("evo", crmBussineLivo);
                                setResult(2015, i);
                                CrmJiontFollowupActivity.this.finish();
                                return;
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        CustomToast.showLongToast(mContext, str);

                    }

                });
        // Intent intent1 = new Intent(mContext, CrmBusinessHomeListActivity.class);
        // mContext.startActivity(intent1);
    }

}
