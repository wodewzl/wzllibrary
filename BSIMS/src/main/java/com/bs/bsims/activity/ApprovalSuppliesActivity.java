
package com.bs.bsims.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.OvertimeResultVO;
import com.bs.bsims.model.OvertimeVO;
import com.bs.bsims.model.SuppliesItemVO;
import com.bs.bsims.time.ScreenInfo;
import com.bs.bsims.time.WheelMain;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSGridView;
import com.bs.bsims.view.BSMoveLayout;
import com.bs.bsims.view.BSMoveLayout.DeleteListeren;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ApprovalSuppliesActivity extends Activity implements OnClickListener, UpdateCallback, OnItemClickListener {
    private static final int ADD_INFORM_PERSON = 10;
    private BSGridView mApproverGv, mInformGv;
    private HeadAdapter mApproverAdapter, mInformAdapter;
    private WheelMain wheelMain;
    private LinearLayout mApplyTimeLayout, mAddItemLayout;
    private TextView mTimeTv;

    private ListView mListView;
    private MyAdapter adapter;
    private TextView mTotalMoney;
    private float mMoney;

    private OvertimeVO mOvertimeVO;

    private StringBuffer mApprovalPerson, mInformPerson, mNameItem, mNumItem, mPrice, mUnit;
    private LinearLayout mApproverLayout, mInformLayout;

    private TextView mApproverTv, mApprovalGoTv, mInformTv, mInformGoTv, mCancel, mSure, mReasonTv, mTitleTv;
    private TextView mBackImg;

    // 添加员工使用
    protected List<EmployeeVO> mDataList = new ArrayList<EmployeeVO>();
    private BSDialog dialog;
    private boolean mFlag = true;
    private OvertimeResultVO mOvertimeResultVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approval_supplies);
        initView();
        initData();
        bindViewsListener();
    }

    public void initView() {
        mTitleTv = (TextView) findViewById(R.id.txt_comm_head_activityName);
        mTitleTv.setText("物资申请");
        mBackImg = (TextView) findViewById(R.id.img_head_back);
        mApplyTimeLayout = (LinearLayout) findViewById(R.id.apply_time_layout);
        mTimeTv = (TextView) findViewById(R.id.time_tv);
        mAddItemLayout = (LinearLayout) findViewById(R.id.add_item);
        mListView = (ListView) findViewById(R.id.listview);
        mTotalMoney = (TextView) findViewById(R.id.total_money);
        mReasonTv = (TextView) findViewById(R.id.reason_tv);

        mApproverTv = (TextView) findViewById(R.id.approver_tv);
        mInformTv = (TextView) findViewById(R.id.inform_people_tv);
        mApproverLayout = (LinearLayout) findViewById(R.id.approver_layout);
        mInformLayout = (LinearLayout) findViewById(R.id.inform_people_layout);
        mCancel = (TextView) findViewById(R.id.cancel);
        mSure = (TextView) findViewById(R.id.sure);
        mApproverGv = (BSGridView) findViewById(R.id.approver_gv);
        mInformGv = (BSGridView) findViewById(R.id.inform_gv);
        mApproverAdapter = new HeadAdapter(this, false);
        mInformAdapter = new HeadAdapter(this, true);
        mApproverGv.setAdapter(mApproverAdapter);
        mInformGv.setAdapter(mInformAdapter);

        mApprovalPerson = new StringBuffer();
        mInformPerson = new StringBuffer();

        mNameItem = new StringBuffer();
        mNumItem = new StringBuffer();
        mPrice = new StringBuffer();
        mUnit = new StringBuffer();

        mListView = (ListView) findViewById(R.id.listview);
        adapter = new MyAdapter();
        mListView.setAdapter(adapter);
        try {
            // 透明状态栏 5.0以后才可以显示
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void initData() {

    }

    public void bindViewsListener() {
        mApplyTimeLayout.setOnClickListener(this);
        mAddItemLayout.setOnClickListener(this);
        mTotalMoney.setOnClickListener(this);
        mSure.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mBackImg.setOnClickListener(this);
    }

    public void initDateView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(this);
        wheelMain = new WheelMain(timepickerview);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelMain.initDateTimePicker(year, month, day, hour, minute);

        dialog = new BSDialog(this, "请选择日期", timepickerview, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mTimeTv.setText(wheelMain.getTime());
                dialog.dismiss();

            }
        });
        dialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_item:
                LayoutInflater inflater = LayoutInflater.from(this);
                final View addSupplies = inflater.inflate(R.layout.add_supplies, null);
                final EditText name = (EditText) addSupplies.findViewById(R.id.name);
                final EditText momey = (EditText) addSupplies.findViewById(R.id.money);
                final EditText number = (EditText) addSupplies.findViewById(R.id.number_tv);
                final EditText unit = (EditText) addSupplies.findViewById(R.id.unit_tv);

                dialog = new BSDialog(this, "添加数据", addSupplies, new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (name.getText().toString().length() == 0 || momey.getText().toString().length() == 0 || number.getText().toString().length() == 0 || unit.getText().toString().length() == 0) {
                            CustomToast.showLongToast(ApprovalSuppliesActivity.this, "请把数据填写完整");
                            return;
                        }
                        DecimalFormat df = new DecimalFormat("#.00");

                        SuppliesItemVO vo = new SuppliesItemVO();
                        vo.setName(name.getText().toString());
                        vo.setPrice(Float.parseFloat(momey.getText().toString()) + "");
                        vo.setUnit(unit.getText().toString());
                        vo.setNum(Integer.parseInt(number.getText().toString()) + "");
                        if (name.getText().length() == 0 || Float.parseFloat(momey.getText().toString()) <= 0 || Integer.parseInt(number.getText().toString()) <= 0 || unit.getText().length() == 0) {
                            CustomToast.showLongToast(ApprovalSuppliesActivity.this, "不能为空或数量正常");
                            return;
                        }
                        adapter.list.add(vo);
                        adapter.notifyDataSetChanged();
                        mMoney = 0;
                        for (int i = 0; i < adapter.list.size(); i++) {
                            float one = Float.parseFloat(adapter.list.get(i).getPrice());
                            mMoney += one * Integer.parseInt(adapter.list.get(i).getNum());
                        }

                        mTotalMoney.setText(df.format(mMoney) + "");
                        new ThreadUtil(ApprovalSuppliesActivity.this, ApprovalSuppliesActivity.this).start();
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;
            case R.id.apply_time_layout:

                initDateView();
                break;

            case R.id.cancel:
                this.finish();
                break;
            case R.id.sure:
                if (mReasonTv.getText().length() == 0) {
                    CustomToast.showLongToast(this, "申请原因没填写");
                    return;
                }
                if (mTimeTv.getText().length() == 0) {
                    CustomToast.showLongToast(this, "时间没有填写");
                    return;
                }
                if (adapter.list.size() == 0) {
                    CustomToast.showLongToast(this, "物资没有添加");
                    return;
                }

                if (mApprovalPerson.length() == 0) {
                    CustomToast.showLongToast(this, "由于你的权限过高，无法发布此审批");
                    return;
                }
                if (mFlag) {
                    mFlag = false;
                    commit();
                }

                break;
            case R.id.img_head_back:
                finish();
                break;
            default:
                break;
        }
    }

    public boolean getData() {
        try {
            String strUlr = UrlUtil.getSuppliesUrl(Constant.APPROVAL_SUPPLIES_URL, BSApplication.getInstance().getUserId(), mTotalMoney.getText().toString());
            String jsonStr = HttpClientUtil.get(strUlr, Constant.ENCODING).trim();
            Gson gson = new Gson();
            mOvertimeResultVO = gson.fromJson(jsonStr, OvertimeResultVO.class);
            if (Constant.RESULT_CODE.equals(mOvertimeResultVO.getCode())) {
                mOvertimeVO = mOvertimeResultVO.getArray();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean commit() {
        CustomDialog.showProgressDialog(this, "正在提到数据...");
        mNameItem.setLength(0);
        mNumItem.setLength(0);
        mPrice.setLength(0);
        mUnit.setLength(0);
        for (int i = 0; i < adapter.list.size(); i++) {
            mNameItem.append(adapter.list.get(i).getName());
            mNumItem.append(adapter.list.get(i).getNum());
            mPrice.append(adapter.list.get(i).getPrice());
            mUnit.append(adapter.list.get(i).getUnit());
            if (i != adapter.list.size() - 1) {
                mNameItem.append("|");
                mNumItem.append("|");
                mPrice.append("|");
                mUnit.append("|");
            }
        }

        RequestParams params = new RequestParams();

        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("total", mTotalMoney.getText().toString());
            params.put("atime", mTimeTv.getText().toString().replace(" ", "").split(":")[0] + ":00");
            params.put("reason", mReasonTv.getText());
            params.put("name", mNameItem.toString());
            params.put("price", mPrice.toString());
            params.put("num", mNumItem.toString());
            params.put("unit", mUnit.toString());
            params.put("totalprice", mTotalMoney.getText().toString());
            params.put("info", "");
            params.put("approver", mApprovalPerson.toString());
            params.put("insider", mInformPerson.toString());

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = BSApplication.getInstance().getHttpTitle() + Constant.APPROVAL_SUPPLIES_ADD_URL;
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
                    if (Constant.RESULT_CODE.equals(code)) {
                        ApprovalSuppliesActivity.this.finish();
                        CustomToast.showShortToast(ApprovalSuppliesActivity.this, str);
                    } else {
                        CustomToast.showShortToast(ApprovalSuppliesActivity.this, str);
                    }
                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    mFlag = true;
                }
            }

        });
        return true;
    }

    class MyAdapter extends BaseAdapter {

        public List<SuppliesItemVO> list;

        public MyAdapter() {
            list = new ArrayList<SuppliesItemVO>();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                View view = View.inflate(ApprovalSuppliesActivity.this, R.layout.lv_supplies_itme, null);
                convertView = new BSMoveLayout(ApprovalSuppliesActivity.this, true, view, listeren);
                holder.text01 = (TextView) convertView.findViewById(R.id.text01);
                holder.text02 = (TextView) convertView.findViewById(R.id.text02);
                holder.text03 = (TextView) convertView.findViewById(R.id.text03);
                holder.text04 = (TextView) convertView.findViewById(R.id.text04);
                holder.delete = (TextView) convertView.findViewById(R.id.delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            SuppliesItemVO vo = list.get(position);
            holder.text01.setText(vo.getName());
            holder.text02.setText(vo.getPrice());
            holder.text03.setText(vo.getNum());
            holder.text04.setText(vo.getUnit());
            holder.delete.setTag(position + "");

            return convertView;
        }

        DeleteListeren listeren = new DeleteListeren() {

            @Override
            public void deleteItem(int position) {

                list.remove(position);
                notifyDataSetChanged();
                if (adapter.list.size() == 0) {
                    mTotalMoney.setText("");
                    mApproverAdapter.mList.clear();
                    mApproverTv.setVisibility(View.GONE);
                    mApproverLayout.setVisibility(View.GONE);
                    return;
                }

                mMoney = 0;
                for (int i = 0; i < adapter.list.size(); i++) {
                    float one = Float.parseFloat(adapter.list.get(i).getPrice());
                    mMoney += one * Integer.parseInt(adapter.list.get(i).getNum());
                }
                mTotalMoney.setText(mMoney + "");
                if (adapter.list.size() == 0) {
                    mTotalMoney.setText("");
                }

                new ThreadUtil(ApprovalSuppliesActivity.this, ApprovalSuppliesActivity.this).start();

            }
        };

        class ViewHolder {
            private TextView delete, text01, text02, text03, text04;
        }

        private class ItemDeleteListeners implements OnClickListener {
            private SuppliesItemVO itemVO;

            public ItemDeleteListeners(SuppliesItemVO itemVO) {
                this.itemVO = itemVO;
            }

            @Override
            public void onClick(View v) {
            }
        }
    }

    public void updateUi() {
        if (mOvertimeVO.getAppUser() != null) {
            mApprovalPerson.setLength(0);
            mApproverAdapter.mList.clear();
            for (int i = 0; i < mOvertimeVO.getAppUser().size(); i++) {
                mApprovalPerson.append(mOvertimeVO.getAppUser().get(i).getUserid());
                if (i != mOvertimeVO.getAppUser().size() - 1) {
                    mApprovalPerson.append(",");
                }
            }

            mApproverTv.setVisibility(View.VISIBLE);
            mApproverLayout.setVisibility(View.VISIBLE);
            mApproverAdapter.setApproval(true);
            mApproverAdapter.updateData(mOvertimeVO.getAppUser());
        } else {
            mApproverTv.setVisibility(View.GONE);
            mApproverLayout.setVisibility(View.GONE);
        }

        mInformPerson.setLength(0);
        mInformAdapter.mList.clear();
        mInformAdapter.notifyDataSetChanged();
        if (mOvertimeVO.getInsUser() != null) {
            for (int i = 0; i < mOvertimeVO.getInsUser().size(); i++) {
                mInformPerson.append(mOvertimeVO.getInsUser().get(i).getUserid());
                if (i != mOvertimeVO.getInsUser().size() - 1) {
                    mInformPerson.append(",");
                }
            }
            mInformTv.setVisibility(View.VISIBLE);
            mInformLayout.setVisibility(View.VISIBLE);
            mInformAdapter.updateData(mOvertimeVO.getInsUser());
        }
    }

    @Override
    public boolean execute() {
        return getData();
    }

    @Override
    public void executeSuccess() {
        updateUi();
    }

    @Override
    public void executeFailure() {
        if (mOvertimeResultVO != null)
            CustomToast.showLongToast(this, mOvertimeResultVO.getRetinfo());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View arg0, int arg1, long arg2) {
        if (parent == mInformGv) {
            if (arg2 == mInformAdapter.mList.size()) {
                Intent intent = new Intent();
                intent.setClass(this, AddByDepartmentActivity.class);
                // intent.putExtra("checkboxlist", (Serializable) mDataList);
                intent.putExtra("employ_name", JournalPublishActivity.class);
                intent.putExtra("requst_number", 10);
                startActivityForResult(intent, 10);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_INFORM_PERSON:
                if (requestCode == 10) {
                    if (data == null)
                        return;
                    mDataList.clear();
                    // mParent.removeAllViews();
                    mDataList = (List<EmployeeVO>) data.getSerializableExtra("checkboxlist");
                    // mTransferAdapter.updateData(mDataList);
                    mInformAdapter.mList.clear();
                    mInformAdapter.mList.addAll(mDataList);
                    mInformAdapter.notifyDataSetChanged();
                    mInformPerson.setLength(0);
                    for (int i = 0; i < mDataList.size(); i++) {
                        mInformPerson.append(mDataList.get(i).getUserid());
                        if (i != mDataList.size() - 1) {
                            mInformPerson.append(",");
                        }
                    }
                }
                break;

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }
}
