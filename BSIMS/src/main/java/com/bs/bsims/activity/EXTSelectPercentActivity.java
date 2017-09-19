
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant4TaskEventPath;
import com.bs.bsims.receiver.TaskActionReceiver;
import com.bs.bsims.receiver.TaskScheduleReceiver;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

/**
 * 选择进度页面
 * 
 * @author Administrator
 */
public class EXTSelectPercentActivity extends BaseActivity implements
        android.view.View.OnClickListener {

    private Context context = EXTSelectPercentActivity.this;

    /** 减少进度 */
    // @ViewInject(R.id.img_taskevent_select_reduce)
    // private ImageView img_taskevent_select_reduce;

    /** 增加进度 */
    // @ViewInject(R.id.img_taskevent_select_add)
    // private ImageView img_taskevent_select_add;

    /** 进度值 */
    @ViewInject(R.id.txt_taskevent_select_percent)
    private TextView txt_taskevent_select_percent;

    /** 选择进度时的描述 */
    @ViewInject(R.id.edit_taskevent_select_progressDescription)
    private EditText editProgressDescription;

    /** 选择值 */
    private String schedule;

    /** 任务事件id */
    private String taskid;

    private int percentValue = 0;

    private int basei = 0;
    private int i = 0;
    int type = 0;

    private boolean isTabTask = false;
    private boolean isTabHBTask = false;

    private SeekBar seekbar = null;

    @Override
    public void initView() {
        initHeadView();
        // img_taskevent_select_reduce.setOnClickListener(this);
        // img_taskevent_select_add.setOnClickListener(this);
        //
        // if (i == 0) {
        // img_taskevent_select_reduce.setImageResource(R.drawable.ic_taskevent_reduce_n);
        // img_taskevent_select_add.setImageResource(R.drawable.ic_taskevent_add_p);
        // } else if (i == 10) {
        // img_taskevent_select_reduce.setImageResource(R.drawable.ic_taskevent_reduce_p);
        // img_taskevent_select_add.setImageResource(R.drawable.ic_taskevent_add_n);
        // } else {
        // img_taskevent_select_reduce.setImageResource(R.drawable.ic_taskevent_reduce_p);
        // img_taskevent_select_add.setImageResource(R.drawable.ic_taskevent_add_p);
        // }

        txt_taskevent_select_percent.setText(percentValue + "%");
        seekbar = (SeekBar) super.findViewById(R.id.seekbar);
        seekbar.setProgress(percentValue);

    }

    public void sendBoardMessage() {
        Intent intent = new Intent(TaskActionReceiver.intentFilter);
        intent.putExtra("type", type);
        intent.putExtra("task_id", taskid);
        intent.putExtra("isAction", true);
        intent.putExtra("schedule", i + "");

        isTabHBTask = getIntent().getBooleanExtra("isTabHBTask", false);
        isTabTask = getIntent().getBooleanExtra("isTabTask", false);

        intent.putExtra("isTabHBTask", isTabHBTask);
        intent.putExtra("isTabTask", isTabTask);
        /** 1 我发布的 2 我负责的 3 我跟进的 4 我跟进的 */
        sendBroadcast(intent);

    }

    public void sendScheduleBoardMessage() {
        Intent intent = new Intent(TaskScheduleReceiver.intentFilter);
        intent.putExtra("change", true);
        intent.putExtra("schedule", i + "");
        intent.putExtra("status", "0");
        intent.putExtra("task_id", taskid);
        sendBroadcast(intent);

    }

    protected void initHeadView() {
        // findViewById(R.id.relative_comm_head_back).setOnClickListener(headBackListener);
        findViewById(R.id.img_head_back).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        ((TextView) findViewById(R.id.txt_comm_head_activityName))
                .setText("选择进度");

        ((TextView) findViewById(R.id.txt_comm_head_right)).setText("确定");

        findViewById(R.id.txt_comm_head_right).setOnClickListener(
                new RightListener());
    }

    class RightListener implements View.OnClickListener {

        @Override
        public void onClick(View paramView) {

            // 判断
            if (basei == i || basei * 10 == i) {
                CustomToast.showShortToast(context, "任务进度没有发生变化");
                return;
            }
            if (i < basei * 10) {
                CustomToast.showShortToast(context, "不能低于当前进度");
                return;
            }

            String des = editProgressDescription.getText().toString().trim();
            if (null == des || des.equals("")) {
                CustomToast.showShortToast(context, "请填写描述信息");
                return;
            }

            requestNet();

        }

    }

    protected void requestNet() {

        CustomDialog.showProgressDialog(context);
        findViewById(R.id.txt_comm_head_right).setOnClickListener(null);

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("uid", BSApplication.getInstance().getUserId());
        paramsMap.put("userid", BSApplication.getInstance().getUserId());
        paramsMap.put("ftoken", BSApplication.getInstance().getmCompany());
        // paramsMap.put("uid", "22");
        // paramsMap.put("userid", "22");
        // paramsMap.put("ftoken", "RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O");
        paramsMap.put("taskid", taskid);
        // paramsMap.put("schedule", i * 10 + "");
        paramsMap.put("schedule", i + "");
        paramsMap.put("description", editProgressDescription.getText()
                .toString().trim());

        // ftoken 公司标识
        // taskid 任务id
        // uid 登录用户id
        // description 描述
        // schedule 进度
        String url = BSApplication.getInstance().getHttpTitle()
                + Constant4TaskEventPath.TASKEVENT_PERCENT_PATH;

        new HttpUtilsByPC().sendPostBYPC(url, paramsMap,
                /**
                 * @author Administrator
                 */
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        CustomDialog.closeProgressDialog();
                        CustomToast.showNetErrorToast(context);
                        findViewById(R.id.txt_comm_head_right).setOnClickListener(
                                new RightListener());
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo rstr) {
                        // TODO Auto-generated method stub
                        // 做假显示了
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
                                CustomToast.showShortToast(context, str);
                                sendScheduleBoardMessage();
                                sendBoardMessage();
                                finish();

                            } else {
                                CustomToast.showShortToast(context, str);
                            }

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            CustomDialog.closeProgressDialog();
                            findViewById(R.id.txt_comm_head_right).setOnClickListener(
                                    new RightListener());
                        }

                    }

                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        // case R.id.img_taskevent_select_reduce:// 减少进度
        //
        // reducePercent();
        //
        // break;
        // case R.id.img_taskevent_select_add:// 增加进度
        //
        // addPercent();
        //
        // break;

        }
    }

    /**
     * 减少进度
     */
    private void reducePercent() {
        // img_taskevent_select_add.setClickable(true);
        //
        // if (i == 0) {
        //
        // img_taskevent_select_reduce.setClickable(false);
        // img_taskevent_select_reduce.setImageResource(R.drawable.ic_taskevent_reduce_n);
        // img_taskevent_select_add.setImageResource(R.drawable.ic_taskevent_add_p);
        //
        // return;
        //
        // } else {
        // img_taskevent_select_add.setImageResource(R.drawable.ic_taskevent_add_p);
        // }
        //
        // i--;// 为了一开始给的进度为0,所以写在这个地方
        // if (i == 0)
        // img_taskevent_select_reduce.setImageResource(R.drawable.ic_taskevent_reduce_n);
        //
        // txt_taskevent_select_percent.setText(i * 10 + "%");
    }

    /**
     * 增加进度
     */
    private void addPercent() {
        // img_taskevent_select_reduce.setClickable(true);
        //
        // if (i == 10) {
        // img_taskevent_select_add.setClickable(false);
        // img_taskevent_select_add.setImageResource(R.drawable.ic_taskevent_add_n);
        // img_taskevent_select_reduce.setImageResource(R.drawable.ic_taskevent_reduce_p);
        // return;
        // } else {
        // img_taskevent_select_reduce.setImageResource(R.drawable.ic_taskevent_reduce_p);
        // }
        //
        // i++;// 为了一开始给的进度为100%,所以写在这个地方
        // if (i == 10)
        // img_taskevent_select_add.setImageResource(R.drawable.ic_taskevent_add_n);
        //
        // txt_taskevent_select_percent.setText(i * 10 + "%");
    }

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
//        setContentView(R.layout.ac_taskevent_select_progress);
        View.inflate(this, R.layout.ac_taskevent_select_progress,mContentLayout);
        x.view().inject(this);
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            taskid = intent.getStringExtra("id");
        }

        if (intent.hasExtra("schedule")) {
            schedule = intent.getStringExtra("schedule");
        }

        if (intent.hasExtra("type")) {
            type = intent.getIntExtra("type", 0);
        }

        percentValue = Integer.valueOf(schedule);
        i = percentValue / 10;
        basei = percentValue / 10;

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
        // TODO Auto-generated method stub
        seekbar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
    }

    OnSeekBarChangeListener mOnSeekBarChangeListener = new OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStartTrackingTouch(SeekBar arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int arg1, boolean arg2) {
            // TODO Auto-generated method stub
            // MainActivity.this.text.append("正在拖动,当前值:"+seekBar.getProgress()+"\n");
            i = seekBar.getProgress();
            if (i > 10) {
                int y = i % 10;
                int ten = i / 10;
                if (y > 5) {
                    i = (ten + 1) * 10;
                } else {
                    i = ten * 10;
                }
            } else {
                i = 10;
            }
            seekBar.setProgress(i);
            txt_taskevent_select_percent.setText(i + "%");

        }
    };

}
