package com.bs.bsims.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.EmployeeVO;

public class MyGridViewTxtAdapter extends BaseAdapter {
	private Context context;

	private List<EmployeeVO> datas = new ArrayList<EmployeeVO>();

	public MyGridViewTxtAdapter(Context context, List<EmployeeVO> datas) {
		super();
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		EmployeeVO item = datas.get(position);
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();

			convertView = LayoutInflater.from(context).inflate(
					R.layout.common_item_onlyname, null);
			holder.txt_name = (TextView) convertView
					.findViewById(R.id.txt_common_onlyName);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		/*
		 * TextView txt = new TextView(context); LinearLayout.LayoutParams
		 * params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
		 * LayoutParams.WRAP_CONTENT);
		 * txt.setTextColor(color.comm_txt_listview_black);
		 * txt.setTextSize(16f); txt.setLayoutParams(params);
		 * 
		 * txt.setText(datas.get(position).getFullname());
		 */

		holder.txt_name.setText(item.getFullname());
		return convertView;
	}

	class ViewHolder {
		TextView txt_name;
	}

}
