
package com.bs.bsims.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmContactAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.ContactDepTabResultVO;
import com.bs.bsims.model.CrmContactComparatorVO;
import com.bs.bsims.utils.CharacterParser;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.ContactUtils;
import com.bs.bsims.utils.ContactUtils.ContactInfo;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSSideBar;
import com.bs.bsims.view.BSSideBar.OnTouchingLetterChangedListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CrmContactListActivity extends BaseActivity implements OnClickListener {

    private BSRefreshListView mListView;
    private BSSideBar mBSSideBar;
    private TextView mDialog;
    private TextView selectNumber;
    private BSIndexEditText mBSIndexEditText;
    private ContactDepTabResultVO mCrmOptionsVO;
    private CharacterParser mCharacterParser;
    private List<ContactDepTabResultVO> mList;
    private CrmContactAdapter mSortAdapter;
    private CrmContactComparatorVO mSortComparatorVO;
    private PopupWindow mPopView;
    private int popwidth;// 手机屏幕宽度的一半
    private int textWidth;// 标题文字的宽度
    private String mType;// 0-我的联系人 1-我下属的联系人 2-共享给我的联系人
    private boolean flag = true;
    private String[] array = {
            "手动添加", "通讯录导入"
    };
    // currentList是根据搜索不断变化的，保存不同列表的数据，避免进入联系人详情的时候出现问题
    private List<ContactDepTabResultVO> currentList = new ArrayList<ContactDepTabResultVO>();
    public static final String CONTACT_EDIT = "contact_edit";// 联系人编辑之后回来刷新
    private TextView mNoReadTv;
    private String mUnread;
    private String datetime, uid,mMine;// 判断新增联系人是上月还是下月,uid是当前查询的哪个人

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.crm_contact_phone_view, null);
        mContentLayout.addView(layout);

    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            // map.put("userid", "22");
            if (flag) {
                mType = "0";// 首次进入展示全部联系人列表
            } else {
                map.put("type", mType);

            }
            // map.put("unread", "1");
            map.put("unread", mUnread);
            /*
             * --- 新增联系人需要的参数---*
             */
            if (datetime != null && !datetime.equals("")) {
                map.put("uid", uid);
                map.put("datetime", datetime);//月份
                map.put("mine", mMine);
            }
            /*
             * ---↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑--*
             */
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_CONTACT_LIST, map);
            mCrmOptionsVO = gson.fromJson(jsonStr, ContactDepTabResultVO.class);

            if (Constant.RESULT_CODE.equals(mCrmOptionsVO.getCode())) {
                flag = false;
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

    @Override
    public void executeFailure() {
        if (mCrmOptionsVO == null) {
            super.showNoNetView();
        } else {
            super.showNoContentView();
        }

    }

    @Override
    public void updateUi() {
        // 由于标题内容会变化，相应弹出框的宽也要随之变化，在此引用，便于刷新
        getWidthHeight();
        mCharacterParser = CharacterParser.getInstance();
        mSortComparatorVO = new CrmContactComparatorVO();
        List<ContactDepTabResultVO> list = mCrmOptionsVO.getList();
        sortData(list);
        Collections.sort(mList, mSortComparatorVO);// 给list列表排序
        mSortAdapter.updateListView(mList);// 刷新数据
        currentList = mList;

    }

    // list每个元素增加字段sortLetters,并把list值传给mList
    private void sortData(List<ContactDepTabResultVO> list) {
        mList.clear();// 清空数据，不然刷新后数据会加倍
        for (int i = 0; i < list.size(); i++) {
            ContactDepTabResultVO personnelVO = list.get(i);

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

    @Override
    public void initView() {

        // mTitleTv.setCompoundDrawablesWithIntrinsicBounds(null, null,
        // getResources().getDrawable(R.drawable.pop_down), null);
        // mTitleTv.setCompoundDrawablePadding(30);
        mPopView = new PopupWindow(this);
        mList = new ArrayList<ContactDepTabResultVO>();
        mListView = (BSRefreshListView) findViewById(R.id.contacts_list);
        mBSSideBar = (BSSideBar) findViewById(R.id.sidrbar);
        mDialog = (TextView) findViewById(R.id.dialog);
        mBSSideBar.setTextView(mDialog);
        mBSIndexEditText = (BSIndexEditText) findViewById(R.id.filter_edit);
        mSortAdapter = new CrmContactAdapter(CrmContactListActivity.this, mList);
        mListView.setAdapter(mSortAdapter);
        selectNumber = (TextView) findViewById(R.id.select_number);
        selectNumber.setVisibility(View.GONE);
        mNoReadTv = (TextView) findViewById(R.id.no_read_tv);
        mNoReadTv.setVisibility(View.GONE);
        getWidthHeight();
        registBroadcast();
        mUnread = this.getIntent().getStringExtra("unread");
        // 首页进来隐藏收索条件
        if (this.getIntent().getStringExtra("msg") != null) {
            mBSIndexEditText.setVisibility(View.GONE);
            // mSearchLayout.setVisibility(View.GONE);
        }
        if (getIntent().getStringExtra("datetime") != null) {
            mTitleTv.setText("新增联系人");
            mMine = null == getIntent().getStringExtra("mine") ? "" : getIntent().getStringExtra("mine");
            datetime = getIntent().getStringExtra("datetime");
            if (getIntent().getStringExtra("userid") != null) {
                uid = getIntent().getStringExtra("userid");
            }
            else {
                uid = BSApplication.getInstance().getUserId();
            }
        }
        else {
            mTitleTv.setText("联系人提醒");
        }

    }

    // 设置弹出框的宽高
    public void getWidthHeight() {
        // 得到标题文字的宽度
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mTitleTv.measure(w, h);
        int width = mTitleTv.getMeasuredWidth();
        textWidth = width;

        // 得到屏幕宽度的一半
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width1 = metric.widthPixels;
        popwidth = width1 / 2;
    }

    @Override
    public void bindViewsListener() {
        // mTitleTv.setOnClickListener(this);
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

        // 列表点击事件，跳转到联系人详情
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                currentList.get((int) arg3).setIsread("1");
                mSortAdapter.notifyDataSetChanged();
                Intent in = new Intent(CrmContactListActivity.this, CrmContactDetailActivity.class);
                in.putExtra("lid", currentList.get((int) arg3).getLid());
                startActivityForResult(in, 520);

            }
        });

        mOkTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                CommonUtils.initPopViewBg(CrmContactListActivity.this, array, mOkTv, mCallback, CommonUtils.getScreenWidth(CrmContactListActivity.this) / 3);

            }
        });
        mNoReadTv.setOnClickListener(this);
    }

    // 添加回调函数
    ResultCallback mCallback = new ResultCallback() {

        @Override
        public void callback(String str, int position) {
            if (position == 0) {
                Intent intent = new Intent();
                intent.setClass(CrmContactListActivity.this, CrmClientAddContactsActivity.class);
                // 0是联系人列表添加跳转 1详情编辑跳转 2是客户列表添加跳转
                intent.putExtra("type", "0");
                startActivityForResult(intent, 1);
            } else if (position == 1) {
                // 将手机联系人的姓名、电话号码保存到ArrayList<ContactInfo>中
                if (ContactUtils.getContactsList(CrmContactListActivity.this) == null) {
                    CustomToast.showShortToast(CrmContactListActivity.this, "通讯录没有联系人");
                } else {
                    ArrayList<ContactInfo> contacts = ContactUtils.getContactsList(CrmContactListActivity.this);
                    Intent in = new Intent(CrmContactListActivity.this, CrmContactsPhoneListActivity.class);
                    in.putExtra("contactInfo", contacts);
                    startActivityForResult(in, 101);
                }

            }
        }
    };

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent intent) {
        super.onActivityResult(arg0, arg1, intent);
        if (intent != null) {
            // mListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
            new ThreadUtil(CrmContactListActivity.this, CrmContactListActivity.this).start();
            mSortAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * 
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<ContactDepTabResultVO> filterDateList = new ArrayList<ContactDepTabResultVO>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mList;
        } else {
            filterDateList.clear();
            for (ContactDepTabResultVO personnelVO : mList) {
                String name = personnelVO.getLname();
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

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.txt_comm_head_activityName:
                if (!mPopView.isShowing()) {
                    showPopView(txt_comm_head_activityNamefather);
                }
                break;

            case R.id.no_read_tv:
                Intent noReadIntent = new Intent();
                noReadIntent.putExtra("type", 16);
                noReadIntent.setClass(this, AllNoReadActivity.class);
                startActivity(noReadIntent);
                break;

            default:
                break;
        }

    }

    // 点击联系人后弹出框的封装
    public void showPopView(View anchor) {

        int month = Integer.parseInt(DateUtils.getCurrentDate().split("-")[1]);

        List<HashMap<String, Object>> list = initPopData();
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.dropdown_approval_month_item,
                new String[] {
                        "contacts"
                }, new int[] {
                        R.id.textview
                });
        ListView listView = new ListView(this);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tv = (TextView) view.findViewById(R.id.textview);
                // if (position == 0) {
                // mTitleTv.setText("全部");
                // } else {
                // mTitleTv.setText(tv.getText().toString());
                // }
                mTitleTv.setText(tv.getText().toString());
                mType = Integer.toString(position);
                new ThreadUtil(CrmContactListActivity.this, CrmContactListActivity.this).start();
                mPopView.dismiss();
            }
        });
        // 改变标题下面弹出框的宽度、高度
        getWidthHeight();
        listView.setAdapter(adapter);
        listView.setDivider(null);
        mPopView.setWidth(popwidth);
        // mPopView.setWidth(LayoutParams.WRAP_CONTENT);
        CustomLog.e("wddd", popwidth + "");
        mPopView.setHeight(LayoutParams.WRAP_CONTENT);
        mPopView.setContentView(listView);
        mPopView.setFocusable(true);
        mPopView.setOutsideTouchable(true);
        mPopView.setBackgroundDrawable(this.getResources().getDrawable(R.color.white));
        mPopView.showAsDropDown(anchor, -(popwidth / 2 - textWidth / 2), 0);

    }

    public List<HashMap<String, Object>> initPopData() {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map0 = new HashMap<String, Object>();
        map0.put("contacts", "全部");
        list.add(map0);
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("contacts", "我的联系人");
        list.add(map1);
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("contacts", "我下属的联系人");
        list.add(map2);
        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("contacts", "共享给我的联系人");
        list.add(map3);
        return list;
    }

    public void registBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CONTACT_EDIT);
        this.registerReceiver(msgBroadcast, filter);
    }

    private void unRegistExitReceiver() {
        this.unregisterReceiver(msgBroadcast);
    }

    private BroadcastReceiver msgBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String company = null;
            String sex = null;
            if (CONTACT_EDIT.equals(intent.getAction())) {
                String lid = intent.getStringExtra("lid");
                String name = intent.getStringExtra("name");
                String post = intent.getStringExtra("post");
                String headpic = intent.getStringExtra("headpic");
                company = intent.getStringExtra("cname");
                sex = intent.getStringExtra("sex");
                for (int i = 0; i < mSortAdapter.list.size(); i++) {
                    if (lid.equals(mSortAdapter.list.get(i).getLid())) {
                        mSortAdapter.list.get(i).setLname(name);
                        mSortAdapter.list.get(i).setPost(post);
                        mSortAdapter.list.get(i).setLheadpic(headpic);
                        mSortAdapter.list.get(i).setSex(sex);
                        mSortAdapter.list.get(i).setCompany(company);
                        break;
                    }
                }
                mSortAdapter.notifyDataSetChanged();
            }
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unRegistExitReceiver();
    };

}
