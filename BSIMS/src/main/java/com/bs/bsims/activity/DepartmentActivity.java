
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.DepartmentAdapter;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.model.PDFOutlineElementVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.utils.CharacterParser;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSIndexEditText;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 按部门分类列表
 * 
 * @author Administrator 只有1个ListView,里面套了几种不同的布局，可以展开的页面，全部是用LinearLayout动态添加
 */
@SuppressLint("ShowToast")
public class DepartmentActivity extends Activity {

    /** 最外层数据 */
    private ArrayList<PDFOutlineElementVO> mPdfOutlinesCount = new ArrayList<PDFOutlineElementVO>();

    /** 内层数据 */
    private ArrayList<PDFOutlineElementVO> mPdfOutlines = new ArrayList<PDFOutlineElementVO>();
    private DepartmentAdapter treeViewAdapter = null;
    private ListView dplistview;

    Context context = DepartmentActivity.this;

    private BSIndexEditText mClearEditText;
    private Handler backHandler;

    private CharacterParser characterParser;

    @Override
    public void setContentView(int layoutResID) {
        // TODO Auto-generated method stub
        super.setContentView(R.layout.ac_department);
        // initSearch();
        initView();
        getDataFromServer();
    }

    protected void initSearch() {
        Thread searchThread = new Thread() {

            @Override
            public void run() {
                Looper.prepare();

                backHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
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

                    // if (name.length() > 0 && filterStr.length() > 0 && name.length() >=
                    // filterStr.length()) {
                    // String name_txt = name.substring(0, filterStr.length());
                    // if (name_txt.equals(filterStr)) {

                    if (name.length() > 0 && filterStr.length() > 0 && name.length() >= filterStr.length()) {
                        // String name_txt = name.substring(0, filterStr.length());
                        if (name.contains(filterStr)) {
                            employee.setSearch(true);
                            list_2.add(employee);
                        }
                    }

                    String pinyin = characterParser.getSelling(filterStr).toUpperCase();
                    String nick = characterParser.getSelling(employee.getDepartmentandwmployee().getFullname()).toUpperCase();

                    if (!CommonUtils.isCNMark(filterStr)) {
                        if (pinyin.length() > 0 && nick.length() > 0 && nick.length() >= pinyin.length()) {
                            String nickString = nick.substring(0, pinyin.length()).toUpperCase();
                            if (pinyin.equals(nickString)) {
                                employee.setSearch(true);
                                list_3.add(employee);

                            }
                        }
                    }
                }

            }
            boolean isFirst = false;
            if (list_2.size() > 0) {
                /*
                 * for (int i = 0; i < list_2.size(); i++) { list_2.get(i).setSearch(true); }
                 */
                mPdfOutlinesCount.addAll(list_2);
                isFirst = true;
            }

            if (!isFirst) {

                if (list_3.size() > 0) {
                    /*
                     * for (int i = 0; i < list_3.size(); i++) { list_3.get(i).setSearch(true); }
                     */
                    mPdfOutlinesCount.addAll(list_3);
                }
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                InputMethodManager imm = (InputMethodManager) ((Activity) context).getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mClearEditText.getWindowToken(), 0);

                treeViewAdapter.notifyDataSetChanged();
            }
        });
    }

    protected void initHeadView() {
        // findViewById(R.id.relative_comm_head_back).setOnClickListener(headBackListener);
        ((TextView) findViewById(R.id.txt_comm_head_activityName)).setText("部门");
    }

    protected void initView() {
        ((TextView) findViewById(R.id.txt_comm_head_activityName)).setText("部门");
        characterParser = CharacterParser.getInstance();

        mClearEditText = (BSIndexEditText) findViewById(R.id.edit_single_search);
        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

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

        dplistview = (ListView) findViewById(R.id.listView_departmentList);

        // treeViewAdapter = new DepartmentAdapter(this, R.layout., mPdfOutlinesCount);

        dplistview.setDivider(null);
        dplistview.setAdapter(treeViewAdapter);

        dplistview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (!mPdfOutlinesCount.get(arg2).isMhasChild()) {
                    // Toast.makeText(this,
                    // mPdfOutlinesCount.get(position).getOutlineTitle(), 2000);
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

                    treeViewAdapter.notifyDataSetChanged();

                } else {

                    mPdfOutlinesCount.get(arg2).setExpanded(true);
                    int level = mPdfOutlinesCount.get(arg2).getLevel();
                    int nextLevel = level + 1;

                    for (PDFOutlineElementVO pdfOutlineElement : mPdfOutlines) {
                        int j = 1;
                        if (pdfOutlineElement.getParent().equals(mPdfOutlinesCount.get(arg2).getId())) {
                            // System.out.println(nextLevel+"nextLevel");
                            pdfOutlineElement.setLevel(pdfOutlineElement.getLevel());
                            pdfOutlineElement.setExpanded(false);
                            if ("user".equals(pdfOutlineElement.getId())) {
                                pdfOutlineElement.setUser_level(mPdfOutlinesCount.get(arg2).getLevel() + 1);
                            }
                            mPdfOutlinesCount.add(arg2 + j, pdfOutlineElement);
                            j++;

                        }
                    }

                    treeViewAdapter.notifyDataSetChanged();

                }

            }
        });
    }

    private void getDataFromServer() {

        // 部门
        String jsonUrlStr = getFromAssets("json.txt");
        Gson gson = new Gson();
        ResultVO resultVO = gson.fromJson(jsonUrlStr, ResultVO.class);
        List<DepartmentAndEmployeeVO> departmentList = resultVO.getDepartments();
        if (departmentList != null && departmentList.size() > 0) {
            for (int i = 0; i < departmentList.size(); i++) {
                DepartmentAndEmployeeVO department = departmentList.get(i);
                int level = Integer.parseInt(department.getLevel());
                if ("0".equals(department.getBelong())) {
                    // 获取第一级的部门

                    mPdfOutlinesCount.add(new PDFOutlineElementVO(department.getDepartmentid(), department, false, true, department.getBelong(), level, false, false));
                    mPdfOutlines.add(new PDFOutlineElementVO(department.getDepartmentid(), department, false, true, department.getBelong(), level, false, false));
                } else {
                    mPdfOutlines.add(new PDFOutlineElementVO(department.getDepartmentid(), department, true, true, department.getBelong(), level, false, false));
                }
            }
        }

        // 用户
        List<DepartmentAndEmployeeVO> employeeList = resultVO.getUsers();
        if (employeeList != null && employeeList.size() > 0) {
            for (int j = 0; j < employeeList.size(); j++) {
                DepartmentAndEmployeeVO employee = employeeList.get(j);
                mPdfOutlines.add(new PDFOutlineElementVO("user", employee, false, false, employee.getDid(), 4, false, false));
            }
        }
        treeViewAdapter.notifyDataSetChanged();

    }

    private void initialData() {

        try {
            processTask();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理数据，检查数据是否正确
     * 
     * @throws ParseException
     */
    public void processTask() throws ParseException {
        getDataFromServer();
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
}
