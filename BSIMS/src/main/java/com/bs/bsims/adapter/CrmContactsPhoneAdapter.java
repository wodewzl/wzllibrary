
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.ContactUtils.ContactInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class CrmContactsPhoneAdapter extends BaseAdapter {
    private Context mContext;
    public List<ContactInfo> contactsList = null;
    public List<ContactInfo> commitList = null;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private Boolean flag = true;

    public CrmContactsPhoneAdapter(Context context, List<ContactInfo> list) {
        this.mContext = context;
        this.contactsList = list;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return contactsList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return contactsList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder = null;
        final ContactInfo contactVo = contactsList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.crm_contacts_phone_item_adapter, null);
            holder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
            holder.mselect = (ImageView) convertView.findViewById(R.id.mselect);
            holder.picture = (ImageView) convertView.findViewById(R.id.picture);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(contactVo.getName());
        if (contactVo.getIsSelect()) {
            holder.mselect.setImageResource(R.drawable.common_ic_selected);
        } else {
            holder.mselect.setImageResource(R.drawable.common_ic_unselect);
        }

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.tvLetter.setVisibility(View.VISIBLE);
            holder.tvLetter.setText(contactVo.getSortLetters());
        } else {
            holder.tvLetter.setVisibility(View.GONE);
        }

        return convertView;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * 
     * @param list
     */
    public void updateListView(List<ContactInfo> list) {
        this.contactsList = list;
        notifyDataSetChanged();
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return contactsList.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = contactsList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    class ViewHolder {
        private ImageView picture;
        private ImageView mselect;
        private TextView tvLetter, name;
    }

}
