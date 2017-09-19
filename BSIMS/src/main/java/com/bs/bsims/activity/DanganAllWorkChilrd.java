
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bs.bsims.R;
import com.bs.bsims.adapter.DanganAllWorkChilrdLisenceApdater;
import com.bs.bsims.adapter.DanganAllWorkChilrdRreawdsApdater;
import com.bs.bsims.adapter.DanganAllWorkChilrdTranisApdater;
import com.bs.bsims.adapter.DanganAllWorkChilrdTransferApdater;
import com.bs.bsims.adapter.DanganAllWorkChilrdUserPayApdater;
import com.bs.bsims.adapter.DanganAllWorkChilrdinterviewApdater;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.DanganWorkInterview;
import com.bs.bsims.model.DanganWorkLicense;
import com.bs.bsims.model.DanganWorkRewards;
import com.bs.bsims.model.DanganWorkTrains;
import com.bs.bsims.model.DanganWorkTransfer;
import com.bs.bsims.model.DanganWorkUserpay;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DanganAllWorkChilrd extends BaseActivity implements OnClickListener {

    /*
     * 档案页面下 工作表现页面的所有activity 都用他一个
     */

    private String uid;
    private RelativeLayout  da_workall_timeselector;

    private static String CONTACTSTRAIN = "";
    private static String CONTACTSTACNAME = "";

    private ImageView top_changedate_last, top_changedate_next;

    private Context mContext;

    /*
     * 1培训
     */
    private DanganWorkTrains dtinfo = new DanganWorkTrains();
    private List<DanganWorkTrains> dtinfoarray = new ArrayList<DanganWorkTrains>();

    /*
     * 2调岗
     */
    private DanganWorkTransfer dtinfotranfer = new DanganWorkTransfer();
    private List<DanganWorkTransfer> dtinfoarraytranfer = new ArrayList<DanganWorkTransfer>();

    /*
     * 3薪资情况
     */
    private DanganWorkUserpay dtinfouserpay = new DanganWorkUserpay();
    private List<DanganWorkUserpay> dtinfoarrayuserpay = new ArrayList<DanganWorkUserpay>();

    /*
     * 4奖惩记录
     */
    private DanganWorkRewards dtinforewards = new DanganWorkRewards();
    private List<DanganWorkRewards> dtinfoarrayrewards = new ArrayList<DanganWorkRewards>();

    /*
     * 5约谈记录
     */
    private DanganWorkInterview dtinfointenview = new DanganWorkInterview();
    private List<DanganWorkInterview> dtinfoarrayintentview = new ArrayList<DanganWorkInterview>();

    /*
     * 6电子证照
     */
    private DanganWorkLicense dtinfolisence = new DanganWorkLicense();
    private List<DanganWorkLicense> dtinfoarraylisence = new ArrayList<DanganWorkLicense>();

    private BSRefreshListView bsresflistview;
    private DanganAllWorkChilrdTranisApdater dcapter = null; // 培训
    private DanganAllWorkChilrdTransferApdater dcapter1 = null;// 调岗
    private DanganAllWorkChilrdUserPayApdater dcapter2 = null;// 薪资
    private DanganAllWorkChilrdRreawdsApdater dcapter3 = null;// 奖惩
    private DanganAllWorkChilrdinterviewApdater dcapter4 = null;// 约谈
    private DanganAllWorkChilrdLisenceApdater dcapter5 = null;// 证件照

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.da_work_train, mContentLayout);
        // setContentView(R.layout.da_work_train);
        Intent i = getIntent();
        CONTACTSTRAIN = i.getStringExtra("keyCONTACTSTRAIN");
        CONTACTSTACNAME = i.getStringExtra("keyCONTACTSTACNAME");
        uid = i.getStringExtra("uid");
        mContext = this;
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        da_workall_timeselector = (RelativeLayout) findViewById(R.id.da_workall_timeselector);
        da_workall_timeselector.setVisibility(View.GONE);

        // TODO Auto-generated method stub
        bsresflistview = (BSRefreshListView) findViewById(R.id.work_attendance_everyone_detail_refreshlistview);
        if (CONTACTSTRAIN.equals("1")) {
            dcapter = new DanganAllWorkChilrdTranisApdater(this);
        } else if (CONTACTSTRAIN.equals("2")) {
            dcapter1 = new DanganAllWorkChilrdTransferApdater(this);
            findViewById(R.id.basicinfoworkexp_fl).setVisibility(View.VISIBLE);
        } else if (CONTACTSTRAIN.equals("3")) {
            dcapter2 = new DanganAllWorkChilrdUserPayApdater(this);
        } else if (CONTACTSTRAIN.equals("4")) {
            dcapter3 = new DanganAllWorkChilrdRreawdsApdater(this);
        } else if (CONTACTSTRAIN.equals("5")) {
            dcapter4 = new DanganAllWorkChilrdinterviewApdater(this);
        } else if (CONTACTSTRAIN.equals("6")) {
            dcapter5 = new DanganAllWorkChilrdLisenceApdater(this);
        }

        mTitleTv.setText(CONTACTSTACNAME);
        top_changedate_last = (ImageView) findViewById(R.id.top_changedate_last);
        top_changedate_next = (ImageView) findViewById(R.id.top_changedate_next);
    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        top_changedate_last.setOnClickListener(this);
        top_changedate_next.setOnClickListener(this);
    }

    public boolean getData() {
        Gson gson = new Gson();
        String urlStr;
        String jsonUrlStr;
        Map<String, String> map = new HashMap<String, String>();
        try {
            // 如果带过来是第一个培训记录
            if (CONTACTSTRAIN.equals("1")) {
                map.put("type", "trains");
                map.put("uid", uid);
                urlStr = UrlUtil.getUrlByMap1(Constant.DANGANARCHIVESWORKING, map);
                CustomLog.e("WorkUrlc", urlStr);
                jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
                // CustomLog.e("UserURL", urlStr);
                // mResultInfoVO = gson.fromJson(jsonUrlStr,
                // UserInfoResultVO.class);
                // mResultVO = gson.fromJson(jsonUrlStr, ResultVO.class);
                dtinfo = gson.fromJson(jsonUrlStr, DanganWorkTrains.class);
                dtinfoarray = dtinfo.getArray();
                CustomLog.e("Jsonarry", dtinfoarray.size() + "");
                if (!dtinfo.getCode().equals("200")) {
                    return false;
                }

            } else if (CONTACTSTRAIN.equals("2")) {
                map.put("type", "transfer");
                map.put("uid", uid);
                urlStr = UrlUtil.getUrlByMap1(Constant.DANGANARCHIVESWORKING, map);
                CustomLog.e("WorkUrlc", urlStr);
                jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
                // CustomLog.e("UserURL", urlStr);
                // mResultInfoVO = gson.fromJson(jsonUrlStr,
                // UserInfoResultVO.class);
                // mResultVO = gson.fromJson(jsonUrlStr, ResultVO.class);
                dtinfotranfer = gson.fromJson(jsonUrlStr, DanganWorkTransfer.class);
                dtinfoarraytranfer = dtinfotranfer.getArray();
                CustomLog.e("Jsonarry", dtinfoarraytranfer.size() + "");
                if (!dtinfotranfer.getCode().equals("200")) {
                    return false;
                }
            }

            else if (CONTACTSTRAIN.equals("3")) {
                map.put("type", "userpay");
                map.put("uid", uid);
                urlStr = UrlUtil.getUrlByMap1(Constant.DANGANARCHIVESWORKING, map);
                CustomLog.e("WorkUrlc", urlStr);
                jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
                // CustomLog.e("UserURL", urlStr);
                // mResultInfoVO = gson.fromJson(jsonUrlStr,
                // UserInfoResultVO.class);
                // mResultVO = gson.fromJson(jsonUrlStr, ResultVO.class);
                dtinfouserpay = gson.fromJson(jsonUrlStr, DanganWorkUserpay.class);
                dtinfoarrayuserpay = dtinfouserpay.getArray();
                CustomLog.e("Jsonarry", dtinfoarraytranfer.size() + "");
                if (!dtinfouserpay.getCode().equals("200")) {
                    return false;
                }
            }

            else if (CONTACTSTRAIN.equals("4")) {
                map.put("type", "rewards");
                map.put("uid", uid);
                urlStr = UrlUtil.getUrlByMap1(Constant.DANGANARCHIVESWORKING, map);
                CustomLog.e("WorkUrlc", urlStr);
                jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
                // CustomLog.e("UserURL", urlStr);
                // mResultInfoVO = gson.fromJson(jsonUrlStr,
                // UserInfoResultVO.class);
                // mResultVO = gson.fromJson(jsonUrlStr, ResultVO.class);
                dtinforewards = gson.fromJson(jsonUrlStr, DanganWorkRewards.class);
                dtinfoarrayrewards = dtinforewards.getArray();
                CustomLog.e("Jsonarry", dtinfoarraytranfer.size() + "");
                if (!dtinforewards.getCode().equals("200")) {
                    return false;
                }
            }

            else if (CONTACTSTRAIN.equals("5")) {
                map.put("type", "interview");
                map.put("uid", uid);
                urlStr = UrlUtil.getUrlByMap1(Constant.DANGANARCHIVESWORKING, map);
                CustomLog.e("WorkUrlc", urlStr);
                jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
                // CustomLog.e("UserURL", urlStr);
                // mResultInfoVO = gson.fromJson(jsonUrlStr,
                // UserInfoResultVO.class);
                // mResultVO = gson.fromJson(jsonUrlStr, ResultVO.class);
                dtinfointenview = gson.fromJson(jsonUrlStr, DanganWorkInterview.class);
                dtinfoarrayintentview = dtinfointenview.getArray();
                if (!dtinfointenview.getCode().equals("200")) {
                    return false;
                }
            }

            else if (CONTACTSTRAIN.equals("6")) {
                map.put("type", "license");
                map.put("uid", uid);
                urlStr = UrlUtil.getUrlByMap1(Constant.DANGANARCHIVESWORKING, map);
                CustomLog.e("WorkUrlc", urlStr);
                jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
                // CustomLog.e("UserURL", urlStr);
                // mResultInfoVO = gson.fromJson(jsonUrlStr,
                // UserInfoResultVO.class);
                // mResultVO = gson.fromJson(jsonUrlStr, ResultVO.class);
                dtinfolisence = gson.fromJson(jsonUrlStr, DanganWorkLicense.class);
                dtinfoarraylisence = dtinfolisence.getArray();
                if (!dtinfolisence.getCode().equals("200")) {
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
        // TODO Auto-generated method stub
        super.executeFailure();
    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
        bsresflistview.setVisibility(View.VISIBLE);
        if (CONTACTSTRAIN.equals("1")) {
            bsresflistview.setAdapter(dcapter);
            dcapter.mList = dtinfoarray;
            dcapter.notifyDataSetChanged();
        } else if (CONTACTSTRAIN.equals("2")) {
            bsresflistview.setAdapter(dcapter1);
            dcapter1.mList = dtinfoarraytranfer;
            dcapter1.notifyDataSetChanged();
        } else if (CONTACTSTRAIN.equals("3")) {
            bsresflistview.setAdapter(dcapter2);
            dcapter2.mList = dtinfoarrayuserpay;
            dcapter2.notifyDataSetChanged();
        } else if (CONTACTSTRAIN.equals("4")) {
            bsresflistview.setAdapter(dcapter3);
            dcapter3.mList = dtinfoarrayrewards;
            dcapter3.notifyDataSetChanged();
        } else if (CONTACTSTRAIN.equals("5")) {
            bsresflistview.setAdapter(dcapter4);
            dcapter4.mList = dtinfoarrayintentview;
            dcapter4.notifyDataSetChanged();
        } else if (CONTACTSTRAIN.equals("6")) {
            bsresflistview.setAdapter(dcapter5);
            dcapter5.mList = dtinfoarraylisence;
            dcapter5.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        // case R.id.top_changedate_last:
        // // 后期拓展又是一堆麻烦事，根据判断 调用传递不同的参数类型
        // CustomToast.showShortToast(getApplicationContext(),
        // "暂时没有2014年的数据....");
        // break;
        // case R.id.top_changedate_next:
        // // 后期拓展又是一堆麻烦事，根据判断 调用传递不同的参数类型
        // CustomToast.showShortToast(getApplicationContext(),
        // "暂时没有2016年的数据....");
        // break;
            case R.id.img_head_back:
                this.finish();
                break;

        }

    }

}
