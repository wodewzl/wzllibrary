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

public class MainActivity3 extends BaseActivity implements View.OnClickListener {
    private EditText mET01;
    private DataAdapter3 mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mOkTv, mResetTv, mTv04, mTv05, mTv06, mTv01, mTv07, mTv08, mTv09, mTv12, mYiLouTv, mThreeAll, mFive, mFiveAll;
    private int mType = 1;//1为大小2为单双
    private String mFist3, mFist4, mFist5;
    private int mFailCount, mFailCount2, mFailCount3, mFailCount5;
    private int rightCount1_1, rightCount1_2, rightCount1_3, rightCount2_1, rightCount2_2, rightCount2_3;
    private double mBackPressed;
    private int rightCount1, failCount1, rightCount2, failCount2, rightCount3, failCount3;
    private int mZuiDaYiLou;
    private String mYiLouNumber;
    private List<String> mList = new ArrayList<String>();
    private List<String> mSameList = new ArrayList<String>();//重复的列表


    private int mThreeCount1, mThreeCount2, mThreeCount3, mThreeCount4, mThreeCount5, mThreeCount6, mThreeCount7, mThreeCount8, mThreeCount9, mThreeCount10, mThreeCountOther;
    private int mThree3Count1, mThree3Count2, mThree3Count3, mThree3CountOther;
    private int mFourCount1, mFourCount2, mFourCount3, mFourCount4, mFourCount5, mFourCount6, mFourCount7, mFourCount8, mFourCountOther;
    private int mFour3Count1, mFour3Count2, mFour3Count3, mFour3Count4, mFour3Count5, mFour3Count6, mFour3Count7, mFour3Count8, mFour3Count9, mFour3Count10, mFour3CountOther;
    private int mFiveCount1, mFiveCount2, mFiveCount3, mFiveCount4, mFiveCount5, mFiveCount6, mFiveCount7, mFiveCount8, mFiveCountOther;
    private int mFive3Count1, mFive3Count2, mFive3Count3, mFive3CountOther;
    private int mAllThreeCount1, mAllThreeCount2, mAllThreeCount3, mAllThreeCount4, mAllThreeCountOther;
    private int mAllFourCount1, mAllFourCount2, mAllFourCount3, mAllFourCountOther;
    private int mAllFiveCount1, mAllFiveCount2, mAllFiveCount3, mAllFiveCountOther;
    private int mPosition1, mPosition2, mPosition3, mPosition4, mPosition5;
    private int mAllPosition1, mAllPosition2, mAllPosition3, mAllPosition4, mAllPosition5;
    private TextView mTitleTv;
    private boolean mFlag = true;


    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_main3);
    }

    @Override
    public void initView() {
        mBaseHeadLayout.setVisibility(View.GONE);
        mET01 = getViewById(R.id.et01);
        mOkTv = getViewById(R.id.ok_tv);
        mTitleTv = getViewById(R.id.title_tv);
        mResetTv = getViewById(R.id.reset_tv);
        mTv01 = getViewById(R.id.text01);
        mTv04 = getViewById(R.id.text04);
        mTv06 = getViewById(R.id.text06);
        mTv07 = getViewById(R.id.text07);
        mTv08 = getViewById(R.id.text08);
        mTv09 = getViewById(R.id.text09);
        mYiLouTv = getViewById(R.id.yi_lou_tv);
        mThreeAll = getViewById(R.id.three_all);
        mFive = getViewById(R.id.five);
        mFiveAll = getViewById(R.id.five_all);
        mTv05 = getViewById(R.id.text05);
        mTv12 = getViewById(R.id.text12);
//        Et08 = getViewById(R.id.et_08);
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
//
//            initList(datavo.getList());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        allCount();
//        threeCount();

    }

    @Override
    public void bindViewsListener() {
        mOkTv.setOnClickListener(this);
        mResetTv.setOnClickListener(this);
        mTv01.setOnClickListener(this);
        mYiLouTv.setOnClickListener(this);
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
            initList(list);
//
//            for (int i = 0; i <list.size() ; i++) {
//                String str = list.get(i).getResult();
//                if(!mList.contains(str.substring(4,5))){
//                    mList.add(str.substring(4,5));
//                    sb.append(str.substring(4,5));
//                }
//                if(mList.size()==10)
//                    break;
//            }
//
//            if(mList.size()==9){
//                for (int i = 0; i <10 ; i++) {
//                    if(!mList.contains(i+"")){
//                        mList.add(i+"");
//                        sb.append(i+"");
//                        break;
//                    }
//                }
//            }
//
//            for (int i = list.size() - 1; i >= 0; i--) {
//                String str = list.get(i).getResult();
//                getData(str);
//
//            }
//
//
//
//            mTv08.setText(sb.toString());
//
//            System.out.println("==========rightcout1>: " + rightCount1 + "  ===============failcout1>:" + failCount1);
//            System.out.println("==========rightcout2>: " + rightCount2 + "  ===============failcout2>:" + failCount2);
//            System.out.println("==========rightcout3>: " + rightCount3 + "  ===============failcout3>:" + failCount3);
//
//            System.out.println("==========rightcout1_1>: " + rightCount1_1 + "  ===============rightcout1_2>:" + rightCount1_2 + "  ===============rightcout1_3>:" + rightCount1_3);
//            System.out.println("==========rightcout2_1>: " + rightCount2_1 + "  ===============rightcout2_2>:" + rightCount2_2 + "  ===============rightcout2_3>:" + rightCount2_3);

        }
    }

    public void initList(List<DataVO> list) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i).getResult();
            if (getSameNumber(str).length() > 0) {
                for (int j = 0; j < getSameNumber(str).length(); j++) {
                    if (!mSameList.contains(String.valueOf(getSameNumber(str).charAt(j)))) {
                        mSameList.add(String.valueOf(getSameNumber(str).charAt(j)));
                        sb.append(String.valueOf(getSameNumber(str).charAt(j)));
                    }

                }

            }


            if (mSameList.size() == 10) {
                System.out.println("====================================>");
                break;
            }

        }

        if (mSameList.size() == 9) {
            for (int i = 0; i < 10; i++) {
                if (!mSameList.contains(i + "")) {
                    mSameList.add(i + "");
                    sb.append(i + "");
                    break;
                }
            }
        }


