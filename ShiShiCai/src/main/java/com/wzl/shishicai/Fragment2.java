package com.wzl.shishicai;

import android.support.v4.widget.SwipeRefreshLayout;
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

public class Fragment2 extends BaseFragment  implements  SwipeRefreshLayout.OnRefreshListener{
    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private DataAdapter3 mAdapter2;


    private EditText mET01;
    private TextView mOkTv, mResetTv, mTv04, mTv06, mTv01, mTv07;
    private int mType = 1;//1为大小2为单双
    private int rightCount3, failCount3,rightCount4, failCount4,rightCount5, failCount5;
    private String mFist3, mFist4, mFist5;

    public static BaseFragment newInstance() {
        BaseFragment fragment = new Fragment2();
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
        contentInflateView(R.layout.fragment2_layout);
    }

    @Override
    public void initView(View view) {
        mAutoSwipeRefreshLayout = getViewById(R.id.swipe_refresh_layout);
        mActivity.setSwipeRefreshLayoutColors(mAutoSwipeRefreshLayout);
        mRecyclerView = getViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        DividerDecoration divider = DividerUtil.linnerDivider(mActivity, R.dimen.dp_1, R.color.C3);
        mRecyclerView.addItemDecoration(divider);
        mAdapter2 = new DataAdapter3(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter2);


        mET01 = getViewById(R.id.et01);
        mOkTv = getViewById(R.id.ok_tv);
        mResetTv = getViewById(R.id.reset_tv);
        mTv01 = getViewById(R.id.text01);
        mTv04 = getViewById(R.id.text04);
        mTv06 = getViewById(R.id.text06);
        mTv07 = getViewById(R.id.text07);
    }

    @Override
    public void bindViewsListener() {
        mAutoSwipeRefreshLayout.setOnRefreshListener(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
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
        mAdapter2.updateDataFrist(dataList);

        StringBuffer sb = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        StringBuffer sb3 = new StringBuffer();
        StringBuffer sb4 = new StringBuffer();
        StringBuffer sb5 = new StringBuffer();


        sb.setLength(0);
        sb3.setLength(0);
        sb4.setLength(0);
        sb5.setLength(0);


        for (int j = 0; j < mAdapter2.getData().size(); j++) {
            if (sb.length() >= 5 && sb5.length() == 0) {
                sb3.append(sb.toString().toString().substring(0, 3));
                sb4.append(sb.toString().toString().substring(0, 4));
                sb5.append(sb.toString().substring(0, 5));
                break;
            }
            DataVO vo = (DataVO) mAdapter2.getData().get(j);
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

        mAdapter2.notifyDataSetChanged();
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

    @Override
    public void onRefresh() {
        daoRu();
        mAutoSwipeRefreshLayout.setRefreshing(false);
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
            mAdapter2.updateData(list);
            mAdapter2.notifyDataSetChanged();
            System.out.println("==========rightcout3>: "+rightCount3+"  ===============failcout3>:"+failCount3);
            System.out.println("==========rightcout4>: "+rightCount4+"  ===============failcout4>:"+failCount4);
            System.out.println("==========rightcout5>: "+rightCount5+"  ===============failcout5>:"+failCount5);
        }

    }
}
