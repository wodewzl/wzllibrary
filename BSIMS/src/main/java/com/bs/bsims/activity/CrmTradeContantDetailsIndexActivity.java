
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.ApprovlaNewIdeaAdapter;
import com.bs.bsims.adapter.CrmVisitorImgSelectAdapter;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmTranctVos;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSGridView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 合同详情界面
 */
public class CrmTradeContantDetailsIndexActivity extends BaseActivity implements OnClickListener {
    private Context mContext;
    private String hid;
    private CrmTranctVos mCrmTranctVos;
    private TextView trande_name;
    private TextView trande_money;
    private TextView trande_starttime;
    private TextView trande_endtime;
    private BSCircleImageView basicinfoheadpic, mTradeApprvalIconImg;
    private TextView bussnies_person_name;
    private TextView bussnies_person_dpname;
    private GridView send_second_person_gv;
    private TextView head_momtv;
    private TextView sale_back;
    private TextView signing_date;
    private TextView remark;
    private TextView addtime;

    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private HeadAdapter Ccadapter;

    // 底部审批
    private BSDialog mDialog;
    private Button mApprovalBt, mUnapprovalBt;
    private LinearLayout mBottomLayout;

    private LinearLayout mAddPannedLayout, mPlanLayout;
    private TextView mPaymentPlan;
    private TextView mProduct;
    private LinearLayout mInforLayout;
    private TextView mTradeTypeTv, mTradeApprovalNameTv, mTradApprovalContentTv, mStatus;
    private LinearLayout mApprovalLayout;
    private TextView mApprovalTitle;
    private BSGridView gridView;
    private CrmVisitorImgSelectAdapter crmVimgsapdater;
    private TextView mTitleName;

    private ListView mListView;
    private ApprovlaNewIdeaAdapter mApprovlaIdeaAdapter;
    private TextView mApprovalIdeaTv;

