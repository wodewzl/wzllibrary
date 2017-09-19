package com.xiaojing.shop.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.rey.material.widget.CheckBox;
import com.vondear.rxtools.view.sidebar.PinnedHeaderDecoration;
import com.wuzhanglong.library.fragment.BaseFragment;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.NumberTypeUtil;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.activity.OrderSureActivity;
import com.xiaojing.shop.adapter.ShopCatLRAdapter;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.ShopCatVO;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGADivider;

/**
 * Created by Administrator on 2017/2/9.
 */

public class TabThreeFragment extends BaseFragment implements ShopCatLRAdapter.OnMoneyChageListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private RecyclerView mRecyclerView;
    private ShopCatLRAdapter mAdapter;
    private ShopCatVO mShopCatVO;
    private List<ShopCatVO> mDataList;//数据源
    private TextView mTotalTv;
    private CheckBox mAllCheck;
    private com.rey.material.widget.TextView mCommitTv;
    private StringBuffer mSb;
    private int mShortCatCount = 0;
    private View mAllCheckView;
    private LinearLayout mBottomLayout;


    @Override
    public void setContentView() {
        View.inflate(mActivity, R.layout.tab_three_fragment, mBaseContentLayout);
    }

    @Override
    public void initView(View view) {
        mActivity.SetTranslanteBar();
        mRecyclerView = getViewById(R.id.recycler_view);
        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(3, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        mRecyclerView.addItemDecoration(decoration);
//        DividerDecoration divider = DividerUtil.linnerDivider(mActivity, R.dimen.dp_1, R.color.C3);


        BGADivider divider = BGADivider.newShapeDivider() //设置分割线,用BGADivider 可以去掉分类顶部的分割线

                .setDelegate(new BGADivider.SimpleDelegate() {
                    @Override
                    public boolean isNeedSkip(int position, int itemCount) {
                        // 如果是分类的话就跳过，顶部不绘制分隔线
                        if (mAdapter.getItemViewType(position) == 4) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
        mRecyclerView.addItemDecoration(divider);

        mAdapter = new ShopCatLRAdapter(mActivity);
        mRecyclerView.setAdapter(mAdapter);

        GridLayoutManager mLayoutManager = new GridLayoutManager(mActivity, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mAdapter.getItemViewType(position)) {
                    case 1:
                    case 2:
                    case 3:
                    case 5:
                        return 2;
                    case 4:
                        return 1;
                    default:
                        return 2;
                }
            }
        });

        mTotalTv = getViewById(R.id.total_tv);
        mCommitTv = getViewById(R.id.commit_tv);
        mAllCheck = getViewById(R.id.check_box);
        mAllCheckView = getViewById(R.id.view_check);
        mBottomLayout = getViewById(R.id.bottom_layout);
        EventBus.getDefault().register(this);
    }

    @Override
    public void bindViewsListener() {
        mAdapter.setMoneyChageListener(this);
        mAllCheckView.setOnClickListener(this);
        mCommitTv.setOnClickListener(this);

    }

    @Override
    public void getData() {
        if (AppApplication.getInstance().getUserInfoVO() != null) {
            RequestParams paramsMap = new RequestParams();
            String mUrl = Constant.SHOP_CAT_URL;
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
            HttpClientUtil.get(mActivity, mThreadUtil, mUrl, paramsMap, ShopCatVO.class);
        } else {
            HttpClientUtil.show(mThreadUtil);
            mDataList = new ArrayList<>();
            ShopCatVO emptyVo = new ShopCatVO();
            emptyVo.setGoods_name("没有商品");
            mDataList.add(emptyVo);
            mAdapter.setNewData(mDataList);
        }

    }

    @Override
    public void hasData(BaseVO vo) {
        if (!(vo instanceof ShopCatVO)) {
            mThreadUtil = new ThreadUtil(mActivity, this);
            mThreadUtil.start();
            return;
        }
        mShortCatCount = 0;
        ShopCatVO shopCatVO = (ShopCatVO) vo;
        mShopCatVO = shopCatVO.getDatas();
        BaseCommonUtils.setTextTwoLast(mActivity, mTotalTv, "合计总金额：", "￥" + mShopCatVO.getSum(), R.color.XJColor2, 1.5f);
        mDataList = new ArrayList<>();
        for (int i = 0; i < mShopCatVO.getCart_list().size(); i++) {
            mDataList.add(mShopCatVO.getCart_list().get(i));
            for (int j = 0; j < mShopCatVO.getCart_list().get(i).getGoods().size(); j++) {
                mDataList.add(mShopCatVO.getCart_list().get(i).getGoods().get(j));
                mShortCatCount++;
            }
        }

        if (mShopCatVO.getCart_list().size() == 0) {
            ShopCatVO emptyVo = new ShopCatVO();
            emptyVo.setGoods_name("没有商品");
            mDataList.add(emptyVo);
        }

        mDataList.add(new ShopCatVO());
        mDataList.addAll(mDataList.size(), mShopCatVO.getHot_recomm_list());
        mAdapter.setNewData(mDataList);
        mAdapter.notifyDataSetChanged();
        mBottomLayout.setVisibility(View.VISIBLE);
    }


    @Override
    public void noData(BaseVO vo) {
        if("0".equals(vo.getLogin())){
            AppApplication.getInstance().saveUserInfoVO(null);
            mBaseContentLayout.setVisibility(View.VISIBLE);
            mNoContentTv.setVisibility(View.GONE);
            mNoNetTv.setVisibility(View.GONE);
        }

    }

    @Override
    public void noNet() {

    }


    @Override
    public void moneyChage() {
        double money = 0;
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            ShopCatVO vo = mAdapter.getData().get(i);
            if (vo.getCart_id() != null && vo.isCheck()) {
//                money = money + BaseCommonUtils.parseInt(vo.getGoods_num()) * Double.parseDoble(vo.getGoods_price());
                money = NumberTypeUtil.add(money, NumberTypeUtil.mul(Double.parseDouble(vo.getGoods_num()), Double.parseDouble(vo.getGoods_price())));
            }
        }
        BaseCommonUtils.setTextTwoLast(mActivity, mTotalTv, "合计总金额：", "￥" + money, R.color.XJColor2, 1.5f);
    }

    @Override
    public void allCheck(boolean isCheck) {
        mAllCheck.setChecked(isCheck);
    }

    @Override
    public void cartDelete() {
        commitShopCat();
        updateData();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.commit_tv:
//                if ("0".equals(mShopCatVO.getHave_address())) {
//                    mActivity.openActivity(AddressAddActivity.class);
//                    return;
//                }

                mSb = new StringBuffer();
                Bundle bundle = new Bundle();
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    ShopCatVO vo = mAdapter.getData().get(i);
                    if (vo.getCart_id() != null && vo.isCheck()) {
                        mSb.append(vo.getCart_id() + "|" + vo.getGoods_num()).append(",");
                    }
                }
                bundle.putString("cart_info", mSb.toString());
                bundle.putString("ifcart", "1");
                if (mSb.length() == 0) {
                    mActivity.showCustomToast("请选择商品");
                    return;
                }
                mActivity.open(OrderSureActivity.class, bundle, 0);
                break;
            case R.id.view_check:
                if (mAllCheck.isChecked()) {
                    mAllCheck.setChecked(false);
                } else {
                    mAllCheck.setChecked(true);
                }
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    ShopCatVO vo = mAdapter.getData().get(i);
                    vo.setCheck(mAllCheck.isChecked());
                }
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }


    }

    public void commitShopCat() {
        if (mActivity == null || mAdapter.getData().size() == 0)
            return;
        RequestParams params = new RequestParams();
        String url = Constant.COMMIT_SHOP_CAT_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("key", AppApplication.getInstance().getUserInfoVO().getKey());

        mSb = new StringBuffer();
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            ShopCatVO vo = mAdapter.getData().get(i);
            if (vo.getCart_id() != null) {
                mSb.append(vo.getGoods_id() + "|" + vo.getGoods_num()).append(",");
            }
        }
        params.put("cart_info", mSb.toString());
