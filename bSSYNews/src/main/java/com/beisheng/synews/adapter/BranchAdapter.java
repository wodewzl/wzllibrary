
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beisheng.base.view.BSBadgeView;
import com.beisheng.synews.mode.ChannelItem;
import com.im.zhsy.R;

import java.util.ArrayList;
import java.util.List;

public class BranchAdapter extends BaseAdapter {
    private Context context;
    public List<ChannelItem.ListvBean.ListBeanX> channelList;
    private TextView item_text;
    /** 是否可见 */
    boolean isVisible = true;
    /** 要删除的position */
    public int remove_position = -1;
    private BSBadgeView badge;

    public BranchAdapter(Context context) {
        this.context = context;
        this.channelList = new ArrayList<ChannelItem.ListvBean.ListBeanX>();
    }

    public BranchAdapter(Context context, List<ChannelItem.ListvBean.ListBeanX> channelList) {
        this.context = context;
        this.channelList = channelList;
    }

    @Override
    public int getCount() {
        return channelList == null ? 0 : channelList.size();
    }

    @Override
    public ChannelItem.ListvBean.ListBeanX getItem(int position) {
        if (channelList != null && channelList.size() != 0) {
            return channelList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.channel_item, null);
        item_text = (TextView) view.findViewById(R.id.text_item);
        badge = new BSBadgeView(context, item_text);
        ChannelItem.ListvBean.ListBeanX channel = getItem(position);
        item_text.setText(channel.getName());
        if (!isVisible && (position == -1 + channelList.size())) {
            item_text.setText("");
        }
        if (remove_position == position) {
            item_text.setText("");
        }

        if ("1".equals(channel.getSign())) {
            badge.setText("news");
            badge.setBadgePosition(BSBadgeView.POSITION_TOP_RIGHT);
            badge.setTextSize(8);
            badge.setBadgeMargin(0);
            TranslateAnimation anim = new TranslateAnimation(-100, 0, 0, 0);
            anim.setInterpolator(new BounceInterpolator());
            anim.setDuration(1000);
            badge.toggle(anim, null);
            badge.show();
        } else {
            badge.hide();
        }
        return view;
    }

    /** 获取频道列表 */
    public List<ChannelItem.ListvBean.ListBeanX> getChannnelLst() {
        return channelList;
    }

    public void updateData(List<ChannelItem.ListvBean.ListBeanX> list) {
        // channelList.clear();
        // channelList.addAll(list);
        this.channelList = list;
        this.notifyDataSetChanged();
    }

    /** 添加频道列表 */
    public void addItem(ChannelItem.ListvBean.ListBeanX channel) {
        channelList.add(channel);
        notifyDataSetChanged();
    }

    /** 设置删除的position */
    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
        // notifyDataSetChanged();
    }

    /** 删除频道列表 */
    public void remove() {
        channelList.remove(remove_position);
        remove_position = -1;
        notifyDataSetChanged();
    }

    /** 设置频道列表 */
    public void setListDate(List<ChannelItem.ListvBean.ListBeanX> list) {
        channelList = list;
    }

    /** 获取是否可见 */
    public boolean isVisible() {
        return isVisible;
    }

    /** 设置是否可见 */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
