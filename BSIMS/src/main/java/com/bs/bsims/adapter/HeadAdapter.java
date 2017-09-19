
package com.bs.bsims.adapter;

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
import com.bs.bsims.activity.AddByDepartmentActivity;
import com.bs.bsims.activity.AddByDepartmentCRMActivity;
import com.bs.bsims.activity.AddByPersonActivity;
import com.bs.bsims.activity.HeadActivity;
import com.bs.bsims.activity.JournalPublishActivity;
import com.bs.bsims.imageLoad.ImageLoaderUtil;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HeadAdapter extends BaseAdapter {
    public List<EmployeeVO> mList;
    private int mState;
    private Context mContext;
    private ImageLoader mImageLoader;
    private boolean mAdd;
    private boolean approval = false;
    private boolean proven = false;
    private String status;

    // 单选
    private String isone;// 1代表单选，2代表关注

    // 只显示一行数据
    private boolean mOneItem = false;

    private ImageLoaderUtil mImageLoaderUtil;
    private String mType = "0";// 默认未通讯露数据,1为crm客户联合跟进人和责任人
    public List<EmployeeVO> mOneSelectList;

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
    public HeadAdapter(Context context, int state) {
        // TODO Auto-generated constructor stub
        mList = new ArrayList<EmployeeVO>();
        mState = state;
        mContext = context;
        mImageLoader = ImageLoader.getInstance();
    }

    public HeadAdapter(Context context, boolean add) {
        mContext = context;
        mAdd = add;
        mList = new ArrayList<EmployeeVO>();
        mImageLoader = ImageLoader.getInstance();
    }

    public HeadAdapter(Context context, boolean add, String isone) {
        mContext = context;
        mAdd = add;
        mList = new ArrayList<EmployeeVO>();
        mImageLoader = ImageLoader.getInstance();
        this.isone = isone;
    }

    public HeadAdapter(Context context, boolean add, String isone, String type) {
        mContext = context;
        mAdd = add;
        mList = new ArrayList<EmployeeVO>();
        mImageLoader = ImageLoader.getInstance();
        this.isone = isone;
        mType = type;
    }

    public HeadAdapter(Context context, boolean add, boolean oneItme) {
        mContext = context;
        mAdd = add;
        mList = new ArrayList<EmployeeVO>();
        mImageLoader = ImageLoader.getInstance();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        EmployeeVO infoVO = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.gv_head_icon_item, null);
            holder.itmeLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
            holder.personHead = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.personName = (TextView) convertView.findViewById(R.id.person_name);
            holder.deleteIcon = (ImageView) convertView.findViewById(R.id.delete_icon);
            holder.go = (TextView) convertView.findViewById(R.id.go_bt);
            holder.favorImg = (ImageView) convertView.findViewById(R.id.favor_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.personHead.setSemicircleNumber("");
        }
        // holder.itmeLayout.setOnLongClickListener(new HeadLongTouchListener());

        if (mAdd) {
            if (mList.size() == position) {
                holder.personHead.setImageResource(R.drawable.add_persion);
                holder.personName.setVisibility(View.GONE);
                holder.deleteIcon.setVisibility(View.GONE);
                holder.favorImg.setVisibility(View.GONE);
                holder.personHead.setOnClickListener(null);
            } else {
                infoVO = mList.get(position);
                mImageLoader.displayImage(infoVO.getHeadpic(), holder.personHead,
                        CommonUtils.initImageLoaderOptions());
                holder.personHead.setUserId(infoVO.getUserid());// HL
                holder.personName.setText(infoVO.getFullname());
                holder.personName.setVisibility(View.VISIBLE);
                holder.deleteIcon.setVisibility(View.VISIBLE);
                if ("1".equals(isone)) {
                    holder.deleteIcon.setVisibility(View.GONE);
                }
                // 代表关注的
                if ("2".equals(isone)) {
                    holder.favorImg.setVisibility(View.VISIBLE);
                } else {
                    holder.favorImg.setVisibility(View.GONE);
                }

                if (null != infoVO.getIsdefault()) {
                    holder.deleteIcon.setVisibility(View.GONE);
                    holder.personHead.setOnClickListener(null);
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
            infoVO = mList.get(position);
            mImageLoader.displayImage(infoVO.getHeadpic(), holder.personHead,
                    CommonUtils.initImageLoaderOptions());
            // 获取item中头像的userID
            holder.personHead.setUserId(infoVO.getUserid());// HL
            holder.personHead.setUserName(infoVO.getFullname());
            holder.personHead.setmImageLoader(mImageLoader);
            holder.personHead.setUrl(infoVO.getHeadpic());
            holder.personName.setText(infoVO.getFullname());
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

        if (mAdd) {
            if (mList.size() == position) {
                holder.personHead.setOnClickListener(new HeadLongTouchListener(null, position));
            } else {
                if (null != infoVO.getIsdefault()) {//是否是必须知会人
                    holder.deleteIcon.setVisibility(View.GONE);
                    holder.personHead.setOnClickListener(null);
                }
                else {
                    holder.personHead.setOnClickListener(new HeadLongTouchListener(mList.get(position), position));
                }
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
        private ImageView favorImg, deleteIcon;
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
                if ("1".equals(mType)) {
                    intent.putExtra("checkboxlist", (Serializable) getmOneSelectList());
                    intent.putExtra("state", "1");
                }
                // 合同下的负责需要传递规定参数获取相应的人
                if (null != isTrade && !isTrade.equals("")) {
                    intent.putExtra("isfrom", "2");// 合同传2
                    intent.putExtra("cid", mCids);// 客户id
                }
                context.startActivityForResult(intent, 2014);
            } else {
                if (employeeVO == null) {
                    Intent intent = new Intent();
                    if ("1".equals(mType)) {
                        intent.setClass(context, AddByDepartmentCRMActivity.class);
                        // 合同下的联合跟进人
                        if (null != isTrade && !isTrade.equals("") && isBussines == null) {
                            intent.putExtra("isfrom", "2");// 合同传2
                            intent.putExtra("cid", mCids);// 客户id
                        }
                        else if (null != isBussines && !isBussines.equals("") && isTrade == null) {
                            intent.putExtra("isfrom", "1");// 商机传1
                            intent.putExtra("cid", mCids);// 客户id
                        }
                    } else {
                        intent.setClass(context, AddByDepartmentActivity.class);
                    }

                    intent.putExtra("employ_name", JournalPublishActivity.class);
                    intent.putExtra("checkboxlist", (Serializable) mList);
                    context.startActivityForResult(intent, 10);
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

    public List<EmployeeVO> getmOneSelectList() {
        return mOneSelectList;
    }

    public void setmOneSelectList(List<EmployeeVO> mOneSelectList) {
        this.mOneSelectList = mOneSelectList;
    }

    private String isTrade;// 是否来自合同
    private String isBussines;// 是否来自商机
    private String mCids;// 当前详情客户的id

    public String getIsBussines() {
        return isBussines;
    }

    public void setIsBussines(String isBussines) {
        this.isBussines = isBussines;
    }

    public String getIsTrade() {
        return isTrade;
    }

    public void setIsTrade(String isTrade) {
        this.isTrade = isTrade;
    }

    public String getmCids() {
        return mCids;
    }

    public void setmCids(String mCids) {
        this.mCids = mCids;
    }

}
