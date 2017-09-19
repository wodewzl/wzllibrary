
package com.bs.bsims.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.task.Async;
import com.bs.bsims.adapter.DiscussAdapter;
import com.bs.bsims.adapter.HeadAdapter4TaskDetails;
import com.bs.bsims.adapter.ListViewReplyAdapter;
import com.bs.bsims.adapter.MyGridViewNoTagAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.constant.Constant4TaskEventPath;
import com.bs.bsims.constant.ExtrasBSVO;
import com.bs.bsims.constant.ReceiverActionBSVO;
import com.bs.bsims.download.domain.DownloadFile;
import com.bs.bsims.fragment.CommentFragment;
import com.bs.bsims.model.DiscussResultVO;
import com.bs.bsims.model.DiscussVO;
import com.bs.bsims.model.LoginUser;
import com.bs.bsims.model.RelativePepoleVO;
import com.bs.bsims.model.ReplyItem;
import com.bs.bsims.model.TaskEventDetailsViewVO;
import com.bs.bsims.model.TaskEventDetailsView_InfoVO;
import com.bs.bsims.model.TaskEventDetailsView_Info_AnnexVO;
import com.bs.bsims.model.TaskEventDetailsView_Info_RelevantVO;
import com.bs.bsims.model.TaskEventDetailsView_Info_ScheduleReplyVO;
import com.bs.bsims.model.TaskEventItem;
import com.bs.bsims.model.TaskEventScheduleDescriptionItem;
import com.bs.bsims.receiver.FileDownloadSuccessReceiver;
import com.bs.bsims.receiver.TaskActionReceiver;
import com.bs.bsims.receiver.TaskScheduleReceiver;
import com.bs.bsims.utils.CommFileUtils;
import com.bs.bsims.utils.CommonDateUtils;
import com.bs.bsims.utils.CommonImageUtils;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.FileIconMapping;
import com.bs.bsims.utils.UserManager;
import com.bs.bsims.utils.WindowUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSListView;
import com.bs.bsims.view.ListViewForScrollView;
import com.bs.bsims.view.MasterLayout;
import com.bs.bsims.view.MyGridView;
import com.bs.bsims.xutils.impl.DownloadInfo;
import com.bs.bsims.xutils.impl.DownloadManager;
import com.bs.bsims.xutils.impl.DownloadViewHolder;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.ex.DbException;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @author peck
 * @Description: 任务详情页面 from TaskEventDetailsActivity_new
 * @date 2015/5/21 11:10
 * @email 971371860@qq.com
 * @version V1.1 2015/5/29 11:02 下载成功接收广播
 * @version V1.2 2015/8/1 11:01 任务评论获取失败，老版本中修改过请求地址，新版未改
 */
