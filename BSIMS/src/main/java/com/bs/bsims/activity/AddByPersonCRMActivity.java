
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmBussinesAdpterListChange;
import com.bs.bsims.adapter.CrmOneTreeCheckAdapter;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.fragment.ContactsLetterTabFragment;
import com.bs.bsims.model.CrmContactVo;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSListView;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddByPersonCRMActivity extends BaseActivity {
    private ResultVO mResultInfoVO;
    private ContactsLetterTabFragment mFragment;
    private String cid = "";
    private String cname = "";
    private String type = "";// 获取商机的负责人

    private CrmBussinesAdpterListChange crmbuslistadpter;
    private CrmOneTreeCheckAdapter crmoneadpter;

    private String proKey = "";// 0表示进入联系人列表，1表示进入商机阶段列表

    private Context mContext;
    public List<CrmContactVo> mList;
    private CrmContactVo contactVo;
    private String bid = "";
    private String subordinate;
    private BSListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void baseSetContentView() {
        View layout = null;
        proKey = getIntent().getStringExtra("proKey");
        if (proKey.equals("0")) {
            layout = View.inflate(this, R.layout.add_by_person, null);
        }
        else {
            layout = View.inflate(this, R.layout.add_by_person1, null);
        }

        mContentLayout.addView(layout);
        mContext = this;

    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
        initData();
    }

    @Override
    public void initView() {
        if (proKey.equals("0")) {
            mTitleTv.setText("请选择商机交接人");
            if (null == getIntent().getStringExtra("typekey")) {
                type = "";
            }
            else {
                type = getIntent().getStringExtra("typekey");
            }
            if (type.equals("6")) {

                if (null != getIntent().getStringExtra("bid")) {
                    bid = getIntent().getStringExtra("bid");
                    mTitleTv.setText("请选择商机负责人");
                }
                else {
                    mTitleTv.setText("请选择人员");
                }

            }
        }
        else if (proKey.equals("8")) {
            if (null == getIntent().getStringExtra("typekey")) {
                type = "";
            }
            else {
                type = getIntent().getStringExtra("typekey");
            }
            if (type.equals("1")) {
                mTitleTv.setText("请选择拜访方式");
            }
            else if (type.equals("2")) {
                mTitleTv.setText("请选择拜访目的");
            }
            else if (type.equals("4")) {
                mTitleTv.setText("关联商机");
            }
            else if (type.equals("5")) {
                mTitleTv.setText("关联合同");
            }
            mList = new ArrayList<CrmContactVo>();
            crmbuslistadpter = new CrmBussinesAdpterListChange(mContext, mList);
            crmbuslistadpter.setStatekey("1");// 拜访方式用和销售阶段列表相同
            listview = (BSListView) findViewById(R.id.listview);
            listview.setAdapter(crmbuslistadpter);
            mOkTv.setText("确认");
            mOkTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (crmbuslistadpter.getId().trim().equals("")) {
                        CustomToast.showLongToast(mContext, "请选择内容");
                    }
                    else {

                        if (type.equals("1")) {
                            // 返回到上一个activity的把姓名和电话带过去
                            Intent mIntent = new Intent();
                            // mIntent.putExtra("name",
                            // crmbuslistadpter.getName().trim().toString());
                            // mIntent.putExtra("phone",
                            // crmbuslistadpter.getPhone().trim().toString());
                            mIntent.putExtra("nameVistor", crmbuslistadpter.getName());
                            mIntent.putExtra("sid", crmbuslistadpter.getId().trim().toString());
                            // 设置结果，并进行传送
                            setResult(2015, mIntent);
                        }
                        else if (type.equals("2")) {
                            Intent mIntent = new Intent();
                            // mInte nt.putExtra("name",
                            // crmbuslistadpter.getName().trim().toString());
                            // mIntent.putExtra("phone",
                            // crmbuslistadpter.getPhone().trim().toString());
                            mIntent.putExtra("nameVistorbwan", crmbuslistadpter.getName());
                            mIntent.putExtra("aid", crmbuslistadpter.getId().trim().toString());
                            // 设置结果，并进行传送
                            setResult(2016, mIntent);
                        }

                        else if (type.equals("4")) {
                            Intent mIntent = new Intent();
                            // mIntent.putExtra("name",
                            // crmbuslistadpter.getName().trim().toString());
                            // mIntent.putExtra("phone",
                            // crmbuslistadpter.getPhone().trim().toString());
                            mIntent.putExtra("bs_name", crmbuslistadpter.getName());
                            mIntent.putExtra("bs_id", crmbuslistadpter.getId().trim().toString());
                            mIntent.putExtra("bstatus", crmbuslistadpter.getBstatus().trim()
                                    .toString());
                            mIntent.putExtra("pr_id", crmbuslistadpter.getProduct().trim()
                                    .toString());
                            mIntent.putExtra("pr_name", crmbuslistadpter.getProname().trim()
                                    .toString());
                            mIntent.putExtra("pr_money", crmbuslistadpter.getMoney().trim()
                                    .toString());
                            // 设置结果，并进行传送
                            setResult(84, mIntent);
                        }
                        else if (type.equals("5")) {
                            Intent mIntent = new Intent();
                            // mIntent.putExtra("name",
                            // crmbuslistadpter.getName().trim().toString());
                            // mIntent.putExtra("phone",
                            // crmbuslistadpter.getPhone().trim().toString());
                            mIntent.putExtra("td_name", crmbuslistadpter.getName());
                            mIntent.putExtra("td_id", crmbuslistadpter.getId().trim().toString());
                            mIntent.putExtra("bstatus", crmbuslistadpter.getBstatus().trim()
                                    .toString());
                            // 设置结果，并进行传送
                            setResult(85, mIntent);
                        }

                        AddByPersonCRMActivity.this.finish();
                    }

                }
            });

        }
        else if (proKey.equals("7")) {
            listview = (BSListView) findViewById(R.id.listview);
            if (null == getIntent().getStringExtra("typekey")) {
                type = "";
            }
            else {
                type = getIntent().getStringExtra("typekey");
            }
            if (type.equals("1")) {// 商机下选择客户
                mTitleTv.setText("请选择客户");
            }
            else if (type.equals("-1")) {// 跟单记录下选择客户
                mTitleTv.setText("请选择客户");
            }
            else if (type.equals("3")) {
                mTitleTv.setText("请选择商机类型");
            }
            else if (type.equals("4")) {
                mTitleTv.setText("请选择商机来源");
            }
            else if (type.equals("5")) {
                mTitleTv.setText("请选择产品");
                mList = new ArrayList<CrmContactVo>();
                crmoneadpter = new CrmOneTreeCheckAdapter(mContext, mList);
                listview.setAdapter(crmoneadpter);
                mOkTv.setText("确认");
                mOkTv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // 选择产品
                        if (null == crmoneadpter.mList) {
                            return;
                        }

                        // String prId[] = new String[crmoneadpter.mList.size()];
                        // String prName[] = new String[crmoneadpter.mList.size()];
                        StringBuffer prId = new StringBuffer();
                        StringBuffer prName = new StringBuffer();
                        for (int i = 0; i < crmoneadpter.mList.size(); i++) {
                            if (crmoneadpter.mList.get(i).getFalgecontant().equals("1")) {
                                // prId[i] = crmoneadpter.mList.get(i).getId();
                                // prName[i] = crmoneadpter.mList.get(i).getName();
                                prId.append(crmoneadpter.mList.get(i).getId() + ",");
                                prName.append(crmoneadpter.mList.get(i).getName() + ",");
                            }
                            else {
                                continue;
                            }
                        }

                        if (prId == null || prId.equals("")) {
                            CustomToast.showLongToast(mContext, "请选择内容!");
                        }
                        else {
                            Intent mIntent = new Intent();
                            mIntent.putExtra("name", prName.toString());
                            mIntent.putExtra("id", prId.toString());
                            // 设置结果，并进行传送
                            setResult(2018, mIntent);
                            AddByPersonCRMActivity.this.finish();
                        }

                        return;
                    }
                });
                return;
            }
            else if (type.equals("7")) {
                mTitleTv.setText("请选择拜访人");
                cname = getIntent().getStringExtra("cname");
                mOkTv.setText("添加");
                mList = new ArrayList<CrmContactVo>();
                crmbuslistadpter = new CrmBussinesAdpterListChange(mContext, mList, AddByPersonCRMActivity.this);
                crmbuslistadpter.setStatekey("7");// 联系人用
                crmbuslistadpter.setVperson(true);
                listview.setAdapter(crmbuslistadpter);
                mOkTv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        // 跳转到添加联系人
                        Intent i = new Intent();
                        i.setClass(AddByPersonCRMActivity.this, CrmClientAddContactsActivity.class);
                        i.putExtra("type", "2");
                        i.putExtra("cid", cid);
                        i.putExtra("cname", cname);
                        i.putExtra("requst_number", 2017);
                        startActivityForResult(i, 2017);
                    }
                });

                return;

            }
            else if (type.equals("9")) {
                mTitleTv.setText("请选择支付方式");
            }
            else {
                mTitleTv.setText("请选择联系人");
            }
            mList = new ArrayList<CrmContactVo>();
            crmbuslistadpter = new CrmBussinesAdpterListChange(mContext, mList);
            crmbuslistadpter.setStatekey("7");// 联系人用
            listview.setAdapter(crmbuslistadpter);
            mOkTv.setText("确认");
            mOkTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (crmbuslistadpter.getId().trim().equals("")) {
                        CustomToast.showLongToast(mContext, "请选择内容!");
                    }
                    else {
                        Intent mIntent = new Intent();
                        if (type.equals("1")) {
                            mIntent.putExtra("name", crmbuslistadpter.getName().trim().toString());
                            mIntent.putExtra("pid", crmbuslistadpter.getId().trim().toString());
                            setResult(2014, mIntent);
                            AddByPersonCRMActivity.this.finish();
                            return;
                        }
                        else if (type.equals("-1")) {
                            mIntent.putExtra("name", crmbuslistadpter.getName().trim().toString());
                            mIntent.putExtra("pid", crmbuslistadpter.getId().trim().toString());
                            setResult(2014, mIntent);
                            AddByPersonCRMActivity.this.finish();
                            return;
                        }
                        else if (type.equals("3")) {
                            // 返回到上一个activity的把姓名和电话带过去
                            mIntent.putExtra("name", crmbuslistadpter.getName().trim().toString());
                            mIntent.putExtra("id", crmbuslistadpter.getId().trim().toString());
                            // 设置结果，并进行传送
                            setResult(2016, mIntent);
                            AddByPersonCRMActivity.this.finish();
                            return;
                        }

                        else if (type.equals("4")) {
                            mIntent.putExtra("name", crmbuslistadpter.getName().trim().toString());
                            mIntent.putExtra("id", crmbuslistadpter.getId().trim().toString());
                            // 设置结果，并进行传送
                            setResult(2017, mIntent);
                            AddByPersonCRMActivity.this.finish();
                            return;
                        }
                        // 获取支付方式
                        else if (type.equals("9")) {
                            mIntent.putExtra("name", crmbuslistadpter.getName().trim().toString());
                            mIntent.putExtra("id", crmbuslistadpter.getId().trim().toString());
                            // 设置结果，并进行传送
                            setResult(2019, mIntent);
                            AddByPersonCRMActivity.this.finish();
                            return;
                        }
                        // 设置结果，并进行传送
                        // 返回到上一个activity的把姓名和电话带过去
                        else {
                            mIntent.putExtra("phone", crmbuslistadpter.getPhone().trim().toString());
                            mIntent.putExtra("name", crmbuslistadpter.getName().trim().toString());
                            mIntent.putExtra("pid", crmbuslistadpter.getId().trim().toString());
                            setResult(2020, mIntent);
                            AddByPersonCRMActivity.this.finish();
                        }

                    }

                }
            });
        }
        /*
         * 选择期次 把审批人也度取出来了。
         */
        else if (proKey.equals("9")) {
            mList = new ArrayList<CrmContactVo>();
            crmbuslistadpter = new CrmBussinesAdpterListChange(mContext, mList);
            crmbuslistadpter.setStatekey("7");// 联系人用
            listview = (BSListView) findViewById(R.id.listview);
            listview.setAdapter(crmbuslistadpter);
            mOkTv.setText("确认");
            mOkTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (crmbuslistadpter.getId().trim().equals("")) {
                        CustomToast.showLongToast(mContext, "请选择内容!");
                        return;
                    }
                    Intent mIntent = new Intent();
                    mIntent.putExtra("name", crmbuslistadpter.getName().trim().toString());
                    mIntent.putExtra("id", crmbuslistadpter.getId().trim().toString());
                    mIntent.putExtra("checkboxlist", (Serializable) contactVo.getAppUser());
                    setResult(2014, mIntent);
                    AddByPersonCRMActivity.this.finish();
                    return;
                }
            });
        }
        // 客户的，不需要加载数据
        else if (proKey.equals("-1")) {
            type = getIntent().getStringExtra("typekey");
            if (type.equals("1")) {
                mTitleTv.setText("请选择行业");

            }
            else if (type.equals("2")) {
                mTitleTv.setText("请选择级别");
            }
            else if (type.equals("3")) {
                mTitleTv.setText("请选择客户来源");

            } else if ("4".equals(type)) {
                mTitleTv.setText("请选联系人");
            }
            contactVo = (CrmContactVo) getIntent().getSerializableExtra("customerlist");
            mOkTv.setText("确认");
            mList = new ArrayList<CrmContactVo>();
            crmbuslistadpter = new CrmBussinesAdpterListChange(mContext, mList);
            crmbuslistadpter.setStatekey("-1");
            listview = (BSListView) findViewById(R.id.listview);
            listview.setAdapter(crmbuslistadpter);
            mOkTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (crmbuslistadpter.getId().trim().equals("")) {
                        CustomToast.showLongToast(mContext, "请选择内容!");
                    }
                    else {
                        Intent intent = new Intent();
                        intent.setClass(AddByPersonCRMActivity.this, CrmClientAddActivity.class);

                        // 选择行业
                        if (type.equals("1")) {

                            intent.putExtra("name", crmbuslistadpter.getName().trim().toString());
                            intent.putExtra("id", crmbuslistadpter.getId().trim().toString());
                            // 设置结果，并进行传送
                            setResult(2015, intent);
                            AddByPersonCRMActivity.this.finish();
                            return;
                        }
                        // 选择级别
                        else if (type.equals("2")) {
                            intent.putExtra("name", crmbuslistadpter.getName().trim().toString());
                            intent.putExtra("id", crmbuslistadpter.getId().trim().toString());
                            // 设置结果，并进行传送
                            setResult(2016, intent);
                            AddByPersonCRMActivity.this.finish();
                            return;
                        }
                        // 选择客户来源
                        else if (type.equals("3")) {
                            intent.putExtra("name", crmbuslistadpter.getName().trim().toString());
                            intent.putExtra("id", crmbuslistadpter.getId().trim().toString());
                            // 设置结果，并进行传送
                            setResult(2017, intent);
                            AddByPersonCRMActivity.this.finish();
                            return;
                        } else if ("4".equals(type)) {
                            intent.putExtra("name", crmbuslistadpter.getName().trim().toString());
                            intent.putExtra("id", crmbuslistadpter.getId().trim().toString());
                            // 设置结果，并进行传送
                            AddByPersonCRMActivity.this.startActivity(intent);
                            AddByPersonCRMActivity.this.finish();
                            return;
                        }
                    }

                }
            });

        }

        // prokey="1"
        else {
            mTitleTv.setText("请选择销售阶段");
            mOkTv.setText("确认");
            mList = new ArrayList<CrmContactVo>();
            crmbuslistadpter = new CrmBussinesAdpterListChange(mContext, mList);
            crmbuslistadpter.setStatekey("1");// 销售阶段使用
            bid = getIntent().getStringExtra("bid");
            if (null == getIntent().getStringExtra("typekey")) {
                type = "";
            }
            else {
                type = getIntent().getStringExtra("typekey");
            }

            listview = (BSListView) findViewById(R.id.listview);
            listview.setAdapter(crmbuslistadpter);
            mOkTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (crmbuslistadpter.getId().trim().equals("")) {
                        CustomToast.showLongToast(mContext, "请选择销售阶段");
                    }
                    else {
                        // 添加商机使用的
                        if (type.equals("2")) {
                            // 返回到上一个activity的把姓名和电话带过去
                            Intent mIntent = new Intent();
                            mIntent.putExtra("name", crmbuslistadpter.getName().trim().toString());
                            mIntent.putExtra("id", crmbuslistadpter.getId().trim().toString());
                            // 设置结果，并进行传送
                            setResult(2015, mIntent);
                            AddByPersonCRMActivity.this.finish();
                            return;
                        }

                    }

                }
            });

        }

    }

    @Override
    public void bindViewsListener() {

    }

    public void initData() {
        if (proKey.equals("0")) {
            try {
                mFragment = new ContactsLetterTabFragment(mResultInfoVO, true);
                mFragment.setCrmState("1");
                FragmentTransaction transaction = this.getSupportFragmentManager()
                        .beginTransaction();
                transaction.add(R.id.add_fragment, mFragment);
                transaction.commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 无张龙客户的
        else if (proKey.equals("-1")) {
            crmbuslistadpter.mList.clear();
            if (type.equals("1")) {
                crmbuslistadpter.mList = contactVo.getIndustry();
            }
            else if (type.equals("2")) {
                crmbuslistadpter.mList = contactVo.getLevel();
            }
            else if (type.equals("3")) {
                crmbuslistadpter.mList = contactVo.getSource();
            } else if ("4".equals(type)) {
                crmbuslistadpter.mList = contactVo.getArray();
            }

            for (int i = 0; i < crmbuslistadpter.mList.size(); i++) {
                crmbuslistadpter.mList.get(i).setFalgecontant("0");
            }
            crmbuslistadpter.notifyDataSetChanged();
        }
        else {

            // 产品多选 在这里判断下
            if (proKey.equals("7") && type.equals("5")) {
                crmoneadpter.mList.clear();
                crmoneadpter.mList = contactVo.getArray();
                String id[] = null;
                if (null != getIntent().getStringExtra("proselect")
                        && getIntent().getStringExtra("proselect").toString().trim().length() > 0) {
                    id = getIntent().getStringExtra("proselect").split(",");
                }

                if (null == id) {
                    for (int i = 0; i < crmoneadpter.mList.size(); i++) {
                        crmoneadpter.mList.get(i).setFalgecontant("0");
                    }

                }
                else {
                    for (int i = 0; i < crmoneadpter.mList.size(); i++) {
                        crmoneadpter.mList.get(i).setFalgecontant("0");
                    }
                    for (int i = 0; i < id.length; i++) {
                        for (int j = 0; j < crmoneadpter.mList.size(); j++) {
                            if (id[i].equals(crmoneadpter.mList.get(j).getId())) {
                                crmoneadpter.mList.get(j).setFalgecontant("1");
                                continue;
                            }
                        }
                    }

                }

                crmoneadpter.notifyDataSetChanged();
            }

            else {
                crmbuslistadpter.mList = contactVo.getArray();
                for (int i = 0; i < crmbuslistadpter.mList.size(); i++) {
                    crmbuslistadpter.mList.get(i).setFalgecontant("0");
                }
                crmbuslistadpter.notifyDataSetChanged();
            }

        }

    }

    public String getProKey() {
        return proKey;
    }

    public void setProKey(String proKey) {
        this.proKey = proKey;
    }

    public boolean getData() {
        if (proKey.equals("0")) {

            try {
                cid = getIntent().getStringExtra("cid");
                bid = getIntent().getStringExtra("bid");
                subordinate = getIntent().getStringExtra("subordinate");
            } catch (Exception exception) {
                bid = "";
                cid = "";
                subordinate = "";
            }
            Gson gson = new Gson();
            Map paramsMap = new HashMap<String, String>();
            paramsMap.put("type", type);
            paramsMap.put("cid", cid);
            paramsMap.put("bid", bid);
            paramsMap.put("subordinate", subordinate);
            // 判断是否来自于商机的新参数接口（商机负责人或跟进人只能在商机所属客户下的跟进人中选择）
            if (getIntent().getStringExtra("cid") != null && getIntent().getStringExtra("isfrom") != null) {
                paramsMap.put("cid", getIntent().getStringExtra("cid"));
                paramsMap.put("isfrom", getIntent().getStringExtra("isfrom"));
            }
            String urlStr;
            String jsonUrlStr;
            try {
                urlStr = UrlUtil.getUrlByMap1(Constant.CRM_GETPEROSONTOBUSSINESTREE, paramsMap);
                jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
                mResultInfoVO = gson.fromJson(jsonUrlStr, ResultVO.class);
                if (mResultInfoVO.getCode().equals("400")) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        // 只有电话和姓名的联系人
        else if (proKey.equals("7")) {
            String phpAdress = "";
            if (type.equals("-1")) {// 拜访记录下选择客户
                phpAdress = Constant.CRM_VISIT_GETCUSTOMER;
            }
            else {//
                phpAdress = Constant.CRM_GETPEROSONTOBUSSINES;
            }

            try {
                cid = getIntent().getStringExtra("cid");
            } catch (Exception exception) {
                cid = "";
            }
            Gson gson = new Gson();
            Map paramsMap = new HashMap<String, String>();
            paramsMap.put("type", type);
            paramsMap.put("cid", cid);
            String urlStr;
            String jsonUrlStr;
            try {

                urlStr = UrlUtil.getUrlByMap1(phpAdress, paramsMap);
                jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
                contactVo = gson.fromJson(jsonUrlStr, CrmContactVo.class);
                if (contactVo.getCode().equals("400")) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        // 拜访方式
        else if (proKey.equals("8")) {
            try {
                cid = getIntent().getStringExtra("cid");
            } catch (Exception exception) {
                cid = "";
            }
            Gson gson = new Gson();
            Map paramsMap = new HashMap<String, String>();
            paramsMap.put("type", type);
            paramsMap.put("cid", cid);
            String urlStr;
            String jsonUrlStr;
            try {
                urlStr = UrlUtil.getUrlByMap1(Constant.CRM_GETVISTITORTOBUSSINES, paramsMap);
                jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
                contactVo = gson.fromJson(jsonUrlStr, CrmContactVo.class);
                if (contactVo.getCode().equals("400")) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        // 回款记录的期次
        else if (proKey.equals("9")) {
            Gson gson = new Gson();
            Map paramsMap = new HashMap<String, String>();
            paramsMap.put("hid", getIntent().getStringExtra("hid"));
            String urlStr;
            String jsonUrlStr;
            try {
                urlStr = UrlUtil.getUrlByMap1(Constant.CRM_RECORDE_PERSONCIINFO, paramsMap);
                jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
                contactVo = gson.fromJson(jsonUrlStr, CrmContactVo.class);
                if (contactVo.getCode().equals("400")) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        // 给无张龙的客户用的
        else if (proKey.equals("-1")) {
            Gson gson = new Gson();
            Map paramsMap = new HashMap<String, String>();
            try {
                String urlStr = UrlUtil.getUrlByMap1(Constant.CRM_CREATE_CLIENT_FROM_CONTACTS,
                        paramsMap);
                String jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
                contactVo = gson.fromJson(jsonUrlStr, CrmContactVo.class);
                if (contactVo.getCode().equals(Constant.RESULT_CODE400)) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        else {
            Gson gson = new Gson();
            Map paramsMap = new HashMap<String, String>();
            paramsMap.put("type", "2");
            String urlStr;
            String jsonUrlStr;
            try {
                urlStr = UrlUtil.getUrlByMap1(Constant.CRM_GETPEROSONTOBUSSINES, paramsMap);
                jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
                contactVo = gson.fromJson(jsonUrlStr, CrmContactVo.class);
                if (contactVo.getCode().equals("400")) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    @Override
    public void executeFailure() {
        // TODO Auto-generated method stub
        if (null != contactVo) {
            CommonUtils.setNonetIcon1(mContext, mLoading, "没有相关信息");
            return;
        }
        CommonUtils.setNonetIcon(mContext, mLoading, this);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        switch (arg0) {
            case 2017:
                if (arg1 == 1 && null != arg2) {
                    new ThreadUtil(this, this).start();
                }
                break;

            default:
                break;
        }
    }
}
