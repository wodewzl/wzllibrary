package com.xiaojing.shop.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.vondear.rxtools.view.dialog.RxDialogEditSureCancel;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.FileUtil;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.adapter.GameDetailAdapter;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.GameVO;

import java.io.File;
import java.util.ArrayList;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class GameDetailActivity extends BaseActivity implements View.OnClickListener, BGAOnRVItemClickListener {
    private RecyclerView mRecyclerView;
    private GameDetailAdapter mAdapter;
    private TextView mNameTv, mPriceTv, mBuyTv, mDownloadTv, mDescTv;
    private ImageView mImg;
    private MaterialRatingBar mRatingBar;
    private GameVO mGameVO;
    private static final int LOGIN_CODE = 1;
    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.game_detail_activity);
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("游戏详情");
        mImg = getViewById(R.id.img);
        mNameTv = getViewById(R.id.name_tv);
        mPriceTv = getViewById(R.id.price_tv);
        mRatingBar = getViewById(R.id.rating_bar);
        mBuyTv = getViewById(R.id.buy_tv);
        mDownloadTv = getViewById(R.id.download_tv);
        mDescTv = getViewById(R.id.desc_tv);
        mRecyclerView = getViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new GameDetailAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
//        DividerDecoration divider = DividerUtil.linnerDivider(this, R.dimen.dp_10, R.color.C3);
//        mRecyclerView.addItemDecoration(divider);
    }

    @Override
    public void bindViewsListener() {
        mBuyTv.setOnClickListener(this);
        mDownloadTv.setOnClickListener(this);
        mAdapter.setOnRVItemClickListener(this);
    }

    @Override
    public void getData() {
        RequestParams params = new RequestParams();
        String mUrl = Constant.GAME_DETAIL_URL;
        params.put("game_id", this.getIntent().getStringExtra("id"));
        HttpClientUtil.get(mActivity, mThreadUtil, mUrl, params, GameVO.class);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void hasData(BaseVO vo) {
        GameVO gameVO = (GameVO) vo;
        mGameVO = gameVO.getDatas().getMerchant();
        mRatingBar.setEnabled(false);
        mRatingBar.setRating(BaseCommonUtils.parseInt(mGameVO.getGame_star()));
        mNameTv.setText(mGameVO.getGame_name());
        BaseCommonUtils.setTextThree(mActivity, mPriceTv, "房卡：", mGameVO.getGame_card_price(), "元/张", R.color.XJColor2, 1.2f);
        Picasso.with(mActivity).load(mGameVO.getGame_logo()).placeholder(R.drawable.img_default).into(mImg);
        mBuyTv.setBackground(BaseCommonUtils.setBackgroundShap(mActivity, 5, R.color.XJColor9, R.color.C1));
        mDownloadTv.setBackground(BaseCommonUtils.setBackgroundShap(mActivity, 5, R.color.XJColor7, R.color.C1));
        mDescTv.setText(mGameVO.getGame_desc());
        mAdapter.updateData(mGameVO.getImgs());
    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.buy_tv:
                if(AppApplication.getInstance().getUserInfoVO()==null){
                    Intent intent = new Intent();
                    intent.putExtra("type", "2");//type 是post 结束类型
                    intent.setClass(this, LoginActivity.class);
                    startActivityForResult(intent, LOGIN_CODE);
                    return;
                }

                final RxDialogEditSureCancel rxDialogEditTextSureCancle = new RxDialogEditSureCancel(this,2);//提示弹窗
                rxDialogEditTextSureCancle.setTitle("购买房卡");
                rxDialogEditTextSureCancle.getTvTitle().setBackgroundColor(ContextCompat.getColor(this, R.color.C1));
                rxDialogEditTextSureCancle.getTvTitle().setTextSize(16);
                rxDialogEditTextSureCancle.getEditText().setHint("请输入购买数量");
                rxDialogEditTextSureCancle.getEditText02().setHint("请输入游戏账户ID");
                rxDialogEditTextSureCancle.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
                rxDialogEditTextSureCancle.getEditText().setTextSize(14);
                rxDialogEditTextSureCancle.getEditText().setHintTextColor(ContextCompat.getColor(this, R.color.C6));
                rxDialogEditTextSureCancle.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(rxDialogEditTextSureCancle.getEditText().getText().toString().length()==0){
                            showCustomToast("请输入购买数量");
                            return;
                        }

                        if(rxDialogEditTextSureCancle.getEditText02().getText().toString().length()==0){
                            showCustomToast("请输入游戏账户ID");
                            return;
                        }

                        Bundle bundle = new Bundle();
                        bundle.putString("cart_info", mGameVO.getGame_id() + "|" + rxDialogEditTextSureCancle.getEditText().getText().toString());
                        bundle.putString("ifcart", "1");
                        bundle.putString("type", "3");
                        bundle.putString("game_userid", rxDialogEditTextSureCancle.getEditText02().getText().toString());
                        mActivity.open(OrderSureActivity.class, bundle, 0);

                        rxDialogEditTextSureCancle.cancel();
                    }
                });
                rxDialogEditTextSureCancle.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogEditTextSureCancle.cancel();
                    }
                });
                rxDialogEditTextSureCancle.show();
                break;
            case R.id.download_tv:
                bundle.putString("title", "游戏下载");
                bundle.putString("url", mGameVO.getGame_android_download_url());
                open(WebViewActivity.class, bundle, 0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRVItemClick(ViewGroup viewGroup, View view, int i) {
        File downloadDir = new File(FileUtil.getSaveFilePath(this, Constant.SDCARD_CACHE));
        startActivity(BGAPhotoPreviewActivity.newIntent(this, downloadDir, (ArrayList<String>) mAdapter.getData(), i));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case LOGIN_CODE:
                mThreadUtil = new ThreadUtil(mActivity, this);
                mThreadUtil.start();
                break;
            default:
                break;
        }
    }
}
