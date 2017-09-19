package com.beisheng.easycar.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.beisheng.easycar.R;
import com.beisheng.easycar.adapter.NearbyLeftAdapter;
import com.beisheng.easycar.adapter.NearbyRightAdapter;
import com.beisheng.easycar.application.AppApplication;
import com.beisheng.easycar.constant.Constant;
import com.beisheng.easycar.mode.NeaybyVO;
import com.loopj.android.http.RequestParams;
import com.wuzhanglong.library.ItemDecoration.DividerDecoration;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.DividerUtil;
import com.wuzhanglong.library.utils.SharePreferenceUtil;

import org.greenrobot.eventbus.EventBus;

import cn.bingoogolapple.androidcommon.adapter.BGADivider;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;

public class NearbyActivity extends BaseActivity implements NearbyLeftAdapter.OnLeftSelectedListener,BGAOnRVItemClickListener{
    private String mKeyword="";
    private RecyclerView mLeftRecyclerView;//左侧菜单栏
    private RecyclerView mRightRecyclerView;//右侧菜单栏
    private NearbyLeftAdapter mLeftAdapter;
    private NearbyRightAdapter mRightAdapter;
    private NeaybyVO mNeaybyVO;
    private LinearLayoutManager mLeftLayoutManger;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_nearby);
    }

    @Override
    public void initView() {
        mBaseHeadLayout.setBackgroundResource(R.color.Car2);
        mBaseTitleTv.setText("附近网点");
        mLeftRecyclerView = getViewById(R.id.left_recyclerview);
        mLeftLayoutManger=new LinearLayoutManager(mActivity);
        mLeftRecyclerView.setLayoutManager(mLeftLayoutManger);
        DividerDecoration divider = DividerUtil.linnerDivider(mActivity, R.dimen.dp_1, R.color.C3);
        mLeftRecyclerView.addItemDecoration(divider);
        mLeftAdapter = new NearbyLeftAdapter(mLeftRecyclerView);
        mLeftRecyclerView.setAdapter(mLeftAdapter);

        mRightRecyclerView = getViewById(R.id.right_recyclerview);
//        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
//        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
//            @Override
//            public boolean create(RecyclerView parent, int adapterPosition) {
//                return true;
//            }
//        });
//        mRightRecyclerView.addItemDecoration(decoration);
        BGADivider divideRight = DividerUtil.bagDivider(15, 0);
        mRightRecyclerView.addItemDecoration(divideRight);
        mRightAdapter = new NearbyRightAdapter(mRightRecyclerView);
        mRightRecyclerView.setAdapter(mRightAdapter);
        mRightRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    @Override
    public void bindViewsListener() {
        mLeftAdapter.setListener(this);
        mRightAdapter.setOnRVItemClickListener(this);
    }

    @Override
    public void getData() {
        RequestParams params = new RequestParams();
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());
        String cityCode= SharePreferenceUtil.getSharedpreferenceValue(this, "address", "cityCode");
        params.put("citycode", cityCode);
        String lat = SharePreferenceUtil.getSharedpreferenceValue(this, "address", "lat");
        params.put("lat", lat);
        String lo = SharePreferenceUtil.getSharedpreferenceValue(this, "address", "lo");
        params.put("lng", lo);

        params.put("keyword", mKeyword);
        String urlList = Constant.NEARBY_LIST_URL;
        HttpClientUtil.get(mActivity, mThreadUtil, urlList, params, NeaybyVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
         mNeaybyVO= (NeaybyVO) vo;
        mLeftAdapter.updateData(mNeaybyVO.getData());
        updateChild(mNeaybyVO.getData().get(0));
    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    @Override
    public void onLeftItemSelected(NeaybyVO neaybyVO) {
        updateChild(neaybyVO);
    }

    @Override
    public void moveToTop(View view, int position) {
        int firstPosition = mLeftLayoutManger.findFirstVisibleItemPosition();
        int lastPosition = mLeftLayoutManger.findLastVisibleItemPosition();
        if (position <= lastPosition) {
            int top = mLeftRecyclerView.getChildAt(position - firstPosition).getTop();
            mLeftRecyclerView.smoothScrollBy(0, top);
        }
    }

    public void updateChild(NeaybyVO vo) {
        mRightAdapter.updateData(vo.getList());
    }

    @Override
    public void onRVItemClick(ViewGroup viewGroup, View view, int i) {
        if(mRightAdapter.getData().size()==0)
            return;

        EBMessageVO ebMessageVO=new EBMessageVO("back_car_address");
        ebMessageVO.setObject(mRightAdapter.getData().get(i));
        EventBus.getDefault().post(ebMessageVO);
        this.finish();
    }
}
