
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class SortPeopleAdapter extends BaseAdapter implements SectionIndexer {
    public List<DepartmentAndEmployeeVO> mList = null;
    private Context mContext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private Boolean mAddPerson = false;

    public SortPeopleAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<DepartmentAndEmployeeVO>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();

    }

    public int getCount() {
        return this.mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final DepartmentAndEmployeeVO vo = mList.get(position);
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
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        mImageLoader.displayImage(vo.getHeadpic(), viewHolder.iconImage, mOptions);
        viewHolder.iconImage.setUserId(vo.getUserid());// HL:获取头像对应的用户ID，以便响应跳转
        viewHolder.nameTv.setText(vo.getFullname());
        viewHolder.detailTv.setVisibility(View.VISIBLE);
        viewHolder.detailTv.setText(vo.getTel());
        viewHolder.departmentTv.setText(vo.getPname());
        viewHolder.groupTv.setText(vo.getDname());

        return view;

    }

    public void updateData(List<DepartmentAndEmployeeVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvLetter;
        // TextView tvTitle;

        private TextView nameTv, detailTv, departmentTv, groupTv;
        private BSCircleImageView iconImage;
        private LinearLayout mItemLayout;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return mList.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mList.get(i).getSortLetters();
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
