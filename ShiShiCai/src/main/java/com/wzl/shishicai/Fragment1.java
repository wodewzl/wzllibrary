package com.wzl.shishicai;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wuzhanglong.library.ItemDecoration.DividerDecoration;
import com.wuzhanglong.library.fragment.BaseFragment;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.DividerUtil;
import com.wuzhanglong.library.view.AutoSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Fragment1 extends BaseFragment implements View.OnClickListener {
    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private DataAdapter4 mAdapter1;
    private EditText mET01;
    private TextView mOkTv, mResetTv, mTv04, mTv06, mTv01, mTv07;
    private int mType = 1;//1为大小2为单双
    private int rightCount3, failCount3, rightCount4, failCount4, rightCount5, failCount5;
    private String mFist3, mFist4, mFist5;
    private int mFailCount, mFailCount2, mFailCount3, mFailCount5;

    public static BaseFragment newInstance() {
        BaseFragment fragment = new Fragment1();
        return fragment;
    }

    @Override
    public void getData() {
        HttpClientUtil.show(mThreadUtil);
    }

    @Override
    public void hasData(BaseVO vo) {

    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    @Override
    public void setContentView() {
        contentInflateView(R.layout.fragment1_layout);
    }

    @Override
    public void initView(View view) {
        mRecyclerView = getViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        DividerDecoration divider = DividerUtil.linnerDivider(mActivity, R.dimen.dp_1, R.color.C3);
        mRecyclerView.addItemDecoration(divider);
        mAdapter1 = new DataAdapter4(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter1);


        mET01 = getViewById(R.id.et01);
        mOkTv = getViewById(R.id.ok_tv);
        mResetTv = getViewById(R.id.reset_tv);
        mTv01 = getViewById(R.id.text01);
        mTv04 = getViewById(R.id.text04);
        mTv06 = getViewById(R.id.text06);
        mTv07 = getViewById(R.id.text07);
        daoRu();
    }

    @Override
    public void bindViewsListener() {
        mTv01.setOnClickListener(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (mET01.getText().toString().trim().length() == 0) {
            new SweetAlertDialog(mActivity, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("请勿连续操作?")
                    .setConfirmText("确定")
                    .setCancelText("取消")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();//直接消失
                        }
                    })
                    .show();
        } else {
            getData(mET01.getText().toString());
            mET01.setText("");
        }

    }

    public String getSameNumber(String str) {
        char[] chars = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        StringBuffer sbSame = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if (!sb.toString().contains(String.valueOf(chars[i]))) {
                sb.append(chars[i]);
            } else {
                if (!sbSame.toString().contains(String.valueOf(chars[i]))) {
                    sbSame.append(chars[i]);
                }
            }
        }

        return sbSame.toString();
    }

    public String getNoSameNumber(String str) {
        char[] chars = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        StringBuffer sbSame = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if (!sb.toString().contains(String.valueOf(chars[i]))) {
                sb.append(chars[i]);
            } else {
                if (!sbSame.toString().contains(String.valueOf(chars[i]))) {
                    sbSame.append(chars[i]);
                }
            }
        }

        return sb.toString();
    }


    public void getData(String str) {
        if (str.length() != 5) {
            mActivity.showCustomToast("请填写正确的号码");
            return;
        }
        List<DataVO> dataList = new ArrayList<>();
        DataVO datavo = new DataVO();
        datavo.setResult(str);
        dataList.add(datavo);
        mAdapter1.updateDataFrist(dataList);
        if (mAdapter1.getData().size() >= 2) {

            if (mTv04.getText().toString().contains(str.substring(4, 5))) {
                ((DataVO) mAdapter1.getData().get(0)).setYuce3("对");
                mFailCount = 0;
            } else {
                if (mTv04.getText().toString().length() == 0) {
                    ((DataVO) mAdapter1.getData().get(0)).setYuce3("无推荐");
                    mFailCount = 0;
                } else {
                    ((DataVO) mAdapter1.getData().get(0)).setYuce3("错");
                    mFailCount = mFailCount + 1;
                }
            }


            if (mTv06.getText().toString().contains(str.substring(4, 5))) {
                ((DataVO) mAdapter1.getData().get(0)).setYuce4("对");
                mTv06.setText("");
                mFailCount2 = 0;
            } else {
                if (mTv06.getText().toString().length() == 0) {
                    ((DataVO) mAdapter1.getData().get(0)).setYuce4("无推荐");
                    mFailCount2 = 0;
                } else {
                    ((DataVO) mAdapter1.getData().get(0)).setYuce4("错");
                    mFailCount2 = mFailCount2 + 1;
                }

            }


            if (mTv07.getText().toString().contains(str.substring(4, 5))) {
                ((DataVO) mAdapter1.getData().get(0)).setYuce5("对");
                mTv07.setText("");
                mFailCount3 = 0;
            } else {
                if (mTv07.getText().toString().length() == 0) {
                    ((DataVO) mAdapter1.getData().get(0)).setYuce5("无推荐");
                    mFailCount3 = 0;
                } else {
                    ((DataVO) mAdapter1.getData().get(0)).setYuce5("错");
                    mFailCount3 = mFailCount3 + 1;
                }

            }


            String str3 = getSameNumber(getNoSameNumber(((DataVO) mAdapter1.getData().get(0)).getResult()) + getNoSameNumber(((DataVO) mAdapter1.getData().get(1)).getResult()));


            if (mFailCount % 3 == 0) {
                if (str3.length() > 0 && str3.length() <= 3) {
                    mTv04.setText(str3);
                } else {
                    mTv04.setText("");
                }
            }

            String str4 = getSameNumber(getNoSameNumber(((DataVO) mAdapter1.getData().get(0)).getResult()) + getNoSameNumber(((DataVO) mAdapter1.getData().get(2)).getResult()));
            if (failCount4 % 3 == 0) {
                if (str4.length() > 0 && str4.length() <= 3) {
                    mTv06.setText(str4);
                } else {
                    mTv06.setText("");
                }
            }

            String str5 = getSameNumber(getNoSameNumber(((DataVO) mAdapter1.getData().get(0)).getResult()) + getNoSameNumber(((DataVO) mAdapter1.getData().get(3)).getResult()));
            if (failCount5 % 3 == 0) {
                if (str5.length() > 0 && str5.length() <= 3) {
                    mTv07.setText(str5);
                } else {
                    mTv07.setText("");
                }
            }
        } else {
            ((DataVO) mAdapter1.getData().get(0)).setYuce3("无推荐");
        }


        mAdapter1.notifyDataSetChanged();
    }


    public void daoRu() {
        List<DataVO> list = new ArrayList<>();
        if (AppApplication.getInstance().getDataVO() != null) {
            list = AppApplication.getInstance().getDataVO();
            mAdapter1.updateData(list);
            mAdapter1.notifyDataSetChanged();
        }
    }

    public void saveData(){
        AppApplication.getInstance().saveDataVO(mAdapter1.getData());
    }

    public  void resetData(){
        List<DataVO> list = new ArrayList<>();
        for (int i = 0; i < mAdapter1.getData().size(); i++) {
            if (i <= 30) {
                list.add((DataVO) mAdapter1.getData().get(i));
            } else {
                break;
            }
        }
        mAdapter1.updateData(list);
    }

}
