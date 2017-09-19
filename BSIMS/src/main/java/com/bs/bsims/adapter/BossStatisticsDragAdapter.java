package com.bs.bsims.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.ChannelItem;
import com.bs.bsims.utils.CommonUtils;

public class BossStatisticsDragAdapter extends BaseAdapter {
	/** TAG */
	private final static String TAG = "DragAdapter";
	/** �Ƿ���ʾ�ײ���ITEM */
	private boolean isItemShow = false;
	private Context context;
	/** ���Ƶ�postion */
	private int holdPosition;
	/** �Ƿ�ı� */
	private boolean isChanged = false;
	/** �Ƿ�ɼ� */
	boolean isVisible = true;
	/** �����϶����б?���û�ѡ���Ƶ���б? */
	public List<ChannelItem> channelList;
	/** TextView Ƶ������ */
	private TextView item_text;
	/** Ҫɾ���position */
	public int remove_position = -1;
	private boolean viewPostionIsGone =false;

	public BossStatisticsDragAdapter(Context context, List<ChannelItem> channelList) {
		this.context = context;
		this.channelList = channelList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public ChannelItem getItem(int position) {
		// TODO Auto-generated method stub
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		GradientDrawable bgdraw = null;
		
 

		convertView = LayoutInflater.from(context).inflate(R.layout.subscribe_category_item, null);
		item_text = (TextView) convertView.findViewById(R.id.text_item);

		ChannelItem channel = getItem(position);

		// if ((position == 0) || (position == 1)) {
		// //
		// item_text.setTextColor(context.getResources().getColor(R.color.black));
		// item_text.setEnabled(false);
		// }

		if (remove_position == position&&viewPostionIsGone) {
			convertView.setVisibility(View.INVISIBLE);
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

		if (channel.getName().toString().trim().length() <= 3) {
			String str = "";
			if (channel.getName().toString().trim().length() == 2) {
				String str1 = "<font size=\"14\" color=\"#ffffff\">" + "吴" + "</font>";
				str = str1 + "<font size=\"14\" color=\"#00a9fe\">" + channel.getName() + "</font>" + str1;
			} else {
				String str2 = "<font size=\"14\" color=\"#ffffff\">" + "1" + "</font>";
				str = str2 + "<font size=\"14\" color=\"#00a9fe\">" + channel.getName() + "</font>" + str2;
			}
			item_text.setText(Html.fromHtml(str));
		} else {
			item_text.setText(channel.getName());
			item_text.setTextColor(Color.parseColor("#00a9fe"));
		}

		bgdraw = CommonUtils.setBackgroundShap(context, 10, "#00a9fe", "#ffffff");

		item_text.setBackgroundDrawable(bgdraw);
		return convertView;
	}

	class ViewHolder {
		TextView item_text;
	}

	/** ���Ƶ���б� */
	public void addItem(ChannelItem channel) {
		channelList.add(channel);
		notifyDataSetChanged();

	}

	/** �϶����Ƶ������ */
	public void exchange(int dragPostion, int dropPostion) {
		holdPosition = dropPostion;
		ChannelItem dragItem = getItem(dragPostion);
		Log.d(TAG, "startPostion=" + dragPostion + ";endPosition=" + dropPostion);
		if (dragPostion < dropPostion) {
			channelList.add(dropPostion + 1, dragItem);
			channelList.remove(dragPostion);
		} else {
			channelList.add(dropPostion, dragItem);
			channelList.remove(dragPostion + 1);
		}
		isChanged = true;
		notifyDataSetChanged();
	}

	/** ��ȡƵ���б� */
	public List<ChannelItem> getChannnelLst() {
		return channelList;
	}

	/** ����ɾ���position */
	public void setRemove(int position) {
		remove_position = position;
		notifyDataSetChanged();
	}

	/** ����ɾ���position */
	public void setDisPlayView(int position, boolean viewPostionIsGone) {
		remove_position = position;
		this.viewPostionIsGone = viewPostionIsGone;
		notifyDataSetChanged();
	}

	/** ɾ��Ƶ���б� */
	public void remove() {
		channelList.remove(remove_position);
		remove_position = -1;
		notifyDataSetChanged();
	}

	/** ����Ƶ���б� */
	public void setListDate(List<ChannelItem> list) {
		channelList = list;
	}

	/** ��ȡ�Ƿ�ɼ� */
	public boolean isVisible() {
		return isVisible;
	}

	/** �����Ƿ�ɼ� */
	public void setVisible(boolean visible) {
		isVisible = visible;
	}

	/** ��ʾ���µ�ITEM */
	public void setShowDropItem(boolean show) {
		isItemShow = show;
	}
}