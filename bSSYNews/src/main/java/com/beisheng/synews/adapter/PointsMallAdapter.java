package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.synews.activity.PointsMallDetailActivity;
import com.beisheng.synews.mode.PointsMallVO;
import com.im.zhsy.R;

public class PointsMallAdapter extends BSBaseAdapter<PointsMallVO> {
	private boolean isAll = false;

	public PointsMallAdapter(Context context) {
		super(context);
	}

	@Override
	public int getCount() {
		if (isAll) {
			return 4;
		} else {
			return super.getCount();

		}

	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (mIsEmpty) {
			return super.getView(position, convertView, parent);
		}

		if (convertView != null && convertView.getTag() == null)
			convertView = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.points_mall_adapter, null);
			holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
			holder.descTv = (TextView) convertView.findViewById(R.id.desc_tv);
			holder.priceTv = (TextView) convertView.findViewById(R.id.price_tv);
			holder.commodityImg = (ImageView) convertView.findViewById(R.id.commodity_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final PointsMallVO vo = (PointsMallVO) mList.get(position);
		holder.nameTv.setText(vo.getTitle());
		holder.descTv.setText("原价￥" + vo.getPrice());
		holder.priceTv.setText(vo.getScore());
		mImageLoader.displayImage(vo.getImg(), holder.commodityImg, mOptions);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				BaseActivity activity = (BaseActivity) mContext;
				Bundle bundle = new Bundle();
				bundle.putString("sid", vo.getSid());
				activity.openActivity(PointsMallDetailActivity.class, bundle, 0);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		private ImageView commodityImg;
		private TextView descTv, nameTv, priceTv;
	}

	public boolean isAll() {
		return isAll;
	}

	public void setAll(boolean isAll) {
		this.isAll = isAll;
	}

}
