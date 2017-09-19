
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.CollectionContactsActivity;
import com.bs.bsims.adapter.SortAdapter;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.model.SortComparatorVO;
import com.bs.bsims.utils.CharacterParser;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSSideBar;
import com.bs.bsims.view.BSSideBar.OnTouchingLetterChangedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressLint("ValidFragment")
public class ContactsLetterTabFragment extends BaseFragment {
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
    private Boolean mAddPerson = false;
    private String crmState = "0";// 专门给Crm用的
    private String likeIsGone = "0";// 我的收藏是否显示，默认不显示
    private TextView mContactsLike;// 通讯录收藏

    public String getLikeIsGone() {
        return likeIsGone;
    }

    public void setLikeIsGone(String likeIsGone) {
        this.likeIsGone = likeIsGone;
    }

    public String getCrmState() {
        return crmState;
    }

    public void setCrmState(String crmState) {
        this.crmState = crmState;
    }

    public ContactsLetterTabFragment(ResultVO resultInfoVO) {
        this.mResultInfoVO = resultInfoVO;
    }

    public ContactsLetterTabFragment(ResultVO resultInfoVO, boolean addPerson) {
        this.mResultInfoVO = resultInfoVO;
        this.mAddPerson = addPerson;
    }

    public void setMsgName(String msgName) {
        this.mMsgName = msgName;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_letter_tab, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        bindListers();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initViews(View view) {
        mListView = (ListView) view.findViewById(R.id.lv_contacts_sort);
        mBSSideBar = (BSSideBar) view.findViewById(R.id.sidrbar);
        mDialog = (TextView) view.findViewById(R.id.dialog);
        mBSSideBar.setTextView(mDialog);
        mBSIndexEditText = (BSIndexEditText) view.findViewById(R.id.filter_edit);
        mContactsLike = (TextView) view.findViewById(R.id.contacts_like);
    }

    private void initData() {

        mCharacterParser = CharacterParser.getInstance();
        mList = new ArrayList<DepartmentAndEmployeeVO>();
        if (mAddPerson) {
            // this.getCrmState() 代表能否选本人 1能选 其它不能选
            mSortAdapter = new SortAdapter(mActivity, mList, true, crmState);
        } else {
            mSortAdapter = new SortAdapter(mActivity, mList);
        }
        mListView.setAdapter(mSortAdapter);

        mSortComparatorVO = new SortComparatorVO();
        List<DepartmentAndEmployeeVO> list = new ArrayList<DepartmentAndEmployeeVO>();
        if (crmState.equals("1")) {
            list = mResultInfoVO.getArray();
        }
        else {
            list = mResultInfoVO.getUsers();
        }
        if (likeIsGone.equals("1"))
            mContactsLike.setVisibility(View.VISIBLE);
        else
            mContactsLike.setVisibility(View.GONE);
        sortData(list);
        Collections.sort(mList, mSortComparatorVO);
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
        mContactsLike.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(mActivity, CollectionContactsActivity.class));
                
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
    public String getFragmentName() {
        return TAG;
    }

}
