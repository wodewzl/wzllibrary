package com.bs.bsims.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.TaskEventDetailsView_Info_RelevantVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author peck
 * @Description: 来自于HeadAdapter 为了任务详情中的相关人展开
 * @date 2015-5-28 下午7:51:56
 * @email 971371860@qq.com
 * @version V1.0 2015/5/28 20:53 专用 任务详情页面
 */

public class HeadAdapter4TaskDetails extends BaseAdapter {
	private List<TaskEventDetailsView_Info_RelevantVO> mList;
	private int mState;
	private Context mContext;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private boolean mAdd;
	private boolean approval;
	private String status;
	private String isone;
	private boolean isShowAdd;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isApproval() {
		return approval;
	}

	public void setApproval(boolean approval) {
		this.approval = approval;
	}

	// CcAdapter state 参数为图片边缘的提醒的小图样式
	public HeadAdapter4TaskDetails(Context context, int state) {
		// TODO Auto-generated constructor stub
		mList = new ArrayList<TaskEventDetailsView_Info_RelevantVO>();
		mState = state;
		mContext = context;
		mImageLoader = ImageLoader.getInstance();
	}

	public HeadAdapter4TaskDetails(Context context, boolean add) {
		mContext = context;
		mAdd = add;
		mList = new ArrayList<TaskEventDetailsView_Info_RelevantVO>();
		mImageLoader = ImageLoader.getInstance();
		mOptions = CommonUtils.initImageLoaderOptions();
	}

	public HeadAdapter4TaskDetails(Context context, boolean add, String isone) {
		mContext = context;
		mAdd = add;
		mList = new ArrayList<TaskEventDetailsView_Info_RelevantVO>();
		mImageLoader = ImageLoader.getInstance();
		mOptions = CommonUtils.initImageLoaderOptions();
		this.isone = isone;
	}

	@Override
	public int getCount() {
		if (mAdd) {
			return (mList.size() + 1);
		} else {
			return mList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.gv_head_icon_item,
					null);

			holder.personHead = (BSCircleImageView) convertView
					.findViewById(R.id.head_icon);
			holder.personName = (TextView) convertView
					.findViewById(R.id.person_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (parent.getChildCount() == position) {
			if (mAdd) {
				if (mList.size() == position) {
					if ("1".equals(isone)) {
						if (mList.size() == 1) {
							holder.personHead.setVisibility(View.GONE);
							holder.personName.setVisibility(View.GONE);
						} else {
							holder.personHead
									.setImageResource(R.drawable.add_persion);
							holder.personHead.setBackground(null);
							holder.personName.setVisibility(View.GONE);
						}
					} else {
						holder.personHead.setBackground(null);
						holder.personHead
								.setImageResource(R.drawable.add_persion);
						if (isShowAdd) {
							holder.personHead.setVisibility(View.GONE);
						}

						holder.personName.setVisibility(View.GONE);
					}

				} else {
					TaskEventDetailsView_Info_RelevantVO infoVO = mList.get(position);
					mImageLoader.displayImage(infoVO.getHeadpic(),
							holder.personHead,
							CommonUtils.initImageLoaderOptions());
					holder.personHead.setUserId(infoVO.getUserid());//HL
					holder.personHead.setUserName(infoVO.getFullname());
					holder.personHead.setUrl(infoVO.getHeadpic());
					holder.personName.setText(infoVO.getFullname());
					holder.personName.setVisibility(View.VISIBLE);
				}

			} else {
				TaskEventDetailsView_Info_RelevantVO infoVO = mList.get(position);
				mImageLoader.displayImage(infoVO.getHeadpic(),
						holder.personHead, mOptions);
				holder.personHead.setUserId(infoVO.getUserid());//HL
		        holder.personHead.setUserName(infoVO.getFullname());
                holder.personHead.setUrl(infoVO.getHeadpic());
				holder.personName.setText(infoVO.getFullname());
				if (approval) {

					if ("1".equals(infoVO.getStatus())) {
						holder.personHead.setColor(1);
						holder.personHead.setSemicircleNumber("批");
					} else if ("2".equals(status)) {
						holder.personHead.setColor(2);
						holder.personHead.setSemicircleNumber("否");
					} else {
						holder.personHead.setColor(3);
						holder.personHead.setSemicircleNumber((position + 1)
								+ "");
					}

				}

			}
		} else {
		}

		return convertView;
	}

	static class ViewHolder {
		private BSCircleImageView personHead;
		private TextView personName;
	}

	public void updateData(List<TaskEventDetailsView_Info_RelevantVO> list) {
		mList.clear();
		mList.addAll(list);
		this.notifyDataSetChanged();
	}

	/**
	 * 专用 任务详情页面
	 * 
	 * @param list
	 * @param isShowAdd
	 *            展开后，对添加进行隐藏，不再进行添加
	 */
	public void updateData(List<TaskEventDetailsView_Info_RelevantVO> list,
			boolean isShowAdd) {
		this.isShowAdd = isShowAdd;
		mList.clear();
		mList.addAll(list);
		this.notifyDataSetChanged();
	}

}
