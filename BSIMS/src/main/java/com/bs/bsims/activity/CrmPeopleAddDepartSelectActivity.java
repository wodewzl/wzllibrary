
package com.bs.bsims.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmOptionsAdapter;
import com.bs.bsims.adapter.NoticeSortAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.fragment.ContactsLetterTabFragment;
import com.bs.bsims.model.CrmOptionsListVO;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.model.SortComparatorVO;
import com.bs.bsims.utils.CharacterParser;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.view.BSSideBar;
import com.bs.bsims.view.BSSideBar.OnTouchingLetterChangedListener;
import com.bs.bsims.view.BSTopIndicator;
import com.bs.bsims.view.BSTopIndicator.OnTopIndicatorListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CrmPeopleAddDepartSelectActivity extends BaseActivity implements OnTopIndicatorListener {
    private BSTopIndicator mTopIndicator;
    private ContactsLetterTabFragment mFragment;
    private String mType;
    private ResultVO mResultVO;
    private ListView mListView, mDepartListView;
    private BSSideBar mBSSideBar;
    private CharacterParser mCharacterParser;
    private List<DepartmentAndEmployeeVO> mList;
    private TextView mDialog;
    private NoticeSortAdapter mSortAdapter;
    private SortComparatorVO mSortComparatorVO;
    private CrmOptionsListVO mCrmOptionsVO;
    private FrameLayout mPeopleLayout;
    private CrmOptionsAdapter mDepartmentAdapter;

    private int[] mDrawableIds;
    private CharSequence[] mLabels;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_people_add_depart_select_activity, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
        // mDepartmentAdapter.updateData(mCrmOptionsVO.getArray());

        mCharacterParser = CharacterParser.getInstance();
        mList = new ArrayList<DepartmentAndEmployeeVO>();
        mSortAdapter = new NoticeSortAdapter(this);
        mListView.setAdapter(mSortAdapter);

        mSortComparatorVO = new SortComparatorVO();
        // List<DepartmentAndEmployeeVO> list = new ArrayList<DepartmentAndEmployeeVO>();
        // list = BSApplication.getInstance().getResultVO().getUsers();
        List<DepartmentAndEmployeeVO> list = mResultVO.getArray();
        sortData(list);
        Collections.sort(mList, mSortComparatorVO);
        mSortAdapter.updateData(mList);
    }

    @Override
    public void initView() {
        mTitleTv.setText("选择人员和部门");
        mTopIndicator = (BSTopIndicator) findViewById(R.id.top_indicator);
        initTitle();
        mTopIndicator.setmLabels(mLabels);
        mTopIndicator.setmDrawableIds(mDrawableIds);
        mTopIndicator.updateUI(this);
        mPeopleLayout = (FrameLayout) findViewById(R.id.people_layout);
        mListView = (ListView) findViewById(R.id.listview_people);
        mDepartListView = (ListView) findViewById(R.id.listview_depart);
        mDepartmentAdapter = new CrmOptionsAdapter(this, 1);
        mDepartListView.setAdapter(mDepartmentAdapter);
        mBSSideBar = (BSSideBar) findViewById(R.id.sidrbar);
        mDialog = (TextView) findViewById(R.id.dialog);
        mBSSideBar.setTextView(mDialog);
        mPeopleLayout.setVisibility(View.VISIBLE);
        mDepartListView.setVisibility(View.GONE);
        initData();
    }

    // 只有人员
    public void initTitle() {
        if (getIntent().getStringExtra("isHidden") != null) {
            mDrawableIds = new int[] {
                    0
            };
            mLabels = new CharSequence[] {
                    "人员"
            };
        }
        else {
            mDrawableIds = new int[] {
                    0, 0
            };
            mLabels = new CharSequence[] {
                    "人员", "部门"
            };
        }
    }

    @Override
    public void bindViewsListener() {
        mTopIndicator.setOnTopIndicatorListener(this);
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

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2, long id) {
                DepartmentAndEmployeeVO vo = (DepartmentAndEmployeeVO) mSortAdapter.getItem((int) id);
                ImageView stautsImg = (ImageView) view.findViewById(R.id.status_img);
                stautsImg.setImageResource(R.drawable.common_ic_selected);
                Intent intent = new Intent();
                intent.putExtra("userid", vo.getUserid());
                intent.putExtra("name", vo.getFullname());
                intent.putExtra("dname", vo.getDname());
                CrmPeopleAddDepartSelectActivity.this.setResult(2015, intent);
                CrmPeopleAddDepartSelectActivity.this.finish();
            }
        });

        mDepartListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // mCrmOptionsVO.getArray().get((int) id).getId();
                CrmOptionsListVO vo = (CrmOptionsListVO) mDepartmentAdapter.getItem((int) id);
                ImageView stautsImg = (ImageView) view.findViewById(R.id.status_img_left);
                stautsImg.setImageResource(R.drawable.common_ic_selected);
                Intent intent = new Intent();
                intent.putExtra("did", vo.getId());
                intent.putExtra("dname", vo.getName());
                CrmPeopleAddDepartSelectActivity.this.setResult(2015, intent);
                CrmPeopleAddDepartSelectActivity.this.finish();
            }
        });

    }

    public void getDepartData() {
        new Thread() {
            public void run() {
                try {
                    Gson gson = new Gson();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("userid", BSApplication.getInstance().getUserId());
                    map.put("subordinate", "1");
                    map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
                    String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_DEPARTMENT_URL, map);
                    mCrmOptionsVO = gson.fromJson(jsonStr, CrmOptionsListVO.class);
                    if (HttpClientUtil.isNetworkConnected(CrmPeopleAddDepartSelectActivity.this)) {
                        if (mCrmOptionsVO == null) {
                            getDepartData();
                        } else {
                            mDepartListView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mDepartmentAdapter.updateData(mCrmOptionsVO.getArray());
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("subordinate", "1");
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_GETPEROSONTOBUSSINESTREE, map);
            mResultVO = gson.fromJson(jsonStr, ResultVO.class);
            if (Constant.RESULT_CODE.equals(mResultVO.getCode())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }

    @Override
    public void onIndicatorSelected(int index) {
        mTopIndicator.setTabsDisplay(this, index);
        if (index == 0) {
            mPeopleLayout.setVisibility(View.VISIBLE);
            mDepartListView.setVisibility(View.GONE);
        } else if (index == 1) {
            mPeopleLayout.setVisibility(View.GONE);
            mDepartListView.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        getDepartData();
        // mCharacterParser = CharacterParser.getInstance();
        // mList = new ArrayList<DepartmentAndEmployeeVO>();
        // mSortAdapter = new NoticeSortAdapter(this);
        // mListView.setAdapter(mSortAdapter);
        //
        // mSortComparatorVO = new SortComparatorVO();
        // List<DepartmentAndEmployeeVO> list = new ArrayList<DepartmentAndEmployeeVO>();
        // list = BSApplication.getInstance().getResultVO().getUsers();
        // sortData(list);
        // Collections.sort(mList, mSortComparatorVO);
        // mSortAdapter.updateData(mList);
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

}
