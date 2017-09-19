
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.bs.bsims.activity.ContactPersonActivity;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.image.selector.ImageActivityUtils;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private List<DepartmentAndEmployeeVO> list = null;
    private Context mContext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private Boolean mAddPerson = false;
    private EmployeeVO employeeVO;
    private ViewHolder viewHolder = null;
    private String state = "2";// crm可以选择本人的判断

    private String mNoHeadColors[] = {
            "#7A929E", "#6194FF", "#65BEE6", "#F75E8C", "#39C3B4", "#FD953C", "#9B89B9",
    };

    public EmployeeVO getEmployeeVO() {
        return employeeVO;
    }

    public void setEmployeeVO(EmployeeVO employeeVO) {
        this.employeeVO = employeeVO;
    }

    public SortAdapter(Context mContext, List<DepartmentAndEmployeeVO> list) {
        this.mContext = mContext;
        this.list = list;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();

    }

    public SortAdapter(Context mContext, List<DepartmentAndEmployeeVO> list, boolean addPerson) {
        this.mContext = mContext;
        this.list = list;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
        this.mAddPerson = addPerson;

    }

    public SortAdapter(Context mContext, List<DepartmentAndEmployeeVO> list, boolean addPerson, String state) {
        this.mContext = mContext;
        this.list = list;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
        this.mAddPerson = addPerson;
        this.state = state;

    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * 
     * @param list
     */
    public void updateListView(List<DepartmentAndEmployeeVO> list) {
        this.list = list;
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
        final DepartmentAndEmployeeVO pVo = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.lv_sort_letter_item, null);
            viewHolder.iconImage = (BSCircleImageView) view.findViewById(R.id.head_icon_s);
            viewHolder.nameTv = (TextView) view.findViewById(R.id.name);
            viewHolder.detailTv = (TextView) view.findViewById(R.id.detail);
            viewHolder.departmentTv = (TextView) view.findViewById(R.id.department);
            viewHolder.groupTv = (TextView) view.findViewById(R.id.group);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.mItemLayout = (LinearLayout) view.findViewById(R.id.item_layout);
            viewHolder.noHeadIconTv = (TextView) view.findViewById(R.id.no_head_icon);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(pVo.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        if ("暂无".equals(pVo.getHeadpic())) {
            viewHolder.iconImage.setVisibility(View.GONE);
            viewHolder.noHeadIconTv.setVisibility(View.VISIBLE);
            String color = mNoHeadColors[position % mNoHeadColors.length];
            viewHolder.noHeadIconTv.setBackground(CommonUtils.setBackgroundShap(mContext, 100, color, color));
            viewHolder.noHeadIconTv.setText(pVo.getNickname());
        } else {
            mImageLoader.displayImage(pVo.getHeadpic(), viewHolder.iconImage, mOptions);
            viewHolder.noHeadIconTv.setVisibility(View.GONE);
            viewHolder.iconImage.setVisibility(View.VISIBLE);
        }

        viewHolder.nameTv.setText(pVo.getFullname());
        viewHolder.detailTv.setVisibility(View.VISIBLE);
        viewHolder.detailTv.setText(pVo.getTel());
        viewHolder.departmentTv.setText(pVo.getPname());
        viewHolder.groupTv.setText(pVo.getDname());
        viewHolder.mItemLayout.setOnClickListener(new ImtemOnclickListeners(pVo));
        return view;

    }

    private class ImtemOnclickListeners implements OnClickListener {
        private DepartmentAndEmployeeVO vo;

        public ImtemOnclickListeners(DepartmentAndEmployeeVO vo) {
            this.vo = vo;
        }

        @SuppressLint("NewApi")
        @Override
        public void onClick(View v) {
            if (mAddPerson) {
                ArrayList<EmployeeVO> checklist = new ArrayList<EmployeeVO>();
                EmployeeVO employee = new EmployeeVO();
                employee.setUserid(vo.getUserid());
                employee.setFullname(vo.getFullname());
                employee.setHeadpic(vo.getHeadpic());
                employee.setTel(vo.getTel());
                employee.setDname(vo.getDname());
                employee.setPname(vo.getPname());
                setEmployeeVO(employee);
                if (!state.equals("1")) {// crm可以选择本人的判断
                    if (vo.getUserid().equals(BSApplication.getInstance().getUserId())) {
                        CustomToast.showLongToast(mContext, "不能选择本人");
                        return;
                    }
                }

                checklist.add(employee);

                Intent intent = new Intent();
                // intent.setClass(mContext, clz);
                intent.putExtra("checkboxlist", (Serializable) checklist);

                ((Activity) mContext).setResult(2014, intent);
                ((Activity) mContext).finish();
            } else {
                final Intent intent = new Intent();
                intent.setClass(mContext, ContactPersonActivity.class);
                intent.putExtra("uid", vo.getUserid());
                intent.putExtra("userid", vo.getUserid());
                intent.putExtra("cornet", vo.getCornet());
                intent.putExtra("username", vo.getFullname());
                ImageActivityUtils.sendByteImg(mImageLoader, mContext, intent, vo.getHeadpic(), viewHolder.iconImage);

            }
        }
    }

    static class ViewHolder {
        TextView tvLetter;
        // TextView tvTitle;

        private TextView nameTv, detailTv, departmentTv, groupTv, noHeadIconTv;
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
