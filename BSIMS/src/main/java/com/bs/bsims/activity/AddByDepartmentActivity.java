    
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.AddByDepartmentAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.EmployeeOnclickCallback;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.PDFOutlineElementVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.utils.CharacterParser;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.LogUtils;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WM 员工单选列表，item点击事件在PublicSingleEmployeeSortAdapter中
 */
public class AddByDepartmentActivity extends BaseActivity implements EmployeeOnclickCallback {

    /** 最外层数据 */
    private ArrayList<PDFOutlineElementVO> mPdfOutlinesCount = new ArrayList<PDFOutlineElementVO>();

    /** 内层数据 */
    private ArrayList<PDFOutlineElementVO> mPdfOutlines = new ArrayList<PDFOutlineElementVO>();

    /** 补充数据源 **/
    private ArrayList<PDFOutlineElementVO> mPdfOutlinesave = new ArrayList<PDFOutlineElementVO>();

    private AddByDepartmentAdapter mAddCcAdapter;
    private ListView listview;
    public Context context = AddByDepartmentActivity.this;

    public List<EmployeeVO> checkboxlist = new ArrayList<EmployeeVO>();

    Class clz = null;

    int falgeresult = 2;
    int falgeresult_s = 0;

    int requst_number = 0;

    private BSIndexEditText mIndexEditText;
    private Handler backHandler;

    private CharacterParser characterParser;

    private ResultVO mResultVO;

    private CheckBox checkBox_up_check_selectall;

