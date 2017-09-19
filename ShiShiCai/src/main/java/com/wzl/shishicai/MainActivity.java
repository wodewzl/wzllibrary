package com.wzl.shishicai;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wuzhanglong.library.ItemDecoration.DividerDecoration;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.cache.ACache;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.DividerUtil;
import com.wuzhanglong.library.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private EditText mET01;
    private DataAdapter3 mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mOkTv, mResetTv, mTv04, mTv06, mTv01, mTv07;
    private int mType = 1;//1为大小2为单双
    private int rightCount3, failCount3,rightCount4, failCount4,rightCount5, failCount5;
    private String mFist3, mFist4, mFist5;
    private double mBackPressed;
    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_main);
    }

    @Override
    public void initView() {
        mBaseHeadLayout.setVisibility(View.GONE);
        mET01 = getViewById(R.id.et01);
        mOkTv = getViewById(R.id.ok_tv);
        mResetTv = getViewById(R.id.reset_tv);
        mTv01 = getViewById(R.id.text01);
        mTv04 = getViewById(R.id.text04);
        mTv06 = getViewById(R.id.text06);
        mTv07 = getViewById(R.id.text07);
        mRecyclerView = getViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
//        layoutManager.setReverseLayout(true);//列表翻转
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DataAdapter3(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        DividerDecoration divider = DividerUtil.linnerDivider(this, R.dimen.dp_1, R.color.C3);
        mRecyclerView.addItemDecoration(divider);
        daoRu();

//        InputStream inputStream = null;
//        try {
//            inputStream = getAssets().open("data.txt");
//            int size = inputStream.available();
//            int len = -1;
//            byte[] bytes = new byte[size];
//            inputStream.read(bytes);
//            inputStream.close();
//            String cacheStr = new String(bytes);
//            Gson gson= new Gson();
//            DataVO datavo=gson.fromJson(cacheStr,DataVO.class);
//            datavo.getList();
//            mAdapter.updateData(datavo.getList());
//            mAdapter.notifyDataSetChanged();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void bindViewsListener() {
        mOkTv.setOnClickListener(this);
        mResetTv.setOnClickListener(this);
        mTv01.setOnClickListener(this);
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

    public void daoRu() {
        List<DataVO> list = new ArrayList<>();
        if (AppApplication.getInstance().getDataVO() != null) {
            list = AppApplication.getInstance().getDataVO();
//            mAdapter.updateData(list);
//            mAdapter.notifyDataSetChanged();
        }


        if (list.size() > 0) {
            StringBuffer sb = new StringBuffer();
            StringBuffer sb3 = new StringBuffer();
            StringBuffer sb4 = new StringBuffer();
            StringBuffer sb5 = new StringBuffer();
            for (int i = 0; i < list.size(); i++) {
                sb.setLength(0);
                sb3.setLength(0);
                sb4.setLength(0);
                sb5.setLength(0);
                for (int j = i+1; j < list.size(); j++) {
                    if (sb.length() >= 5 && sb5.length() == 0) {
                        sb5.append(sb.toString().substring(0, 5));
                        sb3.append(sb.toString().toString().substring(0, 3));
                        sb4.append(sb.toString().toString().substring(0, 4));
                        break;
                    }

                    String str = list.get(j).getResult().substring(0, 5);
                    if (getSameNumber(str).length() > 1) {
                        for (int k = 0; k < getSameNumber(str).length(); k++) {
                            if (!sb.toString().contains(String.valueOf(getSameNumber(str).charAt(k)))) {
                                sb.append(String.valueOf(getSameNumber(str).charAt(k)));
                            }
                        }
                    } else {
                        if (!sb.toString().contains(getSameNumber(str))) {
                            sb.append(getSameNumber(str));
                        }
                    }

                }

                if (i == 0) {
                    mFist3 = sb3.toString();
                    mFist4 = sb4.toString();
                    mFist5 = sb5.toString();
                }


                if (sb5.toString().length() == 5) {
                    mTv04.setText(sb3.toString());
                    if (mTv04.getText().toString().trim().length() > 0) {
                        if (mTv04.getText().toString().contains(list.get(i).getResult().substring(4, 5))) {
                            list.get(i).setYuce3("对");
                            rightCount3=rightCount3+1;
                        } else {
                            list.get(i).setYuce3("错");
                            failCount3=failCount3+1;
                        }
                    }


                    mTv06.setText(sb4.toString());
                    if (mTv06.getText().toString().trim().length() > 0) {
                        if (mTv06.getText().toString().contains(list.get(i).getResult().substring(4, 5))) {
                            list.get(i).setYuce4("对");
                            rightCount4=rightCount4+1;
                        } else {
                            list.get(i).setYuce4("错");
                            failCount4=failCount4+1;
                        }
                    }

                    mTv07.setText(sb5.toString());
                    if (mTv07.getText().toString().trim().length() > 0) {
                        if (mTv07.getText().toString().contains(list.get(i).getResult().substring(4, 5))) {
                            list.get(i).setYuce5("对");
                            rightCount5=rightCount5+1;
                        } else {
                            list.get(i).setYuce5("错");
                            failCount5=failCount5+1;
                        }
                    }


                }

                if (i == list.size() - 1) {
                    mTv04.setText(mFist3);
                    mTv06.setText(mFist4);
                    mTv07.setText(mFist5);
                }
            }
            mAdapter.updateData(list);
            mAdapter.notifyDataSetChanged();
            System.out.println("==========rightcout3>: "+rightCount3+"  ===============failcout3>:"+failCount3);
            System.out.println("==========rightcout4>: "+rightCount4+"  ===============failcout4>:"+failCount4);
            System.out.println("==========rightcout5>: "+rightCount5+"  ===============failcout5>:"+failCount5);
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


    public void getData(String str) {
        List<DataVO> dataList = new ArrayList<>();
        DataVO datavo = new DataVO();
        datavo.setResult(str);

        if(mTv04.getText().toString().length()>0){
            if (mTv04.getText().toString().contains(str.substring(4, 5))) {
                datavo.setYuce3("对");
            } else {
                datavo.setYuce3("错");
            }


            if (mTv06.getText().toString().contains(str.substring(4, 5))) {
                datavo.setYuce4("对");
            } else {
                datavo.setYuce4("错");
            }


            if (mTv07.getText().toString().contains(str.substring(4, 5))) {
                datavo.setYuce5("对");
            } else {
                datavo.setYuce5("错");
            }
        }

        dataList.add(datavo);
        mAdapter.updateDataFrist(dataList);

        StringBuffer sb = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        StringBuffer sb3 = new StringBuffer();
        StringBuffer sb4 = new StringBuffer();
        StringBuffer sb5 = new StringBuffer();


            sb.setLength(0);
            sb3.setLength(0);
            sb4.setLength(0);
            sb5.setLength(0);


            for (int j = 0; j < mAdapter.getData().size(); j++) {
                if (sb.length() >= 5 && sb5.length() == 0) {
                    sb3.append(sb.toString().toString().substring(0, 3));
                    sb4.append(sb.toString().toString().substring(0, 4));
                    sb5.append(sb.toString().substring(0, 5));
                    break;
                }
                DataVO vo = (DataVO) mAdapter.getData().get(j);
                String str1 = vo.getResult().substring(0, 5);
                if (getSameNumber(str1).length() > 1) {
                    for (int k = 0; k < getSameNumber(str1).length(); k++) {
                        if (!sb.toString().contains(String.valueOf(getSameNumber(str1).charAt(k)))) {
                            sb.append(String.valueOf(getSameNumber(str1).charAt(k)));
                        }
                    }
                } else {
                    if (!sb.toString().contains(getSameNumber(str1))) {
                        sb.append(getSameNumber(str1));
                    }
                }
            }

            if (sb.length() >= 5) {
                mTv04.setText(sb3.toString());
                mTv07.setText(sb5.toString());
                mTv06.setText(sb4.toString());
            }

//        dataList.add(datavo);
//        mAdapter.updateDataFrist(dataList);
        mAdapter.notifyDataSetChanged();
    }

    public void countRightOrFail(String str) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text01:
                if (mET01.getText().toString().trim().length() == 0) {
                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
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
                break;

            case R.id.ok_tv:
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定要保存数据吗吗?")
                        .setConfirmText("确定")
                        .setCancelText("取消")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();//直接消失

                                AppApplication.getInstance().saveDataVO(mAdapter.getData());

                                DataVO dataVO = new DataVO();
                                dataVO.setList(mAdapter.getData());
                                ACache.get(MainActivity.this).put("key", JsonUtil.toJson(dataVO));
                            }
                        })
                        .show();
                break;
            case R.id.reset_tv:
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定要重置吗数据吗吗?")
                        .setConfirmText("确定")
                        .setCancelText("取消")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();//直接消失

                                List<DataVO> list = new ArrayList<>();
                                for (int i = 0; i < mAdapter.getData().size(); i++) {
                                    if (i <= 30) {
                                        list.add((DataVO) mAdapter.getData().get(i));
                                    } else {
                                        break;
                                    }
                                }
                                mAdapter.updateData(list);
                            }
                        })
                        .show();

                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isShow()) {
//            dismissProgressDialog();
        } else {
            if (mBackPressed + 3000 > System.currentTimeMillis()) {
                finish();
                super.onBackPressed();
            } else
                showCustomToast("再次点击，退出" + this.getResources().getString(R.string.app_name));
            mBackPressed = System.currentTimeMillis();
        }
    }
}