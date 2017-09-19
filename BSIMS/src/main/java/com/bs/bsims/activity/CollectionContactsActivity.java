/**
 * 
 */

package com.bs.bsims.activity;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.SortAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.model.SortComparatorVO;
import com.bs.bsims.utils.CharacterParser;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSSideBar;
import com.bs.bsims.view.BSSideBar.OnTouchingLetterChangedListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-6-12
 * @version 2.0
 */
public class CollectionContactsActivity extends BaseActivity {

    private static final String TAG = "ContactsLetterTabFragment";
    private Activity mActivity;
    private TextView mMsgTv;
    private String mMsgName;
    private ListView mListView;
    private BSSideBar mBSSideBar;
    private CharacterParser mCharacterParser;
    private List<DepartmentAndEmployeeVO> mList;
    private TextView mDialog;
    private SortAdapter mSortAdapter;
    private BSIndexEditText mBSIndexEditText;
    private ResultVO mResultInfoVO;
    private SortComparatorVO mSortComparatorVO;

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.fragment_letter_tab, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub
        if(mResultInfoVO.getUserscount().equals("0")){
            super.showNoContentView();
        }
        else{
            mCharacterParser = CharacterParser.getInstance();
            mList = new ArrayList<DepartmentAndEmployeeVO>();
            mSortAdapter = new SortAdapter(mActivity, mList);
            mListView.setAdapter(mSortAdapter);
            mSortComparatorVO = new SortComparatorVO();
            List<DepartmentAndEmployeeVO> list = new ArrayList<DepartmentAndEmployeeVO>();
            list = mResultInfoVO.getUsers();
            sortData(list);
            Collections.sort(mList, mSortComparatorVO);
        }
        
        
   
    }
    
   

    private void sortData(List<DepartmentAndEmployeeVO> list) {
        for (int i = 0; i < list.size(); i++) {
            DepartmentAndEmployeeVO personnelVO = list.get(i);

            // 汉字转换成拼音List<BusinessVisit> visitList
            String pinyin = mCharacterParser.getSelling(personnelVO.getFullname());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                personnelVO.setSortLetters(sortString.toUpperCase());
            } else {
                personnelVO.setSortLetters("#");
            }
            mList.add(personnelVO);
        }
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * 
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<DepartmentAndEmployeeVO> filterDateList = new ArrayList<DepartmentAndEmployeeVO>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mList;
        } else {
            filterDateList.clear();
            for (DepartmentAndEmployeeVO personnelVO : mList) {
                String name = personnelVO.getFullname();
                if (name.indexOf(filterStr.toString()) != -1 || mCharacterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(personnelVO);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, mSortComparatorVO);
        mSortAdapter.updateListView(filterDateList);
    }
    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mActivity=this;
        mListView = (ListView) findViewById(R.id.lv_contacts_sort);
        mBSSideBar = (BSSideBar) findViewById(R.id.sidrbar);
        mDialog = (TextView) findViewById(R.id.dialog);
        mBSSideBar.setTextView(mDialog);
        mBSIndexEditText = (BSIndexEditText) findViewById(R.id.filter_edit);
        mTitleTv.setText("我收藏的联系人");
    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        mBSSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = mSortAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }
            }
        });

        mBSIndexEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("collec", "1");
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CONTACTS_INFO, map);
            mResultInfoVO = gson.fromJson(jsonStrList, ResultVO.class);
            if (Constant.RESULT_CODE.equals(mResultInfoVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }
}
