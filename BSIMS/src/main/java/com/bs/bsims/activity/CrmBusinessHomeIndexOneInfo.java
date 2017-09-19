
package com.bs.bsims.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.fragment.CrmBussinesFragment1;
import com.bs.bsims.fragment.CrmBussinesFragment2;
import com.bs.bsims.fragment.CrmBussinesFragment3;
import com.bs.bsims.model.CrmBussinesListindexVo;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSScrollView;
import com.bs.bsims.view.BSScrollView.onScrollListener;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;
import org.xutils.ex.HttpException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrmBusinessHomeIndexOneInfo extends BaseActivity implements OnClickListener, onScrollListener {

    /**
     * moveEdit 新添字段，表示三个点下面的转移他人的显示问题
     */
    public static CrmBusinessHomeIndexOneInfo aBusinessHomeIndexOneInfo;
    private int pagecount;
    public static final String BUSSNESS_EDIT = "bussness_edit";
    private String bid = "";// 商机的id
    private String cid;// 客户id
    private String cname;
    private Context mContext;
    private CrmBussinesListindexVo crmBussineLivo, crmBussineLivo1;// 商机实体类
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private BSCircleImageView clireview;
    private TextView cilent_company_name, cilent_company_money, bussines_canme, bussines_createtime, bussines_type, bussines_resoure, bussines_resoure_fromcstomer, bussines_pname, bussines_pmoney,
            bussines_lname, bussines_ldname, bussnies_person_time, bussnies_person_name, bussnies_person_dpname, head_momtv;
    private Intent intent;
    private String str[] = null;

    private TextView texttop_1, texttop_2, texttop_3, texttop_4, texttop_5, texttop_6;
    private TextView textbot_1, textbot_2, textbot_3, textbot_4, textbot_5, textbot_6;

    private ImageView bussines_state1, bussines_state0;
    private ImageView bussines_state2, bussines_state3, bussines_state4, bussines_state5, bussines_lmsg, bussines_ltel;

    private BSCircleImageView basicinfoheadpic, basicinfoheadpic_create;

    private LinearLayout send_ly, top1_sofar;

    private String phone = "";
    private FrameLayout id_content;

    private TextView detailinfo, detailinfo2, shoukuan_record;

    private BSDialog bsd;

    private View view_white_direction;// 头部白线的布局

    private String mGiveKey = "";// 判断是否是启动或者放弃商机
    private TextView head_back1;
    /**
     * 判断是否刷新数据
     */
    private String stateUtilthread = "0";

    private CrmBussinesFragment1 mAllFragment = null;
    private CrmBussinesFragment2 myUploadFragment = null;
    private CrmBussinesFragment3 myCollectionFragment = null;
    // 广播用于仪表盘中新增商机列表数据刷新
    private String STATISTICS_NEW_BUSSINESS_DETAIL = "statistics_new_bussiness_detail";

    private BSScrollView mImgScrollView;
    private ImageView mTitleBg;
    private DisplayMetrics metric;
    private Boolean mScaling = false;
    private float mFirstPosition = 0;
    private LinearLayout mMiddleLy;
    private FrameLayout.LayoutParams layoutParams1;
    private int distance = 0;

    private ImageView mMneuTv;
    private String[] mArrayOne = {
            "商机详情", "添加跟进人", "启动商机", "客户动向"
    };
    private String[] mArrayTwo = {
            "商机详情", "添加跟进人", "放弃商机", "客户动向"
    };
    private String[] mArrayThree = {
            "商机详情", "客户动向"
    };

    private String[] mArray;

    /*
     * (non-Javadoc)
     * @see BaseActivity#onCreate(android.os.Bundle)
     */

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.crmbusiness_details_home, mContentLayout);
        baseHeadLayout.setBackgroundColor(Color.TRANSPARENT);
        mHeadLayout.setVisibility(View.GONE);
        mContext = this;
        aBusinessHomeIndexOneInfo = this;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
        metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);

    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub
        baseHeadLayout.setBackgroundColor(Color.parseColor("#2A89CD"));
        crmBussineLivo1 = crmBussineLivo.getArray();
        cid = crmBussineLivo1.getCid();
        cname = crmBussineLivo1.getCname();
        cilent_company_name.setText(crmBussineLivo1.getBname());
        cilent_company_money.setText(CommonUtils.formatDetailMoney(crmBussineLivo1.getMoney()));
        if (null != crmBussineLivo1.getCrmEdit() && "2".equals(crmBussineLivo1.getCrmEdit())) {
            // 0表示该商机可以放弃
            if (crmBussineLivo1.getDel().equals("0")) {
                mGiveKey = "0";
                ImageView i = (ImageView) findViewById(R.id.crm_client_giveup_iv);
                i.setImageResource(R.drawable.crm_client_giveup);
                mArray = mArrayTwo;

            }
            // 2表示该商机可以启用
            else if (crmBussineLivo1.getDel().equals("2")) {
                mGiveKey = "2";
                findViewById(R.id.crm_give_logo).setVisibility(View.VISIBLE);
                ImageView i = (ImageView) findViewById(R.id.crm_client_giveup_iv);
                i.setImageResource(R.drawable.crm_bussines_open);

                mArray = mArrayOne;
            }

        } else {
            mArray = mArrayThree;
            findViewById(R.id.crm_client_contacts_iv).setVisibility(View.GONE);
            findViewById(R.id.crm_client_giveup_iv).setVisibility(View.GONE);
        }

        if (crmBussineLivo1.getCrmEdit().equals("1") || crmBussineLivo1.getCrmEdit().equals("2")) {
            findViewById(R.id.add_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.add_layout).setVisibility(View.GONE);
        }

        ShowUIForBussinesTop(Integer.parseInt(crmBussineLivo1.getStatus()));
        bussines_canme.setText(crmBussineLivo1.getCname());
        setSelect(1);
        mMneuTv.setVisibility(View.VISIBLE);
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {
        // TODO Auto-generated method stub
        bid = getIntent().getStringExtra("bid");
        cilent_company_name = (TextView) findViewById(R.id.cilent_company_name);// 头部名称
        cilent_company_money = (TextView) findViewById(R.id.cilent_company_money);// 头部金额
        texttop_1 = (TextView) findViewById(R.id.texttop_1);
        texttop_2 = (TextView) findViewById(R.id.texttop_2);
        texttop_3 = (TextView) findViewById(R.id.texttop_3);
        texttop_4 = (TextView) findViewById(R.id.texttop_4);
        texttop_5 = (TextView) findViewById(R.id.texttop_5);
        texttop_6 = (TextView) findViewById(R.id.texttop_6);
        textbot_1 = (TextView) findViewById(R.id.textbot_1);
        textbot_2 = (TextView) findViewById(R.id.textbot_2);
        textbot_3 = (TextView) findViewById(R.id.textbot_3);
        textbot_4 = (TextView) findViewById(R.id.textbot_4);
        textbot_5 = (TextView) findViewById(R.id.textbot_5);
        textbot_6 = (TextView) findViewById(R.id.textbot_6);
        bussines_state1 = (ImageView) findViewById(R.id.bussines_state1);
        bussines_state0 = (ImageView) findViewById(R.id.bussines_state0);
        bussines_state2 = (ImageView) findViewById(R.id.bussines_state2);
        bussines_state3 = (ImageView) findViewById(R.id.bussines_state3);
        bussines_state4 = (ImageView) findViewById(R.id.bussines_state4);
        bussines_state5 = (ImageView) findViewById(R.id.bussines_state5);
        bussines_canme = (TextView) findViewById(R.id.bussines_canme);// 客户公司名称
        head_back1 = (TextView) findViewById(R.id.head_back1);
        mImgScrollView = (BSScrollView) findViewById(R.id.pullscroller);
        mTitleBg = (ImageView) findViewById(R.id.top_bg);
        mMneuTv = (ImageView) findViewById(R.id.crm_client_notify_ivs);
        mMiddleLy = (LinearLayout) findViewById(R.id.middle_ly);
        layoutParams1 = (android.widget.FrameLayout.LayoutParams) mMiddleLy.getLayoutParams();
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mTitleBg.getLayoutParams();
        lp.width = metric.widthPixels;
        lp.height = metric.widthPixels * 9 / 16;
        mTitleBg.setLayoutParams(lp);
        if (null != getIntent().getStringExtra("stateUtilthread")) {
            stateUtilthread = getIntent().getStringExtra("stateUtilthread");
        }
        top1_sofar = (LinearLayout) findViewById(R.id.top1sofar);
        id_content = (FrameLayout) this.findViewById(R.id.id_content);
        // mViewPager.setOffscreenPageLimit(0);
        detailinfo = (TextView) findViewById(R.id.detailinfo);
        detailinfo2 = (TextView) findViewById(R.id.detailinfo2);
        shoukuan_record = (TextView) findViewById(R.id.shoukuan_record);
        view_white_direction = (View) findViewById(R.id.view_white_direction);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(view_white_direction.getLayoutParams());
        layoutParam.setMargins(CommonUtils.getScreenWidth(mContext) / 6 / 2, 0, CommonUtils.getScreenWidth(mContext) / 6 / 2, 0);
        view_white_direction.setLayoutParams(layoutParam);

    }

    ResultCallback mCallback = new ResultCallback() {
        @Override
        public void callback(String str, int position) {

            Intent intent = new Intent();
            if ("商机详情".equals(str)) {
                Intent i = new Intent();
                i.putExtra("crmBussineLivo1", crmBussineLivo1);
                i.putExtra("mBussinesKey", "1");
                i.putExtra("stateUtilthread", stateUtilthread);
                i.setClass(mContext, CrmBussinesDetailActivity.class);
                startActivity(i);

            } else if ("添加跟进人".equals(str)) {

                Intent intent1 = new Intent();
                intent1.putExtra("bid", bid);
                //
                // /*
                // * 这里要带入负责，联合跟进人，相关人
                // */
                intent1.putExtra("evo", crmBussineLivo1);
                intent1.putExtra("cid", cid);
                intent1.setClass(mContext, CrmJiontFollowupActivity.class);
                startActivityForResult(intent1, 2015);

            } else if ("启动商机".equals(str)) {

                View isoffice = View.inflate(mContext, R.layout.ishaveofficeapp, null);
                final TextView tv_content = (TextView) isoffice.findViewById(R.id.tv_text);
                isoffice.findViewById(R.id.office_logo).setVisibility(View.GONE);
                tv_content.setGravity(Gravity.CENTER);
                tv_content.setTextSize(16);
                if (!mGiveKey.equals("0")) {
                    tv_content.setText("您确定要启动该商机?");
                    bsd = new BSDialog(mContext, "商机提醒", isoffice, new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            PostChangeBussineChanceGiveUp("2");// 启动参数为2
                            bsd.dismiss();
                        }
                    });
                    bsd.show();
                }

            } else if ("放弃商机".equals(str)) {
                View isoffice = View.inflate(mContext, R.layout.ishaveofficeapp, null);
                final TextView tv_content = (TextView) isoffice.findViewById(R.id.tv_text);
                isoffice.findViewById(R.id.office_logo).setVisibility(View.GONE);
                tv_content.setGravity(Gravity.CENTER);
                tv_content.setTextSize(16);
                // 这里来个系统提醒
                if (mGiveKey.equals("0")) {
                    tv_content.setText("您确定要放弃该商机?");
                    bsd = new BSDialog(mContext, "商机提醒", isoffice, new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            PostChangeBussineChanceGiveUp("0");// 放弃参数为0
                            bsd.dismiss();
                        }
                    });
                    bsd.show();
                }

            } else if ("客户动向".equals(str)) {
                Intent i = new Intent();
                i.putExtra("bid", bid);
                i.putExtra("mBussinesKey", "2");
                i.setClass(mContext, CrmBussinesDetailActivity.class);
                startActivity(i);
            }

        }
    };

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        head_back1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // 客户跳转到商机
                if (stateUtilthread.equals("2")) {
                    Intent intent = new Intent(BUSSNESS_EDIT);
                    if (null != crmBussineLivo1) {
                        intent.putExtra("bvo", crmBussineLivo1);
                        CrmBusinessHomeIndexOneInfo.this.sendBroadcast(intent);
                    }
                    CrmBusinessHomeIndexOneInfo.this.finish();
                    return;
                }
                // 拜访记录跳转到商机
                else if (stateUtilthread.equals("3")) {
                    CrmBusinessHomeIndexOneInfo.this.finish();
                    return;
                }

                // 跳转到仪表盘新增商机界面
                else if (stateUtilthread.equals("4")) {
                    Intent i = new Intent(STATISTICS_NEW_BUSSINESS_DETAIL);
                    if (null != crmBussineLivo1) {
                        i.putExtra("bid", bid);
                        i.putExtra("bidstate", crmBussineLivo1.getStatus());
                        i.putExtra("bidstateName", crmBussineLivo1.getStatusName());
                        i.putExtra("bid_fuzerenname", crmBussineLivo1.getFullname());
                        i.putExtra("bid_name", crmBussineLivo1.getBname());
                        i.putExtra("bid_money", crmBussineLivo1.getMoney());
                    }
                    CrmBusinessHomeIndexOneInfo.this.sendBroadcast(i);
                    CrmBusinessHomeIndexOneInfo.this.finish();
                    return;
                }
                // 商机列表跳转到商机
                else if (stateUtilthread.equals("1")) {

                    Intent i = new Intent(mContext, CrmBusinessHomeListActivity.class);
                    // i.putExtra("postion", postion);
                    if (null != crmBussineLivo1) {
                        i.putExtra("bid", bid);
                        i.putExtra("bidstate", crmBussineLivo1.getStatus());
                        i.putExtra("bidstateName", crmBussineLivo1.getStatusName());
                        i.putExtra("bid_fuzerenname", crmBussineLivo1.getFullname());
                        i.putExtra("bid_name", crmBussineLivo1.getBname());
                        i.putExtra("bid_money", crmBussineLivo1.getMoney());
                    }
                    startActivity(i);
                    CrmBusinessHomeIndexOneInfo.this.finish();
                    return;

                }
                // 如果不需要回到商机列表的话，就直接finsh

            }
        });

        bussines_state1.setOnClickListener(this);
        bussines_state2.setOnClickListener(this);
        bussines_state3.setOnClickListener(this);
        bussines_state4.setOnClickListener(this);
        bussines_state5.setOnClickListener(this);
        bussines_state0.setOnClickListener(this);
        textbot_1.setOnClickListener(this);
        textbot_2.setOnClickListener(this);
        textbot_3.setOnClickListener(this);
        textbot_4.setOnClickListener(this);
        textbot_5.setOnClickListener(this);
        textbot_6.setOnClickListener(this);
        texttop_1.setOnClickListener(this);
        texttop_2.setOnClickListener(this);
        texttop_3.setOnClickListener(this);
        texttop_4.setOnClickListener(this);
        texttop_5.setOnClickListener(this);
        texttop_6.setOnClickListener(this);
        mImgScrollView.setOnScrollListener(this);

        mMneuTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                CommonUtils.initPopViewBg(CrmBusinessHomeIndexOneInfo.this, mArray, mMneuTv, mCallback, CommonUtils.getScreenWidth(mContext) / 3);
            }
        });
        // 商机头部右侧四个按钮点击 编辑商机
        findViewById(R.id.crm_client_detail_iv).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent();
                i.putExtra("crmBussineLivo1", crmBussineLivo1);
                i.putExtra("mBussinesKey", "1");
                i.putExtra("stateUtilthread", stateUtilthread);
                i.setClass(mContext, CrmBussinesDetailActivity.class);
                startActivity(i);
            }
        });
        // 商机头部右侧四个按钮点击 选择联合跟进人
        findViewById(R.id.crm_client_contacts_iv).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent intent = new Intent();
                intent.putExtra("bid", bid);
                //
                // /*
                // * 这里要带入负责，联合跟进人，相关人
                // */
                intent.putExtra("evo", crmBussineLivo1);
                intent.putExtra("cid", cid);
                intent.setClass(mContext, CrmJiontFollowupActivity.class);
                startActivityForResult(intent, 2015);

            }
        });
        // 商机头部右侧四个按钮点击 放弃商机
        findViewById(R.id.crm_client_giveup_iv).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                View isoffice = View.inflate(mContext, R.layout.ishaveofficeapp, null);
                final TextView tv_content = (TextView) isoffice.findViewById(R.id.tv_text);
                isoffice.findViewById(R.id.office_logo).setVisibility(View.GONE);
                tv_content.setGravity(Gravity.CENTER);
                tv_content.setTextSize(16);
                // 这里来个系统提醒
                if (mGiveKey.equals("0")) {
                    tv_content.setText("您确定要放弃该商机?");
                    bsd = new BSDialog(mContext, "商机提醒", isoffice, new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            PostChangeBussineChanceGiveUp("0");// 放弃参数为0
                            bsd.dismiss();
                        }
                    });
                    bsd.show();
                } else {
                    tv_content.setText("您确定要启动该商机?");
                    bsd = new BSDialog(mContext, "商机提醒", isoffice, new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub

                            PostChangeBussineChanceGiveUp("2");// 启动参数为2
                            bsd.dismiss();
                        }
                    });
                    bsd.show();
                }

            }
        });
        // 商机头部右侧四个按钮点击 商机动态
        findViewById(R.id.crm_client_notify_iv).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent();
                i.putExtra("bid", bid);
                i.putExtra("mBussinesKey", "2");
                i.setClass(mContext, CrmBussinesDetailActivity.class);
                startActivity(i);

            }
        });

        // 添加跟单记录
        findViewById(R.id.add_layout).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent();
                i.putExtra("bstatus", crmBussineLivo1.getStatus());
                i.putExtra("bname", crmBussineLivo1.getBname());
                i.putExtra("bid", crmBussineLivo1.getBid());
                i.putExtra("cid", crmBussineLivo1.getCid());
                i.putExtra("cname", crmBussineLivo1.getCname());
                i.putExtra("vistorSate", "4");
                i.setClass(mContext, CrmVisitRecordActivityAddInfo.class);
                startActivityForResult(i, 2013);
            }
        });

        detailinfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setSelect(0);
            }
        });

        detailinfo2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setSelect(1);
            }
        });

        shoukuan_record.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setSelect(2);
            }
        });
        top1_sofar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String crmEdit = crmBussineLivo1.getCrmEdit();
                Intent i = new Intent();
                i.putExtra("cid", cid);
                i.putExtra("crmEdit", crmEdit);
                i.setClass(mContext, CrmClientDetailActivity.class);
                startActivity(i);
            }
        });
        mImgScrollView.setOnTouchListener(new OnTouchListener() {
            @SuppressLint({
                    "ClickableViewAccessibility", "NewApi"
            })
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mTitleBg.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mScaling = false;
                        replyImage();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!mScaling) {
                            if (mImgScrollView.getScrollY() == 0) {
                                findViewById(R.id.comm_head_layout).setBackgroundColor(getResources().getColor(R.color.translucent));
                                mFirstPosition = event.getY();
                            } else {
                                break;
                            }
                        }
                        distance = (int) ((event.getY() - mFirstPosition) * 0.6);
                        mScaling = true;
                        if (distance > 300)
                            distance = 300;
                        if (distance < 0 || distance > 300) {
                            break;
                        }
                        else {
                            lp.width = metric.widthPixels + distance;
                            lp.height = (metric.widthPixels + distance) * 9 / 16;
                            mTitleBg.setLayoutParams(lp);
                            layoutParams1.setMargins(0, CommonUtils.dip2px(mContext, 55) + CommonUtils.px2dip(mContext, distance), 0, 0);
                        }

                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        if (CheckOnclick(crmBussineLivo1.getCrmEdit(), crmBussineLivo1.getStatus())) {
            switch (v.getId()) {
                case R.id.bussines_state1:
                case R.id.textbot_1:
                case R.id.texttop_1:

                    if (!crmBussineLivo1.getStatus().equals("1"))
                        PostChangeBussineChanceTo6("1", "初步接洽");

                    break;
                case R.id.bussines_state2:
                case R.id.textbot_2:
                case R.id.texttop_2:
                    if (!crmBussineLivo1.getStatus().equals("2"))
                        PostChangeBussineChanceTo6("2", "需求确认");
                    break;
                case R.id.bussines_state3:
                case R.id.textbot_3:
                case R.id.texttop_3:
                    if (!crmBussineLivo1.getStatus().equals("3"))
                        PostChangeBussineChanceTo6("3", "方案报价");
                    break;
                case R.id.bussines_state4:
                case R.id.textbot_4:
                case R.id.texttop_4:
                    if (!crmBussineLivo1.getStatus().equals("4"))
                        PostChangeBussineChanceTo6("4", "谈判审核");
                    break;
                case R.id.texttop_5:
                case R.id.bussines_state5:
                case R.id.textbot_5:
                    if (!crmBussineLivo1.getStatus().equals("5")) {
                        // 跳转到添加合同
                        Intent i = new Intent();
                        i.putExtra("cid", cid);
                        i.putExtra("cname", cname);
                        i.putExtra("bid", bid);
                        i.putExtra("bname", crmBussineLivo1.getBname());
                        i.setClass(mContext, CrmTradeContantAddInfo.class);
                        // startActivity(i);
                        startActivityForResult(i, 2012);
                    }
                    break;

                case R.id.texttop_6:
                case R.id.bussines_state0:
                case R.id.textbot_6:
                    if (!crmBussineLivo1.getStatus().equals("6"))

                    {
                        View isoffice = View.inflate(mContext, R.layout.ishaveofficeapp, null);
                        TextView tv_content = (TextView) isoffice.findViewById(R.id.tv_text);
                        ImageView islogo = (ImageView) isoffice.findViewById(R.id.office_logo);
                        islogo.setVisibility(View.GONE);
                        // 这里来个系统提醒
                        tv_content.setTextSize(16);
                        tv_content.setText("确定更改此商机为输单?");
                        bsd = new BSDialog(mContext, "商机提醒", isoffice, new OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                PostChangeBussineChanceTo6("6", "输单");
                                bsd.dismiss();
                            }
                        });
                        bsd.show();

                    }

                    break;

            }

        } else {
            CustomToast.showLongToast(mContext, "没有更改权限");
        }

    }

    /** 获取商机首页头部数据 */

    public boolean getData() {
        Gson gson = new Gson();
        Map paramsMap = new HashMap<String, String>();
        paramsMap.put("bid", bid);
        String urlStr;
        String jsonUrlStr;
        try {
            urlStr = UrlUtil.getUrlByMap1(Constant.CRM_BUSSINES_HOMEBUSSINESINDEX, paramsMap);
            jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            crmBussineLivo = gson.fromJson(jsonUrlStr, CrmBussinesListindexVo.class);
            if (crmBussineLivo.getCode().equals("200")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * UI显示当前头部商机的状态蓝
     */
    public void ShowUIForBussinesTop(int vKey) {
        ClearColor();
        switch (vKey) {
            case 1:
                texttop_1.setTextColor(getResources().getColor(R.color.yellow1));
                textbot_1.setTextColor(getResources().getColor(R.color.yellow1));
                bussines_state1.setImageResource(R.drawable.bg_circle_yellow);
                break;
            case 2:
                texttop_2.setTextColor(getResources().getColor(R.color.yellow1));
                textbot_2.setTextColor(getResources().getColor(R.color.yellow1));
                bussines_state2.setImageResource(R.drawable.bg_circle_yellow);
                break;
            case 3:
                texttop_3.setTextColor(getResources().getColor(R.color.yellow1));
                textbot_3.setTextColor(getResources().getColor(R.color.yellow1));
                bussines_state3.setImageResource(R.drawable.bg_circle_yellow);
                break;
            case 4:
                texttop_4.setTextColor(getResources().getColor(R.color.yellow1));
                textbot_4.setTextColor(getResources().getColor(R.color.yellow1));
                bussines_state4.setImageResource(R.drawable.bg_circle_yellow);
                break;
            case 5:
                texttop_5.setTextColor(getResources().getColor(R.color.yellow1));
                textbot_5.setTextColor(getResources().getColor(R.color.yellow1));
                bussines_state5.setImageResource(R.drawable.bg_circle_yellow);
                break;
            case 6:

                texttop_6.setTextColor(getResources().getColor(R.color.yellow1));
                textbot_6.setTextColor(getResources().getColor(R.color.yellow1));
                bussines_state0.setImageResource(R.drawable.bg_circle_yellow);
                break;

            default:
                break;
        }
    }

    /*
     * 清除头部的颜色
     */

    public void ClearColor() {
        texttop_1.setTextColor(getResources().getColor(R.color.white));
        texttop_2.setTextColor(getResources().getColor(R.color.white));
        texttop_3.setTextColor(getResources().getColor(R.color.white));
        texttop_4.setTextColor(getResources().getColor(R.color.white));
        texttop_5.setTextColor(getResources().getColor(R.color.white));
        texttop_6.setTextColor(getResources().getColor(R.color.white));
        textbot_1.setTextColor(getResources().getColor(R.color.white));
        textbot_2.setTextColor(getResources().getColor(R.color.white));
        textbot_3.setTextColor(getResources().getColor(R.color.white));
        textbot_4.setTextColor(getResources().getColor(R.color.white));
        textbot_5.setTextColor(getResources().getColor(R.color.white));
        textbot_6.setTextColor(getResources().getColor(R.color.white));
        bussines_state1.setImageResource(R.drawable.bg_circle_c3);
        bussines_state0.setImageResource(R.drawable.bg_circle_c3);
        bussines_state2.setImageResource(R.drawable.bg_circle_c3);
        bussines_state3.setImageResource(R.drawable.bg_circle_c3);
        bussines_state4.setImageResource(R.drawable.bg_circle_c3);
        bussines_state5.setImageResource(R.drawable.bg_circle_c3);
    }

    /**
     * 提交修改当前的商机的
     */

    public void PostChangeBussineChanceTo6(final String states, final String stateName) {
        CustomDialog.showProgressDialog(mContext, "正在更改商机..");
        final String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_BUSSINES_HOMEBUSSINESEDIT;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("ftoken", BSApplication.getInstance().getmCompany());
        map.put("userid", BSApplication.getInstance().getUserId());
        map.put("type", "2");
        map.put("bid", bid);
        map.put("status", states);
        new HttpUtilsByPC().sendPostBYPC(url, map,
                /**
                 * @author Administrator
                 */
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        CustomToast.showLongToast(mContext, "更改失败!");
                        CustomDialog.closeProgressDialog();
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo rstr) {
                        // TODO Auto-generated method stub
                        // 做假显示了
                        ShowUIForBussinesTop(Integer.parseInt(states));
                        CustomDialog.closeProgressDialog();
                        CustomToast.showLongToast(mContext, "更改成功!");
                        crmBussineLivo1.setStatus(states);
                        crmBussineLivo1.setStatusName(stateName);

                    }

                });

    }

    /**
     * 启动和放弃商机
     */

    public void PostChangeBussineChanceGiveUp(final String states) {
        CustomDialog.showProgressDialog(mContext, "正在更改商机..");
        final String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_BUSSINES_HOMEBUSSGIVEUP;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("ftoken", BSApplication.getInstance().getmCompany());
        map.put("userid", BSApplication.getInstance().getUserId());
        if (states.equals("0")) {
            map.put("type", "1");
        } else if (states.equals("2")) {
            map.put("type", "2");
        }
        map.put("bid", bid);
        new HttpUtilsByPC().sendPostBYPC(url, map,
                /**
                 * @author Administrator
                 */
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        CustomToast.showLongToast(mContext, "更改失败!");
                        CustomDialog.closeProgressDialog();
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo rstr) {
                        // TODO Auto-generated method stub
                        // 做假显示了
                        CustomDialog.closeProgressDialog();
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(new String(rstr.result.toString()));
                            String str = (String) jsonObject.get("retinfo");
                            String code = (String) jsonObject.get("code");
                            CustomToast.showLongToast(mContext, str);
                            if (Constant.RESULT_CODE.equals(code)) {
                                Intent i = new Intent(mContext, CrmBusinessHomeListActivity.class);
                                i.putExtra("blistclear", "1");
                                startActivity(i);
                                CrmBusinessHomeIndexOneInfo.this.finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                });

    }

    public boolean CheckOnclick(String crmEdit, String crmState) {
        // 先判断有没有编辑权限
        if (crmEdit.equals("1") || crmEdit.equals("2")) {
            // 有编辑权限的情况下
            if (!crmState.equals("5") && !crmState.equals("6")) {// 状态不是赢单和输单
                return true;
            } else {
                return false;
            }
        } else
            return false;

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {

        switch (arg0) {

            case 2012:
                // 添加合同成功变成赢单了
                if (arg1 == 2012) {
                    if (arg2 != null) {
                        // PostChangeBussineChanceTo6("5", "赢单");
                        crmBussineLivo1.setStatus("5");
                        crmBussineLivo1.setStatusName("赢单");
                        ShowUIForBussinesTop(5);
                    }
                }
                break;

            case 2013:
                // 添加拜访记录成功
                if (arg1 == 2013) {
                    if (arg2 != null) {
                        myUploadFragment.GetNewCrmVisitorInfo();
                    }
                }
                break;
            case 2014:
                if (arg1 == 2014) {
                    if (arg2 != null) {
                        EmployeeVO employee = (EmployeeVO) arg2.getSerializableExtra("approve_activity");
                        if (employee == null) {
                            employee = ((List<EmployeeVO>) arg2.getSerializableExtra("checkboxlist")).get(0);
                        }
                        if (employee != null) {
                            /** 采用post请求 */
                            CustomDialog.showProgressDialog(mContext, "正在转移此商机...");
                            PostBussinesToAnthoer(employee.getUserid());
                        }
                    }
                }
                break;

            case 2015:
                if (arg1 == 2015) {
                    if (arg2 != null) {
                        crmBussineLivo1 = new CrmBussinesListindexVo();
                        crmBussineLivo1 = (CrmBussinesListindexVo) arg2.getSerializableExtra("evo");
                    }

                }
                break;

            case 1:
                if (arg1 == 1) {
                    if (arg2 != null) {
                        if (myUploadFragment != null) {
                            myUploadFragment.onActivityResult(arg0, arg1, arg2);
                        }
                    }
                }

        }

    }

    /**
     * 转给给某个责任人的商机方法
     */

    public void PostBussinesToAnthoer(String useridanthoer) {
        final String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_BUSSINES_HOMEBUSSINESEDIT;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("ftoken", BSApplication.getInstance().getmCompany());
        map.put("userid", BSApplication.getInstance().getUserId());
        map.put("type", "1");
        map.put("bid", bid);
        map.put("person", useridanthoer);
        new HttpUtilsByPC().sendPostBYPC(url, map,
                /**
                 * @author Administrator
                 */
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        CustomToast.showLongToast(mContext, "转移失败!");
                        CustomDialog.closeProgressDialog();
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo rstr) {
                        // TODO Auto-generated method stub
                        CustomDialog.closeProgressDialog();
                        // editKey1 = "0";// 假设定 提高体验不刷新数据
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(new String(rstr.result.toString()));
                            String str = (String) jsonObject.get("retinfo");
                            String code = (String) jsonObject.get("code");
                            CustomToast.showLongToast(mContext, str);
                            if (Constant.RESULT_CODE.equals(code)) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                });
        // Intent intent1 = new Intent(mContext,
        // CrmBusinessHomeListActivity.class);
        // mContext.startActivity(intent1);

    }

    // 由于返回的列表要刷新数据源，必须捕捉back键盘来支持刷新
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复

            // 如果不需要回到商机列表的话，就直接finsh
            if (stateUtilthread.equals("2")) {
                Intent intent = new Intent(BUSSNESS_EDIT);
                if (null != crmBussineLivo1) {
                    intent.putExtra("bvo", crmBussineLivo1);
                    mContext.sendBroadcast(intent);
                }

                this.finish();
                return true;
            }
            // 拜访记录跳转到商机
            else if (stateUtilthread.equals("3")) {
                CrmBusinessHomeIndexOneInfo.this.finish();
                return true;
            }

            else if (stateUtilthread.equals("4")) {
                Intent i = new Intent(STATISTICS_NEW_BUSSINESS_DETAIL);
                if (null != crmBussineLivo1) {
                    i.putExtra("bid", bid);
                    i.putExtra("bidstate", crmBussineLivo1.getStatus());
                    i.putExtra("bidstateName", crmBussineLivo1.getStatusName());
                    i.putExtra("bid_fuzerenname", crmBussineLivo1.getFullname());
                    i.putExtra("bid_name", crmBussineLivo1.getBname());
                    i.putExtra("bid_money", crmBussineLivo1.getMoney());
                }
                CrmBusinessHomeIndexOneInfo.this.sendBroadcast(i);
                CrmBusinessHomeIndexOneInfo.this.finish();
                return true;
            }

            else if (stateUtilthread.equals("1")) {

                // **做假设置状态*//
                Intent i = new Intent(mContext, CrmBusinessHomeListActivity.class);
                // i.putExtra("postion", postion);
                if (null != crmBussineLivo1) {
                    i.putExtra("bid", bid);
                    i.putExtra("bidstate", crmBussineLivo1.getStatus());
                    i.putExtra("bidstateName", crmBussineLivo1.getStatusName());
                    i.putExtra("bid_fuzerenname", crmBussineLivo1.getFullname());
                    i.putExtra("bid_name", crmBussineLivo1.getBname());
                    i.putExtra("bid_money", crmBussineLivo1.getMoney());

                }
                startActivity(i);
                this.finish();
                return true;
            }

        }

        return super.onKeyDown(keyCode, event);
    }

    public void setSelect(int i) {
        ClearColorText();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        // 把图片设置为亮的
        // 设置内容区域
        switch (i) {
            case 0:
                detailinfo.setTextColor(getResources().getColor(R.color.white));
                detailinfo.setBackgroundResource(R.drawable.corners_tab_left_select);
                pagecount = 0;
                if (mAllFragment == null) {
                    mAllFragment = new CrmBussinesFragment1(crmBussineLivo1);
                    transaction.add(R.id.id_content, mAllFragment);
                } else {
                    transaction.show(mAllFragment);
                }
                break;
            case 1:
                detailinfo2.setTextColor(getResources().getColor(R.color.white));
                detailinfo2.setBackgroundResource(R.drawable.corners_tab_center_select);
                pagecount = 1;
                if (myUploadFragment == null) {
                    myUploadFragment = new CrmBussinesFragment2(bid, "");
                    transaction.add(R.id.id_content, myUploadFragment);
                } else {
                    transaction.show(myUploadFragment);

                }
                findViewById(R.id.tv_location_father_ly).setVisibility(View.GONE);
                break;
            case 2:
                shoukuan_record.setTextColor(getResources().getColor(R.color.white));
                shoukuan_record.setBackgroundResource(R.drawable.corners_tab_right_select);
                pagecount = 2;
                if (myCollectionFragment == null) {
                    myCollectionFragment = new CrmBussinesFragment3(bid);
                    transaction.add(R.id.id_content, myCollectionFragment);
                } else {
                    transaction.show(myCollectionFragment);
                }
                break;

            default:
                break;

        }
        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mAllFragment != null) {
            transaction.hide(mAllFragment);
        }
        if (myUploadFragment != null) {
            transaction.hide(myUploadFragment);
        }
        if (myCollectionFragment != null) {
            transaction.hide(myCollectionFragment);
        }

    }

    /*
     * 清除背景颜色和字体的颜色
     */
    public void ClearColorText() {
        detailinfo.setTextColor(getResources().getColor(R.color.C5));
        detailinfo2.setTextColor(getResources().getColor(R.color.C5));
        shoukuan_record.setTextColor(getResources().getColor(R.color.C5));
        detailinfo.setBackgroundResource(R.drawable.corners_tab_left_normal);
        detailinfo2.setBackgroundResource(R.drawable.corners_tab_center_normal);
        shoukuan_record.setBackgroundResource(R.drawable.corners_tab_right_normal);
    }

    // 拉动放大图片
    @SuppressLint("NewApi")
    public void replyImage() {
        final ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mTitleBg.getLayoutParams();
        final float w = mTitleBg.getLayoutParams().width;// ͼƬ��ǰ���
        final float h = mTitleBg.getLayoutParams().height;// ͼƬ��ǰ�߶�
        final float newW = metric.widthPixels;// ͼƬԭ���
        final float newH = metric.widthPixels * 9 / 16;// ͼƬԭ�߶�
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(200);

        anim.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                lp.width = (int) (w - (w - newW) * cVal);
                lp.height = (int) (h - (h - newH) * cVal);
                mTitleBg.setLayoutParams(lp);
                mTitleBg.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layoutParams1.setMargins(0, CommonUtils.dip2px(mContext, 55), 0, 0);
                    }
                }, 120);

            }
        });
        anim.start();

    }

    @Override
    public void onScroll(int line, int tran, int oldLine, int oldTran) {
        CustomLog.e("tran", "tran----" + tran + "|||" + "oldTran----" + oldTran);
        if (tran >= CommonUtils.getViewHigh(mTitleBg)) {
            findViewById(R.id.comm_head_layout).setBackgroundColor(Color.parseColor("#2A89CD"));
        }
        else {
            findViewById(R.id.comm_head_layout).setBackgroundColor(getResources().getColor(R.color.translucent));
        }
    }

    @Override
    public void onScrollUp() {
    }

    @Override
    public void onScrollDown() {

    }
}
