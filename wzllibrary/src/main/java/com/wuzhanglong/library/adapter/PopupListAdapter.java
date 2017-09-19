
package com.wuzhanglong.library.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wuzhanglong.library.R;
import com.wuzhanglong.library.mode.TreeVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PopupListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> itemList;
    private List<TreeVO> mList;
    private int selectDid;

    public int getSelectDid() {
        return selectDid;
    }

    public void setSelectDid(int selectDid) {
        this.selectDid = selectDid;
    }

    public PopupListAdapter(Context context,
                            ArrayList<HashMap<String, Object>> item) {
        this.context = context;
        this.itemList = item;
    }

    public PopupListAdapter(Context context,
                            ArrayList<HashMap<String, Object>> item, List<TreeVO> treevo) {
        this.context = context;
        this.itemList = item;
        this.mList = treevo;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.popup_category_item, null);
            holder.mNameTextView = (TextView) convertView.findViewById(R.id.name);
            holder.mImage = (ImageView) convertView.findViewById(R.id.haschild);
            holder.mItemLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
            holder.mRootLaout = (LinearLayout) convertView.findViewById(R.id.root_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // final String name = itemList.get(position).get("name").toString();
        // holder.mNameTextView.setText(name);
        // holder.mImage.setVisibility(View.VISIBLE);

        TreeVO vo = mList.get(position);
        String name = vo.getName();
        holder.mNameTextView.setText(name);
        if (vo.isHaschild()) {
            holder.mImage.setVisibility(View.VISIBLE);
        } else {
            holder.mImage.setVisibility(View.INVISIBLE);
        }

        if (vo.getLevel() == 1) {
            holder.mItemLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.mNameTextView.setTextColor(Color.parseColor("#000000"));
        } else if (vo.getLevel() == 2) {
            holder.mItemLayout.setBackgroundColor(Color.parseColor("#F2F2F2"));
            holder.mNameTextView.setTextColor(Color.parseColor("#888888"));
        } else if (vo.getLevel() == 3) {
            holder.mItemLayout.setBackgroundColor(Color.parseColor("#DCDCDC"));
            holder.mNameTextView.setTextColor(Color.parseColor("#888888"));
        }

        if (vo.getId() == getSelectDid()) {
            if (vo.getLevel() == 1) {
                holder.mItemLayout.findViewById(R.id.item_layout).setBackgroundColor(Color.parseColor("#F2F2F2"));
            } else if (vo.getLevel() == 2) {
                holder.mItemLayout.findViewById(R.id.item_layout).setBackgroundColor(Color.parseColor("#DCDCDC"));
            } else if (vo.getLevel() == 3) {
                holder.mItemLayout.findViewById(R.id.item_layout).setBackgroundColor(Color.parseColor("#CCCCCC"));
            }
        }

        return convertView;
    }

    private class ViewHolder {
        public TextView mNameTextView;
        public ImageView mImage;
        public ImageView iv_my;
        public LinearLayout mItemLayout, mRootLaout;
    }

    class ItemClass implements OnClickListener {
        @Override
        public void onClick(View arg0) {

        }

    }

    public void updateData(List<TreeVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

}