//        for (int i = 0; i < list.size(); i++) {
//            String str = list.get(i).getResult();
//            if (!mList.contains(str.substring(4, 5))) {
//                mList.add(str.substring(4, 5));
//                sb.append(str.substring(4, 5));
//            }
//            if (mList.size() == 10)
//                break;
//        }
//
//        if (mList.size() == 9) {
//            for (int i = 0; i < 10; i++) {
//                if (!mList.contains(i + "")) {
//                    mList.add(i + "");
//                    sb.append(i + "");
//                    break;
//                }
//            }
//        }

        for (int i = list.size() - 1; i >= 0; i--) {
            String str = list.get(i).getResult();
            getData(str);

        }


//        mTv08.setText(sb.toString());

        System.out.println("==========rightcout1>: " + rightCount1 + "  ===============failcout1>:" + failCount1);
        System.out.println("==========rightcout2>: " + rightCount2 + "  ===============failcout2>:" + failCount2);
        System.out.println("==========rightcout3>: " + rightCount3 + "  ===============failcout3>:" + failCount3);

        System.out.println("==========rightcout1_1>: " + rightCount1_1 + "  ===============rightcout1_2>:" + rightCount1_2 + "  ===============rightcout1_3>:" + rightCount1_3);
        System.out.println("==========rightcout2_1>: " + rightCount2_1 + "  ===============rightcout2_2>:" + rightCount2_2 + "  ===============rightcout2_3>:" + rightCount2_3);
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
            showCustomToast("请填写正确的号码");
            return;
        }
        List<DataVO> dataList = new ArrayList<>();
        DataVO datavo = new DataVO();

        if (getNoSameNumber(str).length() == 5) {
            datavo.setMark("5");
        } else if (getNoSameNumber(str).length() == 3) {
            datavo.setMark("3");
        } else if (getNoSameNumber(str).length() == 2) {
            datavo.setMark("2");
        } else {
            datavo.setMark("0");
        }
        datavo.setResult(str);
        dataList.add(datavo);

        if (getSameNumber(str).length() > 0) {
            for (int i = 0; i < getSameNumber(str).length(); i++) {
                mSameList.remove(String.valueOf(getSameNumber(str).charAt(i)));
                List<String> list = new ArrayList<String>();
                list.add(String.valueOf(getSameNumber(str).charAt(i)));
                list.addAll(mSameList);
                mSameList.clear();
                mSameList.addAll(list);
            }
        }

        StringBuffer sameSb = new StringBuffer();
        for (int i = 0; i < mSameList.size(); i++) {
            sameSb.append(mSameList.get(i));
        }
        mTv12.setText(sameSb.toString());

