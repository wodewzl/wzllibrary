
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.AddByDepartmentAdapter;
import com.bs.bsims.adapter.BSBaseAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.EmployeeOnclickCallback;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.NoticeObjectResultVO;
import com.bs.bsims.model.NoticeObjectVO;
import com.bs.bsims.model.PDFOutlineElementVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.utils.DepartmentMoreUtis;
import com.bs.bsims.utils.HttpClientUtil;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("ResourceAsColor")
public class NoticeObjectActivity extends BaseActivity implements OnClickListener, EmployeeOnclickCallback, OnItemClickListener {
    private TextView mDepartTv, mPostTv;
    private LinearLayout mDepartTitleLayout, mPostTitleLayout;
    private ImageView mSelectOne, mSelectTwo;
    private List<LinearLayout> mLayouts;

    private AddByDepartmentAdapter mAddByDepartmentAdapter;
    private ListView mListview;
    private ArrayList<PDFOutlineElementVO> mPdfOutlinesCount;
    private DepartmentMoreUtis mDepartmentUtis;

    private List<NoticeObjectVO> mPostsVOList, mPositionsVOList;

    private List<EmployeeVO> mOneList;
    private List<NoticeObjectVO> mTwoList;
    private List<Object> mResult;
    private boolean flag = true;

    private ResultVO mResultVO;
    private NoticeObjectVO mAllVo;
    private NoticeObjectResultVO mPostsResultVO;
    private OptionsAdapter mOptionsAdapter;
    private ListView mPostListView;

    int requst_number = 0;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mAddByDepartmentAdapter.notifyDataSetChanged();
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void initView() {
        mTitleTv.setText(R.string.notice_object);
        mOkTv.setText("确定");
        mDepartTitleLayout = (LinearLayout) findViewById(R.id.depart_layout);
        mPostTitleLayout = (LinearLayout) findViewById(R.id.post_layout);
        mDepartTv = (TextView) findViewById(R.id.by_depart_tv);
        mPostTv = (TextView) findViewById(R.id.by_post_tv);
        mSelectOne = (ImageView) findViewById(R.id.select_icon01);
        mSelectTwo = (ImageView) findViewById(R.id.select_icon02);
        mListview = (ListView) findViewById(R.id.listView_single_newList);
        mListview.setDivider(null);
        mLayouts = new ArrayList<LinearLayout>();
        mOneList = new ArrayList<EmployeeVO>();
        mTwoList = new ArrayList<NoticeObjectVO>();
        mResult = new ArrayList<Object>();
        mResultVO = ResultVO.getInstance();
        mAddByDepartmentAdapter = new AddByDepartmentAdapter(this, this, R.layout.item_contacts_department_tree_view, true);
        mDepartmentUtis = new DepartmentMoreUtis(this, mResultVO, mHandler, true);
        mAddByDepartmentAdapter.mfilelist = mDepartmentUtis.getPdfOutlinesCount();

        // 岗位
        mOptionsAdapter = new OptionsAdapter(this);
        mPostListView = (ListView) findViewById(R.id.post_list);
        mPostListView.setAdapter(mOptionsAdapter);
        showDepartmentView();
        getIntentDatas();

        findViewById(R.id.title_layout).setVisibility(View.GONE);
    }

