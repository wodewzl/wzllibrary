
package com.bs.bsims.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

import com.bs.bsims.R;
import com.bs.bsims.activity.ApprovalAttendanceDetailActivity;
import com.bs.bsims.activity.ApprovalCustomDetailActivity;
import com.bs.bsims.activity.ApprovalFeeApplyDetailActivity;
import com.bs.bsims.activity.ApprovalLeaveDetailActivity;
import com.bs.bsims.activity.ApprovalOvertimeDetailActivity;
import com.bs.bsims.activity.ApprovalSuppliesDetailActivity;
import com.bs.bsims.activity.CreativeIdeaDetailActivity;
import com.bs.bsims.activity.CrmApprovalDetailActivity;
import com.bs.bsims.activity.CrmBusinessHomeIndexOneInfo;
import com.bs.bsims.activity.CrmClientHomeActivity;
import com.bs.bsims.activity.CrmContactDetailActivity;
import com.bs.bsims.activity.CrmHighseasClientsHomeActivity;
import com.bs.bsims.activity.CrmTradeContantDeatilsHomeTop3Activity;
import com.bs.bsims.activity.CrmTradeContantDetailsIndexActivity;
import com.bs.bsims.activity.CrmVisitRecordDetailActivity;
import com.bs.bsims.activity.EXTTaskEventDetailsActivity;
import com.bs.bsims.activity.JournalPublishActivity;
import com.bs.bsims.activity.JournalPublishDetailActivity;
import com.bs.bsims.activity.LoginActivity;
import com.bs.bsims.activity.NoticeDetailActivity;
import com.bs.bsims.activity.RedMoneyActivity;
import com.bs.bsims.activity.ScheduleDetailActivity;
import com.bs.bsims.activity.SignInActivity;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.constant.ExtrasBSVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DataCleanUtil;
import com.bs.bsims.view.BSDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