//        if (mSb.length() > 0)
        HttpClientUtil.post(mActivity, this, url, params, null, "1");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (AppApplication.getInstance().getUserInfoVO() == null) {
                HttpClientUtil.show(mThreadUtil);
                mDataList = new ArrayList<>();
                ShopCatVO emptyVo = new ShopCatVO();
                emptyVo.setGoods_name("没有商品");
                mDataList.add(emptyVo);
                mAdapter.setNewData(mDataList);
                mBottomLayout.setVisibility(View.GONE);
            } else {
                mAllCheck.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateData();
                    }
                }, 500);
            }

        } else {
            if (AppApplication.getInstance().getUserInfoVO() != null) {
                commitShopCat();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EBMessageVO event) {
        if ("back_webview".equals(event.getMessage())) {
//            mThreadUtil = new ThreadUtil(mActivity, this);
//            mThreadUtil.start();
            mAllCheck.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateData();
                }
            },200);
        }
    }

    public void updateData() {
        if (AppApplication.getInstance().getUserInfoVO() != null) {
            RequestParams paramsMap = new RequestParams();
            String mUrl = Constant.SHOP_CAT_URL;
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
            HttpClientUtil.get(mActivity, TabThreeFragment.this, mUrl, paramsMap, ShopCatVO.class);
        }
    }
}
