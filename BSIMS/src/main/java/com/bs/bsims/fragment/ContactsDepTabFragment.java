
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
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.ContactsDepTabAdapter;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.ContactDepTabVO;
import com.bs.bsims.model.SortComparatorDepVo;
import com.bs.bsims.utils.CharacterParser;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.bs.bsims.view.BSSideBar;
import com.bs.bsims.view.BSSideBar.OnTouchingLetterChangedListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class ContactsDepTabFragment extends BaseFragment implements UpdateCallback {
    private static final String TAG = "ContactsLetterTabFragment";
    private Activity mActivity;
    private TextView mMsgTv;
    private String mMsgName;
    private BSRefreshListView mListView;
    private BSSideBar mBSSideBar;
    private CharacterParser mCharacterParser;
    private List<ContactDepTabVO> mList;
    private TextView mDialog;
    // private SortAdapter mSortAdapter;
    private ContactsDepTabAdapter mSortAdapter;

    private BSIndexEditText mBSIndexEditText;
    // private UserInfoResultVO mResultInfoVO;
    private ContactDepTabVO mResultInfoVO;
    private SortComparatorDepVo mSortComparatorVO;
    private Boolean mAddPerson = false;
    private ContactDepTabVO contactDepResultVo_flash;
    private LinearLayout no_content_layout;
    private TextView no_content_layout_content;

    public ContactsDepTabFragment(ContactDepTabVO resultInfoVO) {
        this.mResultInfoVO = resultInfoVO;
    }

    public ContactsDepTabFragment(ContactDepTabVO resultInfoVO, boolean addPerson) {
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
        mListView = (BSRefreshListView) view.findViewById(R.id.lv_contacts_sort);
        mBSSideBar = (BSSideBar) view.findViewById(R.id.sidrbar);
        mDialog = (TextView) view.findViewById(R.id.dialog);
        no_content_layout = (LinearLayout) view.findViewById(R.id.no_content_layout);
        no_content_layout_content = (TextView) view.findViewById(R.id.no_content_layout_content);
        mBSSideBar.setTextView(mDialog);
        mBSIndexEditText = (BSIndexEditText) view.findViewById(R.id.filter_edit);
    }

    private void initData() {
        mCharacterParser = CharacterParser.getInstance();
        mList = new ArrayList<ContactDepTabVO>();
        mSortAdapter = new ContactsDepTabAdapter(mActivity);
        mListView.setAdapter(mSortAdapter);
        mSortComparatorVO = new SortComparatorDepVo();
        List<ContactDepTabVO> list = new ArrayList<ContactDepTabVO>();
        if (null != mResultInfoVO && null != mResultInfoVO.getList()) {
            list = mResultInfoVO.getList();
            sortData(list);
            Collections.sort(mList, mSortComparatorVO);
            mSortAdapter.updateData(mList);
        }
        else {
            makeNoContent();
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

        mListView.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                new ThreadUtil(mActivity, ContactsDepTabFragment.this).start();
            }
        });

    }

    private void sortData(List<ContactDepTabVO> list) {
        for (int i = 0; i < list.size(); i++) {
            ContactDepTabVO personnelVO = list.get(i);

            // 汉字转换成拼音List<BusinessVisit> visitList
            String pinyin = mCharacterParser.getSelling(personnelVO.getLname());
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
        List<ContactDepTabVO> filterDateList = new ArrayList<ContactDepTabVO>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mList;
        } else {
            filterDateList.clear();
            for (ContactDepTabVO personnelVO : mList) {
                String name = personnelVO.getCname();
                String uname = personnelVO.getLname();
                if (name.indexOf(filterStr.toString()) != -1 || mCharacterParser.getSelling(name).startsWith(filterStr.toString()) || uname.indexOf(filterStr.toString()) != -1 || mCharacterParser.getSelling(uname).startsWith(filterStr.toString())) {
                    filterDateList.add(personnelVO);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, mSortComparatorVO);
        mSortAdapter.updateData(filterDateList);
    }

    @Override
    public String getFragmentName() {
        return TAG;
    }

    /*
     * (non-Javadoc)
     * @see UpdateCallback#execute()
     */
    @Override
    public boolean execute() {
        // TODO Auto-generated method stub
        return getdata();
    }

    /*
     * (non-Javadoc)
     * @see UpdateCallback#executeSuccess()
     */
    @Override
    public void executeSuccess() {
        // TODO Auto-generated method stub
        mListView.onRefreshComplete();
        no_content_layout.setVisibility(View.GONE);
        no_content_layout_content.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        mBSSideBar.setVisibility(View.VISIBLE);
        mList = new ArrayList<ContactDepTabVO>();
        mSortAdapter = new ContactsDepTabAdapter(mActivity);
        mListView.setAdapter(mSortAdapter);
        mSortComparatorVO = new SortComparatorDepVo();
        List<ContactDepTabVO> list = new ArrayList<ContactDepTabVO>();
        list = contactDepResultVo_flash.getList();
        sortData(list);
        Collections.sort(mList, mSortComparatorVO);
        mSortAdapter.updateData(mList);
        if (!TextUtils.isEmpty(mBSIndexEditText.getText().toString().trim()))
            mBSIndexEditText.setText("");

    }

    /*
     * (non-Javadoc)
     * @see UpdateCallback#executeFailure()
     */
    @Override
    public void executeFailure() {
        // TODO Auto-generated method stub
        mListView.onRefreshComplete();
        if (null != mSortAdapter.mList && mSortAdapter.mList.size() > 0) {
            return;
        }
        else {
            makeNoContent();
        }

    }

    public void makeNoContent() {
        no_content_layout.setVisibility(View.VISIBLE);
        no_content_layout_content.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        mBSSideBar.setVisibility(View.GONE);
    }

    public boolean getdata() {
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<String, String>();
        try {
            String url1 = UrlUtil.getUrlByMap1(Constant.CRM_CONTACTS_FRAGMENT,
                    null);
            String jsonUrlStr1 = HttpClientUtil.get(url1, Constant.ENCODING).trim();
            // 瀹㈡埛閫氳褰�
            contactDepResultVo_flash = gson.fromJson(jsonUrlStr1, ContactDepTabVO.class);
            if (contactDepResultVo_flash.getCode().equals("200")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case 1:
                if (resultCode == 1) {
                    mListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(mActivity, ContactsDepTabFragment.this).start();
                }
                break;
            case 101:
                if (resultCode == 101) {
                    mListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(mActivity, ContactsDepTabFragment.this).start();
                }
                break;

            default:
                break;
        }

    }

}
