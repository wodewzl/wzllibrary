package com.xiaojing.shop.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.TextView;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.constant.BaseConstant;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.TreeVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.DividerUtil;
import com.wuzhanglong.library.utils.SharePreferenceUtil;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.wuzhanglong.library.utils.WidthHigthUtil;
import com.wuzhanglong.library.view.AutoSwipeRefreshLayout;
import com.wuzhanglong.library.view.BSPopupWindowsTitle;
import com.xiaojing.shop.R;
import com.xiaojing.shop.adapter.NearbyShopRadapter;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.CityVO;
import com.xiaojing.shop.mode.NearbyShopVO;
import com.xiaojing.shop.mode.OptionVO;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;

public class NearbyShopStoreActivity extends BaseActivity implements View.OnClickListener, OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener,
        android.widget.TextView.OnEditorActionListener, BGAOnRVItemClickListener, TextWatcher,PostCallback{
    private LuRecyclerView mRecyclerView;
    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private NearbyShopRadapter mAdapter;
    private TextView mOptionsTv1, mOptionsTv2, mOptionsTv3;
    private CityVO mCityVO;
    private OptionVO mOptionVO;
    private BSPopupWindowsTitle mCityPop, mTypePop, mDistanPop;
    private String mKeyword, mCity, mType, mDistan;
    private NearbyShopVO mNearbyShopVO;
    private int mCurrentPage = 1;
    private boolean isLoadMore = true;
    private View mDivider;
    private android.widget.TextView mBackTv, mAddressTv;
    private EditText mSearchEt;
    private ImageView mMsgImg;


    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.nearby_shop_activity);
    }

    @Override
    public void initView() {
        mBaseHeadLayout.setVisibility(View.GONE);
        mBackTv = getViewById(R.id.back_tv);
        mSearchEt = getViewById(R.id.search_et);
        mSearchEt.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchEt.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        mOptionsTv1 = getViewById(R.id.options1_tv);
        mOptionsTv2 = getViewById(R.id.options2_tv);
        mOptionsTv3 = getViewById(R.id.options3_tv);
        mDivider = getViewById(R.id.divider);
        mAddressTv = getViewById(R.id.address_tv);
        mAddressTv.setText(SharePreferenceUtil.getSharedpreferenceValue(this, "address", "detail_address"));
        mAutoSwipeRefreshLayout = getViewById(R.id.swipe_refresh_layout);
        mActivity.setSwipeRefreshLayoutColors(mAutoSwipeRefreshLayout);
        mRecyclerView = getViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(DividerUtil.linnerDivider(this, R.dimen.dp_1, R.color.C3));
        mAdapter = new NearbyShopRadapter(mRecyclerView);
        LuRecyclerViewAdapter adapter = new LuRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(adapter);
        mMsgImg = getViewById(R.id.msg_img);

        mBaseHeadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

                //条件
                if (mOptionVO == null) {
                    RequestParams paramsOption = new RequestParams();
                    if (AppApplication.getInstance().getUserInfoVO() != null)
                        paramsOption.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
                    String urlOption = Constant.OPTION_URL;
                    HttpClientUtil.post(mActivity, mActivity, urlOption, paramsOption, OptionVO.class,NearbyShopStoreActivity.this);
                }

                //城市
                if (mCityVO == null) {
                    RequestParams paramsMapCity = new RequestParams();
                    if (AppApplication.getInstance().getUserInfoVO() != null)
                        paramsMapCity.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
                    String urlCity = Constant.GET_CITY_ALL_URL;
                    HttpClientUtil.post(mActivity, mActivity, urlCity, paramsMapCity, CityVO.class,NearbyShopStoreActivity.this);
                }
            }
        },1000);
    }

    @Override
    public void bindViewsListener() {
        mOptionsTv1.setOnClickListener(this);
        mOptionsTv2.setOnClickListener(this);
        mOptionsTv3.setOnClickListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mAutoSwipeRefreshLayout.setOnRefreshListener(this);
        mBackTv.setOnClickListener(this);
        mSearchEt.setOnEditorActionListener(this);
        mSearchEt.addTextChangedListener(this);
        mAdapter.setOnRVItemClickListener(this);
        mMsgImg.setOnClickListener(this);
    }

    @Override
    public void getData() {
        //列表
        RequestParams paramsMapList = new RequestParams();
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMapList.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        paramsMapList.put("order_type", mDistan);
        paramsMapList.put("sc_id", mType);
        paramsMapList.put("district_id", mCity);
        paramsMapList.put("keyword", mKeyword);
        paramsMapList.put("client", 1);
        String lat = SharePreferenceUtil.getSharedpreferenceValue(this, "address", "lat");
        paramsMapList.put("lat", lat);
        String lo = SharePreferenceUtil.getSharedpreferenceValue(this, "address", "lo");
        paramsMapList.put("lng", lo);
        paramsMapList.put("curpage", mCurrentPage);
        String urlList = Constant.NERBY_SHOP_URL;
        HttpClientUtil.get(mActivity, mThreadUtil, urlList, paramsMapList, NearbyShopVO.class);



    }

    @Override
    public void hasData(final BaseVO vo) {

            NearbyShopVO nearbyShopVO = (NearbyShopVO) vo;
            mNearbyShopVO = nearbyShopVO.getDatas();
            List<NearbyShopVO> list = mNearbyShopVO.getList();
            if (vo.getHasmore() != null && "1".equals(vo.getHasmore())) {
                mRecyclerView.setNoMore(false);
            } else {
                mRecyclerView.setNoMore(true);
            }
            if (isLoadMore) {
                mAdapter.updateDataLast(list);
                isLoadMore = false;
                mCurrentPage++;
            } else {
                mCurrentPage++;
                mAdapter.updateData(list);
            }
            mAutoSwipeRefreshLayout.setRefreshing(false);
            mAdapter.notifyDataSetChanged();

    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.options1_tv:
                if (mCityPop != null)
                    mCityPop.showPopupWindow(mDivider);
                break;
            case R.id.options2_tv:
                if (mTypePop != null)
                    mTypePop.showPopupWindow(mDivider);
                break;
            case R.id.options3_tv:
                if (mDistanPop != null)
                    mDistanPop.showPopupWindow(mDivider);
                break;
            case R.id.back_tv:
                finish();
                break;
            case R.id.msg_img:
                Bundle bundle = new Bundle();
                bundle.putString("url", SharePreferenceUtil.getSharedpreferenceValue(mActivity, "jpush", "msg"));
                mActivity.open(WebViewActivity.class, bundle, 0);
                break;
            default:
                break;
        }
    }


    // 一级二级都带全部的菜单
    public ArrayList<TreeVO> getTreeVOList(ArrayList<CityVO> allList) {
        ArrayList<TreeVO> treeList = new ArrayList<TreeVO>();
        for (int i = 0; i < allList.size(); i++) {
            CityVO oneCityVO = allList.get(i);
            TreeVO oneTreeVo = new TreeVO();
            oneTreeVo.setSearchId(oneCityVO.getProvince_id());
            oneTreeVo.setParentId(0);
            oneTreeVo.setId(Integer.parseInt(oneCityVO.getProvince_id()));
            oneTreeVo.setName(oneCityVO.getProvince_name());
            oneTreeVo.setLevel(1);
            if (oneCityVO.getCitys().size() > 0) {
                oneTreeVo.setHaschild(true);
            } else {
                oneTreeVo.setHaschild(false);
            }
            treeList.add(oneTreeVo);
            for (int j = 0; j < oneCityVO.getCitys().size(); j++) {
                CityVO twoCityVO = oneCityVO.getCitys().get(j);
                TreeVO twoTreeVo = new TreeVO();
                twoTreeVo.setSearchId(twoCityVO.getCity_id());
                twoTreeVo.setName(twoCityVO.getCity_name());
                twoTreeVo.setLevel(2);
                twoTreeVo.setParentId(Integer.parseInt(oneCityVO.getProvince_id()));
                twoTreeVo.setId(Integer.parseInt(twoCityVO.getCity_id()));
                if (twoCityVO.getDistricts().size() > 0) {
                    twoTreeVo.setHaschild(true);
                } else {
                    twoTreeVo.setHaschild(false);
                }
                treeList.add(twoTreeVo);
                for (int k = 0; k < twoCityVO.getDistricts().size(); k++) {
                    CityVO threeCityVO = twoCityVO.getDistricts().get(k);
                    TreeVO threeTreeVo = new TreeVO();
                    threeTreeVo.setSearchId(threeCityVO.getDistrict_id());
                    threeTreeVo.setName(threeCityVO.getDistrict_name());
                    threeTreeVo.setLevel(3);
                    threeTreeVo.setParentId(Integer.parseInt(twoCityVO.getCity_id()));
                    threeTreeVo.setId(Integer.parseInt(threeCityVO.getDistrict_id()));
                    threeTreeVo.setHaschild(false);
                    treeList.add(threeTreeVo);
                }
            }
        }
        return treeList;
    }

    // 菜单点击回调函数
    BSPopupWindowsTitle.TreeCallBack callbackCity = new BSPopupWindowsTitle.TreeCallBack() {
        @Override
        public void callback(TreeVO vo) {
            mCity = vo.getSearchId();
            mOptionsTv1.setText(vo.getName());
            mAutoSwipeRefreshLayout.autoRefresh();

        }
    };

    BSPopupWindowsTitle.TreeCallBack callbackType = new BSPopupWindowsTitle.TreeCallBack() {
        @Override
        public void callback(TreeVO vo) {
            mType = vo.getSearchId();
            mOptionsTv2.setText(vo.getName());
            mAutoSwipeRefreshLayout.autoRefresh();
        }
    };

    BSPopupWindowsTitle.TreeCallBack callbackDistan = new BSPopupWindowsTitle.TreeCallBack() {
        @Override
        public void callback(TreeVO vo) {
            mDistan = vo.getSearchId();
            mOptionsTv3.setText(vo.getName());
            mAutoSwipeRefreshLayout.autoRefresh();
        }
    };

    @Override
    public void onLoadMore() {
        isLoadMore = true;
        mThreadUtil = new ThreadUtil(mActivity, this);
        mThreadUtil.start();
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        mThreadUtil = new ThreadUtil(mActivity, this);
        mThreadUtil.start();
    }

    @Override
    public boolean onEditorAction(android.widget.TextView textView, int i, KeyEvent keyEvent) {
        mKeyword = textView.getText().toString();
        mAutoSwipeRefreshLayout.autoRefresh();
        return false;
    }

    @Override
    public void onRVItemClick(ViewGroup viewGroup, View view, int i) {
        if (mAdapter.getData().size() == 0)
            return;
        Bundle bundle = new Bundle();
        NearbyShopVO vo = (NearbyShopVO) mAdapter.getData().get(i);
        bundle.putString("merchant_id", vo.getMerchant_id());
        open(NearbyShopStoreDetailActivity.class, bundle, 0);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        if ("".equals(s.toString())) {
            mKeyword = "";
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void success(BaseVO vo) {
        if(BaseConstant.RESULT_SUCCESS_CODE.equals(vo.getCode())){
            if (vo instanceof OptionVO) {
                OptionVO optionVO = (OptionVO) vo;
                mOptionVO = optionVO.getDatas();
                //行业类别
                String[] typeName = new String[mOptionVO.getFilter().get(0).getOptions().size()];
                String[] typeid = new String[mOptionVO.getFilter().get(0).getOptions().size()];
                for (int i = 0; i < mOptionVO.getFilter().get(0).getOptions().size(); i++) {
                    typeName[i] = mOptionVO.getFilter().get(0).getOptions().get(i).getSc_name();
                    typeid[i] = mOptionVO.getFilter().get(0).getOptions().get(i).getSc_id();
                }
                List<TreeVO> typeList = BaseCommonUtils.getOneLeveTreeVoZero(typeName, typeid);
                mTypePop = new BSPopupWindowsTitle(mActivity, typeList, callbackType, WidthHigthUtil.getScreenHigh(NearbyShopStoreActivity.this) / 3);
                //距离
                String[] distanName = new String[mOptionVO.getFilter().get(1).getOptions().size()];
                String[] distanId = new String[mOptionVO.getFilter().get(1).getOptions().size()];
                for (int i = 0; i < mOptionVO.getFilter().get(1).getOptions().size(); i++) {
                    distanName[i] = mOptionVO.getFilter().get(1).getOptions().get(i).getText();
                    distanId[i] = mOptionVO.getFilter().get(1).getOptions().get(i).getOrder_type();
                }
                List<TreeVO> distanList = BaseCommonUtils.getOneLeveTreeVoZero(distanName, distanId);
                mDistanPop = new BSPopupWindowsTitle(mActivity, distanList, callbackDistan, LinearLayout.LayoutParams.WRAP_CONTENT);
            } else {
                CityVO cityVO = (CityVO) vo;
                mCityVO = cityVO.getDatas();
                ArrayList<TreeVO> treeList = getTreeVOList(mCityVO.getArea_list());
                mCityPop = new BSPopupWindowsTitle(mActivity, treeList, callbackCity);
            }
        }

    }
}