//        if (mList.size() == 10)
//            datavo.setMaxYiLou(mList.get(9) + "（" + mZuiDaYiLou + "）");
//        mList.remove(str.substring(4, 5));
//        List<String> list = new ArrayList<String>();
//        list.add(str.substring(4, 5));
//        list.addAll(mList);
//        mList.clear();
//        mList.addAll(list);
//
//        if (mList.size() == 10 && mAdapter.getData().size() >= 11) {
//            int zuidayilou = 0;
//            for (int j = 0; j < mAdapter.getData().size() - 1; j++) {
//                if (!((DataVO) mAdapter.getData().get(j)).getResult().substring(4, 5).equals(mList.get(9))) {
//                    zuidayilou = zuidayilou + 1;
////                    mZuiDaYiLou = mZuiDaYiLou + 1;
//                } else {
//                    zuidayilou = zuidayilou + 1;
//                    break;
//                }
//            }
//
//            mZuiDaYiLou = zuidayilou;
//            mTitleTv.setText("最大遗漏：" + mList.get(9) + "（" + mZuiDaYiLou + "）");
//        }


        mAdapter.updateDataFrist(dataList);

        if (sameSb.length() == 10) {
            if (mTv04.getText().toString().contains(str.substring(4, 5))) {
                ((DataVO) mAdapter.getData().get(0)).setYuce3("对");
                ((DataVO) mAdapter.getData().get(0)).setNumber3("(" + mTv04.getText().toString() + ")");
                mFailCount = 0;
                rightCount1 = rightCount1 + 1;
                switch (mTv04.getText().toString().indexOf(str.substring(4, 5))) {
                    case 0:
                        rightCount1_1 = rightCount1_1 + 1;
                        break;
                    case 1:
                        rightCount1_2 = rightCount1_2 + 1;
                        break;
                    case 2:
                        rightCount1_3 = rightCount1_3 + 1;
                        break;
                    default:
                        break;
                }

            } else {
                if (mTv04.getText().toString().length() == 0) {
                    ((DataVO) mAdapter.getData().get(0)).setYuce3("无推荐");
                    mFailCount = 0;
                } else {
                    ((DataVO) mAdapter.getData().get(0)).setYuce3("错");
                    ((DataVO) mAdapter.getData().get(0)).setNumber3("(" + mTv04.getText().toString() + ")");
                    mFailCount = mFailCount + 1;
                    failCount1 = failCount1 + 1;
                }

            }


            if (mTv05.getText().toString().contains(str.substring(4, 5))) {
                ((DataVO) mAdapter.getData().get(0)).setYuce4("对");
                ((DataVO) mAdapter.getData().get(0)).setNumber4("(" + mTv05.getText().toString() + ")");
                mFailCount2 = 0;
                rightCount2 = rightCount2 + 1;
                switch (mTv05.getText().toString().indexOf(str.substring(4, 5))) {
                    case 0:
                        rightCount2_1 = rightCount2_1 + 1;
                        break;
                    case 1:
                        rightCount2_2 = rightCount2_2 + 1;
                        break;
                    case 2:
                        rightCount2_3 = rightCount2_3 + 1;
                        break;
                    default:
                        break;
                }
                mTv05.setText("");
            } else {
                if (mTv05.getText().toString().length() == 0) {
                    ((DataVO) mAdapter.getData().get(0)).setYuce4("无推荐");
                    mFailCount2 = 0;
                } else {

                    ((DataVO) mAdapter.getData().get(0)).setYuce4("错");
                    ((DataVO) mAdapter.getData().get(0)).setNumber4("(" + mTv05.getText().toString() + ")");
                    mFailCount2 = mFailCount2 + 1;
                    failCount2 = failCount2 + 1;
                }

            }


            if (mTv07.getText().toString().contains(str.substring(4, 5))) {
                ((DataVO) mAdapter.getData().get(0)).setYuce5("对");
                ((DataVO) mAdapter.getData().get(0)).setNumber5("(" + mTv07.getText().toString() + ")");
            } else {
                if (mTv07.getText().toString().length() == 0) {
                    ((DataVO) mAdapter.getData().get(0)).setYuce5("无推荐");
                } else {
                    ((DataVO) mAdapter.getData().get(0)).setYuce5("错");
                    ((DataVO) mAdapter.getData().get(0)).setNumber5("(" + mTv07.getText().toString() + ")");
                }
            }


//            int maxIndex = 0;
//            int maxVaue = 0;
//            for (int j = 0; j < 5; j++) {
//                for (int i = 0; i < mAdapter.getData().size(); i++) {
//                    DataVO vo = (DataVO) mAdapter.getData().get(i);
//                    if (sameSb.substring(7, 10).contains(String.valueOf(vo.getResult().charAt(j)))) {
//                        if(i>maxVaue){
//                            maxIndex=j;
//                            maxVaue = i;
//                        }
//                        break;
//                    }
//                }
//            }
//            mTv12.setText((maxIndex+1)+"("+maxVaue+")"+"---"+sameSb.substring(7, 10));
//            mTv04.setText(sameSb.substring(5, 10));
        }


        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            DataVO dataVo = (DataVO) mAdapter.getData().get(i);
            for (int j = 0; j < getSameNumber(dataVo.getResult()).length(); j++) {
                if (!sb1.toString().contains(String.valueOf(getSameNumber(dataVo.getResult()).charAt(j)))) {
                    sb1.append(String.valueOf(getSameNumber(dataVo.getResult()).charAt(j)));
                } else {
                    if (!sb2.toString().contains(String.valueOf(getSameNumber(dataVo.getResult()).charAt(j))))
                        sb2.append(String.valueOf(getSameNumber(dataVo.getResult()).charAt(j)));
                }
            }
            if (sb2.toString().length() >= 5) {
                mTv04.setText(sb2.toString().substring(0, 3));
                mTv07.setText(sb2.toString().substring(0, 4));
                mTv05.setText(sb2.toString().substring(0,5));

//                mTv05.setText(sb2.toString().substring(0, 7));
//                mTv07.setText(sb2.toString().substring(0, 2) + mSameList.get(0));
                return;
            }
        }


//            if (mTv05.getText().toString().contains(str.substring(4, 5))) {
//                ((DataVO) mAdapter.getData().get(0)).setYuce4("对");
//                ((DataVO) mAdapter.getData().get(0)).setNumber4("(" + mTv05.getText().toString() + ")");
//                mFailCount2 = 0;
//                rightCount2 = rightCount2 + 1;
//                switch (mTv05.getText().toString().indexOf(str.substring(4, 5))) {
//                    case 0:
//                        rightCount2_1 = rightCount2_1 + 1;
//                        break;
//                    case 1:
//                        rightCount2_2 = rightCount2_2 + 1;
//                        break;
//                    case 2:
//                        rightCount2_3 = rightCount2_3 + 1;
//                        break;
//                    default:
//                        break;
//                }
//                mTv05.setText("");
//            } else {
//                if (mTv05.getText().toString().length() == 0) {
//                    ((DataVO) mAdapter.getData().get(0)).setYuce4("无推荐");
//                    mFailCount2 = 0;
//                } else {
//
//
//                    ((DataVO) mAdapter.getData().get(0)).setYuce4("错");
//                    ((DataVO) mAdapter.getData().get(0)).setNumber4("(" + mTv05.getText().toString() + ")");
//                    mFailCount2 = mFailCount2 + 1;
//                    failCount2 = failCount2 + 1;
//
//
//                }

//            }


//            if (mTv07.getText().toString().contains(str.substring(4, 5))) {
//                ((DataVO) mAdapter.getData().get(0)).setYuce5("对");
//                ((DataVO) mAdapter.getData().get(0)).setNumber5("(" + mTv07.getText().toString() + ")");
//                mTv07.setText("");
//                mFailCount3 = 0;
//                rightCount3 = rightCount3 + 1;
//            } else {
//                if (mTv07.getText().toString().length() == 0) {
//                    ((DataVO) mAdapter.getData().get(0)).setYuce5("无推荐");
//                    mFailCount3 = 0;
//                } else {
//                    ((DataVO) mAdapter.getData().get(0)).setYuce5("错");
//                    ((DataVO) mAdapter.getData().get(0)).setNumber5("(" + mTv07.getText().toString() + ")");
//                    mFailCount3 = mFailCount3 + 1;
//                    failCount3 = failCount3 + 1;
//                }
//            }