    private int mCurrent = 0;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.crmtradecontant_details, mContentLayout);
        mContext = this;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
        CrmTranctVos vo = mCrmTranctVos.getArray();
        trande_name.setText(vo.getCname());
        // mProduct.setText(vo.getProname());
        mImageLoader.displayImage(vo.getHeadpic(), basicinfoheadpic, mOptions);
        basicinfoheadpic.setUserId(vo.getUserid());// HL:获取合同负责人头像对应的ID
        basicinfoheadpic.setUrl(vo.getHeadpic());
        basicinfoheadpic.setUserName(vo.getFullname());
        trande_money.setText("￥" + vo.getMoney());
        trande_starttime.setText(DateUtils.parseDateDay(vo.getStarttime()));
        trande_endtime.setText(DateUtils.parseDateDay(vo.getEndtime()));
        bussnies_person_name.setText(vo.getFullname());
        bussnies_person_dpname.setText(vo.getDname() + "/" + vo.getPname());
        sale_back.setText("￥" + vo.getDiscount());
        signing_date.setText(DateUtils.parseDateDay(vo.getSigning_date()));
        if (CommonUtils.isNormalString(vo.getRemark()))
            remark.setText(vo.getRemark());
        mTradeTypeTv.setText(vo.getType());
        addtime.setText("创建日期：" + DateUtils.parseDateDay(vo.getAddtime()));

        head_momtv.setVisibility(View.GONE);
        mInforLayout.setVisibility(View.GONE);


        if (vo.getAppUser() != null) {
            if (vo.getOpinion() != null) {
                mApprovalIdeaTv.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.VISIBLE);
                List<EmployeeVO> appUserList = new ArrayList<EmployeeVO>();
                appUserList.addAll(vo.getAppUser());
                for (int i = 0; i < vo.getAppUser().size(); i++) {
                    for (int j = 0; j < vo.getOpinion().size(); j++) {
                        if (vo.getOpinion().get(j).getUserid().equals(vo.getAppUser().get(i).getUserid())) {
                            appUserList.remove(i);
                            appUserList.add(i, vo.getOpinion().get(j));
                            continue;
                        }
                    }
                }
                mApprovlaIdeaAdapter.updateData(appUserList);
                mApprovalIdeaTv.setVisibility(View.VISIBLE);

                // mApprovlaIdeaAdapter.setApprovalType(vo.getGenre());
                mApprovlaIdeaAdapter.setApprovalId(vo.getHid());
            }
            else {
                mApprovlaIdeaAdapter.setApprovalId(vo.getHid());
                mApprovlaIdeaAdapter.updateData(vo.getAppUser());
                mApprovalIdeaTv.setVisibility(View.VISIBLE);
            }

        }
        
        else {
            mApprovalIdeaTv.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
        }
        
        
        if (vo.getUserid().equals(BSApplication.getInstance().getUserId())) {
            mApprovlaIdeaAdapter.setViewCui(false);
        }
        else {
            mApprovlaIdeaAdapter.setViewCui(true);
        }

        if ("1".equals(vo.getCrmApproval())) {
            mBottomLayout.setVisibility(View.VISIBLE);

        } else {
            mBottomLayout.setVisibility(View.GONE);
        }
        mPlanLayout.setVisibility(View.GONE);
        mPaymentPlan.setVisibility(View.GONE);

        // if (vo.getPlanList() != null) {
        // mPlanLayout.setVisibility(View.VISIBLE);
        // mPaymentPlan.setVisibility(View.VISIBLE);
        // for (int i = 0; i < vo.getPlanList().size(); i++) {
        // LinearLayout layout = (LinearLayout) View.inflate(this,
        // R.layout.crm_trade_detail_plan_itme, null);
        // TextView money = (TextView) layout.findViewById(R.id.money);
        // CommonUtils.setDifferentTextColor(money, "回款金额：", "￥ " +
        // vo.getPlanList().get(i).getMoney(), "#ff0000");
        // TextView pannedDate = (TextView) layout.findViewById(R.id.panned_date);
        // pannedDate.setText(DateUtils.parseDateDay(vo.getPlanList().get(i).getPlanned_date()));
        // TextView notifyDate = (TextView) layout.findViewById(R.id.notify_date);
        // notifyDate.setText(DateUtils.parseDateDayAndHour(vo.getPlanList().get(i).getReminder_time()));
        // mPlanLayout.addView(layout);
        // layout.setOnClickListener(new LayoutClickListener(vo.getPlanList().get(i)));
        // }
        // }

        if ("1".equals(vo.getAddPlan())) {
            mAddPannedLayout.setVisibility(View.VISIBLE);
            mApprovalLayout.setVisibility(View.GONE);
            mApprovalTitle.setVisibility(View.GONE);
        } else {
            mAddPannedLayout.setVisibility(View.GONE);
        }

        // 展现备注下面的图片
        if (vo.getImgs() != null) {
            crmVimgsapdater.updateList(vo.getImgs());
        } else {
            gridView.setVisibility(View.GONE);
        }

    }

    class LayoutClickListener implements OnClickListener {
        private CrmTranctVos vo;

        public LayoutClickListener(CrmTranctVos vo) {
            this.vo = vo;
        }

        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent();
            intent.setClass(CrmTradeContantDetailsIndexActivity.this, CrmTradeContantAddPaymentPlanActivity.class);
            intent.putExtra("pid", vo.getPid());
            intent.putExtra("money", vo.getMoney());
            intent.putExtra("planned_date", vo.getPlanned_date());
            intent.putExtra("reminder_time", vo.getReminder_time());
            intent.putExtra("remark", vo.getRemark());
            intent.putExtra("hid", hid);
            intent.putExtra("totalMoney", mCrmTranctVos.getArray().getMoney());
            intent.putExtra("edit", true);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void bindViewsListener() {
        mApprovalBt.setOnClickListener(this);
        mUnapprovalBt.setOnClickListener(this);
        mAddPannedLayout.setOnClickListener(this);

        // 点击图片，调到另一个浏览
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = new Intent();
                // intent.putStringArrayListExtra("piclist", (ArrayList<String>) strlist);
                intent.putStringArrayListExtra("piclist", (ArrayList<String>) mCrmTranctVos.getArray().getImgs());
                intent.setClass(CrmTradeContantDetailsIndexActivity.this, ImagePreviewActivity.class);
                intent.putExtra("imgIndex", arg2);
                startActivity(intent);
            }
        });
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            map.put("hid", hid);
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_RECORDE_DETAILS, map);
            mCrmTranctVos = gson.fromJson(jsonStr, CrmTranctVos.class);
            if (mCrmTranctVos.getCode().equals("200")) {
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

    @Override
    public void initView() {
        hid = getIntent().getStringExtra("hid");
        mTitleTv.setText("合同详情");
        trande_name = (TextView) findViewById(R.id.trande_name);
        trande_money = (TextView) findViewById(R.id.trande_money);
        trande_starttime = (TextView) findViewById(R.id.trande_starttime);
        trande_endtime = (TextView) findViewById(R.id.trande_endtime);
        basicinfoheadpic = (BSCircleImageView) findViewById(R.id.basicinfoheadpic);
        bussnies_person_name = (TextView) findViewById(R.id.bussnies_person_name);
        bussnies_person_dpname = (TextView) findViewById(R.id.bussnies_person_dpname);
        send_second_person_gv = (GridView) findViewById(R.id.send_second_person_gv);
        head_momtv = (TextView) findViewById(R.id.head_momtv);
        sale_back = (TextView) findViewById(R.id.sale_back);
        signing_date = (TextView) findViewById(R.id.signing_date);
        remark = (TextView) findViewById(R.id.remark);
        addtime = (TextView) findViewById(R.id.add_time);

        mBottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        mApprovalBt = (Button) findViewById(R.id.approval_bt);
        mUnapprovalBt = (Button) findViewById(R.id.unapproval_bt);
        mAddPannedLayout = (LinearLayout) findViewById(R.id.add_panned_layout);
        mPlanLayout = (LinearLayout) findViewById(R.id.payment_layout);

        mPaymentPlan = (TextView) findViewById(R.id.payment_plan);
        mProduct = (TextView) findViewById(R.id.product);
        mInforLayout = (LinearLayout) findViewById(R.id.inform_layout);
        mTradeTypeTv = (TextView) findViewById(R.id.trade_type);
        mTradeApprovalNameTv = (TextView) findViewById(R.id.name);
        mTradApprovalContentTv = (TextView) findViewById(R.id.content);
        mTradeApprvalIconImg = (BSCircleImageView) findViewById(R.id.head_icon);
        mStatus = (TextView) findViewById(R.id.status);
        mApprovalLayout = (LinearLayout) findViewById(R.id.approval_layout);
        mApprovalTitle = (TextView) findViewById(R.id.approval_title);

        gridView = (BSGridView) findViewById(R.id.show_images);// 备注下面图片
        crmVimgsapdater = new CrmVisitorImgSelectAdapter(CrmTradeContantDetailsIndexActivity.this);
        gridView.setAdapter(crmVimgsapdater);

        mTitleName = (TextView) findViewById(R.id.title_name);
        mTitleName.setText("审核人：");

        // 2.0合同详情 改成跟审批一样 添加催一下
        mListView = (ListView) findViewById(R.id.list_view);
        mApprovlaIdeaAdapter = new ApprovlaNewIdeaAdapter(this);
        mApprovlaIdeaAdapter.setIsCrm("1");
        mListView.setAdapter(mApprovlaIdeaAdapter);
        mApprovalIdeaTv = (TextView) findViewById(R.id.approval_idea_tv);
    }

    public void getFromUserToKeyset() {
        Ccadapter = new HeadAdapter(this, false, true);
        Ccadapter.updateData(mCrmTranctVos.getArray().getInsUser());
        send_second_person_gv.setAdapter(Ccadapter);
        Ccadapter.notifyDataSetChanged();
    }

    public void showDialog(final String status) {

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_edittext, null);
        final EditText textView = (EditText) v.findViewById(R.id.edit_content);
        mDialog = new BSDialog(this, "请输入内容", v, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                uploadAdopt(textView.getText().toString(), status);
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public void uploadAdopt(String content, final String status) {
        RequestParams params = new RequestParams();

        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("status", status);
            params.put("content", content);
            params.put("hid", hid);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_TRADECONTANT_APPROVAL;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                System.out.println(new String(arg2));

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if ("200".equals(code)) {
                        mBottomLayout.setVisibility(View.GONE);
                        mAddPannedLayout.setVisibility(View.VISIBLE);
                        Intent intent = new Intent();
                        intent.putExtra("status", status);
                        setResult(1, intent);
                        CrmTradeContantDetailsIndexActivity.this.finish();
                    } else {

                    }
                    CustomToast.showShortToast(CrmTradeContantDetailsIndexActivity.this, str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.approval_bt:
                showDialog("1");
                break;
            case R.id.unapproval_bt:
                showDialog("2");
                break;

            case R.id.add_panned_layout:
                Intent intent = new Intent();
                intent.putExtra("hid", hid);
                intent.putExtra("totalMoney", mCrmTranctVos.getArray().getMoney());
                intent.setClass(CrmTradeContantDetailsIndexActivity.this, CrmTradeContantAddPaymentPlanActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent intent) {
        super.onActivityResult(arg0, arg1, intent);
        if (intent != null) {
            new ThreadUtil(this, this).start();
        }
    }
}
