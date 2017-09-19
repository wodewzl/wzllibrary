package com.bs.bsims.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.ChannelItem;
import com.bs.bsims.utils.CommonUtils;

public class BossStatisticsOtherAdapter extends BaseAdapter {
	private Context context;
	public List<ChannelItem> channelList;
	private TextView item_text;
	/** �Ƿ�ɼ� */
	boolean isVisible = true;
	/** Ҫɾ���position */
	public int remove_position = -1;

	private boolean viewPostionIsGone = false;

	public BossStatisticsOtherAdapter(Context context, List<ChannelItem> channelList) {
		this.context = context;
		this.channelList = channelList;
	}

	@Override
	public int getCount() {
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public ChannelItem getItem(int position) {
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
		
		
		
		convertView= LayoutInflater.from(context).inflate(R.layout.subscribe_category_item, null);
		item_text = (TextView) convertView.findViewById(R.id.text_item);
		ChannelItem channel = getItem(position);
		item_text.setText(channel.getName());

		if (remove_position == position && viewPostionIsGone) {
			convertView.setVisibility(View.INVISIBLE);
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
				str = str1 + "<font size=\"14\" color=\"#000000\">" + channel.getName() + "</font>" + str1;
			} else {
				String str2 = "<font size=\"14\" color=\"#ffffff\">" + "1" + "</font>";
				str = str2 + "<font size=\"14\" color=\"#00000\">" + channel.getName() + "</font>" + str2;
			}
			item_text.setText(Html.fromHtml(str));
		} else {
			item_text.setText(channel.getName());
			item_text.setTextColor(Color.parseColor("#000000"));
		}

		item_text.setBackgroundDrawable(CommonUtils.setBackgroundShap(context, 10, "#aaaaaa", "#ffffff"));
		return convertView;
	}

	/** ��ȡƵ���б� */
	public List<ChannelItem> getChannnelLst() {
		return channelList;
	}

	/** ���Ƶ���б� */
	public void addItem(ChannelItem channel) {
		channelList.add(channel);
		notifyDataSetChanged();
	}

	/** ����ɾ���position */
	public void setRemove(int position) {
		remove_position = position;
		notifyDataSetChanged();
		// notifyDataSetChanged();
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
}