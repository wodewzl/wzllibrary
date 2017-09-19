
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.DanganIndextwoActivity;
import com.bs.bsims.model.DangBasicUserInfo;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class DanganBasicSortAdapter extends BaseAdapter implements
        SectionIndexer {
    private List<DangBasicUserInfo> list = null;
    private Context mContext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private Boolean mAddPerson = false;
    private EmployeeVO employeeVO;
    private DangBasicUserInfo pVo = null;
    private boolean isShowLetter = true;
    private String type = "";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isShowLetter() {
        return isShowLetter;
    }

    public void setShowLetter(boolean isShowLetter) {
        this.isShowLetter = isShowLetter;
    }

    public EmployeeVO getEmployeeVO() {
        return employeeVO;
    }

    public void setEmployeeVO(EmployeeVO employeeVO) {
        this.employeeVO = employeeVO;
    }

    public DanganBasicSortAdapter(Context mContext, List<DangBasicUserInfo> list) {
        this.mContext = mContext;
        this.list = list;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
    }

    public DanganBasicSortAdapter(Context mContext, List<DangBasicUserInfo> list,
            boolean addPerson) {
        this.mContext = mContext;
        this.list = list;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
        this.mAddPerson = addPerson;

    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * 
     * @param list
     */
    public void updateListView(List<DangBasicUserInfo> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    public View getView(final int position, View view, ViewGroup arg2) {

        ViewHolder viewHolder = null;
        pVo = list.get(position);

        if ("1".equals(type)) {

            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.danganbasicsortadpter, null);
                viewHolder.iconImage = (BSCircleImageView) view
                        .findViewById(R.id.head_iconbasic);
                viewHolder.nameTv = (TextView) view.findViewById(R.id.name);
                viewHolder.sex = (TextView) view.findViewById(R.id.sex);
                viewHolder.detailTv = (TextView) view.findViewById(R.id.detail);
                viewHolder.departmentTv = (TextView) view
                        .findViewById(R.id.department);
                viewHolder.groupTv = (TextView) view.findViewById(R.id.group);
                viewHolder.mWorkStartTime = (TextView) view.findViewById(R.id.publish_time_tv);
                viewHolder.mWorkTimeLng = (TextView) view.findViewById(R.id.praise_tv);
                viewHolder.mWorkState = (TextView) view.findViewById(R.id.group);
                viewHolder.mItemLayout = (LinearLayout) view
                        .findViewById(R.id.item_layout);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();

            }

            // 根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);

            // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
 
            switch (Integer.parseInt(pVo.getStatus())) {
                case 1:
                    viewHolder.mWorkState.setText("正式");
                    viewHolder.mWorkState.setBackground(CommonUtils.setBackgroundShap(mContext, 5, "#2C9BE3", "#2C9BE3"));
                    break;
                case 2:
                    viewHolder.mWorkState.setText("试用");
                    viewHolder.mWorkState.setBackground(CommonUtils.setBackgroundShap(mContext, 5, "#32AC9D", "#32AC9D"));
                    break;
                case 3:
                    viewHolder.mWorkState.setText("实习");
                    viewHolder.mWorkState.setBackground(CommonUtils.setBackgroundShap(mContext, 5, "#FCB72E", "#FCB72E"));
                    break;
            }
            
            mImageLoader.displayImage(pVo.getHeadpic(), viewHolder.iconImage,
                    mOptions);

            viewHolder.nameTv.setText(pVo.getFullname());

            // viewHolder.detailTv.setText(pVo.getPostsname());

            viewHolder.detailTv.setText(pVo.getPname() + "/" + pVo.getDname());
            // viewHolder.departmentTv.setText(pVo.getPname());
            // viewHolder.groupTv.setText(pVo.getDname());
            if (pVo.getSex().equals("男")) {
                viewHolder.sex.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        mContext.getResources().getDrawable(R.drawable.sex_man),
                        null);
            } else {
                viewHolder.sex.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        mContext.getResources().getDrawable(R.drawable.sex_woman),
                        null);
            }
            
            viewHolder.mWorkStartTime.setText(DateUtils.parseDateDay(pVo.getEntrytime()));
            viewHolder.mWorkTimeLng.setText(pVo.getWorkage());
            viewHolder.mItemLayout
                    .setOnClickListener(new ImtemOnclickListeners(pVo));
        }

        else {
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.danganbasicsortadpternearorlevae, null);
                viewHolder.iconImage = (BSCircleImageView) view
                        .findViewById(R.id.head_iconbasic);
                viewHolder.nameTv = (TextView) view.findViewById(R.id.name);
                viewHolder.sex = (TextView) view.findViewById(R.id.sex);
                viewHolder.detailTv = (TextView) view.findViewById(R.id.detail);
                viewHolder.groupTv = (TextView) view.findViewById(R.id.group);
                viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
                viewHolder.mItemLayout = (LinearLayout) view
                        .findViewById(R.id.item_layout);
                viewHolder.whereuserform = (TextView) view.findViewById(R.id.whereuserform);
                viewHolder.fromcompany = (TextView) view.findViewById(R.id.fromcompany);
                viewHolder.department1 = (TextView) view.findViewById(R.id.department1);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            int section = getSectionForPosition(position);
            if (isShowLetter) {
                if (position == getPositionForSection(section)) {
                    viewHolder.tvLetter.setVisibility(View.VISIBLE);
                    viewHolder.tvLetter.setText(pVo.getSortLetters());
                } else {
                    viewHolder.tvLetter.setVisibility(View.GONE);
                }
            } else {
                viewHolder.tvLetter.setVisibility(View.GONE);
            }

            mImageLoader.displayImage(pVo.getHeadpic(), viewHolder.iconImage,
                    mOptions);
            viewHolder.nameTv.setText(pVo.getFullname());
            viewHolder.groupTv.setText(pVo.getPname());
            // viewHolder.detailTv.setText(pVo.getPostsname());
            viewHolder.detailTv.setText(pVo.getDname());
            if (pVo.getSex().equals("男")) {
                viewHolder.sex.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        mContext.getResources().getDrawable(R.drawable.sex_man),
                        null);
            } else {
                viewHolder.sex.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        mContext.getResources().getDrawable(R.drawable.sex_woman),
                        null);
            }
            viewHolder.mItemLayout
                    .setOnClickListener(new ImtemOnclickListeners(pVo));
            // 如果是新进员工
            if (null == pVo.getQrname() || pVo.getQrname().equals("")) {
                viewHolder.department1.setText("入职时间:" + DateUtils.parseDateDay(pVo.getEntrytime()));
                viewHolder.whereuserform.setText("招聘渠道：");
                viewHolder.fromcompany.setText(pVo.getChname());
            }
            // 如果是离职原因
            else {
                viewHolder.department1.setText("离职时间:" + DateUtils.parseDateDay(pVo.getQuittime()));
                viewHolder.whereuserform.setText("离职原因：");
                viewHolder.fromcompany.setText(pVo.getQrname());

            }

        }

        return view;

    }

    private class ImtemOnclickListeners implements OnClickListener {
        private DangBasicUserInfo vo;

        public ImtemOnclickListeners(DangBasicUserInfo vo) {
            this.vo = vo;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent();
            i.putExtra("uid", vo.getUserid());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setClass(mContext, DanganIndextwoActivity.class);
            mContext.startActivity(i);
        }

    }

    static class ViewHolder {
        // TextView tvTitle;

        private TextView nameTv,tvLetter, detailTv, departmentTv, groupTv, sex, whereuserform, fromcompany, department1,mWorkStartTime,mWorkTimeLng,mWorkState;
        private BSCircleImageView iconImage;
        private LinearLayout mItemLayout;

    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     * 
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}
