package com.xiaojing.shop.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.activity.AddressEditActivity;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.AddressVO;

import java.io.Serializable;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/2/13.
 */

public class AddressRAdapter extends RecyclerBaseAdapter<AddressVO> {
    private AddressVO mDefalutVO;

    public AddressRAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.address_adapter);
    }

    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        AddressVO vo = (AddressVO) model;
        helper.setText(R.id.name_tv, vo.getTrue_name());
        helper.setText(R.id.phone_tv, vo.getMob_phone());
        helper.setText(R.id.address_tv, vo.getArea_info() + vo.getAddress());
        if ("1".equals(vo.getIs_default())) {
            helper.setImageResource(R.id.check_img, R.drawable.check_select);
            mDefalutVO = vo;
        } else {
            helper.setImageResource(R.id.check_img, R.drawable.check_normal);
        }

        helper.setItemChildClickListener(R.id.delete_tv);
        helper.setItemChildClickListener(R.id.edit_text);
        helper.setItemChildClickListener(R.id.check_img);
        helper.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup viewGroup, View v, final int i) {
                final AddressVO vo = (AddressVO) mData.get(i);
                switch (v.getId()) {
                    case R.id.delete_tv:

                        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("确定要删除该地址吗?")
//                            .setContentText("删除成功")
                                .setConfirmText("确定")
                                .setCancelText("取消")

                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();//直接消失
                                        delete(vo.getAddress_id());
                                        mData.remove(i);
                                        AddressRAdapter.this.notifyDataSetChanged();
                                    }
                                })
                                .show();

                        break;
                    case R.id.edit_text:

                        Intent intent = new Intent();
                        intent.putExtra("address", (Serializable) vo);
                        intent.setClass(mActivity, AddressEditActivity.class);
                        mActivity.startActivity(intent);
                        break;
                    case R.id.check_img:
                        if ("1".equals(vo.getIs_default()))
                            return;
                        setAddressDefault(vo.getAddress_id());
                        vo.setIs_default("1");
                        if (mDefalutVO != null)
                            mDefalutVO.setIs_default("0");
                        notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        });

    }

    public void delete(final String addressId) {
        new Thread() {
            @Override
            public void run() {
                BaseActivity activity = (BaseActivity) mContext;
                RequestParams paramsMap = new RequestParams();
                String mUrl = Constant.DELETE_ADDRESS_URL;
                if (AppApplication.getInstance().getUserInfoVO() != null)
                    paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
                paramsMap.put("address_id", addressId);
                HttpClientUtil.getRequest(activity, activity, mUrl, paramsMap, null);
            }
        }.start();
    }

    public void setAddressDefault(final String addressId) {
        new Thread() {
            @Override
            public void run() {
                BaseActivity activity = (BaseActivity) mContext;
                RequestParams paramsMap = new RequestParams();
                String mUrl = Constant.DEFALULT_ADDRESS_URL;
                if (AppApplication.getInstance().getUserInfoVO() != null)
                    paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
                paramsMap.put("address_id", addressId);
                HttpClientUtil.getRequest(activity, activity, mUrl, paramsMap, null);
            }
        }.start();

    }
}
