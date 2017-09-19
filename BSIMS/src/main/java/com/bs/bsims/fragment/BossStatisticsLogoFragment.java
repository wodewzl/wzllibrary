/**
 * 
 */

package com.bs.bsims.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.BossStatisticsLogoUtilsActivity;
import com.bs.bsims.adapter.CrmSaleDetailsAdapter;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.CrmProductVo;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-1-22
 * @version 1.22 content：boss统计首页
 */
public class BossStatisticsLogoFragment extends BaseFragment implements UpdateCallback {

    /*
     * (non-Javadoc)
     * @see BaseFragment#getFragmentName() notify
     */

    private static final String TAG = "BossStatisticsLogoFragment";

    private Activity mContext;

    private BSRefreshListView mLogoRefreshListView;
    private CrmProductVo mCrmtranctsbussinesVo;
    private CrmSaleDetailsAdapter mSaleAdapter;
    private LinearLayout no_content_layout;
    private TextView no_content_layout_content;
    private TextView mTxt_comm_head_right;

    private boolean isFlush = false;

   

    @Override
    public String getFragmentName() {
        // TODO Auto-generated method stub
        return TAG;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notify, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        bindListers();
    }

    /**
	 * 
	 */
    private void bindListers() {
        // TODO Auto-generated method stub
        mLogoRefreshListView.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new ThreadUtil(mContext, BossStatisticsLogoFragment.this).start();
            }
        });

        mTxt_comm_head_right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (isFlush) {
                    // 表示正在加载数据 不处理跳转
                    CustomToast.showShortToast(mContext, "正在处理数据 等等哦~");
                } else {

                    if (null != mCrmtranctsbussinesVo && (null != mCrmtranctsbussinesVo.getInfo().getSelected() || null != mCrmtranctsbussinesVo.getInfo().getUnselected())) {
                        Intent i = new Intent();
                        i.putExtra("logomodel", mCrmtranctsbussinesVo);
                        i.setClass(mContext, BossStatisticsLogoUtilsActivity.class);
                        startActivityForResult(i, 2016);
                    } else {
                        CustomToast.showShortToast(mContext, "没有最新配置信息");
                        return;
                    }
                }

            }
        });

    }

    /**
     * @param view
     */
    private void initViews(View view) {
        // TODO Auto-generated method stub
        mLogoRefreshListView = (BSRefreshListView) view.findViewById(R.id.lv_refresh);
        no_content_layout = (LinearLayout) view.findViewById(R.id.no_content_layout);
        no_content_layout_content = (TextView) view.findViewById(R.id.no_content_layout_content);
        mTxt_comm_head_right = (TextView) view.findViewById(R.id.txt_comm_head_right);
        mSaleAdapter = new CrmSaleDetailsAdapter(mContext, "2");
        mLogoRefreshListView.setAdapter(mSaleAdapter);
        view.findViewById(R.id.comm_head_layout).setVisibility(View.VISIBLE);
        // CommonSystemUtils.changeHeadFourFragment((RelativeLayout)
        // view.findViewById(R.id.comm_head_layout));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initData();
        mLogoRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(mContext, this).start();
    }

    /*
     * (non-Javadoc)
     * @see UpdateCallback#execute()
     */
    @Override
    public boolean execute() {
        // TODO Auto-generated method stub
        isFlush = true;
        return getdata();
    }

    /*
     * (non-Javadoc)
     * @see UpdateCallback#executeSuccess()
     */
    @Override
    public void executeSuccess() {
        // TODO Auto-generated method stub
        isFlush = false;
        no_content_layout.setVisibility(View.GONE);
        no_content_layout_content.setVisibility(View.GONE);
        mLogoRefreshListView.setVisibility(View.VISIBLE);
        // 是否有选中的菜单配置
        if (null != mCrmtranctsbussinesVo.getInfo().getSelected()) {
            mSaleAdapter.updateData(mCrmtranctsbussinesVo.getInfo().getSelected());
            mSaleAdapter.notifyDataSetChanged();
        } else {
            CustomToast.showLongToast(mContext, "点击右上角配置查看的分类哦~");
        }
        mLogoRefreshListView.onRefreshComplete();

      
    }

    /*
     * (non-Javadoc)
     * @see UpdateCallback#executeFailure()
     */
    @Override
    public void executeFailure() {
        // TODO Auto-generated method stub
        isFlush = false;
        if (null == mCrmtranctsbussinesVo && mSaleAdapter.mList.size() == 0) {
            mLogoRefreshListView.setVisibility(View.GONE);
            no_content_layout.setVisibility(View.VISIBLE);
            no_content_layout_content.setVisibility(View.VISIBLE);
            CommonUtils.setNonetIcon(mContext, no_content_layout_content,this);
        }
        mLogoRefreshListView.onRefreshComplete();

    }

    public boolean getdata() {
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<String, String>();
        try {
            String urlStr;
            String jsonUrlStr;
            urlStr = UrlUtil.getUrlByMap1(Constant.BOSS_STATISTICS_INDEX, map);
            jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            mCrmtranctsbussinesVo = gson.fromJson(jsonUrlStr, CrmProductVo.class);
            if (mCrmtranctsbussinesVo.getCode().equals("200")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO Auto-generated method stub
        switch (requestCode) {
            case 2016:
                if (resultCode == 2016) {
                    if (data != null) {
                        mLogoRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                        new ThreadUtil(mContext, this).start();
                    }
                }

                break;
        }
    }

    /**
     * 解决网络问题首次加载不出来一直下拉刷新的问题
     */

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            new ThreadUtil(mContext, BossStatisticsLogoFragment.this).start();

        }

    }

 

}
