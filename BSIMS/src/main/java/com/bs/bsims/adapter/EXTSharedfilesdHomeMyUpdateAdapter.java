
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.constant.Constant4Sharedfiles;
import com.bs.bsims.download.domain.DownloadFile;
import com.bs.bsims.utils.BSTextReadUtils;
import com.bs.bsims.utils.BsFileMathUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.FileUtils;
import com.bs.bsims.utils.ToastUtil;
import com.bs.bsims.view.BSCirleDwLoadProgrossView;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSPointImageView;
import com.bs.bsims.xutils.impl.DownloadInfo;
import com.bs.bsims.xutils.impl.DownloadManager;
import com.bs.bsims.xutils.impl.DownloadViewHolder;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;

import org.json.JSONObject;
import org.xutils.common.Callback.CancelledException;
import org.xutils.ex.HttpException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author LQC
 * @Description: 2.修改了 现在这个适配器提供给我的收藏的下载和部门下的文档下载。
 * @date
 * @email 问题：2016-1-11 当前只允许下载两个文件，当两个线程去更新ui的时候会有交叉更新的情况发生
 * @version V1.0
 */
@SuppressLint("NewApi")
public class EXTSharedfilesdHomeMyUpdateAdapter extends BaseAdapter {

    /* 定义全局变量 记录用户选择下载个数 */
    public int downcount = 0;
    private ListView listview = null;
    // private Handler handlers = new Handler();
    private Activity mContext;
    public List<DownloadFile> mList;
    private String DownloadState = "1";// 默认表示handler不未空
    private String type = "";
    // private String dowmflage = "";
    /** 屏幕宽度 */
    private int mScreenWidth;
    private int downFileId;
    /** 删除按钮事件 */
    private DeleteButtonOnclickImpl mDelOnclickImpl;
    /** HorizontalScrollView左右滑动事件 */
    private ScrollViewScrollImpl mScrollImpl;

    private int finalitempstion;
    /** 布局参数,动态让HorizontalScrollView中的TextView宽度包裹父容器 */
    private LinearLayout.LayoutParams mParams;

    /** 记录滑动出删除按钮的itemView */
    public HorizontalScrollView mScrollView;

    /** touch事件锁定,如果已经有滑动出删除按钮的itemView,就屏蔽下一整次(down,move,up)的onTouch操作 */
    public boolean mLockOnTouch = false;

    private DownloadFile mData = null;
    private int currentProgress;
    private DownloadManager downloadManager;

