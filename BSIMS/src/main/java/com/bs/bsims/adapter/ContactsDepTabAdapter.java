
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.CrmContactDetailActivity;
import com.bs.bsims.model.ContactDepTabVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;

@SuppressLint("NewApi")
public class ContactsDepTabAdapter extends BSBaseAdapter<ContactDepTabVO> implements SectionIndexer {

    /**
     * @param context
     */
    public ContactsDepTabAdapter(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    private String mNoHeadColors[] = {
            "#7A929E", "#6194FF", "#65BEE6", "#F75E8C", "#39C3B4", "#FD953C", "#9B89B9",
    };
    private int colorIndex = 0;
    private String mPhone = "";

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (mIsEmpty) {
            View view = super.getView(position, convertView, parent);
            return view;
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        final ContactDepTabVO pVo = mList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_sort_dep_item, null);
            viewHolder.companyName = (TextView) convertView.findViewById(R.id.company_name);
            viewHolder.detailName = (TextView) convertView.findViewById(R.id.detail_name);
            viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
            viewHolder.mText_item = (TextView) convertView.findViewById(R.id.text_item);
            viewHolder.mItemLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
            viewHolder.pame = (TextView) convertView.findViewById(R.id.pame);
            viewHolder.mCallMe = (ImageView) convertView.findViewById(R.id.call);
            viewHolder.bsCircleImageView = (BSCircleImageView) convertView.findViewById(R.id.head_iconbasic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
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
        viewHolder.companyName.setText(pVo.getLname());
        if (pVo.getSex().equals("男")) {
            viewHolder.companyName.setCompoundDrawablePadding(5);
            viewHolder.companyName.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext
                    .getResources().getDrawable(R.drawable.sex_man), null);
        }
        else {
            viewHolder.companyName.setCompoundDrawablePadding(5);
            viewHolder.companyName.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext
                    .getResources().getDrawable(R.drawable.sex_woman), null);
        }

        if (pVo.getLheadpic() != null && !pVo.getLheadpic().equals("")) {
            viewHolder.mText_item.setVisibility(View.GONE);
            viewHolder.bsCircleImageView.setVisibility(View.VISIBLE);
            mImageLoader.displayImage(pVo.getLheadpic(), viewHolder.bsCircleImageView, mOptions);
        }
        else {
            viewHolder.mText_item.setVisibility(View.VISIBLE);
            viewHolder.bsCircleImageView.setVisibility(View.GONE);
            viewHolder.mText_item.setText(pVo.getNickname());
            // viewHolder.mText_item.setBackgroundDrawable(CommonUtils.setBackgroundShap(mContext,
            // 40, "#ffffff", mNoHeadColors[colorIndex]));
            String color = mNoHeadColors[position % mNoHeadColors.length];
            viewHolder.mText_item.setBackground(CommonUtils.setBackgroundShap(mContext, 100, color, color));
        }

        viewHolder.pame.setText(pVo.getPost());
        viewHolder.detailName.setText(pVo.getCompany());
        viewHolder.mItemLayout.setOnClickListener(new ImtemOnclickListeners(pVo));
        if (pVo.getPhone() != null)
            mPhone = pVo.getPhone().get(0);
        else {
            if (pVo.getTel() != null)
                mPhone = pVo.getTel().get(0);
        }

        if (mPhone.equals(""))
            viewHolder.mCallMe.setVisibility(View.GONE);
        else
            viewHolder.mCallMe.setVisibility(View.VISIBLE);
        viewHolder.mCallMe.setOnClickListener(new CallMeOnclickListeners(mPhone));
        return convertView;

    }

    private class ImtemOnclickListeners implements OnClickListener {
        private ContactDepTabVO vo;

        public ImtemOnclickListeners(ContactDepTabVO vo) {
            this.vo = vo;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent in = new Intent(mContext, CrmContactDetailActivity.class);
            in.putExtra("lid", vo.getLid());
            mContext.startActivity(in);
        }
    }

    private class CallMeOnclickListeners implements OnClickListener {
        private String phone = "";

        public CallMeOnclickListeners(String vPhone) {
            this.phone = vPhone;
        }

        @Override
        public void onClick(View arg0) {
            CommonUtils.call(mContext, phone);
        }

    }

    static class ViewHolder {
        TextView tvLetter;
        // TextView tvTitle;

        private TextView companyName, detailName, pame, mText_item;
        private LinearLayout mItemLayout;
        private ImageView mCallMe;
        private BSCircleImageView bsCircleImageView;
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
