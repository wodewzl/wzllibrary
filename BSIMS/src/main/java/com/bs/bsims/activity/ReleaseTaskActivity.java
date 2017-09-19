
package com.bs.bsims.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.AttachmentAdapter;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.calendarmanager.ui.datedialog.CalendarPopupWindowUtils;
import com.bs.bsims.calendarmanager.ui.datedialog.CalendarPopupWindowUtils.KcalendarCallback;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.Attachment;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.TaskEventItem;
import com.bs.bsims.receiver.TaskActionReceiver;
import com.bs.bsims.utils.CommDateFormat;
import com.bs.bsims.utils.CommFileUtils;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.WindowUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author peck
 * @Description: 发布任务页面
 * @date 2015-5-18 下午4:31:29
 * @email 971371860@qq.com
 * @version V1.0
 */
public class ReleaseTaskActivity extends BaseActivity implements
        OnItemClickListener, android.view.View.OnClickListener {
    private String TAG = "ReleaseTaskActivity";

    private Context context = ReleaseTaskActivity.this;

    /** 发布任务标题 */
    @ViewInject(R.id.edit_taskevent_release_tasks_title)
    private EditText edit_taskevent_release_tasks_title;

    /** 发布任务详情 */
    @ViewInject(R.id.edit_taskevent_release_tasks_details)
    private EditText edit_taskevent_release_tasks_details;

    /** 开始布局 */
    @ViewInject(R.id.relative_taskevent_release_tasks_start)
    private RelativeLayout relative_taskevent_release_tasks_start;

    /** 结束布局 */
    @ViewInject(R.id.relative_taskevent_release_tasks_end)
    private RelativeLayout relative_taskevent_release_tasks_end;

    /** 添加附件布局 */
    // @ViewInject(R.id.relative_taskevent_release_tasks_addFujian)
    // private RelativeLayout relative_taskevent_release_tasks_addFujian;

    /** 附件名字 */
    /*
     * @ViewInject(R.id.txt_releaseTaskEvent_fujianName) private TextView txt_fujianName;
     */
    /** 跟进人布局 */
    @ViewInject(R.id.relative_taskevent_release_tasks_genjinren)
    private RelativeLayout relative_taskevent_release_tasks_genjinren;

    /** 负责人布局 */
    @ViewInject(R.id.relative_taskevent_release_tasks_fuzeren)
    private RelativeLayout relative_taskevent_release_tasks_zherenren;

    /** 相关人布局 */
    @ViewInject(R.id.relative_taskevent_release_tasks_xiangguanren)
    private RelativeLayout relative_taskevent_release_tasks_xiangguanren;

    /** 开始时间 */
    @ViewInject(R.id.txt_taskevent_releasetask_startTime)
    private TextView txt_taskevent_releaseTask_startTime;

    /** 结束时间 */
    @ViewInject(R.id.txt_taskevent_releasetask_endTime)
    private TextView txt_taskevent_releaseTask_endTime;

    /** 跟进人 */
    @ViewInject(R.id.txt_taskevent_releasetask_genjinren)
    private TextView txt_taskevent_releaseTask_genjinren;

    /** 责任人 */
    @ViewInject(R.id.txt_taskevent_releasetask_fuzeren)
    private TextView txt_taskevent_releaseTask_zherenren;

    /** 相关人 */
    @ViewInject(R.id.myGridView_taskevent_releasetask_xiangguanren)
    private MyGridView myGridView_taskevent_releaseTask_xiangguanren;

    /** 添加附件内容容器 */
    @ViewInject(R.id.mygridView_addtask_attachment)
    private MyGridView gridViewAttachment;

    /** 添加附件提示 */
    @ViewInject(R.id.txt_addtask_attachment)
    private TextView msg_attachment;

    /** 添加附件提示 */
    @ViewInject(R.id.txt_taskevent_releasetask_xiangguanren)
    private TextView txt_xiangguanren;

    /** 跟进人 */
    private static final int REQUEST_CODE_GEN = 1;

    /** 责任人 */
    private static final int REQUEST_CODE_ZHE = 2;

    /** 相关人 */
    private static final int REQUEST_CODE_RELATIVE = 3;

    /** 相关人的集合 */
    private List<EmployeeVO> checkboxlist = new ArrayList<EmployeeVO>();

    /** 跟进人ID */
    private String id_gen = "";

    /** 责任人ID */
    private String id_zhe = "";

    /** 相关人ID */
    private String id_relative = "";

    /** userid */
    private String userid;

    /** 开始日期 */
    private String start_date = "";

    /** 结束日期 */
    private String end_date = "";

    /** 开始时间戳 */
    private long start_time;

    /** 结束时间戳 */
    private long end_time;

    /** 文件绝对路径 */
    // private String url_AbsolutePath = "";

    private final int progressWidth = 1000; // 总长度\

    /** 附件适配器 */
    private AttachmentAdapter attachmentAdapter;

    /** 附件集合 */
    ArrayList<Attachment> attachmentList = new ArrayList<Attachment>();

    private boolean isTabTask = false;
    private boolean isTabHBTask = false;

    List<TaskEventItem> parserJson = new ArrayList<TaskEventItem>();
    TaskEventItem taskEventItem = null;

    protected SharedPreferences mSettings;

    /** 跟进人头像 */
    @ViewInject(R.id.image_taskevent_releasetask_genjinren)
    private BSCircleImageView imageGenjinren;
    /** 负责人头像 */
    @ViewInject(R.id.image_taskevent_releasetask_fuzeren)
    private BSCircleImageView imageFuzeren;

    private HeadAdapter mTransferAdapter;

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        switch (arg0) {
            case REQUEST_CODE_GEN:// 跟进人
    
                if (arg2 != null) {
                    EmployeeVO employee = (EmployeeVO) arg2
                            .getSerializableExtra("approve_activity");
    
                    if (employee == null) {
                        employee = ((List<EmployeeVO>) arg2.getSerializableExtra("checkboxlist"))
                                .get(0);
                    }
    
                    if (employee != null) {
                        txt_taskevent_releaseTask_genjinren.setText(employee
                                .getFullname());
                        id_gen = employee.getUserid();
                        String imgPath = employee.getHeadpic();
                        CustomLog.e(TAG, imgPath);
                        if (!TextUtils.isEmpty(imgPath)) {
                            if (!"暂无".equalsIgnoreCase(imgPath)) {
                                ImageLoader.getInstance().displayImage(imgPath,
                                        imageGenjinren,
                                        CommonUtils.initImageLoaderOptions());
                            } else {
                                imageGenjinren.setImageResource(R.drawable.ic_default_portrait_s);
                            }
                        }
                    }
                }
    
                break;
            case REQUEST_CODE_ZHE:// 责任人
                if (arg2 != null) {
                    EmployeeVO employee = (EmployeeVO) arg2
                            .getSerializableExtra("approve_activity");
                    if (employee == null) {
                        employee = ((List<EmployeeVO>) arg2.getSerializableExtra("checkboxlist"))
                                .get(0);
                    }
                    if (employee != null) {
                        txt_taskevent_releaseTask_zherenren.setText(employee
                                .getFullname());
                        id_zhe = employee.getUserid();
                        String imgPath = employee.getHeadpic();
                        CustomLog.e(TAG, imgPath);
                        if (!TextUtils.isEmpty(imgPath)) {
                            if (!"暂无".equalsIgnoreCase(imgPath)) {
                                ImageLoader.getInstance().displayImage(imgPath,
                                        imageFuzeren,
                                        CommonUtils.initImageLoaderOptions());
                            } else {
                                imageFuzeren.setImageResource(R.drawable.ic_default_portrait_s);
                            }
                        }
                    }
                }
                break;
    
            case REQUEST_CODE_RELATIVE:// 相关人
                checkboxlist.clear();
                if (arg2 != null && arg2.hasExtra("checkboxlist")) {
                    checkboxlist = (List<EmployeeVO>) arg2
                            .getSerializableExtra("checkboxlist");
                    mTransferAdapter.updateData(checkboxlist);
                    txt_xiangguanren.setVisibility(View.GONE);
                    for (int i = 0; i < checkboxlist.size(); i++) {
                        id_relative = id_relative + checkboxlist.get(i).getUserid()
                                + ",";
                    }
                }
                break;
            case 10:// 相关人
                /**
                 * 在相关人多选的时候 2015/8/3 10:54 之后 选择不了
                 */
                checkboxlist.clear();
                if (arg2 != null && arg2.hasExtra("checkboxlist")) {
                    checkboxlist = (List<EmployeeVO>) arg2
                            .getSerializableExtra("checkboxlist");
                    mTransferAdapter.updateData(checkboxlist);
                    txt_xiangguanren.setVisibility(View.GONE);
                    for (int i = 0; i < checkboxlist.size(); i++) {
                        id_relative = id_relative + checkboxlist.get(i).getUserid()
                                + ",";
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        WindowUtils.hideKeyboard(ReleaseTaskActivity.this);
        switch (v.getId()) {
            case R.id.relative_taskevent_release_tasks_start:// 开始时间
                CalendarPopupWindowUtils.showPopup(context, txt_comm_head_activityNamefather,
                        new KcalendarCallback() {
                            @Override
                            public void kcalendarViewClick(String date) {
                                start_date = date;
                                txt_taskevent_releaseTask_startTime.setText(date);
                            }
                        }, true, null);

                break;

            case R.id.relative_taskevent_release_tasks_end:// 结束时间

                CalendarPopupWindowUtils.showPopup(context, txt_comm_head_activityNamefather,
                        new KcalendarCallback() {
                            @Override
                            public void kcalendarViewClick(String date) {
                                end_date = date;
                                txt_taskevent_releaseTask_endTime.setText(date);
                                // CustomLog.e(TAG, date);
                                WindowUtils.hideKeyboard(ReleaseTaskActivity.this);
                            }
                        }, true, null);

                break;

            case R.id.relative_taskevent_release_tasks_genjinren:// 跟进人
//                ((RelativeLayout) v).requestDisallowInterceptTouchEvent(true);;
                intent.setClass(context, AddByDepartmentActivity.class);
                intent.putExtra("approve_name", ReleaseTaskActivity.class);// 类名
                intent.putExtra("employ_name", ReleaseTaskActivity.class);// 类名
                intent.putExtra("requst_number", REQUEST_CODE_GEN);// 请求码
                startActivityForResult(intent, REQUEST_CODE_GEN);
                break;

            case R.id.relative_taskevent_release_tasks_fuzeren:// 负责人
//                ((RelativeLayout) v).requestDisallowInterceptTouchEvent(true);
                intent.setClass(context, AddByDepartmentActivity.class);
                intent.putExtra("approve_name", ReleaseTaskActivity.class);
                intent.putExtra("employ_name", ReleaseTaskActivity.class);
                intent.putExtra("requst_number", REQUEST_CODE_ZHE);
                startActivityForResult(intent, REQUEST_CODE_ZHE);
                break;

            case R.id.relative_taskevent_release_tasks_xiangguanren:// 相关人(可以不选)

                intent.setClass(context, AddByDepartmentActivity.class);
                intent.putExtra("employ_name", ReleaseTaskActivity.class);
                intent.putExtra("requst_number", REQUEST_CODE_RELATIVE);
                intent.putExtra("checkboxlist", (Serializable) checkboxlist);
                startActivityForResult(intent, REQUEST_CODE_RELATIVE);
                break;

        }

    }

    @Override
    public void initView() {
        initHeadView();
        relative_taskevent_release_tasks_start.setOnClickListener(this);
        relative_taskevent_release_tasks_end.setOnClickListener(this);
        relative_taskevent_release_tasks_genjinren.setOnClickListener(this);
        relative_taskevent_release_tasks_zherenren.setOnClickListener(this);
        relative_taskevent_release_tasks_xiangguanren.setOnClickListener(this);

        mTransferAdapter = new HeadAdapter(this, true);

        // 附件部分
        addTypeAttachment();
        // 附件部分
        initAttachmentAdapter();
        // 附件部分
        initAttachmentListener();
    }

    /** 附件添加初始化适配器 */
    // 附件部分
    private void initAttachmentAdapter() {
        initMsgAttachment();

        attachmentAdapter = new AttachmentAdapter(context, attachmentList);
        gridViewAttachment.setAdapter(attachmentAdapter);
        myGridView_taskevent_releaseTask_xiangguanren
                .setAdapter(mTransferAdapter);
        attachmentAdapter.notifyDataSetChanged();
    }

    // 附件部分

    private void initAttachmentListener() {
        gridViewAttachment.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> paramAdapterView,
                    View paramView, int paramInt, long paramLong) {
                itemAttachmentClick(attachmentList.get(paramInt));

            }
        });
        gridViewAttachment
                .setOnItemLongClickListener(new OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(
                            AdapterView<?> paramAdapterView, View paramView,
                            int paramInt, long paramLong) {
                        // itemClick(attachmentList.get(paramInt));
                        if (attachmentList.get(paramInt).getType() == 3) {
                            delTypeAttachment();
                            cancelTypeAttachment();
                            initAttachmentAdapter();
                        }
                        return true;
                    }
                });
    }

    // 附件部分

    private void itemAttachmentClick(Attachment attachment) {
        switch (attachment.getType()) {
        // 1添加2取消3正常

            case 1: {
                Intent intent = new Intent();
                intent.putExtra("flag_fujian", true);
                intent.setClass(context, UploadFileListActivity.class);
                startActivity(intent);
                break;
            }
            case 2: {
                delTypeAttachment();
                addTypeAttachment();
                initAttachmentAdapter();
                break;
            }
            case 3: {
                if (attachment.isDel()) {
                    attachmentList.remove(attachment);
                }
                initAttachmentAdapter();
                break;
            }

        }
    }

    protected void initHeadView() {
        findViewById(R.id.img_head_back).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        finish();
                    }
                });

        ((TextView) findViewById(R.id.txt_comm_head_activityName))
                .setText("发布任务");

        ((TextView) findViewById(R.id.txt_comm_head_right)).setText("确定");

        // 提交后台数据
        findViewById(R.id.txt_comm_head_right).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if ("".equals(edit_taskevent_release_tasks_title
                                .getText().toString().trim())) {
                            CustomToast.showShortToast(context, "请输入标题");
                            return;
                        }

                        if ("".equals(edit_taskevent_release_tasks_details
                                .getText().toString().trim())) {
                            CustomToast.showShortToast(context, "请输入内容");
                            return;
                        }

                        if ("".equals(start_date)) {
                            CustomToast.showShortToast(context, "请选择开始时间");
                            return;
                        }

                        if ("".equals(end_date)) {
                            CustomToast.showShortToast(context, "请选择结束时间");
                            return;
                        }

                        // 判断开始与结束时间
                        try {
                            start_time = CommDateFormat.getShortTimeStamp(
                                    start_date, "yyyy-MM-dd");
                            end_time = CommDateFormat.getShortTimeStamp(
                                    end_date, "yyyy-MM-dd");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (start_time > end_time) {
                            CustomToast
                                    .showShortToast(context, "亲，开始时间大于结束的时间");
                            return;
                        }

                        if ("".equals(id_gen)) {
                            CustomToast.showShortToast(context, "请选择跟进人");
                            return;
                        }

                        if ("".equals(id_zhe)) {
                            CustomToast.showShortToast(context, "请选择责任人");
                            return;
                        }

                        submintSignIn();
                    }
                });
    }

    private void sendBroadMessage() {
        Intent intent = new Intent(TaskActionReceiver.intentFilter);
        intent.putExtra("isAdd", true);
        intent.putExtra("type", 1);
        isTabHBTask = getIntent().getBooleanExtra("isTabHBTask", false);
        isTabTask = getIntent().getBooleanExtra("isTabTask", false);
        intent.putExtra("isTabHBTask", isTabHBTask);
        intent.putExtra("isTabTask", isTabTask);
        if (taskEventItem != null) {
            intent.putExtra("TaskEventItem", taskEventItem);
        }
        /** 1 我发布的 2 我负责的 3 我跟进的 4 我相关的 */
        sendBroadcast(intent);
    }

    /**
     * 注册广播
     */

    private void registerBoradcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_NAME_FUJIAN);
        registerReceiver(receiver, intentFilter);
    }

    /** 定义一个BroadcastReceiver广播接收类 */

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(Constant.ACTION_NAME_FUJIAN)) {
                String url_AbsolutePath = intent.getStringExtra("url");
                boolean ishas = checkHas(url_AbsolutePath);

                if (!ishas) {
                    String suffix = CommFileUtils.getSuffix(url_AbsolutePath);
                    File file = new File(url_AbsolutePath);
                    long filesize = 0;
                    String fileSizeStr = "";
                    if (file.exists() && file.isFile()) {
                        filesize = file.length();

                        // long B = 1024;
                        long KB = 1024;
                        long MB = KB * 1024;

                        if (filesize > MB) {
                            filesize /= MB;
                            fileSizeStr = filesize + "M";
                        } else if (filesize > KB) {
                            filesize /= KB;
                            fileSizeStr = filesize + "K";
                        } else {
                            fileSizeStr = filesize + "B";
                        }

                    }
                    Attachment attachment = new Attachment();
                    attachment.setFileType(suffix);
                    attachment.setTitle(fileSizeStr);
                    attachment.setFilePath(url_AbsolutePath);
                    attachment.setType(3);

                    delTypeAttachment();

                    attachmentList.add(attachment);

                    addTypeAttachment();

                    initAttachmentAdapter();
                }

            }

        }
    };

    private boolean checkHas(String path) {
        boolean check = false;
        for (int i = 0; i < attachmentList.size(); i++) {
            if (path.equals(attachmentList.get(i).getFilePath())) {
                check = true;
            }
        }

        return check;

    }

    private void cancelTypeAttachment() {
        ArrayList<Attachment> test = new ArrayList<Attachment>();
        for (int i = 0; i < attachmentList.size(); i++) {
            Attachment attachment = attachmentList.get(i);
            int type = attachment.getType();
            if (type == 3) {
                attachment.setDel(true);
                test.add(attachment);
            }
        }

        attachmentList.clear();
        attachmentList.addAll(test);

        Attachment attachment = new Attachment();
        attachment.setType(2);
        attachmentList.add(attachment);
    }

    // 附件部分

    private void addTypeAttachment() {
        ArrayList<Attachment> test = new ArrayList<Attachment>();
        for (int i = 0; i < attachmentList.size(); i++) {
            Attachment attachment = attachmentList.get(i);
            int type = attachment.getType();
            if (type == 3) {
                attachment.setDel(false);
                test.add(attachment);
            }
        }

        attachmentList.clear();
        attachmentList.addAll(test);

        Attachment attachment = new Attachment();
        attachment.setType(1);
        attachmentList.add(attachment);
    }

    // 附件部分

    private void delTypeAttachment() {
        ArrayList<Attachment> test = new ArrayList<Attachment>();
        for (int i = 0; i < attachmentList.size(); i++) {
            Attachment attachment = attachmentList.get(i);
            int type = attachment.getType();
            if (type == 3) {
                attachment.setDel(false);
                test.add(attachment);
            }
        }

        attachmentList.clear();
        attachmentList.addAll(test);
    }

    // 附件部分

    private void initMsgAttachment() {
        if (attachmentList.size() == 1 && attachmentList.get(0).getType() == 2) {
            attachmentList.clear();
            addTypeAttachment();
        }
        if (attachmentList.size() == 1 && attachmentList.get(0).getType() == 1) {
            msg_attachment.setVisibility(View.VISIBLE);
        } else {
            msg_attachment.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.ac_taskevent_release_tasks, mContentLayout);
        x.view().inject(this);
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);

        registerBoradcastReceiver();
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void bindViewsListener() {
        myGridView_taskevent_releaseTask_xiangguanren
                .setOnItemClickListener(this);
        imageGenjinren.setOnClickListener(this);
        imageFuzeren.setOnClickListener(this);
        imageGenjinren.setClickable(false);
        imageFuzeren.setClickable(false);
     
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View arg0, int arg1,
            long arg2) {
        if (parent == myGridView_taskevent_releaseTask_xiangguanren) {
            if (arg2 == mTransferAdapter.mList.size()) {
                Intent intent = new Intent();
                intent.setClass(context, AddByDepartmentActivity.class);
                intent.putExtra("employ_name", ReleaseTaskActivity.class);
                intent.putExtra("checkboxlist", (Serializable) checkboxlist);
                intent.putExtra("requst_number", REQUEST_CODE_RELATIVE);
                startActivityForResult(intent, REQUEST_CODE_RELATIVE);
            }
        }
    }

    private void submintSignIn() {
        CustomDialog.showProgressDialog(context);
        String url = Constant.TaskEventPath.TaskEventRelease_path;
        url = BSApplication.getInstance().getHttpTitle()
                + Constant.TaskEventPath.TaskEventRelease_path_T;
        RequestParams paramsMap = new RequestParams(url);

        try {
            setBodyParams(paramsMap, "ftoken", BSApplication.getInstance()
                    .getmCompany());

            setBodyParams(paramsMap, "userid", BSApplication.getInstance()
                    .getUserId());
            setBodyParams(paramsMap, "t_title",
                    edit_taskevent_release_tasks_title.getText().toString()
                            .trim());
            setBodyParams(paramsMap, "t_content",
                    edit_taskevent_release_tasks_details.getText().toString()
                            .trim());
            setBodyParams(paramsMap, "starttime", start_time + "");
            setBodyParams(paramsMap, "endtime", end_time + "");
            setBodyParams(paramsMap, "t_relevant", id_relative);// 相关人员传id
            setBodyParams(paramsMap, "t_follow_up", id_gen);// 跟进人id
            setBodyParams(paramsMap, "t_responsible", id_zhe);// 责任人id

            for (int i = 0; i < attachmentList.size(); i++) {
                Attachment attachment = attachmentList.get(i);
                if (attachment.getType() == 3) {
                    File file = new File(attachment.getFilePath());
                    if (file.exists()) {
                        setBodyParams(paramsMap, "annex" + i,
                                attachment.getFilePath());
                        CustomLog.e(TAG,
                                "path >>>> " + attachment.getFilePath());
                    }
                }
            }
        } catch (Exception e1) {
            CustomToast.showShortToast(context, "发布失败");
            CustomDialog.closeProgressDialog();
            e1.printStackTrace();
        }

        x.http().post(paramsMap, new CommonCallback<String>() {

            @Override
            public void onCancelled(CancelledException arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                CustomToast.showShortToast(context, "网络数据错误,请重新尝试");
                CustomDialog.closeProgressDialog();
            }

            @Override
            public void onFinished() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSuccess(String arg0) {
                // TODO Auto-generated method stub
                JSONObject jsonObject;
                String result = arg0;
                try {
                    jsonObject = new JSONObject(result);
                    String str = "retinfo";

                    if (jsonObject.has("retinfo")) {
                        str = (String) jsonObject.get("retinfo");
                    }
                    if (jsonObject.has("code")) {
                        String code = (String) jsonObject.get("code");
                        if ("200".equalsIgnoreCase(code)) {
                            // 成功
                            CustomToast.showShortToast(context, str);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(
                                            context,
                                            EXTTaskHomeLAVAActivity.class));
                                    finish();
                                }
                            }, 700);
                        } else {
                            CustomToast.showShortToast(context, str);
                        }
                    }
                    if (jsonObject.has("return")) {
                        String codeReturn = (String) jsonObject
                                .get("return");
                        if ("false".equalsIgnoreCase(codeReturn)) {
                            // "retinfo":"对不起，请求数据出错！",
                            CustomToast.showShortToast(context, str);
                        }
                    }
                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    CustomToast.showShortToast(context, "请与管理员联系");
                    CustomDialog.closeProgressDialog();
                }
            }

        });

    }

    private void setBodyParams(RequestParams paramsMap,
            String str, String value) {
        paramsMap.addBodyParameter(str, value);
        if (str.contains("annex")) {
            File file = new File(value);
            if (file.exists()) {
                paramsMap.addBodyParameter(str, file);
            }
        }
    } 
}