    public EXTSharedfilesdHomeMyUpdateAdapter(Activity context, String type, ListView listviews) {
        this.mContext = context;
        this.mList = new ArrayList<DownloadFile>();
        this.type = type;
        this.listview = listviews;
        // new BSRefreshListView(context).setLeftSrollstateset(true);
        // 搞到屏幕宽度
        Display defaultDisplay = ((Activity) mContext).getWindowManager()
                .getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        defaultDisplay.getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        CustomLog.e("aaa", mScreenWidth + "");
        mParams = new LinearLayout.LayoutParams(mScreenWidth,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mParams.gravity = Gravity.CENTER;
        CustomLog.e("mParams", mParams + "");
        // 初始化删除按钮事件与item滑动事件

        if (this.type.equals("-2")) {
            mDelOnclickImpl = new DeleteButtonOnclickImpl();
            mScrollImpl = new ScrollViewScrollImpl();
        }
        downloadManager = DownloadManager.getInstance();

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CustomLog.e("downcount", downcount + "");
        // TODO Auto-generated method stub
        final ViewHolder holder;
        if (convertView == null) {
            // holder = new ViewHolder();
            // view = View.inflate(mContext,
            // R.layout.item_sharedfilesd_grouphome_down, null);
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_sharedfilesd_grouphome_down, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.img_item_sharedfilesd_grouphome_all);
            holder.mTime = (TextView) convertView
                    .findViewById(R.id.txt_item_sharedfilesd_grouphome_all);
            holder.mColor = (TextView) convertView
                    .findViewById(R.id.txt_item_sharedfilesd_grouphome_all_color);
            holder.mNum = (TextView) convertView
                    .findViewById(R.id.txt_item_sharedfilesd_grouphome_all_num);
            // 上个月
            holder.mName1 = (TextView) convertView
                    .findViewById(R.id.txt_last_item_sharedfilesd_grouphome_all);
            holder.mTime1 = (TextView) convertView
                    .findViewById(R.id.txt_last_item_sharedfilesd_grouphome_all_month);
            holder.mColor1 = (TextView) convertView
                    .findViewById(R.id.txt_last_item_sharedfilesd_grouphome_all_color);
            holder.mNum1 = (TextView) convertView
                    .findViewById(R.id.txt_last_item_sharedfilesd_grouphome_all_num);

            holder.mMoreImg = (ImageView) convertView
                    .findViewById(R.id.item_sharedfilesd_grouphome_more_img);
            holder.mMoreLy = (LinearLayout) convertView
                    .findViewById(R.id.tem_sharedfilesd_grouphome_more_ly);
            holder.mMoreDownLy = (LinearLayout) convertView
                    .findViewById(R.id.tem_sharedfilesd_grouphome_more_down_ly);// 下载按钮的布局
            holder.filedown_stoporcontinue = (TextView) convertView
                    .findViewById(R.id.filedown_stoporcontinue);// 下载的字体
            holder.mMoreDownLy1 = (LinearLayout)
                    convertView.findViewById(R.id.tem_sharedfilesd_grouphome_more_down_ly1);
            holder.mMoreCollectionLy = (LinearLayout) convertView
                    .findViewById(R.id.tem_sharedfilesd_grouphome_more_collection_ly);
            holder.mMoreCollectionLy2 = (LinearLayout) convertView
                    .findViewById(R.id.tem_sharedfilesd_grouphome_more_collection_ly2);
            holder.mMoreCollectionLy1 = (LinearLayout)
                    convertView.findViewById(R.id.tem_sharedfilesd_grouphome_more_collection_ly1);
            holder.item_sharedfilesd_grouphome_more_txt = (TextView)
                    convertView.findViewById(R.id.item_sharedfilesd_grouphome_more_txt);
            holder.Wholefile = (LinearLayout) convertView.findViewById(R.id.Wholefile);
            holder.Wholefile1 = (LinearLayout) convertView.findViewById(R.id.Wholefile1);
            holder.img_stop = (ImageView) convertView
                    .findViewById(R.id.item_sharedfilesd_grouphome_more_img1);
            /** 设置宽高 **/
            holder.Wholefile1.setLayoutParams(mParams);
            holder.deleteButton = (ImageButton) convertView
                    .findViewById(R.id.item_delete);
            holder.linearfileprogress = (LinearLayout) convertView
                    .findViewById(R.id.filedownprogress);// 文件的大小的布局
            holder.tem_sharedfilesd_grouphome_more_collection_lying = (LinearLayout) convertView
                    .findViewById(R.id.tem_sharedfilesd_grouphome_more_collection_lying);// 正在下载的布局
            holder.grossview = (BSCirleDwLoadProgrossView) convertView
                    .findViewById(R.id.roundProgressBar5);
            if (type.equals("-2")) {
                holder.scrollView = (HorizontalScrollView) convertView;
                holder.scrollView.setOnTouchListener(mScrollImpl);
                holder.deleteButton.setOnClickListener(new DeleteButtonOnclickImpl());
            }
            else {
                holder.scrollView = (HorizontalScrollView) convertView;
                holder.scrollView.setOnTouchListener(null);
                holder.deleteButton.setOnClickListener(null);
                holder.deleteButton.setVisibility(View.GONE);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            convertView.setTag(holder);
        }

        if (type.equals("-2")) {
            holder.position = position;
            holder.deleteButton.setTag(holder);
            holder.scrollView.scrollTo(0, 0);
            holder.Wholefile1.setTag(holder);
            holder.mMoreDownLy.setTag(holder);
            holder.mMoreDownLy1.setTag(holder);
            holder.mMoreImg.setTag(holder);
            holder.mMoreCollectionLy.setTag(holder);
            holder.mMoreCollectionLy1.setTag(holder);
            holder.img_stop.setTag(holder);
        }
        else {
            holder.position = position;
            holder.Wholefile1.setTag(holder);
            holder.mMoreDownLy.setTag(holder);
            holder.mMoreDownLy1.setTag(holder);
            holder.mMoreImg.setTag(holder);
            holder.mMoreCollectionLy.setTag(holder);
            holder.mMoreCollectionLy1.setTag(holder);
            holder.img_stop.setTag(holder);
        }
        mData = mList.get(position);

        CustomLog.e("likes", mData.getLikes() + "");
        // 判断文件是否存在，是否被收藏
        // 判断文件是否已经被下载 在这里有可能有文件重名 所以在每一次下载的都要在路径后面加上一个表示以区分
        // 存在sdcard中，并且文件已经成功下载有记录

        if (mData.getDownload().equals("1")
                && FileUtils.isFileExistDown(mData.getTitle() + mData.getSharedid() + "."
                        + mData.getFilepath().substring(mData.getFilepath().lastIndexOf(".") + 1))) {
            holder.mMoreDownLy.setVisibility(View.GONE);
            holder.mMoreDownLy1.setVisibility(View.VISIBLE);
            holder.mTime.setTextColor(Color.parseColor("#00a9fe"));
        }
        else {
            holder.mMoreDownLy.setVisibility(View.VISIBLE);
            holder.mMoreDownLy1.setVisibility(View.GONE);
            holder.mTime.setTextColor(Color.parseColor("#000000"));
        }

        // 如果是部门列表或者是我上传的列表
        if (type.equals("-2") || type.equals("1")) {
            // 在这里要判断是否被收藏
            // 如果被收藏过，而且查看页面是我的上传的页面
            if (!mData.getLikes().equals("0")) {
                holder.mMoreCollectionLy.setVisibility(View.GONE);
                holder.mMoreCollectionLy2.setVisibility(View.VISIBLE);
            }
            else {
                holder.mMoreCollectionLy.setVisibility(View.VISIBLE);
                holder.mMoreCollectionLy2.setVisibility(View.GONE);
            }
        }
        else {
            if (!mData.getLikes().equals("0")) {
                holder.mMoreCollectionLy.setVisibility(View.GONE);
                holder.mMoreCollectionLy1.setVisibility(View.VISIBLE);
            }
            else {
                holder.mMoreCollectionLy.setVisibility(View.VISIBLE);
                holder.mMoreCollectionLy1.setVisibility(View.GONE);
            }
        }

        if (holder.mMoreLy.getVisibility() == View.VISIBLE) {
            holder.mMoreLy.setVisibility(View.GONE);
        }
        else {
            holder.mMoreLy.setVisibility(View.GONE);
        }
        String tempTime = mData.getTime();
        if (TextUtils.isEmpty(tempTime)) {
            tempTime = "暂无文档";
        } else {
            tempTime = DateUtils.parseDate(Long.parseLong(tempTime) * 1000);
        }

        // 当前的状态下没有下载文件
        if ("0".equals(mData.getIsDownLoading())) {
            holder.mMoreImg.setVisibility(View.VISIBLE);
            holder.item_sharedfilesd_grouphome_more_txt.setVisibility(View.VISIBLE);
            holder.grossview.setVisibility(View.GONE);
            holder.tem_sharedfilesd_grouphome_more_collection_lying.setVisibility(View.GONE);
            if (mData.getDownload().equals("0")
                    && FileUtils.isFileExistDown(mData.getTitle()
                            + mData.getSharedid()
                            + "."
                            + mData.getFilepath().substring(
                                    mData.getFilepath().lastIndexOf(".") + 1))) {
                holder.img_stop.setVisibility(View.VISIBLE);
                holder.mMoreImg.setVisibility(View.GONE);
                holder.filedown_stoporcontinue.setText("继续下载");
            }
            else {
                holder.img_stop.setVisibility(View.GONE);
                holder.mMoreImg.setVisibility(View.VISIBLE);
                holder.filedown_stoporcontinue.setText("下载");
            }
        }

        // 当前的状态下正在下载文件
        else {
            holder.img_stop.setVisibility(View.GONE);
            holder.mMoreImg.setVisibility(View.GONE);
            holder.item_sharedfilesd_grouphome_more_txt.setVisibility(View.GONE);
            holder.grossview.setVisibility(View.VISIBLE);
            holder.grossview.setProgress(mData.getProgress());
            holder.mMoreDownLy.setVisibility(View.GONE);// 下载按钮隐藏
            holder.mMoreDownLy1.setVisibility(View.GONE);// 查看按钮隐藏
            holder.tem_sharedfilesd_grouphome_more_collection_lying.setVisibility(View.VISIBLE);
        }

        /**
         * 第二行文件详情
         */
        BsFileMathUtils.SetShowFileName(mData.getExtension(), holder.image, mContext);
        String fileInfo = mData.getDname() + "\t" + mData.getFullname() + "\t" + tempTime;
        holder.mTime.setText(BsFileMathUtils.SetShowFileNames(mData.getExtension(),
                mData.getTitle(), mData.getFilepath()));
        holder.mTime1.setText(fileInfo);
        holder.mNum.setText(mData.getSharedid());
        holder.mNum1.setText(mData.getDid());
        holder.item_sharedfilesd_grouphome_more_txt.setText(BsFileMathUtils
                .FromartDoubleOfFileSize(mData.getSize()));
        holder.Wholefile1.setOnClickListener(new SetonclickViewGone());
        // holder.mMoreDownLy.setOnClickListener(new SetonclickFiledown());
        holder.mMoreDownLy.setOnClickListener(new SetonclickFiledown(
                holder.item_sharedfilesd_grouphome_more_txt, holder.mMoreImg, holder.grossview,
                mData, holder));
        holder.mMoreDownLy1.setOnClickListener(new SetonclickReadFile());
        holder.mMoreCollectionLy.setOnClickListener(new SetonclickFilelike());
        holder.mMoreCollectionLy1.setOnClickListener(new SetonclickFileDislike());
        holder.tem_sharedfilesd_grouphome_more_collection_lying
                .setOnClickListener(new fileOnclickIsCanleDownload());// 设置断点下载返回事件
        holder.mMoreImg.setOnClickListener(new SetonclickViewGone());
        holder.img_stop.setOnClickListener(new SetonclickViewGone());

        /**
         * 收藏
         */

        return convertView;
    }

    static class ViewHolder {
        private TextView mTime, mType, mColor, mNum;
        private TextView mName1, mContent1, mTime1, mType1, mColor1, mNum1,
                item_sharedfilesd_grouphome_more_txt, filedown_stoporcontinue;
        private ImageView image, mMoreImg, img_stop;
        private BSPointImageView mBSPonit;
        private LinearLayout mMoreLy, linearfileprogress;
        private LinearLayout mMoreDownLy, mMoreDownLy1;
        private LinearLayout mMoreCollectionLy, mMoreCollectionLy1, mMoreCollectionLy2, Wholefile,
                Wholefile1, tem_sharedfilesd_grouphome_more_collection_lying;
        private HorizontalScrollView scrollView;
        private TextView infoTextView;
        private ImageButton deleteButton;
        private int position;
        private BSCirleDwLoadProgrossView grossview;;
        private int prograss;

    }

    public void updateData(List<DownloadFile> list, String type) {
        this.type = type;
        if (null == mList) {
            mList = new ArrayList<DownloadFile>();
        }
        if (null == list) {
            return;
        }
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateDataFrist(List<DownloadFile> list, String type) {
        this.type = type;
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateDataLast(List<DownloadFile> list, String type) {
        this.type = type;
        mList.addAll(list);
        notifyDataSetChanged();
    }

    /** HorizontalScrollView的滑动事件 */
    private class ScrollViewScrollImpl implements OnTouchListener {
        /** 记录开始时的坐标 */
        private float startX = 0;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 如果有划出删除按钮的itemView,就让他滑回去并且锁定本次touch操作,解锁会在父组件的dispatchTouchEvent中进行
                    if (mScrollView != null) {
                        scrollView(mScrollView, HorizontalScrollView.FOCUS_LEFT);
                        mScrollView = null;
                        mLockOnTouch = true;
                        return true;
                    }
                    startX = event.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    HorizontalScrollView view = (HorizontalScrollView) v;
                    // 如果滑动了>50个像素,就显示出删除按钮
                    if (startX > event.getX() + 50) {
                        startX = 0;// 因为公用一个事件处理对象,防止错乱,还原startX值
                        scrollView(view, HorizontalScrollView.FOCUS_RIGHT);
                        mScrollView = view;
                    } else {
                        scrollView(view, HorizontalScrollView.FOCUS_LEFT);
                    }
                    break;
            }
            return false;
        }
    }

    /** HorizontalScrollView左右滑动 */
    public void scrollView(final HorizontalScrollView view, final int parameter) {
        view.post(new Runnable() {
            @Override
            public void run() {
                view.pageScroll(parameter);
            }
        });
    }

    /** 删除事件 */
    private class DeleteButtonOnclickImpl implements OnClickListener {
        @Override
        public void onClick(View v) {
            final ViewHolder holder = (ViewHolder) v.getTag();
            CustomDialog.showProgressDialog(mContext);
            // Toast.makeText(getContext(), "删除第" + holder.position + "项",
            // Toast.LENGTH_SHORT).show();
            Animation animation = AnimationUtils.loadAnimation(mContext,
                    R.anim.anim_item_delete);
            holder.scrollView.startAnimation(animation);
            animation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // remove(getItem(holder.position));
                    CustomLog.e("aaaa", holder.position + "");
                    // post提交给服务器，删除文档
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("ftoken", BSApplication.getInstance().getmCompany());
                    map.put("userid", BSApplication.getInstance().getUserId());
                    map.put("sid", mList.get(holder.position).getSharedid());
                    final String url = BSApplication.getInstance().getHttpTitle()
                            + Constant4Sharedfiles.FILES_LIST_DELETE;
                    new HttpUtilsByPC().sendPostBYPC(url, map,
                            new RequestCallBackPC() {
                                @Override
                                public void onFailurePC(HttpException arg0, String arg1) {
                                    CustomLog.e("downurl", url);
                                    // TODO Auto-generated method stub
                                    CustomDialog.closeProgressDialog();
                                    CustomToast.showShortToast(mContext, "删除失败");
                                }

                                @Override
                                public void onSuccessPC(ResponseInfo rstr) {
                                    // TODO Auto-generated method stub
                                    // CustomLog.e("filenpathtrue",

                                    JSONObject jsonObject;
                                    try {
                                        jsonObject = new JSONObject(new String(rstr.result
                                                .toString()));
                                        String str = (String) jsonObject.get("retinfo");
                                        String code = (String) jsonObject.get("code");
                                        if (code.equals("200")) {
                                            CustomDialog.closeProgressDialog();
                                            mList.remove(holder.position);
                                            notifyDataSetChanged();
                                            CustomToast.showShortToast(mContext, "删除成功");
                                        } else {
                                            CustomDialog.closeProgressDialog();
                                            CustomToast.showShortToast(mContext, "删除失败");
                                        }

                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                        CustomDialog.closeProgressDialog();
                                        CustomToast.showShortToast(mContext, "删除失败");
                                    }

                                }

                            });

                }
            });

        }
    }

    public class SetonclickViewGone implements OnClickListener {
        DownloadFile maFiles = null;

        @Override
        public void onClick(View v) {
            final ViewHolder holder = (ViewHolder) v.getTag();
            maFiles = mList.get(holder.position);
            // TODO Auto-generated method stub
            if (holder.mMoreLy.getVisibility() != View.VISIBLE) {
                holder.mMoreImg.setBackgroundDrawable(mContext.getResources().getDrawable(
                        R.drawable.filedonw_arrowup));
                holder.mMoreLy.setVisibility(View.VISIBLE);
            } else {
                holder.mMoreImg.setBackgroundDrawable(mContext.getResources().getDrawable(
                        R.drawable.filedonw_arrowdown));
                holder.mMoreLy.setVisibility(View.GONE);
            }

        }
    }

    public class SetonclickFiledown implements OnClickListener {
        String sdcardurlfile = "";

        private TextView item_sharedfilesd_grouphome_more_txt;
        private ImageView mMoreImg;
        private DownloadFile mData;
        private BSCirleDwLoadProgrossView grossview;
        private ViewHolder holder;

        public SetonclickFiledown(TextView tv, ImageView img, BSCirleDwLoadProgrossView progress,
                DownloadFile data, ViewHolder holders) {
            this.item_sharedfilesd_grouphome_more_txt = tv;
            this.mMoreImg = img;
            this.grossview = progress;
            this.mData = data;
            this.holder = holders;
        }

        @Override
        public void onClick(View v) {
            // mData = mList.get(holder.position);
            // CustomToast.showLongToast(mContext, mData.getTitle());
            sdcardurlfile = mData.getTitle() + mData.getSharedid() + "."
                    + mData.getFilepath().substring(mData.getFilepath().lastIndexOf(".") + 1);
            try {
                // 先得到SDCARD的当前剩下的容量
                long sdsize = BsFileMathUtils.GetSDCardSize();
                // CustomLog.e("sdsize", sdsize * 1024 * 1024 + "b");
                // 如果sdcard的容量小于当前下载文件的容量则不下载
                if (sdsize != 0) {
                    if ((Long.parseLong(mData.getSize()) / 1024 / 1024) > sdsize) {
                        CustomToast.showLongToast(mContext, "您的存储空间不够了哦~");
                    }
                    else if (downcount >= 2) {
                        CustomToast.showShortToast(mContext, "下载文件一次不能超过2个哦~");
                    }
                    else {
                        mData.setIsDownLoading("1");
                        final String sdcardurl = FileUtils.createBSDDir();
                        if (!sdcardurl.equals("") && !mData.getTime().equals("")) {

                            downloadManager.startDownload(mData.getFilepath(),
                                    mData.getSharedid(),
                                    sdcardurl + "/"
                                            + mData.getTitle()
                                            + mData.getSharedid()
                                            + "."
                                            + mData.getFilepath().substring(
                                                    mData.getFilepath().lastIndexOf(".") + 1),
                                    true, true, new DownloadViewHolder(new DownloadInfo()) {

                                        @Override
                                        public void onWaiting() {
                                            // TODO Auto-generated method stub

                                        }

                                        @Override
                                        public void onSuccess(File result) {
                                            // TODO Auto-generated method stub
                                            mData.setIsDownLoading("0");
                                            // dowmflage = "";
                                            // TODO Auto-generated method stub
                                            // 右侧变成查看按钮
                                            // 如果下载成功 提交下载成功的记录
                                            HashMap<String, String> map = new HashMap<String, String>();
                                            map.put("ftoken", BSApplication.getInstance()
                                                    .getmCompany());
                                            map.put("userid", BSApplication.getInstance()
                                                    .getUserId());
                                            map.put("sid", mData.getSharedid());

                                            final String url = BSApplication.getInstance()
                                                    .getHttpTitle()
                                                    + Constant4Sharedfiles.DOWNLOAD_PATH;
                                            new HttpUtilsByPC().sendPostBYPC(url, map,
                                                    new RequestCallBackPC() {
                                                        @Override
                                                        public void onFailurePC(HttpException arg0,
                                                                String arg1) {
                                                            CustomLog.e("downurl", url);
                                                            // TODO Auto-generated method
                                                            // stub
                                                            mData.setIsDownLoading("0");
                                                            ToastUtil.show(mContext, "服务器提交错误."
                                                                    + mData.getTitle() + "下载失败");
                                                            // 删除sdcard文件 保证重新下载不会出问题
                                                            FileUtils.BsdelFile(sdcardurlfile);
                                                            CustomLog
                                                                    .e("deletefile", sdcardurlfile);
                                                            holder.grossview
                                                                    .setVisibility(View.GONE);
                                                            // holder.linearfileprogress.removeView(view);
                                                            holder.mMoreDownLy
                                                                    .setVisibility(View.GONE);
                                                            holder.mMoreDownLy1
                                                                    .setVisibility(View.VISIBLE);

                                                            holder.mMoreImg
                                                                    .setVisibility(View.VISIBLE);
                                                            holder.mMoreImg
                                                                    .setBackgroundResource(R.drawable.filedonw_arrowdown);
                                                            holder.item_sharedfilesd_grouphome_more_txt
                                                                    .setVisibility(View.VISIBLE);
                                                            /** 没有下载了 */
                                                            holder.tem_sharedfilesd_grouphome_more_collection_lying
                                                                    .setVisibility(View.GONE);
                                                            downcount--;// 下载失败
                                                            notifyDataSetChanged();

                                                        }

                                                        @Override
                                                        public void onSuccessPC(ResponseInfo rstr) {
                                                            // TODO Auto-generated method
                                                            // stub
                                                            // CustomLog.e("filenpathtrue",
                                                            // file.result.getAbsolutePath()
                                                            // +
                                                            // "");
                                                            mData.setIsDownLoading("0");
                                                            downcount--;// 如果有下载成功的
                                                            JSONObject jsonObject;
                                                            try {
                                                                jsonObject = new JSONObject(
                                                                        new String(rstr.result
                                                                                .toString()));
                                                                String str = (String) jsonObject
                                                                        .get("retinfo");
                                                                String code = (String) jsonObject
                                                                        .get("code");
                                                                if (code.equals("200")) {
                                                                    ToastUtil.show(mContext,
                                                                            mData.getTitle()
                                                                                    + "下载完成");
                                                                    holder.mMoreDownLy
                                                                            .setVisibility(View.GONE);
                                                                    holder.mMoreDownLy1
                                                                            .setVisibility(View.VISIBLE);
                                                                    mData.setDownload("1");
                                                                } else {
                                                                    ToastUtil.show(mContext,
                                                                            mData.getTitle()
                                                                                    + "下载失败");
                                                                }

                                                            }
                                                            catch (Exception e) {
                                                                e.printStackTrace();
                                                                ToastUtil.show(mContext,
                                                                        mData.getTitle()
                                                                                + "下载失败");
                                                            }

                                                            holder.grossview
                                                                    .setVisibility(View.GONE);
                                                            holder.mMoreImg
                                                                    .setVisibility(View.VISIBLE);
                                                            holder.mMoreImg
                                                                    .setBackgroundResource(R.drawable.filedonw_arrowdown);
                                                            holder.item_sharedfilesd_grouphome_more_txt
                                                                    .setVisibility(View.VISIBLE);
                                                            /** 没有下载了 */
                                                            holder.tem_sharedfilesd_grouphome_more_collection_lying
                                                                    .setVisibility(View.GONE);
                                                            notifyDataSetChanged();

                                                        }

                                                    });
                                        }

                                        @Override
                                        public void onStarted() {
                                            // TODO Auto-generated method stub
                                            holder.img_stop.setVisibility(View.GONE);
                                            holder.mMoreLy.setVisibility(View.GONE);
                                            mMoreImg.setVisibility(View.GONE);
                                            item_sharedfilesd_grouphome_more_txt
                                                    .setVisibility(View.GONE);
                                            mData.setIsDownLoading("1");
                                            /** 如果是正在下载 就把下载按钮隐藏避免bug的出现 **/
                                            holder.mMoreDownLy.setVisibility(View.GONE);
                                            holder.tem_sharedfilesd_grouphome_more_collection_lying
                                                    .setVisibility(View.VISIBLE);
                                            /** 如果是正在下载 就把下载按钮隐藏避免bug的出现 **/
                                            grossview.setVisibility(View.VISIBLE);
                                            grossview.setProgress(1);// 先设定为1
                                            CustomLog.e(
                                                    "currentmax",
                                                    sdcardurl
                                                            + "/"
                                                            + mData.getTitle()
                                                            + "."
                                                            + mData.getFilepath().substring(
                                                                    mData.getFilepath()
                                                                            .lastIndexOf(".") + 1));
                                            CustomLog.e("", mData.getFilepath());
                                            downcount++;
                                        }

                                        @Override
                                        public void onLoading(long total, long current) {
                                            // TODO Auto-generated method stub
                                            if (total >= 0) {
                                                grossview.setProgress((int) ((current *
                                                        100 / total + 1)));
                                                mData.setProgress((int) ((current *
                                                        100 / total + 1)));
                                                // updateProgressPartly((int) (current * 100 / total
                                                // + 1)) + ""),holder.position);
                                                // updateProgressPartly((int) ((current *
                                                // 100 / total + 1)), holder.position);
                                                // notifyDataSetChanged();
                                            }
                                            else {
                                                CustomLog.e("current1", current + "");
                                                CustomLog.e("current2", total + "");
                                            }
                                            CustomLog.e("current1", current + "");
                                            CustomLog.e("current2", total + "");
                                            CustomLog.e("current2", "输出");
                                        }

                                        @Override
                                        public void onError(Throwable ex, boolean isOnCallback) {
                                            // TODO Auto-generated method stub
                                            mData.setIsDownLoading("0");
                                            holder.grossview.setProgress(0);

                                            ToastUtil.show(mContext, mData.getTitle()
                                                    + "下载失败请重新下载...");
                                            FileUtils.BsdelFile(sdcardurlfile);
                                            // holder.linearfileprogress.removeView(view);
                                            holder.grossview.setVisibility(View.GONE);
                                            holder.mMoreImg.setVisibility(View.VISIBLE);
                                            holder.mMoreImg
                                                    .setBackgroundResource(R.drawable.filedonw_arrowdown);
                                            holder.item_sharedfilesd_grouphome_more_txt
                                                    .setVisibility(View.VISIBLE);
                                            /** 没有下载了 */
                                            // holder.mMoreDownLy.setVisibility(View.VISIBLE);
                                            holder.tem_sharedfilesd_grouphome_more_collection_lying
                                                    .setVisibility(View.GONE);
                                            notifyDataSetChanged();
                                            downcount--;// 下载失败
                                        }

                                        @Override
                                        public void onCancelled(CancelledException cex) {
                                            // TODO Auto-generated method stub

                                        }
                                    });

                        }

                    }
                }
                else {
                    mData.setIsDownLoading("0");
                    CustomToast.showShortToast(mContext, "手机内存版本太低 无法下载。");
                    return;
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public class SetonclickFilelike implements OnClickListener {
        DownloadFile maFiles = null;

        @Override
        public void onClick(View v) {
            final ViewHolder holder = (ViewHolder) v.getTag();
            maFiles = mList.get(holder.position);
            // TODO Auto-generated method stub
            final String url = BSApplication.getInstance().getHttpTitle()
                    + Constant4Sharedfiles.LIKES_PATH;
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ftoken", BSApplication.getInstance().getmCompany());
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("sid", maFiles.getSharedid());
            map.put("status", "1");
            new HttpUtilsByPC().sendPostBYPC(url, map,
                    new RequestCallBackPC() {
                        @Override
                        public void onFailurePC(HttpException arg0, String arg1) {
                            // TODO Auto-generated method stub
                            CustomToast.showLongToast(mContext, "网络似乎断开了哦");
                        }

                        @Override
                        public void onSuccessPC(ResponseInfo rstr) {
                            // TODO Auto-generated method stub
                            // CustomLog.e("filenpathtrue",
                            // file.result.getAbsolutePath() + "");

                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(
                                        new String(rstr.result
                                                .toString()));
                                String str = (String) jsonObject
                                        .get("retinfo");
                                String code = (String) jsonObject
                                        .get("code");
                                if (code.equals("200")) {
                                    maFiles.setLikes("1");
                                    CustomToast.showLongToast(mContext, "收藏成功");
                                } else {
                                    CustomToast.showLongToast(mContext, "收藏失败");
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                CustomToast.showLongToast(mContext, "收藏失败");
                            }

                            holder.mMoreLy.setVisibility(View.GONE);
                            holder.mMoreImg.setVisibility(View.VISIBLE);
                            holder.mMoreImg.setBackgroundResource(R.drawable.filedonw_arrowdown);
                            holder.item_sharedfilesd_grouphome_more_txt.setVisibility(View.VISIBLE);
                            notifyDataSetChanged();

                        }

                    });
        }

    }

    public class SetonclickFileDislike implements OnClickListener {
        DownloadFile maFiles = null;

        @Override
        public void onClick(View v) {
            final ViewHolder holder = (ViewHolder) v.getTag();
            maFiles = mList.get(holder.position);
            // TODO Auto-generated method stub
            final String url = BSApplication.getInstance().getHttpTitle()
                    + Constant4Sharedfiles.LIKES_PATH;
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ftoken", BSApplication.getInstance().getmCompany());
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("sid", maFiles.getSharedid());
            map.put("status", "0");
            // TODO Auto-generated method stub
            new HttpUtilsByPC().sendPostBYPC(url, map,
                    new RequestCallBackPC() {

                        @Override
                        public void onFailurePC(HttpException arg0, String arg1) {
                            // TODO Auto-generated method stub
                            CustomToast.showLongToast(mContext, "网络似乎断开了哦");
                        }

                        @Override
                        public void onSuccessPC(ResponseInfo rstr) {
                            // TODO Auto-generated method stub
                            // CustomLog.e("filenpathtrue",
                            // file.result.getAbsolutePath() + "");
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(
                                        new String(rstr.result
                                                .toString()));
                                String str = (String) jsonObject
                                        .get("retinfo");
                                String code = (String) jsonObject
                                        .get("code");
                                if (code.equals("200")) {
                                    holder.mMoreCollectionLy.setVisibility(View.VISIBLE);
                                    holder.mMoreCollectionLy1.setVisibility(View.GONE);
                                    maFiles.setLikes("0");
                                    CustomToast.showLongToast(mContext, "取消收藏成功");
                                    if (maFiles.getLikes().equals("0") && type.equals("-1")) {
                                        mList.remove(holder.position);
                                    }
                                } else {
                                    CustomToast.showLongToast(mContext, "取消收藏失败");
                                }
                            }
                            catch (Exception e) {
                                CustomToast.showLongToast(mContext, "取消收藏失败");
                                e.printStackTrace();
                            }

                            holder.mMoreLy.setVisibility(View.GONE);
                            holder.mMoreImg.setVisibility(View.VISIBLE);
                            holder.mMoreImg.setBackgroundResource(R.drawable.filedonw_arrowdown);
                            holder.item_sharedfilesd_grouphome_more_txt.setVisibility(View.VISIBLE);
                            notifyDataSetChanged();
                        }

                    });
        }

    }

    public class SetonclickReadFile implements OnClickListener {
        DownloadFile maFiles = null;
        String sdcardurlfile = "";

        @Override
        public void onClick(View v) {
            View isoffice = View.inflate(mContext, R.layout.ishaveofficeapp, null);
            // 这里来个系统提醒
            BSDialog bsd = new BSDialog(mContext, "北企星系统提醒您!", isoffice, new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent i = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://mo.wps.cn/office-for-android/index.html"));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
            });
            // TODO Auto-generated method stub
            final ViewHolder holder = (ViewHolder) v.getTag();
            maFiles = mList.get(holder.position);
            sdcardurlfile = maFiles.getTitle() + maFiles.getSharedid() + "." +
                    maFiles.getFilepath().substring(maFiles.getFilepath().lastIndexOf(".") + 1);
            holder.mMoreLy.setVisibility(View.GONE);
            holder.mMoreImg.setBackgroundResource(R.drawable.filedonw_arrowdown);
            // TODO Auto-generated method stub
            if (maFiles.getExtension().equals("5")) {
                CustomToast.showShortToast(mContext, "不支持打开zip文件哦~");
            }
            else if (maFiles.getExtension().equals("2") || maFiles.getExtension().equals("3")
                    || maFiles.getExtension().equals("4")) {
                // BSTextReadUtils
                if (BSTextReadUtils.isAvilible(mContext)) {
                    // mContext.startActivity(BSTextReadUtils.openFile(
                    // Environment.getExternalStorageDirectory()
                    // + "/BSYUNFILE/" + sdcardurlfile, mContext));
                    try {
                        mContext.startActivity(BSTextReadUtils.openFile(
                                Environment.getExternalStorageDirectory()
                                        + "/" + Constant.SDCARD_CACHE + sdcardurlfile, mContext));
                    }

                    catch (Exception e) {
                        CustomToast.showShortToast(mContext, "文件缺损，无法查看");
                    }

                }
                else {
                    bsd.show();
                }
            }
            else {
                try {
                    // mContext.startActivity(BSTextReadUtils.openFile(
                    // Environment.getExternalStorageDirectory()
                    // + "/BSYUNFILE/" + sdcardurlfile, mContext));
                    mContext.startActivity(BSTextReadUtils.openFile(
                            Environment.getExternalStorageDirectory()
                                    + "/" + Constant.SDCARD_CACHE + sdcardurlfile, mContext));
                } catch (Exception e) {
                    // CustomToast.showShortToast(mContext, "报错了哈哈哈哈~");
                    bsd.show();
                }
            }
        }

    }

    // private void updateProgressPartly(int progress, int position) {
    // int firstVisiblePosition = listview.getFirstVisiblePosition();
    // int lastVisiblePosition = listview.getLastVisiblePosition();
    // if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
    // View view = listview.getChildAt(position - firstVisiblePosition);
    // if (view.getTag() instanceof ViewHolder) {
    // ViewHolder vh = (ViewHolder) view.getTag();
    // CustomLog.e("a23", position + "");
    // mList.get(position).setDownloadhaving("1");
    // mList.get(position).setProgresslong(progress + "");
    // vh.grossview.setProgress(progress);
    // }
    // }
    // }

    /*-------------------------------------------------------------------------------------------------*/
    // private void updateProgressPartly(final int positionInAdapter, String progresslong)
    // {
    //
    // if (positionInAdapter >= listview.getFirstVisiblePosition() && positionInAdapter <=
    // listview.getLastVisiblePosition())
    // {
    // int positionInListView = positionInAdapter - listview.getFirstVisiblePosition();
    // BSCirleDwLoadProgrossView item = (BSCirleDwLoadProgrossView)
    // listview.getChildAt(positionInListView).findViewById(R.id.roundProgressBar5);
    //
    // if (item != null) {
    // mList.get(positionInAdapter).setDownloadhaving("1");
    // item.setVisibility(View.VISIBLE);
    // item.setProgress(Integer.parseInt(progresslong));
    // mList.get(positionInAdapter).setProgresslong(progresslong);
    // }
    // }
    // }
    public class fileOnclickIsCanleDownload implements OnClickListener {
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            View nodown = View.inflate(mContext, R.layout.extshardfilesdhomemyupdateapternodonw,
                    null);
            BSDialog bsd = new BSDialog(mContext, "系统提醒", nodown, new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    // if (handler != null) {
                    // handler.cancel();
                    // mContext.finish();
                    // }
                    try {
                        for (int i = 0; i < downcount; i++) {
                            DownloadManager.getInstance().removeDownload(i);
                        }
                    }
                    catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mContext.finish();
                }
            });
            bsd.show();
        }
    }

}
