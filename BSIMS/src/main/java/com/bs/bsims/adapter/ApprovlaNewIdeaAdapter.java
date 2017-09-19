
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;
import org.xutils.ex.HttpException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApprovlaNewIdeaAdapter extends BaseAdapter {

    public List<EmployeeVO> mList;
    private int mState;
    private Context mContext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private Boolean mAdd;
    private String statusType = "0";// 0是默认1是合同审批，回款审批
    public String approvalType;// 催办判断是哪种类型的审批
    public String approvalId;// 审批id

    private boolean mFalge = false;
    private int indexPostion;

    private boolean isViewCui;// 是否显示催办

    private String isCrm = "0";// 判断是否是来自CRM的催办(例如合同) 默认为0是来自于6大审批 1是来自CRM

    // CcAdapter state 参数为图片边缘的提醒的小图样式
    public ApprovlaNewIdeaAdapter(Context context) {
        // TODO Auto-generated constructor stub
        mList = new ArrayList<EmployeeVO>();
        mContext = context;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
    }

    public String getIsCrm() {
        return isCrm;
    }

    public void setIsCrm(String isCrm) {
        this.isCrm = isCrm;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.approval_person_list, null);
            holder.personHead = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.titleName = (TextView) convertView.findViewById(R.id.title_name);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.imageView = (ImageView) convertView.findViewById(R.id.is_approval);
            holder.mRemind = (TextView) convertView.findViewById(R.id.cui);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        EmployeeVO employeeVO = mList.get(position);
        mImageLoader.displayImage(employeeVO.getHeadpic(), holder.personHead, mOptions);
        holder.personHead.setUserId(employeeVO.getUserid());// HL:获取头像对应的用户ID，以便响应跳转
        holder.personHead.setUserName(employeeVO.getFullname());
        holder.personHead.setmImageLoader(mImageLoader);
        holder.personHead.setUrl(employeeVO.getHeadpic());
        holder.name.setText(employeeVO.getFullname());
        switch (Integer.parseInt(employeeVO.getStatus())) {
        // 未审核
            case 0:
                holder.imageView.setBackgroundResource(R.drawable.approval_wait);
                holder.content.setVisibility(View.GONE);
                holder.mRemind.setVisibility(View.VISIBLE);
                holder.time.setVisibility(View.GONE);
                if (!mFalge) {
                    indexPostion = position;
                    mFalge = true;
                }

                break;
            // 已审核
            case 1:

                holder.imageView.setBackgroundResource(R.drawable.common_ic_selected);
                holder.content.setVisibility(View.VISIBLE);
                holder.mRemind.setVisibility(View.GONE);
                holder.time.setVisibility(View.VISIBLE);
                break;
            // 驳回
            case 2:
                holder.imageView.setBackgroundResource(R.drawable.ic_download_cancel);
                holder.content.setVisibility(View.VISIBLE);
                holder.mRemind.setVisibility(View.GONE);
                holder.time.setVisibility(View.VISIBLE);
                break;

        }
        if (!TextUtils.isEmpty(employeeVO.getTime())) {
            holder.time.setVisibility(View.VISIBLE);
            holder.time.setText(DateUtils.parseDateDayAndHour(employeeVO.getTime()));
        }
        else {
            holder.time.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(employeeVO.getContent())) {
            holder.content.setVisibility(View.VISIBLE);
            holder.content.setText(employeeVO.getContent());
        }
        else {
            holder.content.setVisibility(View.GONE);
        }

        if ("1".equals(employeeVO.getStatus())) {
            holder.status.setTextColor(Color.parseColor("#00A9FE"));
            holder.status.setText("已批准");
            // 合同与回款
            if ("1".equals(this.getStatusType())) {
                holder.status.setText("已确定");
                holder.titleName.setText("审核人：");
            }
        }
        else if ("2".equals(employeeVO.getStatus())) {
            holder.status.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.status.setText("驳回");
        }

        else {
            if (!TextUtils.isEmpty(employeeVO.getTime())) {
                holder.status.setText("审核中");
                holder.status.setTextColor(mContext.getResources().getColor(R.color.C6));
            }
            else {
                holder.status.setText("未审核");
                holder.status.setTextColor(mContext.getResources().getColor(R.color.C6));
            }

        }

        if (!isViewCui) {
            if(employeeVO.getStatus().equals("0")){
                holder.mRemind.setVisibility(View.VISIBLE);
            }
        }
        else {
            holder.mRemind.setVisibility(View.GONE);// true的时候就不显示
        }
        if (position > indexPostion) {
            if (holder.mRemind.getVisibility() != View.GONE) {
                holder.mRemind.setVisibility(View.GONE);
            }
        }

        // 判断催办按钮显示问题

        holder.mRemind.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (holder.mRemind.getTag() != null && holder.mRemind.getTag().equals("1")) {
                    CustomToast.showLongToast(mContext, "已经催办过一次了哦~");
                    return;
                }

                if (isCrm.equals("0")) {
                    // 审批催办
                    commitApproal(holder);
                }
                else {
                    // CRM合同催办
                    commitCRM(holder);
                }

            }
        });

        return convertView;
    }

    static class ViewHolder
    {
        private BSCircleImageView personHead;
        private TextView name, status, content, titleName, time, mRemind;
        private ImageView imageView;
    }

    public void updateData(List<EmployeeVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(String approvalType) {
        this.approvalType = approvalType;
    }

    public String getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(String approvalId) {
        this.approvalId = approvalId;
    }

    public boolean isViewCui() {
        return isViewCui;
    }

    public void setViewCui(boolean isViewCui) {
        this.isViewCui = isViewCui;
    }

    public void commitApproal(final ViewHolder holder) {
        CustomDialog.showProgressDialog(mContext, "催办中..");
        final String url = BSApplication.getInstance().getHttpTitle() + Constant.APPROVAL_REMIND;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("ftoken", BSApplication.getInstance().getmCompany());
        map.put("userid", BSApplication.getInstance().getUserId());
        map.put("typeid", approvalType);
        map.put("alid", approvalId);
        new HttpUtilsByPC().sendPostBYPC(url, map,
                /**
                 * @author Administrator
                 */
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        CustomToast.showLongToast(mContext, "网络异常");
                        CustomDialog.closeProgressDialog();
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo rstr) {
                        // TODO Auto-generated method stub
                        // 做假显示了
                        CustomDialog.closeProgressDialog();
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(new String(rstr.result.toString()));
                            String str = (String) jsonObject.get("retinfo");
                            String code = (String) jsonObject.get("code");
                            CustomToast.showLongToast(mContext, str);
                            if (Constant.RESULT_CODE.equals(code)) {
                                holder.mRemind.setTag("1");
                            }
                            CustomToast.showLongToast(mContext, str);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                });
    }

    public void commitCRM(final ViewHolder holder) {
        CustomDialog.showProgressDialog(mContext, "催办中..");
        final String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_REMIND;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("ftoken", BSApplication.getInstance().getmCompany());
        map.put("userid", BSApplication.getInstance().getUserId());
        map.put("hid", approvalId);
        new HttpUtilsByPC().sendPostBYPC(url, map,
                /**
                 * @author Administrator
                 */
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        CustomToast.showLongToast(mContext, "网络异常");
                        CustomDialog.closeProgressDialog();
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo rstr) {
                        // TODO Auto-generated method stub
                        // 做假显示了
                        CustomDialog.closeProgressDialog();
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(new String(rstr.result.toString()));
                            String str = (String) jsonObject.get("retinfo");
                            String code = (String) jsonObject.get("code");
                            CustomToast.showLongToast(mContext, str);
                            if (Constant.RESULT_CODE.equals(code)) {
                                holder.mRemind.setTag("1");
                            }
                            CustomToast.showLongToast(mContext, str);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                });
    }

}