    private Handler mAdapterHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mAddCcAdapter.notifyDataSetChanged();
                CustomDialog.closeProgressDialog();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.add_by_department);
        // initView();
        // getData();
        // getDataFromServer();
        // getIntentDatas();
        // initSearch();

    }

    protected void initSearch() {
        Thread searchThread = new Thread() {

            @Override
            public void run() {
                Looper.prepare();

                backHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        CustomDialog.showProgressDialog(context, "正在搜索中..");
                        String key = (String) msg.obj;
                        filterData(key);
                    }
                };

                Looper.loop();
            }
        };

        searchThread.start();

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * 
     * @param filterStr
     */
    private void filterData(String filterStr) {
        mPdfOutlinesCount.clear();
        if (TextUtils.isEmpty(filterStr)) {
            for (PDFOutlineElementVO employee : mPdfOutlines) {
                employee.setSearch(false);
                employee.setExpanded(false);
                int level = employee.getLevel();
                if (level == 1) {
                    mPdfOutlinesCount.add(employee);
                }
            }
        } else {

            List<PDFOutlineElementVO> list_2 = new ArrayList<PDFOutlineElementVO>();
            List<PDFOutlineElementVO> list_3 = new ArrayList<PDFOutlineElementVO>();

            for (PDFOutlineElementVO employee : mPdfOutlines) {
                if ("user".equals(employee.getId())) {

                    String name = employee.getDepartmentandwmployee().getFullname();

                    if (name.length() > 0 && filterStr.length() > 0 && name.length() >= filterStr.length()) {
                        // String name_txt = name.substring(0, filterStr.length());
                        if (name.contains(filterStr)) {
                            employee.setSearch(true);
                            list_2.add(employee);
                        }
                    }

                    // String pinyin = characterParser.getSelling(filterStr).toUpperCase();
                    // String nick =
                    // characterParser.getSelling(employee.getDepartmentandwmployee().getFullname()).toUpperCase();
                    //
                    // if (!CommonUtils.isCNMark(filterStr)) {
                    // if (pinyin.length() > 0 && nick.length() > 0 && nick.length() >=
                    // pinyin.length()) {
                    // String nickString = nick.substring(0, pinyin.length()).toUpperCase();
                    // if (pinyin.equals(nickString)) {
                    // employee.setSearch(true);
                    // list_3.add(employee);
                    //
                    // }
                    // }
                    // }
                }

            }
            // boolean isFirst = false;
            if (list_2.size() > 0) {
                mPdfOutlinesCount.addAll(list_2);
                // isFirst = true;
            }

            // if (!isFirst) {
            // if (list_3.size() > 0) {
            // mPdfOutlinesCount.addAll(list_3);
            // }
            // }
        }
        mAdapterHandler.sendEmptyMessage(1);
    }

    private void getIntentDatas() {
        Intent intent = getIntent();
        if (intent.hasExtra("employ_name")) { // 跳转回去的Class
            clz = (Class) getIntent().getSerializableExtra("employ_name");
        }

        if (intent.hasExtra("requst_number")) { // 返回码
            requst_number = intent.getIntExtra("requst_number", 0);
        }

        if (intent.hasExtra("checkboxlist")) { // 之前选中的抄送人的集合
            checkboxlist.clear();
            List<EmployeeVO> boxlist = (List<EmployeeVO>) intent.getSerializableExtra("checkboxlist");
            checkboxlist.addAll(boxlist);
        }

        ((TextView) findViewById(R.id.txt_comm_head_activityName)).setText("选择联系人");
        if (intent.hasExtra("title_name")) {
            String titleName = intent.getStringExtra("title_name");
            mTitleTv.setText(titleName);
        }

    }

    public void match() {
        for (int i = 0; i < mPdfOutlines.size(); i++) {
            for (int j = 0; j < checkboxlist.size(); j++) {

                if (mPdfOutlines.get(i).getDepartmentandwmployee().getUserid().equals(checkboxlist.get(j).getUserid())) {
                    mPdfOutlines.get(i).setSelect(true);
                    mPdfOutlines.get(i).setUser_level(checkboxlist.get(j).getUser_level());
                    LogUtils.i("back", "checkboxlist.get(j).getUser_level()=ccc===" + checkboxlist.get(j).getUser_level());
                    move(mPdfOutlines.get(i));
                    // 2016-8-22 必须知会人不能删除，只有必须知会人有isDefault字段
                    if (null != checkboxlist.get(j).getIsdefault()) {
                        mPdfOutlines.get(i).getDepartmentandwmployee().setIsdefault(checkboxlist.get(j).getIsdefault());
                    }
                }

            }
        }
    }

    protected void initHeadView() {
        // findViewById(R.id.relative_comm_head_back).setOnClickListener(headBackListener);
        ((TextView) findViewById(R.id.txt_comm_head_activityName)).setFocusableInTouchMode(true);
        ((TextView) findViewById(R.id.txt_comm_head_right)).setText("确认");
        mOkTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                ArrayList<EmployeeVO> checklist = new ArrayList<EmployeeVO>();

                for (int i = 0; i < mPdfOutlines.size(); i++) {

                    PDFOutlineElementVO pdf = mPdfOutlines.get(i);
                    if (pdf.isSelect() && "user".equals(pdf.getId())) {
                        EmployeeVO employee = new EmployeeVO();
                        employee.setUserid(pdf.getDepartmentandwmployee().getUserid());
                        employee.setFullname(pdf.getDepartmentandwmployee().getFullname());
                        employee.setHeadpic(pdf.getDepartmentandwmployee().getHeadpic());
                        employee.setTel(pdf.getDepartmentandwmployee().getTel());
                        employee.setUser_level(pdf.getUser_level());
                        if (pdf.getDepartmentandwmployee().getIsdefault() != null) {
                            employee.setIsdefault(pdf.getDepartmentandwmployee().getIsdefault());
                        }
                        checklist.add(employee);
                    }
                }

                Intent intent = new Intent();
                intent.putExtra("selectsure", falgeresult_s);
                intent.setClass(context, clz);
                intent.putExtra("checkboxlist", (Serializable) checklist);
                setResult(requst_number, intent);
                finish();

            }
        });
        mHeadBackImag.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // ArrayList<EmployeeVO> checklist = new ArrayList<EmployeeVO>();
                //
                // for (int i = 0; i < mPdfOutlines.size(); i++) {
                //
                // PDFOutlineElementVO pdf = mPdfOutlines.get(i);
                // if (pdf.isSelect() && "user".equals(pdf.getId())) {
                // EmployeeVO employee = new EmployeeVO();
                // employee.setUserid(pdf.getDepartmentandwmployee().getUserid());
                // employee.setFullname(pdf.getDepartmentandwmployee().getFullname());
                // employee.setHeadpic(pdf.getDepartmentandwmployee().getHeadpic());
                // employee.setTel(pdf.getDepartmentandwmployee().getTel());
                // employee.setUser_level(pdf.getUser_level());
                // checklist.add(employee);
                // }
                // }
                //
                // Intent intent = new Intent();
                // intent.setClass(context, clz);
                // intent.putExtra("checkboxlist", (Serializable) checklist);
                // intent.putExtra("selectsure", falgeresult);
                // setResult(requst_number, intent);
                // finish();
                // mPdfOutlines.clear();
                // checkboxlist.clear();
                // ArrayList<EmployeeVO> checklist = new ArrayList<EmployeeVO>();
                // Intent intent = new Intent();
                // intent.setClass(context, clz);
                // intent.putExtra("checkboxlist", (Serializable) checklist);
                // intent.putExtra("selectsure", falgeresult_s);
                // setResult(requst_number, intent);
                finish();

            }
        });
    }

    @Override
    public void initView() {

        getIntentDatas();
        initHeadView();
        characterParser = CharacterParser.getInstance();

        mIndexEditText = (BSIndexEditText) findViewById(R.id.edit_many_search);
        // 根据输入框输入值的改变来过滤搜索
        mIndexEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                // filterData(s.toString());
                Message msg = backHandler.obtainMessage();
                msg.obj = s.toString();
                backHandler.sendMessage(msg);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        listview = (ListView) findViewById(R.id.listView_single_newList);

        mAddCcAdapter = new AddByDepartmentAdapter(this, this, R.layout.item_contacts_department_tree_view, mPdfOutlinesCount, true);
        checkBox_up_check_selectall = (CheckBox) findViewById(R.id.checkBox_up_check_selectall);
        listview.setDivider(null);
        listview.setAdapter(mAddCcAdapter);

    }

    private void getDataFromServer() {
        List<DepartmentAndEmployeeVO> departmentList = mResultVO.getDepartments();
        if (departmentList != null && departmentList.size() > 0) {
            for (int i = 0; i < departmentList.size(); i++) {
                DepartmentAndEmployeeVO department = departmentList.get(i);
                int level = Integer.parseInt(department.getLevel());
                if ("0".equals(department.getBelong())) {
                    // 获取第一级的部门

                    mPdfOutlinesCount.add(new PDFOutlineElementVO(department.getDepartmentid(), department, false, true, department.getBelong(), level, false, false));
                    mPdfOutlines.add(new PDFOutlineElementVO(department.getDepartmentid(), department, false, true, department.getBelong(), level, false, false));
                    if (falgeresult == 2)
                        mPdfOutlinesave.add(new PDFOutlineElementVO(department.getDepartmentid(), department, false, true, department.getBelong(), level, false, true));

                } else {
                    mPdfOutlines.add(new PDFOutlineElementVO(department.getDepartmentid(), department, true, true, department.getBelong(), level, false, false));
                    if (falgeresult == 2)
                        mPdfOutlinesave.add(new PDFOutlineElementVO(department.getDepartmentid(), department, true, true, department.getBelong(), level, false, true));
                }
            }
        }

        // 用户
        List<DepartmentAndEmployeeVO> employeeList = mResultVO.getUsers();
        if (employeeList != null && employeeList.size() > 0) {
            for (int j = 0; j < employeeList.size(); j++) {
                DepartmentAndEmployeeVO employee = employeeList.get(j);
                mPdfOutlines.add(new PDFOutlineElementVO("user", employee, false, false, employee.getDid(), 4, false, false));
                if (falgeresult == 2)
                    mPdfOutlinesave.add(new PDFOutlineElementVO("user", employee, false, false, employee.getDid(), 4, false, true));

            }
        }
        match();

    }

    private void dateCancel(PDFOutlineElementVO element, int level) {
        switch (level) {
            case 1: {
                for (int i = 0; i < mPdfOutlines.size(); i++) {
                    PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlines.get(i);

                    if (pdfOutlineElement1.getParent().equals(element.getId())) {
                        pdfOutlineElement1.setSelect(false);

                        for (int j = 0; j < mPdfOutlines.size(); j++) {
                            PDFOutlineElementVO pdfOutlineElement2 = mPdfOutlines.get(j);
                            if (pdfOutlineElement2.getParent().equals(pdfOutlineElement1.getId())) {
                                pdfOutlineElement2.setSelect(false);

                                for (int k = 0; k < mPdfOutlines.size(); k++) {
                                    PDFOutlineElementVO pdfOutlineElement3 = mPdfOutlines.get(k);
                                    if (pdfOutlineElement3.getParent().equals(pdfOutlineElement2.getId())) {
                                        pdfOutlineElement3.setSelect(false);
                                    }
                                }

                            }
                        }

                    }

                }
                for (int i = 0; i < mPdfOutlinesCount.size(); i++) {
                    PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlinesCount.get(i);

                    if (pdfOutlineElement1.getParent().equals(element.getId())) {
                        pdfOutlineElement1.setSelect(false);

                        for (int j = 0; j < mPdfOutlinesCount.size(); j++) {
                            PDFOutlineElementVO pdfOutlineElement2 = mPdfOutlinesCount.get(j);
                            if (pdfOutlineElement2.getParent().equals(pdfOutlineElement1.getId())) {
                                pdfOutlineElement2.setSelect(false);

                                for (int k = 0; k < mPdfOutlinesCount.size(); k++) {
                                    PDFOutlineElementVO pdfOutlineElement3 = mPdfOutlinesCount.get(k);
                                    if (pdfOutlineElement3.getParent().equals(pdfOutlineElement2.getId())) {
                                        pdfOutlineElement3.setSelect(false);
                                    }
                                }

                            }
                        }

                    }

                }
                break;
            }
            case 2: {
                if (!"user".equals(element.getId())) {
                    for (int i = 0; i < mPdfOutlines.size(); i++) {
                        PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlines.get(i);

                        if (pdfOutlineElement1.getParent().equals(element.getId())) {
                            pdfOutlineElement1.setSelect(false);

                            for (int j = 0; j < mPdfOutlines.size(); j++) {
                                PDFOutlineElementVO pdfOutlineElement2 = mPdfOutlines.get(j);
                                if (pdfOutlineElement2.getParent().equals(pdfOutlineElement1.getId())) {
                                    pdfOutlineElement2.setSelect(false);

                                }
                            }

                        }

                    }
                }

                if (!"user".equals(element.getId())) {
                    for (int i = 0; i < mPdfOutlinesCount.size(); i++) {
                        PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlinesCount.get(i);

                        if (pdfOutlineElement1.getParent().equals(element.getId())) {
                            pdfOutlineElement1.setSelect(false);

                            for (int j = 0; j < mPdfOutlinesCount.size(); j++) {
                                PDFOutlineElementVO pdfOutlineElement2 = mPdfOutlinesCount.get(j);
                                if (pdfOutlineElement2.getParent().equals(pdfOutlineElement1.getId())) {
                                    pdfOutlineElement2.setSelect(false);

                                }
                            }

                        }

                    }
                }

                boolean ishas = false;
                for (int i = 0; i < mPdfOutlines.size(); i++) {
                    PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlines.get(i);

                    if (pdfOutlineElement1.getParent().equals(element.getParent())) {
                        // true_lines.add(pdfOutlineElement1);
                        if (pdfOutlineElement1.isSelect()) {
                            ishas = true;
                        }

                    }
                }
                if (!ishas) {
                    for (int i = 0; i < mPdfOutlines.size(); i++) {
                        PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlines.get(i);

                        if (pdfOutlineElement1.getId().equals(element.getParent())) {
                            pdfOutlineElement1.setSelect(false);

                        }
                    }
                }

                boolean isCounthas = false;
                for (int i = 0; i < mPdfOutlinesCount.size(); i++) {
                    PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlinesCount.get(i);

                    if (pdfOutlineElement1.getParent().equals(element.getParent())) {
                        if (pdfOutlineElement1.isSelect()) {
                            isCounthas = true;
                        }

                    }
                }
                if (!isCounthas) {
                    for (int i = 0; i < mPdfOutlinesCount.size(); i++) {
                        PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlinesCount.get(i);

                        if (pdfOutlineElement1.getId().equals(element.getParent())) {
                            // true_lines.add(pdfOutlineElement1);
                            pdfOutlineElement1.setSelect(false);
                        }
                    }
                }

                break;
            }
            case 3: {
                if (!"user".equals(element.getId())) {
                    for (int i = 0; i < mPdfOutlines.size(); i++) {
                        PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlines.get(i);

                        if (pdfOutlineElement1.getParent().equals(element.getId())) {
                            pdfOutlineElement1.setSelect(false);

                        }

                    }
                }

                if (!"user".equals(element.getId())) {
                    for (int i = 0; i < mPdfOutlinesCount.size(); i++) {
                        PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlinesCount.get(i);

                        if (pdfOutlineElement1.getParent().equals(element.getId())) {
                            pdfOutlineElement1.setSelect(false);

                        }

                    }
                }

                boolean isfirst = false;
                for (int i = 0; i < mPdfOutlines.size(); i++) {
                    PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlines.get(i);

                    if (pdfOutlineElement1.getParent().equals(element.getParent())) {
                        if (pdfOutlineElement1.isSelect()) {
                            isfirst = true;
                        }

                    }
                }
                if (!isfirst) {
                    for (int i = 0; i < mPdfOutlines.size(); i++) {
                        PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlines.get(i);

                        if (pdfOutlineElement1.getId().equals(element.getParent())) {
                            pdfOutlineElement1.setSelect(false);

                            boolean issecond = false;
                            for (int j = 0; j < mPdfOutlines.size(); j++) {
                                PDFOutlineElementVO pdfOutlineElement_second = mPdfOutlines.get(j);
                                if (pdfOutlineElement_second.getParent().equals(pdfOutlineElement1.getParent())) {
                                    if (pdfOutlineElement_second.isSelect()) {
                                        issecond = true;
                                    }

                                }
                            }

                            if (!issecond) {
                                for (int j = 0; j < mPdfOutlines.size(); j++) {
                                    if (mPdfOutlines.get(j).getId().equals(pdfOutlineElement1.getParent())) {
                                        mPdfOutlines.get(j).setSelect(false);
                                    }
                                }

                            }
                        }
                    }
                }

                boolean iscountfirst = false;
                for (int i = 0; i < mPdfOutlinesCount.size(); i++) {
                    PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlinesCount.get(i);

                    if (pdfOutlineElement1.getParent().equals(element.getParent())) {
                        if (pdfOutlineElement1.isSelect()) {
                            iscountfirst = true;
                        }

                    }
                }
                if (!iscountfirst) {
                    for (int i = 0; i < mPdfOutlinesCount.size(); i++) {
                        PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlinesCount.get(i);

                        if (pdfOutlineElement1.getId().equals(element.getParent())) {
                            pdfOutlineElement1.setSelect(false);

                            boolean issecond = false;
                            for (int j = 0; j < mPdfOutlinesCount.size(); j++) {
                                PDFOutlineElementVO pdfOutlineElement_second = mPdfOutlinesCount.get(j);
                                if (pdfOutlineElement_second.getParent().equals(pdfOutlineElement1.getParent())) {
                                    if (pdfOutlineElement_second.isSelect()) {
                                        issecond = true;
                                    }

                                }
                            }

                            if (!issecond) {
                                for (int j = 0; j < mPdfOutlinesCount.size(); j++) {
                                    if (mPdfOutlinesCount.get(j).getId().equals(pdfOutlineElement1.getParent())) {
                                        // true_lines.add(pdfOutlineElement1);
                                        mPdfOutlinesCount.get(j).setSelect(false);
                                    }
                                }

                            }
                        }
                    }
                }
                break;
            }

            case 4: {

                boolean isfirst = false;
                for (int i = 0; i < mPdfOutlines.size(); i++) {
                    PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlines.get(i);

                    if (pdfOutlineElement1.getParent().equals(element.getParent())) {
                        // true_lines.add(pdfOutlineElement1);
                        if (pdfOutlineElement1.isSelect()) {
                            isfirst = true;
                        }

                    }
                }
                if (!isfirst) {
                    for (int i = 0; i < mPdfOutlines.size(); i++) {
                        PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlines.get(i);
                        if (pdfOutlineElement1.getId().equals(element.getParent())) {
                            // true_lines.add(pdfOutlineElement1);
                            pdfOutlineElement1.setSelect(false);

                            boolean issecond = false;
                            for (int j = 0; j < mPdfOutlines.size(); j++) {
                                PDFOutlineElementVO pdfOutlineElement_second = mPdfOutlines.get(j);
                                if (pdfOutlineElement_second.getParent().equals(pdfOutlineElement1.getParent())) {
                                    // true_lines.add(pdfOutlineElement1);
                                    if (pdfOutlineElement_second.isSelect()) {
                                        issecond = true;
                                    }

                                }
                            }

                            if (!issecond) {
                                for (int j = 0; j < mPdfOutlines.size(); j++) {
                                    PDFOutlineElementVO pdfOutlineElement_third = mPdfOutlines.get(j);
                                    if (pdfOutlineElement_third.getId().equals(pdfOutlineElement1.getParent())) {
                                        pdfOutlineElement_third.setSelect(false);
                                    }

                                    boolean isthird = false;
                                    for (int k = 0; k < mPdfOutlines.size(); k++) {
                                        PDFOutlineElementVO pdfOutlineElement2 = mPdfOutlines.get(k);

                                        if (pdfOutlineElement2.getParent().equals(pdfOutlineElement_third.getParent())) {
                                            if (pdfOutlineElement2.isSelect()) {
                                                isthird = true;
                                            }

                                        }
                                    }
                                    if (!isthird) {
                                        for (int k = 0; k < mPdfOutlines.size(); k++) {
                                            PDFOutlineElementVO pdfOutlineElement2 = mPdfOutlines.get(i);

                                            if (pdfOutlineElement2.getId().equals(pdfOutlineElement_third.getParent())) {
                                                pdfOutlineElement2.setSelect(false);

                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                boolean iscountfirst = false;
                for (int w = 0; w < mPdfOutlinesCount.size(); w++) {
                    PDFOutlineElementVO pdfOutlineElement_1 = mPdfOutlinesCount.get(w);

                    if (pdfOutlineElement_1.getParent().equals(element.getParent())) {
                        if (pdfOutlineElement_1.isSelect()) {
                            iscountfirst = true;
                        }

                    }
                }

                if (!iscountfirst) {
                    for (int w = 0; w < mPdfOutlinesCount.size(); w++) {
                        PDFOutlineElementVO pdfOutlineElement_1 = mPdfOutlinesCount.get(w);

                        if (pdfOutlineElement_1.getId().equals(element.getParent())) {
                            pdfOutlineElement_1.setSelect(false);

                            boolean iscountsecond = false;
                            for (int j = 0; j < mPdfOutlinesCount.size(); j++) {
                                PDFOutlineElementVO pdfOutlineElement_second = mPdfOutlinesCount.get(j);
                                if (pdfOutlineElement_second.getParent().equals(pdfOutlineElement_1.getParent())) {
                                    if (pdfOutlineElement_second.isSelect()) {
                                        iscountsecond = true;
                                    }

                                }
                            }

                            if (!iscountsecond) {
                                for (int j = 0; j < mPdfOutlinesCount.size(); j++) {
                                    PDFOutlineElementVO pdfOutlineElement_third = mPdfOutlinesCount.get(j);
                                    if (pdfOutlineElement_third.getId().equals(pdfOutlineElement_1.getParent())) {
                                        pdfOutlineElement_third.setSelect(false);
                                    }

                                    boolean isthird = false;
                                    for (int k = 0; k < mPdfOutlinesCount.size(); k++) {
                                        PDFOutlineElementVO pdfOutlineElement2 = mPdfOutlinesCount.get(k);

                                        if (pdfOutlineElement2.getParent().equals(pdfOutlineElement_third.getParent())) {
                                            if (pdfOutlineElement2.isSelect()) {
                                                isthird = true;
                                            }

                                        }
                                    }
                                    if (!isthird) {
                                        for (int k = 0; k < mPdfOutlinesCount.size(); k++) {
                                            PDFOutlineElementVO pdfOutlineElement2 = mPdfOutlinesCount.get(k);

                                            if (pdfOutlineElement2.getId().equals(pdfOutlineElement_third.getParent())) {
                                                pdfOutlineElement2.setSelect(false);

                                            }

                                        }
                                    }

                                }
                            }
                        }
                    }
                }

                break;
            }
        }

    }

    private void move(PDFOutlineElementVO element) {

        switch (element.getLevel()) {
            case 1: {

                for (int i = 0; i < mPdfOutlines.size(); i++) {
                    PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlines.get(i);

                    if (pdfOutlineElement1.getParent().equals(element.getId())) {
                        pdfOutlineElement1.setSelect(element.isSelect());

                        for (int j = 0; j < mPdfOutlines.size(); j++) {
                            PDFOutlineElementVO pdfOutlineElement2 = mPdfOutlines.get(j);
                            if (pdfOutlineElement2.getParent().equals(pdfOutlineElement1.getId())) {
                                pdfOutlineElement2.setSelect(element.isSelect());

                                for (int k = 0; k < mPdfOutlines.size(); k++) {
                                    PDFOutlineElementVO pdfOutlineElement3 = mPdfOutlines.get(k);
                                    if (pdfOutlineElement3.getParent().equals(pdfOutlineElement2.getId())) {
                                        pdfOutlineElement3.setSelect(element.isSelect());
                                    }
                                }

                            }
                        }

                    }

                }

                break;
            }
            case 2: {
                for (int i = 0; i < mPdfOutlines.size(); i++) {
                    PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlines.get(i);

                    if (pdfOutlineElement1.getParent().equals(element.getId())) {
                        pdfOutlineElement1.setSelect(element.isSelect());

                        for (int j = 0; j < mPdfOutlines.size(); j++) {
                            PDFOutlineElementVO pdfOutlineElement2 = mPdfOutlines.get(j);
                            if (pdfOutlineElement2.getParent().equals(pdfOutlineElement1.getId())) {
                                pdfOutlineElement2.setSelect(element.isSelect());

                            }
                        }

                    }

                }

                for (int i = 0; i < mPdfOutlines.size(); i++) {
                    PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlines.get(i);

                    if (pdfOutlineElement1.getId().equals(element.getParent())) {
                        pdfOutlineElement1.setSelect(element.isSelect());

                    }

                }
                for (int l = 0; l < mPdfOutlinesCount.size(); l++) {
                    if (mPdfOutlinesCount.get(l).getId().equals(element.getParent())) {
                        mPdfOutlinesCount.get(l).setSelect(element.isSelect());
                    }
                }

                break;
            }
            case 3: {
                for (int i = 0; i < mPdfOutlines.size(); i++) {
                    PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlines.get(i);

                    if (pdfOutlineElement1.getParent().equals(element.getId())) {
                        pdfOutlineElement1.setSelect(element.isSelect());

                    }

                }

                for (int i = 0; i < mPdfOutlines.size(); i++) {
                    PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlines.get(i);

                    if (pdfOutlineElement1.getId().equals(element.getParent())) {
                        pdfOutlineElement1.setSelect(element.isSelect());
                        for (int j = 0; j < mPdfOutlines.size(); j++) {
                            PDFOutlineElementVO pdfOutlineElement2 = mPdfOutlines.get(j);
                            if (pdfOutlineElement2.getId().equals(pdfOutlineElement1.getParent())) {
                                pdfOutlineElement2.setSelect(element.isSelect());

                            }
                        }
                        for (int l = 0; l < mPdfOutlinesCount.size(); l++) {
                            if (mPdfOutlinesCount.get(l).getId().equals(pdfOutlineElement1.getParent())) {
                                mPdfOutlinesCount.get(l).setSelect(element.isSelect());
                            }
                        }
                    }

                }

                break;
            }
            case 4: {

                userChange(element);

                break;
            }

        }

    }

    private void userCancel() {

    }

    public void userChange(PDFOutlineElementVO element) {

        for (int l = 0; l < mPdfOutlines.size(); l++) {
            if (mPdfOutlines.get(l).getId().equals(element.getParent())) {
                mPdfOutlines.get(l).setSelect(element.isSelect());
            }
        }

        for (int l = 0; l < mPdfOutlinesCount.size(); l++) {
            if (mPdfOutlinesCount.get(l).getId().equals(element.getParent())) {
                mPdfOutlinesCount.get(l).setSelect(element.isSelect());
            }
        }

        for (int i = 0; i < mPdfOutlines.size(); i++) {
            PDFOutlineElementVO pdfOutlineElement1 = mPdfOutlines.get(i);

            if (pdfOutlineElement1.getId().equals(element.getParent())) {
                pdfOutlineElement1.setSelect(element.isSelect());

                if (element.getUser_level() == 2) {
                    for (int l = 0; l < mPdfOutlinesCount.size(); l++) {
                        if (mPdfOutlinesCount.get(l).getId().equals(element.getParent())) {
                            mPdfOutlinesCount.get(l).setSelect(element.isSelect());
                        }
                    }
                }

                for (int j = 0; j < mPdfOutlines.size(); j++) {
                    PDFOutlineElementVO pdfOutlineElement2 = mPdfOutlines.get(j);

                    if (pdfOutlineElement2.getId().equals(pdfOutlineElement1.getParent())) {
                        pdfOutlineElement2.setSelect(element.isSelect());

                        if (element.getUser_level() == 3) {
                            for (int l = 0; l < mPdfOutlinesCount.size(); l++) {
                                if (mPdfOutlinesCount.get(l).getId().equals(pdfOutlineElement1.getParent())) {
                                    mPdfOutlinesCount.get(l).setSelect(element.isSelect());
                                }
                            }
                        }

                        for (int k = 0; k < mPdfOutlines.size(); k++) {
                            PDFOutlineElementVO pdfOutlineElement3 = mPdfOutlines.get(k);

                            if (pdfOutlineElement3.getId().equals(pdfOutlineElement2.getParent())) {
                                pdfOutlineElement3.setSelect(element.isSelect());

                                if (element.getUser_level() == 4) {
                                    for (int l = 0; l < mPdfOutlinesCount.size(); l++) {
                                        if (mPdfOutlinesCount.get(l).getId().equals(pdfOutlineElement2.getParent())) {
                                            mPdfOutlinesCount.get(l).setSelect(element.isSelect());
                                        }
                                    }
                                }

                            }
                        }

                    }

                }

            }

        }

    }

    @Override
    public void employeeOnclick(int arg2, int viewType, PDFOutlineElementVO vo) {
        // 1 check 2 see

        if (viewType == 1) {
            PDFOutlineElementVO element = mPdfOutlinesCount.get(arg2);
            if (element.isSelect()) {
                element.setSelect(false);

                boolean ishas = element.isSelect();
                for (int l = 0; l < mPdfOutlines.size(); l++) {
                    if ("user".equals(element.getId())) {
                        if (mPdfOutlines.get(l).getDepartmentandwmployee().getUserid().equals(element.getDepartmentandwmployee().getUserid())) {
                            mPdfOutlines.get(l).setSelect(ishas);
                        }
                    } else {
                        if (mPdfOutlines.get(l).getId().equals(element.getId())) {
                            mPdfOutlines.get(l).setSelect(ishas);
                        }
                    }

                }
                int level = 0;
                if ("user".equals(element.getId())) {
                    level = element.getUser_level();
                } else {
                    level = element.getLevel();
                }
                dateCancel(element, level);

            } else {
                element.setSelect(true);
                boolean ishas = element.isSelect();
                for (int l = 0; l < mPdfOutlines.size(); l++) {
                    if ("user".equals(element.getId())) {
                        if (mPdfOutlines.get(l).getDepartmentandwmployee().getUserid().equals(element.getDepartmentandwmployee().getUserid())) {
                            mPdfOutlines.get(l).setSelect(ishas);
                        }
                    } else {
                        if (mPdfOutlines.get(l).getId().equals(element.getId())) {
                            mPdfOutlines.get(l).setSelect(ishas);
                        }
                    }

                }
                move(element);
                /**
                 * @author peck
                 * @Description: 发布任务中的选择相关人员
                 * @date 2015/5/19 12:18
                 * @email 971371860@qq.com
                 * @version V1.0
                 */
                if (requst_number < 3 && requst_number > 0) {
                    // 只有在相关人的时候是需要多选，并且相应的定义是1，2，3的常量
                    submitStartActivityForResult();
                }
            }

            mAddCcAdapter.notifyDataSetChanged();
        } else {

            if (!mPdfOutlinesCount.get(arg2).isMhasChild()) {
                return;
            }

            if (mPdfOutlinesCount.get(arg2).isExpanded()) {
                mPdfOutlinesCount.get(arg2).setExpanded(false);
                PDFOutlineElementVO pdfOutlineElement = mPdfOutlinesCount.get(arg2);
                ArrayList<PDFOutlineElementVO> temp = new ArrayList<PDFOutlineElementVO>();

                for (int i = arg2 + 1; i < mPdfOutlinesCount.size(); i++) {
                    if (pdfOutlineElement.getLevel() >= mPdfOutlinesCount.get(i).getLevel()) {
                        break;
                    }
                    temp.add(mPdfOutlinesCount.get(i));
                }

                mPdfOutlinesCount.removeAll(temp);

                mAddCcAdapter.notifyDataSetChanged();

            } else {
                mPdfOutlinesCount.get(arg2).setExpanded(true);
                int level = mPdfOutlinesCount.get(arg2).getLevel();
                int nextLevel = level + 1;

                for (PDFOutlineElementVO pdfOutlineElement : mPdfOutlines) {
                    int j = 1;
                    if (pdfOutlineElement.getParent().equals(mPdfOutlinesCount.get(arg2).getId())) {
                        pdfOutlineElement.setLevel(pdfOutlineElement.getLevel());
                        pdfOutlineElement.setExpanded(false);
                        if ("user".equals(pdfOutlineElement.getId())) {
                            pdfOutlineElement.setUser_level(mPdfOutlinesCount.get(arg2).getLevel() + 1);
                        }
                        mPdfOutlinesCount.add(arg2 + j, pdfOutlineElement);
                        j++;

                    }
                }

                mAddCcAdapter.notifyDataSetChanged();

            }

        }
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();

            String departUrl = UrlUtil.getUrl(Constant.USER_INFO, BSApplication.getInstance().getmCompany(), "");
            String departJosn = HttpClientUtil.get(departUrl, Constant.ENCODING).trim();
            mResultVO = gson.fromJson(departJosn, ResultVO.class);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getFromAssets(String name) {
        try {
            InputStreamReader reader = new InputStreamReader(getResources().getAssets().open(name));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;

            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.add_by_department, null);
        mContentLayout.addView(layout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
        getDataFromServer();
        initSearch();
        mAddCcAdapter.notifyDataSetChanged();

        Intent intent = getIntent();
        if (intent.getIntExtra("selectsure", 0) == 2) {
            checkBox_up_check_selectall.setChecked(true);
        }
        else {
            checkBox_up_check_selectall.setChecked(false);
        }
    }

    @Override
    public void bindViewsListener() {
        // 如果点击全部选择
        checkBox_up_check_selectall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                checkboxlist.clear();
                // 如果被选中
                if (checkBox_up_check_selectall.isChecked()) {
                    falgeresult = 2;
                    falgeresult_s = 2;
                    checkBox_up_check_selectall.setChecked(true);
                    mPdfOutlines = mPdfOutlinesave;
                    for (int i = 0; i < mPdfOutlinesCount.size(); i++) {
                        mPdfOutlinesCount.get(i).setSelect(true);
                    }
                    // mAddCcAdapter.notifyDataSetChanged();

                }
                // 如果取消选择了
                else {
                    checkBox_up_check_selectall.setChecked(false);
                    mPdfOutlines = new ArrayList<PDFOutlineElementVO>();
                    falgeresult = 0;
                    falgeresult_s = 0;
                    for (int i = 0; i < mPdfOutlinesCount.size(); i++) {
                        mPdfOutlinesCount.get(i).setSelect(false);
                    }
                    mAddCcAdapter.mfilelist.clear();
                    getDataFromServer();
                    // mAddCcAdapter.notifyDataSetChanged();
                }

                mAddCcAdapter = new AddByDepartmentAdapter(AddByDepartmentActivity.this, AddByDepartmentActivity.this, R.layout.item_contacts_department_tree_view, mPdfOutlinesCount, true);
                listview.setDivider(null);
                listview.setAdapter(mAddCcAdapter);
            }
        });
    }

    /**
     * 获取选择数据并返回
     */
    private void submitStartActivityForResult() {
        // TODO Auto-generated method stub
        ArrayList<EmployeeVO> checklist = new ArrayList<EmployeeVO>();

        for (int i = 0; i < mPdfOutlines.size(); i++) {

            PDFOutlineElementVO pdf = mPdfOutlines.get(i);
            if (pdf.isSelect() && "user".equals(pdf.getId())) {
                EmployeeVO employee = new EmployeeVO();
                employee.setUserid(pdf.getDepartmentandwmployee().getUserid());
                employee.setFullname(pdf.getDepartmentandwmployee().getFullname());
                employee.setHeadpic(pdf.getDepartmentandwmployee().getHeadpic());
                employee.setTel(pdf.getDepartmentandwmployee().getTel());
                employee.setUser_level(pdf.getUser_level());
                if (pdf.getDepartmentandwmployee().getIsdefault() != null) {
                    employee.setIsdefault(pdf.getDepartmentandwmployee().getIsdefault());
                }
                checklist.add(employee);
                Intent intent = new Intent();
                intent.setClass(context, clz);
                intent.putExtra("checkboxlist", (Serializable) checklist);
                intent.putExtra("approve_activity", employee);
                // intent.putExtra("selectsure", falgeresult_s);
                setResult(requst_number, intent);
                finish();
                break;
            }
        }

    }

}
