
package com.bs.bsims.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmContactsPhoneAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.utils.CharacterParser;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.ContactUtils.ContactInfo;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSSideBar;
import com.bs.bsims.view.BSSideBar.OnTouchingLetterChangedListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CrmContactsPhoneListActivity extends BaseActivity {
    private BSRefreshListView mListView;
    private BSSideBar mBSSideBar;
    private TextView mDialog;
    private BSIndexEditText mBSIndexEditText;
    private TextView selectNumber;
    private CrmContactsPhoneAdapter mSortAdapter;
    private List<ContactInfo> mList;// 从本机通讯录得到的联系人（一旦获得，就不会变化）
    private List<ContactInfo> contactList;// 保存选中的联系人
    private CharacterParser mCharacterParser;
    private CrmContactPhoneComparatorVO mSortComparatorVO;

    // 记录列表数据，根据搜索列表数据会变化;用于点击列表时，数据对应（）；
    private List<ContactInfo> currentList = new ArrayList<ContactInfo>();

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.crm_contact_phone_view, null);
        mContentLayout.addView(layout);
        mList = (List<ContactInfo>) getIntent().getSerializableExtra("contactInfo");
        updataList();
        currentList = mList;
        mCharacterParser = CharacterParser.getInstance();
        mSortComparatorVO = new CrmContactPhoneComparatorVO();
        contactList = new ArrayList<ContactInfo>();
    }

    // 因为后台手机号为必填字段，就算Phone为空，tel不为空，数据也提交不上去，所以过滤掉phone为空的数据;
    public void updataList() {
        List<ContactInfo> testList = new ArrayList<ContactInfo>();
        for (int i = 0; i < mList.size(); i++) {
            ContactInfo info = mList.get(i);
            if (info.getPhoneNumList().size() != 0) {
                testList.add(info);
            }
        }
        mList.clear();
        mList = testList;
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        mOkTv.setText("确定");
        mTitleTv.setText("选中人员");
        mListView = (BSRefreshListView) findViewById(R.id.contacts_list);
        mBSSideBar = (BSSideBar) findViewById(R.id.sidrbar);
        mDialog = (TextView) findViewById(R.id.dialog);
        mBSIndexEditText = (BSIndexEditText) findViewById(R.id.filter_edit);
        mBSSideBar.setTextView(mDialog);
        selectNumber = (TextView) findViewById(R.id.select_number);
        sortData();
        Collections.sort(mList, mSortComparatorVO);// 给list列表排序
        mSortAdapter = new CrmContactsPhoneAdapter(CrmContactsPhoneListActivity.this, mList);
        mSortAdapter.notifyDataSetChanged();
        mListView.setAdapter(mSortAdapter);
    }

    // list每个元素增加字段sortLetters,并把list值传给mList
    private void sortData() {
        for (int i = 0; i < mList.size(); i++) {
            ContactInfo personnelVO = mList.get(i);

            // 汉字转换成拼音List<BusinessVisit> visitList
            String pinyin = mCharacterParser.getSelling(personnelVO.getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                personnelVO.setSortLetters(sortString.toUpperCase());
            } else {
                personnelVO.setSortLetters("#");
            }
            // 设置这个字段作用是标记通讯录中联系人是否被选中
            personnelVO.setIsSelect(false);
        }
    }

    @Override
    public void bindViewsListener() {

        // 当点击最右侧的字母【A--Z】时，跳转到此字母开头这行，即响应事件
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

        // 搜索监听
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

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ContactInfo info = currentList.get(arg2 - 1);
                if (info.getPhoneNumList().size() == 0) {
                    CustomToast.showShortToast(CrmContactsPhoneListActivity.this, "不是手机号或者号码格式不正确，不能选择！");
                } else {
                    if (info.getIsSelect()) {
                        info.setIsSelect(false);
                    } else {
                        info.setIsSelect(true);
                    }
                    mSortAdapter.notifyDataSetChanged();// 点击后及时改变状态
                    getCurrentSelectContact();
                    if (contactList.size() == 0) {
                        selectNumber.setText("未选中联系人");
                    } else {
                        selectNumber.setText("已选中" + contactList.size() + "个联系人");
                    }
                }
            }
        });

        mOkTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 选中有联系人才提交
                if (contactList.size() != 0) {
                    getCurrentSelectContact();
                    submitData();
                }
            }

        });

    }

    // 保存当前选中的联系人
    public void getCurrentSelectContact() {
        if (contactList.size() != 0) {
            contactList.clear();
        }
        for (int i = 0; i < mList.size(); i++) {
            ContactInfo info = mList.get(i);
            if (info.getIsSelect()) {
                contactList.add(info);
            }
        }
    }

    // 批量提交要添加的联系人数据
    public void submitData() {
        RequestParams params = new RequestParams();
        params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
        params.put("userid", BSApplication.getInstance().getUserId());
        int size = contactList.size();
        String[] names = new String[size];
        String[] phones = new String[size];
        String[] tels = new String[size];
        for (int i = 0; i < contactList.size(); i++) {
            ContactInfo info = contactList.get(i);
            names[i] = info.getName();
            if (info.getPhoneNumList().size() != 0) {
                phones[i] = getNewData(info.getPhoneNumList());
            } else {
                phones[i] = "";
            }

            if (info.getMobileTelList().size() != 0) {
                tels[i] = getNewData(info.getMobileTelList());
            } else {
                tels[i] = "";
            }

        }
        params.put("name", names);
        params.put("phone", phones);
        params.put("tel", tels);

        String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_CONTACT_PHONE_LIST;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_CODE.equals(code)) {
                        contactList.clear();// 清空数据，避免多次提交
                        setResult(101, new Intent());
                        CrmContactsPhoneListActivity.this.finish();

                    }
                    CustomToast.showLongToast(CrmContactsPhoneListActivity.this, str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public String getNewData(ArrayList<String> strList) {
        StringBuffer str = new StringBuffer();
        // 得到手机号

        for (int n = 0; n < strList.size(); n++) {
            // 当号码有多个时，拼接起来
            if (strList.size() >= 2) {
                if (n == strList.size() - 1) {
                    str.append(strList.get(n));
                } else {
                    str.append(strList.get(n) + ",");
                }

            } else {
                // 当只有一个号码时
                str.append(strList.get(n));
            }
        }

        return str.toString();
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * 
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<ContactInfo> filterDateList = new ArrayList<ContactInfo>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mList;
        } else {
            filterDateList.clear();
            for (ContactInfo personnelVO : mList) {
                String name = personnelVO.getName();
                if (name.indexOf(filterStr.toString()) != -1 || mCharacterParser.getSelling(name).startsWith(filterStr.toString())) {
                    // 用于保存含有输入字段的列表数据
                    filterDateList.add(personnelVO);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, mSortComparatorVO);
        mSortAdapter.updateListView(filterDateList);
        currentList = filterDateList;
    }

    public class CrmContactPhoneComparatorVO implements Comparator<ContactInfo> {

        public int compare(ContactInfo o1, ContactInfo o2) {
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

}
