package com.bs.bsims.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bs.bsims.R;
import com.bs.bsims.model.DanganWorkUserpay;
import com.bs.bsims.utils.DateUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DanganAllWorkChilrdUserPayApdater extends BaseAdapter {
	private Context mContext;
	public List<DanganWorkUserpay> mList;

	public DanganAllWorkChilrdUserPayApdater(Context context) {
		mContext = context;
		mList = new ArrayList<DanganWorkUserpay>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext,
					R.layout.da_workallchrilduserpay_apdater, null);
			holder.userpaytime = (TextView) convertView
					.findViewById(R.id.userpaytime);
			holder.news = (TextView) convertView.findViewById(R.id.news);
			holder.pay = (TextView) convertView.findViewById(R.id.pay);
			holder.userpaytype = (ImageView) convertView
					.findViewById(R.id.userpaytype);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 给值

		if (!mList.get(position).getTime().equals("暂无")) {
			holder.userpaytime.setText(DateUtils.parseDateDay(mList.get(
					position).getTime()));
		} else {
			holder.userpaytime.setText(mList.get(position).getTime());
		}

		holder.news.setText(mList.get(position).getNews());
		holder.pay.setText(mList.get(position).getPay());

		// 涨薪资
		if (mList.get(position).getType().equals("1")) {
			holder.userpaytype.setBackgroundResource(R.drawable.da_xinzizhang);
		} else {
			holder.userpaytype.setBackgroundResource(R.drawable.da_xinzijiang);
		}

		return convertView;
	}

	static class ViewHolder {
		private TextView userpaytime;
		private TextView news;
		private TextView pay;
		private ImageView userpaytype;

	}
}
