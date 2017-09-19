
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beisheng.synews.mode.ChannelItem;
import com.im.zhsy.R;

import java.util.ArrayList;
import java.util.List;

import static com.im.zhsy.R.id.text_item;

@SuppressLint("NewApi")
public class DragAdapter extends BaseAdapter {
    /** TAG */
    private final static String TAG = "DragAdapter";
    /** 是否显示底部的ITEM */
    private boolean isItemShow = false;
    private Context context;
    /** 控制的postion */
    private int holdPosition;
    /** 是否改变 */
    private boolean isChanged = false;
    /** 列表数据是否改变 */
    private boolean isListChanged = false;
    /** 是否可见 */
    boolean isVisible = true;
    /** 可以拖动的列表（即用户选择的频道列表） */
    public List<ChannelItem.ListvBean.ListBeanX> channelList;
    /** TextView 频道内容 */
    private TextView item_text;
    /** 要删除的position */
    public int remove_position = -1;
    private boolean isDelete;
    private ImageView deleteImg;
    private ImageView xxxImg;

    public DragAdapter(Context context) {
        this.context = context;
        this.channelList = new ArrayList<ChannelItem.ListvBean.ListBeanX>();
    }

    public DragAdapter(Context context, List<ChannelItem.ListvBean.ListBeanX> channelList) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.channel_item, null);
        item_text = (TextView) view.findViewById(text_item);
        xxxImg = (ImageView)     view.findViewById(R.id.delete_img2);
        deleteImg = (ImageView) view.findViewById(R.id.delete_img);
        ChannelItem.ListvBean.ListBeanX channel = getItem(position);
        item_text.setText(channel.getName());
        if ("1".equals(channel.getFixed())) {
            item_text.setEnabled(false);
        } else {
            item_text.setEnabled(true);
        }

        Animation anim = AnimationUtils.loadAnimation(context, R.anim.myanim);

//
//        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
//        translateAnimation.setInterpolator(new CycleInterpolator(5));
//        translateAnimation.setDuration(1);
//        item_text.setAnimation(translateAnimation);
        if (isDelete && "0".equals(channel.getFixed())) {
            view.startAnimation(anim);
            if("0".equals(channel.getIsdel())){
                xxxImg.setVisibility(View.VISIBLE);
                deleteImg.setVisibility(View.GONE);
//                translateAnimation.start();

            }else{
                deleteImg.setVisibility(View.VISIBLE);
                xxxImg.setVisibility(View.GONE);
//                translateAnimation.start();

            }

        } else {
            deleteImg.setVisibility(View.GONE);
//            translateAnimation.cancel();
            anim.cancel();

        }

        if (isChanged && (position == holdPosition) && !isItemShow) {
            item_text.setText("");
            item_text.setSelected(true);
            item_text.setEnabled(true);
            isChanged = false;
        }
        if (!isVisible && (position == -1 + channelList.size())) {
            item_text.setText("");
            item_text.setSelected(true);
            item_text.setEnabled(true);
        }
        if (remove_position == position) {
            item_text.setText("");
        }


        return view;
    }

    public void updateData(List<ChannelItem.ListvBean.ListBeanX> list) {
        // channelList.clear();
        // channelList.addAll(list);
        this.channelList = list;
        this.notifyDataSetChanged();
    }





    /** 添加频道列表 */
    public void addItem(ChannelItem.ListvBean.ListBeanX channel) {
        channelList.add(channelList.size()-4,channel);
//        channelList.add(channel);
        isListChanged = true;
        notifyDataSetChanged();
    }

    /** 拖动变更频道排序 */
    public void exchange(int dragPostion, int dropPostion) {
        holdPosition = dropPostion;
        ChannelItem.ListvBean.ListBeanX dragItem = getItem(dragPostion);
        Log.d(TAG, "startPostion=" + dragPostion + ";endPosition=" + dropPostion);
        if (dragPostion < dropPostion) {
            channelList.add(dropPostion + 1, dragItem);
            channelList.remove(dragPostion);
        } else {
            channelList.add(dropPostion, dragItem);
            channelList.remove(dragPostion + 1);
        }
        isChanged = true;
        isListChanged = true;
        notifyDataSetChanged();
    }

    /** 获取频道列表 */
    public List<ChannelItem.ListvBean.ListBeanX> getChannnelLst() {
        return channelList;
    }

    /** 设置删除的position */
    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
    }

    /** 删除频道列表 */
    public void remove() {
        channelList.remove(remove_position);
        remove_position = -1;
        isListChanged = true;
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

    /** 排序是否发生改变 */
    public boolean isListChanged() {
        return isListChanged;
    }

    /** 设置是否可见 */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    /** 显示放下的ITEM */
    public void setShowDropItem(boolean show) {
        isItemShow = show;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

}
