
package com.bs.bsims.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.constant.Constant4Sharedfiles;
import com.bs.bsims.download.domain.DownloadFile;
import com.bs.bsims.download.domain.ViewCache;
import com.bs.bsims.model.ContactObject;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.receiver.FileDownloadSuccessReceiver;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.view.Cc;
import com.bs.bsims.xutils.impl.CommSubmitFileUtils;
import com.bs.bsims.xutils.impl.RequestCallBackPCFile;
 

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author peck
 * @Description: 文件上传 这里需要从文件列表中进入
 * @date 2015-6-13 下午2:55:20
 * @email 971371860@qq.com
 * @version V1.0
 */
public class UploadDetailActivity extends BaseActivity implements
        OnClickListener {

    private static final String TAG = "UploadDetailActivity";
    private Context mAppContext;
    protected Context mContext;
    private DownloadManager downloadManager;

    @ViewInject(R.id.linear_download_detail_fileNotDownloadLayout)
    private View layout_fileNotDownloadLayout;

    @ViewInject(R.id.rl_download_detail_fileDownloadingLayout)
    private View layout_fileDownloadingLayout;

    @ViewInject(R.id.ui_download_detail_cc)
    private Cc cc;

    @ViewInject(R.id.progress_downloadProgress)
    private ProgressBar progressBar;

    @ViewInject(R.id.txt_download_detail_downloadDes)
    private TextView txt_downloadDes;
    private final int progressWidth = 1000; // 总长度

    private String saveFileName; // 保存的路径，包含文件名称

    // 下载的路径
    private String fileDownloadPath; // 下载的路径

    // 后缀名
    private String suffix;

    // 不含后缀的名称
    private String fileName;

    private String sharedid;
    private DownloadFile downloadFile;

    @ViewInject(R.id.img_download_detail_icon)
    private ImageView imgIcon;

    @ViewInject(R.id.txt_download_detail_name)
    private TextView txtName;

    @ViewInject(R.id.txt_download_detail_des)
    private TextView txtDes;

    @ViewInject(R.id.btn_download_detail_downloadFile)
    private Button mBtn_UpLoad;

    private String ulr;

    protected SharedPreferences mSettings;

    private RadioButton rgp_open;
    private RadioButton rgp_not_open;

    /**
     * 是否公开（1公开，2不公开）
     */
    private String open;

    @Override
    public void initView() {
        findViewById(R.id.img_head_back).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });

        ((TextView) findViewById(R.id.txt_comm_head_activityName))
                .setText("文件上传");

        mBtn_UpLoad.setText("上传文件");
        imgIcon.setBackgroundColor(Color.TRANSPARENT);
        imgIcon.setImageResource(R.drawable.ic_upload_file);

        ulr = getIntent().getExtras().getString("url");
        txtDes.setText("这是您要上传的文件");
        /**
         * 与后台协调一致为最大10M
         */
        txtDes.setText("文件大小不要超过10M");
        txtName.setText(ulr);

        progressBar.setMax(progressWidth);

        ViewCache.progressBar = progressBar;
        ViewCache.txt_downloadDes = txt_downloadDes;
        ViewCache.layout_fileNotDownloadLayout = layout_fileNotDownloadLayout;
        ViewCache.layout_fileDownloadingLayout = layout_fileDownloadingLayout;
        // ViewCache.img_downloadCancel = img_downloadCancel;
        ViewCache.btnDownloadFile = mBtn_UpLoad;

        // 抄送控件
        cc.setCallback(UploadDetailActivity.this, 2014);

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);

        rgp_open = (RadioButton) findViewById(R.id.ac_download_upload_detail_rgp_open);
        rgp_not_open = (RadioButton) findViewById(R.id.ac_download_upload_detail_rgp_not_open);
    }

    /**
     * 抄送人员的List
     */
    private List<EmployeeVO> mDataListCc = new ArrayList<EmployeeVO>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2014) {
            if (resultCode == 2014) {
                mDataListCc = (List<EmployeeVO>) data
                        .getSerializableExtra("checkboxlist");
                cc.setDataList(mDataListCc);
            }
        }
    }

    public void commitFile() {

        Map<String, String> map = new HashMap<String, String>();
        // map.put("userid", UserManager.getLoginUser(mSettings).getUserid());
        /**
         * edit bypc
         */
        map.put("userid", BSApplication.getInstance().getUserId());

        if (rgp_open.isChecked()) {
            open = "1";
        } else if (rgp_not_open.isChecked()) {
            open = "2";
        }
        map.put("open", open);

        if (mDataListCc.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (EmployeeVO employee : mDataListCc) {
                sb.append(employee.getUserid()).append(",");
            }
            map.put("recipient", sb.toString());
        }

        List<ContactObject> list = new ArrayList<ContactObject>();
        ContactObject item = new ContactObject();

        item.setPortraitPath(ulr);
        item.setPortraitName("test");
        list.add(item);
        String urlStr = BSApplication.getInstance().getHttpTitle() +
                Constant4Sharedfiles.INSERT_PATH;
        
        
        
        new CommSubmitFileUtils(mSettings).submitFile(mAppContext, urlStr, map, list, new RequestCallBackPCFile(){
            
            @Override
            public void onSuccess(String arg0) {
                // TODO Auto-generated method stub
                String str = arg0;

                try {

                    JSONObject object = new JSONObject(str);

                    // boolean flag = object.getBoolean("return");
                    boolean flag = false;
                    if (Constant.RESULT_CODE.equals(object.getString("code"))) {
                        flag = true;
                    }

                    if (flag) {
                        txtDes.setText("上传成功");
                        mBtn_UpLoad.setVisibility(View.INVISIBLE);
                        layout_fileNotDownloadLayout
                                .setVisibility(View.VISIBLE);
                        layout_fileDownloadingLayout
                                .setVisibility(View.INVISIBLE);

                        sendUploadSuccessBroadcast();
                    } else {
                        mBtn_UpLoad.setVisibility(View.VISIBLE);
                        layout_fileNotDownloadLayout
                                .setVisibility(View.VISIBLE);
                        layout_fileDownloadingLayout
                                .setVisibility(View.INVISIBLE);
                        // img_downloadCancel.setVisibility(View.INVISIBLE);
                        mBtn_UpLoad.setText("重新上传");
                    }

                    CustomToast.showShortToast(
                            UploadDetailActivity.this,
                            object.getString("retinfo"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void sendUploadSuccessBroadcast() {
                Intent intent = new Intent(
                        FileDownloadSuccessReceiver.receiverAction);
                intent.putExtra(
                        FileDownloadSuccessReceiver.keyUploadFile, "");
                mContext.sendBroadcast(intent);
            }

            
            @Override
            public void onFinished() {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                CustomToast.showShortToast(mContext, "文件上传出错，请重试!");

                // 上传出错，将页面还原成初始化状态
                mBtn_UpLoad.setText("上传文件");
                mBtn_UpLoad.setVisibility(View.VISIBLE);
                layout_fileNotDownloadLayout
                        .setVisibility(View.VISIBLE);
                layout_fileDownloadingLayout.setVisibility(View.GONE);
            }
            
            @Override
            public void onCancelled(CancelledException arg0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void onLoading(long total, long current, boolean arg2) {
                // TODO Auto-generated method stub
                // 进度条展示
                long currentProgress = progressWidth * current / total;
                ViewCache.progressBar
                        .setProgress((int) currentProgress);

                // 文字进度展示
                if (total / 1024 / 1024 > 1) { // 大于1M时
                    BigDecimal base = new BigDecimal(1024 * 1024);
                    BigDecimal curr = new BigDecimal(current);
                    BigDecimal tot = new BigDecimal(total);

                    BigDecimal bigCurrent = curr.divide(base, 2,
                            BigDecimal.ROUND_HALF_UP);
                    BigDecimal bigTotal = tot.divide(base, 2,
                            BigDecimal.ROUND_HALF_UP);
                    ViewCache.txt_downloadDes.setText("上传中...("
                            + bigCurrent + "M/" + bigTotal + "M)");
                    CustomLog.v(TAG, "上传进度 ： " + "上传中...(" + bigCurrent
                            + "M/" + bigTotal + "M)");
                } else {
                    ViewCache.txt_downloadDes.setText("上传中...("
                            + current / 1024 + "kb/" + total / 1024
                            + "kb)");
                    CustomLog.v(TAG, "上传进度 ： " + "上传中...(" + current
                            / 1024 + "kb/" + total / 1024 + "kb)");
                }

            }


            @Override
            public void onStarted() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void onWaiting() {
                // TODO Auto-generated method stub
                
            }

         
        });
        
        
        
        
        
        
        
        
        
        
 

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_download_detail_downloadFile: {
                commitFile();

                // 显示隐藏界面
                mBtn_UpLoad.setVisibility(View.INVISIBLE);
                layout_fileNotDownloadLayout.setVisibility(View.INVISIBLE);
                layout_fileDownloadingLayout.setVisibility(View.VISIBLE);
                // img_downloadCancel.setVisibility(View.VISIBLE);

                break;
            }

        }

    }

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        setContentView(R.layout.ac_download_upload_detail);
        x.view().inject(this);
        mAppContext = getApplicationContext();
        mContext = this;
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        mBtn_UpLoad.setOnClickListener(this);
    }

}