/**
 * 自定义接收器 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    public static final String MESSAGE_RECEIVED_ACTION = "com.bs.bsims.MESSAGE_RECEIVED_ACTION";

    private String type = "4";
    private String detailid = "4";
    private String msgtype = "4";
    private String typename = "typename";
    // value:{"type":4,"detailid":12638,"msgtype":1,"typename":"BOSS批注"}
    private String typeTAG = "type";
    private String detailidTAG = "detailid";
    private BSDialog mDialog;

    @Override
    public void onReceive(Context context, Intent intent) {
        CommonUtils.sendBroadcast(context, Constant.HOME_MSG);
        Bundle bundle = intent.getExtras();

        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Gson gson = new Gson();
        Map<String, String> retMap = gson.fromJson(extra,
                new TypeToken<Map<String, String>>() {
                }.getType());

        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
                + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle
                    .getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            // send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

            // processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            // processCustomMessage(context, bundle);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            type = retMap.get(typeTAG);
            if (Integer.parseInt(type) == Constant.JPushTypeId.JPUSHTYPEID33 || Integer.parseInt(type) == Constant.JPushTypeId.JPUSHTYPEID34) {
                // new DataCleanUtil(context).cleanApplicationAllData(context);
                // Intent newIntent = new Intent();
                // newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // newIntent.setClass(context, LoginActivity.class);
                // context.startActivity(newIntent);
                showDialog(context);
                return;
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            // 打开自定义的Activity
            Intent goIntent = new Intent();
            goIntent.putExtras(bundle);
            goIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            type = retMap.get(typeTAG);
            detailid = retMap.get(detailidTAG);
            typename = retMap.get(typename);
            int intType = Integer.parseInt(type);

            /**
             * 是否有对应的适配 如果有适配则跳转，否则不处理
             */

            if (TextUtils.isEmpty(BSApplication.getInstance().getUserId())
                    || TextUtils.isEmpty(BSApplication.getInstance()
                            .getHttpTitle())) {
                goIntent.setClass(context, LoginActivity.class);
                context.startActivity(goIntent);
                return;
            }

            switch (intType) {
                case Constant.JPushTypeId.JPUSHTYPEID1:
                    goIntent.putExtra("articleid", detailid);
                    // goIntent.putExtra("logid", "412");
                    goIntent.putExtra("sortid", "3");
                    goIntent.setClass(context, NoticeDetailActivity.class);
                    break;
                case Constant.JPushTypeId.JPUSHTYPEID2:
                    goIntent.putExtra("articleid", detailid);
                    goIntent.putExtra("sortid", "11");
                    goIntent.setClass(context, NoticeDetailActivity.class);
                    break;
                case Constant.JPushTypeId.JPUSHTYPEID3:
                    goIntent.putExtra("articleid", detailid);
                    goIntent.putExtra("sortid", "19");
                    goIntent.setClass(context, NoticeDetailActivity.class);
                    break;
                case Constant.JPushTypeId.JPUSHTYPEID4:
                case Constant.JPushTypeId.JPUSHTYPEID5:
                    goIntent.putExtra("logid", detailid);
                    goIntent.setClass(context, JournalPublishDetailActivity.class);
                    break;

                case Constant.JPushTypeId.JPUSHTYPEID6:
                    goIntent.putExtra("sortid", "19");
                    goIntent.putExtra(ExtrasBSVO.Push.BREAK_ID, detailid);
                    goIntent.putExtra("type", 1);
                    goIntent.putExtra("isTabTask", true);
                    goIntent.setClass(context, EXTTaskEventDetailsActivity.class);
                    break;
                case Constant.JPushTypeId.JPUSHTYPEID7:
                    goIntent.putExtra("articleid", detailid);
                    goIntent.putExtra("id", detailid);
                    goIntent.putExtra("type", "7");
                    goIntent.putExtra("sortid", "19");
                    goIntent.putExtra("typename", typename);

                    goIntent.setClass(context, CreativeIdeaDetailActivity.class);
                    break;
                case Constant.JPushTypeId.JPUSHTYPEID8:
                    // 请假详情
                    goIntent.putExtra("alid", detailid);
                    goIntent.putExtra("uid", BSApplication.getInstance()
                            .getUserId());
                    goIntent.putExtra("type", "1");
                    goIntent.setClass(context, ApprovalLeaveDetailActivity.class);
                    break;
                case Constant.JPushTypeId.JPUSHTYPEID9:
                    // 物资详情
                    goIntent.putExtra("alid", detailid);
                    goIntent.putExtra("uid", BSApplication.getInstance()
                            .getUserId());
                    goIntent.putExtra("type", "2");
                    goIntent.setClass(context, ApprovalSuppliesDetailActivity.class);
                    break;

                case Constant.JPushTypeId.JPUSHTYPEID10:
                    // 加班详情 加班提醒
                    goIntent.putExtra("alid", detailid);
                    goIntent.putExtra("uid", BSApplication.getInstance()
                            .getUserId());
                    goIntent.putExtra("type", "3");
                    goIntent.setClass(context, ApprovalOvertimeDetailActivity.class);
                    break;
                case Constant.JPushTypeId.JPUSHTYPEID11:
                    // 费用申请详情
                    goIntent.putExtra("alid", detailid);
                    goIntent.putExtra("uid", BSApplication.getInstance()
                            .getUserId());
                    goIntent.putExtra("type", "4");
                    goIntent.setClass(context, ApprovalFeeApplyDetailActivity.class);
                    break;
                case Constant.JPushTypeId.JPUSHTYPEID12:
                    // 考勤申请详情
                    goIntent.putExtra("alid", detailid);
                    goIntent.putExtra("uid", BSApplication.getInstance()
                            .getUserId());
                    goIntent.putExtra("type", "5");
                    goIntent.setClass(context,
                            ApprovalAttendanceDetailActivity.class);
                    break;
                case Constant.JPushTypeId.JPUSHTYPEID13:
                    // 其它审批
                    goIntent.putExtra("alid", detailid);
                    goIntent.putExtra("uid", BSApplication.getInstance()
                            .getUserId());
                    goIntent.putExtra("type", "6");
                    goIntent.setClass(context,
                            ApprovalCustomDetailActivity.class);
                    break;

                case Constant.JPushTypeId.JPUSHTYPEID14:
                    // 为写日志提醒
                    goIntent.setClass(context, JournalPublishActivity.class);
                    break;

                case Constant.JPushTypeId.JPUSHTYPEID15:
                    // 打卡提醒
                    intent.setClass(context, SignInActivity.class);
                    break;

                case Constant.JPushTypeId.JPUSHTYPEID21:
                    // 日程提醒
                    goIntent.putExtra("id", detailid);
                    goIntent.setClass(context, ScheduleDetailActivity.class);
                    break;

                case Constant.JPushTypeId.JPUSHTYPEID22:
                    // 制度提醒
                    goIntent.putExtra("articleid", detailid);
                    goIntent.putExtra("sortid", "12");
                    goIntent.setClass(context, NoticeDetailActivity.class);
                    break;
                // 商机转移提醒
                case Constant.JPushTypeId.JPUSHTYPEID23:
                    goIntent.putExtra("bid", detailid);
                    goIntent.putExtra("stateUtilthread", "1");
                    goIntent.setClass(context, CrmBusinessHomeIndexOneInfo.class);
                    break;
                // 合同到期提醒
                case Constant.JPushTypeId.JPUSHTYPEID24:
                    goIntent.putExtra("hid", detailid);
                    goIntent.setClass(context, CrmTradeContantDeatilsHomeTop3Activity.class);
                    break;
                // CRM客户提醒
                case Constant.JPushTypeId.JPUSHTYPEID25:
                    goIntent.putExtra("cid", detailid);
                    goIntent.putExtra("jpush", true);
                    String isPub = retMap.get("isPub");
                    if ("1".equals(isPub)) {
                        if ("1".equals(CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL004))) {
                            goIntent.setClass(context, CrmClientHomeActivity.class);
                        } else {
                            goIntent.setClass(context, CrmHighseasClientsHomeActivity.class);
                        }
                    } else {
                        goIntent.setClass(context, CrmClientHomeActivity.class);
                    }
                    break;
                // 回款审批
                case Constant.JPushTypeId.JPUSHTYPEID26:
                    goIntent.putExtra("mid", detailid);
                    goIntent.setClass(context, CrmApprovalDetailActivity.class);
                    break;
                // 日志评论
                case Constant.JPushTypeId.JPUSHTYPEID27:
                    goIntent.putExtra("logid", detailid);
                    goIntent.setClass(context, JournalPublishDetailActivity.class);
                    break;
                // 日志抄送
                case Constant.JPushTypeId.JPUSHTYPEID28:
                    goIntent.putExtra("logid", detailid);
                    goIntent.setClass(context, JournalPublishDetailActivity.class);
                    break;
                // 打卡机离线提醒
                case Constant.JPushTypeId.JPUSHTYPEID29:
                    break;
                // 联系人提醒
                case Constant.JPushTypeId.JPUSHTYPEID30:
                    goIntent.putExtra("lid", detailid);
                    goIntent.setClass(context, CrmContactDetailActivity.class);
                    break;
                // CRM跟单提醒
                case Constant.JPushTypeId.JPUSHTYPEID31:
                    goIntent.putExtra("vid", detailid);
                    goIntent.setClass(context, CrmVisitRecordDetailActivity.class);
                    break;
                // CRM合同审批
                case Constant.JPushTypeId.JPUSHTYPEID32:
                    goIntent.putExtra("hid", detailid);
                    goIntent.setClass(context, CrmTradeContantDetailsIndexActivity.class);
                    break;

                // 离职推送
                case Constant.JPushTypeId.JPUSHTYPEID34:
                case Constant.JPushTypeId.JPUSHTYPEID33:
                    showDialog(context);
                    break;

                case Constant.JPushTypeId.JPUSHTYPEID35:
                    goIntent.setClass(context, RedMoneyActivity.class);
                    break;
                default:
                    break;
            }
            if (goIntent.getComponent() == null)
                return;
            context.startActivity(goIntent);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
                .getAction())) {
            Log.d(TAG,
                    "[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
                            + bundle.getString(JPushInterface.EXTRA_EXTRA));
            // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
            // 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
                .getAction())) {
            boolean connected = intent.getBooleanExtra(
                    JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction()
                    + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    public void showDialog(final Context context) {
//        View view = View.inflate(context, R.layout.dialog_lv_item, null);
//        TextView tv = (TextView) view.findViewById(R.id.textview);
//        tv.setText("密码已被修改，请重新登录");
//        mDialog = new BSDialog(context, "警告", view, new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                new DataCleanUtil(context).cleanApplicationAllData(context);
//                Intent newIntent = new Intent();
//                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                newIntent.setClass(context, LoginActivity.class);
//                context.startActivity(newIntent);
//                mDialog.dismiss();
//            }
//        });
//        mDialog.show();
//        mDialog.setButtonTwoGone(true);
        
        new DataCleanUtil(context).cleanApplicationAllData(context);
        Intent newIntent = new Intent();
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        newIntent.setClass(context, LoginActivity.class);
        newIntent.putExtra("hiddenpwd",true);
        newIntent.putExtra("jupshpwd",true);
        context.startActivity(newIntent);
    }
}