//            String str3 = getSameNumber( getNoSameNumber(((DataVO) mAdapter.getData().get(0)).getResult())+ getNoSameNumber(((DataVO) mAdapter.getData().get(1)).getResult()));
//            StringBuffer sb3 = new StringBuffer();
//            for (int i = 1; i < mAdapter.getData().size(); i++) {
//                if (((DataVO) mAdapter.getData().get(0)).getResult().contains(((DataVO) mAdapter.getData().get(i)).getResult().substring(4, 5))) {
//                    if (!sb3.toString().contains(((DataVO) mAdapter.getData().get(i)).getResult().substring(4, 5))) {
//                        sb3.append(((DataVO) mAdapter.getData().get(i)).getResult().substring(4, 5));
//                    }
//                }
//                if (sb3.length() == 3)
//                    break;
//            }
//
////            mFailCount2 %
//            if (0 == 0) {
//                if (sb3.length() > 0 && sb3.length() <= 3) {
//                    mTv04.setText(sb3.toString());
//                } else {
//                    mTv04.setText("");
//                }
//            }

//        } else {
//            ((DataVO) mAdapter.getData().get(0)).setYuce3("无推荐");
//        }


//        if (mTv12.getText().toString().length() != 0) {
//            String[] strarr = mTv12.getText().toString().toString().split(",");
//            mTv12.setText("");
//            StringBuffer sb = new StringBuffer();
//            for (int i = 0; i < strarr.length; i++) {
//                if (!strarr[i].contains(str.substring(4, 5))) {
//                    sb.append(strarr[i]).append(",");
//
//                }
//            }
//            mTv12.setText(sb.toString());
//        }
//
//        if (mTv04.getText().toString().length() != 0) {
//            String[] strarr = mTv04.getText().toString().toString().split(",");
//            mTv04.setText("");
//            StringBuffer sb = new StringBuffer();
//            for (int i = 0; i < strarr.length; i++) {
//                if (!strarr[i].contains(str.substring(4, 5))) {
//                    sb.append(strarr[i]).append(",");
//
//                }
//            }
//            mTv04.setText(sb.toString());
//        }


//        if (mTv05.getText().toString().length() != 0) {
//            String[] strarr = mTv05.getText().toString().toString().split(",");
//            mTv05.setText("");
//            StringBuffer sb = new StringBuffer();
//            for (int i = 0; i < strarr.length; i++) {
//                if (!strarr[i].contains(str.substring(4, 5))) {
//                    sb.append(strarr[i]).append(",");
//
//                }
//            }
//            mTv05.setText(sb.toString());
//        }
//        mFlag = true;
//        StringBuffer sb1 = new StringBuffer();
//        StringBuffer sb2 = new StringBuffer();
//        String str3 = "";
//        List<DataVO> listVo = mAdapter.getData();
//        for (int i = 1; i < listVo.size(); i++) {
//            if (listVo.get(i).getResult().contains(str.substring(4, 5)) && !listVo.get(i).getRepeat().contains(str.substring(4, 5))) {
//                listVo.get(i).setRepeat(listVo.get(i).getRepeat() + str.substring(4, 5));
//                if (listVo.get(i).getRepeat().length() == getNoSameNumber(listVo.get(i).getResult()).length()) {
//                    if (getNoSameNumber(listVo.get(i).getResult()).length() == 3) {
//                        if (mTv12.getText().toString().length() != 0) {
//                            mTv12.setText(mTv12.getText().toString() + "," + listVo.get(i).getRepeat());
//                        } else {
//                            mTv12.setText(listVo.get(i).getRepeat());
//                        }
//                    } else if (getNoSameNumber(listVo.get(i).getResult()).length() == 4) {
//                        if (mTv04.getText().toString().length() != 0) {
//                            mTv04.setText(mTv04.getText().toString() + "," + listVo.get(i).getRepeat());
//                        } else {
//                            mTv04.setText(listVo.get(i).getRepeat());
//                        }
//
//                    }
//                }
//            }


//            String str1 = "";
//            String str2 = "";
//            if ((listVo.get(i).getRepeat().length() + 1) == getNoSameNumber(listVo.get(i).getResult()).length()) {
//                str1 = getNoSameNumber(listVo.get(i).getResult());
//                for (int j = 0; j < listVo.get(i).getRepeat().length(); j++) {
//                    str1 = str1.replace(String.valueOf(listVo.get(i).getRepeat().charAt(j)), "");
//                }
////                mTv05.setText(getNoSameNumber(mTv05.getText().toString()+str1));
//                sb1.append(str1);
//            }
//
//            if ((listVo.get(i).getRepeat().length() + 2) == getNoSameNumber(listVo.get(i).getResult()).length()) {
//                str2 = getNoSameNumber(listVo.get(i).getResult());
//                for (int j = 0; j < listVo.get(i).getRepeat().length(); j++) {
//                    str2 = str2.replace(String.valueOf(listVo.get(i).getRepeat().charAt(j)), "");
//                }
////                mTv05.setText(getNoSameNumber(mTv05.getText().toString()+str2));
//                sb2.append(str2);
//            }
//        }

//        str3 = sb2.toString();
//        for (int j = 0; j < sb1.length(); j++) {
//            str3 = str3.replace(String.valueOf(sb1.toString().charAt(j)), "");
//        }
//
//        mTv05.setText(getNoSameNumber(str3) + "(" + getNoSameNumber(sb1.toString()) + ")");
//        mTv04.setText(getNoSameNumber(str3) );
//        mTv05.setText(getNoSameNumber(sb1.toString()));


        mAdapter.notifyDataSetChanged();
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
                                ACache.get(MainActivity3.this).put("key", JsonUtil.toJson(dataVO));
//                                allCount();
//                                threeCount();

                            }
                        })
                        .show();
                break;
            case R.id.reset_tv:
//                mAdapter.getData().remove(0);
//                mAdapter.notifyDataSetChanged();


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
            case R.id.yi_lou_tv:
//                if (Et08.getText().toString().length() == 0)
//                    return;
//                for (int i = 0; i < Et08.getText().toString().length(); i++) {
//                    mList.add(String.valueOf(Et08.getText().toString().charAt(i)));
//                }
//                mYiLouTv.setVisibility(View.GONE);
//                mTv08.setText(Et08.getText().toString());
//                Et08.setVisibility(View.GONE);
//                mTv08.setVisibility(View.VISIBLE);

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

    public void threeCount() {
        mThreeCount1 = 0;
        mThreeCount2 = 0;
        mThreeCount3 = 0;
        mThreeCount4 = 0;
        mThreeCount5 = 0;
        mThreeCount6 = 0;
        mThreeCount7 = 0;
        mThreeCount8 = 0;
        mThreeCount9 = 0;
        mThreeCount10 = 0;
        mThreeCountOther = 0;
        mFourCount1 = 0;
        mFourCount2 = 0;
        mFourCount3 = 0;

        mFourCountOther = 0;
        mFiveCount1 = 0;
        mFiveCount2 = 0;
        mFiveCount3 = 0;
        mFiveCount4 = 0;
        mFiveCount5 = 0;
        mFiveCount6 = 0;
        mFiveCount7 = 0;
        mFiveCount8 = 0;
        mFiveCountOther = 0;

        mPosition1 = 0;
        mPosition2 = 0;
        mPosition3 = 0;
        mPosition4 = 0;
        mPosition5 = 0;

        mThree3Count1 = 0;
        mThree3Count2 = 0;
        mThree3Count3 = 0;
        mThree3CountOther = 0;

        mFour3Count1 = 0;
        mFour3Count2 = 0;
        mFour3Count3 = 0;
        mFour3Count4 = 0;
        mFour3Count5 = 0;
        mFour3Count6 = 0;
        mFour3Count7 = 0;
        mFour3Count8 = 0;
        mFour3Count9 = 0;
        mFour3Count10 = 0;
        mFour3CountOther = 0;
        mFive3Count1 = 0;
        mFive3Count2 = 0;
        mFive3Count3 = 0;
        mFive3CountOther = 0;
//        List<DataVO> list = new ArrayList<>();
//        list = AppApplication.getInstance().getDataVO();
        List<DataVO> list = mAdapter.getData();
        StringBuffer sb4 = new StringBuffer();
        StringBuffer sb5 = new StringBuffer();
        StringBuffer sb3 = new StringBuffer();
//        int index = 0;
//        if (list != null && list.size() < 18)
//            return;
//        for (int i = list.size() - 1; i >= 18; i--) {
//            sb4.setLength(0);
//            sb5.setLength(0);
//            sb3.setLength(0);
//            index = 0;
//            if (getNoSameNumber(list.get(i).getResult()).length() == 4) {
//                for (int j = i - 1; j < list.size(); j--) {
//                    index++;
//                    if (list.get(i).getResult().contains(list.get(j).getResult().substring(4, 5))) {
//                        sb4.append(getNoSameNumber(list.get(i).getResult()).replace(list.get(j).getResult().substring(4, 5), ""));
//                        break;
//                    }
//                }
//            } else if (getNoSameNumber(list.get(i).getResult()).length() == 5) {
//                index++;
//                for (int j = i - 1; j < list.size(); j--) {
//                    if (list.get(i).getResult().contains(list.get(j).getResult().substring(4, 5))) {
//                        sb5.append(getNoSameNumber(list.get(i).getResult()).replace(list.get(j).getResult().substring(4, 5), ""));
//                        break;
//                    }
//                }
//            } else if (getNoSameNumber(list.get(i).getResult()).length() == 3) {
//                for (int j = i - 1; j < list.size(); j--) {
//                    index++;
//                    if (list.get(i).getResult().contains(list.get(j).getResult().substring(4, 5))) {
//                        sb3.append(getNoSameNumber(list.get(i).getResult()).replace(list.get(j).getResult().substring(4, 5), ""));
//                        break;
//                    }
//
//                }
//            }
//
//
//            String str3 = sb3.toString();
//            if (str3.length() == 2) {
//                if (str3.contains(list.get(i - index - 1).getResult().substring(4, 5))) {
//                    mThreeCount1 = mThreeCount1 + 1;
//                    list.get(i).setNumber3Result("1");
//                } else if (str3.contains(list.get(i - index - 2).getResult().substring(4, 5))) {
//                    mThreeCount2 = mThreeCount2 + 1;
//                    list.get(i).setNumber3Result("1");
//                } else if (str3.contains(list.get(i - index - 3).getResult().substring(4, 5))) {
//                    mThreeCount3 = mThreeCount3 + 1;
//                    list.get(i).setNumber3Result("1");
//                } else if (str3.contains(list.get(i - index - 4).getResult().substring(4, 5))) {
//                    mThreeCount4 = mThreeCount4 + 1;
//                    list.get(i).setNumber3Result("1");
//                } else if (str3.contains(list.get(i - index - 5).getResult().substring(4, 5))) {
//                    mThreeCount5 = mThreeCount5 + 1;
//                    list.get(i).setNumber3Result("1");
//                } else if (str3.contains(list.get(i - index - 6).getResult().substring(4, 5))) {
//                    mThreeCount6 = mThreeCount6 + 1;
//                    list.get(i).setNumber3Result("1");
//                } else if (str3.contains(list.get(i - index - 7).getResult().substring(4, 5))) {
//                    mThreeCount7 = mThreeCount7 + 1;
//                    list.get(i).setNumber3Result("1");
//                } else if (str3.contains(list.get(i - index - 8).getResult().substring(4, 5))) {
//                    mThreeCount8 = mThreeCount8 + 1;
//                    list.get(i).setNumber3Result("0");
//                } else if (str3.contains(list.get(i - index - 9).getResult().substring(4, 5))) {
//                    mThreeCount9 = mThreeCount9 + 1;
//                    list.get(i).setNumber3Result("0");
//                } else if (str3.contains(list.get(i - index - 10).getResult().substring(4, 5))) {
//                    mThreeCount10 = mThreeCount10 + 1;
//                    list.get(i).setNumber3Result("0");
//                } else {
//                    mThreeCountOther = mThreeCountOther + 1;
//                    list.get(i).setNumber3Result("0");
//                }
//            }
//
//
//            String str4 = sb4.toString();
//            if (str4.length() == 3) {
//                if (str4.contains(list.get(i - index - 1).getResult().substring(4, 5))) {
//                    mFour3Count1 = mFour3Count1 + 1;
//                    list.get(i).setNumber4Result("1");
//                } else if (str4.contains(list.get(i - index - 2).getResult().substring(4, 5))) {
//                    mFour3Count2 = mFour3Count2 + 1;
//                    list.get(i).setNumber4Result("1");
//                } else if (str4.contains(list.get(i - index - 3).getResult().substring(4, 5))) {
//                    mFour3Count3 = mFour3Count3 + 1;
//                    list.get(i).setNumber4Result("1");
//                } else if (str4.contains(list.get(i - index - 4).getResult().substring(4, 5))) {
//                    mFour3Count4 = mFour3Count4 + 1;
//                    list.get(i).setNumber4Result("1");
//                } else if (str4.contains(list.get(i - index - 5).getResult().substring(4, 5))) {
//                    mFour3Count5 = mFour3Count5 + 1;
//                    list.get(i).setNumber4Result("1");
//                } else if (str4.contains(list.get(i - index - 6).getResult().substring(4, 5))) {
//                    mFour3Count6 = mFour3Count6 + 1;
//                    list.get(i).setNumber4Result("1");
//                } else if (str4.contains(list.get(i - index - 7).getResult().substring(4, 5))) {
//                    mFour3Count7 = mFour3Count7 + 1;
//                    list.get(i).setNumber4Result("0");
//                } else if (str4.contains(list.get(i - index - 8).getResult().substring(4, 5))) {
//                    mFour3Count8 = mFour3Count8 + 1;
//                    list.get(i).setNumber4Result("0");
//                } else if (str4.contains(list.get(i - index - 9).getResult().substring(4, 5))) {
//                    mFour3Count9 = mFour3Count9 + 1;
//                    list.get(i).setNumber4Result("0");
//                } else if (str4.contains(list.get(i - index - 10).getResult().substring(4, 5))) {
//                    mFour3Count10 = mFour3Count10 + 1;
//                    list.get(i).setNumber4Result("0");
//                } else {
//                    mFour3CountOther = mFour3CountOther + 1;
//                }
//            }
//
//            String str5 = sb5.toString();
//            if (str5.length() == 4) {
//                if (str5.contains(list.get(i - index - 1).getResult().substring(4, 5))) {
//                    mFiveCount1 = mFiveCount1 + 1;
//                } else if (str5.contains(list.get(i - index - 2).getResult().substring(4, 5))) {
//                    mFiveCount2 = mFiveCount2 + 1;
//                } else if (str5.contains(list.get(i - index - 3).getResult().substring(4, 5))) {
//                    mFiveCount3 = mFiveCount3 + 1;
//                } else if (str5.contains(list.get(i - index - 4).getResult().substring(4, 5))) {
//                    mFiveCount4 = mFiveCount4 + 1;
//
//                } else if (str5.contains(list.get(i - index - 5).getResult().substring(4, 5))) {
//                    mFiveCount5 = mFiveCount5 + 1;
//
//                } else if (str5.contains(list.get(i - index - 6).getResult().substring(4, 5))) {
//                    mFiveCount6 = mFiveCount6 + 1;
//
//                } else {
//                    mFiveCountOther = mFiveCountOther + 1;
//
//                }
//            }
//
//        }


        if (list != null && list.size() < 6)
            return;
        for (int i = list.size() - 1; i >= 6; i--) {

            String str = getNoSameNumber(list.get(i).getResult());
            if (str.length() == 4) {
                if (str.contains(list.get(i - 1).getResult().substring(4, 5))) {
                    if (i <= 120) {
                        mFourCount1 = mFourCount1 + 1;
                        list.get(i).setNumber4Result("1" + "(" + mFourCountOther + "--" + (mAllFourCountOther - mFourCountOther) + ")");
                    }

                    list.get(i).setNumberResult("1");
                } else if (str.contains(list.get(i - 2).getResult().substring(4, 5))) {
                    if (i <= 120) {
                        mFourCount2 = mFourCount2 + 1;
                        list.get(i).setNumber4Result("1" + "(" + mFourCountOther + "--" + (mAllFourCountOther - mFourCountOther) + ")");
                    }

                    list.get(i).setNumberResult("1");
                } else if (str.contains(list.get(i - 3).getResult().substring(4, 5))) {
                    if (i <= 120) {
                        mFourCount3 = mFourCount3 + 1;
                        list.get(i).setNumber4Result("1" + "(" + mFourCountOther + "--" + (mAllFourCountOther - mFourCountOther) + ")");
                    }
                    list.get(i).setNumberResult("1");
                } else if (str.contains(list.get(i - 4).getResult().substring(4, 5))) {
                    if (i <= 120) {
                        mFourCountOther = mFourCountOther + 1;
                        list.get(i).setNumber4Result("0" + "(" + mFourCountOther + "--" + (mAllFourCountOther - mFourCountOther) + ")");
                    }

                    list.get(i).setNumberResult("1");
                } else if (str.contains(list.get(i - 5).getResult().substring(4, 5))) {
                    if (i <= 120) {
                        mFourCountOther = mFourCountOther + 1;
                        list.get(i).setNumber4Result("0" + "(" + mFourCountOther + "--" + (mAllFourCountOther - mFourCountOther) + ")");
                    }
                    list.get(i).setNumberResult("1");
                } else if (str.contains(list.get(i - 6).getResult().substring(4, 5))) {
                    if (i <= 120) {
                        mFourCountOther = mFourCountOther + 1;
                        list.get(i).setNumber4Result("0" + "(" + mFourCountOther + "--" + (mAllFourCountOther - mFourCountOther) + ")");
                    }
                    list.get(i).setNumberResult("1");
                }
//                else if (str.contains(list.get(i - 7).getResult().substring(4, 5))) {
//                    mFourCount7 = mFourCount7 + 1;
////                    list.get(i).setNumber4Result("1" + "(" + mFourCountOther + "--" + (mAllFourCountOther - mFourCountOther) + ")");
//                    list.get(i).setNumberResult("1");
//                }
                else {
                    if (i <= 120) {
                        mFourCountOther = mFourCountOther + 1;
                        list.get(i).setNumber4Result("0" + "(" + mFourCountOther + "--" + (mAllFourCountOther - mFourCountOther) + ")");
                    }
                    list.get(i).setNumberResult("0");
                }
            } else if (str.length() == 3) {
                if (str.contains(list.get(i - 1).getResult().substring(4, 5))) {
                    list.get(i).setNumberResult("1");
                } else if (str.contains(list.get(i - 2).getResult().substring(4, 5))) {
                    list.get(i).setNumberResult("1");
                } else if (str.contains(list.get(i - 3).getResult().substring(4, 5))) {
                    list.get(i).setNumberResult("1");
                } else if (str.contains(list.get(i - 4).getResult().substring(4, 5))) {
                    list.get(i).setNumberResult("1");
                } else if (str.contains(list.get(i - 5).getResult().substring(4, 5))) {
                    list.get(i).setNumberResult("1");
                } else if (str.contains(list.get(i - 6).getResult().substring(4, 5))) {
                    list.get(i).setNumberResult("1");
                } else {
                    list.get(i).setNumberResult("0");
                }
            } else if (str.length() == 5) {
                if (str.contains(list.get(i - 1).getResult().substring(4, 5))) {
                    list.get(i).setNumberResult("1");
                } else if (str.contains(list.get(i - 2).getResult().substring(4, 5))) {
                    list.get(i).setNumberResult("1");
                } else if (str.contains(list.get(i - 3).getResult().substring(4, 5))) {
                    list.get(i).setNumberResult("1");
                } else if (str.contains(list.get(i - 4).getResult().substring(4, 5))) {
                    list.get(i).setNumberResult("1");
                } else if (str.contains(list.get(i - 5).getResult().substring(4, 5))) {
                    list.get(i).setNumberResult("1");
                } else if (str.contains(list.get(i - 6).getResult().substring(4, 5))) {
                    list.get(i).setNumberResult("1");
                }
// else if (str.contains(list.get(i - 7).getResult().substring(4, 5))) {
//                    list.get(i).setNumberResult("1");
//                }
                else {
                    list.get(i).setNumberResult("0");
                }
            }

            mAdapter.notifyDataSetChanged();
        }

        System.out.println("=====================4号：" + mFourCount1 + "--" + mFourCount2 + "--" + mFourCount3 + "--" + mFourCountOther);
        System.out.println("=====================5号：" + mFiveCount1 + "--" + mFiveCount2 + "--" + mFiveCount3 + "--" + mFiveCountOther);
        mTv06.setText("3号：" + mThreeCount1 + "--" + mThreeCount2 + "--" + mThreeCount3 + "--" + mThreeCount4 + "--" + mThreeCount5
                + "--" + mThreeCount6 + "--" + mThreeCount7 + "--" + mThreeCount8 + "--" + mThreeCount9 + "--" + mThreeCount10 + "--" +
                mThreeCountOther);
        mThreeAll.setText("3号：" + mThree3Count1 + "--" + mThree3Count2 + "--" + mThree3Count3 + "--" + mThree3CountOther);


        mFive.setText("4号：" + mFourCount1 + "--" + mFourCount2 + "--" + mFourCount3 + "--" + mFourCountOther + "(" + (mAllFourCount1 - mFourCount1) + "--" + (mAllFourCount2 - mFourCount2) + "--" +
                (mAllFourCount3 - mFourCount3) + "--" + (mAllFourCountOther - mFourCountOther) + ")");
        mTv08.setText("4号：" + mFour3Count1 + "--" + mFour3Count2 + "--" + mFour3Count3 + "--" + mFour3Count4
                + "--" + mFour3Count5 + "--" + mFour3Count6 + "--" + mFour3Count7 + "--" + mFour3Count8 + "--" + mFour3Count9 + "--" + mFour3Count10 + "--" + mFour3CountOther);


//        mFive.setText("4号：" + mFiveCount1 + "--" + mFiveCount2 + "--" + mFiveCount3 + "--" + mFiveCount4 + "--" + mFiveCount5 + "--" + mFiveCount6 + "--" +
//                mFiveCountOther);
//        mFive.setText("5号：" + mFiveCount1 + "--" + mFiveCount2 + "--" + mFiveCount3 + "--" + mFiveCount4+ "--" + mFiveCount5+ "--" + mFiveCount6+ "--" + mFiveCountOther );
//        mFiveAll.setText("5号：" + mFive3Count1 + "--" + mFive3Count2 + "--" + mFive3Count3 + "--" + mFive3CountOther);


    }


    public void countPosition(String result, String str) {
        for (int j = 0; j < 5; j++) {
            if (str.substring(4, 5).equals(String.valueOf(result.charAt(j)))) {
                switch (j + 1) {
                    case 1:
                        mPosition1 = mPosition1 + 1;
                        break;
                    case 2:
                        mPosition2 = mPosition2 + 1;
                        break;
                    case 3:
                        mPosition3 = mPosition3 + 1;
                        break;
                    case 4:
                        mPosition4 = mPosition4 + 1;
                        break;
                    case 5:
                        mPosition5 = mPosition5 + 1;
                        break;
                    default:
                        break;
                }
            }
        }


    }

    public void countAllPosition(String result, String str) {
        for (int j = 0; j < 5; j++) {
            if (str.substring(4, 5).equals(String.valueOf(result.charAt(j)))) {
                switch (j + 1) {
                    case 1:
                        mAllPosition1 = mAllPosition1 + 1;
                        break;
                    case 2:
                        mAllPosition2 = mAllPosition2 + 1;
                        break;
                    case 3:
                        mAllPosition3 = mAllPosition3 + 1;
                        break;
                    case 4:
                        mAllPosition4 = mAllPosition4 + 1;
                        break;
                    case 5:
                        mAllPosition5 = mAllPosition5 + 1;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void allCount() {
        mAllThreeCount1 = 0;
        mAllThreeCount2 = 0;
        mAllThreeCount3 = 0;
        mAllThreeCountOther = 0;
        mAllFourCount1 = 0;
        mAllFourCount2 = 0;
        mAllFourCount3 = 0;
        mAllFourCountOther = 0;
        mAllFiveCount1 = 0;
        mAllFiveCount2 = 0;
        mAllFiveCount3 = 0;
        mAllFiveCountOther = 0;
        mAllPosition1 = 0;
        mAllPosition2 = 0;
        mAllPosition3 = 0;
        mAllPosition4 = 0;
        mAllPosition5 = 0;

//        List<DataVO> list = new ArrayList<>();
//        list = AppApplication.getInstance().getDataVO();
        List<DataVO> list = mAdapter.getData();
        if (list != null && list.size() < 240)
            return;
        for (int i = 240; i >= 3; i--) {
            String str = getNoSameNumber(list.get(i).getResult());
            if (str.length() == 3) {
                if (str.contains(list.get(i - 1).getResult().substring(4, 5))) {
                    mAllThreeCount1 = mAllThreeCount1 + 1;
                } else if (str.contains(list.get(i - 2).getResult().substring(4, 5))) {
                    mAllThreeCount2 = mAllThreeCount2 + 1;
                } else if (str.contains(list.get(i - 3).getResult().substring(4, 5))) {
                    mAllThreeCount3 = mAllThreeCount3 + 1;
                } else {
                    mAllThreeCountOther = mAllThreeCountOther + 1;
                }
            } else if (str.length() == 4) {
                if (str.contains(list.get(i - 1).getResult().substring(4, 5))) {
                    mAllFourCount1 = mAllFourCount1 + 1;
                } else if (str.contains(list.get(i - 2).getResult().substring(4, 5))) {
                    mAllFourCount2 = mAllFourCount2 + 1;
                } else if (str.contains(list.get(i - 3).getResult().substring(4, 5))) {
                    mAllFourCount3 = mAllFourCount3 + 1;
                } else {
                    mAllFourCountOther = mAllFourCountOther + 1;
                }
            } else if (str.length() == 5) {
                if (str.contains(list.get(i - 1).getResult().substring(4, 5))) {
                    mFiveCount1 = mFiveCount1 + 1;
                } else if (str.contains(list.get(i - 2).getResult().substring(4, 5))) {
                    mFiveCount2 = mFiveCount2 + 1;
                } else if (str.contains(list.get(i - 3).getResult().substring(4, 5))) {
                    mFiveCount3 = mFiveCount3 + 1;
                } else {
                    mFiveCountOther = mFiveCountOther + 1;
                }
            }

//            if (str.contains(list.get(i - 1).getResult().substring(4, 5))) {
//                countAllPosition(list.get(i).getResult(), list.get(i - 1).getResult());
//            } else if (str.contains(list.get(i - 2).getResult().substring(4, 5))) {
//                countAllPosition(list.get(i).getResult(), list.get(i - 2).getResult());
//            } else if (str.contains(list.get(i - 3).getResult().substring(4, 5))) {
//                countAllPosition(list.get(i).getResult(), list.get(i - 3).getResult());
//            }
        }

//        if (list.get(2).getResult().contains(list.get(1).getResult().substring(4, 5))) {
//            countAllPosition(list.get(2).getResult(), list.get(1).getResult());
//        } else if (list.get(2).getResult().contains(list.get(0).getResult().substring(4, 5))) {
//            countAllPosition(list.get(2).getResult(), list.get(0).getResult());
//        }

//
//
//        if (list.get(1).getResult().contains(list.get(0).getResult().substring(4, 5))) {
//            countAllPosition(list.get(1).getResult(), list.get(0).getResult());
//        }

        System.out.println("=====================4号：" + mAllFourCount1 + "--" + mAllFourCount2 + "--" + mAllFourCount3 + "--" + mAllFourCountOther);
        System.out.println("=====================5号：" + mAllFiveCount1 + "--" + mAllFiveCount2 + "--" + mAllFiveCount3 + "--" + mAllFiveCountOther);

//        mThreeAll.setText("3号：" + mAllThreeCount1 + "--" + mAllThreeCount2 + "--" + mAllThreeCount3 + "--" + mAllThreeCountOther);
//        mTv08.setText("4号：" + mAllFourCount1 + "--" + mAllFourCount2 + "--" + mAllFourCount3 + "--" + mAllFourCountOther);
//        mFiveAll.setText("5号：" + mAllFiveCount1 + "--" + mAllFiveCount2 + "--" + mAllFiveCount3 + "--" + mAllFiveCountOther);
//        mFiveAll.setText("号：" + mAllPosition1 + "--" + mAllPosition2 + "--" + mAllPosition3 + "--" + mAllPosition4 + "--" + mAllPosition5);
    }
}