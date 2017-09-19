
package com.bs.bsims.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.NoticeSortAdapter;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.NoticeObjectVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.utils.CharacterParser;
import com.bs.bsims.view.BSSideBar;
import com.bs.bsims.view.BSSideBar.OnTouchingLetterChangedListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NoticePersonActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    private static final int BY_DEPARTMENTS = 2015;
    private ListView mListView;
    private BSSideBar mBSSideBar;
    private CharacterParser mCharacterParser;
    private TextView mDialog;
    private NoticeSortAdapter mSortAdapter;
    private SortComparatorDepVo mSortComparatorVO;
    private List<DepartmentAndEmployeeVO> mList;
    private LinearLayout mDepartLayout, mPostLayout;
    private List<NoticeObjectVO> mTwoList = new ArrayList<NoticeObjectVO>();
    private List<EmployeeVO> mOneList = new ArrayList<EmployeeVO>();
    int requst_number = 0;
    private StringBuffer mSb = new StringBuffer();
    // private List<DepartmentAndEmployeeVO> mSelectedList = new
    // ArrayList<DepartmentAndEmployeeVO>();
    private LinearLayout mAllComapanyLayout;
    private CheckBox mAllCb;
    private boolean mIsAll = false;// 默认不是全部，选择全部为true;
    private List<String> mlettersList = new ArrayList<String>();

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.notice_person, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void initView() {
        mTitleTv.setText("通知对象");
        mOkTv.setText("确定");
        mListView = (ListView) findViewById(R.id.lv_contacts_sort);
        mBSSideBar = (BSSideBar) findViewById(R.id.sidrbar);
        mDialog = (TextView) findViewById(R.id.dialog);
        LinearLayout headLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.notice_person_list_head, null);
        mListView.addHeaderView(headLayout);
        mDepartLayout = (LinearLayout) headLayout.findViewById(R.id.depart_layout);
        mPostLayout = (LinearLayout) headLayout.findViewById(R.id.post_layout);
        mAllComapanyLayout = (LinearLayout) headLayout.findViewById(R.id.all_company_layout);
        mAllCb = (CheckBox) findViewById(R.id.status_img);
        mBSSideBar.setTextView(mDialog);
        mSortAdapter = new NoticeSortAdapter(this);
        mListView.setAdapter(mSortAdapter);

        mList = new ArrayList<DepartmentAndEmployeeVO>();
        initData();

    }

    @Override
    public void bindViewsListener() {
        mDepartLayout.setOnClickListener(this);
        mPostLayout.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
        mAllComapanyLayout.setOnClickListener(this);
        mOkTv.setOnClickListener(this);

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

    }

    private void initData() {

        mCharacterParser = CharacterParser.getInstance();
        mSortAdapter = new NoticeSortAdapter(this);
        mListView.setAdapter(mSortAdapter);
        mSortComparatorVO = new SortComparatorDepVo();
        List<DepartmentAndEmployeeVO> list = ResultVO.getInstance().getUsers();
        sortData(list);
        Collections.sort(mList, mSortComparatorVO);
        sortLetters();
        String[] letters = mlettersList.toArray(new String[mlettersList.size()]);

        mBSSideBar.setNewLetter(letters);
        mBSSideBar.invalidate();
        mSortAdapter.updateData(mList);

        if (this.getIntent().hasExtra("requst_number")) { // 返回码
            requst_number = this.getIntent().getIntExtra("requst_number", 0);
        }

        if (this.getIntent().hasExtra("users_id")) {
            String usersId = this.getIntent().getStringExtra("users_id");
            mSb.append(usersId);
            setSelected();
        } else if (this.getIntent().hasExtra("all_id")) {
            selectAll(true);
            mAllCb.setChecked(true);
            mIsAll = true;
            mAllCb.setButtonDrawable(R.drawable.common_ic_selected);
        } else {
            // selectAll(false);
        }
    }

    public void bindListers() {
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

    private void sortLetters() {
        for (int i = 0; i < mList.size(); i++) {
            DepartmentAndEmployeeVO personnelVO = mList.get(i);
            // 汉字转换成拼音List<BusinessVisit> visitList
            String pinyin = mCharacterParser.getSelling(personnelVO.getFullname());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (!mlettersList.contains(sortString.toUpperCase()) && sortString.matches("[A-Z]")) {
                mlettersList.add(sortString.toUpperCase());
            }
        }
        mlettersList.add("#");
    }

    class SortComparatorDepVo implements Comparator<DepartmentAndEmployeeVO> {

        public int compare(DepartmentAndEmployeeVO o1, DepartmentAndEmployeeVO o2) {
            if (o1.getSortLetters().equals("@")
                    || o2.getSortLetters().equals("#")) {
                return -1;
            } else if (o1.getSortLetters().equals("#")
                    || o2.getSortLetters().equals("@")) {
                return 1;
            } else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.depart_layout:
                intent.setClass(this, NoticeObjectActivity.class);
                // intent.putExtra("notice_publish", NoticePublishActivity.class);
                intent.putExtra("oneList", (Serializable) mOneList);
                intent.putExtra("requst_number", BY_DEPARTMENTS);
                startActivityForResult(intent, BY_DEPARTMENTS);
                break;
            case R.id.post_layout:
                intent.setClass(this, NoticeObjectActivity.class);
                intent.putExtra("twoList", (Serializable) mTwoList);
                intent.putExtra("requst_number", BY_DEPARTMENTS);
                startActivityForResult(intent, BY_DEPARTMENTS);
                break;

            case R.id.txt_comm_head_right:
                if (mIsAll) {
                    intent.putExtra("all_id", "all");
                    setResult(requst_number, intent);
                } else {
                    getUsersId();
                    intent.putExtra("users_id", mSb.toString());
                    setResult(requst_number, intent);
                }
                finish();
                break;

            case R.id.all_company_layout:
                if (mAllCb.isChecked()) {
                    mAllCb.setChecked(false);
                    mAllCb.setButtonDrawable(R.drawable.common_ic_unselect);
                    selectAll(false);
                    mIsAll = false;
                } else {
                    mAllCb.setChecked(true);
                    mAllCb.setButtonDrawable(R.drawable.common_ic_selected);
                    selectAll(true);
                    mIsAll = true;
                }
                break;

            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case BY_DEPARTMENTS:
                if (data == null)
                    return;
                if (resultCode == BY_DEPARTMENTS) {

                    // 部门
                    List<EmployeeVO> onelist = (List<EmployeeVO>) data.getSerializableExtra("oneList");
                    if (onelist != null && onelist.size() > 0) {
                        mOneList.clear();
                        mOneList.addAll(onelist);
                        selectedByDepart();
                    }

                    // 岗位
                    List<NoticeObjectVO> twolist = (List<NoticeObjectVO>) data.getSerializableExtra("twoList");
                    if (twolist != null && twolist.size() > 0) {
                        mTwoList.clear();
                        mTwoList.addAll(twolist);
                        selectedByPost();
                    }
                }
                mSortAdapter.notifyDataSetChanged();
                break;
        }
    }

    public void selectedByDepart() {
        for (int i = 0; i < mOneList.size(); i++) {
            for (int j = 0; j < mSortAdapter.mList.size(); j++) {
                if (mOneList.get(i).getUserid().equals(mSortAdapter.mList.get(j).getUserid())) {
                    mSortAdapter.mList.get(j).setSelected(true);
                    continue;
                }
            }
        }
    }

    public void selectedByPost() {
        for (int i = 0; i < mTwoList.size(); i++) {
            for (int j = 0; j < mSortAdapter.mList.size(); j++) {
                if (mTwoList.get(i).getPositionsid().equals(mSortAdapter.mList.get(j).getPid())) {
                    mSortAdapter.mList.get(j).setSelected(true);
                    continue;
                }
            }
        }
    }

    public void getUsersId() {
        mSb.setLength(0);
        for (int i = 0; i < mSortAdapter.mList.size(); i++) {
            if (mSortAdapter.mList.get(i).isSelected()) {
                mSb.append(mSortAdapter.mList.get(i).getUserid());
                mSb.append(",");
            }

        }
    }

    public void setSelected() {
        String[] strId = mSb.toString().split(",");
        for (int i = 0; i < strId.length; i++) {
            for (int j = 0; j < mSortAdapter.mList.size(); j++) {
                if (strId[i].equals(mSortAdapter.mList.get(j).getUserid())) {
                    mSortAdapter.mList.get(j).setSelected(true);
                    continue;
                }
            }
        }
    }

    public void selectAll(boolean select) {
        for (int i = 0; i < mSortAdapter.mList.size(); i++) {

            if (select) {
                mSortAdapter.mList.get(i).setSelected(true);
            } else {
                mSortAdapter.mList.get(i).setSelected(false);
            }

            mSortAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (mIsAll)
            mIsAll = false;
        int position = (int) arg3;
        DepartmentAndEmployeeVO vo = mSortAdapter.mList.get(position);
        if (vo.isSelected()) {
            vo.setSelected(false);
        } else {
            vo.setSelected(true);
        }
        mSortAdapter.notifyDataSetChanged();
    }

}
