
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.CrmClientTrendVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("ValidFragment")
// 商机详情
public class CrmBussinesFragment3 extends BaseFragment implements
        OnClickListener, UpdateCallback {

    private BSRefreshListView mRefreshListView;
    private CrmClientTrendAdapterCopy mCrmListAdapter;
    private CrmClientTrendVO mClientTrendVO;

    // 下拉上拉刷新部分
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private int mState = 0; // 0为首次,1为上拉刷新 ，2为下拉刷新

    private boolean mFlag = true;
    private String mFristid, mLastid;
    private List<View> mViewList;// 筛选选中的布局
    private BSIndexEditText mBSBsIndexEditText;
    private String bid;

    private Activity mActivity;

    public CrmBussinesFragment3(String uid) {
        this.bid = uid;
    }

    public CrmBussinesFragment3() {
    }

    // DanganChuQingInfoLineChartActivity
    @Override
    public String getFragmentName() {
        // TODO Auto-generated method stub
        return null;
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
        View view = inflater.inflate(R.layout.crm_client_trend, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        bindListers();
    }

    private void bindListers() {
        // TODO Auto-generated method stub
        mRefreshListView.setonRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mCrmListAdapter.mList.size() > 0) {
                    match(1, mCrmListAdapter.mList.get(0).getTlid());
                } else {
                    mFristid = "";
                    match(1, "");
                }
            }
        });
        mFootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoreTextView.setText("正在加载...");
                mProgressBar.setVisibility(View.VISIBLE);
                match(2, mCrmListAdapter.mList.get(mCrmListAdapter.mList.size() - 1).getTlid());
            }
        });
    }

    public void match(int key, String value) {

        switch (key) {
            case 1:
                mFristid = value;
                mLastid = "";
                mState = 1;
                break;
            case 2:
                mLastid = value;
                mFristid = "";
                mState = 2;
                break;
            default:
                break;
        }
        mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(mActivity, CrmBussinesFragment3.this).start();
    }

    private void initViews(View view) {
        // TODO Auto-generated method stub
        mRefreshListView = (BSRefreshListView) view.findViewById(R.id.lv_refresh);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);  
        lp.setMargins(0, 0, 0, 0);  
        mRefreshListView.setLayoutParams(lp);  
        mCrmListAdapter = new CrmClientTrendAdapterCopy(mActivity);
        mCrmListAdapter.mList = new ArrayList<CrmClientTrendVO>();
        mRefreshListView.setAdapter(mCrmListAdapter);
        initFoot();
    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(mActivity).inflate(R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        mRefreshListView.addFooterView(mFootLayout);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initData();
        mRefreshListView
                .changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(mActivity, this).start();
    }

    @Override
    public boolean execute() {
        // TODO Auto-generated method stub
        return getdata();
    }

    public void footViewIsVisibility() {
        if (mClientTrendVO == null || mClientTrendVO.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mClientTrendVO.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void executeSuccess() {
        // TODO Auto-generated method stub
        if (1 == mState) {
            mCrmListAdapter.updateDataFrist(mClientTrendVO.getArray());
        } else if (2 == mState) {
            mCrmListAdapter.updateDataLast(mClientTrendVO.getArray());
        } else {
            mCrmListAdapter.updateData(mClientTrendVO.getArray());
        }

        mCrmListAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        if ("".equals(mFristid) || mState != 1) {
            footViewIsVisibility();
        }
        mState = 0;
    }

    @Override
    public void executeFailure() {
        // TODO Auto-generated method stub
        if (null == mClientTrendVO || mCrmListAdapter.mList == null
                || mCrmListAdapter.mList.size() == 0) {
            mCrmListAdapter.updateData(new ArrayList<CrmClientTrendVO>());
        }

        mRefreshListView.onRefreshComplete();
        if ("".equals(mFristid) || mState != 1) {
            footViewIsVisibility();
        }
        mState = 0;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    public boolean getdata() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            if (0 == mState) {
                mFristid = "";
                mLastid = "";
            }
            map.put("firstid", mFristid);
            map.put("lastid", mLastid);
            map.put("bid", bid);

            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance()
                    .getHttpTitle() + Constant.CRM_BUSSINES_ZONE, map);
            mClientTrendVO = gson.fromJson(jsonStrList, CrmClientTrendVO.class);
            if (Constant.RESULT_CODE.equals(mClientTrendVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public class CrmClientTrendAdapterCopy extends BaseAdapter {
        private Context mContext;
        private List<CrmClientTrendVO> mList;

        public CrmClientTrendAdapterCopy(Context context) {
            mContext = context;
            this.mList = new ArrayList<CrmClientTrendVO>();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            String date = "";
            GradientDrawable bgdraw = null;

            CrmClientTrendVO vo = mList.get(position);
            if (convertView != null && convertView.getTag() == null)
                convertView = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.crm_client_trend_lv, null);
                holder.mTimeTv = (TextView) convertView.findViewById(R.id.time_tv);
                holder.mText01Tv = (TextView) convertView.findViewById(R.id.text01);
                holder.mText02Tv = (TextView) convertView.findViewById(R.id.text02);
                holder.mText03Tv = (TextView) convertView.findViewById(R.id.text03);
                holder.typeIv = (ImageView) convertView.findViewById(R.id.head_iconbasic);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mText01Tv.setText(vo.getOpname());
            if (vo.getContent().contains("：") && vo.getContent().split("：").length >= 2) {
                String content1 = vo.getContent().split("：")[0];
                String content2 = vo.getContent().split("：")[1];
                CommonUtils.setDifferentTextColor(holder.mText02Tv, content1 + "：", content2,
                        "#00a9fe");
            } else {
                holder.mText02Tv.setText(vo.getContent());

            }

            holder.mText03Tv.setText(DateUtils.parseHour(vo.getAddtime()));

            switch (Integer.parseInt(vo.getIcon())) {
                case 1:
                    holder.typeIv.setImageResource(R.drawable.crm_bszone_customer);
                    break;
                case 2:
                    holder.typeIv.setImageResource(R.drawable.crm_bszone_person);

                    break;
                case 3:
                    holder.typeIv.setImageResource(R.drawable.crm_bszone_money);

                    break;
                case 4:
                    holder.typeIv.setImageResource(R.drawable.crm_bszone_product);

                    break;
                case 5:
                    holder.typeIv.setImageResource(R.drawable.crm_bszone_save);

                    break;
                case 6:
                    holder.typeIv.setImageResource(R.drawable.crm_bszone_getpay);
                    break;

                default:
                    break;
            }

            // 拿当前条的date与上一条进行比对
            if (position == 0) {
                holder.mTimeTv.setVisibility(View.VISIBLE);
                date = mList.get(position).getDate();
                holder.mTimeTv.setText(date);
            }
            else {
                if (mList.get(position).getDate().equals(mList.get(position - 1).getDate())) {
                    holder.mTimeTv.setVisibility(View.GONE);
                }
                else {
                    holder.mTimeTv.setText(mList.get(position).getDate());
                    holder.mTimeTv.setVisibility(View.VISIBLE);
                }

            }
            return convertView;
        }

        public void updateData(List<CrmClientTrendVO> list) {
            if (null == mList) {
                mList = new ArrayList<CrmClientTrendVO>();
            }
            if (null == list) {
                return;
            }
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }

        public void updateDataFrist(List<CrmClientTrendVO> list) {
            list.addAll(mList);
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }

        public void updateDataLast(List<CrmClientTrendVO> list) {
            mList.addAll(list);
            notifyDataSetChanged();
        }

    }

    static class ViewHolder {
        private TextView mText01Tv, mText02Tv, mText03Tv, mTimeTv;
        private ImageView typeIv;
    }

}
