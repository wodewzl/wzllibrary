package com.bs.bsims.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.TaskStatistics;
import com.bs.bsims.view.BSCircleImageView;

/**
 * @author peck
 * @Description:
 * @date 2015-6-13 下午5:21:11
 * @email 971371860@qq.com
 * @version V1.0
 */

public class EXTTaskStatisticsProgressBarListAdapter extends BaseAdapter {

	private Context context;
	public List<TaskStatistics> mList;

	public EXTTaskStatisticsProgressBarListAdapter(Context context) {
		this.context = context;
		this.mList = new ArrayList<TaskStatistics>();
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
		ViewHolderNew holderNew;
		if (convertView == null) {
			holderNew = new ViewHolderNew();
			convertView = View.inflate(context,
					R.layout.item_taskstatistics_progressbar, null);
			holderNew.mName = (TextView) convertView
					.findViewById(R.id.item_taskstatistics_progressbar_name_tv);
			holderNew.mType = (TextView) convertView
					.findViewById(R.id.item_taskstatistics_progressbar_type_tv);
			holderNew.mTime = (TextView) convertView
					.findViewById(R.id.item_taskstatistics_progressbar_time_tv);
			holderNew.mContent = (TextView) convertView
					.findViewById(R.id.item_taskstatistics_progressbar_content_tv);
			holderNew.mState = (ImageView) convertView
					.findViewById(R.id.item_taskstatistics_progressbar_state_img);
			holderNew.mFujian = (ImageView) convertView
					.findViewById(R.id.img_item_taskstatistics_progressbar_havefile);
			holderNew.mProgressBar = (ProgressBar) convertView
					.findViewById(R.id.item_taskstatistics_progressbar_seekbar);
			convertView.setTag(holderNew);
		} else {
			holderNew = (ViewHolderNew) convertView.getTag();
		}
		TaskStatistics data = mList.get(position);

		// 进度
		String strPercentValue = data.getPercent();
		int percentValue = 0;
		double dValue= 0;

		if (!TextUtils.isEmpty(strPercentValue)) {
			if (strPercentValue.contains("%")) {
				String subStr = strPercentValue.substring(0,
						strPercentValue.indexOf("%"));
				dValue=Double.parseDouble(subStr);
				percentValue = (int) dValue;
//				percentValue = Integer.valueOf(subStr);
//				percentValue = Integer.parseInt(dValue);
			}
		}
		holderNew.mName.setText(data.getDname());
		holderNew.mType.setText(percentValue + "%");
		holderNew.mContent.setText(percentValue + "%");
		holderNew.mTime.setText(data.getNum());
		holderNew.mProgressBar.setProgress(percentValue);
		return convertView;
	}

	static class ViewHolderNew {
		private TextView mName, mContent, mTime, mType;
		private ImageView mState, mFujian;
		private BSCircleImageView mHead;
		private View mItem;
		private ProgressBar mProgressBar;
	}

	public void updateData(List<TaskStatistics> list) {
		mList.clear();
		mList.addAll(list);
		this.notifyDataSetChanged();
	}

	public void updateDataFrist(List<TaskStatistics> list) {
		list.addAll(mList);
		mList.clear();
		mList.addAll(list);
		this.notifyDataSetChanged();
	}

	public void updateDataLast(List<TaskStatistics> list) {
		mList.addAll(list);
		this.notifyDataSetChanged();
	}
}