public class EXTTaskEventDetailsActivity extends BaseActivity implements
        android.view.View.OnClickListener {
    private String TAG = "EXTTaskEventDetailsActivity";

    private Context context = EXTTaskEventDetailsActivity.this;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions options;

    /** 标题 */
    @ViewInject(R.id.txt_taskevent_tasksDetails_title)
    private TextView txt_taskevent_tasksDetails_title;

    /** 开始时间 */
    @ViewInject(R.id.txt_taskevent_tasksDetails_StartTime)
    private TextView txt_taskevent_tasksDetails_StartTime;

    /** 结束时间 */
    @ViewInject(R.id.txt_taskevent_tasksDetails_endTime)
    private TextView txt_taskevent_tasksDetails_endTime;

    /** 详情 */
    @ViewInject(R.id.txt_taskevent_tasksDetails_details)
    private TextView txt_taskevent_tasksDetails_details;
    /** 详情 */
    @ViewInject(R.id.ly_taskevent_tasksDetails_details)
    private LinearLayout ly_taskevent_tasksDetails_details;

    /** 没有附件时用于隐藏 */
    @ViewInject(R.id.line_noFujian)
    private View line_noFujian;

    /** 下载布局用于隐藏 */
    // @ViewInject(R.id.relative_taskEventDetails_downLoadFujian)
    // private RelativeLayout relative_taskEventDetails_downLoadFujian;

    /** 下载 */
    // @ViewInject(R.id.txt_taskevent_tasksDetails_downLoad)
    // private TextView txt_taskevent_tasksDetails_downLoad;

    /** 为了得到LinearLayout宽度 */
    @ViewInject(R.id.linear_taskevent_taskDetails_progress)
    private LinearLayout linear_taskevent_taskDetails_progress;

    /** 填充进度颜色的txt */
    @ViewInject(R.id.txt_taskevent_taskDetails_fillColor)
    private TextView txt_taskevent_taskDetails_fillColor;

    /** 进度点 */
    @ViewInject(R.id.img_taskevent_taskDetails_percentPoint)
    private ImageView img_taskevent_taskDetails_percentPoint;

    /** 进度值 */
    @ViewInject(R.id.txt_taskevent_taskDetails_percentColor)
    private TextView txt_taskevent_taskDetails_percentColor;

    /** 任务事件进度的文字为0时用于隐藏 */
    @ViewInject(R.id.txt_taskevent_details_percent_title)
    private TextView txt_itemTitle;

    /** 任务事件进度为0时布局用于隐藏 */
    @ViewInject(R.id.relative_taskevent_details_percent)
    private LinearLayout relative_percent;

    /** 任务事件进度为0时下面的线用于隐藏 */
    @ViewInject(R.id.line_taskevent_details_percentTop)
    private View view_lineTop;

    /** 添加进度描述的布局 */
    @ViewInject(R.id.linear_taskevent_taskDetails_detailsDescription)
    private LinearLayout linear_taskevent_taskDetails_detailsDescription;

    /** 跟进人头像 */
    @ViewInject(R.id.img_taskevent_taskDetails_protait_gen)
    private BSCircleImageView img_taskevent_taskDetails_protait_gen;

    /** 跟进人名字 */
    @ViewInject(R.id.txt_taskevent_taskDetails_protait_gen)
    private TextView txt_taskevent_taskDetails_protait_gen;

    /** 责任人头像 */
    @ViewInject(R.id.img_taskevent_taskDetails_protait_zhe)
    private BSCircleImageView img_taskevent_taskDetails_protait_zhe;

    /** 责任人名字 */
    @ViewInject(R.id.txt_taskevent_taskDetails_protait_zhe)
    private TextView txt_taskevent_taskDetails_protait_zhe;

    /** 评论数量 */
    @ViewInject(R.id.txt_taskevent_taskDetails_pinglunNum)
    private TextView txt_taskevent_taskDetails_pinglunNum;

    @ViewInject(R.id.listView_taskevent_taskDetails_pinglun)
    private ListViewForScrollView listView_taskevent_taskDetails_pinglun;

    /** 相关人文字用于隐藏 */
    @ViewInject(R.id.txt_taskevent_details_pepole_title)
    private TextView txt_pepole;

    /** 相关人图片用于隐藏 */
    @ViewInject(R.id.relative_part_personMore)
    private RelativeLayout relative_pepole;

    /** 相关人更多 */
    @ViewInject(R.id.linear_common_arrow_threePoints)
    private LinearLayout linear_common_arrow_threePoints;

    /** 相关人 */
    @ViewInject(R.id.common_myGridViewOfClass_full)
    private MyGridView common_myGridViewOfClass_full;

    /** 相关人的适配器 */
    private MyGridViewNoTagAdapter adapter_gridView;

    /** 评论的适配器 */
    private ListViewReplyAdapter adapterReply;

    /** 屏幕宽度 */
    private int width;

    /** 任务进度条的宽度 */
    private int width_line;

    /** 相关人的宽度 */
    private int width_gridView;

    /** 展示进度的图片标签的宽度 */
    private int imgTag_width;

    /** 详情内容的集合 */
    private List<TaskEventItem> datas_details = new ArrayList<TaskEventItem>();

    /** 描述集合 */
    private List<TaskEventScheduleDescriptionItem> datas_description = new ArrayList<TaskEventScheduleDescriptionItem>();

    /** 跟进人集合 */
    private List<RelativePepoleVO> datas_genjin = new ArrayList<RelativePepoleVO>();

    /** 责任人集合 */
    private List<RelativePepoleVO> datas_fuzhe = new ArrayList<RelativePepoleVO>();

    /** 相关人集合 */
    private List<RelativePepoleVO> datas_relevant = new ArrayList<RelativePepoleVO>();

    /** 评论集合 */
    private List<ReplyItem> datas_pinlun = new ArrayList<ReplyItem>();

    private boolean fangHuiDiao = false;

    private boolean hasData = false;

    /** 返回标识 */
    // public static boolean flag_back = true;

    /** 文件路径 */
    private static String path = "";

    /** 任务事件id */
    private String taskid = "";

    /** 用于判断描述为暂无 */
    private String schedule = "";

    /** 用于判断相关人为暂无 */
    private String relevant = "";

    /** 用于判断跟进人为暂无 */
    private String follow_up = "";

    /** 用于判断责任人为暂无 */
    private String responsible = "";

    /** 更改进度 */
    private TextView txt_chageProgress;

    /** 回复通用的fragment */
    private CommentFragment fragment;

    /** 查看发布人详情 */
    @ViewInject(R.id.img_taskeventDetails_userTag)
    private ImageView img_userTag;

    /** 发布人头像 */
    @ViewInject(R.id.img_taskeventDetails_userheadPic)
    private ImageView img_headPic;

    /** 命名 */
    @ViewInject(R.id.txt_taskeventDetails_username)
    private TextView txt_fullname;

    /** 部门 */
    @ViewInject(R.id.txt_taskeventDetails_department)
    private TextView txt_dname;

    /** 延期 */
    @ViewInject(R.id.txt_taskeventDetails_postpone)
    private TextView txt_postpone;

    /** 发布者姓名 */
    @ViewInject(R.id.txt_taskevent_tasksdetails_fullname)
    private TextView txt_taskevent_tasksdetails_fullname;

    /** 整个可以滑动的scrollView，默认一进页面隐藏，数据加载完了以后显示 */
    @ViewInject(R.id.scroll_task_details_new_scrollView)
    private View scrollView;

    @ViewInject(R.id.linear_task_details_fileContainer)
    private LinearLayout linearFileContainer;

    @ViewInject(R.id.bottom_taskdetails__preliminary_view)
    private LinearLayout lyTaskdetailsPreliminary;
    @ViewInject(R.id.bottom_taskdetails__bottom_view)
    private LinearLayout lyTaskdetailsBottom;
    @ViewInject(R.id.edit_taskdetails_content)
    private EditText editTaskdetailsPreliminaryContent;
    @ViewInject(R.id.task_details_fromaction)
    private LinearLayout task_details_fromaction_ly;
    /**
     * 查看者是否有初审权限（1有0无）
     */
    @ViewInject(R.id.bottom_taskdetails__btn_preliminary)
    private Button btnTaskdetailsPreliminary;
    @ViewInject(R.id.bottom_taskdetails__btn_preliminary_submit)
    private Button btnTaskdetailsPreliminarySubmit;
    /** 附件的List */
    private List<DownloadFile> fileList;
    private String savePath = Constant.FileInfo.ROOT_PATH
            + Constant.FileInfo.SAVE_FILE_PATH;
    int type = 0;

    String userid = "";

    private boolean isTabTask = false;
    private boolean isTabHBTask = false;

    private boolean isReplyFirst = true;// 是否是第一次请求回复数据
    private boolean isReplyMore = false;// 是否请求更多回复数据
    private boolean isReplyReflash = false;// 是否刷新数据

    private int count = 0;// 回复总数

    private View mFootLayout;

    private TextView txt_More;

    private ProgressBar mProgressBar;

    private DiscussAdapter mDiscussAdapter;
    private BSListView mDiscussLv;
    private String comment;
    private String scheduleFromDB;
    private String commentid;
    /**
     * 查看者是否有添加进度权限（1有0无）
     */
    private String scheduleAdd;
    /**
     * 任务进度 状态 100%
     */
    private String scheduleStatus;

    /** 用来存放每一个正在进行的下载任务 */
    public static List<Map<String, Async>> listTask = new ArrayList<Map<String, Async>>();

    /**
     * 当有更多相关人时，直接替换掉，赶进度的权宜之计
     */
    private HeadAdapter4TaskDetails mTransferAdapter;

    /**
     * 查看者是否有初审权限（1有0无）
     */
    private String preliminary;
    private String preliminaryStatus;
    /**
     * 查看者是否有定审权限（1有0无）
     */
    private String finals;
    private String editTaskdetailsPreliminaryContentTxt;

    /**
     * 任务是否过期（1是，0不是）
     */
    private String overtime;

    /**
     * editEtime 查看者是否有编辑任务结束时间权限（1有0无）
     */
    private String editEtime;

    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 到期时间
     */
    private String endTime;

    private String mMessageid = "";

    private BroadcastReceiver schedule_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TaskActionReceiver.CHANGEDATE.equals(intent.getAction())) {
                txt_chageProgress.setVisibility(View.GONE);
                taskid = intent.getStringExtra("task_id");
                startTime = intent.getStringExtra("startTime");
                endTime = intent.getStringExtra("newEndTime");
                txt_taskevent_tasksDetails_StartTime.setText(CommonDateUtils
                        .parseDate(Long.parseLong(startTime) * 1000, "MM.dd")
                        + "至"
                        + CommonDateUtils.parseDate(
                                Long.parseLong(endTime) * 1000, "MM.dd"));
            } else {
                boolean change = intent.getBooleanExtra("change", false);
                taskid = intent.getStringExtra("task_id");
                scheduleStatus = intent.getStringExtra("schedule");
                if ("100".equalsIgnoreCase(scheduleStatus)) {
                    txt_chageProgress.setVisibility(View.GONE);
                }

                if (change) {
                    isReplyFirst = false;
                    isReplyMore = false;
                    isReplyReflash = false;

                    // requestNet();
                    getDataFromServerOnStart();
                }
            }
        }
    };

    @Override
    public void initView() {
        ((TextView) findViewById(R.id.txt_comm_head_activityName))
                .setText("任务详情");
        txt_chageProgress = (TextView) findViewById(R.id.txt_comm_head_right);
        findViewById(R.id.img_head_back).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        txt_chageProgress.setText("改进度");
        txt_chageProgress.setVisibility(View.GONE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;

        linear_common_arrow_threePoints.setOnClickListener(this);
        // relative_taskEventDetails_downLoadFujian.setOnClickListener(this);
        img_userTag.setOnClickListener(this);

        // 下面加载更多设置
        mFootLayout = LayoutInflater.from(context).inflate(
                R.layout.listview_bottom_more, null);
        txt_More = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        txt_More.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        listView_taskevent_taskDetails_pinglun.addFooterView(mFootLayout);

        mFootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_More.setText("正在加载...");
                mProgressBar.setVisibility(View.VISIBLE);

                isReplyMore = true;
                isReplyReflash = false;
                isReplyFirst = false;
            }
        });

        mDiscussLv = (BSListView) findViewById(R.id.bslistView_taskevent_taskDetails_pinglun);

        mDiscussAdapter = new DiscussAdapter(this, taskid, fragment,
                txt_taskevent_taskDetails_pinglunNum);
        mDiscussLv.setAdapter(mDiscussAdapter);
        mDiscussAdapter.setAgreeUrl(BSApplication.getInstance().getHttpTitle()
                + Constant.TaskEventPath.TASKEVENTMAKEPRAISE_PATH);
        mDiscussAdapter.setOpposeUrl(BSApplication.getInstance().getHttpTitle()
                + Constant.TaskEventPath.TASKEVENTMAKEDECLINE_PATH);
        mDiscussAdapter.setContentUrl(BSApplication.getInstance()
                .getHttpTitle()
                + Constant.TaskEventPath.TASKEVENTREPLY_PATH_NOSTART);
        mDiscussAdapter.setType(3);

    }

    public void dataClear() {
        datas_details.clear();
        datas_description.clear();
        datas_genjin.clear();
        datas_fuzhe.clear();

        datas_relevant.clear();// 相关人清除
        if (adapter_gridView != null)
            adapter_gridView.notifyDataSetChanged();

        /*
         * datas_pinlun.clear();// 回复评论清除 if (adapterReply != null)
         * adapterReply.notifyDataSetChanged();
         */

        linear_taskevent_taskDetails_detailsDescription.removeAllViews();
    }

   
    /** 右边是改进度还是确认 */
    public void rightClick() {
        txt_chageProgress.setText("");
        if (datas_details.size() != 0 && datas_details != null) {
            String pro = datas_details.get(0).getSchedule();
            int c = datas_fuzhe.size();
            if ("100".equals(pro)) {
                if (datas_fuzhe.size() > 0) {
                    RelativePepoleVO fu_people = datas_fuzhe.get(0);
                    String status = datas_details.get(0).getStatus();

                    if (userid.equals(fu_people.getUserid())
                            && "0".equals(status)) {
                        txt_chageProgress.setText("初审");
                    }
                    if (userid.equals(datas_details.get(0).getUserid())
                            && "1".equals(status)) {
                        txt_chageProgress.setText("定审");
                    }
                }
            } else {

                if (datas_genjin.size() > 0) {
                    RelativePepoleVO rel_people = datas_genjin.get(0);
                    if (userid.equals(rel_people.getUserid())) {
                        txt_chageProgress.setText("改进度");
                    }
                }

            }

        }

        txt_chageProgress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (datas_details.size() != 0 && datas_details != null) {
                    String name = txt_chageProgress.getText().toString().trim();
                    Intent intent = new Intent();
                    intent.putExtra("type", type);
                    intent.putExtra("isTabHBTask", isTabHBTask);
                    intent.putExtra("isTabTask", isTabTask);
                    // if ("初审".equals(name)) {
                    //
                    // intent.setClass(context, TaskConfirmActivity.class);
                    // // intent.putExtra("userid",);
                    // intent.putExtra("taskid",
                    // datas_details.get(0).getTaskid());
                    // intent.putExtra("status",
                    // datas_details.get(0).getStatus());
                    //
                    // } else if ("定审".equals(name)) {
                    //
                    // intent.setClass(context, TaskConfirmActivity.class);
                    // // intent.putExtra("userid",);
                    // intent.putExtra("taskid",
                    // datas_details.get(0).getTaskid());
                    // intent.putExtra("status",
                    // datas_details.get(0).getStatus());
                    //
                    // } else if ("改进度".equals(name)) {
                    //
                    intent.setClass(context, EXTSelectPercentActivity.class);
                    intent.putExtra("schedule", datas_details.get(0)
                            .getSchedule());
                    intent.putExtra("id", taskid);

                    //
                    // }
                    startActivity(intent);
                }

            }
        });
    }

    public void sendBoardMessage() {
        Intent intent = new Intent(TaskActionReceiver.intentFilter);
        intent.putExtra("type", type);
        intent.putExtra("task_id", taskid);
        intent.putExtra("isTabHBTask", isTabHBTask);
        intent.putExtra("isTabTask", isTabTask);
        /** 1 我发布的 2 我负责的 3 我跟进的 4 我跟进的 */
        sendBroadcast(intent);

    }

   
 
    protected void fillData() {

        if (fangHuiDiao)// 第一次必须走完foucus方法，第二次因为fangHuiDiao为true所以就直接调用填充进度
            fillColor(width_line, imgTag_width);

        TaskEventItem taskEventItem = datas_details.get(0);

        // 发布人信息
        if (Constant.nil.equals(taskEventItem.getHeadpic())) {
            img_headPic.setImageResource(R.drawable.ic_default_portrait_s);
        } else {
            imageLoader.displayImage(taskEventItem.getHeadpic(), img_headPic,
                    options, new ImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                FailReason failReason) {
                        }

                        @Override
                        public void onLoadingComplete(String imageUri,
                                View view, Bitmap loadedImage) {
                            Bitmap bitmap = CommonImageUtils
                                    .toRoundBitmap(loadedImage);

                            ImageView imageView = (ImageView) view;

                            imageView.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri,
                                View view) {
                        }
                    });
        }

        txt_fullname.setText(taskEventItem.getFullname());
        txt_dname.setText(taskEventItem.getDname() + "/"
                + taskEventItem.getPname());

        // 改进度的权限
        /*
         * if ("0".equals(taskEventItem.getPurview())) {// 无权限
         * txt_chageProgress.setVisibility(View.GONE); } else if
         * ("1".equals(taskEventItem.getPurview())) {// 有权限
         * txt_chageProgress.setVisibility(View.VISIBLE); }
         */
        txt_taskevent_tasksDetails_title.setText(taskEventItem.getTitle());

        startTime = CommonDateUtils.parseDate(
                taskEventItem.getStarttime() * 1000, "yyyy-MM-dd HH:mm");
        endTime = CommonDateUtils.parseDate(taskEventItem.getEndtime() * 1000,
                "yyyy-MM-dd HH:mm");
        txt_taskevent_tasksDetails_StartTime.setText("开始时间：" + startTime);
        txt_taskevent_tasksDetails_endTime.setText("到期时间：" + endTime);

        // 是否延期
        if (System.currentTimeMillis() / 1000 > taskEventItem.getEndtime()
                && !"100".equals(taskEventItem.getSchedule())) {

            txt_postpone.setVisibility(View.VISIBLE);
        } else {

            txt_postpone.setVisibility(View.GONE);
        }

        txt_taskevent_tasksDetails_details.setText(taskEventItem.getContent());

        // 没有附件时隐藏布局
        if ("0".equals(taskEventItem.getAnnex_size())) {
            // relative_taskEventDetails_downLoadFujian.setVisibility(View.GONE);
            line_noFujian.setVisibility(View.GONE);
        }

        // 描述为暂无时隐藏布局
        if (Constant.nil.equals(schedule)) {
            txt_itemTitle.setVisibility(View.GONE);
            relative_percent.setVisibility(View.GONE);
            view_lineTop.setVisibility(View.GONE);
        } else {
            txt_itemTitle.setVisibility(View.VISIBLE);
            relative_percent.setVisibility(View.VISIBLE);
            view_lineTop.setVisibility(View.VISIBLE);
        }

        // 动态添加进度描述
        // detailsChangePercent(mTEDV_InfoVO);
        // // 跟进人
        // setFollow_up_view(mTEDV_InfoVO);
        //
        // // 责任人
        // setResponsible_view(mTEDV_InfoVO);

        // 展示进度的图片标签
        imgTag_width = img_taskevent_taskDetails_percentPoint.getWidth();
        if (datas_details.size() > 0)
            fillColor(width_line, imgTag_width);

        // 相关人为暂无时隐藏布局
        if (Constant.nil.equals(relevant)) {
            txt_pepole.setVisibility(View.GONE);
            relative_pepole.setVisibility(View.GONE);
        }

        if (datas_relevant.size() != 0 && datas_relevant != null)
            hasData = true;

    }

    protected void fillDataReplay() {
        if (datas_pinlun.size() != 0) {
            txt_taskevent_taskDetails_pinglunNum.setText("回复(" + count + ")");
            txt_taskevent_taskDetails_pinglunNum.setVisibility(View.VISIBLE);
            adapterReply = new ListViewReplyAdapter(context, datas_pinlun);
            listView_taskevent_taskDetails_pinglun.setAdapter(adapterReply);
            listView_taskevent_taskDetails_pinglun.setVisibility(View.VISIBLE);

            if (isReplyReflash) {
                // listView_publicNoticeDocument.setSelection(listView_publicNoticeDocument.getCount()
                // - 1);
                CustomToast.showShortToast(context, "回复成功");
            }
        } else {
            txt_taskevent_taskDetails_pinglunNum.setVisibility(View.GONE);
            listView_taskevent_taskDetails_pinglun.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.img_taskeventDetails_userTag:// 查看个人详情

                if (datas_details != null && datas_details.size() != 0) {
                    if (Constant.nil.equals(datas_details.get(0).getUserid())) {
                        CustomToast.showShortToast(context, "此客户没有信息！");
                        return;
                    }

                    // intent.putExtra("personid",
                    // datas_details.get(0).getUserid());
                    // intent.setClass(context, EmployeeDetailActivity.class);
                    // startActivity(intent);
                }

                break;
            case R.id.linear_common_arrow_threePoints:// 更多

                // flag_back = false;// 为了返回页面时不再onStart里面进行请求

                // intent.putExtra("activityTitle", "相关人");
                // intent.putExtra("pepoleList", (Serializable) datas_relevant);
                // intent.setClass(context, GeneralPeopleShowActivity.class);
                // openActivity(intent);
                common_myGridViewOfClass_full.setAdapter(mTransferAdapter);
                linear_common_arrow_threePoints.setVisibility(View.GONE);
                break;

        // case R.id.relative_taskEventDetails_downLoadFujian:// 下载附件
        // if (datas_details.size() != 0 && datas_details != null) {
        // if (!"0".equals(datas_details.get(0).getAnnex_size())) {
        // String urlFile = datas_details.get(0).getAnnex();
        // downLoadAnnex(urlFile);
        // }
        // }
        // break;
        }
    }

    /**
     * 下载附件
     * 
     * @throws DbException
     */
    private void downLoadAnnex(String annex) throws DbException {
        CustomDialog.showProgressDialog(context, "正在下载附件");

        String savePath = Constant.FileInfo.ROOT_PATH
                + Constant.FileInfo.SAVE_FILE_PATH
                + CommFileUtils.getFileNameCloudSuffix(annex);

        // HttpUtils httpUtils = new HttpUtils();

        RequestParams pamars = new RequestParams();

        DownloadManager.getInstance().startDownload(annex,
                CommFileUtils.getFileNameCloudSuffix(annex), savePath, true, true,
                new DownloadViewHolder(new DownloadInfo()) {

                    @Override
                    public void onWaiting() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onSuccess(File result) {
                        // TODO Auto-generated method stub
                        CustomToast.showShortToast(context, "此文件已下载成功位于"
                                + result.getPath());

                        path = result.getPath();
                        // 当下载成功后此布局隐藏
                        // relative_taskEventDetails_downLoadFujian.setVisibility(View.GONE);
                        line_noFujian.setVisibility(View.GONE);

                        CustomDialog.closeProgressDialog();
                    }

                    @Override
                    public void onStarted() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onLoading(long total, long current) {
                        // TODO Auto-generated method stub
                        Log.e("tga", total + "");
                        Log.e("tga", current + "");
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        // TODO Auto-generated method stub
                        CustomDialog.closeProgressDialog();
                        CustomToast.showShortToast(context, "此文件可能已存在你设备中");
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        // TODO Auto-generated method stub

                    }
                });
        //
        // // 第一个true代表如果目标文件存在，接着未完成的部分继续下载。 第二个true代表如果从请求返回信息中获取到文件名，下载完成后自动重命名。
        // HttpHandler httpHandler = httpUtils.download(annex, savePath, true,
        // false, new RequestCallBack<File>() {
        // @Override
        // public void onSuccess(ResponseInfo<File> arg0) {
        // CustomToast.showShortToast(context, "此文件已下载成功位于"
        // + arg0.result.getPath());
        //
        // path = arg0.result.getPath();
        // // 当下载成功后此布局隐藏
        // // relative_taskEventDetails_downLoadFujian.setVisibility(View.GONE);
        // line_noFujian.setVisibility(View.GONE);
        //
        // CustomDialog.closeProgressDialog();
        // }
        //
        // @Override
        // public void onFailure(HttpException arg0, String arg1) {
        //
        // CustomDialog.closeProgressDialog();
        // CustomToast.showShortToast(context, "此文件可能已存在你设备中");
        // }
        //
        // @Override
        // public void onLoading(long total, long current,
        // boolean isUploading) {
        // // CustomToast.showShortToast(context,current + "/" +
        // // total);
        // }
        // });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && !fangHuiDiao) {
            fangHuiDiao = true;

            width_line = linear_taskevent_taskDetails_progress.getWidth();

            // 展示进度的图片标签
            imgTag_width = img_taskevent_taskDetails_percentPoint.getWidth();
            // if (datas_details.size() > 0)
            fillColor(width_line, imgTag_width);
            CustomLog.e(TAG, "" + width_line);
            CustomLog.e(TAG, "" + imgTag_width);
            // fillColor(600, 13);

            // 通过分辨率来决定放多少张图片(相关人)
            if (hasData) {// 当有相关人的时候才可以进入这方法

                width_gridView = common_myGridViewOfClass_full.getWidth();

                int dip2px = WindowUtils.dip2px(context, 60);
                int nm = width_gridView / dip2px;
                List<RelativePepoleVO> newDatas = new ArrayList<RelativePepoleVO>();

                for (int i = 0; i < Math.min(nm, datas_relevant.size()); i++) {
                    newDatas.add(datas_relevant.get(i));
                }

                /*
                 * if (nm < 5) { common_myGridViewOfClass_full.setNumColumns(5); } else {
                 * common_myGridViewOfClass_full.setNumColumns(nm); }
                 */
                common_myGridViewOfClass_full.setNumColumns(nm);
                // adapter_gridView = new MyGridViewNoTagAdapter(context,
                // newDatas);
                common_myGridViewOfClass_full.setAdapter(adapter_gridView);
            }

        }

    }

    /**
     * 设置对应的百分比所展示的颜色以及点和文字的位置
     */
    private void fillColor(int width_line, int imgTag_width) {

        if (TextUtils.isEmpty(scheduleFromDB)) {
            return;
        }
        // int Percents = Integer.valueOf(datas_details.get(0).getSchedule());
        int Percents = Integer.valueOf(scheduleFromDB);

        float percentValue = (float) Percents / 100;
        // float percentValue = (float) 20 / 100;

        // textView颜色填充
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) Math.round(percentValue * width_line),
                LayoutParams.FILL_PARENT);
        txt_taskevent_taskDetails_fillColor.setLayoutParams(params);

        // 标记图片
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params1.setMargins((int) Math.round(percentValue * width_line), 0, 0, 0);

        if (percentValue == 1f) {

            params1.setMargins(
                    (int) Math.round(percentValue * width_line - width * 4
                            / 275), 0, 0, 0);
            img_taskevent_taskDetails_percentPoint.setVisibility(View.GONE);
        } else {

            params1.setMargins(
                    (int) Math.round(percentValue * width_line - imgTag_width
                            / 2), 0, 0, 0);
            img_taskevent_taskDetails_percentPoint.setVisibility(View.VISIBLE);
        }

        img_taskevent_taskDetails_percentPoint.setLayoutParams(params1);

        // 百分比的展示
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (percentValue == 0f) {

            params2.setMargins((int) Math.round(percentValue * width_line), 0,
                    0, 0);
        } else if (percentValue == 1f) {

            params2.setMargins((int) Math.round(percentValue * width_line
                    - width * 1 / 30), 0, 0, 0);
        } else {

            params2.setMargins((int) Math
                    .round(percentValue * width_line /*- width/30*/), 0, 0, 0);
        }

        txt_taskevent_taskDetails_percentColor.setLayoutParams(params2);// 位置设置

        txt_taskevent_taskDetails_percentColor.setText(Math
                .round(percentValue * 100) + "%");
    }

    /*
     * 这里的key必须是ReceiverAction.Key, value就是你注册广播时候的action,
     * 手动在org.baiteng.oa.setting.ReceiverAction中添加此activity的action
     */
    protected void initChatView() {
        fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        // bundle.putString(ReceiverActionBSVO.DocKey,
        // ReceiverActionBSVO.TaskEventDetailsAction);
        bundle.putString(Constant.DocKey, Constant.NoticeDetailsAction);
        fragment.setArguments(bundle);

        // 显示界面
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.bottom_view, fragment);
        transaction.commit();

        // 注册广播
        IntentFilter filter = new IntentFilter(
                ReceiverActionBSVO.TaskEventDetailsAction);
        registerReceiver(receiver, filter);

        // 注册进度广播
        IntentFilter schedule_filter = new IntentFilter(
                TaskScheduleReceiver.intentFilter);
        schedule_filter.addAction(TaskActionReceiver.CHANGEDATE);
        registerReceiver(schedule_receiver, schedule_filter);

        // 注册下载广播
        IntentFilter download_filter = new IntentFilter(
                FileDownloadSuccessReceiver.receiverAction);
        registerReceiver(downloadReceiver, download_filter);

    }

    /**
     * 发送文本消息
     * 
     * @param content message content
     * @param isResend boolean resend
     */
    private void sendText(final String content) {
        String url = Constant.TaskEventPath.TaskEventReply_path;
        if (content.length() > 0) {
            Log.d("taskid to send text", taskid);
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("uid", BSApplication.getInstance().getUserId());
            paramsMap.put("ftoken", BSApplication.getInstance().getmCompany());
            paramsMap.put("content", content);
            paramsMap.put("id", taskid);
            paramsMap.put("lcid", "0");
            if (!TextUtils.isEmpty(commentid)) {
                paramsMap.put("lcid", commentid);
                url = BSApplication.getInstance().getHttpTitle()
                        + Constant.TaskEventPath.TASKEVENTREPLY_PATH_NOSTART;
            }
            paramsMap.put("isanonymous", "0");
            paramsMap.put("soundlength", "0");
            paramsMap.put("sort", "0");
            new HttpUtilsByPC().sendPostBYPC(url, paramsMap,
                    new RequestCallBackPC() {
                        // httpUtils.send(HttpRequest.HttpMethod.POST,
                        // Constant.TaskEventPath.TaskEventReply_path, params,
                        // new RequestCallBack<String>() {
                        @Override
                        // 上传失败处理方法
                        public void onFailurePC(HttpException arg0, String msg) {
                            // alert(msg);
                            fragment.errorOpenSendClickListener();
                        }

                        @Override
                        // 上传成功处理
                        public void onSuccessPC(ResponseInfo responseInfo) {
                            String result = (String) responseInfo.result;
                            String system_time = "";
                            // "code": "200",
                            // "retinfo": "添加任务进度成功！",
                            // "system_time": 1432524933
                            try {
                                JSONObject object = new JSONObject(result);
                                if (object.has("code")
                                        && object.has("system_time")) {
                                    String code =object.getString("code");
                                    if (Constant.RESULT_CODE
                                            .equalsIgnoreCase(code)) {
                                        setComment_view();
                                    }
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });
            // listView_publicNoticeDocument.setSelection(listView_publicNoticeDocument.getCount()
            // - 1);
            // mEditTextContent.setText("");

        }
    }

    /**
     * 发送语音
     * 
     * @param filePath
     * @param fileName
     * @param length
     * @param isResend
     */
    private void sendVoice(String filePath, String length) {
        if (!(new File(filePath).exists())) {
            return;
        }
        // ReplyItem item=new
        // ReplyItem("芳芳",String.valueOf(System.currentTimeMillis()/1000),null,filePath,Integer.parseInt(length),1);
        // datas_pinlun.add(item);
        RequestParams params = new RequestParams(Constant.TaskEventPath.TaskEventReply_path);
        params.addBodyParameter("sort", "1");
        params.addBodyParameter("taskid", taskid);
        params.addBodyParameter("userid", BSApplication.getInstance()
                .getUserId());
        params.addBodyParameter("ftoken", BSApplication.getInstance()
                .getmCompany());
        params.addBodyParameter("soundlength", length);
        params.addBodyParameter("content", "jh");
        params.addBodyParameter("voicefile", new File(filePath));

        x.http().post(params, new CommonCallback<String>() {

            @Override
            public void onCancelled(CancelledException arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                Log.d("voice reply failure", arg1 + "");
            }

            @Override
            public void onFinished() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSuccess(String arg0) {
                // TODO Auto-generated method stub
                isReplyFirst = false;
                isReplyMore = false;
                isReplyReflash = true;
            }
        });

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.hasExtra("content")) {
                String stringExtra = intent.getStringExtra("content");
                String length = intent.getStringExtra("length");
                commentid = intent.getStringExtra("commentid");
                DiscussVO mDiscussVO = (DiscussVO) intent
                        .getSerializableExtra("mDiscussVO");
                Log.d("content:" + stringExtra, ",length : " + length);
                if (length.equals("-1")) {
                    sendText(stringExtra);
                } else {
                    sendVoice(stringExtra, length);
                }
            }

        }

    };

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String path = intent.getStringExtra("path");
            for (int i = 0; i < linearFileContainer.getChildCount(); i++) {
                View childAt = linearFileContainer.getChildAt(i);
                TextView fileName = (TextView) childAt
                        .findViewById(R.id.txt_item_task_detail_annex_fileName);
                String tagPath = (String) fileName.getTag();
                // if (tagPath.equals(path)) {
                // ImageView downloadIcon = (ImageView) childAt
                // .findViewById(R.id.img_task_detail_annex_downloadIcon);
                // downloadIcon
                // .setBackgroundResource(R.drawable.ic_task_openfile);
                // downloadIcon.setTag(true);
                // CustomToast.showShortToast(context, path);
                // return;
                // }
                String filename = savePath + fileName.getText().toString();
                if (filename.equals(path)) {
                    File file = new File(path);
                    if (file.exists()) {
                        MasterLayout mMasterLayout = (MasterLayout) childAt
                                .findViewById(R.id.masterlayout_task_detail_annex);
                        mMasterLayout.setVisibility(View.GONE);
                        ImageView downloadIcon = (ImageView) childAt
                                .findViewById(R.id.img_task_detail_annex_downloadIcon);
                        downloadIcon
                                .setBackgroundResource(R.drawable.ic_task_openfile);
                        downloadIcon.setTag(true);
                        downloadIcon.setVisibility(View.VISIBLE);
                    }
                    CustomDialog.closeProgressDialog();
                    return;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        unregisterReceiver(schedule_receiver);
        unregisterReceiver(downloadReceiver);
        super.onDestroy();
        mDiscussAdapter.stopSound();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // adapter.refresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 返回
     * 
     * @param view
     */
    public void back(View view) {
        finish();
    }

    /**
     * 覆盖手机返回键
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // TaskListActivity.flag_back = true;
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
    	View.inflate(this, R.layout.ac_taskevent_task_details_new, mContentLayout);
        x.view().inject(this);

        SharedPreferences mSettings = PreferenceManager
                .getDefaultSharedPreferences(this);
        LoginUser loginUser = UserManager.getLoginUser(mSettings);
        userid = loginUser.getUserid();

        initChatView();

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_default_portrait_s)
                // 默认图片
                .showImageForEmptyUri(R.drawable.ic_default_portrait_s)
                // 空url的图片
                .showImageOnFail(R.drawable.ic_default_portrait_s)
                .cacheInMemory().cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        Intent intent = getIntent();
        if (intent.hasExtra(ExtrasBSVO.Push.BREAK_ID))
            taskid = intent.getStringExtra(ExtrasBSVO.Push.BREAK_ID);

        type = intent.getIntExtra("type", 0);

        isTabHBTask = getIntent().getBooleanExtra("isTabHBTask", false);
        isTabTask = getIntent().getBooleanExtra("isTabTask", false);
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
    public void bindViewsListener() {
        // TODO Auto-generated method stub btnTaskdetailsPreliminary
        final InputMethodManager inputManager = (InputMethodManager) editTaskdetailsPreliminaryContent
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        btnTaskdetailsPreliminary.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lyTaskdetailsPreliminary.setVisibility(View.VISIBLE);
                btnTaskdetailsPreliminary.setVisibility(View.GONE);
                editTaskdetailsPreliminaryContent.requestFocus();

                inputManager
                        .showSoftInput(editTaskdetailsPreliminaryContent, 0);
            }
        });
        btnTaskdetailsPreliminarySubmit
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editTaskdetailsPreliminaryContentTxt = editTaskdetailsPreliminaryContent
                                .getText().toString();
                        // 非空判断
                        if (TextUtils
                                .isEmpty(editTaskdetailsPreliminaryContentTxt)) {
                            CustomToast.showShortToast(context, "说点什么吧···");
                            return;
                        }
                        submitPreliminary1();
                    }
                });
        txt_chageProgress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // if (datas_details.size() != 0 && datas_details != null) {
                String name = txt_chageProgress.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("type", type);
                intent.putExtra("isTabHBTask", isTabHBTask);
                intent.putExtra("isTabTask", isTabTask);
                if ("初审".equals(name)) {

                    // intent.setClass(context, TaskConfirmActivity.class);
                    // intent.putExtra("userid",);
                    // intent.putExtra("taskid",
                    // datas_details.get(0).getTaskid());
                    // intent.putExtra("status",
                    // datas_details.get(0).getStatus());

                } else if ("定审".equals(name)) {

                    // intent.setClass(context, TaskConfirmActivity.class);
                    // intent.putExtra("userid",);
                    // intent.putExtra("taskid",
                    // datas_details.get(0).getTaskid());
                    // intent.putExtra("status",
                    // datas_details.get(0).getStatus());

                } else if ("改进度".equals(name)) {

                    intent.setClass(context, EXTSelectPercentActivity.class);
                    // intent.putExtra("schedule", taskid);
                    intent.putExtra("schedule", scheduleFromDB);
                    intent.putExtra("id", taskid);

                } else if ("改时间".equals(name)) {
                    intent.setClass(context, EXTTaskChangeDateActivity.class);
                    intent.putExtra("startTime", startTime);
                    intent.putExtra("endTime", endTime);
                    intent.putExtra("id", taskid);
                }
                startActivity(intent);
                // }

            }
        });
    }

   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getDataFromServerOnStart();
    }

    private void getDataFromServerOnStart() {
        CustomDialog.showProgressDialog(context);

        if (TextUtils.isEmpty(taskid)) {
            Intent intent = getIntent();
            if (intent.hasExtra(ExtrasBSVO.Push.BREAK_ID))
                taskid = intent.getStringExtra(ExtrasBSVO.Push.BREAK_ID);
        }

        if (getIntent().getStringExtra("messageid") != null) {
            mMessageid = getIntent().getStringExtra("messageid");
        }

        String url = BSApplication.getInstance().getHttpTitle()
                + Constant4TaskEventPath.TASKEVENTDETAILS_PATH;
        // long lon = System.currentTimeMillis();

        Map<String, String> paramsMap = new HashMap<String, String>();
        try {
            // CommonUtils.setBodyParams(paramsMap,
            // "ftoken",BSApplication.getInstance()
            // .getmCompany());
            // paramsMap.put("ftoken",
            // "RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O");
            paramsMap.put("ftoken", BSApplication.getInstance().getmCompany());
            String username = BSApplication.getInstance().getUserId();
            // username = "22";
            paramsMap.put("uid", username);
            paramsMap.put("userid", username);
            paramsMap.put("id", taskid);
            paramsMap.put("messageid", mMessageid);
        } catch (Exception e1) {
            CustomDialog.closeProgressDialog();
            CustomToast.showShortToast(context, "获取任务失败");
            e1.printStackTrace();
        }

        new HttpUtilsByPC().sendPostBYPC(url, paramsMap,
                new RequestCallBackPC() {

                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        CustomDialog.closeProgressDialog();
                        scrollView.setVisibility(View.GONE);
                        task_details_fromaction_ly.setVisibility(View.GONE);
                        CustomToast.showShortToast(context, "网络似乎断开了哦");
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo arg0) {
                        // TODO Auto-generated method stub
                        String result = (String) arg0.result;
                        scrollView.setVisibility(View.VISIBLE);
                        task_details_fromaction_ly.setVisibility(View.VISIBLE);
                        try {
                            Gson gson = new Gson();
                            TaskEventDetailsViewVO taskEventDetailsViewVO = gson
                                    .fromJson(result,
                                            TaskEventDetailsViewVO.class);
                            String code = taskEventDetailsViewVO.getCode();
                            TaskEventDetailsView_InfoVO mTEDV_InfoVO = null;
                            if (code.equalsIgnoreCase(Constant.RESULT_CODE)) {
                                CommonUtils.sendBroadcast(context, Constant.HOME_MSG);
                                mTEDV_InfoVO = taskEventDetailsViewVO.getInfo();
                                scheduleStatus = mTEDV_InfoVO.getSchedule();
                                /**
                                 * 修改进度的权限
                                 */
                                scheduleAdd = mTEDV_InfoVO.getScheduleAdd();
                                if (!TextUtils.isEmpty(scheduleAdd)) {
                                    // if ("1".equalsIgnoreCase(scheduleAdd)) {
                                    if ("1".equalsIgnoreCase(scheduleAdd)
                                            && !"100"
                                                    .equalsIgnoreCase(scheduleStatus)) {
                                        txt_chageProgress
                                                .setVisibility(View.VISIBLE);
                                    }
                                }
                                /**
                                 * 查看者是否有初审权限（1有0无）
                                 */
                                preliminary = mTEDV_InfoVO.getPreliminary();
                                if (!TextUtils.isEmpty(preliminary)) {
                                    if ("1".equalsIgnoreCase(preliminary)
                                            && "100".equalsIgnoreCase(scheduleStatus)) {
                                        btnTaskdetailsPreliminary
                                                .setVisibility(View.VISIBLE);
                                        lyTaskdetailsBottom
                                                .setVisibility(View.GONE);
                                        preliminaryStatus = "1";
                                    }
                                }

                                /**
                                 * 查看者是否有定审权限（1有0无）
                                 */
                                finals = mTEDV_InfoVO.getFinals();
                                if (!TextUtils.isEmpty(finals)) {
                                    if ("1".equalsIgnoreCase(finals)
                                            && "100".equalsIgnoreCase(scheduleStatus)) {
                                        setPreliminarySubmit();
                                    }
                                }

                                /**
                                 * 改时间
                                 */
                                editEtime = mTEDV_InfoVO.getEditEtime();
                                overtime = mTEDV_InfoVO.getOvertime();
                                if (!TextUtils.isEmpty(editEtime)) {
                                    if ("1".equalsIgnoreCase(editEtime)) {
                                        if (!TextUtils.isEmpty(overtime)) {
                                            if ("1".equalsIgnoreCase(overtime)) {
                                                txt_chageProgress
                                                        .setText("改时间");
                                                txt_chageProgress
                                                        .setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }
                                }

                                txt_taskevent_tasksDetails_title
                                        .setText(mTEDV_InfoVO.getTitle());

                                txt_taskevent_tasksdetails_fullname
                                        .setText("发布人："
                                                + mTEDV_InfoVO.getFullname());
                                // 开始与结束时间
                                txt_taskevent_tasksDetails_StartTime.setText("有效时间:"
                                        + DateUtils.parseDateDay(mTEDV_InfoVO.getStarttime() + "")
                                        + "至"
                                        + DateUtils.parseDateDay(mTEDV_InfoVO.getEndtime() + ""));

                                startTime = mTEDV_InfoVO.getStarttime() + "";
                                endTime = mTEDV_InfoVO.getEndtime() + "";
                                // txt_taskevent_tasksDetails_details
                                // .setText(new
                                // WebView(context).loadDataWithBaseURL(null,mTEDV_InfoVO.getContent(),"text/html",
                                // "UTF-8", null));
                                // txt_taskevent_tasksDetails_details
                                // .setText(Html.fromHtml(mTEDV_InfoVO.getContent()));
                                WebView nWebView = new WebView(context);
                                nWebView.clearHistory();
                                nWebView.clearFormData();
                                nWebView.getSettings().setJavaScriptEnabled(
                                        true);
                                nWebView.loadDataWithBaseURL(null,
                                        mTEDV_InfoVO.getContent(), "text/html",
                                        "UTF-8", null);
                                ly_taskevent_tasksDetails_details.removeAllViews();
                                ly_taskevent_tasksDetails_details
                                        .addView(nWebView);

                                txt_taskevent_taskDetails_percentColor
                                        .setText(Double
                                                .parseDouble(mTEDV_InfoVO
                                                        .getSchedule())
                                                + "%");

                                width_line = linear_taskevent_taskDetails_progress
                                        .getWidth();

                                // 展示进度的图片标签
                                imgTag_width = img_taskevent_taskDetails_percentPoint
                                        .getWidth();
                                // if (datas_details.size() > 0)
                                scheduleFromDB = mTEDV_InfoVO.getSchedule();
                                fillColor(width_line, imgTag_width);
                                // fillColor(600, 13);

                                // 动态添加进度描述
                                // detailsChangePercent(mTEDV_InfoVO);
                                detailsChangePercentScheduleReply(mTEDV_InfoVO);
                                // 跟进人
                                setFollow_up_view(mTEDV_InfoVO);

                                // 责任人
                                setResponsible_view(mTEDV_InfoVO);
                                // 责任人
                                setRelevant_view(mTEDV_InfoVO);

                                // fillColor(20,40);

                                /**
                                 * 对是否有评论进行处理
                                 */
                                comment = mTEDV_InfoVO.getComment();
                                // mTEDV_InfoVO.gets
                                if (!TextUtils.isEmpty(comment)) {
                                    txt_taskevent_taskDetails_pinglunNum
                                            .setText("回复(" + comment + ")");
                                    txt_taskevent_taskDetails_pinglunNum
                                            .setVisibility(View.VISIBLE);
                                    // mTEDV_InfoVO.getComment();
                                    setComment_view();
                                }

                                /**
                                 * 附件 bypc
                                 */
                                setDownloadFile_view(mTEDV_InfoVO);
                            } else {
                                // return;
                            }
                            CustomDialog.closeProgressDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                            CustomDialog.closeProgressDialog();
                        }
                    }
                });
    }

    // 动态添加进度描述
    private void detailsChangePercent(TaskEventDetailsView_InfoVO mTEDV_InfoVO) {
        for (int i = 0; i < datas_description.size(); i++) {

            final TaskEventScheduleDescriptionItem descriptionItem = datas_description
                    .get(i);
            View view = LayoutInflater.from(context).inflate(
                    R.layout.part_taskevent_tasks_details_descrption, null);
            TextView txt_precent = (TextView) view
                    .findViewById(R.id.txt_taskevent_taskDetails_percent);
            TextView txt_descrption = (TextView) view
                    .findViewById(R.id.txt_taskevent_taskDetails_descrption);
            TextView txt_time = (TextView) view
                    .findViewById(R.id.txt_taskevent_taskDetails_time);
            ImageView img_replay = (ImageView) view
                    .findViewById(R.id.img_taskevent_taskDetails_replay);
            View line = (View) view
                    .findViewById(R.id.line_taskevent_taskDetails_line);

            // 最后一条进度描述时去掉下面的线
            if (i == datas_description.size() - 1) {
                line.setVisibility(View.GONE);
            }

            final int j = i;
            img_replay.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    // Intent intent = new Intent();
                    // intent.putExtra("tsid", descriptionItem.getTsid());
                    datas_description.get(j).getSchedule();
                    String strChange = "评论#"
                            + datas_description.get(j).getSchedule() + "%";
                    fragment.setFrontText(strChange);
                }
            });

            txt_precent.setText(descriptionItem.getSchedule() + "%");
            txt_descrption.setText(descriptionItem.getDescription().trim());
            txt_time.setText(CommonDateUtils.parseDate(
                    descriptionItem.getTime() * 1000, "MM-dd HH:mm"));

            linear_taskevent_taskDetails_detailsDescription.addView(view);
        }
    }

    // 跟进人
    private void setFollow_up_view(TaskEventDetailsView_InfoVO mTEDV_InfoVO) {
        // mTEDV_InfoVO.getFollow_up();
        // tedv_infoVO.getRelevant();
        // mTEDV_InfoVO.getResponsible();
        // RelativePepoleVO genItem = datas_genjin.get(0);
        TaskEventDetailsView_Info_RelevantVO genItem = mTEDV_InfoVO
                .getFollow_up();
        img_taskevent_taskDetails_protait_gen.setUserId(genItem.getUserid());//获取头像对应的用户ID,给头像点击事件
        img_taskevent_taskDetails_protait_gen.setUserName(genItem.getFullname());
        img_taskevent_taskDetails_protait_gen.setUrl(genItem.getHeadpic());
        if (Constant.nil.equals(genItem.getHeadpic())) {
            img_taskevent_taskDetails_protait_gen
                    .setImageResource(R.drawable.ic_default_portrait_s);
        } else {
            ImageLoader.getInstance().displayImage(genItem.getHeadpic(),
                    img_taskevent_taskDetails_protait_gen,
                    CommonUtils.initImageLoaderOptions());
        }
        txt_taskevent_taskDetails_protait_gen.setText(genItem.getFullname());
    }

    // 责任人
    private void setResponsible_view(TaskEventDetailsView_InfoVO mTEDV_InfoVO) {
        TaskEventDetailsView_Info_RelevantVO fuItem = mTEDV_InfoVO
                .getResponsible();
        img_taskevent_taskDetails_protait_zhe.setUserId(fuItem.getUserid());//获取头像对应的用户ID,给头像点击事件
        img_taskevent_taskDetails_protait_zhe.setUserName(fuItem.getFullname());
        img_taskevent_taskDetails_protait_zhe.setUrl(fuItem.getHeadpic());
        // RelativePepoleVO fuItem = datas_fuzhe.get(0);
        if (Constant.nil.equals(fuItem.getHeadpic())) {
            img_taskevent_taskDetails_protait_zhe
                    .setImageResource(R.drawable.ic_default_portrait_s);
        } else {
            ImageLoader.getInstance().displayImage(fuItem.getHeadpic(),
                    img_taskevent_taskDetails_protait_zhe,
                    CommonUtils.initImageLoaderOptions());
        }
        txt_taskevent_taskDetails_protait_zhe.setText(fuItem.getFullname());
    }

    private void setRelevant_view(TaskEventDetailsView_InfoVO mTEDV_InfoVO) {
        // 相关人
        // if (!Constant.nil.equals(relevant)) {
        // List<RelativePepoleVO> parserCompleteJson4key =
        // parserCompleteJson4key(
        // jsonStr, RelativePepoleVO.class,
        // "relevant");
        // if (parserCompleteJson4key != null)
        // datas_relevant
        // .addAll(parserCompleteJson4key);
        // }

        List<TaskEventDetailsView_Info_RelevantVO> newDatas = mTEDV_InfoVO
                .getRelevant();

        if (newDatas != null && !newDatas.isEmpty()) {
            // 这个里面取list中的值
            width_gridView = common_myGridViewOfClass_full.getWidth();

            int dip2px = WindowUtils.dip2px(context, 60);
            int nm = width_gridView / dip2px;
            for (int i = 0; i < Math.min(nm, datas_relevant.size()); i++) {
                // newDatas.add(datas_relevant.get(i));
            }
            /**
             * 相关人少于4人的时候,隐藏向右更多选项
             */
            if (newDatas.size() <= 4) {
                linear_common_arrow_threePoints.setVisibility(View.GONE);
            } else {
                /**
                 * 赶进度 多于4人的时候，替换适配器
                 */
                mTransferAdapter = new HeadAdapter4TaskDetails(this, true);
                mTransferAdapter.updateData(newDatas, true);
            }
            int listsize = Math.min(nm, newDatas.size());
            newDatas = newDatas.subList(0, listsize);
            common_myGridViewOfClass_full.setNumColumns(nm);
            adapter_gridView = new MyGridViewNoTagAdapter(context, newDatas);
            common_myGridViewOfClass_full.setAdapter(adapter_gridView);
        } else {
            // 做其他处理 // 相关人为暂无时隐藏布局
            txt_pepole.setVisibility(View.GONE);
            relative_pepole.setVisibility(View.GONE);
        }
    }

    /**
     * 进度回复
     * 
     * @param TaskEventDetailsView_InfoVO fillDataReplay
     */
    private void detailsChangePercentScheduleReply(
            TaskEventDetailsView_InfoVO mTEDV_InfoVO) {
        List<TaskEventDetailsView_Info_ScheduleReplyVO> newDatas = mTEDV_InfoVO
                .getScheduleReply();

        if (newDatas != null && !newDatas.isEmpty()) {
            /**
             * 100%进度的时候 有两条数据
             */
            linear_taskevent_taskDetails_detailsDescription.removeAllViews();
            for (int i = 0; i < newDatas.size(); i++) {

                final TaskEventDetailsView_Info_ScheduleReplyVO descriptionItem = newDatas
                        .get(i);
                View view = LayoutInflater.from(context).inflate(
                        R.layout.part_taskevent_tasks_details_descrption, null);
                TextView txt_precent = (TextView) view
                        .findViewById(R.id.txt_taskevent_taskDetails_percent);
                TextView txt_descrption = (TextView) view
                        .findViewById(R.id.txt_taskevent_taskDetails_descrption);
                TextView txt_time = (TextView) view
                        .findViewById(R.id.txt_taskevent_taskDetails_time);
                ImageView img_replay = (ImageView) view
                        .findViewById(R.id.img_taskevent_taskDetails_replay);
                View line = (View) view
                        .findViewById(R.id.line_taskevent_taskDetails_line);

                // 最后一条进度描述时去掉下面的线
                if (i == datas_description.size() - 1) {
                    line.setVisibility(View.GONE);
                }

                long longTime = Long.parseLong(descriptionItem.getTs_time());
                int scheduleValueInt = Integer.parseInt(descriptionItem
                        .getTs_schedule());
                txt_precent.setText(scheduleValueInt + "%");
                if (scheduleValueInt > 100 && scheduleValueInt == 101) {
                    txt_precent.setText("初审");
                } else if (scheduleValueInt > 100 && scheduleValueInt == 102) {
                    txt_precent.setText("定审");
                }
                txt_descrption.setText(descriptionItem.getTs_description()
                        .trim());
                txt_time.setText(CommonDateUtils.parseDate(longTime * 1000,
                        "MM-dd HH:mm"));

                linear_taskevent_taskDetails_detailsDescription.addView(view);
            }
        }
    }

    /**
     * 任务评论
     * 
     * @param mTEDV_InfoVO
     */
    private void setComment_view() {

        CustomDialog.showProgressDialog(context);
        String url = Constant.TaskEventPath.TASKEVENTGETCOMMENT_PATH;

        url = BSApplication.getInstance().getHttpTitle()
                + Constant4TaskEventPath.TASKEVENT_GETCOMMENT_PATH;

        Map<String, String> paramsMap = new HashMap<String, String>();
        try {
            // paramsMap.put("ftoken",
            // "RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O");
            paramsMap.put("ftoken", BSApplication.getInstance().getmCompany());
            paramsMap.put("id", taskid);
            paramsMap.put("userid", BSApplication.getInstance().getUserId());
        } catch (Exception e1) {
            CustomDialog.closeProgressDialog();
            CustomToast.showShortToast(context, "任务评论失败");
            e1.printStackTrace();
        }

        new HttpUtilsByPC().sendPostBYPC(url, paramsMap,
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        // CustomToast.showShortToast(context,
                        // "网络数据错误，任务评论失败，请重新尝试");
                        CustomDialog.closeProgressDialog();
                        /**
                         * 网络数据错误，与大师傅沟通后，任务评论失败，再次请求 2015/6/6 17:52 bypc
                         */
                        setComment_view();
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo arg0) {
                        // TODO Auto-generated method stub
                        String result = (String) arg0.result;
                        try {
                            Gson gson = new Gson();

                            DiscussResultVO mDiscussResultVO = gson.fromJson(
                                    result, DiscussResultVO.class);
                            String code = mDiscussResultVO.getCode();
                            DiscussVO mDiscussVO = null;
                            ArrayList<DiscussVO> array = null;
                            if (code.equalsIgnoreCase(Constant.RESULT_CODE)) {
                                array = mDiscussResultVO.getArray();

                                comment = mDiscussResultVO.getCount();
                                if (!TextUtils.isEmpty(comment)) {
                                    txt_taskevent_taskDetails_pinglunNum
                                            .setText("回复(" + comment + ")");
                                    txt_taskevent_taskDetails_pinglunNum
                                            .setVisibility(View.VISIBLE);
                                }
                                mDiscussAdapter.updateData(array);

                            } else {
                                return;
                            }
                            CustomDialog.closeProgressDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                            CustomDialog.closeProgressDialog();

                        }
                    }
                });

    }

    /**
     * 附件
     * 
     * @param mTEDV_InfoVO
     */
    private void setDownloadFile_view(TaskEventDetailsView_InfoVO mTEDV_InfoVO) {
        List<TaskEventDetailsView_Info_AnnexVO> mAnnexList = mTEDV_InfoVO
                .getAnnex();
        if (null == mAnnexList || mAnnexList.isEmpty()) {
            return;
        }
        for (int i = 0; i < mAnnexList.size(); i++) {
            final TaskEventDetailsView_Info_AnnexVO mAnnexVO = mAnnexList
                    .get(i);
            View child = View.inflate(context, R.layout.item_task_detail_annex,
                    null);
            TextView fileName = (TextView) child
                    .findViewById(R.id.txt_item_task_detail_annex_fileName);
            TextView des = (TextView) child
                    .findViewById(R.id.txt_item_task_detail_annex_des);
            ImageView fileIcon = (ImageView) child
                    .findViewById(R.id.img_item_task_detail_annex_fileIcon);
            View line = child.findViewById(R.id.view_bottom_line);
            final ImageView imgDownloadOpen = (ImageView) child
                    .findViewById(R.id.img_task_detail_annex_downloadIcon);
            final MasterLayout mMasterLayout = (MasterLayout) child
                    .findViewById(R.id.masterlayout_task_detail_annex);
            final int tagId = i;

            // 名字
            fileName.setText(mAnnexVO.getTitle());
            fileName.setTag(mAnnexVO.getFilepath()); // 将文件的下载路径设置为tag
            // int fileSize=Integer.parseInt(downloadFile.getFilesize());
            // // 描述
            // if ( fileSize< 1024) {
            // des.setText(downloadFile.getFilesize() + "B " + "来自  "
            // + mTEDV_InfoVO.getDname() + " "
            // + mTEDV_InfoVO.getFullname());
            // } else {
            // }
            des.setText(mAnnexVO.getFilesize() + "  来自  "
                    + mTEDV_InfoVO.getDname() + " "
                    + mTEDV_InfoVO.getFullname());

            // 图片
            // String[] split = downloadFile.getTitle().split("\\.");
            // downloadFile.setFiletype(split[split.length - 1]);
            // Integer icon =
            // FileIconMapping.getIcon(downloadFile.getFiletype());
            // fileIcon.setBackgroundResource(icon);
            fileIcon.setTag(tagId);
            Integer icon = FileIconMapping.getIcon(mAnnexVO.getTypename());
            fileIcon.setBackgroundResource(icon);

            // 是否下载
            final String mFilepathInM = savePath + mAnnexVO.getTitle();
            CustomLog.e(TAG, ">>>>> " + mFilepathInM);
            File file = new File(mFilepathInM);
            if (file.exists()) {
                imgDownloadOpen
                        .setBackgroundResource(R.drawable.ic_task_openfile);
                imgDownloadOpen.setTag(true);
            } else {
                imgDownloadOpen
                        .setBackgroundResource(R.drawable.ic_task_downloadfile);
                imgDownloadOpen.setTag(false);
            }

            Async.listMasterLayout.add(mMasterLayout);
            imgDownloadOpen.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean isdownload = (Boolean) v.getTag();
                    DownloadFile downloadFile = new DownloadFile();
                    downloadFile.setTitle(mAnnexVO.getTitle());

                    if (isdownload) {
                        Intent intent = new Intent();
                        intent.setAction(android.content.Intent.ACTION_VIEW);
                        File file = new File(mFilepathInM);
                        String type = CommFileUtils.getMIMEType(file);
                        CustomLog.e(TAG, "打开文件  MIME>>>" + type + "<<<");
                        intent.setDataAndType(Uri.fromFile(file), type);
                        startActivity(intent);
                    } else {
                        downloadFile.setFiletype(mAnnexVO.getTypename());
                        // downloadFile.setFilesize(mAnnexVO.getFilesize());
                        downloadFile.setFilepath(mAnnexVO.getFilepath());

                        // 将数据封装在对象中
                        // downloadFile.setFullname(mAnnexVO.get());
                        // downloadFile.setDname(mAnnexVO.getDname());

                        // 大小
                        downloadFile.setFilesize(downloadFile.getFilesize() / 1024);

                        Async startTask = null;
                        boolean isHas = false;
                        Async asyncTask = null;
                        Map<String, Async> map = null;
                        // 判断当前要下载的这个连接是否已经正在进行，如果正在进行就阻止在此启动一个下载任务
                        for (int i = 0; i < listTask.size(); i++) {
                            startTask = listTask.get(i).get(
                                    String.valueOf(tagId));
                            if (startTask != null) {
                                isHas = true;
                                break;
                            }
                        }
                        // 如果这个连接的下载任务还没有开始，就创建一个新的下载任务启动下载，并这个下载任务加到下载列表中
                        if (!isHas) {
                            mMasterLayout.setTag(String.valueOf(tagId));
                            asyncTask = new Async(context); // 创建新异步
                            map = new Hashtable<String, Async>();
                            map.put(String.valueOf(tagId), asyncTask);
                            listTask.add(map);
                            asyncTask.setFiletype(mAnnexVO.getTypename());
                            // downloadFile.setFilesize(mAnnexVO.getFilesize());
                            asyncTask.setFilepath(mAnnexVO.getFilepath());
                            asyncTask.setTitle(mAnnexVO.getTitle());
                            asyncTask.setFilepathInM(mFilepathInM);

                            // 当调用AsyncTask的方法execute时，就回去自动调用doInBackground方法
                            // asyncTask.execute(String.valueOf(v.getTag()));
                            asyncTask.execute(String.valueOf(tagId));
                            mMasterLayout.setVisibility(View.GONE);
                            CustomDialog.showProgressDialog(context, "正在下载...");
                            mMasterLayout.setFilepathInM(mFilepathInM);
                            imgDownloadOpen.setVisibility(View.GONE);
                            mMasterLayout.cusview.setupprogress(0);
                            mMasterLayout.animation();

                            // mMasterLayout.callOnClick();
                        }

                        // 图片类型
                        // String[] split =
                        // downloadFile.getTitle().split("\\.");
                        // downloadFile.setFiletype(split[split.length - 1]);

                        // Intent intent =
                        // new
                        // Intent(context,
                        // DownloadDetailActivity.class);
                        // intent.putExtra("DownloadFile",
                        // downloadFile);
                        // openActivity(intent);
                    }

                }
            });

            if (i == mAnnexList.size() - 1)
                line.setVisibility(View.GONE);
            linearFileContainer.addView(child);
        }

    }

    /**
     * 任务初审，定审提交
     * 
     * @param status 状态（1初审。2定审）
     * @param description 描述
     * @param taskid 任务id
     */
    private void submitPreliminary1() {
        // TODO Auto-generated method stub
        CustomDialog.showProgressDialog(context);

        String url = BSApplication.getInstance().getHttpTitle()
                + Constant4TaskEventPath.TASKEVENTCONFIRMTASK_PATH;

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("ftoken", BSApplication.getInstance().getmCompany());
        paramsMap.put("userid", BSApplication.getInstance().getUserId());
        paramsMap.put("uid", BSApplication.getInstance().getUserId());
        paramsMap.put("description", editTaskdetailsPreliminaryContentTxt);
        // paramsMap.put("status", status);
        paramsMap.put("status", preliminaryStatus);
        paramsMap.put("taskid", taskid);
        new HttpUtilsByPC().sendPostBYPC(url, paramsMap,
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        CustomDialog.closeProgressDialog();
                        CustomToast.showShortToast(context, "网络似乎断开了哦");
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo arg0) {
                        // TODO Auto-generated method stub
                        String result = (String) arg0.result;
                        String code = "";
                        String retinfo = "";
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            code = (String) jsonObject.get("code");
                            if (Constant.RESULT_CODE.equalsIgnoreCase(code)) {

                                if ("1".equalsIgnoreCase(preliminaryStatus)) {
                                    if (!TextUtils.isEmpty(finals)) {
                                        if ("1".equalsIgnoreCase(finals)
                                                && "100".equalsIgnoreCase(scheduleStatus)) {
                                            setPreliminarySubmit();
                                        } else {
                                            afterPreliminarySubmit1();
                                        }
                                    } else {
                                        afterPreliminarySubmit1();
                                    }
                                } else if ("2"
                                        .equalsIgnoreCase(preliminaryStatus)) {
                                    lyTaskdetailsPreliminary
                                            .setVisibility(View.GONE);
                                    btnTaskdetailsPreliminary
                                            .setVisibility(View.GONE);
                                    lyTaskdetailsBottom
                                            .setVisibility(View.VISIBLE);
                                    txt_chageProgress.setVisibility(View.GONE);
                                }

                                getDataFromServerOnStart();

                            } else if (Constant.RESULT_CODE400
                                    .equalsIgnoreCase(code)) {
                                retinfo = (String) jsonObject.get("retinfo");
                                CustomToast.showShortToast(context, retinfo);
                                txt_chageProgress.setVisibility(View.GONE);
                            }
                            CustomDialog.closeProgressDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            CustomDialog.closeProgressDialog();
                            CustomToast.showShortToast(context, "操作失败，请重新尝试");
                        }

                    }
                });
    }

    /**
     * 定审 设置定审状态
     */
    private void setPreliminarySubmit() {
        // TODO Auto-generated method stub
        btnTaskdetailsPreliminary.setVisibility(View.VISIBLE);
        btnTaskdetailsPreliminary.setText("定 审");
        btnTaskdetailsPreliminarySubmit.setText("定 审");
        lyTaskdetailsBottom.setVisibility(View.GONE);
        preliminaryStatus = "2";
        txt_chageProgress.setVisibility(View.GONE);
        lyTaskdetailsPreliminary.setVisibility(View.GONE);
    }

    /**
     * 初审结束后，针对没有定审的权限
     */
    private void afterPreliminarySubmit1() {
        // TODO Auto-generated method stub
        btnTaskdetailsPreliminary.setVisibility(View.GONE);
        lyTaskdetailsBottom.setVisibility(View.VISIBLE);
        txt_chageProgress.setVisibility(View.GONE);
        btnTaskdetailsPreliminarySubmit.setVisibility(View.GONE);
        lyTaskdetailsPreliminary.setVisibility(View.GONE);
        editTaskdetailsPreliminaryContent.setVisibility(View.GONE);
    }

    public void sendNewTimeBoardMessage() {
        Intent intent = new Intent(TaskActionReceiver.CHANGEPRELIMINARYSTATUS);
        intent.putExtra("task_id", taskid);
        sendBroadcast(intent);
    }

}
