package com.bs.bsims.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.TaskEventItem;
import com.bs.bsims.utils.CommonDateUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 任务事件列表适配器
 * 
 * @author Administrator
 * 
 */
public class TaskEventListAdapter extends BaseAdapter {

	private Context context;

	private List<TaskEventItem> datasList = new ArrayList<TaskEventItem>();

	private ImageLoader imageLoader = ImageLoader.getInstance();

	private DisplayImageOptions options;

	public TaskEventListAdapter(Context context, List<TaskEventItem> datas) {
		this.context = context;
		this.datasList = datas;

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_default_portrait_s)
				// 默认图片
				.showImageForEmptyUri(R.drawable.ic_default_portrait_s)
				// 空url的图片
				.showImageOnFail(R.drawable.ic_default_portrait_s)
				.cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		return datasList.size();
	}

	@Override
	public Object getItem(int position) {
		return datasList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView_old(int position, View convertView, ViewGroup parent) {

		TaskEventItem data = datasList.get(position);

		View view;
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.item_taskevent, null);

			holder.img_portrait = (ImageView) view
					.findViewById(R.id.img_taskList_portrait);
			holder.txt_title = (TextView) view
					.findViewById(R.id.txt_taskevent_list_title);
			holder.txt_time = (TextView) view
					.findViewById(R.id.txt_taskevent_list_time);
			holder.txt_fujian = (TextView) view
					.findViewById(R.id.txt_taskevent_list_fujian);
			holder.txt_percent = (TextView) view
					.findViewById(R.id.txt_taskevent_list_percent);
			holder.txt_percent_noFujian = (TextView) view
					.findViewById(R.id.txt_taskevent_list_percent_noFujian);
			holder.img_isComplete = (ImageView) view
					.findViewById(R.id.img_taskevent_list_status);

			holder.txt_isread = (TextView) view
					.findViewById(R.id.txt_taskevent_list_isread);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		holder.txt_title.setText(data.getTitle());
		// 完成状态
		/** status 1负责人初审确认2 发布人定审确认任务完成状态 */
		long currentTimeMillis = System.currentTimeMillis();
		long end = data.getEndtime() * 1000;
		if ("100".equals(data.getSchedule()) && "2".equals(data.getStatus())) {
			holder.img_isComplete
					.setImageResource(R.drawable.ic_taskevent_complete);
		} else {
			if (currentTimeMillis >= end) {
				holder.img_isComplete.setImageResource(R.drawable.task_yq1);
			} else {
				holder.img_isComplete
						.setImageResource(R.drawable.ic_taskevent_underway);
			}
		}

		// 开始与结束时间
		holder.txt_time.setText(CommonDateUtils.parseDate(
				data.getStarttime() * 1000, "MM.dd")
				+ "-"
				+ CommonDateUtils.parseDate(data.getEndtime() * 1000, "MM.dd"));

		// 附件的隐藏与显示
		holder.txt_fujian.setVisibility(View.VISIBLE);
		holder.txt_percent.setVisibility(View.VISIBLE);
		holder.txt_percent_noFujian.setVisibility(View.VISIBLE);

		if ("0".equals(data.getAnnex_size())
				|| "暂无".equals(data.getAnnex_size())) {

			holder.txt_fujian.setVisibility(View.GONE);
			holder.txt_percent.setVisibility(View.GONE);
			holder.txt_percent_noFujian.setVisibility(View.VISIBLE);
			holder.txt_percent_noFujian.setText(data.getSchedule() + "%");

		} else {
			holder.txt_fujian.setVisibility(View.VISIBLE);
			holder.txt_percent.setVisibility(View.VISIBLE);
			holder.txt_percent_noFujian.setVisibility(View.GONE);
			holder.txt_percent.setText(data.getSchedule() + "%");
		}
		
		return view;
	}

	class ViewHolder {
		ImageView img_portrait;// 头像

		TextView txt_title;// 标题

		TextView txt_time;// 时间

		TextView txt_percent;// 进度

		/** 用于隐藏(下载完成时) */
		TextView txt_fujian;

		TextView txt_percent_noFujian; // 没有附件时

		ImageView img_isComplete;// 进行或者已结束

		TextView txt_isread;// 是否已读

	}

	class ViewHolderNew {
		private TextView mName, mContent, mTime, mType;
		private ImageView mState, mFujian;
		private BSCircleImageView mHead;
		private View mItem;
		private ProgressBar mProgressBar;
	}

	public void setDatanotifyDataSetChanged(List<TaskEventItem> datas) {
		// TODO Auto-generated method stub
		this.datasList = datas;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderNew holderNew;
		if (convertView == null) {
			holderNew = new ViewHolderNew();
			convertView = View.inflate(context,
					R.layout.item_taskeventlistadapter_ly, null);
			holderNew.mName = (TextView) convertView
					.findViewById(R.id.item_taskeventlistadapter_name_tv);
			// holderNew.mType = (TextView)
			// convertView.findViewById(R.id.type_tv);
			holderNew.mTime = (TextView) convertView
					.findViewById(R.id.item_taskeventlistadapter_time_tv);
			holderNew.mContent = (TextView) convertView
					.findViewById(R.id.item_taskeventlistadapter_content_tv);
			holderNew.mState = (ImageView) convertView
					.findViewById(R.id.item_taskeventlistadapter_state_img);
			holderNew.mFujian = (ImageView) convertView
					.findViewById(R.id.img_item_taskeventlistadapter_havefile);
			// holderNew.mHead = (BSCircleImageView)
			// convertView.findViewById(R.id.head_icon);
			// holderNew.mItem = convertView.findViewById(R.id.item_layout);
			holderNew.mProgressBar = (ProgressBar) convertView
					.findViewById(R.id.item_taskeventlistadapter_seekbar);
			convertView.setTag(holderNew);
		} else {
			holderNew = (ViewHolderNew) convertView.getTag();
		}
		TaskEventItem data = datasList.get(position);

		holderNew.mName.setText(data.getTitle());
		// 开始与结束时间
		holderNew.mTime.setText(CommonDateUtils.parseDate(
				data.getStarttime() * 1000, "MM.dd")
				+ "-"
				+ CommonDateUtils.parseDate(data.getEndtime() * 1000, "MM.dd"));
		// 进度
		int percentValue = (int) (Double.parseDouble(data.getSchedule()) * 100);
		holderNew.mContent.setText(percentValue + "%");
		holderNew.mProgressBar.setProgress(percentValue);
		/**
		 * 当前状态0未审1已完成2完结3定审完成4终止5过期
		 */
		int statusInt = Integer.parseInt(data.getStatus());
		int drawableId = R.drawable.creative_01;
		switch (statusInt) {
		case 0:
			drawableId = R.drawable.nsm_task_state_0;
			break;
		case 1:
			drawableId = R.drawable.nsm_task_state_1;
			break;
		case 2:
			drawableId = R.drawable.nsm_task_state_2;
			break;
		case 3:
			drawableId = R.drawable.nsm_task_state_3;
			break;
		case 5:
			drawableId = R.drawable.nsm_task_state_5;
			break;
		}
		holderNew.mState.setImageResource(drawableId);

//		if (TextUtils.isEmpty(data.getAnnex_size())
//				|| "0".equals(data.getAnnex_size())
//				|| "暂无".equals(data.getAnnex_size())) {
//			holderNew.mFujian.setVisibility(View.GONE);
//		} else {
//			holderNew.mFujian.setVisibility(View.VISIBLE);
//		}
		holderNew.mFujian.setVisibility(View.GONE);
		
		if ("1".equals(data.getIsnoread())) {
        	Drawable img_off = context.getResources()
					.getDrawable(R.drawable.num_bg_ico);
//        	 Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//             // 绘制右上角的圆圈
//             paint.setColor(Color.RED);
			// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
			img_off.setBounds(0, 0,
					img_off.getMinimumWidth(),
					img_off.getMinimumHeight());
			holderNew.mName.setCompoundDrawables(img_off,null, null,
					 null); // 设置左图标
        }

		return convertView;
	}
	
	public List<TaskEventItem> getDatasList() {
		return datasList;
	}

	public void setDatasList(List<TaskEventItem> datasList) {
		this.datasList = datasList;
	}
}