    public boolean getData() {
        try {

            Gson gson = new Gson();
            String ppostsUlr = BSApplication.getInstance().getHttpTitle() + Constant.NOTICE_OBJECT_POSITIONS + BSApplication.getInstance().getmCompany();
            String jsonPosts = HttpClientUtil.get(ppostsUlr, Constant.ENCODING).trim();
            mPostsResultVO = gson.fromJson(jsonPosts, NoticeObjectResultVO.class);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void bindViewsListener() {
        mDepartTitleLayout.setOnClickListener(this);
        mPostTitleLayout.setOnClickListener(this);
        mPostListView.setOnItemClickListener(this);
        mOkTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {

            case R.id.depart_layout:
                showDepartmentView();
                break;
            case R.id.post_layout:
                showPostView();
                break;

            case R.id.ok_bt:
                reslutData();
                intent.putExtra("oneList", (Serializable) mOneList);
                intent.putExtra("twoList", (Serializable) mTwoList);
                setResult(requst_number, intent);
                finish();
                break;

            case R.id.txt_comm_head_right:
                reslutData();
                intent.putExtra("oneList", (Serializable) mOneList);
                intent.putExtra("twoList", (Serializable) mTwoList);
                setResult(requst_number, intent);
                finish();
                break;

            default:
                break;
        }
    }

    public void showDepartmentView() {
        mDepartTv.setTextColor(Color.parseColor("#00A9FE"));
        mPostTv.setTextColor(Color.parseColor("#999999"));
        mListview.setVisibility(View.VISIBLE);
        mPostListView.setVisibility(View.GONE);
        mSelectOne.setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
        mSelectTwo.setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_default1);
    }

    public void showPostView() {
        mDepartTv.setTextColor(Color.parseColor("#999999"));
        mPostTv.setTextColor(Color.parseColor("#00A9FE"));
        mListview.setVisibility(View.GONE);
        mPostListView.setVisibility(View.VISIBLE);
        mSelectTwo.setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
        mSelectOne.setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_default1);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("oneList", (Serializable) mOneList);
            intent.putExtra("twoList", (Serializable) mTwoList);
            setResult(requst_number, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void employeeOnclick(int arg2, int viewType, PDFOutlineElementVO vo) {
        mDepartmentUtis.employeeOnclick(arg2, viewType);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
        mListview.setAdapter(mAddByDepartmentAdapter);
        if (Constant.RESULT_CODE.equals(mPostsResultVO.getCode())) {
            mOptionsAdapter.updateData(mPostsResultVO.getArray());
            matchPost();
        }

    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.notice_object, null);
        mContentLayout.addView(layout);
    }

    /**
     * 获取选择数据并返回
     */
    public void reslutData() {
        if (mDepartmentUtis == null)
            return;
        mOneList.clear();
        for (int i = 0; i < mDepartmentUtis.mPdfOutlines.size(); i++) {

            PDFOutlineElementVO pdf = mDepartmentUtis.mPdfOutlines.get(i);
            if (pdf.isSelect() && "user".equals(pdf.getId())) {
                EmployeeVO employee = new EmployeeVO();
                employee.setUserid(pdf.getDepartmentandwmployee().getUserid());
                employee.setFullname(pdf.getDepartmentandwmployee().getFullname());
                employee.setHeadpic(pdf.getDepartmentandwmployee().getHeadpic());
                employee.setTel(pdf.getDepartmentandwmployee().getTel());
                employee.setUser_level(pdf.getUser_level());
                mOneList.add(employee);
            }
        }

    }

    private void getIntentDatas() {
        Intent intent = getIntent();

        if (intent.hasExtra("requst_number")) { // 返回码
            requst_number = intent.getIntExtra("requst_number", 0);
        }

        List<EmployeeVO> oneList = (List<EmployeeVO>) intent.getSerializableExtra("oneList");
        List<NoticeObjectVO> twoList = (List<NoticeObjectVO>) intent.getSerializableExtra("twoList");

        if (oneList != null) { // 之前选中的抄送人的集合
            showDepartmentView();
            if (oneList.size() > 0) {
                mOneList.clear();
                mOneList.addAll(oneList);
                matchDepartment();
            }
            return;

        } else if (twoList != null) { // 之前选中的抄送人的集合

            showPostView();
            if (twoList.size() > 0) {
                mTwoList.clear();
                mTwoList.addAll(twoList);
            }
        }
    }

    public void matchDepartment() {
        for (int i = 0; i < mDepartmentUtis.mPdfOutlines.size(); i++) {
            for (int j = 0; j < mOneList.size(); j++) {
                if (mDepartmentUtis.mPdfOutlines.get(i).getDepartmentandwmployee().getUserid().equals(mOneList.get(j).getUserid())) {
                    mDepartmentUtis.mPdfOutlines.get(i).setSelect(true);
                    mDepartmentUtis.mPdfOutlines.get(i).setUser_level(mOneList.get(j).getUser_level());
                    mDepartmentUtis.move(mDepartmentUtis.mPdfOutlines.get(i));
                }

            }
        }
    }

    public void matchPost() {
        for (int i = 0; i < mTwoList.size(); i++) {
            for (int j = 0; j < mOptionsAdapter.mList.size(); j++) {
                if (mTwoList.get(i).getPositionsid().equals(mOptionsAdapter.mList.get(j).getPositionsid())) {
                    mOptionsAdapter.mList.get(j).setIsselected(true);
                }
            }
        }
        mOptionsAdapter.notifyDataSetChanged();
    }

    class OptionsAdapter extends BSBaseAdapter<NoticeObjectVO> {
        private Context mContext;

        public OptionsAdapter(Context context) {
            super(context);
            mContext = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (mIsEmpty) {
                return super.getView(position, convertView, parent);
            }

            if (convertView != null && convertView.getTag() == null)
                convertView = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.crm_lv_options, null);
                holder.mTileTv = (TextView) convertView.findViewById(R.id.title_tv);
                holder.mStatusImg = (ImageView) convertView.findViewById(R.id.status_img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            NoticeObjectVO vo = mList.get(position);
            if (vo.isIsselected()) {
                holder.mStatusImg.setImageResource(R.drawable.common_ic_selected);
            } else {
                holder.mStatusImg.setImageResource(R.drawable.common_ic_unselect);
            }
            holder.mTileTv.setText(vo.getPositionsname());
            holder.mTileTv.setTextColor(Color.parseColor("#ff000000"));

            return convertView;
        }

        class ViewHolder {
            private TextView mTileTv;
            private ImageView mStatusImg;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        int position = (int) arg3;
        NoticeObjectVO vo = mOptionsAdapter.mList.get(position);
        if (vo.isIsselected()) {
            vo.setIsselected(false);
            mTwoList.remove(vo);
        } else {
            vo.setIsselected(true);
            mTwoList.add(vo);
        }

        mOptionsAdapter.notifyDataSetChanged();
    }

}
