package com.bs.bsims.adapter;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.TwoTreeAdapterBk.ItemOnClickCallback;

/** 
 * @author peck
 * @Description:  
 * @date 2015-5-30 下午5:43:35 
 * @email  971371860@qq.com
 * @version V1.0 
 */

public class TwoTree4WorkAttendanceDetailAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private List<String> groupArray;// 组列表
	private List<List<String>> childArray;// 子列表
	ItemOnClickCallback mCallback;
	
	public TwoTree4WorkAttendanceDetailAdapter(Context context, List<String> groupArray, List<List<String>> childArray)
	{
		this.mContext = context;
		this.groupArray = groupArray;
		this.childArray = childArray;
	}
	
	/*-----------------Child */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childArray.get(groupPosition).get(childPosition);
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		ChildHolder holder = null;
		if (convertView == null) {
			holder = new ChildHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.tow_tree_child_itme, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.child_name);
			holder.childLayout = (LinearLayout) convertView.findViewById(R.id.child_layout);
			
			convertView.setTag(holder);
		} else {
			holder = (ChildHolder) convertView.getTag();
		}
		// HashMap<String, Object> general = getChild(groupPosition, childPosition);
		// holder.tvName.setText(general.get(KEY_NAME).toString());
		// holder.ivPhoto.setImageResource((Integer) general.get(KEY_PHOTO));
		holder.tvName.setText(childArray.get(groupPosition).get(childPosition));
		if ("".equals(childArray.get(groupPosition).get(childPosition))) {
			holder.tvName.setVisibility(View.GONE);
			holder.childLayout.setVisibility(View.GONE);
		}
		holder.tvName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				match(3, (groupPosition + 1) + "," + (childPosition + 1));
//				mPop.dismiss();
//				mBsRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
//				new ThreadUtil(ApprovalViewActivity.this, ApprovalViewActivity.this).start();
			}
		});
		return convertView;
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return childArray.get(groupPosition).size();
	}
	
	@Override
	public Object getGroup(int groupPosition) {
		return getGroup(groupPosition);
	}
	
	@Override
	public int getGroupCount() {
		return groupArray.size();
	}
	
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	
	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		ParentHolder holder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.tow_tree_parent_item, null);
			holder = new ParentHolder();
			holder.parentName = (TextView) convertView.findViewById(R.id.parent_name);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		} else {
			holder = (ParentHolder) convertView.getTag();
		}
		holder.parentName.setText(groupArray.get(groupPosition));
		if (isExpanded) {
			holder.icon.setImageResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
		} else {
			holder.icon.setImageResource(R.drawable.ic_contacts_department_fragment_arrow_default);
		}
		
		holder.parentName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				match(2, (groupPosition + 1) + "");
//				mPop.dismiss();
//				mBsRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
//				new ThreadUtil(ApprovalViewActivity.this, ApprovalViewActivity.this).start();
			}
		});
		return convertView;
	}
	
	@Override
	public boolean hasStableIds() {
		return false;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}
	
	private TextView getGenericView(String string)
	{
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		
		TextView textView = new TextView(mContext);
		textView.setLayoutParams(layoutParams);
		
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		
		textView.setPadding(40, 0, 0, 0);
		textView.setText(string);
		return textView;
	}
	
	class ParentHolder {
		TextView parentName;
		ImageView icon;
	}
	
	class ChildHolder {
		TextView tvName;
		LinearLayout childLayout;
	}
	
}