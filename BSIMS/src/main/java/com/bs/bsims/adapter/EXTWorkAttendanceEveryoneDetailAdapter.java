package com.bs.bsims.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.huanxin.SmileUtils;
import com.bs.bsims.utils.DateUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author peck
 * @Description:
 * @date 2015-5-31 下午12:09:06
 * @email 971371860@qq.com
 * @version V1.0
 */

public class EXTWorkAttendanceEveryoneDetailAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> mEXTWAEveryoneItemList;
	private ImageLoader mImageLoader;
	private Map<String, String> valueMap;

	public EXTWorkAttendanceEveryoneDetailAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		mEXTWAEveryoneItemList = new ArrayList<String>();
	}

	@Override
	public int getCount() {
		return mEXTWAEveryoneItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return mEXTWAEveryoneItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext,
					R.layout.item_work_attendance_everyone_detail, null);
			holder.mDetail = (TextView) convertView
					.findViewById(R.id.item_work_attendance_everyone_detail_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String info = mEXTWAEveryoneItemList.get(position);

		String date = DateUtils.runMatcher(info);

		Spannable span = SmileUtils.getSmiledText(mContext, info);
		span.setSpan(new ForegroundColorSpan(Color.parseColor("#00A9FE")), 0,
				date.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		 holder.mDetail.setText(info);
//		holder.mDetail.setText(span, BufferType.SPANNABLE);
		return convertView;
	}

	static class ViewHolder {
		private TextView mDetail;
	}

	public List<String> getmEXTWAEveryoneItemList() {
		return mEXTWAEveryoneItemList;
	}

	public void setmEXTWAEveryoneItemList(List<String> mEXTWAEveryoneItemList) {
		this.mEXTWAEveryoneItemList = mEXTWAEveryoneItemList;
		this.notifyDataSetChanged();
	}

}
