
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.AddByDepartmentCRMActivity;
import com.bs.bsims.activity.AddByPersonActivity;
import com.bs.bsims.activity.CrmJiontFollowupActivity;
import com.bs.bsims.activity.HeadActivity;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HeadCRMAdapter extends BaseAdapter {
    public List<EmployeeVO> mList;
    private int mState;
    private Context mContext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private boolean mAdd;
    private boolean approval = false;
    private boolean proven = false;
    private String status;

    private int requesKey;

    // 单选
    private String isone;
    public List<EmployeeVO> mEmployeeVOList;

    // 只显示一行数据
    private boolean mOneItem = false;

    public boolean isProven() {
        return proven;
    }

    public void setProven(boolean proven) {
        this.proven = proven;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }

    // CcAdapter state 参数为图片边缘的提醒的小图样式
    public HeadCRMAdapter(Context context, int state) {
        // TODO Auto-generated constructor stub
        mList = new ArrayList<EmployeeVO>();
        mState = state;
        mContext = context;
        mImageLoader = ImageLoader.getInstance();
    }

    public HeadCRMAdapter(Context context, boolean add, int crmJionrequesKey) {
        mContext = context;
        mAdd = add;
        mList = new ArrayList<EmployeeVO>();
        requesKey = crmJionrequesKey;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
    }

    public HeadCRMAdapter(Context context, boolean add, String isone) {
        mContext = context;
        mAdd = add;
        mList = new ArrayList<EmployeeVO>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
        this.isone = isone;
    }

    public HeadCRMAdapter(Context context, boolean add, boolean oneItme) {
        mContext = context;
        mAdd = add;
        mList = new ArrayList<EmployeeVO>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
        mOneItem = oneItme;
    }

    @Override
    public int getCount() {
        if (mAdd) {
            return (mList.size() + 1);
        } else if (mOneItem) {
            if (mList.size() >= 5) {
                return 5;
            } else {
                return mList.size();
            }

        } else {
            return mList.size();
        }
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.gv_head_icon_item, null);
            holder.itmeLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
            holder.personHead = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.personName = (TextView) convertView.findViewById(R.id.person_name);
            holder.deleteIcon = (ImageView) convertView.findViewById(R.id.delete_icon);
            holder.go = (TextView) convertView.findViewById(R.id.go_bt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.personHead.setSemicircleNumber("");
        }

        if (mAdd) {
            if (mList.size() == position) {
                if ("1".equals(isone)) {
                    if (mList.size() == 1) {
                        holder.personHead.setVisibility(View.GONE);
                        holder.personName.setVisibility(View.GONE);
                    } else {
                        holder.personHead.setImageResource(R.drawable.add_persion);
                        holder.personHead.setBackground(null);
                        holder.personName.setVisibility(View.GONE);
                    }
                } else {
                    holder.personHead.setBackground(null);
                    holder.personHead.setImageResource(R.drawable.add_persion);
                    holder.personName.setVisibility(View.GONE);

                }
                holder.deleteIcon.setVisibility(View.GONE);
            } else {
                EmployeeVO infoVO = mList.get(position);
                mImageLoader.displayImage(infoVO.getHeadpic(), holder.personHead,
                        CommonUtils.initImageLoaderOptions());
                holder.personHead.setUserId(infoVO.getUserid());//HL
                holder.personName.setText(infoVO.getFullname());
                holder.personName.setVisibility(View.VISIBLE);
                holder.deleteIcon.setVisibility(View.VISIBLE);
                if ("1".equals(isone)) {
                    holder.deleteIcon.setVisibility(View.GONE);
                }
            }

        } else {
            // 详情页面只显示一行，点击跳转界面显示更多
            if (mOneItem) {
                if (position >= 4) {
                    holder.go.setVisibility(View.VISIBLE);
                    holder.itmeLayout.setVisibility(View.GONE);
                }
            }

            EmployeeVO infoVO = mList.get(position);
            mImageLoader.displayImage(infoVO.getHeadpic(), holder.personHead, mOptions);
            holder.personName.setText(infoVO.getFullname());
            holder.personHead.setUserId(infoVO.getUserid());//HL
            if (infoVO.getIsread() != null) {
                if ("1".equals(infoVO.getIsread())) {
                    holder.personHead.setColor(3);
                    holder.personHead.setSemicircleNumber("阅");
                }
            }

            if (approval) {

                if ("1".equals(infoVO.getStatus())) {
                    holder.personHead.setColor(1);
                    holder.personHead.setSemicircleNumber("批");
                } else if ("2".equals(infoVO.getStatus())) {
                    holder.personHead.setColor(2);
                    holder.personHead.setSemicircleNumber("否");
                } else {
                    holder.personHead.setColor(3);
                    holder.personHead.setSemicircleNumber((position + 1) + "");
                }

            }

            if (proven) {
                if ("1".equals(infoVO.getProven())) {
                    holder.personHead.setColor(1);
                    holder.personHead.setSemicircleNumber("证");
                }
            }
        }

        // holder.itmeLayout.setOnLongClickListener(new HeadLongTouchListener());
        if (mAdd) {
            if (mList.size() == position) {
                holder.personHead.setOnClickListener(new HeadLongTouchListener(null, position));
            } else {
                holder.personHead.setOnClickListener(new HeadLongTouchListener(mList.get(position),
                        position));
            }
        }

        holder.go.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("head_list", (Serializable) mList);
                intent.putExtra("title", "知会人");
                intent.setClass(mContext, HeadActivity.class);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder
    {
        private BSCircleImageView personHead;
        private TextView personName, go;
        private LinearLayout itmeLayout;
        private ImageView deleteIcon;
    }

    public void updateData(List<EmployeeVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    private class HeadLongTouchListener implements OnLongClickListener, OnClickListener {
        private Activity context;
        private int position;
        private EmployeeVO employeeVO;

        public HeadLongTouchListener(EmployeeVO employeeVO, int position) {
            this.context = (Activity) mContext;
            this.position = position;
            this.employeeVO = employeeVO;
        }

        @Override
        public boolean onLongClick(View arg0) {
            return false;
        }

        @Override
        public void onClick(View arg0) {
            if ("1".equals(isone)) {
                Intent intent = new Intent();
                intent.setClass(context, AddByPersonActivity.class);
                intent.putExtra("requst_number", 2014);
                intent.putExtra("checkboxlist", (Serializable) getmEmployeeVOList());
                intent.putExtra("is_new_data", true);
                context.startActivityForResult(intent, 2014);
            } else {
                if (employeeVO == null) {
                    Intent intent = new Intent();
                    intent.setClass(context, AddByDepartmentCRMActivity.class);
                    intent.putExtra("employ_name", CrmJiontFollowupActivity.class);
                    intent.putExtra("checkboxlist", (Serializable) mList);
                    // 表示选择的联合跟进人
                    if (requesKey == 1) {
                        intent.putExtra("requst_number", 11);
                        context.startActivityForResult(intent, 11);

                    }
                    // 表示选择相关人
                    else if (requesKey == 2) {
                        intent.putExtra("requst_number", 10);
                        context.startActivityForResult(intent, 10);
                    }
                } else {
                    mList.remove(employeeVO);
                    notifyDataSetChanged();
                }
            }
        }
    }

    public boolean ismAdd() {
        return mAdd;
    }

    public void setmAdd(boolean mAdd) {
        this.mAdd = mAdd;
    }

    public List<EmployeeVO> getmEmployeeVOList() {
        return mEmployeeVOList;
    }

    public void setmEmployeeVOList(List<EmployeeVO> mEmployeeVOList) {
        this.mEmployeeVOList = mEmployeeVOList;
    }

}
