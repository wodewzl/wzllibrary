
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.view.BSCircleImageView;

public class NoticeSortAdapter extends BSBaseAdapter<DepartmentAndEmployeeVO> implements SectionIndexer {
    private Context mContext;

    public NoticeSortAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }
        DepartmentAndEmployeeVO vo = mList.get(position);
        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.notice_lv_sort_letter_item, null);
            holder.iconImage = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name);
            holder.detailTv = (TextView) convertView.findViewById(R.id.detail);
            holder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
            holder.mStatusImg = (ImageView) convertView.findViewById(R.id.status_img);
            // holder.mItemLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.tvLetter.setVisibility(View.VISIBLE);
            holder.tvLetter.setText(vo.getSortLetters());
        } else {
            holder.tvLetter.setVisibility(View.GONE);
        }

        if (vo.isSelected()) {
            holder.mStatusImg.setImageResource(R.drawable.common_ic_selected);
        } else {
            holder.mStatusImg.setImageResource(R.drawable.common_ic_unselect);
        }

        mImageLoader.displayImage(vo.getHeadpic(), holder.iconImage, mOptions);
        holder.nameTv.setText(vo.getFullname());
        holder.detailTv.setText(vo.getDname() + "/" + vo.getPname());

        return convertView;
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

    static class ViewHolder {
        TextView tvLetter;
        // TextView tvTitle;

        private TextView nameTv, detailTv, departmentTv, groupTv;
        private BSCircleImageView iconImage;
        private LinearLayout mItemLayout;
        private ImageView mStatusImg;
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

}
