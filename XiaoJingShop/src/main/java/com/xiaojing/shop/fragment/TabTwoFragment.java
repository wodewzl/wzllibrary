package com.xiaojing.shop.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.vondear.rxtools.activity.ActivityScanerCode;
import com.vondear.rxtools.view.sidebar.PinnedHeaderDecoration;
import com.wuzhanglong.library.ItemDecoration.DividerDecoration;
import com.wuzhanglong.library.fragment.BaseFragment;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.DividerUtil;
import com.wuzhanglong.library.utils.SharePreferenceUtil;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.activity.KeyWrodActivity;
import com.xiaojing.shop.activity.LoginActivity;
import com.xiaojing.shop.activity.WebViewActivity;
import com.xiaojing.shop.adapter.CategoryLeftAdapter;
import com.xiaojing.shop.adapter.CategoryRightAdapter;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.CategoryVO;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/2/9.
 */

public class TabTwoFragment extends BaseFragment implements CategoryLeftAdapter.OnLeftSelectedListener, View.OnClickListener {
    public static final int SCANER_CODE = 2;
    private RecyclerView mLeftRecyclerView;//左侧菜单栏
    private RecyclerView mRightRecyclerView;//右侧菜单栏
    private CategoryLeftAdapter mLeftAdapter;
    private CategoryRightAdapter mRightAdapter;
    private List<CategoryVO> mDataList = new ArrayList<>();//数据源
    private CategoryVO mCategoryVO;
    private LinearLayoutManager mLeftLayoutManger;
    private TextView mSearchTv;
    private ImageView mSaoImg, mMsgImg, mStateImg;


    @Override
    public void setContentView() {
        View.inflate(mActivity, R.layout.tab_two_fragment, mBaseContentLayout);
    }

    @Override
    public void initView(View view) {
        mActivity.SetTranslanteBar();
        mLeftRecyclerView = (RecyclerView) view.findViewById(R.id.left_menu);
        mLeftLayoutManger = new LinearLayoutManager(mActivity);
        mLeftRecyclerView.setLayoutManager(mLeftLayoutManger);
        DividerDecoration divider = DividerUtil.linnerDivider(mActivity, R.dimen.dp_1, R.color.C3);
        mLeftRecyclerView.addItemDecoration(divider);
        mLeftAdapter = new CategoryLeftAdapter(mLeftRecyclerView);
        mLeftRecyclerView.setAdapter(mLeftAdapter);

        mRightRecyclerView = getViewById(R.id.right_menu);
        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        mRightRecyclerView.addItemDecoration(decoration);
//        DividerDecoration mAdapterdivider = DividerUtil.linnerDivider(mActivity, R.dimen.dp_1, R.color.C3);
//        mRightRecyclerView.addItemDecoration(divider);
        mRightAdapter = new CategoryRightAdapter(mActivity);
        mRightRecyclerView.setAdapter(mRightAdapter);
        GridLayoutManager mLayoutManager = new GridLayoutManager(mActivity, 3);
        mRightRecyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mRightAdapter.getItemViewType(position) == 1) {
                    return 3;
                } else {
                    return 1;
                }
            }
        });
        mSearchTv = getViewById(R.id.search_tv);
        mSaoImg = getViewById(R.id.sao_img);
        mMsgImg = getViewById(R.id.msg_img);
        mStateImg = getViewById(R.id.state_img);

    }

    @Override
    public void bindViewsListener() {
        mLeftAdapter.setListener(this);
        mSearchTv.setOnClickListener(this);
        mSaoImg.setOnClickListener(this);
        mMsgImg.setOnClickListener(this);
    }

    @Override
    public void getData() {
        RequestParams paramsMap = new RequestParams();
        String mUrl = Constant.CATEGORY_URL;
        HttpClientUtil.get(mActivity, mThreadUtil, mUrl, paramsMap, CategoryVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        if (!(vo instanceof CategoryVO)) {
            mThreadUtil = new ThreadUtil(mActivity, this);
            mThreadUtil.start();
            return;
        }
        mCategoryVO = (CategoryVO) vo;
        mLeftAdapter.updateData(mCategoryVO.getDatas().getClass_list());
        updateChild(mCategoryVO.getDatas().getClass_list().get(0));

        if("1".equals(SharePreferenceUtil.getSharedpreferenceValue(mActivity,"jpush","msg_state"))){
            mStateImg.setVisibility(View.VISIBLE);
        }else{
            mStateImg.setVisibility(View.GONE);
        }
    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {
    }


    @Override
    public void onLeftItemSelected(CategoryVO menu) {
        updateChild(menu);
    }

    @Override
    public void moveToTop(View view, int position) {
        smoothMoveToPosition(position);
        mLeftRecyclerView.setAdapter(mLeftAdapter);
    }

    public void updateChild(CategoryVO vo) {
        mDataList.clear();
        for (int i = 0; i < vo.getChild().size(); i++) {
            mDataList.add(vo.getChild().get(i));
            for (int j = 0; j < vo.getChild().get(i).getGrandson().size(); j++) {
                mDataList.add(vo.getChild().get(i).getGrandson().get(j));
            }
        }
        mRightAdapter.setNewData(mDataList);
        mRightAdapter.notifyDataSetChanged();
    }

    private void smoothMoveToPosition(int position) {
        int firstPosition = mLeftLayoutManger.findFirstVisibleItemPosition();
        int lastPosition = mLeftLayoutManger.findLastVisibleItemPosition();
        if (position <= lastPosition) {
            int top = mLeftRecyclerView.getChildAt(position - firstPosition).getTop();
            mLeftRecyclerView.smoothScrollBy(0, top);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_tv:
                mActivity.openActivity(KeyWrodActivity.class);
                break;
            case R.id.sao_img:
                mActivity.openActivityForResult(ActivityScanerCode.class, SCANER_CODE);
                break;
            case R.id.msg_img:
                Bundle bundle =new Bundle();
                if(AppApplication.getInstance().getUserInfoVO() ==null){
                    bundle.putString("type","2");
                    mActivity.open(LoginActivity.class,bundle,1);
                    return;
                }
                bundle.putString("url", SharePreferenceUtil.getSharedpreferenceValue(mActivity, "jpush", "msg"));
                mActivity.open(WebViewActivity.class, bundle, 0);
                mStateImg.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;

        if (requestCode == SCANER_CODE) {
            String result = data.getStringExtra("result");
            Intent gotoIntent = new Intent();
            gotoIntent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(result.toString());
            gotoIntent.setData(content_url);
            startActivity(gotoIntent);
        }
    }


}
